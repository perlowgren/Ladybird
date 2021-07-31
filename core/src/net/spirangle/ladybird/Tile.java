package net.spirangle.ladybird;

public class Tile extends GameObject {
    public Tile(Level l,int x,int y,int z,int t) {
        super(l,x,y,z,t);
    }

    @Override
    public boolean isTile() {
        return true;
    }
}
