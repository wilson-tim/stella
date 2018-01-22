<%@ page errorPage="ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" session="true" %>
<%@ taglib uri="stellatags" prefix="stella" %>
<%@ taglib uri="utiltags" prefix="util" %>
<jsp:useBean id="refund" class="uk.co.firstchoice.stella.frontend.RefundBean" scope="request" />
<jsp:useBean id="rtcb" class="uk.co.firstchoice.stella.frontend.RefundTicketCollectionBean" scope="session" />
<html>
<head>
<title>Refund Entry</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="/stella/stella.css" rel="stylesheet" type="text/css">
<script src="/stella/validation.js"></script>
<script>
var frm;
var fromDate = new Date(2000, 0, 1); 
var docType = "";
var ticketsAdded = <%= rtcb.getRefundTickets().size()>0?"true":"false" %>;

function hideShowADM(show) {
	var displayMode = show?"inline":"none";
	window.document.all.admSpan1.style.display = displayMode;
	window.document.all.admSpan2.style.display = displayMode;
	window.document.all.admSpan3.style.display = displayMode;
	window.document.all.admSpan4.style.display = displayMode;
	window.document.all.admSpan5.style.display = displayMode;
	window.document.all.admSpan6.style.display = displayMode;
  if (show) {
    hideShowAdmReason();
  }
}

function doRefundTypeSelectChange() { // called from selectction change
frm.documentType.value=frm.refundTypeSelect.options[frm.refundTypeSelect.selectedIndex].value;
//.substr(0,3);
    if (frm.documentType.value == "ADM") { hideShowADM(true);}
    else {	hideShowADM(false); }

}

function validDocumentNo(doAlert) {	

	var tnrStr = frm.documentNumber.value;
//alert("in validDocumentNo is " + tnrStr);

////// Added in fix 
    if (frm.documentType.value == "ADM") { hideShowADM(true);}
    else {	hideShowADM(false); }
//////////

	if (tnrStr!="") {
		//if (validNumber(tnrStr, 60000000, 8999999999)) {
		if (validNumber(tnrStr, 1, 9999999999)) { // any number upto 10 digit
//alert("doc no is valid");
            return true;
		}// valid no 
	} // if tnrstr 

	if (doAlert) {
		frm.documentNumber.focus();
		alert("Please enter a valid document number");	}

	return false;

}


function validateForm(forSubmit) {

//alert("in validateForm");
	var errors = "";
	if (!validDocumentNo(false)) {
        alert("Document Number is missing or invalid.");
	  	errors += "Document Number is missing or invalid.\n"; }
	var tmp = validDate(frm.documentDate.value, fromDate);

	if (tmp == "")	{	
            alert("Departure Date missing or invalid");
			errors += "Departure Date missing or invalid\n"; }
	else {	 frm.documentDate.value = tmp; }

    if (frm.documentType.value == "ADM") { hideShowADM(true);}
    else {	hideShowADM(false); }

	if (frm.documentType.value == "ADM" && frm.disputeAdm.checked) {
		tmp = validDate(frm.disputeDate.value, fromDate);
		if (tmp == "")
			errors += "Dispute Date missing or invalid\n";
		else
			frm.disputeDate.value = tmp;
	}

	if ((frm.iataNumber.value == "") || (frm.iataSelect.selectedIndex < 1))
		errors += "IATA number not selected\n";
	if (forSubmit && !ticketsAdded)
		errors += "No tickets have been added to the list\n";

	if (errors != "") {
		alert("The form contains the following errors and cannot be saved:\n\n"+errors);
		return false;
	}
	return true;
}

function isReadOnly() {
   if (tiketsAdded) {
       return true;
   } else { return false;}
}

