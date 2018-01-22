<%@ page errorPage="jsp/ErrorPage.jsp" %>
<%@ page import="java.util.*" session="true" %>
<jsp:useBean id="refundletter" class="uk.co.firstchoice.stella.frontend.RefundLetterBean" scope="session"/>
<html>
<head>
<title>Refund Letter Part One</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="stella.css" rel="stylesheet" type="text/css">
<style>
th { text-align: left; font-weight: bold; }
</style>
<script src="validation.js"></script>
<script>
var frm;

function doOnLoad() {
	frm=document.forms[0];
	window.moveTo(	parseInt((window.screen.availWidth-document.body.offsetWidth) / 2),
					parseInt((window.screen.availHeight-document.body.offsetHeight) / 2));
}

function validate() {
	var found = 0;
	if (frm.selectedTickets.length) {	//has a length property, is an array - there are more than 1 tkt
		for (var i=0; i<frm.selectedTickets.length; i++)
			found += frm.selectedTickets[i].checked?1:0;
	} else {
		found = frm.selectedTickets.checked?1:0;
	}
	if (found==0) {
		alert("Please select ticket(s) to dispute");
		return false;
	}
	if (frm.disputeReason.value == "") {
		alert("Please enter reason for dispute");
		frm.disputeReason.focus();
		return false;
	}
	frm.numticket.value = found;
	return true;
}

</script>
</head>
<body onLoad="doOnLoad();">
<form method="post" action="/stellabsp?cmd=refundletter2" >
<input type="hidden" name="numticket">
<h3>Refund Letter</h3>
<p/>
<table align="left" width="100%" cellpadding="2" cellspacing="0">
	<tr>
		<th>
			Airline: <%= refundletter.getAirlineName() %> (<%= refundletter.getAirlineNum() %>)
		</th>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<th>Select tickets to dispute:</th>
	</tr>
	<tr>
		<td>
<%
	for (Enumeration e = refundletter.getAllTickets().elements(); e.hasMoreElements(); ) {
		String tkt = (String)e.nextElement();
		String tktno = tkt.substring(0, tkt.indexOf(";"));
		String tktna = tkt.substring(tkt.indexOf(";")+1);
		out.println("<input type=\"checkbox\" name=\"selectedTickets\" value=\""+
					tktno+"\">"+tktno+" - "+tktna+"<br/>");
	}
%>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<th>Enter reason for dispute:</th>
	</tr>
	<tr>
		<td>
			<input type="text" name="disputeReason" style="width: 100%;">
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td style="text-align: center;">
			<button onClick="if (validate()) frm.submit();">Next ></button>&nbsp;
			<button onClick="window.close();" style="color: red;">Cancel</button>
		</td>
	</tr>
</table>
</form>
</body>
</html>
