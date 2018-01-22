<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
String series  = request.getParameter("seriesNo");
String season  = request.getParameter("season");
String version = request.getParameter("version");
String queryStr =	"?seriesNo="+request.getParameter("seriesNo")+
									(version==null?"":("&version="+request.getParameter("version")))+
									(season==null?"":("&season="+request.getParameter("season")));
%>

<html>
  <head><title>Flight Operations Input</title></head>
<% if (request.getParameter("seriesNo") == null) { %>
  <frameset rows="100%" framespacing="0">
	<frame src="flighttop.do" noresize>
<% } else { %>
  <frameset rows="52%,48%" framespacing="0">
	<frame src="flighttop.do<%= queryStr %>" noresize>
	<frame src="flightbot.do<%= queryStr %>" noresize>
<% } %>
  </frameset>
</html>