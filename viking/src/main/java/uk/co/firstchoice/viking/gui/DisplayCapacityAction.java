package uk.co.firstchoice.viking.gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

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

public class DisplayCapacityAction extends Action {

	/**
	 * Process the specified HTTP request, and create the corresponding HTTP
	 * response (or forward to another web component that will create it).
	 * Return an <code>ActionForward</code> instance describing where and how
	 * control should be forwarded, or <code>null</code> if the response has
	 * already been completed.
	 * 
	 * @param form
	 *            The bean/form used to communicate with the frontend with.
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * @exception Exception
	 *                if the application business logic throws an exception
	 * @return A Struts ActionForward indicating where to go next
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DisplayCapacityFormBean dcfb = (DisplayCapacityFormBean) form;
		VikingJDBC vJdbc = new VikingJDBC();
		String seriesNo = request.getParameter("seriesNo");
		char type = dcfb.getType().equals("Purchased") ? 'P' : dcfb.getType()
				.equals("Available") ? 'A' : 'S';
		ResultSet rset = vJdbc.getCapacityDetails(seriesNo, type, dcfb
				.getType());
		LinkedHashMap capacityMap = new LinkedHashMap();
		LinkedHashMap sectorMap = null;
		String currentDate = "";
		try {
			while (rset.next()) {
				String date = FCUtils.dateToString(rset
						.getDate("departure_date"));
				if (!currentDate.equals(date)) {
					currentDate = date;
					sectorMap = new LinkedHashMap();
					capacityMap.put(date, sectorMap);
				}
				sectorMap.put(rset.getString("sector"), rset
						.getString("sum_seats"));
			}
		} catch (SQLException e) {
			throw new FCException(
					"SQL error building Display Capacity details", e);
		} finally {
			vJdbc.closeResultSet(rset);
			vJdbc.close();
		}
		dcfb.setCapacityDetails(capacityMap);
		return mapping.findForward(ForwardConstants.SUCCESS);
	}

}