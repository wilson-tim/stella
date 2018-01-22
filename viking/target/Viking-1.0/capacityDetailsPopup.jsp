<%@ page errorPage="/ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<jsp:useBean	id="displaycapacityform"
							class="uk.co.firstchoice.viking.gui.DisplayCapacityFormBean"
							scope="request" />
<html>
<head>
<title><bean:write name="displaycapacityform" property="type" /> Allocation Details</title>
<link href="<html:rewrite page="/css/viking.css"/>" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="<html:rewrite page="/table.js"/>"></script>
<style>
td {
	text-align: center;
	border: 1px solid darkblue;
	background-color: white;
	color: darkblue;
	font-size: 8pt;
}
th {
	text-align: center;
	border: 1px solid darkblue;
	background-color: #f0f0f0;
	color: darkblue;
	font-weight: normal;
	font-size: 8pt;
}
</style>
</head>

<body style="text-align: center;">
<h3><bean:write name="displaycapacityform" property="type" /> Allocation Details<br/>
for Series <bean:write name="displaycapacityform" property="seriesNo" /></h3>
<table cellspacing="0" cellpadding="1" border="0">
<%
	java.util.Map capacityMap = displaycapacityform.getCapacityDetails();
	boolean firstRow = true;
	for (java.util.Iterator dates = capacityMap.keySet().iterator(); dates.hasNext(); ) {
		String date = (String)dates.next();
		java.util.Map sectorMap = (java.util.Map)capacityMap.get(date);
		if (firstRow) {
			firstRow = false;
			out.print("<tr><th>&nbsp;</th>");
			for (java.util.Iterator sectors = sectorMap.keySet().iterator(); sectors.hasNext(); ) {
				out.print("<th>"+(String)sectors.next()+"</th>");
			}
			out.println("</tr>");
		}
		out.print("<tr><th>"+date+"</th>");
		for (java.util.Iterator capacities = sectorMap.values().iterator(); capacities.hasNext(); ) {
			String capacity = (String)capacities.next();
			out.print("<th>"+(capacity.equals("-99999")?"&nbsp;":capacity)+"</th>");
		}
		out.print("</tr>");
	}
%>
</table>
</body>
</html>
