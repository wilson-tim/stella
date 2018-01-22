/*
 * RefundLetterBean.java
 *
 * Created on 04 June 2003, 11:53
 */

package uk.co.firstchoice.stella.frontend;

import java.util.Vector;

/**
 * 
 * @author Lsvensson
 */
public class RefundLetterBean {

	// Variables set up at the start of RefundLetter
	private Vector allTickets;

	private java.util.Date creationDate;

	private int airlineNum;

	private String airlineName;

	private AirlineBean airlineBean;

	private MatchExceptionsBean matchExceptionsBean;

	private int letterNo = 0;

	private String letterText = "";

	private String[] letterTextArray = null;

	private String officeAddress = "";

	private String unmatchedAmount = "";

	private String atopReference = "";

	private String atopSeason = "";

	// Variables returned by the GUI
	private String selectedTickets[];

	private String disputeReason = "";

	private boolean newRefundLetter = true; // otherwise we have fetched an old

	// one

	private String savedLetterText = ""; // the text from a fetched one

	private String letterAuthor = "";

	/** Creates a new instance of RefundLetterBean */
	public RefundLetterBean() {
		allTickets = new Vector();
		creationDate = new java.util.Date();
	}

	/**
	 * Getter for property selectedTickets.
	 * 
	 * @return Value of property selectedTickets.
	 *  
	 */
	public String[] getSelectedTickets() {
		return selectedTickets;
	}

	/**
	 * Setter for property selectedTickets.
	 * 
	 * @param st[]
	 *            New value of property selectedTickets.
	 *  
	 */
	public void setSelectedTickets(String st[]) {
		selectedTickets = st;
	}

	/**
	 * Getter for property allTickets.
	 * 
	 * @return Value of property allTickets.
	 *  
	 */
	public java.util.Vector getAllTickets() {
		return allTickets;
	}

	/**
	 * Setter for property allTickets.
	 * 
	 * @param allTickets
	 *            New value of property allTickets.
	 *  
	 */
	public void setAllTickets(java.util.Vector allTickets) {
		this.allTickets = allTickets;
	}

	/**
	 * Getter for property disputeReason.
	 * 
	 * @return Value of property disputeReason.
	 *  
	 */
	public java.lang.String getDisputeReason() {
		return disputeReason;
	}

	/**
	 * Setter for property disputeReason.
	 * 
	 * @param disputeReason
	 *            New value of property disputeReason.
	 *  
	 */
	public void setDisputeReason(java.lang.String disputeReason) {
		this.disputeReason = disputeReason;
	}

	/**
	 * Getter for property airlineNum.
	 * 
	 * @return Value of property airlineNum.
	 *  
	 */
	public int getAirlineNum() {
		return airlineNum;
	}

	/**
	 * Setter for property airlineNum.
	 * 
	 * @param airlineNum
	 *            New value of property airlineNum.
	 *  
	 */
	public void setAirlineNum(int airlineNum) {
		this.airlineNum = airlineNum;
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
	 * Getter for property airlineBean.
	 * 
	 * @return Value of property airlineBean.
	 *  
	 */
	public uk.co.firstchoice.stella.frontend.AirlineBean getAirlineBean() {
		return airlineBean;
	}

	/**
	 * Setter for property airlineBean.
	 * 
	 * @param airlineBean
	 *            New value of property airlineBean.
	 *  
	 */
	public void setAirlineBean(uk.co.firstchoice.stella.frontend.AirlineBean ab) {
		this.airlineBean = ab;
	}

	/**
	 * Getter for property creationDate.
	 * 
	 * @return Value of property creationDate.
	 *  
	 */
	public java.util.Date getCreationDate() {
		return creationDate;
	}

	public String getFormattedCreationDate() {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"EEEE, d MMMM yyyy");
		return sdf.format(creationDate);
	}

	/**
	 * Setter for property creationDate.
	 * 
	 * @param creationDate
	 *            New value of property creationDate.
	 *  
	 */
	public void setCreationDate(java.util.Date cd) {
		creationDate = cd;
	}

	/**
	 * Getter for property letterNo.
	 * 
	 * @return Value of property letterNo.
	 *  
	 */
	public int getLetterNo() {
		return letterNo;
	}

	/**
	 * Setter for property letterNo.
	 * 
	 * @param letterNo
	 *            New value of property letterNo.
	 *  
	 */
	public void setLetterNo(int lNo) {
		letterNo = lNo;
	}

	public String getLetterReference() {
		return StellaUtils.leftPad(letterNo, 6) + "-" + atopReference + "/"
				+ atopSeason;
	}

