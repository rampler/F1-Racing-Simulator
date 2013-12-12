package Enums;

/**
 * Driver skill enumeration
 * @author Sabina Rydzek, Kacper Furma�ski, Mateusz Kotlarz
 *
 */
public enum DriverSkill {
	MONKEY(0), NOVICE(1), INTERMEDIATE(2), PRO(3), EXPERT(4), MASTER(5);
	
	private int num;
	
	DriverSkill(int num){ this.num = num; }
	public int getNum(){ return num; }
}
