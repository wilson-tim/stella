package uk.co.firstchoice.viking.gui;

import uk.co.firstchoice.fcutil.FCUtils;
import uk.co.firstchoice.viking.gui.util.ForwardConstants;

/**
 * Form Bean to display the values of the Search Result . Have getter and setter
 * methods for all values. Is automatically populated by Struts (see Struts
 * configuration)
 */
public final class SeriesSearchResult implements ISearchResult {

	private String seriesNo = ForwardConstants.EMPTY;

	//	private String route = ForwardConstants.EMPTY;
	private String dow = ForwardConstants.EMPTY;

	private String startdate = ForwardConstants.EMPTY;

	private String enddate = ForwardConstants.EMPTY;

	private String deptime = ForwardConstants.EMPTY;
	
	private String flightNumber = ForwardConstants.EMPTY;

	private String supplier = ForwardConstants.EMPTY;

	private String carrier = ForwardConstants.EMPTY;

	private String allgateway = ForwardConstants.EMPTY;

	private String purchaseminmax = ForwardConstants.EMPTY;

	private String availabilityMinMax = ForwardConstants.EMPTY;

	private String[] customerMinMax;

	public SeriesSearchResult() {
	};

	public SeriesSearchResult(String seriesNo,
	//	String route,
			String dow, String startdate, String enddate, String deptime, String flightNumber,
			String supplier, String carrier, String allgateway,
			String purchaseminmax, String availabilityMinMax,
			String[] customerMinMax) {
		this.seriesNo = seriesNo;
		//		this.route = route;
		this.dow = dow;
		this.startdate = startdate;
		this.enddate = enddate;
		this.deptime = deptime;
		this.flightNumber = flightNumber;
		this.supplier = supplier;
		this.carrier = carrier;
		this.allgateway = allgateway;
		this.purchaseminmax = purchaseminmax;
		this.availabilityMinMax = availabilityMinMax;
		this.customerMinMax = customerMinMax;
	};

	private String toNBSP(String s) {
		if (s == null || s.equals(""))
			return " ";
		else
			return s;
	}

	public String buildCSVString() {
		
		String gws = FCUtils.notNull(allgateway);
		if (gws.endsWith(ForwardConstants.MULTILINE_SEPERATOR)) {
			gws = gws.substring(0, gws.length()
					- ForwardConstants.MULTILINE_SEPERATOR.length());
		}
		String[] gwsa = gws.split(ForwardConstants.MULTILINE_SEPERATOR);
		for (int i = 0; i < gwsa.length; i++) {
			if (gwsa[i].indexOf("(0)") > -1) {
				gwsa[i] = "<span style='color: red;'>" + gwsa[i] + "</span>";
			}
		}
		gws = FCUtils.join(gwsa, "<br/>");
		
		// For formating flight no
		String fnum = FCUtils.notNull(flightNumber);
		if (gws.endsWith(ForwardConstants.MULTILINE_SEPERATOR)) {
			fnum = fnum.substring(0, gws.length()
					- ForwardConstants.MULTILINE_SEPERATOR.length());
		}
		String[] fnuma = fnum.split(ForwardConstants.MULTILINE_SEPERATOR);
		for (int i = 0; i < fnuma.length; i++) {
			if (fnuma[i].indexOf("(0)") > -1) {
				fnuma[i] = "<span style='color: red;'>" + fnuma[i] + "</span>";
			}
		}
		fnum = FCUtils.join(fnuma, "<br/>");
		
		//		gws = gws.replaceAll(ForwardConstants.MULTILINE_SEPERATOR,"\n");
		// adding " / " below ensures that split("/") will always return
		// at least two elements in the array, even if just two blanks
		String[] pua = (FCUtils.notNull(purchaseminmax) + " ; ; ").split(";");
		String[] ava = (FCUtils.notNull(availabilityMinMax) + " ; ; ")
				.split(";");
		String s = ROW_SEP + toNBSP(seriesNo) + COL_SEP + gws + COL_SEP
				+ toNBSP(dow) + COL_SEP + toNBSP(startdate) + COL_SEP
				+ toNBSP(enddate) + COL_SEP + toNBSP(deptime) + COL_SEP
				+ fnum + COL_SEP
				+ toNBSP(supplier) + COL_SEP + toNBSP(carrier) + COL_SEP
				+ pua[0] + // for sort
				COL_SEP + "<span class='" + pua[2].trim() + "'>" + pua[0]
				+ "</span>" + // for display
				COL_SEP + pua[1] + // for sort
				COL_SEP + "<span class='" + pua[2].trim() + "'>" + pua[1]
				+ "</span>" + // for display
				COL_SEP + ava[0] + // for sort
				COL_SEP + "<span class='" + ava[2].trim() + "'>" + ava[0]
				+ "</span>" + // for display
				COL_SEP + ava[1] + // for sort
				COL_SEP + "<span class='" + ava[2].trim() + "'>" + ava[1]
				+ "</span>"; // for display
		String[] sa = customerMinMax;
		for (int idx = 0; idx < sa.length; idx++) {
			String[] sa2 = (FCUtils.notNull(sa[idx]) + " ").split(";");
			if (sa2.length == 3) {
				s += (COL_SEP + "<span class='" + sa2[2].trim() + "'>" + sa2[0]
						+ "</span>" + // name
						COL_SEP + "<span class='" + sa2[2].trim() + "'>"
						+ sa2[1] + "</span>" + // min/max
						COL_SEP + FCUtils.notNull(seriesNo) + ";" + sa2[0]); // key
			}
		}
		return s;
	}

