package objects;

import java.awt.Component;
import java.awt.Rectangle;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;

import constants.Constants;

public class TicketPanel extends JPanel {

	private TicketSort ticketSort;
	private JFormattedTextField amountTextField;

	public TicketPanel(TicketSort ticketSort) {
		this.setTicketSort(ticketSort);

		createPanel();
	}

	private void createPanel() {
		setBackground(Constants.BACKGROUND_COLOR);
		setLayout(null);

		JLabel nameLabel = new JLabel(getTicketSort().getTicketName());
		nameLabel.setBounds(0, 11, 103, 22);
		add(nameLabel);

		JLabel priceLabel = new JLabel(getTicketSort().getPriceRepresentation());
		priceLabel.setBounds(new Rectangle(182, 11, 71, 22));
		add(priceLabel);

		JLabel availabilityLabel = new JLabel(getTicketSort().getCapacity() - getTicketSort().getSold()+" / "+getTicketSort().getCapacity());
		availabilityLabel.setBounds(new Rectangle(309, 11, 55, 22));
		add(availabilityLabel);

		amountTextField = new JFormattedTextField(NumberFormat.getInstance());
		amountTextField.setBounds(393, 11, 29, 22);
		amountTextField.setAlignmentX(Component.RIGHT_ALIGNMENT);
		amountTextField.setColumns(2);

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
