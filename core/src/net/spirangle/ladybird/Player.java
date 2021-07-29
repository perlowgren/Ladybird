package net.spirangle.ladybird;

import static net.spirangle.ladybird.GameScreen.LAYERS;
import static net.spirangle.ladybird.LadybirdGame.*;
import static net.spirangle.ladybird.Level.NEXT_LEVEL;
import static net.spirangle.ladybird.Level.START_GAME;
import static net.spirangle.ladybird.LevelFactory.*;
import static net.spirangle.ladybird.LevelFactory.GameObjectTemplate.APPLE;

import net.spirangle.ladybird.LevelFactory.*;
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
    public void hitTarget(int n) {
        if(n>0) {
            ++hits;
            if(n==2) ++kills;
        }
    }

    @Override
    public int hit(int jmp) {
        if((stat&DEAD)!=0) return 0;
        if(jmp>=-1) jump = jmp;
        z = LAYERS-1;
        stat |= DEAD;
        return 2;
    }

    @Override
    public void update() {
        LadybirdGame game = LadybirdGame.getInstance();
        Rectangle border = new Rectangle(0,0,level.getWidth(),level.getHeight());
        int n;
        GameObject obj = null;
        GameObject o1;

        ++frames;
        if(timer>0) --timer;

        if((stat&PASSIVE)!=0) return;

        if((stat&DEAD)!=0) {
            if(life>0) {
                if(!game.getGameScreen().isVisible(space)) {
                    --life;
                    if(life>0) {
                        transport(startX,startY,startZ,true);
                        stat = startStat;
                    } else {
                        stat |= PASSIVE;
                        level.removeObject(this);
                        game.showMessage(GameBase.str.get("gameOver"),40,START_GAME);
                    }
                } else {
                    move(flip? -speed : speed,-jump,true);
                    if(jump>-8) --jump;
                }
            }
            return;
        }

        stat &= ~(STAND|WALK);
        action = 0;
        if(keys[0]) action |= LEFT;
        if(keys[1]) action |= RIGHT;
        if(keys[3]) action |= UP;
        if(keys[4]) action |= DOWN;
        if((action&(LEFT|RIGHT))!=0) stat |= WALK;

        if(keys[2]) {
            action |= JUMP;
            keys[2] = false;
        }

        if(keys[5]) shoot(game);

        // Handle walking:
        if((stat&WALK)!=0) {
            if((action&LEFT)!=0) {
                flip = true;
                if(getCollision(-speed,0,SOLID,MOBILE,false)==null) move(-speed,0,true);
            } else if((action&RIGHT)!=0) {
                flip = false;
                if(getCollision(speed,0,SOLID,MOBILE,false)==null) move(speed,0,true);
            }
        }

        if((action&JUMP)!=0 && (stat&JUMP)==0) {
            jump = power;
            stat |= JUMP;
            game.playSound(SOUND_JUMP);
        }

        // Check if standing on solid ground, otherwise flag jumping:
        if((stat&JUMP)==0 && getCollision(0,1,SOLID,0,false)==null) {
            stat |= JUMP;
        }

        // Handle jumping:
        if((stat&JUMP)!=0) {
            if(jump>0) { // Collision detection for jumping up:
                if((obj=getCollision(0,-jump,SOLID))!=null) {
                    for(o1=obj,obj=null; o1!=null; o1=o1.next)
                        if((o1.stat&MOBILE)==0 && // For unmoving solid objects
                           o1.solid.y+o1.solid.height>=solid.y-jump && o1.solid.y+o1.solid.height<=solid.y && // Only collision if above, not if already within bounds
                           (obj==null || o1.solid.y+o1.solid.height>obj.solid.y+obj.solid.height) // Find highest GameObject
                        ) obj = o1;
                    if(obj!=null) {
                        move(0,(obj.solid.y+obj.solid.height)-solid.y,true);
                        jump = 0; // Stop upward jump motion
                        --jump; // Next cycle begin falling down (otherwise two cycles with jump==0)
                    }
                }
            }
            if(jump<=0) { // Collision detection for falling down:
                if((obj=getCollision(0,-jump,SOLID))!=null) {
                    for(o1=obj,obj=null; o1!=null; o1=o1.next)
                        if(o1.solid.y>=solid.y+solid.height && (obj==null || o1.solid.y<obj.solid.y)) obj = o1;
                    if(obj!=null) {
                        move(0,obj.solid.y-(solid.y+solid.height),true);
                        stat &= ~JUMP;
                        jump = 0;
                    }
                }
            }
            // If no collision and still jumping
            if(obj==null && (stat&JUMP)!=0) {
                move(0,-jump,true);
                if(jump>-power) --jump;
            }
        }

        if((obj=getCollision())!=null) {

            // Check if hit by a monster or projectile:
            for(o1=obj; o1!=null; o1=o1.next)
                if((o1.stat&AGGRO)!=0 && (o1.stat&DEAD)==0) {
                    o1.hit(-1);
                    hit(3);
                    break;
                }

            for(o1=obj; o1!=null; o1=o1.next)
                if((o1.stat&BUFF)!=0)
                    switch(o1.effect) {
                        case 0:
                        default:
                            break;

                        case LIFE:
                            if(life<10) life += o.value;
                            o.delete();
                            break;

                        case AMMO:
                            ammo += o.value;
                            o.delete();
                            break;

                        case GOLD:
                            chests += o.value;
                            o.delete();
                            break;

                        case POWER:
                            power += o.value;
                            if(power<0) power = 0;
                            o.delete();
                            ++buffs[2];
                            break;

                        case SPEED:
                            speed += o.value;
                            if(speed<1) speed = 1;
                            o.delete();
                            ++buffs[1];
                            break;

                        case EXIT:
                            if(chests>=collect) {
                                String nextLevelId = o.getParam("level");
                                if(nextLevelId==null) nextLevelId = game.getLevel().getNextLevelId();
                                if(nextLevelId!=null) {
                                    stat |= PASSIVE;
                                    level.removeObject(this);
                                    game.playSound(SOUND_DOOR);
                                    game.setNextLevelId(nextLevelId);
                                    game.setAction(NEXT_LEVEL,12);
                                }
                            }
                            break;
                    }
        }

        // Cannot move outside level border, to left and right:
        if(solid.x<border.x) move(border.x-solid.x,0,true);
        else if(solid.x+solid.width>border.x+border.width) move((border.x+border.width)-(solid.x+solid.width),0,true);
        // Killed if moves below level border bottom:
        if(solid.y+solid.height>border.height) hit(-2);

        if((stat&DEAD)!=0) n = 3;
        else if((stat&JUMP)!=0) n = 2;
        else if((stat&WALK)!=0) n = 1;
        else  n = 0;
        image.setAnimation(animationIndexByType[n][type],this);

        if((stat&JUMP)!=0 || (stat&WALK)==0) {
            if(footsteps) {
                game.stopSound(SOUND_FOOTSTEPS);
                footsteps = false;
            }
        } else if((stat&WALK)!=0) {
            if(!footsteps) {
                game.loopSound(SOUND_FOOTSTEPS);
                footsteps = true;
            }
        }
    }

    public void setData(PlayerTemplate pd) {
        action = pd.action;
        speed = pd.speed;
        power = pd.power;
        jump = pd.jump;
        chests = pd.chests;
        life += pd.life;
        ammo += pd.ammo;
        buffs = new int[] { 0,0,0,0 };
        stat = pd.stat;
        timer = 0;
        if((stat&FLIP)!=0) flip = true;
        startStat = stat;
    }

    public void shoot(LadybirdGame g) {
        if(ammo==0 || timer>0) return;
        Projectile p = new Projectile(level,solid.x+solid.width/2,solid.y+solid.height/2,LAYERS-1,APPLE.index,this);
        p.setData(0,3,0,1,0,MOVING|(flip? FLIP : 0));
        p.jump = 1;
        level.addObject(p);
        --ammo;
        timer = 3;
        LadybirdGame.getInstance().playSound(SOUND_THROW);
    }

    @Override
    public void transport(int x,int y,int z,boolean g) {
        super.transport(x,y,z,g);
        startX = x;
        startY = y;
        startZ = z;
    }
}
