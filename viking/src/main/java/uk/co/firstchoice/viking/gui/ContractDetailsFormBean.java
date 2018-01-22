package uk.co.firstchoice.viking.gui;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import uk.co.firstchoice.fcutil.FCUtils;
import uk.co.firstchoice.viking.gui.util.ForwardConstants;

public final class ContractDetailsFormBean extends ActionForm {

	private static final int SHORTTERMSLEN = 30; // the length of

	// ShortPaymentTerms

	private String error = ForwardConstants.EMPTY;

	private String code = ForwardConstants.EMPTY; // the parent account

	private String contractID = ForwardConstants.EMPTY;

	private String contractType = ForwardConstants.EMPTY;

	private String fuelFixed = ForwardConstants.EMPTY;

	private String currencyFixed = ForwardConstants.EMPTY;

	private String euroFixed = ForwardConstants.EMPTY;

	private String apdR = ForwardConstants.EMPTY;

	private String apdI = ForwardConstants.EMPTY;

	private String apdPct = ForwardConstants.EMPTY;

	private String osTaxesInR = ForwardConstants.EMPTY;

	private String osTaxesInI = ForwardConstants.EMPTY;

	private String osTaxesInPct = ForwardConstants.EMPTY;

	private String osTaxesOutR = ForwardConstants.EMPTY;

	private String osTaxesOutI = ForwardConstants.EMPTY;

	private String osTaxesOutPct = ForwardConstants.EMPTY;

	private String pilR = ForwardConstants.EMPTY;

	private String pilI = ForwardConstants.EMPTY;

	private String pilPct = ForwardConstants.EMPTY;

	private String season = ForwardConstants.EMPTY;

	private String ukTaxesInR = ForwardConstants.EMPTY;

	private String ukTaxesInI = ForwardConstants.EMPTY;

	private String ukTaxesInPct = ForwardConstants.EMPTY;

	private String ukTaxesOutR = ForwardConstants.EMPTY;

	private String ukTaxesOutI = ForwardConstants.EMPTY;

	private String ukTaxesOutPct = ForwardConstants.EMPTY;

	private String paymentTerms = ForwardConstants.EMPTY;

	private String action = ForwardConstants.EMPTY;

	private String fromAction = ForwardConstants.EMPTY;

