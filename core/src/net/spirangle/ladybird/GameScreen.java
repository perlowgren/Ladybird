package net.spirangle.ladybird;

import static net.spirangle.ladybird.GameScreen.SplashScreenType.*;
import static net.spirangle.ladybird.LadybirdGame.*;
import static net.spirangle.ladybird.Level.Action.NEXT_ROUND;
import static net.spirangle.ladybird.Level.Action.START_GAME;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import net.spirangle.ladybird.LadybirdGame.*;
import net.spirangle.ladybird.Level.Action;
import net.spirangle.minerva.Point;
import net.spirangle.minerva.Rectangle;
import net.spirangle.minerva.gdx.GameBase;
import net.spirangle.minerva.gdx.ScreenBase;
import net.spirangle.minerva.gdx.sprite.Clip;

public class GameScreen extends ScreenBase {

    /** Frames per second. */
    public static final float FPS              = 6.667f;
    /** Seconds per frame. */
    public static final float FRAME_RATE       = 1.0f/FPS;

    public enum SplashScreenType {
        IDLE,
        START,
        WIN,
        SCORE,
        HISCORE
    }

    public static final int GRID               = 10;
    public static final int LAYERS             = 8;

    public static final int CLIP_VTILE         = 0;
    public static final int CLIP_HEART         = 1;
    public static final int CLIP_APPLE         = 2;
    public static final int CLIP_CHEST         = 3;
    public static final int CLIP_CHEST_DIM     = 4;
    public static final int CLIP_BOTTLE        = 5;
    public static final int CLIP_BOTTLE_G      = 5;
    public static final int CLIP_BOTTLE_V      = 6;
    public static final int CLIP_BOTTLE_O      = 7;
    public static final int CLIP_BOTTLE_B      = 8;
    public static final int CLIP_LEFT_BUTTON   = 9;
    public static final int CLIP_RIGHT_BUTTON  = 10;
    public static final int CLIP_JUMP_BUTTON   = 11;
    public static final int CLIP_FIRE_BUTTON   = 12;
    public static final int CLIP_GOOGLE_ICON   = 13;

    private static class TextInput implements TextInputListener {
        public String input = null;
        public boolean cancel = false;

        TextInput() {}

        TextInput(String text) {
            input = text;
        }

        @Override
        public void input(String text) {
            if(text.length()>16) input = text.substring(0,16);
            else input = text;
        }

        @Override
        public void canceled() {
            cancel = true;
        }
    }

    private static class Message extends Rectangle {
        public BitmapFont font = null;
        public String text = null;
        public GlyphLayout layout = new GlyphLayout();
        public int margin = 0;
        public int timer = 0;
    }

    private final BitmapFont h1Font;
    private final BitmapFont h2Font;
    private final BitmapFont pFont;

    private final Point[] touch;
    private final Rectangle levelBorder;
    private final Rectangle levelView;

    private final Message[] message;
    private int messageTimer;
    private Action messageAction;

    private SplashScreenType splash;
    private int splashTimer;

    private String name;
    private int round;
    private int time;
    private int score;
    private int rank;
    private HiScore hiscore;

    private TextInput textListener;

    private final Clip tileClip;

    private final Clip heartClip;
    private final Clip appleClip;
    private final Clip chestClip;
    private final Clip chestDimClip;
    private final Clip[] bottleClip;

    private final Clip leftButtonClip;
    private final Clip rightButtonClip;
    private final Clip jumpButtonClip;
    private final Clip fireButtonClip;

    private final Rectangle leftButton;
    private final Rectangle rightButton;
    private final Rectangle jumpButton;
    private final Rectangle fireButton;

    public static String timeFormat(int frames) {
        int n = (int)Math.ceil((float)frames/FPS);
        int s = n%60;
        int m = (n/60)%60;
        int h = n/3600;
        return (h>0? h+":" : "")+(m>0? (m>=10? m : "0"+m) : "00")+":"+(s>=10? s : "0"+s);
    }

