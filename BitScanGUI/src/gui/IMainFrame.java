package gui;

import javax.swing.JButton;
import javax.swing.JFrame;

import objects.TicketHolder;
import objects.TicketsFile;

public interface IMainFrame {
	
	public JFrame returnFrame();
	
	public void updateListOfTicketsAndLabels(TicketsFile ticketsFile);
	
	public void updateListOfTicketsAndLabels(TicketHolder[] data, TicketsFile ticketsFile);
	
	public String searchTextFieldContents();
	
	public void clickSearchButton();
}
