package uk.co.firstchoice.stella.frontend;

/*******************************************************************************
 * Save information about a refund ticket.
 ******************************************************************************/

public class RefundTicket {

	private String ticketNumber = "";

	private float fareUsed = 0;

	private float airlinePenalty = 0;

	private float taxAdjust = 0;

	private float seatCost = 0;

	private float taxCost = 0;

	private float totalSeatCost = 0;

	private float totalTax = 0;

	private float commissionPct = 0;

	private float commissionAmt = 0;

	private float documentTotal = 0;

	private boolean deleted = false; //Has the refund been edited and this

	// ticket removed?

	private boolean updated = false; //Has the refund been edited and this

	// ticket updated?

	private boolean added = false; //Has the refund been edited and this ticket

	// added?

	private String createdBy = ""; //The user who initially created this refund

	private String createdOn = ""; //... and the date

	private String lastEditedBy = ""; //The user who last edited this refund

	private String lastEditedOn = ""; //... and the date

	public RefundTicket() {
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String tn) {
		ticketNumber = tn;
	}

	public float getFareUsedFloat() {
		return fareUsed;
	}

	public String getFareUsedString() {
		return StellaUtils.floatToString(fareUsed);
	}

	public void setFareUsed(String fare) throws StellaException {
		fareUsed = StellaUtils.stringToFloat(fare);
	}

	public void setFareUsed(float fare) {
		fareUsed = fare;
	}

	public float getAirlinePenaltyFloat() {
		return airlinePenalty;
	}

	public String getAirlinePenaltyString() {
		return StellaUtils.floatToString(airlinePenalty);
	}

	public void setAirlinePenalty(String fare) throws StellaException {
		airlinePenalty = StellaUtils.stringToFloat(fare);
	}

	public void setAirlinePenalty(float fare) {
		airlinePenalty = fare;
	}

	public float getTaxAdjustFloat() {
		return taxAdjust;
	}

	public String getTaxAdjustString() {
		return StellaUtils.floatToString(taxAdjust);
	}

	public void setTaxAdjust(String fare) throws StellaException {
		taxAdjust = StellaUtils.stringToFloat(fare);
	}

	public void setTaxAdjust(float fare) {
		taxAdjust = fare;
	}

	public float getSeatCostFloat() {
		return seatCost;
	}

	public String getSeatCostString() {
		return StellaUtils.floatToString(seatCost);
	}

	public void setSeatCost(String fare) throws StellaException {
		seatCost = StellaUtils.stringToFloat(fare);
	}

	public void setSeatCost(float fare) {
		seatCost = fare;
	}

	public float getTaxCostFloat() {
		return taxCost;
	}

	public String getTaxCostString() {
		return StellaUtils.floatToString(taxCost);
	}

	public void setTaxCost(String fare) throws StellaException {
		taxCost = StellaUtils.stringToFloat(fare);
	}

	public void setTaxCost(float fare) {
		taxCost = fare;
	}

	public void setTotalSeatCost(float tsc) {
		totalSeatCost = tsc;
	}

	public void setTotalSeatCost(String tsc) throws StellaException {
		totalSeatCost = StellaUtils.stringToFloat(tsc);
	}

	public void setTotalTax(float tsc) {
		totalTax = tsc;
	}

	public void setTotalTax(String tsc) throws StellaException {
		totalTax = StellaUtils.stringToFloat(tsc);
	}

	//	public float getTotalRefundFloat() { return
	// (airlinePenalty-totalSeatCost-totalTax+fareUsed-commissionAmt+taxAdjust);
	// }
	//	public String getTotalRefundString() { return
	// StellaUtils.floatToString(getTotalRefundFloat()); }

	//	public float getTotalCostFloat() { return
	// seatCost+taxCost+airlinePenalty; }
	//	public String getTotalCostString() { return
	// StellaUtils.floatToString(getTotalCostFloat()); }

	public float getCommissionPctFloat() {
		return commissionPct;
	}

	public String getCommissionPct() {
		return StellaUtils.floatToString(commissionPct);
	}

	public void setCommissionPct(String fare) throws StellaException {
		commissionPct = StellaUtils.stringToFloat(fare);
	}

	public void setCommissionPct(float fare) {
		commissionPct = fare;
	}

	public float getCommissionAmtFloat() {
		return commissionAmt;
	}

	public String getCommissionAmt() {
		return StellaUtils.floatToString(commissionAmt);
	}

	public void setCommissionAmt(String fare) throws StellaException {
		commissionAmt = StellaUtils.stringToFloat(fare);
	}

	public void setCommissionAmt(float fare) {
		commissionAmt = fare;
	}

	/**
	 * Getter for property deleted.
	 * 
	 * @return Value of property deleted.
	 *  
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * Setter for property deleted.
	 * 
	 * @param deleted
	 *            New value of property deleted.
	 *  
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * Getter for property updated.
	 * 
	 * @return Value of property updated.
	 *  
	 */
	public boolean isUpdated() {
		return updated;
	}

	/**
	 * Setter for property updated.
	 * 
	 * @param updated
	 *            New value of property updated.
	 *  
	 */
	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	/**
	 * Getter for property added.
	 * 
	 * @return Value of property added.
	 *  
	 */
	public boolean isAdded() {
		return added;
	}

	/**
	 * Setter for property added.
	 * 
	 * @param added
	 *            New value of property added.
	 *  
	 */
	public void setAdded(boolean added) {
		this.added = added;
	}

	/**
	 * Getter for property documentTotal.
	 * 
	 * @return Value of property documentTotal.
	 *  
	 */
	public float getDocumentTotalFloat() {
		return documentTotal;
	}

	public String getDocumentTotal() {
		return StellaUtils.floatToString(documentTotal);
	}

	/**
	 * Setter for property documentTotal.
	 * 
	 * @param documentTotal
	 *            New value of property documentTotal.
	 *  
	 */
	public void setDocumentTotal(float documentTotal) {
		this.documentTotal = documentTotal;
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

}