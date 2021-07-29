package net.spirangle.ladybird;

import static net.spirangle.ladybird.GameScreen.GRID;
import static net.spirangle.ladybird.GameScreen.LAYERS;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.eclipsesource.json.JsonObject;

import net.spirangle.minerva.Rectangle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private final Set<GameObject> repository;
    private final GameObject[][] grid;
    private Set<GameObject> added;
    private Set<GameObject> removed;

    public Level(LadybirdGame g,JsonObject json,String levelId) {
        this.screen = g.getGameScreen();
        this.bgcolor = Color.valueOf(json.getString("bgcolor","#c8c8ff"));
        this.bgmusic = json.getString("bgmusic",null);
        this.width = json.getInt("width",0);
        this.height = json.getInt("height",0);
        this.chests = 0;
        this.levelId = levelId;
        this.nextLevelId = json.getString("nextLevel","finish");
        this.repository = new HashSet<>();
        this.grid = new GameObject[width/GRID+1][height/GRID+1];
        this.added = null;
        this.removed = null;
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

    public synchronized void addObject(GameObject o) {
        if(added==null) added = new HashSet<>();
        added.add(o);
    }

    public void deleteObject(GameObject o) {
        if(removed==null) removed = new HashSet<>();
        removed.add(o);
    }

    public void putObject(GameObject o) {
        int x = o.getX()/GRID,y = o.getY()/GRID;
        if(x<0 || x>=grid.length || y<0 || y>=grid[0].length) return;
        o.grid = grid[x][y];
        grid[x][y] = o;
    }

    public void removeObject(GameObject o) {
        int x = o.getX()/GRID,y = o.getY()/GRID;
        if(x<0 || x>=grid.length || y<0 || y>=grid[0].length) return;
        GameObject to1 = grid[x][y];
        if(to1==o) grid[x][y] = o.grid;
        else if(to1!=null) {
            while(to1.grid!=o && to1.grid!=null) to1 = to1.grid;
            if(to1.grid==o) to1.grid = o.grid;
        }
        o.grid = null;
    }

    public List<GameObject> getCollisions(GameObject o,Rectangle r,int hasFlags,int hasNotFlags) {
        List<GameObject> objList = null;
        int x = r.x/GRID,y = r.y/GRID;
        for(int x1=Math.max(x-4,0); x1<=x+4 && x1<grid.length; ++x1)
            for(int y1=Math.max(y-4,0); y1<=y+4 && y1<grid[x1].length; ++y1)
                for(GameObject obj=grid[x1][y1]; obj!=null; obj=obj.grid)
                    if(obj.isCollision(o,r,hasFlags,hasNotFlags)) {
                        if(objList==null) objList = new ArrayList<>();
                        objList.add(obj);
                    }
        return objList;
    }

    public boolean hasCollision(GameObject o,Rectangle r,int hasFlags,int hasNotFlags) {
        int x = r.x/GRID,y = r.y/GRID;
        for(int x1=Math.max(x-4,0); x1<=x+4 && x1<grid.length; ++x1)
            for(int y1=Math.max(y-4,0); y1<=y+4 && y1<grid[x1].length; ++y1)
                for(GameObject obj=grid[x1][y1]; obj!=null; obj=obj.grid)
                    if(obj.isCollision(o,r,hasFlags,hasNotFlags))
                        return true;
        return false;
    }

    public void update() {
        if(added!=null) {
            for(GameObject o : added) {
                if(!o.isStatic()) repository.add(o);
                putObject(o);
            }
            added = null;
        }
        if(removed!=null) {
            for(GameObject o : removed) {
                repository.remove(o);
                removeObject(o);
            }
            removed = null;
        }
        repository.forEach(GameObject::update);
    }

    public void draw(SpriteBatch batch) {
        Rectangle view = screen.getView();
        Set<GameObject> objList = new HashSet<>();

        for(int x=Math.max(view.x/GRID-4,0); (x-4)*GRID<=view.x+view.width && x<grid.length; ++x)
            for(int y=Math.max(view.y/GRID-4,0); (y-4)*GRID<=view.y+view.height && y<grid[x].length; ++y)
                for(GameObject o=grid[x][y]; o!=null; o=o.grid)
                    objList.add(o);

        for(int n=0; n<LAYERS; ++n)
            for(GameObject o : objList)
                if(o.getZ()==n) o.draw(screen,batch);
    }
}
