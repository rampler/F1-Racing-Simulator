package POJOs;

import Enums.DriverSkill;
import Enums.Tire;

/**
 * Car class contains all car parameters.
 * @author Sabina Rydzek, Kacper Furma�ski, Mateusz Kotlarz
 *
 */
public class Car {

	private final String DRIVER_NAME;
	private final int POS_START_X, POS_START_Y;
	private final DriverSkill DRIVER_SKILL;
	private double speed;
	private double accelerate;
	private int kersSystemPercent;
	private Tire tireType;
	private int laps;
	private int angle;
	private int number;
	private Point[][] visibility;
	private double tempDistance;
	
	/**
	 * Setting up initial parameters
	 * @param driverName
	 * @param driverSkill
	 * @param posStartX
	 * @param posStartY
	 */
	public Car(String driverName, DriverSkill driverSkill, int posStartX, int posStartY, int number)
	{
		DRIVER_NAME = driverName;
		DRIVER_SKILL = driverSkill;
		POS_START_X  = posStartX;
		POS_START_Y = posStartY;
		this.number = number;
		tireType = Tire.DRY;
		kersSystemPercent = 0;
		speed = 0;
		accelerate = 16.5;
		laps = 0;
		angle = 0;
		tempDistance = 0;
	}
	
	//Getters
	public double getSpeed(){ return speed; }
	public double getAccelerate(){ return accelerate; }
	public int getKersSystemPercent(){ return kersSystemPercent; }
	public int getLaps(){ return laps; }
	public Tire getTireType(){ return tireType; }
	public DriverSkill getDriverSkills(){ return DRIVER_SKILL; }
	public String getDriverName(){ return DRIVER_NAME; }
	public int getPosStartX(){ return POS_START_X; }
	public int getPosStartY(){ return POS_START_Y; }
	public int getAngle(){ return angle; }
	public int getNumber(){ return number; }
	public Point[][] getVisibility(){ return visibility; }
	public double getTempDistance(){ return tempDistance; }
	
	//Setters
	public void setSpeed(double speed){ this.speed = speed; }
	public void setAccelerate(double accelerate){ this.accelerate = accelerate; }
	public void setTireType(Tire tireType){ this.tireType = tireType; }
	public void setAngle(int angle){ this.angle = angle; }
	public void setVisibility(Point[][] visibility){ this.visibility = visibility; }
	public void setTempDistance(double tempDistance){ this.tempDistance = tempDistance; }
	
	/**
	 * Add percent to KERS System Charging
	 * @param percent
	 */
	public void addKersSystemPercent(int percent){ 
		if(this.kersSystemPercent+percent < 100) this.kersSystemPercent += percent; 
		else this.kersSystemPercent = 100;
	}
	public void addLap(){ this.laps++; }
}
