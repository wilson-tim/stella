package uk.co.firstchoice.viking.gui;

//import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;

import uk.co.firstchoice.viking.gui.util.ForwardConstants;

//import org.apache.struts.action.ActionMapping;

public final class FlightBotFormBean extends ActionForm {

	private String action = ForwardConstants.EMPTY;

	private String error = ForwardConstants.EMPTY;

	private String seriesNo = ForwardConstants.EMPTY;

	private String season = ForwardConstants.EMPTY;

	private String version = ForwardConstants.EMPTY;

	private boolean liveVersion;

	private String sellPurchaseTable = "1"; // 1 = sell table visible, 0 =

	// purchase table visible

	private String sellCustDateTable = "1"; // 1 = By customer visible, 0 = By

	// date visible

	private String purcSectorDateTable = "1"; // 1 = By sector visible, 0 = By

	// date visible

	private String departureDates; // "['01/10/2000','08/01/2000']" for use by

	// Javascript to build selector

	private java.util.Collection customerAllocation; // collection of

	// AllocationBean

	private java.util.Collection supplierAllocation; // collection of

	// AllocationBean

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
	 * Getter for property seriesNo.
	 * 
	 * @return Value of property seriesNo.
	 *  
	 */
	public java.lang.String getSeriesNo() {
		return seriesNo;
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
		return version;
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
	 * Getter for property departureDates.
	 * 
	 * @return Value of property departureDates.
	 *  
	 */
	public java.lang.String getDepartureDates() {
		return departureDates;
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
	 * Getter for property customerAllocation.
	 * 
	 * @return Value of property customerAllocation.
	 *  
	 */
	public java.util.Collection getCustomerAllocation() {
		return customerAllocation;
	}

	/**
	 * Setter for property customerAllocation.
	 * 
	 * @param customerAllocation
	 *            New value of property customerAllocation.
	 *  
	 */
	public void setCustomerAllocation(java.util.Collection customerAllocation) {
		this.customerAllocation = customerAllocation;
	}

	/**
	 * Getter for property supplierAllocation.
	 * 
	 * @return Value of property supplierAllocation.
	 *  
	 */
	public java.util.Collection getSupplierAllocation() {
		return supplierAllocation;
	}

	/**
	 * Setter for property supplierAllocation.
	 * 
	 * @param supplierAllocation
	 *            New value of property supplierAllocation.
	 *  
	 */
	public void setSupplierAllocation(java.util.Collection supplierAllocation) {
		this.supplierAllocation = supplierAllocation;
	}

	/**
	 * Getter for property sellPurchaseTable.
	 * 
	 * @return Value of property sellPurchaseTable.
	 *  
	 */
	public java.lang.String getSellPurchaseTable() {
		return sellPurchaseTable;
	}

	/**
	 * Setter for property sellPurchaseTable.
	 * 
	 * @param sellPurchaseTable
	 *            New value of property sellPurchaseTable.
	 *  
	 */
	public void setSellPurchaseTable(java.lang.String sellPurchaseTable) {
		this.sellPurchaseTable = sellPurchaseTable;
	}

	/**
	 * Getter for property sellCustDateTable.
	 * 
	 * @return Value of property sellCustDateTable.
	 *  
	 */
	public java.lang.String getSellCustDateTable() {
		return sellCustDateTable;
	}

	/**
	 * Setter for property sellCustDateTable.
	 * 
	 * @param sellCustDateTable
	 *            New value of property sellCustDateTable.
	 *  
	 */
	public void setSellCustDateTable(java.lang.String sellCustDateTable) {
		this.sellCustDateTable = sellCustDateTable;
	}

	/**
	 * Getter for property purcSectorDateTable.
	 * 
	 * @return Value of property purcSectorDateTable.
	 *  
	 */
	public java.lang.String getPurcSectorDateTable() {
		return purcSectorDateTable;
	}

	/**
	 * Setter for property purcSectorDateTable.
	 * 
	 * @param purcSectorDateTable
	 *            New value of property purcSectorDateTable.
	 *  
	 */
	public void setPurcSectorDateTable(java.lang.String purcSectorDateTable) {
		this.purcSectorDateTable = purcSectorDateTable;
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