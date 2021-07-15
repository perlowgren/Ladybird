package net.spirangle.ladybird;

public class Projectile extends GameObject {
	public Projectile(Main g,Level l,int x,int y,int z,int t) {
		super(g,l,x,y,z,t);
	}

	public boolean isProjectile() { return true; }

	public void update(Main g) {
		GameObject to = getCollision();
		Player player = g.getPlayer();
		for(; to!=null; to=to.next) {
			if(to==player) continue;
			player.hitTarget(to.hit(-1));
			delete();
			return;
		}

		move(flip ? -speed : speed,-jump,true);
		if(jump>-4) --jump;
		if(!level.isVisible(space)) delete();
	}
}
