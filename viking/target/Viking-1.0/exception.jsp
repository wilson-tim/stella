<%@ page import="java.util.*" import="java.io.*" session="false" %>
<%
	Exception exceptionJsp = (Exception)request.getAttribute("org.apache.struts.action.EXCEPTION");
%>
<html>
<head>
<title>Error Page</title>
<style>
th {text-align: right; font-size: 10pt;}
td {font-size: 10pt;}
</style>
</head>
<body style="font-family: Helvetica; font-size: 10pt;">
<center><h2><font color=#FF0000>Error Page</font></h2></center>
<p/>
We have unfortunately encountered an unforseen error.
Please inform your helpdesk about the following error by copying it and sending it in an email. Once sent please try restarting the application.
<hr/>
Exception: <b><%= exceptionJsp.toString() %></b> on <%= new java.util.Date() %>
<p/>
Stack trace:
<pre>
<%
    ByteArrayOutputStream ostr = new ByteArrayOutputStream();
    exceptionJsp.printStackTrace(new PrintStream(ostr));
    out.print(ostr);
%>
</pre>
<table>
	<tr>
		<th>User:</th>
		<td><%= request.getRemoteUser() %><td/>
	</tr>
	<tr>
		<th>URL:</th>
		<td><script>document.write(location.href);</script><td/>
	</tr>
	<tr>
		<th>Method:</th>
		<td><%= request.getMethod() %></td>
	</tr>
	<tr>
		<th>Parameters:</th>
		<td><%
		boolean first = true;
		for (java.util.Enumeration e = request.getParameterNames(); e.hasMoreElements(); ) {
			String pName = (String)e.nextElement();
			String[] values = request.getParameterValues(pName);
			for (int i=0; i<values.length; i++) {
				out.print((first?"":"&")+pName+"="+values[i]);
			}
			first = false;
		}
%></td>
	</tr>
</table>
<hr/>
</body>
</html>