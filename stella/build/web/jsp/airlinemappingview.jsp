<%@ page errorPage="ErrorPage.jsp" %>
<%@ page import="uk.co.firstchoice.stella.frontend.AirlineMappingBean, java.util.*" session="true" %>
<html>
<head>
<title>Airline Mapping</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="/stella/stella.css" rel="stylesheet" type="text/css">
<style>
.dataTable td {
				border: 1px solid #aaaaaa;
				padding: 2px 4px;
}

</style>
<script src="validation.js"></script>
<script>
var frm;

function addAirline() {
	var win = window.open('/stella/stellabsp?cmd=airlinedetails','am_airlinedetails',
				'width=600,height=450'+
				',scrollbars=yes,menubar=no,location=no,status=no,toolbar=no,resizable=yes',false);
	win.focus();
}

function addUser() {
	var win = window.open('/stella/stellabsp?cmd=am_adduser','am_editdetails',
				'width=450,height=400'+
				',scrollbars=yes,menubar=no,location=no,status=no,toolbar=no,resizable=yes',false);
	win.focus();
}

function editUser(user) {
	var win = window.open('/stella/stellabsp?cmd=am_edituser&param='+user,'am_editdetails',
				'width=450,height=400'+
				',scrollbars=yes,menubar=no,location=no,status=no,toolbar=no,resizable=yes',false);
	win.focus();
}

function editAirline(airline) {
	var win = window.open('/stella/stellabsp?cmd=am_editairline&param='+airline,'am_editdetails',
				'width=450,height=400'+
				',scrollbars=yes,menubar=no,location=no,status=no,toolbar=no,resizable=yes',false);
	win.focus();
}

function doOnLoad() {
	frm=document.forms[0];
	window.moveTo(	parseInt((window.screen.availWidth-document.body.offsetWidth) / 2),
					parseInt((window.screen.availHeight-document.body.offsetHeight) / 2));
}

</script>
</head>

<body onLoad="doOnLoad();">
<form method="post" action="/stella/stellabsp?cmd=startairlinemapping" >
<input type="hidden" name="sortorder" value=""/>
<h3>Airline Mapping</h3>
<p/><hr/><p/>
<% 
	int lineNo = 0; 
	boolean sortByUser = request.getParameter("sortorder")==null || request.getParameter("sortorder").equals("byuser");
	AirlineMappingBean amb = (AirlineMappingBean)session.getAttribute("amb");
	String param = "";
	String data = "";
%>
<table cellspacing="0" class="dataTable" style="width: auto;">
	<tr>
		<th>#</th>
<% 	if (sortByUser) { %>
		<th>User Name</th>
		<th><a href="#" onClick="frm.sortorder.value='byairline'; frm.submit();">Airline</a></th>
		</tr>
<%
		for (Iterator it = amb.getAirlinesByUser().keySet().iterator(); it.hasNext(); ) {
			param = (String)it.next();
System.out.println("param"+ param);

			out.print(	"<tr class=\"line"+(++lineNo%2)+"\"><td>"+lineNo+
						"</td><td nowrap><a href=\"#\" onClick=\"editUser('"+
						java.net.URLEncoder.encode(param, "UTF-8")+"');\">"+param+
						"</a></td><td style=\"text-align: left;\">");


			TreeSet values = (TreeSet)amb.getAirlinesByUser().get(param);
			for (Iterator it2 = values.iterator(); it2.hasNext(); ) {
				data = (String)it2.next();
				out.print("<a href=\"#\" onClick=\"editAirline('"+
						java.net.URLEncoder.encode(data, "UTF-8")+"');\">"+data+"</a>");
				out.print(it2.hasNext()?", ":"");
			}
			out.println("</td></tr>");
		}
	} else {
%>
		<th>Airline</th>
		<th><a href="#" onClick="frm.sortorder.value='byuser'; frm.submit();">User Name</a></th>
		</tr>
<%
		for (Iterator it = amb.getUsersByAirline().keySet().iterator(); it.hasNext(); ) {
			param = (String)it.next();
			out.print(	"<tr class=\"line"+(++lineNo%2)+"\"><td>"+lineNo+
						"</td><td nowrap><a href=\"#\" onClick=\"editAirline('"+
						java.net.URLEncoder.encode(param, "UTF-8")+"');\">"+ param+
						"</a></td><td style=\"text-align: left;\">");

			TreeSet values = (TreeSet)amb.getUsersByAirline().get(param);
			for (Iterator it2 = values.iterator(); it2.hasNext(); ) {
				data = (String)it2.next();
				out.print("<a href=\"#\" onClick=\"editUser('"+
						java.net.URLEncoder.encode(data, "UTF-8")+"');\">"+data+"</a>");
				out.print(it2.hasNext()?", ":"");
			}
			out.println("</td></tr>");
		}
	}
%>
</table>
<p/>
<button name="adduser" accesskey="u" tabindex="-1" onClick="addUser();">Add <u>U</u>ser</button>
<button name="addairine" accesskey="a" tabindex="-1" onClick="addAirline();">Add <u>A</u>irline</button>
<button name="close" accesskey="c" tabindex="-1" onClick="window.close();"><u>C</u>lose</button>
</form>
</body>
</html>
