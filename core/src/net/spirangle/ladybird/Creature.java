package net.spirangle.ladybird;

public class Creature extends GameObject {

    public Creature(Level l,int x,int y,int z,int t) {
        super(l,x,y,z,t);
    }

    @Override
    public boolean isCreature() {
        return true;
    }

    @Override
    public void update() {
        int n;
        if((stat&DEAD)!=0) n = 3;
        else if((stat&MOBILE)!=0) n = 1;
        else n = 0;
        image.setAnimation(animationIndexByType[n][type],this);
        super.update();
    }
}
