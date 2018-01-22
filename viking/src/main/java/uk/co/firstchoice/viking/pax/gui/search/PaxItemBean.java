/*
 * Created on 08-Mar-2005
 * Filename: PaxItemBean.java
 */
package uk.co.firstchoice.viking.pax.gui.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.firstchoice.viking.pax.gui.search.PaxAllocationItemBean;
import uk.co.firstchoice.viking.pax.gui.utils.PaxUtil;

/**
 * @author Dwood
 * bean to model pax item data from datasource
 *  
 */
public class PaxItemBean {
	
	private String supplierId = null;

	private int allocationId = 0 ;
	
	private int allocation = 0 ;
	
	private String userId = null;
	
	private String statusCode = null;
	
	private Date departureDate = null;
	
	private String flightNumber = null;

	private String flightCode = null;

	private String seriesDetailId = null;

	private String carrier = null;
				
	private String series = null;
	
	private int noOfSeats = 0;
	
	private String seatClass = null;

	private Date fromDate = null;

	private Date toDate = null;

	private String route = null;
	
	private String customerCode = null;
	
	private List allocationItems = new ArrayList();

	private String allocationType = null;
	
	private String departureTime = null;
	
	private String flightNumberRoute = null;

	/**
	 * @return Returns the departureTime.
	 */
	public String getDepartureTime() {
		return departureTime;
	}
	/**
	 * @param departureTime The departureTime to set.
	 */
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}
	/**
	 * Default constructor, sets up allocation Item list
	 */
	public PaxItemBean() {
		final List allocList = new ArrayList();
		final PaxAllocationItemBean allocBean1 = new PaxAllocationItemBean();
		final PaxAllocationItemBean allocBean2 = new PaxAllocationItemBean();
		allocBean1.setPaxStatus(1);		// inital item
		allocList.add(allocBean1);	
		allocBean2.setPaxStatus(2);		// updated item
		allocList.add(allocBean2);
		this.setAllocationItems(allocList);
	}
	
	/**
	 * @return Returns the allocationItems.
	 */
	public List getAllocationItems() {
		return allocationItems;
	}

	/**
	 * @param allocationItems The allocationItems to set.
	 */
	public void setAllocationItems(final List allocationItems) {
		this.allocationItems = allocationItems;
	}
	

	/**
	 * @return Returns the seatClass.
	 */
	public String getSeatClass() {
		return this.seatClass;
	}
	/**
	 * @param seatClass The seatClass to set.
	 */
	public void setSeatClass(String seatClass) {
		this.seatClass = seatClass;
	}
	/**
	 * @return Returns the noOfSeats.
	 */
	public int getNoOfSeats() {
		return this.noOfSeats;
	}
	/**
	 * @param noOfSeats The noOfSeats to set.
	 */
	public void setNoOfSeats(int noOfSeats) {
		this.noOfSeats = noOfSeats;
	}
	/**
	 * @return Returns the flightNumber.
	 */
	public String getFlightNumber() {
		return this.flightCode + this.flightNumber;
	}
	/**
	 * @param flightNumber The flightNumber to set.
	 */
	public void setFlightNumber(final String flightNumber) {
		this.flightNumber = flightNumber;
	}
	/**
	 * @return Returns the statusCode.
	 */
	public String getStatusCode() {
		return this.statusCode;
	}
	/**
	 * @param statusCode The statusCode to set.
	 */
	public void setStatusCode(final String statusCode) {
		this.statusCode = statusCode;
	}
	/**
	 * @return Returns the allocationId.
	 */
	public int getAllocationId() {
		return this.allocationId;
	}
	
	/**
	 * @return Returns the allocationId as a string.
	 */
	public String getAllocationIdAsString() {
		return String.valueOf(this.allocationId);
	}	
	
	/**
	 * @param allocationId The allocationId to set.
	 */
	public void setAllocationId(final int allocationId) {
		this.allocationId = allocationId;
	}

	/**
	 * @return Returns the supplierId.
	 */
	public String getSupplierId() {
		return this.supplierId;
	}
	
	/**
	 * @return Returns the supplierId as a string.
	 */
	public String getSupplierIdAsString() {
		return String.valueOf(this.supplierId);
	}	
	/**
	 * @param supplierId The supplierId to set.
	 */
	public void setSupplierId(final String supplierId) {
		this.supplierId = supplierId;
	}
	/**
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return this.userId;
	}
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(final String userId) {
		this.userId = userId;
	}
	
	/**
	 * @return Returns the departureDate.
	 */
	public Date getDepartureDate() {
		return this.departureDate;
	}
	/**
	 * @param departureDate The departureDate to set.
	 */
	public void setDepartureDate(final Date departureDate) {
		this.departureDate = departureDate;
	}

	/**
	 * @param flightCode The flightCode to set.
	 */
	public void setFlightCode(final String flightCode) {
		this.flightCode = flightCode;
	}

	/**
	 * @return Returns the series.
	 */
	public String getSeries() {
		return this.series;
	}
	/**
	 * @param series The series to set.
	 */
	public void setSeries(final String series) {
		this.series = series;
	}

	/**
	 * @return Returns the seriesDetailId.
	 */
	public String getSeriesDetailId() {
		return this.seriesDetailId;
	}
	/**
	 * @param seriesDetailId The seriesDetailId to set.
	 */
	public void setSeriesDetailId(final String seriesDetailId) {
		this.seriesDetailId = seriesDetailId;
	}

	/**
	 * @return Returns the carrier.
	 */
	public String getCarrier() {
		return this.carrier;
	}
	/**
	 * @param carrier The carrier to set.
	 */
	public void setCarrier(final String carrier) {
		this.carrier = carrier;
	}


	/**
	 * @return Returns the allocation.
	 */
	public int getAllocation() {
		return this.allocation;
	}
	/**
	 * @param allocation The allocation to set.
	 */
	public void setAllocation(int allocation) {
		this.allocation = allocation;
	}
	
	/**
	 * 
	 * @return String route
	 */
	public String getRoute() {
		return this.route;
	}
	
	/**
	 * paxPropertiesMap - returns properties of this bean in a Bean
	 * TODO - change this to use refelction!
	 * @return Map
	 */
	public Map getPaxPropertiesMap() {
		HashMap map = new HashMap();

		PaxUtil paxUtil = new PaxUtil();
		
		map.put("supplierId", this.supplierId);
		map.put("allocationId", String.valueOf(this.allocationId));
		map.put("allocation", String.valueOf(this.allocation));
		map.put("statusCode", this.statusCode);
		map.put("toDate", paxUtil.dateToStringConversion(this.toDate));
		map.put("fromDate", paxUtil.dateToStringConversion(this.fromDate));
		map.put("departureDate", paxUtil.dateToStringConversion(this.departureDate));
		map.put("flightCode", this.flightCode);
		map.put("series", this.series);
		map.put("seriesDetailId", this.seriesDetailId);
		map.put("flightNumber", this.flightNumber);
		map.put("noOfSeats", String.valueOf(this.noOfSeats));
		map.put("customerCode", this.customerCode);
		map.put("seatClass", this.seatClass);
		map.put("flightNumber", this.flightNumber);
		map.put("route", this.route);
		map.put("allocationType", this.allocationType);
		return map;
	}
	

	/**
	 * @return Returns the flightCode.
	 */
	public String getFlightCode() {
		return this.flightCode;
	}
	/**
	 * @return Returns the fromDate.
	 */
	public Date getFromDate() {
		return this.fromDate;
	}
	/**
	 * @param fromDate The fromDate to set.
	 */
	public void setFromDate(final Date fromDate) {
		this.fromDate = fromDate;
	}
	/**
	 * @return Returns the toDate.
	 */
	public Date getToDate() {
		return this.toDate;
	}
	/**
	 * @param toDate The toDate to set.
	 */
	public void setToDate(final Date toDate) {
		this.toDate = toDate;
	}
	/**
	 * @param route The route to set.
	 */
	public void setRoute(final String route) {
		this.route = route;
	}
	/**
	 * @return Returns the customerCode.
	 */
	public String getCustomerCode() {
		return this.customerCode;
	}
	/**
	 * @param customerCode The customerCode to set.
	 */
	public void setCustomerCode(final String customerCode) {
		this.customerCode = customerCode;
	}
	/**
	 * @return Returns the allocationType.
	 */
	public String getAllocationType() {
		return this.allocationType;
	}
	/**
	 * @param allocationType The allocationType to set.
	 */
	public void setAllocationType(final String allocationType) {
		this.allocationType = allocationType;
	}
	/**
	 * @return Returns the flightNumberRoute.
	 */
	public String getFlightNumberRoute() {
		return this.flightNumber + this.route;
		//return flightNumberRoute;
	}
	/**
	 * @param flightNumberRoute The flightNumberRoute to set.
	 */
	public void setFlightNumberRoute(String flightNumberRoute) {
		this.flightNumberRoute = flightNumberRoute;
	}
}