package net.spirangle.minerva.path;

public abstract class IsometricMap implements PathMap {
	private static final short[][] xcoords = {{  0, 0,-1,-1 },{   1, 1, 0, 0 }};
	private static final short[] ycoords = { -1, 1, 1,-1 };

	public int getMapDir(Path p,PathPoint c1,PathPoint c2) {
		PathPoint p1 = new PathPoint(c1),p2 = new PathPoint(c2);
		p.adjustDir(p1,p2);
		return p2.y<p1.y? (p2.x<p1.x+(p1.y&1)? Path.NW : Path.NE) : (p2.x<p1.x+(p1.y&1)? Path.SW : Path.SE);
	}

	public int getMapHeuristic(Path p,PathPoint c) {
		PathPoint p1 = new PathPoint(c),p2 = p.getDestination();
		p.adjustDir(p1,p2);
	//	x = abs(x-dx)/2,y = abs(y-dy);
	//	return x>y? x+y/2 : y+x/2;
		return p1.isometricDistance(p2);
	}

	public int movePathPoint(Path p,PathNode n,PathPoint c,int i) {
		c.set(n.x+xcoords[n.y&1][i],n.y+ycoords[i]);
		p.adjustMove(c);
		return i<3? 1 : -1;
	}
}

