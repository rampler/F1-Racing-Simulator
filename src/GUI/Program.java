package GUI;
import javax.swing.JFrame;

public class Program extends JFrame {

	private static final long serialVersionUID = 1L;
	private GUI gof;

	public Program() {
		setTitle("F1 Racing Simulator Track Editor");
		this.setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		gof = new GUI();
		gof.initialize(this.getContentPane());
		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
		
	}

	public static void main(String[] args) {
		new Program();
	}
}
