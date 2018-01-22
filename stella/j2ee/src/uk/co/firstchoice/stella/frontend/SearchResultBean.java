/*
 * SearchResultBean.java
 *
 * Created on 21 February 2003, 10:39
 */
package uk.co.firstchoice.stella.frontend;

/**
 * 
 * @author Lsvensson
 */
public class SearchResultBean {

	private String documentType = ""; //refund / ticket

	private String documentNo = "";

	private String pnr = "";

	private String airlineName = "";

	private String passengerName = "";

	private String departureDate = "";

	private String atopReference = "";

	private String atopSeason = "";

	private float documentTotal = 0;

	/** Default, no parameter, constructor */
	public SearchResultBean() {
	}

	/**
	 * Getter for property airlineName.
	 * 
	 * @return Value of property airlineName.
	 *  
	 */
	public java.lang.String getAirlineName() {
		return airlineName;
	}

	/**
	 * Setter for property airlineName.
	 * 
	 * @param airlineName
	 *            New value of property airlineName.
	 *  
	 */
	public void setAirlineName(java.lang.String airlineName) {
		this.airlineName = airlineName;
	}

	/**
	 * Getter for property atopReference.
	 * 
	 * @return Value of property atopReference.
	 *  
	 */
	public java.lang.String getAtopReference() {
		return atopReference;
	}

	/**
	 * Setter for property atopReference.
	 * 
	 * @param atopReference
	 *            New value of property atopReference.
	 *  
	 */
	public void setAtopReference(java.lang.String atopReference) {
		this.atopReference = atopReference;
	}

	/**
	 * Getter for property atopSeason.
	 * 
	 * @return Value of property atopSeason.
	 *  
	 */
	public java.lang.String getAtopSeason() {
		return atopSeason;
	}

	/**
	 * Setter for property atopSeason.
	 * 
	 * @param atopSeason
	 *            New value of property atopSeason.
	 *  
	 */
	public void setAtopSeason(java.lang.String atopSeason) {
		this.atopSeason = atopSeason;
	}

	/**
	 * Getter for property departureDate.
	 * 
	 * @return Value of property departureDate.
	 *  
	 */
	public java.lang.String getDepartureDate() {
		return departureDate;
	}

	/**
	 * Setter for property departureDate.
	 * 
	 * @param departureDate
	 *            New value of property departureDate.
	 *  
	 */
	public void setDepartureDate(java.lang.String departureDate) {
		this.departureDate = departureDate;
	}

	/**
	 * Getter for property documentNo.
	 * 
	 * @return Value of property documentNo.
	 *  
	 */
	public java.lang.String getDocumentNo() {
		return documentNo;
	}

	/**
	 * Setter for property documentNo.
	 * 
	 * @param documentNo
	 *            New value of property documentNo.
	 *  
	 */
	public void setDocumentNo(java.lang.String documentNo) {
		this.documentNo = documentNo;
	}

	/**
	 * Getter for property documentType.
	 * 
	 * @return Value of property documentType.
	 *  
	 */
	public java.lang.String getDocumentType() {
		return documentType;
	}

	/**
	 * Setter for property documentType.
	 * 
	 * @param documentType
	 *            New value of property documentType.
	 *  
	 */
	public void setDocumentType(java.lang.String documentType) {
		this.documentType = documentType;
	}

	/**
	 * Getter for property passengerName.
	 * 
	 * @return Value of property passengerName.
	 *  
	 */
	public java.lang.String getPassengerName() {
		return passengerName;
	}

	/**
	 * Setter for property passengerName.
	 * 
	 * @param passengerName
	 *            New value of property passengerName.
	 *  
	 */
	public void setPassengerName(java.lang.String passengerName) {
		this.passengerName = passengerName;
	}

	/**
	 * Getter for property pnr.
	 * 
	 * @return Value of property pnr.
	 *  
	 */
	public java.lang.String getPnr() {
		return pnr;
	}

	/**
	 * Setter for property pnr.
	 * 
	 * @param pnr
	 *            New value of property pnr.
	 *  
	 */
	public void setPnr(java.lang.String pnr) {
		this.pnr = pnr;
	}

	/**
	 * Getter for property docTotal.
	 * 
	 * @return Value of property docTotal.
	 *  
	 */
	public String getDocumentTotal() {
		return StellaUtils.floatToString(documentTotal);
	}

	/**
	 * Setter for property docTotal.
	 * 
	 * @param docTotal
	 *            New value of property docTotal.
	 *  
	 */
	public void setDocumentTotal(float documentTotal) {
		this.documentTotal = documentTotal;
	}

}