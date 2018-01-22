package uk.co.firstchoice.util;

import java.text.*;
import java.util.*;

import uk.co.firstchoice.common.base.logging.LogManager;
import uk.co.firstchoice.common.base.logging.Logger;

public class DateUtils {

    /**
     * The logger used to log tracing messages to the log file.
     */
    private static Logger logger = LogManager.getLogger();

    public static final String INPUT_DATE_FORMAT = "dd/MM/yyyy";

    public static final String INPUT_DATE_FORMAT_SHORT = "dd/MM/yy";
    /**
     * The dats format for the date reference
     */
    public static final String REFERENCE_DATE_FORMAT = "MM-yyyy";

    /**
     * The date format that will be displayed to the user
     */
    public static final String LABEL_DATE_FORMAT = "MMM yy";

    /**
     * The date format that is used to convert day and month values back into
     * Dates. This is dependant REFERENCE_DATE_FORMAT constant.
     */
    protected static final String CONVERSION_DATE_FORMAT = "dd-MM-yyyy HH:mm";

    /**
     * The character that is ued to separate day and month strings in a
     * formatted date string. This is dependant upon CONVERSION_DATE_FORMAT.
     */
    protected static final String CONVERSION_DATE_FORMAT_DAY_MONTH_SEPARATOR = "-";

    /**
     * The character that is ued to separate hours and minutes in a formatted
     * date time string. This is dependant upon the CONVERSION_DATE_FORMAT
     * constant.
     */
    protected static final String CONVERSION_DATE_FORMAT_HRS_MINS_SEPARATOR = ":";

    public String formatDate(Date stringDate) {

        SimpleDateFormat formatter = new SimpleDateFormat(INPUT_DATE_FORMAT, Locale.UK);
        formatter.setLenient(false);
        return formatter.format(stringDate);
    }

    public String getFormatedDate() {
        String nextDate = new SimpleDateFormat(DateUtils.INPUT_DATE_FORMAT).format(new Date());
        return nextDate;
    }
    
    public Date getFormatedDate(Date date) {
        Date formattedDate = getFormatedDate(date, INPUT_DATE_FORMAT);
	    return formattedDate;
    }

    public Date getShortFormatedDate(Date date) {
        Date formattedDate = getFormatedDate(date, INPUT_DATE_FORMAT_SHORT);
	    return formattedDate;
    } 
    
    public Date getFormatedDate(Date date, String format) {
        Date formattedDate = null;
	    try {
	        SimpleDateFormat dateFormatter = new SimpleDateFormat( format );
	        formattedDate =  dateFormatter.parse(dateFormatter.format( date ));
	    } catch (ParseException e) {
            logger.log(Logger.ERROR, e);
	    }
	    return formattedDate;
    }
    
    public Calendar getCalculatedDate(Calendar cal, int diffrence) {
        cal.add(Calendar.DATE, diffrence);

        return cal;
    }

    /**
     * Calculates the number of days between two calendar days in a manner which
     * is independent of the Calendar type used.
     * 
     * @param date1
     *            The first date.
     * @param date2
     *            The second date.
     * @return The number of days between the two dates. Zero is returned if the
     *         dates are the same, one if the dates are adjacent, etc. The order
     *         of the dates does not matter, the value returned is always >= 0.
     *         If Calendar types of date1 and date2 are different, the result
     *         may not be accurate.
     */
    static int getDaysBetween(java.util.Calendar date1, java.util.Calendar date2) {
        if (date1.after(date2)) { // swap dates so that d1 is start and d2 is
                                  // end
            java.util.Calendar swap = date1;
            date1 = date2;
            date2 = swap;
        }
        int days = date2.get(java.util.Calendar.DAY_OF_YEAR) - date1.get(java.util.Calendar.DAY_OF_YEAR);
        int year2 = date2.get(java.util.Calendar.YEAR);
        if (date1.get(java.util.Calendar.YEAR) != year2) {
            date1 = (java.util.Calendar) date1.clone();
            do {
                days += date1.getActualMaximum(java.util.Calendar.DAY_OF_YEAR);
                date1.add(java.util.Calendar.YEAR, 1);
            } while (date1.get(java.util.Calendar.YEAR) != year2);
        }
        return days;
    }
}