package constants;

import java.awt.Color;
import java.awt.Font;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * List important, project wide constants. 
 * @author Vincent
 *
 */
public class Constants {
	
	/* Layout */
	
	// Color
	public static final Color MAIN_BACKGROUND_COLOR = Color.BLUE;
	public static final Color BACKGROUND_COLOR = Color.WHITE; 
	
	// Font
	public static final String STANDARD_FONT = "SansSerif";
	public static final Font DEFAULT_FONT = new Font(Constants.STANDARD_FONT, Font.PLAIN, 12);
	public static final Font HEADER_FONT = new Font(Constants.STANDARD_FONT, Font.PLAIN, 18);
	public static final Font TITLE_FONT = new Font(Constants.STANDARD_FONT, Font.PLAIN, 24);
	public static final Font TICKET_FONT = new Font(Constants.STANDARD_FONT, Font.PLAIN, 16);
	
	// Border
	public static final int RIGHT_PANEL_SIDE_MARGIN = 20;
	public static final Border STATISTICS_LABEL_BORDER = new EmptyBorder(0, 0, 5, 0);
	public static final Border SORT_BUTTON_BORDER = new EmptyBorder(5, 5, 5, 5);
	
	// Size
	public static final int PREFERRED_APP_WIDTH = 500;
	public static final int PREFERRED_APP_HEIGHT = 400;
	
	// Waiting times
	public static final long TIMETODOSAVEUPDATE = 10000; // In miliseconds
	public static final long TIMESHOWNOTIFICATION = 3000; // In miliseconds
	
	// Dialogs
	public static final String FILE_CHOOSER_TITLE = "Import";
	
	/* Strings */
	
	// Main
	public static final String TITLE = "BitScan";
	public static final String ENTRANCE_TAB_TIP = "Entrance";
	public static final String ENTRANCE_TAB_LABEL = "Entrance";
	
	// Buttons and Labels
	public static final String EVENT_TITLE_LABEL = "Event";
	public static final String SEARCH_BUTTON = "Search";
	public static final String LOAD_BUTTON = "Load";
	public static final String STATISTICS_LABEL_TITLE = "Statistics";
	public static final String CAPACITY_LABEL_TITLE = "Capacity";
	public static final String CHECKED_IN_LABEL_TITLE = "Checked-In";
	public static final String AVAILABLE_LABEL_TITLE = "Available Tickets";
	public static final String PURCHASE_TICKET_LABEL_TITLE = "Purchase Ticket";
	public static final String PURCHASE_TICKET_BUTTON_TITLE = "Purchase Tickets";
	public static final String LOADING = "Loading";
	
	// Unicode
	public static final String euro = "\u20ac";
	
	// CSV
	public static final String identifierCSV = "identifier";
	public static final String CSV_DESCRIPTION = "CSV file";
	public static final String CSV_EXTENSION = "csv";
	
	// FileWriter extensions, case ignored
	public static final String[] VALID_EXTENSIONS = {CSV_EXTENSION};
	
	// Images
	// Note that these will only work with the ant build, if the images folder is in the root
	public static final String GREEN_BAR = "/images/green-bar.gif";
	public static final String GRAY_BAR = "/images/gray-bar.png";
	public static final String RED_BAR = "/images/red-bar.png";
	public static final String LOADING_OBJECT = "/images/loading.gif";
	
	// Messages
	public static final String LOAD_FILE_ERROR_MESSAGE = "The file contents is compromised!";
	public static final String NOT_CSV_FILE_ERROR_MESSAGE = "You can only import CSV files!";
	public static final String LOAD_FILE_ERROR_TITLE = "Import error";	
	public static final String LOAD_MULTIPLE_FILES_ERROR_MESSAGE = "You can only import one file!";
	public static final String INVARIANT_FAIL_TITLE = "Invariant Failure";
	public static final String INVARIANT_FAILS = "The invariant check has failed! This program can no longer be trusted";
	public static final String CHOOSE_UNDO_CHECK_IN = "Do you wish to undo the check in of %s?";
	public static final String CHOOSE_UNDO_CHECK_IN_TITLE = "Undo check in";
	public static final String DOOR_SALE_CONFIRMATION = "Are you sure you want to sell the following tickets? \n%s";
	public static final String DOOR_SALE_CONFIRMATION_TITLE = "Door sale confirmation";

	// Door sale
	public static final String DOOR_SOLD_TICKET_COMMENT = "Doorsale";

	// Sorting
	public static enum SortArrayBy {BARCODE, NAME, EMAIL}
}
