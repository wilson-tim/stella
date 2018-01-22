package uk.co.firstchoice.viking.gui;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import uk.co.firstchoice.viking.gui.util.ForwardConstants;

public final class DisplayCapacityFormBean extends ActionForm {

	/** Holder for any errors signalled by the Action class */
	private String error = ForwardConstants.EMPTY;

	/** The series that we are displaying details for */
	private String seriesNo = ForwardConstants.EMPTY;

	/**
	 * The type of details to show. Available, Purchased or a customers name
	 */
	private String type = ForwardConstants.EMPTY;

	/**
	 * capacityDetails is a "Map of Maps" with the first map having departure
	 * date as key and a map as object. This second map uses Sector ("LGW-LHR")
	 * as key and capacity (String) as object.
	 *  
	 */
	private java.util.Map capacityDetails;

	/**
	 * Reset all properties to their default values.
	 * 
	 * @param mapping
	 *            The mapping used to select this instance
	 * @param request
	 *            The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		capacityDetails = new java.util.Hashtable();
	}

	/**
	 * Getter for property capacityDetails.
	 * 
	 * @return Value of property capacityDetails.
	 *  
	 */
	public java.util.Map getCapacityDetails() {
		return capacityDetails;
	}

	/**
	 * Setter for property capacityDetails.
	 * 
	 * @param capacityDetails
	 *            New value of property capacityDetails.
	 *  
	 */
	public void setCapacityDetails(java.util.Map capacityDetails) {
		this.capacityDetails = capacityDetails;
	}

	/**
	 * Getter for property error.
	 * 
	 * @return Value of property error.
	 *  
	 */
	public java.lang.String getError() {
		return error;
	}

	/**
	 * Setter for property error.
	 * 
	 * @param error
	 *            New value of property error.
	 *  
	 */
	public void setError(java.lang.String error) {
		this.error = error;
	}

	/**
	 * Getter for property seriesNo.
	 * 
	 * @return Value of property seriesNo.
	 *  
	 */
	public java.lang.String getSeriesNo() {
		return seriesNo;
	}

	/**
	 * Setter for property seriesNo.
	 * 
	 * @param seriesNo
	 *            New value of property seriesNo.
	 *  
	 */
	public void setSeriesNo(java.lang.String seriesNo) {
		this.seriesNo = seriesNo;
	}

	/**
	 * Getter for property type.
	 * 
	 * @return Value of property type.
	 *  
	 */
	public java.lang.String getType() {
		return type;
	}

	/**
	 * Setter for property type.
	 * 
	 * @param type
	 *            New value of property type.
	 *  
	 */
	public void setType(java.lang.String type) {
		this.type = type;
	}

}