package objects;

import gui.GreenNotification;
import gui.IMainFrame;
import gui.MainFrame;
import gui.NotDoneSavingNotification;
import gui.TicketPanel;
import io.CSVFileReader;
import io.CSVFileWriter;
import io.IFileReader;
import io.IFileWriter;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import constants.Constants;
import constants.Constants.SortArrayBy;
import constants.GeneralMethods;

public class MainLogic {
	
	private TicketsFile ticketsFile;
	private IFileWriter csvFileWriter;
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

		if (returnVal == fc.APPROVE_OPTION) {			 // Continue with appropriate path
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
				if (ticketsFile != null) {
					mainFrame.updateListOfTicketsAndLabels(ticketsFile);
					startFileWriter();
				}
			}
		};
		r.run();
	}
	
	public void startFileWriter() {
		csvFileWriter = new CSVFileWriter(ticketsFile);
		csvFileWriter.open();
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
	
	public void singleTicketFound(TicketHolder ticketHolder) {
		if (ticketHolder.getDateTime() == null) {
			ticketHolder.checkIn();
			ticketsFile.singleCheckIn(ticketHolder.getTicketSort());

			final GreenNotification notification = new GreenNotification(mainFrame(), ticketHolder);
			new Thread(){
				@Override
				public void run() {
					try {
						Thread.sleep(Constants.TIMESHOWNOTIFICATION); // time after which pop up will disappear
						notification.dispose();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				};
			}.start();
		} else {
			// This ticket has already been checked!
		}
		mainFrame.updateListOfTicketsAndLabels(new TicketHolder[]{ticketHolder}, ticketsFile);
		csvFileWriter.update(ticketsFile);
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
		if (csvFileWriter != null && !csvFileWriter.isDone()) {
			csvFileWriter.forceUpdate();
		}

		if (csvFileWriter != null) {
			// If it takes long this notification will be shown.
			NotDoneSavingNotification notification = startNotDoneSavingNotification();

			while (!csvFileWriter.isDone()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			notification.dispose();
		}
	}

	public void addDoorSoldTicket(int n, TicketSort ticketSort) {
		for (TicketSort ts : ticketsFile.getTicketSorts()) {
			if (ticketSort.getName().equals(ts.getName())) {
				ts.addDoorSoldTickets(n);
			}
		}


		// Add TicketHolder(s) to ticketsFile

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
}
