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

public class Board extends JComponent implements MouseInputListener, ComponentListener {
	private static final long serialVersionUID = 1L;
	private Point[][] points;
	private Track track;
	private int size = 4;
	private int width=340, height=180;
	public SurfaceType type = SurfaceType.ROAD;
	public Direction direction = Direction.TOP;
	public boolean directionShowed = false;

	public Board(int length, int height) {
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
		g.setColor(Color.GRAY);
		drawNetting(g, size);
	}

	private void drawNetting(Graphics g, int gridSpace) {
		int firstX = 0;
		int firstY = 0;
		int lastX = width*size;
		int lastY = height*size;

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
				if(!directionShowed)
				{
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
					else if (type == SurfaceType.SAND) {
						g.setColor(Color.YELLOW);
					}
					else if (type == SurfaceType.WORSE_ROAD) {
						g.setColor(Color.LIGHT_GRAY);
					}
					else if(type == SurfaceType.START_LINE) g.setColor(Color.WHITE);
				}
				else
				{
					Direction direction = points[x][y].getDirection();
					if (direction == Direction.NONE) {
						g.setColor(Color.WHITE);
					}
					else if (direction == Direction.TOP) {
						g.setColor(Color.RED);
					}
					else if (direction == Direction.BOTTOM) {
						g.setColor(Color.BLACK);
					}
					else if (direction == Direction.LEFT) {
						g.setColor(Color.BLUE);
					}
					else if (direction == Direction.RIGHT) {
						g.setColor(Color.MAGENTA);
					}
					else if (direction == Direction.TOP_LEFT) {
						g.setColor(Color.CYAN);
					}
					else if (direction == Direction.TOP_RIGHT) {
						g.setColor(Color.PINK);
					}
					else if (direction == Direction.BOTTOM_LEFT) {
						g.setColor(Color.YELLOW);
					}
					else if (direction == Direction.BOTTOM_RIGHT) {
						g.setColor(Color.ORANGE);
					}
				}

				g.fillRect((x * size) + 1, (y * size) + 1, (size - 1), (size - 1));
			}
		}

	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX() / size;
		int y = e.getY() / size;
		if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
			//if(type == SurfaceType.ROAD || type == SurfaceType.WORSE_ROAD) points[x][y].change(type, direction);
			if(directionShowed) points[x][y].change(direction);
			else points[x][y].change(type);
			this.repaint();
		}
	}

	public void componentResized(ComponentEvent e) {
		if(track == null) initialize(width, height);
	}

	public void mouseDragged(MouseEvent e) {
		mouseClicked(e);
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

}
