<%@ page errorPage="ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" session="true" %>
<%@ taglib uri="utiltags" prefix="util" %>
<%@ taglib uri="stellatags" prefix="stella" %>
<jsp:useBean id="search" class="uk.co.firstchoice.stella.frontend.SearchBean" scope="request"/>
<jsp:useBean id="searchresults" class="uk.co.firstchoice.stella.frontend.SearchResultCollectionBean" scope="session"/>
<html>
<head>
<title>Document Search</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="/stella/stella.css" rel="stylesheet" type="text/css">
<script src="/stella/validation.js"></script>
<script>
var frm;
var fromDate = new Date(2000, 0, 1);

function validateForm() {
	if (frm.pnr.value == "" &&
		frm.atopReference.value == "" &&
		frm.airlineNumber.value == "" &&
		frm.refundNumber.value == "" &&
		frm.ticketNumber.value == "" &&
		frm.passengerName.value == "" &&
		frm.departureDate.value == "" &&
		frm.refundLetterNumber.value == "" &&
		frm.iataNumber.value == "" &&
		frm.issueDate.value == "") {
		alert("At least one search criteria (in addition to Season) must be specified");
		return false;
	} else {
		return true;
	}
}

function viewDocument(docNo) {
	var width = 900;
	var height = 550;
	var maxHeight = window.screen.availHeight;
	var maxWidth = window.screen.availWidth;
	var leftPoint = parseInt(maxWidth / 2) - parseInt(width / 2);
	var topPoint = parseInt(maxHeight / 2) - parseInt(height / 2);


	var win = window.open("/stella/stellabsp?cmd=viewdocument&lineno="+docNo,'viewDoc',
				'width='+width+',height='+height+
				',scrollbars=yes,menubar=no,location=no,status=no,toolbar=no,resizable=yes',false);
	win.focus();
//	window.location = "stellabsp?cmd=viewdocument&lineno="+docNo;
}

function updateAirlineSelect() {
	var airlineNo = frm.airlineNumber.value;
	if (airlineNo != "") {
		airlineNo = leftPad(true, airlineNo, 3);
		frm.airlineNumber.value = airlineNo;
		for (i=0; i<frm.airlineSelect.options.length; i++) {
			if (frm.airlineSelect.options[i].value.substr(0, 3) == airlineNo) {
				frm.airlineSelect.selectedIndex = i;
				return true;
			}
		}
	}
	frm.airlineSelect.selectedIndex = 0;
	return false;
}

function doAirlineSelectChange() {
	frm.airlineNumber.value=frm.airlineSelect.options[frm.airlineSelect.selectedIndex].value.substr(0,3);
	sectorPaymentInd = frm.airlineSelect.options[frm.airlineSelect.selectedIndex].value.substr(3,1);
}

function doSearch() {
	window.document.all["theBody"].style.cursor = "wait";
	var searchingStyle = window.document.all["searchingId"].style;
	searchingStyle.top = document.body.offsetHeight / 2;
	searchingStyle.width = 100;
	searchingStyle.left = (document.body.offsetWidth - 100) / 2;
	searchingStyle.display = "block";
	frm.submit();
}

function doOnLoad() {
	frm=document.forms[0];
	window.moveTo(	parseInt((window.screen.availWidth-document.body.offsetWidth) / 2),
					parseInt((window.screen.availHeight-document.body.offsetHeight) / 2));
}

</script>
</head>

<body onLoad="doOnLoad();" id="theBody">
<form method="post" action="/stella/jsp/postsearch.jsp">
<h3>Document Search</h3>
<input name="iataNumber" type="hidden" value="<%= search.getIataNumber() %>">
<table width="100%" border="0" cellspacing="0">
  <tr>
    <th>PNR:</th>
    <td><input name="pnr" type="text" id="pnr" tabindex="1" onChange="this.value = this.value.toUpperCase();" size="11" maxlength="6" value="<%= search.getPnr() %>"></td>
    <th>ATOP Reference:</th>
    <td><input name="atopReference" type="text" id="atopReference" tabindex="2" onBlur="validNumberField(this, true, 0, 999999999);" size="9" maxlength="8" value="<%= search.getAtopReference() %>"></td>
    <th>ATOP Season:</th>
    <td><input name="atopSeason" type="text" id="atopSeason" tabindex="3" onChange="this.value = this.value.toUpperCase();" size="4" maxlength="3" value="<%= search.getAtopSeason() %>"></td>
  </tr>
  <tr>
    <th>Airline:</th>
    <td><input name="airlineNumber" type="text" tabindex="4" size="3" maxlength="3" onBlur="if (validNumberField(this, true, 0, 999)) updateAirlineSelect();" value="<%= search.getAirlineNumber() %>"> <stella:stellaSelector selectionType="airlineSelect" /></td>
    
    <th>Refund Doc No.:</th>
