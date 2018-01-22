package uk.co.firstchoice.stella.frontend;

import java.util.Calendar;
import java.util.Map;

public class StellaUtils {

	public static String notNull(String s) {
		return s == null ? "" : s;
	}

	/* Helper function that eases the extraction of parameters from the */
	/* parameterMap. parameterMap contains an array of strings for each key. */
	/* This function returns the first string in the array, or null if key not */
	/* found */
	public static String getParamString(Map parameterMap, String key) {
		if (parameterMap.containsKey(key)) {
			return ((String[]) parameterMap.get(key))[0];
		} else {
			return "";
		}
	}

	public static java.sql.Date parseDate(String ds) throws StellaException {
		try {
			Calendar cal = Calendar.getInstance();
			//  01/34/6789
			cal.set(Integer.parseInt(ds.substring(6)), Integer.parseInt(ds
					.substring(3, 5)) - 1,
					Integer.parseInt(ds.substring(0, 2)), 0, 0);
			return new java.sql.Date(cal.getTimeInMillis());
		} catch (NumberFormatException e) {
			throw new StellaException(
					"Error: The string \""
							+ ds
							+ "\" could not be parsed to a date. Please use the browsers Back button and correct the value.",
					e);
		} catch (IndexOutOfBoundsException e) {
			throw new StellaException(
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
		String s = Float.toString(Math.round(num * 100f) / 100f);
		if (s.length() - s.indexOf('.') < 3)
			return s + "0";
		else
			return s;
	}

	public static int stringToInt(String str) throws StellaException {
		int i;
		try {
			i = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			throw new StellaException(
					"Error: The string \""
							+ str
							+ "\" could not be parsed to a number. Please use the browsers Back button and correct the value.",
					e);
		}
		return i;
	}

	public static long stringToLong(String str) throws StellaException {
		long l;
		try {
			l = Long.parseLong(str);
		} catch (NumberFormatException e) {
			throw new StellaException(
					"Error: The string \""
							+ str
							+ "\" could not be parsed to a number. Please use the browsers Back button and correct the value.",
					e);
		}
		return l;
	}

	public static float stringToFloat(String str) throws StellaException {
		float f;
		try {
			f = Float.parseFloat(str);
		} catch (NumberFormatException e) {
			throw new StellaException(
					"Error: The string \""
							+ str
							+ "\" could not be parsed to a number. Please use the browsers Back button and correct the value.",
					e);
		}
		return f;
	}

}