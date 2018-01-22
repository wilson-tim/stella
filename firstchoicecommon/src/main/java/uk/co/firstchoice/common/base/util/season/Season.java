/*
 * Season.java
 *
 * Created on 30 March 2004, 16:46
 */

package uk.co.firstchoice.common.base.util.season;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Class to handle First Choice Sesons. Almost everything within Frist Choice is
 * Season based e.g. departures, accommodations and brochures. Each year has two
 * seasons, winter and summer, so a typical season could be S2002 for summer
 * 2002 or W2001 for winter 2001. The summer season currently goes from 1 May to
 * 31 October and the winter season from 1 November to 30 April the following
 * year. Hence 1 January 2003 is actually in the W2002 season.
 * 
 * This class is a holder for Seasons. It can be created and set using either
 * one of various Season descriptions (W03, S2003, (W,2002)) or by using a date
 * in which case the Season will be set so that it includes the date. There are
 * a number of methods for comparing seasons and for checking that a given date
 * falls within a given season.
 * 
 * @author Lsvensson
 */
public class Season implements Comparable {

	private static final int FIRST_SUMMER_MONTH = GregorianCalendar.MAY;

	private static final int LAST_SUMMER_MONTH = GregorianCalendar.OCTOBER;

	/** Constant char indicating the symbol for the summer season ('S') */
	public static final char SUMMER = 'S';

	/** Constant char indicating the symbol for the winter season ('W') */
	public static final char WINTER = 'W';

	private static final int MIN_SEASON_YEAR = 1990;

	private static final int MAX_SEASON_YEAR = 2029;

	private char seasonType; // 'S' or 'W'

	private int seasonYear;

	private Calendar seasonStart;

	private Calendar seasonEnd;

	/**
	 * Create a new instance of Season Set season to current day's season
	 */
	public Season() {
		try {
			setSeason(new Date());
		} catch (InvalidSeasonException e) {
			// Only thrown if date is invalid and current date should never be
			// invalid
			e.printStackTrace();
		}
	}

	/**
	 * Create a new instance of Season Set season to date
	 * 
	 * @param date
	 *            The date to initialize this Season to
	 * @throws InvalidSeasonException
	 *             Thrown if date is invalid (Year not between MIN_SEASON_YEAR
	 *             and MAX_SEASON_YEAR)
	 */
	public Season(Date date) throws InvalidSeasonException {
		setSeason(date);
	}

	/**
	 * Create a new instance of Season Set season to seasonStr
	 * 
	 * @param seasonStr
	 *            Season to initialize this Season to. Format S03 or S2003
	 * @throws InvalidSeasonException
	 *             Thrown if seasonStr not in correct format.
	 */
	public Season(String seasonStr) throws InvalidSeasonException { // W04 or
		// W2004
		setSeason(seasonStr);
	}

	/**
	 * Create a new instance of Season Set season to seasonType, seasonYear
	 * 
	 * @param seasonType
	 *            "S" or "W" (or "S....." - only first char is checked)
	 * @param seasonYear
	 *            Season year to initialize to
	 * @throws InvalidSeasonException
	 *             Thrown if parameters doesn't specify a correct Season
	 */
	public Season(String seasonType, int seasonYear)
			throws InvalidSeasonException {
		setSeason(seasonType, seasonYear);
	}

	/**
	 * Create a new instance of Season Set season to seasonType, seasonYear
	 * 
	 * @param seasonType
	 *            "S" or "W" (or "S....." - only first char is checked)
	 * @param seasonYear
	 *            Season year to initialize to
	 * @throws InvalidSeasonException
	 *             Thrown if parameters doesn't specify a correct Season (type
	 *             not "S" or "W" or year cannot be convertet to an int)
	 */
	public Season(String seasonType, String seasonYear)
			throws InvalidSeasonException {
		setSeason(seasonType, seasonYear);
	}

	/**
	 * Inherited from Object. Generate hashCode for this Season
	 * 
	 * @return seasonYear*10+(if Winter, 2, if summer, 1 else 0)
	 */
	public int hashCode() {
		return seasonYear * 10
				+ (SUMMER == seasonType ? 1 : WINTER == seasonType ? 2 : 0);
	}

