package Enums;

/**
 * Direction contain unique representation number.
 * @author Sabina Rydzek, Kacper Furmañski, Mateusz Kotlarz
 *
 */
public enum Direction {
	NONE(-1,0), TOP_LEFT(0,315), TOP(1,0), TOP_RIGHT(2,45), LEFT(7,270), RIGHT(3,90), BOTTOM_LEFT(6,225), BOTTOM(5,180), BOTTOM_RIGHT(4,135);
	
	private int num;
	private int angle;
	
	Direction(int num, int angle){ this.num = num; this.angle = angle;}
	public int getNum(){ return num; }
	public int getAngle(){ return angle; }
	public static Direction getDirectionFromAngle(int angle)
	{
		if(angle < 45) return TOP;
		if(angle < 90) return TOP_RIGHT;
		if(angle < 135) return RIGHT;
		if(angle < 180) return BOTTOM_RIGHT;
		if(angle < 225) return BOTTOM;
		if(angle < 270) return BOTTOM_LEFT;
		if(angle < 315) return LEFT;
		if(angle < 360) return TOP_LEFT;
		return NONE;
	}
	public static Direction getDirectionFromNum(int num)
	{
		if(num == 0) return TOP_LEFT;
		if(num == 1) return TOP;
		if(num == 2) return TOP_RIGHT;
		if(num == 3) return RIGHT;
		if(num == 4) return BOTTOM_RIGHT;
		if(num == 5) return BOTTOM;
		if(num == 6) return BOTTOM_LEFT;
		if(num == 7) return LEFT;
		return NONE;
	}
}
