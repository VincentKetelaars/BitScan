package constants;

import java.awt.Color;
import java.awt.Font;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.joda.time.DateTime;

/**
 * List important, project wide constants. 
 * @author Vincent
 *
 */
public class Constants {
	
	/* Layout */
	
	// Color
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
	
	/* Strings */
	
	// Unicode
	public static final String euro = "\u20ac";
	
	// CSV
	public static final String identifierCSV = "identifier";
	public static final String EXTENSION = "csv";
	
	// Images
	// Note that these will only work with the ant build, if the images folder is in the root
	public static final String GREEN_BAR = "/images/green-bar.gif";
	public static final String GRAY_BAR = "/images/gray-bar.png";
	public static final String RED_BAR = "/images/red-bar.png";
	
	// Messages
	public static final String LOAD_FILE_ERROR_MESSAGE = "The file contents is compromised!";
	public static final String NOT_CSV_FILE_ERROR_MESSAGE = "You can only import CSV files!";
	public static final String LOAD_FILE_ERROR_TITLE = "Import error";	
	public static final String LOAD_MULTIPLE_FILES_ERROR_MESSAGE = "You can only import one file!";


}
