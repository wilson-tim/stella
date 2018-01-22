package uk.co.firstchoice.viking.gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.co.firstchoice.fcutil.FCException;
import uk.co.firstchoice.viking.gui.util.ForwardConstants;
import uk.co.firstchoice.viking.persist.VikingJDBC;

public class FlightDetailsAction extends Action {

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
		ActionForward nextAction = mapping.findForward("showedit");
		FlightDetailsFormBean fdfb = (FlightDetailsFormBean) form;
		if (fdfb.getAction().equals(ForwardConstants.EMPTY)) {
			fdfb.setAction(ForwardConstants.EDIT_ACTION);
		}
		if (fdfb.getAction().equals(ForwardConstants.SAVE_ACTION)) {
			String rtn = vJdbc.insertSeries(
					Integer.parseInt(fdfb.getVersion()), fdfb.getStartDate(),
					fdfb.getEndDate(), fdfb.getWeekday(), fdfb.getFrequency(),
					fdfb.getSector1(), fdfb.getSector2(), fdfb.getSector3(),
					fdfb.getSector4(), fdfb.getSector5(), fdfb.getSector6(),
					fdfb.getSlot(), fdfb.getInitialCapacity(), fdfb
							.getAmendmentType(), fdfb.getComments(), request
							.getRemoteUser());
			try {
				if (Integer.parseInt(rtn) > 0) {
					request.setAttribute("seriesNo", rtn);
					nextAction = mapping
							.findForward(ForwardConstants.RELOAD_PARENT_ACTION);
				}
			} catch (NumberFormatException e) {
				fdfb.setError(rtn);
			}
		} else {
			HttpSession sess = request.getSession();
			if (sess.getAttribute(ForwardConstants.ALL_FLIGHTCODES) == null) {
				sess.setAttribute(ForwardConstants.ALL_FLIGHTCODES,
						buildFlightCodes(vJdbc));
			}
			if (sess.getAttribute(ForwardConstants.ALL_GATEWAYCODES) == null) {
				sess.setAttribute(ForwardConstants.ALL_GATEWAYCODES,
						buildGatewayCodes(vJdbc));
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
			vJdbc.closeResultSet(rset);
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