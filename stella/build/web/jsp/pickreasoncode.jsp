<%@ page errorPage="jsp/ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="stellatags" prefix="stella" %>
<html>
<head>
<title>Pick new reason code</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="/stella/stella.css" rel="stylesheet" type="text/css">
<script>
function doSelect(code) {
	window.returnValue = code;
	window.close();
}
</script>
</head>
<body>
<form>
<h3>Pick New Reason Code</h3>
<p/><hr/>

<table border="0" cellpadding="0" cellspacing="0">
<% if (request.getParameter("bsp") == null) { %>
	<stella:stellaSelector selectionType="reasonCodeSelect" />
<% } else { %>
	<stella:stellaSelector selectionType="bspReasonCodeSelect" />
<% } %>
</table>
</form>
</body>
</html>