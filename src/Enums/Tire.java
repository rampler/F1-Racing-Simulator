package Enums;

/**
 * Tire types with adhesion
 * @author Sabina Rydzek, Kacper Furma�ski, Mateusz Kotlarz
 *
 */
public enum Tire {
	DRY(1,0.7), WET(0.9,0.8);
	
	private double adhesionOnSame, adhensionOnElse;
	Tire(double adhesionOnSame, double adhensionOnElse)
	{ 
		this.adhesionOnSame = adhesionOnSame; 
		this.adhensionOnElse = adhensionOnElse; 
	}
	
	public double getAdhensionOnSame(){ return adhesionOnSame; }
	public double getAdhensionOnElse(){ return adhensionOnElse; }
}
