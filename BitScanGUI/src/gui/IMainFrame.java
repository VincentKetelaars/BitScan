package gui;

import javax.swing.JFrame;

import objects.TicketHolder;
import objects.TicketsFile;

public interface IMainFrame {
	
	public JFrame getFrame();
	
	public void updateListOfTicketsAndLabels(TicketsFile ticketsFile);
	
	public void updateListOfTicketsAndLabels(TicketHolder[] data, TicketsFile ticketsFile);	
}
