package net.spirangle.ladybird;

import static net.spirangle.ladybird.GameScreen.LAYERS;
import static net.spirangle.ladybird.LadybirdGame.SOUND_CREATURE_HIT;

public class Creature extends GameObject {

    public Creature(Level level,int x,int y,int z,int type) {
        super(level,x,y,z,type);
    }

    @Override
    public boolean isCreature() {
        return true;
    }

    @Override
    public boolean hit(int verticalForce) {
        if(isDead()) return false;
        if(--life<=0) {
            life = 0;
            setDead(true);
            z = LAYERS-1;
            if(verticalForce>=-1) jump = verticalForce;
        }
        LadybirdGame.getInstance().playSound(SOUND_CREATURE_HIT);
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