	/**
	 * Getter for property carrier.
	 * 
	 * @return Value of property carrier.
	 *  
	 */
	public java.lang.String getCarrier() {
		return carrier;
	}

	/**
	 * Setter for property carrier.
	 * 
	 * @param carrier
	 *            New value of property carrier.
	 *  
	 */
	public void setCarrier(java.lang.String carrier) {
		this.carrier = carrier;
	}

	/**
	 * Getter for property deptime.
	 * 
	 * @return Value of property deptime.
	 *  
	 */
	public java.lang.String getDeptime() {
		return deptime;
	}

	/**
	 * Setter for property deptime.
	 * 
	 * @param deptime
	 *            New value of property deptime.
	 *  
	 */
	public void setDeptime(java.lang.String deptime) {
		this.deptime = deptime;
	}

	/**
	 * Getter for property dow.
	 * 
	 * @return Value of property dow.
	 *  
	 */
	public java.lang.String getDow() {
		return dow;
	}

	/**
	 * Setter for property dow.
	 * 
	 * @param dow
	 *            New value of property dow.
	 *  
	 */
	public void setDow(java.lang.String dow) {
		this.dow = dow;
	}

	/**
	 * Getter for property enddate.
	 * 
	 * @return Value of property enddate.
	 *  
	 */
	public java.lang.String getEnddate() {
		return enddate;
	}

	/**
	 * Setter for property enddate.
	 * 
	 * @param enddate
	 *            New value of property enddate.
	 *  
	 */
	public void setEnddate(java.lang.String enddate) {
		this.enddate = enddate;
	}

	/**
	 * Getter for property route.
	 * 
	 * @return Value of property route.
	 *  
	 */
	//	public java.lang.String getRoute() {
	//	    return route;
	//	}
	/**
	 * Setter for property route.
	 * 
	 * @param route
	 *            New value of property route.
	 *  
	 */
	//	public void setRoute(java.lang.String route) {
	//	    this.route = route;
	//	}
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
	 * Getter for property startdate.
	 * 
	 * @return Value of property startdate.
	 *  
	 */
	public java.lang.String getStartdate() {
		return startdate;
	}

	/**
	 * Setter for property startdate.
	 * 
	 * @param startdate
	 *            New value of property startdate.
	 *  
	 */
	public void setStartdate(java.lang.String startdate) {
		this.startdate = startdate;
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
	 * Getter for property allgateway.
	 * 
	 * @return Value of property allgateway.
	 *  
	 */
	public java.lang.String getAllgateway() {
		return allgateway;
	}

	/**
	 * Setter for property allgateway.
	 * 
	 * @param allgateway
	 *            New value of property allgateway.
	 *  
	 */
	public void setAllgateway(java.lang.String allgateway) {
		this.allgateway = allgateway;
	}

	/**
	 * Getter for property purchaseminmax.
	 * 
	 * @return Value of property purchaseminmax.
	 *  
	 */
	public java.lang.String getPurchaseminmax() {
		return purchaseminmax;
	}

	/**
	 * Setter for property purchaseminmax.
	 * 
	 * @param purchaseminmax
	 *            New value of property purchaseminmax.
	 *  
	 */
	public void setPurchaseminmax(java.lang.String purchaseminmax) {
		this.purchaseminmax = purchaseminmax;
	}

	/**
	 * Getter for property customerMinMax.
	 * 
	 * @return Value of property customerMinMax.
	 *  
	 */
	public java.lang.String[] getCustomerMinMax() {
		return this.customerMinMax;
	}

	/**
	 * Getter for single property customerMinMax.
	 * 
	 * @return Value of property customerMinMax[idx].
	 *  
	 */
	public java.lang.String getCustomerMinMax(int idx) {
		try {
			return customerMinMax[idx];
		} catch (Exception e) { // nullpointer or indexOutOfRange
			return "";
		}
	}

	/**
	 * Setter for property customerMinMax.
	 * 
	 * @param customerMinMax
	 *            New value of property customerMinMax.
	 *  
	 */
	public void setCustomerMinMax(java.lang.String[] customerMinMax) {
		this.customerMinMax = customerMinMax;
	}

	/**
	 * Getter for property availabilityMinMax.
	 * 
	 * @return Value of property availabilityMinMax.
	 *  
	 */
	public java.lang.String getAvailabilityMinMax() {
		return availabilityMinMax;
	}

	/**
	 * Setter for property availabilityMinMax.
	 * 
	 * @param availabilityMinMax
	 *            New value of property availabilityMinMax.
	 *  
	 */
	public void setAvailabilityMinMax(java.lang.String availabilityMinMax) {
		this.availabilityMinMax = availabilityMinMax;
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

}