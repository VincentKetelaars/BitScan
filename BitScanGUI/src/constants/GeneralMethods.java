package constants;

import java.awt.Component;
import java.util.Arrays;

import javax.swing.JOptionPane;

import org.joda.time.DateTime;

import constants.Constants.SortArrayBy;

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

	public static boolean isValidExtension(String extension) {
		for (String e : Constants.VALID_EXTENSIONS) {
			if (e.equalsIgnoreCase(extension))
				return true;
		}
		return false;
	}

	public static void showCompromisedFileErrorDialog(Component component) {
		JOptionPane.showMessageDialog(component, Constants.LOAD_FILE_ERROR_MESSAGE, Constants.LOAD_FILE_ERROR_TITLE, JOptionPane.WARNING_MESSAGE);
	}

	public static void showWrongFileErrorDialog(Component component) {
		JOptionPane.showMessageDialog(component, Constants.NOT_CSV_FILE_ERROR_MESSAGE, Constants.LOAD_FILE_ERROR_TITLE, JOptionPane.WARNING_MESSAGE);
	}

	public static void showTooManyFilesErrorDialog(Component component) {
		JOptionPane.showMessageDialog(component, Constants.LOAD_MULTIPLE_FILES_ERROR_MESSAGE, Constants.LOAD_FILE_ERROR_TITLE, JOptionPane.WARNING_MESSAGE);
	}

	public static SortArrayBy convertStringToSortArrayBy(String text) {
		switch(text) {
		case "Barcode": 
			return SortArrayBy.BARCODE;
		case "Name": 
			return SortArrayBy.NAME;
		case "Email": 
			return SortArrayBy.EMAIL;
		default : 
			return SortArrayBy.BARCODE;
		}
	}

	public static String convertSortArrayByToString(SortArrayBy sortBy) {
		switch(sortBy) {
		case BARCODE: 
			return "Barcode";
		case NAME: 
			return "Name";
		case EMAIL: 
			return "Email";
		default : 
			return "Barcode";
		}
	}
}
