<%@ page errorPage="jsp/ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" session="true" %>
<%@ taglib uri="utiltags" prefix="util" %>
<jsp:useBean id="refund" class="uk.co.firstchoice.stella.frontend.RefundBean" scope="request" />
<jsp:useBean id="rtcb" class="uk.co.firstchoice.stella.frontend.RefundTicketCollectionBean" scope="session" />
<html>
<head>
<title>View Refund</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="stella.css" rel="stylesheet" type="text/css">

<script>
function viewTicket(lineno) {
	var winWidth = 750;
	var winHeight = 390;
	window.showModalDialog('/stella/stellabsp?cmd=viewrefundticket&ticketLineSelect='+lineno+
							'&docType=<%= refund.getDocumentType() %>', '', 'dialogHeight:390px;'+
							'dialogWidth:750px;help:no;scroll:no;status:no');
}
</script>

</head>
<% boolean ACM = refund.getDocumentType().equals("ACM"); %>
<% boolean ADM = refund.getDocumentType().equals("ADM"); %>
<% boolean MAN = refund.getDocumentType().equals("MAN"); %>
<% boolean MRN = refund.getDocumentType().equals("MRN"); %>
<body>
<script>
window.moveTo(	parseInt((window.screen.availWidth-document.body.offsetWidth) / 2),
				parseInt((window.screen.availHeight-document.body.offsetHeight) / 2));
</script>
<h3>View Refund</h3>
    <table class="displaydetails" width="100%" border="0" cellspacing="0">
      <tr>
        <th>Document Number:</th><td><%= refund.getDocumentNumber() %></td>
        <th>Document Type:</th><td><%= refund.getDocumentType() %></td>
        <th>Document Date:</th><td><%= refund.getDocumentDate() %></td>
        <th>Airline:</th><td><%= rtcb.getAirlineName() %></td>
      </tr>
      <tr>
        <th colspan="4"></th>
        <th>IATA No.</th><td><%= refund.getIataNumber() %></td>
        <th>Document Total:</th>
        <td><%= rtcb.getDocumentTotal() %></td>
      </tr>
<% if (ADM) { %>
      <tr>
        <th>Dispute ADM:</th>
        <td><%= refund.getDisputeAdm() %></td>
   <% if ("Y".equalsIgnoreCase(refund.getDisputeAdm())) { %>
        <th>Dispute Date:</th>
        <td><%= refund.getDisputeDate() %></td>
   <% } else { %>
        <th colspan="2"></th>
   <% } %>
        <th>Reason:</th>
        <td colspan="3"><%= refund.getAdmReasonText() %></td>
      </tr>
<% } %>
<% if (request.isUserInRole("STELLA_SUPERVISOR")) { %>
			<tr>
				<th>Created By:</th><td><%= refund.getCreatedBy() %>&nbsp;</td>
				<th>Created On:</th><td><%= refund.getCreatedOn() %>&nbsp;</td>
				<th>Last Edit By:</th><td><%= refund.getLastEditedBy() %>&nbsp;</td>
				<th>Last Edit On:</th><td><%= refund.getLastEditedOn() %>&nbsp;</td>
			</tr>
<% } %>
    </table>
    <p/><hr/>
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr><td style="text-align: center;">
        <button accesskey="e" onClick="window.location='/stella/stellabsp?cmd=editdocument&lineno=<%= request.getParameter("lineno") %>';"><u>E</u>dit</button>
        <button accesskey="c" onClick="window.close();"><u>C</u>ancel</button>
      </td></tr>
    </table>
    <hr/>
<% int lineNo = 0; %>
	<table cellspacing="0" class="dataTable">
		<tr>
			<th>#</th>
			<th>Ticket No</th>
			<%= ACM || ADM || MRN?"<th>Airline Penalty</th>":"" %>
			<%= MRN?"<th>Fare Used</th>":"" %>
			<%= ACM || ADM || MAN?"<th>Seat Cost</th>":"" %>
			<%= MRN?"<th>Tax Adjust</th>":"" %>
			<%= ACM || ADM || MAN?"<th>Tax Cost</th>":"" %>
			<%= MRN?"<th>Total Refund</th>":"" %>
			<%= ACM || ADM || MAN?"<th>Total Cost</th>":"" %>
		</tr>
	

<% if (rtcb.getRefundTickets().size() > 0 ) { %>

<util:iterate id="ticket" type="uk.co.firstchoice.stella.frontend.RefundTicket" collection="<%= rtcb.getRefundTickets()%>" >
		<tr class="<%="line"+(lineNo%2) %>" onClick="viewTicket(<%= lineNo %>);">
			<td><%= ++lineNo %></td>
			<td><jsp:getProperty name="ticket" property="ticketNumber"/></td>
			<%= ACM || ADM || MRN?"<td>"+ticket.getAirlinePenaltyString()+"</td>":"" %>
			<%= MRN?"<td>"+ticket.getFareUsedString()+"</td>":"" %>
			<%= ACM || ADM || MAN?"<td>"+ticket.getSeatCostString()+"</td>":"" %>
			<%= MRN?"<td>"+ticket.getTaxAdjustString()+"</td>":"" %>
			<%= ACM || ADM || MAN?"<td>"+ticket.getTaxCostString()+"</td>":"" %>
			<td><jsp:getProperty name="ticket" property="documentTotal"/></td>
<%--
			<%= MRN?"<td>"+ticket.getTotalRefundString()+"</td>":"" %>
			<%= ACM || ADM || MAN?"<td>"+ticket.getTotalCostString()+"</td>":"" %>
--%>
		</tr>
</util:iterate>
 <% } %>
	</table>
	</div>    
</body>
</html>
