package net.spirangle.ladybird;

public class Tile extends GameObject {
	public Tile(Main g,Level l,int x,int y,int z,int t) {
		super(g,l,x,y,z,t);
	}

	public boolean isTile() { return true; }
}
