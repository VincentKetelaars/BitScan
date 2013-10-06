package objects;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

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
					th.getName()+","+th.getEmail()+","+th.getTicketSortName()+System.lineSeparator());
		}
		return sb.toString();
	}

	public void singleCheckIn(String ticketSortName) {
		for (TicketSort ts : ticketSorts) {
			if (ts.getName().equals(ticketSortName)) {
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
			ticketHolders.add(new TicketHolder(0,"",Constants.DOOR_SOLD_TICKET_COMMENT,GeneralMethods.getCurrentTime(),"","",ticketSort.getName()));
		}
	}
	
	/**
	 * Determine whether all invariants are actually true. Return false if one of them is not.
	 * @return
	 */
	public boolean invariant() {
		// TODO: Perhaps try assert?
		
		boolean realFile = file != null && file.exists() && file.isFile(); 
		// TODO: Check if file extension is correct as well
		boolean holdersExist = ticketHolders != null && ticketHolders.size() > 0;
		boolean eventNameExists = eventName != null && !eventName.isEmpty();
		boolean eventDescriptionExists = eventDescription != null && !eventDescription.isEmpty();
		boolean eventDataExist = startDate != null && endDate != null && startDate.isBefore(endDate);
		boolean sortsExist = ticketSorts != null && ticketSorts.size() > 0;
		boolean attributesExist = realFile && holdersExist && eventNameExists && eventDescriptionExists 
				&& eventDataExist && sortsExist;
		
		if (!attributesExist)
			return false;
		
		// Check if the number of ticketSort tickets sold, matches the number of TicketHolders with this ticketSortName
		HashMap<String, Integer> matchNames = new HashMap<String, Integer>();
		// Check if the number of ticketSorts tickets checked in, matches the number of TicketHolders checked in.
		HashMap<String, Integer> matchCheckedIn = new HashMap<String, Integer>();
		for (TicketSort t : ticketSorts) {
			matchNames.put(t.getName(), 0);
			matchCheckedIn.put(t.getName(), 0);
		}
		
		for (TicketHolder t : ticketHolders) {
			if (!t.invariant())
				return false;
			matchNames.put(t.getTicketSortName(), matchNames.get(t.getTicketSortName()) + 1); 
			if (t.isCheckedIn()) {
				matchCheckedIn.put(t.getTicketSortName(), matchCheckedIn.get(t.getTicketSortName()) + 1);
			}
		}
		
		for (TicketSort t : ticketSorts) {
			if (matchNames.get(t.getName()) != t.getSold())
				return false;
			if (matchCheckedIn.get(t.getName()) != t.getCheckedIn())
				return false;
			if (!t.invariant())
				return false;
		}
		
		boolean notTooManySold = getSold() <= getCapacity();
		boolean notTooManyAvailable = getAvailable() <= getCapacity() - getSold(); // Can be less if DoorSale
		boolean notTooManyCheckedIn = getCheckedIn() <= getSold();
		boolean ExactAmountOfTicketHolders = getTicketHolders().size() == getSold();
		
		return notTooManySold && notTooManyAvailable && notTooManyCheckedIn && ExactAmountOfTicketHolders;
	}
}
