package uk.co.firstchoice.genericmaint.frontend;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.*;

public class ControllerServlet extends HttpServlet {

	ServletContext context;

	public void init(ServletConfig config) throws ServletException {
		context = config.getServletContext();
		super.init(config);
	}
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		doPost(request, response);
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		try {
			String forwardURL = "";
			String cmd = request.getParameter("cmd");
			HttpSession sess = request.getSession(true);
			boolean newSess = false;
			
			System.out.println("GenericMaint: >"+cmd+"< Usr: >"+request.getRemoteUser()+"< "+new Date());			
			
			if (null != request.getRequestedSessionId() && !request.isRequestedSessionIdValid())
				newSess = true;

			//HttpSession sess = request.getSession(true);
					
			
			
			if (sess.isNew() || newSess)
			   cmd = "";
			
			
			
			if (null == cmd || cmd.equals("")) {
				String appl = request.getParameter("appl");
				String table = request.getParameter("table");
				
				
				if (null != appl && null != table) {
					MaintRecord mr = new MaintRecord(appl, table); //lookup the specifics for this maint type
					if (mr.isValid()) {
						//if (request.isUserInRole(mr.getUserRoleForAccess())) {
						System.out.println("role" + mr.getUserRoleForAccess());
						
						if (request.isUserInRole(mr.getUserRoleForAccess().toUpperCase())) {
						    Vector dataVector = new DataHandler().getData(mr);
							sess.setAttribute("maintrecord", mr);
							request.setAttribute("datavector", dataVector);
							forwardURL = "listrecords.jsp";
						} else
							forwardURL = "closewindow.jsp?error="+
										java.net.URLEncoder.encode("Error - you do not have access to this application", "UTF-8");
					} else
						forwardURL = "closewindow.jsp?error="+
									java.net.URLEncoder.encode("Error - maintenance type ("+appl+","+table+") not found", "UTF-8");
				} else
						forwardURL = "closewindow.jsp?error="+
									java.net.URLEncoder.encode("Error - maintenance type not specified", "UTF-8");
			} else if (	cmd.equalsIgnoreCase("addrecord") || 
						cmd.equalsIgnoreCase("saverecord") ||
						cmd.equalsIgnoreCase("deleterecord")) {
				MaintRecord mr = (MaintRecord)sess.getAttribute("maintrecord");
				if (mr != null) {
					if (request.isUserInRole(mr.getUserRoleForAccess().toUpperCase())) {
						new DataHandler().saveRecord(	mr,
														request,
														cmd.equalsIgnoreCase("deleterecord")?"D":
														cmd.equalsIgnoreCase("saverecord")?"U":"A");
						Vector dataVector = new DataHandler().getData(mr);
						request.setAttribute("datavector", dataVector);
						forwardURL = "listrecords.jsp";
					} else
						forwardURL = "closewindow.jsp?error="+
									java.net.URLEncoder.encode("Error - you do not have access to this application", "UTF-8");
				} else
					forwardURL = "closewindow.jsp?error="+
								java.net.URLEncoder.encode("Error - session expired. Restart application.", "UTF-8");
			}
			if (!forwardURL.equals(""))
				request.getRequestDispatcher(forwardURL).forward(request, response);
		}
		catch (MaintException e) {
			PrintWriter out = response.getWriter();
			out.print("<html><head><title>Servlet Error</title></head><body bgcolor=#ffffff>");
			out.print("<font face=\"Helvetica\"><h2><font color=#FF0000>Server Error Page</font></h2>");
			out.print("<hr/>We encountered an error - please correct the problem and try again<p/>");
			out.print("Error message: <b>"+e.getMessage()+"</b></body></html>");
			log(e.getMessage(), e.getCause());
		}
		catch (Exception e) {
			System.out.println("Query: "+request.getQueryString());
			String paramName;
			for (Enumeration en = request.getParameterNames(); en.hasMoreElements() ;) {
				paramName = (String)en.nextElement();
				System.out.println(paramName+": >"+request.getParameter(paramName)+"<");
			}			
			e.printStackTrace();
			PrintWriter out = response.getWriter();
			out.print("<html><head><title>Servlet Error</title></head><body bgcolor=#ffffff>");
			out.print("<font face=\"Helvetica\"><h2><font color=#FF0000>Server Error Page</font></h2>");
			out.print("<hr/>Please report the following error by copy/pasting it to an e-mail to your helpdesk:<p/>");
			out.print("Error message: <b>"+e.getMessage()+"</b><p/>Stack trace:<br><b>");
			e.printStackTrace(out);
			out.print("</b><p/>Query string: <b>"+request.getQueryString()+"</b><p/><hr/></body></html>");

			log("Uncaught Exception in GenericMaint", e);
			
		}
	}
}
