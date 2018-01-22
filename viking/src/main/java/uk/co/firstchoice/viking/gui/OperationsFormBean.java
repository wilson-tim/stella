package uk.co.firstchoice.viking.gui;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form Bean to hold the values for the Search html form. Have getter and setter
 * methods for all values. Is automatically populated by Struts (see Struts
 * configuration)
 */
public final class OperationsFormBean extends ActionForm {

	private String action;

	private String error;

	private String startDate;

	private String endDate;

	private String fromTime;

	private boolean onlyIncomplete;

	private String excludeCarrier;

	private java.util.Map series;

	private String[] seriesIds;

	private int maxSectors; // the highest number of sectors in any series

	private String seriesId; //

	private String seriesDetailId; //

	private String actualDep; //  Submitted changes

	private String actualArr; //

	private String depOvernight; //   

	private String arrOvernight; //

	private String departureDate;

	private String comments; //

	private boolean incExcCarrierInd ;

	private String gatewayCode;
	
	private String estimateOrActual;
	
	private String allSeriesDetailId;

	/**
	 * Reset all properties to their default values.
	 * 
	 * @param mapping
	 *            The mapping used to select this instance
	 * @param request
	 *            The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		onlyIncomplete = false;
		incExcCarrierInd = false;
		series = new java.util.HashMap();
		seriesIds = new String[0];
		java.util.GregorianCalendar gc = new java.util.GregorianCalendar();
		gc.add(java.util.Calendar.HOUR_OF_DAY, -1);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
		fromTime = "04:00"; //sdf.format(gc.getTime());
		sdf.applyPattern("dd/MM/yyyy");
		startDate = sdf.format(gc.getTime());
		gc.add(java.util.Calendar.DAY_OF_MONTH, 3);
		endDate = sdf.format(gc.getTime());
		excludeCarrier = "FCA,TOM";
		seriesDetailId = null;
		seriesId = null;
		
	}

	/**
	 * Getter for property endDate.
	 * 
	 * @return Value of property endDate.
	 */
	public java.lang.String getEndDate() {
		return endDate;
	}

	/**
	 * Setter for property endDate.
	 * 
	 * @param endDate
	 *            New value of property endDate.
	 */
	public void setEndDate(java.lang.String endDate) {
		this.endDate = endDate;
	}

	/**
	 * Getter for property excludeCarrier.
	 * 
	 * @return Value of property excludeCarrier.
	 */
	public java.lang.String getExcludeCarrier() {
		return excludeCarrier;
	}

	/**
	 * Setter for property excludeCarrier.
	 * 
	 * @param excludeCarrier
	 *            New value of property excludeCarrier.
	 */
	public void setExcludeCarrier(java.lang.String excludeCarrier) {
		this.excludeCarrier = excludeCarrier;
	}

	/**
	 * Getter for property fromTime.
	 * 
	 * @return Value of property fromTime.
	 */
	public java.lang.String getFromTime() {
		return fromTime;
	}

	/**
	 * Setter for property fromTime.
	 * 
	 * @param fromTime
	 *            New value of property fromTime.
	 */
	public void setFromTime(java.lang.String fromTime) {
		this.fromTime = fromTime;
	}

	/**
	 * Getter for property onlyIncomplete.
	 * 
	 * @return Value of property onlyIncomplete.
	 */
	public boolean isOnlyIncomplete() {
		return onlyIncomplete;
	}

	/**
	 * Setter for property onlyIncomplete.
	 * 
	 * @param onlyIncomplete
	 *            New value of property onlyIncomplete.
	 */
	public void setOnlyIncomplete(boolean onlyIncomplete) {
		this.onlyIncomplete = onlyIncomplete;
	}

	/**
	 * Getter for property startDate.
	 * 
	 * @return Value of property startDate.
	 */
	public java.lang.String getStartDate() {
		return startDate;
	}

	/**
	 * Setter for property startDate.
	 * 
	 * @param startDate
	 *            New value of property startDate.
	 */
	public void setStartDate(java.lang.String startDate) {
		this.startDate = startDate;
	}

	/**
	 * Getter for property maxSectors.
	 * 
	 * @return Value of property maxSectors.
	 */
	public int getMaxSectors() {
		return maxSectors;
	}

	/**
	 * Setter for property maxSectors.
	 * 
	 * @param maxSectors
	 *            New value of property maxSectors.
	 */
	public void setMaxSectors(int maxSectors) {
		this.maxSectors = maxSectors;
	}

	/**
	 * Getter for property series.
	 * 
	 * @return Value of property series.
	 */
	public java.util.Map getSeries() {
		return series;
	}

	/**
	 * Setter for property series.
	 * 
	 * @param series
	 *            New value of property series.
	 */
	public void setSeries(java.util.Map series) {
		this.series = series;
	}

	/**
	 * Getter for property seriesIds.
	 * 
	 * @return Value of property seriesIds.
	 */
	public java.lang.String[] getSeriesIds() {
		return this.seriesIds;
	}

	/**
	 * Setter for property seriesIds.
	 * 
	 * @param seriesIds
	 *            New value of property seriesIds.
	 */
	public void setSeriesIds(java.lang.String[] seriesIds) {
		this.seriesIds = seriesIds;
	}

	/**
	 * Getter for property action.
	 * 
	 * @return Value of property action.
	 */
	public java.lang.String getAction() {
		return action;
	}

