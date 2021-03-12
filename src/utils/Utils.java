package utils;

public class Utils {
	
	private Utils() {}
	
	/*
	 * Add a capital letter at the beginning of the string.
	 */
	public static String capitalize(String str) {
	    if(str == null || str.isEmpty()) {
	        return str;
	    }
	    return str.substring(0, 1).toUpperCase() + str.substring(1);
	}


}
