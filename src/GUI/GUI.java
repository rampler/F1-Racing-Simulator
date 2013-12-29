package GUI;
import java.awt.BorderLayout;
import java.awt.Color;
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

import javax.swing.BorderFactory;
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
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Enums.Dryness;
import Enums.Tire;
import Exceptions.FileFormatException;
import POJOs.Car;

/**
 * Program's GUI with all ActionListeners
 * @author Sabina Rydzek, Kacper Furmañski, Mateusz Kotlarz
 *
 */
public class GUI extends JPanel implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	private Board board;
	
	private JButton exit, open, simulation, start, clear, about, drivers, result;
	private JComboBox<String> drynessCB, tiresCB;
	private JScrollPane scrollPane;
	private JPanel buttonPanel;
	private JSlider zoom;
	private JFrame driversWindow, paramWindow, resultWindow;
	private JSpinner g100, g200, g300, b100, b200, b300, n100, n200, n300;
	private JTable table, tableResult;
	private Container parent;
	
	private double screenWidth, screenHeight;
	private File loadedTrackFile;
	private Timer timer, timerDrivers;
	private int timerDelay = 10, timerDriversDelay = 300;
	private boolean notStarted = true;

	/**
	 * Initialize GUI
	 * @param container
	 */
	public GUI(Container container) {
		//GUI Layout
		parent = container;
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.setSize(new Dimension(1024, 768));
		
		//Timers
		timer = new Timer(timerDelay, this);
		timer.stop();
		timerDrivers = new Timer(timerDriversDelay, this);
		timerDrivers.stop();
		
		//Buttons
		buttonPanel = new JPanel();
		
		start = new JButton("Start");
		start.setActionCommand("start");
		start.addActionListener(this);
		
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
		
		drivers = new JButton("Drivers");
		drivers.setActionCommand("drivers");
		drivers.addActionListener(this);
		table = new JTable();
		
		result = new JButton("Result");
		result.setActionCommand("result");
		result.addActionListener(this);
		tableResult = new JTable();
		
		simulation = new JButton("Simulation Parameters");
		simulation.setActionCommand("parameters");
		simulation.addActionListener(this);
        
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
		
		//Control Panel
		buttonPanel.add(open);
		buttonPanel.add(simulation);
		buttonPanel.add(Box.createHorizontalStrut(50));
		buttonPanel.add(start);
		buttonPanel.add(clear);
		buttonPanel.add(Box.createHorizontalStrut(50));
		buttonPanel.add(about);
		buttonPanel.add(exit);
		buttonPanel.add(Box.createHorizontalStrut(50));
		buttonPanel.add(new JLabel("Zoom:"));
		buttonPanel.add(zoom);
		buttonPanel.add(Box.createHorizontalStrut(50));
		buttonPanel.add(drivers);
		buttonPanel.add(result);
	        
		
		//Board creating
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = screenSize.getWidth();
		screenHeight = screenSize.getHeight();
		board = new Board((int)screenWidth, (int)screenHeight);
		
		scrollPane = new JScrollPane(board);
		scrollPane.setPreferredSize(new Dimension(1363,729));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		container.add(scrollPane, BorderLayout.CENTER);
		container.add(buttonPanel, BorderLayout.SOUTH);
		
		//Opening Loading Track Window
		openButtonAction();
	}

	/**
	 * Implemented from ActionListener - Buttons and timers actions
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(timer)) { board.iteration(timerDelay); } 
		else if(e.getSource().equals(timerDrivers))
		{ 
			boolean none = true;
			if(driversWindow != null && driversWindow.isVisible()) { TablesRefresher.refreshDriversTableModel(board, table); none = false; }
			if(resultWindow != null && resultWindow.isVisible()) { TablesRefresher.refreshResultTableModel(board, tableResult); none = false; }
			if(none) timerDrivers.stop();
		}
		else 
		{
			String command = e.getActionCommand();
			if (command.equals("exit")) {
				timer.stop();
				timerDrivers.stop();
				for(Frame frame : Frame.getFrames())
					frame.dispose();		
			}
			else if (command.equals("open")) { openButtonAction(); }
			else if(command.equals("parameters")){ parametersButtonAction(); }
			else if(command.equals("start"))
			{ 
				if(!timer.isRunning()) 
				{ 
					timer.start();  
					notStarted = false; 
					start.setText("Pause");
				}
				else{ timer.stop(); start.setText("Start"); }
			}
			else if(command.equals("clear")){ clearSimulationWindow(); }
			else if(command.equals("about")){ aboutButtonAction(); }
			else if(command.equals("drivers")){ showDriversWindow(); }
			else if(command.equals("result")){ showResultWindow(); }
			
			//Parameters window commands
			else if(command.equals("changedDryness")){ board.setTrackDryness(Dryness.valueOf((String)drynessCB.getSelectedItem())); }
			else if(command.equals("changedTires")){ board.changeCarsTires(Tire.valueOf((String)tiresCB.getSelectedItem())); }
			else if(command.equals("restoreDefaultParameters")){ restoreDefaultParameters(); }
		}
	}
	
	/**
	 * Implemented from ChangeListener - Slider and Spinners actions
	 * Changed Zoom
	 * @param e
	 */
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
		else if(e.getSource().equals(g100))
		{ 
			board.getAccelerationTable()[0][0] = (double) g100.getValue(); 
			if(notStarted) //If not simulation Started then change start acceleration
				for(Car car : board.getCars())
					car.setAcceleration((double) g100.getValue());
		}
		else if(e.getSource().equals(g200)){ board.getAccelerationTable()[0][1] = (double) g200.getValue(); }
		else if(e.getSource().equals(g300)){ board.getAccelerationTable()[0][2] = (double) g300.getValue(); }
		else if(e.getSource().equals(b100)){ board.getAccelerationTable()[1][0] = (double) b100.getValue(); }
		else if(e.getSource().equals(b200)){ board.getAccelerationTable()[1][1] = (double) b200.getValue(); }
		else if(e.getSource().equals(b300)){ board.getAccelerationTable()[1][2] = (double) b300.getValue(); }
		else if(e.getSource().equals(n100)){ board.getAccelerationTable()[2][0] = (double) n100.getValue(); }
		else if(e.getSource().equals(n200)){ board.getAccelerationTable()[2][1] = (double) n200.getValue(); }
		else if(e.getSource().equals(n300)){ board.getAccelerationTable()[2][2] = (double) n300.getValue(); }
	}

	
	/**
	 * Clear simulation window
	 */
	private void clearSimulationWindow()
	{
		if(timer.isRunning()) { timer.stop(); start.setText("Start"); }
		try { openTrack(loadedTrackFile); } 
		catch(Exception exp){JOptionPane.showMessageDialog(this, "Track loading problem!");}
		notStarted = true;
		TablesRefresher.createResultTableModel(board, tableResult);
	}
	
	/**
	 * Show drivers information window
	 */
	private void showDriversWindow()
	{
		if(driversWindow == null)
		{
			driversWindow = new JFrame("Drivers");
			driversWindow.setUndecorated(true);
			driversWindow.setAlwaysOnTop(true);
			driversWindow.setLayout(new BorderLayout());
			driversWindow.setBounds((int)(screenWidth-455-19), (int)(screenHeight-218-(buttonPanel.getHeight()+19)), 455, 218);
			JPanel mainPanel = new JPanel(new BorderLayout());
			timerDrivers.start();
			
			//Add Table
			JScrollPane scroll = new JScrollPane(table);
			mainPanel.add(scroll, BorderLayout.CENTER);
			driversWindow.add(mainPanel, BorderLayout.CENTER);
			driversWindow.setVisible(true);
		}
		else if(!driversWindow.isVisible()){ driversWindow.setVisible(true); timerDrivers.start(); }
		else {driversWindow.setVisible(false); }
	}
	
	/**
	 * Show results window
	 */
	private void showResultWindow()
	{
		if(resultWindow == null)
		{
			resultWindow = new JFrame("Results");
			resultWindow.setUndecorated(true);
			resultWindow.setAlwaysOnTop(true);
			resultWindow.setLayout(new BorderLayout());
			resultWindow.setBounds(0, 0, 455, 218);
			JPanel mainPanel = new JPanel(new BorderLayout());
			timerDrivers.start();
			
			//Add Table
			JScrollPane scroll = new JScrollPane(tableResult);
			mainPanel.add(scroll, BorderLayout.CENTER);
			resultWindow.add(mainPanel, BorderLayout.CENTER);
			resultWindow.setVisible(true);
		}
		else if(!resultWindow.isVisible()){ resultWindow.setVisible(true); timerDrivers.start(); }
		else {resultWindow.setVisible(false); }
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
		if(timer.isRunning()) { timer.stop(); start.setText("Start"); }
		loadedTrackFile = file;
		board.loadTrack(loadedTrackFile);
		board.repaint();
		this.repaint();
		parent.repaint();
		TablesRefresher.refreshResultTableModel(board, tableResult);
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
		if(paramWindow == null)
		{
			paramWindow = new JFrame("Simulation Parameters");
			paramWindow.setUndecorated(true);
			paramWindow.setAlwaysOnTop(true);
			paramWindow.setBounds(0, (int)(screenHeight-150-(buttonPanel.getHeight()+19)), 350, 150);
			JPanel contentPanel = new JPanel(new BorderLayout());
			contentPanel.setBorder(BorderFactory.createLineBorder(new Color(50,50,50)));
			
			JPanel optionsPanel = new JPanel();
			optionsPanel.setLayout(new GridLayout(3,2));
			
			//Track dryness	
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
			
			//Tires equipped			
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
			
			optionsPanel.add(new JLabel("Track dryness: "));
			optionsPanel.add(drynessCB);
			optionsPanel.add(new JLabel("Tires equipped: "));
			optionsPanel.add(tiresCB);
			
			optionsPanel.add(new JLabel("Acceleration(m/s^2): "));
			optionsPanel.add(new JLabel(""));
			
			//Accelerations
			JPanel accPanel = new JPanel();
			accPanel.setLayout(new GridLayout(5,5));
			
			JButton restore = new JButton("Default");
			restore.setActionCommand("restoreDefaultParameters");
			restore.addActionListener(this);
			
			g100 = new JSpinner(new SpinnerNumberModel(16.50, 0.00, 100.00, 0.01));
			g200 = new JSpinner(new SpinnerNumberModel(14.70, 0.00, 100.00, 0.01));
			g300 = new JSpinner(new SpinnerNumberModel(9.76, 0.00, 100.00, 0.01));
			
			b100 = new JSpinner(new SpinnerNumberModel(-24.00, -100.00, 0.00, 0.01));
			b200 = new JSpinner(new SpinnerNumberModel(-21.00, -100.00, 0.00, 0.01));
			b300 = new JSpinner(new SpinnerNumberModel(-17.30, -100.00, 0.00, 0.01));
			
			n100 = new JSpinner(new SpinnerNumberModel(-2.00, -100.00, 0.00, 0.01));
			n200 = new JSpinner(new SpinnerNumberModel(-2.30, -100.00, 0.00, 0.01));
			n300 = new JSpinner(new SpinnerNumberModel(-2.70, -100.00, 0.00, 0.01));
			
			g100.addChangeListener(this);
			g200.addChangeListener(this);
			g300.addChangeListener(this);
			b100.addChangeListener(this);
			b200.addChangeListener(this);
			b300.addChangeListener(this);
			n100.addChangeListener(this);
			n200.addChangeListener(this);
			n300.addChangeListener(this);
			
			accPanel.add(new JLabel(""));
			accPanel.add(new JLabel(""));
			accPanel.add(new JLabel("0-100 km/h:"));
			accPanel.add(new JLabel("100-200 km/h:"));
			accPanel.add(new JLabel("200-300 km/h:"));
			
			accPanel.add(new JLabel(""));
			accPanel.add(new JLabel("Gas"));
			accPanel.add(g100);
			accPanel.add(g200);
			accPanel.add(g300);
			
			accPanel.add(new JLabel(""));
			accPanel.add(new JLabel("Break"));
			accPanel.add(b100);
			accPanel.add(b200);
			accPanel.add(b300);
			
			accPanel.add(new JLabel(""));
			accPanel.add(new JLabel("None"));
			accPanel.add(n100);
			accPanel.add(n200);
			accPanel.add(n300);
			
			accPanel.add(restore);
			accPanel.add(new JLabel(""));
			accPanel.add(new JLabel(""));
			accPanel.add(new JLabel(""));
			accPanel.add(new JLabel(""));
			
			contentPanel.add(optionsPanel, BorderLayout.NORTH);
			contentPanel.add(accPanel, BorderLayout.CENTER);
			paramWindow.add(contentPanel);
			paramWindow.setVisible(true);
		}
		else if(!paramWindow.isVisible()) paramWindow.setVisible(true);
		else paramWindow.setVisible(false);
	}
	
	/**
	 * Restore default simulation parameters
	 */
	private void restoreDefaultParameters()
	{ 
		//Table of accelerations
		double[][] table = board.getDefaultAccelerationTable();
		board.setAccelerationTable(table);
		g100.setValue(table[0][0]);
		g200.setValue(table[0][1]);
		g300.setValue(table[0][2]);
		b100.setValue(table[1][0]);
		b200.setValue(table[1][1]);
		b300.setValue(table[1][2]);
		n100.setValue(table[2][0]);
		n200.setValue(table[2][1]);
		n300.setValue(table[2][2]);
		
		//Track dryness and tires
		board.setTrackDryness(Dryness.DRY);
		board.changeCarsTires(Tire.DRY);
		tiresCB.setSelectedIndex(0);
		drynessCB.setSelectedIndex(0);
	}
}
