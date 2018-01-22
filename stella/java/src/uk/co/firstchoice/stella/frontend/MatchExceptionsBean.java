/*
 * MatchExceptionsBean.java
 *
 * Created on 12 March 2003, 12:07
 */

package uk.co.firstchoice.stella.frontend;

/**
 * 
 * @author Lsvensson
 */

public class MatchExceptionsBean {

	private String matchProcessDate = "";

	private String atopReference = "";

	private String atopSeason = "";

	private String pnr = "";

	private String departureDate = "";

	private String failType = "";

	private String source = "";

	private String dwSeatCost = "";

	private String dwTaxCost = "";

	private String dwTotalCost = "";

	private String stellaSeatCost = "";

	private String stellaTaxCost = "";

	private String stellaTotalCost = "";

	private String unmatchedAmount = "";

	private String oldReasonCode = "";

	private String newReasonCode = "";

	private String exceptionType = "";
	
	
	//private String specialistBranch = "";

	private long pnr_id = 0;

	/** Creates a new instance of ExceptionsBean */
	public MatchExceptionsBean() {
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
	 * Getter for property dwTaxCost.
	 * 
	 * @return Value of property dwTaxCost.
	 *  
	 */
	public String getDwTaxCost() {
		return dwTaxCost;
	}

	/**
	 * Setter for property daTaxCost.
	 * 
	 * @param dwTaxCost
	 *            New value of property dwTaxCost.
	 *  
	 */
	public void setDwTaxCost(float dwTaxCost) {
		this.dwTaxCost = StellaUtils.floatToString(dwTaxCost);
	}

	/**
	 * Getter for property dwSeatCost.
	 * 
	 * @return Value of property dwSeatCost.
	 *  
	 */
	public String getDwSeatCost() {
		return dwSeatCost;
	}

	/**
	 * Setter for property dwSeatCost.
	 * 
	 * @param dwSeatCost
	 *            New value of property dwSeatCost.
	 *  
	 */
	public void setDwSeatCost(float dwSeatCost) {
		this.dwSeatCost = StellaUtils.floatToString(dwSeatCost);
	}

	/**
	 * Getter for property dwTotalCost.
	 * 
	 * @return Value of property dwTotalCost.
	 *  
	 */
	public String getDwTotalCost() {
		return dwTotalCost;
	}

	/**
	 * Setter for property dwTotalCost.
	 * 
	 * @param dwTotalCost
	 *            New value of property dwTotalCost.
	 *  
	 */
	public void setDwTotalCost(float dwTotalCost) {
		this.dwTotalCost = StellaUtils.floatToString(dwTotalCost);
	}

	/**
	 * Getter for property failType.
	 * 
	 * @return Value of property failType.
	 *  
	 */
	public String getFailType() {
		return failType;
	}

	/**
	 * Setter for property failType.
	 * 
	 * @param failType
	 *            New value of property failType.
	 *  
	 */
	public void setFailType(String failType) {
		this.failType = failType;
	}

	/**
	 * Getter for property matchProcessDate.
	 * 
	 * @return Value of property matchProcessDate.
	 *  
	 */
	public String getMatchProcessDate() {
		return matchProcessDate;
	}

	/**
	 * Setter for property matchProcessDate.
	 * 
	 * @param matchProcessDate
	 *            New value of property matchProcessDate.
	 *  
	 */
	public void setMatchProcessDate(String matchProcessDate) {
		this.matchProcessDate = matchProcessDate;
	}

	/**
	 * Getter for property newReasonCode.
	 * 
	 * @return Value of property newReasonCode.
	 *  
	 */
	public String getNewReasonCode() {
		return newReasonCode;
	}

	/**
	 * Setter for property newReasonCode.
	 * 
	 * @param newReasonCode
	 *            New value of property newReasonCode.
	 *  
	 */
	public void setNewReasonCode(String newReasonCode) {
		this.newReasonCode = newReasonCode;
	}

	/**
	 * Getter for property oldReasonCode.
	 * 
	 * @return Value of property oldReasonCode.
	 *  
	 */
	public String getOldReasonCode() {
		return oldReasonCode;
	}

	/**
	 * Setter for property oldReasonCode.
	 * 
	 * @param oldReasonCode
	 *            New value of property oldReasonCode.
	 *  
	 */
	public void setOldReasonCode(String oldReasonCode) {
		this.oldReasonCode = oldReasonCode;
	}

	/**
	 * Getter for property pnr.
	 * 
	 * @return Value of property pnr.
	 *  
	 */
	public String getPnr() {
		return pnr;
	}

	/**
	 * Setter for property pnr.
	 * 
	 * @param pnr
	 *            New value of property pnr.
	 *  
	 */
	public void setPnr(String pnr) {
		this.pnr = pnr;
	}

	/**
	 * Getter for property stellaSeatCost.
	 * 
	 * @return Value of property stellaSeatCost.
	 *  
	 */
	public String getStellaSeatCost() {
		return stellaSeatCost;
	}

	/**
	 * Setter for property stellaSeatCost.
	 * 
	 * @param stellaSeatCost
	 *            New value of property stellaSeatCost.
	 *  
	 */
	public void setStellaSeatCost(float stellaSeatCost) {
		this.stellaSeatCost = StellaUtils.floatToString(stellaSeatCost);
	}

	/**
	 * Getter for property stellaTaxCost.
	 * 
	 * @return Value of property stellaTaxCost.
	 *  
	 */
	public String getStellaTaxCost() {
		return stellaTaxCost;
	}

	/**
	 * Setter for property stellaTaxCost.
	 * 
	 * @param stellaTaxCost
	 *            New value of property stellaTaxCost.
	 *  
	 */
	public void setStellaTaxCost(float stellaTaxCost) {
		this.stellaTaxCost = StellaUtils.floatToString(stellaTaxCost);
	}

	/**
	 * Getter for property stellaTotalCost.
	 * 
	 * @return Value of property stellaTotalCost.
	 *  
	 */
	public String getStellaTotalCost() {
		return stellaTotalCost;
	}

	/**
	 * Setter for property stellaTotalCost.
	 * 
	 * @param stellaTotalCost
	 *            New value of property stellaTotalCost.
	 *  
	 */
	public void setStellaTotalCost(float stellaTotalCost) {
		this.stellaTotalCost = StellaUtils.floatToString(stellaTotalCost);
	}

	/**
	 * Getter for property unmatchedAmount.
	 * 
	 * @return Value of property unmatchedAmount.
	 *  
	 */
	public String getUnmatchedAmount() {
		return unmatchedAmount;
	}

	/**
	 * Setter for property unmatchedAmount.
	 * 
	 * @param unmatchedAmount
	 *            New value of property unmatchedAmount.
	 *  
	 */
	public void setUnmatchedAmount(float unmatchedAmount) {
		this.unmatchedAmount = StellaUtils.floatToString(unmatchedAmount);
	}

	/**
	 * Getter for property pnr_id.
	 * 
	 * @return Value of property pnr_id.
	 *  
	 */
	public long getPnr_id() {
		return pnr_id;
	}

	/**
	 * Setter for property pnr_id.
	 * 
	 * @param pnr_id
	 *            New value of property pnr_id.
	 *  
	 */
	public void setPnr_id(long pi) {
		pnr_id = pi;
	}

	/**
	 * Getter for property source.
	 * 
	 * @return Value of property source.
	 *  
	 */
	public java.lang.String getSource() {
		return source;
	}

	/**
	 * Setter for property source.
	 * 
	 * @param source
	 *            New value of property source.
	 *  
	 */
	public void setSource(java.lang.String source) {
		this.source = source;
	}

	/**
	 * Getter for property exceptionType.
	 * 
	 * @return Value of property exceptionType.
	 *  
	 */
	public java.lang.String getExceptionType() {
		return exceptionType;
	}

	/**
	 * Setter for property exceptionType.
	 * 
	 * @param exceptionType
	 *            New value of property exceptionType.
	 *  
	 */
	public void setExceptionType(java.lang.String exceptionType) {
		this.exceptionType = exceptionType;
	}
	
	
	

}