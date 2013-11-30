package GUI;

public class Track {
	
	private String name;
	private Point[][] points;
	
	/**
	 * Constructor
	 * @param name - name of track
	 * @param points - 2D array of track points
	 */
	public Track(String name, Point[][] points)
	{ 
		this.points = points; 
		this.name = name;
	}	
	
	/**
	 * Get 2D array of track points
	 * @return Points[][] points
	 */
	public Point[][] getPoints(){ return points; }
	
	/**
	 * Get track name
	 * @return String name
	 */
	public String getName(){ return name; }
	
}
