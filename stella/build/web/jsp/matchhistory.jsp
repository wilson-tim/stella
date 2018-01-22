<%@ page errorPage="jsp/ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" session="true" %>
<%@ taglib uri="utiltags" prefix="util" %>
<jsp:useBean id="matchhistory" class="uk.co.firstchoice.stella.frontend.BeanCollectionBean" scope="request"/>
<html>
<head>
<title>Match History</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="stella.css" rel="stylesheet" type="text/css">
<script src="validation.js"></script>
</head>

<body>
<h3>Match History</h3>
<p/><hr/>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td style="text-align: center;">
      <button accesskey="c" onClick="window.close();"><u>C</u>lose</button>
    </td>
  </tr>
</table>
<hr/><p/>
<h6><jsp:getProperty name="matchhistory" property="error" /></h6>
<% int lineNo = 0; %>
<table cellspacing="0" class="dataTable">
	<tr>
		<th>#</th>
		<th>Match<br/>Process</th>
		<th>Match<br/>Process<br/>Date</th>
		<th>Reason<br/>Code</th>
		<th>Reason<br/>Desc.</th>
		<th>Amended<br/>By</th>
		<th>DW<br/>Seat<br/>Cost</th>
		<th>DW<br/>Tax<br/>Cost</th>
		<th>DW<br/>Total<br/>Cost</th>
		<th>Stella<br/>Seat<br/>Cost</th>
		<th>Stella<br/>Tax<br/>Cost</th>
		<th>Stella<br/>Total<br/>Cost</th>
		<th>UN-<br/>matched<br/>Amount</th>
	</tr>
	
	<% if (matchhistory.getBeanCollection().size() > 0 ) { %>
<util:iterate id="hist" type="uk.co.firstchoice.stella.frontend.MatchHistoryBean" collection="<%= matchhistory.getBeanCollection() %>" >
	<tr class="<%="line"+(lineNo%2) %>">
		<td><%= ++lineNo %></td>
		<td><jsp:getProperty name="hist" property="processCode" /></td>
		<td><jsp:getProperty name="hist" property="processDate"/></td>
		<td><jsp:getProperty name="hist" property="reasonCode"/></td>
		<td><jsp:getProperty name="hist" property="reasonShortDesc"/></td>
		<td><jsp:getProperty name="hist" property="amendedUserId"/></td>
		<td><jsp:getProperty name="hist" property="dwSeatCost"/></td>
		<td><jsp:getProperty name="hist" property="dwTaxCost"/></td>
		<td><jsp:getProperty name="hist" property="dwTotalCost"/></td>
		<td><jsp:getProperty name="hist" property="stellaSeatCost"/></td>
		<td><jsp:getProperty name="hist" property="stellaTaxCost"/></td>
		<td><jsp:getProperty name="hist" property="stellaTotalCost"/></td>
		<td><jsp:getProperty name="hist" property="unmatchedAmount"/></td>
	</tr>
</util:iterate>
<% } %>
</table>
<%= matchhistory.getBeanCollection().size() == 0?"<p/><h6>No history found</h6>":"" %>
</form>
</body>
</html>
