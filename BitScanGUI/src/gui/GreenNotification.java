package gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import objects.TicketHolder;

public class GreenNotification extends JFrame {
	
	int WIDTH = 200;
	int HEIGHT = 150;

	private TicketHolder ticketHolder;
	private JFrame parentFrame;

	public GreenNotification(JFrame f, TicketHolder th) {
		this.parentFrame = f;
		this.ticketHolder = th;
		createFrame();
	}

	private void createFrame() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		setUndecorated(true);
		getContentPane().setBackground(Color.GREEN);
		
		int centerX = parentFrame.getX() + parentFrame.getWidth() / 2;
		int centerY = parentFrame.getY() + parentFrame.getHeight() / 2;
		setBounds(centerX - WIDTH / 2, centerY - HEIGHT / 2, WIDTH, HEIGHT);

		JLabel mainLabel = new JLabel(ticketHolder.getName() +" has been added!");
		mainLabel.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(mainLabel, BorderLayout.CENTER);
		
		setVisible(true);
	}
}