	/**
	 * Setter for property action.
	 * 
	 * @param action
	 *            New value of property action.
	 */
	public void setAction(java.lang.String action) {
		this.action = action;
	}

	/**
	 * Getter for property error.
	 * 
	 * @return Value of property error.
	 */
	public java.lang.String getError() {
		return error;
	}

	/**
	 * Setter for property error.
	 * 
	 * @param error
	 *            New value of property error.
	 */
	public void setError(java.lang.String error) {
		this.error = error;
	}

	/**
	 * Getter for property comments.
	 * 
	 * @return Value of property comments.
	 */
	public java.lang.String getComments() {
		return comments;
	}

	/**
	 * Setter for property comments.
	 * 
	 * @param comments
	 *            New value of property comments.
	 */
	public void setComments(java.lang.String comments) {
		this.comments = comments;
	}

	/**
	 * Getter for property actualArr.
	 * 
	 * @return Value of property actualArr.
	 */
	public java.lang.String getActualArr() {
		return actualArr;
	}

	/**
	 * Setter for property actualArr.
	 * 
	 * @param actualArr
	 *            New value of property actualArr.
	 */
	public void setActualArr(java.lang.String actualArr) {
		this.actualArr = actualArr;
	}

	/**
	 * Getter for property actualDep.
	 * 
	 * @return Value of property actualDep.
	 */
	public java.lang.String getActualDep() {
		return actualDep;
	}

	/**
	 * Setter for property actualDep.
	 * 
	 * @param actualDep
	 *            New value of property actualDep.
	 */
	public void setActualDep(java.lang.String actualDep) {
		this.actualDep = actualDep;
	}

	/**
	 * Getter for property seriesId.
	 * 
	 * @return Value of property seriesId.
	 */
	public java.lang.String getSeriesId() {
		return seriesId;
	}

	/**
	 * Setter for property seriesId.
	 * 
	 * @param seriesId
	 *            New value of property seriesId.
	 */
	public void setSeriesId(java.lang.String seriesId) {
		this.seriesId = seriesId;
	}

	/**
	 * Getter for property seriesDetailId.
	 * 
	 * @return Value of property seriesDetailId.
	 */
	public java.lang.String getSeriesDetailId() {
		return seriesDetailId;
	}

	/**
	 * Setter for property seriesDetailId.
	 * 
	 * @param seriesDetailId
	 *            New value of property seriesDetailId.
	 */
	public void setSeriesDetailId(java.lang.String seriesDetailId) {
		this.seriesDetailId = seriesDetailId;
	}

	/**
	 * Getter for property departureDate.
	 * 
	 * @return Value of property departureDate.
	 */
	public java.lang.String getDepartureDate() {
		return departureDate;
	}

	/**
	 * Setter for property departureDate.
	 * 
	 * @param departureDate
	 *            New value of property departureDate.
	 */
	public void setDepartureDate(java.lang.String departureDate) {
		this.departureDate = departureDate;
	}

	/**
	 * Getter for property arrOvernight.
	 * 
	 * @return Value of property arrOvernight.
	 */
	public java.lang.String getArrOvernight() {
		return arrOvernight;
	}

	/**
	 * Setter for property arrOvernight.
	 * 
	 * @param arrOvernight
	 *            New value of property arrOvernight.
	 */
	public void setArrOvernight(java.lang.String arrOvernight) {
		this.arrOvernight = arrOvernight;
	}

	/**
	 * Getter for property depOvernight.
	 * 
	 * @return Value of property depOvernight.
	 */
	public java.lang.String getDepOvernight() {
		return depOvernight;
	}

	/**
	 * Setter for property depOvernight.
	 * 
	 * @param depOvernight
	 *            New value of property depOvernight.
	 */
	public void setDepOvernight(java.lang.String depOvernight) {
		this.depOvernight = depOvernight;
	}

	/**
	 * Getter for property gatewayCode.
	 * 
	 * @return Value of property gatewayCode.
	 */
	public java.lang.String getGatewayCode() {
		return gatewayCode;
	}

	/**
	 * Setter for property gatewayCode.
	 * 
	 * @param gatewayCode
	 *            New value of property gatewayCode.
	 */
	public void setGatewayCode(java.lang.String gatewayCode) {
		this.gatewayCode = gatewayCode;
	}

	/**
	 * Getter for property incExcCarrierInd.
	 * 
	 * @return Value of property incExcCarrierInd.
	 */
	public boolean isIncExcCarrierInd() {
		return incExcCarrierInd;
	}

	/**
	 * Setter for property incExcCarrierInd.
	 * 
	 * @param incExcCarrierInd
	 *            New value of property incExcCarrierInd.
	 */
	public void setIncExcCarrierInd(boolean incExcCarrierInd) {
		this.incExcCarrierInd = incExcCarrierInd;
	}

	/**
	 * @return Returns the estimateOrActual.
	 */
	public String getEstimateOrActual() {
		return estimateOrActual;
	}
	/**
	 * @param estimateOrActual The estimateOrActual to set.
	 */
	public void setEstimateOrActual(String estimateOrActual) {
		this.estimateOrActual = estimateOrActual;
	}
	/**
	 * @return Returns the allSeriesDetailId.
	 */
	public String getAllSeriesDetailId() {
		return allSeriesDetailId;
	}
	/**
	 * @param allSeriesDetailId The allSeriesDetailId to set.
	 */
	public void setAllSeriesDetailId(String allSeriesDetailId) {
		this.allSeriesDetailId = allSeriesDetailId;
	}
}