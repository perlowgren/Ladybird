package net.spirangle.ladybird;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.spirangle.minerva.Point;
import net.spirangle.minerva.Rectangle;

public class GameImage {
    private static final int N = 13;

    private static class Frame {
        int time;
        Rectangle clip;
        Point center;
        Rectangle solid;
        Point flip;
    }

    private final Texture texture;
    private final Frame[] frames;
    private final int[] anims;

    public GameImage(Texture texture,int[] frameData,int[] anims) {
        this.texture = texture;
        this.frames = new Frame[frameData.length/N];
        for(int i=0,n=0; n<frameData.length; ++i,n+=N) {
            int w = frameData[n+3];
            int h = frameData[n+4];
            int sx = frameData[n+7];
            int sy = frameData[n+8];
            int sw = frameData[n+9];
            int sh = frameData[n+10];
            Frame frame = new Frame();
            frame.time = frameData[n];
            frame.clip = new Rectangle(frameData[n+1],frameData[n+2],w,h);
            frame.center = new Point(frameData[n+5],frameData[n+6]);
            frame.solid = sx==-1 || sy==-1? null : new Rectangle(sx,sy,sw==0? w : sw,sh==0? h : sh);
            frame.flip = new Point(frameData[n+11],frameData[n+12]);
            this.frames[i] = frame;
        }
        this.anims = new int[anims.length];
        System.arraycopy(anims,0,this.anims,0,anims.length);
    }

    public int getWidth(Anim a) {
        return frames[a.frame].clip.width;
    }

    public int getHeight(Anim a) {
        return frames[a.frame].clip.height;
    }

    public int getCenterX(Anim a) {
        return frames[a.frame].center.x;
    }

    public int getCenterY(Anim a) {
        return frames[a.frame].center.y;
    }

    public Rectangle getSolid(Anim a) {
        return frames[a.frame].solid;
    }

    public void draw(SpriteBatch batch,int x,int y,Anim a) {
        int fx = 0,fy = 0;
        if(a.timer<=0) {
            ++a.frame;
            if(a.frame>=frames.length || a.frame==anims[a.anim+1]) a.frame = anims[a.anim];
            a.timer = frames[a.frame].time;
        }
        Rectangle f = frames[a.frame].clip;
        if(a.flip) {
            fx = frames[a.frame].flip.x;
            fy = frames[a.frame].flip.y;
        }
        batch.draw(texture,
                   x,y-f.height,f.width,f.height,
                   f.x+fx,f.y+fy,f.width,f.height,
                   false,true);
//		Point c = centers[a.frame];
//		batch.draw(texture,(float)(x-c.x),(float)(y-f.height+c.y),(float)f.width,(float)f.height,f.x+fx,f.y+fy,f.width,f.height,false,true);
        --a.timer;
    }

    public void setAnimation(int n,Anim a) {
        n = n<0 || n>=anims.length-1? 0 : n;
        if(n==a.anim) return;
        a.anim = n;
        a.frame = anims[a.anim];
        a.timer = frames[a.frame].time;
    }
}