<%--     <td><input name="refundNumber" type="text" tabindex="5" onBlur="validNumberField(this, true, 60000000, 8999999999);" size="11" maxlength="10" value="<%= search.getRefundNumber() %>"></td> --%>
    <td><input name="refundNumber" type="text" tabindex="5" onBlur="validNumberField(this, true, 1, 9999999999);" size="11" maxlength="10" value="<%= search.getRefundNumber() %>"></td>
    <th>Ticket No.:</th>
    <td><input name="ticketNumber" type="text" tabindex="6" onBlur="validNumberField(this, true, 1000000000, 9999999999);" size="11" maxlength="10" value="<%= search.getTicketNumber() %>"></td>
  </tr>
  <tr>
    <th>Passenger Name:</th>
    <td><input name="passengerName" type="text" id="passengerName" tabindex="7" onChange="this.value = this.value.toUpperCase();" size="55" maxlength="50" value="<%= search.getPassengerName() %>"></td>
    <th>Refund Letter No.:</th>
    <td><input name="refundLetterNumber" type="text" tabindex="8" onBlur="validNumberField(this, true, 1, 999999);" size="8" maxlength="6" value="<%= search.getRefundLetterNumber() %>"></td>
    <th>IATA No.:</th>
		<td><stella:stellaSelector selectionType="iataSelect" tabindex="9" /></td>
  </tr>
  <tr>
    <th>Departure Date:</th>
    <td><input name="departureDate" type="text" tabindex="10" onBlur="validDateField(this, true, fromDate); return false;" size="11" maxlength="11" value="<%= search.getDepartureDate() %>"></td>
    <th>Issue Date:</th>
    <td><input name="issueDate" type="text" id="issueDate" tabindex="11" onBlur="validDateField(this, true, fromDate); return false;" size="11" maxlength="11" value="<%= search.getIssueDate() %>"></td>
  </tr>
</table>
<p/><hr/>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td style="text-align: center;">
      <button accesskey="s" onClick="if (validateForm()) {this.disabled=true; doSearch();};"><u>S</u>earch</button>
<%--  <button accesskey="c" onClick="window.location='stellabsp';"><u>C</u>ancel</button> --%>
      <button accesskey="c" onClick="window.close();"><u>C</u>ancel</button>
    </td>
  </tr>
</table>
<hr/><p/>
<% int lineNo = 0; %>
<table cellspacing="0" class="dataTable">
	<tr>
		<th>#</th>
		<th>Type</th>
		<th>Document No</th>
		<th>PNR</th>
		<th>Passenger Name</th>
		<th>Departure Date</th>
		<th>ATOP Reference</th>
		<th>ATOP Season</th>
		<th>Doc. Amount</th>
	</tr>

<% if (searchresults.getFoundDocuments().size() > 0 ) { %>
<util:iterate id="doc" type="uk.co.firstchoice.stella.frontend.SearchResultBean" collection="<%=searchresults.getFoundDocuments()%>" >
	<tr class="<%="linkline"+(lineNo%2) %>" onClick="viewDocument(<%= lineNo++ %>);">
		<td><%= lineNo %></td>
		<td><jsp:getProperty name="doc" property="documentType" /></td>
		<td><jsp:getProperty name="doc" property="documentNo"/></td>
		<td><jsp:getProperty name="doc" property="pnr"/></td>
		<td><jsp:getProperty name="doc" property="passengerName"/></td>
		<td><jsp:getProperty name="doc" property="departureDate"/></td>
		<td><jsp:getProperty name="doc" property="atopReference"/></td>
		<td><jsp:getProperty name="doc" property="atopSeason"/></td>
		<td><jsp:getProperty name="doc" property="documentTotal"/></td>
	</tr>
</util:iterate>
<% } %>
<tr style="background-color: white;">
  <td colspan="8" style="text-align: right; border-top: 2px solid black; font-weight: bold;">Total:</td>
  <td style="text-align: right; border-top: 2px solid black; font-weight: bold;"><jsp:getProperty name="searchresults" property="grandTotal"/></td>
</tr>
</table>
<%= search.getSearchPerformed() && searchresults.getFoundDocuments().size() == 0?"<p/><h6>No documents found</h6>":"" %>
</form>
<h6 id="searchingId" style="display:none; position:absolute;">Searching</h6>
</body>
</html>
