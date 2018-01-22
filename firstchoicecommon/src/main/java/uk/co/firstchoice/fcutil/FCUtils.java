package uk.co.firstchoice.fcutil;

//import java.util.*;
//import java.util.logging.*;

public class FCUtils {

	private FCUtils() {
	} // Prevent instantation

	public static String notNull(String s) {
		return s == null ? "" : s;
	}

	public static String join(String[] strings, String token) {
		StringBuffer sb = new StringBuffer();
		for (int x = 0; x < (strings.length - 1); x++) {
			sb.append(strings[x]);
			sb.append(token);
		}
		sb.append(strings[strings.length - 1]);
		return (sb.toString());
	}

	/* Helper function that eases the extraction of parameters from the */
	/* parameterMap. parameterMap contains an array of strings for each key. */
	/* This function returns the first string in the array, or null if key not */
	/* found */
	public static String getParamString(java.util.Map parameterMap, String key) {
		if (parameterMap.containsKey(key)) {
			return ((String[]) parameterMap.get(key))[0];
		} else {
			return "";
		}
	}

	public static java.sql.Date parseDate(String ds) throws FCException {
		try {
			java.util.Calendar cal = java.util.Calendar.getInstance();
			//  01/34/6789
			cal.set(Integer.parseInt(ds.substring(6)), Integer.parseInt(ds
					.substring(3, 5)) - 1,
					Integer.parseInt(ds.substring(0, 2)), 0, 0);
			return new java.sql.Date(cal.getTimeInMillis());
		} catch (NumberFormatException e) {
			throw new FCException(
					"Error: The string \""
							+ ds
							+ "\" could not be parsed to a date. Please use the browsers Back button and correct the value.",
					e);
		} catch (IndexOutOfBoundsException e) {
			throw new FCException(
					"Error: The string \""
							+ ds
							+ "\" could not be parsed to a date. Please use the browsers Back button and correct the value.",
					e);
		}
	}

	public static String dateToString(java.sql.Date dt) {
		if (null == dt)
			return "";
		else {
			String ds = dt.toString(); //yyyy-mm-dd
			return ds.substring(8) + "/" + ds.substring(5, 7) + "/"
					+ ds.substring(0, 4);
		}
	}

	public static String dateToShortString(java.sql.Date dt) {
		if (null == dt)
			return "";
		else {
			String ds = dt.toString(); //yyyy-mm-dd
			return ds.substring(8) + "/" + ds.substring(5, 7) + "/"
					+ ds.substring(2, 4);
		}
	}

	public static String leftPad(long num, int digits) {
		String str = "000000000000000" + num;
		return str.substring(str.length() - digits);
	}

	public static String floatToString(float num) {
		return floatToString(num, 2);
	}

	public static String floatToString(float num, int decimals) {
		float x = 0f + (float) Math.pow(10, decimals);
		String s = Float.toString(Math.round(num * x) / x);
		int decs = s.length() - s.indexOf('.') - 1;
		if (decs < decimals)
			return s + "0000000000".substring(0, decimals - decs);
		else
			return s;
	}

	public static int stringToInt(String str) throws FCException {
		int i;
		try {
			i = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			throw new FCException(
					"Error: The string \""
							+ str
							+ "\" could not be parsed to a number. Please use the browsers Back button and correct the value.",
					e);
		}
		return i;
	}

	public static long stringToLong(String str) throws FCException {
		long l;
		try {
			l = Long.parseLong(str);
		} catch (NumberFormatException e) {
			throw new FCException(
					"Error: The string \""
							+ str
							+ "\" could not be parsed to a number. Please use the browsers Back button and correct the value.",
					e);
		}
		return l;
	}

	public static float stringToFloat(String str) throws FCException {
		float f;
		try {
			f = Float.parseFloat(str);
		} catch (NumberFormatException e) {
			throw new FCException(
					"Error: The string \""
							+ str
							+ "\" could not be parsed to a number. Please use the browsers Back button and correct the value.",
					e);
		}
		return f;
	}

	public static boolean emptyString(String str) {
		return str == null || "".equals(str.trim());
	}

}