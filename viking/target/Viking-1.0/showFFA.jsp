<%@ page errorPage="/ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="uk.co.firstchoice.fcutil.*" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<jsp:useBean	id="ffabean"
							class="uk.co.firstchoice.viking.gui.FFABean"
							scope="request"/>
<%
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
	String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
	Calendar cal = Calendar.getInstance();
%>
<html>
<head>
<title>Flight Fixture Advice for series <%= request.getParameter("seriesNo") %></title>
<link href="<html:rewrite page="/css/viking.css"/>" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="<html:rewrite page="/table.js"/>"></script>

<script type="text/javascript" src="<html:rewrite page="/validation.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/utils.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/datepicker.js"/>"></script>

<style>
	th { background-color: white; color: black; font-weight: bold; text-align: left; vertical-align: top; font-size: 7pt; }
	.outer { background-color: white; border: 1px solid black; width: 100%;}
	.outer td { border: 1px solid black; text-align: left; padding: 3px; }
	.tal { text-align: left; }
	.tar { text-align: right; }
	.inner { border: 0px; width: 100%; }
	.inner td { vertical-align: top; border: 0px; font-size: 7pt; }
	.inner th { padding: 3px; }
	.rotations { border: 0px; width: auto; }
	.rotations td { vertical-align: top; border: 0px solid black; font-size: 7pt; padding: 2px 8px; }
	.rotations th { padding: 3px 8px; }
	.sharersTable { border: 0px; }
	.sharersTable td { vertical-align: top; border: 0px; font-size: 7pt; text-align: center; padding: 0px 15px; }
	.sharersTable th { vertical-align: bottom; padding: 3px; }
	.sectionHd { font-size: 9pt; font-weight: bold; padding: 3px; }
	.darker { background-color: #eeeeee; }
@media print {
	.noPrint { display: none; }
	.darker { background-color: black; color: white;}
}
</style>
</head>

<body>
<table cellspacing="0" class="outer">
	<tr class="noPrint"><td style="padding: 4px; text-align: right;">
		<a href="#" onClick="print();">Print</a>
	</td></tr>
	
	<tr><td>
		<table cellspacing="0" class="inner" style="width: 100%;">
			<tr>
				<td class="tal" style="font-size: 12pt; font-weight: bold; ">
					Flight Fixture Advice for series <%= request.getParameter("seriesNo") %>
				</td>
				<td style="text-align: right; vertical-align: bottom;">
					<%= java.text.DateFormat.getInstance().format(new java.util.Date()) %> by <%= request.getRemoteUser() %>
				</td>
			</tr>
		</table>
	</td></tr>
	
	<tr><td>
		<table cellspacing="0" class="inner">
			<tr>
				<th style="text-align: right;">Version:</th><td><bean:write name="ffabean" property="version" /></td>
				<th style="text-align: right;">Start:</th><td><bean:write name="ffabean" property="seriesStartDate" /></td>
				<th style="text-align: right;">End:</th><td><bean:write name="ffabean" property="seriesEndDate" /></td>
				<th style="text-align: right;">Season:</th><td><bean:write name="ffabean" property="season" /></td>
				<th style="text-align: right;">Day:</th><td><bean:write name="ffabean" property="seriesDayOfWeek" /></td>
			</tr>
			<tr>
				<th style="text-align: right;">Created:</th><td><bean:write name="ffabean" property="seriesCreatedDate" /></td>
				<th style="text-align: right;">Gateways:</th>
				<td>
					<script>document.write('<bean:write name="ffabean" property="seriesGateways" />'.replace(/;/g, '<br/>'));</script>
				</td>
				<th style="text-align: right;">Freq.:</th><td><bean:write name="ffabean" property="seriesFrequency" /></td>
				<th style="text-align: right;">Rotations:</th><td><bean:write name="ffabean" property="seriesNoRotations" /></td>
				<th style="text-align: right;">Flights:</th><td><bean:write name="ffabean" property="seriesNoFlights" /></td>
			</tr>
		</table>
	</td></tr>
	
	<tr><td>
		<table cellspacing="0" class="rotations">
			<tr><th colspan="4" class="sectionHd" style="padding: 3px;">Rotations by Period</th></tr>
			<tr>
				<th>Flight#</th>
				<th>Carrier</th>
				<th>From</th>
				<th>Dep.</th>
				<th>Arr.</th>
				<th>To</th>
				<th>A/C</th>
				<th>Day</th>
				<th>Dates of operation</th>
			</tr>
<%
	if (ffabean.getRotationSectors().size() == 0) {
		out.print("<tr><td colspan='6'>-- None --</td></tr>");
	} else {
		Collection sectors;
		Collection dates;
		Map rotationSectors = ffabean.getRotationSectors();
		for (Iterator i = ffabean.getAllKeys().iterator(); i.hasNext(); ) {
			String key = (String)i.next();
			sectors = (Vector)rotationSectors.get(key);
			dates = (TreeSet)(ffabean.getSectorDates().get(key));
			int numMonths = 0;
			int currentMonth = -1;
			for (Iterator di = dates.iterator(); di.hasNext(); ) {
				cal.setTime((java.sql.Date)di.next());
				if (cal.get(cal.MONTH) != currentMonth) {
					currentMonth = cal.get(cal.MONTH);
					numMonths++;
				}
			}
			int rowNo = 0;
			for (Iterator si = sectors.iterator(); si.hasNext(); ) {
				uk.co.firstchoice.viking.gui.FFABean.Sector sector = (uk.co.firstchoice.viking.gui.FFABean.Sector)si.next();
				out.print("<tr><td>"+sector.getFlightCode()+sector.getFlightNumber()+"</td>");
				out.print("<td>"+sector.getCarrier()+"</td>");
				out.print("<td>"+sector.getGwFrom()+"</td>");
				out.print("<td>"+sector.getDepTime()+"</td>");
				out.print("<td>"+sector.getArrTime()+"</td>");
				out.print("<td>"+sector.getGwTo()+"</td>");
				out.print("<td>"+sector.getAircraftType()+"</td>");
				out.print("<td>"+sector.getDow()+"</td>");
				if (rowNo++ == 0) {
					out.print("<td rowspan='"+numMonths+"' style='padding: 0px;'><table class='rotations'>");
					
					if (dates.size() == 0) {
						out.print("<tr><td colspan='6'>-- None --</td></tr>");
					} else {
						currentMonth = -1;
						for (Iterator di = dates.iterator(); di.hasNext(); ) {
							cal.setTime((java.sql.Date)di.next());
							if (cal.get(cal.MONTH) != currentMonth) {
								currentMonth = cal.get(cal.MONTH);
								out.print("<tr><td>"+months[currentMonth]+"</td>");
							}
							out.print("<td>"+FCUtils.leftPad(cal.get(cal.DAY_OF_MONTH), 2)+"</td>");
						}
						out.print("</tr>");
					}
					
					out.print("</table></td>");
				}
				out.println("</tr>");
			}
			for (int x = 0; x < numMonths-rowNo; x++) {
				out.print("<tr><td>&nbsp;</td></tr>");
			}
			out.print("<tr><td>&nbsp;</td></tr>");
		}
	}
%>
			<tr>
				<td colspan="8"></td><th style="text-align: left;">Deleted Rotations</th>
			</tr>
<%
	Collection canSec = ffabean.getCancelledSectors();
	if (canSec.size() == 0) {
		out.print("<tr><td colspan='8'></td><td>-- None --</td></tr>");
	} else {
		int currentMonth = -1;
		out.print("<tr><td colspan='8'></td><td style='padding: 0px;'><table class='rotations'>");
		for (Iterator i = canSec.iterator(); i.hasNext(); ) {
			cal.setTime((java.sql.Date)i.next());
			if (cal.get(cal.MONTH) != currentMonth) {
				currentMonth = cal.get(cal.MONTH);
				out.print("<tr><td>"+months[currentMonth]+"</td>");
			}
			out.print("<td>"+FCUtils.leftPad(cal.get(cal.DAY_OF_MONTH), 2)+"</td>");
		}
		out.print("</tr></table></td></tr>");
	}
%>			
		</table>
	</td></tr>
	
	<tr><td>
		<table cellspacing="0" class="sharersTable">
			<tr><th colspan="99" class="sectionHd"><%= "S".equals(ffabean.getType())?"Sharers":"Purchases"  %></th></tr>
			<tr>
				<th style="text-align: center;">Date</th>
				<th style="text-align: center;">Sector</th>
				<logic:iterate	id="allocation"
												name="ffabean"
												property="allSharers">
					<th style="text-align: center;"><script>document.write('<bean:write name="allocation" />'.replace(';','<br/>'))</script></th>
				</logic:iterate>
			</tr>
<%
	Collection allSharers = ffabean.getAllSharers();
	TreeMap dateMap = ffabean.getSharerAllocations();
	if (dateMap.size() == 0) {
		out.print("<tr><td colspan='2'>-- No Sharers --</td></tr>");
	} else {
		boolean odd = true;
		for (Iterator di = dateMap.keySet().iterator(); di.hasNext(); ) {
			java.sql.Date date = (java.sql.Date)di.next();
			TreeMap sectorMap = (TreeMap)dateMap.get(date);
			odd = !odd;
			for (Iterator si = sectorMap.keySet().iterator(); si.hasNext(); ) {
				String sector = (String)si.next();
				Map sharerMap = (Hashtable)sectorMap.get(sector);
				out.print("<tr"+(odd?" class='darker'":"")+"><td>"+sdf.format(date)+"</td><td>"+sector.split(";")[1]+"</td>");
				for (Iterator ai = allSharers.iterator(); ai.hasNext(); ) {
					String sharer = (String)ai.next();
					out.print("<td>");
					if (sharerMap.containsKey(sharer)) {
						out.print(((Integer)sharerMap.get(sharer)).intValue());
					}
					out.print("</td>");
				}
				out.println("</tr>");
			}
		}
	}
%>			
		</table>
	</td></tr>
	
	<tr><td>
		<table cellspacing="2" class="inner">
			<tr><th colspan="99" class="sectionHd">Amendments</th></tr>
			<tr>
				<th>Date</th>
				<th>User</th>
				<th>Type</th>
				<th>Comment</th>
			<tr>
			<logic:iterate	id="comment"
											name="seriescomments"
											type="uk.co.firstchoice.viking.gui.CommentHolderBean">
				<tr>
					<td nowrap="true"><bean:write name="comment" property="date" /></td>
					<td nowrap="true"><bean:write name="comment" property="user" /></td>
					<td nowrap="true"><bean:write name="comment" property="amendmentType" /></td>
					<td><bean:write name="comment" property="comments" /></td>
				</tr>
			</logic:iterate>
		</table>
	</td></tr>

	</table>
</body>
</html>
