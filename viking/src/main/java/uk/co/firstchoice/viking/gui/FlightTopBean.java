package uk.co.firstchoice.viking.gui;

import uk.co.firstchoice.fcutil.FCUtils;

public final class FlightTopBean {

	private String seriesNo;

	private String season;

	private String version;

	private boolean liveVersion;

	private String weekday;

	private String startDate;

	private String endDate;

	private String frequency;

	private String departureDates; // "['01/10/2000','08/01/2000']" for use by

	// Javascript to build selector

	private String dateGroupInfo;

	/**
	 * [
	 * ['02/10/2003','16/10/2003','FF00','01','MAN','LPA','09:00','14:30','0','FCA332C','B757'],
	 * ['02/10/2003','16/10/2003','FF00','02','LPA','MAN','15:00','20:00','0','FCA332D','B757'] ]
	 */

	private String error;

	/**
	 * Getter for property season.
	 * 
	 * @return Value of property season.
	 *  
	 */
	public java.lang.String getSeason() {
		return FCUtils.notNull(season);
	}

	/**
	 * Setter for property season.
	 * 
	 * @param season
	 *            New value of property season.
	 *  
	 */
	public void setSeason(java.lang.String season) {
		this.season = season;
	}

	/**
	 * Getter for property seriesNo.
	 * 
	 * @return Value of property seriesNo.
	 *  
	 */
	public java.lang.String getSeriesNo() {
		return FCUtils.notNull(seriesNo);
	}

	/**
	 * Setter for property seriesNo.
	 * 
	 * @param seriesNo
	 *            New value of property seriesNo.
	 *  
	 */
	public void setSeriesNo(java.lang.String seriesNo) {
		this.seriesNo = seriesNo;
	}

	/**
	 * Getter for property version.
	 * 
	 * @return Value of property version.
	 *  
	 */
	public java.lang.String getVersion() {
		return FCUtils.notNull(version);
	}

	/**
	 * Setter for property version.
	 * 
	 * @param version
	 *            New value of property version.
	 *  
	 */
	public void setVersion(java.lang.String version) {
		this.version = version;
	}

	/**
	 * Getter for property endDate.
	 * 
	 * @return Value of property endDate.
	 *  
	 */
	public java.lang.String getEndDate() {
		return FCUtils.notNull(endDate);
	}

	/**
	 * Setter for property endDate.
	 * 
	 * @param endDate
	 *            New value of property endDate.
	 *  
	 */
	public void setEndDate(java.lang.String endDate) {
		this.endDate = endDate;
	}

	/**
	 * Getter for property frequency.
	 * 
	 * @return Value of property frequency.
	 *  
	 */
	public java.lang.String getFrequency() {
		return FCUtils.notNull(frequency);
	}

	/**
	 * Setter for property frequency.
	 * 
	 * @param frequency
	 *            New value of property frequency.
	 *  
	 */
	public void setFrequency(java.lang.String frequency) {
		this.frequency = frequency;
	}

	/**
	 * Getter for property startDate.
	 * 
	 * @return Value of property startDate.
	 *  
	 */
	public java.lang.String getStartDate() {
		return FCUtils.notNull(startDate);
	}

	/**
	 * Setter for property startDate.
	 * 
	 * @param startDate
	 *            New value of property startDate.
	 *  
	 */
	public void setStartDate(java.lang.String startDate) {
		this.startDate = startDate;
	}

	/**
	 * Getter for property weekday.
	 * 
	 * @return Value of property weekday.
	 *  
	 */
	public java.lang.String getWeekday() {
		return FCUtils.notNull(weekday);
	}

	/**
	 * Setter for property weekday.
	 * 
	 * @param weekday
	 *            New value of property weekday.
	 *  
	 */
	public void setWeekday(java.lang.String weekday) {
		this.weekday = weekday;
	}

	/**
	 * Getter for property error.
	 * 
	 * @return Value of property error.
	 *  
	 */
	public java.lang.String getError() {
		return error;
	}

	/**
	 * Setter for property error.
	 * 
	 * @param error
	 *            New value of property error.
	 *  
	 */
	public void setError(java.lang.String error) {
		this.error = error;
	}

	/**
	 * Getter for property dateGroupInfo.
	 * 
	 * @return Value of property dateGroupInfo.
	 *  
	 */
	public java.lang.String getDateGroupInfo() {
		return FCUtils.notNull(dateGroupInfo);
	}

	/**
	 * Setter for property dateGroupInfo.
	 * 
	 * @param dateGroupInfo
	 *            New value of property dateGroupInfo.
	 *  
	 */
	public void setDateGroupInfo(java.lang.String dateGroupInfo) {
		this.dateGroupInfo = dateGroupInfo;
	}

	/**
	 * Getter for property departureDates.
	 * 
	 * @return Value of property departureDates.
	 *  
	 */
	public java.lang.String getDepartureDates() {
		return FCUtils.notNull(departureDates);
	}

	/**
	 * Setter for property departureDates.
	 * 
	 * @param departureDates
	 *            New value of property departureDates.
	 *  
	 */
	public void setDepartureDates(java.lang.String departureDates) {
		this.departureDates = departureDates;
	}

	/**
	 * Getter for property liveVersion.
	 * 
	 * @return Value of property liveVersion.
	 */
	public boolean isLiveVersion() {
		return liveVersion;
	}

	/**
	 * Setter for property liveVersion.
	 * 
	 * @param liveVersion
	 *            New value of property liveVersion.
	 */
	public void setLiveVersion(boolean liveVersion) {
		this.liveVersion = liveVersion;
	}

}