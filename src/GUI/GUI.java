package GUI;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.MatchResult;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Enums.Direction;
import POJOs.Point;
import POJOs.Track;

public class GUI extends JPanel implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	private Board board;
	private JButton exit, open, simulation, start, pause, clear, about;
	private double screenWidth, screenHeight;

	public GUI() {}

	public void initialize(Container container) {
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS)); //new BorderLayout());
		container.setSize(new Dimension(1024, 768));

		JPanel buttonPanel = new JPanel();
		
		start = new JButton("Start");
		start.setActionCommand("start");
		start.addActionListener(this);
		
		pause = new JButton("Pause");
		pause.setActionCommand("pause");
		pause.addActionListener(this);
		
		clear = new JButton("Clear");
		clear.setActionCommand("clear");
		clear.addActionListener(this);
		
		about = new JButton("About");
		about.setActionCommand("about");
		about.addActionListener(this);
		
		exit = new JButton("Exit");
		exit.setActionCommand("exit");
		exit.addActionListener(this);
		
		open = new JButton("Load track");
		open.setActionCommand("open");
		open.addActionListener(this);
		
		simulation = new JButton("Simulation Parameters");
		simulation.setActionCommand("parameters");
		simulation.addActionListener(this);
		
		
		buttonPanel.add(open);
		buttonPanel.add(simulation);
		buttonPanel.add(Box.createHorizontalStrut(100));
		buttonPanel.add(start);
		buttonPanel.add(pause);
		buttonPanel.add(clear);
		buttonPanel.add(Box.createHorizontalStrut(100));
		buttonPanel.add(about);
		buttonPanel.add(exit);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = screenSize.getWidth();
		screenHeight = screenSize.getHeight();
		board = new Board((int)screenWidth, (int)screenHeight);
		board.setPreferredSize(new Dimension(1363,729));
		
		JScrollPane scrollPane = new JScrollPane(board);
		container.add(scrollPane, BorderLayout.CENTER);
		container.add(buttonPanel, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("exit")) {
			for(Frame frame : Frame.getFrames())
				frame.dispose();					
		}
		else if (command.equals("open")) {
			try
			{
				JFileChooser fc = new JFileChooser();
				fc.showOpenDialog(this);
				File file = fc.getSelectedFile();
				board.setTrack(loadTrack(file));
				board.repaint();
			}
			catch(IOException exp){JOptionPane.showMessageDialog(this, "Track loading problem!");};
		}
		else if(command.equals("parameters")){
			JFrame param = new JFrame("Simulation Parameters");
			param.setLayout(new BorderLayout());
			param.setBounds((int)(screenWidth-500)/2, (int)(screenHeight-400)/2, 500, 400);
			JPanel optionsPanel = new JPanel();
			
			JButton uselessButton = new JButton("Useless Button");
			optionsPanel.add(uselessButton);
			
			param.add(optionsPanel, BorderLayout.CENTER);
			param.setVisible(true);
		}
		else if(command.equals("start")){}
		else if(command.equals("pause")){}
		else if(command.equals("clear")){}
		else if(command.equals("about")){
			JFrame param = new JFrame("About");
			param.setLayout(new BorderLayout());
			param.setBounds((int)(screenWidth-455)/2, (int)(screenHeight-355)/2, 455, 355);
			JPanel optionsPanel = new AboutWindow();
			param.add(optionsPanel, BorderLayout.CENTER);
			param.setVisible(true);
		}
	}
	
	/**
	 * Loading Track from file
	 * @param file 
	 * @return loaded Track
	 * @throws FileNotFoundException
	 */
	private Track loadTrack(File file) throws FileNotFoundException
	{
		Scanner in = new Scanner(file);
		in.findInLine("(\\w+);(\\d+);(\\d+)");
		MatchResult result = in.match();
		Point[][] points = new Point[Integer.parseInt(result.group(2))][Integer.parseInt(result.group(3))];
		String trackName = result.group(1);
		for(int x=0; x<points.length; x++)
			for(int y=0; y<points[x].length; y++)
			{
				in.nextLine();
				in.findInLine("(\\w+);(\\w+);(\\d+);(\\d+);(\\w+)");
				result = in.match();
				
				points[x][y] = new Point();
				points[x][y].setType(result.group(1));
				points[x][y].setDirection(result.group(2));
				points[x][y].setState(Integer.parseInt(result.group(3)));
				points[x][y].setAngle(Integer.parseInt(result.group(4)));
				points[x][y].setCarCenter(Boolean.getBoolean(result.group(5)));
				
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
		in.close();
		return new Track(trackName, points);
	}
	
	public void stateChanged(ChangeEvent e) {}
}
