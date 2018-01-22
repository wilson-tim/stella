<%@ page errorPage="/ErrorPage.jsp" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import = "uk.co.firstchoice.viking.gui.util.Const" %>
<jsp:useBean	id="flighttopbean"
							class="uk.co.firstchoice.viking.gui.FlightTopBean"
							scope="request"/>
<html>
<head>
<%uk.co.firstchoice.viking.gui.FlightTopBean ftb = (uk.co.firstchoice.viking.gui.FlightTopBean)request.getAttribute("flighttopbean");%>
<title>Flight Operations Input</title>
<link href="<html:rewrite page="/css/viking.css"/>" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="<html:rewrite page="/table.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/validation.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/utils.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/datepicker.js"/>"></script>

<style>
table { padding-bottom: 10px;  }
td { text-align: center; vertical-align: top;  }
.monospace { font-family: courier; font-size: 8pt; font-weight: normal; }
</style>
<script>
var frm;
var groupActive;
var selectedDate;
var selectedGroup;
var dateOptionsArray;
var groupOptionsArray;
var dow = ['Su','Mo','Tu','We','Th','Fr','Sa'];

var cutoffDate = '<%= new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()) %>';
var isManager = <%= request.isUserInRole(Const.VIKING_MANAGER) %>;
var liveVersion = <%= ftb.isLiveVersion() %>;

var allDates = [<jsp:getProperty name="flighttopbean" property="departureDates" />];
// ['02/10/2003','09/10/2003','16/10/2003','22/10/2003','30/10/2003']; 

var sectorsArr = [<jsp:getProperty name="flighttopbean" property="dateGroupInfo" />];
/*
[
['02/10/2003','16/10/2003','FF00','01','MAN','LPA','09:00','14:30','0','FCA332C','B757','C'],
['30/10/2003','30/10/2003','FF00','02','LPA','MAN','15:00','20:00','0','FCA332D','B757','']
];
*/

function reloadSectorsByDateTable() {
	var tId = "sectorsByDateTable";
	deleteAllTableRows(tId);
	var sDate = frm.dateSelect.options[frm.dateSelect.selectedIndex].value;
	<%
	if (request.isUserInRole("VIKING_EDITOR")) { 
	%>
		  var canEdit = false;
		  if (groupActive) {
		    var saText = frm.dateSelect.options[frm.dateSelect.selectedIndex].text.split(" ");
		    canEdit = !(dateBefore(saText[0], cutoffDate) && dateBefore(saText[2], cutoffDate));
		  } else {
		    canEdit = !dateBefore(sDate, cutoffDate);
		  }
			if (canEdit) {
				window.document.all.modifyId.style.display = "block";
			} else {
				window.document.all.modifyId.style.display = "none";
			}
	<%
	    }
	%>
	if (groupActive) {
		selectedGroup = frm.dateSelect.selectedIndex;
	} else {
		selectedDate = frm.dateSelect.selectedIndex;
	}
	var found = false;
	for (var i=0; i < sectorsArr.length; i++) {
		if (dateBetween(strToDate(sDate), strToDate(sectorsArr[i][0]), strToDate(sectorsArr[i][1]))) {
			found = true;
			var tr = addTableRow(	tId,
														sectorsArr[i][3],
														sectorsArr[i][4],
														sectorsArr[i][5],
														sectorsArr[i][6],
														sectorsArr[i][7],
														sectorsArr[i][8],
														sectorsArr[i][9],
														sectorsArr[i][10],
														sectorsArr[i][11] // Flight type added by  JR

													     );  

			if (sectorsArr[i][12] == 'C') { // Cancelled record - mark data as Red
				tr.style.color = "red";
			}
		} else {
			if (found) break;		// we have added some, and are now past so exit
		}
	}
	var trs = document.getElementById(tId).getElementsByTagName('tbody')[0].getElementsByTagName('tr');
	for (var j=0,len2=trs.length; j < len2; j++) {
		trs[j].className=(j==0?'':('line'+(j%2)));
	}
}


function loadDateList() {
	var sel = frm.dateSelect;
	sel.options.length = 0;
	for (var i=0; i < dateOptionsArray.length; i++) {
		sel.options[i] = dateOptionsArray[i];
	}
	if (sel.options.length >= selectedDate) {
		sel.selectedIndex = selectedDate;
	}
	reloadSectorsByDateTable();
}


function loadDateGroups() {
	var sel = frm.dateSelect;
	sel.options.length = 0;
	for (var i=0; i < groupOptionsArray.length; i++) {
		sel.options[i] = groupOptionsArray[i];
	}
	if (sel.options.length >= selectedGroup) {
		sel.selectedIndex = selectedGroup;
	}
	reloadSectorsByDateTable();
}


