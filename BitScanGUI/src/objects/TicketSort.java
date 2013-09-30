package objects;

import java.util.logging.Logger;

import constants.GeneralMethods;

public class TicketSort {
	
	private String name;
	private int price; // In cents and positive
	private int capacity;
	private int sold;
	private int checkedIn;
	private boolean doorSale;
	
	private final static Logger LOGGER = Logger.getLogger(TicketSort.class.getName()); 
	
	public TicketSort(String name) {
		setName(name);
	}
	
	public TicketSort(String ticketName, int price, int capacity, int sold, int checkedIn, boolean doorSale) {
		setName(ticketName);
		setPrice(price);
		setCapacity(capacity);
		setSold(sold);
		setCheckedIn(checkedIn);
		setDoorSale(doorSale);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		return GeneralMethods.convertPriceIntToEuroString(price);
	}
	
	public void addDoorSoldTickets(int n) {
		if (isDoorSale()) {
			LOGGER.warning(String.format("A ticket is sold add the door, while this is not authorized for: %s", getName()));
			return;
		}
		checkedIn += n;
		sold += n;
	}
	
	public void singleCheckIn() {
		checkedIn++;
	}

}
