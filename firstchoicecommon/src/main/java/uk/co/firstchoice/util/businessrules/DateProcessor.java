
package uk.co.firstchoice.util.businessrules;

import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * DateProcessor.java
 * 
 * Created on 08 January 2003, 10:45 Leigh Ashton business rules for dates
 */
public class DateProcessor {

	/**
	 * converts string to sql date
	 * 
	 * @param inString
	 *            string of date to be parsed
	 * @param inpFormat
	 *            format of the date to be converted to
	 * @return converted date
	 */
	public static Date parseDate(String inString, String inpFormat) {
		Date calcDate;
		ParsePosition p = new ParsePosition(0);
		//SimpleDateFormat dformat = new SimpleDateFormat("ddMMMyyhhmm");
		SimpleDateFormat dformat = new SimpleDateFormat(inpFormat);

		calcDate = dformat.parse(inString, p);
		//System.out.println("tkt cal:" +cal.getTime());
		//System.out.println("sqldt:" + new
		// java.sql.Timestamp(cal.getTimeInMillis()));
		//return new java.sql.Date(dformat.parse(inString,p));

		// can't use gettimeinmillis in jdk1.2 or below
		//return new java.sql.Timestamp(cal.getTimeInMillis());
		//return new java.sql.Timestamp(cal.get(cal.YEAR),cal.get(cal.MONTH),
		// cal.get(cal.DAY_OF_MONTH),
		//                    cal.get(cal.HOUR_OF_DAY),cal.get(cal.MINUTE),cal.get(cal.SECOND),
		// cal.get(cal.MILLISECOND));
		return calcDate;
	}

	/**
	 * validate string is a valid date
	 * 
	 * @param inpdate
	 *            string representing a date
	 * @param inpFormat
	 *            format of date to be checked e.g ddmmyy
	 * @return true if a valid date, false if not
	 */
	public static boolean checkDate(String inpdate, String inpFormat) {

		int yy, mm, dd;
		// The date format entered should be in dd/mm/yy or dd/mm/yyyy
		// if the length is more than 8/10 it is invalid

		if (inpFormat.equals("ddMMMyyyy")) {
			// The date format entered should be in ddmmmyy or ddmmmyyyy
			// eg. 02MAY02 or 02MAY2002
			// if the length is more than 8/10 it is invalid
			if ((inpdate.length() != 7) && (inpdate.length() != 9)) {
				System.out.println(" Invalid Date (length):" + inpdate);
				return false;
			}
			try {

				// Check the dd,yy are numeric otherewise display the numeric
				// error
				dd = Integer.parseInt(inpdate.substring(0, 2));

				yy = Integer.parseInt(inpdate.substring(6, inpdate.length()));

				if (yy < 100)
					yy = yy + 2000;

			} catch (NumberFormatException nfe) {

				System.out
						.println("Invaid Date entered <<NumberFormatException:>> "
								+ nfe.getMessage());
				return false;
			}

			try {
				Calendar calendar = new GregorianCalendar();
				// set the date 54/45/02 as invalid
				calendar.setLenient(false);
				// pass the date into java date format , if error it will be
				// caught in exception
				// and displayed appropriate error
				SimpleDateFormat format = new SimpleDateFormat(inpFormat);
				format.setLenient(false); //this is so date won't roll over

				calendar.setTime(format.parse(inpdate));
				calendar.set(yy, 1, dd);

			}

			catch (Exception e) {
				System.out.println("Invalid Date  " + inpdate);
				return false;
			}
			return true;

		} // end that date format

		if (inpFormat.equals("yymmdd")) {
			// The date format entered should be in yymmdd
			// eg. 040602
			// if the length is more than 6 it is invalid
			if (inpdate.length() != 6) {
				System.out.println(" Invalid Date (length):" + inpdate);
				return false;
			}
			try {
				// Check the dd,yy and mm are numeric otherewise display the
				// numeric error
				yy = Integer.parseInt(inpdate.substring(0, 2));
				mm = Integer.parseInt(inpdate.substring(2, 4));
				dd = Integer.parseInt(inpdate.substring(4, inpdate.length()));
				if (yy < 100)
					yy = yy + 2000;

			} catch (NumberFormatException nfe) {

				System.out
						.println("Invaid Date entered <<NumberFormatException:>> "
								+ nfe.getMessage());
				return false;
			}

			try {
				Calendar calendar = new GregorianCalendar();
				// set the date 54/45/02 as invalid
				calendar.setLenient(false);
				// pass the date into java date format , if error it will be
				// caught in exception
				// and displayed appropriate error
				SimpleDateFormat format = new SimpleDateFormat(inpFormat);
				format.setLenient(false); //this is so date won't roll over

				calendar.setTime(format.parse(inpdate));
				calendar.set(yy, 1, dd);

			}

			catch (Exception e) {
				System.out.println("Invalid Date  " + inpdate);
				return false;
			}
			return true;

		} // end of yymmdd format

		else { // other date formats -- NOT YET CODED FOR OR TESTED
			if ((inpdate.length() != 8) && (inpdate.length() != 10)) {
				System.out.println(" Invalid Date (length):" + inpdate);
				return false;
			}
			try {

				// Check the dd,mm,yy are numeric otherewise display the numeric
				// error
				dd = Integer.parseInt(inpdate.substring(0, 2));
				mm = Integer.parseInt(inpdate.substring(3, 5));
				yy = Integer.parseInt(inpdate.substring(6, inpdate.length()));

				if (yy < 100)
					yy = yy + 2000;

			} catch (NumberFormatException nfe) {

				System.out
						.println("Invaid Date entered <<NumberFormatException:>> "
								+ nfe.getMessage());
				return false;
			}

			try {
				Calendar calendar = new GregorianCalendar();
				// set the date 54/45/02 as invalid
				calendar.setLenient(false);
				// pass the date into java date format , if error it will be
				// caught in exception
				// and displayed appropriate error
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				format.setLenient(false); //this is so date won't roll over
				format.parse(inpdate);
				calendar.set(yy, mm - 1, dd);

			}

			catch (Exception e) {
				System.out.println("Invalid Date  " + inpdate);
				return false;
			}
			return true;
		}// end if

	} // end method

