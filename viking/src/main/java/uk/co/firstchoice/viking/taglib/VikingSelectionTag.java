package uk.co.firstchoice.viking.taglib;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import uk.co.firstchoice.viking.gui.AllocationBean;
import uk.co.firstchoice.viking.gui.FlightBotFormBean;
import uk.co.firstchoice.viking.gui.ISearchResult;
import uk.co.firstchoice.viking.gui.SearchFormBean;
import uk.co.firstchoice.viking.gui.SessionData;
import uk.co.firstchoice.viking.gui.util.ForwardConstants;

import javax.servlet.http.HttpServletRequest;

//import uk.co.firstchoice.fcutil.*;

/** A set of custom JSP tags for the Viking project. */
public class VikingSelectionTag extends TagSupport {

	private String selectionType = ForwardConstants.EMPTY; //selectionType

	// indicates which
	// select to build -
	// season/year,
	// location

	private String selectionName = ForwardConstants.EMPTY;

	private String selectionValue = ForwardConstants.EMPTY;

	private String onChange = ForwardConstants.EMPTY;

	private String[] dows = { "1 - Monday", "2 - Tuesday", "3 - Wednesday",
			"4 - Thursday", "5 - Friday", "6 - Saturday", "7 - Sunday" };

	private int tabindex = -2;

	//	private static final String CONTEXT = "context";

	private JspWriter out = null;

	//	private ContextData contextdata = null;
	//	private SessionData sessiondata = null;

	/**
	 * Setter method for the selectionType. A tag in the JSP document must have
	 * a selectionType attribute indicating what type of selector is required.
	 * 
	 * @param st
	 *            The selection to be generated in the JSP. Stored in
	 *            selectionType
	 */
	public void setSelectionType(final String st) {
		selectionType = st;
	}

	/**
	 * Setter method for the tabIndex. Optional attribute for a JSP tag. Will
	 * generate a TabIndex attribute with this value in the generated html with
	 * this value
	 * 
	 * @param ti
	 *            The value of the TabIndex to generate
	 */
	public void setTabindex(final int ti) {
		tabindex = ti;
	}

	/**
	 * @param sn
	 *            String
	 */
	public void setName(final String sn) {
		selectionName = sn;
	}

	/**
	 * @param sv
	 *            String
	 */
	public void setValue(final String sv) {
		selectionValue = sv;
	}

	/**
	 * @param oc
	 *            String
	 */
	public void setOnChange(final String oc) {
		onChange = oc;
	}

	/**
	 * 
	 * @throws JspException
	 */
	private void displayEnvironmentInfo() throws JspException {
/*        HttpSession session = this.pageContext.getSession();
		SessionData sessionData = (SessionData) session.getAttribute(ForwardConstants.SESSION_DATA);
		String dw = sessionData.getDwName();
		String ver = sessionData.getVersion();
		String bld = sessionData.getBuildDT();
		try {
			out
					.print("<th class=\"versioninfo\">Ver:</th><td class=\"versioninfo\">"
							+ ver
							+ "</td>"
							+ "<th class=\"versioninfo\">Build:</th><td class=\"versioninfo\">"
							+ bld
							+ "<th class=\"versioninfo\">DW:</th><td class=\"versioninfo\">"
							+ dw + "</td>");
		} catch (IOException e) {
			throw new JspException(e.toString());
		}*/
	}