    public GameScreen() {
        super();
        LadybirdGame game = LadybirdGame.getInstance();

        h1Font = game.getFont(FONT_RISQUE18);
        h2Font = game.getFont(FONT_RISQUE14);
        pFont = game.getFont(FONT_PROFONT12W);

        touch = new Point[10];
        for(int i = 0; i<10; ++i) touch[i] = new Point(-1,-1);

        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        zoom = (float)Math.floor((float)w/240.0f);

        levelBorder = new Rectangle(0,0,display.width,display.height);
        levelView = new Rectangle(0,0,display.width,display.height);

        message = new Message[] {
            new Message(),
            new Message(),
            new Message(),
            new Message(),
        };
        messageTimer = 0;
        messageAction = null;

        splash = null;
        splashTimer = 0;

        textListener = null;

        tileClip = game.getClip(CLIP_VTILE);

        heartClip = game.getClip(CLIP_HEART);
        appleClip = game.getClip(CLIP_APPLE);
        chestClip = game.getClip(CLIP_CHEST);
        chestDimClip = game.getClip(CLIP_CHEST_DIM);
        bottleClip = new Clip[] {
            game.getClip(CLIP_BOTTLE_G),
            game.getClip(CLIP_BOTTLE_V),
            game.getClip(CLIP_BOTTLE_O),
            game.getClip(CLIP_BOTTLE_B),
        };

        leftButtonClip = game.getClip(CLIP_LEFT_BUTTON);
        rightButtonClip = game.getClip(CLIP_RIGHT_BUTTON);
        jumpButtonClip = game.getClip(CLIP_JUMP_BUTTON);
        fireButtonClip = game.getClip(CLIP_FIRE_BUTTON);

        leftButton = new Rectangle(0,0,20,40);
        rightButton = new Rectangle(0,0,20,40);
        jumpButton = new Rectangle(0,0,30,30);
        fireButton = new Rectangle(0,0,30,30);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        LadybirdGame game = LadybirdGame.getInstance();
        if(textListener!=null) enteringText();
        else if(splash!=null) paintSplash();
        else if(game.getLevel()!=null) {
            Level level = game.getLevel();
            Player player = game.getPlayer();
            int i, m, n, x, y;
            Color bg = level.getBackgroundColor();

            level.update();

            if(!player.isDead()) {
                levelBorder.set(0,0,level.getWidth(),level.getHeight());
                levelView.set(0,0,Math.min(levelBorder.width,viewport.width),Math.min(levelBorder.height,viewport.height));

                if(levelBorder.width<viewport.width)
                    levelBorder.x = (viewport.width-levelBorder.width)/2;
                else {
                    levelView.x = player.getX()+player.getCenterX()-levelView.width/2;
                    if(levelView.x<0) levelView.x = 0;
                    else if(levelView.x+levelView.width>levelBorder.width)
                        levelView.x = levelBorder.width-levelView.width;
                }
                if(levelBorder.height<viewport.height)
                    levelBorder.y = (viewport.height-levelBorder.height)/2;
                else {
                    levelView.y = player.getY()-player.getHeight()+player.getCenterY()-levelView.height/2;
                    if(levelView.y<0) levelView.y = 0;
                    else if(levelView.y+levelView.height>levelBorder.height)
                        levelView.y = levelBorder.height-levelView.height;
                }
            }

            Gdx.gl.glClearColor(bg.r,bg.g,bg.b,1.0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            if(levelBorder.x>0) {
                renderer.setProjectionMatrix(camera.combined);
                renderer.begin(ShapeType.Filled);
                renderer.setColor(0.0f,0.0f,0.0f,1.0f);
                renderer.rect(0,0,levelBorder.x,viewport.height);
                renderer.rect(levelBorder.x+levelView.width,0,viewport.width-(levelBorder.x+levelView.width),viewport.height);
                renderer.end();
            }

            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            batch.enableBlending();

            if(levelBorder.x>0)
                for(y = levelBorder.y+levelBorder.height-levelView.y+25; y+25>0; y -= 25) {
                    tileClip.draw(batch,viewport,levelBorder.x-5,y);
                    tileClip.draw(batch,viewport,levelBorder.x+levelView.width,y);
                }

            level.draw(batch);

            for(i = player.getHearts(),x = levelBorder.x+2,y = 2; i>0; --i,x += 11)
                heartClip.draw(batch,null,x,y);
            for(i = 0,m = 0,n = 0,x += 4,y = 2; i<4; ++i,m = 0)
                for(n = player.getBuffs(i); n>0; ++m,--n,x += 11)
                    bottleClip[i].draw(batch,null,x,y);
            for(i = player.getCollect(),n = player.getChests(),x = levelBorder.x+2,y = 13; i>0; --i,--n,x += 11) {
                if(n>0) chestClip.draw(batch,null,x,y);
                else chestDimClip.draw(batch,null,x,y);
            }
            for(i = player.getApples(),n = 0,m = levelBorder.x+levelBorder.width,x = m-12,y = 2; i>0; --i,++n,x -= 11) {
                appleClip.draw(batch,null,x,y);
                if(n==7) {
                    x = m-1;
                    y += 11;
                }
            }

            leftButtonClip.draw(batch,null,leftButton.x,leftButton.y);
            rightButtonClip.draw(batch,null,rightButton.x,rightButton.y);
            jumpButtonClip.draw(batch,null,jumpButton.x,jumpButton.y);
            fireButtonClip.draw(batch,null,fireButton.x,fireButton.y);

            paintMessage();

            batch.end();

            batch.setProjectionMatrix(uiCamera.combined);
            batch.begin();
            batch.enableBlending();
            drawWidgets();
            drawString("Frame: "+frame+
                       "   Density: "+density+
                       "   Viewport: "+viewport.width+","+viewport.height+
                       "   Touch: "+touch[0].x+","+touch[0].y+
                       "   Zoom: "+zoom,5,display.height-14);
            batch.end();
        }

        game.handleAction();

        LadybirdGame.sleepFPS(FPS,delta);
    }

    @Override
    public void resize(int w,int h) {
        super.resize(w,h);
        leftButton.setLocation(5,viewport.height-45);
        rightButton.setLocation(45,viewport.height-45);
        jumpButton.setLocation(viewport.width-35,viewport.height-40);
        fireButton.setLocation(viewport.width-75,viewport.height-40);
    }

    @Override
    public boolean touchDown(int x,int y,int pointer,int button) {
        if(splash!=null) splashTimer = 0;
        touch[pointer].x = x;
        touch[pointer].y = y;
        int n = getTouchAction(x,y);
        if(n!=-1) Player.keys[n] = true;
        return true; // Return true to say we handled the touch.
    }

    @Override
    public boolean touchUp(int x,int y,int pointer,int button) {
        touch[pointer].x = -1;
        touch[pointer].y = -1;
        int n = getTouchAction(x,y);
        if(n!=-1) Player.keys[n] = false;
        return true;
    }

    @Override
    public boolean touchDragged(int x,int y,int pointer) {
        int n1 = -1, n2 = -1;
        if(touch[pointer].x>=0 && touch[pointer].y>=0) {
            n1 = getTouchAction(touch[pointer].x,touch[pointer].y);
        }
        touch[pointer].x = x;
        touch[pointer].y = y;
        n2 = getTouchAction(x,y);
        if(n1!=n2) {
            if(n1!=-1) Player.keys[n1] = false;
            if(n2!=-1) Player.keys[n2] = true;
        }
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(splash!=null) splashTimer = 0;
        return keyAction(keycode,true);
    }

    @Override
    public boolean keyUp(int keycode) {
        return keyAction(keycode,false);
    }

    @Override
    public boolean keyTyped(char character) {
        return true;
    }

    public int getTouchAction(int x,int y) {
        x /= (int)zoom;
        y /= (int)zoom;
        if(leftButton.contains(x,y)) return 0; // Left
        else if(rightButton.contains(x,y)) return 1; // Right
        else if(jumpButton.contains(x,y)) return 2; // Jump
        else if(fireButton.contains(x,y)) return 5; // Fire
        return -1;
    }

    public boolean keyAction(int keycode,boolean active) {
        switch(keycode) {
            default:
                return false;
            case Keys.ESCAPE:
                if(active) showSplash(START,20,GameBase.str.get("appName"));
                break;
            case Keys.SPACE:
                if(active) LadybirdGame.getInstance().nextLevel();
                break;
            case Keys.UP:
                Player.keys[0] = active;
                break;
            case Keys.DOWN:
                Player.keys[1] = active;
                break;
            case Keys.RIGHT:
                Player.keys[2] = active;
                break;
            case Keys.LEFT:
                Player.keys[5] = active;
                break;
        }
        return true;
    }

    public void drawString(String s,int x,int y) {
        pFont.draw(batch,s,(float)x,(float)y);
    }

    public void drawString(BitmapFont f,String s,int x,int y) {
        f.draw(batch,s,(float)x,(float)y);
    }

    public int getX() {
        return levelView.x-levelBorder.x;
    }

    public int getY() {
        return levelView.y-levelBorder.y;
    }

    public Rectangle getView() {
        return new Rectangle(levelView);
    }

    public boolean isVisible(Rectangle r) {
        return levelView.intersects(r);
    }

    public int getWidth() {
        return viewport.width;
    }

    public int getHeight() {
        return viewport.height;
    }

    public int getResolution() {
        return (int)zoom;
    }

    public String getName() {
        return name;
    }

    public int getRound() {
        return round;
    }

    public int getScore() {
        return score;
    }

    public int getRank() {
        return rank;
    }

    public void clearMessage() {
        messageTimer = 0;
        for(Message m : message)
            m.timer = 0;
    }

    public boolean showingMessage() {
        return messageTimer>0;
    }

    public void showMessage(int font,String text,int timer,Action action,boolean clear) {
        BitmapFont f = LadybirdGame.getInstance().getFont(font);
        showMessage(f,text,timer,action,clear);
    }

    public void showMessage(BitmapFont font,String text,int timer,Action action,boolean clear) {
        if(text==null || timer<=0) clear = true;
        if(clear) clearMessage();
        if(text==null || timer<=0) return;
        Message m = null;
        for(int i = message.length-1; i>=0; --i)
            if(message[i].timer<=0) m = message[i];
        if(m==null) return;
        m.font = font==null? h1Font : font;
        m.text = text;
        m.layout.setText(m.font,text);
        m.width = (int)Math.ceil(m.layout.width);
        m.height = (int)Math.ceil(m.layout.height);
        m.margin = (int)(m.font.getLineHeight()+m.font.getAscent()-m.font.getCapHeight()+m.font.getDescent());
        m.x = (viewport.width-m.width)/2;
        m.timer = timer;
        if(action!=null) messageAction = action;
        updateMessage();
    }

    public void paintMessage() {
        if(messageTimer>0) {
            boolean u = false;
            for(Message m : message) {
                if(m.timer<=0) continue;
                m.font.draw(batch,m.layout,m.x,m.y);
                --m.timer;
                if(m.timer<=0) u = true;
            }
            if(u) updateMessage();
            --messageTimer;
            if(messageTimer<=0 && messageAction!=null) {
                LadybirdGame.getInstance().setAction(messageAction,0);
                messageAction = null;
            }
        }
    }

    private void updateMessage() {
        int i, y, h = 0;
        Message m0, m;
        for(i = 0,m0 = null,m = null; i<message.length; ++i,m0 = m) {
            m = message[i];
            if(m.timer<=0) continue;
            h += m.height;
            if(m0!=null) h += m0.margin;
        }
        for(i = 0,y = (viewport.height-h)/2,messageTimer = 0; i<message.length; ++i) {
            m = message[i];
            if(m.timer<=0) continue;
            m.y = y;
            y += m.height+m.margin;
            if(m.timer>messageTimer) messageTimer = m.timer;
        }
    }

    public void clearSplash() {
        splash = null;
    }

    public void showSplash(SplashScreenType splash,int splashTimer,String msg) {
        showSplash(splash,splashTimer,msg,null);
    }

    public void showSplash(SplashScreenType splash,int splashTimer,String msg,Action action) {
        this.splash = splash;
        this.splashTimer = splashTimer;
        showMessage(null,msg,splashTimer,action,true);
    }

    public void paintSplash() {
        LadybirdGame game = LadybirdGame.getInstance();
//		int bg = 0xc8c8ff;
//		Gdx.gl.glClearColor(((float)((bg>>16)&0xff))/255.f,((float)((bg>>8)&0xff))/255.f,((float)(bg&0xff))/255.f,1.0f);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Texture t = game.getTexture(TEXTURE_SPLASH);
        int w = t.getWidth();
        int h = t.getHeight();
        int x = (w-viewport.width)/2;
        int y = (h-viewport.height)/2;
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.enableBlending();
        batch.draw(t,0.0f,0.0f,(float)viewport.width,(float)viewport.height,x,y,viewport.width,viewport.height,false,true);
        if(showingMessage()) paintMessage();
        batch.end();
        if(splashTimer>0) --splashTimer;
        else {
            switch(splash) {
                case START:
                    splash = null;
                    game.setAction(START_GAME,0);
                    break;
                case WIN:
                    if(name==null) {
                        textListener = new TextInput();
                        Gdx.input.getTextInput(textListener,GameBase.str.get("enterName"),"","");
                    } else {
                        splash = IDLE;
                        hiscore = null;
                        game.requestHiScore(name,round,time,score);
                    }
                    break;
                case SCORE:
                    showHiScore();
                    break;
                case HISCORE:
                    splash = null;
                    game.setAction(NEXT_ROUND,0);
                    break;
            }
        }
    }

    public void enteringText() {
        if(textListener.input!=null || textListener.cancel) {
            name = textListener.cancel? "---" : textListener.input;
            textListener = null;
        }
    }

    public void start() {
        name = null;
        round = 0;
        time = 0;
        score = 0;
        rank = 0;
    }

    public void endRound() {
        LadybirdGame game = LadybirdGame.getInstance();
        Player player = game.getPlayer();
        ++round;
        time = player.getFrames();
        score = player.getScore();
        game.playSound(SOUND_APPEAR);
        showSplash(WIN,20,GameBase.str.format("round",round));
        showMessage(null,GameBase.str.format("completed",round),20,null,false);
    }

    public void setHiScore(HiScore hiscore) {
        this.hiscore = hiscore;
        this.name = hiscore.name;
        this.rank = hiscore.rank;
    }

    public void showScore() {
        showSplash(SCORE,60,GameBase.str.format("round",round));
        showMessage(h2Font,GameBase.str.format("score",name,score,timeFormat(time),rank),60,null,false);
    }

    public void showHiScore() {
        if(hiscore!=null) {
            showSplash(HISCORE,60,GameBase.str.format("round",hiscore.round));
            showMessage(h2Font,GameBase.str.get("topRank"),60,null,false);
            showMessage(pFont,GameBase.str.format("topPlayers",
                                                  hiscore.hiscore1,
                                                  hiscore.hiscore2,
                                                  hiscore.hiscore3,
                                                  hiscore.hiscore4),60,null,false);
        } else {
            splash = null;
        }
    }
}

