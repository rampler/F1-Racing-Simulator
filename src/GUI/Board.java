package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.MatchResult;

import javax.swing.JPanel;

import Enums.Direction;
import Enums.DriverSkill;
import Enums.Dryness;
import Enums.SurfaceType;
import Enums.Tire;
import Exceptions.FileFormatException;
import POJOs.Car;
import POJOs.Point;
import POJOs.Track;

/**
 * Main component containing table of track points, drawing this table and initialize simulation.
 * @author Sabina Rydzek, Kacper Furma�ski, Mateusz Kotlarz
 *
 */
public class Board extends JPanel{
	private static final long serialVersionUID = 1L;
	private Point[][] points;
	private Track track;
	private final int SIZE = 4;
	private final int SCREEN_WIDTH, SCREEN_HEIGHT;
	private Dryness trackDryness;
	private LinkedList<Car> cars;

	/**
	 * Constructor gets screen size and setting background color
	 * @param screenWidth
	 * @param screenHeight
	 */
	public Board(int screenWidth, int screenHeight) {
		this.SCREEN_HEIGHT = screenHeight;
		this.SCREEN_WIDTH = screenWidth;
		setBackground(Color.DARK_GRAY);
		cars = new LinkedList<Car>();
		trackDryness = Dryness.DRY;
	}

	public Track getTrack(){ return track; }
	public void setTrack(Track track){ this.track = track; points = track.getPoints(); }
	public void setTrackDryness(Dryness dryness){ trackDryness = dryness; }
	
	/**
	 * Changing tires type of all cars
	 * @param tire
	 */
	public void changeCarsTires(Tire tire)
	{
		for(Car car : cars) 
			car.setTireType(tire);
	}
	
	/**
	 * Loading Track from file
	 * @param file
	 * @throws FileNotFoundException - file not found
	 * @throws FileFormatException - regex doesn't match
	 */
	public void loadTrack(File file) throws FileNotFoundException, FileFormatException
	{
		//Loading Track parameters
		Scanner in = new Scanner(file);
		in.findInLine("(\\w+);(\\d+);(\\d+);(\\d+)");
		try
		{ 
			MatchResult result = in.match();
			Point[][] points = new Point[Integer.parseInt(result.group(2))][Integer.parseInt(result.group(3))];
			String trackName = result.group(1);
			int driversCount = Integer.parseInt(result.group(4));
			
			//Loading points
			for(int x=0; x<points.length; x++)
				for(int y=0; y<points[x].length; y++)
				{
					in.nextLine();
					in.findInLine("(\\w+);(\\w+)");
					result = in.match();
					
					points[x][y] = new Point();
					points[x][y].setType(result.group(1));
					points[x][y].setDirection(result.group(2));
				}
			
			//Loading drivers
			for(int x=0; x<driversCount; x++)
			{
				in.nextLine();
				in.findInLine("(\\w+);(\\w+);(\\d+);(\\d+)");
				result = in.match();
				cars.add(new Car(result.group(1),DriverSkill.valueOf(result.group(2)),Integer.parseInt(result.group(3)),Integer.parseInt(result.group(4))));
			}
			in.close();
			
			//Adding neighborhood
			for(int x=0; x<points.length; x++)
				for(int y=0; y<points[x].length; y++)
				{
					if(x>0 && y>0 && x<points.length-1 && y<points[x].length-1)
					{
						Point temp[] = new Point[8];
						temp[Direction.TOP_LEFT.getNum()] = points[x-1][y-1];
						temp[Direction.TOP.getNum()] = points[x-1][y];
						temp[Direction.TOP_RIGHT.getNum()] = points[x-1][y+1];
						temp[Direction.LEFT.getNum()] = points[x][y-1];
						temp[Direction.RIGHT.getNum()] = points[x][y+1];
						temp[Direction.BOTTOM_LEFT.getNum()] = points[x+1][y-1];
						temp[Direction.BOTTOM.getNum()] = points[x+1][y];
						temp[Direction.BOTTOM_RIGHT.getNum()] = points[x+1][y+1];
						points[x][y].setNeighbors(temp);
					}
				}
			setTrack(new Track(trackName, points));
		}
		catch(IllegalStateException e){ throw new FileFormatException(); }
	}

	/**
	 * Override painting procedure to draw netting
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(track != null) drawNetting(g);
	}

	/**
	 * Draw visualization of Points table
	 * @param g - Graphics
	 */
	private void drawNetting(Graphics g) {
		int verticalOffset = (SCREEN_HEIGHT- (points[0].length*SIZE+35))/2;
		int horizontalOffset = (SCREEN_WIDTH - (points.length*SIZE+SIZE-1))/2;

		for (int x = 0; x < points.length; ++x) {
			for (int y = 0; y < points[x].length; ++y) {
				SurfaceType type = points[x][y].getType();
				if(points[x][y].isCarCenter()) g.setColor(Color.RED);
				else if (type == SurfaceType.ROAD) g.setColor(Color.DARK_GRAY);
				else if (type == SurfaceType.BARRIER) g.setColor(Color.BLUE);
				else if (type == SurfaceType.GRASS) g.setColor(Color.GREEN);
				else if (type == SurfaceType.WORSE_ROAD) g.setColor(Color.LIGHT_GRAY);
				else if (type == SurfaceType.START_LINE) g.setColor(Color.WHITE);
				
				g.fillRect((x * SIZE+horizontalOffset) + 1, (y * SIZE+verticalOffset) + 1, (SIZE - 1), (SIZE - 1));
			}
		}
	}
}
