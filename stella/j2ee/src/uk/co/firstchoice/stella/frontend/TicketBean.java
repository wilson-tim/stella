package uk.co.firstchoice.stella.frontend;

/*******************************************************************************
 * Save information about a ticket. Used to store the last saved ticket in case
 * of repeat or conjunction tickets need the info, or to store ticket info to
 * display found tickets.
 ******************************************************************************/

public class TicketBean {

	private String lastSaveType = ""; // used to indicate if last was

	// conjunction or repeat ticket

	// of if it's a ticket being edited (c, r, e)
	private String ticketType = "";

	private String airlineNumber = "";

	private String airlineName = "";

	private String ticketNumber = "";

	private String iataNumber = "";

	private String eTicket = "";

	private String departureDate = "";

	private String issueDate = "";

	private String agentInit = "";

	private String pnr = "";

	private String passengerType = "";

	private String passengerName = "";

	private String numPassengers = "1";

	private float publishedFare = 0;

	private float sellingFare = 0;

	private float commissionAmt = 0;

	private float commissionPct = 0;

	private float gbTax = 0;

	private float ubTax = 0;

	private float remainingTax = 0;

	private String atopReference = "";

	private String atopSeason = "";

	private String branchCode = "";

	private String sourceIndicator = "";

	private String exchangeTicketNo = "";

	private long pnrUniqueKey;

	private boolean sectorPayment = false;

	private String tourCode = "";

	private String fareBasisCode = "";

	private String createdBy = ""; //The user who initially created this refund

	private String createdOn = ""; //... and the date

	private String lastEditedBy = ""; //The user who last edited this refund

	private String lastEditedOn = ""; //... and the date

	private String pnrDate = "";

	private String conjunctionMaster = ""; //Link to the head ticket

	private String saveError = ""; //In case there was an error

	public TicketBean() {
	}

	public String getLastSaveType() {
		return lastSaveType;
	}

	public void setLastSaveType(String value) {
		lastSaveType = value;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String value) {
		ticketType = value;
	}

	public String getAirlineNumber() {
		return airlineNumber;
	}

	public void setAirlineNumber(String value) {
		airlineNumber = value;
	}

	public String getAirlineName() {
		return airlineName;
	}

	public void setAirlineName(String value) {
		airlineName = value;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String value) {
		ticketNumber = value;
	}

	public String getIataNumber() {
		return iataNumber;
	}

	public void setIataNumber(String value) {
		iataNumber = value;
	}

	public String getEticket() {
		return eTicket;
	}

