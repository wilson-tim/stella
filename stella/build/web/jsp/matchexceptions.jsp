<%@ page errorPage="jsp/ErrorPage.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" session="true"%>
<%@ taglib uri="utiltags" prefix="util"%>
<%@ taglib uri="stellatags" prefix="stella"%>
<jsp:useBean id="matchexceptions"
	class="uk.co.firstchoice.stella.frontend.BeanCollectionBean"
	scope="session" />

<head>
<title>Match Exceptions</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="/stella/stella.css" rel="stylesheet" type="text/css">
<style>
td.ct {text-align: center; }
input {text-align: center; }
</style>
<script src="/stella/validation.js"></script>
<script>

var sortString = '';

function sortTable(sFieldName) {
  if (frm.anyChanged.value=="0" || confirm("If you re-sort you will loose any unsaved changes! Continue?")) {
  	var oDSO = document.forms[0].dsoMatch;
  	var idx = sortString.indexOf(sFieldName);
  	if (idx == -1) {	<%--	new sort column	--%>
  		sortString += "+"+sFieldName+";";
  	} else {					<%--	column already exist --%>
  		var direction = sortString.charAt(idx-1);		<%-- '+' or '-' --%>
  		if (direction == '+') {		<%--	change to '-' --%>
  			sortString = sortString.replace('+'+sFieldName, '-'+sFieldName);
  		} else {									<%--	remove column --%>
  			sortString = sortString.replace('-'+sFieldName+';', '');
  		}
  	}
  	if (sortString == "") {
  		window.document.all["sortId"].innerText = "-NONE-";
  	} else {
  		window.document.all["sortId"].innerText = sortString.substring(0,sortString.length-1);
  	}
  	oDSO.Sort = sortString;
  	oDSO.reset();
    frm.anyChanged.value = "0";
  }
}


function resetSort() {
  if (frm.anyChanged.value == "0" || confirm("If you re-sort you will loose any unsaved changes! Continue?")) {
  	var oDSO = document.forms[0].dsoMatch;
  	window.document.all["sortId"].innerText = "-NONE-";
  	sortString = "";
  	oDSO.Sort = sortString;
  	oDSO.reset();
    frm.anyChanged.value = "0";
  }
}


var frm;


function doSubmit() {
	if (frm.anyChanged.value == "1") {
		if (confirm('Are you sure you wish to update the changed exceptions with new reason codes')) {
			frm.submit();
		}
	} else
		alert('No reason codes have been changed - nothing to update');
}

function doCancel() {
	if (frm.anyChanged.value == "1")
		if (!confirm("You have changed one or more reason codes - sure you wish to cancel your updates?"))
			return;
	window.close();
}

function searchPNR(pnr) {
  var a = pnr.split(";");
	var width = window.screen.availWidth - 5;
	var height = window.screen.availHeight - 5;
	var URL = '/stella/jsp/postsearch.jsp?'+((a[1]=='DWH'||a[1]=='ITR'||a[1]=='TRL')?'pnr':a[1]=='BSPT'?'ticketNumber':'refundNumber')+'='+a[0] ; 
	var win = window.open(URL,'startsearch',
				'width='+width+',height='+height+
				',scrollbars=yes,menubar=no,location=no,status=no,toolbar=no,resizable=yes',false);
	win.focus();
}

function doReasonCode(field) {
//	var field = eval("frm.newReasonCode"+lineno)
//	field.blur();
var bsp=false;
	var newCode = window.showModalDialog('/stella/jsp/pickreasoncode.jsp'+(bsp?'?bsp=1':''), '',
									'dialogHeight:650px;dialogWidth:750px;help:no;scroll:yes;status:no');
	if (newCode != null) {
		field.value = newCode.substring(0, 2);
		frm.anyChanged.value = "1";
		if (newCode.substr(newCode.length-1)=="Y") {
			if (confirm("Do you wish to create a refund letter for this entry?")) {
				var win = window.open('/stella/stellabsp?cmd=startrefundletter&lineno='+lineno,'refundletter',
						'width=750,height=650,scrollbars=yes,menubar=no,location=no,status=no,toolbar=no,resizable=yes',false);
				win.focus();
			}
		}

	}
}

function updateBranchSelect() {

	var branchCode = frm.specialistBranch.value.toUpperCase();
	if (branchCode != "") {
		for (i=1; i<frm.branchSelect.options.length; i++) {

//       alert('frm.branchSelect.options[i].value '+ frm.branchSelect.options[i].value);

			if (frm.branchSelect.options[i].value == branchCode) {
              frm.branchSelect.selectedIndex = i;
				return true;
			}
		}
	}
	frm.branchSelect.selectedIndex = 0;
	return false;
}