	public java.lang.String getLetterText1() {
		return (letterTextArray.length > 0 ? letterTextArray[0] : "");
	}

	public java.lang.String getLetterText2() {
		return (letterTextArray.length > 1 ? letterTextArray[1] : "");
	}

	public java.lang.String getLetterText3() {
		return (letterTextArray.length > 2 ? letterTextArray[2] : "");
	}

	public java.lang.String getLetterText4() {
		return (letterTextArray.length > 3 ? letterTextArray[3] : "");
	}

	public void setLetterText1(String txt) {
		if (letterTextArray != null && letterTextArray.length > 0)
			letterTextArray[0] = txt;
	}

	public void setLetterText2(String txt) {
		if (letterTextArray != null && letterTextArray.length > 1)
			letterTextArray[1] = txt;
	}

	public void setLetterText3(String txt) {
		if (letterTextArray != null && letterTextArray.length > 2)
			letterTextArray[2] = txt;
	}

	public void setLetterText4(String txt) {
		if (letterTextArray != null && letterTextArray.length > 3)
			letterTextArray[3] = txt;
	}

	/**
	 * Setter for property letterText.
	 * 
	 * @param letterText
	 *            New value of property letterText.
	 *  
	 */
	public void setLetterText(java.lang.String letterText) {
		this.letterText = letterText;
		letterTextArray = letterText.split("~");
	}

	/**
	 * Getter for property matchExceptionsBean.
	 * 
	 * @return Value of property matchExceptionsBean.
	 *  
	 */
	public uk.co.firstchoice.stella.frontend.MatchExceptionsBean getMatchExceptionsBean() {
		return matchExceptionsBean;
	}

	/**
	 * Setter for property matchExceptionsBean.
	 * 
	 * @param matchExceptionsBean
	 *            New value of property matchExceptionsBean.
	 *  
	 */
	public void setMatchExceptionsBean(
			uk.co.firstchoice.stella.frontend.MatchExceptionsBean matchExceptionsBean) {
		this.matchExceptionsBean = matchExceptionsBean;
	}

	/**
	 * Getter for property officeAddress.
	 * 
	 * @return Value of property officeAddress.
	 *  
	 */
	public java.lang.String getOfficeAddress() {
		return officeAddress;
	}

	/**
	 * Setter for property officeAddress.
	 * 
	 * @param officeAddress
	 *            New value of property officeAddress.
	 *  
	 */
	public void setOfficeAddress(java.lang.String officeAddress) {
		this.officeAddress = officeAddress;
	}

	/**
	 * Getter for property unmatchedAmount.
	 * 
	 * @return Value of property unmatchedAmount.
	 *  
	 */
	public java.lang.String getUnmatchedAmount() {
		return unmatchedAmount;
	}

	/**
	 * Setter for property unmatchedAmount.
	 * 
	 * @param unmatchedAmount
	 *            New value of property unmatchedAmount.
	 *  
	 */
	public void setUnmatchedAmount(java.lang.String unmatchedAmount) {
		this.unmatchedAmount = unmatchedAmount;
	}

	/**
	 * Getter for property newRefundLetter.
	 * 
	 * @return Value of property newRefundLetter.
	 *  
	 */
	public boolean isNewRefundLetter() {
		return newRefundLetter;
	}

	/**
	 * Setter for property newRefundLetter.
	 * 
	 * @param newRefundLetter
	 *            New value of property newRefundLetter.
	 *  
	 */
	public void setNewRefundLetter(boolean newRefundLetter) {
		this.newRefundLetter = newRefundLetter;
	}

	/**
	 * Getter for property savedLetterText.
	 * 
	 * @return Value of property savedLetterText.
	 *  
	 */
	public java.lang.String getSavedLetterText() {
		return savedLetterText;
	}

	/**
	 * Setter for property savedLetterText.
	 * 
	 * @param savedLetterText
	 *            New value of property savedLetterText.
	 *  
	 */
	public void setSavedLetterText(java.lang.String savedLetterText) {
		this.savedLetterText = savedLetterText;
	}

	/**
	 * Getter for property letterAuthor.
	 * 
	 * @return Value of property letterAuthor.
	 *  
	 */
	public java.lang.String getLetterAuthor() {
		return letterAuthor;
	}

	/**
	 * Setter for property letterAuthor.
	 * 
	 * @param letterAuthor
	 *            New value of property letterAuthor.
	 *  
	 */
	public void setLetterAuthor(java.lang.String letterAuthor) {
		this.letterAuthor = letterAuthor;
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

}