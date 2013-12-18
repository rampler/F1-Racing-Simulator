package POJOs;

import Enums.Direction;
import Enums.Dryness;
import Enums.SurfaceType;
import Exceptions.CarsCollisionException;

/**
 * Track point.
 * @author Sabina Rydzek, Kacper Furmañski, Mateusz Kotlarz
 *
 */
public class Point {
	
	private Point[] neighbors;
	private Direction direction;
	private SurfaceType type;
	private Car car;
	private int state;
	private boolean blocked;
	
	//Constructors
	public Point() {
		direction = Direction.NONE;
		type = SurfaceType.GRASS;
		state = 1000;
		car = null;
		blocked = false;
	}
	
	/**
	 * Next iteration of simulation
	 * @throws CarsCollisionException 
	 */
	public void nextIteraton(Dryness trackDryness) throws CarsCollisionException
	{
		//TODO - blocking works fine
		if(!blocked) 
		{
			for(Point neighbor : neighbors) if(neighbor.isCarCenter()) throw new CarsCollisionException(car, neighbor.getCar(), this, neighbor);
			//Code below for tests only
			//car.setAngle(45);
			neighbors[Direction.TOP.getNum()].setCar(car);
			car = null;
		}
		else blocked = false;
	}
	
	//Getters
	public SurfaceType getType(){ return type; }
	public Direction getDirection(){ return direction; }
	public int getState(){ return state; }
	public Point[] getNeighbors(){ return neighbors; }
	public Car getCar(){ return car; }
	
	//Is's
	public boolean isCarCenter(){ if(car != null) return true; return false; }
	
	//Setters
	public void setNeighbors(Point[] neighbors){ this.neighbors = neighbors; }
	public void setState(int state){ this.state = state; }
	
	/**
	 * Set Car and block point
	 * @param car
	 */
	public void setCar(Car car){ this.car = car; blocked = true; }
	
	public void setDirection(Direction direction){ this.direction = direction; }
	public void setDirection(String direction){ this.direction = Direction.valueOf(direction); }
	
	public void setType(SurfaceType type){ this.type = type;}
	public void setType(String type){ this.type = SurfaceType.valueOf(type);}
	
	public void unblock(){ blocked = false; }
}