	public void setEticket(String value) {
		eTicket = value;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(String value) {
		departureDate = value;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String value) {
		issueDate = value;
	}

	public String getAgentInit() {
		return agentInit;
	}

	public void setAgentInit(String value) {
		agentInit = value;
	}

	public String getPnr() {
		return pnr;
	}

	public void setPnr(String value) {
		pnr = value;
	}

	public String getPassengerType() {
		return passengerType;
	}

	public void setPassengerType(String value) {
		passengerType = value;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String value) {
		passengerName = value;
	}

	public String getNumPassengers() {
		return numPassengers;
	}

	public void setNumPassengers(String value) {
		numPassengers = value;
	}

	public String getPublishedFare() {
		return StellaUtils.floatToString(publishedFare);
	}

	public float getPublishedFareFloat() {
		return publishedFare;
	}

	public void setPublishedFare(String value) throws StellaException {
		publishedFare = StellaUtils.stringToFloat(value);
	}

	public void setPublishedFare(float value) {
		publishedFare = value;
	}

	public String getSellingFare() {
		return StellaUtils.floatToString(sellingFare);
	}

	public float getSellingFareFloat() {
		return sellingFare;
	}

	public void setSellingFare(String value) throws StellaException {
		sellingFare = StellaUtils.stringToFloat(value);
	}

	public void setSellingFare(float value) {
		sellingFare = value;
	}

	public String getCommissionPct() {
		return StellaUtils.floatToString(commissionPct);
	}

	public float getCommissionPctFloat() {
		return commissionPct;
	}

	public void setCommissionPct(String value) throws StellaException {
		commissionPct = StellaUtils.stringToFloat(value);
	}

	public void setCommissionPct(float value) {
		commissionPct = value;
	}

	public String getCommissionAmt() {
		return StellaUtils.floatToString(commissionAmt);
	}

	public float getCommissionAmtFloat() {
		return commissionAmt;
	}

	public void setCommissionAmt(String value) throws StellaException {
		commissionAmt = StellaUtils.stringToFloat(value);
	}

	public void setCommissionAmt(float value) {
		commissionAmt = value;
	}

	public String getGbTax() {
		return StellaUtils.floatToString(gbTax);
	}

	public float getGbTaxFloat() {
		return gbTax;
	}

	public void setGbTax(String value) throws StellaException {
		gbTax = StellaUtils.stringToFloat(value);
	}

	public void setGbTax(float value) {
		gbTax = value;
	}

	public String getUbTax() {
		return StellaUtils.floatToString(ubTax);
	}

	public float getUbTaxFloat() {
		return ubTax;
	}

	public void setUbTax(String value) throws StellaException {
		ubTax = StellaUtils.stringToFloat(value);
	}

	public void setUbTax(float value) {
		ubTax = value;
	}

	public String getRemainingTax() {
		return StellaUtils.floatToString(remainingTax);
	}

	public float getRemainingTaxFloat() {
		return remainingTax;
	}

	public void setRemainingTax(String value) throws StellaException {
		remainingTax = StellaUtils.stringToFloat(value);
	}

	public void setRemainingTax(float value) {
		remainingTax = value;
	}

	public String getSeatCostString() {
		return StellaUtils.floatToString(getSeatCost());
	}

	public float getSeatCost() {
		return sellingFare == publishedFare ? publishedFare - commissionAmt
				- (ubTax * commissionPct / 100 * (sectorPayment ? 0 : 1))
				: sellingFare;
	}

	public String getTotalTaxString() {
		return StellaUtils.floatToString(getTotalTax());
	}

	public float getTotalTax() {
		//		return
		// gbTax+ubTax*(sellingFare==publishedFare?(100.0F-commissionPct)/100.0F:1.0F)+remainingTax;
		return gbTax + ubTax + remainingTax;
	}

	public String getAtopReference() {
		return atopReference;
	}

	public void setAtopReference(String value) {
		atopReference = value;
	}

	public String getAtopSeason() {
		return atopSeason;
	}

	public void setAtopSeason(String value) {
		atopSeason = value;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String value) {
		branchCode = value;
	}

	public String getSourceIndicator() {
		return sourceIndicator;
	}

	public void setSourceIndicator(String value) {
		sourceIndicator = value;
	}

	public String getExchangeTicketNo() {
		return exchangeTicketNo;
	}

	public void setExchangeTicketNo(String value) {
		exchangeTicketNo = value;
	}

	public long getPnrUniqueKey() {
		return pnrUniqueKey;
	}

	public void setPnrUniqueKey(long l) {
		pnrUniqueKey = l;
	}

	public String getConjunctionMaster() {
		return conjunctionMaster;
	}

	public void setConjunctionMaster(String value) {
		conjunctionMaster = value;
	}

	public String getSaveError() {
		return saveError;
	}

	public void setSaveError(String value) {
		saveError = value;
	}

	/**
	 * Getter for property sectorPayment.
	 * 
	 * @return Value of property sectorPayment.
	 *  
	 */
	public boolean isSectorPayment() {
		return sectorPayment;
	}

	/**
	 * Setter for property sectorPayment.
	 * 
	 * @param sectorPayment
	 *            New value of property sectorPayment.
	 *  
	 */
	public void setSectorPayment(boolean sectorPayment) {
		this.sectorPayment = sectorPayment;
	}

	/**
	 * Getter for property fareBasisCode.
	 * 
	 * @return Value of property fareBasisCode.
	 *  
	 */
	public java.lang.String getFareBasisCode() {
		return fareBasisCode;
	}

	/**
	 * Setter for property fareBasisCode.
	 * 
	 * @param fareBasisCode
	 *            New value of property fareBasisCode.
	 *  
	 */
	public void setFareBasisCode(java.lang.String fareBasisCode) {
		this.fareBasisCode = fareBasisCode;
	}

	/**
	 * Getter for property tourCode.
	 * 
	 * @return Value of property tourCode.
	 *  
	 */
	public java.lang.String getTourCode() {
		return tourCode;
	}

	/**
	 * Setter for property tourCode.
	 * 
	 * @param tourCode
	 *            New value of property tourCode.
	 *  
	 */
	public void setTourCode(java.lang.String tourCode) {
		this.tourCode = tourCode;
	}

	/**
	 * Getter for property lastEditedBy.
	 * 
	 * @return Value of property lastEditedBy.
	 *  
	 */
	public java.lang.String getLastEditedBy() {
		return lastEditedBy;
	}

	/**
	 * Setter for property lastEditedBy.
	 * 
	 * @param lastEditedBy
	 *            New value of property lastEditedBy.
	 *  
	 */
	public void setLastEditedBy(java.lang.String lastEditedBy) {
		this.lastEditedBy = lastEditedBy;
	}

	/**
	 * Getter for property lastEditedOn.
	 * 
	 * @return Value of property lastEditedOn.
	 *  
	 */
	public java.lang.String getLastEditedOn() {
		return lastEditedOn;
	}

	/**
	 * Setter for property lastEditedOn.
	 * 
	 * @param lastEditedOn
	 *            New value of property lastEditedOn.
	 *  
	 */
	public void setLastEditedOn(java.lang.String lastEditedOn) {
		this.lastEditedOn = lastEditedOn;
	}

	/**
	 * Getter for property createdBy.
	 * 
	 * @return Value of property createdBy.
	 *  
	 */
	public java.lang.String getCreatedBy() {
		return createdBy;
	}

	/**
	 * Setter for property createdBy.
	 * 
	 * @param createdBy
	 *            New value of property createdBy.
	 *  
	 */
	public void setCreatedBy(java.lang.String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Getter for property createdOn.
	 * 
	 * @return Value of property createdOn.
	 *  
	 */
	public java.lang.String getCreatedOn() {
		return createdOn;
	}

	/**
	 * Setter for property createdOn.
	 * 
	 * @param createdOn
	 *            New value of property createdOn.
	 *  
	 */
	public void setCreatedOn(java.lang.String createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * Getter for property pnrDate.
	 * 
	 * @return Value of property pnrDate.
	 */
	public java.lang.String getPnrDate() {
		return pnrDate;
	}

	/**
	 * Setter for property pnrDate.
	 * 
	 * @param pnrDate
	 *            New value of property pnrDate.
	 */
	public void setPnrDate(java.lang.String pnrDate) {
		this.pnrDate = pnrDate;
	}

}