/*
 * AirlineBean.java
 *
 * Created on 06 May 2003, 16:35
 */

package uk.co.firstchoice.stella.frontend;

/**
 * 
 * @author Lsvensson
 */
public class AirlineBean {

	private String airlineNumber = "";

	private String airlineCode = "";

	private String airlineName = "";

	private String airlineContact = "";

	private String airlineAddress1 = "";

	private String airlineAddress2 = "";

	private String airlineCity = "";

	private String airlineCounty = "";

	private String airlineCountry = "";

	private String airlinePostcode = "";

	private String airlinePhone = "";

	private String airlineAddressee = "";

	private float airlineTaxCostTL = 2.5f;

	private float airlineSeatCostTL = 0;

	private String airlineSectorPayment = "";

	private float airlineBookingPayment = 0;

	private String doDelete = ""; //Will be set to YES if airline is to be

	// deleted

	private long oracleUniqueKey = 0;

	private String saveError = "";

	/** Creates a new instance of AirlineBean */
	public AirlineBean() {
	}

	/**
	 * Getter for property airlineAddress1.
	 * 
	 * @return Value of property airlineAddress1.
	 *  
	 */
	public String getAirlineAddress1() {
		return airlineAddress1;
	}

	/**
	 * Setter for property airlineAddress1.
	 * 
	 * @param airlineAddress1
	 *            New value of property airlineAddress1.
	 *  
	 */
	public void setAirlineAddress1(String airlineAddress1) {
		this.airlineAddress1 = airlineAddress1;
	}

	/**
	 * Getter for property airlineAddress2.
	 * 
	 * @return Value of property airlineAddress2.
	 *  
	 */
	public String getAirlineAddress2() {
		return airlineAddress2;
	}

	/**
	 * Setter for property airlineAddress2.
	 * 
	 * @param airlineAddress2
	 *            New value of property airlineAddress2.
	 *  
	 */
	public void setAirlineAddress2(String airlineAddress2) {
		this.airlineAddress2 = airlineAddress2;
	}

	/**
	 * Getter for property airlineAddressee.
	 * 
	 * @return Value of property airlineAddressee.
	 *  
	 */
	public String getAirlineAddressee() {
		return airlineAddressee;
	}

	/**
	 * Setter for property airlineAddressee.
	 * 
	 * @param airlineAddressee
	 *            New value of property airlineAddressee.
	 *  
	 */
	public void setAirlineAddressee(String airlineAddressee) {
		this.airlineAddressee = airlineAddressee;
	}

	/**
	 * Getter for property airlineBookingPayment.
	 * 
	 * @return Value of property airlineBookingPayment.
	 *  
	 */
	public String getAirlineBookingPayment() {
		return StellaUtils.floatToString(airlineBookingPayment);
	}

	public float getAirlineBookingPaymentFloat() {
		return airlineBookingPayment;
	}

	/**
	 * Setter for property airlineBookingPayment.
	 * 
	 * @param airlineBookingPayment
	 *            New value of property airlineBookingPayment.
	 *  
	 */
	public void setAirlineBookingPayment(String value) throws StellaException {
		airlineBookingPayment = StellaUtils.stringToFloat(value);
	}

	public void setAirlineBookingPayment(float airlineBookingPayment) {
		this.airlineBookingPayment = airlineBookingPayment;
	}

	/**
	 * Getter for property airlineCity.
	 * 
	 * @return Value of property airlineCity.
	 *  
	 */
	public String getAirlineCity() {
		return airlineCity;
	}

	/**
	 * Setter for property airlineCity.
	 * 
	 * @param airlineCity
	 *            New value of property airlineCity.
	 *  
	 */
	public void setAirlineCity(String airlineCity) {
		this.airlineCity = airlineCity;
	}

	/**
	 * Getter for property airlineCode.
	 * 
	 * @return Value of property airlineCode.
	 *  
	 */
	public String getAirlineCode() {
		return airlineCode;
	}

	/**
	 * Setter for property airlineCode.
	 * 
	 * @param airlineCode
	 *            New value of property airlineCode.
	 *  
	 */
	public void setAirlineCode(String airlineCode) {
		this.airlineCode = airlineCode;
	}

	/**
	 * Getter for property airlineContact.
	 * 
	 * @return Value of property airlineContact.
	 *  
	 */
	public String getAirlineContact() {
		return airlineContact;
	}

	/**
	 * Setter for property airlineContact.
	 * 
	 * @param airlineContact
	 *            New value of property airlineContact.
	 *  
	 */
	public void setAirlineContact(String airlineContact) {
		this.airlineContact = airlineContact;
	}

	/**
	 * Getter for property airlineCountry.
	 * 
	 * @return Value of property airlineCountry.
	 *  
	 */
	public String getAirlineCountry() {
		return airlineCountry;
	}

	/**
	 * Setter for property airlineCountry.
	 * 
	 * @param airlineCountry
	 *            New value of property airlineCountry.
	 *  
	 */
	public void setAirlineCountry(String airlineCountry) {
		this.airlineCountry = airlineCountry;
	}

	/**
	 * Getter for property airlineCounty.
	 * 
	 * @return Value of property airlineCounty.
	 *  
	 */
	public String getAirlineCounty() {
		return airlineCounty;
	}

	/**
	 * Setter for property airlineCounty.
	 * 
	 * @param airlineCounty
	 *            New value of property airlineCounty.
	 *  
	 */
	public void setAirlineCounty(String airlineCounty) {
		this.airlineCounty = airlineCounty;
	}

