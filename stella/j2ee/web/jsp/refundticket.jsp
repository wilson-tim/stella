<%@ page errorPage="ErrorPage.jsp" %>
<%@ page import="java.util.*" session="true" %>
<%@ taglib uri="stellatags" prefix="stella" %>
<jsp:useBean id="ticket" class="uk.co.firstchoice.stella.frontend.TicketBean" scope="session"/>
<jsp:useBean id="rtcb" class="uk.co.firstchoice.stella.frontend.RefundTicketCollectionBean" scope="session"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="stella.css" rel="stylesheet" type="text/css">
<script src="validation.js"></script>
<script>
var frm;

function updateTotal() {
	frm = document.forms[0];
	var sign = <%= request.getParameter("doctype").equals("ACM")?"-1":"1" %>;
	var fareUsed = 0.0;
	var airlinePenalty = 0.0;
	var taxAdjust = 0.0;
	var seatCost = 0.0;
	var taxCost = 0.0;
	var commissionAmt = 0.0;
	if (frm.fareUsed)
		if (frm.fareUsed.value != "")
			fareUsed = parseFloat(frm.fareUsed.value);
	if (frm.airlinePenalty)
		if (frm.airlinePenalty.value != "")
			airlinePenalty = parseFloat(frm.airlinePenalty.value);
	if (frm.taxAdjust)
		if (frm.taxAdjust.value != "")
			taxAdjust = parseFloat(frm.taxAdjust.value);
	if (frm.seatCost)
		if (frm.seatCost.value != "")
			seatCost = parseFloat(frm.seatCost.value);
	if (frm.taxCost)
		if (frm.taxCost.value != "")
			taxCost = parseFloat(frm.taxCost.value);
	if (frm.commissionAmt)
		if (frm.commissionAmt.value != "")
			commissionAmt = parseFloat(frm.commissionAmt.value);
	if (frm.totalCost)
		frm.totalCost.value = floatToString((seatCost+taxCost+airlinePenalty)*sign);
	if (frm.totalRefund)
		frm.totalRefund.value = floatToString(fareUsed-<%= ticket.getSeatCostString() %>-<%= ticket.getTotalTaxString() %>-commissionAmt+airlinePenalty+taxAdjust);
}
</script>
<% if (!rtcb.isViewed()) { %>
<title>Edit Refund Ticket</title>
<script>
var frm;
var ticketNo = "<%= ticket.getTicketNumber() %>";


function newCommPct() {	// Commission Pct has changed - calculate Commission Amount
	var sellingFare = <%= ticket.getSellingFare() %>;
	if (frm.commissionPct.value != "" && frm.fareUsed.value != "" && frm.airlinePenalty.value != "") {
		var pct = parseFloat(frm.commissionPct.value);
		var fareUsed = parseFloat(frm.fareUsed.value);
		var airlinePenalty = parseFloat(frm.airlinePenalty.value);
//		frm.commissionAmt.value = floatToString(Math.round(fareUsed*pct)/100);
		frm.commissionAmt.value = floatToString(Math.round((sellingFare-fareUsed-airlinePenalty)*pct)/100);
	}
}


function newCommAmt() {	// Commission Amount has changed - calculate Commission Pct
	var sellingFare = <%= ticket.getSellingFare() %>;
	if (frm.commissionAmt.value != "" && frm.fareUsed.value != "" && frm.airlinePenalty.value != "") {
		var comm = parseFloat(frm.commissionAmt.value);
		var fareUsed = parseFloat(frm.fareUsed.value);
		var airlinePenalty = parseFloat(frm.airlinePenalty.value);
		var amt = sellingFare-fareUsed-airlinePenalty;
//		frm.commissionPct.value = (fareUsed==0?"0.00":floatToString(Math.round(comm*10000/fareUsed)/100));
		frm.commissionPct.value = (amt==0?"0.00":floatToString(Math.round(comm*10000/amt)/100));
	}
}


function validateForm() {
	var errors = "";
	if (frm.fareUsed) if (frm.fareUsed.value == "") errors += "Fare Used is blank\n";
	if (frm.airlinePenalty) if (frm.airlinePenalty.value == "") errors += "Airline Penalty is blank\n";
	if (frm.taxAdjust) if (frm.taxAdjust.value == "") errors += "Tax Adjustment is blank\n";
	if (frm.seatCost) if (frm.seatCost.value == "") errors += "Seat Cost is blank\n";
	if (frm.taxCost) if (frm.taxCost.value == "") errors += "Tax Cost is blank\n";
	if (frm.commissionPct) if (frm.commissionPct.value == "") errors += "Commission Pct is blank\n";
	if (frm.commissionAmt) if (frm.commissionAmt.value == "") errors += "Commission Amount is blank\n";
	if (errors != "") {
		alert("Ticket cannot be added with the following errors:\n\n"+errors);
		return false;
	}
	return true;
}


function saveTicket() {
	if (!validateForm()) {	return false;}

	window.returnValue =	(frm.airlinePenalty?frm.airlinePenalty.value:"0.00")+","+
							(frm.fareUsed?frm.fareUsed.value:"0.00")+","+
							(frm.seatCost?frm.seatCost.value:"0.00")+","+
							(frm.taxAdjust?frm.taxAdjust.value:"0.00")+","+
							(frm.taxCost?frm.taxCost.value:"0.00")+","+
							(frm.commissionPct?frm.commissionPct.value:"0.00")+","+
							(frm.commissionAmt?frm.commissionAmt.value:"0.00");
	window.close();
}


function doOnLoad() {
	frm=document.forms[0];
	updateTotal();
	if (frm.fareUsed)
		frm.fareUsed.focus();
	else if (frm.seatCost)
		frm.seatCost.focus();
	window.moveTo(	parseInt((window.screen.availWidth-document.body.offsetWidth) / 2),
					parseInt((window.screen.availHeight-document.body.offsetHeight) / 2));
}

</script>
</head>

<body onLoad="doOnLoad();">
<input name="totalSeatCost" type="hidden" value="<%= ticket.getSeatCostString() %>">
<input name="totalTax" type="hidden" value="<%= ticket.getTotalTaxString() %>">
<h3>Edit Refund Ticket</h3>
<% } else { %>  <%-- ticket is being viewed --%>
<title>View Refund Ticket</title>
</head>
<body onLoad="updateTotal();">
<h3>View Refund Ticket</h3>
<% } %>
  <form>
  <table class="displaydetails" width="100%" border="0" cellspacing="2">
    <tr>
      <th>Ticket Number:</th><td><%= ticket.getTicketNumber() %></td>
      <th>Airline number:</th><td><%= ticket.getAirlineNumber() %></td>
      <th>PNR:</th><td><%= ticket.getPnr() %></td>
    </tr>
    <tr>
      <th>Departure Date:</th><td><%= ticket.getDepartureDate() %></td>
      <th>Ticket Issue Date:</th><td><%= ticket.getIssueDate() %></td>
      <th>Num Pax:</th><td><%= ticket.getNumPassengers() %></td>
    </tr>
    <tr>
      <th>Passenger Name:</th><td colspan="2"><%= ticket.getPassengerName() %></td>
    </tr>
    <tr>
      <th></th><th></th>
      <th>Published Fare Amount:</th><td style="text-align: right;"><%= ticket.getPublishedFare() %></td>
      <th>GB Tax:</th><td style="text-align: right;"><%= ticket.getGbTax() %></td>
    </tr>
    <tr>
      <th></th><th></th>
      <th>Selling Fare Amount:</th><td style="text-align: right;"><%= ticket.getSellingFare() %></td>
      <th>UB Tax:</th><td style="text-align: right;"><%= ticket.getUbTax() %></td>
    </tr>
    <tr>
      <th>Commission Pct:</th><td style="text-align: right;"><%= ticket.getCommissionPct() %> %</td>
      <th>Commission Amount:</th><td style="text-align: right;"><%= ticket.getCommissionAmt() %></td>
      <th>Remaining Tax:</th><td style="text-align: right;"><%= ticket.getRemainingTax() %></td>
    </tr>
    <tr>
      <th></th><th></th>
      <th style="border-top: 1px solid black;">Total Seat Cost:</th>
      <td style="border-top: 1px solid black; text-align: right;"><%= ticket.getSeatCostString() %></td>
      <th style="border-top: 1px solid black;">Total Tax Cost:</th>
      <td style="border-top: 1px solid black; text-align: right;"><%= ticket.getTotalTaxString() %></td>
    </tr>
  </table>
  <p/><hr/>
