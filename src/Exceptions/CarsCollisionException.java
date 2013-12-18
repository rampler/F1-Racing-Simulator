package Exceptions;

import POJOs.Car;
import POJOs.Point;

/**
 * Exception thrown when there were collisions on track
 * Contain two broken cars
 * @author Sabina Rydzek, Kacper Furmañski, Mateusz Kotlarz
 *
 */
public class CarsCollisionException extends Exception {

	private static final long serialVersionUID = 1L;
	private Car car, car2;
	
	public CarsCollisionException(Car car, Car car2, Point point, Point neighbor)
	{
		this.car = car;
		this.car2 = car2;
		point.setCar(null);
		neighbor.setCar(null);
	}
	
	public Car getCar1(){ return car; }
	public Car getCar2(){ return car2; }
}
