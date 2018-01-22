<%@ page errorPage="ErrorPage.jsp" %>
<%@ page import="java.util.*" session="true" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="stellatags" prefix="stella" %>
<jsp:useBean id="ticket" class="uk.co.firstchoice.stella.frontend.TicketBean" scope="request"/>
<html>
<head>
<title>Ticket Entry</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="/stella/stella.css" rel="stylesheet" type="text/css">
<script src="/stella/validation.js"></script>
<script>
var frm;
var sectorPaymentInd = 'N';
var fromDate = new Date(1997, 0, 1);
var lastSaveType = "<%= ticket.getLastSaveType() %>";	//c=conjunction, r=repeat otherwise blank
var prvType = "AMA"; // used for AAIR setting
var bcode = "AAIR";

function updateTotals() {	// One of the amount fields has changed - update totals
	var publishedFare = parseFloat(frm.publishedFare.value);
	var sellingFare = parseFloat(frm.sellingFare.value);
	var commissionAmt = parseFloat(frm.commissionAmt.value);
	var gbTax = parseFloat(frm.gbTax.value);
	var commPct = parseFloat(frm.commissionPct.value);
	var ubTax = parseFloat(frm.ubTax.value);		//*(publishedFare==sellingFare?(100.0-commPct)/100:1.0);
	var remTax = parseFloat(frm.remainingTax.value);
	frm.seatCost.value = floatToString(
                                publishedFare!=sellingFare?
                                    sellingFare:
                                    (publishedFare-commissionAmt-
                                        (sectorPaymentInd=='Y'?
                                            0:
                                            (ubTax*commPct/100)
                                        )
                                    )
                                            );
	frm.totalTax.value = floatToString(gbTax+ubTax+remTax);
}

function formatAmounts() {
	frm.publishedFare.value = floatToString(parseFloat(frm.publishedFare.value));
	frm.sellingFare.value = floatToString(parseFloat(frm.sellingFare.value));
	frm.commissionAmt.value = floatToString(parseFloat(frm.commissionAmt.value));
	frm.commissionPct.value = floatToString(parseFloat(frm.commissionPct.value));
	frm.gbTax.value = floatToString(parseFloat(frm.gbTax.value));
	frm.ubTax.value = floatToString(parseFloat(frm.ubTax.value));
	frm.remainingTax.value = floatToString(parseFloat(frm.remainingTax.value));
}

function newCommPct() {	// Commission Pct has changed - calculate Commission Amount
	var pct = parseFloat(frm.commissionPct.value);
	var pubFare = parseFloat(frm.publishedFare.value);
	frm.commissionAmt.value = floatToString(Math.round(pubFare*pct)/100);
}

function newCommAmt() {	// Commission Amount has changed - calculate Commission Pct
	var comm = parseFloat(frm.commissionAmt.value);
	var pubFare = parseFloat(frm.publishedFare.value);
	frm.commissionPct.value = (pubFare==0?"0.00":floatToString(Math.round(comm*10000/pubFare)/100));
}

function validTicketNo(field, doAlert) {	//Can be 9999999999 or 9999999990/2 which is a range 9999999990, 9999999991, 9999999992
	var tnrStr = field.value;
	if (tnrStr!="")
		if (validNumber(tnrStr, 1000000000, 9999999999))
			return true;
	if (doAlert) {
		field.focus();
		alert("Please enter a ticket number in the format 9999999999");
	}
	return false;
}

