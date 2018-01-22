<%@ page errorPage="/ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/vikingtags.tld" prefix="viking" %>
<jsp:useBean	id="sellbuyform"
							class="uk.co.firstchoice.viking.gui.SellBuyFormBean"
							scope="request" />

<html:html>
<head>
<title>Warning</title>
<link href="<html:rewrite page="/css/viking.css"/>" type="text/css" rel="stylesheet"/>

<style>
td {vertical-align: top; text-align: center;}
input {border: 1px solid black;}
table {border: 1px solid darkblue;}
</style>
<script>
function doOnLoad() {
	frm=window.document.forms[0];
	window.resizeTo(document.body.offsetWidth + 450, document.body.offsetHeight + 100);
	window.moveTo(	parseInt((window.screen.availWidth-document.body.offsetWidth) / 2),
									parseInt((window.screen.availHeight-document.body.offsetHeight) / 2));
}
</script>
<html:base/>
</head>
<body style="text-align: center;" onLoad="doOnLoad();">
<html:form action="flightsellbuy.do">
<h6>Warning</h6>
<table>
<tr>
<td>
	<input type="hidden" name="seriesNo"     value= "<%=sellbuyform.getSeriesNo()%>" />
	<input type="hidden" name="type"         value="<%=sellbuyform.getType()%>"/>
	<input type="hidden" name="custSupp"     value="<%=sellbuyform.getCustSupp()%>"/>
	<% for (int i= 0; i < sellbuyform.getSectors().length; i++) { %>
		<input type="hidden" name="sectors"      value="<%=sellbuyform.getSectors()[i]%>"/>
	<%} %>
	<% for (int i= 0; i < sellbuyform.getStartDate().length; i++) { %>
		<input type="hidden" name="startDate"    value="<%=sellbuyform.getStartDate()[i]%>"/>
	<% } %>
	<% for (int i= 0; i < sellbuyform.getEndDate().length; i++) { %>
		<input type="hidden" name="endDate"      value="<%=sellbuyform.getEndDate()[i]%>"/>
	<% } %>
	<input type="hidden" name="seatClass"    value="<%=sellbuyform.getSeatClass()%>"/>
	<input type="hidden" name="comments"     value="<%=sellbuyform.getComments()%>"/>
	<input type="hidden" name="action"		 value="delete"/>
	<input type="hidden" name="single"		 value="<%=sellbuyform.getSingle()%>"/>
	<input type="hidden" name="deleteAfterWarning" value="Y" />
	<input type="hidden" name="amendmentType" value="<%=sellbuyform.getAmendmentType()%>"/>
</td>
</tr>
<tr>
<td>
	<h5>Records already exist in PAX tables. Do you want to continue to delete the records.</h5>
</td>
</tr>
</table>
<p/>
<button	type="submit"
accesskey="Y"
        name="saveBtn"
				style="color: #008000;"><u>Y</u>es</button>
<button	accesskey="N"
				style="color: #c00000;"
				onClick="window.close();"><u>N</u>o</button>
</html:form>
</body>
</html:html>
