package objects;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

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
	private HashMap<String, TicketHolder> ticketHolders;
	private String eventName;
	private String eventDescription;
	private DateTime startDate;
	private DateTime endDate;
	private HashMap<String, TicketSort> ticketSorts;
	private int doorSoldTickets;

	private final static Logger LOGGER = Logger.getLogger(TicketSort.class.getName()); 

	public TicketsFile(File file) {
		this.file = file;
		doorSoldTickets = 0;
	}

	public HashMap<String, TicketHolder> getTicketHolders() {
		return (HashMap<String, TicketHolder>) ticketHolders.clone(); // Shallow copy
	}

	public void setTicketHolders(HashMap<String, TicketHolder> ticketHolders) {
		this.ticketHolders = ticketHolders;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TicketFile {\n");
		sb.append("file : "+ getFile().toPath().toString() +"\n");
		sb.append("ticket holders {\n");

		// Iterate over all TicketHolders
		for (TicketHolder i : ticketHolders.values()) {
			sb.append("\t"+ i +"\n");
		}

		sb.append("};\n");
		return sb.toString();
	}

	public int getCapacity() {
		int cap = 0;
		for (TicketSort ts : getTicketSorts().values()) {
			cap += ts.getCapacity();
		}
		return cap;
	}

	public int getCheckedIn() {
		int checkedIn = 0;
		for (TicketSort ts : getTicketSorts().values()) {
			checkedIn += ts.getCheckedIn();
		}
		return checkedIn;
	}

	public int getSold() {
		int sold = 0;
		for (TicketSort ts : ticketSorts.values()) {
			sold += ts.getSold();
		}
		return sold;
	}

	public int getAvailable() {
		int available = 0;
		for (TicketSort ts : ticketSorts.values()) {
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

	public HashMap<String, TicketSort> getTicketSorts() {
		return ticketSorts;
	}

	public void setTicketSorts(HashMap<String, TicketSort> ticketSorts) {
		this.ticketSorts = ticketSorts;
	}

	public File getFile() {
		return file;
	}

	public String toCSVOutput() {
		StringBuilder sb = new StringBuilder();
		sb.append(eventName+","+eventDescription+","+GeneralMethods.dateTimeToString(startDate)+","+
				GeneralMethods.dateTimeToString(endDate)+","+ticketSorts.size()+System.lineSeparator());
		for (TicketSort ts : ticketSorts.values()) {
			sb.append(ts.getName()+","+ts.getCapacity()+","+ts.getSold()+","+ts.getCheckedIn()+","+ts.isDoorSale()+","+
					GeneralMethods.convertPriceIntToString(ts.getPrice())+System.lineSeparator());
		}
		for (TicketHolder th : ticketHolders.values()) {
			sb.append(th.getTable()+","+th.getId()+","+th.getComment()+","+GeneralMethods.dateTimeToString(th.getDateTime())+","+
					th.getName()+","+th.getEmail()+","+th.getTicketSortName()+System.lineSeparator());
		}
		return sb.toString();
	}

	public void singleCheckIn(String ticketSortName) {
		ticketSorts.get(ticketSortName).singleCheckIn();
	}

	public void undoCheckIn(String ticketSortName) {
		ticketSorts.get(ticketSortName).undoCheckIn();
	}

	public void addDoorSoldTicket(String ticketSortName, int n) {
		ticketSorts.get(ticketSortName).addDoorSoldTickets(n);
		for (int i = 0; i < n; i ++) {
			String id = ""+(doorSoldTickets++); // TODO: Find better ID
			ticketHolders.put(id, new TicketHolder(0,id,Constants.DOOR_SOLD_TICKET_COMMENT,GeneralMethods.getCurrentTime(),"","",ticketSortName));
		}
	}

	/**
	 * Determine whether all invariants are actually true. Return false if one of them is not.
	 * @return
	 */
	public boolean invariant() {
		try {
			assert file != null && file.exists() && file.isFile(); 
			// TODO: Check if file extension is correct as well
			assert ticketHolders != null && ticketHolders.size() > 0;
			assert eventName != null && !eventName.isEmpty();
			assert eventDescription != null && !eventDescription.isEmpty();
			assert startDate != null && endDate != null && startDate.isBefore(endDate);
			assert ticketSorts != null && ticketSorts.size() > 0;

			// Check if the number of ticketSort tickets sold, matches the number of TicketHolders with this ticketSortName
			HashMap<String, Integer> matchNames = new HashMap<String, Integer>();
			// Check if the number of ticketSorts tickets checked in, matches the number of TicketHolders checked in.
			HashMap<String, Integer> matchCheckedIn = new HashMap<String, Integer>();
			for (TicketSort t : ticketSorts.values()) {
				matchNames.put(t.getName(), 0);
				matchCheckedIn.put(t.getName(), 0);
			}

			for (TicketHolder t : ticketHolders.values()) {
				t.invariant();
				matchNames.put(t.getTicketSortName(), matchNames.get(t.getTicketSortName()) + 1); 
				if (t.isCheckedIn()) {
					matchCheckedIn.put(t.getTicketSortName(), matchCheckedIn.get(t.getTicketSortName()) + 1);
				}
			}

			for (TicketSort t : ticketSorts.values()) {
				assert matchNames.get(t.getName()) == t.getSold() : matchNames.get(t.getName()) + " " + t.getSold();
				assert matchCheckedIn.get(t.getName()) == t.getCheckedIn() : matchCheckedIn.get(t.getName()) + " " + t.getCheckedIn();
				t.invariant();
			}

			assert getSold() <= getCapacity();
			assert getAvailable() <= getCapacity() - getSold(); // Can be less if DoorSale
			assert getCheckedIn() <= getSold();
			assert getTicketHolders().size() == getSold();
		} catch (AssertionError ae) {
			ae.printStackTrace();
			return false;
		}

		return true;
	}
}
