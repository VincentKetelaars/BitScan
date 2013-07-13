package objects;

import org.joda.time.DateTime;

public class TicketHolder {

	private int table;
	private String id;
	private String comment;
	private DateTime dateTime;
	private String name;
	private String email;
	private TicketSort ticketSort;

	public TicketHolder() {
		// Don't initialize
	}

	public TicketHolder(int table, String id, String comment, DateTime dateTime, String name, String email, TicketSort ticketSort) {
		setTable(table);
		setId(id);
		setComment(comment);
		setDateTime(dateTime);
		setName(name);
		setEmail(email);
		setTicketSort(ticketSort);
	}

	public void setTicketHolder(int table, String id, String comment, DateTime dateTime, String name, String email, TicketSort ticketSort) {
		setTable(table);
		setId(id);
		setComment(comment);
		setDateTime(dateTime);
		setName(name);
		setEmail(email);
		setTicketSort(ticketSort);
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

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TicketHolder {\n");
		sb.append("\ttable : "+ table +"\n");
		sb.append("\tid : "+ id +"\n");
		sb.append("\tcomment : "+ comment +"\n");
		sb.append("\tdatetime : "+ dateTime +"\n");
		sb.append("\tname : "+ name +"\n");
		sb.append("\temail : "+ email +"\n");
		sb.append("\tticketsort : "+ ticketSort +"\n");
		sb.append("};");
		return sb.toString();
	}

	public TicketSort getTicketSort() {
		return ticketSort;
	}

	public void setTicketSort(TicketSort ticketSort) {
		this.ticketSort = ticketSort;
	}

}
