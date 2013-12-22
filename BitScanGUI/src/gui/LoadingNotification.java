package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.MediaTracker;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import constants.Constants;

public class LoadingNotification extends JFrame {

	private int WIDTH = 150;
	private int HEIGHT = 100;

	private JFrame parentFrame;

	public LoadingNotification(JFrame frame) {
		parentFrame = frame;
		createFrame();
	}

	private void createFrame() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		final ImageIcon loadingIcon = new ImageIcon(getClass().getResource(Constants.LOADING_OBJECT));
		loadingIcon.setImageObserver(this);
		loadingIcon.setDescription(Constants.LOADING);
		JLabel label = new JLabel(loadingIcon);

		setUndecorated(true);

		int centerX = parentFrame.getX() + parentFrame.getWidth() / 2;
		int centerY = parentFrame.getY() + parentFrame.getHeight() / 2;
		setBounds(centerX - WIDTH / 2, centerY - HEIGHT / 2, WIDTH, HEIGHT);

		getContentPane().add(label, BorderLayout.CENTER);

		// Wait till its ready loading before displaying
//		int counters = 0;
//		while (loadingIcon.getImageLoadStatus() != MediaTracker.COMPLETE) {
//			try {
//				Thread.sleep(10);
//				if (counters++ < 100)
//					break;
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
		setVisible(true);
		// TODO: Remove the first gray background
	}

}
