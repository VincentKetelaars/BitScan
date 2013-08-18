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
	private JLabel availabilityLabel;
	private JLabel priceLabel;

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

		priceLabel = new JLabel();
		setPriceLabel();
		priceLabel.setBounds(new Rectangle(182, 11, 71, 22));
		add(priceLabel);

		availabilityLabel = new JLabel();
		setAvailabilityLabel();
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

	public void updatePanel() {
		setPriceLabel();
		setAvailabilityLabel();
		amountTextField.setText("");
		this.invalidate();
	}
	
	private void setPriceLabel() {
		priceLabel.setText(getTicketSort().getPriceRepresentation());
	}
	
	private void setAvailabilityLabel() {
		availabilityLabel.setText(getTicketSort().getCapacity() - getTicketSort().getSold()+" / "+getTicketSort().getCapacity());
	}

}
