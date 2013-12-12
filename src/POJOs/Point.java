package POJOs;

import Enums.Direction;
import Enums.SurfaceType;

/**
 * Track point.
 * @author Sabina Rydzek, Kacper Furma�ski, Mateusz Kotlarz
 *
 */
public class Point {
	
	private Point[] neighbors;
	private Direction direction;
	private SurfaceType type;
	private Car car;
	private int angle;
	private int state;
	
	//Constructors
	public Point() {
		direction = Direction.NONE;
		type = SurfaceType.GRASS;
		angle = 0;
		state = 1000;
		car = null;
	}
	
	/**
	 * Next iteration of simulation
	 */
	public void nextIteraton()
	{
		//TODO
	}
	
	//Getters
	public SurfaceType getType(){ return type; }
	public Direction getDirection(){ return direction; }
	public int getAngle(){ return angle; }
	public int getState(){ return state; }
	public Point[] getNeighbors(){ return neighbors; }
	
	//Is's
	public boolean isCarCenter(){ if(car != null) return true; return false; }
	
	//Setters
	public void setNeighbors(Point[] neighbors){ this.neighbors = neighbors; }
	public void setState(int state){ this.state = state; }
	public void setCar(Car car){ this.car = car; }
	public void setAngle(int angle){ this.angle = angle; }
	
	public void setDirection(Direction direction){ this.direction = direction; }
	public void setDirection(String direction){ this.direction = Direction.valueOf(direction); }
	
	public void setType(SurfaceType type){ this.type = type;}
	public void setType(String type){ this.type = SurfaceType.valueOf(type);}
}