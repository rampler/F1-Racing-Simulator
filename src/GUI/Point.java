package GUI;

import Enums.Direction;
import Enums.SurfaceType;

public class Point {

	private Point nNeighbor;
	private Point wNeighbor;
	private Point eNeighbor;
	private Point sNeighbor;
	private Direction direction;
	private SurfaceType type;
	private boolean carCenter;
	private int angle;
	
	public Point() {
		direction = Direction.TOP;
		type = SurfaceType.GRASS;
		carCenter = false;
		angle = 0;
	}
	
	public void clicked()
	{
		type = SurfaceType.ROAD;
	}
	
	public SurfaceType getType(){ return type; }
	public Direction getDirection(){ return direction; }
	public boolean isCarCenter(){ return carCenter; }
	public int getAngle(){ return angle; }
	public void setNeighbors(Point nNeighbor, Point wNeighbor, Point eNeighbor, Point sNeighbor)
	{
		this.nNeighbor = nNeighbor;
		this.wNeighbor = wNeighbor;
		this.eNeighbor = eNeighbor;
		this.sNeighbor = sNeighbor;
	}
}