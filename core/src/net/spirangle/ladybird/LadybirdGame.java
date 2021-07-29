package net.spirangle.ladybird;

import static net.spirangle.ladybird.GameScreen.SPLASH_START;
import static net.spirangle.ladybird.Level.*;
import static net.spirangle.ladybird.LevelFactory.GameObjectTemplate.TROLL;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Json;
import com.eclipsesource.json.JsonObject;

import net.spirangle.minerva.gdx.GameBase;
import net.spirangle.minerva.gdx.LoadingScreen;
import net.spirangle.minerva.gdx.sprite.Clip;
import net.spirangle.minerva.gdx.sprite.Sprite;

import java.net.URLEncoder;

public class LadybirdGame extends GameBase {

    public static final String APP_NAME        = "ladybird";
    public static final String APP_TITLE       = "Ladybird's Gift";
    public static final String HOST_URL        = "https://ladybird.spirangle.net";

    public static final int FONT_PROFONT12B    = 0;
    public static final int FONT_PROFONT12W    = 1;
    public static final int FONT_RISQUE14      = 2;
    public static final int FONT_RISQUE18      = 3;

    public static final int TEXTURE_SPLASH     = 0;
    public static final int TEXTURE_GUI        = 1;
    public static final int TEXTURE_TILES      = 2;
    public static final int TEXTURE_ITEMS      = 3;
    public static final int TEXTURE_CREATURES  = 4;
    public static final int TEXTURE_TROLL      = 5;

    public static final int SOUND_APPEAR       = 0;
    public static final int SOUND_FOOTSTEPS    = 1;
    public static final int SOUND_JUMP         = 2;
    public static final int SOUND_THROW        = 3;
    public static final int SOUND_TROLL_HIT    = 4;
    public static final int SOUND_CREATURE_HIT = 5;
    public static final int SOUND_DOOR         = 6;

    public static final int MUSIC_NONE         = -1;
    public static final int MUSIC_SUNNY_DAY    = 0;

    public static class HiScore {
        public String name;
        public int rank;
        public String hiscore1;
        public String hiscore2;
        public String hiscore3;
        public String hiscore4;
    };

    private static LadybirdGame instance = null;

    public static LadybirdGame getInstance() {
        return instance;
    }

    private GameScreen screen;
    private JsonObject levels;
    private Level level;
    private String nextLevelId;
    private Player player;
    private Clip[] clips;
    private Sprite[] sprites;
    private GameImage[] images;

    private int action = 0;
    private int actionTimer = 0;

    public LadybirdGame() {
        super();
        instance = this;
    }

    @Override
    public void create() {
        String splashTexture = "images/splash.png";
        String guiTexture = "images/gui.png";
        addAsset(TEXTURE_SPLASH,splashTexture,AssetFileType.TEXTURE);
        addAsset(TEXTURE_GUI,guiTexture,AssetFileType.TEXTURE);
        addAsset(TEXTURE_TILES,"images/tiles.png",AssetFileType.TEXTURE);
        addAsset(TEXTURE_ITEMS,"images/items.png",AssetFileType.TEXTURE);
        addAsset(TEXTURE_CREATURES,"images/creatures.png",AssetFileType.TEXTURE);
        addAsset(TEXTURE_TROLL,"images/troll.png",AssetFileType.TEXTURE);
        addAsset(FONT_PROFONT12B,"fonts/profont12b.fnt",AssetFileType.BITMAP_FONT);
        addAsset(FONT_PROFONT12W,"fonts/profont12w.fnt",AssetFileType.BITMAP_FONT);
        addAsset(FONT_RISQUE14,"fonts/risque14.fnt",AssetFileType.BITMAP_FONT);
        addAsset(FONT_RISQUE18,"fonts/risque18.fnt",AssetFileType.BITMAP_FONT);
        addAsset(SOUND_APPEAR,"audio/Appear.ogg",AssetFileType.SOUND);
        addAsset(SOUND_FOOTSTEPS,"audio/Footsteps.ogg",AssetFileType.SOUND);
        addAsset(SOUND_JUMP,"audio/Jump.ogg",AssetFileType.SOUND);
        addAsset(SOUND_THROW,"audio/Throw.ogg",AssetFileType.SOUND);
        addAsset(SOUND_TROLL_HIT,"audio/TrollHit.ogg",AssetFileType.SOUND);
        addAsset(SOUND_CREATURE_HIT,"audio/CreatureHit.ogg",AssetFileType.SOUND);
        addAsset(SOUND_DOOR,"audio/Door.ogg",AssetFileType.SOUND);
        addAsset(MUSIC_SUNNY_DAY,"SunnyDay","audio/Sunny_Day.ogg",AssetFileType.MUSIC);
        addAsset(-1,"i18n/ladybird",AssetFileType.I18BUNDLE);

        super.create();

        int[] screenData = { 4,4,13,  0,55,7,13,  7,55,92,13,  99,55,7,13,  0,68,7,13,  7,68,92,13,  99,68,7,13, };
        Screen screen = new LoadingScreen(4.0f,splashTexture,guiTexture,screenData);
        setScreen(screen);
    }

