package objects;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.joda.time.DateTime;

import constants.Constants;
import constants.GeneralMethods;

public class TicketsFile {
	
	private File file;
	private ArrayList<TicketHolder> ticketHolders;
	private String eventName;
	private String eventDescription;
	private DateTime startDate;
	private DateTime endDate;
	private ArrayList<TicketSort> ticketSorts;	
	
	public TicketsFile(File file) {
		this.file = file;
	}

	public ArrayList<TicketHolder> getTicketHolders() {
		return ticketHolders;
	}

	public void setTicketHolders(ArrayList<TicketHolder> ticketHolders2) {
		this.ticketHolders = ticketHolders2;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TicketFile {\n");
		sb.append("file : "+ getFile().toPath().toString() +"\n");
		sb.append("ticket holders {\n");
		
		// Iterate over all TicketHolders
		for (TicketHolder i : ticketHolders) {
			sb.append("\t"+ i +"\n");
		}
		
		sb.append("};\n");
		return sb.toString();
	}

	public int getCapacity() {
		int cap = 0;
		for (TicketSort ts : getTicketSorts()) {
			cap += ts.getCapacity();
		}
		return cap;
	}

	public int getCheckedIn() {
		int checkedIn = 0;
		for (TicketSort ts : getTicketSorts()) {
			checkedIn += ts.getCheckedIn();
		}
		return checkedIn;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public DateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}

	public DateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}

	public ArrayList<TicketSort> getTicketSorts() {
		return ticketSorts;
	}

	public void setTicketSorts(ArrayList<TicketSort> ticketSorts) {
		this.ticketSorts = ticketSorts;
	}

	public File getFile() {
		return file;
	}
	
	public String toCSVOutput() {
		StringBuilder sb = new StringBuilder();
		sb.append(eventName+","+eventDescription+","+GeneralMethods.dateTimeToString(startDate)+","+
					GeneralMethods.dateTimeToString(endDate)+","+ticketSorts.size()+System.lineSeparator());
		for (TicketSort ts : ticketSorts) {
			sb.append(ts.getName()+","+ts.getCapacity()+","+ts.getSold()+","+ts.getCheckedIn()+","+ts.isDoorSale()+","+
					GeneralMethods.convertPriceIntToString(ts.getPrice())+System.lineSeparator());
		}
		for (TicketHolder th : ticketHolders) {
			sb.append(th.getTable()+","+th.getId()+","+th.getComment()+","+GeneralMethods.dateTimeToString(th.getDateTime())+","+
					th.getName()+","+th.getEmail()+","+th.getTicketSort().getName()+System.lineSeparator());
		}
		return sb.toString();
	}
	
	public void singleCheckIn(TicketSort ticketSort) {
		for (TicketSort ts : ticketSorts) {
			if (ts.getName().equals(ticketSort.getName())) {
				ts.singleCheckIn();
			}
		}
	}
}
