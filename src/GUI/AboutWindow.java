package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class AboutWindow extends JPanel{

	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	private URI uri;
	
	public AboutWindow() {	 
		this.setLayout(new BorderLayout());
	       try {                
	          image = ImageIO.read(new File("res/about.png"));
	          uri = new URI("https://github.com/rampler/F1-Racing-Simulator");
	       } 
	       catch (IOException ex) { JOptionPane.showMessageDialog(this, "Something goes wrong!"); }
	       catch (URISyntaxException e) { e.printStackTrace(); }
	       
	       JButton button = new JButton();
	       button.setText("Github: github.com/rampler/F1-Racing-Simulator");
	       button.setHorizontalAlignment(SwingConstants.LEFT);
	       button.setBorderPainted(false);
	       button.setOpaque(false);
	       button.setBackground(Color.WHITE);
	       button.setToolTipText(uri.toString());
	       button.addActionListener(new ActionListener(){
	    	   @Override public void actionPerformed(ActionEvent e) {
		        open(uri);
		      }});
	       JPanel bottomPanel = new JPanel();
	       bottomPanel.add(button);
	       this.add(bottomPanel, BorderLayout.SOUTH);
	    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);         
    }
    
    private static void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
          try {
            Desktop.getDesktop().browse(uri);
          } catch (IOException e) {}
        }
    }
}