	/**
	 * Implementation of Comparable's compareTo method
	 * 
	 * @param obj
	 *            Season to compare this (current) Season to
	 * @return -1 if this Season is before obj Season 0 if same Seasons 1 if
	 *         this Season is after obj Season
	 */
	public int compareTo(Object obj) {
		Season s = (Season) obj;
		return this.hashCode() < s.hashCode() ? -1 : this.hashCode() > s
				.hashCode() ? 1 : 0;
	}

	/**
	 * Compare this Season with another, return true if same
	 * 
	 * @param obj
	 *            Season to compare this (current) Season with
	 * @return True if same season Type/Year, otherwise false
	 */
	public boolean equals(Object obj) {
		return 0 == this.compareTo(obj);
	}

	/**
	 * Return this Season as a String
	 * 
	 * @return seasonType+seasonYear ( "S2002" )
	 */
	public String toString() {
		return "" + seasonType + seasonYear; // W2002
	}

	/**
	 *  
	 */
	private void calcSeasonStartEnd() {
		if (SUMMER == this.seasonType) {
			this.seasonStart = new GregorianCalendar(this.seasonYear,
					FIRST_SUMMER_MONTH, 1);
			this.seasonEnd = new GregorianCalendar(this.seasonYear,
					LAST_SUMMER_MONTH + 1, 0);
		} else {
			this.seasonStart = new GregorianCalendar(this.seasonYear,
					LAST_SUMMER_MONTH + 1, 1);
			this.seasonEnd = new GregorianCalendar(this.seasonYear + 1,
					FIRST_SUMMER_MONTH, 0);
		}
	}

	/**
	 * Change this Season object so that the Date parameter would fall within it
	 * 
	 * @param date
	 *            The date to change this season to. e.g. 1/1/2000 would set
	 *            Season to W2000
	 * @throws InvalidSeasonException
	 *             Thrown if date is invalid (year before MIN_SEASON_YEAR or
	 *             after MAX_SEASON_YEAR)
	 */
	public void setSeason(Date date) throws InvalidSeasonException {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);

