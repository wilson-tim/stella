package uk.co.firstchoice.viking.gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.co.firstchoice.fcutil.FCException;
import uk.co.firstchoice.fcutil.FCUtils;
import uk.co.firstchoice.viking.gui.util.ForwardConstants;
import uk.co.firstchoice.viking.persist.VikingJDBC;

/**
 * Struts action defined in Struts to be called when search screen is opened or
 * submitted. The action performed will depend on the <B>action </B> form field.
 */
public class SearchAction extends Action {

	/**
	 * Process the specified HTTP request, and create the corresponding HTTP
	 * response (or forward to another web component that will create it).
	 * Return an <code>ActionForward</code> instance describing where and how
	 * control should be forwarded, or <code>null</code> if the response has
	 * already been completed.
	 * 
	 * @param form
	 *            The form bean matching the input fields on the form.
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * @exception Exception
	 *                if the application business logic throws an exception
	 * @return An ActionForward informing Struts where to go next
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		VikingJDBC vJdbc = new VikingJDBC();
		HttpSession sess = request.getSession();
		SearchFormBean sfb = (SearchFormBean) form;
		ActionForward nextAction = mapping
				.findForward(ForwardConstants.SUCCESS);
		AccountsContainer accounts = (AccountsContainer) sess
				.getAttribute(ForwardConstants.ALL_ACCOUNTS);
		if (accounts == null) {
			accounts = new AccountsContainer(vJdbc);
			sess.setAttribute(ForwardConstants.ALL_ACCOUNTS, accounts);
		}
		if (sess.getAttribute(ForwardConstants.ALL_FLIGHTCODES) == null) {
			sess.setAttribute(ForwardConstants.ALL_FLIGHTCODES,
					buildFlightCodes(vJdbc));
		}
		if (sess.getAttribute(ForwardConstants.ALL_GATEWAYCODES) == null) {
			sess.setAttribute(ForwardConstants.ALL_GATEWAYCODES,
					buildGatewayCodes(vJdbc));
		}
		if (sfb.getAction() != null && sfb.getAction().equals("search")) {
			Vector v = new Vector();
			try {
				if ((!(sfb.getVersion().equals(ForwardConstants.EMPTY)))
						|| (!(sfb.getSeriesNo().equals(ForwardConstants.EMPTY)))) {
					//       long start = System.currentTimeMillis();
					
		
					java.sql.ResultSet rs = vJdbc
							.getSearchResult(sfb.getSearchType(), (sfb
									.getSeriesNo().equals(
											ForwardConstants.EMPTY) ? 0 : Long
									.parseLong(sfb.getSeriesNo())), (sfb
									.getVersion()
									.equals(ForwardConstants.EMPTY) ? 0 : Long
									.parseLong(sfb.getVersion())), sfb
									.getFlightPre(), sfb.getFlightNo(), sfb
									.getStartDate(), sfb.getEndDate(), (sfb
									.getDepTime()
									.equals(ForwardConstants.EMPTY) ? "0" : sfb
									.getDepTime()), sfb.getDepTimeOper(), (sfb
									.getArrTime()
									.equals(ForwardConstants.EMPTY) ? "0" : sfb
									.getArrTime()), sfb.getArrTimeOper(), sfb
									.getDepAirport(), sfb.getArrAirport(), sfb
									.getAircraftType(), sfb.getWeekday(),
							//											"N",
									sfb.getCustomer(), sfb.getSupplier(), sfb
											.getBroker(), sfb.getSeatClass(),
									sfb.getContractId(), (sfb.getTotalPrice()
											.equals(ForwardConstants.EMPTY) ? 0
											: Long.parseLong(sfb
													.getTotalPrice())), sfb
											.getTotalPriceOper(), sfb
											.getSeatQtyType(),
									(sfb.getSeatQty().equals(
											ForwardConstants.EMPTY) ? 0 : Long
											.parseLong(sfb.getSeatQty())), sfb
											.getSeatQtyOper(), sfb
											.getStatusStr(), sfb
											.getGeminiCode(), sfb
											.isIncludeAllocation(),
											sfb.getFlightType(),
											sfb.getMealType()    );
					//         long sqlTime = System.currentTimeMillis()-start;
					String[] sa = new String[0];
					boolean showAmount = !request
							.isUserInRole("VIKING_EXTERNAL_READER") 
							&& !request.isUserInRole("VIKING_RESTRICTED_EXTERNAL_READER")
							&& !request.isUserInRole("VIKING_OPERATIONS");
					//         long nextTime = 0;
					//         long notNextTime = 0;
					//         long otherTime;
					//         start = System.currentTimeMillis();
					while (rs.next()) {
						//         otherTime = System.currentTimeMillis();
						//         nextTime += (otherTime-start);
						if (sfb.getSearchType().equals(
								SearchFormBean.SERIES_SEARCH_TYPE)) {
							// used to be fixed 10 column customer list, now
							// concatenated into one string:
							// NAME1;2;5~NAME2;10;50
							ArrayList al = new ArrayList();
							try {
								//                                System.out.println(FCUtils.notNull(rs.getString("customer_min_max")));
								String[] s = FCUtils.notNull(
										rs.getString("customer_min_max"))
										.split("~");
								for (int x = 0; x < s.length; x++) {
									al.add(s[x]);
								}
							} catch (Exception e) {
								System.out.println(e.getMessage());
							} // Just don't add any if format error

							/*
							 * for (int i=1; i <=10; i++) { String s =
							 * FCUtils.notNull(rs.getString("customer"+i+"_min_max"));
							 * if (!s.equals("")) { al.add(s); } else { break; } }
							 */
							//System.out.println(" flightnumber" + rs.getString("summ_flight_number"));
							v.add(new SeriesSearchResult(rs
									.getString("series_id"), rs
									.getString("day_of_week")
									+ rs.getString("day_of_week_cnt"), rs
									.getString("min_departure_date"), rs
									.getString("max_departure_date"), rs
									.getString("departure_time"), rs
									.getString("summ_flight_number"), rs
									.getString("supplier"), rs									
									.getString("carrier"), rs
									.getString("all_gateway"), rs
									.getString("purchase_min_max"), rs
									.getString("avail_min_max"), (String[]) al
									.toArray(sa)));
						} else if (sfb.getSearchType().equals(
								SearchFormBean.CUSTOMER_SEARCH_TYPE)) {
											v.add(new AllocationSearchResult(
											rs.getString("series_id"), 
											rs.getString("gateway_code_from"), 
											rs.getString("gateway_code_to"), 
											rs.getString("min_departure_date"),
											rs.getString("max_departure_date"),
											rs.getString("departure_time"), 
											rs.getString("arrival_time"),
											rs.getString("frequency_interval"),
											rs.getString("flight_number"),
											//								"carrier",
											// //rs.getString("carrier"),
											rs.getString("allocation"), 
											rs.getString("seat_class"),
											showAmount ? rs
													.getString("ccy_price")
													: "", rs
													.getString("broker_code"),
											rs.getString("gemini_code"),
											rs.getString("day_of_week")));
						}
						//              start = System.currentTimeMillis();
						//              notNextTime += (start-otherTime);
					}
					rs.close();
					//             System.out.println("end loop: "+new java.util.Date());
					//             System.out.println("Search complete. getResultset:
					// "+(sqlTime)+
					//                                ", getNext: "+(nextTime)+
					//                                ", other: "+(notNextTime)
					//                               );
					sfb.setSearchResult(v);
				}
			} catch (SQLException e) {
				throw new FCException("SQL Error searching for series data.", e);
			}
		}

		vJdbc.close();
		return nextAction;

	}

	private Map buildFlightCodes(VikingJDBC vJdbc) throws FCException {
		Map hm = new HashMap();
		try {
			ResultSet rset = vJdbc.getAllAirlineCodes();
			while (rset.next()) {
				String fc = rset.getString("flight_code");
				if (fc != null) {
					fc = fc.trim() + "   ";
					hm.put(fc.substring(0, 3), rset.getString("customer_name"));
				}
			}
		} catch (SQLException e) {
			throw new FCException("SQL error getting flightcodes", e);
		}
		return hm;
	}

	private Map buildGatewayCodes(VikingJDBC vJdbc) throws FCException {
		Map hm = new HashMap();
		try {
			ResultSet rset = vJdbc.getAllGatewayCodes();
			while (rset.next()) {
				hm.put(rset.getString("gateway_code"), rset
						.getString("gateway_desc"));
			}
			vJdbc.closeResultSet(rset);
		} catch (SQLException e) {
			throw new FCException("SQL error getting gatewaycodes", e);
		}
		return hm;
	}

}