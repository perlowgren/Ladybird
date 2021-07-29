package net.spirangle.ladybird;

import static net.spirangle.ladybird.GameScreen.GRID;
import static net.spirangle.ladybird.GameScreen.LAYERS;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.spirangle.ladybird.LevelFactory.CreatureTemplate;
import net.spirangle.ladybird.LevelFactory.ItemTemplate;
import net.spirangle.ladybird.LevelFactory.TileTemplate;
import net.spirangle.minerva.Rectangle;

import java.util.List;
import java.util.Map;

public abstract class GameObject extends Anim {

    public static final int[] imageIndexByType = {
        0,  // Horizontal Flat Tile
        0,  // Vertical Flat Tile
        0,  // Block Tile
        0,  // Large Block 1 Tile
        0,  // Large Block 2 Tile
        0,  // Bush Tile
        0,  // Cloud Tile
        0,  // Door
        1,  // Heart
        1,  // Apple
        1,  // Chest
        1,  // Bottle Green
        1,  // Bottle Violet
        1,  // Bottle Orange
        1,  // Bottle Blue
        2,  // Boogie
        2,  // Grull
        3,  // Troll
    };

    public static final int[][] animationIndexByType = {
        {  0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 0, 2, 0, }, // Stand
        {  0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 1, 3, 1, }, // Walk
        {  0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 0, 2, 2, }, // Jump
        {  0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 0, 2, 2, }, // Die
    };

    public static final int BUFF               = 0x00000001;
    public static final int AGGRO              = 0x00000002;
    public static final int SOLID              = 0x00000004;
    public static final int MOBILE             = 0x00000008;
    public static final int MOVING             = 0x00000010;
    public static final int DEAD               = 0x00000020;
    public static final int STAND              = 0x00000040;
    public static final int WALK               = 0x00000080;
    public static final int JUMP               = 0x00000100;
    public static final int FLIP               = 0x00000200;
    public static final int HIDDEN             = 0x00000400;
    public static final int PASSIVE            = 0x00000800;

    public static final int LEFT               = 0x00001000;
    public static final int RIGHT              = 0x00002000;
    public static final int UP                 = 0x00004000;
    public static final int DOWN               = 0x00008000;

    protected Level level;
    protected int x;
    protected int y;
    protected int z;
    protected int type;
    protected int speed;
    protected int action;
    protected int life;
    protected int jump;
    protected int effect;
    protected int value;
    protected int stat;
    protected Map<String,String> params;
    protected Rectangle space;
    protected Rectangle solid;
    public GameObject grid;

