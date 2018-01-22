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

public class MatchDescriptionBean {

	private String reasonCode = "";

	private String reasonDescription = "";

	/** Creates a new instance of MatchHistoryBean */
	public MatchDescriptionBean() {
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
	 * Getter for property reasonDescription.
	 * 
	 * @return Value of property reasonDescription.
	 *  
	 */
	public String getReasonDescription() {
		return reasonDescription;
	}

	/**
	 * Setter for property reasonDescription.
	 * 
	 * @param reasonDescription
	 *            New value of property reasonDescription.
	 *  
	 */
	public void setReasonDescription(String reasonDescription) {
		this.reasonDescription = reasonDescription;
	}

}
