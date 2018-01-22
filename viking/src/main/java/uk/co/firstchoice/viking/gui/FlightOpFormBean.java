package uk.co.firstchoice.viking.gui;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import uk.co.firstchoice.fcutil.FCUtils;
import uk.co.firstchoice.viking.gui.util.ForwardConstants;

/** Form bean to hold the "editopdetails" JSP form data */
public final class FlightOpFormBean extends ActionForm {

	private String action = ForwardConstants.EMPTY;

	private String startAction = ForwardConstants.EMPTY;

	private String error = ForwardConstants.EMPTY;

	private String seriesNo = ForwardConstants.EMPTY;

	private String season = ForwardConstants.EMPTY;

	private String seasonStart = ForwardConstants.EMPTY;

	private String seasonEnd = ForwardConstants.EMPTY;

	private String fromDate = ForwardConstants.EMPTY;

	private String toDate = ForwardConstants.EMPTY;

	private String delFromDate = ForwardConstants.EMPTY;

	private String delToDate = ForwardConstants.EMPTY;

	private String weekday = ForwardConstants.EMPTY;

	private boolean cancelledFlight;

	private String amendmentType = ForwardConstants.EMPTY;

	private String comments = ForwardConstants.EMPTY;

	private String deletedSequences = ForwardConstants.EMPTY;

	private boolean group;

	private int originalNumSectors;

	private boolean liveVersion;

	private String[] sequenceNo;

	private String[] depAirport;

	private String[] arrAirport;

	//	private String[] depSlot;
	private String[] depTime;

	private String[] arrTime;

	private String[] dayOffset;

	private String[] flightPre;

	private String[] flightNo;

	private String[] aircraft;
	
	private String[] flightType; /* TUI Enhancements, added flight type  */
	
	private String[] rowChanged;

	private String[] gwChanged;

