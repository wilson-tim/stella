package uk.co.firstchoice.viking.gui;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import uk.co.firstchoice.viking.gui.util.ForwardConstants;

public final class RevenueAccountFormBean extends ActionForm {

	private String error = ForwardConstants.EMPTY;

	private String saveType = ForwardConstants.EMPTY;

	private String action = ForwardConstants.EMPTY;

	private String code = ForwardConstants.EMPTY;

	private String contactName = ForwardConstants.EMPTY;

	private String address1 = ForwardConstants.EMPTY;

	private String address2 = ForwardConstants.EMPTY;

	private String address3 = ForwardConstants.EMPTY;

	private String address4 = ForwardConstants.EMPTY;

	private String address5 = ForwardConstants.EMPTY;

	private String postCode = ForwardConstants.EMPTY;

	private String countryCode = ForwardConstants.EMPTY;

	private String name = ForwardConstants.EMPTY;

	private String officePhone = ForwardConstants.EMPTY;

	private String oooContact1 = ForwardConstants.EMPTY;

	private String oooContact2 = ForwardConstants.EMPTY;

	private String opsEmail = ForwardConstants.EMPTY;

	private String generalEmail = ForwardConstants.EMPTY;

	private String atol = ForwardConstants.EMPTY;

	private String officeFax = ForwardConstants.EMPTY;

	private String oooPhone1 = ForwardConstants.EMPTY;

	private String oooPhone2 = ForwardConstants.EMPTY;

	private String informDelay = ForwardConstants.EMPTY;

	private String informLate = ForwardConstants.EMPTY;

	private String flightCode1 = ForwardConstants.EMPTY;

	private String flightCode2 = ForwardConstants.EMPTY;

	private String flightCode3 = ForwardConstants.EMPTY;

	private String flightCode4 = ForwardConstants.EMPTY;

	private String accountHolder = ForwardConstants.EMPTY;

	private String accountDeputy = ForwardConstants.EMPTY;

	private boolean fcGroup = false;

	private String comments = ForwardConstants.EMPTY;

	private java.util.List allContracts = new java.util.Vector();

	private String contractSelector = ForwardConstants.EMPTY;

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
		saveType = ForwardConstants.EMPTY;
		code = ForwardConstants.EMPTY;
		contactName = ForwardConstants.EMPTY;
		address1 = ForwardConstants.EMPTY;
		address2 = ForwardConstants.EMPTY;
		address3 = ForwardConstants.EMPTY;
		address4 = ForwardConstants.EMPTY;
		address5 = ForwardConstants.EMPTY;
		postCode = ForwardConstants.EMPTY;
		countryCode = ForwardConstants.EMPTY;
		name = ForwardConstants.EMPTY;
		officePhone = ForwardConstants.EMPTY;
		oooContact1 = ForwardConstants.EMPTY;
		oooContact2 = ForwardConstants.EMPTY;
		opsEmail = ForwardConstants.EMPTY;
		generalEmail = ForwardConstants.EMPTY;
		atol = ForwardConstants.EMPTY;
		officeFax = ForwardConstants.EMPTY;
		oooPhone1 = ForwardConstants.EMPTY;
		oooPhone2 = ForwardConstants.EMPTY;
		informDelay = ForwardConstants.EMPTY;
		informLate = ForwardConstants.EMPTY;
		flightCode1 = ForwardConstants.EMPTY;
		flightCode2 = ForwardConstants.EMPTY;
		flightCode3 = ForwardConstants.EMPTY;
		flightCode4 = ForwardConstants.EMPTY;
		accountHolder = ForwardConstants.EMPTY;
		accountDeputy = ForwardConstants.EMPTY;
		fcGroup = false;
		comments = ForwardConstants.EMPTY;
		allContracts = new java.util.Vector();
		contractSelector = ForwardConstants.EMPTY;
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
	 * Getter for property informLate.
	 * 
	 * @return Value of property informLate.
	 *  
	 */
	public java.lang.String getInformLate() {
		return informLate == null ? ForwardConstants.EMPTY : informLate;
	}

	/**
	 * Setter for property informLate.
	 * 
	 * @param informLate
	 *            New value of property informLate.
	 *  
	 */
	public void setInformLate(java.lang.String informLate) {
		this.informLate = informLate;
	}

	/**
	 * Getter for property oooPhone2.
	 * 
	 * @return Value of property oooPhone2.
	 *  
	 */
	public java.lang.String getOooPhone2() {
		return oooPhone2 == null ? ForwardConstants.EMPTY : oooPhone2;
	}

	/**
	 * Setter for property oooPhone2.
	 * 
	 * @param oooPhone2
	 *            New value of property oooPhone2.
	 *  
	 */
	public void setOooPhone2(java.lang.String oooPhone2) {
		this.oooPhone2 = oooPhone2;
	}

