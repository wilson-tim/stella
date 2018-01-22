package uk.co.firstchoice.viking.gui;

import uk.co.firstchoice.fcutil.FCUtils;
import uk.co.firstchoice.viking.gui.util.ForwardConstants;

/**
 * Form Bean to display the values of the Search Result . Have getter and setter
 * methods for all values.
 */
public final class AllocationSearchResult implements ISearchResult {

	private String seriesNo = ForwardConstants.EMPTY;

	private String gwFrom = ForwardConstants.EMPTY;

	private String gwTo = ForwardConstants.EMPTY;

	private String startDate = ForwardConstants.EMPTY;

	private String endDate = ForwardConstants.EMPTY;

	private String depTime = ForwardConstants.EMPTY;
	
	private String frequency = ForwardConstants.EMPTY;

	private String arrTime = ForwardConstants.EMPTY;

	private String flightNumber = ForwardConstants.EMPTY;

	//	private String carrier = ForwardConstants.EMPTY;
	private String allocation = ForwardConstants.EMPTY;

	private String seatClass = ForwardConstants.EMPTY;

	private String price = ForwardConstants.EMPTY;

	private String brokerCode = ForwardConstants.EMPTY;

	private String geminiCode = ForwardConstants.EMPTY;
	
	//private String mealType = ForwardConstants.EMPTY;

	private String dayOfWeek = ForwardConstants.EMPTY;

	public AllocationSearchResult() {
	}

	public AllocationSearchResult(String seriesNo, String gwFrom, String gwTo,
			String startDate, String endDate, String depTime, String arrTime,
			String frequency,
			String flightNumber,
			//	String carrier,
			String allocation, String seatClass, String price,
			String brokerCode, String geminiCode, String dayOfWeek) {
		this.seriesNo = seriesNo;
		this.gwFrom = gwFrom;
		this.gwTo = gwTo;
		this.startDate = startDate;
		this.endDate = endDate;
		this.depTime = depTime;
		this.arrTime = arrTime;
		this.frequency = frequency;
		this.flightNumber = flightNumber;
		//		this.carrier = carrier;
		this.allocation = allocation;
		this.seatClass = seatClass;
		this.price = price;
		this.brokerCode = brokerCode;
		this.geminiCode = geminiCode;
		//this.mealType = mealType;
		this.dayOfWeek = dayOfWeek;
	}

	private String toNBSP(String s) {
		if (s == null || s.equals(""))
			return " ";
		else
			return s;
	}

	public String buildCSVString() {
		final String ENTRY_SEP = ";";
		final String RATE_SEP = "-";
		String pce = FCUtils.notNull(price);
		String[] entries = pce.split(ENTRY_SEP);
		String currencies = "";
		String fares = "";
		for (int i = 0; i < entries.length; i++) {
			String[] sa = entries[i].split(RATE_SEP);
			if (sa.length == 2) {
				currencies += ((i == 0 ? "" : "\n") + sa[0]);
				float f;
				try {
					f = Float.parseFloat(sa[1]);
					fares += ((i == 0 ? "" : "\n") + FCUtils.floatToString(f));
				} catch (NumberFormatException e) {
					fares += (i == 0 ? "" : "\n");
				}
			}
		}
		//		if (pce.endsWith("u000d")) pce = pce.substring(0, pce.length()-5);
		//		pce = pce.replaceAll("u000d","\n");
		String[] ala = (FCUtils.notNull(allocation) + " ; ; ").split(";");
		return ROW_SEP + toNBSP(seriesNo) + COL_SEP + toNBSP(gwFrom) + COL_SEP
				+ toNBSP(gwTo) + COL_SEP + toNBSP(startDate) + COL_SEP
				+ toNBSP(endDate) + COL_SEP + toNBSP(frequency) + COL_SEP + toNBSP(depTime) + COL_SEP
				+ toNBSP(arrTime) + COL_SEP + toNBSP(flightNumber) +
				//				COL_SEP+toNBSP(carrier)+
				COL_SEP + ala[0] + // for sort
				COL_SEP + "<span class='" + ala[1].trim() + "'>" + ala[0]
				+ "</span>" + // for display
				COL_SEP + FCUtils.notNull(seatClass) + COL_SEP + currencies
				+ COL_SEP + fares + COL_SEP + toNBSP(brokerCode) + COL_SEP
				+ toNBSP(geminiCode) + COL_SEP  +  dayOfWeek;
	}

