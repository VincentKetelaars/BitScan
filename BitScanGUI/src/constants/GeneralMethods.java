package constants;

public class GeneralMethods {
	
	public static String convertPriceIntToString(int price) {
		return "€" + price / 100 + "," + String.format("%02d", price % 100);
	}

}
