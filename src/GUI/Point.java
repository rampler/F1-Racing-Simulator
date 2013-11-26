package GUI;

import Enums.Direction;
import Enums.SurfaceType;

public class Point {

	private Point nNeighbor;
	private Point wNeighbor;
	private Point eNeighbor;
	private Point sNeighbor;
	private Direction direction = Direction.TOP;
	private SurfaceType type = SurfaceType.ROAD;
	private boolean isCarCenter = false;
	private int angle = 0;
	
	public Point() {
		
	}

	public void clicked() {
		
	}
}