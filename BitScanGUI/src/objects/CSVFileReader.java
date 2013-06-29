package objects;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;

import org.joda.time.DateTime;

public class CSVFileReader {

	private File file;

	final static Charset ENCODING = StandardCharsets.UTF_8;

	public CSVFileReader(File file) {
		this.file = file;
	}

	/**
	 * Read and parse file. 
	 * @return TicketsFile object
	 */
	public TicketsFile read() {
		try {
			return readFile();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}

	private TicketsFile readFile() throws IOException {
		TicketsFile tf = new TicketsFile(file);
		HashMap<String, TicketHolder> ticketHolders = new HashMap<String, TicketHolder>();

		BufferedReader reader = Files.newBufferedReader(file.toPath(), ENCODING);
		String line = null;
		while ((line = reader.readLine()) != null) {
			TicketHolder th = parseCSVLine(line);
			ticketHolders.put(th.getId(), th);
		}      
		
		tf.setTicketHolders(ticketHolders);
		return tf;
	}
	
	/**
	 * Parse a single line, which is one TicketHolder
	 * @param line
	 * @return
	 */
	private TicketHolder parseCSVLine(String line) {
		TicketHolder th = new TicketHolder();
		String[] items = line.split(",");
		
		if (items.length != 6) {
			// Something is wrong!
		} else {
			int table = Integer.parseInt(items[0]);
			String id = items[1];
			String comment = items[2];
			DateTime dateTime = convertStringToDateTime(items[3]);
			String name = items[4];
			String email = items[5];
			
			th.setTicketHolder(table, id, comment, dateTime, name, email);
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
		if (year == 0 && month == 0 && day == 0 && hour == 0 && minute == 0 && second == 0) {
			return null;
		}
		DateTime dt = new DateTime(year, month, day, hour, minute, second);
		return dt;
	}

}
