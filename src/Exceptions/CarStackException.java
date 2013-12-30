package Exceptions;

import GUI.Car;
import GUI.Point;

/**
 * Exception thrown when car stack (speed=0 and acceleration < 0)
 * Contain stacked car
 * @author Sabina Rydzek, Kacper Furmañski, Mateusz Kotlarz
 *
 */
public class CarStackException extends Exception {

	private static final long serialVersionUID = 1L;
	private Car car;
	private Point point;
	
	public CarStackException(Car car, Point point) { 
		this.car = car;
		this.point = point;
	}
	
	public Car getCar(){ return car; }
	public Point getPoint(){ return point; }
}
