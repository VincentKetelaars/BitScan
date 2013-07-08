package objects;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

public class TicketHolderRenderer implements ListCellRenderer<TicketHolder> {
	
	final private String imageGreenBarPath = "images/green-bar.gif";
	final private int width = 250;
	final private int height = 100;
	final private int imageWidth = 16;
	final private int imageHeight = 80;
	final private int marginImage = 10;
	
	public TicketHolderRenderer() {
		
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public Component getListCellRendererComponent(JList<? extends TicketHolder> list, TicketHolder value, int index, boolean isSelected, boolean cellHasFocus) {
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setLayout(new BorderLayout());	
		mainPanel.setPreferredSize(new Dimension(width, height));
		
		JPanel textPanel = new JPanel();
		textPanel.setOpaque(false);
		mainPanel.add(textPanel, BorderLayout.WEST);
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		
		JLabel idLabel = new JLabel(value.getId());
		textPanel.add(idLabel);
		
		JLabel nameLabel = new JLabel(value.getName());
		textPanel.add(nameLabel);	
		
		JPanel imagePanel = new JPanel();
		imagePanel.setOpaque(false);
		mainPanel.add(imagePanel, BorderLayout.EAST);
		imagePanel.setLayout(new BorderLayout());	
		
		Image img = null;
		try {
			img = ImageIO.read(new File(imageGreenBarPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		img = img.getScaledInstance(imageWidth, imageHeight, Image.SCALE_FAST);
		JLabel label = new JLabel(new ImageIcon(img));
		label.setBorder(new EmptyBorder(marginImage, marginImage, marginImage, marginImage));
		imagePanel.add(label, BorderLayout.CENTER);
		return mainPanel;
	}
	
	

}
