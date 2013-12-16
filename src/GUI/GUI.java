package GUI;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Enums.Dryness;
import Enums.Tire;
import Exceptions.FileFormatException;

/**
 * Program's GUI with all ActionListeners
 * @author Sabina Rydzek, Kacper Furmañski, Mateusz Kotlarz
 *
 */
public class GUI extends JPanel implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	private Board board;
	private JButton exit, open, simulation, start, pause, clear, about;
	private JComboBox<String> drynessCB, tiresCB;
	private JScrollPane scrollPane;
	private JSlider zoom;
	private double screenWidth, screenHeight;
	private Container parent;
	private File loadedTrackFile;

	public void initialize(Container container) {
		parent = container;
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
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
        
        zoom = new JSlider(0,2);
        Hashtable<Integer, JLabel> hashtable = new Hashtable<>();
        hashtable.put(0, new JLabel("50%"));
        hashtable.put(1, new JLabel("100%"));
        hashtable.put(2, new JLabel("200%"));
        zoom.setLabelTable(hashtable);
        zoom.setPaintLabels(true);
        zoom.setSnapToTicks(true);
        zoom.addChangeListener(this);
		
		
		buttonPanel.add(open);
		buttonPanel.add(simulation);
		buttonPanel.add(Box.createHorizontalStrut(100));
		buttonPanel.add(start);
		buttonPanel.add(pause);
		buttonPanel.add(clear);
		buttonPanel.add(Box.createHorizontalStrut(100));
		buttonPanel.add(about);
		buttonPanel.add(exit);
		buttonPanel.add(Box.createHorizontalStrut(100));
		buttonPanel.add(new JLabel("Zoom:"));
		buttonPanel.add(zoom);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = screenSize.getWidth();
		screenHeight = screenSize.getHeight();
		board = new Board((int)screenWidth, (int)screenHeight);
		
		scrollPane = new JScrollPane(board);
		scrollPane.setPreferredSize(new Dimension(1363,729));
		container.add(scrollPane, BorderLayout.CENTER);
		container.add(buttonPanel, BorderLayout.SOUTH);
		
		openButtonAction();
	}

	/**
	 * Implemented from ActionListener - Buttons actions
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("exit")) {
			for(Frame frame : Frame.getFrames())
				frame.dispose();					
		}
		else if (command.equals("open")) { openButtonAction(); }
		else if(command.equals("parameters")){ parametersButtonAction(); }
		else if(command.equals("start")){ startSimulation(); }
		else if(command.equals("pause")){ pauseSimulation(); }
		else if(command.equals("clear")){ clearSimulationWindow(); }
		else if(command.equals("about")){ aboutButtonAction(); }
		
		//Parameters window commands
		else if(command.equals("changedDryness")){ board.setTrackDryness(Dryness.valueOf((String)drynessCB.getSelectedItem())); }
		else if(command.equals("changedTires")){ board.changeCarsTires(Tire.valueOf((String)tiresCB.getSelectedItem())); }
	}
	
	/**
	 * Implemented from ChangeListener - Slider action
	 * Changed Zoom
	 * @param e
	 */
	public void stateChanged(ChangeEvent e) {
		switch(zoom.getValue())
		{
			case 0: board.setSizeScalePercent(50); break;
			case 1: board.setSizeScalePercent(100); break;
			case 2: board.setSizeScalePercent(200); break;
		}
	}
	
	/**
	 * Start simulation
	 */
	private void startSimulation()
	{
		//TODO
	}
	
	/**
	 * Pause simulation
	 */
	private void pauseSimulation()
	{
		//TODO
	}
	
	/**
	 * Clear simulation window
	 */
	private void clearSimulationWindow()
	{
		//TODO - code below only load same track once again
		
		try { openTrack(loadedTrackFile); } 
		catch(Exception exp){JOptionPane.showMessageDialog(this, "Track loading problem!");}
	}
	
	/**
	 * Load Track Button Action
	 */
	private void openButtonAction()
	{
		try
		{
			JFileChooser fc = new JFileChooser();
			fc.showOpenDialog(this);
			if(fc.getSelectedFile() != null) openTrack(fc.getSelectedFile());
			else throw new FileNotFoundException();
			
		}
		catch(Exception exp){JOptionPane.showMessageDialog(this, "Track loading problem!"); }
	}
	
	/**
	 * Open track from file
	 * @param file
	 * @throws FileNotFoundException - file not found
	 * @throws FileFormatException - regex not match
	 */
	private void openTrack(File file) throws FileNotFoundException, FileFormatException
	{
		loadedTrackFile = file;
		board.loadTrack(loadedTrackFile);
		board.repaint();
		this.repaint();
		parent.repaint();
	}
	
	/**
	 * Show about window with credits
	 */
	private void aboutButtonAction()
	{
		JFrame param = new JFrame("About");
		param.setLayout(new BorderLayout());
		param.setBounds((int)(screenWidth-455)/2, (int)(screenHeight-355)/2, 455, 355);
		JPanel optionsPanel = new AboutWindow();
		param.add(optionsPanel, BorderLayout.CENTER);
		param.setVisible(true);
	}
	
	/**
	 * Show parameters window
	 */
	private void parametersButtonAction()
	{
		JFrame param = new JFrame("Simulation Parameters");
		param.setLayout(new BorderLayout());
		param.setBounds((int)(screenWidth-300)/2, (int)(screenHeight-100)/2, 300, 100);
		JPanel optionsPanel = new JPanel();
		optionsPanel.setLayout(new GridLayout(2,2));
		
		//Track dryness
		JLabel drynessLbl = new JLabel("Track dryness: ");
		optionsPanel.add(drynessLbl);
		
		String[] list = new String[2];
		int i=0;
		for(Dryness value : Dryness.values())
		{
			list[i] = value.toString();
			i++;
		}
		drynessCB = new JComboBox<String>(list);
		drynessCB.setActionCommand("changedDryness");
		drynessCB.addActionListener(this);
		optionsPanel.add(drynessCB);
		
		//Tires equipped
		JLabel tiresLbl = new JLabel("Tires equipped: ");
		optionsPanel.add(tiresLbl);
		list = new String[2];
		i=0;
		for(Tire value : Tire.values())
		{
			list[i] = value.toString();
			i++;
		}
		tiresCB = new JComboBox<String>(list);
		tiresCB.setActionCommand("changedTires");
		tiresCB.addActionListener(this);
		optionsPanel.add(tiresCB);
		
		param.add(optionsPanel, BorderLayout.CENTER);
		param.setVisible(true);
	}
}
