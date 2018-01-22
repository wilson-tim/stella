<%@ page errorPage="ErrorPage.jsp" %>
<%@ page import="java.util.*" session="true" %>
<%@ taglib uri="stellatags" prefix="stella" %>
<jsp:useBean id="ticket" class="uk.co.firstchoice.stella.frontend.TicketBean" scope="request"/>
<html>
<head>
<title>View Ticket</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="/stella/stella.css" rel="stylesheet" type="text/css">
</head>

<body>
<script>
window.moveTo(	parseInt((window.screen.availWidth-document.body.offsetWidth) / 2),
				parseInt((window.screen.availHeight-document.body.offsetHeight) / 2));
</script>
<h3>View Ticket</h3>
  <table class="displaydetails" width="100%" border="0" cellspacing="2">
    <tr>
      <th>Document Type:</th>
      <td><jsp:getProperty name="ticket" property="ticketType" /></td>
    </tr>
    <tr>
      <th>Airline number:</th><td><jsp:getProperty name="ticket" property="airlineNumber" /></td>
      <th>Ticket Number:</th><td><jsp:getProperty name="ticket" property="ticketNumber" /></td>
      <th>IATA number:</th><td><jsp:getProperty name="ticket" property="iataNumber" /></td>
    </tr>
    <tr>
      <th>E-Ticket:</th><td><%= ticket.getEticket().equals("Y")?"Yes":"No" %></td>
      <th>Departure Date:</th><td><jsp:getProperty name="ticket" property="departureDate" /></td>
      <th>Ticket Issue Date:</th><td><jsp:getProperty name="ticket" property="issueDate" /></td>
    </tr>
    <tr>
      <th>Ticketing Agent Init.:</th><td><jsp:getProperty name="ticket" property="agentInit" /></td>
      <th>PNR:</th><td><jsp:getProperty name="ticket" property="pnr" /></td>
      <th>Passenger Type:</th><td><%= ticket.getPassengerType().startsWith("A")?"Adult":ticket.getPassengerType().startsWith("C")?"Child":"Infant" %></td>
    </tr>
    <tr>
      <th>Passenger Name:</th><td colspan="2"><jsp:getProperty name="ticket" property="passengerName" /></td>
      <th colspan="2">Number of Passengers:</th><td><jsp:getProperty name="ticket" property="numPassengers" /></td>
    </tr>
    <tr>
      <th>Ticket Source Ind.:</th><td><jsp:getProperty name="ticket" property="sourceIndicator" /></td>
      <th>Exchange Ticket No.:</th><td><jsp:getProperty name="ticket" property="exchangeTicketNo" />&nbsp;</td>
<% if (!ticket.getConjunctionMaster().equals("")) { %>
      <th>Conjunction Ticket Master:</th><td><jsp:getProperty name="ticket" property="conjunctionMaster" />&nbsp;</td>
<% } %>
    </tr>
    <tr>
      <th>&nbsp;</th>
    </tr>
    <tr>
      <th>PNR Creation Date:</th>
      <td><jsp:getProperty name="ticket" property="pnrDate"/> </td>
      <th>Published Fare Amount:</th><td style="text-align: right;"><jsp:getProperty name="ticket" property="publishedFare" /></td>
      <th>GB Tax:</th><td style="text-align: right;"><jsp:getProperty name="ticket" property="gbTax" /></td>
    </tr>
    <tr>
      <th></th><th></th>
      <th>Selling Fare Amount:</th><td style="text-align: right;"><jsp:getProperty name="ticket" property="sellingFare" /></td>
      <th>UB Tax:</th><td style="text-align: right;"><jsp:getProperty name="ticket" property="ubTax" /></td>
    </tr>
    <tr>
      <th>Commission Pct:</th><td style="text-align: right;"><jsp:getProperty name="ticket" property="commissionPct" /> %</td>
      <th>Commission Amount:</th><td style="text-align: right;"><jsp:getProperty name="ticket" property="commissionAmt" /></td>
      <th>Remaining Tax:</th><td style="text-align: right;"><jsp:getProperty name="ticket" property="remainingTax" /></td>
    </tr>
    <tr>
      <th></th><th></th>
      <th style="border-top: 1px solid black;">Total Seat Cost:</th>
      <td style="border-top: 1px solid black; text-align: right;"><jsp:getProperty name="ticket" property="seatCostString" /></td>
      <th style="border-top: 1px solid black;">Total Tax Cost:</th>
      <td style="border-top: 1px solid black; text-align: right;"><jsp:getProperty name="ticket" property="totalTaxString" /></td>
    </tr>
    <tr>
      <th>&nbsp;</th>
    </tr>
    <tr>
      <th>Tour Code:</th><td><jsp:getProperty name="ticket" property="tourCode" /></td>
      <th>Fare Basis Code:</th><td><jsp:getProperty name="ticket" property="fareBasisCode" /></td>
    </tr>
    <tr>
      <th>ATOP Reference:</th><td><jsp:getProperty name="ticket" property="atopReference" /></td>
      <th>ATOP Season:</th><td><jsp:getProperty name="ticket" property="atopSeason" /></td>
      <th>Branch Code:</th><td><jsp:getProperty name="ticket" property="branchCode" /></td>
    </tr>
  </table>


   
<% if (request.isUserInRole("STELLA_SUPERVISOR")) { %>
	<p/>
	<table class="displaydetails" width="100%" border="0" cellspacing="2">
			<tr>
				<th>Update History</th>
			</tr>
			<tr>
				<th width="12%">Created By:</th><td width="13%"><jsp:getProperty name="ticket" property="createdBy" /></td>
				<th width="12%">Created On:</th><td width="13%"><jsp:getProperty name="ticket" property="createdOn" /></td>
				<th width="12%">Last Edit By:</th><td width="13%"><jsp:getProperty name="ticket" property="lastEditedBy" />&nbsp;</td>
				<th width="12%">Last Edit On:</th><td width="13%"><jsp:getProperty name="ticket" property="lastEditedOn" />&nbsp;</td>
			</tr>
	</table>
<% } %>

  <p/><hr/>
  <table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td style="text-align: center;">
        <button accesskey="e" onClick="window.location='/stella/stellabsp?cmd=editdocument&lineno=<%= request.getParameter("lineno") %>';"><u>E</u>dit</button>
<%  if (request.isUserInRole("STELLA_SUPERVISOR")) { %>
        <button name="Delete" style="color : red;" onClick="if (confirm('Are you sure you wish to completely remove this ticket?')) {window.location='/stella/stellabsp?cmd=deleteticket&ticketno=<jsp:getProperty name="ticket" property="ticketNumber" />'; };">Delete</button>
<% } %>
	    <button accesskey="c" onClick="window.close();"><u>C</u>ancel</button>
	  </td>
    </tr>
  </table>
  <hr/>
</body>
</html>
