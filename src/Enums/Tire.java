package Enums;

/**
 * Tire types with adhesion
 * @author Sabina Rydzek, Kacper Furmañski, Mateusz Kotlarz
 *
 */
public enum Tire {
	DRY(1), WET(0.8);
	
	private double adhesion;
	Tire(double adhesion){ this.adhesion = adhesion; }
	
	public double getAdhension(){ return adhesion;}
}
