package objects;

import java.io.File;
import java.util.ArrayList;

import org.joda.time.DateTime;

import constants.Constants;
import constants.GeneralMethods;

/**
 * This TicketsFile most importantly holds everything there is to know about the event.
 * Ticketholders can be door sold tickets. These should be ignored when displayed.
 * @author Vincent Ketelaars
 *
 */
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
		return (ArrayList<TicketHolder>) ticketHolders.clone();
	}

	public void setTicketHolders(ArrayList<TicketHolder> ticketHolders) {
		this.ticketHolders = ticketHolders;
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

	public int getSold() {
		int sold = 0;
		for (TicketSort ts : ticketSorts) {
			sold += ts.getSold();
		}
		return sold;
	}
	
	public int getAvailable() {
		int available = 0;
		for (TicketSort ts : ticketSorts) {
			if (ts.isDoorSale()) {
				available += ts.getCapacity() - ts.getSold();
			}
		}
		return available;
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

	public void addDoorSoldTicket(int n, TicketSort ticketSort) {
		for (TicketSort ts : getTicketSorts()) {
			if (ticketSort.getName().equals(ts.getName())) {
				ts.addDoorSoldTickets(n);
			}
		}
		for (int i = 0; i < n; i ++) {
			ticketHolders.add(new TicketHolder(0,"",Constants.DOOR_SOLD_TICKET_COMMENT,GeneralMethods.getCurrentTime(),"","",ticketSort));
		}
	}
}
