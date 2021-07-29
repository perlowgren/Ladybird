package net.spirangle.ladybird;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.spirangle.minerva.Rectangle;

public class Anim {
    public GameImage image;
    public int timer;
    public int frame;
    public int anim;
    public boolean flip;

    public Anim(int imageId) {
        this.image = LadybirdGame.getInstance().getImage(imageId);
        this.timer = 0;
        this.frame = 0;
        this.anim = 0;
        this.flip = false;
    }

    public void draw(SpriteBatch batch,int x,int y) {
        image.draw(batch,x,y,this);
    }

    public int getWidth() {
        return image.getWidth(this);
    }

    public int getHeight() {
        return image.getHeight(this);
    }

    public int getCenterX() {
        return image.getCenterX(this);
    }

    public int getCenterY() {
        return image.getCenterY(this);
    }

    public Rectangle getSolid() {
        return image.getSolid(this);
    }

    public void setAnimation(int n,boolean f) {
        image.setAnimation(n,this);
        flip = f;
    }
}
