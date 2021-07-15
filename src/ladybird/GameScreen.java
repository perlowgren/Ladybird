package net.spirangle.ladybird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import net.spirangle.minerva.Point;
import net.spirangle.minerva.Rectangle;
import net.spirangle.minerva.Dimension;
import net.spirangle.minerva.gdx.BasicScreen;
import net.spirangle.minerva.gdx.sprite.Clip;
import net.spirangle.ladybird.Main.HiScore;

public class GameScreen extends BasicScreen implements Data {
	private class TextInput implements TextInputListener {
		public String input = null;
		public boolean cancel = false;
		TextInput() {}
		TextInput(String text) { input = text; }
		@Override
		public void input(String text) {
			if(text.length()>16) input = text.substring(0,16);
			else input = text;
		}
		@Override
		public void canceled() { cancel = true; }
	}

	private class Message extends Rectangle {
		public BitmapFont font     = null;
		public String text         = null;
		public GlyphLayout layout  = new GlyphLayout();
		public int margin          = 0;
		public int timer           = 0;
	}

	private BitmapFont h1Font;
	private BitmapFont h2Font;
	private BitmapFont pFont;

	private Main main;
	private Point[] touch;
	private Rectangle levelBorder;
	private Rectangle levelView;

	private Message[] message;
	private int messageTimer;
	private int messageAction;

	private int splash;
	private int splashTimer;

	private String name;
	private int round;
	private int time;
	private int score;
	private int rank;
	private HiScore hiscore;

	private TextInput textListener;

	private Clip tileClip;

	private Clip heartClip;
	private Clip appleClip;
	private Clip chestClip;
	private Clip chestDimClip;
	private Clip[] bottleClip;

	private Clip leftButtonClip;
	private Clip rightButtonClip;
	private Clip jumpButtonClip;
	private Clip fireButtonClip;

	private Rectangle leftButton;
	private Rectangle rightButton;
	private Rectangle jumpButton;
	private Rectangle fireButton;

	public static String timeFormat(int frames) {
		int n = (int)Math.ceil((float)frames/FPS);
		int s = n%60;
		int m = (n/60)%60;
		int h = n/3600;
		return (h>0? h+":" : "")+(m>0? (m>=10? m : "0"+m) : "00")+":"+(s>=10? s : "0"+s);
	}

