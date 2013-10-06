/**
 * 
 */
package gui;

import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import constants.Constants;

/**
 * @author Vincent
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Set the look and feel to the system default
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		// Set Fonts
		UIManager.put("Label.font", Constants.DEFAULT_FONT);
		UIManager.put("TextField.font", Constants.DEFAULT_FONT);
		UIManager.put("Button.font", Constants.DEFAULT_FONT);
		UIManager.put("TabbedPane.font", Constants.DEFAULT_FONT);
		
		// Start the application 
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
