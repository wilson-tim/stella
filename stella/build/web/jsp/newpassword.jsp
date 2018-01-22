<%@ page errorPage="jsp/ErrorPage.jsp" %>
<html>
<head>
<title>Password Maintenance</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="stella.css" rel="stylesheet" type="text/css">
<script>
function doSubmit() {
	var frm = document.forms[0];
	var pw1 = frm.pw1.value;
	var pw2 = frm.pw2.value;
	var oldpw = frm.oldpw.value;
	if (oldpw == "") {
		alert("Please enter your old password");
		frm.oldpw.focus();
		return false;
	}
	if (pw1.length < 5) {
		alert("Password must be between 5 and 16 characters long");
		frm.pw1.focus();
		return false;
	}
	if (pw1 != pw2) {
		alert("New Password and Confirmation doesn't match");
		frm.pw2.focus();
		return false;
	}
	if (oldpw == pw1) {
		alert("New Password cannot be the same as the old password");
		frm.pw1.value = "";
		frm.pw2.value = "";
		frm.pw1.focus();
		return false;
	}
	frm.submit();
}
</script>
</head>
<body style="text-align: center;" onload="document.forms[0].oldpw.focus();">
<form method="post" action="/stellabsp">
<input type="hidden" name="cmd" value="newpassword">
<p/>
<h2>Password Maintenance</h2>
<hr/><p/>
<table cellpadding="3" cellspacing="0" style="border-bottom: 1px solid black;">
	<tr>
		<th>Username:</th><td><%= request.getRemoteUser() %></td>
	</tr>
	<tr>
		<th>Enter current password:</th><td><input type="password" name="oldpw" size="20" maxlength="16"></td>
	</tr>
	<tr>
		<th>Enter new password:</th><td><input type="password" name="pw1" size="20" maxlength="16"></td>
	</tr>
	<tr>
		<th>Confirm new password:</th><td><input type="password" name="pw2" size="20" maxlength="16"></td>
	</tr>
</table>
<p/>
<button accessKey="U" onclick="doSubmit();"><u>U</u>pdate Password</button>
 <button accessKey="C" onclick="window.close();"><u>C</u>ancel Update</button>
<%-- <button accessKey="C" onclick="window.location='stellabsp';"><u>C</u>ancel Update</button> --%>
<p/>
<h6><%= request.getAttribute("chgpwerr")==null?"":(String)request.getAttribute("chgpwerr") %></h6>
</form>
</body>
</html>
