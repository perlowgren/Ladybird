package net.spirangle.ladybird;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.spirangle.minerva.Rectangle;

public abstract class GameObject extends Anim implements Data {
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
	protected Rectangle space;
	protected Rectangle solid;
	public GameObject grid;
	public GameObject next;

	public GameObject(Main g,Level l,int x,int y,int z,int t) {
		super(imageIndexByType[t]);
		setType(l,t);
		this.x = x;
		this.y = y;
		this.z = z;
		space = new Rectangle();
		solid = new Rectangle();
		grid = null;
		next = null;
		setPosition(x,y);
	}

	public int getX() { return x; }
	public int getY() { return y; }
	public int getZ() { return z; }

	public boolean isStatic() { return (stat&MOBILE)==0 && (stat&MOVING)==0; }

	public boolean isTile() { return false; }
	public boolean isItem() { return false; }
	public boolean isCreature() { return false; }
	public boolean isPlayer() { return false; }
	public boolean isProjectile() { return false; }

	public boolean isSolid() { return (stat&SOLID)!=0; }
	public boolean isMobile() { return (stat&MOBILE)!=0; }
	public boolean isMoving() { return (stat&MOVING)!=0; }
	public boolean isDead() { return (stat&DEAD)!=0; }
	public boolean isJumping() { return (stat&JUMP)!=0; }

	public boolean isFacingLeft() { return flip==true; }
	public boolean isFacingRight() { return flip==false; }

	public void setType(Level l,int t) {
		level = l;
		type = t;
		setAnimation(animationIndexByType[0][type],false);
	}

	public int getType() { return type; }

	public GameObject getCollision() { return getCollision(0,0,0,0,true); }
	public GameObject getCollision(int f) { return getCollision(0,0,f,0,true); }
	public GameObject getCollision(int x,int y) { return getCollision(x,y,0,0,true); }
	public GameObject getCollision(int x,int y,int f) { return getCollision(x,y,f,0,true); }
	public GameObject getCollision(int x,int y,int f,int n) { return getCollision(x,y,f,0,true); }
	public GameObject getCollision(int x,int y,int f,int n,boolean s) {
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

	public void update(Main g) {
		if((stat&DEAD)!=0) {
			move(0,-jump,true);
			if(jump>-8) --jump;
			if(!level.isVisible(space)) delete();
			return;
		}
		if((stat&MOBILE)!=0) {
			GameObject o;
			Rectangle border = new Rectangle(0,0,level.getWidth(),level.getHeight());
			int x = 0,y = 0;
			if((action&LEFT)!=0) {
				flip = true;
				if((o=getCollision(-speed,0,SOLID))!=null || solid.x-speed<=border.x) {
					if(o!=null) {
						for(GameObject o1=o; o1!=null; o1=o1.next)
							if(o1.solid.x+o1.solid.width>o.solid.x+o.solid.width) o = o1;
						x = (o.solid.x+o.solid.width)-solid.x;
					} else x = border.x-solid.x;
					action = (action& ~LEFT)|RIGHT;
				} else x = -speed;
			} else if((action&RIGHT)!=0) {
				flip = false;
				if((o=getCollision(speed,0,SOLID))!=null || solid.x+solid.width+speed>=border.x+border.width) {
					if(o!=null) {
						for(GameObject o1=o; o1!=null; o1=o1.next)
							if(o1.solid.x<o.solid.x) o = o1;
						x = o.solid.x-(solid.x+solid.width);
					} else x = (border.x+border.width)-(solid.x+solid.width);
					action = (action& ~RIGHT)|LEFT;
				} else x = speed;
			} else if((action&UP)!=0) {
				if((o=getCollision(0,-speed,SOLID))!=null || solid.y-speed<=border.y) {
					if(o!=null) {
						for(GameObject o1=o; o1!=null; o1=o1.next)
							if(o1.solid.y+o1.solid.height>o.solid.y+o.solid.height) o = o1;
						y = (o.solid.y+o.solid.height)-solid.y;
					} else y = border.y-solid.y;
					action = (action& ~UP)|DOWN;
				} else y = -speed;
			} else if((action&DOWN)!=0) {
				if((o=getCollision(0,speed,SOLID))!=null || solid.y-solid.height+speed>=border.y+border.height) {
					if(o!=null) {
						for(GameObject o1=o; o1!=null; o1=o1.next)
							if(o1.solid.y<o.solid.y) o = o1;
						y = o.solid.y-(solid.y+solid.height);
					} else y = (border.y+border.height)-(solid.y+solid.height);
					action = (action& ~DOWN)|UP;
				} else y = speed;
			}
			if(x!=0 || y!=0) {
				// Move objects standing on top:
				o = getCollision(0,-1,MOVING,0,false);
				for(; o!=null; o=o.next)
					if(o.solid.y+o.solid.height==solid.y) o.move(x,y,true);

				move(x,y,true);
			}
		}
	}

	public void draw(Main g,GameScreen sc,SpriteBatch batch) {
		if((stat&HIDDEN)==0 && sc.isVisible(space)) {
			draw(batch,x-sc.getX(),y-sc.getY());
//Main.log("GameObject.paint(x="+(space.x-sc.getX())+","+(space.y-sc.getY())+")");
		}
	}

	public void delete() { level.deleteObject(this); }

	public void setData(int a,int sp,int e,int v,int l,int st) {
		action = a;
		speed = sp;
		effect = e;
		value = v;
		life = l;
		stat = st;
		if((stat&FLIP)!=0) flip = true;
	}

	public void setData(int d[]) {
		action = d[0];
		speed = d[1];
		effect = d[2];
		value = d[3];
		life = d[4];
		stat = d[5];
		if((stat&FLIP)!=0) flip = true;
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
