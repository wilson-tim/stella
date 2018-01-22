package uk.co.firstchoice.viking.gui;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import uk.co.firstchoice.viking.gui.util.ForwardConstants;

/**
 * Form Bean to hold the values for the Search html form. Have getter and setter
 * methods for all values. Is automatically populated by Struts (see Struts
 * configuration)
 */
public final class SearchFormBean extends ActionForm {

	public static final String SERIES_SEARCH_TYPE = "S";

	public static final String CUSTOMER_SEARCH_TYPE = "C";

	private String action = ForwardConstants.EMPTY;

	private String seriesNo = ForwardConstants.EMPTY;

	private String season = ForwardConstants.EMPTY;

	private String businessArea = ForwardConstants.EMPTY;

	private String flightPre = ForwardConstants.EMPTY;

	private String flightNo = ForwardConstants.EMPTY;

	private String startDate = ForwardConstants.EMPTY;

	private String endDate = ForwardConstants.EMPTY;

	private String depTime = ForwardConstants.EMPTY;

	private String depTimeOper = ForwardConstants.EMPTY;

	private String arrTime = ForwardConstants.EMPTY;

	private String arrTimeOper = ForwardConstants.EMPTY;

	private String depAirport = ForwardConstants.EMPTY;

	private String arrAirport = ForwardConstants.EMPTY;

	private String aircraftType = ForwardConstants.EMPTY;
	
	private String weekday = ForwardConstants.EMPTY;

	private String cancel = ForwardConstants.EMPTY;

	private String flightNumber = ForwardConstants.EMPTY;
	
	private String customer = ForwardConstants.EMPTY;

	private String supplier = ForwardConstants.EMPTY;

	private String broker = ForwardConstants.EMPTY;

	private String seatClass = ForwardConstants.EMPTY;

	private String contractId = ForwardConstants.EMPTY;

	private String totalPrice = ForwardConstants.EMPTY;

	private String totalPriceOper = ForwardConstants.EMPTY;

	private String seatQty = ForwardConstants.EMPTY;

	private String seatQtyOper = ForwardConstants.EMPTY;

	private String seatQtyType = ForwardConstants.EMPTY;

	private String searchType = ForwardConstants.EMPTY;

	private String geminiCode = ForwardConstants.EMPTY;
	
	private String flightType = ForwardConstants.EMPTY;
	
	private String mealType = ForwardConstants.EMPTY;
	
	private boolean includeAllocation = false;

	private String[] status;

	private String version = ForwardConstants.EMPTY;

