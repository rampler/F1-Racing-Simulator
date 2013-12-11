package Enums;

/**
 * Track surface types with adhesion parameters
 * @author Sabina Rydzek, Kacper Furmañski, Mateusz Kotlarz
 *
 */
public enum SurfaceType {
	ROAD(1), GRASS(0.3), WORSE_ROAD(0.9), BARRIER(0), START_LINE(1); //TODO calculate parameters
	
	private double adhesion;
	SurfaceType(double adhesion){ this.adhesion = adhesion; }
	
	public double getAdhension(){ return adhesion;}
}
