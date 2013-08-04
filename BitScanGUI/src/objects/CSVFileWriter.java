package objects;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import constants.Constants;

public class CSVFileWriter {

	private long TIMETOWAIT = 10000;
	private boolean available = false; // False by default

	private TicketsFile ticketFile;
	private FileWriter fw;

	private Thread thread;

	public CSVFileWriter(TicketsFile tf) {
		this.ticketFile = tf;
		openStream();
	}

	private void openStream() {
		thread = new Thread() {

			public synchronized void run() {
				try {
					fw = new FileWriter(ticketFile.getFile(), false);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				while (true) {
					try {
						wait(TIMETOWAIT);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					write();
				}
			}
		};
		thread.run();
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

	private void closeStream() {
		try {
			fw.close();
			thread.join();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		available = false;
	}
	
	public void update(TicketsFile tf) {
		this.ticketFile = tf;
	}
	
	public void forceUpdate(TicketsFile tf) {
		this.ticketFile = tf;
		write();
	}
}
