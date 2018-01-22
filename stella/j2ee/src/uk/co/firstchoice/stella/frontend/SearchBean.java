package uk.co.firstchoice.stella.frontend;

public class SearchBean {

	private String airlineNumber = "";

	private String ticketNumber = "";

	private String refundNumber = "";

	private String departureDate = "";

	private String issueDate = "";

	private String pnr = "";

	private String passengerName = "";

	private String atopReference = "";

	private String atopSeason = "";

	private String refundLetterNumber = "";

	private String iataNumber = "";


	private boolean searchPerformed = false;

	public SearchBean() {
	}

	public String getAirlineNumber() {
		return airlineNumber;
	}

	public void setAirlineNumber(String value) {
		airlineNumber = value;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String value) {
		ticketNumber = value;
	}

	public String getRefundNumber() {
		return refundNumber;
	}

	public void setRefundNumber(String value) {
		refundNumber = value;
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

	public String getPnr() {
		return pnr;
	}

	public void setPnr(String value) {
		pnr = value;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String value) {
		passengerName = value;
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

	public boolean getSearchPerformed() {
		return searchPerformed;
	}

	public void setSearchPerformed(boolean s) {
		searchPerformed = s;
	}

	/**
	 * Getter for property iataNumber.
	 * 
	 * @return Value of property iataNumber.
	 *  
	 */
	public String getIataNumber() {
		return iataNumber;
	}

	/**
	 * Setter for property iataNumber.
	 * 
	 * @param iataNumber
	 *            New value of property iataNumber.
	 *  
	 */
	public void setIataNumber(String iataNumber) {
		this.iataNumber = iataNumber;
	}

	/**
	 * Getter for property refundLetterNumber.
	 * 
	 * @return Value of property refundLetterNumber.
	 *  
	 */
	public String getRefundLetterNumber() {
		return refundLetterNumber;
	}

	/**
	 * Setter for property refundLetterNumber.
	 * 
	 * @param refundLetterNumber
	 *            New value of property refundLetterNumber.
	 *  
	 */
	public void setRefundLetterNumber(String refundLetterNumber) {
		this.refundLetterNumber = refundLetterNumber;
	}



	
}