function updateRefundTypeSelect() {

    var refundType = frm.documentType.value;
	if (refundType != "") {
		for (i=0; i<frm.refundTypeSelect.options.length; i++) {
//			if (frm.refundTypeSelect.options[i].value.substr(0, 3)  == refundType ) {
			if (frm.refundTypeSelect.options[i].value  == refundType ) {
				frm.refundTypeSelect.selectedIndex = i;
				return true;
			}  
        }

    if (frm.documentType.value == "ADM") { hideShowADM(true);}
    else {	hideShowADM(false); }

}
}

function addTicket() {
//alert ("in add ticket");
	if (validateForm(false)) {
		var tnrStr = frm.ticketNumber.value;

	//alert ("tns Str in add ticket " + tnrStr);

	if (tnrStr!="") {
			if (validNumber(tnrStr, 1000000000, 9999999999)) {

				var winWidth = 750;
				var winHeight = 390;

				var newTicket = window.showModalDialog('/stella/stellabsp?cmd=addrefundticket&ticketNumber='+tnrStr+
									'&docType='+frm.documentType.value, '', 'dialogHeight:'+winHeight+'px;dialogWidth:'+
										winWidth+'px;help:no;scroll:no;status:no');
				if (null != newTicket && '' != newTicket) {
					frm.postTicket.value = newTicket;
				//	frm.docType.value = docType;
//                    frm.documentType.value  = docType;         
					frm.cmd.value = "saverefundticket";
					frm.submit();	
					 }
		   } else {
				frm.ticketNumber.focus();
				alert("Please enter a valid ticket number in the format 9999999999");
			}
         } // tnsStr

  }  // if validate form
}


function editTicket() {
	var lineNo = isLineSelected();
	if (lineNo > -1) {
		var winWidth = 750;
		var winHeight = 390;

		var newTicket = window.showModalDialog('/stella/stellabsp?cmd=editrefundticket&ticketLineSelect='+lineNo+
									'&docType='+frm.documentType.value, '', 'dialogHeight:'+winHeight+'px;dialogWidth:'+
									winWidth+'px;help:no;scroll:no;status:no');


		if (null != newTicket && '' != newTicket) {

			frm.postTicket.value = newTicket;
			frm.cmd.value = "saverefundticket";
			frm.submit();
		}
	}
}


function selectLine(lineNo) {
	if (typeof frm.ticketLineSelect.length == "undefined")		//there's only one option, not an array of options
		frm.ticketLineSelect.checked = true;
	else
		for (var i=0; i<frm.ticketLineSelect.length; i++)
			if (frm.ticketLineSelect[i].value == lineNo) {
				frm.ticketLineSelect[i].checked = true;
				return;
			}
}


function isLineSelected() {
	var selectedLine = -1;
	if (typeof frm.ticketLineSelect.length == "undefined") {	//there's only one option, not an array of options
		if (frm.ticketLineSelect.checked == true)
			return frm.ticketLineSelect.value;
	} else {
		for (var i=0; i<frm.ticketLineSelect.length && selectedLine==-1; i++) {
			if (frm.ticketLineSelect[i].checked == true) {
				return frm.ticketLineSelect[i].value;
			}
		}
	}
	alert("Please select line to Edit or Remove");
	return -1;
}



function removeTicket() {
	if (isLineSelected() > -1) {
		frm.cmd.value = "removerefundticket";
		frm.submit();
	}
}


function hideShowAdmReason() {
  var sel = frm.admReasonCode;
  var txt = frm.admReasonText;
  var a = sel.options[sel.selectedIndex].value.split("~");
  if (a[2]=='Y') {
    window.document.all.admSpan7.style.display = 'inline';
  } else {
    window.document.all.admSpan7.style.display = 'none';
    txt.value = "";
  }
}


function doAdmReasonCodeChange() {
  var txt = frm.admReasonText;
  txt.value = ""; //a[1];
  hideShowAdmReason();
}


