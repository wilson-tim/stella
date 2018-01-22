<%@ page errorPage="/ErrorPage.jsp" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/vikingtags.tld" prefix="viking" %>
<%@page session="true" %>
<html>
<head>
<title>Viking login</title>
<style>
th {text-align: right;}
body {text-align: center;}
</style>
<script>

function doOnLoad() {
	var frm=window.document.forms[0];
	var result = location.search.match(/(\S+)user=([\w%]+)(&*\S*)/i);
	if (result != null) {
		frm.j_username.value = unescape(result[2]);
		frm.j_password.focus();
	} else
		frm.j_username.focus();
}
</script>
</head>
<body onLoad="doOnLoad();">
<form method="POST" action="j_security_check">
<h1>Please login to access Viking</h1>
<p>
<table border="0" cellpadding="3" cellspacing="0">
	<tr>
		<th>User Name:</th>
		<td><input type="text" name="j_username" value=""></td>
		<td></td>
	</tr>
	<tr>
		<th>Password:</th>
		<td><input type="password" name="j_password"></td>
		<td><input type="submit" name="action" value="Login"></td>
	</tr>
</table>
</form>
</body>
</html>