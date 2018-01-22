<%@ page errorPage="/ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<html>
<head>
<title>Audit trail for series <%= request.getParameter("seriesNo") %></title>
<link href="<html:rewrite page="/css/viking.css"/>" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="<html:rewrite page="/table.js"/>"></script>

<script type="text/javascript" src="<html:rewrite page="/validation.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/utils.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/datepicker.js"/>"></script>

<style>
.comments td {
	text-align: left;
	vertical-align: top;
	border: 1px solid darkblue;
	background-color: white;
	color: darkblue;
	font-size: 8pt;
}
.comments th {
	text-align: center;
	border: 1px solid darkblue;
	background-color: #f0f0f0;
	color: darkblue;
	font-weight: bold;
	font-size: 8pt;
}
</style>
<script>
var frm;

function getComment() {
	var width = window.screen.availWidth*80/100;
	var height = window.screen.availHeight*40/100;
	var leftPoint = parseInt(window.screen.availWidth / 2) - parseInt(width / 2);
	var topPoint = parseInt(window.screen.availHeight / 2) - parseInt(height / 2);
	var comm = window.showModalDialog('<html:rewrite page="/getCommentsPopup.jsp"/>', '', 'dialogHeight:'+height+'px;dialogWidth:'+
									width+'px;help:no;scroll:yes;status:no');
	if (comm != null) {
    var i = comm.indexOf(';');
		if (i > -1 && i < 5) {
      frm.amendmentType.value = comm.substr(0, i);
      frm.comments.value = comm.substr(i+1);
      frm.submit();
    }
	}
}


function doOnLoad() {
	window.moveTo(	parseInt((window.screen.availWidth-document.body.offsetWidth) / 2),
									parseInt((window.screen.availHeight-document.body.offsetHeight) / 2));
  frm = document.forms[0];
}
</script>
</head>

<body style="text-align: center;" onload="doOnLoad();">
<form action="viewcomments.do">
<input type="hidden" name="action" value="add" />
<input type="hidden" name="amendmentType" />
<input type="hidden" name="comments" />
<input type="hidden" name="seriesNo" value="<%= request.getParameter("seriesNo") %>" />
</form>
<table width="100%" border="0">
  <tr>
    <td width="33%"></td>
    <td width="34%"><h3>Audit trail for series <%= request.getParameter("seriesNo") %></h3></td>
    <td width="33%" align="right"><button onClick="getComment();">Add Comment</button></td>
  </tr>
</table>
<table cellspacing="0" cellpadding="4" border="0" width="100%" class="comments">
	<tr>
		<th>Date</th>
		<th>User</th>
		<th>Amendment Type</th>
		<th>Comment</th>
	<tr>
	<logic:iterate	id="comment"
									name="seriescomments"
									type="uk.co.firstchoice.viking.gui.CommentHolderBean">
		<tr>
			<td nowrap="true"><bean:write name="comment" property="date" /></td>
			<td nowrap="true"><bean:write name="comment" property="user" /></td>
			<td nowrap="true"><bean:write name="comment" property="amendmentType" /></td>
			<td><bean:write name="comment" property="comments" /></td>
		</tr>
	</logic:iterate>
</table>
</body>
</html>