	/**
	 * Getter for property airlineName.
	 * 
	 * @return Value of property airlineName.
	 *  
	 */
	public String getAirlineName() {
		return airlineName;
	}

	/**
	 * Setter for property airlineName.
	 * 
	 * @param airlineName
	 *            New value of property airlineName.
	 *  
	 */
	public void setAirlineName(String airlineName) {
		this.airlineName = airlineName;
	}

	/**
	 * Getter for property airlineNumber.
	 * 
	 * @return Value of property airlineNumber.
	 *  
	 */
	public String getAirlineNumber() {
		return airlineNumber;
	}

	/**
	 * Setter for property airlineNumber.
	 * 
	 * @param airlineNumber
	 *            New value of property airlineNumber.
	 *  
	 */
	public void setAirlineNumber(String airlineNumber) {
		this.airlineNumber = airlineNumber;
	}

	/**
	 * Getter for property airlinePhone.
	 * 
	 * @return Value of property airlinePhone.
	 *  
	 */
	public String getAirlinePhone() {
		return airlinePhone;
	}

	/**
	 * Setter for property airlinePhone.
	 * 
	 * @param airlinePhone
	 *            New value of property airlinePhone.
	 *  
	 */
	public void setAirlinePhone(String airlinePhone) {
		this.airlinePhone = airlinePhone;
	}

	/**
	 * Getter for property airlinePostcode.
	 * 
	 * @return Value of property airlinePostcode.
	 *  
	 */
	public String getAirlinePostcode() {
		return airlinePostcode;
	}

	/**
	 * Setter for property airlinePostcode.
	 * 
	 * @param airlinePostcode
	 *            New value of property airlinePostcode.
	 *  
	 */
	public void setAirlinePostcode(String airlinePostcode) {
		this.airlinePostcode = airlinePostcode;
	}

	/**
	 * Getter for property airlineSeatCostTL.
	 * 
	 * @return Value of property airlineSeatCostTL.
	 *  
	 */
	public String getAirlineSeatCostTL() {
		return StellaUtils.floatToString(airlineSeatCostTL);
	}

	public float getAirlineSeatCostTLFloat() {
		return airlineSeatCostTL;
	}

	/**
	 * Setter for property airlineSeatCostTL.
	 * 
	 * @param airlineSeatCostTL
	 *            New value of property airlineSeatCostTL.
	 *  
	 */
	public void setAirlineSeatCostTL(String value) throws StellaException {
		airlineSeatCostTL = StellaUtils.stringToFloat(value);
	}

	public void setAirlineSeatCostTL(float airlineSeatCostTL) {
		this.airlineSeatCostTL = airlineSeatCostTL;
	}

	/**
	 * Getter for property airlineSectorPayment.
	 * 
	 * @return Value of property airlineSectorPayment.
	 *  
	 */
	public String getAirlineSectorPayment() {
		return airlineSectorPayment;
	}

	/**
	 * Setter for property airlineSectorPayment.
	 * 
	 * @param airlineSectorPayment
	 *            New value of property airlineSectorPayment.
	 *  
	 */
	public void setAirlineSectorPayment(String airlineSectorPayment) {
		this.airlineSectorPayment = airlineSectorPayment;
	}

	/**
	 * Getter for property airlineTaxCostTL.
	 * 
	 * @return Value of property airlineTaxCostTL.
	 *  
	 */
	public float getAirlineTaxCostTLFloat() {
		return airlineTaxCostTL;
	}

	public String getAirlineTaxCostTL() {
		return StellaUtils.floatToString(airlineTaxCostTL);
	}

	/**
	 * Setter for property airlineTaxCostTL.
	 * 
	 * @param airlineTaxCostTL
	 *            New value of property airlineTaxCostTL.
	 *  
	 */
	public void setAirlineTaxCostTL(float airlineTaxCostTL) {
		this.airlineTaxCostTL = airlineTaxCostTL;
	}

	public void setAirlineTaxCostTL(String value) throws StellaException {
		airlineTaxCostTL = StellaUtils.stringToFloat(value);
	}

	/**
	 * Getter for property oracleUniqueKey.
	 * 
	 * @return Value of property oracleUniqueKey.
	 *  
	 */
	public long getOracleUniqueKey() {
		return oracleUniqueKey;
	}

	/**
	 * Setter for property oracleUniqueKey.
	 * 
	 * @param oracleUniqueKey
	 *            New value of property oracleUniqueKey.
	 *  
	 */
	public void setOracleUniqueKey(long oracleUniqueKey) {
		this.oracleUniqueKey = oracleUniqueKey;
	}

	/**
	 * Getter for property saveError.
	 * 
	 * @return Value of property saveError.
	 *  
	 */
	public String getSaveError() {
		return saveError;
	}

	/**
	 * Setter for property saveError.
	 * 
	 * @param saveError
	 *            New value of property saveError.
	 *  
	 */
	public void setSaveError(String saveError) {
		this.saveError = saveError;
	}

	/**
	 * Getter for property doDelete.
	 * 
	 * @return Value of property doDelete.
	 *  
	 */
	public java.lang.String getDoDelete() {
		return doDelete;
	}

	/**
	 * Setter for property doDelete.
	 * 
	 * @param doDelete
	 *            New value of property doDelete.
	 *  
	 */
	public void setDoDelete(java.lang.String doDelete) {
		this.doDelete = doDelete;
	}

}