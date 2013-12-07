package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

import Enums.Direction;
import Enums.SurfaceType;
import POJOs.Point;
import POJOs.Track;

public class Board extends JComponent implements MouseInputListener, ComponentListener {
	private static final long serialVersionUID = 1L;
	private Point[][] points;
	private Track track;
	private int size = 4;
	private final int width=340, height=180;
	private final int screenWidth, screenHeight;

	public Board(int screenWidth, int screenHeight) {
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		addMouseListener(this);
		addComponentListener(this);
		addMouseMotionListener(this);
		setBackground(Color.GRAY);
		setOpaque(true);
	}

	public Track getTrack(){ return track; }
	public void setTrack(Track track){ this.track = track; points = track.getPoints(); }

	private void initialize(int length, int height) {
		points = new Point[length][height];

		for (int x = 0; x < points.length; ++x)
			for (int y = 0; y < points[x].length; ++y)
				points[x][y] = new Point();
				track = new Track("Silverstone", points);

		for (int x = 1; x < points.length-1; ++x) {
			for (int y = 1; y < points[x].length-1; ++y) {
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
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.GRAY);
		drawNetting(g, size);
	}

	private void drawNetting(Graphics g, int gridSpace) {
		
		int verticalOffset = (screenHeight - 755)/2;
		int horizontalOffset = (screenWidth - 1363)/2;

		int firstX = horizontalOffset;
		int firstY = verticalOffset;
		int lastX = width*size+horizontalOffset;
		int lastY = height*size+verticalOffset;

		int x = firstX;
		while (x < lastX+1) {
			g.drawLine(x, firstY, x, lastY);
			x += gridSpace;
		}

		int y = firstY;
		while (y < lastY+1) {
			g.drawLine(firstX, y, lastX, y);
			y += gridSpace;
		}

		for (x = 0; x < points.length; ++x) {
			for (y = 0; y < points[x].length; ++y) {
				SurfaceType type = points[x][y].getType();
				if (type == SurfaceType.ROAD) {
					g.setColor(Color.DARK_GRAY);
				}
				else if (type == SurfaceType.BARRIER) {
					g.setColor(Color.BLUE);
				}
				else if (type == SurfaceType.GRASS) {
					g.setColor(Color.GREEN);
				}
				else if (type == SurfaceType.WORSE_ROAD) {
					g.setColor(Color.LIGHT_GRAY);
				}

				g.fillRect((x * size+horizontalOffset) + 1, (y * size+verticalOffset) + 1, (size - 1), (size - 1));
			}
		}

	}

	public void componentResized(ComponentEvent e) {
		if(track == null) initialize(width, height);
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void componentShown(ComponentEvent e) {}
	public void componentMoved(ComponentEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	public void componentHidden(ComponentEvent e) {}
	public void mousePressed(MouseEvent e) {}

}
