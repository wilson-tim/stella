package uk.co.firstchoice.viking.gui;

import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import uk.co.firstchoice.fcutil.FCException;
import uk.co.firstchoice.fcutil.FCUtils;
import uk.co.firstchoice.viking.gui.util.ForwardConstants;

public final class SellBuyFormBean extends ActionForm {

	private static final String GBP = "GBP";

	/* private static final int numCurrencies = 5; */

	private String action = ForwardConstants.EMPTY; // Action to perform (Save,

	// reload etc. )

	private String editType = ForwardConstants.EMPTY; // add, edit

	private String csCode = ForwardConstants.EMPTY; // The Customer/Supplier

	// code to edit

	private String type = ForwardConstants.EMPTY; // Customer or Supplier

	private String seriesNo = ForwardConstants.EMPTY;

	private String single = ForwardConstants.EMPTY;

	private boolean liveVersion;

	private String[] sectors; //	Selected sectors

	private String[] availSectors; //	Available sectors

	private String[] startDate;

	private String[] endDate;

	private String delstartDate = ForwardConstants.EMPTY;

	private String delendDate = ForwardConstants.EMPTY;

	private String seatClass = ForwardConstants.EMPTY;

	private String custSupp = ForwardConstants.EMPTY;

	private String contractId = ForwardConstants.EMPTY;

	private String geminiCode = ForwardConstants.EMPTY;
	
	private String mealType = ForwardConstants.EMPTY;
	
//	private List mealTypes;

//	private String status = ForwardConstants.EMPTY;
	
	private String status; // = 'N'  

	private String seatQty = ForwardConstants.EMPTY;

	private String[] oldcurrency;// = new String[1];

	private String[] currency;// = {GBP};

	private String[] exchangeRate;// = {"1.00"};

	private String[] price;// = new String[1];

	private String broker = ForwardConstants.EMPTY;

	private String amendmentType = ForwardConstants.EMPTY;

	private String comments = ForwardConstants.EMPTY;

	private List contractCodes;

	private String error = ForwardConstants.EMPTY;
	
	private String deleteAfterWarning =ForwardConstants.EMPTY;

