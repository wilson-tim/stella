package uk.co.firstchoice.viking.gui;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

/**
 * Struts action defined in Struts to be called when search screen is opened or
 * submitted. The action performed will depend on the <B>action </B> form field.
 */
public class OperationsAction extends Action {

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

		OperationsFormBean ofb = (OperationsFormBean) form;
		if ("savetimings".equals(ofb.getAction())) {
			saveTimings(ofb);
		} else if ("savecomments".equals(ofb.getAction())) {
			saveComments(ofb);
		} else if ("saveestimateoractual".equals(ofb.getAction())){
			saveEstimateOrActual(ofb);	
		}
		ActionForward nextAction = mapping
				.findForward(ForwardConstants.SUCCESS);
		findOperations(ofb);
		return nextAction;

	}

	private void saveTimings(OperationsFormBean ofb) throws FCException {

		try {
			VikingJDBC vJdbc = new VikingJDBC();
			ofb.setError(vJdbc.operationsUpdate(ofb.getSeriesId(), ofb
					.getSeriesDetailId(), ofb.getActualDep(), ofb
					.getActualArr(), ofb.getDepOvernight(), ofb
					.getArrOvernight(), ofb.getEstimateOrActual()
					));
			vJdbc.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new FCException("Error saving operations details.", e);
		}
	}

	private void saveComments(OperationsFormBean ofb) throws FCException {

		try {
			VikingJDBC vJdbc = new VikingJDBC();
			ofb.setError(vJdbc.operationsCommentsUpdate(ofb.getSeriesId(), ofb
					.getDepartureDate(), ofb.getComments()));
			vJdbc.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new FCException("Error saving operations details.", e);
		}
	}

	
	private void saveEstimateOrActual(OperationsFormBean ofb) throws FCException {

		try {
			VikingJDBC vJdbc = new VikingJDBC();
			ofb.setError(vJdbc.operationsEstimateOrActualUpdate(ofb.getSeriesId(),  ofb.getAllSeriesDetailId() , ofb.getEstimateOrActual()));
			vJdbc.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new FCException("Error saving operations details.", e);
		}
	}

	private void findOperations(OperationsFormBean ofb) throws FCException {

		try {
			VikingJDBC vJdbc = new VikingJDBC();
			List seriesIds = new ArrayList();
			Map series = new HashMap(); 
			int maxSectors = 0;
			ResultSet rset = vJdbc.getOperationsDetails(ofb.getStartDate(), ofb
					.getEndDate(), ofb.getFromTime(), ofb.getExcludeCarrier(),
					ofb.isIncExcCarrierInd(), ofb.getGatewayCode(), ofb
							.isOnlyIncomplete());
			OperationsSeriesBean osb;
			while (rset.next()) {
				String depDate = FCUtils.dateToString(rset
						.getDate("departure_date"));
				String seriesIdKey = rset.getString("series_id") + "~"
						+ depDate;
				if (series.containsKey(seriesIdKey)) {
					osb = (OperationsSeriesBean) series.get(seriesIdKey);
				} else {
					osb = new OperationsSeriesBean();
					series.put(seriesIdKey, osb);
					seriesIds.add(seriesIdKey);
					osb.setSeriesNo(rset.getString("series_id"));
					osb.setDepartureDate(depDate);
					osb.setRoute(rset.getString("routing_summary"));
					osb.setSupplier(FCUtils.notNull(rset
							.getString("flight_supplier")));
					osb.setSharers(FCUtils.notNull(rset
							.getString("concat_customers")));
					osb.setComments(FCUtils.notNull(rset
							.getString("ops_comment_text")));
				}
				int adop = rset.getInt("actual_dep_overnight_period");
				int aaop = rset.getInt("actual_arr_overnight_period");
			
				osb.getSectors().add(
						new OperationsSectorBean(rset.getString("flight_code")
								+ rset.getString("flight_number"), 
								rset.getString("planned_departure_time"), 
								FCUtils.notNull(rset.getString("actual_departure_time")),
								rset.getString("planned_arrival_time"),
								FCUtils.notNull(rset.getString("actual_arrival_time")),
								(adop < 0 ? ("" + adop) : adop > 0 ? ("+" + adop) : ""),
								(aaop < 0 ? ("" + aaop)	: aaop > 0 ? ("+" + aaop) : ""),
								//                                        FCUtils.notNull(rset.getString("actual_dep_overnight_period")),
								//                                        FCUtils.notNull(rset.getString("actual_arr_overnight_period")),
								rset.getString("series_detail_id"),
								rset.getString("estimate_or_actual") 
								));
				if (osb.getSectors().size() > maxSectors) {
					maxSectors = osb.getSectors().size();
				}
			}
			ofb.setSeries(series);
			ofb.setSeriesIds((String[]) seriesIds.toArray(new String[seriesIds
					.size()]));
			ofb.setMaxSectors(maxSectors);
			vJdbc.closeResultSet(rset);
			vJdbc.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new FCException("Error retrieving operations details.", e);
		}
	}

}