    public GameObject(Level level,int x,int y,int z,int type) {
        super(imageIndexByType[type]);
        this.level = level;
        setType(type);
        this.x = x;
        this.y = y;
        this.z = z;
        space = new Rectangle();
        solid = new Rectangle();
        grid = null;
        setPosition(x,y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public boolean isStatic() {
        return (stat&MOBILE)==0 && (stat&MOVING)==0;
    }

    public boolean isTile() {
        return false;
    }

    public boolean isItem() {
        return false;
    }

    public boolean isCreature() {
        return false;
    }

    public boolean isPlayer() {
        return false;
    }

    public boolean isProjectile() {
        return false;
    }

    public boolean isSolid() {
        return (stat&SOLID)!=0;
    }

    public boolean isMobile() {
        return (stat&MOBILE)!=0;
    }

    public boolean isMoving() {
        return (stat&MOVING)!=0;
    }

    public boolean isDead() {
        return (stat&DEAD)!=0;
    }

    public boolean isJumping() {
        return (stat&JUMP)!=0;
    }

    public boolean isFacingLeft() {
        return flip;
    }

    public boolean isFacingRight() {
        return !flip;
    }

    public void setType(int type) {
        this.type = type;
        setAnimation(animationIndexByType[0][type],false);
    }

    public int getType() {
        return type;
    }

    public List<GameObject> getCollision() {
        return getCollision(0,0,0,0,true);
    }

    public List<GameObject> getCollision(int f) {
        return getCollision(0,0,f,0,true);
    }

    public List<GameObject> getCollision(int x,int y) {
        return getCollision(x,y,0,0,true);
    }

    public List<GameObject> getCollision(int x,int y,int f) {
        return getCollision(x,y,f,0,true);
    }

    public List<GameObject> getCollision(int x,int y,int f,int n) {
        return getCollision(x,y,f,0,true);
    }

    public List<GameObject> getCollision(int x,int y,int f,int n,boolean s) {
        Rectangle r = new Rectangle(solid);
        if(x<0) { r.x += x;r.width -= x; }
        else if(x>0) r.width += x;
        if(y<0) { r.y += y;r.height -= y; }
        else if(y>0) r.height += y;
        if(!s) {
            if(x<0) r.width -= solid.width;
            else if(x>0) { r.x += solid.width;r.width -= solid.width; }
            if(y<0) r.height -= solid.height;
            else if(y>0) { r.y += solid.height;r.height -= solid.height; }
        }
        return level.getCollision(this,r,f,n);
    }

    public boolean isCollision(GameObject o,Rectangle r,int f,int n) {
        return this!=o && solid.intersects(r) && (f==0 || (stat&f)!=0) && (n==0 || (stat&n)==0);
    }

    public void hitTarget(int n) {
    }

    /**
     * @return 0) No hit, dead or inanimate object. 1) Hit, not dead. 2) Hit and dead.
     */
    public int hit(int jmp) {
        if(life<0) life = 0;
        if((stat&DEAD)!=0 || life==0) return 0;
        --life;
        if(life>0) return 1;
        stat |= DEAD;
        z = LAYERS-1;
        if(jmp>=-1) jump = jmp;
        return 2;
    }

    public void update() {
        if(isDead()) {
            move(0,-jump,true);
            if(jump>-8) --jump;
            if(!level.isVisible(space)) delete();
            return;
        }
        if(isMobile()) {
            List<GameObject> objList;
            GameObject obj = null;
            Rectangle border = new Rectangle(0,0,level.getWidth(),level.getHeight());
            int x = 0,y = 0;
            if((action&LEFT)!=0) {
                flip = true;
                if((objList=getCollision(-speed,0,SOLID))!=null || solid.x-speed<=border.x) {
                    if(objList!=null) {
                        for(GameObject o : objList)
                            if(obj==null || o.solid.x+o.solid.width>obj.solid.x+obj.solid.width) obj = o;
                        x = (obj.solid.x+obj.solid.width)-solid.x;
                    } else x = border.x-solid.x;
                    action = (action& ~LEFT)|RIGHT;
                } else x = -speed;
            } else if((action&RIGHT)!=0) {
                flip = false;
                if((objList=getCollision(speed,0,SOLID))!=null || solid.x+solid.width+speed>=border.x+border.width) {
                    if(objList!=null) {
                        for(GameObject o : objList)
                            if(obj==null || o.solid.x<obj.solid.x) obj = o;
                        x = obj.solid.x-(solid.x+solid.width);
                    } else x = (border.x+border.width)-(solid.x+solid.width);
                    action = (action& ~RIGHT)|LEFT;
                } else x = speed;
            } else if((action&UP)!=0) {
                if((objList=getCollision(0,-speed,SOLID))!=null || solid.y-speed<=border.y) {
                    if(objList!=null) {
                        for(GameObject o : objList)
                            if(obj==null || o.solid.y+o.solid.height>obj.solid.y+obj.solid.height) obj = o;
                        y = (obj.solid.y+obj.solid.height)-solid.y;
                    } else y = border.y-solid.y;
                    action = (action& ~UP)|DOWN;
                } else y = -speed;
            } else if((action&DOWN)!=0) {
                if((objList=getCollision(0,speed,SOLID))!=null || solid.y-solid.height+speed>=border.y+border.height) {
                    if(objList!=null) {
                        for(GameObject o : objList)
                            if(obj==null || o.solid.y<obj.solid.y) obj = o;
                        y = obj.solid.y-(solid.y+solid.height);
                    } else y = (border.y+border.height)-(solid.y+solid.height);
                    action = (action& ~DOWN)|UP;
                } else y = speed;
            }
            if(x!=0 || y!=0) {
                // Move objects standing on top:
                if((objList=getCollision(0,-1,MOVING,0,false))!=null)
                    for(GameObject o : objList)
                        if(o.solid.y+o.solid.height==solid.y) o.move(x,y,true);
                move(x,y,true);
            }
        }
    }

    public void draw(GameScreen sc,SpriteBatch batch) {
        if((stat&HIDDEN)==0 && sc.isVisible(space)) {
            draw(batch,x-sc.getX(),y-sc.getY());
        }
    }

    public void delete() {
        level.deleteObject(this);
    }

    public void setData(int action,int speed,int effect,int value,int life,int stat) {
        this.action = action;
        this.speed = speed;
        this.effect = effect;
        this.value = value;
        this.life = life;
        this.stat = stat;
        this.flip = (this.stat&FLIP)!=0;
    }

    public void setData(CreatureTemplate ct) {
        setData(ct.action,ct.speed,ct.effect,ct.value,ct.life,ct.stat);
    }

    public void setData(ItemTemplate it) {
        setData(it.action,it.speed,it.effect,it.value,it.life,it.stat);
    }

    public void setData(TileTemplate tt) {
        setData(tt.action,tt.speed,tt.effect,tt.value,tt.life,tt.stat);
    }

    public void setParams(Map<String,String> params) {
        this.params = params;
    }

    public String getParam(String param) {
        return params!=null? params.get(param) : null;
    }

    public void move(int x,int y,boolean g) {
        if(x==0 && y==0) return;
        if(g && (x==0 || this.x/GRID==(this.x+x)/GRID) && (y==0 || this.y/GRID==(this.y+y)/GRID)) g = false;
        if(g) level.removeObject(this);
        setPosition(this.x+x,this.y+y);
        if(g) level.putObject(this);
    }

    public void transport(int x,int y,int z,boolean g) {
        if(x==this.x && y==this.y && z==this.z) return;
        if(g && (x==0 || this.x/GRID==x/GRID) && (y==0 || this.y/GRID==y/GRID)) g = false;
        if(g) level.removeObject(this);
        setPosition(x,y);
        this.z = z;
        if(g) level.putObject(this);
    }

    private void setPosition(int x,int y) {
        int w = getWidth(),h = getHeight();
        Rectangle s = getSolid();
        this.x = x;
        this.y = y;
        space.set(x,y-h,w,h);
        solid.set(x+s.x,y-h+s.y,s.width,s.height);
    }
}
