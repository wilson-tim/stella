<%@ page errorPage="ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" session="true" %>
<%@ taglib uri="utiltags" prefix="util" %>
<jsp:useBean id="matchdescription" class="uk.co.firstchoice.stella.frontend.BeanCollectionBean" scope="request"/>
<html>
<head>
<title>Exception Code Description</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="stella.css" rel="stylesheet" type="text/css">
<script src="validation.js"></script>
</head>

<body>
<h3>Exception Code Description</h3>
<p/><hr/>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td style="text-align: center;">
      <button accesskey="c" onClick="window.close();"><u>C</u>lose</button>
    </td>
  </tr>
</table>
<hr/><p/>
<h6><jsp:getProperty name="matchdescription" property="error" /></h6>
<% int lineNo = 0; %>
<table cellspacing="0" cellpadding= "2" class="dataTable">
	<tr>
		<th>#</th>
		<th>Reason<br/>Code</th>
		<th>Reason Description</th>
	</tr>

	<% if (matchdescription.getBeanCollection().size() > 0 ) { %>	
<util:iterate id="hist" type="uk.co.firstchoice.stella.frontend.MatchDescriptionBean" collection="<%= matchdescription.getBeanCollection() %>" >
	<tr class="<%="line"+(lineNo%2) %>">
		<td><%= ++lineNo %></td>
		<td style="text-align: center;"><jsp:getProperty name="hist" property="reasonCode"/></td>
		<td style="text-align: left;"><jsp:getProperty name="hist" property="reasonDescription"/></td>
	</tr>
</util:iterate>
<% } %>

</table>
<%= matchdescription.getBeanCollection().size() == 0?"<p/><h6>No descriptions found</h6>":"" %>
</form>
</body>
</html>
