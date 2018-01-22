package uk.co.firstchoice.stella.frontend;

public class RefundBean {

	private String documentType = "";

	private String documentNumber = "";

	private String documentDate = "";

	private String disputeAdm = "";

	private String disputeDate = "";

	private String postTicket = "";

	private String saveError = "";

	private String iataNumber = "";

	private String createdBy = ""; //The user who initially created this refund

	private String createdOn = ""; //... and the date

	private String lastEditedBy = ""; //The user who last edited this refund

	private String lastEditedOn = ""; //... and the date

	private String admReasonCode = "";

	private String admReasonText = "";
	
	private String docType = "";

	public RefundBean() {
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String value) {
		documentType = value;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String value) {
		documentNumber = value;
	}

	public String getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(String value) {
		documentDate = value;
	}

	public String getDisputeAdm() {
		return disputeAdm;
	}

	public void setDisputeAdm(String value) {
		disputeAdm = value;
	}

	public String getDisputeDate() {
		return disputeDate;
	}

	public void setDisputeDate(String value) {
		disputeDate = value;
	}

	public void setPostTicket(String value) {
		postTicket = value;
	}

	public String getPostTicket() {
		return postTicket;
	}

	public String getSaveError() {
		return saveError;
	}

	public void setSaveError(String value) {
		saveError = value;
	}

	/**
	 * Getter for property iataNumber.
	 * 
	 * @return Value of property iataNumber.
	 *  
	 */
	public java.lang.String getIataNumber() {
		return iataNumber;
	}

	/**
	 * Setter for property iataNumber.
	 * 
	 * @param iataNumber
	 *            New value of property iataNumber.
	 *  
	 */
	public void setIataNumber(java.lang.String iataNumber) {
		this.iataNumber = iataNumber;
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

	/**
	 * Getter for property admReasonCode.
	 * 
	 * @return Value of property admReasonCode.
	 */
	public java.lang.String getAdmReasonCode() {
		return admReasonCode;
	}

	/**
	 * Setter for property admReasonCode.
	 * 
	 * @param admReasonCode
	 *            New value of property admReasonCode.
	 */
	public void setAdmReasonCode(java.lang.String admReasonCode) {
		this.admReasonCode = admReasonCode == null ? "" : admReasonCode
				.split("~")[0];
	}

	/**
	 * Getter for property admReasonText.
	 * 
	 * @return Value of property admReasonText.
	 */
	public java.lang.String getAdmReasonText() {
		return admReasonText;
	}

	/**
	 * Setter for property admReasonText.
	 * 
	 * @param admReasonText
	 *            New value of property admReasonText.
	 */
	public void setAdmReasonText(java.lang.String admReasonText) {
		this.admReasonText = admReasonText;
	}



	/**
	 * Getter for property 	docType.
	 * 
	 * @return Value of property docType.
	 *  
	 */
	public java.lang.String getDocType() {
		return docType;
	}

	/**
	 * Setter for property docType.
	 * 
	 * @param docType
	 *            New value of property docType.
	 *  
	 */
	public void setDocType(java.lang.String docType) {
		this.docType = docType;
	}

}