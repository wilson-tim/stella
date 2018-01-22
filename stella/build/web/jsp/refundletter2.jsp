<%@ page errorPage="jsp/ErrorPage.jsp" %>
<jsp:useBean id="refundletter" class="uk.co.firstchoice.stella.frontend.RefundLetterBean" scope="session" />
<jsp:setProperty name="refundletter" property="*"/>
<html>
<head>
<title>Refund Letter 2</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="/stella/stella.css" rel="stylesheet" type="text/css">
<style>
</style>
<script src="/stella/validation.js"></script>
<script>
</script>
</head>
<%!
private String notNull(String inStr) {
	return (inStr==null||inStr.equals("")?"":(inStr+"<br/>"));
}
%>
<body>
<form method="post" action="jsp/refundletter3.jsp" >
<table width="100%" cellspacing="0" cellpadding="0"><tr><td style="text-align: left;">
<table width="100%" cellspacing="0" cellpadding="0">
<tr><td><%= refundletter.getFormattedCreationDate() %></td></tr>
<tr><td>&nbsp;</td></tr>
<tr><td>Our Ref: <%= refundletter.getLetterReference() %></td></tr>
<tr><td>&nbsp;</td></tr>
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
<p/>
<p/>
Dear <%= refundletter.getAirlineBean().getAirlineAddressee() %><br/>
<p/>
<textarea name="letterText1" rows="2" style="width: 100%"><%= refundletter.getLetterText1() %></textarea>
<%
	for (int i=0; i<refundletter.getSelectedTickets().length-1; i++)
		out.print(refundletter.getSelectedTickets()[i]+", ");
	out.print(refundletter.getSelectedTickets()[refundletter.getSelectedTickets().length-1]);
%>
<textarea name="letterText2" rows="4" style="width: 100%"><%= refundletter.getLetterText2() %>£</textarea>
<input type="text" name="unmatchedAmount" value="<%= refundletter.getUnmatchedAmount() %>" onBlur="return validAmount(this, 0.0);">
<textarea name="letterText3" rows="2" style="width: 100%"><%= refundletter.getLetterText3() %></textarea>
<input type="text" name="disputeReason" style="width: 100%;" value="<%= refundletter.getDisputeReason() %>">
<textarea name="letterText4" rows="2" style="width: 100%"><%= refundletter.getLetterText4() %></textarea>
<p/>
Yours faithfully,
<p/><p/>
<%= refundletter.getLetterAuthor() %><br/>
First Choice BSP Accounts
</td></tr></table>
<p/>
<input type="button" onClick="history.back();" value="< Back"> - <input type="submit" value="Next >">
</form>
</body>
</html>
