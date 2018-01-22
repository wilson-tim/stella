package uk.co.firstchoice.viking.gui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.co.firstchoice.viking.gui.util.ForwardConstants;

public class MenuAction extends Action {

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

		String action = request.getParameter("action");
		HttpSession sess = request.getSession();
		String path = mapping.getPath();
		
		if (path.indexOf("logoff") > -1) {
			sess.invalidate();
			mapping.getPath();
			//return (mapping.findForward("menu"));
			return (mapping.findForward(ForwardConstants.SUCCESS));
		} else {
			sess.removeAttribute(ForwardConstants.ALL_ACCOUNTS);
			sess.removeAttribute(ForwardConstants.ALL_FLIGHTCODES);
			sess.removeAttribute(ForwardConstants.ALL_GATEWAYCODES);
			sess.removeAttribute("searchform");
			SessionData sessionData = (SessionData) sess.getAttribute(ForwardConstants.SESSION_DATA);
			if (sessionData == null) {
				sessionData = new SessionData();
				sess.setAttribute(ForwardConstants.SESSION_DATA, sessionData);
			}
			sess.setAttribute("managerIndicator", new Boolean(request
					.isUserInRole(ForwardConstants.VIKING_MANAGER)));
		
			return (mapping.findForward(ForwardConstants.SUCCESS));
		}
	}
}