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
	
	/* Strings */
	
	// CSV
	public static final String identifierCSV = "identifier";
	
	// Files
	public static final String GREEN_BAR = "images/green-bar.gif";	
	
	// Messages
	public static final String LOAD_FILE_ERROR_MESSAGE = "This file can not be imported!";
	public static final String LOAD_FILE_ERROR_TITLE = "Import error";	
	public static final String LOAD_MULTIPLE_FILES_ERROR_MESSAGE = "You can only import one file!";


}
