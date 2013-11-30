package GUI;

import Enums.Direction;
import Enums.SurfaceType;

public class Point {
	
	private Point[] neighbors;
	private Direction direction;
	private SurfaceType type;
	private boolean carCenter;
	private int angle;
	private int state;
	
	public Point() {
		direction = Direction.NONE;
		type = SurfaceType.GRASS;
		carCenter = false;
		angle = 0;
		state = 1000;
	}
	
	public Point(SurfaceType type, Direction direction, int state, int angle, boolean carCenter, Point[] neighbors) {
		this.direction = direction;
		this.type = type;
		this.carCenter = carCenter;
		this.angle = angle;
		this.state = state;
		this.neighbors = neighbors;
	}
	
	//To erase or change
	public void clicked()
	{
		type = SurfaceType.ROAD;
	}
	
	public SurfaceType getType(){ return type; }
	public Direction getDirection(){ return direction; }
	public int getAngle(){ return angle; }
	public int getState(){ return state; }
	public Point[] getNeighbors(){ return neighbors; }
	
	public boolean isCarCenter(){ return carCenter; }
	
	public void setNeighbors(Point[] neighbors){ this.neighbors = neighbors; }
	public void setState(int state){ this.state = state; }
	public void setCarCenter(boolean carCenter){ this.carCenter = carCenter; }
	public void setAngle(int angle){ this.angle = angle; }
	
	public void setDirection(Direction direction){ this.direction = direction; }
	public void setDirection(String direction){ this.direction = Direction.valueOf(direction); }
	
	public void setType(SurfaceType type){ this.type = type;}
	public void setType(String type){ this.type = SurfaceType.valueOf(type);}
}