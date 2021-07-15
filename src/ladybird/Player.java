package net.spirangle.ladybird;

import net.spirangle.minerva.Rectangle;

public class Player extends GameObject {
	public static boolean keys[] = new boolean[6];

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

	public Player(Main g,Level l,int x,int y,int z,int t) {
		super(g,l,x,y,z,t);
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
	}

	@Override
	public boolean isPlayer() { return true; }

	public int getHearts() { return life; }
	public int getApples() { return ammo; }
	public int getChests() { return chests; }
	public void setCollect(int n) { collect = n; }
	public int getCollect() { return collect; }
	public int getBuffs(int n) { return n>=0 && n<4? buffs[n] : 0; }
	public boolean gameOver() { return life==0; }

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
	public void update(Main g) {
		Rectangle border = new Rectangle(0,0,level.getWidth(),level.getHeight());
		int n;
		GameObject to = null,to1;

		++frames;
		if(timer>0) --timer;

		if((stat&PASSIVE)!=0) return;

		if((stat&DEAD)!=0) {
			if(life>0) {
				if(!g.getGameScreen().isVisible(space)) {
					--life;
					if(life>0) {
						transport(startX,startY,startZ,true);
						stat = startStat;
					} else {
						stat |= PASSIVE;
						level.removeObject(this);
						g.showMessage(Main.str.get("gameOver"),40,START_GAME);
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

		if(keys[5]) shoot(g);

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
			g.playSound(SOUND_JUMP);
		}

		// Check if standing on solid ground, otherwise flag jumping:
		if((stat&JUMP)==0 && getCollision(0,1,SOLID,0,false)==null) stat |= JUMP;

		// Handle jumping:
		if((stat&JUMP)!=0) {
			if(jump>0) { // Collision detection for jumping up:
				if((to=getCollision(0,-jump,SOLID))!=null) {
					for(to1=to,to=null; to1!=null; to1=to1.next)
						if((to1.stat&MOBILE)==0 && // For unmoving solid objects
							to1.solid.y+to1.solid.height>=solid.y-jump && to1.solid.y+to1.solid.height<=solid.y && // Only collision if above, not if already within bounds
							(to==null || to1.solid.y+to1.solid.height>to.solid.y+to.solid.height) // Find highest GameObject
						) to = to1;
					if(to!=null) {
						move(0,(to.solid.y+to.solid.height)-solid.y,true);
						jump = 0; // Stop upward jump motion
						--jump; // Next cycle begin falling down (otherwise two cycles with jump==0)
					}
				}
			}
			if(jump<=0) { // Collision detection for falling down:
				if((to=getCollision(0,-jump,SOLID))!=null) {
					for(to1=to,to=null; to1!=null; to1=to1.next)
						if(
							to1.solid.y>=solid.y+solid.height &&
							(to==null || to1.solid.y<to.solid.y)
						) to = to1;
					if(to!=null) {
						move(0,to.solid.y-(solid.y+solid.height),true);
						stat &= ~JUMP;
						jump = 0;
					}
				}
			}
			// If no collision and still jumping 
			if(to==null && (stat&JUMP)!=0) {
				move(0,-jump,true);
				if(jump>-power) --jump;
			}
		}

		if((to=getCollision())!=null) {

			// Check if hit by a monster or projectile:
			for(to1=to; to1!=null; to1=to1.next)
				if((to1.stat&AGGRO)!=0 && (to1.stat&DEAD)==0) {
					to1.hit(-1);
					hit(3);
					break;
				}

			for(to1=to; to1!=null; to1=to1.next)
				if((to1.stat&BUFF)!=0)
					switch(to.effect) {
						case 0:
						default:
						break;

						case LIFE:
							if(life<10) life += to.value;
							to1.delete();
							break;

						case AMMO:
							ammo += to.value;
							to1.delete();
							break;

						case GOLD:
							chests += to.value;
							to1.delete();
							break;

						case POWER:
							power += to.value;
							if(power<0) power = 0;
							to1.delete();
							++buffs[2];
							break;

						case SPEED:
							speed += to.value;
							if(speed<1) speed = 1;
							to1.delete();
							++buffs[1];
							break;

						case EXIT:
							if(chests>=collect) {
								stat |= PASSIVE;
								level.removeObject(this);
								g.playSound(SOUND_DOOR);
								g.setAction(NEXT_LEVEL,12);
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
	}

	@Override
	public void setData(int d[]) {
		action = d[0];
		speed = d[1];
		power = d[2];
		jump = d[3];
		chests = d[4];
		life += d[5];
		ammo += d[6];
		buffs = new int[] { 0,0,0,0 };
		stat = d[7];
		timer = 0;
		if((stat&FLIP)!=0) flip = true;
		startStat = stat;
	}

	public void shoot(Main g) {
		if(ammo==0 || timer>0) return;
		Projectile p = new Projectile(g,level,solid.x+solid.width/2,solid.y+solid.height/2,LAYERS-1,APPLE);
		p.setData(0,3,0,1,0,MOVING|(flip? FLIP : 0));
		p.jump = 1;
		level.addObject(p);
		--ammo;
		timer = 3;
	}

	@Override
	public void transport(int x,int y,int z,boolean g) {
		super.transport(x,y,z,g);
		startX = x;
		startY = y;
		startZ = z;
	}
}
