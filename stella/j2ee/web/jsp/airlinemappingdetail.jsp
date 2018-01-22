<%@ page errorPage="ErrorPage.jsp" %>
<%@ page import="java.util.*" session="true" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="stellatags" prefix="stella" %>
<html>
<head>
<title>Airline Mapping</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="/stella/stella.css" rel="stylesheet" type="text/css">
<style>
select { width: 100%; }
</style>
<script src="validation.js"></script>
<script>
var frm;
var changed = false;

function selectorChange() {
	changed = true;
	frm.save.disabled = false;
}

function deSelectAll(selectorname) {
	var sel = eval("frm."+selectorname);
	for (var i = 0; i < sel.length; i++)
		sel.options[i].selected = false;
	selectorChange();
}

function doSubmit() {
	if (frm.userName) frm.userName.disabled = false;
	if (frm.airline) frm.airline.disabled = false;
	frm.submit();
}

function doOnLoad() {
	frm=document.forms[0];
	frm.save.disabled=true;
	window.moveTo(	parseInt((window.screen.availWidth-document.body.offsetWidth) / 2),
					parseInt((window.screen.availHeight-document.body.offsetHeight) / 2));
}

function doEditAirline(airline) {
	var aln = airline.substr(airline.length-3, 3);		//British Airways-125 -> 125
	var win = window.open('?cmd=airlinedetails&param='+aln,'am_airlinedetails',
				'width=600,height=450'+
				',scrollbars=yes,menubar=no,location=no,status=no,toolbar=no,resizable=yes',false);
	win.focus();
}

</script>
</head>

<body onLoad="doOnLoad();" style="text-align: center;">
<form method="post" action="/stella/stellabsp?cmd=saveairlinemapping">
<input type="hidden" name="type" value='<%= request.getParameter("type") %>'/>

<h3>Airline Mapping</h3>
<table cellspacing="0" cellpadding="5" width="100%">
  <tr style="background-color: #f0f0f0;">
<% if (request.getParameter("type").equals("user") || request.getParameter("type").equals("adduser")) { %>
    <th style="text-align: center; font-weight: bold; width: 50%">User</th>
	<th style="text-align: center; font-weight: bold; width: 50%">Mapped Airlines</th>
<% } else if (request.getParameter("type").equals("airline")) { %>
	<th style="text-align: center; font-weight: bold; width: 50%">Airline</th>
    <th style="text-align: center; font-weight: bold; width: 50%">Mapped Users</th>
<% } %>
  </tr>
  <tr style="vertical-align: top;">

<% if (request.getParameter("type").equals("user") || request.getParameter("type").equals("adduser")) { %>
	<td>
	<% if (request.getParameter("type").equals("user")) { %>
		<input size="35" name="userName" value='<%= request.getParameter("param") %>' <%= request.getParameter("param")==null?"":"disabled" %>>

	<% } else { %>
		<stella:stellaSelector selectionType="amAddUser"/>
	<% } %>
	</td>

	<td>
		<stella:stellaSelector selectionType="amAirlineSelect" amUser='<%= request.getParameter("param") %>' />
	
		<a href="javascript:deSelectAll('amAirlineSelect')">de-select all</a>
<% } else { %>
	<td>
		<input size="35" name="airline" value='<%= request.getParameter("param") %>' disabled>
		<br/><a href="javascript:doEditAirline('<%= request.getParameter("param") %>');">Edit airline details</a>
	</td>
	<td>
		<stella:stellaSelector selectionType="amUserSelect" amAirline='<%= request.getParameter("param") %>' />
		<a href="javascript:deSelectAll('amUserSelect')">de-select all</a>
<% } %>
		<p/>Press Ctrl for multiple select
	</td>
  </tr>
</table>

<p/>
<button name="save" accesskey="s" tabindex="-1" onClick="doSubmit();"><u>S</u>ave</button>
<button name="cancel" accesskey="c" tabindex="-1" onClick="if (!changed || confirm('Cancel and loose input')) window.close();"><u>C</u>ancel</button>
</form>
</body>
</html>