	/**
	 * Reset all properties to their default values.
	 * 
	 * @param mapping
	 *            The mapping used to select this instance
	 * @param request
	 *            The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		action = ForwardConstants.EMPTY;
		type = ForwardConstants.EMPTY;
		sectors = new String[0];
		startDate = new String[0];
		endDate = new String[0];
		seatClass = ForwardConstants.EMPTY;
		custSupp = ForwardConstants.EMPTY;
		contractCodes = null;
		//mealTypes = null;
		geminiCode = ForwardConstants.EMPTY;
		mealType  = ForwardConstants.EMPTY;
		status =  "N" ;  //ForwardConstants.EMPTY;
		seatQty = ForwardConstants.EMPTY;
		oldcurrency = new String[1];
		currency = new String[1];
		currency[0] = GBP;
		exchangeRate = new String[1];
		exchangeRate[0] = "1.00";
		price = new String[1];
		broker = ForwardConstants.EMPTY;
		comments = ForwardConstants.EMPTY;
		deleteAfterWarning = "N";
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
	 * Getter for property comments.
	 * 
	 * @return Value of property comments.
	 *  
	 */
	public java.lang.String getComments() {
		return comments;
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
	 * Getter for property custSupp.
	 * 
	 * @return Value of property custSupp.
	 *  
	 */
	public java.lang.String getCustSupp() {
		return custSupp;
	}

	/**
	 * Setter for property custSupp.
	 * 
	 * @param custSupp
	 *            New value of property custSupp.
	 *  
	 */
	public void setCustSupp(java.lang.String custSupp) {
		this.custSupp = custSupp;
	}

	/**
	 * Getter for property endDate.
	 * 
	 * @return Value of property endDate.
	 *  
	 */
	public java.lang.String[] getEndDate() {
		return endDate;
	}

	/**
	 * Setter for property endDate.
	 * 
	 * @param endDate
	 *            New value of property endDate.
	 *  
	 */
	public void setEndDate(java.lang.String[] endDate) {
		this.endDate = endDate;
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
	 * Getter for property seatQty.
	 * 
	 * @return Value of property seatQty.
	 *  
	 */
	public java.lang.String getSeatQty() {
		return seatQty;
	}

	/**
	 * Setter for property seatQty.
	 * 
	 * @param seatQty
	 *            New value of property seatQty.
	 *  
	 */
	public void setSeatQty(java.lang.String seatQty) {
		this.seatQty = seatQty;
	}

	/**
	 * Getter for property sectors.
	 * 
	 * @return Value of property sectors.
	 *  
	 */
	public java.lang.String[] getSectors() {
		return this.sectors;
	}

	/**
	 * Getter for property sector .
	 * 
	 * @return Value of property sectors.
	 *  
	 */
	public java.lang.String getSectors(int i) {
		return this.sectors[i].trim();
	}

	/**
	 * Setter for property sectors.
	 * 
	 * @param sectors
	 *            New value of property sectors.
	 *  
	 */
	public void setSectors(java.lang.String[] sectors) {
		this.sectors = sectors;
	}

	/**
	 * Getter for property startDate.
	 * 
	 * @return Value of property startDate.
	 *  
	 */
	public java.lang.String[] getStartDate() {
		return startDate;
	}

	/**
	 * Setter for property startDate.
	 * 
	 * @param startDate
	 *            New value of property startDate.
	 *  
	 */
	public void setStartDate(java.lang.String[] startDate) {
		this.startDate = startDate;
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

	/**
	 * Getter for property availSectors.
	 * 
	 * @return Value of property availSectors.
	 *  
	 */
	public java.lang.String[] getAvailSectors() {
		return this.availSectors;
	}

	/**
	 * Setter for property availSectors.
	 * 
	 * @param availSectors
	 *            New value of property availSectors.
	 *  
	 */
	public void setAvailSectors(java.lang.String[] availSectors) {
		this.availSectors = availSectors;
	}

	/**
	 * Getter for property geminiCode.
	 * 
	 * @return Value of property geminiCode.
	 *  
	 */
	public java.lang.String getGeminiCode() {
		return geminiCode;
	}

	/**
	 * Setter for property geminiCode.
	 * 
	 * @param geminiCode
	 *            New value of property geminiCode.
	 *  
	 */
	public void setGeminiCode(java.lang.String geminiCode) {
		this.geminiCode = geminiCode;
	}

	/**
	 * Getter for property status.
	 * 
	 * @return Value of property status.
	 *  
	 */
	
	/**
	 * Getter for property mealType.
	 * 
	 * @return Value of property mealType.
	 *  
	 */
	public java.lang.String getMealType() {
		return mealType;
	}

	/**
	 * Setter for property mealType.
	 * 
	 * @param mealType
	 *            New value of property mealType.
	 *  
	 */
	public void setMealType(java.lang.String mealType) {
		this.mealType = mealType;
	}

	/**
	 * Getter for property mealTypes.
	 * 
	 * @return Value of property mealTypes.
	 *  
	 */
	/*public java.util.List getMealTypes() {
		return mealTypes == null ? new Vector() : mealTypes;
	}

*/
	/**
	 * Setter for property mealTypes.
	 * 
	 * @param mealTypes
	 *            New value of property mealTypes.
	 *  
	 */
	/*public void setMealTypes(java.util.List mealTypes) {
		this.mealTypes = mealTypes;
	}
	*/
	
	/**
	 * Getter for property status.
	 * 
	 * @return Value of property status.
	 *  
	 */
	public java.lang.String getStatus() {
		return status;
	}

	/**
	 * Setter for property status.
	 * 
	 * @param status
	 *            New value of property status.
	 *  
	 */
	public void setStatus(java.lang.String status) {
		this.status = status;
	}

	/**
	 * Getter for property contractCodes.
	 * 
	 * @return Value of property contractCodes.
	 *  
	 */
	public java.util.List getContractCodes() {
		return contractCodes == null ? new Vector() : contractCodes;
	}

	/**
	 * Setter for property contractCodes.
	 * 
	 * @param contractCodes
	 *            New value of property contractCodes.
	 *  
	 */
	public void setContractCodes(java.util.List contractCodes) {
		this.contractCodes = contractCodes;
	}

	/**
	 * Getter for property contractId.
	 * 
	 * @return Value of property contractId.
	 *  
	 */
	public java.lang.String getContractId() {
		return contractId;
	}

	/**
	 * Setter for property contractId.
	 * 
	 * @param contractId
	 *            New value of property contractId.
	 *  
	 */
	public void setContractId(java.lang.String contractId) {
		this.contractId = contractId;
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
	 * Getter for property seriesNo as Long.
	 * 
	 * @return Value of property parseLong(seriesNo).
	 *  
	 */
	public long getSeriesNoLong() throws FCException {
		long sn = -1;
		try {
			sn = Long.parseLong(seriesNo);
		} catch (NumberFormatException e) {
			throw new FCException("Unable to parse seriesNo (" + seriesNo
					+ ") to long", e);
		}
		return sn;
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
	 * Getter for property editType.
	 * 
	 * @return Value of property editType.
	 *  
	 */
	public java.lang.String getEditType() {
		return editType;
	}

	/**
	 * Setter for property editType.
	 * 
	 * @param editType
	 *            New value of property editType.
	 *  
	 */
	public void setEditType(java.lang.String editType) {
		this.editType = editType;
	}

	/**
	 * Getter for property csCode.
	 * 
	 * @return Value of property csCode.
	 *  
	 */
	public java.lang.String getCsCode() {
		return csCode;
	}

	/**
	 * Setter for property csCode.
	 * 
	 * @param csCode
	 *            New value of property csCode.
	 *  
	 */
	public void setCsCode(java.lang.String csCode) {
		this.csCode = csCode;
	}

	/**
	 * Getter for property delendDate.
	 * 
	 * @return Value of property delendDate.
	 *  
	 */
	public java.lang.String getDelendDate() {
		return delendDate;
	}

	/**
	 * Setter for property delendDate.
	 * 
	 * @param delendDate
	 *            New value of property delendDate.
	 *  
	 */
	public void setDelendDate(java.lang.String delendDate) {
		this.delendDate = delendDate;
	}

	/**
	 * Getter for property delstartDate.
	 * 
	 * @return Value of property delstartDate.
	 *  
	 */
	public java.lang.String getDelstartDate() {
		return delstartDate;
	}

	/**
	 * Setter for property delstartDate.
	 * 
	 * @param delstartDate
	 *            New value of property delstartDate.
	 *  
	 */
	public void setDelstartDate(java.lang.String delstartDate) {
		this.delstartDate = delstartDate;
	}

	/**
	 * Getter for property currency.
	 * 
	 * @return Value of property currency.
	 *  
	 */
	public java.lang.String[] getCurrency() {
		return this.currency;
	}

	/**
	 * Getter for single property currency.
	 * 
	 * @return Value of property currency[x].
	 *  
	 */
	public java.lang.String getCurrency(int x) {
		if (x > -1 && x < this.currency.length)
			return FCUtils.notNull(this.currency[x]);
		else
			return "";
	}

	/**
	 * Setter for property currency.
	 * 
	 * @param currency
	 *            New value of property currency.
	 *  
	 */
	public void setCurrency(java.lang.String[] currency) {
		this.currency = currency;
	}

	/**
	 * Getter for property exchangeRate.
	 * 
	 * @return Value of property exchangeRate.
	 *  
	 */
	public java.lang.String[] getExchangeRate() {
		return this.exchangeRate;
	}

	/**
	 * Getter for single property exchangeRate.
	 * 
	 * @return Value of property exchangeRate[x].
	 *  
	 */
	public java.lang.String getExchangeRate(int x) {
		if (x > -1 && x < this.exchangeRate.length)
			return FCUtils.notNull(this.exchangeRate[x]);
		else
			return "";
	}

	/**
	 * Setter for property exchangeRate.
	 * 
	 * @param exchangeRate
	 *            New value of property exchangeRate.
	 *  
	 */
	public void setExchangeRate(java.lang.String[] exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	/**
	 * Getter for property oldcurrency.
	 * 
	 * @return Value of property oldcurrency.
	 *  
	 */
	public java.lang.String[] getOldcurrency() {
		return this.oldcurrency;
	}

	/**
	 * Getter for single property oldcurrency.
	 * 
	 * @return Value of property oldcurrency[x].
	 *  
	 */
	public java.lang.String getOldcurrency(int x) {
		if (x > -1 && x < this.oldcurrency.length)
			return FCUtils.notNull(this.oldcurrency[x]);
		else
			return "";
	}

	/**
	 * Setter for property oldcurrency.
	 * 
	 * @param oldcurrency
	 *            New value of property oldcurrency.
	 *  
	 */
	public void setOldcurrency(java.lang.String[] oldcurrency) {
		this.oldcurrency = oldcurrency;
	}

	/**
	 * Getter for property price.
	 * 
	 * @return Value of property price.
	 *  
	 */
	public java.lang.String[] getPrice() {
		return this.price;
	}

	/**
	 * Getter for single property price.
	 * 
	 * @return Value of property price[x].
	 *  
	 */
	public java.lang.String getPrice(int x) {
		if (x > -1 && x < this.price.length)
			return FCUtils.notNull(this.price[x]);
		else
			return "";
	}

	/**
	 * Setter for property price.
	 * 
	 * @param price
	 *            New value of property price.
	 *  
	 */
	public void setPrice(java.lang.String[] price) {
		this.price = price;
	}

	/**
	 * Getter for property amendmentType.
	 * 
	 * @return Value of property amendmentType.
	 *  
	 */
	public java.lang.String getAmendmentType() {
		return amendmentType;
	}

	/**
	 * Setter for property amendmentType.
	 * 
	 * @param amendmentType
	 *            New value of property amendmentType.
	 *  
	 */
	public void setAmendmentType(java.lang.String amendmentType) {
		this.amendmentType = amendmentType;
	}

	/**
	 * Getter for property single.
	 * 
	 * @return Value of property single.
	 *  
	 */
	public java.lang.String getSingle() {
		return single;
	}

	/**
	 * Setter for property single.
	 * 
	 * @param single
	 *            New value of property single.
	 *  
	 */
	public void setSingle(java.lang.String single) {
		this.single = single;
	}

	/**
	 * Getter for property liveVersion.
	 * 
	 * @return Value of property liveVersion.
	 */
	public boolean isLiveVersion() {
		return liveVersion;
	}

	/**
	 * Setter for property liveVersion.
	 * 
	 * @param liveVersion
	 *            New value of property liveVersion.
	 */
	public void setLiveVersion(boolean liveVersion) {
		this.liveVersion = liveVersion;
	}

	/**
	 * @return Returns the deleteAfterWarning.
	 */
	public String getDeleteAfterWarning() {
		return deleteAfterWarning;
	}
	/**
	 * @param deleteAfterWarning The deleteAfterWarning to set.
	 */
	public void setDeleteAfterWarning(String deleteAfterWarning) {
		this.deleteAfterWarning = deleteAfterWarning;
	}
}