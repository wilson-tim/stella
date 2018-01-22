package uk.co.firstchoice.viking.gui;

import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.co.firstchoice.fcutil.FCException;
import uk.co.firstchoice.fcutil.FCUtils;
import uk.co.firstchoice.viking.gui.util.ForwardConstants;
import uk.co.firstchoice.viking.persist.VikingJDBC;

public class FlightOpAction extends Action {

	/**
	 * Process the specified HTTP request, and create the corresponding HTTP
	 * response (or forward to another web component that will create it).
	 * Return an <code>ActionForward</code> instance describing where and how
	 * control should be forwarded, or <code>null</code> if the response has
	 * already been completed.
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param actionForm
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * 
	 * @exception Exception
	 *                if the application business logic throws an exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		VikingJDBC vJdbc = new VikingJDBC();
		FlightOpFormBean fofb = (FlightOpFormBean) form;
		ActionForward nextAction = mapping
				.findForward(ForwardConstants.SUCCESS);

		String action = fofb.getAction();
		if (action.equalsIgnoreCase(ForwardConstants.ADD_ACTION)
				|| action.equalsIgnoreCase(ForwardConstants.EDIT_ACTION)) {
			/**
			 * ADD_ACTION: When user clicks add or edit we first find existing
			 * ones to display
			 *  
			 */
			loadSeries(vJdbc, fofb, request);
		} else if (action.equalsIgnoreCase(ForwardConstants.SAVE_ACTION)) {
			/*
			 * SAVE_ACTION: User is saving an addition or edit. First we need to
			 * delete any sectors removed, then we update any changed sectors
			 * and finally we add any new sectors. Updates need to be done....
			 * @ToDo
			 */
			try {
				vJdbc.startTransaction();
				/* delete the sectors that was removed */

				if (!fofb.getDeletedSequences().equals(ForwardConstants.EMPTY)
						&& !fofb.getStartAction().equalsIgnoreCase(
								ForwardConstants.ADD_ACTION)) { //don't delete
					// when adding
					String[] deletedSeq = fofb.getDeletedSequences().split(",");
					for (int i = 0; i < deletedSeq.length; i++) {
						if (!deletedSeq[i].equals(ForwardConstants.EMPTY)) {
							String error = vJdbc.deleteSeriesDetail(Long
									.parseLong(fofb.getSeriesNo()), fofb
									.getFromDate(), fofb.getToDate(), Integer
									.parseInt(deletedSeq[i]), request
									.getRemoteUser());
							if (error != null && !error.equals("")) {
								System.out.println("Delete error :" + error);
								fofb.setError("Sector No: " + i + ": " + error);
								vJdbc.abortTransaction();
								vJdbc.close();
								return nextAction;
							}
						}
					}
				}
				// update from bottom
				String depdate = null;
				int start, end, increment;
				boolean movingUp;
				for (int direction = 0; direction < 2; direction++) {
					//boolean movingUp = fofb.getDepAirport().length >
					// fofb.getOriginalNumSectors();
					movingUp = ((direction == 0) ? true : false);
					start = (movingUp ? fofb.getDepAirport().length - 1 : 0);
					end = (movingUp ? -1 : fofb.getDepAirport().length);
					increment = (movingUp ? -1 : 1);
				   System.out.println("Direction " + direction );
				   System.out.println("Moving "+(movingUp?"up":"down"));
					for (int i = start; i != end; i += increment) {
						if (fofb.isRowChanged(i)) { // only bother with rows
							// that have actually
							// changed
							int oldSeq = fofb.getStartAction()
									.equalsIgnoreCase(
											ForwardConstants.ADD_ACTION)
									|| fofb.getSequenceNo(i).equals(
											ForwardConstants.EMPTY) ? 0
									: Integer.parseInt(fofb.getSequenceNo(i));
							
							
							if ((oldSeq > 0)&& (((direction == 0) && (oldSeq < i + 1)) || ((direction == 1) && (oldSeq >= i + 1)))) {

				/*System.out.print("insert statement  ===== " + 
						fofb.getSeriesNo()+ '/'
						fofb.getFromDate()+ '/'
						fofb.isGroup() ? fofb.getToDate() : fofb.getFromDate()+ '/'
						fofb.isGroup() ? null : fofb.getToDate()+ '/'
						oldSeq+ '/'
						fofb.isGwChanged(i) ? "Y" : "N"+ '/'
								*/
				
								String error = vJdbc
										.insertUpdateSeriesDetail(
												Integer.parseInt(fofb
														.getSeriesNo()),
												// if Group update then from and
												// to dates are as selected and
												// depDate is null. If not Group
												// (individual date) update then
												// from and to are original date
												// and depDate is new date.
												fofb.getFromDate(), //from
												fofb.isGroup() ? fofb
														.getToDate() : fofb
														.getFromDate(), //to
												fofb.isGroup() ? null : fofb
														.getToDate(), //depDate
												oldSeq,
												fofb.isGwChanged(i) ? "Y" : "N", //gateway
												// changed
												// is Y
												// then
												// backend
												// will
												// delete
												// the
												// allocationrecord
												i + 1,
												fofb.getDepAirport(i),
												fofb.getArrAirport(i),
												fofb.getDepSlot(i),
												fofb.getDepTime(i),
												fofb.getArrTime(i),
												fofb.getFlightPre(i),
												fofb.getFlightNo(i),
												fofb.getAircraft(i),
												fofb.getFlightType(i),
												fofb.getDayOffset(i),
												fofb.isCancelledFlight() ? ForwardConstants.RECORD_CANCEL
														: ForwardConstants.RECORD_EDIT,
												request.getRemoteUser());
								//System.out.println("i: "+i+" typ:
								// "+(i==(fofb.getDepAirport().length-1)?fofb.getAmendmentType():ForwardConstants.EMPTY)+"
								// com: "+fofb.getComments());
								if (error != null
										&& !error
												.equals(ForwardConstants.EMPTY)) {
									System.out.println("error :" + error);
									fofb.setError("Sector No: " + i + ": "
											+ error);
									vJdbc.abortTransaction();
									vJdbc.close();
									return nextAction;
								}
							}
						}
					}
				}
				// insert from top
				for (int i = 0; i < fofb.getDepAirport().length; i++) {
					int oldSeq = fofb.getStartAction().equalsIgnoreCase(
							ForwardConstants.ADD_ACTION)
							|| fofb.getSequenceNo(i).equals(
									ForwardConstants.EMPTY) ? 0 : Integer
							.parseInt(fofb.getSequenceNo(i));

	 
					
					if (oldSeq <= 0) {

						String error = vJdbc.insertUpdateSeriesDetail(Integer
								.parseInt(fofb.getSeriesNo()),
						// if Group insert then from and to dates are as
								// selected and depDate is null. If not Group
								// (individual date) insert then from and to are
								// null and depDate is new date.
								fofb.isGroup() ? fofb.getFromDate() : null, //from
								fofb.isGroup() ? fofb.getToDate() : null, //to
								fofb.isGroup() ? null : fofb.getToDate(), //depDate
								oldSeq, "N", //gateway changed is Y then
								// backend will delete the
								// allocationrecord
								i + 1, fofb.getDepAirport(i), fofb
										.getArrAirport(i), fofb.getDepSlot(i),
								fofb.getDepTime(i), fofb.getArrTime(i), fofb
										.getFlightPre(i), fofb.getFlightNo(i),
								fofb.getAircraft(i), fofb.getFlightType(i),fofb.getDayOffset(i),
								ForwardConstants.RECORD_ADD, request
										.getRemoteUser());
						if (error != null
								&& !error.equals(ForwardConstants.EMPTY)) {
							System.out.println("error :" + error);
							fofb.setError("Sector No: " + i + ": " + error);
							vJdbc.abortTransaction();
							vJdbc.close();
							return nextAction;
						}
					}
				}
				String error = vJdbc.insertComment(fofb.getSeriesNo(), request
						.getRemoteUser(), "D", fofb.getComments(), fofb
						.getAmendmentType());
				if (error != null && !error.equals(ForwardConstants.EMPTY)) {
					System.out.println("error :" + error);
					fofb.setError("Error adding comment: " + error);
					vJdbc.abortTransaction();
					vJdbc.close();
					return nextAction;
				}
				vJdbc.commitTransaction();
				nextAction = mapping
						.findForward(ForwardConstants.RELOAD_PARENT_ACTION);
			} catch (FCException e) {
				e.printStackTrace();
				try {
					vJdbc.abortTransaction();
				} catch (FCException ex) {
					throw new Exception("SQL Error inserting Operation Detail",
							e);
				}
				fofb.setError(e.toString());
				vJdbc.close();
				return nextAction;
			}
		} else if (action.equalsIgnoreCase(ForwardConstants.DELETE_ACTION)) {
			try {
				String error = vJdbc.deleteCancelSeriesDetail(Long
						.parseLong(fofb.getSeriesNo()), fofb.getFromDate(),
						fofb.getFromDate(), ForwardConstants.RECORD_DELETE,
						request.getRemoteUser());
				if (error != null && !error.equals(ForwardConstants.EMPTY)) {
					System.out.println("error :" + error);
					fofb.setError("Error deleting date: " + error);
					request.setAttribute("error", "Error deleting date: "
							+ error);
					vJdbc.close();
					//					return nextAction;
				} else {
					vJdbc.insertComment(fofb.getSeriesNo(), request
							.getRemoteUser(), "D", fofb.getComments(), fofb
							.getAmendmentType());
					loadSeries(vJdbc, fofb, request);
				}
			} catch (FCException e) {
				e.printStackTrace();
			}
			nextAction = mapping
					.findForward(ForwardConstants.RELOAD_PARENT_ACTION);
		} else if (action
				.equalsIgnoreCase(ForwardConstants.DELETE_SERIES_ACTION)) {
			try {
				String error = vJdbc.deleteSeries(Long.parseLong(fofb
						.getSeriesNo()), request.getRemoteUser());
				if (error != null && !error.equals(ForwardConstants.EMPTY)) {
					System.out.println("error :" + error);
					fofb.setError("Error deleting series: " + error);
					request.setAttribute("error", "Error deleting series: "
							+ error);
					vJdbc.close();
				} else {
					vJdbc.insertComment(fofb.getSeriesNo(), request
							.getRemoteUser(), "D", fofb.getComments(), fofb
							.getAmendmentType());
					request.setAttribute("doClose", "true");
				}
			} catch (FCException e) {
				e.printStackTrace();
			}
			nextAction = mapping
					.findForward(ForwardConstants.RELOAD_PARENT_ACTION);
		}
		vJdbc.close();
		return nextAction;
	}

	private void loadSeries(VikingJDBC vJdbc, FlightOpFormBean fofb,
			HttpServletRequest request) {
		try {
			String season = fofb.getSeason();
			String seasonRec = null;
			java.util.Collection allSeasons = ((SessionData) request
					.getSession().getAttribute("sessiondata")).getSeasons();
			for (java.util.Iterator i = allSeasons.iterator(); i.hasNext();) {
				seasonRec = (String) i.next();
				if (seasonRec.startsWith(season)) {
					fofb.setSeasonStart(seasonRec.split(";")[1]);
					fofb.setSeasonEnd(seasonRec.split(";")[2]);
					break;
				}
			}
			ResultSet rset;
			if (fofb.getToDate().equals(ForwardConstants.EMPTY)) {
				rset = vJdbc.getSeriesDetail(
						Long.parseLong(fofb.getSeriesNo()), fofb.getFromDate());
			} else {
				rset = vJdbc.getSeriesDetail(
						Long.parseLong(fofb.getSeriesNo()), fofb.getFromDate(),
						fofb.getToDate());
			}
			ArrayList sequenceNo = new ArrayList();
			ArrayList depAirport = new ArrayList();
			ArrayList arrAirport = new ArrayList();
			ArrayList depTime = new ArrayList();
			ArrayList arrTime = new ArrayList();
			ArrayList dayOffset = new ArrayList();
			ArrayList flightPre = new ArrayList();
			ArrayList flightNo = new ArrayList();
			ArrayList aircraft = new ArrayList();
			ArrayList flightType = new ArrayList();
			//			ArrayList numAlloc = new ArrayList();
			while (rset.next()) {
				if (sequenceNo.size() == 0) {
					fofb.setCancelledFlight(rset.getString("record_status")
							.equals(ForwardConstants.RECORD_CANCEL));
				}
				sequenceNo.add(FCUtils.notNull(rset.getString("sequence_no")));
				depAirport.add(FCUtils.notNull(rset
						.getString("gateway_code_from")));
				arrAirport.add(FCUtils.notNull(rset
						.getString("gateway_code_to")));
				if (depTime.size() == 0
						&& FCUtils.notNull(rset.getString("departure_time"))
								.equals(ForwardConstants.EMPTY)) {
					depTime.add(FCUtils.notNull(rset
							.getString("departure_slot")));
				} else {
					depTime.add(FCUtils.notNull(rset
							.getString("departure_time")));
				}
				arrTime.add(FCUtils.notNull(rset.getString("arrival_time")));
				dayOffset.add(FCUtils.notNull(rset
						.getString("overnight_period")));
				flightPre.add(FCUtils.notNull(rset.getString("flight_code")));
				flightNo.add(FCUtils.notNull(rset.getString("flight_number")));
				aircraft.add(FCUtils.notNull(rset.getString("aircraft_type")));
				
				flightType.add(FCUtils.notNull(rset.getString("flight_type_code")));
				
				//				numAlloc.add(new Integer(0
				// /*rset.getInt("no_of_allocation")*/));
			}
			String[] a = new String[0];
			fofb.setSequenceNo((String[]) sequenceNo.toArray(a));
			fofb.setDepAirport((String[]) depAirport.toArray(a));
			fofb.setArrAirport((String[]) arrAirport.toArray(a));
			fofb.setDepTime((String[]) depTime.toArray(a));
			fofb.setArrTime((String[]) arrTime.toArray(a));
			fofb.setDayOffset((String[]) dayOffset.toArray(a));
			fofb.setFlightPre((String[]) flightPre.toArray(a));
			fofb.setFlightNo((String[]) flightNo.toArray(a));
			fofb.setAircraft((String[]) aircraft.toArray(a));
			fofb.setFlightType((String[]) flightType.toArray(a));
			
			
			//			fofb.setNumberOfAllocations((Integer[])numAlloc.toArray(new
			// Integer[0]));
			fofb.setOriginalNumSectors(fofb.getSequenceNo().length);
		} catch (Exception e) {
			e.printStackTrace();
			fofb.setError("SQL error retrieving sector details. ("
					+ e.getMessage() + ")");
		}
	}

}