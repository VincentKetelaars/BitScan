package objects;

import gui.IMainFrame;
import gui.MainFrame;
import gui.NotDoneSavingNotification;
import io.CSVFileReader;
import io.CSVFileWriter;
import io.IFileReader;
import io.IFileWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import constants.Constants;
import constants.Constants.SortArrayBy;
import constants.GeneralMethods;

public class MainLogic {

	private TicketsFile ticketsFile;
	private IFileWriter fileWriter;
	private IMainFrame mainFrame;

	public MainLogic(MainFrame mf) {
		this.mainFrame = mf;
	}

	public JFrame mainFrame() {
		return mainFrame.getFrame();
	}

	public void openFileChooser() {
		JFileChooser fc = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter(Constants.CSV_DESCRIPTION, Constants.CSV_EXTENSION);
		fc.setFileFilter(filter);
		int returnVal = fc.showDialog(mainFrame(), Constants.FILE_CHOOSER_TITLE);		

		if (returnVal == JFileChooser.APPROVE_OPTION) { // Continue with appropriate path
			File f = fc.getSelectedFile();				
			runFileReader(f);
		} 
	}

	public void runFileReader(File f) {
		final IFileReader fr = new CSVFileReader(f, mainFrame());
		Runnable r = new Runnable() {

			@Override
			public void run() {
				ticketsFile = fr.read();
				invariant(); // Right after reading, everything should be in order!
				if (ticketsFile != null) {
					mainFrame.updateListOfTicketsAndLabels(ticketsFile);
					startFileWriter();
				}
			}
		};
		r.run();
	}

	public void startFileWriter() {
		fileWriter = new CSVFileWriter(ticketsFile);
		fileWriter.open();
	}

	public TicketHolder[] sortArraybySortBy(TicketHolder[] data, SortArrayBy sortBy, boolean removeDoorSold) {
		ArrayList<TicketHolder> filtered = new ArrayList<TicketHolder>(data.length);
		for (TicketHolder th : data) {
			// Only show tickets that have not been sold at the door!
			if (!th.getComment().equals(Constants.DOOR_SOLD_TICKET_COMMENT)) {
				filtered.add(th);
			}
		}
		return sortArraybySortBy(filtered.toArray(new TicketHolder[0]), sortBy);
	}

	public TicketHolder[] sortArraybySortBy(TicketHolder[] data, SortArrayBy sortBy) {
		// Sorted list
		switch (sortBy) {
		case BARCODE: 
			Arrays.sort(data, IDComparator);
			break;
		case NAME: 
			Arrays.sort(data, NameComparator);
			break;
		case EMAIL: 
			Arrays.sort(data, EmailComparator);
			break;
		default : break;
		}
		return data;
	}

	public ArrayList<TicketHolder> search(String s, SortArrayBy sortBy) {
		if (ticketsFile == null || ticketsFile.getTicketHolders() == null)
			return null;
		ArrayList<TicketHolder> l = new ArrayList<TicketHolder>();
		switch (sortBy) {
		case BARCODE: // Return if the search is the first part of the id
			for (TicketHolder t : ticketsFile.getTicketHolders()) {
				if (t.getId().startsWith(s)) {
					l.add(t);
				}
			}
			break;
		case NAME: // Return if the search is contained within the name, case insensitive
			for (TicketHolder t : ticketsFile.getTicketHolders()) {
				if (t.getName().toLowerCase().contains(s.toLowerCase())) {
					l.add(t);
				}
			}
			break;
		case EMAIL: // Return if the search is contained within the email, case insensitive
			for (TicketHolder t : ticketsFile.getTicketHolders()) {
				if (t.getEmail().toLowerCase().contains(s.toLowerCase())) {
					l.add(t);
				}
			}
			break;
		default: 
			return null;
		}
		return l;
	}