    @Override
    public void loadingAssetsCompleted() {
        super.loadingAssetsCompleted();

        images = new GameImage[]{
            new GameImage(getTexture(TEXTURE_TILES),new int[] {
                /* tm    x   y   w   h  cx  cy  sx  sy  sw  sh  fx  fy */
                100,  0, 25, 25,  5,  0,  0,  0,  0,  0,  0,  0,  0,  // 0. Horizontal Flat
                100,  0,  0,  5, 25,  0,  0,  0,  0,  0,  0,  0,  0,  // 1. Vertical Flat
                100, 25,  0, 10, 10,  0,  0,  0,  0,  0,  0,  0,  0,  // 2. Block
                100,  0, 30, 25, 25,  0,  0,  0,  0,  0,  0,  0,  0,  // 3. Large Block 1
                100, 25, 30, 25, 25,  0,  0,  0,  0,  0,  0,  0,  0,  // 3. Large Block 2
                100, 35,  0, 15, 15,  0,  0,  2,  5, 11,  5,  0,  0,  // 4. Bush
                100, 25, 15, 25, 15,  0,  0,  2,  5, 21,  5,  0,  0,  // 5. Cloud
                100,  5,  0, 20, 25,  0,  0,  0,  0, 10, 25,  0,  0,  // 6. Door
            },new int[]{ 0,1,2,3,4,5,6,7, }),

            new GameImage(getTexture(TEXTURE_ITEMS),new int[] {
                /* tm    x   y   w   h  cx  cy  sx  sy  sw  sh  fx  fy */
                100,  1,  1, 10, 10,  0,  0,  0,  0,  0,  0,  0,  0,  // 0. Heart
                100, 12,  1, 10, 10,  0,  0,  0,  0,  0,  0,  0,  0,  // 1. Apple
                100, 23,  1, 10, 10,  0,  0,  0,  0,  0,  0,  0,  0,  // 3. Chest
                100,  1, 12, 10, 10,  0,  0,  0,  0,  0,  0,  0,  0,  // 2. Bottle Green
                100, 12, 12, 10, 10,  0,  0,  0,  0,  0,  0,  0,  0,  // 2. Bottle Violet
                100, 23, 12, 10, 10,  0,  0,  0,  0,  0,  0,  0,  0,  // 2. Bottle Orange
                100, 34, 12, 10, 10,  0,  0,  0,  0,  0,  0,  0,  0,  // 2. Bottle Blue
            },new int[] { 0,1,2,3,4,5,6, }),

            new GameImage(getTexture(TEXTURE_CREATURES),new int[]{
                /* tm    x   y   w   h  cx  cy  sx  sy  sw  sh  fx  fy */
                100,  1,  1, 20, 20, 10, 20,  1,  4, 18, 12, 22,  0,  // 0. Boogie - Stand
                1, 45,  1, 20, 20, 10, 20,  1,  4, 18, 12, 22,  0,  // 1. Boogie - Walk
                1,  1,  1, 20, 20, 10, 20,  1,  4, 18, 12, 22,  0,
                100,  1, 23, 15, 20,  8, 20,  0,  0,  0,  0, 17,  0,  // 3. Grull - Stand
                1, 35, 23, 15, 20,  8, 20,  0,  0,  0,  0, 17,  0,  // 4. Grull - Walk
                1,  1, 23, 15, 20,  8, 20,  0,  0,  0,  0, 17,  0,
            },new int[]{ 0,1,3,4, }),

            new GameImage(getTexture(TEXTURE_TROLL),new int[]{
                /* tm    x   y   w   h  cx  cy  sx  sy  sw  sh  fx  fy */
                100,  1,  1, 18, 25,  9, 13,  6,  2,  6, 23, 20,  0,  // 0. Stand
                1, 41,  1, 18, 25,  9, 13,  6,  2,  6, 23, 20,  0,  // 1. Walk
                1,  1,  1, 18, 25,  9, 13,  6,  2,  6, 23, 20,  0,
                100, 41,  1, 18, 25,  9, 13,  6,  2,  6, 23, 20,  0,  // 3. Jump/Fall
            },new int[]{ 0,1,3, }),
        };

        Texture t1 = getTexture(TEXTURE_TILES);
        Texture t2 = getTexture(TEXTURE_ITEMS);
        Texture t3 = getTexture(TEXTURE_GUI);
        clips = new Clip[] {
            new Clip(t1,  0,  0, 5,25, 0, 0,false),  // CLIP_VTILE

            new Clip(t2,  1,  1,10,10, 0, 0,false),  // CLIP_HEART
            new Clip(t2, 12,  1,10,10, 0, 0,false),  // CLIP_APPLE
            new Clip(t2, 23,  1,10,10, 0, 0,false),  // CLIP_CHEST
            new Clip(t2, 34,  1,10,10, 0, 0,false),  // CLIP_CHEST_DIM
            new Clip(t2,  1, 12,10,10, 0, 0,false),  // CLIP_BOTTLE_G
            new Clip(t2, 12, 12,10,10, 0, 0,false),  // CLIP_BOTTLE_V
            new Clip(t2, 23, 12,10,10, 0, 0,false),  // CLIP_BOTTLE_O
            new Clip(t2, 34, 12,10,10, 0, 0,false),  // CLIP_BOTTLE_B

            new Clip(t3,  1,  1,20,40, 0, 0,false),  // CLIP_LEFT_BUTTON
            new Clip(t3, 22,  1,20,40, 0, 0,false),  // CLIP_RIGHT_BUTTON
            new Clip(t3, 43,  1,30,30, 0, 0,false),  // CLIP_JUMP_BUTTON
            new Clip(t3, 74,  1,30,30, 0, 0,false),  // CLIP_FIRE_BUTTON
            new Clip(t3, 43, 32,30,22, 0, 0,false),  // CLIP_GOOGLE_ICON
        };

        Screen s0 = getScreen();
        s0.dispose();

        screen = new GameScreen();
        setScreen(screen);

        loadLevels();
    }

