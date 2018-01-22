package uk.co.firstchoice.viking.gui;

//import java.sql.*;
//import javax.sql.*;
//import javax.naming.*;
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

public class FlightBotAction extends Action {

	private static final char COMMA = ',';

	private static final char SPEECH = '\'';

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

		FlightBotFormBean fbb = (FlightBotFormBean) form; //new
		// FlightBotBean();
		VikingJDBC vJdbc = new VikingJDBC();
		String seriesNo = request.getParameter("seriesNo");
		if (seriesNo != null) {
			fbb.setSeriesNo(seriesNo);
			getDepartureInfo(fbb, vJdbc);
			getAllocation(fbb, vJdbc, !request.isUserInRole("VIKING_EXTERNAL_READER") &&   !request.isUserInRole("VIKING_RESTRICTED_EXTERNAL_READER")
					&& !request.isUserInRole("VIKING_OPERATIONS"));
		}
		vJdbc.close();
		request.setAttribute("flightbotbean", fbb);
		return (mapping.findForward(ForwardConstants.SUCCESS));
	}

	private void getDepartureInfo(FlightBotFormBean fbb, VikingJDBC vJdbc)
			throws FCException {
		try {
			java.sql.ResultSet rset = vJdbc.getSeries(Long.parseLong(fbb
					.getSeriesNo()));
			if (rset.next()) {
				fbb.setLiveVersion("Y".equals(rset
						.getString("live_version_ind")));
			}
			rset.close();
			rset = vJdbc.getSeriesDetailDepDate(Long.parseLong(fbb
					.getSeriesNo()));
			StringBuffer strBuf = new StringBuffer();
			boolean firstDate = true;
			while (rset.next()) {
				if (!firstDate) {
					strBuf.append(COMMA);
				}
				strBuf.append(SPEECH + rset.getString("dep_date") + SPEECH);
				firstDate = false;
			}
			fbb.setDepartureDates(strBuf.toString());
			vJdbc.closeResultSet(rset);
		} catch (Exception e) {
			e.printStackTrace();
			throw new FCException("Error building list of departuredates.", e);
		}
	}

	private void getAllocation(FlightBotFormBean fbb, VikingJDBC vJdbc,
			boolean showAmounts) throws FCException {
		try {
			java.sql.ResultSet rset = vJdbc.getAllocation(Long.parseLong(fbb
					.getSeriesNo()));
			java.util.Vector vCust = new java.util.Vector();
			java.util.Vector vSupp = new java.util.Vector();
			while (rset.next()) {
				AllocationBean ab = new AllocationBean(FCUtils.notNull(rset
						.getString("colour_id")), FCUtils.notNull(rset
						.getString("customer_code")), FCUtils.dateToString(rset
						.getDate("min_departure_date")), FCUtils
						.dateToString(rset.getDate("max_departure_date")),
						(showAmounts ? FCUtils.notNull(rset
								.getString("split_currency")) : ""), FCUtils
								.notNull(rset.getString("gateway_code_from")),
						FCUtils.notNull(rset.getString("gateway_code_to")),
						FCUtils.notNull(rset.getString("seat_class")), FCUtils
								.notNull(rset
										.getString("seat_class_description")),
						FCUtils.notNull(rset.getString("no_of_seat")), FCUtils
								.notNull(rset.getString("contract_id")),
						FCUtils.notNull(rset.getString("gemini_code")),
						FCUtils.notNull(rset.getString("meal_type_code")),
						FCUtils.notNull(rset.getString("broker_code")),
						FCUtils.notNull(rset
								.getString("allocation_status_code")));
				if (rset.getString("allocation_type") != null
						&& rset.getString("allocation_type").equals("S")) {
					vCust.add(ab);
				} else {
					vSupp.add(ab);
				}
			}
			fbb.setCustomerAllocation(vCust);
			fbb.setSupplierAllocation(vSupp);
			vJdbc.closeResultSet(rset);
		} catch (Exception e) {
			e.printStackTrace();
			throw new FCException("Error building list of departuredates.", e);
		}
	}

}