<%
  String docType = request.getParameter("doctype");
  if (docType==null) docType="";
  String roStr = rtcb.isViewed()?"ReadOnly=\"true\"":"";
%>
  <table width="100%" border="0" cellspacing="2">
<%  if (docType.equalsIgnoreCase("MRN")) { %>
      <tr>
        <th>Fare Used:</th>
        <td><input name="fareUsed" type="text" <%= roStr %> class="amount" onChange="if (validAmount(this, 0.0)) {newCommPct(); updateTotal();}" size="8" maxlength="8" value="<%= null!=rtcb.getEditedTicket()?rtcb.getEditedTicket().getFareUsedString():"0.00" %>"></td>
        <th>Airline Penalty</th>
        <td><input name="airlinePenalty" type="text" <%= roStr %> class="amount" onChange="if (validAmount(this)) {newCommPct(); updateTotal();}" size="8" maxlength="8" value="<%= null!=rtcb.getEditedTicket()?rtcb.getEditedTicket().getAirlinePenaltyString():"0.00" %>"></td>
        <th>Tax Used:</th>
        <td><input name="taxAdjust" type="text" <%= roStr %> class="amount" onChange="if (validAmount(this)) {newCommPct(); updateTotal();}" size="8" maxlength="8" value="<%= null!=rtcb.getEditedTicket()?rtcb.getEditedTicket().getTaxAdjustString():"0.00" %>"></td>
      </tr>
      <tr>
        <th>Commission Pct:</th>
        <td><input	name="commissionPct" 
										type="text" <%= roStr %>
										class="amount"
										id="commissionPct"
										onBlur="if (this.value != '') if (validAmount(this, 0.0, 99.0)) {updateTotal();} else {return false;}"
										onChange="if (validAmount(this, 0.0, 99.0)) newCommPct();"
										size="6"
										maxlength="5"
										value="<%= (null!=rtcb.getEditedTicket())?
																	rtcb.getEditedTicket().getCommissionPct():
																	ticket.getAirlineNumber().equals("125")?
																		"0.00":
																		ticket.getPublishedFareFloat()==ticket.getSellingFareFloat()?
																			ticket.getCommissionPct():
																			"0.00" %>">
        %</td>
        <th>Commission Amt:</th>
        <td><input name="commissionAmt" type="text" <%= roStr %> class="amount" id="commissionAmt" onBlur="if (this.value != '') if (validAmount(this, 0.0)) {updateTotal();} else {return false;}" onChange="if (validAmount(this, 0.0)) newCommAmt();" size="8" maxlength="8" value="<%= (null!=rtcb.getEditedTicket())?rtcb.getEditedTicket().getCommissionAmt():"0.00" %>"></td>
        <th style="font-weight: bold;">Total Refund:</th>
        <td><input name="totalRefund" type="text" class="amount" readonly="true" size="8" maxlength="8" value="0.00"></td>
      </tr>
    <% } else if (docType.equalsIgnoreCase("MAN")) { %>
      <tr>
        <th>Seat Adjustment:</th>
        <td><input name="seatCost" type="text" <%= roStr %> class="amount" onChange="if (validAmount(this, 0.0)) updateTotal();" size="8" maxlength="8" value="<%= null!=rtcb.getEditedTicket()?rtcb.getEditedTicket().getSeatCostString():"0.00" %>"></td>
        <th>Tax Adjustment:</th>
        <td><input name="taxCost" type="text" <%= roStr %> class="amount" onChange="if (validAmount(this, 0.0)) updateTotal();" size="8" maxlength="8" value="<%= null!=rtcb.getEditedTicket()?rtcb.getEditedTicket().getTaxCostString():"0.00" %>"></td>
        <th>Total Adjustment:</th>
        <td><input name="totalCost" type="text" class="amount" readonly="true" size="8" maxlength="8" value="0.00"></td>
      </tr>
    <% } else if (docType.equalsIgnoreCase("ACM") || docType.equalsIgnoreCase("ADM")) { %>
      <tr>
        <th>Seat Cost:</th>
        <td><input name="seatCost" type="text" <%= roStr %> class="amount" onChange="if (validAmount(this, 0.0)) updateTotal();" size="8" maxlength="8" value="<%= null!=rtcb.getEditedTicket()?rtcb.getEditedTicket().getSeatCostString():"0.00" %>"></td>
        <th>Tax Cost:</th>
        <td><input name="taxCost" type="text" <%= roStr %> class="amount" onChange="if (validAmount(this, 0.0)) updateTotal();" size="8" maxlength="8" value="<%= null!=rtcb.getEditedTicket()?rtcb.getEditedTicket().getTaxCostString():"0.00" %>"></td>
        <th>Airline Penalty</th>
        <td><input name="airlinePenalty" type="text" <%= roStr %> class="amount" onChange="if (validAmount(this)) updateTotal();" size="8" maxlength="8" value="<%= null!=rtcb.getEditedTicket()?rtcb.getEditedTicket().getAirlinePenaltyString():"0.00" %>"></td>
      </tr>
      <tr>
        <th colspan="5">Total Cost:</th>
        <td><input name="totalCost" type="text" class="amount" readonly="true" size="8" maxlength="8" value="0.00"></td>
      </tr>
	<% } %>
  </table>