function changeActiveTab() {
	var tab0 = document.getElementById("tab0");
	var tab1 = document.getElementById("tab1");
	groupActive = !groupActive;
	tab0.className = groupActive?"activeTab":"inactiveTab";
	tab1.className = !groupActive?"activeTab":"inactiveTab";
	if (groupActive) {
		loadDateGroups();
<%
		if (request.isUserInRole("VIKING_MANAGER") ||
				request.isUserInRole("VIKING_EDITOR")) { 
%>
		window.document.all.deleteLink.style.display = 'none';
<% } %>
	} else {
		loadDateList();
<%
		if (request.isUserInRole("VIKING_MANAGER") ||
				request.isUserInRole("VIKING_EDITOR")) { 
%>
		window.document.all.deleteLink.style.display = 'inline';
<% } %>
	}
}


function getComment() {
	var width = window.screen.availWidth*80/100;
	var height = window.screen.availHeight*40/100;
	var leftPoint = parseInt(window.screen.availWidth / 2) - parseInt(width / 2);
	var topPoint = parseInt(window.screen.availHeight / 2) - parseInt(height / 2);
	var comm = window.showModalDialog('<html:rewrite page="/getCommentsPopup.jsp"/>', '', 'dialogHeight:'+height+'px;dialogWidth:'+
									width+'px;help:no;scroll:yes;status:no');
	return comm;
}


function doClose() {
  top.close();
}


function doOp(action) {
	if (action != "add" && frm.dateSelect.selectedIndex == -1) {
		alert("Please select line to "+action);
		return false;
	}
	if (action == 'delete') {
	
		var comm = getComment();
		var commentOK = false;
		if (comm != null) {
			var i = comm.indexOf(';');
			if (i > -1 && i < 5) {
				var ct = comm.substr(0, i);
				var co = comm.substr(i+1);
				editOpDetails(action,ct,co);
				commentOK = true;
			}
		}
		if (!commentOK) {
      if (liveVersion) {
        alert("Date NOT deleted due to missing comment");
      } else {
    		editOpDetails(action);
      }
    }
	} else {
		editOpDetails(action);
	}
}


function editOpDetails(action,amendmentType,comment) {
	var width = 900;
	var height = 550;
	var maxHeight = window.screen.availHeight;
	var maxWidth = window.screen.availWidth;
	var leftPoint = parseInt(maxWidth / 2) - parseInt(width / 2);
	var topPoint = parseInt(maxHeight / 2) - parseInt(height / 2);
	var sStr = 'action='+action+"&liveVersion="+liveVersion+
              '&seriesNo=<jsp:getProperty name="flighttopbean" property="seriesNo" />'+
              '&season=<jsp:getProperty name="flighttopbean" property="season" />';
	if (action != 'deleteSeries') {
		sStr += ('&fromDate='+frm.dateSelect.options[frm.dateSelect.selectedIndex].value);
		if (groupActive) {
			sStr += '&toDate='+frm.dateSelect.options[frm.dateSelect.selectedIndex].text.substring(13, 23)+'&group=true';
		}
	}
	if ((action == 'delete' || action == 'deleteSeries') && amendmentType != null && comment != null) {
		sStr += "&amendmentType="+amendmentType+"&comments="+escape(comment);
		height = 10; width = 10;
	}
	var win = window.open("flightopdetails.do?"+sStr,'addopdetails',
				'width='+width+',height='+height+
				',scrollbars=yes,menubar=no,location=no,status=no,toolbar=no,resizable=yes',false);
	if (action == 'delete')
		window.focus();
	else
		win.focus();
}


function editFlight() {
	var width = 700;
	var height = 350;
	var maxHeight = window.screen.availHeight;
	var maxWidth = window.screen.availWidth;
	var leftPoint = parseInt(maxWidth / 2) - parseInt(width / 2);
	var topPoint = parseInt(maxHeight / 2) - parseInt(height / 2);
	var win = window.open("flightdetails.do",'seriesdetails',
				'width='+width+',height='+height+
				',scrollbars=yes,menubar=no,location=no,status=no,toolbar=no,resizable=yes',false);
	win.focus();
	win.moveTo(leftPoint, topPoint);
}


