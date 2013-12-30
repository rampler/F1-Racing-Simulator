package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.MatchResult;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import Enums.Direction;
import Enums.DriverSkill;
import Enums.SurfaceType;
import Exceptions.FileFormatException;

public class Board extends JPanel implements MouseInputListener {
	private static final long serialVersionUID = 1L;
	private Point[][] points;
	private Track track;
	private int size = 4;
	public SurfaceType type = SurfaceType.ROAD;
	public Direction direction = Direction.TOP;
	public boolean directionShowed = false;
	private LinkedList<Car> cars;
	
	private final int SCREEN_WIDTH, SCREEN_HEIGHT;
	
	private int sizeScalePercent = 100;
	private int verticalOffset = 0;
	private int horizontalOffset = 0;
	private int simulationWidth = 0;
	private int simulationHeight = 0;

	public Board(int screenWidth, int screenHeight) {
		this.SCREEN_HEIGHT = screenHeight;
		this.SCREEN_WIDTH = screenWidth;
		addMouseListener(this);
		addMouseMotionListener(this);
		setBackground(Color.DARK_GRAY);
	}

	public Track getTrack(){ return track; }
	public int getSizeScalePercent(){ return sizeScalePercent; }
	public LinkedList<Car> getCars(){ return cars; }
	public void setTrack(Track track){ this.track = track; points = track.getPoints(); refreshSimulationSize(); }
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
	 * Refresh values about simulation netting size and offsets
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

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(track != null) drawNetting(g);
	}

	/**
	 * Draw visualization of Points table
	 * @param g - Graphics
	 */
	private void drawNetting(Graphics g) {
		for (int x = 0; x < points.length; ++x) {
			for (int y = 0; y < points[x].length; ++y) {
				if(!directionShowed)
				{
					SurfaceType type = points[x][y].getType();
					if (type == SurfaceType.ROAD) g.setColor(Color.DARK_GRAY);
					else if (type == SurfaceType.BARRIER) g.setColor(Color.BLUE);
					else if (type == SurfaceType.GRASS) g.setColor(Color.GREEN);
					else if (type == SurfaceType.WORSE_ROAD) g.setColor(Color.LIGHT_GRAY);
					else if (type == SurfaceType.START_LINE) g.setColor(Color.WHITE);
					else if (type == SurfaceType.SAND) g.setColor(Color.YELLOW);
				}
				else
				{
					Direction direction = points[x][y].getDirection();
					if (direction == Direction.NONE) g.setColor(Color.WHITE);
					else if (direction == Direction.TOP) g.setColor(Color.RED);
					else if (direction == Direction.BOTTOM) g.setColor(Color.BLACK);
					else if (direction == Direction.LEFT) g.setColor(Color.BLUE);
					else if (direction == Direction.RIGHT) g.setColor(Color.MAGENTA);
					else if (direction == Direction.TOP_LEFT) g.setColor(Color.CYAN);
					else if (direction == Direction.TOP_RIGHT) g.setColor(Color.PINK);
					else if (direction == Direction.BOTTOM_LEFT) g.setColor(Color.YELLOW);
					else if (direction == Direction.BOTTOM_RIGHT) g.setColor(Color.ORANGE);
				}
				
				g.fillRect((x * size+horizontalOffset) + 1, (y * size+verticalOffset) + 1, (size - 1), (size - 1));
			}
		}
	}
	
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
				cars.add(new Car(result.group(1),DriverSkill.valueOf(result.group(2)),Integer.parseInt(result.group(3)),Integer.parseInt(result.group(4)),x+1,16.5));
			}
			in.close();
			
			//Adding drivers to points
			for(Car car : cars) points[car.getPosStartX()][car.getPosStartY()].setCar(car);
			
			//Adding neighborhood
			for(int x=1; x<points.length-1; x++)
				for(int y=1; y<points[x].length-1; y++)
				{
						Point temp[] = new Point[8];
						temp[Direction.TOP_LEFT.getNum()] = points[x-1][y-1];
						temp[Direction.TOP.getNum()] = points[x][y-1];
						temp[Direction.TOP_RIGHT.getNum()] = points[x-1][y+1];
						temp[Direction.LEFT.getNum()] = points[x-1][y];
						temp[Direction.RIGHT.getNum()] = points[x+1][y];
						temp[Direction.BOTTOM_LEFT.getNum()] = points[x+1][y-1];
						temp[Direction.BOTTOM.getNum()] = points[x][y+1];
						temp[Direction.BOTTOM_RIGHT.getNum()] = points[x+1][y+1];
						points[x][y].setNeighbors(temp);
				}
			setTrack(new Track(trackName, points));
		}
		catch(IllegalStateException e){ throw new FileFormatException(); }
	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX() / size;
		int y = e.getY() / size;
		if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
			//if(type == SurfaceType.ROAD || type == SurfaceType.WORSE_ROAD) points[x][y].change(type, direction);
			if(directionShowed) points[x][y].change(direction);
			else points[x][y].change(type);
			this.repaint();
			this.getParent().repaint();
		}
	}

	public void mouseDragged(MouseEvent e) {
		mouseClicked(e);
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

}
