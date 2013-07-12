package objects;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class TicketsFile {
	
	private File file;
	private HashMap<String, TicketHolder> ticketHolders;
	private int capacity;
	private int checkedIn;
	
	public TicketsFile(File file) {
		this.file = file;
	}

	public HashMap<String, TicketHolder> getTicketHolders() {
		return ticketHolders;
	}

	public void setTicketHolders(HashMap<String, TicketHolder> ticketHolders) {
		this.ticketHolders = ticketHolders;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TicketFile {\n");
		sb.append("file : "+ file.toPath().toString() +"\n");
		sb.append("ticket holders {\n");
		
		// Iterate over all TicketHolders
		Iterator<Entry<String, TicketHolder>> i = ticketHolders.entrySet().iterator();
		while (i.hasNext()) {
			sb.append("\t"+i.next().getValue()+"\n");
		}
		
		sb.append("};\n");
		return sb.toString();
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getCheckedIn() {
		return checkedIn;
	}

	public void setCheckedIn(int checkedIn) {
		this.checkedIn = checkedIn;
	}
}
