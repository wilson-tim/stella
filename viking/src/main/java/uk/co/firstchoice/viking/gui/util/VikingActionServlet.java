/*
 * VikingActionServlet.java
 *
 * Created on 23 September 2003, 13:52
 */

package uk.co.firstchoice.viking.gui.util;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionServlet;

import uk.co.firstchoice.fcutil.FCException;
import uk.co.firstchoice.viking.gui.SessionData;

/**
 * 
 * @author Lars Svensson
 */
public class VikingActionServlet extends ActionServlet {

	ServletContext context;

	private boolean debug;

	/**
	 * @param config
	 * @throws ServletException
	 */
	public void init(ServletConfig config) throws ServletException {

		context = config.getServletContext();
		super.init(config);
		String debugParam = config.getInitParameter("debug");
		debug = debugParam != null && debugParam.equalsIgnoreCase("true");
		System.out.println("Viking.init: Debug set to " + debug);

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		if (debug) {
			System.out.println("Viking.doGet User: " + request.getRemoteUser());
		}
		checkSessionData(request);
		super.doGet(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		if (debug) {
			System.out
					.println("Viking.doPost User: " + request.getRemoteUser());
		}
		checkSessionData(request);
		super.doPost(request, response);
	}

	private void checkSessionData(HttpServletRequest request) {
		HttpSession sess = request.getSession();
		if (sess.getAttribute(ForwardConstants.SESSION_DATA) == null) {
			try {
				sess.setAttribute(ForwardConstants.SESSION_DATA,
						new SessionData());
				if (debug) {
					System.out
							.println("Session re-created in VikingActionServlet");
				}
			} catch (FCException e) {
				System.out
						.println("Error re-creating Session in VikingActionServlet: "
								+ e.getMessage());
			}
		}
	}

}