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
import java.io.PrintWriter;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
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
	private Container parent;
	private JSlider zoom;
	
	private double screenWidth, screenHeight;

	public GUI(Container container) {
		parent = container;

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
		
		String[] list = new String[6]; 
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
		
		//Zoom
        zoom = new JSlider(0,3);
        Hashtable<Integer, JLabel> hashtable = new Hashtable<>();
        hashtable.put(0, new JLabel("50%"));
        hashtable.put(1, new JLabel("100%"));
        hashtable.put(2, new JLabel("150%"));
        hashtable.put(3, new JLabel("200%"));
        zoom.setLabelTable(hashtable);
        zoom.setPaintLabels(true);
        zoom.setSnapToTicks(true);
        zoom.addChangeListener(this);
		
		buttonPanel.add(exit);
		buttonPanel.add(save);
		buttonPanel.add(open);
		buttonPanel.add(typeBox);
		buttonPanel.add(directionBox);
		buttonPanel.add(directionShowed);
		buttonPanel.add(zoom);

		//Board creating
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = screenSize.getWidth();
		screenHeight = screenSize.getHeight();
		board = new Board((int)screenWidth, (int)screenHeight);
		
		JScrollPane spane = new JScrollPane(board);
		container.add(spane, BorderLayout.CENTER);
		container.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public void stateChanged(ChangeEvent e) {
		if(e.getSource().equals(zoom))
		{
			switch(zoom.getValue())
			{
				case 0: board.setSizeScalePercent(50); break;
				case 1: board.setSizeScalePercent(100); break;
				case 2: board.setSizeScalePercent(150); break;
				case 3: board.setSizeScalePercent(200); break;
			}
		}
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
				board.loadTrack(file);
				board.repaint();
				this.repaint();
				parent.repaint();
			}
			catch(Exception exp){JOptionPane.showMessageDialog(this, "Track loading problem!");};
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
	
	private void saveTrack(File file, Track track) throws FileNotFoundException
	{
		Point[][] points = track.getPoints();
		PrintWriter out = new PrintWriter(file);
		out.write(track.getName()+";"+points.length+";"+points[0].length+";"+board.getCars().size()+"\n");
		for(int i=0; i<points.length; i++)
			for(int j=0; j<points[i].length; j++)
				out.write(points[i][j].getType()+";"+points[i][j].getDirection()+"\n");
		for(Car car : board.getCars())
			out.write(car.getDriverName()+";"+car.getDriverSkills()+";"+car.getPosStartX()+";"+car.getPosStartY()+"\n");
		out.close();
	}
}
