package uk.co.firstchoice.viking.gui;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import uk.co.firstchoice.viking.gui.util.ForwardConstants;

public final class FlightDetailsFormBean extends ActionForm {

	private String action = ForwardConstants.EMPTY;

	private String series = ForwardConstants.EMPTY;

	private String season = ForwardConstants.EMPTY;

	private String weekday = ForwardConstants.EMPTY;

	private String startDate = ForwardConstants.EMPTY;

	private String endDate = ForwardConstants.EMPTY;

	private String frequency = ForwardConstants.EMPTY;

	private String version = ForwardConstants.EMPTY;

	private String sector1 = ForwardConstants.EMPTY;

	private String sector2 = ForwardConstants.EMPTY;

	private String sector3 = ForwardConstants.EMPTY;

	private String sector4 = ForwardConstants.EMPTY;

	private String sector5 = ForwardConstants.EMPTY;

	private String sector6 = ForwardConstants.EMPTY;

	private String slot = ForwardConstants.EMPTY;

	private String initialCapacity = ForwardConstants.EMPTY;

	private String amendmentType = ForwardConstants.EMPTY;

	private String comments = ForwardConstants.EMPTY;

	private String error = ForwardConstants.EMPTY;

	/**
	 * Reset all properties to their default values.
	 * 
	 * @param mapping
	 *            The mapping used to select this instance
	 * @param request
	 *            The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	}

	/**
	 * Getter for property action.
	 * 
	 * @return Value of property action.
	 *  
	 */
	public java.lang.String getAction() {
		return action;
	}

	/**
	 * Setter for property action.
	 * 
	 * @param action
	 *            New value of property action.
	 *  
	 */
	public void setAction(java.lang.String action) {
		this.action = action;
	}

	/**
	 * Getter for property comments.
	 * 
	 * @return Value of property comments.
	 *  
	 */
	public java.lang.String getComments() {
		return comments;
	}

	/**
	 * Setter for property comments.
	 * 
	 * @param comments
	 *            New value of property comments.
	 *  
	 */
	public void setComments(java.lang.String comments) {
		this.comments = comments;
	}

	/**
	 * Getter for property endDate.
	 * 
	 * @return Value of property endDate.
	 *  
	 */
	public java.lang.String getEndDate() {
		return endDate;
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
	 * Getter for property frequency.
	 * 
	 * @return Value of property frequency.
	 *  
	 */
	public java.lang.String getFrequency() {
		return frequency;
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
	 * Getter for property season.
	 * 
	 * @return Value of property season.
	 *  
	 */
	public java.lang.String getSeason() {
		return season;
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
	 * Getter for property series.
	 * 
	 * @return Value of property series.
	 *  
	 */
	public java.lang.String getSeries() {
		return series;
	}

	/**
	 * Setter for property series.
	 * 
	 * @param series
	 *            New value of property series.
	 *  
	 */
	public void setSeries(java.lang.String series) {
		this.series = series;
	}

	/**
	 * Getter for property startDate.
	 * 
	 * @return Value of property startDate.
	 *  
	 */
	public java.lang.String getStartDate() {
		return startDate;
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
	 * Getter for property version.
	 * 
	 * @return Value of property version.
	 *  
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Setter for property version.
	 * 
	 * @param version
	 *            New value of property version.
	 *  
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Getter for property weekday.
	 * 
	 * @return Value of property weekday.
	 *  
	 */
	public java.lang.String getWeekday() {
		return weekday;
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
	 * Getter for property sector6.
	 * 
	 * @return Value of property sector6.
	 *  
	 */
	public java.lang.String getSector6() {
		return sector6;
	}

	/**
	 * Setter for property sector6.
	 * 
	 * @param sector6
	 *            New value of property sector6.
	 *  
	 */
	public void setSector6(java.lang.String sector6) {
		this.sector6 = sector6;
	}

	/**
	 * Getter for property sector5.
	 * 
	 * @return Value of property sector5.
	 *  
	 */
	public java.lang.String getSector5() {
		return sector5;
	}

	/**
	 * Setter for property sector5.
	 * 
	 * @param sector5
	 *            New value of property sector5.
	 *  
	 */
	public void setSector5(java.lang.String sector5) {
		this.sector5 = sector5;
	}

	/**
	 * Getter for property sector4.
	 * 
	 * @return Value of property sector4.
	 *  
	 */
	public java.lang.String getSector4() {
		return sector4;
	}

	/**
	 * Setter for property sector4.
	 * 
	 * @param sector4
	 *            New value of property sector4.
	 *  
	 */
	public void setSector4(java.lang.String sector4) {
		this.sector4 = sector4;
	}

	/**
	 * Getter for property sector1.
	 * 
	 * @return Value of property sector1.
	 *  
	 */
	public java.lang.String getSector1() {
		return sector1;
	}

	/**
	 * Setter for property sector1.
	 * 
	 * @param sector1
	 *            New value of property sector1.
	 *  
	 */
	public void setSector1(java.lang.String sector1) {
		this.sector1 = sector1;
	}

	/**
	 * Getter for property slot.
	 * 
	 * @return Value of property slot.
	 *  
	 */
	public java.lang.String getSlot() {
		return slot;
	}

	/**
	 * Setter for property slot.
	 * 
	 * @param slot
	 *            New value of property slot.
	 *  
	 */
	public void setSlot(java.lang.String slot) {
		this.slot = slot;
	}

	/**
	 * Getter for property sector2.
	 * 
	 * @return Value of property sector2.
	 *  
	 */
	public java.lang.String getSector2() {
		return sector2;
	}

	/**
	 * Setter for property sector2.
	 * 
	 * @param sector2
	 *            New value of property sector2.
	 *  
	 */
	public void setSector2(java.lang.String sector2) {
		this.sector2 = sector2;
	}

	/**
	 * Getter for property sector3.
	 * 
	 * @return Value of property sector3.
	 *  
	 */
	public java.lang.String getSector3() {
		return sector3;
	}

	/**
	 * Setter for property sector3.
	 * 
	 * @param sector3
	 *            New value of property sector3.
	 *  
	 */
	public void setSector3(java.lang.String sector3) {
		this.sector3 = sector3;
	}

	/**
	 * Getter for property amendmentType.
	 * 
	 * @return Value of property amendmentType.
	 *  
	 */
	public java.lang.String getAmendmentType() {
		return amendmentType;
	}

	/**
	 * Setter for property amendmentType.
	 * 
	 * @param amendmentType
	 *            New value of property amendmentType.
	 *  
	 */
	public void setAmendmentType(java.lang.String amendmentType) {
		this.amendmentType = amendmentType;
	}

	/**
	 * Getter for property initialCapacity.
	 * 
	 * @return Value of property initialCapacity.
	 *  
	 */
	public java.lang.String getInitialCapacity() {
		return initialCapacity;
	}

	/**
	 * Setter for property initialCapacity.
	 * 
	 * @param initialCapacity
	 *            New value of property initialCapacity.
	 *  
	 */
	public void setInitialCapacity(java.lang.String initialCapacity) {
		this.initialCapacity = initialCapacity;
	}

}