	private java.util.List searchResult;

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
		season = ForwardConstants.EMPTY;
		businessArea = ForwardConstants.EMPTY;
		flightNo = ForwardConstants.EMPTY;
		startDate = ForwardConstants.EMPTY;
		endDate = ForwardConstants.EMPTY;
		depTime = ForwardConstants.EMPTY;
		depTimeOper = ForwardConstants.EMPTY;
		arrTime = ForwardConstants.EMPTY;
		arrTimeOper = ForwardConstants.EMPTY;
		depAirport = ForwardConstants.EMPTY;
		arrAirport = ForwardConstants.EMPTY;
		aircraftType = ForwardConstants.EMPTY;
		weekday = ForwardConstants.EMPTY;
		flightNumber = ForwardConstants.EMPTY;
		cancel = ForwardConstants.EMPTY;
		customer = ForwardConstants.EMPTY;
		supplier = ForwardConstants.EMPTY;
		broker = ForwardConstants.EMPTY;
		seatClass = ForwardConstants.EMPTY;
		contractId = ForwardConstants.EMPTY;
		totalPrice = ForwardConstants.EMPTY;
		totalPriceOper = ForwardConstants.EMPTY;
		seatQty = ForwardConstants.EMPTY;
		seatQtyOper = ForwardConstants.EMPTY;
		seatQtyType = ForwardConstants.EMPTY;
		flightType = ForwardConstants.EMPTY;
		mealType = ForwardConstants.EMPTY;
		
		
		includeAllocation = false;
	}

	/**
	 * Getter for property flightnumber.
	 * 
	 * @return Value of property flightnumber.
	 *  
	 */
	public java.lang.String getFlightNumber() {
		return flightNumber;
	}

	/**
	 * Setter for property flightnumber.
	 * 
	 * @param action
	 *            New value of property flightnumber.
	 *  
	 */
	public void setFightNumber(java.lang.String flightNumber) {
		this.flightNumber = flightNumber;
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
	 * Getter for property aircraftType.
	 * 
	 * @return Value of property aircraftType.
	 *  
	 */
	public java.lang.String getAircraftType() {
		return aircraftType;
	}

	/**
	 * Setter for property aircraftType.
	 * 
	 * @param aircraftType
	 *            New value of property aircraftType.
	 *  
	 */
	public void setAircraftType(java.lang.String aircraftType) {
		this.aircraftType = aircraftType;
	}

	/**
	 * Getter for property arrAirport.
	 * 
	 * @return Value of property arrAirport.
	 *  
	 */
	public java.lang.String getArrAirport() {
		return arrAirport;
	}

	/**
	 * Setter for property arrAirport.
	 * 
	 * @param arrAirport
	 *            New value of property arrAirport.
	 *  
	 */
	public void setArrAirport(java.lang.String arrAirport) {
		this.arrAirport = arrAirport;
	}

	/**
	 * Getter for property arrTime.
	 * 
	 * @return Value of property arrTime.
	 *  
	 */
	public java.lang.String getArrTime() {
		return arrTime;
	}

	/**
	 * Setter for property arrTime.
	 * 
	 * @param arrTime
	 *            New value of property arrTime.
	 *  
	 */
	public void setArrTime(java.lang.String arrTime) {
		this.arrTime = arrTime;
	}

	/**
	 * Getter for property arrTimeOper.
	 * 
	 * @return Value of property arrTimeOper.
	 *  
	 */
	public java.lang.String getArrTimeOper() {
		return arrTimeOper;
	}

	/**
	 * Setter for property arrTimeOper.
	 * 
	 * @param arrTimeOper
	 *            New value of property arrTimeOper.
	 *  
	 */
	public void setArrTimeOper(java.lang.String arrTimeOper) {
		this.arrTimeOper = arrTimeOper;
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
	 * Getter for property businessArea.
	 * 
	 * @return Value of property businessArea.
	 *  
	 */
	public java.lang.String getBusinessArea() {
		return businessArea;
	}

	/**
	 * Setter for property businessArea.
	 * 
	 * @param businessArea
	 *            New value of property businessArea.
	 *  
	 */
	public void setBusinessArea(java.lang.String businessArea) {
		this.businessArea = businessArea;
	}

	/**
	 * Getter for property cancel.
	 * 
	 * @return Value of property cancel.
	 *  
	 */
	public java.lang.String getCancel() {
		return cancel;
	}

	/**
	 * Setter for property cancel.
	 * 
	 * @param cancel
	 *            New value of property cancel.
	 *  
	 */
	public void setCancel(java.lang.String cancel) {
		this.cancel = cancel;
	}

	/**
	 * Getter for property contractID.
	 * 
	 * @return Value of property contractID.
	 *  
	 */
	public java.lang.String getContractId() {
		return contractId;
	}

	/**
	 * Setter for property contractID.
	 * 
	 * @param contractID
	 *            New value of property contractID.
	 *  
	 */
	public void setContractId(java.lang.String contractId) {
		this.contractId = contractId;
	}

	/**
	 * Getter for property customer.
	 * 
	 * @return Value of property customer.
	 *  
	 */
	public java.lang.String getCustomer() {
		return customer;
	}

	/**
	 * Setter for property customer.
	 * 
	 * @param customer
	 *            New value of property customer.
	 *  
	 */
	public void setCustomer(java.lang.String customer) {
		this.customer = customer;
	}

	/**
	 * Getter for property depAirport.
	 * 
	 * @return Value of property depAirport.
	 *  
	 */
	public java.lang.String getDepAirport() {
		return depAirport;
	}

	/**
	 * Setter for property depAirport.
	 * 
	 * @param depAirport
	 *            New value of property depAirport.
	 *  
	 */
	public void setDepAirport(java.lang.String depAirport) {
		this.depAirport = depAirport;
	}

	/**
	 * Getter for property depTime.
	 * 
	 * @return Value of property depTime.
	 *  
	 */
	public java.lang.String getDepTime() {
		return depTime;
	}

	/**
	 * Setter for property depTime.
	 * 
	 * @param depTime
	 *            New value of property depTime.
	 *  
	 */
	public void setDepTime(java.lang.String depTime) {
		this.depTime = depTime;
	}

	/**
	 * Getter for property depTimeOper.
	 * 
	 * @return Value of property depTimeOper.
	 *  
	 */
	public java.lang.String getDepTimeOper() {
		return depTimeOper;
	}

	/**
	 * Setter for property depTimeOper.
	 * 
	 * @param depTimeOper
	 *            New value of property depTimeOper.
	 *  
	 */
	public void setDepTimeOper(java.lang.String depTimeOper) {
		this.depTimeOper = depTimeOper;
	}

	/**
	 * Getter for property endDate.
	 * 
	 * @return Value of property endDate.
	 *  
	 */
	public java.lang.String getEndDate() {
		return endDate;
	}

	/**
	 * Setter for property endDate.
	 * 
	 * @param endDate
	 *            New value of property endDate.
	 *  
	 */
	public void setEndDate(java.lang.String endDate) {
		this.endDate = endDate;
	}

	/**
	 * Getter for property flightNo.
	 * 
	 * @return Value of property flightNo.
	 *  
	 */
	public java.lang.String getFlightNo() {
		return flightNo;
	}

	/**
	 * Setter for property flightNo.
	 * 
	 * @param flightNo
	 *            New value of property flightNo.
	 *  
	 */
	public void setFlightNo(java.lang.String flightNo) {
		this.flightNo = flightNo;
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
	 * Getter for property seatQtyOper.
	 * 
	 * @return Value of property seatQtyOper.
	 *  
	 */
	public java.lang.String getSeatQtyOper() {
		return seatQtyOper;
	}

	/**
	 * Setter for property seatQtyOper.
	 * 
	 * @param seatQtyOper
	 *            New value of property seatQtyOper.
	 *  
	 */
	public void setSeatQtyOper(java.lang.String seatQtyOper) {
		this.seatQtyOper = seatQtyOper;
	}

	/**
	 * Getter for property seatQtyType.
	 * 
	 * @return Value of property seatQtyType.
	 *  
	 */
	public java.lang.String getSeatQtyType() {
		return seatQtyType;
	}

	/**
	 * Setter for property seatQtyType.
	 * 
	 * @param seatQtyType
	 *            New value of property seatQtyType.
	 *  
	 */
	public void setSeatQtyType(java.lang.String seatQtyType) {
		this.seatQtyType = seatQtyType;
	}

	/**
	 * Getter for property startDate.
	 * 
	 * @return Value of property startDate.
	 *  
	 */
	public java.lang.String getStartDate() {
		return startDate;
	}

	/**
	 * Setter for property startDate.
	 * 
	 * @param startDate
	 *            New value of property startDate.
	 *  
	 */
	public void setStartDate(java.lang.String startDate) {
		this.startDate = startDate;
	}

	/**
	 * Getter for property status.
	 * 
	 * @return Value of property status.
	 *  
	 */
	public java.lang.String[] getStatus() {
		return status;
	}

	/**
	 * Getter for status string
	 * 
	 * @return Value of property status.
	 *  
	 */
	public java.lang.String getStatusStr() {
		StringBuffer sbuf = new StringBuffer();
		for (int i = 0; i < status.length; i++)
			sbuf.append(status[i]);
		return sbuf.toString();
	}

	/**
	 * Setter for property status.
	 * 
	 * @param status
	 *            New value of property status.
	 *  
	 */
	public void setStatus(java.lang.String[] status) {
		this.status = status;
	}

	/**
	 * Getter for property supplier.
	 * 
	 * @return Value of property supplier.
	 *  
	 */
	public java.lang.String getSupplier() {
		return supplier;
	}

	/**
	 * Setter for property supplier.
	 * 
	 * @param supplier
	 *            New value of property supplier.
	 *  
	 */
	public void setSupplier(java.lang.String supplier) {
		this.supplier = supplier;
	}

	/**
	 * Getter for property totalPrice.
	 * 
	 * @return Value of property totalPrice.
	 *  
	 */
	public java.lang.String getTotalPrice() {
		return totalPrice;
	}

	/**
	 * Setter for property totalPrice.
	 * 
	 * @param totalPrice
	 *            New value of property totalPrice.
	 *  
	 */
	public void setTotalPrice(java.lang.String totalPrice) {
		this.totalPrice = totalPrice;
	}

	/**
	 * Getter for property totalPriceOper.
	 * 
	 * @return Value of property totalPriceOper.
	 *  
	 */
	public java.lang.String getTotalPriceOper() {
		return totalPriceOper;
	}

	/**
	 * Setter for property totalPriceOper.
	 * 
	 * @param totalPriceOper
	 *            New value of property totalPriceOper.
	 *  
	 */
	public void setTotalPriceOper(java.lang.String totalPriceOper) {
		this.totalPriceOper = totalPriceOper;
	}

	/**
	 * Getter for property weekday.
	 * 
	 * @return Value of property weekday.
	 *  
	 */
	public java.lang.String getWeekday() {
		return weekday;
	}

	/**
	 * Setter for property weekday.
	 * 
	 * @param weekday
	 *            New value of property weekday.
	 *  
	 */
	public void setWeekday(java.lang.String weekday) {
		this.weekday = weekday;
	}

	/**
	 * Getter for property flightPre.
	 * 
	 * @return Value of property flightPre.
	 *  
	 */
	public java.lang.String getFlightPre() {
		return flightPre;
	}

	/**
	 * Setter for property flightPre.
	 * 
	 * @param flightPre
	 *            New value of property flightPre.
	 *  
	 */
	public void setFlightPre(java.lang.String flightPre) {
		this.flightPre = flightPre;
	}

	/**
	 * Getter for property version.
	 * 
	 * @return Value of property version.
	 *  
	 */
	public java.lang.String getVersion() {
		return version;
	}

	/**
	 * Setter for property version.
	 * 
	 * @param version
	 *            New value of property version.
	 *  
	 */
	public void setVersion(java.lang.String version) {
		this.version = version;
	}

	/**
	 * Getter for property searchResult.
	 * 
	 * @return Value of property searchResult.
	 *  
	 */
	public java.util.List getSearchResult() {
		return searchResult;
	}

	/**
	 * Setter for property searchResult.
	 * 
	 * @param searchResult
	 *            New value of property searchResult.
	 *  
	 */
	public void setSearchResult(java.util.List searchResult) {
		this.searchResult = searchResult;
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
	 * Calculate and return the maximum number of customers
	 *  
	 */
	public int getMaxNumCustomers() {
		int max = 1;
		for (java.util.Iterator i = searchResult.iterator(); i.hasNext();) {
			int len = ((SeriesSearchResult) i.next()).getCustomerMinMax().length;
			if (max < len)
				max = len;
		}
		return max;
	}

	/**
	 * Getter for property searchType.
	 * 
	 * @return Value of property searchType.
	 *  
	 */
	public java.lang.String getSearchType() {
		return searchType;
	}

	/**
	 * Setter for property searchType.
	 * 
	 * @param searchType
	 *            New value of property searchType.
	 *  
	 */
	public void setSearchType(java.lang.String searchType) {
		this.searchType = searchType;
	}

	/**
	 * Getter for property includeAllocation.
	 * 
	 * @return Value of property includeAllocation.
	 */
	public boolean isIncludeAllocation() {
		return includeAllocation;
	}

	/**
	 * Setter for property includeAllocation.
	 * 
	 * @param includeAllocation
	 *            New value of property includeAllocation.
	 */
	public void setIncludeAllocation(boolean includeAllocation) {
		this.includeAllocation = includeAllocation;
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
	 * Getter for property flightType.
	 * 
	 * @return Value of property flightType.
	 */
	public java.lang.String getFlightType() {
		return flightType;
	}

	/**
	 * Setter for property flightType.
	 * 
	 * @param flightType
	 *            New value of property flightType.
	 */
	public void setFlightType(java.lang.String flightType) {
		this.flightType = flightType;
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
	 * @param mealType
	 *            New value of property mealType.
	 */
	public void setMealType(java.lang.String mealType) {
		this.mealType = mealType;
	}
	
}