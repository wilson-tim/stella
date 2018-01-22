<%@ page errorPage="ErrorPage.jsp" %>
<%@ page import="java.util.*" session="true" %>
<jsp:useBean id="datavector" class="java.util.Vector" scope="request"/>
<jsp:useBean id="maintrecord" class="uk.co.firstchoice.genericmaint.frontend.MaintRecord" scope="session"/>
<%
	Vector md = maintrecord.getMetaData();
	uk.co.firstchoice.genericmaint.frontend.DbColumnMetadata dcm = null;
%>
<html>
<head>
<title><%= maintrecord.getAppTitle() %></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="genericmaint.css" rel="stylesheet" type="text/css">
<style>
th {text-align: center; vertical-align: bottom;}
td {text-align: center;}
</style>
<script src="validation.js"></script>
<script>
var numCols = <%= md.size() %>;
var blankOK = [<%
	for (int i=0; i<(md.size()-1); i++) {
		dcm = (uk.co.firstchoice.genericmaint.frontend.DbColumnMetadata)md.get(i);
		out.print(dcm.isBlankAllowed()+",");
	}
	dcm = (uk.co.firstchoice.genericmaint.frontend.DbColumnMetadata)md.get(md.size()-1);
	out.print(dcm.isBlankAllowed());
%>];

var datatype = [<%
	for (int i=0; i<(md.size()-1); i++) {
		dcm = (uk.co.firstchoice.genericmaint.frontend.DbColumnMetadata)md.get(i);
		out.print((dcm.isDate()?"'dat'":dcm.isFloat()?"'flo'":dcm.isInteger()?"'int'":"''")+",");
	}
	dcm = (uk.co.firstchoice.genericmaint.frontend.DbColumnMetadata)md.get(md.size()-1);
	out.print(dcm.isDate()?"'dat'":dcm.isFloat()?"'flo'":dcm.isInteger()?"'int'":"''");
%>];

function validateForm(frm) {
	var field;
	for (var i=0; i < numCols; i++) {
		field = eval("frm.column"+i);
		if (field.value=="" && !blankOK[i]) {
			alert("Column "+(i+1)+" cannot be blank.");
			field.focus();
			return false;
		}
		if (datatype[i]=='int' && !validNumberField(field, blankOK[i])) {
			return false;
		}
		if (field.value!="")
			if (datatype[i]=='flo' && isNaN(parseFloat(field.value))) {
				alert("Column "+(i+1)+" must contain a valid number.");
				field.focus();
				return false;
			}
		if (datatype[i]=='dat' && !validDateField(field, blankOK[i])) { //returns false on empty field, without an error, even though blankOK is true - should return true, validation OK
			return false;
		}
	}
	for (var i=0; i < numCols; i++) {
		field = eval("frm.column"+i);
		field.disabled = false;
	}
	return true;
}

function doAdd() {
	var frm=document.forms[0];
	if (validateForm(frm)) {
		frm.cmd.value='addrecord';
		frm.submit();
	}
}

function doSave(formNo) {
	var frm=document.forms[formNo];
	if (validateForm(frm)) {
		frm.cmd.value='saverecord';
		frm.submit();
	}
}

function doDelete(formNo) {
	var frm=document.forms[formNo];
	if (confirm('Are you sure you wish to delete this record?')) {
		frm.cmd.value='deleterecord';
		for (var i=0; i < numCols; i++) {
			field = eval("frm.column"+i);
			field.disabled = false;
		}
		frm.submit();
	}
}

function doOnLoad() {
//	window.moveTo(	parseInt((window.screen.availWidth-document.body.offsetWidth) / 2),
//					parseInt((window.screen.availHeight-document.body.offsetHeight) / 2));
}

</script>
</head>

<body onLoad="doOnLoad();" id="theBody">
<h3><%= maintrecord.getAppTitle() %></h3>
<h6><%= maintrecord.getError() %></h6>
<table border="1" cellspacing="0">
  <tr>
  <th>#</th>
<%
	for (Enumeration en = maintrecord.getMetaData().elements(); en.hasMoreElements(); ) {
		out.print("<th>"+((uk.co.firstchoice.genericmaint.frontend.DbColumnMetadata)en.nextElement()).getColumnDisplayLabel()+"</th>");
	}
%>
  <th style="vertical-align: middle;"><input type="button" value="Exit" onClick="window.close();"></th>
  </tr>
  <tr>
  <form method="post" action="genericmaint" >
  <input type="hidden" name="cmd">
  <td>&nbsp;</td>
<%
	for (int i=0; i<maintrecord.getMetaData().size(); i++) {
		dcm = (uk.co.firstchoice.genericmaint.frontend.DbColumnMetadata)maintrecord.getMetaData().get(i);
		out.print("<td><input type=\"text\" name=\"column"+i+"\" ");
		out.print("size=\""+dcm.getColumnDisplayWidth()+"\" maxlength=\""+dcm.getColumnWidth()+"\" ");
		out.print("></td>");
	}
%>
  <td><a href="javascript:doAdd()">Add</a></td>
  </form>
  </tr>
<%
	for (int lineno=0; lineno<datavector.size(); lineno++) {
		Vector row=(Vector)datavector.get(lineno);
		out.println("<tr><th>"+(lineno+1)+"</th><form method=\"post\" action=\"genericmaint\">");
		out.println("<input type=\"hidden\" name=\"cmd\">");
		for (int i=0; i<row.size(); i++) {
			dcm = (uk.co.firstchoice.genericmaint.frontend.DbColumnMetadata)maintrecord.getMetaData().get(i);
			out.print("<td><input type=\"text\" name=\"column"+i+"\" ");
			out.print("value=\""+(String)row.get(i)+"\" ");
			out.print("size=\""+dcm.getColumnDisplayWidth()+"\" maxlength=\""+dcm.getColumnWidth()+"\" ");
			out.print(dcm.isPrimaryKey()?"disabled":"");
			out.print("></td>");
		}
		out.print("<td><a href=\"javascript:doSave("+(lineno+1)+")\">Save</a> | ");
		out.println("<a href=\"javascript:doDelete("+(lineno+1)+")\">Delete</a></td>");
		out.println("</form></tr>");
	}
%>
</table>
<p/>
<span class="versioninfo">
<table><tr>
	<th>Conf. DW:</th><td><%= maintrecord.getConfigDwName() %></td>
	<th>Appl. DW:</th><td><%= maintrecord.getApplDwName() %></td>
	<th>Server:</th><td><%= request.getServerName() %></td>
	<th>User:</th><td><%= request.getRemoteUser() %></td>
</tr></table>
</span>
</body>
</html>
