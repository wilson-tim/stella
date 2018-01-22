<%@ page errorPage="ErrorPage.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="stellatags" prefix="stella"%>
<%@ taglib uri="firstchoice" prefix="firstchoice" %>

<html>
<head>
<title>Stella Menu Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="stella.css" rel="stylesheet" type="text/css">
<script>
var frm;

function go(cmd) {
	if (cmd == 'startsearch' || cmd == 'startexceptions') {
		var width = parseInt(window.screen.availWidth * 0.98);
		var height = parseInt(window.screen.availHeight * 0.95);
	} else {
		var width = 900;
		var height = 500;
	}
	var maxHeight = window.screen.availHeight;
	var maxWidth = window.screen.availWidth;
	var leftPoint = parseInt(maxWidth / 2) - parseInt(width / 2);
	var topPoint = parseInt(maxHeight / 2) - parseInt(height / 2);
	/*var win = window.open('/stella/stellabsp?cmd='+cmd,cmd,
		'width='+width+',height='+height+
		',scrollbars=yes,menubar=no,location=no,status=no,toolbar=no,resizable=yes',false);
		*/
		
		var win = window.open('/stella/stellabsp?cmd='+cmd,cmd,
				'width='+width+',height='+height+
		',scrollbars=yes,menubar=no,location=no,status=no,toolbar=no,resizable=yes',false);
		
	win.focus();
//	frm.cmd.value = cmd;
//	frm.submit();
}
</script>
</head>
<body style="background-color: #ffffff; text-align: center;"
	onLoad="frm=document.forms[0];">
  <form method="post" action="/stella/stellabsp"><input type="hidden" name="cmd" value=""> 
<h2><font color="#FF0000">Stella/BSP (Specialist Business)</font></h2>
<span class="versioninfo">
<table>
	<tr>
		<stella:stellaSelector selectionType="environmentInfo" />
		<th>Server:</th>
		<td><%=request.getServerName()%></td>
		<th>User:</th>
		<td><%=request.getRemoteUser()%></td>
	</tr>
</table>
</span>
<hr />
<p /><img src="/stella/images/artois3.jpg">
<p />
<table>
	<tr style="vertical-align: top;">
		<td>Please Select:</td>
		<td><a href="JavaScript:go('createticket')">Input Ticket</a> | 
            <a href="JavaScript:go('createrefund')">Input Other Documents</a> | 
            <a href="JavaScript:go('startsearch')">Search Documents</a> | 
            <a href="JavaScript:go('startexceptions')">Examine Exceptions</a> | 
<!--        <a href="JavaScript:go('usermaint')">User Maintenance</a> |  --> 
			<a href="JavaScript:if (confirm('Sure you wish to log off Stella')) {frm.cmd.value='logoff'; frm.submit();}">Log Off</a>
         </td>
	</tr>

	<tr>
		<td>Maintenance:</td>
		<td><a href="JavaScript:go('startairlinemapping')">Airline Setup</a> |
		<a href="/genericmaint/genericmaint?appl=STELLA&table=BRANCH" target="gmaint">Branch</a> |
		<a href="/genericmaint/genericmaint?appl=STELLA&table=BRANCH_GROUP" target="gmaint">Branch Group</a> |
		<a href="/genericmaint/genericmaint?appl=STELLA&table=BRANCH_GROUP_ALLOCATION" target="gmaint">Branch Group Allocation</a> |
		<a href="/genericmaint/genericmaint?appl=STELLA&table=TICKET_RANGE" target="gmaint">Ticket Range</a> |
		<a href="/genericmaint/genericmaint?appl=STELLA&table=TICKETING_AGENT" target="gmaint">Ticketing Agents</a>
		</td>
	</tr>



</table>
</body> 
</html>
