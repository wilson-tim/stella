/*
 * Created on 08-Mar-2005
 * File name: PaxSearchAction.java
 */
package uk.co.firstchoice.viking.pax.gui.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.co.firstchoice.common.gui.struts.util.ClonebleFormInterface;
import uk.co.firstchoice.viking.gui.util.ForwardConstants;
import uk.co.firstchoice.viking.pax.gui.utils.PaxUtil;
import uk.co.firstchoice.viking.pax.gui.utils.StringConstants;
import uk.co.firstchoice.viking.persist.VikingJDBC;

/**
 * @author Dwood
 * 
 * Main Action for Pax Search/Edit screen(s). Action detacts the type of
 * form/url and acts accordingly to request.
 */
public class PaxSearchAction extends Action {

	/**
	 * Process the specified HTTP request, and create the corresponding HTTP
	 * response (or forward to another web component that will create it).
	 * Return an <code>ActionForward</code> instance describing where and how
	 * control should be forwarded, or <code>null</code> if the response has
	 * already been completed.
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param form
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * 
	 * @exception Exception
	 *                if the application business logic throws an exception
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		VikingJDBC jdbc = new VikingJDBC();
		PaxUtil util = new PaxUtil();
		ClonebleFormInterface vForm = (ClonebleFormInterface) form;
		ActionForward forward = null;

		if (mapping.getParameter() == null) {
			throw new IllegalStateException();
		} else {
			if ("search".compareTo(mapping.getParameter()) == 0) {
				forward = doSearch(jdbc, util, mapping, vForm, request, response);
			} else if ("edit".compareTo(mapping.getParameter()) == 0) {
				forward = doEdit(jdbc, util, mapping, vForm, request, response);
			}
		}

		return forward;
	}

	/**
	 * @param jdbc
	 *            the jdbc accessor object for this operation
	 * @param util
	 *           pax util object used for action requests
	 * @param mapping
	 *            The VikingActionMapping used to select this instance
	 * @param form
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * 
	 * @exception Exception
	 *                if the application business logic throws an exception
	 * @return ActionForward
	 */
	protected ActionForward doSearch(VikingJDBC jdbc, PaxUtil util,
			ActionMapping mapping, ClonebleFormInterface form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = mapping.findForward(ForwardConstants.SUCCESS);

		PaxSearchForm paxForm = (PaxSearchForm) form;

		HttpSession session = request.getSession();

		// check to see if carrier list is in the user session
		if (session.getAttribute(StringConstants.CARRIER_LIST) == null) {
			List carrierList = jdbc.getCarrierList();
			session.setAttribute(StringConstants.CARRIER_LIST, carrierList);
		}

		if (session.getAttribute(StringConstants.TYPE_LIST) == null) {
			List flightTypeList = jdbc.getFlightTypeList();
			session.setAttribute(StringConstants.TYPE_LIST, flightTypeList);
		}
		
		// check form post state and perform search if required
		if (PaxSearchForm.POST_VALUE.compareTo(paxForm.getPostState()) == 0) {
			PaxItemBean paxItem = util.searchFormToBean(paxForm);
			List results = null;
			// check to see if we need to get report results or search
			if ( "report".compareTo(paxForm.getSearchType()) == 0  ) {
				results = jdbc.reportPax(paxItem, paxForm.getFlightType());
			} else {
				results = jdbc.searchPax(paxItem, paxForm.getFlightType());
			}
			// save results to session
			session.setAttribute(StringConstants.PAX_RESULTS, results);
		} else {
			// give form default date values
			Date date = new Date();
			String todaysDate = util.dateToStringConversion(date);
			paxForm.setFromDate(todaysDate);
			paxForm.setToDate(todaysDate);
		}

		return forward;
	}

	/**
	 * @param jdbc
	 *            the jdbc accessor objecvt for this operation
	 * @param util
	 *           pax util object used for action requests
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param form
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * 
	 * @exception Exception
	 *                if the application business logic throws an exception
	 * @return ActionForward
	 */
	protected ActionForward doEdit(VikingJDBC jdbc, PaxUtil util,
			ActionMapping mapping, ClonebleFormInterface form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = mapping.findForward(ForwardConstants.SUCCESS);

		PaxEditForm paxForm = (PaxEditForm) form;

		// check to see if form is viewed for first time
		if (PaxEditForm.POST_VALUE.compareTo(paxForm.getPostState()) != 0 ) {
			// form is in session, check to see if it has been resubmitted and reset if required.
			// this is to get around ArrayList out of bounds bug
			if ( paxForm.getFlightNumber() != null ) {
				//paxForm.reset(mapping, request);
			}
			// check that request paramters have been passed from results screen
			if (request.getParameter("series") != null
				&& request.getParameter("seriesDetailId") != null
				) {
				PaxItemBean paxItem = new PaxItemBean();
				paxItem = util.requestToBean(request);
				
				// load form for the first time and populate with search criteria
				List editResultsList = jdbc.getPaxDetails(paxItem);
				
				// add item to bean list so that users are always able to add new items
				editResultsList = util.setUpAllocationBeanList(editResultsList);
				
				// set flight number
				if ( editResultsList.size() > 0 ) {
					paxForm.setFlightNumber(((PaxItemBean) editResultsList.get(0)).getFlightNumber());
				}
				
				// set value to form
				paxForm.setPaxItemArray(editResultsList);
				
			}
		} else {

			List paxItems = paxForm.getPaxItemArray();
			String userId = request.getRemoteUser();
			
			// set save message - this will be changed 
			paxForm.setSaveStatus(true);
			
			// iterate results and save data
			final List newPaxList = new ArrayList();
			for ( int paxCounter = 0; paxCounter < paxItems.size(); paxCounter++ ) {
				PaxItemBean itemBean = (PaxItemBean) paxItems.get(paxCounter);
				if ( itemBean.getAllocationType() != null && ( "".compareTo(itemBean.getStatusCode()) != 0 ) ) { 
					itemBean = jdbc.savePaxDetails( itemBean, userId );
					newPaxList.add(itemBean);
				} else if ( itemBean.getAllocationType() != null && ( "".compareTo(itemBean.getStatusCode()) == 0 ) ) {
					 // add back into list to redisplay
					// but do not save.
					newPaxList.add(itemBean);					
				} else {
					paxItems.remove(paxCounter);
				}
			}
			paxForm.setPaxItemArray(newPaxList);
		}

		return forward;
	}

}