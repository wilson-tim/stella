<%@ page errorPage="/ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/vikingtags.tld" prefix="viking" %>
<html>
<head>
<title>Enter comments</title>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<link href="<html:rewrite page="/css/viking.css"/>" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="<html:rewrite page="/table.js"/>"></script>

<script type="text/javascript" src="<html:rewrite page="/validation.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/utils.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/datepicker.js"/>"></script>

<script>
function doSave() {
	var frm = document.forms[0];
	if (frm.amendmentType.selectedIndex < 1 || frm.comments.value == "") {
		alert("Amendment type and comment must be entered.");
	} else {
		window.returnValue = frm.amendmentType.options[frm.amendmentType.selectedIndex].value+";"+frm.comments.value;
		window.close();
	}
}
</script>

</head>

<body style="text-align: center;">
<form>
<h3>Please enter comments</h3>
<table cellspacing="0" cellpadding="1" border="0" width="100%">
	<tr>
		<th style="text-align: left; padding-top: 4px; border-top: 1px solid black;">Comments</th>
		<th style="text-align: right; border-top: 1px solid black;">
			Amendment Type <viking:vikingSelector	selectionType="amendmentTypeSelector"
																						name="amendmentType" />
		</th>
	</tr>
	<tr>
		<td colspan="2" style="padding: 2px;">
			<textarea	name="comments"	rows="5" style="width: 100%"></textarea>
		</td>
	</tr>
</table><p/>
<button	accesskey="S"
				style="color: #008000;"
				onClick="doSave();"><u>S</u>ave</button>
<button	accesskey="C"
				style="color: #c00000;"
				onClick="window.close();"><u>C</u>ancel</button>
</form>
</body>
</html>
