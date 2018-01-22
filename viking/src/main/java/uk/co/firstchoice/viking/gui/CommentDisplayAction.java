package uk.co.firstchoice.viking.gui;

import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.co.firstchoice.fcutil.FCUtils;
import uk.co.firstchoice.viking.gui.util.ForwardConstants;
import uk.co.firstchoice.viking.persist.VikingJDBC;

public class CommentDisplayAction extends Action {

	/**
	 * Process the specified HTTP request, and create the corresponding HTTP
	 * response (or forward to another web component that will create it).
	 * Return an <code>ActionForward</code> instance describing where and how
	 * control should be forwarded, or <code>null</code> if the response has
	 * already been completed.
	 * 
	 * @param form
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * @exception Exception
	 *                if the application business logic throws an exception
	 * @return
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		VikingJDBC vJdbc = new VikingJDBC();
		String seriesNo = request.getParameter("seriesNo");
		if ("add".equals(request.getParameter("action"))
				&& null != request.getParameter("amendmentType")
				&& null != request.getParameter("comments")) {
			vJdbc.insertComment(seriesNo, request.getRemoteUser(), "A", request
					.getParameter("comments"), request
					.getParameter("amendmentType"));
		}
		ResultSet rs = vJdbc.getAmendmentDetails(seriesNo);
		java.util.Collection commentsHolder = new java.util.Vector();
		while (rs.next()) {
			commentsHolder.add(new CommentHolderBean(FCUtils.dateToString(rs
					.getDate("AMENDED_DATE")), rs.getString("AMENDED_USER_ID"),
					rs.getString("DESCRIPTION"), rs.getString("VK_COMMENTS")));
		}
		request.setAttribute("seriescomments", commentsHolder);
		vJdbc.closeResultSet(rs);
		vJdbc.close();
		return mapping.findForward(ForwardConstants.SUCCESS);
	}

}