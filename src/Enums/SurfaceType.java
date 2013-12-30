package Enums;

/**
 * Track surface types with adhesion parameters
 * @author Sabina Rydzek, Kacper Furma�ski, Mateusz Kotlarz
 *
 */
public enum SurfaceType {
	ROAD(0), GRASS(5), WORSE_ROAD(0.5), BARRIER(100), START_LINE(0), SAND(18);
	
	private double friction; //Decreased from acceleration
	SurfaceType(double friction){ this.friction = friction; }
	
	public double getFriction(){ return friction;}
}
