<%@ page errorPage="ErrorPage.jsp" %>
<html>
<head><title>Ticket Not Found</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="stella.css" rel="stylesheet" type="text/css">
</head>
<body style="background-color: #ffffff; text-align: center;">

<font face="Helvetica">
<p/><p/>
<h6><%= request.getParameter("error") %></h6>
<p/><p/>
<button accesskey="c" onClick="window.close();"><u>C</u>lose</button>
</body>
</html>