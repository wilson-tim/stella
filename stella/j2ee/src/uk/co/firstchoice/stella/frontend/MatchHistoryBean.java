/*
 * MatchHistoryBean.java
 *
 * Created on 12 March 2003, 12:07
 */

package uk.co.firstchoice.stella.frontend;

/**
 * 
 * @author Lsvensson
 */

public class MatchHistoryBean {

	private String processCode = "";

	private String processDate = "";

	private String reasonCode = "";

	private String reasonShortDesc = "";

	private String amendedUserId = "";

	private String dwSeatCost = "";

	private String dwTaxCost = "";

	private String dwTotalCost = "";

	private String stellaSeatCost = "";

	private String stellaTaxCost = "";

	private String stellaTotalCost = "";

	private String unmatchedAmount = "";

	/** Creates a new instance of MatchHistoryBean */
	public MatchHistoryBean() {
	}

	/**
	 * Getter for property amendedUserId.
	 * 
	 * @return Value of property amendedUserId.
	 *  
	 */
	public String getAmendedUserId() {
		return amendedUserId;
	}

	/**
	 * Setter for property amendedUserId.
	 * 
	 * @param amendedUserId
	 *            New value of property amendedUserId.
	 *  
	 */
	public void setAmendedUserId(String amendedUserId) {
		this.amendedUserId = amendedUserId;
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
	 * Getter for property dwTaxCost.
	 * 
	 * @return Value of property dwTaxCost.
	 *  
	 */
	public String getDwTaxCost() {
		return dwTaxCost;
	}

	/**
	 * Setter for property dwTaxCost.
	 * 
	 * @param dwTaxCost
	 *            New value of property dwTaxCost.
	 *  
	 */
	public void setDwTaxCost(float dwTaxCost) {
		this.dwTaxCost = StellaUtils.floatToString(dwTaxCost);
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
	 * Getter for property processCode.
	 * 
	 * @return Value of property processCode.
	 *  
	 */
	public String getProcessCode() {
		return processCode;
	}

	/**
	 * Setter for property processCode.
	 * 
	 * @param processCode
	 *            New value of property processCode.
	 *  
	 */
	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}

	/**
	 * Getter for property processDate.
	 * 
	 * @return Value of property processDate.
	 *  
	 */
	public String getProcessDate() {
		return processDate;
	}

	/**
	 * Setter for property processDate.
	 * 
	 * @param processDate
	 *            New value of property processDate.
	 *  
	 */
	public void setProcessDate(String processDate) {
		this.processDate = processDate;
	}

	/**
	 * Getter for property reasonCode.
	 * 
	 * @return Value of property reasonCode.
	 *  
	 */
	public String getReasonCode() {
		return reasonCode;
	}

	/**
	 * Setter for property reasonCode.
	 * 
	 * @param reasonCode
	 *            New value of property reasonCode.
	 *  
	 */
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	/**
	 * Getter for property reasonShortDesc.
	 * 
	 * @return Value of property reasonShortDesc.
	 *  
	 */
	public String getReasonShortDesc() {
		return reasonShortDesc;
	}

	/**
	 * Setter for property reasonShortDesc.
	 * 
	 * @param reasonShortDesc
	 *            New value of property reasonShortDesc.
	 *  
	 */
	public void setReasonShortDesc(String reasonShortDesc) {
		this.reasonShortDesc = reasonShortDesc;
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

}
