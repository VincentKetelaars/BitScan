package objects;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import constants.Constants;

public class CSVFileWriter {

	private boolean done = true; // False by default

	private TicketsFile ticketFile;
	private FileWriter fw;

	private ScheduledExecutorService executor;

	public CSVFileWriter(TicketsFile tf) {
		this.ticketFile = tf;
		openStream();
	}

	private void openStream() {
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new FileWriterRunner(), 0, Constants.TIMETOWAIT / 1000, TimeUnit.SECONDS);
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
		executor.shutdown();
	}

	public void update(TicketsFile tf) {
		this.ticketFile = tf;
		done = false;
	}

	public void forceUpdate() {
		executor.execute(new FileWriterRunner());
	}
	
	public boolean isDone() {
		return done;
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
			} finally{
				done = true;
			}
		}
	}
}
