package GUI;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main program class - LookAndFeel changed to Substance
 * @author Sabina Rydzek, Kacper Furmañski, Mateusz Kotlarz
 *
 */
public class Program extends JFrame {

	private static final long serialVersionUID = 1L;
	private GUI gof;

	public Program() {
		try { UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel"); }
        catch (Exception exc) { SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(null, "Error!", "Message", JOptionPane.ERROR_MESSAGE);
            }
        }); }
		
		setTitle("F1 Racing Simulator Track Editor");
		this.setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		gof = new GUI();
		gof.initialize(this.getContentPane());
		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
            public void run() {
                    Program frame = new Program();
                    frame.setVisible(true);
            }
        });
	}
}