function doOnLoad() {

	frm = document.forms[0];
	if (frm.documentNumber.value != ""){
//alert("inside document no if ");
		validDocumentNo(false);
//alert("after validate doc no  ");
		updateRefundTypeSelect();
     }
	if (ticketsAdded) {
		frm.ticketNumber.focus();
		frm.documentNumber.readOnly = true;
// Added as fix so when coming from search screen , it should make doc type read only
		frm.documentNumber.readOnly = true;
	} else {
		frm.documentNumber.focus();
	}
	window.moveTo(	parseInt((window.screen.availWidth-document.body.offsetWidth) / 2),
					parseInt((window.screen.availHeight-document.body.offsetHeight) / 2));
}

function saveTicket() {

if (validateForm(false)) {
//alert ("in else validate true");
	frm = document.forms[0];
    frm.cmd.value="saverefund"; 
    frm.submit();

}

}
</script>
</head>

<body onLoad="doOnLoad();">
<form method="post" action="/stella/jsp/postrefund.jsp" >
<input name="postTicket" type="hidden" value="">

<input name="documentType" readonly="true"  type="hidden"  value="<%= refund.getDocumentType()%>" > 
<input name="cmd" type="hidden" value="">
<input name="editedTicketLine" type="hidden" value="">
<input name="iataNumber" type="hidden" value="<%= refund.getIataNumber() %>">
<h3>Manual Refund Entry</h3>
    <table width="100%" border="0" cellspacing="0">
      <tr>
        <th>Doc. Number:</th>
        <%-- <td><input name="documentNumber" type="text" tabindex="1" onChange="validDocumentNo(true);" size="12" maxlength="10" value="<%= refund.getDocumentNumber() %>"></td>  --%>

        <td><input name="documentNumber" type="text" tabindex="1" size="12" maxlength="10"  value="<%= refund.getDocumentNumber() %>"></td> 

         
<%--         <td><input name="docType" type="text" readonly="true" tabindex="-1" size="30" value="<%= refund.getDocumentType() %>"></td>  --%>


<%--  <input name="documentType" readonly="true" tabindex="-1" size="5" value="<%= refund.getDocumentType() %>">type="hidden"  --%>
		 <th>Type:</th> 
         <td> <stella:stellaSelector selectionType="refundTypeSelect" tabindex="2"/></td>

        <th>Date:</th>
        <td><input name="documentDate" type="text" tabindex="3" onBlur="validDateField(this, true, fromDate); return false;" size="11" maxlength="11" value="<%= refund.getDocumentDate() %>"></td>
        <th>Airline:</th>
        <td style="background-color: #ffffff; border: 1px solid black;"><%= rtcb.getAirlineName() %></td>
      </tr>
      <tr>
        <td colspan="4"></td>
<%--        <th>IATA No.</th><td><input name="iataNumber" type="text" tabindex="7" onBlur="validNumberField(this, true, 1, 999999999);" size="11" maxlength="9" value="<%= refund.getIataNumber() %>"></td> --%>
        <th>IATA No.</th><td><stella:stellaSelector selectionType="iataSelect" tabindex="5"/></td>
        <th>Doc. Total:</th>
        <td><input name="documentTotal" type="text" style="text-align: right;" tabindex="-1" readonly="true" size="11" maxlength="11" value="<%= rtcb.getDocumentTotal() %>"></td>
      </tr>
      <tr>
        <th><span id="admSpan1" style="display: none;">Dispute ADM:</span></th>
        <td><span id="admSpan2" style="display: none;"><input name="disputeAdm" type="checkbox" style="border: 0px;" tabindex="6" value="Y" <%= refund.getDisputeAdm().equals("Y")?"checked":"" %>></span></td>
        <th><span id="admSpan3" style="display: none;">Dispute Date:</span></th>
        <td><span id="admSpan4" style="display: none;"><input name="disputeDate" type="text" tabindex="7" onBlur="validDateField(this, true, fromDate); return false;" size="11" maxlength="11" value="<%= refund.getDisputeDate() %>"></span></td>
        <th><span id="admSpan5" style="display: none;">Reason:</span></th>
        <td colspan="3">
          <span id="admSpan6" style="display: none;">
            <stella:stellaSelector selectionType="amdReason" tabindex="8"/>
          </span>
          <span id="admSpan7" style="display: none;">
            <input name="admReasonText" type="text" tabindex="9" size="36" value="<%= refund.getAdmReasonText() %>">
          </span>
        </td>
      </tr>
    </table>
    <hr/>
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr><td style="text-align: center;">
        Ticket No: <input name="ticketNumber" type="text" tabindex="10" size="12" maxlength="10">
        <button accesskey="a" onClick="addTicket();"><u>A</u>dd</button>
        <%= rtcb.getRefundTickets().size()>0?"<button accesskey=\"e\" tabindex=\"-1\" onClick=\"editTicket();\"><u>E</u>dit</button>":"" %>
        <%= rtcb.getRefundTickets().size()>0?"<button accesskey=\"r\" tabindex=\"-1\" onClick=\"removeTicket();\"><u>R</u>emove</button>":"" %>
      </td></tr>
    </table>
    
	<div style="display: <%= refund.getDocumentType().equals("")?"none":"block" %>">