	/**
	 * Getter for property address2.
	 * 
	 * @return Value of property address2.
	 *  
	 */
	public java.lang.String getAddress2() {
		return address2 == null ? ForwardConstants.EMPTY : address2;
	}

	/**
	 * Setter for property address2.
	 * 
	 * @param address2
	 *            New value of property address2.
	 *  
	 */
	public void setAddress2(java.lang.String address2) {
		this.address2 = address2;
	}

	/**
	 * Getter for property generalEmail.
	 * 
	 * @return Value of property generalEmail.
	 *  
	 */
	public java.lang.String getGeneralEmail() {
		return generalEmail == null ? ForwardConstants.EMPTY : generalEmail;
	}

	/**
	 * Setter for property generalEmail.
	 * 
	 * @param generalEmail
	 *            New value of property generalEmail.
	 *  
	 */
	public void setGeneralEmail(java.lang.String generalEmail) {
		this.generalEmail = generalEmail;
	}

	/**
	 * Getter for property oooPhone1.
	 * 
	 * @return Value of property oooPhone1.
	 *  
	 */
	public java.lang.String getOooPhone1() {
		return oooPhone1 == null ? ForwardConstants.EMPTY : oooPhone1;
	}

	/**
	 * Setter for property oooPhone1.
	 * 
	 * @param oooPhone1
	 *            New value of property oooPhone1.
	 *  
	 */
	public void setOooPhone1(java.lang.String oooPhone1) {
		this.oooPhone1 = oooPhone1;
	}

	/**
	 * Getter for property atol.
	 * 
	 * @return Value of property atol.
	 *  
	 */
	public java.lang.String getAtol() {
		return atol == null ? ForwardConstants.EMPTY : atol;
	}

	/**
	 * Setter for property atol.
	 * 
	 * @param atol
	 *            New value of property atol.
	 *  
	 */
	public void setAtol(java.lang.String atol) {
		this.atol = atol;
	}

