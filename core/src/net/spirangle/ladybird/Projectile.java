package net.spirangle.ladybird;

import java.util.List;

public class Projectile extends GameObject {

    protected GameObject source;

    public Projectile(Level level,int x,int y,int z,int type,GameObject source) {
        super(level,x,y,z,type);
        this.source = source;
    }

    @Override
    public boolean isProjectile() {
        return true;
    }

    @Override
    public void update() {
        List<GameObject> targetList = getCollisions();
        if(targetList!=null) {
            for(GameObject target : targetList) {
                if(target==source) continue;
                if(target.hit(-1)) {
                    source.hitTarget(target);
                    delete();
                    return;
                }
            }
        }
        move(flip? -speed : speed,-jump,true);
        if(jump>-4) --jump;
        if(!level.isVisible(space)) delete();
    }
}
