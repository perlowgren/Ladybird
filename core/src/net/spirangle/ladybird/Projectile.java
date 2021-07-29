package net.spirangle.ladybird;

public class Projectile extends GameObject {
    public Projectile(Level l,int x,int y,int z,int t) {
        super(l,x,y,z,t);
    }

    @Override
    public boolean isProjectile() {
        return true;
    }

    @Override
    public void update() {
        GameObject to = getCollision();
        Player player = LadybirdGame.getInstance().getPlayer();
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