<% int lineNo = 0; %>
<% boolean ACM = refund.getDocumentType().equals("ACM"); %>
<% boolean ADM = refund.getDocumentType().equals("ADM"); %>
<% boolean MAN = refund.getDocumentType().equals("MAN"); %>
<% boolean MRN = refund.getDocumentType().equals("MRN"); %>
	<table cellspacing="0" class="dataTable">
		<tr>
			<th>&nbsp;</th>
			<th>#</th>
			<th>Ticket No</th>
			<%= ACM || ADM || MRN?"<th>Airline Penalty</th>":"" %>
			<%= MRN?"<th>Fare Used</th>":"" %>
			<%= ACM || ADM?"<th>Seat Cost</th>":"" %>
			<%= MAN?"<th>Seat Adjustment</th>":"" %>
			<%= MRN?"<th>Tax Adjust</th>":"" %>
			<%= ACM || ADM?"<th>Tax Cost</th>":"" %>
			<%= MAN?"<th>Tax Adjustment</th>":"" %>
			<%= MRN?"<th>Total Refund</th>":"" %>
			<%= ACM || ADM?"<th>Total Cost</th>":"" %>
			<%= MAN?"<th>Total Adjustment</th>":"" %>
		</tr>
<% if (rtcb.getRefundTickets().size() > 0 ) { %>		
<util:iterate id="ticket" type="uk.co.firstchoice.stella.frontend.RefundTicket" collection="<%= rtcb.getRefundTickets()%>" >
<% lineNo++;
   if (!ticket.isDeleted()) { %>
		<tr class="<%="linkline"+(lineNo%2) %>" onClick="selectLine(<%= lineNo-1 %>);">
			<td>
	<input name="ticketLineSelect" <%= (lineNo<10)?("accesskey=\""+lineNo+"\""):("") %> type="radio" class="rowradio" value="<%= lineNo-1 %>" />
<% } else { %>
		<tr class="<%="linkline"+(lineNo%2) %>">
			<td>
	Deleted
<% } %>
			</td>
			<td><%= (lineNo<10 && !ticket.isDeleted())?("<u>"+lineNo+"</u>"):(""+lineNo) %></td>
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
    <p/><hr/>
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
		<td style="text-align: center;">
<%--			<button accesskey="s" tabindex="-1" onClick="if (validateForm(true)) {frm.cmd.value='saverefund'; this.disabled=true; frm.submit();}"><u>S</u>ave</button> --%>
 
	<button name "Save" accesskey="s" onClick="saveTicket();"><u>S</u>ave</button>
	<button accesskey="c" tabindex="-1" onClick="if (confirm('Cancel ticket and loose input')) window.close();"><u>C</u>ancel</button>

		</td>
	  </tr>
    </table>
    <h6><%= refund.getSaveError() %></h6>
</form>
</body>
</html>
