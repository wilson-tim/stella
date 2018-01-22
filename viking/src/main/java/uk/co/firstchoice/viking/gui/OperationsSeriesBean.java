/*
 * OperationsSeriesBean.java
 *
 * Created on 28 July 2004, 11:37
 */

package uk.co.firstchoice.viking.gui;

/**
 * 
 * @author Lars Svensson
 */
public class OperationsSeriesBean {

	private String seriesNo;

	private String departureDate;

	private String route;

	private String supplier;

	private String sharers;

	private String comments;

	private java.util.ArrayList sectors;

	/** Creates a new instance of OperationsSeriesBean */
	public OperationsSeriesBean() {
		sectors = new java.util.ArrayList();
	}

	/**
	 * Getter for property route.
	 * 
	 * @return Value of property route.
	 */
	public java.lang.String getRoute() {
		return route;
	}

	/**
	 * Setter for property route.
	 * 
	 * @param route
	 *            New value of property route.
	 */
	public void setRoute(java.lang.String route) {
		this.route = route;
	}

	/**
	 * Getter for property sectors.
	 * 
	 * @return Value of property sectors.
	 */
	public java.util.ArrayList getSectors() {
		return this.sectors;
	}

	/**
	 * Setter for property sectors.
	 * 
	 * @param sectors
	 *            New value of property sectors.
	 */
	public void setSectors(java.util.ArrayList sectors) {
		this.sectors = sectors;
	}

	/**
	 * Getter for property seriesNo.
	 * 
	 * @return Value of property seriesNo.
	 */
	public java.lang.String getSeriesNo() {
		return seriesNo;
	}

	/**
	 * Setter for property seriesNo.
	 * 
	 * @param seriesNo
	 *            New value of property seriesNo.
	 */
	public void setSeriesNo(java.lang.String seriesNo) {
		this.seriesNo = seriesNo;
	}

	/**
	 * Getter for property sharers.
	 * 
	 * @return Value of property sharers.
	 */
	public java.lang.String getSharers() {
		return sharers;
	}

	/**
	 * Setter for property sharers.
	 * 
	 * @param sharers
	 *            New value of property sharers.
	 */
	public void setSharers(java.lang.String sharers) {
		this.sharers = sharers;
	}

	/**
	 * Getter for property supplier.
	 * 
	 * @return Value of property supplier.
	 */
	public java.lang.String getSupplier() {
		return supplier;
	}

	/**
	 * Setter for property supplier.
	 * 
	 * @param supplier
	 *            New value of property supplier.
	 */
	public void setSupplier(java.lang.String supplier) {
		this.supplier = supplier;
	}

	/**
	 * Getter for property departureDate.
	 * 
	 * @return Value of property departureDate.
	 */
	public java.lang.String getDepartureDate() {
		return departureDate;
	}

	/**
	 * Setter for property departureDate.
	 * 
	 * @param departureDate
	 *            New value of property departureDate.
	 */
	public void setDepartureDate(java.lang.String departureDate) {
		this.departureDate = departureDate;
	}

	/**
	 * Getter for property comments.
	 * 
	 * @return Value of property comments.
	 */
	public java.lang.String getComments() {
		return comments;
	}

	/**
	 * Setter for property comments.
	 * 
	 * @param comments
	 *            New value of property comments.
	 */
	public void setComments(java.lang.String comments) {
		this.comments = comments;
	}

}