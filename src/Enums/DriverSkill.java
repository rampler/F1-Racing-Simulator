package Enums;

/**
 * Driver skill enumeration
 * @author Sabina Rydzek, Kacper Furmañski, Mateusz Kotlarz
 *
 */
public enum DriverSkill {
	MONKEY(0,10), NOVICE(1,8), INTERMEDIATE(2,6), PRO(3,4), EXPERT(4,2), MASTER(5,0);
	
	private int num;
	private int randomMistakeParameter;
	
	DriverSkill(int num, int randomMistakeParameter){ this.num = num; this.randomMistakeParameter = randomMistakeParameter; }
	public int getNum(){ return num; }
	public int getRandomMistakeParameter(){ return randomMistakeParameter; }
}
