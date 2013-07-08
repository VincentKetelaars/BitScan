package objects;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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
import java.awt.Cursor;

public class TicketHolderRenderer implements ListCellRenderer<TicketHolder> {
	
	final private String imageGreenBarPath = "images/green-bar.gif";
	final private int itemWidth = 200;
	final private int itemHeight = 120;
	final private int imageWidth = 16;
	final private int imageHeight = 80;
	final private int marginPanel = 10;
	final private int marginText = 15;
	
	public TicketHolderRenderer() {
		
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public Component getListCellRendererComponent(JList<? extends TicketHolder> list, TicketHolder value, int index, boolean isSelected, boolean cellHasFocus) {
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setLayout(new BorderLayout());	
		mainPanel.setPreferredSize(new Dimension(itemWidth, itemHeight));
		mainPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(marginPanel, 50, marginPanel, 50), BorderFactory.createLineBorder(Color.BLACK)));
		
		JPanel textPanel = new JPanel();
		textPanel.setOpaque(false);
		mainPanel.add(textPanel, BorderLayout.WEST);
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		
		JLabel idLabel = new JLabel(value.getId());
		idLabel.setFont(new Font("Serif", Font.BOLD, 16));
		idLabel.setBorder(new EmptyBorder(marginText, marginText, marginText, marginText));
		textPanel.add(idLabel);
		
		JLabel nameLabel = new JLabel(value.getName());
		nameLabel.setFont(new Font("Serif", Font.BOLD, 16));
		nameLabel.setBorder(new EmptyBorder(marginText, marginText, marginText, marginText));
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
		label.setBorder(new EmptyBorder(marginPanel, marginPanel, marginPanel, marginPanel));
		imagePanel.add(label, BorderLayout.CENTER);
		return mainPanel;
	}
	
	

}