function viewCF(FFA, purchase) {
	var maxHeight = window.screen.availHeight;
	var maxWidth = window.screen.availWidth;
	var width;
	var height;
	var url;
	if (FFA) {
		width = parseInt(maxWidth * 0.95);
		height = parseInt(maxHeight * 0.95);
		url = 'showFFA.do?type='+(purchase?'P':'S')+'&';
  	var leftPoint = parseInt(maxWidth / 2) - parseInt(width / 2);
  	var topPoint = parseInt(maxHeight / 2) - parseInt(height / 2);
  	window.showModalDialog(url+'seriesNo=<jsp:getProperty name="flighttopbean" property="seriesNo" />', '', 'dialogHeight:'+height+'px;dialogWidth:'+
  								width+'px;help:no;scroll:yes;status:no');
	} else { // coments 
		width = parseInt(maxWidth * 0.9);
		height = parseInt(maxHeight * 0.8);
  	var leftPoint = parseInt(maxWidth / 2) - parseInt(width / 2);
  	var topPoint = parseInt(maxHeight / 2) - parseInt(height / 2);
  	var win = window.open('viewcomments.do?'+'seriesNo=<jsp:getProperty name="flighttopbean" property="seriesNo" />',
                          'viewcomments','width='+width+',height='+height+
                  				',scrollbars=yes,menubar=no,location=no,status=no,toolbar=no,resizable=yes',false);
  	win.focus();
    win.moveTo(leftPoint, topPoint);
	}
}


function deleteSeries() {
	if (confirm('Are you sure you wish to delete this ENTIRE SERIES?')) {
		var comm = getComment();
		var commentOK = false;
		if (comm != null) {
			var i = comm.indexOf(';');
			if (i > -1 && i < 5) {
				var ct = comm.substr(0, i);
				var co = comm.substr(i+1);
				editOpDetails('deleteSeries',ct,co);
				commentOK = true;
			}
		}
		if (!commentOK) {
      if (liveVersion) {
        alert("Series NOT deleted due to missing comment");
      } else {
    		editOpDetails('deleteSeries');
      }
    }
		
	}
}


function doOnLoad() {
	frm=document.forms[0];
	groupActive = true;
	selectedDate = 0;
	selectedGroup = 0;
<% if (request.getParameter("seriesNo")==null) { %>
	editFlight();
<% } else  { %>
	dateOptionsArray = new Array();
	for (var i=0; i < allDates.length; i++) {
		var op = new Option(allDates[i]+" "+dow[strToDate(allDates[i]).getDay()], allDates[i]);
		for (var x = 0; x < sectorsArr.length; x++) {
			if (dateBetween(strToDate(allDates[i]), strToDate(sectorsArr[x][0]), strToDate(sectorsArr[x][1]))) {
//				if (sectorsArr[x][11] == 'C') {
				if (sectorsArr[x][12] == 'C') {
					op.style.cssText = 'background-color:'+sectorsArr[x][2]+';color:red;font-style:italic;';
				} else {
					op.style.cssText = 'background-color:'+sectorsArr[x][2]+';';
				}
				break;
			}
		}
		dateOptionsArray[dateOptionsArray.length] = op;
	}
	
	groupOptionsArray = new Array();
	var prev = '';
	var curr = '';
	for (var i=0; i < sectorsArr.length; i++) {
		curr = sectorsArr[i][0]+' - '+sectorsArr[i][1];
		if (curr != prev) {
			prev = curr;
			curr += (' '+dow[strToDate(sectorsArr[i][0]).getDay()]);
			var op = new Option(curr, sectorsArr[i][0]);
			if (sectorsArr[i][12] == 'C') {
				op.style.cssText = 'background-color:'+sectorsArr[i][2]+';color:red;font-style:italic;';
			} else {
				op.style.cssText = 'background-color:'+sectorsArr[i][2]+';';
			}
			groupOptionsArray[groupOptionsArray.length] = op;
		}
	}
	loadDateGroups();

<%}%>

}

