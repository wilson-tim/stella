/*
 * AllocationBean.java
 *
 * Created on 11 November 2003, 09:30
 */

package uk.co.firstchoice.viking.gui;

import uk.co.firstchoice.fcutil.FCException;
import uk.co.firstchoice.fcutil.FCUtils;
import uk.co.firstchoice.viking.gui.util.ForwardConstants;

/**
 * 
 * @author Lars Svensson
 */
public class AllocationBean {

	private String colourID;

	private String customerCode;

	private String minDepartureDate;

	private String maxDepartureDate;

	private java.util.Collection currencies;

	private java.util.Collection amounts;

	private String gatewayFrom;

	private String gatewayTo;

	private String seatClass;

	private String seatClassDescription;

	private String numSeats;

	private String contractID;

	private String geminiCode;
	
	private String mealType;
	
	private String broker;

	private String allocationStatus;

	/** Creates a new empty instance of AllocationBean */
	public AllocationBean() {
	}

	/**
	 * Creates a new empty instance of AllocationBean
	 * 
	 * @param colourID
	 * @param customerCode
	 * @param minDepartureDate
	 * @param maxDepartureDate
	 * @param currencyAmount
	 * @param gatewayFrom
	 * @param gatewayTo
	 * @param seatClass
	 * @param numSeats
	 * @param contractID
	 * @param broker
	 * @param allocationStatus
	 * @throws FCException
	 */
	public AllocationBean(String colourID, String customerCode,
			String minDepartureDate, String maxDepartureDate,
			String currencyAmount, String gatewayFrom, String gatewayTo,
			String seatClass, String seatClassDescription, String numSeats,
			String contractID, String geminiCode, String mealType,String broker,
			String allocationStatus) throws FCException {

		this.colourID = colourID;
		this.customerCode = customerCode;
		this.minDepartureDate = minDepartureDate;
		this.maxDepartureDate = maxDepartureDate;
		this.gatewayFrom = gatewayFrom;
		this.gatewayTo = gatewayTo;
		this.seatClass = seatClass;
		this.seatClassDescription = seatClassDescription;
		this.numSeats = numSeats;
		this.contractID = contractID;
		this.broker = broker;
		this.geminiCode = geminiCode;
		this.mealType = mealType;
		this.allocationStatus = allocationStatus;
		this.setCurrencyAmount(currencyAmount);
	}

	/**
	 * Getter for property allocationStatus.
	 * 
	 * @return Value of property allocationStatus.
	 *  
	 */
	public java.lang.String getAllocationStatus() {
		return allocationStatus;
	}

	/**
	 * Setter for property allocationStatus.
	 * 
	 * @param allocationStatus
	 *            New value of property allocationStatus.
	 *  
	 */
	public void setAllocationStatus(java.lang.String allocationStatus) {
		this.allocationStatus = allocationStatus;
	}

	/**
	 * Getter for property broker.
	 * 
	 * @return Value of property broker.
	 *  
	 */
	public java.lang.String getBroker() {
		return broker;
	}

	/**
	 * Setter for property broker.
	 * 
	 * @param broker
	 *            New value of property broker.
	 *  
	 */
	public void setBroker(java.lang.String broker) {
		this.broker = broker;
	}

	/**
	 * Getter for property colourID.
	 * 
	 * @return Value of property colourID.
	 *  
	 */
	public java.lang.String getColourID() {
		return colourID;
	}

	/**
	 * Setter for property colourID.
	 * 
	 * @param colourID
	 *            New value of property colourID.
	 *  
	 */
	public void setColourID(java.lang.String colourID) {
		this.colourID = colourID;
	}

	/**
	 * Getter for property contractID.
	 * 
	 * @return Value of property contractID.
	 *  
	 */
	public java.lang.String getContractID() {
		return contractID;
	}

	/**
	 * Setter for property contractID.
	 * 
	 * @param contractID
	 *            New value of property contractID.
	 *  
	 */
	public void setContractID(java.lang.String contractID) {
		this.contractID = contractID;
	}

	/**
	 * Getter for property customerCode.
	 * 
	 * @return Value of property customerCode.
	 *  
	 */
	public java.lang.String getCustomerCode() {
		return customerCode;
	}

	/**
	 * Setter for property customerCode.
	 * 
	 * @param customerCode
	 *            New value of property customerCode.
	 *  
	 */
	public void setCustomerCode(java.lang.String customerCode) {
		this.customerCode = customerCode;
	}

	/**
	 * Getter for property gatewayFrom.
	 * 
	 * @return Value of property gatewayFrom.
	 *  
	 */
	public java.lang.String getGatewayFrom() {
		return gatewayFrom;
	}

	/**
	 * Setter for property gatewayFrom.
	 * 
	 * @param gatewayFrom
	 *            New value of property gatewayFrom.
	 *  
	 */
	public void setGatewayFrom(java.lang.String gatewayFrom) {
		this.gatewayFrom = gatewayFrom;
	}

