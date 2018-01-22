<%@ page errorPage="/ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-nested.tld" prefix="nested" %>
<%@ taglib uri="/WEB-INF/tlds/firstchoice-tags.tld" prefix="fctags" %>
<%@ page import="uk.co.firstchoice.viking.pax.gui.utils.StringConstants"%>
<html:xhtml/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<html:base/>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>PAX Management</title>
	<link href="<html:rewrite page="/css/firstchoice.css"/>" type="text/css" rel="stylesheet"/>
<title>PAX Load Report</title>
</head>
<body class="single">
<div class="head">
<table class="page" align="center">
	<tbody>
		<tr><td><img src="<html:rewrite page="/images/fc_logo.gif"/>" alt="First Choice logo"/>&nbsp;:: PAX Management: Passenger Load Report</td><td>&nbsp;:: User:<%= request.getRemoteUser() %></td></tr>
		<tr></tr>
	</tbody>
</table>
</div>
<table cellpadding="0" cellspacing="0" border="0" width="600">
  <thead>
<tr>
  <th width="100">Departure Date</th>
  <th width="40">Flight #</th>
  <th width="120">Route</th>
  <th width="40">Class</th>
  <th width="40">Allocation</th>
  <th width="80">T/O Code</th>
  <th></th>
  <th>
	<table align="left"  border="0" width="180">
		<thead>
		<tr>
		  <th width="40">Updated Adults&nbsp;&nbsp;&nbsp;&nbsp;</th>
		  <th width="40">Updated Child&nbsp;&nbsp;</th>
		  <th width="40">Updated Infant&nbsp;&nbsp;</th>
		  <th width="40">Updated Total&nbsp;&nbsp;</th>
		</tr>
		</thead>
	</table>
   </th>
  </tr>
  </thead>
  <tbody id="defTblBdy">
	<logic:present name="<%=StringConstants.PAX_RESULTS%>">
	<%
	String prevSeries = "0"; 
	String flightNo = "0"; 
	String prevFlightNo = "0"; 
	int totalAdult = 0;
	int totalChild = 0;
	int totalInft = 0;
	int total = 0;
	int allocationItemId = 0;
	%>
    <logic:iterate id="paxItem" name="<%=StringConstants.PAX_RESULTS%>" type="uk.co.firstchoice.viking.pax.gui.search.PaxItemBean" >
		<% if ( prevFlightNo.compareTo("0") != 0  ) { %>   

	     <logic:notEqual name="paxItem" property="flightNumberRoute" value="<%=prevFlightNo%>">
			<tr class='hilite'>
				<td colspan="7" align="right">Total:</td>
				<td width="180">
					<table align="left" width="180" border="0">
						<tr>
						  <td width="40" align="right"><b><%=totalAdult %></b></td>
						  <td width="40" align="right"><b><%=totalChild %></b></td>
						  <td width="40" align="right"><b><%=totalInft %></b></td>
						  <td width="40" align="right"><b><%=totalAdult %></b></td>
						</tr>
					</table>
				</td>
			</tr>
			<%
			totalAdult = 0;
			totalChild  = 0;
			totalInft  = 0;
			total  = 0;		
			%>
			<tr></tr>
		  </logic:notEqual>
		<%}%>

		  <tr onmouseover="this.className='hilite';" onmouseout="this.className='plain';">
		   	  <logic:notEqual name="paxItem" property="flightNumberRoute" value="<%=prevFlightNo%>">
			   <td width="100" align="center"><bean:write filter="false" name="paxItem" property="departureDate"/></td>
			    <td width="40" class="numeric" align="center"><bean:write filter="false" name="paxItem" property="flightNumber"/></td>
			    <td width="120" align="center"><bean:write filter="false" name="paxItem" property="route"/></td>
				<td width="40" align="center"><bean:write filter="false" name="paxItem" property="seatClass"   /></td>
				<td width="40" align="center"><bean:write filter="false" name="paxItem" property="allocation"  /></td>
				<td width="80" align="center"><bean:write filter="false" name="paxItem" property="customerCode"/></td>
				<td></td>
			 </logic:notEqual> 
		   	 <logic:equal name="paxItem" property="flightNumberRoute" value="<%=prevFlightNo%>">
				<td colspan="3"></td>
				<td width="40" align="center"><bean:write filter="false" name="paxItem" property="seatClass"   /></td>
				<td width="40" align="center"><bean:write filter="false" name="paxItem" property="allocation"  /></td>
				<td width="80" align="center"><bean:write filter="false" name="paxItem" property="customerCode"/></td>
				<td></td>
			 </logic:equal> 
		    <logic:iterate name="paxItem" property="allocationItems" id="allocationItem" type="uk.co.firstchoice.viking.pax.gui.search.PaxAllocationItemBean" >
			 	
				<logic:equal name="allocationItem" property="paxStatus" value="2">
				
					<td width="180">
						<table align="left" width="180" border="0">
							<tr>
							  <td width="40" align="right"><bean:write name="allocationItem" property='inAdults' 		/></td>
							  <td width="40" align="right"><bean:write name="allocationItem" property='inChildren' 		/></td>
							  <td width="40" align="right"><bean:write name="allocationItem" property='inInfants' 		/></td>
							  <td width="40" align="right"><bean:write name="allocationItem" property='totalAllocation' /></td>
							</tr>
						</table>
					</td>
					<%
					allocationItemId = allocationItem.getAllocationId();
					totalAdult = totalAdult + allocationItem.getInAdults();
					totalChild  = totalChild + allocationItem.getInChildren();	
					totalInft  = totalInft + allocationItem.getInInfants();
					total  = totalAdult + allocationItem.getTotalAllocation();		
					%>
				</logic:equal>
			 </logic:iterate>
		  </tr>
		  <% 
			prevSeries = paxItem.getSeries();
			prevFlightNo = paxItem.getFlightNumberRoute();
		  %>
	   </logic:iterate>
		<logic:present name="paxItem">
			<tr class='hilite'>
				<td colspan="7" align="right">Total:</td>
				<td width="180">
					<table align="left" width="180" border="0">
						<tr>
						  <td width="40" align="right"><b><%=totalAdult %></b></td>
						  <td width="40" align="right"><b><%=totalChild %></b></td>
						  <td width="40" align="right"><b><%=totalInft %></b></td>
						  <td width="40" align="right"><b><%=totalAdult %></b></td>
						</tr>
					</table>
				</td>
			</tr>
			<%
			totalAdult = 0;
			totalChild  = 0;
			totalInft  = 0;
			total  = 0;		
			%>
			<tr></tr>
		</logic:present>
	</logic:present>
  </tbody>
</table>
</body>
</html>