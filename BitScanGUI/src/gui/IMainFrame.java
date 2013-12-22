package gui;

import java.util.Collection;

import javax.swing.JFrame;

import objects.TicketHolder;
import objects.TicketsFile;

public interface IMainFrame {
	
	public JFrame getFrame();
	
	public void updateListOfTicketsAndLabels(TicketsFile ticketsFile);
	
	public void updateListOfTicketsAndLabels(Collection<TicketHolder> data, TicketsFile ticketsFile);
	
	public void showLoadingNotification();
	
	public void stopLoadingNotification();
}
