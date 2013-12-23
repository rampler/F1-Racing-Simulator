package Enums;

/**
 * Direction contain unique representation number.
 * @author Sabina Rydzek, Kacper Furmañski, Mateusz Kotlarz
 *
 */
public enum Direction {
	NONE(-1,0), TOP_LEFT(0,315), TOP(1,0), TOP_RIGHT(2,45), LEFT(3,270), RIGHT(4,90), BOTTOM_LEFT(5,225), BOTTOM(6,180), BOTTOM_RIGHT(7,135);
	
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
}