function doBranchSelectChange() {
frm.specialistBranch.value=frm.branchSelect.options[frm.branchSelect.selectedIndex].value.substr(0,4);
//	sectorPaymentInd = frm.airlineSelect.options[frm.airlineSelect.selectedIndex].value.substr(3,1);
}


function showHistory(pnr_id) {
	var newTicket = window.showModalDialog('/stella/stellabsp?cmd=viewmatchhistory&pnrid='+pnr_id, '',
									'dialogHeight:390px;dialogWidth:750px;help:no;scroll:yes;status:no');
}

function showExceptionDescription() {
	var winWidth = 650;
	var winHeight = 390;
	var newTicket = window.showModalDialog('/stella/stellabsp?cmd=viewexceptiondescription',
											'', 'dialogHeight:'+winHeight+'px;dialogWidth:'+
											winWidth+'px;help:no;scroll:yes;status:no');
}

function doOnLoad() {  
	frm=document.forms[0];
//	window.moveTo(	parseInt((window.screen.availWidth-document.body.offsetWidth) / 2),
//					parseInt((window.screen.availHeight-document.body.offsetHeight) / 2));
}

function doChangeType() {


	<!-- if (frm.anyChanged.value == "0" || confirm("Any changes will be committed when you change view, proceed?")) { -->
		var x = "";
        var sbranch = ""; 
		for (var i=0; i<frm.exceptionType.length; i++) {
			if (frm.exceptionType[i].checked) {
				x = frm.exceptionType[i].value; }
			}  // for loop
        sbranch = frm.specialistBranch.value ;


    if (x == "" || x == null){
       alert('Please choose either BSP or Selling Systems ...'); 
       return ; }
    if (sbranch == "" ||  sbranch == null) {
       alert('Please choose Product from the drop down list');
       return ; }


     
  	if (( x != "<%= matchexceptions.getIndicator() %>" ) || ( sbranch != "<%= matchexceptions.getSpecialistBranch()%>" )){
        window.document.all["theBody"].style.cursor = "wait";
		frm.submit();
	}
}

function showBSP(bspId) {
	var winWidth = 750;
	var winHeight = 390;
	var newTicket = window.showModalDialog('/stellabsp?cmd=viewbspdetails&bspid='+bspId,
											'', 'dialogHeight:'+winHeight+'px;dialogWidth:'+
											winWidth+'px;help:no;scroll:yes;status:no');
}


function func_1(thisObj, thisEvent) {
//use 'thisObj' to refer directly to this component instead of keyword 'this'
//use 'thisEvent' to refer to the event generated instead of keyword 'event'

}</script>
</head>

<body onLoad="doOnLoad();" id="theBody">
<form method="post" action="/stella/stellabsp?cmd=postexceptions"
	onclick="return func_1(this, event);" name="dataForm"><input
	type="hidden" name="anyChanged" value="0" />


<h3>Match Exceptions</h3>
<p />
<hr />
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td style="text-align: center;">
		<button accesskey="u" onClick="doSubmit();" tabindex="15"><u>U</u>pdate</button>
		<button accesskey="c" onClick="doCancel();" tabindex="20"><u>C</u>ancel</button>
		<br />
		Show exceptions: <!-- 	  <input type="radio" name="exceptionType" onClick="doChangeType();" value="A" <%= matchexceptions.getIndicator().equals("A")?"checked":"" %>>All
	  <input type="radio" name="exceptionType" onClick="doChangeType();" value="B" <%= matchexceptions.getIndicator().equals("B")?"checked":"" %>>BSP
	  <input type="radio" name="exceptionType" onClick="doChangeType();" value="D" <%= matchexceptions.getIndicator().equals("D")?"checked":"" %>>Data Warehouse -->

		<input type="radio" name="exceptionType" value="B"
			<%= matchexceptions.getIndicator().equals("B")?"checked":"" %>>BSP <input
			type="radio" name="exceptionType" value="S"
			<%= matchexceptions.getIndicator().equals("S")?"checked":"" %>>Selling Systems 
      
         <input name="specialistBranch" id="specialistBranch" tabindex="15"
			onChange="this.value = trim(this.value.toUpperCase());" size="4"
			maxlength="4" value="<%= matchexceptions.getSpecialistBranch() %>"
			onBlur="if (validText(this, 0, 4)) updateBranchSelect();"> <stella:stellaSelector
			selectionType="branchSelect" /> <br />
		
        <button name="search" accesskey="s" onClick="doChangeType();"
			tabindex="10"><u>S</u>earch</button>
		</td>
	</tr>

</table>
<hr />
<p />
<h6><jsp:getProperty name="matchexceptions" property="error" /></h6>
<%
if (matchexceptions.getBeanCollection() != null
	&& matchexceptions.getBeanCollection().size() > 0) {
%>
<object id="dsoMatch"
	classid="clsid:333C7BC4-460F-11D0-BC04-0080C7055A83">
	<param name="DataURL" value="jsp/matchexceptionCSV.jsp" />
	<param name="UseHeader" value="true" />
	<param name="FieldDelim" value="^" />
	<param name="RowDelim" value="~" />
</object>
<table class="datatable" id="matchexceptions" datasrc="#dsoMatch"
	cellspacing="0" cellpadding="4" style="width: 100%;">
	<thead>
		<tr>
			<th colspan="15" style="text-align: left; border-right: 0px;">Sort
			Order: <span id="sortId" style="color: black;">-NONE-</span> (<a
				href="javascript:resetSort();">reset</a>)</th>
			<th colspan="2" style="text-align: center; border-left: 0px;"># Total
			pending: <%= matchexceptions.getBeanCollection().size()%></th>
		</tr>
		<tr>
			<th class="sortHeader" onClick="sortTable('matchProcessDate',this);"
				currentsort=""><span class="sortStatus"></span>Match<br />
			Process<br />
			Date</th>
			<th class="sortHeader" onClick="sortTable('atopReference',this);"
				currentsort=""><span class="sortStatus"></span>ATOP<br />
			Ref.</th>
			<th class="sortHeader" onClick="sortTable('atopSeason',this);"
				currentsort=""><span class="sortStatus"></span>ATOP<br />
			Seas.</th>
			<th class="sortHeader" onClick="sortTable('pnr',this);"
				currentsort=""><span class="sortStatus"></span>PNR /<br />
			Ticket</th>
			<th class="sortHeader" onClick="sortTable('departureDate',this);"
				currentsort=""><span class="sortStatus"></span>Dep.<br />
			Date</th>
			<th class="sortHeader" onClick="sortTable('failType',this);"
				currentsort=""><span class="sortStatus"></span>T<br />
			y<br />
			p</th>
			<th class="sortHeader" onClick="sortTable('source',this);"
				currentsort=""><span class="sortStatus"></span>Src</th>
			<th class="sortHeader" onClick="sortTable('dwSeatCost',this);"
				currentsort=""><span class="sortStatus"></span>Seat<br />
			Cost</th>
			<th class="sortHeader" onClick="sortTable('dwTaxCost',this);"
				currentsort=""><span class="sortStatus"></span>Tax<br />
			Cost</th>
			<th class="sortHeader" onClick="sortTable('dwTotalCost',this);"
				currentsort=""><span class="sortStatus"></span>Total<br />
			Cost</th>
			<th class="sortHeader" onClick="sortTable('stellaSeatCost',this);"
				currentsort=""><span class="sortStatus"></span>Stella<br />
			Seat<br />
			Cost</th>
			<th class="sortHeader" onClick="sortTable('stellaTaxCost',this);"
				currentsort=""><span class="sortStatus"></span>Stella<br />
			Tax<br />
			Cost</th>
			<th class="sortHeader" onClick="sortTable('stellaTotalCost',this);"
				currentsort=""><span class="sortStatus"></span>Stella<br />
			Total<br />
			Cost</th>
			<th class="sortHeader" onClick="sortTable('unmatchedAmount',this);"
				currentsort=""><span class="sortStatus"></span>UN-<br />
			matched<br />
			Amount</th>
			<th class="sortHeader" onClick="sortTable('oldReasonCode',this);"
				currentsort=""><span class="sortStatus"></span>Prev<br />
			Reas<br />
			Code</th>
			<th>BSP<br />
			Disp</th>
			<th>Reason<br />
			Code<br />
			<img border="0" src="images/qm.gif"
				onclick="showExceptionDescription();"></th>
		</tr>
	</thead>
	<tbody>
		<tr class="line0">
			<td class="ct"><span datafld="matchProcessDate"></span></td>
			<td><span datafld="atopReference"></span></td>
			<td class="ct"><span datafld="atopSeasonDisp"></span></td>
			<td class="ct"><a href="#" datafld="pnrException"
				onClick="searchPNR(this.href); return false;"> <span datafld="pnr"></span>
			</a></td>

			<td class="ct"><span datafld="departureDate"></span></td>
			<td class="ct"><span datafld="failType"></span></td>
			<td class="ct"><span datafld="source"></span></td>
			<td><span datafld="dwSeatCostDisp"></span></td>
			<td><span datafld="dwTaxCostDisp"></span></td>
			<td><span datafld="dwTotalCostDisp"></span></td>
			<td><span datafld="stellaSeatCostDisp"></span></td>
			<td><span datafld="stellaTaxCostDisp"></span></td>
			<td><span datafld="stellaTotalCostDisp"></span></td>
			<td><span datafld="unmatchedAmountDisp"></span></td>
			<td class="ct"><a datafld="pnr_id"
				onClick="showHistory(this.href); return false;"> <span
				datafld="oldReasonCode"></span> </a></td>
			<td class="ct"><a datafld="pnr_id"
				onClick="showBSP(this.href); return false;"> <span datafld="bspLink"></span>
			</a></td>
			<td class="ct"><input name="newReasonCode" value="--" size="3"
				onFocus="doReasonCode(this);"> <input name="pnr" type="hidden"
				datafld="pnr" /> <input name="pnrId" type="hidden" datafld="pnr_id" />
			<input name="oldReasonCode" type="hidden" datafld="oldReasonCode" />
			</td>
		</tr>
	</tbody>
	<tfoot>
		<tr>
			<th colspan="99"></th>
		</tr>
	</tfoot>
</table>
<%
		}
		%> <%--
<table cellspacing="0" class="dataTable">
	<tr>
		<th>Match<br/>Process<br/>Date</th>
		<th>ATOP<br/>Ref.</th>
		<th>ATOP<br/>Seas.</th>
		<th>PNR<br/>/<br/>Ticket</th>
		<th>Dep.<br/>Date</th>
		<th>T<br/>y<br/>p</th>
		<th>Src</th>
		<th>Seat<br/>Cost</th>
		<th>Tax<br/>Cost</th>
		<th>Total<br/>Cost</th>
		<th>Stella<br/>Seat<br/>Cost</th>
		<th>Stella<br/>Tax<br/>Cost</th>
		<th>Stella<br/>Total<br/>Cost</th>
		<th>UN-<br/>matched<br/>Amount</th>
		<th>Prev<br/>Reas<br/>Code</th>
		<th>BSP<br/>Disp</th>
		<th>Reason<br/>Code<br/><img border="0" src="images/qm.gif" onclick="showExceptionDescription();"></th>
	</tr>

<% if (matchexceptions.getBeanCollection().size() > 0 ) { %>		
<util:iterate id="exce" type="uk.co.firstchoice.stella.frontend.MatchExceptionsBean" collection="<%= matchexceptions.getBeanCollection() %>" >
<tr class="<%= "line"+(lineNo%2) %>">
<td class="ct"><jsp:getProperty name="exce" property="matchProcessDate" /></td>
<td class="ct"><jsp:getProperty name="exce" property="atopReference"/></td>
<td class="ct"><jsp:getProperty name="exce" property="atopSeason"/></td>
<td class="ct"><a href="JavaScript:searchPNR('<jsp:getProperty name="exce" property="pnr"/>','<jsp:getProperty name="exce" property="exceptionType"/>');">
<jsp:getProperty name="exce" property="pnr"/>
</a></td>
<td class="ct"><jsp:getProperty name="exce" property="departureDate"/></td>
<td class="ct"><jsp:getProperty name="exce" property="failType"/></td>
<td class="ct"><jsp:getProperty name="exce" property="source"/></td>
<td><jsp:getProperty name="exce" property="dwSeatCost"/></td>
<td><jsp:getProperty name="exce" property="dwTaxCost"/></td>
<td><jsp:getProperty name="exce" property="dwTotalCost"/></td>
<td><jsp:getProperty name="exce" property="stellaSeatCost"/></td>
<td><jsp:getProperty name="exce" property="stellaTaxCost"/></td>
<td><jsp:getProperty name="exce" property="stellaTotalCost"/></td>
<td><jsp:getProperty name="exce" property="unmatchedAmount"/></td>
<td class="ct"><a href="JavaScript:showHistory(<jsp:getProperty name="exce" property="pnr_id"/>);">
<jsp:getProperty name="exce" property="oldReasonCode"/></a></td>
<td class="ct"><%= exce.getExceptionType().equals("DWH")?"":"<a href=\"JavaScript:showBSP("+exce.getPnr_id()+");\">*</a>" %>
<td class="ct"><input name="newReasonCode<%= lineNo %>" value="--" size="3" onFocus="doReasonCode(<%= lineNo++ %>,<%= exce.getPnr().length()>6 %>)"></td>
</tr>
</util:iterate>
<% } %>

</table>
--%> <%= "".equals(matchexceptions.getIndicator()) ? "<p/><h3>Please select exception type</h3>"
						: matchexceptions.getBeanCollection().size() == 0 ? "<p/><h6>No exceptions found</h6>"
								: ""%>
</form>
</body>
</html>
