package gui;

import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;

import objects.DigitsDocumentFilter;
import objects.TicketSort;
import constants.Constants;

public class TicketPanel extends JPanel {

	private TicketSort ticketSort;
	private JTextField amountTextField;

	public TicketPanel(TicketSort ticketSort) {
		this.setTicketSort(ticketSort);

		createPanel();
	}

	private void createPanel() {
		setBackground(Constants.BACKGROUND_COLOR);
		setLayout(null);

		JLabel nameLabel = new JLabel(getTicketSort().getName());
		nameLabel.setBounds(0, 11, 103, 22);
		add(nameLabel);

		JLabel priceLabel = new JLabel(getTicketSort().getPriceRepresentation());
		priceLabel.setBounds(new Rectangle(182, 11, 71, 22));
		add(priceLabel);

		JLabel availabilityLabel = new JLabel(getTicketSort().getCapacity() - getTicketSort().getSold()+" / "+getTicketSort().getCapacity());
		availabilityLabel.setBounds(new Rectangle(309, 11, 55, 22));
		add(availabilityLabel);

		amountTextField = new JTextField(3);
		amountTextField.setBounds(393, 11, 29, 22);
		amountTextField.setAlignmentX(Component.RIGHT_ALIGNMENT);
		((AbstractDocument) amountTextField.getDocument()).setDocumentFilter(new DigitsDocumentFilter());

		add(amountTextField);
	}
	
	public void setDocumentListener(DocumentListener dl) {
		amountTextField.getDocument().addDocumentListener(dl);
	}
	
	public int numberOfTickets() {
		try {
			return Integer.parseInt(amountTextField.getText());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public TicketSort getTicketSort() {
		return ticketSort;
	}

	public void setTicketSort(TicketSort ticketSort) {
		this.ticketSort = ticketSort;
	}

}
