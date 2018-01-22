/*
 * Created on 08-Mar-2005
 * Filename: PaxSearchForm.java
 */
package uk.co.firstchoice.viking.pax.gui.search;

import uk.co.firstchoice.common.gui.struts.util.CloneableForm;

/**
 * @author Dwood
 * 
 * Action Form to gather search criteria for pax search
 */
public class PaxSearchForm extends CloneableForm {
	
	private String flightCode;

	private String fromDate ;

	private String toDate;

	private String carrier;
	
	private String routeStart;
	
	private String routeEnd; 
	
	private String searchType; 
	
	private String flightType;
	
	private String reportButton;

	private String resultsButton;
	
	/**
	 * @return Returns the resultsButton.
	 */
	public String getResultsButton() {
		return resultsButton;
	}
	/**
	 * @param resultsButton The resultsButton to set.
	 */
	public void setResultsButton(final String resultsButton) {
		this.resultsButton = resultsButton;
	}
	
	/**
	 * @return Returns the reportButton.
	 */
	public String getReportButton() {
		return reportButton;
	}
	/**
	 * @param reportButton The reportButton to set.
	 */
	public void setReportButton(final String reportButton) {
		this.reportButton = reportButton;
	}
	/**
	 * @return Returns the flightType.
	 */
	public String getFlightType() {
		return flightType;
	}
	/**
	 * @param flightType The flightType to set.
	 */
	public void setFlightType(final String flightType) {
		this.flightType = flightType;
	}
	/**
	 * @return Returns the routeEnd.
	 */
	public String getRouteEnd() {
		return routeEnd;
	}
	/**
	 * @param routeEnd The routeEnd to set.
	 */
	public void setRouteEnd(final String routeEnd) {
		this.routeEnd = routeEnd;
	}
	/**
	 * @return Returns the routeStart.
	 */
	public String getRouteStart() {
		return routeStart;
	}
	/**
	 * @param routeStart The routeStart to set.
	 */
	public void setRouteStart(final String routeStart) {
		this.routeStart = routeStart;
	}
	/**
	 * @return Returns the searchType.
	 */
	public String getSearchType() {
		return searchType;
	}
	/**
	 * @param searchType The searchType to set.
	 * This field is currently only used by the client side javascript
	 */
	public void setSearchType(final String searchType) {
		this.searchType = searchType;
	}
	/**
	 * @return Returns the carrier.
	 */
	public String getCarrier() {
		return this.carrier;
	}
	/**
	 * @param carrier The carrier to set.
	 */
	public void setCarrier(final String carrier) {
		if (carrier.compareTo("") == 0 ) {
			this.carrier = null;
		}
		this.carrier = carrier;
	}
	/**
	 * @return Returns the flightCode.
	 */
	public String getFlightCode() {
		return this.flightCode;
	}

	/**
	 * @param flightCode
	 *            The flightCode to set.
	 */
	public void setFlightCode(final String flightCode) {
		if (flightCode.compareTo("") == 0 ) {
			this.flightCode = null;
		}
		this.flightCode = flightCode;
	}

	/**
	 * @return Returns the fromDate.
	 */
	public String getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate
	 *            The fromDate to set.
	 */
	public void setFromDate(final String fromDate) {
		this.fromDate = fromDate;
	}


	/**
	 * @return Returns the toDate.
	 */
	public String getToDate() {
		return this.toDate;
	}

	/**
	 * @param toDate
	 *            The toDate to set.
	 */
	public void setToDate(final String toDate) {
		this.toDate = toDate;
	}

}