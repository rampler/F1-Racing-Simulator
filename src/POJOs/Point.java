package POJOs;

import java.util.Arrays;

import Enums.Direction;
import Enums.Dryness;
import Enums.SurfaceType;
import Exceptions.BarrierCrashException;
import Exceptions.CarStackException;
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
	private final int MAX_SPEED = 302;
	private final double KERS_POWER_PARAMETER = 1.1;
	
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
	 * @throws CarsCollisionException - collision on track between two cars
	 * @throws CarStackException - car stack on track
	 * @throws BarrierCrashException - car hits barrier
	 */
	public void nextIteraton(Dryness trackDryness, int timerDelay, double[][] accelerationTable) throws CarsCollisionException, CarStackException, BarrierCrashException
	{
		if(!blocked) 
		{
			//Check collision
			for(Point neighbor : neighbors) if(neighbor.isCarCenter() && neighbor.getCar().getAngle() != car.getAngle()) throw new CarsCollisionException(car, neighbor.getCar(), this, neighbor);
			
			//Calculating distance and speed in m/s
			double speed_m_s = car.getSpeed()*10/36;
			double distance = car.getTempDistance()+speed_m_s/(1000/timerDelay);
			double acc = car.getAcceleration();
			if(car.isKersActivated() && acc > 0) acc *= KERS_POWER_PARAMETER;
			
			/*Decrease KERS iterations - if not activated nothing happens
			  and actualize lap time*/
			car.nextIteration(timerDelay);
			
			//Setting random mistake
			double random = (Math.random()*car.getDriverSkills().getRandomMistakeParameter());
			
			//Calculating new speed - mistake included
			speed_m_s += acc/(1000/timerDelay);
			if(speed_m_s < 0) speed_m_s = 0; 
			double speed_km_h = speed_m_s*36/10;
			if(speed_km_h  <= MAX_SPEED) car.setSpeed(speed_km_h);
			else car.setSpeed(MAX_SPEED-random);
			
			//Stack Exception
			if(car.getSpeed() == 0 && car.getAcceleration() < 0) throw new CarStackException(this.car, this);
			
			//If distance is > 2.6 then send car to next point
			if(distance >= 2.6) 
			{
				distance -= 2.6;
				car.setTempDistance(distance);
				
				//Add lap and KERS Percent
				if(type == SurfaceType.START_LINE) car.addLap();
				if(car.getAcceleration() < 0) car.addKersSystemPercent((int) (-1*car.getAcceleration())/10);
				
				//Calculating new decision - TODO TEST
				Direction carActualDirection = Direction.getDirectionFromAngle(car.getAngle());
				Direction nextDirection = carActualDirection;
				
				Point[][] visibility = car.getVisibility();
//				if(visibility.length == 5) //TOP,LEFT,RIGHT,BOTTOM
//				{
//					double[] medianTable = new double[5];
//					for(int i=0; i<5; i++)
//					{
//						int[] table = new int[2*i+3];
//						for(int j=0; j<2*i+3; j++) table[j] = visibility[i][j].getDirection().getNum();
//						Arrays.sort(table);
//						int k=0;
//						while(k<table.length/2 && table[table.length/2] == -1) k++;
//						medianTable[i] = table[(table.length/2+k+table.length-1)/2]*((5-i)/5);
//					}
//					double num = 0;
//					for(int i=0; i<5; i++) num += medianTable[i];
//					if((int) num == 0) num = direction.getNum();
//					nextDirection = Direction.getDirectionFromNum((int) num);
//				}
//				else //TOP_LEFT,TOP_RIGHT,BOTTOM_LEFT,BOTTOM_RIGHT
//				{
//					double[] medianTable = new double[7];
//					for(int i=0; i<7; i++)
//					{
//						int[] table = new int[i+2];
//						for(int j=0; j<i+2; j++) table[j] = visibility[i][j].getDirection().getNum();
//						Arrays.sort(table);
//						int k=0;
//						while(k<table.length/2 && table[table.length/2] == -1) k++;
//						medianTable[i] = table[(table.length/2+k+table.length-1)/2]*((7-i)/7);
//					}
//					double num = 0;
//					for(int i=0; i<7; i++) num += medianTable[i];
//					nextDirection = Direction.getDirectionFromNum((int) num/7);
//				}
				nextDirection = direction;
				//System.out.println(nextDirection.toString());
				//if(nextDirection == Direction.NONE) nextDirection = direction; //If none then direction is the same as before
				if(nextDirection == Direction.NONE) nextDirection = carActualDirection;
				
				//Calculating acceleration
				double newAcceleration;
				if(nextDirection == Direction.getDirectionFromAngle(car.getAngle()))
				{
					//Calculating new acceleration - gas - mistake included
					if(car.getSpeed() <= 100) newAcceleration = accelerationTable[0][0]-random;
					else if(car.getSpeed() <= 200) newAcceleration = accelerationTable[0][1]-random;
					else newAcceleration = accelerationTable[0][2]-random;
					
					//Activating KERS - //TODO - TEST
					if(car.getKersSystemPercent() == 100)
					{
						boolean roadAhead = true;
						int i=0;
						if(car.getVisibility().length == 5){
							int j=5;
							while(roadAhead && i<j){
								if(car.getVisibility()[i][i+1].getType() != SurfaceType.ROAD) roadAhead = false;
								i++;
							}
						}
						else{
							int j=3;
							while(roadAhead && i<j){
								if(car.getVisibility()[2*i+1][i+1].getType() != SurfaceType.ROAD) roadAhead = false;
								i++;
							}
						}
						if(roadAhead) car.activateKers(timerDelay);
					}
				}
				else if(Math.abs(nextDirection.getNum() - carActualDirection.getNum()) == 1 || Math.abs(nextDirection.getNum() - carActualDirection.getNum()) == 7)
				{
					//Calculating new acceleration - none - mistake included
					if(car.getSpeed() <= 100) newAcceleration = accelerationTable[2][0]+random;
					else if(car.getSpeed() <= 200) newAcceleration = accelerationTable[2][1]+random;
					else newAcceleration = accelerationTable[2][2]+random;
				}
				else
				{
					//Calculating new acceleration - brake - mistake included
					if(car.getSpeed() <= 100) newAcceleration = accelerationTable[1][0]+random;
					else if(car.getSpeed() <= 200) newAcceleration = accelerationTable[1][1]+random;
					else newAcceleration = accelerationTable[1][2]+random;
				}
				if(!trackDryness.toString().equals(car.getTireType().toString())) newAcceleration *= car.getTireType().getAdhensionOnElse();
				else newAcceleration *= car.getTireType().getAdhensionOnSame();
				car.setAcceleration(newAcceleration*trackDryness.getAdhension()-type.getFriction());
				
//				//If speed is too high car can't change direction fast - TODO - TEST
//				if(!(Math.abs(nextDirection.getNum() - carActualDirection.getNum()) <= 1 || Math.abs(nextDirection.getNum() - carActualDirection.getNum()) == 7))
//				{
//					if(car.getSpeed() > 200) nextDirection = carActualDirection; 
//					else if(car.getSpeed() > 100)
//					{
//						if(nextDirection.getNum() < carActualDirection.getNum()) nextDirection = Direction.getDirectionFromNum(carActualDirection.getNum()-1);
//						else if(carActualDirection.getNum() == 0 && nextDirection.getNum() > 5) nextDirection = Direction.getDirectionFromNum(7);
//						else nextDirection = Direction.getDirectionFromNum(carActualDirection.getNum()+1);
//					}
//				}
				
				//Sending car to next point
				if(neighbors[nextDirection.getNum()].getType() == SurfaceType.BARRIER) throw new BarrierCrashException(this.car, this);
				if(neighbors[nextDirection.getNum()].getCar() == null)
				{
					car.setAngle(nextDirection.getAngle());
					neighbors[nextDirection.getNum()].setCar(car);
					car = null;
				}
				else throw new CarsCollisionException(this.car, neighbors[nextDirection.getNum()].getCar(), this, neighbors[nextDirection.getNum()]);
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