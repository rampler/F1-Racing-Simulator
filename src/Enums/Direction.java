package Enums;

import java.io.Serializable;

public enum Direction implements Serializable{
	TOP_LEFT(0), TOP(1), TOP_RIGHT(2), LEFT(3), RIGHT(4), BOTTOM_LEFT(5), BOTTOM(6), BOTTOM_RIGHT(7);
	
	private int num;
	
	Direction(int num){ this.num = num; }
	public int getNum(){ return num; }
}
