package net.spirangle.ladybird;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.spirangle.minerva.Point;
import net.spirangle.minerva.Rectangle;

public class GameImage {
    private static final int N = 13;

    private final Texture texture;
    private final int[] times;
    private final Rectangle[] frames;
    private final Point[] centers;
    private final Rectangle[] solids;
    private final int[] anims;
    private final Point[] flip;

    public GameImage(Texture t) {
        this(t,null,null);
    }

    public GameImage(Texture t,int[] f,int[] a) {
        int i,n = f.length/N,x,y,w,h,cx,cy,sx,sy,sw,sh,fx,fy;
        texture = t;
        times = new int[n];
        frames = new Rectangle[n];
        centers = new Point[n];
        solids = new Rectangle[n];
        flip = new Point[n];
        for(i=0,n=0; n<f.length; ++i,n+=N) {
            times[i] = f[n];
            x = f[n+1];
            y = f[n+2];
            w = f[n+3];
            h = f[n+4];
            cx = f[n+5];
            cy = f[n+6];
            sx = f[n+7];
            sy = f[n+8];
            sw = f[n+9];
            sh = f[n+10];
            fx = f[n+11];
            fy = f[n+12];
            frames[i] = new Rectangle(x,y,w,h);
            centers[i] = new Point(cx,cy);
            solids[i] = sx==-1 || sy==-1? null : new Rectangle(sx,sy,sw==0? w : sw,sh==0? h : sh);
            flip[i] = new Point(fx,fy);
        }
        anims = new int[a.length+1];
        for(i=0; i<=a.length; ++i)
            if(i==a.length) anims[i] = frames.length;
            else anims[i] = a[i];
    }

    public int getWidth(Anim a) {
        return frames[a.frame].width;
    }

    public int getHeight(Anim a) {
        return frames[a.frame].height;
    }

    public int getCenterX(Anim a) {
        return centers[a.frame].x;
    }

    public int getCenterY(Anim a) {
        return centers[a.frame].y;
    }

    public Rectangle getSolid(Anim a) {
        return solids[a.frame];
    }

    public void draw(SpriteBatch batch,int x,int y,Anim a) {
        int fx = 0,fy = 0;
        if(a.timer<=0) {
            ++a.frame;
            if(a.frame>=frames.length || a.frame==anims[a.anim+1]) a.frame = anims[a.anim];
            a.timer = times[a.frame];
        }
        Rectangle f = frames[a.frame];
        if(a.flip) {
            fx = flip[a.frame].x;
            fy = flip[a.frame].y;
        }
        batch.draw(texture,(float)x,(float)(y-f.height),(float)f.width,(float)f.height,f.x+fx,f.y+fy,f.width,f.height,false,true);
//		Point c = centers[a.frame];
//		batch.draw(texture,(float)(x-c.x),(float)(y-f.height+c.y),(float)f.width,(float)f.height,f.x+fx,f.y+fy,f.width,f.height,false,true);
        --a.timer;
    }

    public void setAnimation(int n,Anim a) {
        n = n<0 || n>=anims.length-1? 0 : n;
        if(n==a.anim) return;
        a.anim = n;
        a.frame = anims[a.anim];
        a.timer = times[a.frame];
    }
}
