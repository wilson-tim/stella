<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head><title></title></head>
  <body>
	<script>
<% if (request.getAttribute("doClose") != null &&
			 request.getAttribute("doClose").equals("true") &&
			 request.getAttribute("error") == null) { %>
	window.opener.doClose();
<% } else { %>
	<% if (request.getAttribute("error") != null) { %>
		alert("<%= (String)request.getAttribute("error") %>");
	<% } %>
		  var pForm = window.opener.document.forms[0];
		  if (pForm.code) pForm.code.disabled = false;
		  pForm.action.value = "reload";
	<% if (request.getAttribute("seriesNo") != null || request.getParameter("seriesNo") != null) { %>
		  if (pForm.seriesNo) {
			  pForm.seriesNo.value = '<%= request.getAttribute("seriesNo")==null?request.getParameter("seriesNo"):(String)request.getAttribute("seriesNo") %>';
		  }
	<% } %>
	pForm.submit();
<% } %>
	window.close();
	</script>
  </body>
</html>
