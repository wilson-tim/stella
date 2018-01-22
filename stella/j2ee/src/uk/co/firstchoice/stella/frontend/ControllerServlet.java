package uk.co.firstchoice.stella.frontend;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ControllerServlet extends HttpServlet {

	ServletContext context;

	public void init(ServletConfig config) throws ServletException {
		try {
			context = config.getServletContext();
			super.init(config);
			context.setAttribute("contextdata", new ContextData());
			
		} catch (StellaException e) {
			log(e.getMessage(), e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			System.out.println(" inside controller servlet ");
			String forwardURL = "jsp/menu.jsp";
			String cmd = request.getParameter("cmd");
			
			System.out.println("forwardURL and cmd" +   forwardURL + "---" + cmd);

			System.out.println("--- Stella: >" + cmd + "< Usr: >"
					+ request.getRemoteUser() + "< " + new Date());
			boolean newSess = false;
			
			if (null != request.getRequestedSessionId()
					&& !request.isRequestedSessionIdValid()) {
System.out.println(cmd + "------ in new session");				
				newSess = true;
			}
		//	System.out.println(cmd + "------ at 56");
			HttpSession sess = request.getSession(true);
		//	System.out.println(cmd + "------ at 58");
			if (null == sess.getAttribute("sessiondata")){
				//System.out.println(cmd + "------ at 60");
				
				
				sess.setAttribute("sessiondata", new SessionData(request.getRemoteUser()));
						
			//System.out.println(cmd + "------ at 61");
			}
			//System.out.println(cmd + "------ at 63");
				
			if (sess.isNew() || newSess) {
			//	System.out.println(cmd + "------ at 62");	
				forwardURL = "jsp/menu.jsp" + (newSess ? "?newsess=true" : "");
			} else {
				if (null == cmd || cmd.equals("")	|| cmd.equalsIgnoreCase("menu")) {
					System.out.println(cmd + "------ at 63");	
					forwardURL = "jsp/menu.jsp";
				} else if (cmd.equalsIgnoreCase("createticket")) {
					System.out.println(cmd + "------ at 66");	
					forwardURL = "jsp/editticket.jsp";
				} else if (cmd.equalsIgnoreCase("saveticket")) {
					TicketHandler th = new TicketHandler();
					TicketBean ticket = (TicketBean) request
							.getAttribute("ticket");
					String saveType = StellaUtils.notNull(request
							.getParameter("savetype"));
					String lastSaveType = StellaUtils.notNull(request
							.getParameter("lastSaveType"));
					ticket = th.createNewTicket(ticket, saveType, request
							.getRemoteUser());
					if (!ticket.getSaveError().equals("")) {
						request.setAttribute("ticket", ticket);
						forwardURL = "jsp/editticket.jsp";
					} else {
						if (lastSaveType.equals("e")) {
							request.removeAttribute("ticket");
							forwardURL = "CLOSE";
						} else if (saveType.equals("")
								|| saveType.equalsIgnoreCase("normal")
								|| saveType.equalsIgnoreCase("edit")
								|| saveType.equalsIgnoreCase("void")) {
							request.removeAttribute("ticket");
							forwardURL = "jsp/editticket.jsp";
						} else if (saveType.equalsIgnoreCase("repeat")
								|| saveType.equalsIgnoreCase("conjunction")) {
							request.setAttribute("ticket", ticket);
							forwardURL = "jsp/editticket.jsp";
						} else {
							request.removeAttribute("ticket");
							forwardURL = "CLOSE";
						}
					}
				} else if (cmd.equalsIgnoreCase("createrefund")) {
					sess.removeAttribute("rtcb");
					Collection c = (new RefundHandler()).allReasonCodes();
					request.setAttribute("admReasonCodes", c);
					forwardURL = "jsp/editrefund.jsp";
				} else if (cmd.equalsIgnoreCase("saverefund")) {
					RefundHandler rh = new RefundHandler();
					RefundBean rb = (RefundBean) request.getAttribute("refund");
					RefundTicketCollectionBean rtcb = (RefundTicketCollectionBean) sess
							.getAttribute("rtcb");
					
					if (rh.saveRefund(rb, rtcb, request.getRemoteUser())) {
						sess.removeAttribute("rtcb");
						request.removeAttribute("refund");
					} else {
						sess.setAttribute("rtcb", rtcb);
					}
					request.setAttribute("admReasonCodes", rh.allReasonCodes());
					forwardURL = "jsp/editrefund.jsp";
				} else if (cmd.equalsIgnoreCase("addrefundticket")) {

					RefundTicketCollectionBean rtcb = (RefundTicketCollectionBean) sess
							.getAttribute("rtcb");
					rtcb.setEditingLineNo(-1);
					rtcb.setViewed(false);
					sess.setAttribute("rtcb", rtcb);
					String tnr = request.getParameter("ticketNumber");
					TicketBean ticket = new SearchHandler()
							.getSingleTicket(tnr);
					if (null != ticket) {
						if (!rtcb.getAirlineNum().equals("")
								&& !rtcb.getAirlineNum().equals(
										ticket.getAirlineNumber())){
							/*forwardURL = "jsp/rfndticketerror.jsp?error=Ticket number "
									+ tnr
									+ "%20is%20not%20by%20same%20airline%20as%20previous%20tickets!";*/
							forwardURL = "jsp/rfndticketerror.jsp?error=Ticket number "
								+ tnr
								+ " is not by same airline as previous tickets!";
					}else {
							sess.setAttribute("ticket", ticket);
							forwardURL = "jsp/refundticket.jsp?doctype="
									+ request.getParameter("docType");
						}
					} else {
						/*forwardURL = "jsp/rfndticketerror.jsp?error=Ticket number " + tnr + "%20not%20found!";*/
						forwardURL = "jsp/rfndticketerror.jsp?error=Ticket number " + tnr + " not found!";
					}
				} else if (cmd.equalsIgnoreCase("saverefundticket")) {
								
					TicketBean ticket = (TicketBean) sess
							.getAttribute("ticket");
					RefundBean refund = (RefundBean) request
							.getAttribute("refund");
					RefundTicketCollectionBean rtcb = (RefundTicketCollectionBean) sess
							.getAttribute("rtcb");
					RefundHandler rh = new RefundHandler();
								
					rh.addTicket(refund, ticket, rtcb);
					
					sess.setAttribute("rtcb", rtcb);
					sess.removeAttribute("ticket");
					request.setAttribute("admReasonCodes", rh.allReasonCodes());
					
					forwardURL = "jsp/editrefund.jsp";
				} else if (cmd.equalsIgnoreCase("removerefundticket")) {
					
					String lineNo = request.getParameter("ticketLineSelect");
					RefundHandler rh = new RefundHandler();
					RefundBean refund = (RefundBean) request
							.getAttribute("refund");
					RefundTicketCollectionBean rtcb = (RefundTicketCollectionBean) sess
							.getAttribute("rtcb");
					try {
						rh.removeTicket(refund, rtcb, Integer.parseInt(lineNo));
					} catch (Exception e) {
					}
					sess.setAttribute("rtcb", rtcb);
					request.setAttribute("admReasonCodes", rh.allReasonCodes());
					forwardURL = "jsp/editrefund.jsp";
				} else if (cmd.equalsIgnoreCase("editrefundticket")
						|| cmd.equalsIgnoreCase("viewrefundticket")) {
					try {
						int lineNo = Integer.parseInt(request
								.getParameter("ticketLineSelect"));
						//						RefundBean refund =
						// (RefundBean)request.getAttribute("refund");
						RefundTicketCollectionBean rtcb = (RefundTicketCollectionBean) sess
								.getAttribute("rtcb");
						RefundTicket rt = (RefundTicket) (rtcb
								.getRefundTickets().elementAt(lineNo));
						if (cmd.equalsIgnoreCase("editrefundticket")) {
							rtcb.setViewed(false);
						} else
							rtcb.setViewed(true);
						rtcb.setEditingLineNo(lineNo);
						sess.setAttribute("rtcb", rtcb);
						TicketBean ticket = new SearchHandler()
								.getSingleTicket(rt.getTicketNumber());
						if (null != ticket) {
							sess.setAttribute("ticket", ticket);
							forwardURL = "jsp/refundticket.jsp?doctype="
									+ request.getParameter("docType");
						} else {
							//forwardURL = "jsp/rfndticketerror.jsp?error=Error%20editing%20line%20" + lineNo;
							forwardURL = "jsp/rfndticketerror.jsp?error=Error editing line " + lineNo;
						}
					} catch (Exception e) {
						//forwardURL = "jsp/rfndticketerror.jsp?error=Error%20editing%20ticket";
						forwardURL = "jsp/rfndticketerror.jsp?error=Error editing ticket";
					}
				} else if (cmd.equalsIgnoreCase("startsearch")) {
					sess.removeAttribute("searchresults");
					forwardURL = "jsp/search.jsp";
				} else if (cmd.equalsIgnoreCase("dosearch")) {
					SearchBean search = (SearchBean) request.getAttribute("search");
					
					SearchHandler sh = new SearchHandler();
					SearchResultCollectionBean srcb = sh.doSearch(search);
					sess.setAttribute("searchresults", srcb);
					forwardURL = "jsp/search.jsp";
				} else if (cmd.equalsIgnoreCase("viewdocument")
						|| cmd.equalsIgnoreCase("editdocument")) {
					String lineStr = request.getParameter("lineno");
					SearchResultCollectionBean srcb = (SearchResultCollectionBean) sess.getAttribute("searchresults");
					int lineInt = -1;
					try {
						lineInt = Integer.parseInt(lineStr);
					} catch (Exception e) {
						lineInt = -1;
					}
					if (srcb.getFoundDocuments() != null && lineInt > -1
							&& lineInt < srcb.getFoundDocuments().size()) {
						SearchResultBean srb = (SearchResultBean) srcb
								.getFoundDocuments().elementAt(lineInt);
						if (srb.getDocumentType().startsWith("T")) {
							TicketBean ticket = new SearchHandler()
									.getSingleTicket(srb.getDocumentNo());
							ticket.setLastSaveType("e");
							request.setAttribute("ticket", ticket);
							if (cmd.equalsIgnoreCase("viewdocument"))
								forwardURL = "jsp/viewticket.jsp";
							else
								forwardURL = "jsp/editticket.jsp";
						} else if (srb.getDocumentType().toUpperCase()
								.startsWith("REFUND LETTER")) {
							RefundLetterBean rlb = new RefundLetterHandler()
									.findRefundLetter(Integer.parseInt(srb
											.getDocumentNo()));
							if (rlb == null) {
								forwardURL = "jsp/closewindow.jsp?error="
										+ java.net.URLEncoder
												.encode("Refund letter number "
														+ srb.getDocumentNo()
														+ " not found", "UTF-8");
							} else {
								sess.setAttribute("refundletter", rlb);
								forwardURL = "jsp/refundletter3.jsp";
							}
						} else {
							RefundBean rb = new RefundBean();
							RefundTicketCollectionBean rtcb = new RefundTicketCollectionBean();
							new SearchHandler().getSingleRefund(srb
									.getDocumentNo(), rb, rtcb);
							sess.setAttribute("rtcb", rtcb);
							request.setAttribute("refund", rb);
							if (cmd.equalsIgnoreCase("viewdocument"))
								forwardURL = "jsp/viewrefund.jsp";
							else {
								request.setAttribute("admReasonCodes",
										(new RefundHandler()).allReasonCodes());
								forwardURL = "jsp/editrefund.jsp";
							}
						}
					} else {
						forwardURL = "CLOSE";
					}
				} else if (cmd.equalsIgnoreCase("deleteticket")) {
					if (request.isUserInRole("STELLA_SUPERVISOR")) {
						String tnr = request.getParameter("ticketno");
						if (tnr != null && tnr != "") {
							String err = new TicketHandler().deleteTicket(tnr);
							if (err == null || err.equals(""))
								forwardURL = "CLOSE";
							else
								forwardURL = "jsp/closewindow.jsp?error="
										+ java.net.URLEncoder.encode(err,
												"UTF-8");
						} else
							forwardURL = "jsp/closewindow.jsp?error="
									+ java.net.URLEncoder
											.encode(
													"Ticket number to delete not specified",
													"UTF-8");
					} else
						forwardURL = "jsp/closewindow.jsp?error="
								+ java.net.URLEncoder
										.encode(
												"You are not authorized to delete tickets!",
												"UTF-8");
				} else if (cmd.equalsIgnoreCase("startexceptions")) {
					BeanCollectionBean bcb = new MatchExceptionsHandler()
							.findMatchExceptions(request /* .getRemoteUser() */);
					sess.setAttribute("matchexceptions", bcb);
					forwardURL = "jsp/matchexceptions.jsp";
				} else if (cmd.equalsIgnoreCase("postexceptions")) {
					BeanCollectionBean bcb = (BeanCollectionBean) (sess
							.getAttribute("matchexceptions"));
					if (null == bcb) {
						forwardURL = "CLOSE";
					} else {
						new MatchExceptionsHandler().updateMatchExceptions(bcb,
								request);
						if (bcb.getError().equals("")) {
							bcb = new MatchExceptionsHandler()
									.findMatchExceptions(request /* .getRemoteUser() */);
							sess.setAttribute("matchexceptions", bcb);
							forwardURL = "jsp/matchexceptions.jsp";
						} else {
							sess.setAttribute("matchexceptions", bcb);
							forwardURL = "jsp/matchexceptions.jsp";
						}
					}
				} else if (cmd.equalsIgnoreCase("viewmatchhistory")) {
					BeanCollectionBean bcb = new MatchExceptionsHandler()
							.findMatchHistory(request.getParameter("pnrid"));
					request.setAttribute("matchhistory", bcb);
					forwardURL = "jsp/matchhistory.jsp";
				} else if (cmd.equalsIgnoreCase("viewexceptiondescription")) {
					BeanCollectionBean bcb = new MatchExceptionsHandler()
							.findMatchDescriptions();
					request.setAttribute("matchdescription", bcb);
					forwardURL = "jsp/matchdescriptions.jsp";
				} else if (cmd.equalsIgnoreCase("viewbspdetails")) {
					BspDetailBean bdb = new MatchExceptionsHandler()
							.findBspDetails(request.getParameter("bspid"));
					request.setAttribute("bspdetails", bdb);
					forwardURL = "jsp/bspdetails.jsp";
				} else if (cmd.equalsIgnoreCase("startrefundletter")) {
					String lineno = request.getParameter("lineno");
					try {
						int line = Integer.parseInt(lineno, 10);
						BeanCollectionBean bcb = (BeanCollectionBean) (sess
								.getAttribute("matchexceptions"));
						if (null == bcb
								|| bcb.getBeanCollection().size() < line) {
							forwardURL = "CLOSE";
						} else {
							MatchExceptionsBean meb = (MatchExceptionsBean) bcb
									.getBeanCollection().get(line);
							RefundLetterBean rlb = new RefundLetterHandler()
									.createRefundLetter(meb);
							rlb.setLetterAuthor(request.getRemoteUser());
							sess.setAttribute("refundletter", rlb);
							forwardURL = "jsp/refundletter1.jsp";
						}
					} catch (NumberFormatException e) {
						forwardURL = "CLOSE";
					}
				} else if (cmd.equalsIgnoreCase("refundletter2")) {
					RefundLetterBean rlb = (RefundLetterBean) sess
							.getAttribute("refundletter");
					String numTicket = request.getParameter("numticket");
					new RefundLetterHandler().lookupText(rlb, numTicket
							.equals("1"));
					forwardURL = "jsp/refundletter2.jsp";
				} else if (cmd.equalsIgnoreCase("saverefundletter")) {
					RefundLetterBean rlb = (RefundLetterBean) sess
							.getAttribute("refundletter");
					String err = new RefundLetterHandler()
							.saveRefundLetter(rlb/* , request.getRemoteUser() */);
					sess.removeAttribute("refundletter");
					if (err.equals(""))
						forwardURL = "CLOSE";
					else
						forwardURL = "jsp/closewindow.jsp?error="
								+ java.net.URLEncoder.encode(err, "UTF-8");
				} else if (cmd.equalsIgnoreCase("startairlinemapping")) {
					AirlineMappingBean amb;
					amb = (AirlineMappingBean) sess.getAttribute("amb");
					if (null == amb) {
						amb = new AirlineMappingHandler().getMappings();
						sess.setAttribute("amb", amb);
					}
					forwardURL = "jsp/airlinemappingview.jsp";
				} else if (cmd.equalsIgnoreCase("am_edituser")) {
					forwardURL = "jsp/airlinemappingdetail.jsp?type=user&param="
							+ request.getParameter("param");
				} else if (cmd.equalsIgnoreCase("am_editairline")) {
					System.out.println("************** controller sevlet  " + request.getParameter("param"));
				
					Vector asu = new UserMaintenanceHandler()
							.getAllStellaUsers();
					request.setAttribute("asu", asu);
					System.out.println(" Total users "  + asu.size());
					
					forwardURL = "jsp/airlinemappingdetail.jsp?type=airline&param="
							+ request.getParameter("param");
					System.out.println("************** controller sevlet  at 394 " + "jsp/airlinemappingdetail.jsp?type=airline&param="
							+ request.getParameter("param"));
				} else if (cmd.equalsIgnoreCase("am_adduser")) {
					Vector asu = new UserMaintenanceHandler()
							.getAllStellaUsers();
					request.setAttribute("asu", asu);
					forwardURL = "jsp/airlinemappingdetail.jsp?type=adduser";
				} else if (cmd.equalsIgnoreCase("saveairlinemapping")) {
					String err = new AirlineMappingHandler()
							.saveMappings(request);
					if (err.equals("")) {
						sess.removeAttribute("amb");
						forwardURL = "jsp/closewindow.jsp?reload";
					} else {
						sess.removeAttribute("amb");
						forwardURL = "jsp/closewindow.jsp?error="
								+ java.net.URLEncoder.encode(err, "UTF-8");
					}
				} else if (cmd.equalsIgnoreCase("airlinedetails")) {
					String param = request.getParameter("param");
					AirlineBean ab = new AirlineBean();
					if (param != null) {
						try {
							ab = new AirlineHandler().getAirlineDetails(Integer
									.parseInt(param, 10));
						} catch (NumberFormatException e) {
						}
					}
					request.setAttribute("airlinedetails", ab);
					forwardURL = "jsp/airlinedetails.jsp";
				} else if (cmd.equalsIgnoreCase("saveairlinedetails")) {
					AirlineBean ab = (AirlineBean) request
							.getAttribute("airlinedetails");
					new AirlineHandler().saveAirlineDetails(ab, request
							.getRemoteUser());
					if (ab.getSaveError().equals("")) {
						sess.removeAttribute("amb");
						context.setAttribute("contextdata", new ContextData());
						forwardURL = "CLOSE";
					} else {
						request.setAttribute("airlinedetails", ab);
						forwardURL = "jsp/airlinedetails.jsp";
					}
				} else if (cmd.equalsIgnoreCase("usermaint")) {
					if (request.isUserInRole("STELLA_SUPERVISOR")) {
						/* populate userlist bean */
						response.sendRedirect("/usermaint");
						forwardURL = "jsp/";
					} else {
						forwardURL = "jsp/newpassword.jsp";
					}
				} else if (cmd.equalsIgnoreCase("newpassword")) {
					String err = new UserMaintenanceHandler()
							.updateUserPassword(request.getRemoteUser(),
									request.getParameter("oldpw"), request
											.getParameter("pw1"));
					if (err.equals("")) {
						forwardURL = "CLOSE";
					} else {
						request.setAttribute("chgpwerr", err);
						forwardURL = "jsp/newpassword.jsp";
					}
				} else if (cmd.equalsIgnoreCase("logoff")) {
					//sess.setMaxInactiveInterval(1);
					//sess.invalidate();
					System.out.println("----In log off");
					
					request.getSession().invalidate();
		
				forwardURL = "loggedoff.html";
				} else {
					System.out.println("Unknown cmd: " + cmd);
					forwardURL = "CLOSE";
				}
			}
			System.out.println(cmd + "------ at 481");
			
			if (!forwardURL.equals(""))
				if (forwardURL.equals("CLOSE"))
					request.getRequestDispatcher("jsp/closewindow.jsp").forward(
							request, response);
				else{
					System.out.println("forwardURL : "+ forwardURL);
					request.getRequestDispatcher(forwardURL).forward(request,
							response);
					System.out.println("after forwardURL ------ at 494");
					}
		} catch (StellaException e) {
			PrintWriter out = response.getWriter();
			out
					.print("<html><head><title>Servlet Error</title></head><body bgcolor=#ffffff>");
			out
					.print("<font face=\"Helvetica\"><h2><font color=#FF0000>Server Error Page</font></h2>");
			out
					.print("<hr/>We encountered an error - please correct the problem and try again<p/>");
			out.print("Error message: <b>" + e.getMessage() + "</b><hr/>");
			out
					.print("<b>Stacktrace</b> (please include in error report):<br/><pre>");
			e.printStackTrace(out);
			out.print("</pre></body></html>");
			e.printStackTrace();
			log(e.getMessage(), e);
		} catch (Exception e) {
			System.out.println("Query: " + request.getQueryString());
			String paramName;
			for (Enumeration en = request.getParameterNames(); en
					.hasMoreElements();) {
				paramName = (String) en.nextElement();
				System.out.println(paramName + ": >"
						+ request.getParameter(paramName) + "<");
			}
			e.printStackTrace();
			PrintWriter out = response.getWriter();
			out
					.print("<html><head><title>Servlet Error</title></head><body bgcolor=#ffffff>");
			out
					.print("<font face=\"Helvetica\"><h2><font color=#FF0000>Server Error Page</font></h2>");
			out
					.print("<hr/>Please report the following error by copy/pasting it to an e-mail to your helpdesk:<p/>");
			out.print("Error message: <b>" + e.getMessage()
					+ "</b><p/>Stack trace:<br><b>");
			e.printStackTrace(out);
			out.print("</b><p/>Query string: <b>" + request.getQueryString()
					+ "</b><p/><hr/></body></html>");

			log("Uncaught Exception in Stella", e);

		}
	}
}
