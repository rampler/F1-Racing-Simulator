package GUI;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.MatchResult;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Enums.Direction;
import Enums.SurfaceType;

public class GUI extends JPanel implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	private Board board;
	private JButton exit, save, open;
	private JComboBox<String> typeBox, directionBox;
	private JCheckBox directionShowed;

	public GUI() {
	}

	public void initialize(Container container) {
		container.setLayout(new BorderLayout());
		container.setSize(new Dimension(1024, 768));

		JPanel buttonPanel = new JPanel();
		
		directionShowed = new JCheckBox();
		directionShowed.setText("Tryb kierunków");
		directionShowed.setActionCommand("directionShowed");
		directionShowed.addActionListener(this);
		
		exit = new JButton("Exit");
		exit.setActionCommand("exit");
		exit.addActionListener(this);
		
		save = new JButton("Save");
		save.setActionCommand("save");
		save.addActionListener(this);
		
		open = new JButton("Open");
		open.setActionCommand("open");
		open.addActionListener(this);
		
		String[] list = new String[5]; 
		int i=0;
		for(SurfaceType value : SurfaceType.values()) 
		{
			list[i] = value.toString();
			i++;
		}
		typeBox = new JComboBox<String>(list);
		typeBox.addActionListener(this);
		typeBox.setActionCommand("typeBox");
		
		list = new String[9]; 
		i=0;
		for(Direction value : Direction.values()) 
		{
			list[i] = value.toString();
			i++;
		}
		directionBox = new JComboBox<String>(list);
		directionBox.setSelectedIndex(2);
		directionBox.addActionListener(this);
		directionBox.setActionCommand("directionBox");
		
		buttonPanel.add(exit);
		buttonPanel.add(save);
		buttonPanel.add(open);
		buttonPanel.add(typeBox);
		buttonPanel.add(directionBox);
		buttonPanel.add(directionShowed);

		board = new Board(1024, 768 - buttonPanel.getHeight());
		container.add(board, BorderLayout.CENTER);
		container.add(buttonPanel, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("exit")) {
			for(Frame frame : Frame.getFrames())
				frame.dispose();					
		}
		else if (command.equals("save")) {
			try
			{
				File file = new File(board.getTrack().getName()+".track");
				saveTrack(file, board.getTrack());
			}
			catch(IOException exp){JOptionPane.showMessageDialog(this, "Saving problem!");};
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
		else if(command.equals("typeBox")){
			board.type = SurfaceType.valueOf((String)typeBox.getSelectedItem());
		}
		else if(command.equals("directionBox")){
			board.direction = Direction.valueOf((String)directionBox.getSelectedItem());
		}
		else if(command.equals("directionShowed")){
			board.directionShowed = directionShowed.isSelected();
			board.repaint();
		}
	}

	public void stateChanged(ChangeEvent e) {
	}
	
	private void saveTrack(File file, Track track) throws FileNotFoundException
	{
		Point[][] points = track.getPoints();
		PrintWriter out = new PrintWriter(file);
		out.write(track.getName()+";"+points.length+";"+points[0].length+"\n");
		for(int i=0; i<points.length; i++)
			for(int j=0; j<points[i].length; j++)
			{
				out.write(points[i][j].getType()+";"+points[i][j].getDirection()+";"+points[i][j].getState()+";"+points[i][j].getAngle()+";"+points[i][j].isCarCenter()+"\n");
			}
		out.close();
	}
	
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
}
