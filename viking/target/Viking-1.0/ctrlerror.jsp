<%@ page errorPage="/ErrorPage.jsp" %>
<html>
<head><title>Servlet Error Page</title></head>

<body bgcolor=#ffffff>

<font face="Helvetica">

<h2><font color=#FF0000>Server Error Page</font></h2>
<hr/>

<p/>
Please inform the helpdesk about the following error:

<p/>
The following error occured: <b> <%= request.getParameter("errmsg") %>

<p>
<hr>
</body>
</html>