package uk.co.firstchoice.stella.frontend;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class StellaSelectionTag extends TagSupport {

	//selectionType indicates which select to build - season/year, location
	private String selectionType = "";

	//	private int indexValue = 0;
	private int tabindex = -2;

	private String amUser = ""; //Airline Management User Name

	private String amAirline = ""; //Airline Management Airline Name

	private JspWriter out = null;

	private ContextData contextdata = null;

	private SessionData sessiondata = null;

	private TicketBean ticket = null;

	private RefundBean refund = null;

	private SearchBean search = null;

	//private MatchExceptionsBean matchExceptions = null;

	private BeanCollectionBean matchExceptions = null;

	public void setSelectionType(String st) {
		selectionType = st;
	}

	//	public void setIndexValue(int idx) { indexValue = idx; }

	public void setTabindex(int ti) {
		tabindex = ti;
	}

	public void setAmUser(String amu) {
		amUser = amu;
	}

	public void setAmAirline(String ama) {
		amAirline = ama;
	}

	private void buildTicketAirlineSelect() throws JspException {
		try {
			out
					.print("<select name=\"airlineSelect\" tabindex=\"-1\" onChange=\"doAirlineSelectChange();\">");
			out.print("<option value=\"\">- Select Airline -</option>");
			String s = "";


			String curr = ticket != null ? ticket.getAirlineNumber()
					: search != null ? search.getAirlineNumber() : "";
			String n = "";
			String ind = "";
			
			for (Enumeration e = contextdata.getAirlines().elements(); e
					.hasMoreElements();) {
				s = (String) e.nextElement(); // British Airways-125;Y
				n = s.substring(s.length() - 5, s.length() - 2); // 125
				ind = s.substring(s.length() - 1); //	Y

				out.print("<option value=\"" + n + ind + "\""
						+ (n.equals(curr) ? " selected " : "") + ">"
						+ s.substring(0, s.length() - 2) + "</option>");
			}
			out.print("</select>");

			

		} catch (IOException e) {
			System.out.println("-----------hooooooooooooooooooooooooo-----------");
			throw new JspException(e.toString());
		}
	}

	private void buildIataSelect() throws JspException {
		try {
			String tiStr = tabindex == -2 ? ""
					: ("tabindex=\"" + tabindex + "\"");
			out
					.print("<select name=\"iataSelect\" "
							+ tiStr
							+ " onChange=\"var num=frm.iataSelect.options[frm.iataSelect.selectedIndex].value; if (num != '---') { frm.iataNumber.value=num;} else {frm.iataNumber.value=''}\">");
			out.print("<option value=\"---\">- Select IATA no -</option>");
			String s = "";
			String curr = refund != null ? refund.getIataNumber()
					: ticket != null ? ticket.getIataNumber()
							: search != null ? search.getIataNumber() : "";
			String n = "";
			for (Enumeration e = contextdata.getIataNumbers().elements(); e
					.hasMoreElements();) {
				s = (String) e.nextElement();
				n = s.substring(0, s.indexOf("-"));
				out.print("<option value=\"" + n + "\""
						+ (n.equals(curr) ? " selected " : "") + ">" + s
						+ "</option>");
			}
			out.print("</select>");
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
	}

	private void buildAmdReasonCodeSelect() throws JspException {
		try {
			Collection allReasons = (Vector) pageContext.getRequest()
					.getAttribute("admReasonCodes");
			String tiStr = tabindex == -2 ? ""
					: ("tabindex=\"" + tabindex + "\"");
			out.print("<select name=\"admReasonCode\" " + tiStr
					+ "onChange=\"doAdmReasonCodeChange();\">");
			String[] s;
			String curr = refund.getAdmReasonCode();
			String tmp;
			out.print("<option/>");
			for (Iterator i = allReasons.iterator(); i.hasNext();) {
				tmp = (String) i.next();
				s = tmp.split("~");
				out.print("<option value=\"" + tmp + "\""
						+ (s[0].equalsIgnoreCase(curr) ? " selected " : "")
						+ ">" + s[1] + "</option>");
			}
			out.print("</select>");
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
	}

	private void buildTicketTypeSelect() throws JspException {
		try {
			out
					.print("<select name=\"ticketType\" tabindex=\"1\" onChange=\"doTypeChange();\">");
			String s = "";
			String curr = ticket.getTicketType();
			String n = "";
			for (Enumeration e = contextdata.getTicketDocTypes().elements(); e
					.hasMoreElements();) {
				s = (String) e.nextElement();
				n = s.substring(0, 3);
				out.print("<option value=\"" + n + "\""
						+ (n.equals(curr) ? " selected " : "") + ">"
						+ s.substring(4) + "</option>");
			}
			out.print("</select>");
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
	}

	private void buildPassengerTypeSelect() throws JspException {
		try {
			String pasTyp = ticket.getPassengerType();
			pasTyp = pasTyp.startsWith("A") || pasTyp.startsWith("C")
					|| pasTyp.startsWith("I") ? pasTyp : "A";
			out
					.print("<input name=\"passengerType\" type=\"radio\" tabindex=\"11\" value=\"AD\""
							+ (pasTyp.startsWith("A") ? " checked" : "")
							+ ">Adult<br>");
			out
					.print("<input name=\"passengerType\" type=\"radio\" tabindex=\"11\" value=\"CH\""
							+ (pasTyp.startsWith("C") ? " checked" : "")
							+ ">Child<br>");
			out
					.print("<input name=\"passengerType\" type=\"radio\" tabindex=\"11\" value=\"IN\""
							+ (pasTyp.startsWith("I") ? " checked" : "")
							+ ">Infant");
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
	}

	private void displayEnvironmentInfo() throws JspException {
		try {
			out.print("<th>Ver:</th><td>" + contextdata.getVersion() +
			//						",</td><th>User:</th><td>"+sessiondata.getUserName()+
					//						",</td><th>Server:</th><td>"+contextdata.getServerName()+
					",</td><th>DW:</th><td>" + contextdata.getDwName()
					+ "</td>");
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
	}

	/*
	 * now doing a radio select in a pop-up window - see below private void
	 * buildReasonCodeSelect() throws JspException { try { out.print(" <select
	 * name=\"newReasonCode"+indexValue+"\"
	 * onChange=\"doChange("+indexValue+");\">"); String s = ""; String n = "";
	 * out.print(" <option value=\"--\">--Select New Reason-- </option>"); for
	 * (Enumeration e = sessiondata.getUserReasons().elements();
	 * e.hasMoreElements(); ) { s = (String)e.nextElement(); n = s.substring(0,
	 * 2)+s.substring(s.length()-2); out.print(" <option
	 * value=\""+n+"\">"+s.substring(0, s.length()-2)+" </option>"); }
	 * out.print(" </select>"); } catch(IOException e) { throw new
	 * JspException(e.toString()); } }
	 */

	private void buildReasonCodeSelect(boolean bsp) throws JspException {
		try {
			String s = "";
			String n = "";
			int nr = 0;
			Enumeration e = bsp ? sessiondata.getBspUserReasons().elements()
					: sessiondata.getUserReasons().elements();
			while (e.hasMoreElements()) {
				out.print("<tr class=\"line" + ((nr++) % 2) + "\"><td>");
				s = (String) e.nextElement();
				n = s.substring(0, 2) + s.substring(s.length() - 2);
				out.print("<input type=\"radio\" name=\"reasonCode\" value=\""
						+ n + "\" onClick=\"doSelect('" + n + "');\">");
				out.print(s.substring(0, s.length() - 2));
				out.println("</td></tr>");
			}
			out.print("<tr class=\"line" + ((nr++) % 2) + "\"><td>");
			out
					.print("<input type=\"radio\" name=\"reasonCode\" value=\"--N\" onClick=\"doSelect('--N');\">");
			out.print("Remove exception code");
			out.println("</td></tr>");
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
	}

	// for a given user, build the airline selector
	private void buildAmAirlineSelect() throws JspException {
		AirlineMappingBean amb = (AirlineMappingBean) (pageContext.getSession()
				.getAttribute("amb"));
		try {
			String s;
			String n;
			TreeSet ts = null;
			if (amUser != null && !amUser.equals(""))
				ts = (TreeSet) amb.getAirlinesByUser().get(amUser);
			if (null == ts)
				ts = new TreeSet();
			out
					.print("<select multiple size=\"15\" name=\"amAirlineSelect\" onChange=\"selectorChange();\">");
			for (Enumeration e = contextdata.getAirlines().elements(); e
					.hasMoreElements();) {
				s = (String) e.nextElement(); // British Airways-125;Y
				s = s.substring(0, s.length() - 2); // British Airways-125
				n = s.substring(s.length() - 3); // 125
				out.print("<option value=\"" + n + "\"");
				if (ts.contains(s))
					out.print(" selected");
				out.print(">" + s + "</option>");
			}
			out.print("</select>");
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
	}

	// for a given airline build the user selector
	private void buildAmUserSelect() throws JspException {
		AirlineMappingBean amb = (AirlineMappingBean) (pageContext.getSession()
				.getAttribute("amb"));
		Vector allStellaUsers = (Vector) (pageContext.getRequest()
				.getAttribute("asu"));
		try {
			String s;
			TreeSet ts = (TreeSet) amb.getUsersByAirline().get(amAirline);
			if (null == ts)
				ts = new TreeSet();
			out
					.print("<select multiple size=\"15\" name=\"amUserSelect\" onChange=\"selectorChange();\">");
			for (Enumeration e = allStellaUsers.elements(); e.hasMoreElements();) {
				s = (String) e.nextElement(); // John Doe
				out.print("<option value=\"" + s + "\"");
				if (ts.contains(s))
					out.print(" selected");
				out.print(">" + s + "</option>");
			}
			out.print("</select>");
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
	}

	//
	private void buildAmAddUserSelect() throws JspException {
		AirlineMappingBean amb = (AirlineMappingBean) (pageContext.getSession()
				.getAttribute("amb"));
		Vector allStellaUsers = (Vector) (pageContext.getRequest()
				.getAttribute("asu"));

		try {
			String s;
			Set keys = amb.getAirlinesByUser().keySet();
			out
					.print("<select size=\"15\" name=\"amAddUserSelect\" onChange=\"selectorChange();\">");
			for (Enumeration e = allStellaUsers.elements(); e.hasMoreElements();) {
				s = (String) e.nextElement(); // John Doe
				if (!keys.contains(s)) {
					out.print("<option value=\"" + s + "\">" + s + "</option>");
				}
			}
			out.print("</select>");
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
	}

	public int doStartTag() throws JspException {
		out = pageContext.getOut();
		contextdata = (ContextData) (pageContext.getServletContext()
				.getAttribute("contextdata"));
		sessiondata = (SessionData) (pageContext.getSession()
				.getAttribute("sessiondata"));
		ticket = (TicketBean) (pageContext.getRequest().getAttribute("ticket"));
		refund = (RefundBean) (pageContext.getRequest().getAttribute("refund"));
		search = (SearchBean) (pageContext.getRequest().getAttribute("search"));
		matchExceptions  = (BeanCollectionBean) (pageContext.getRequest().getAttribute("matchExceptions"));

		if (selectionType.startsWith("airlineSelect")) {
			buildTicketAirlineSelect();
		} else if (selectionType.startsWith("iataSelect")) {
			buildIataSelect();
		} else if (selectionType.startsWith("ticketTypeSelect")) {
			buildTicketTypeSelect();
		} else if (selectionType.startsWith("pasTypeSelect")) {
			buildPassengerTypeSelect();
		} else if (selectionType.startsWith("environmentInfo")) {
			displayEnvironmentInfo();
		} else if (selectionType.startsWith("reasonCodeSelect")) {
			buildReasonCodeSelect(false);
		} else if (selectionType.startsWith("bspReasonCodeSelect")) {
			buildReasonCodeSelect(true);
		} else if (selectionType.startsWith("amAirlineSelect")) {
			buildAmAirlineSelect();
		} else if (selectionType.startsWith("amUserSelect")) {
			buildAmUserSelect();
		} else if (selectionType.startsWith("amAddUser")) {
			buildAmAddUserSelect();
		} else if (selectionType.startsWith("amdReason")) {
			buildAmdReasonCodeSelect();
		}else if (selectionType.startsWith("branchSelect")) {
			buildBranchSelect();
		}else if (selectionType.startsWith("refundTypeSelect")) {
			buildRefundTypeSelect();

		}
		return SKIP_BODY;
	}

	// Added by Jyoti , 20/04/06 ,  will be used in exception screen for specialist branch code
	private void buildBranchSelect() throws JspException {
		try {

			out
			.print("<select name=\"branchSelect\" tabindex=\"-1\" onChange=\"doBranchSelectChange();\">");
	out.print("<option value=\"\">- Select Reconciliation Level -</option>");

	String s = "";
	String curr = matchExceptions != null ? matchExceptions.getSpecialistBranch(): "";

	//: search != null ? search.getAirlineNumber() : "";


	out.print("<option value=\"" + "AAIR" + "\""
			+ ("AAIR".equals(curr) ? " selected " : "") + ">" + "AAIR-Itour"
			+ "</option>");

	out.print("<option value=\"" + "DWHS" + "\""
			+ ("DWHS".equals(curr) ? " selected " : "") + ">" + "DWHS-DataWarehouse"
			+ "</option>");


	String n = "";
	String ind = "";
	for (Enumeration e = contextdata.getSpecialistBranchList().elements(); e
			.hasMoreElements();) {
		/*s = (String) e.nextElement(); // HAJS-Hays&Jarvis
		n = s.substring(0,4); // HAJS
		out.print("<option value=\"" + n + "\""
				+ (n.equals(curr) ? " selected " : "") + ">"
				+ s.substring(0, s.length() - 2) + "</option>");
		*/

		s = (String) e.nextElement();
		n = s.substring(0,4);  //HAJS,CITA,MEON,SOVE   //  s.indexOf("-"));


		out.print("<option value=\"" + n + "\""
				+ (n.equals(curr) ? " selected " : "") + ">" + s
				+ "</option>");

	}
	out.print("</select>");
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
	}


//	 Added by Jyoti , 20/04/06 ,  will be used in refund entry  screen
	private void buildRefundTypeSelect() throws JspException {
		try {

			String tiStr = tabindex == -2 ? ""
					: ("tabindex=\"" + tabindex + "\"");

		/*out	.print("<select name=\"refundTypeSelect\" "
					+ tiStr
					+ " onChange=\"var num=frm.refundTypeSelect.options[frm.refundTypeSelect.selectedIndex].value; if (num != '---') { frm.refundTypeSelect.value=num;} else {frm.refundTypeSelect.value=''}\">");
					*/
			/*out.print("<select name=\"refundTypeSelect\" tabindex=\"-1\" onChange=\"doRefundTypeSelectChange();\">");*/
			out.print("<select name=\"refundTypeSelect\" " +tiStr +
					 "onChange=\"doRefundTypeSelectChange();\">");

	out.print("<option value=\"\">- Select Document Type -</option>");

	String s = "";
	String curr = refund != null ? refund.getDocType(): "";

	String n = "";
	String ind = "";
	for (Enumeration e = contextdata.getRefundDocTypes().elements(); e
			.hasMoreElements();) {

		s = (String) e.nextElement();
		n = s.substring(0,3);  //ACM,ADM,MRN,MAN


		out.print("<option value=\"" + n + "\""
				+ (n.equals(curr) ? " selected " : "") + ">" + s
				+ "</option>");

	}
			out.print("</select>");
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
	}

} //Class

/*

out
.print("<select name=\"airlineSelect\" tabindex=\"-1\" onChange=\"doAirlineSelectChange();\">");
out.print("<option value=\"\">- Select Airline -</option>");
String s = "";
String curr = ticket != null ? ticket.getAirlineNumber()
: search != null ? search.getAirlineNumber() : "";
String n = "";
String ind = "";
for (Enumeration e = contextdata.getAirlines().elements(); e
.hasMoreElements();) {
s = (String) e.nextElement(); // British Airways-125;Y
n = s.substring(s.length() - 5, s.length() - 2); // 125
ind = s.substring(s.length() - 1); //	Y
out.print("<option value=\"" + n + ind + "\""
	+ (n.equals(curr) ? " selected " : "") + ">"
	+ s.substring(0, s.length() - 2) + "</option>");
}
out.print("</select>");


*/

