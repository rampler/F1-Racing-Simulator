package Exceptions;

import GUI.Car;
import GUI.Point;

/**
 * Exception thrown when car hits barrier
 * Contain broken car
 * @author Sabina Rydzek, Kacper Furma�ski, Mateusz Kotlarz
 *
 */
public class BarrierCrashException extends Exception {

	private static final long serialVersionUID = 1L;
	private Car car;
	private Point point;
	
	public BarrierCrashException(Car car, Point point) {
		this.car = car;
		this.point = point;
	}
	
	public Car getCar(){ return car; }
	public Point getPoint(){ return point; }
}
