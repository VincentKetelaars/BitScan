package objects;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import constants.Constants;

public class CSVFileWriter {

	private long TIMETOWAIT = 10; // In Seconds
	private boolean available = false; // False by default

	private TicketsFile ticketFile;
	private FileWriter fw;

	private ScheduledExecutorService executor;

	public CSVFileWriter(TicketsFile tf) {
		this.ticketFile = tf;
		openStream();
	}

	private void openStream() {
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new FileWriterRunner(), 0, TIMETOWAIT, TimeUnit.SECONDS);
		available = true;
	}

	private void write() {
		try {
			fw.write(Constants.identifierCSV + System.lineSeparator());
			fw.write(ticketFile.toCSVOutput());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void closeExecutor() {
		executor.shutdownNow();
		available = false;
	}

	public void update(TicketsFile tf) {
		this.ticketFile = tf;
	}

	public void forceUpdate(TicketsFile tf) {
		this.ticketFile = tf;
		write();
	}

	private class FileWriterRunner implements Runnable {

		public void run() {
			try {
				fw = new FileWriter(ticketFile.getFile(), false);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			write();
			try {
				fw.flush();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