	/**
	 * 
	 * @throws JspException
	 */
	private void displayYNSelector() throws JspException {
		try {
			out.print("<select name=\""
					+ selectionName
					+ "\">"
					+ "<option> "
					+ "<option value=\"Y\" "
					+ (selectionValue.equals("Y") ? "selected"
							: ForwardConstants.EMPTY)
					+ ">Y"
					+ "<option value=\"N\" "
					+ (selectionValue.equals("N") ? "selected"
							: ForwardConstants.EMPTY) + ">N" + "</select>");
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
	}

	private void displaySeasonSelector() throws JspException {
		SessionData sd = (SessionData) (pageContext.getSession()
				.getAttribute(ForwardConstants.SESSION_DATA));
		try {
			out.print("<select name=\"" + selectionName + "\"");
			if (!onChange.equals(ForwardConstants.EMPTY)) {
				out.print(" onChange=\"" + onChange + "\"");
			}
			out.println(">");
			
	         for (Iterator i = sd.getSeasons().iterator(); i.hasNext();) {
				String s = (String) i.next(); //S03;010303;281003;Y
				String season = s.substring(0, 3);
				out.print("<option value=\"" + s + "\"");
				if (selectionValue.equals(s)
						|| selectionValue.equals(season)
						|| (selectionValue.equals(ForwardConstants.EMPTY) && s
								.substring(s.length() - 1).equals("Y"))) {
					out.print(" selected");
				}
				out.println(">" + season + "</option>");
			 }
	         
			out.println("</select>");
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
	}
	
	private void displayRestrcitedSeasonSelector() throws JspException {
		SessionData sd = (SessionData) (pageContext.getSession()
				.getAttribute(ForwardConstants.SESSION_DATA));
		try {
			out.print("<select name=\"" + selectionName + "\"");
			if (!onChange.equals(ForwardConstants.EMPTY)) {
				out.print(" onChange=\"" + onChange + "\"");
			}
			out.println(">");
			
			for (Iterator i = sd.getRestrictedSeasons().iterator(); i.hasNext();) {
				String s = (String) i.next(); //S03;010303;281003;Y
				String season = s.substring(0, 3);
				out.print("<option value=\"" + s + "\"");
				if (selectionValue.equals(s)
						|| selectionValue.equals(season)
						|| (selectionValue.equals(ForwardConstants.EMPTY) && s
								.substring(s.length() - 1).equals("Y"))) {
					out.print(" selected");
				}
				out.println(">" + season + "</option>");
			}
			out.println("</select>");
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
	}

	private void displayDOWSelector() throws JspException {
		try {
			out.print("<select name=\"" + selectionName + "\"");
			if (!onChange.equals(ForwardConstants.EMPTY)) {
				out.print(" onChange='" + onChange + "'");
			}
			out.println(">");
			out.println("<option></option>");
			for (int i = 0; i < dows.length; i++) {
				out.print("<option value=\"" + (i + 1) + "\"");
				if (selectionValue.equals("" + (i + 1))) {
					out.print(" selected");
				}
				out.println(">" + dows[i] + "</option>");
			}
			out.print("</select>");
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
	}

	private void displayCurrencySelector() throws JspException {
		Collection currencies = (TreeSet) (pageContext.getRequest()
				.getAttribute("currencies"));
		try {
			out.print("<select name=\"" + selectionName + "\"");
			if (!onChange.equals(ForwardConstants.EMPTY)) {
				out.print(" onChange='" + onChange + "'");
			}
			out.println(">");
			for (Iterator i = currencies.iterator(); i.hasNext();) {
				String s = (String) i.next();
				String[] sa = s.split(";");
				out.print("<option value=\"" + s + "\"");
				if (selectionValue.length() >= 3) {
					selectionValue = selectionValue.substring(0, 3);
				}
				if (sa[0].equals(selectionValue)
						|| (selectionValue.equals(ForwardConstants.EMPTY) && sa[0]
								.equals("GBP"))) {
					out.print(" selected");
				}
				out.println(">" + sa[0] + "</option>");
			}
			out.print("</select>");
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
	}

	private void displaySeatClassSelector() throws JspException {
		SessionData sd = (SessionData) (pageContext.getSession()
				.getAttribute(ForwardConstants.SESSION_DATA));
		try {
			out.print("<select name=\"" + selectionName + "\"");
			if (!onChange.equals(ForwardConstants.EMPTY)) {
				out.print(" onChange='" + onChange + "'");
			}
			out.println(">");
			for (Iterator i = sd.getSeatClasses().iterator(); i.hasNext();) {
				String s = (String) i.next();
				out.print("<option value=\"" + s + "\"");
				if (s.equals(selectionValue)) {
					out.print(" selected");
				}
				out.println(">" + s + "</option>");
			}
			out.print("</select>");
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
	}

	private void displayAmendmentTypeSelector() throws JspException {
		SessionData sd = (SessionData) (pageContext.getSession()
				.getAttribute(ForwardConstants.SESSION_DATA));
		try {
			out.print("<select name=\"" + selectionName + "\"");
			if (!onChange.equals(ForwardConstants.EMPTY)) {
				out.print(" onChange='" + onChange + "'");
			}
			out.println(">");
			for (Iterator i = sd.getAmendmentTypes().iterator(); i.hasNext();) {
				String[] s = ((String) i.next()).split(";");
				if (s.length > 1) {
					out.print("<option value=\"" + s[0] + "\"");
					if (s.equals(selectionValue)) {
						out.print(" selected");
					}
					out.println(">" + s[1] + "</option>");
				}
			}
		
			out.print("</select>");
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
	}

	private void displayVersionsArray() throws JspException {
		SessionData sd = (SessionData) (pageContext.getSession()
				.getAttribute(ForwardConstants.SESSION_DATA));
		try {
			out.print("[");
			Map versions = sd.getVersions();
			boolean isFirst = true;
			for (Iterator s = versions.keySet().iterator(); s.hasNext();) {
				String season = (String) s.next();
				out.print((isFirst ? "" : ",") + "['" + season + "'");
				isFirst = false;
				for (Iterator v = ((LinkedHashSet) versions.get(season))
						.iterator(); v.hasNext();) {
					out.print(",'" + (String) v.next() + "'");
				}
				out.print("]");
			}
			out.print("];");
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
	}

	private void displayMealTypesSelector() throws JspException {
		SessionData sd = (SessionData) (pageContext.getSession()
				.getAttribute(ForwardConstants.SESSION_DATA));
		try {
			out.print("<select name=\"" + selectionName + "\"");
			if (!onChange.equals(ForwardConstants.EMPTY)) {
				out.print(" onChange='" + onChange + "'");
			}
			out.println(">");
			for (Iterator i = sd.getMealTypes().iterator(); i.hasNext();) {
				String s = (String) i.next();
				out.print("<option value=\"" + s + "\"");
				if (s.equals(selectionValue)) {
					out.print(" selected");
				}
				out.println(">" + s + "</option>");
			}
			out.print("</select>");
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
	}
	
	
	private void displayFlightTypesSelector() throws JspException {
		SessionData sd = (SessionData) (pageContext.getSession()
				.getAttribute(ForwardConstants.SESSION_DATA));
		try {
			out.print("<select name=\"" + selectionName + "\"");
			if (!onChange.equals(ForwardConstants.EMPTY)) {
				out.print(" onChange='" + onChange + "'");
			}
			out.println(">");
			for (Iterator i = sd.getFlightTypes().iterator(); i.hasNext();) {
				String s = (String) i.next();
				out.print("<option value=\"" + s + "\"");
				if (s.equals(selectionValue)) {
					out.print(" selected");
				}
				out.println(">" + s + "</option>");
			}
			out.print("</select>");
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
	}
	
	private void listFlightTypesSelector() throws JspException {
		SessionData sd = (SessionData) (pageContext.getSession()
				.getAttribute(ForwardConstants.SESSION_DATA));
		try {
			//out.print("var ele = document.createElement('select')");
			int cnt = 0;
			for (Iterator i = sd.getFlightTypes().iterator(); i.hasNext();) {
				cnt = cnt+1;
				String s = (String) i.next();
				out.println("var opt = ele.appendChild(document.createElement('option'));");
				out.println("opt.text = '"+s+ "';");
				out.println("opt.value = '"+s+ "';");
			}
				  
					
				
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
	}
	
	private void buildCustSuppArray(boolean customer) throws JspException {
		FlightBotFormBean fbb = (FlightBotFormBean) pageContext.getRequest()
				.getAttribute("flightbotbean");
		try {
			if (fbb == null
					|| (customer && fbb.getCustomerAllocation() == null)
					|| (!customer && fbb.getSupplierAllocation() == null)) {
				out.print("[]");
			} else {
				Collection csColl = customer ? fbb.getCustomerAllocation()
						: fbb.getSupplierAllocation();
				out.print("[");
				String prevCS = ForwardConstants.EMPTY;
				for (Iterator i = csColl.iterator(); i.hasNext();) {
					AllocationBean ab = (AllocationBean) i.next();
					if (prevCS.equals(ab.getCustomerCode())) { // same cust/supp
						// name
						out.println("],"); // end prev, but with more to come
					} else { // new customer/supplyer name
						if (!prevCS.equals(ForwardConstants.EMPTY)) { // if it's
							// not the
							// first
							// row
							out.println("]"); // end of last row for this cust
							out.println("],"); // end of prev customer
						}
						out.println("["); // start of new customer
					}
					prevCS = ab.getCustomerCode();
					out.print("["); // start of record
					out.print("'" + ab.getCustomerCode() + "',");
					out.print("'" + ab.getMinDepartureDate() + "',");
					out.print("'" + ab.getMaxDepartureDate() + "',");
					out.print("'" + ab.getGatewayFrom() + "-"
							+ ab.getGatewayTo() + "',");
					out.print("'" + ab.getSeatClass() + "',");
					out.print("'" + ab.getSeatClassDescription() + "',");
					out.print("'" + ab.getNumSeats() + "',");
					out.print("'");
					boolean isFirst = true;
					for (Iterator c = ab.getCurrencies().iterator(); c
							.hasNext();) {
						String s = (String) c.next();
						out.print((isFirst ? "" : "<br>") + s);
						isFirst = false;
					}
					out.print("','");
					isFirst = true;
					for (Iterator c = ab.getAmounts().iterator(); c.hasNext();) {
						String s = (String) c.next();
						out.print((isFirst ? "" : "<br>") + s);
						isFirst = false;
					}
					out.print("',");
					// Change by DSB for adding gemini
					out.print("'" + ab.getGeminiCode() + "',");
					out.print("'" + ab.getMealType() + "',");
					out.print("'" + ab.getContractID() + "',");
					out.print("'" + ab.getAllocationStatus() + "',");
					out.print("'" + ab.getBroker() + "',");
					out.print("'" + ab.getColourID() + "'");
				}
				if (!prevCS.equals(ForwardConstants.EMPTY))
					out.print("]]");
				out.print("]");
			}
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
	}

	private void buildFlightCodeNameList(final boolean doCode) throws JspException {
		HashMap flightCodes = (HashMap) (pageContext.getSession()
				.getAttribute(ForwardConstants.ALL_FLIGHTCODES));
		if (flightCodes != null) {
			try {
				boolean isFirst = true;
				Iterator i = (doCode ? (flightCodes.keySet().iterator())
						: (flightCodes.values().iterator()));
				while (i.hasNext()) {
					String value = (String) i.next();
					out.print((isFirst ? "" : ",") + (doCode ? "" : "\"")
							+ value + (doCode ? "" : "\""));
					isFirst = false;
				}
			} catch (IOException e) {
				throw new JspException(e.toString());
			}
		}
	}

	private void buildGatewayCodeNameList(final boolean doCode) throws JspException {
		HashMap gatewayCodes = (HashMap) (pageContext.getSession()
				.getAttribute(ForwardConstants.ALL_GATEWAYCODES));
		if (gatewayCodes != null) {
			try {
				boolean isFirst = true;
				Iterator i = doCode ? gatewayCodes.keySet().iterator()
						: gatewayCodes.values().iterator();
				while (i.hasNext()) {
					String value = (String) i.next();
					out.print((isFirst ? "" : ",") + (doCode ? "" : "\"")
							+ value + (doCode ? "" : "\""));
					isFirst = false;
				}
			} catch (IOException e) {
				throw new JspException(e.toString());
			}
		}
	}

	private void buildSearchCSV() throws JspException {
		HttpSession sess = pageContext.getSession();
		SearchFormBean sfb = (SearchFormBean) sess.getAttribute("searchform");
		if (sfb != null) {
			try {
				String s = "";
				if (sfb.getSearchType().equals(
						SearchFormBean.SERIES_SEARCH_TYPE)) {
					int maxCust = sfb.getMaxNumCustomers();
					s = "seriesNo:Int^route^dow^startDate:Date^endDate:Date^depTime^flightNumber^supplier^carrier^purchasedMin:Int^dPurchasedMin^purchasedMax:Int^dPurchasedMax^availableMin:Int^dAvailableMin^availableMax:Int^dAvailableMax";
					for (int i = 0; i < maxCust; i++) {
						s += ("^custName" + (i + 1) + "^custMinMax" + (i + 1)
								+ "^custKey" + (i + 1));
					}
				} else if (sfb.getSearchType().equals(
						SearchFormBean.CUSTOMER_SEARCH_TYPE)) {
					s = "seriesNo^gwFrom^gwTo^startDate:Date^endDate:Date^frequency^depTime^arrTime^flight^allocation:Int^dAllocation^seatClass^currency^fare^brokerCode^geminiCode^dayOfWeek";
				}
				System.out.println("s");
				out.print(s);
				for (Iterator i = sfb.getSearchResult().iterator(); i.hasNext();) {
					ISearchResult sr = (ISearchResult) i.next();
					out.print(sr.buildCSVString());
				}
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
				throw new JspException(e.toString());
			}
		}
	}

	/**
	 * @throws JspException
	 * @return
	 */
	public int doStartTag() throws JspException {
		out = pageContext.getOut();
		if (selectionType.startsWith("seasonSelector")) {
			displaySeasonSelector();
		} else if (selectionType.startsWith("restrictedseasonSelector")) {
				displayRestrcitedSeasonSelector();
		} else if (selectionType.startsWith("ynSelector")) {
			displayYNSelector();
		} else if (selectionType.startsWith("dowSelector")) {
			displayDOWSelector();
		} else if (selectionType.startsWith("currencySelector")) {
			displayCurrencySelector();
		} else if (selectionType.startsWith("seatClassSelector")) {
			displaySeatClassSelector();
		} else if (selectionType.startsWith("amendmentTypeSelector")) {
			displayAmendmentTypeSelector();
		} else if (selectionType.startsWith("environmentInfo")) {
			displayEnvironmentInfo();
		} else if (selectionType.startsWith("versionsArray")) {
			displayVersionsArray();
		} else if (selectionType.startsWith("listFlightCodes")) {
			buildFlightCodeNameList(true);
		} else if (selectionType.startsWith("listFlightNames")) {
			buildFlightCodeNameList(false);
		} else if (selectionType.startsWith("listGatewayCodes")) {
			buildGatewayCodeNameList(true);
		} else if (selectionType.startsWith("listGatewayNames")) {
			buildGatewayCodeNameList(false);
		} else if (selectionType.startsWith("customerArray")) {
			buildCustSuppArray(true);
		} else if (selectionType.startsWith("supplierArray")) {
			buildCustSuppArray(false);
		} else if (selectionType.startsWith("searchCSV")) {
			buildSearchCSV();
		} else if (selectionType.startsWith("mealTypeSelector")) {
		displayMealTypesSelector();
		}
	 else if (selectionType.startsWith("flightTypeSelector")) {
		displayFlightTypesSelector();
		}
	 else if (selectionType.startsWith("listFlightTypeSelector")) {
		listFlightTypesSelector();
		}
	 

		return SKIP_BODY;
	}

}
