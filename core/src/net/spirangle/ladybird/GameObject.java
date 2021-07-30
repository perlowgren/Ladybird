package net.spirangle.ladybird;

import static net.spirangle.ladybird.GameScreen.GRID;
import static net.spirangle.ladybird.LadybirdGame.*;
import static net.spirangle.ladybird.Level.Action.NEXT_LEVEL;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.spirangle.ladybird.LevelFactory.CreatureTemplate;
import net.spirangle.ladybird.LevelFactory.ItemTemplate;
import net.spirangle.ladybird.LevelFactory.TileTemplate;
import net.spirangle.minerva.Rectangle;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

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
    public static final int PASSIVE            = 0x00000040;
    public static final int WALK               = 0x00000080;
    public static final int JUMP               = 0x00000100;
    public static final int FLIP               = 0x00000200;
    public static final int HIDDEN             = 0x00000400;

    public static final int LEFT               = 0x00001000;
    public static final int RIGHT              = 0x00002000;
    public static final int UP                 = 0x00004000;
    public static final int DOWN               = 0x00008000;

    public enum Effect {
        LIFE(true,(p,o) -> {
            if(p.life<10) p.life += o.value;
            LadybirdGame.getInstance().playSound(SOUND_HEART);
        }),
        AMMO(true,(p,o) -> {
            p.ammo += o.value;
            LadybirdGame.getInstance().playSound(SOUND_APPLE);
        }),
        POWER(true,(p,o) -> {
            p.power += o.value;
            if(p.power<0) p.power = 0;
            ++p.buffs[2];
            LadybirdGame.getInstance().playSound(SOUND_POTION);
        }),
        SPEED(true,(p,o) -> {
            p.speed += o.value;
            if(p.speed<1) p.speed = 1;
            ++p.buffs[1];
            LadybirdGame.getInstance().playSound(SOUND_POTION);
        }),
        GOLD(true,(p,o) -> {
            p.chests += o.value;
            LadybirdGame.getInstance().playSound(SOUND_CHEST);
        }),
        EXIT(false,(p,o) -> {
            if(p.chests>=p.collect) {
                LadybirdGame game = LadybirdGame.getInstance();
                String nextLevelId = o.getParam("level");
                if(nextLevelId==null) nextLevelId = game.getLevel().getNextLevelId();
                if(nextLevelId!=null) {
                    p.setPassive(true);
                    p.level.removeObject(p);
                    game.playSound(SOUND_DOOR);
                    game.setNextLevelId(nextLevelId);
                    game.setAction(NEXT_LEVEL,12);
                }
            }
        });

        private final boolean consume;
        private final BiConsumer<Player,GameObject> effect;

        Effect(boolean consume,BiConsumer<Player,GameObject> effect) {
            this.consume = consume;
            this.effect = effect;
        }

        public void activate(Player player,GameObject object) {
            effect.accept(player,object);
            if(consume) object.delete();
        }
    }

    protected Level level;
    protected int x;
    protected int y;
    protected int z;
    protected int type;
    protected int speed;
    protected int action;
    protected int life;
    protected int jump;
    protected Effect effect;
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

    public final boolean isBuff() {
        return (stat&BUFF)!=0;
    }

    public final boolean isAggro() {
        return (stat&AGGRO)!=0;
    }

    public final boolean isSolid() {
        return (stat&SOLID)!=0;
    }

    public final boolean isMobile() {
        return (stat&MOBILE)!=0;
    }

    public final boolean isMoving() {
        return (stat&MOVING)!=0;
    }

    public final void setDead(boolean dead) {
        if(dead) stat |= DEAD;
        else stat &= ~DEAD;
    }

    public final boolean isDead() {
        return (stat&DEAD)!=0;
    }

    public final void setPassive(boolean passive) {
        if(passive) stat |= PASSIVE;
        else stat &= ~PASSIVE;
    }

    public final boolean isPassive() {
        return (stat&PASSIVE)!=0;
    }

    public final void setWalking(boolean walking) {
        if(walking) stat |= WALK;
        else stat &= ~WALK;
    }

    public final boolean isWalking() {
        return (stat&WALK)!=0;
    }

    public final boolean isNotWalking() {
        return (stat&WALK)==0 || (stat&(DEAD|PASSIVE|JUMP))!=0;
    }

    public final void setJumping(boolean jumping) {
        if(jumping) stat |= JUMP;
        else stat &= ~JUMP;
    }

    public final boolean isJumping() {
        return (stat&JUMP)!=0;
    }

    public final void setHidden(boolean hidden) {
        if(hidden) stat |= HIDDEN;
        else stat &= ~HIDDEN;
    }

    public final boolean isHidden() {
        return (stat&HIDDEN)!=0;
    }

    public final void setActionJump() {
        action |= JUMP;
    }

    public final boolean isActionJump() {
        return (action&JUMP)!=0;
    }

    public final void setActionMoveLeft() {
        action = (action& ~RIGHT)|LEFT;
    }

    public final boolean isActionMoveLeft() {
        return (action&LEFT)!=0;
    }

    public final void setActionMoveRight() {
        action = (action& ~LEFT)|RIGHT;
    }

    public final boolean isActionMoveRight() {
        return (action&RIGHT)!=0;
    }

    public final void setActionMoveUp() {
        action = (action& ~DOWN)|UP;
    }

    public final boolean isActionMoveUp() {
        return (action&UP)!=0;
    }

    public final void setActionMoveDown() {
        action = (action& ~UP)|DOWN;
    }

    public final boolean isActionMoveDown() {
        return (action&DOWN)!=0;
    }

    public final boolean isActionMoveHorizontally() {
        return (action&(LEFT|RIGHT))!=0;
    }

    public final boolean isActionMoveVertically() {
        return (action&(UP|DOWN))!=0;
    }

    public final void setFacingLeft() {
        flip = true;
    }

    public final boolean isFacingLeft() {
        return flip;
    }

    public final void setFacingRight() {
        flip = false;
    }

    public final boolean isFacingRight() {
        return !flip;
    }

    public void setType(int type) {
        this.type = type;
        setAnimation(animationIndexByType[0][type],false);
    }

    public int getType() {
        return type;
    }

    public List<GameObject> getCollisions() {
        return getCollisions(0,0,0,0,true);
    }

    public List<GameObject> getCollisions(int x,int y,int hasFlags) {
        return getCollisions(x,y,hasFlags,0,true);
    }

    public List<GameObject> getCollisions(int x,int y,int hasFlags,int hasNotFlags,boolean includeSolid) {
        return level.getCollisions(this,getCollisionRectangle(x,y,includeSolid),hasFlags,hasNotFlags);
    }

    public boolean hasCollision(int x,int y,int hasFlags,int hasNotFlags,boolean includeSolid) {
        return level.hasCollision(this,getCollisionRectangle(x,y,includeSolid),hasFlags,hasNotFlags);
    }

    private Rectangle getCollisionRectangle(int x,int y,boolean includeSolid) {
        Rectangle r = new Rectangle(solid);
        if(x>0) r.width += x;
        else if(x<0) {
            r.x += x;
            r.width -= x;
        }
        if(y>0) r.height += y;
        else if(y<0) {
            r.y += y;
            r.height -= y;
        }
        if(!includeSolid) {
            if(x<0) r.width -= solid.width;
            else if(x>0) {
                r.x += solid.width;
                r.width -= solid.width;
            }
            if(y<0) r.height -= solid.height;
            else if(y>0) {
                r.y += solid.height;
                r.height -= solid.height;
            }
        }
        return r;
    }

    public boolean isCollision(GameObject o,Rectangle r,int hasFlags,int hasNotFlags) {
        return this!=o && solid.intersects(r) &&
               (hasFlags==0 || (stat&hasFlags)!=0) &&
               (hasNotFlags==0 || (stat&hasNotFlags)==0);
    }

    public boolean isCollisionAbove(GameObject o,int y) {
        return o!=null && o.solid.y+o.solid.height>=solid.y-jump && o.solid.y+o.solid.height<=solid.y;
    }

    public boolean isCollisionBelow(GameObject o,int y) {
        return o!=null && o.solid.y>=solid.y+solid.height;
    }

    public boolean isBottomBelow(GameObject o) {
        return o==null || solid.y+solid.height>o.solid.y+o.solid.height;
    }

    public boolean isTopAbove(GameObject o) {
        return o==null || solid.y<o.solid.y;
    }

    public void hitTarget(GameObject target) {
    }

    public boolean hit(int xForce,int yForce) {
        return true;
    }

    public void update() {
        if(isDead()) {
            move(speed,-jump,true);
            if(jump>-8) --jump;
            if(!level.isVisible(space)) delete();
            return;
        }
        if(isMobile()) {
            List<GameObject> objList;
            GameObject obj = null;
            Rectangle border = new Rectangle(0,0,level.getWidth(),level.getHeight());
            int x = 0,y = 0;
            if(isActionMoveLeft()) {
                setFacingLeft();
                if((objList=getCollisions(-speed,0,SOLID))!=null || solid.x-speed<=border.x) {
                    if(objList!=null) {
                        for(GameObject o : objList)
                            if(obj==null || o.solid.x+o.solid.width>obj.solid.x+obj.solid.width) obj = o;
                        x = (obj.solid.x+obj.solid.width)-solid.x;
                    } else x = border.x-solid.x;
                    setActionMoveRight();
                } else x = -speed;
            } else if(isActionMoveRight()) {
                setFacingRight();
                if((objList=getCollisions(speed,0,SOLID))!=null || solid.x+solid.width+speed>=border.x+border.width) {
                    if(objList!=null) {
                        for(GameObject o : objList)
                            if(obj==null || o.solid.x<obj.solid.x) obj = o;
                        x = obj.solid.x-(solid.x+solid.width);
                    } else x = (border.x+border.width)-(solid.x+solid.width);
                    setActionMoveLeft();
                } else x = speed;
            }
            if(isActionMoveUp()) {
                if((objList=getCollisions(0,-speed,SOLID))!=null || solid.y-speed<=border.y) {
                    if(objList!=null) {
                        for(GameObject o : objList)
                            if(obj==null || o.solid.y+o.solid.height>obj.solid.y+obj.solid.height) obj = o;
                        y = (obj.solid.y+obj.solid.height)-solid.y;
                    } else y = border.y-solid.y;
                    setActionMoveDown();
                } else y = -speed;
            } else if(isActionMoveDown()) {
                if((objList=getCollisions(0,speed,SOLID))!=null || solid.y-solid.height+speed>=border.y+border.height) {
                    if(objList!=null) {
                        for(GameObject o : objList)
                            if(obj==null || o.solid.y<obj.solid.y) obj = o;
                        y = obj.solid.y-(solid.y+solid.height);
                    } else y = (border.y+border.height)-(solid.y+solid.height);
                    setActionMoveUp();
                } else y = speed;
            }
            if(x!=0 || y!=0) {
                // Move objects standing on top:
                if((objList=getCollisions(0,-1,MOVING,0,false))!=null)
                    for(GameObject o : objList)
                        if(o.solid.y+o.solid.height==solid.y) o.move(x,y,true);
                move(x,y,true);
            }
        }
    }

    public void draw(GameScreen screen,SpriteBatch batch) {
        if(!isHidden() && screen.isVisible(space)) {
            draw(batch,x-screen.getX(),y-screen.getY());
        }
    }

    public void delete() {
        level.deleteObject(this);
    }

    public void setData(int action,int speed,Effect effect,int value,int life,int stat) {
        this.action = action;
        this.speed = speed;
        this.effect = effect;
        this.value = value;
        this.life = life;
        this.stat = stat;
        this.flip = (this.stat&FLIP)!=0;
    }

    public void setData(CreatureTemplate template) {
        setData(template.action,template.speed,template.effect,template.value,template.life,template.stat);
    }

    public void setData(ItemTemplate template) {
        setData(template.action,template.speed,template.effect,template.value,template.life,template.stat);
    }

    public void setData(TileTemplate template) {
        setData(template.action,template.speed,template.effect,template.value,template.life,template.stat);
    }

    public void setParams(Map<String,String> params) {
        this.params = params;
    }

    public String getParam(String param) {
        return params!=null? params.get(param) : null;
    }

    public void move(int x,int y,boolean updateGrid) {
        if(x==0 && y==0) return;
        if(updateGrid && (x==0 || this.x/GRID==(this.x+x)/GRID) && (y==0 || this.y/GRID==(this.y+y)/GRID)) updateGrid = false;
        if(updateGrid) level.removeObject(this);
        setPosition(this.x+x,this.y+y);
        if(updateGrid) level.putObject(this);
    }

    public void transport(int x,int y,int z,boolean updateGrid) {
        if(x==this.x && y==this.y && z==this.z) return;
        if(updateGrid && (x==0 || this.x/GRID==x/GRID) && (y==0 || this.y/GRID==y/GRID)) updateGrid = false;
        if(updateGrid) level.removeObject(this);
        setPosition(x,y);
        this.z = z;
        if(updateGrid) level.putObject(this);
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
