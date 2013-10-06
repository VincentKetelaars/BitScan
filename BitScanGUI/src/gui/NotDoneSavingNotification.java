package gui;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;


public class NotDoneSavingNotification extends JFrame {
	
	int WIDTH = 200;
	int HEIGHT = 150;

	private JFrame parentFrame;

	public NotDoneSavingNotification(JFrame f) {
		this.parentFrame = f;
		createFrame();
	}

	private void createFrame() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		setUndecorated(true);
		getContentPane().setBackground(Color.RED);
		
		int centerX = parentFrame.getX() + parentFrame.getWidth() / 2;
		int centerY = parentFrame.getY() + parentFrame.getHeight() / 2;
		setBounds(centerX - WIDTH / 2, centerY - HEIGHT / 2, WIDTH, HEIGHT);

		JLabel mainLabel = new JLabel("Please wait while your final changes are being saved.");
		mainLabel.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(mainLabel, BorderLayout.CENTER);
		
		setVisible(true);
	}

}