		if (gc.get(Calendar.YEAR) < MIN_SEASON_YEAR
				|| gc.get(GregorianCalendar.YEAR) > MAX_SEASON_YEAR) {
			throw new InvalidSeasonException("(" + date + ")");
		}
		if (gc.get(Calendar.MONTH) < FIRST_SUMMER_MONTH) {
			this.seasonType = WINTER;
			this.seasonYear = gc.get(GregorianCalendar.YEAR) - 1;

		} else {
			this.seasonYear = gc.get(Calendar.YEAR);
			if (gc.get(GregorianCalendar.MONTH) > LAST_SUMMER_MONTH) {
				this.seasonType = WINTER;
			} else {
				this.seasonType = SUMMER;
			}
		}
		calcSeasonStartEnd();
	}

	/**
	 * Change this Season to seasonStr
	 * 
	 * @param seasonStr
	 *            Season in String format (eg. "S03" or "S2003")
	 * @throws InvalidSeasonException
	 *             If seasonStr doesn't describe a valid Season
	 */
	public void setSeason(String seasonStr) throws InvalidSeasonException { // W04
		// or
		// W2004
		if (null != seasonStr
				&& (seasonStr.length() == 3 || seasonStr.length() == 5)) {
			setSeason(seasonStr.substring(0, 1), seasonStr.substring(1));
		} else {
			throw new InvalidSeasonException("(" + seasonType + seasonYear
					+ ")");
		}
	}

	/**
	 * Set this Season to seasonType, seasonYear
	 * 
	 * @param seasonType
	 *            "S" or "W"
	 * @param seasonYear
	 *            Year
	 * @throws InvalidSeasonException
	 *             Thrown if seasonType not "S" or "W" or seasonYear not between
	 *             MIN_SEASON_YEAR, MAX_SEASON_YEAR
	 */
	public void setSeason(String seasonType, int seasonYear)
			throws InvalidSeasonException {
		try {
			char stChar = seasonType.toUpperCase().charAt(0);
			if (SUMMER != stChar && WINTER != stChar) {
				throw new InvalidSeasonException("(" + seasonType + seasonYear
						+ ")");
			}
			if (seasonYear >= 0 && seasonYear < (MAX_SEASON_YEAR % 100)) {
				seasonYear += 2000;
			} else if (seasonYear > (MIN_SEASON_YEAR % 100) && seasonYear < 100) {
				seasonYear += 1900;
			}
			if (seasonYear < MIN_SEASON_YEAR || seasonYear > MAX_SEASON_YEAR) {
				throw new InvalidSeasonException("(" + seasonType + seasonYear
						+ ")");
			}
			this.seasonType = stChar;
			this.seasonYear = seasonYear;
			calcSeasonStartEnd();
		} catch (NullPointerException e) {
			throw new InvalidSeasonException("(" + seasonType + seasonYear
					+ ")");
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidSeasonException("(" + seasonType + seasonYear
					+ ")");
		}
	}

	/**
	 * Set this Season to seasonType, seasonYear
	 * 
	 * @param seasonType
	 *            "S" or "W"
	 * @param seasonYear
	 *            String representing Year
	 * @throws InvalidSeasonException
	 *             Thrown if seasonYear cannot be converted to int
	 */
	public void setSeason(String seasonType, String seasonYear)
			throws InvalidSeasonException {
		try {
			setSeason(seasonType, Integer.parseInt(seasonYear));
		} catch (NumberFormatException e) {
			throw new InvalidSeasonException("(" + seasonType + seasonYear
					+ ")");
		}
	}

	/**
	 * Return this Season as String ( "S2002" )
	 * 
	 * @return Season as String ( "W2003" )
	 */
	public String getSeason() {
		return this.toString();
	}

	/**
	 * Return this Season as String ( "S02" )
	 * 
	 * @return Season as String ( "W03" )
	 */
	public String getShortSeason() {
		return "" + this.seasonType + ((this.seasonYear % 100) < 10 ? "0" : "")
				+ (this.seasonYear % 100);
	}

	/**
	 * Return the seasonType
	 * 
	 * @return "S" or "W"
	 */
	public String getSeasonType() {
		return "" + this.seasonType;
	}

	/**
	 * Return the seasonYear
	 * 
	 * @return this.seasonYear
	 */
	public int getSeasonYear() {
		return this.seasonYear;
	}

	/**
	 * Return the date of the first day of this Season
	 * 
	 * @return GregorianCalendar object set to first day of this Season
	 */
	public Calendar getSeasonStart() {
		return this.seasonStart;
	}

	/**
	 * Return the date of the last day of this Season
	 * 
	 * @return GregorianCalendar object set to last day of this Season
	 */
	public Calendar getSeasonEnd() {
		return this.seasonEnd;
	}

	/**
	 * Test to see if a given date belongs in this Season
	 * 
	 * @return true if date in this Season, false if not
	 * @param date
	 *            Date object to test whether in this (current) Season
	 * @throws InvalidSeasonException
	 *             Thrown if date's year is not between MIN_SEASON_YEAR and
	 *             MAX_SEASON_YEAR
	 */
	public boolean isInSeason(Date date) throws InvalidSeasonException {
		return this.equals(new Season(date));
	}

	/**
	 * Test whether current season is summer or winter
	 * 
	 * @return True if this is a summer season, false if not
	 */
	public boolean isSummer() {
		return this.seasonType == SUMMER;
	}

	/**
	 * Return Season prior to this
	 * 
	 * @throws InvalidSeasonException
	 *             Passed along from the Season Constructor
	 * @return Season object initialised to the season prior to this one
	 */
	public Season prevSeason() throws InvalidSeasonException {
		Calendar c = new GregorianCalendar();
		c.setTime(seasonStart.getTime());
		c.add(Calendar.DATE, -1);
		return new Season(c.getTime());
	}

	/**
	 * Return Season following this one
	 * 
	 * @throws InvalidSeasonException
	 *             Passed along from the Season Constructor
	 * @return Season object initialised to the season following this one
	 */
	public Season nextSeason() throws InvalidSeasonException {
		Calendar c = new GregorianCalendar();
		c.setTime(seasonEnd.getTime());
		c.add(Calendar.DATE, 1);
		return new Season(c.getTime());
	}

}