<% if (request.isUserInRole("STELLA_SUPERVISOR") && rtcb.isViewed()) { %>
	<table class="displaydetails" width="100%" border="0" cellspacing="2">
			<tr>
				<th width="12%">Created By:</th><td width="13%"><%= rtcb.getEditedTicket().getCreatedBy() %></td>
				<th width="12%">Created On:</th><td width="13%"><%= rtcb.getEditedTicket().getCreatedOn() %></td>
				<th width="12%">Last Edit By:</th><td width="13%"><%= rtcb.getEditedTicket().getLastEditedBy() %>&nbsp;</td>
				<th width="12%">Last Edit On:</th><td width="13%"><%= rtcb.getEditedTicket().getLastEditedOn() %>&nbsp;</td>
			</tr>
	</table>
<% } %>
  <p/><hr/>
  <table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td style="text-align: center;">
<% if (rtcb.isViewed()) { %>
	    <button accesskey="c" onClick="window.close();"><u>C</u>ancel</button>
<% } else { %>
        <button accesskey="s" onClick="saveTicket();"><u>S</u>ave</button>
	    <button accesskey="c" onClick="if (confirm('Exit editing this document?')) window.close();"><u>C</u>ancel</button>
<% } %>
	  </td>
    </tr>
  </table>
  <hr/>
</form>
</body>
</html>
