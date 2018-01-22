/*
 * BeanCollectionBean.java
 *
 * Created on 12 March 2003, 12:18
 */

package uk.co.firstchoice.stella.frontend;

/**
 * 
 * @author Lsvensson
 */
public class BeanCollectionBean {

	private java.util.Vector beanCollection = null;

	private String error = "";

	private String indicator = ""; // an extra field that can be used for

	// whatever needed
	
	private String specialistBranch = "";

	/** Creates a new instance of MatchExceptionsCollectionBean */
	public BeanCollectionBean() {
		beanCollection = new java.util.Vector();
	}

	/**
	 * Getter for property beanCollection.
	 * 
	 * @return Value of property beanCollection.
	 *  
	 */
	public java.util.Vector getBeanCollection() {
		return beanCollection;
	}

	/**
	 * Getter for property error.
	 * 
	 * @return Value of property error.
	 *  
	 */
	public String getError() {
		return error;
	}

	/**
	 * Setter for property error.
	 * 
	 * @param error
	 *            New value of property error.
	 *  
	 */
	public void setError(String err) {
		error = err;
	}

	/**
	 * Getter for property indicator.
	 * 
	 * @return Value of property indicator.
	 *  
	 */
	public java.lang.String getIndicator() {
		return indicator;
	}

	/**
	 * Setter for property indicator.
	 * 
	 * @param indicator
	 *            New value of property indicator.
	 *  
	 */
	public void setIndicator(java.lang.String indicator) {
		this.indicator = indicator;
	}

	/**
	 * Getter for property specialistBranch.
	 * 
	 * @return Value of property specialistBranch.
	 *  
	 */
	public String getSpecialistBranch() {
		return specialistBranch;
	}

	/**
	 * Setter for property specialistBranch.
	 * 
	 * @param specialistBranch
	 *            New value of property specialistBranch.
	 *  
	 */
	public void setSpecialistBranch(String specialistBranch) {
		this.specialistBranch = specialistBranch;
	}

}