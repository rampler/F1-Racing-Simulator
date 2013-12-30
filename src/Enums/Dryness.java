package Enums;

/**
 * Track dryness
 * @author Sabina Rydzek, Kacper Furma�ski, Mateusz Kotlarz
 *
 */
public enum Dryness {
	DRY(1), WET(0.8);
	
	private double adhesion;
	Dryness(double adhesion){ this.adhesion = adhesion; }
	
	public double getAdhension(){ return adhesion;}
}