function updateAirlineSelect() {
	var airlineNo = frm.airlineNumber.value;
	if (airlineNo != "") {
		airlineNo = leftPad(true, airlineNo, 3);
		frm.airlineNumber.value = airlineNo;
		for (i=0; i<frm.airlineSelect.options.length; i++) {
			if (frm.airlineSelect.options[i].value.substr(0, 3) == airlineNo) {
				frm.airlineSelect.selectedIndex = i;
				sectorPaymentInd = 
					frm.airlineSelect.options[frm.airlineSelect.selectedIndex].value.substr(3,1);
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
	updateTotals();
}

function updateIataSelect() {
	var iataNo = frm.iataNumber.value;
	if (iataNo != "") {
		for (i=0; i<frm.iataSelect.options.length; i++) {
			if (frm.iataSelect.options[i].value == iataNo) {
				frm.iataSelect.selectedIndex = i;
				return true;
			}
		}
	}
	return false;
}

function validateForm(voidTicket) {
        var errors = "";
	if (frm.airlineNumber.value.length != 3 || !validNumber(frm.airlineNumber.value, 0, 999))
		errors += "Airline Number missing or invalid\n";
	else if (!updateAirlineSelect())
		errors += "Unknown Airline Number entered\n";
	if (!validTicketNo(frm.ticketNumber, false))
		errors += "Ticket Number is missing or invalid.\n";
	if (frm.exchangeTicketNo.value != '')
		if (!validTicketNo(frm.exchangeTicketNo, false))
			errors += "Exchange Ticket Number is invalid.\n";
        if (frm.pnrDate.value == '') 
               errors += "PNR Date missing. \n";
	if (!validNumber(frm.iataNumber.value, 0, 999999999))
		errors += "IATA Number missing or invalid.\n";
	else if (!updateIataSelect())
		errors += "Unknown IATA Number entered\n";
        if (radioSelected(frm.passengerType) == "") {
            errors += "Passenger type not selected\n";
        }
        if (!voidTicket) {
            var tmp = validDate(frm.departureDate.value, fromDate);
            if (tmp == "")
                errors += "Departure Date missing or invalid\n";
            else
                frm.departureDate.value = tmp;
        }
        tmp = validDate(frm.issueDate.value, fromDate);
        if (tmp == "")
            errors += "Ticket Issue Date missing or invalid\n";
        else
            frm.issueDate.value = tmp;
        frm.agentInit.value = trim(frm.agentInit.value);
        var mai = frm.agentInit.value.match(/[a-zA-Z\?]+/);
        if (frm.agentInit.value != mai)
            errors += "Ticketing Agent Initials missing or not A-Z\n";
            if (frm.pnr.value.length != 6)
                errors += "PNR missing or invalid\n";
                if (!voidTicket) {
                    tmp = frm.passengerName.value.split("/");
                    if (frm.passengerName.value.length < 3 || tmp.length != 2)
                    errors += "Passenger Name missing or invalid\n";
                    if (frm.atopReference.value != "")
                        if (!validNumber(frm.atopReference.value, 0, 999999999))
                            errors += "ATOP Reference invalid (must be numeric)\n";
                            if (frm.branchCode.value == "")
                                errors += "Branch Code missing\n";
                            }
                            if (errors != "") {
                                alert("The form contains the following errors and cannot be saved:\n\n"+errors);
                                return false;
                            } else if (voidTicket) {
                                frm.departureDate.value = "";
                                frm.passengerName.value = "VOID";
                                frm.publishedFare.value = "0.00";
                                frm.sellingFare.value = "0.00";
                                frm.commissionPct.value = "0.00";
                                frm.commissionAmt.value = "0.00";
                                frm.seatCost.value = "0.00";
                                frm.gbTax.value = "0.00";
                                frm.ubTax.value = "0.00";
                                frm.remainingTax.value = "0.00";
                                frm.totalTax.value = "0.00";
                //		frm.atopReference.value = "";
                //?		frm.branchCode.value = "";
                           }
                           return true;
              }

function doTypeChange() {
        var curType =  frm.ticketType.options[frm.ticketType.selectedIndex].value;
        if ((curType != prvType) && ( curType == "AMA"))  {	// allow multiple passengers
             frm.branchCode.value = bcode;
             frm.branchCode.readOnly = false;  // H&J can enter 
             frm.atopReference.value = 1;
             frm.atopReference.readOnly = false; // H&J can enter 
	}
	else if ((prvType != curType)  && ( prvType == "AMA")) {
	      frm.branchCode.value = '';
	      frm.branchCode.readOnly = false;
              frm.atopReference.value = "";
              frm.atopReference.readOnly = false;
	}
	if (frm.ticketType.options[frm.ticketType.selectedIndex].value == "MPD") {	// allow multiple passengers
		frm.numPassengers.readOnly = false;
	} else {    
		frm.numPassengers.value = "1";
		frm.numPassengers.readOnly = true;
	}
	prvType = curType ;
}

function disableFields() {
        // Assuming the first Docunment Type = 'AMA';
/* commented for specialist business
           if ( frm.ticketType.options[frm.ticketType.selectedIndex].value == "AMA" ) {
            frm.atopReference.readOnly = true;
            frm.branchCode.readOnly = true;
        }    */
	if (lastSaveType == "r" || lastSaveType == "c") {
		frm.ticketType.readOnly = true;
		frm.airlineNumber.readOnly = true;
		frm.airlineSelect.disabled = true;
		frm.ticketNumber.readOnly = true;
		frm.iataNumber.readOnly = true;
		frm.eticket.readOnly = true;
		frm.departureDate.readOnly = true;
		frm.issueDate.readOnly = true;
		frm.agentInit.readOnly = true;
		frm.pnr.readOnly = true;
		for (var i=0; i<frm.passengerType.length; i++)
			frm.passengerType[i].readOnly = true;
		frm.numPassengers.readOnly = true;
		frm.branchCode.readOnly = true;
		frm.atopReference.readOnly = true;
		frm.Void.disabled = true;
		if (lastSaveType == "c") {
			frm.commissionPct.readOnly = true;
			frm.commissionAmt.readOnly = true;
			frm.publishedFare.readOnly = true;
			frm.sellingFare.readOnly = true;
			frm.gbTax.readOnly = true;
			frm.ubTax.readOnly = true;
			frm.remainingTax.readOnly = true;
			frm.passengerName.readOnly = true;
			frm.Repeat.disabled = true;
		} else {
			frm.Conjunction.disabled = true;
		}
	}
}

function doOnLoad() {
	frm=window.document.forms[0];
	disableFields();
	formatAmounts();
	updateTotals();
	updateAirlineSelect();
	doAirlineSelectChange();
	if (lastSaveType == "r") {
		frm.passengerName.focus();
	} else {
		frm.ticketType.focus();
	}
	
	window.moveTo(	parseInt((window.screen.availWidth-document.body.offsetWidth) / 2),
					parseInt((window.screen.availHeight-document.body.offsetHeight) / 2));
}

function doSubmit() {
	frm.Save.disabled = true;
	frm.Repeat.disabled = true;
	frm.Conjunction.disabled = true;
	frm.Void.disabled = true;
	frm.Cancel.disabled = true;
	window.document.all["theBody"].style.cursor = "wait";
	frm.submit();
}

</script>
</head>

<body onLoad="doOnLoad();" id="theBody">
<form method="post" action="/stella/jsp/postticket.jsp" >
<input name="savetype" type="hidden"/>
<input name="lastSaveType" type="hidden" value="<%= ticket.getLastSaveType() %>" />
<input name="conjunctionMaster" type="hidden" value="<%= ticket.getConjunctionMaster() %>"/>
<input name="pnrUniqueKey" type="hidden" value="<%= ticket.getPnrUniqueKey() %>"/>
<h3>Manual Ticket Entry</h3>
  <table width="100%" border="0" cellspacing="0">
    <tr>
      <th>Document Type:</th>
      <td colspan="5"><stella:stellaSelector selectionType="ticketTypeSelect" /></td>
    </tr>
    <tr>
      <th>Airline number:</th>
      <td><input name="airlineNumber" type="text" tabindex="2" onBlur="if (validNumberField(this, true, 0, 999)) updateAirlineSelect();" size="3" maxlength="3" value="<%= ticket.getAirlineNumber() %>"> <stella:stellaSelector selectionType="airlineSelect" /></td>
      <th>Ticket Number:</th>
      <td><input name="ticketNumber" type="text" tabindex="3" onChange="validTicketNo(this, true);" size="12" maxlength="10" value="<%= ticket.getTicketNumber() %>"></td>
      <th>E-Ticket:</th>
      <td><input name="eticket" type="checkbox" style="border: 0px;" tabindex="4" value="Y" <%= ticket.getEticket().equals("Y")?"checked":"" %>></td>
    </tr>
    <tr>
      <th>IATA number:</th>
      <td><input name="iataNumber" type="text" id="iataNumber" tabindex="5" onBlur="if (validNumberField(this, true, 0, 999999999)) updateIataSelect();" size="11" maxlength="9" value="<%= ticket.getIataNumber() %>"> <stella:stellaSelector selectionType="iataSelect" /></td>
      <th>Ticket Issue Date:</th>
      <td><input name="issueDate" type="text" id="issueDate" tabindex="6" onBlur="validDateField(this, true, fromDate); return false;" size="11" maxlength="11" value="<%= ticket.getIssueDate() %>"></td>
      <th>Departure Date:</th>
      <td><input name="departureDate" type="text" tabindex="7" onBlur="validDateField(this, true, fromDate); return false;" size="11" maxlength="11" value="<%= ticket.getDepartureDate() %>"></td>
    </tr>
    <tr>
      <th>Ticketing Agent Init.:</th>
      <td><input name="agentInit" type="text" id="agentInit" tabindex="8" onChange="this.value = trim(this.value.toUpperCase());" size="4" maxlength="3" value="<%= ticket.getAgentInit() %>"></td>
      <th>PNR:</th>
      <td><input name="pnr" type="text" id="pnr" tabindex="9" onChange="this.value = this.value.toUpperCase().replace(/\W/g,'');" size="11" maxlength="6" value="<%= ticket.getPnr() %>"></td>
      <th>Passenger Type:</th>
      <td rowspan="2"><stella:stellaSelector selectionType="pasTypeSelect" /></td>
    </tr>
    <tr>
      <th>Passenger Name:</th>
      <td><input name="passengerName" type="text" id="passengerName" tabindex="12" onChange="this.value = trim(this.value.toUpperCase());" size="55" maxlength="50" value="<%= ticket.getPassengerName() %>"></td>
      <th>PNR Creation Date:</th>
      <td><input name="pnrDate" type="text" id="pnrDate" tabindex="12" onBlur="validDateField(this, true, fromDate); return false;" size="11" maxlength="11" value="<%= ticket.getPnrDate() %>"></td>      
    </tr>
    <tr>
      <th>Exchange Ticket No.</th>
      <td><input name="exchangeTicketNo" type="text" tabindex="14" onChange="if (this.value != '') validTicketNo(this, true);" size="12" maxlength="10" value="<%= ticket.getExchangeTicketNo() %>"></td>
      <th>Number of Passengers:</th>
      <td><input name="numPassengers" type="text" readonly id="numPassengers" tabindex="13" onBlur="validNumberField(this, true, 0, 9);" size="4" maxlength="3" value="<%= ticket.getNumPassengers() %>"></td>
    </tr>
    <tr>
      <th></th>
      <td></td>
      <th>Published Fare Amount:</th>
      <td><input name="publishedFare" type="text" class="amount" id="publishedFare" tabindex="20" onBlur="if (validAmount(this, 0.0)) {updateTotals();} else {return false;}" size="8" maxlength="8" value="<%= ticket.getPublishedFare() %>"></td>
      <th>GB Tax:</th>
      <td><input name="gbTax" type="text" id="gbTax" class="amount" tabindex="24" onBlur="if (validAmount(this, 0.0)) {updateTotals();} else {return false;}" size="8" maxlength="8" value="<%= ticket.getGbTax() %>"></td>
    </tr>
    <tr>
      <th></th>
      <td></td>
      <th>Selling Fare Amount:</th>
      <td><input name="sellingFare" type="text" id="sellingFare" class="amount" tabindex="21" onBlur="if (validAmount(this, 0.0)) {updateTotals();} else {return false;}" size="8" maxlength="8" value="<%= ticket.getSellingFare() %>"></td>
      <th>UB Tax:</th>
      <td><input name="ubTax" type="text" id="ubTax" class="amount" tabindex="25" onBlur="if (validAmount(this, 0.0)) {updateTotals();} else {return false;}" size="8" maxlength="8" value="<%= ticket.getUbTax() %>"></td>
    </tr>
    <tr>
      <th>Commission Pct:</th>
      <td><input name="commissionPct" type="text" class="amount" id="commissionPct" tabindex="22" onBlur="if (validAmount(this, 0.0, 99.0)) {updateTotals();} else {return false;}" onChange="if (validAmount(this, 0.0, 99.0)) newCommPct();" size="5" maxlength="5" value="<%= ticket.getCommissionPct() %>">
        %</td>
      <th>Commission Amount:</th>
      <td><input name="commissionAmt" type="text" class="amount" id="commissionAmt" tabindex="23" onBlur="if (validAmount(this, 0.0)) {updateTotals();} else {return false;}" onChange="if (validAmount(this, 0.0)) newCommAmt();" size="8" maxlength="8" value="<%= ticket.getCommissionAmt() %>"></td>
      <th>Remaining Tax:</th>
      <td><input name="remainingTax" type="text" id="remainingTax3" class="amount" tabindex="26" onBlur="if (validAmount(this, 0.0)) {updateTotals();} else {return false;}" size="8" maxlength="8" value="<%= ticket.getRemainingTax() %>"></td>
    </tr>
    <tr>
      <th></th>
      <td></td>
      <th style="border-top: 1px solid black;">Total Seat Cost:</th>
      <td style="border-top: 1px solid black;"><input name="seatCost" type="text" class="readOnlyAmount" tabindex="-1" id="seatCost" size="8" maxlength="8" readonly="true" value="<%= ticket.getSeatCost() %>"></td>
      <th style="border-top: 1px solid black;">Total Tax Cost:</th>
      <td style="border-top: 1px solid black;"><input name="totalTax" type="text" class="readOnlyAmount" tabindex="-1" id="totalTax" size="8" maxlength="8" readonly="true" value="<%= ticket.getTotalTax() %>"></td>
    </tr>
    <tr>
      <th>&nbsp;</th>
    </tr>
    <tr>
      <th>Tour Code:</th>
      <td><input name="tourCode" type="text" id="tourCode" tabindex="27" onBlur="this.value=trim(this.value.toUpperCase());" size="18" maxlength="15" value="<%= ticket.getTourCode() %>"></td>
      <th>Fare Basis Code:</th>
      <td><input name="fareBasisCode" type="text" id="fareBasisCode" tabindex="29" onChange="this.value = trim(this.value.toUpperCase());" size="15" maxlength="12" value="<%= ticket.getFareBasisCode() %>"></td>
    </tr>
    <tr>
      <th>ATOP Reference:</th>
      <td><input name="atopReference" type="text" id="atopReference" tabindex="31" onBlur="this.value=this.value.toUpperCase(); if (this.value != 'FLEX') validNumberField(this, true, 0, 999999999);" size="10" maxlength="8" value="<%= ticket.getAtopReference().equals("")?"1":ticket.getAtopReference() %>"></td>
      <th>Branch Code:</th>
      <td><input name="branchCode" type="text" id="branchCode" tabindex="33" onChange="this.value = trim(this.value.toUpperCase());" size="6" maxlength="4" value="<%= ticket.getBranchCode().equals("")?"AAIR":ticket.getBranchCode()  %>"></td>
    </tr>
  </table>
  <p/><hr/>
  <table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr><td style="text-align: center;">
      <button name="Save" accesskey="s" onClick="if (validateForm()) {frm.savetype.value=(lastSaveType=='e'?'edit':'normal'); doSubmit();};"><u>S</u>ave</button>
      <button name="Repeat" accesskey="r" onClick="if (validateForm()) {frm.savetype.value='repeat'; doSubmit();};"><u>R</u>epeat Ticket</button>
      <button name="Conjunction" accesskey="c" onClick="if (validateForm()) {frm.savetype.value='conjunction'; doSubmit();};"><u>C</u>onjunction Ticket</button>
      <button name="Void" accesskey="v" onClick="if (confirm('Are you sure you wish to Void this ticket?')) if (validateForm(true)) {frm.savetype.value='void'; doSubmit();};"><u>V</u>oid Ticket</button>
      <button name="Cancel" accesskey="a" onClick="if (confirm('Cancel ticket and loose input')) window.close();">C<u>a</u>ncel</button>
    </td></tr>
  </table>
  <hr/><p/>
  <h6><%= ticket.getSaveError() %></h6>
</form>
</body>
</html>
