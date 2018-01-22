<%@ page isErrorPage="true" %>
<%@ page import="java.util.*" import="java.io.*" session="true" %>
<html>
<head><title>JSP Error Page</title></head>

<body bgcolor=#ffffff>

<font face="Helvetica">

<h2><font color=#FF0000>Error Page</font></h2>
<hr/>

<p/>
Please inform the helpdesk about the following error:

<p/>
An exception was thrown: <b> <%= exception.toString() %>

<p/>
With the following stack trace:
<pre>

<%
    ByteArrayOutputStream ostr = new ByteArrayOutputStream();
    exception.printStackTrace(new PrintStream(ostr));
    out.print(ostr);
%>
</pre>

<p>
<hr>
</body>
</html>