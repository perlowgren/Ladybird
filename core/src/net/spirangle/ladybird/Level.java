package net.spirangle.ladybird;

import java.util.Iterator;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import net.spirangle.minerva.Rectangle;

public class Level implements Data {
	private Main game;
	private GameScreen screen;
	private int bgcolor;
	private int bgmusic;
	private int level;
	private int width;
	private int height;
	private Array<GameObject> updateIndex;
	private GameObject[][] grid;

	public Level(Main g,int bgc,int bgm,int w,int h,int lvl) {
		game = g;
		screen = g.getGameScreen();
		bgcolor = bgc;
		bgmusic = bgm;
		width = w;
		height = h;
		level = lvl;
		updateIndex = new Array<GameObject>();
		grid = new GameObject[w/GRID+1][h/GRID+1];
Main.log("Level(grid: w="+((w/GRID)+1)+", h="+((h/GRID)+1)+")");
	}

	public int getBackgroundColor() { return bgcolor; }
	public int getMusic() { return bgmusic; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getLevel() { return level; }

	public boolean isVisible(Rectangle r) {
		return r.x+r.width>0 && r.y+r.height>0 && r.x<width && r.y<height;
	}

	public void addObject(GameObject to) {
		if(!to.isStatic()) updateIndex.add(to);
		putObject(to);
	}

	public void deleteObject(GameObject to) {
		updateIndex.removeValue(to,true);
		removeObject(to);
	}

	public void putObject(GameObject to) {
		int x = to.getX()/GRID,y = to.getY()/GRID;
//Main.log("Level.putObject(x="+x+", y="+y+", type="+to.getType()+")");
		if(x<0 || x>=grid.length || y<0 || y>=grid[0].length) return;
		to.grid = grid[x][y];
		grid[x][y] = to;
	}

	public void removeObject(GameObject to) {
		int x = to.getX()/GRID,y = to.getY()/GRID;
//Main.log("Level.removeObject(x="+x+", y="+y+", type="+to.getType()+")");
		if(x<0 || x>=grid.length || y<0 || y>=grid[0].length) return;
		GameObject to1 = grid[x][y];
		if(to1==to) grid[x][y] = to.grid;
		else if(to1!=null) {
			while(to1.grid!=to && to1.grid!=null) to1 = to1.grid;
			if(to1.grid==to) to1.grid = to.grid;
		}
		to.grid = null;
	}

	public GameObject getCollision(GameObject to,Rectangle r,int f,int n) {
		GameObject to1 = null,to2;
		int x = r.x/GRID,y = r.y/GRID,x1,y1;
		for(x1=(x-4>=0? x-4 : 0); x1<=x+4 &&  x1<grid.length; ++x1)
			for(y1=(y-4>=0? y-4 : 0); y1<=y+4 && y1<grid[x1].length; ++y1)
				for(to2=grid[x1][y1]; to2!=null; to2=to2.grid) {
					if(to2.isCollision(to,r,f,n)) {
						to2.next = to1;
						to1 = to2;
					}
				}
		return to1;
	}

	public void update() {
		Iterator<GameObject> iter = updateIndex.iterator();
		GameObject to;
		while(iter.hasNext()) {
			to = iter.next();
			to.update(game);
		}
	}

	public void draw(SpriteBatch batch) {
		int x,y,n;
		GameObject to,z[] = new GameObject[LAYERS];
		Rectangle view = screen.getView();

		for(x=(view.x/GRID-4>=0? view.x/GRID-4 : 0); (x-4)*GRID<=view.x+view.width && x<grid.length; ++x)
			for(y=(view.y/GRID-4>=0? view.y/GRID-4 : 0); (y-4)*GRID<=view.y+view.height && y<grid[x].length; ++y)
				for(to=grid[x][y]; to!=null; to=to.grid) {
					n = to.getZ();
					to.next = z[n];
					z[n] = to;
				}

		for(n=0; n<LAYERS; ++n)
			for(to=z[n]; to!=null; to=to.next)
				to.draw(game,screen,batch);
	}
}
