package objects;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import constants.Constants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Rectangle;
import java.awt.Component;

public class TicketPanel extends JPanel {

	private TicketSort ticketSort;
	private JTextField amountTextField;

	public TicketPanel(TicketSort ticketSort) {
		this.ticketSort = ticketSort;

		createPanel();
	}

	private void createPanel() {
		setBackground(Constants.BACKGROUND_COLOR);
		setLayout(null);

		JLabel nameLabel = new JLabel(ticketSort.getTicketName());
		nameLabel.setBounds(0, 11, 103, 22);
		add(nameLabel);

		JLabel priceLabel = new JLabel(ticketSort.getPriceRepresentation());
		priceLabel.setBounds(new Rectangle(182, 11, 71, 22));
		add(priceLabel);

		JLabel availabilityLabel = new JLabel(ticketSort.getCapacity() - ticketSort.getSold()+" / "+ticketSort.getCapacity());
		availabilityLabel.setBounds(new Rectangle(309, 11, 55, 22));
		add(availabilityLabel);

		amountTextField = new JTextField("0");
		amountTextField.setBounds(393, 11, 29, 22);
		amountTextField.setAlignmentX(Component.RIGHT_ALIGNMENT);
		amountTextField.setColumns(2);
		amountTextField.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		add(amountTextField);
	}

}
