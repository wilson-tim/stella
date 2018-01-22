/*
 * OperationsSectorBean.java
 *
 * Created on 28 July 2004, 11:37
 */

package uk.co.firstchoice.viking.gui;

/**
 * 
 * @author Lars Svensson
 */
public class OperationsSectorBean {

	private String flightNo;

	private String scheduledDep;

	private String actualDep;

	private String scheduledArr;

	private String actualArr;

	private String depOvernight;

	private String arrOvernight;

	private String seriesDetailId;

	private String estimateOrActual; 
	
	/** Creates a new instance of OperationsSectorBean */
	public OperationsSectorBean() {
	}

	/** Creates a new instance of OperationsSectorBean */
	public OperationsSectorBean(String flightNo, String scheduledDep,
			String actualDep, String scheduledArr, String actualArr,
			String depOvernight, String arrOvernight, String seriesDetailId,
			String estimateOrActual ) {
		this.flightNo = flightNo;
		this.actualDep = actualDep;
		this.actualArr = actualArr;
		this.scheduledArr = scheduledArr;
		this.scheduledDep = scheduledDep;
		this.depOvernight = depOvernight;
		this.arrOvernight = arrOvernight;
		this.seriesDetailId = seriesDetailId;
		this.estimateOrActual = estimateOrActual;
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
	 * Getter for property flightNo.
	 * 
	 * @return Value of property flightNo.
	 */
	public java.lang.String getFlightNo() {
		return flightNo;
	}

	/**
	 * Setter for property flightNo.
	 * 
	 * @param flightNo
	 *            New value of property flightNo.
	 */
	public void setFlightNo(java.lang.String flightNo) {
		this.flightNo = flightNo;
	}

	/**
	 * Getter for property scheduledArr.
	 * 
	 * @return Value of property scheduledArr.
	 */
	public java.lang.String getScheduledArr() {
		return scheduledArr;
	}

	/**
	 * Setter for property scheduledArr.
	 * 
	 * @param scheduledArr
	 *            New value of property scheduledArr.
	 */
	public void setScheduledArr(java.lang.String scheduledArr) {
		this.scheduledArr = scheduledArr;
	}

	/**
	 * Getter for property scheduledDep.
	 * 
	 * @return Value of property scheduledDep.
	 */
	public java.lang.String getScheduledDep() {
		return scheduledDep;
	}

	/**
	 * Setter for property scheduledDep.
	 * 
	 * @param scheduledDep
	 *            New value of property scheduledDep.
	 */
	public void setScheduledDep(java.lang.String scheduledDep) {
		this.scheduledDep = scheduledDep;
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
	 * @return Returns the estimateOrAcctual.
	 */
	public String getEstimateOrActual() {
		return estimateOrActual;
	}
	/**
	 * @param estimateOrAcctual The estimateOrAcctual to set.
	 */
	public void setEstimateOrActual(String estimateOrActual) {
		this.estimateOrActual = estimateOrActual;
	}
}