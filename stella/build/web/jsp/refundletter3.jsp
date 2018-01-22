<%@ page errorPage="ErrorPage.jsp" %>
<jsp:useBean id="refundletter" class="uk.co.firstchoice.stella.frontend.RefundLetterBean" scope="session"/>
<jsp:setProperty name="refundletter" property="*"/>
<html>
<head>
<title>Refund Letter 3</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="stella.css" rel="stylesheet" type="text/css">
<style>
@media print {
	body, td {
		font-family: "Times New Roman", Serif;
		font-size: 11pt;
	}
	button {
		display: none;
	}
}
</style>
<script src="validation.js"></script>
<script>
var frm;

function doOnLoad() {
	frm=document.forms[0];
	window.resizeTo(750,650);
	window.moveTo(	parseInt((window.screen.availWidth-document.body.offsetWidth) / 2),
					parseInt((window.screen.availHeight-document.body.offsetHeight) / 2));
}

</script>
</head>
<%!
private String notNull(String inStr) {
	return (inStr==null||inStr.equals("")?"":(inStr+"<br/>"));
}
%>
<body onLoad="doOnLoad();">
<form method="post" action="/stellabsp?cmd=saverefundletter" >
<table width="100%" cellspacing="0" cellpadding="0"><tr><td style="text-align: left;">
<table width="100%" cellspacing="0" cellpadding="0">
<tr><td><%= refundletter.getFormattedCreationDate() %><p/>&nbsp;<p/></td></tr>
<tr><td>Our Ref: <%= refundletter.getLetterReference() %><p/>&nbsp;<p/></td></tr>
<tr style="vertical-align: top;">
	<td align="left">
		<table cellspacing="0" cellpadding="0">
			<tr><td>
				Refunds Department<br/>
				<%= 
					notNull(refundletter.getAirlineBean().getAirlineName())
				%><%=
					notNull(refundletter.getAirlineBean().getAirlineAddress1())
				%><%=
					notNull(refundletter.getAirlineBean().getAirlineAddress2())
				%><%=
					notNull(refundletter.getAirlineBean().getAirlineCity())
				%><%=
					notNull(refundletter.getAirlineBean().getAirlineCounty())
				%><%=
					notNull(refundletter.getAirlineBean().getAirlinePostcode())
				%><%=
					notNull(refundletter.getAirlineBean().getAirlineCountry())
				%>
			</td></tr>
		</table>
	</td>
	<td style="text-align: right;">
		<table cellspacing="0" cellpadding="0">
			<tr><td><%= refundletter.getOfficeAddress().replaceAll("~", "<br/>") %></td></tr>
		</table>
	</td>
</tr>
</table>
<p/>
&nbsp;<p/>
&nbsp;<p/>
Dear <%= refundletter.getAirlineBean().getAirlineAddressee() %><br/>
<p/>
<% if (refundletter.isNewRefundLetter()) { %><%=
refundletter.getLetterText1().replaceAll("\n", "<br>")
%><%
	for (int i=0; i<refundletter.getSelectedTickets().length-1; i++)
		out.print(refundletter.getSelectedTickets()[i]+", ");
	out.print(refundletter.getSelectedTickets()[refundletter.getSelectedTickets().length-1]);
%><%=
refundletter.getLetterText2().replaceAll("\n", "<br>") %><%=
refundletter.getUnmatchedAmount()
%>
<%=
refundletter.getLetterText3().replaceAll("\n", "<br>")
%><%=
refundletter.getDisputeReason()
%><%=
refundletter.getLetterText4().replaceAll("\n", "<br>")
%>
<% } else { %><%=
refundletter.getSavedLetterText()
%><% } %>
<p/>
&nbsp;<p/>
&nbsp;<p/>
Yours faithfully,
<p/>
&nbsp;<p/>
<%= refundletter.getLetterAuthor() %><br/>
First Choice BSP Accounts
</td></tr></table>
<p/>
<% if (refundletter.isNewRefundLetter()) { %>
<button onClick="history.back();">< Back</button> <button onClick="print(); frm.submit();">Save & Print</button>
<% } else { %>
<button onClick="print(); window.close();">Re-print</button>
<% } %>
</form>
</body>
</html>