	/**
	 * Class to determine business season and year from a date
	 * 
	 * @param inDate
	 *            date e.g. departure date
	 * @param inLength
	 *            length of season to be returned e.g. if S2002 return value is
	 *            required, length should be 5, if S02 then 3
	 * @return season e.g. S03, S2003
	 */
	public static String calcSeason(java.sql.Date inDate, int inLength) {
		return calcSeason(inDate, inLength, " ");
	}

	/*
	 * @param inDate date e.g. departure date @param inLength length of season
	 * to be returned @param inSeasonType default season type , for some of
	 * group compies season types are not S or W byt A,B,C D etc. so if this
	 * parameter is passed than don't calculte season type from date , this is
	 * used for Amadeus where season type will be Y @return Y04, Y2004 etc.
	 */

	public static String calcSeason(java.sql.Date inDate, int inLength,
			String inSeasonType) {

		// determine season of departure
		// if between 1st May and 31-Oct then Summer
		// else Winter
		// set up dates in that same year for these season barriers so can
		// be compared
		String season = "";
		String seasonType = "";
		String seasonYear = "";
		Calendar cal = Calendar.getInstance();
		// get departure year
		cal.setTime(inDate);
		int deptYY = cal.get(Calendar.YEAR);

		// set season start/end dates in that year
		Calendar seasonStart = Calendar.getInstance();
		// set up summer season dates
		seasonStart.set(deptYY, 3, 30, 23, 59, 59);
		Calendar seasonEnd = Calendar.getInstance();
		seasonEnd.set(deptYY, 9, 31, 23, 59, 59);

		//System.out.println("sst:" + seasonStart.getTime() + "dt:" + inDate +
		// " yr:" + deptYY );
		//now compare to see if within range
		// can't use gettimeinmillis in jdk1.2 or less
		if (((inSeasonType.trim()).length()) > 0) { // air load , only one
													// season Y throught whole
													// year
			seasonType = inSeasonType;
			seasonYear = String.valueOf(deptYY);

		} else { // mir load

			if (cal.before(seasonStart)) {
				// before season start, must be winter of previous year e.g.
				// 2-feb
				seasonType = "W";
				seasonYear = String.valueOf(deptYY - 1);
			} else if (cal.after(seasonEnd)) {
				// after season end, must be winter of current year e.g. 2-nov
				seasonType = "W";
				seasonYear = String.valueOf(deptYY);
			} else {
				// before season start, must be summer of current year
				seasonType = "S";
				seasonYear = String.valueOf(deptYY);
			}
		} //  end of inSeasonType if

		if (inLength == 3) {
			// e.g. S03
			season = seasonType + seasonYear.substring(2, 4);
		} else {
			// e.g. S2003
			season = seasonType + seasonYear;
		}

		return season;
	}

	/**
	 * Class to find month number from given date , eg i/p is month JAN it
	 * returns 7
	 * 
	 * @param input
	 *            date in a string variable
	 * @param input
	 *            date format Created on 27 Sept 2004 Jyoti Renganathan
	 * @return month number
	 */
	public static int findMonthNumber(String inpDate, String inpFormat) {

		Format formatter;

		formatter = new SimpleDateFormat("MM"); // 01
		Date dt = DateProcessor.parseDate(inpDate, inpFormat);

		int monthNum = Integer.parseInt(formatter.format(dt));

		return monthNum;
	}
}