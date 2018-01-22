/*
 * Created on 30-Mar-2005
 * Filename: PaxUtil.java
 */
package uk.co.firstchoice.viking.pax.gui.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import uk.co.firstchoice.viking.pax.gui.search.PaxAllocationItemBean;
import uk.co.firstchoice.viking.pax.gui.search.PaxItemBean;
import uk.co.firstchoice.viking.pax.gui.search.PaxSearchForm;


/**
 * @author Dwood
 * Util class for converting between object types
 */
public class PaxUtil {

	/**
	 * Default constructor
	 *
	 */
	public PaxUtil() {
	}
	
	/**
	 * method to convert PaxSearchForm to PaxSearchItem
	 * @param searchForm PaxSearchForm
	 * @return PaxSearchItem
	 */
	public PaxItemBean searchFormToBean(final PaxSearchForm searchForm) {

		PaxItemBean returnItem = new PaxItemBean();
		
		returnItem.setFlightCode(searchForm.getFlightCode());
		returnItem.setCarrier(searchForm.getCarrier());
		returnItem.setFromDate(stringToDateConversion(searchForm.getFromDate()));
		returnItem.setToDate(stringToDateConversion(searchForm.getToDate()));
		
		return returnItem;
	}
	
	/**
	 * Method to capture request parameters and contruct PaxItemBean object
	 * @param request HttpServletRequest
	 * @return PaxItemBean
	 */
	public PaxItemBean requestToBean(final HttpServletRequest request) {
		
		final PaxItemBean paxItem = new PaxItemBean();
		paxItem.setSeries(request.getParameter("series"));
		paxItem.setCustomerCode(request.getParameter("customerCode"));
		paxItem.setSeriesDetailId(request.getParameter("seriesDetailId"));
		paxItem.setFlightCode(request.getParameter("flightCode"));
		paxItem.setAllocation(Integer.valueOf(request.getParameter("allocation")).intValue());
		paxItem.setNoOfSeats(Integer.valueOf(request.getParameter("noOfSeats")).intValue());
		paxItem.setSeatClass(request.getParameter("seatClass"));
		paxItem.setSupplierId(request.getParameter("supplierId"));
		paxItem.setRoute(request.getParameter("route"));
		paxItem.setAllocationType(request.getParameter("allocationType"));
		
		return paxItem;
	}
	
	/**
	 * 
	 * @param editResultsList List
	 * @return List
	 */
	public List setUpAllocationBeanList(final List editResultsList) {
		
		for (int listCounter = 0 ; listCounter < editResultsList.size(); listCounter++ ) { 
			final PaxItemBean paxItem = (PaxItemBean) editResultsList.get(listCounter);
			
			
			for (int allocCounter = 0 ; allocCounter < paxItem.getAllocationItems().size(); allocCounter++) {
				final PaxAllocationItemBean allocBean = (PaxAllocationItemBean) paxItem.getAllocationItems().get(allocCounter);
				
				if ( paxItem.getRoute() != null ) { 
					final String[] route = paxItem.getRoute().split("-");
					allocBean.setDepartureGateFrom(route[0]);
					allocBean.setDepartureGateTo(route[1]);
					allocBean.setAllocationType(paxItem.getAllocationType());
				}
				paxItem.getAllocationItems().set(allocCounter, allocBean);
			}
			// fudge as we need two items - need to check this with the PaxAllocationItemBean constructor
			if ( paxItem.getAllocationItems().size() < 2 && paxItem.getAllocationItems().size() > 0 ) {
				final PaxAllocationItemBean allocBean = (PaxAllocationItemBean) paxItem.getAllocationItems().get(0);
				PaxAllocationItemBean newAllocBean = new PaxAllocationItemBean();
				newAllocBean.setDepartureGateFrom(allocBean.getDepartureGateFrom());
				newAllocBean.setDepartureGateTo(allocBean.getDepartureGateTo());
				newAllocBean.setAllocationType(paxItem.getAllocationType());
				newAllocBean.setPaxStatus(2);
				// construct a new allocation list
				List newAllocList = new ArrayList();
				newAllocList.add(allocBean);
				newAllocList.add(newAllocBean);
				// add allocations to pax
				paxItem.setAllocationItems(newAllocList);
			}
			editResultsList.set(listCounter, paxItem);
		}
		
		return editResultsList;
	}
	
	
	/**
	 * dateToStringConversion, util method to convert
	 * a date object to a string
	 * @param date Date
	 * @return String
	 */
	public String dateToStringConversion(final Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String dateString = null;
		if ( date != null ) {
			dateString = simpleDateFormat.format(date);
		}
		return dateString;
	}
	
	/**
	 * stringToDateConversion, util method to convert a 
	 * String to a date
	 * @param dateString String
	 * @return Date
	 */
	public Date stringToDateConversion(final String dateString) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
		if ( dateString != null ) {
			try {
				date = simpleDateFormat.parse(dateString);
			} catch (ParseException e) {}
		}
		return date;
	}
	
	/**
	 * Method to convert java.util.List to an array of objects
	 * @param objectList List
	 * @return Object[]
	 */
	public PaxItemBean[] listToArray(final List objectList) {
		PaxItemBean[] objectArray = new PaxItemBean[objectList.size()];
		
		for (int listCounter = 0 ; listCounter < objectList.size(); listCounter++) {
			objectArray[listCounter] = (PaxItemBean) objectList.get(listCounter);
		}
		return objectArray;
	}
	
	
	/**
	 * 
	 * @param objectList List
	 * @return PaxAllocationItemBean[]
	 */
	public PaxAllocationItemBean[] allocationListToArray(final List objectList) {
		PaxAllocationItemBean[] objectArray = new PaxAllocationItemBean[objectList.size()];
		
		for (int listCounter = 0 ; listCounter < objectList.size(); listCounter++) {
			objectArray[listCounter] = (PaxAllocationItemBean) objectList.get(listCounter);
		}
		return objectArray;
	}
	
	public boolean checkSize(List list) {
		if ( list == null ) {
			return false;
		} else if (list.size() > 0 ) {
			return true;
		} else {
			return false;
		}
	}
}
