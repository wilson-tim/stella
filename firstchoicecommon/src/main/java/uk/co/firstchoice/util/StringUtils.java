/*
 * @author Jyoti Renganathan
 * 
 * @version 1.0 @parm mainStr - main string to search into @param subStr -
 *          string to search for @param whichOccur - nth occurance of substrinto
 *          mainStr @returns Position of nth occurance of a substring
 */

/* Simple utility to find the nth occurance of a substring in a string */

package uk.co.firstchoice.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {

	
	/**
	 * stringToDateConversion, util method to convert a 
	 * String to a date
	 * @param dateString String
	 * @return Date
	 */
	public Date stringToDateConversion(final String dateString) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.INPUT_DATE_FORMAT);
		Date date = null;
		if ( dateString != null ) {
			try {
				date = simpleDateFormat.parse(dateString);
			} catch (ParseException e) {}
		}
		return date;
	}
	
	/**
	 * stringToDateConversion, util method to convert a 
	 * String to a date
	 * @param dateString String
	 * @return Date
	 */
	public Date shortStringToDateConversion(final String dateString) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.INPUT_DATE_FORMAT_SHORT);
		Date date = null;
		if ( dateString != null ) {
			try {
				date = simpleDateFormat.parse(dateString);
			} catch (ParseException e) {}
		}
		return date;
	}	

	/**
	 * dateToStringConversion, util method to convert
	 * a date object to a string
	 * @param date Date
	 * @return String
	 */
	public String dateToStringConversion(final Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.INPUT_DATE_FORMAT);
		String dateString = null;
		if ( date != null ) {
			dateString = simpleDateFormat.format(date);
		}
		return dateString;
	}
	

	/**
	 * dateToStringConversion, util method to convert
	 * a date object to a string
	 * @param date Date
	 * @return String
	 */
	public String shortDateToStringConversion(final Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.INPUT_DATE_FORMAT_SHORT);
		String dateString = null;
		if ( date != null ) {
			dateString = simpleDateFormat.format(date);
		}
		return dateString;
	}
	
    
	public static int searchStringOccur(String mainStr, String subStr,
			int whichOccur) {

		int colonPos = 0, counter = 0;
		colonPos = mainStr.indexOf(subStr, colonPos); // start from oth position

		if (colonPos != -1) {
			counter++;

			while (counter < whichOccur) {
				colonPos = mainStr.indexOf(subStr, colonPos + 1);

				if (colonPos == -1) {
					//	  application.log.severe(" Can not find " + whichOccur +
					// "th occurance of " + subStr + " into " + mainStr);
					break;
				}

				counter++;
			} // end of while

		} // end of if

		return colonPos;

		// } // end of try

	}
}

