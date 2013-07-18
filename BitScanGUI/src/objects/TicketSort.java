package objects;


public class TicketSort {
	
	private String ticketName;
	private int price; // In cents and positive
	private int capacity;
	private int sold;
	private int checkedIn;
	private boolean doorSale;
	
	public TicketSort(String ticketName) {
		setTicketName(ticketName);
	}
	
	public TicketSort(String ticketName, int price, int capacity, int sold, int checkedIn, boolean doorSale) {
		setTicketName(ticketName);
		setPrice(price);
		setCapacity(capacity);
		setSold(sold);
		setCheckedIn(checkedIn);
		setDoorSale(doorSale);
	}

	public String getTicketName() {
		return ticketName;
	}

	public void setTicketName(String ticketName) {
		this.ticketName = ticketName;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getSold() {
		return sold;
	}

	public void setSold(int sold) {
		this.sold = sold;
	}

	public boolean isDoorSale() {
		return doorSale;
	}

	public void setDoorSale(boolean doorSale) {
		this.doorSale = doorSale;
	}

	public int getCheckedIn() {
		return checkedIn;
	}

	public void setCheckedIn(int checkedIn) {
		this.checkedIn = checkedIn;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
	public String getPriceRepresentation() {
		return "€" + price / 100 + "," + String.format("%02d", price % 100);
	}

}
