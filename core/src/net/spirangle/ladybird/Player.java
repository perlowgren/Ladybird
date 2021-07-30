package net.spirangle.ladybird;

import static net.spirangle.ladybird.GameScreen.LAYERS;
import static net.spirangle.ladybird.LadybirdGame.*;
import static net.spirangle.ladybird.Level.Action.START_GAME;
import static net.spirangle.ladybird.LevelFactory.GameObjectTemplate.APPLE;

import net.spirangle.ladybird.LevelFactory.PlayerTemplate;
import net.spirangle.minerva.Rectangle;
import net.spirangle.minerva.gdx.GameBase;

import java.util.List;

public class Player extends Creature {
    public static boolean[] keys = new boolean[6];

    protected int startX;
    protected int startY;
    protected int startZ;
    protected int startStat;
    protected int chests;
    protected int collect;
    protected int power;
    protected int ammo;
    protected int[] buffs;
    protected int timer;
    protected int frames;
    protected int hits;
    protected int kills;

    protected boolean footsteps;

    public Player(Level level,int x,int y,int z,int type) {
        super(level,x,y,z,type);
        startX = x;
        startY = y;
        startZ = z;
        startStat = 0;

        chests = 0;
        life = 0;
        ammo = 0;
        buffs = new int[] { 0,0,0,0 };

        frames = 0;
        hits = 0;
        kills = 0;

        footsteps = false;
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getHearts() {
        return life;
    }

    public int getApples() {
        return ammo;
    }

    public int getChests() {
        return chests;
    }

    public void setCollect(int n) {
        collect = n;
    }

    public int getCollect() {
        return collect;
    }

    public int getBuffs(int n) {
        return n>=0 && n<4? buffs[n] : 0;
    }

    public boolean gameOver() {
        return life==0;
    }

    public int getFrames() {
        return frames;
    }

    public int getScore() {
        int score = 3000-frames;
        if(score<0) score = 0;
        score += life  * 1000;
        score += ammo  * 50;
        score += hits  * 50;
        score += kills * 100;
        return score;
    }

    @Override
    public void hitTarget(GameObject target) {
        ++hits;
        if(target.isDead()) ++kills;
    }

    @Override
    public boolean hit(int xForce,int yForce) {
        if(isDead()) return false;
        if(yForce!=0) jump = yForce;
        z = LAYERS-1;
        setDead(true);
        LadybirdGame.getInstance().playSound(SOUND_TROLL_HIT);
        return true;
    }

    @Override
    public void update() {
        LadybirdGame game = LadybirdGame.getInstance();
        Rectangle border = new Rectangle(0,0,level.getWidth(),level.getHeight());
        int n;
        List<GameObject> objList;
        GameObject obj = null;

        ++frames;
        if(timer>0) --timer;

        if(isPassive()) return;

        if(isDead()) {
            if(life>0) {
                if(!game.getGameScreen().isVisible(space)) {
                    --life;
                    if(life>0) {
                        transport(startX,startY,startZ,true);
                        stat = startStat;
                    } else {
                        setPassive(true);
                        level.removeObject(this);
                        game.showMessage(GameBase.str.get("gameOver"),40,START_GAME);
                    }
                } else {
                    move(isFacingLeft()? -speed : speed,-jump,true);
                    if(jump>-8) --jump;
                }
            }
            return;
        }

        setWalking(false);
        action = 0;
        if(keys[0]) setActionMoveLeft();
        if(keys[1]) setActionMoveRight();
        if(keys[2]) {
            setActionJump();
            keys[2] = false;
        }
        if(keys[3]) setActionMoveUp();
        if(keys[4]) setActionMoveDown();
        if(keys[5]) shoot();

        if(isActionMoveHorizontally()) setWalking(true);

        // Handle walking:
        if(isWalking()) {
            if(isActionMoveLeft()) {
                setFacingLeft();
                if(!hasCollision(-speed,0,SOLID,MOBILE,false))
                    move(-speed,0,true);
            } else if(isActionMoveRight()) {
                setFacingRight();
                if(!hasCollision(speed,0,SOLID,MOBILE,false))
                    move(speed,0,true);
            }
        }

        if(isActionJump() && !isJumping()) {
            jump = power;
            setJumping(true);
            game.playSound(SOUND_JUMP);
        }

        // Check if standing on solid ground, otherwise flag jumping:
        if(!isJumping() && !hasCollision(0,1,SOLID,0,false))
            setJumping(true);

        // Handle jumping:
        if(isJumping()) {
            if(jump>0) { // Collision detection for jumping up:
                if((objList=getCollisions(0,-jump,SOLID))!=null) {
                    obj = null;
                    for(GameObject o : objList)
                        if(!o.isMobile() && isCollisionAbove(o,jump) && o.isBottomBelow(obj)) obj = o;
                    if(obj!=null) {
                        move(0,(obj.solid.y+obj.solid.height)-solid.y,true);
                        jump = 0; // Stop upward jump motion
                        --jump; // Next cycle begin falling down (otherwise two cycles with jump==0)
                    }
                }
            }
            if(jump<=0) { // Collision detection for falling down:
                if((objList=getCollisions(0,-jump,SOLID))!=null) {
                    obj = null;
                    for(GameObject o : objList)
                        if(isCollisionBelow(o,0) && o.isTopAbove(obj)) obj = o;
                    if(obj!=null) {
                        move(0,obj.solid.y-(solid.y+solid.height),true);
                        jump = 0;
                        setJumping(false);
                    }
                }
            }
            // If no collision and still jumping
            if(obj==null && isJumping()) {
                move(0,-jump,true);
                if(jump>-power) --jump;
            }
        }

        if((objList=getCollisions())!=null) {
            // Check if hit by a monster:
            for(GameObject o : objList)
                if(o.isAggro() && !o.isDead()) {
                    o.hit(0,-1);
                    hit(0,3);
                    break;
                }

            for(GameObject o : objList)
                if(o.effect!=null)
                    o.effect.activate(this,o);
        }

        // Cannot move outside level border, to left and right:
        if(solid.x<border.x)
            move(border.x-solid.x,0,true);
        else if(solid.x+solid.width>border.x+border.width)
            move((border.x+border.width)-(solid.x+solid.width),0,true);
        // Killed if moves below level border bottom:
        if(solid.y+solid.height>border.height)
            hit(0,-2);

        if(isDead()) n = 3;
        else if(isJumping()) n = 2;
        else if(isWalking()) n = 1;
        else  n = 0;
        image.setAnimation(animationIndexByType[n][type],this);

        if(isNotWalking()) {
            if(footsteps) {
                game.stopSound(SOUND_FOOTSTEPS);
                footsteps = false;
            }
        } else if(isWalking()) {
            if(!footsteps) {
                game.loopSound(SOUND_FOOTSTEPS);
                footsteps = true;
            }
        }
    }

    public void setData(PlayerTemplate pd) {
        this.action = pd.action;
        this.speed = pd.speed;
        this.power = pd.power;
        this.jump = pd.jump;
        this.chests = pd.chests;
        this.life += pd.life;
        this.ammo += pd.ammo;
        this.buffs = new int[] { 0,0,0,0 };
        this.stat = pd.stat;
        this.timer = 0;
        if((this.stat&FLIP)!=0) setFacingLeft();
        this.startStat = this.stat;
    }

    public void shoot() {
        if(ammo==0 || timer>0) return;
        int x = solid.x+solid.width/2;
        int y = solid.y+solid.height/2;
        Projectile p = new Projectile(level,x,y,LAYERS-1,APPLE.index,this);
        p.setData(0,3,null,1,0,MOVING|(flip? FLIP : 0));
        p.jump = 1;
        level.addObject(p);
        --ammo;
        timer = 3;
        LadybirdGame.getInstance().playSound(SOUND_THROW);
    }

    @Override
    public void transport(int x,int y,int z,boolean updateGrid) {
        super.transport(x,y,z,updateGrid);
        startX = x;
        startY = y;
        startZ = z;
    }
}