	public GameScreen(Main m) {
		super(m);
		main = m;

Main.log("GameScreen()");

		h1Font = game.getFont(FONT_RISQUE18);
		h2Font = game.getFont(FONT_RISQUE14);
		pFont = game.getFont(FONT_PROFONT12W);

		touch = new Point[10];
		for(int i=0; i<10; ++i) touch[i] = new Point(-1,-1);

		int w,h;
		w = (int)Gdx.graphics.getWidth();
		h = (int)Gdx.graphics.getHeight();
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
		messageAction = 0;

		splash = 0;
		splashTimer = 0;

		textListener = null;

		tileClip = main.getClip(CLIP_VTILE);

		heartClip = main.getClip(CLIP_HEART);
		appleClip = main.getClip(CLIP_APPLE);
		chestClip = main.getClip(CLIP_CHEST);
		chestDimClip = main.getClip(CLIP_CHEST_DIM);
		bottleClip = new Clip[] {
			main.getClip(CLIP_BOTTLE_G),
			main.getClip(CLIP_BOTTLE_V),
			main.getClip(CLIP_BOTTLE_O),
			main.getClip(CLIP_BOTTLE_B),
		};

		leftButtonClip = main.getClip(CLIP_LEFT_BUTTON);
		rightButtonClip = main.getClip(CLIP_RIGHT_BUTTON);
		jumpButtonClip = main.getClip(CLIP_JUMP_BUTTON);
		fireButtonClip = main.getClip(CLIP_FIRE_BUTTON);

		leftButton = new Rectangle(0,0,20,40);
		rightButton = new Rectangle(0,0,20,40);
		jumpButton = new Rectangle(0,0,30,30);
		fireButton = new Rectangle(0,0,30,30);
Main.log("GameScreen(done)");
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		if(textListener!=null) enteringText();
		else if(splash!=0) paintSplash();
		else {
			Level level = main.getLevel();
			Player player = main.getPlayer();
			GameObject focus = player;
			int i,m,n,x,y,bg = level.getBackgroundColor();

//Main.log("GameScreen.render(x="+levelView.x+",y="+levelView.y+")");

			level.update();

			if(!focus.isDead()) {
				levelBorder.set(0,0,level.getWidth(),level.getHeight());
				levelView.set(0,0,
				              levelBorder.width<viewport.width? levelBorder.width : viewport.width,
				              levelBorder.height<viewport.height? levelBorder.height : viewport.height);

				if(levelBorder.width<viewport.width) levelBorder.x = (viewport.width-levelBorder.width)/2;
				else {
					levelView.x = focus.getX()+focus.getCenterX()-levelView.width/2;
					if(levelView.x<0) levelView.x = 0;
					else if(levelView.x+levelView.width>levelBorder.width) levelView.x = levelBorder.width-levelView.width;
				}
				if(levelBorder.height<viewport.height) levelBorder.y = (viewport.height-levelBorder.height)/2;
				else {
					levelView.y = focus.getY()-focus.getHeight()+focus.getCenterY()-levelView.height/2;
					if(levelView.y<0) levelView.y = 0;
					else if(levelView.y+levelView.height>levelBorder.height) levelView.y = levelBorder.height-levelView.height;
				}
			}

			Gdx.gl.glClearColor(((float)((bg>>16)&0xff))/255.f,((float)((bg>>8)&0xff))/255.f,((float)(bg&0xff))/255.f,1.0f);
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
				for(y=levelBorder.y+levelBorder.height-levelView.y+25; y+25>0; y-=25) {
					tileClip.draw(batch,viewport,levelBorder.x-5,y);
					tileClip.draw(batch,viewport,levelBorder.x+levelView.width,y);
				}

			level.draw(batch);

			for(i=player.getHearts(),x=levelBorder.x+2,y=2; i>0; --i,x+=11)
				heartClip.draw(batch,null,x,y);
			for(i=0,m=0,n=0,x+=4,y=2; i<4; ++i,m=0)
				for(n=player.getBuffs(i); n>0; ++m,--n,x+=11)
					bottleClip[i].draw(batch,null,x,y);
			for(i=player.getCollect(),n=player.getChests(),x=levelBorder.x+2,y=13; i>0; --i,--n,x+=11) {
				if(n>0) chestClip.draw(batch,null,x,y);
				else chestDimClip.draw(batch,null,x,y);
			}
			for(i=player.getApples(),n=0,m=levelBorder.x+levelBorder.width,x=m-12,y=2; i>0; --i,++n,x-=11) {
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

		main.handleAction();

		Main.sleepFPS(FPS,delta);
	}

	@Override
	public void resize(int w,int h) {
		super.resize(w,h);
Main.log("GameScreen.resize(w: "+w+", h: "+h+")");
		leftButton.setLocation(5,viewport.height-45);
		rightButton.setLocation(45,viewport.height-45);
		jumpButton.setLocation(viewport.width-35,viewport.height-40);
		fireButton.setLocation(viewport.width-75,viewport.height-40);
	}

	@Override
	public boolean touchDown(int x,int y,int pointer,int button) {
		if(splash!=0) splashTimer = 0;
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
		int n1 = -1,n2 = -1;
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
//Main.log("GameScreen.drag(x="+levelView.x+",y="+levelView.y+")");
		return true;
	}

	@Override
	public boolean keyDown(int keycode) {
		if(splash!=0) splashTimer = 0;
		return keyAction(keycode,true);
	}

	@Override
	public boolean keyUp(int keycode) {
		return keyAction(keycode,false);
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
			case Keys.ESCAPE:
				if(active) showSplash(SPLASH_START,20,Main.str.get("appName"));
				return true;
			case Keys.LEFT:
				Player.keys[0] = active;
				return true;
			case Keys.RIGHT:
				Player.keys[1] = active;
				return true;
			case Keys.SPACE:
				Player.keys[2] = active;
				return true;
			case Keys.ALT_LEFT:
			case Keys.ALT_RIGHT:
				Player.keys[5] = active;
				return true;
		}
		return false;
	}

	public void drawString(String s,int x,int y) {
		pFont.draw(batch,s,(float)x,(float)y);
	}

	public void drawString(BitmapFont f,String s,int x,int y) {
		f.draw(batch,s,(float)x,(float)y);
	}

	public int getX() { return levelView.x-levelBorder.x; }
	public int getY() { return levelView.y-levelBorder.y; }
	public Rectangle getView() { return new Rectangle(levelView); }
	public boolean isVisible(Rectangle r) { return levelView.intersects(r); }
	public int getWidth() { return viewport.width; }
	public int getHeight() { return viewport.height; }
	public int getResolution() { return (int)zoom; }

	public String getName() { return name; }
	public int getRound() { return round; }
	public int getScore() { return score; }
	public int getRank() { return rank; }

	public void clearMessage() {
		messageTimer = 0;
		for(int i=0; i<message.length; ++i)
			message[i].timer = 0;
	}

	public boolean showingMessage() { return messageTimer>0; }

	public void showMessage(BitmapFont f,String s,int t,int a,boolean cl) {
		if(s==null || t<=0) cl = true;
		if(cl) clearMessage();
		if(s==null || t<=0) return;
		Message m = null;
		for(int i=message.length-1; i>=0; --i)
			if(message[i].timer<=0) m = message[i];
		if(m==null) return;
		m.font = f==null? h1Font : f;
		m.text = s;
		m.layout.setText(m.font,s);
		m.width = (int)Math.ceil(m.layout.width);
		m.height = (int)Math.ceil(m.layout.height);
Main.log("Main.showMessage(lineHeight: "+m.font.getLineHeight()+", ascent: "+m.font.getAscent()+", capHeight: "+m.font.getCapHeight()+", descent: "+m.font.getDescent()+")");
		m.margin = (int)(m.font.getLineHeight()+m.font.getAscent()-m.font.getCapHeight()+m.font.getDescent());
		m.x = (viewport.width-m.width)/2;
		m.timer = t;
Main.log("Main.showMessage(text: "+s+", height: "+m.height+", margin: "+m.margin+")");
		if(a!=0) messageAction = a;
		updateMessage();
	}

	public void paintMessage() {
		if(messageTimer>0) {
			Message m;
			boolean u = false;
			for(int i=0; i<message.length; ++i) {
				m = message[i];
				if(m.timer<=0) continue;
				m.font.draw(batch,m.layout,m.x,m.y);
				--m.timer;
				if(m.timer<=0) u = true;
			}
			if(u) updateMessage();
			--messageTimer;
			if(messageTimer<=0 && messageAction!=0) {
				main.setAction(messageAction,0);
				messageAction = 0;
			}
		}
	}

	private void updateMessage() {
		int i,y,h = 0;
		Message m0,m;
		for(i=0,m0=null,m=null; i<message.length; ++i,m0=m) {
			m = message[i];
			if(m.timer<=0) continue;
			h += m.height;
			if(m0!=null) h += m0.margin;
		}
		for(i=0,y=(viewport.height-h)/2,messageTimer=0; i<message.length; ++i) {
			m = message[i];
			if(m.timer<=0) continue;
			m.y = y;
			y += m.height+m.margin;
			if(m.timer>messageTimer) messageTimer = m.timer;
		}
	}

	public void clearSplash() {
		splash = SPLASH_NONE;
	}

	public void showSplash(int s,int t,String msg) {
Main.log("Main.showSplash(s: "+s+", t: "+t+", msg: "+msg+")");
		splash = s;
		splashTimer = t;
		showMessage(null,msg,t,0,true);
	}

	public void paintSplash() {
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
//Main.log("Main.paintSplash(x: "+x+", y: "+y+", width: "+viewport.width+" ["+w+"], height: "+viewport.height+" ["+h+"])");
		batch.draw(t,0.0f,0.0f,(float)viewport.width,(float)viewport.height,x,y,viewport.width,viewport.height,false,true);
		if(showingMessage()) paintMessage();
		batch.end();
		if(splashTimer>0) --splashTimer;
		else {
			Player player = main.getPlayer();
			switch(splash) {
				case SPLASH_START:
					splash = SPLASH_NONE;
					main.setAction(START_GAME,0);
					break;
				case SPLASH_WIN:
					if(name==null) {
						textListener = new TextInput();
						Gdx.input.getTextInput(textListener,Main.str.get("enterName"),"","");
					} else {
						splash = SPLASH_IDLE;
						hiscore = null;
						main.uploadScore(name,round,time,score);
					}
					break;
				case SPLASH_SCORE:
					showSplash(SPLASH_HISCORE,40,Main.str.format("round",round));
					showMessage(h2Font,Main.str.get("topRank"),40,0,false);
					showMessage(pFont,Main.str.format("topPlayers",
						hiscore.hiscore1,
						hiscore.hiscore2,
						hiscore.hiscore3,
						hiscore.hiscore4),40,0,false);
					break;
				case SPLASH_HISCORE:
					splash = SPLASH_NONE;
					main.setAction(NEXT_ROUND,0);
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
		Player player = main.getPlayer();
		++round;
		time = player.getFrames();
		score = player.getScore();
		game.playSound(SOUND_APPEAR);
		showSplash(SPLASH_WIN,20,Main.str.format("round",round));
		showMessage(null,Main.str.format("completed",round),20,0,false);
	}

	public void showHiScore(HiScore hs) {
		hiscore = hs;
		name = hiscore.name;
		rank = hiscore.rank;
		showSplash(SPLASH_SCORE,120,Main.str.format("round",round));
		showMessage(h2Font,Main.str.format("score",name,score,timeFormat(time),rank),120,0,false);
	}
}

