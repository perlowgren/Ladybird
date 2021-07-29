package net.spirangle.ladybird;

public class Item extends GameObject {
    public Item(Level level,int x,int y,int z,int type) {
        super(level,x,y,z,type);
    }

    @Override
    public boolean isItem() {
        return true;
    }

    @Override
    public boolean hit(int verticalForce) {
        return false;
    }
}