    private void loadLevels() {
        if(false) {
            String url = HOST_URL+"/levels.json";
            HttpRequest httpGet = new HttpRequest(HttpMethods.GET);
            httpGet.setUrl(url);
            Gdx.net.sendHttpRequest(httpGet,new HttpResponseListener() {
                @Override
                public void handleHttpResponse(HttpResponse httpResponse) {
                    String response = httpResponse.getResultAsString();
                    levels = com.eclipsesource.json.Json.parse(response).asObject();
                    screen.showSplash(SPLASH_START,20,str.get("appName"));
                }

                @Override
                public void failed(Throwable t) {
                }

                @Override
                public void cancelled() {
                }
            });
        } else {
            FileHandle file = Gdx.files.internal("levels.json");
            levels = com.eclipsesource.json.Json.parse(file.readString("UTF-8")).asObject();
            screen.showSplash(SPLASH_START,20,str.get("appName"));
        }
    }

    public void startLevel() {
        level = null;
        nextLevel();
    }

    public void nextLevel() {
        if(level==null) {
            player = new Player(null,0,0,0,TROLL.index);
            loadLevel("start");
        } else {
            if(nextLevelId==null) nextLevelId = level.getNextLevelId();
            loadLevel(nextLevelId);
        }
        nextLevelId = null;
    }

    public void loadLevel(String levelId) {
        screen.clearSplash();
        stopMusic();
        Level level = LevelFactory.getInstance().createLevel(levels,levelId);
        if(level!=null) {
            this.level = level;
            playMusic(level.getMusic());
            String text;
            if(!"start".equals(levelId)) text = str.get("level."+levelId);
            else if(screen.getRound()==0) text = str.get("start");
            else text = str.format("round",screen.getRound()+1);
            showMessage(text,20,0);
        } else {
            screen.endRound();
        }
    }

    public void showMessage(String text,int timer,int action) {
        screen.showMessage(null,text,timer,action,true);
    }

    public void setAction(int action,int timer) {
        this.action = action;
        this.actionTimer = timer;
    }

    public void handleAction() {
        if(action!=0 && (--actionTimer)<=0) {
            int a = action;
            action = 0;
            actionTimer = 0;
            switch(a) {
                case START_GAME:
                    screen.start();
                    startLevel();
                    break;
                case NEXT_LEVEL:
                    nextLevel();
                    break;
                case NEXT_ROUND:
                    startLevel();
                    break;
            }
        }
    }

    /**
     * Upload score to the http-server, and get rank and hiscores.
     */
    public void uploadScore(String name,int round,int time,int score) {
        try {
            name = URLEncoder.encode(name,"UTF-8");
        } catch(Exception ex) {
            name = "---";
        }
        String url = HOST_URL+"/score.php";
        String content = "name="+name+"&round="+round+"&time="+time+"&score="+score;
        HttpRequest httpGet = new HttpRequest(HttpMethods.GET);
        httpGet.setUrl(url);
        httpGet.setContent(content);
        Gdx.net.sendHttpRequest(httpGet,new HttpResponseListener() {
            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
                String response = httpResponse.getResultAsString();
                Json json = new Json();
                HiScore data = json.fromJson(HiScore.class,response);
                screen.showHiScore(data);
            }

            @Override
            public void failed(Throwable t) {
                LadybirdGame.this.startLevel();
            }

            @Override
            public void cancelled() {
                LadybirdGame.this.startLevel();
            }
        });
    }

    @Override
    public String getAppName() {
        return APP_NAME;
    }

    @Override
    public Texture getGUITexture() {
        return getTexture(TEXTURE_GUI);
    }

    public GameScreen getGameScreen() {
        return screen;
    }

    public Level getLevel() {
        return level;
    }

    public void setNextLevelId(String nextLevelId) {
        this.nextLevelId = nextLevelId;
    }

    public Player getPlayer() {
        return player;
    }

    public Clip getClip(int i) {
        return clips[i];
    }

    public Sprite getSprite(int i) {
        return sprites[i];
    }

    public GameImage getImage(int i) {
        return images[i];
    }
}

