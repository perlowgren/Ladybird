package net.spirangle.ladybird;

import static net.spirangle.ladybird.GameScreen.GRID;
import static net.spirangle.ladybird.GameScreen.LAYERS;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.eclipsesource.json.JsonObject;

import net.spirangle.minerva.Rectangle;

import java.util.Iterator;

public class Level {

    public static final int START_GAME         = 1;
    public static final int NEXT_LEVEL         = 2;
    public static final int NEXT_ROUND         = 3;

    private final GameScreen screen;
    private final Color bgcolor;
    private final String bgmusic;
    private final String levelId;
    private final String nextLevelId;
    private final int width;
    private final int height;
    private int chests;
    private final Array<GameObject> updateIndex;
    private final GameObject[][] grid;

    public Level(LadybirdGame g,JsonObject json,String levelId) {
        this.screen = g.getGameScreen();
        this.bgcolor = Color.valueOf(json.getString("bgcolor","#c8c8ff"));
        this.bgmusic = json.getString("bgmusic",null);
        this.width = json.getInt("width",0);
        this.height = json.getInt("height",0);
        this.chests = 0;
        this.levelId = levelId;
        this.nextLevelId = json.getString("nextLevel","finish");
        this.updateIndex = new Array<>();
        this.grid = new GameObject[width/GRID+1][height/GRID+1];
    }

    public Color getBackgroundColor() {
        return bgcolor;
    }

    public String getMusic() {
        return bgmusic;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void addChest() {
        ++chests;
    }

    public int getChests() {
        return chests;
    }

    public String getLevelId() {
        return levelId;
    }

    public String getNextLevelId() {
        return nextLevelId;
    }

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
        if(x<0 || x>=grid.length || y<0 || y>=grid[0].length) return;
        to.grid = grid[x][y];
        grid[x][y] = to;
    }

    public void removeObject(GameObject to) {
        int x = to.getX()/GRID,y = to.getY()/GRID;
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
        for(x1=Math.max(x-4,0); x1<=x+4 && x1<grid.length; ++x1)
            for(y1=Math.max(y-4,0); y1<=y+4 && y1<grid[x1].length; ++y1)
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
            to.update();
        }
    }

    public void draw(SpriteBatch batch) {
        int x,y,n;
        GameObject to;
        GameObject[] z = new GameObject[LAYERS];
        Rectangle view = screen.getView();

        for(x=Math.max(view.x/GRID-4,0); (x-4)*GRID<=view.x+view.width && x<grid.length; ++x)
            for(y=Math.max(view.y/GRID-4,0); (y-4)*GRID<=view.y+view.height && y<grid[x].length; ++y)
                for(to=grid[x][y]; to!=null; to=to.grid) {
                    n = to.getZ();
                    to.next = z[n];
                    z[n] = to;
                }

        for(n=0; n<LAYERS; ++n)
            for(to=z[n]; to!=null; to=to.next)
                to.draw(screen,batch);
    }
}
