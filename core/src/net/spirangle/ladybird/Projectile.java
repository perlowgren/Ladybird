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
        List<GameObject> targetList = getCollision();
        if(targetList!=null) {
            for(GameObject target : targetList) {
                if(target==source) continue;
                source.hitTarget(target.hit(-1));
                delete();
                return;
            }
        }
        move(flip? -speed : speed,-jump,true);
        if(jump>-4) --jump;
        if(!level.isVisible(space)) delete();
    }
}
