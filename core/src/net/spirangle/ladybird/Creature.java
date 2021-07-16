package net.spirangle.ladybird;

public class Creature extends GameObject {

	public Creature(Main g,Level l,int x,int y,int z,int t) {
		super(g,l,x,y,z,t);
	}

	public boolean isCreature() { return true; }

	public void update(Main g) {
		int n;
		if((stat&DEAD)!=0) n = 3;
		else if((stat&MOBILE)!=0) n = 1;
		else n = 0;
		image.setAnimation(animationIndexByType[n][type],this);
		super.update(g);
	}
}
