package constants;

import org.joda.time.DateTime;

public class GeneralMethods {
	
	public static String convertPriceIntToString(int price) {
		return price / 100 + "." + String.format("%02d", price % 100);
	}
	
	public static String convertPriceIntToEuroString(int price) {
		return "€" + convertPriceIntToString(price);
	}	

	public static String dateTimeToString(DateTime dt) {
		if (dt == null) {
			return "0000-00-00 00:00:00";
		}
		return dt.toString("yyyy-MM-dd hh:mm:ss");
	}

}
