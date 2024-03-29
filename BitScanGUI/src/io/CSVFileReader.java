package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

import objects.TicketHolder;
import objects.TicketSort;
import objects.TicketsFile;

import org.joda.time.DateTime;

import constants.Constants;
import constants.GeneralMethods;

public class CSVFileReader implements IFileReader {

	private File file;

	private JFrame frame;

	final static Charset ENCODING = StandardCharsets.UTF_8;

	public CSVFileReader(File file, JFrame frame) {
		this.file = file;
		this.frame = frame;
	}

	/**
	 * Read and parse file. 
	 * @return TicketsFile object
	 */
	public synchronized TicketsFile read() {
		try {
			return readFile();
		} catch (IOException e) {
			e.printStackTrace();
			GeneralMethods.showCompromisedFileErrorDialog(frame);
		} 
		return null;
	}

	private TicketsFile readFile() throws IOException {
		TicketsFile tf = new TicketsFile(file);
		HashMap<String, TicketHolder> ticketHolders = new HashMap<String, TicketHolder>();
		HashMap<String, TicketSort> ticketSorts = new HashMap<String, TicketSort>();

		BufferedReader reader = Files.newBufferedReader(file.toPath(), ENCODING);
		String line = reader.readLine();
		if (line == null || !line.contains(Constants.identifierCSV)) {// Should be equals!!!
			GeneralMethods.showCompromisedFileErrorDialog(frame);
			return null;
		}
		
		// Event line
		line = reader.readLine();
		String[] items = line.split(",");
		String eventName = items[0];
		String eventDescription = items[1];
		DateTime startDate = convertStringToDateTime(items[2]);
		DateTime endDate = convertStringToDateTime(items[3]);
		int numTicketSorts = Integer.parseInt(items[4]);

		// Kinds of Tickets
		for (int i = 0; i < numTicketSorts; i++) {
			line = reader.readLine();
			items = line.split(",");
			String ticketName = items[0];
			int capacity = Integer.parseInt(items[1]);
			int sold = Integer.parseInt(items[2]);
			int checkedIn = Integer.parseInt(items[3]);
			boolean doorSale = convertStringtoBoolean(items[4]);
			int price = (int) (Double.parseDouble(items[5]) * 100);

			TicketSort ts = new TicketSort(ticketName, price, capacity, sold, checkedIn, doorSale);
			ticketSorts.put(ticketName, ts);
		}

		// Tickets
		while ((line = reader.readLine()) != null) {
			TicketHolder th = parseCSVLine(line);
			if (th != null) {
				ticketHolders.put(th.getId(), th);
			} else {
				break;
			}
		}
		
		reader.close();

		tf.setTicketHolders(ticketHolders);
		tf.setEventName(eventName);
		tf.setEventDescription(eventDescription);
		tf.setStartDate(startDate);
		tf.setEndDate(endDate);
		tf.setTicketSorts(ticketSorts);
		return tf;
	}

	/**
	 * Parse a single line, which is one TicketHolder
	 * @param line
	 * @return
	 */
	private TicketHolder parseCSVLine(String line) {
		TicketHolder th;
		String[] items = line.split(",");

		if (items.length != 7) {
			// Something is wrong!
			GeneralMethods.showCompromisedFileErrorDialog(frame);
			return null;
		} else {
			int table = Integer.parseInt(items[0]);
			String id = items[1];
			String comment = items[2];
			DateTime dateTime = convertStringToDateTime(items[3]);
			String name = items[4];
			String email = items[5];
			String ticketSortName = items[6];

			th = new TicketHolder(table, id, comment, dateTime, name, email, ticketSortName);
		}
		return th;
	}

	/**
	 * Parse a dateTime String of the form: 0000-00-00 00:00:00
	 * @param dateTime
	 * @return
	 */
	private DateTime convertStringToDateTime(String dateTime) {
		String[] items = dateTime.split("[-:\\s]");
		int year = Integer.parseInt(items[0]);
		int month = Integer.parseInt(items[1]);
		int day = Integer.parseInt(items[2]);
		int hour = Integer.parseInt(items[3]);
		int minute = Integer.parseInt(items[4]);
		int second = Integer.parseInt(items[5]);
		if (year == 0 || month == 0 || day == 0) {
			return null;
		}
		DateTime dt = new DateTime(year, month, day, hour, minute, second);
		return dt;
	}

	/**
	 * Parse true or false string to boolean.
	 * @param string
	 * @return
	 */
	private boolean convertStringtoBoolean(String s) {
		switch (s.toLowerCase()) {
		case "true": 
			return true;
		case "false":
			return false;
		default: return false;
		}		
	}

}
