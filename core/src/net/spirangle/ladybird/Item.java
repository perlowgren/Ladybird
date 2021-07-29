package net.spirangle.ladybird;

public class Item extends GameObject {
    public Item(Level l,int x,int y,int z,int t) {
        super(l,x,y,z,t);
    }

    @Override
    public boolean isItem() {
        return true;
    }
}
