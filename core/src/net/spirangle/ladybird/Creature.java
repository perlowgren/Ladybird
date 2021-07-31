package net.spirangle.ladybird;

import static net.spirangle.ladybird.GameScreen.LAYERS;
import static net.spirangle.ladybird.LadybirdGame.SOUND_CREATURE_DEAD;
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
    public boolean hit(int xForce,int yForce) {
        if(isDead()) return false;
        LadybirdGame.getInstance().playSound(SOUND_CREATURE_HIT);
        if(--life<=0) {
            life = 0;
            setDead(true);
            z = LAYERS-1;
            speed = xForce;
            jump = yForce;
            LadybirdGame.getInstance().playSound(SOUND_CREATURE_DEAD);
        }
        return true;
    }

    @Override
    public void update() {
        int n;
        if(isDead()) n = 3;
        else if(isMobile()) n = 1;
        else n = 0;
        image.setAnimation(animationIndexByType[n][type],this);
        super.update();
    }
}
