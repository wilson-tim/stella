/*
 * Created on 31-Mar-2005
 * Filename: CustomerBean.java
 */
package uk.co.firstchoice.viking.gui;

/**
 * @author Dwood
 * Object to represent a customer class.
 */
public class CustomerBean {

	
	private String customerCode = null;
	
	private String customerName = null;
	
	/**
	 * @return Returns the customerCode.
	 */
	public String getCustomerCode() {
		return customerCode;
	}
	/**
	 * @param customerCode The customerCode to set.
	 */
	public void setCustomerCode(final String customerCode) {
		this.customerCode = customerCode;
	}
	/**
	 * @return Returns the customerName.
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName The customerName to set.
	 */
	public void setCustomerName(final String customerName) {
		this.customerName = customerName;
	}
}