	/**
	 * This method responds to a single click on a single ticket.
	 * @param TicketHolder ticketHolder represents the ticket that was clicked
	 * @return 0: ticketHolder has undergone no changes. 1: ticketHolder has been checked in. 2: ticketHolder's check in is undone 
	 */
	public int singleTicketClicked(TicketHolder ticketHolder) {
		int ret = 0; // Default return value, nothing happens
		if (!ticketHolder.isCheckedIn()) {
			ticketHolder.checkIn();
			ticketsFile.singleCheckIn(ticketHolder.getTicketSortName());
			ret = 1;
		} else {
			// This ticket has already been checked in
			int result = GeneralMethods.showYesNoDialog(mainFrame(), String.format(Constants.CHOOSE_UNDO_CHECK_IN, ticketHolder.getName()), Constants.CHOOSE_UNDO_CHECK_IN_TITLE);
			if (result == 0) {// Yes
				ticketHolder.undoCheckIn();
				ticketsFile.undoCheckIn(ticketHolder.getTicketSortName());
				ret = 2;					
			} else { // No or closed
				// TODO: Perhaps some action is required?
				ret = 0; // Not necessary to assign again			
			}
		}
		updateFileWriter();
		return ret;
	}


	Comparator<TicketHolder> IDComparator = new Comparator<TicketHolder>() {

		@Override
		public int compare(TicketHolder o1, TicketHolder o2) {
			return o1.getId().compareTo(o2.getId());
		}

	};

	Comparator<TicketHolder> NameComparator = new Comparator<TicketHolder>() {

		@Override
		public int compare(TicketHolder o1, TicketHolder o2) {
			return o1.getName().compareTo(o2.getName());
		}

	};

	Comparator<TicketHolder> EmailComparator = new Comparator<TicketHolder>() {

		@Override
		public int compare(TicketHolder o1, TicketHolder o2) {
			return o1.getEmail().compareTo(o2.getEmail());
		}

	};	

	public void windowClosing() {
		if (fileWriter != null && !fileWriter.isDone()) {
			fileWriter.forceUpdate();
		}

		if (fileWriter != null) {
			// If it takes long this notification will be shown.
			NotDoneSavingNotification notification = startNotDoneSavingNotification();

			while (!fileWriter.isDone()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			notification.dispose();
		}
	}

	/**
	 * This method presents the user with the option to renege on the door sale. 
	 * If the user continues, ticketsFile is updated and the fileWriter updated.
	 * @param doorSale
	 * @return 0: Sold tickets. 1: No tickets sold.
	 */
	public int addDoorSoldTicket(HashMap<String, Integer> doorSale) {
		// Ask for permission first
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Integer> d : doorSale.entrySet()) {
			sb.append(d.getValue() + " " + d.getKey() + "\n");
		}
		String dialogPermissionQuestion = String.format(Constants.DOOR_SALE_CONFIRMATION, sb.toString());
		int ret = GeneralMethods.showYesNoDialog(mainFrame(), dialogPermissionQuestion, Constants.DOOR_SALE_CONFIRMATION_TITLE);
		if (ret == 0) { // Yes
			// Set ticketSfile
			for (Map.Entry<String, Integer> d : doorSale.entrySet()) {
				ticketsFile.addDoorSoldTicket(d.getKey(), d.getValue());
			}
			updateFileWriter();
		} else { // No or closed
			// TODO: Do something here?			
		}
		return ret;
	}

	public TicketsFile getTicketsFile() {
		return ticketsFile;
	}

	protected NotDoneSavingNotification startNotDoneSavingNotification() {
		final NotDoneSavingNotification notification = new NotDoneSavingNotification(mainFrame.getFrame());
		new Thread(){
			@Override
			public void run() {
				try {
					Thread.sleep(Constants.TIMETODOSAVEUPDATE); // time after which pop up will disappear
					notification.dispose();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
		return notification;
	}
	
	private void updateFileWriter() {
		invariant();
		fileWriter.update(ticketsFile);
	}
	
	/**
	 * This method determines whether the current state of the MainLogic is correct,
	 * insofar this can be checked statically.
	 * @return True if the state is correct
	 */
	private void invariant() {
		if (ticketsFile == null)
			return;
		if (!ticketsFile.invariant())
			GeneralMethods.showInvariantErrorDialog(mainFrame());
	}
}
