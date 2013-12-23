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
	public void nextIteraton(Dryness trackDryness, int timerDelay, double[][] accelerationTable) throws CarsCollisionException
	{
		if(!blocked) 
		{
			for(Point neighbor : neighbors) if(neighbor.isCarCenter() && neighbor.getCar().getAngle() != car.getAngle()) throw new CarsCollisionException(car, neighbor.getCar(), this, neighbor);
			
			//Calculating distance and speed in m/s
			double speed_m_s = car.getSpeed()*10/36;
			double distance = car.getTempDistance()+speed_m_s/(1000/timerDelay);
			double acc = car.getAcceleration();
			
			//Setting random mistake
			double random = (Math.random()*car.getDriverSkills().getRandomMistakeParameter());
			
			//Calculating new speed - mistake included
			speed_m_s += acc/(1000/timerDelay);
			if(speed_m_s < 0) speed_m_s = 0; 
			double speed_km_h = speed_m_s*36/10;
			if(speed_km_h  <= 300) car.setSpeed(speed_km_h);
			else car.setSpeed(300-random);
			
			//If distance is > 2.6 then send car to next point
			if(distance >= 2.6) 
			{
				distance -= 2.6;
				car.setTempDistance(distance);
				
				//Add lap
				if(type == SurfaceType.START_LINE) car.addLap();
				
			//Calculating new decision
				Direction carActualDirection = Direction.getDirectionFromAngle(car.getAngle());
				Direction nextDirection = carActualDirection;
				//TODO Calculating Next Direction
				
				if(nextDirection == Direction.getDirectionFromAngle(car.getAngle()))
				{
					//Calculating new acceleration - gas - mistake included
					if(car.getSpeed() <= 100) car.setAcceleration(accelerationTable[0][0]-random);
					else if(car.getSpeed() <= 200) car.setAcceleration(accelerationTable[0][1]-random);
					else if(car.getSpeed() <= 300) car.setAcceleration(accelerationTable[0][2]-random);
				}
				else if(nextDirection.getNum() - carActualDirection.getNum() == 1)
				{
					//Calculating new acceleration - none - mistake included
					if(car.getSpeed() <= 100) car.setAcceleration(accelerationTable[2][0]+random);
					else if(car.getSpeed() <= 200) car.setAcceleration(accelerationTable[2][1]+random);
					else if(car.getSpeed() <= 300) car.setAcceleration(accelerationTable[2][2]+random);
				}
				else
				{
					//Calculating new acceleration - brake - mistake included
					if(car.getSpeed() <= 100) car.setAcceleration(accelerationTable[1][0]+random);
					else if(car.getSpeed() <= 200) car.setAcceleration(accelerationTable[1][1]+random);
					else if(car.getSpeed() <= 300) car.setAcceleration(accelerationTable[1][2]+random);
				}
				

				
				//Code below for tests only
				car.setAngle(45);
				if(neighbors[Direction.RIGHT.getNum()].getCar() == null)
				{
						neighbors[Direction.RIGHT.getNum()].setCar(car);
						car = null;
				}
				else throw new CarsCollisionException(this.car, neighbors[Direction.RIGHT.getNum()].getCar(), this, neighbors[Direction.RIGHT.getNum()]);
			}
			else car.setTempDistance(distance);
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