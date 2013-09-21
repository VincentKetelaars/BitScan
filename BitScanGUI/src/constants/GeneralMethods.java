package constants;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.joda.time.DateTime;

public class GeneralMethods {
	
	public static String convertPriceIntToString(int price) {
		return price / 100 + "." + String.format("%02d", price % 100);
	}
	
	public static String convertPriceIntToEuroString(int price) {
		return Constants.euro + convertPriceIntToString(price);
	}	

	public static String dateTimeToString(DateTime dt) {
		if (dt == null) {
			return "0000-00-00 00:00:00";
		}
		return dt.toString("yyyy-MM-dd hh:mm:ss");
	}
	
	public static void showCompromisedFileErrorDialog(Component component) {
		JOptionPane.showMessageDialog(component, Constants.LOAD_FILE_ERROR_MESSAGE, Constants.LOAD_FILE_ERROR_TITLE, JOptionPane.WARNING_MESSAGE);
	}

	public static void showWrongFileErrorDialog(Component component) {
		JOptionPane.showMessageDialog(component, Constants.NOT_CSV_FILE_ERROR_MESSAGE, Constants.LOAD_FILE_ERROR_TITLE, JOptionPane.WARNING_MESSAGE);
	}

}