	/**
	 * Getter for property allocation.
	 * 
	 * @return Value of property allocation.
	 *  
	 */
	public java.lang.String getAllocation() {
		return allocation;
	}

	/**
	 * Setter for property allocation.
	 * 
	 * @param allocation
	 *            New value of property allocation.
	 *  
	 */
	public void setAllocation(java.lang.String allocation) {
		this.allocation = allocation;
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
	 * Getter for property frequency.
	 * 
	 * @return Value of property frequency.
	 *  
	 */
	public java.lang.String getFrequency() {
		return frequency;
	}

	/**
	 * Setter for property depTime.
	 * 
	 * @param depTime
	 *            New value of property depTime.
	 *  
	 */
	public void setFrequency(java.lang.String frequency) {
		this.frequency = frequency;
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
	 * Getter for property flightNumber.
	 * 
	 * @return Value of property flightNumber.
	 *  
	 */
	public java.lang.String getFlightNumber() {
		return flightNumber;
	}

	/**
	 * Setter for property flightNumber.
	 * 
	 * @param flightNumber
	 *            New value of property flightNumber.
	 *  
	 */
	public void setFlightNumber(java.lang.String flightNumber) {
		this.flightNumber = flightNumber;
	}

	/**
	 * Getter for property gwFrom.
	 * 
	 * @return Value of property gwFrom.
	 *  
	 */
	public java.lang.String getGwFrom() {
		return gwFrom;
	}

	/**
	 * Setter for property gwFrom.
	 * 
	 * @param gwFrom
	 *            New value of property gwFrom.
	 *  
	 */
	public void setGwFrom(java.lang.String gwFrom) {
		this.gwFrom = gwFrom;
	}

	/**
	 * Getter for property gwTo.
	 * 
	 * @return Value of property gwTo.
	 *  
	 */
	public java.lang.String getGwTo() {
		return gwTo;
	}

	/**
	 * Setter for property gwTo.
	 * 
	 * @param gwTo
	 *            New value of property gwTo.
	 *  
	 */
	public void setGwTo(java.lang.String gwTo) {
		this.gwTo = gwTo;
	}

	/**
	 * Getter for property price.
	 * 
	 * @return Value of property price.
	 *  
	 */
	public java.lang.String getPrice() {
		return price;
	}

	/**
	 * Setter for property price.
	 * 
	 * @param price
	 *            New value of property price.
	 *  
	 */
	public void setPrice(java.lang.String price) {
		this.price = price;
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
	 * Getter for property brokerCode.
	 * 
	 * @return Value of property brokerCode.
	 *  
	 */
	public java.lang.String getBrokerCode() {
		return brokerCode;
	}

	/**
	 * Setter for property brokerCode.
	 * 
	 * @param brokerCode
	 *            New value of property brokerCode.
	 *  
	 */
	public void setBrokerCode(java.lang.String brokerCode) {
		this.brokerCode = brokerCode;
	}

	/**
	 * Getter for property dayOfWeek.
	 * 
	 * @return Value of property dayOfWeek.
	 *  
	 */
	public java.lang.String getDayOfWeek() {
		return dayOfWeek;
	}

	/**
	 * Setter for property dayOfWeek.
	 * 
	 * @param dayOfWeek
	 *            New value of property dayOfWeek.
	 *  
	 */
	public void setDayOfWeek(java.lang.String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
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
	 * Getter for property mealType.
	 * 
	 * @return Value of property mealType.
	 *  
	 */
	/*public java.lang.String getMealType() {
		return mealType;
	}
	*/

	/**
	 * Setter for property mealType.
	 * 
	 * @param mealType
	 *            New value of property mealType.
	 *  
	 */
/*	public void setMealType(java.lang.String mealType) {
		this.mealType = mealType;
	}*/
}