	//	private Integer[] numberOfAllocations;

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
		error = ForwardConstants.EMPTY;
		weekday = ForwardConstants.EMPTY;
		fromDate = ForwardConstants.EMPTY;
		toDate = ForwardConstants.EMPTY;
		cancelledFlight = false;
		comments = ForwardConstants.EMPTY;
		deletedSequences = ForwardConstants.EMPTY;
		//		depSlot = null;
		sequenceNo = null;
		depAirport = null;
		arrAirport = null;
		depTime = null;
		arrTime = null;
		dayOffset = null;
		flightPre = null;
		flightNo = null;
		aircraft = null;
		rowChanged = null;
		gwChanged = null;
		flightType = null;
			
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
		if (!action.equalsIgnoreCase("save")) {
			this.startAction = action;
		}
	}

	/**
	 * Get a single aircraft from the aircraft array
	 * 
	 * @param idx
	 *            The aircraft to get
	 * @return String. Aircraft type
	 */
	public java.lang.String getAircraft(int idx) {
		return (this.aircraft == null || idx >= this.aircraft.length ? ""
				: this.aircraft[idx]);
	}

	/**
	 * Getter for property aircraft.
	 * 
	 * @return Value of property aircraft.
	 *  
	 */
	public java.lang.String[] getAircraft() {
		return this.aircraft;
	}

	/**
	 * Setter for property aircraft.
	 * 
	 * @param aircraft
	 *            New value of property aircraft.
	 *  
	 */
	public void setAircraft(java.lang.String[] aircraft) {
		this.aircraft = aircraft;
	}

	/*  ADDED for TUI */
	/**
	 * Get a single flighttype from the flighttype array
	 * 
	 * @param idx
	 *            The flighttype to get
	 * @return String. Flighttype type
	 */
	public java.lang.String getFlightType(int idx) {
		return (this.flightType == null || idx >= this.flightType.length ? ""
				: this.flightType[idx]);
	}

	/**
	 * Getter for property flighttype.
	 * 
	 * @return Value of property flighttype.
	 *  
	 */
	public java.lang.String[] getFlightType() {
		return this.flightType;
	}

	/**
	 * Setter for property flighttype.
	 * 
	 * @param flighttype
	 *            New value of property flighttype.
	 *  
	 */
	public void setFlightType(java.lang.String[] flightType) {
		this.flightType = flightType;
	}
	
	
	
	
	/**
	 * Get a single Arrival Airport from the array
	 * 
	 * @param idx
	 *            Airport no. to get
	 * @return String. Airport
	 */
	public java.lang.String getArrAirport(int idx) {
		return (this.arrAirport == null || idx >= this.arrAirport.length ? ""
				: this.arrAirport[idx]);
	}

	/**
	 * Getter for property arrAirport.
	 * 
	 * @return Value of property arrAirport.
	 *  
	 */
	public java.lang.String[] getArrAirport() {
		return this.arrAirport;
	}

	/**
	 * Setter for property arrAirport.
	 * 
	 * @param arrAirport
	 *            New value of property arrAirport.
	 *  
	 */
	public void setArrAirport(java.lang.String[] arrAirport) {
		this.arrAirport = arrAirport;
	}

	/**
	 * Getter for single property arrTime.
	 * 
	 * @param idx -
	 *            index to get
	 * @return String. arrTime[idx]
	 */
	public java.lang.String getArrTime(int idx) {
		return (this.arrTime == null || idx >= this.arrTime.length ? ""
				: this.arrTime[idx]);
	}

	/**
	 * Getter for property arrTime.
	 * 
	 * @return Value of property arrTime.
	 *  
	 */
	public java.lang.String[] getArrTime() {
		return this.arrTime;
	}

	/**
	 * Setter for property arrTime.
	 * 
	 * @param arrTime
	 *            New value of property arrTime.
	 *  
	 */
	public void setArrTime(java.lang.String[] arrTime) {
		this.arrTime = arrTime;
	}

	/**
	 * Getter for property canceledFlight.
	 * 
	 * @return Value of property canceledFlight.
	 *  
	 */
	public boolean isCancelledFlight() {
		return cancelledFlight;
	}

	/**
	 * Setter for property canceledFlight.
	 * 
	 * @param canceledFlight
	 *            New value of property canceledFlight.
	 *  
	 */
	public void setCancelledFlight(boolean cancelledFlight) {
		this.cancelledFlight = cancelledFlight;
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
	 * Getter for single property dayOffset.
	 * 
	 * @param idx -
	 *            index to get
	 * @return int. parseInt(dayOffset[idx])
	 */
	public int getDayOffset(int idx) {
		String off = ((this.dayOffset == null || idx >= this.dayOffset.length) ? "0"
				: this.dayOffset[idx]);
		try {
			return Integer.parseInt(off);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Getter for property dayOffset.
	 * 
	 * @return Value of property dayOffset.
	 *  
	 */
	public String[] getDayOffset() {
		return this.dayOffset;
	}

	/**
	 * Setter for property dayOffset.
	 * 
	 * @param dayOffset
	 *            New value of property dayOffset.
	 *  
	 */
	public void setDayOffset(String[] dayOffset) {
		this.dayOffset = dayOffset;
	}

	/**
	 * Getter for single property depAirport.
	 * 
	 * @param idx -
	 *            index to get
	 * @return String. depAirport[idx]
	 */
	public java.lang.String getDepAirport(int idx) {
		return (this.depAirport == null || idx >= this.depAirport.length ? ""
				: this.depAirport[idx]);
	}

	/**
	 * Getter for property depAirport.
	 * 
	 * @return Value of property depAirport.
	 *  
	 */
	public java.lang.String[] getDepAirport() {
		return this.depAirport;
	}

	/**
	 * Setter for property depAirport.
	 * 
	 * @param depAirport
	 *            New value of property depAirport.
	 *  
	 */
	public void setDepAirport(java.lang.String[] depAirport) {
		this.depAirport = depAirport;
	}

	private boolean isSlot(String s) {
		s = FCUtils.notNull(s).trim();
		try {
			int i = Integer.parseInt(s);
			return (i > 0 && i < 4);
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Getter for single property depTime.
	 * 
	 * @param idx -
	 *            index to get
	 * @return String. depTime[idx]
	 */
	public java.lang.String getDepTime(int idx) {
		return (this.depTime == null || idx >= this.depTime.length
				|| isSlot(this.depTime[idx]) ? "" : this.depTime[idx]);
	}

	/**
	 * Getter for single property depSlot.
	 * 
	 * @param idx -
	 *            index to get
	 * @return String. depSlot[idx]
	 */
	public java.lang.String getDepSlot(int idx) {
		//		return (this.depSlot==null || idx >=
		// this.depSlot.length?"":this.depSlot[idx]);
		return (this.depTime == null || idx >= this.depTime.length
				|| !isSlot(this.depTime[idx]) ? "" : this.depTime[idx]);
	}

	/**
	 * Getter for property depTime.
	 * 
	 * @return Value of property depTime.
	 *  
	 */
	public java.lang.String[] getDepTime() {
		return this.depTime;
	}

	/**
	 * Setter for property depTime.
	 * 
	 * @param depTime
	 *            New value of property depTime.
	 *  
	 */
	public void setDepTime(java.lang.String[] depTime) {
		this.depTime = depTime;
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
	 * Getter for single property flightNo.
	 * 
	 * @param idx -
	 *            index to get
	 * @return String. flightNo[idx]
	 */
	public java.lang.String getFlightNo(int idx) {
		return (this.flightNo == null || idx >= this.flightNo.length ? ""
				: this.flightNo[idx]);
	}

	/**
	 * Getter for property flightNo.
	 * 
	 * @return Value of property flightNo.
	 *  
	 */
	public java.lang.String[] getFlightNo() {
		return this.flightNo;
	}

	/**
	 * Setter for property flightNo.
	 * 
	 * @param flightNo
	 *            New value of property flightNo.
	 *  
	 */
	public void setFlightNo(java.lang.String[] flightNo) {
		this.flightNo = flightNo;
	}

	/**
	 * Getter for property flightPre.
	 * 
	 * @return Value of property flightPre.
	 *  
	 */
	public java.lang.String getFlightPre(int idx) {
		return (this.flightPre == null || idx >= this.flightPre.length ? ""
				: this.flightPre[idx]);
	}

	/**
	 * Getter for property flightPre.
	 * 
	 * @return Value of property flightPre.
	 *  
	 */
	public java.lang.String[] getFlightPre() {
		return this.flightPre;
	}

	/**
	 * Setter for property flightPre.
	 * 
	 * @param flightPre
	 *            New value of property flightPre.
	 *  
	 */
	public void setFlightPre(java.lang.String[] flightPre) {
		this.flightPre = flightPre;
	}

	/**
	 * Getter for property fromDate.
	 * 
	 * @return Value of property fromDate.
	 *  
	 */
	public java.lang.String getFromDate() {
		return fromDate;
	}

	/**
	 * Setter for property fromDate.
	 * 
	 * @param fromDate
	 *            New value of property fromDate.
	 *  
	 */
	public void setFromDate(java.lang.String fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * Getter for property toDate.
	 * 
	 * @return Value of property toDate.
	 *  
	 */
	public java.lang.String getToDate() {
		return toDate;
	}

	/**
	 * Setter for property toDate.
	 * 
	 * @param toDate
	 *            New value of property toDate.
	 *  
	 */
	public void setToDate(java.lang.String toDate) {
		this.toDate = toDate;
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
	 * Getter for property depSlot.
	 * 
	 * @return Value of property depSlot.
	 *  
	 */
	//	public java.lang.String[] getDepSlot() {
	//		return this.depSlot;
	//	}
	/**
	 * Setter for property depSlot.
	 * 
	 * @param depSlot
	 *            New value of property depSlot.
	 *  
	 */
	//	public void setDepSlot(java.lang.String[] depSlot) {
	//		this.depSlot = depSlot;
	//	}
	/**
	 * Getter for property sequenceNo.
	 * 
	 * @return Value of property sequenceNo.
	 *  
	 */
	public String[] getSequenceNo() {
		return this.sequenceNo;
	}

	/**
	 * Getter for single property sequenceNo.
	 * 
	 * @param idx -
	 *            index to get
	 * @return String. sequenceNo[idx]
	 */
	public String getSequenceNo(int idx) {
		return (this.sequenceNo == null || idx >= this.sequenceNo.length ? "0"
				: this.sequenceNo[idx]);
	}

	/**
	 * Setter for property sequenceNo.
	 * 
	 * @param sequenceNo
	 *            New value of property sequenceNo.
	 *  
	 */
	public void setSequenceNo(String[] sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	/**
	 * Getter for property deletedSequences.
	 * 
	 * @return Value of property deletedSequences.
	 *  
	 */
	public java.lang.String getDeletedSequences() {
		return deletedSequences;
	}

	/**
	 * Setter for property deletedSequences.
	 * 
	 * @param deletedSequences
	 *            New value of property deletedSequences.
	 *  
	 */
	public void setDeletedSequences(java.lang.String deletedSequences) {
		this.deletedSequences = deletedSequences;
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
	 * Getter for property group.
	 * 
	 * @return Value of property group.
	 *  
	 */
	public boolean isGroup() {
		return group;
	}

	/**
	 * Setter for property group.
	 * 
	 * @param group
	 *            New value of property group.
	 *  
	 */
	public void setGroup(boolean group) {
		this.group = group;
	}

	/**
	 * Getter for property startAction.
	 * 
	 * @return Value of property startAction.
	 *  
	 */
	public java.lang.String getStartAction() {
		return startAction;
	}

	/**
	 * Setter for property startAction.
	 * 
	 * @param startAction
	 *            New value of property startAction.
	 *  
	 */
	public void setStartAction(java.lang.String startAction) {
		this.startAction = startAction;
	}

	/**
	 * Getter for property rowChanged.
	 * 
	 * @return Value of property rowChanged.
	 *  
	 */
	public java.lang.String[] getRowChanged() {
		return this.rowChanged;
	}

	/**
	 * Getter for property rowChanged.
	 * 
	 * @return boolean - row[idx] changed?
	 *  
	 */
	public boolean isRowChanged(int idx) {
		return (this.rowChanged == null || idx >= this.rowChanged.length ? false
				: this.rowChanged[idx].equals("1"));
	}

	/**
	 * Setter for property rowChanged.
	 * 
	 * @param rowChanged
	 *            New value of property rowChanged.
	 *  
	 */
	public void setRowChanged(java.lang.String[] rowChanged) {
		this.rowChanged = rowChanged;
	}

	/**
	 * Getter for property gwChanged.
	 * 
	 * @return Value of property gwChanged.
	 *  
	 */
	public java.lang.String[] getGwChanged() {
		return this.gwChanged;
	}

	/**
	 * Getter for property gwChanged.
	 * 
	 * @return boolean - row[idx] changed?
	 *  
	 */
	public boolean isGwChanged(int idx) {
		return (this.gwChanged == null || idx >= this.gwChanged.length ? false
				: this.gwChanged[idx].equals("1"));
	}

	/**
	 * Setter for property gwChanged.
	 * 
	 * @param gwChanged
	 *            New value of property gwChanged.
	 *  
	 */
	public void setGwChanged(java.lang.String[] gwChanged) {
		this.gwChanged = gwChanged;
	}

	/**
	 * Getter for property originalNumSectors.
	 * 
	 * @return Value of property originalNumSectors.
	 *  
	 */
	public int getOriginalNumSectors() {
		return originalNumSectors;
	}

	/**
	 * Setter for property originalNumSectors.
	 * 
	 * @param originalNumSectors
	 *            New value of property originalNumSectors.
	 *  
	 */
	public void setOriginalNumSectors(int originalNumSectors) {
		this.originalNumSectors = originalNumSectors;
	}

	/**
	 * Getter for property numberOfAllocations.
	 * 
	 * @return Value of property numberOfAllocations.
	 *  
	 */
	//	public java.lang.Integer[] getNumberOfAllocations() {
	//		return this.numberOfAllocations;
	//	}
	/**
	 * Getter for property single numberOfAllocations.
	 * 
	 * @return Value of property numberOfAllocations[idx].
	 *  
	 */
	//	public int getNumberOfAllocations(int idx) {
	//		if (numberOfAllocations != null && numberOfAllocations.length > idx) {
	//			return this.numberOfAllocations[idx].intValue();
	//		} else {
	//			return 0;
	//		}
	//	}
	/**
	 * Setter for property numberOfAllocations.
	 * 
	 * @param numberOfAllocations
	 *            New value of property numberOfAllocations.
	 *  
	 */
	//	public void setNumberOfAllocations(java.lang.Integer[]
	// numberOfAllocations) {
	//		this.numberOfAllocations = numberOfAllocations;
	//	}
	//	public boolean isAllocated() {
	//		if (numberOfAllocations != null)
	//			for (int i=0; i<numberOfAllocations.length; i++)
	//				if (numberOfAllocations[i] != null && numberOfAllocations[i].intValue() >
	// 0)
	//					return true;
	//		return false;
	//	}
	/**
	 * Getter for property delFromDate.
	 * 
	 * @return Value of property delFromDate.
	 *  
	 */
	public java.lang.String getDelFromDate() {
		return delFromDate;
	}

	/**
	 * Setter for property delFromDate.
	 * 
	 * @param delFromDate
	 *            New value of property delFromDate.
	 *  
	 */
	public void setDelFromDate(java.lang.String delFromDate) {
		this.delFromDate = delFromDate;
	}

	/**
	 * Getter for property delToDate.
	 * 
	 * @return Value of property delToDate.
	 *  
	 */
	public java.lang.String getDelToDate() {
		return delToDate;
	}

	/**
	 * Setter for property delToDate.
	 * 
	 * @param delToDate
	 *            New value of property delToDate.
	 *  
	 */
	public void setDelToDate(java.lang.String delToDate) {
		this.delToDate = delToDate;
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
	 * Getter for property seasonEnd.
	 * 
	 * @return Value of property seasonEnd.
	 *  
	 */
	public java.lang.String getSeasonEnd() {
		return seasonEnd;
	}

	/**
	 * Setter for property seasonEnd.
	 * 
	 * @param seasonEnd
	 *            New value of property seasonEnd.
	 *  
	 */
	public void setSeasonEnd(java.lang.String seasonEnd) {
		this.seasonEnd = seasonEnd;
	}

	/**
	 * Getter for property seasonStart.
	 * 
	 * @return Value of property seasonStart.
	 *  
	 */
	public java.lang.String getSeasonStart() {
		return seasonStart;
	}

	/**
	 * Setter for property seasonStart.
	 * 
	 * @param seasonStart
	 *            New value of property seasonStart.
	 *  
	 */
	public void setSeasonStart(java.lang.String seasonStart) {
		this.seasonStart = seasonStart;
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

}