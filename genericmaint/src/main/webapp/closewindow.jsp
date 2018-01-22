<%@ page errorPage="ErrorPage.jsp" %>
<html>
<head>
<title>Error</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="genericmaint.css" rel="stylesheet" type="text/css">
<title></title>
</head>
<body>
<% if (request.getParameter("reload") != null) { %>
	<script>
		if (!window.opener.closed) window.opener.location.reload();
	</script>
<% } %>
<% if (request.getParameter("error") != null) { %>
	<h6><%= request.getParameter("error") %></h6>
	<input type="button" value="Close" onClick="window.close();" />
<% } else { %>
	<script>
		if (window.name == "am_airlinedetails") { <%-- we've just edited, added or deleted airline details --%>
			if (!window.opener.closed) {
				if (window.opener.name == "am_editdetails") {
					if (!window.opener.opener.closed) window.opener.opener.location.reload();
					window.opener.close();
				} else {
					window.opener.location.reload();
				}
			}
		}
		window.close();
	</script>
<% } %>
</body>
</html>
