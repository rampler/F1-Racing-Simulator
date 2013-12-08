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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Exceptions.FileFormatException;

public class GUI extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private Board board;
	private JButton exit, open, simulation, start, pause, clear, about;
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
		
		openButtonAction();
	}

	@Override
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
		catch(Exception exp){JOptionPane.showMessageDialog(this, "Track loading problem!");}
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
		param.setBounds((int)(screenWidth-500)/2, (int)(screenHeight-400)/2, 500, 400);
		JPanel optionsPanel = new JPanel();
		
		JButton uselessButton = new JButton("Useless Button");
		optionsPanel.add(uselessButton);
		
		param.add(optionsPanel, BorderLayout.CENTER);
		param.setVisible(true);
	}
}
