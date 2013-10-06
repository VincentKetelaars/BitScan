package objects;

import org.joda.time.DateTime;

import constants.Constants;
import constants.GeneralMethods;

public class TicketHolder {

	private int table;
	private String id;
	private String comment;
	private DateTime dateTime;
	private String name;
	private String email;
	private String ticketSortName;

	public TicketHolder() {
		// Don't initialize
	}

	public TicketHolder(int table, String id, String comment, DateTime dateTime, String name, String email, String ticketSortName) {
		setTable(table);
		setId(id);
		setComment(comment);
		setDateTime(dateTime);
		setName(name);
		setEmail(email);
		setTicketSortName(ticketSortName);
	}

	public int getTable() {
		return table;
	}


	public void setTable(int table) {
		this.table = table;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public DateTime getDateTime() {
		return dateTime;
	}


	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	public String getTicketSortName() {
		return ticketSortName;
	}

	public void setTicketSortName(String items) {
		this.ticketSortName = items;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TicketHolder {\n");
		sb.append("\ttable : "+ table +"\n");
		sb.append("\tid : "+ id +"\n");
		sb.append("\tcomment : "+ comment +"\n");
		sb.append("\tdatetime : "+ dateTime +"\n");
		sb.append("\tname : "+ name +"\n");
		sb.append("\temail : "+ email +"\n");
		sb.append("\tticketsort : "+ ticketSortName +"\n");
		sb.append("};");
		return sb.toString();
	}
	
	/**
	 * Updates the DateTime to the current time
	 */
	public void checkIn() {
		if (dateTime == null)
			setDateTime(GeneralMethods.getCurrentTime());
		// TODO : Find a proper response if this ticket holder was already checked in.
	}
	
	public boolean isCheckedIn() {
		if (dateTime != null && !dateTime.isBeforeNow()) {
			// TODO: Appropriate response!
		}
		return dateTime != null;
	}

	public boolean invariant() {
		boolean tableExists = table >= 0;
		boolean idExists = id != null; // id is the empty string in case of doorsale
		boolean commentExists = comment != null; // comment can be empty
		boolean dateTimeExists = dateTime == null || dateTime.isBeforeNow(); // Either checked in already or not
		boolean nameExists = name != null; // May be empty in case of doorsale
		boolean emailExists = email != null; // May be empty in case of doorsale
		boolean ticketSortExists = ticketSortName != null;
		
		boolean allExist = tableExists && idExists && commentExists && dateTimeExists && nameExists && emailExists && ticketSortExists;
		if (!allExist)
			return false;
		
		boolean doorSale = comment.equals(Constants.DOOR_SOLD_TICKET_COMMENT) && name.equals("") && email.equals("")
				&& table == 0 && id.equals("");
		boolean preSale = name.length() > 0 && email.length() > 0 && table >= 0 && id.length() > 0;
		
		return doorSale || preSale;
	}

}
