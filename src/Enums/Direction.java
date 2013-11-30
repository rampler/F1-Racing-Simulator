package Enums;

public enum Direction {
	TOP_LEFT(0), TOP(1), TOP_RIGHT(2), LEFT(3), RIGHT(4), BOTTOM_LEFT(5), BOTTOM(6), BOTTOM_RIGHT(7), NONE(-1);
	
	private int num;
	
	Direction(int num){ this.num = num; }
	public int getNum(){ return num; }
}
