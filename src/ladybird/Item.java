package net.spirangle.ladybird;

public class Item extends GameObject {
	public Item(Main g,Level l,int x,int y,int z,int t) {
		super(g,l,x,y,z,t);
	}

	public boolean isItem() { return true; }
}
