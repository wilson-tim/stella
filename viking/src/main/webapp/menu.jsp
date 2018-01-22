<!--%@ page errorPage="/ErrorPage.jsp" %-->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/vikingtags.tld" prefix="viking" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<html>
<head><title>Viking Menu Page</title>
<link href="<html:rewrite page="css/viking.css"/>" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="<html:rewrite page="/table.js"/>"></script>

<script type="text/javascript" src="<html:rewrite page="/validation.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/utils.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/datepicker.js"/>"></script>

<style>
body {background: url(images/airplwallpaper.gif); text-align: center; }
<!-- .menuTable { width: 100%; } -->
.menuTable th { font-weight: bold; font-size: 10pt; }
.menuTable td { font-size: 9pt; padding-left: 10%; padding-right: 10%; background-color: #d0d0e0; border-width: 2px; border-style: outset; cursor: pointer; }
.menuTable .emptyCell { border: 0px; background-color: transparent; cursor: default; }
</style>
<script>
function op(url,target) {
	var win = window.open(url,target,'scrollbars=yes,menubar=yes,location=yes,status=yes,toolbar=yes,resizable=yes');
	win.moveTo(0,0);
	win.resizeTo(screen.availWidth,screen.availHeight);
}

function mi(td) {	// onMouseOver
	td.style.color = "white";
}

function mo(td) {	//onMouseOut 
	td.style.color = "black";
	td.style.borderStyle = "outset";
}

function md(td) {	// onMouseDown 
	td.style.borderStyle = "inset";
}

function mu(td) {	// onMouseUp 
	td.style.borderStyle = "outset";
}


</script>
</head>

<body>
<table cellspacing="3" cellpadding="0" style="table-layout: fixed; width: 100%; height: 100%; margin: 0px">
	<tr height="1"> <!-- establish the column layout -->
		<td style="width: 20%;"></td><td style="width: 60%;"></td><td style="width: 20%;"></td>
	</tr>
	<tr height="1%">
		<td colspan="3" style="text-align: center;">
			<h2><font color="#FF0000">Viking Seat Brokering</font></h2>
			<table>
				<tr>
					<viking:vikingSelector selectionType="environmentInfo" />
					<th class="versioninfo">Server:</th><td class="versioninfo"><%= request.getServerName() %></td>
					<th class="versioninfo">User:</th><td class="versioninfo"><%= request.getRemoteUser() %></td>
				</tr>
			</table>
			<hr/>
		</td>
	</tr>
	<tr>
		<td style="vertical-align: top; padding-top: 1cm;">
			<table class="menuTable" cellpadding="3" cellspacing="3">
				<tr><th>Please Select:</th></tr>
				<tr><td	onmouseover="mi(this);"
								onmouseout="mo(this);"
								onmousedown="md(this);"
								onmouseup="mu(this);"
								onclick="location.href='search.do'">Search</td></tr>
<%
		if (request.isUserInRole("VIKING_MANAGER") ||
				request.isUserInRole("VIKING_EDITOR")) { 
%>
				<tr><td	onmouseover="mi(this);"
								onmouseout="mo(this);"
								onmousedown="md(this);"
								onmouseup="mu(this);"
								onclick="op('editseries.do','_blank');">Add Series</td></tr>
<%	} %>
<%
		if (request.isUserInRole("VIKING_PAX_MANAGER")) { 
%>
				<tr>
					<td	onmouseover="mi(this);"
								onmouseout="mo(this);"
								onmousedown="md(this);"
								onmouseup="mu(this);"
								onclick="op('<html:rewrite page='/pax/index.do' />','_pax');">Pax Management</td>
				</tr>
<%	} %>
				<tr><td	onmouseover="mi(this);"
								onmouseout="mo(this);"
								onmousedown="md(this);"
								onmouseup="mu(this);"
								onclick="op('operations.do','_blank');">Operations</td></tr>
				<tr><td	onmouseover="mi(this);"
								onmouseout="mo(this);"
								onmousedown="md(this);"
								onmouseup="mu(this);"
								onclick="if (confirm('Are you sure you wish to log off Viking?')) location.href='logoff.do'">Logoff</td></tr>
				<tr><td class="emptyCell">&nbsp;</td></tr>
				<tr><th>Maintenance:</th></tr>
				<tr><td	onmouseover="mi(this);"
								onmouseout="mo(this);"
								onmousedown="md(this);"
								onmouseup="mu(this);"
								onclick="location.href='revenueaccount.do'">Cust/Supp Accounts</td></tr>

<!--  User Maintenace is commentd as security is moved to Ldap and users will be maintained using notes  -->
<!-- 				<tr><td	onmouseover="mi(this);"
								onmouseout="mo(this);"
								onmousedown="md(this);"
								onmouseup="mu(this);"
								onclick="op('<bean:message key="prod.host"/>/usermaint','gmaint')">User Maintenance</td></tr>
 -->

<%
        if ( request.isUserInRole("VIKING_MANAGER")) {   System.out.println("user  role is :  VIKING_MANAGER" ); }
        if ( request.isUserInRole("VIKING_EDITOR")) {   System.out.println("user  role is :  VIKING_EDITOR" ); }
        if ( request.isUserInRole("VIKING_READER")) {   System.out.println("user  role is :  VIKING_READER" ); }
        if ( request.isUserInRole("VIKING_EXTERNAL_READER")) {   System.out.println("user  role is :  VIKING_EXTERNAL_READER"); }
        if ( request.isUserInRole("VIKING_PAX_MANAGER")) {   System.out.println("user  role is :  VIKING_PAX_MANAGER" ); }
        if ( request.isUserInRole("VIKING_OPERATIONS")) {   System.out.println("user  role is :  VIKING_OPERATIONS" ); }
	    if ( request.isUserInRole("VIKING_RESTRICTED_EXTERNAL_READER")) {   System.out.println("user  role is :  VIKING_RESTRICTED_EXTERNAL_READER" ); }


		if (request.isUserInRole("VIKING_MANAGER") || request.isUserInRole("VIKING_EDITOR") &&  !request.isUserInRole("VIKING_READER") ) { 
%>
 				<tr><td	onmouseover="mi(this);"
								onmouseout="mo(this);"
								onmousedown="md(this);"
								onmouseup="mu(this);"
								onclick="location.href='versionsmaint.do'">Version Maintenance</td></tr>  
				<tr><td	onmouseover="mi(this);"
								onmouseout="mo(this);"
								onmousedown="md(this);"
								onmouseup="mu(this);"
								onclick="op('<bean:message key="prod.host"/>/genericmaint/genericmaint?appl=VIKING&table=VK_COUNTRY','gmaint')">Country Codes</td></tr>
				<tr><td	onmouseover="mi(this);"
								onmouseout="mo(this);"
								onmousedown="md(this);"
								onmouseup="mu(this);"
								onclick="op('<bean:message key="prod.host"/>/genericmaint/genericmaint?appl=VIKING&table=VK_GATEWAY','gmaint')">Airport Codes</td></tr>
				<tr><td	onmouseover="mi(this);"
								onmouseout="mo(this);"
								onmousedown="md(this);"
								onmouseup="mu(this);"
								onclick="op('<bean:message key="prod.host"/>/genericmaint/genericmaint?appl=VIKING&table=VK_CURRENCY','gmaint')">Currency Codes</td></tr>
				<tr><td	onmouseover="mi(this);"
								onmouseout="mo(this);"
								onmousedown="md(this);"
								onmouseup="mu(this);"
								onclick="op('<bean:message key="prod.host"/>/genericmaint/genericmaint?appl=VIKING&table=VK_SEASON','gmaint')">Seasons</td></tr>
				<tr><td	onmouseover="mi(this);"
								onmouseout="mo(this);"
								onmousedown="md(this);"
								onmouseup="mu(this);"
								onclick="op('<bean:message key="prod.host"/>/genericmaint/genericmaint?appl=VIKING&table=VK_CURRENCY_RATE','gmaint')">Exchange Rates</td></tr>
				<tr><td	onmouseover="mi(this);"
								onmouseout="mo(this);"
								onmousedown="md(this);"
								onmouseup="mu(this);"
								onclick="op('<bean:message key="prod.host"/>/genericmaint/genericmaint?appl=VIKING&table=VK_SEAT_CLASS','gmaint')">Seat Class</td></tr>
				<tr><td	onmouseover="mi(this);"
								onmouseout="mo(this);"
								onmousedown="md(this);"
								onmouseup="mu(this);"
								onclick="op('<bean:message key="prod.host"/>/genericmaint/genericmaint?appl=VIKING&table=VK_COMMENT_TYPE','gmaint')">Comment Types</td></tr>
                                <tr><td	onmouseover="mi(this);"
								onmouseout="mo(this);"
								onmousedown="md(this);"
								onmouseup="mu(this);"
								onclick="op('<bean:message key="prod.host"/>/genericmaint/genericmaint?appl=VIKING&table=VK_CARRIER','gmaint')">Carrier </td></tr>								
<tr><td	onmouseover="mi(this);"
								onmouseout="mo(this);"
								onmousedown="md(this);"
								onmouseup="mu(this);"
								onclick="op('<bean:message key="prod.host"/>/genericmaint/genericmaint?appl=VIKING&table=VK_FLIGHT_TYPE','gmaint')">Flight Type </td></tr>								
<tr><td	onmouseover="mi(this);"
								onmouseout="mo(this);"
								onmousedown="md(this);"
								onmouseup="mu(this);"
								onclick="op('<bean:message key="prod.host"/>/genericmaint/genericmaint?appl=VIKING&table=VK_MEAL_TYPE','gmaint')">Meal Type </td></tr>								




<% } %>
			</table>
		</td>
		<td style="text-align: center; vertical-align: middle;">
			<img src="<html:rewrite page="/images/viking.gif"/>">
		</td>
		<td></td>
	</tr>
</table>
</body>
</html>