	/**
	 * Getter for property code.
	 * 
	 * @return Value of property code.
	 *  
	 */
	public java.lang.String getCode() {
		return code == null ? ForwardConstants.EMPTY : code;
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
	 * Getter for property flightCode2.
	 * 
	 * @return Value of property flightCode2.
	 *  
	 */
	public java.lang.String getFlightCode2() {
		return flightCode2 == null ? ForwardConstants.EMPTY : flightCode2;
	}

	/**
	 * Setter for property flightCode2.
	 * 
	 * @param flightCode2
	 *            New value of property flightCode2.
	 *  
	 */
	public void setFlightCode2(java.lang.String flightCode2) {
		this.flightCode2 = flightCode2;
	}

	/**
	 * Getter for property comments.
	 * 
	 * @return Value of property comments.
	 *  
	 */
	public java.lang.String getComments() {
		return comments == null ? ForwardConstants.EMPTY : comments;
	}

	/**
	 * Setter for property comments.
	 * 
	 * @param comments
	 *            New value of property comments.
	 *  
	 */
	public void setComments(java.lang.String comments) {
		this.comments = comments;
	}

	/**
	 * Getter for property oooContact1.
	 * 
	 * @return Value of property oooContact1.
	 *  
	 */
	public java.lang.String getOooContact1() {
		return oooContact1 == null ? ForwardConstants.EMPTY : oooContact1;
	}

	/**
	 * Setter for property oooContact1.
	 * 
	 * @param oooContact1
	 *            New value of property oooContact1.
	 *  
	 */
	public void setOooContact1(java.lang.String oooContact1) {
		this.oooContact1 = oooContact1;
	}

	/**
	 * Getter for property contactName.
	 * 
	 * @return Value of property contactName.
	 *  
	 */
	public java.lang.String getContactName() {
		return contactName == null ? ForwardConstants.EMPTY : contactName;
	}

	/**
	 * Setter for property contactName.
	 * 
	 * @param contactName
	 *            New value of property contactName.
	 *  
	 */
	public void setContactName(java.lang.String contactName) {
		this.contactName = contactName;
	}

	/**
	 * Getter for property name.
	 * 
	 * @return Value of property name.
	 *  
	 */
	public java.lang.String getName() {
		return name == null ? ForwardConstants.EMPTY : name;
	}

	/**
	 * Setter for property name.
	 * 
	 * @param name
	 *            New value of property name.
	 *  
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Getter for property address1.
	 * 
	 * @return Value of property address1.
	 *  
	 */
	public java.lang.String getAddress1() {
		return address1 == null ? ForwardConstants.EMPTY : address1;
	}

	/**
	 * Setter for property address1.
	 * 
	 * @param address1
	 *            New value of property address1.
	 *  
	 */
	public void setAddress1(java.lang.String address1) {
		this.address1 = address1;
	}

	/**
	 * Getter for property officeFax.
	 * 
	 * @return Value of property officeFax.
	 *  
	 */
	public java.lang.String getOfficeFax() {
		return officeFax == null ? ForwardConstants.EMPTY : officeFax;
	}

	/**
	 * Setter for property officeFax.
	 * 
	 * @param officeFax
	 *            New value of property officeFax.
	 *  
	 */
	public void setOfficeFax(java.lang.String officeFax) {
		this.officeFax = officeFax;
	}

	/**
	 * Getter for property opsEmail.
	 * 
	 * @return Value of property opsEmail.
	 *  
	 */
	public java.lang.String getOpsEmail() {
		return opsEmail == null ? ForwardConstants.EMPTY : opsEmail;
	}

	/**
	 * Setter for property opsEmail.
	 * 
	 * @param opsEmail
	 *            New value of property opsEmail.
	 *  
	 */
	public void setOpsEmail(java.lang.String opsEmail) {
		this.opsEmail = opsEmail;
	}

	/**
	 * Getter for property address4.
	 * 
	 * @return Value of property address4.
	 *  
	 */
	public java.lang.String getAddress4() {
		return address4 == null ? ForwardConstants.EMPTY : address4;
	}

	/**
	 * Setter for property address4.
	 * 
	 * @param address4
	 *            New value of property address4.
	 *  
	 */
	public void setAddress4(java.lang.String address4) {
		this.address4 = address4;
	}

	/**
	 * Getter for property countryCode.
	 * 
	 * @return Value of property countryCode.
	 *  
	 */
	public java.lang.String getCountryCode() {
		return countryCode == null ? ForwardConstants.EMPTY : countryCode;
	}

	/**
	 * Setter for property countryCode.
	 * 
	 * @param countryCode
	 *            New value of property countryCode.
	 *  
	 */
	public void setCountryCode(java.lang.String countryCode) {
		this.countryCode = countryCode;
	}

	/**
	 * Getter for property flightCode3.
	 * 
	 * @return Value of property flightCode3.
	 *  
	 */
	public java.lang.String getFlightCode3() {
		return flightCode3 == null ? ForwardConstants.EMPTY : flightCode3;
	}

	/**
	 * Setter for property flightCode3.
	 * 
	 * @param flightCode3
	 *            New value of property flightCode3.
	 *  
	 */
	public void setFlightCode3(java.lang.String flightCode3) {
		this.flightCode3 = flightCode3;
	}

	/**
	 * Getter for property flightCode4.
	 * 
	 * @return Value of property flightCode4.
	 *  
	 */
	public java.lang.String getFlightCode4() {
		return flightCode4 == null ? ForwardConstants.EMPTY : flightCode4;
	}

	/**
	 * Setter for property flightCode4.
	 * 
	 * @param flightCode4
	 *            New value of property flightCode4.
	 *  
	 */
	public void setFlightCode4(java.lang.String flightCode4) {
		this.flightCode4 = flightCode4;
	}

	/**
	 * Getter for property postCode.
	 * 
	 * @return Value of property postCode.
	 *  
	 */
	public java.lang.String getPostCode() {
		return postCode == null ? ForwardConstants.EMPTY : postCode;
	}

	/**
	 * Setter for property postCode.
	 * 
	 * @param postCode
	 *            New value of property postCode.
	 *  
	 */
	public void setPostCode(java.lang.String postCode) {
		this.postCode = postCode;
	}

	/**
	 * Getter for property officePhone.
	 * 
	 * @return Value of property officePhone.
	 *  
	 */
	public java.lang.String getOfficePhone() {
		return officePhone == null ? ForwardConstants.EMPTY : officePhone;
	}

	/**
	 * Setter for property officePhone.
	 * 
	 * @param officePhone
	 *            New value of property officePhone.
	 *  
	 */
	public void setOfficePhone(java.lang.String officePhone) {
		this.officePhone = officePhone;
	}

	/**
	 * Getter for property address3.
	 * 
	 * @return Value of property address3.
	 *  
	 */
	public java.lang.String getAddress3() {
		return address3 == null ? ForwardConstants.EMPTY : address3;
	}

	/**
	 * Setter for property address3.
	 * 
	 * @param address3
	 *            New value of property address3.
	 *  
	 */
	public void setAddress3(java.lang.String address3) {
		this.address3 = address3;
	}

	/**
	 * Getter for property informDelay.
	 * 
	 * @return Value of property informDelay.
	 *  
	 */
	public java.lang.String getInformDelay() {
		return informDelay == null ? ForwardConstants.EMPTY : informDelay;
	}

	/**
	 * Setter for property informDelay.
	 * 
	 * @param informDelay
	 *            New value of property informDelay.
	 *  
	 */
	public void setInformDelay(java.lang.String informDelay) {
		this.informDelay = informDelay;
	}

	/**
	 * Getter for property address5.
	 * 
	 * @return Value of property address5.
	 *  
	 */
	public java.lang.String getAddress5() {
		return address5 == null ? ForwardConstants.EMPTY : address5;
	}

	/**
	 * Setter for property address5.
	 * 
	 * @param address5
	 *            New value of property address5.
	 *  
	 */
	public void setAddress5(java.lang.String address5) {
		this.address5 = address5;
	}

	/**
	 * Getter for property oooContact2.
	 * 
	 * @return Value of property oooContact2.
	 *  
	 */
	public java.lang.String getOooContact2() {
		return oooContact2 == null ? ForwardConstants.EMPTY : oooContact2;
	}

	/**
	 * Setter for property oooContact2.
	 * 
	 * @param oooContact2
	 *            New value of property oooContact2.
	 *  
	 */
	public void setOooContact2(java.lang.String oooContact2) {
		this.oooContact2 = oooContact2;
	}

	/**
	 * Getter for property flightCode1.
	 * 
	 * @return Value of property flightCode1.
	 *  
	 */
	public java.lang.String getFlightCode1() {
		return flightCode1 == null ? ForwardConstants.EMPTY : flightCode1;
	}

	/**
	 * Setter for property flightCode1.
	 * 
	 * @param flightCode1
	 *            New value of property flightCode1.
	 *  
	 */
	public void setFlightCode1(java.lang.String flightCode1) {
		this.flightCode1 = flightCode1;
	}

	/**
	 * Getter for property allContracts.
	 * 
	 * @return Value of property allContracts.
	 *  
	 */
	public java.util.List getAllContracts() {
		return allContracts;
	}

	/**
	 * Setter for property allContracts.
	 * 
	 * @param allContracts
	 *            New value of property allContracts.
	 *  
	 */
	public void setAllContracts(java.util.List allContracts) {
		this.allContracts = allContracts;
	}

	/**
	 * Getter for property action.
	 * 
	 * @return Value of property action.
	 *  
	 */
	public java.lang.String getAction() {
		return action == null ? ForwardConstants.EMPTY : action;
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
	 * Getter for property contractSelector.
	 * 
	 * @return Value of property contractSelector.
	 *  
	 */
	public java.lang.String getContractSelector() {
		return contractSelector;
	}

	/**
	 * Setter for property contractSelector.
	 * 
	 * @param contractSelector
	 *            New value of property contractSelector.
	 *  
	 */
	public void setContractSelector(java.lang.String contractSelector) {
		this.contractSelector = contractSelector;
	}

	/**
	 * Getter for property accountDeputy.
	 * 
	 * @return Value of property accountDeputy.
	 *  
	 */
	public java.lang.String getAccountDeputy() {
		return accountDeputy == null ? ForwardConstants.EMPTY : accountDeputy;
	}

	/**
	 * Setter for property accountDeputy.
	 * 
	 * @param accountDeputy
	 *            New value of property accountDeputy.
	 *  
	 */
	public void setAccountDeputy(java.lang.String accountDeputy) {
		this.accountDeputy = accountDeputy;
	}

	/**
	 * Getter for property accountHolder.
	 * 
	 * @return Value of property accountHolder.
	 *  
	 */
	public java.lang.String getAccountHolder() {
		return accountHolder == null ? ForwardConstants.EMPTY : accountHolder;
	}

	/**
	 * Setter for property accountHolder.
	 * 
	 * @param accountHolder
	 *            New value of property accountHolder.
	 *  
	 */
	public void setAccountHolder(java.lang.String accountHolder) {
		this.accountHolder = accountHolder;
	}

	/**
	 * Getter for property fcGroup.
	 * 
	 * @return Value of property fcGroup.
	 *  
	 */
	public boolean isFcGroup() {
		return fcGroup;
	}

	/**
	 * Setter for property fcGroup.
	 * 
	 * @param fcGroup
	 *            New value of property fcGroup.
	 *  
	 */
	public void setFcGroup(boolean fcGroup) {
		this.fcGroup = fcGroup;
	}

	/**
	 * Getter for property saveType.
	 * 
	 * @return Value of property saveType.
	 *  
	 */
	public java.lang.String getSaveType() {
		return saveType;
	}

	/**
	 * Setter for property saveType.
	 * 
	 * @param saveType
	 *            New value of property saveType.
	 *  
	 */
	public void setSaveType(java.lang.String saveType) {
		this.saveType = saveType;
	}

}