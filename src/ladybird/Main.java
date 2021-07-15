package net.spirangle.ladybird;

import java.util.Locale;
import java.net.URLEncoder;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.utils.Json;

import net.spirangle.minerva.gdx.BasicGame;
import net.spirangle.minerva.gdx.BasicScreen;
import net.spirangle.minerva.gdx.LoadingScreen;
import net.spirangle.minerva.gdx.sprite.Clip;
import net.spirangle.minerva.gdx.sprite.Sprite;

public class Main extends BasicGame implements Data {
	public static class HiScore {
		public String name;
		public int rank;
		public String hiscore1;
		public String hiscore2;
		public String hiscore3;
		public String hiscore4;
	};

	private class HiScoreResponse implements HttpResponseListener {
		Main main;
		GameScreen screen;
		HiScoreResponse(Main m,GameScreen s) {
			main = m;
			screen = s;
		}
		public void handleHttpResponse(HttpResponse httpResponse) {
			String response = httpResponse.getResultAsString();
Main.log("HiScoreResponse.handleHttpResponse(response: "+response+")");
			Json json = new Json();
			HiScore data = json.fromJson(HiScore.class,response);
Main.log("HiScoreResponse.uploadScore(name: "+data.name+")");
			screen.showHiScore(data);
		}
		public void failed(Throwable t) {
Main.log("HiScoreResponse.failed()");
			main.startLevel();
		}
		public void cancelled() {
Main.log("HiScoreResponse.cancelled()");
			main.startLevel();
		}
	}

	private static final String[] textureFiles = {
		"splash.png",
		"gui.png",
		"tiles.png",
		"items.png",
		"creatures.png",
		"troll.png",
	};

	private static final String[] fontFiles = {
		"profont12b.fnt",
		"profont12w.fnt",
		"risque14.fnt",
		"risque18.fnt",
	};

	private static final String[] soundFiles = {
		"Appear.ogg",
		"Jump.ogg",
		"Door.ogg",
	};

	private static final String[] musicFiles = {
		"Sunny_Day.ogg",
	};

	private static final String bundleBaseFileHandle = "i18n/ladybird";

	private GameScreen screen;
	private Level level;
	private Player player;
	private Clip[] clips;
	private Sprite[] sprites;
	private GameImage[] images;

	private int action              = 0;
	private int actionTimer         = 0;

	@Override
	public void create() {
		super.create();
		Screen s = new LoadingScreen(this,4.0f,
		                             textureFiles[0],
		                             textureFiles[1],
		                             new int[] { 4,4,13,  0,55,7,13,  7,55,92,13,  99,55,7,13,  0,68,7,13,  7,68,92,13,  99,68,7,13, });
		setScreen(s);
	}

	@Override
	public void loadingAssetsCompleted() {
		super.loadingAssetsCompleted();

		Screen s0 = getScreen();
		s0.dispose();
Main.log("Main.loadingAssetsCompleted(2)");
		screen = new GameScreen(this);
Main.log("Main.loadingAssetsCompleted(3)");
		setScreen(screen);
Main.log("Main.loadingAssetsCompleted(4)");

		screen.showSplash(SPLASH_START,20,str.get("appName"));
	}

	@Override
	public void createGlobalObjects() {
		super.createGlobalObjects();

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
	}

	public void startLevel() {
		player = new Player(this,null,0,0,0,TROLL);
		loadLevel(0);
	}

	public void nextLevel() {
		loadLevel(level.getLevel()+1);
	}

	public void loadLevel(int l) {
Main.log("Main.loadLevel("+l+")");
		screen.clearSplash();
		stopMusic();
		if(l<LevelFactory.getMaxLevels()) {
			level = LevelFactory.createLevel(this,l);
			playMusic(level.getMusic());
			String msg;
			if(l>0) msg = str.format("level",l+1);
			else if(screen.getRound()==0) msg = str.get("start");
			else msg = str.format("round",screen.getRound()+1);
			showMessage(msg,20,0);
		} else {
			screen.endRound();
		}
	}

	public void showMessage(String m,int t,int a) {
		screen.showMessage(null,m,t,a,true);
	}

	public void setAction(int a,int t) {
		action = a;
		actionTimer = t;
	}

	public void handleAction() {
		if(action!=0 && (--actionTimer)<=0) {
			int a = action;
			action = 0;
			actionTimer = 0;
			switch(a) {
				case START_GAME:
Main.log("Main.handleAction(START_GAME)");
					screen.start();
					startLevel();
					break;

				case NEXT_LEVEL:
Main.log("Main.handleAction(NEXT_LEVEL)");
					nextLevel();
					break;

				case NEXT_ROUND:
Main.log("Main.handleAction(NEXT_ROUND)");
					startLevel();
					break;
			}
		}
	}

	/** Upload score to the http-server, and get rank and hiscores.
	 */
	public void uploadScore(String name,int round,int time,int score) {
Main.log("Main.uploadScore(name: "+name+", round: "+round+", time: "+time+", score: "+score+")");
		try {
			name = URLEncoder.encode(name,"UTF-8");
		} catch(Exception ex) {
			name = "---";
		}
		String url = HOST_URL+"/score.php";
		String content = "name="+name+"&round="+round+"&time="+time+"&score="+score;
Main.log("Main.uploadScore(content: "+content+")");
		HttpRequest httpGet = new HttpRequest(HttpMethods.GET);
		httpGet.setUrl(url);
		httpGet.setContent(content);
		HiScoreResponse response = new HiScoreResponse(this,screen);
		Gdx.net.sendHttpRequest(httpGet,response);
	}

	@Override
	public String getAppName() { return APP_NAME; }

	public String[] getTextureFiles() { return textureFiles; }
	public String[] getFontFiles() { return fontFiles; }
	public String[] getSoundFiles() { return soundFiles; }
	public String[] getMusicFiles() { return musicFiles; }

	public String getTextureFileName(int i) { return textureFiles[i]; }
	public String getFontFileName(int i) { return fontFiles[i]; }
	public String getSoundFileName(int i) { return soundFiles[i]; }
	public String getMusicFileName(int i) { return musicFiles[i]; }

	public String getBundleBaseFileHandle() { return bundleBaseFileHandle; }

	public Texture getGUITexture() { return getTexture(TEXTURE_GUI); }

	public GameScreen getGameScreen() { return screen; }
	public Level getLevel() { return level; }
	public Player getPlayer() { return player; }
	public Clip getClip(int i) { return clips[i]; }
	public Sprite getSprite(int i) { return sprites[i]; }
	public GameImage getImage(int i) { return images[i]; }
}