	/**
	 * Reset all properties to their default values.
	 * 
	 * @param mapping
	 *            The mapping used to select this instance
	 * @param request
	 *            The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		error = ForwardConstants.EMPTY;
		code = ForwardConstants.EMPTY;
		contractID = ForwardConstants.EMPTY;
		contractType = ForwardConstants.EMPTY;
		fuelFixed = ForwardConstants.EMPTY;
		currencyFixed = ForwardConstants.EMPTY;
		euroFixed = ForwardConstants.EMPTY;
		apdR = ForwardConstants.EMPTY;
		apdI = ForwardConstants.EMPTY;
		apdPct = ForwardConstants.EMPTY;
		osTaxesInR = ForwardConstants.EMPTY;
		osTaxesInI = ForwardConstants.EMPTY;
		osTaxesInPct = ForwardConstants.EMPTY;
		osTaxesOutR = ForwardConstants.EMPTY;
		osTaxesOutI = ForwardConstants.EMPTY;
		osTaxesOutPct = ForwardConstants.EMPTY;
		pilR = ForwardConstants.EMPTY;
		pilI = ForwardConstants.EMPTY;
		pilPct = ForwardConstants.EMPTY;
		ukTaxesInR = ForwardConstants.EMPTY;
		ukTaxesInI = ForwardConstants.EMPTY;
		ukTaxesInPct = ForwardConstants.EMPTY;
		ukTaxesOutR = ForwardConstants.EMPTY;
		ukTaxesOutI = ForwardConstants.EMPTY;
		ukTaxesOutPct = ForwardConstants.EMPTY;
		paymentTerms = ForwardConstants.EMPTY;
	}

	/**
	 * Getter for property error.
	 * 
	 * @return Value of property error.
	 *  
	 */
	public java.lang.String getError() {
		return error == null ? ForwardConstants.EMPTY : error;
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
	 * Getter for property ukTaxesOutR.
	 * 
	 * @return Value of property ukTaxesOutR.
	 *  
	 */
	public java.lang.String getUkTaxesOutR() {
		return ukTaxesOutR;
	}

	/**
	 * Setter for property ukTaxesOutR.
	 * 
	 * @param ukTaxesOutR
	 *            New value of property ukTaxesOutR.
	 *  
	 */
	public void setUkTaxesOutR(java.lang.String ukTaxesOutR) {
		this.ukTaxesOutR = ukTaxesOutR;
	}

	/**
	 * Getter for property apdI.
	 * 
	 * @return Value of property apdI.
	 *  
	 */
	public java.lang.String getApdI() {
		return apdI;
	}

	/**
	 * Setter for property apdI.
	 * 
	 * @param apdI
	 *            New value of property apdI.
	 *  
	 */
	public void setApdI(java.lang.String apdI) {
		this.apdI = apdI;
	}

	/**
	 * Getter for property pilI.
	 * 
	 * @return Value of property pilI.
	 *  
	 */
	public java.lang.String getPilI() {
		return pilI;
	}

	/**
	 * Setter for property pilI.
	 * 
	 * @param pilI
	 *            New value of property pilI.
	 *  
	 */
	public void setPilI(java.lang.String pilI) {
		this.pilI = pilI;
	}

	/**
	 * Getter for property ukTaxesInI.
	 * 
	 * @return Value of property ukTaxesInI.
	 *  
	 */
	public java.lang.String getUkTaxesInI() {
		return ukTaxesInI;
	}

	/**
	 * Setter for property ukTaxesInI.
	 * 
	 * @param ukTaxesInI
	 *            New value of property ukTaxesInI.
	 *  
	 */
	public void setUkTaxesInI(java.lang.String ukTaxesInI) {
		this.ukTaxesInI = ukTaxesInI;
	}

	/**
	 * Getter for property osTaxesOutR.
	 * 
	 * @return Value of property osTaxesOutR.
	 *  
	 */
	public java.lang.String getOsTaxesOutR() {
		return osTaxesOutR;
	}

	/**
	 * Setter for property osTaxesOutR.
	 * 
	 * @param osTaxesOutR
	 *            New value of property osTaxesOutR.
	 *  
	 */
	public void setOsTaxesOutR(java.lang.String osTaxesOutR) {
		this.osTaxesOutR = osTaxesOutR;
	}

	/**
	 * Getter for property currencyFixed.
	 * 
	 * @return Value of property currencyFixed.
	 *  
	 */
	public java.lang.String getCurrencyFixed() {
		return currencyFixed;
	}

	/**
	 * Setter for property currencyFixed.
	 * 
	 * @param currencyFixed
	 *            New value of property currencyFixed.
	 *  
	 */
	public void setCurrencyFixed(java.lang.String currencyFixed) {
		this.currencyFixed = currencyFixed;
	}

	/**
	 * Getter for property euroFixed.
	 * 
	 * @return Value of property euroFixed.
	 *  
	 */
	public java.lang.String getEuroFixed() {
		return euroFixed;
	}

	/**
	 * Setter for property euroFixed.
	 * 
	 * @param euroFixed
	 *            New value of property euroFixed.
	 *  
	 */
	public void setEuroFixed(java.lang.String euroFixed) {
		this.euroFixed = euroFixed;
	}

	/**
	 * Getter for property fuelFixed.
	 * 
	 * @return Value of property fuelFixed.
	 *  
	 */
	public java.lang.String getFuelFixed() {
		return fuelFixed;
	}

	/**
	 * Setter for property fuelFixed.
	 * 
	 * @param fuelFixed
	 *            New value of property fuelFixed.
	 *  
	 */
	public void setFuelFixed(java.lang.String fuelFixed) {
		this.fuelFixed = fuelFixed;
	}

	/**
	 * Getter for property ukTaxesOutI.
	 * 
	 * @return Value of property ukTaxesOutI.
	 *  
	 */
	public java.lang.String getUkTaxesOutI() {
		return ukTaxesOutI;
	}

	/**
	 * Setter for property ukTaxesOutI.
	 * 
	 * @param ukTaxesOutI
	 *            New value of property ukTaxesOutI.
	 *  
	 */
	public void setUkTaxesOutI(java.lang.String ukTaxesOutI) {
		this.ukTaxesOutI = ukTaxesOutI;
	}

	/**
	 * Getter for property osTaxesOutI.
	 * 
	 * @return Value of property osTaxesOutI.
	 *  
	 */
	public java.lang.String getOsTaxesOutI() {
		return osTaxesOutI;
	}

	/**
	 * Setter for property osTaxesOutI.
	 * 
	 * @param osTaxesOutI
	 *            New value of property osTaxesOutI.
	 *  
	 */
	public void setOsTaxesOutI(java.lang.String osTaxesOutI) {
		this.osTaxesOutI = osTaxesOutI;
	}

	/**
	 * Getter for property ukTaxesInPct.
	 * 
	 * @return Value of property ukTaxesInPct.
	 *  
	 */
	public java.lang.String getUkTaxesInPct() {
		return ukTaxesInPct;
	}

	/**
	 * Setter for property ukTaxesInPct.
	 * 
	 * @param ukTaxesInPct
	 *            New value of property ukTaxesInPct.
	 *  
	 */
	public void setUkTaxesInPct(java.lang.String ukTaxesInPct) {
		this.ukTaxesInPct = ukTaxesInPct;
	}

	/**
	 * Getter for property osTaxesInPct.
	 * 
	 * @return Value of property osTaxesInPct.
	 *  
	 */
	public java.lang.String getOsTaxesInPct() {
		return osTaxesInPct;
	}

	/**
	 * Setter for property osTaxesInPct.
	 * 
	 * @param osTaxesInPct
	 *            New value of property osTaxesInPct.
	 *  
	 */
	public void setOsTaxesInPct(java.lang.String osTaxesInPct) {
		this.osTaxesInPct = osTaxesInPct;
	}

	/**
	 * Getter for property ukTaxesInR.
	 * 
	 * @return Value of property ukTaxesInR.
	 *  
	 */
	public java.lang.String getUkTaxesInR() {
		return ukTaxesInR;
	}

	/**
	 * Setter for property ukTaxesInR.
	 * 
	 * @param ukTaxesInR
	 *            New value of property ukTaxesInR.
	 *  
	 */
	public void setUkTaxesInR(java.lang.String ukTaxesInR) {
		this.ukTaxesInR = ukTaxesInR;
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
	 * Getter for property ukTaxesOutPct.
	 * 
	 * @return Value of property ukTaxesOutPct.
	 *  
	 */
	public java.lang.String getUkTaxesOutPct() {
		return ukTaxesOutPct;
	}

	/**
	 * Setter for property ukTaxesOutPct.
	 * 
	 * @param ukTaxesOutPct
	 *            New value of property ukTaxesOutPct.
	 *  
	 */
	public void setUkTaxesOutPct(java.lang.String ukTaxesOutPct) {
		this.ukTaxesOutPct = ukTaxesOutPct;
	}

	/**
	 * Getter for property osTaxesInR.
	 * 
	 * @return Value of property osTaxesInR.
	 *  
	 */
	public java.lang.String getOsTaxesInR() {
		return osTaxesInR;
	}

	/**
	 * Setter for property osTaxesInR.
	 * 
	 * @param osTaxesInR
	 *            New value of property osTaxesInR.
	 *  
	 */
	public void setOsTaxesInR(java.lang.String osTaxesInR) {
		this.osTaxesInR = osTaxesInR;
	}

	/**
	 * Getter for property paymentTerms.
	 * 
	 * @return Value of property paymentTerms.
	 *  
	 */
	public java.lang.String getPaymentTerms() {
		return paymentTerms;
	}

	/**
	 * Getter for property shortPaymentTerms.
	 * 
	 * @return First shortTermsLen characters of value of property paymentTerms.
	 *  
	 */
	public java.lang.String getShortPaymentTerms() {
		if (paymentTerms.length() > SHORTTERMSLEN) {
			return paymentTerms.substring(0, 27) + "...";
		} else {
			return paymentTerms;
		}
	}

	/**
	 * Setter for property paymentTerms.
	 * 
	 * @param paymentTerms
	 *            New value of property paymentTerms.
	 *  
	 */
	public void setPaymentTerms(java.lang.String paymentTerms) {
		this.paymentTerms = FCUtils.notNull(paymentTerms).replace('"', '\''); // "
		// will
		// break
		// the
		// JavaScript
	}

	/**
	 * Getter for property pilR.
	 * 
	 * @return Value of property pilR.
	 *  
	 */
	public java.lang.String getPilR() {
		return pilR;
	}

	/**
	 * Setter for property pilR.
	 * 
	 * @param pilR
	 *            New value of property pilR.
	 *  
	 */
	public void setPilR(java.lang.String pilR) {
		this.pilR = pilR;
	}

	/**
	 * Getter for property osTaxesInI.
	 * 
	 * @return Value of property osTaxesInI.
	 *  
	 */
	public java.lang.String getOsTaxesInI() {
		return osTaxesInI;
	}

	/**
	 * Setter for property osTaxesInI.
	 * 
	 * @param osTaxesInI
	 *            New value of property osTaxesInI.
	 *  
	 */
	public void setOsTaxesInI(java.lang.String osTaxesInI) {
		this.osTaxesInI = osTaxesInI;
	}

	/**
	 * Getter for property osTaxesOutPct.
	 * 
	 * @return Value of property osTaxesOutPct.
	 *  
	 */
	public java.lang.String getOsTaxesOutPct() {
		return osTaxesOutPct;
	}

	/**
	 * Setter for property osTaxesOutPct.
	 * 
	 * @param osTaxesOutPct
	 *            New value of property osTaxesOutPct.
	 *  
	 */
	public void setOsTaxesOutPct(java.lang.String osTaxesOutPct) {
		this.osTaxesOutPct = osTaxesOutPct;
	}

	/**
	 * Getter for property pilPct.
	 * 
	 * @return Value of property pilPct.
	 *  
	 */
	public java.lang.String getPilPct() {
		return pilPct;
	}

	/**
	 * Setter for property pilPct.
	 * 
	 * @param pilPct
	 *            New value of property pilPct.
	 *  
	 */
	public void setPilPct(java.lang.String pilPct) {
		this.pilPct = pilPct;
	}

	/**
	 * Getter for property apdPct.
	 * 
	 * @return Value of property apdPct.
	 *  
	 */
	public java.lang.String getApdPct() {
		return apdPct;
	}

	/**
	 * Setter for property apdPct.
	 * 
	 * @param apdPct
	 *            New value of property apdPct.
	 *  
	 */
	public void setApdPct(java.lang.String apdPct) {
		this.apdPct = apdPct;
	}

	/**
	 * Getter for property apdR.
	 * 
	 * @return Value of property apdR.
	 *  
	 */
	public java.lang.String getApdR() {
		return apdR;
	}

	/**
	 * Setter for property apdR.
	 * 
	 * @param apdR
	 *            New value of property apdR.
	 *  
	 */
	public void setApdR(java.lang.String apdR) {
		this.apdR = apdR;
	}

	/**
	 * Getter for property code.
	 * 
	 * @return Value of property code.
	 *  
	 */
	public java.lang.String getCode() {
		return code;
	}

	/**
	 * Setter for property code.
	 * 
	 * @param code
	 *            New value of property code.
	 *  
	 */
	public void setCode(java.lang.String code) {
		this.code = code;
	}

	/**
	 * Getter for property action.
	 * 
	 * @return Value of property action.
	 *  
	 */
	public java.lang.String getAction() {
		return action;
	}

	/**
	 * Setter for property action.
	 * 
	 * @param action
	 *            New value of property action.
	 *  
	 */
	public void setAction(java.lang.String action) {
		this.action = action;
	}

	/**
	 * Getter for property fromAction.
	 * 
	 * @return Value of property fromAction.
	 *  
	 */
	public java.lang.String getFromAction() {
		return fromAction;
	}

	/**
	 * Setter for property fromAction.
	 * 
	 * @param fromAction
	 *            New value of property fromAction.
	 *  
	 */
	public void setFromAction(java.lang.String fromAction) {
		this.fromAction = fromAction;
	}

	/**
	 * Getter for property contractType.
	 * 
	 * @return Value of property contractType.
	 *  
	 */
	public java.lang.String getContractType() {
		return contractType;
	}

	/**
	 * Setter for property contractType.
	 * 
	 * @param contractType
	 *            New value of property contractType.
	 *  
	 */
	public void setContractType(java.lang.String contractType) {
		this.contractType = contractType;
	}

	/**
	 * Getter for property season.
	 * 
	 * @return Value of property season.
	 *  
	 */
	public java.lang.String getSeason() {
		return season;
	}

	/**
	 * Setter for property season.
	 * 
	 * @param season
	 *            New value of property season.
	 *  
	 */
	public void setSeason(java.lang.String season) {
		this.season = season;
	}

}