</script>
</head>
<body onload="doOnLoad();">
<form method="post" action="editseries.do" target="_top">
<input type="hidden" name="seriesNo" value="<jsp:getProperty name="flighttopbean" property="seriesNo" />" />
<input type="hidden" name="version" value="<jsp:getProperty name="flighttopbean" property="version" />" />
<h3>Flight Operations</h3>
<table cellspacing="0" cellpadding="0" border="0" width="100%">
	<tr>
		<td style="text-align: left;">
			<table class="displaydetails" cellspacing="3">
				<tr>
					<th>Series No.</th>
					<th>Season</th>
					<th>Version</th>
					<th>Weekday</th>
					<th>Start Date</th>
					<th>End Date</th>
					<th>Frequency</th>
			<%
			if (request.isUserInRole("VIKING_MANAGER") ||
							request.isUserInRole("VIKING_EDITOR")) { 
			%>
				<% if (request.getParameter("seriesNo")==null) { %>
								<th rowspan="2" style="vertical-align: bottom;"><a href="#" onclick="editFlight();">Add</a></th>
				<% } else { %>
								<th rowspan="2" style="vertical-align: bottom;"><a href="#" onclick="viewCF(false);">View Comments</a> | </th>
								<th rowspan="2" style="vertical-align: bottom;"><a href="#" onclick="viewCF(true);">Sharer FFA</a> | </th>
								<th rowspan="2" style="vertical-align: bottom;"><a href="#" onclick="viewCF(true,true);">Purchase FFA</a> | </th>
								<th rowspan="2" style="vertical-align: bottom;"><a href="#" onclick="deleteSeries();">Delete Series</a></th>
				<% } %>
			<% } else if (request.getParameter("seriesNo")!=null) { %>
								<th rowspan="2" style="vertical-align: bottom;"><a href="#" onclick="viewCF(false);">View Comments</a> | </th>
								<th rowspan="2" style="vertical-align: bottom;"><a href="#" onclick="viewCF(true);">Sharer FFA</a> | </th>
								<th rowspan="2" style="vertical-align: bottom;"><a href="#" onclick="viewCF(true,true);">Purchase FFA</a> | </th>
			<% } %>
				</tr>
				<tr>
					<td><jsp:getProperty name="flighttopbean" property="seriesNo" />&nbsp;</td>
					<td><jsp:getProperty name="flighttopbean" property="season" />&nbsp;</td>
					<td><jsp:getProperty name="flighttopbean" property="version" />&nbsp;</td>
					<td><jsp:getProperty name="flighttopbean" property="weekday" />&nbsp;</td>
					<td><jsp:getProperty name="flighttopbean" property="startDate" />&nbsp;</td>
					<td><jsp:getProperty name="flighttopbean" property="endDate" />&nbsp;</td>
					<td><jsp:getProperty name="flighttopbean" property="frequency" />&nbsp;</td>
				</tr>
			</table>
		</td>
		<td style="text-align: right;">
			<button onclick="doClose();">Close</button>
		</td>
	</tr>
</table>
<hr/>
<!--
 Operational Details section
-->
<% if (request.getParameter("seriesNo")!=null) { %>
<table cellspacing="0" cellpadding="2" class="innerTabTable">
	<tr>
		<th id="tab0" class="activeTab" onclick="if (!groupActive) changeActiveTab();">Date Groups</th>
		<th id="tab1" class="inactiveTab" onclick="if (groupActive) changeActiveTab();">Date List</th>
	</tr>
	<tr>
		<th class="monospace" style="border-left: 1px solid black; text-align: center;" width="250px">Dates</th>
		<td rowspan="2" align="left" style="border-right: 1px solid black;">
			<div style="overflow: auto; width: 100%; height: 100%;">
				<table class="monospace" id="sectorsByDateTable" width="100%" cellspacing="0" cellpadding="2" style="background-color: #ffffff;">
					<tr>
						<th>#</th>
						<th>From</th>
						<th>To</th>
						<th>Dep.Time</th>
						<th>Arr.Time</th>
						<th>+Day</th>
						<th>Flight No.</th>
						<th>Aircraft</th>
						<th>Flighttype</th>
					</tr>
				</table>
			</div>
		</td>
	</tr>
	<tr>
		<td class="activeData" style="border-bottom: 0px; border-right: 0px; text-align: center; " >
			<select name="dateSelect" size="10" class="monospace" onchange="reloadSectorsByDateTable();" style="font-family: courier; font-size: 8pt; font-weight: normal;">
			</select>
		</td>
	</tr>
	<%
	if (request.isUserInRole("VIKING_MANAGER") ||
					request.isUserInRole("VIKING_EDITOR")) { 
	%>
		<tr>
			<td class="activeData" style="border-right: 0px; text-align: center;">
				<div id="modifyId">
					<a href="#" onclick="doOp('edit');">Edit</a>
					<span id="deleteLink" style="display:none;">| <a href="#" onclick="doOp('add');">Add</a>
					| <a href="#" onclick="if (confirm('Are you sure you wish to delete this date and all its sector information?')) doOp('delete');">Delete Date</a></span>
				</div>
			</td>
			<td class="activeData" style="border-left: 0px;">
				&nbsp;
			</td>
		</tr>
	<% } %>
</table>
<% } %>
</form>
</body>
</html>