package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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
 * @author Sabina Rydzek, Kacper Furmañski, Mateusz Kotlarz
 *
 */
public class Board extends JPanel{
	private static final long serialVersionUID = 1L;
	private Point[][] points;
	private Track track;
	private int size = 4;
	private final int SCREEN_WIDTH, SCREEN_HEIGHT;
	private Dryness trackDryness;
	private LinkedList<Car> cars;
	
	private int sizeScalePercent = 100;
	private int verticalOffset = 0;
	private int horizontalOffset = 0;
	private int simulationWidth = 0;
	private int simulationHeight = 0;

	/**
	 * Constructor gets screen size and setting background color
	 * @param screenWidth
	 * @param screenHeight
	 */
	public Board(int screenWidth, int screenHeight) {
		this.SCREEN_HEIGHT = screenHeight;
		this.SCREEN_WIDTH = screenWidth;
		setBackground(Color.DARK_GRAY);
		trackDryness = Dryness.DRY;
	}

	//Getters
	public Track getTrack(){ return track; }
	public int getSizeScalePercent(){ return sizeScalePercent; }
	public LinkedList<Car> getCars(){ return cars; }
	
	//Setters
	public void setTrack(Track track){ this.track = track; points = track.getPoints(); refreshSimulationSize(); }
	public void setTrackDryness(Dryness dryness){ trackDryness = dryness; }
	public void setSizeScalePercent(int sizeScalePercent){ 
		this.sizeScalePercent = sizeScalePercent; 
		if(sizeScalePercent == 50) size = 2;
		else if(sizeScalePercent == 100) size = 4;
		else if(sizeScalePercent == 150) size = 6;
		else if(sizeScalePercent == 200) size = 8;
		refreshSimulationSize();
		repaint();
	}
	
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
			cars = new LinkedList<Car>();
			for(int x=0; x<driversCount; x++)
			{
				in.nextLine();
				in.findInLine("(\\w+);(\\w+);(\\d+);(\\d+)");
				result = in.match();
				cars.add(new Car(result.group(1),DriverSkill.valueOf(result.group(2)),Integer.parseInt(result.group(3)),Integer.parseInt(result.group(4)),x+1));
			}
			in.close();
			
			//Adding drivers to points
			for(Car car : cars) points[car.getPosX()-1][car.getPosY()-1].setCar(car);
			
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
	 * Override painting procedure to draw netting and cars
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(track != null) { drawNetting(g); drawCars(g); }
	}
	
	/**
	 * Refresh values about simulation netting size
	 */
	private void refreshSimulationSize()
	{
		simulationWidth = points.length*size+size-1;
		simulationHeight = points[0].length*size+35;
		
		if(simulationWidth < SCREEN_WIDTH) horizontalOffset = (SCREEN_WIDTH - simulationWidth)/2;
		else verticalOffset = 0;
		
		if(simulationHeight < SCREEN_HEIGHT-35) verticalOffset = (SCREEN_HEIGHT- simulationHeight)/2;
		else horizontalOffset = 0;
		
		this.setPreferredSize(new Dimension(simulationWidth, simulationHeight-35));
		this.getParent().revalidate(); //Revalidate ScrollPane
	}

	/**
	 * Draw cars visualization
	 * If zoom is 50% then draw only car center point
	 * If zoom is 200% also draw cars numbers
	 * @param g - Graphics
	 * @param x - Horizontal coordinate of car
	 * @param y - Vertical coordinate of car
	 */
	private void drawCars(Graphics g) {
		for(Car car : cars)
		{
			int x = car.getPosX();
			int y = car.getPosY();
			g.setColor(Color.RED);
			//car.setAngle(45); //for test only 
			if(sizeScalePercent < 100) g.fillRect((x * size+horizontalOffset) + 1, (y * size+verticalOffset) + 1, (size - 1), (size - 1));
			else
			{
				int translateX = (x * size+horizontalOffset) - size/2;
				int translateY = (y * size+verticalOffset) - (size/2);
				
				//Draw cars rects
				Graphics2D g2d = (Graphics2D)g;
			    Rectangle rect = new Rectangle(-(size - 2)/2, -(2*size - 2)/2, (size - 2), (2*size - 2)); //Create Rectangle with center on (0,0)
			    g2d.translate(translateX, translateY);
			    g2d.rotate(Math.toRadians(car.getAngle()));
			    g2d.draw(rect);
			    g2d.fill(rect);
			    
			    //Draw cars numbers
			    if(sizeScalePercent == 200)
			    {
				    g2d.setColor(Color.BLACK);
				    if(car.getNumber() < 10)
			    	{
				    	g2d.setFont(new Font("Verdana", Font.BOLD, size));
				    	g2d.drawString(car.getNumber()+"", -(size - 2)/2+1, size/4);
			    	}
				    else 
			    	{
				    	g2d.setFont(new Font("Verdana", Font.BOLD, size-1));
				    	g2d.drawString(car.getNumber()+"", -(size - 2)/2-2, size/4);
			    	}
				    
			    }
			    
			    //Reset graphics
			    g2d.rotate(Math.toRadians(-car.getAngle()));
			    g2d.translate(-translateX, -translateY);
			}
		}
	}
	
	/**
	 * Draw visualization of Points table
	 * @param g - Graphics
	 */
	private void drawNetting(Graphics g) {
		for (int x = 0; x < points.length; ++x) {
			for (int y = 0; y < points[x].length; ++y) {
				SurfaceType type = points[x][y].getType();
				if (type == SurfaceType.ROAD) g.setColor(Color.DARK_GRAY);
				else if (type == SurfaceType.BARRIER) g.setColor(Color.BLUE);
				else if (type == SurfaceType.GRASS) g.setColor(Color.GREEN);
				else if (type == SurfaceType.WORSE_ROAD) g.setColor(Color.LIGHT_GRAY);
				else if (type == SurfaceType.START_LINE) g.setColor(Color.WHITE);
				else if (type == SurfaceType.SAND) g.setColor(Color.YELLOW);
				
				g.fillRect((x * size+horizontalOffset) + 1, (y * size+verticalOffset) + 1, (size - 1), (size - 1));
			}
		}
	}
}