	/**
	 * Getter for property gatewayTo.
	 * 
	 * @return Value of property gatewayTo.
	 *  
	 */
	public java.lang.String getGatewayTo() {
		return gatewayTo;
	}

	/**
	 * Setter for property gatewayTo.
	 * 
	 * @param gatewayTo
	 *            New value of property gatewayTo.
	 *  
	 */
	public void setGatewayTo(java.lang.String gatewayTo) {
		this.gatewayTo = gatewayTo;
	}

	/**
	 * Getter for property maxDepartureDate.
	 * 
	 * @return Value of property maxDepartureDate.
	 *  
	 */
	public java.lang.String getMaxDepartureDate() {
		return maxDepartureDate;
	}

	/**
	 * Setter for property maxDepartureDate.
	 * 
	 * @param maxDepartureDate
	 *            New value of property maxDepartureDate.
	 *  
	 */
	public void setMaxDepartureDate(java.lang.String maxDepartureDate) {
		this.maxDepartureDate = maxDepartureDate;
	}

	/**
	 * Getter for property minDepartureDate.
	 * 
	 * @return Value of property minDepartureDate.
	 *  
	 */
	public java.lang.String getMinDepartureDate() {
		return minDepartureDate;
	}

	/**
	 * Setter for property minDepartureDate.
	 * 
	 * @param minDepartureDate
	 *            New value of property minDepartureDate.
	 *  
	 */
	public void setMinDepartureDate(java.lang.String minDepartureDate) {
		this.minDepartureDate = minDepartureDate;
	}

	/**
	 * Getter for property numSeats.
	 * 
	 * @return Value of property numSeats.
	 *  
	 */
	public java.lang.String getNumSeats() {
		return numSeats;
	}

	/**
	 * Setter for property numSeats.
	 * 
	 * @param numSeats
	 *            New value of property numSeats.
	 *  
	 */
	public void setNumSeats(java.lang.String numSeats) {
		this.numSeats = numSeats;
	}

	/**
	 * Getter for property seatClass.
	 * 
	 * @return Value of property seatClass.
	 *  
	 */
	public java.lang.String getSeatClass() {
		return seatClass;
	}

	/**
	 * Setter for property seatClass.
	 * 
	 * @param seatClass
	 *            New value of property seatClass.
	 *  
	 */
	public void setSeatClass(java.lang.String seatClass) {
		this.seatClass = seatClass;
	}

	/**
	 * Getter for property amounts.
	 * 
	 * @return Value of property amounts.
	 *  
	 */
	public java.util.Collection getAmounts() {
		return amounts;
	}

	/**
	 * Getter for property currencies.
	 * 
	 * @return Value of property currencies.
	 *  
	 */
	public java.util.Collection getCurrencies() {
		return currencies;
	}

	/**
	 * @param ca
	 * @throws FCException
	 */
	public void setCurrencyAmount(String ca) throws FCException { // GBP-15.3u000dUSD-21.5u000dGBP-15.3u000dUSD-21.5u000d
		currencies = new java.util.Vector();
		amounts = new java.util.Vector();
		try {
			if (ca != null && !ca.equals("")) {
				String[] arr = ca.split(ForwardConstants.MULTILINE_SEPERATOR);
				for (int i = 0; i < arr.length; i++) {
					String[] arr2 = arr[i].split("-");
					currencies.add(arr2[0]);
					amounts.add(FCUtils
							.floatToString(Float.parseFloat(arr2[1])));
				}
			}
		} catch (Exception e) {
			throw new FCException("Currency/amount format error (" + ca + ")",
					e);
		}
	}

	/**
	 * Getter for property seatClassDescription.
	 * 
	 * @return Value of property seatClassDescription.
	 *  
	 */
	public java.lang.String getSeatClassDescription() {
		return seatClassDescription;
	}

	/**
	 * Setter for property seatClassDescription.
	 * 
	 * @param seatClassDescription
	 *            New value of property seatClassDescription.
	 *  
	 */
	public void setSeatClassDescription(java.lang.String seatClassDescription) {
		this.seatClassDescription = seatClassDescription;
	}

	/**
	 * Getter for property geminiCode.
	 * 
	 * @return Value of property geminiCode.
	 */
	public java.lang.String getGeminiCode() {
		return geminiCode;
	}

	/**
	 * Setter for property geminiCode.
	 * 
	 * @param geminiCode
	 *            New value of property geminiCode.
	 */
	public void setGeminiCode(java.lang.String geminiCode) {
		this.geminiCode = geminiCode;
	}

	/**
	 * Getter for property mealType.
	 * 
	 * @return Value of property mealType.
	 */
	public java.lang.String getMealType() {
		return mealType;
	}

	/**
	 * Setter for property mealType.
	 * 
	 * @param geminiCode
	 *            New value of property mealType.
	 */
	public void setMealType(java.lang.String mealType) {
		this.mealType = mealType;
	}

}