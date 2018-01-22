<%@ page errorPage="/ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/vikingtags.tld" prefix="viking" %>
<jsp:useBean	id="flightbotbean"
							class="uk.co.firstchoice.viking.gui.FlightBotFormBean"
							scope="request" />
<html>
<head>
<title>Flight Operations Input</title>
<link href="<html:rewrite page="/css/viking.css"/>" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="<html:rewrite page="/table.js"/>"></script>

<script type="text/javascript" src="<html:rewrite page="/validation.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/utils.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/datepicker.js"/>"></script>

<style>
table { padding-bottom: 10px; }
td { text-align: center; vertical-align: top; }
.monospace { font-family: courier; font-size: 8pt; font-weight: normal; }
.datatable th { text-align: center; vertical-align: middle; color: darkBlue; }
.datatable td { text-align: center; }
</style>
<script>
var frm;

var cutoffDate = '<%= new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()) %>';
var isManager = <%= request.isUserInRole(uk.co.firstchoice.viking.gui.util.Const.VIKING_MANAGER) %>;
var liveVersion = <%= flightbotbean.isLiveVersion() %>;

var allDates = [<jsp:getProperty name="flightbotbean" property="departureDates" />];
// ['02/10/2003','09/10/2003','16/10/2003','22/10/2003','30/10/2003'];

var season = "<%= request.getParameter("season") %>";

var customerAllocationArr = <viking:vikingSelector selectionType="customerArray" />;

var supplierAllocationArr = <viking:vikingSelector selectionType="supplierArray" />;


function changeSPTable(toSell) {
	frm.sellPurchaseTable.value = toSell;
	window.document.all.selltable.style.display = toSell=="1"?"block":"none";
	window.document.all.purchasetable.style.display = toSell=="0"?"block":"none";
}


//Toggle between which of the two selling tables (By Sector or By Date) are active

function changeSellTable(toCustomer) {
	frm.sellCustDateTable.value = toCustomer;
	window.document.all.sellCustomerTable.style.display = toCustomer=="1"?"block":"none";
	window.document.all.sellDateTable.style.display = toCustomer=="0"?"block":"none";
}


//Toggle between which of the two purchasing tables (By Sector or By Date) are active

function changePurchaseTable(toSector) {
	frm.purcSectorDateTable.value = toSector;
	window.document.all.purchaseCustomerTable.style.display = toSector=="1"?"block":"none";
	window.document.all.purchaseDateTable.style.display = toSector=="0"?"block":"none";
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


function doCS(action, csType, single) {
	if (action == "add") {
			editCS(csType, action, false);
	} else {
		var val = radioSelected(eval("frm."+csType+(single==null?"RowSelect":"BySectorRowSelect")));
		if (val == "") {
			alert("Please select line to "+action);
		} else {
			if (action == "delete") {
				if (confirm("Are you sure you wish to delete this "+csType+" entry?")) {
					var comm = getComment();
					var commentOK = false;
					if (comm != null) {
						var i = comm.indexOf(';');
						if (i > -1 && i < 5) {
							var ct = comm.substr(0, i);
							var co = comm.substr(i+1);
							editCS(csType,action,(single != null),val,ct,co);
							commentOK = true;
						}
					}
      		if (!commentOK) {
            if (liveVersion) {
              alert("Allocation NOT deleted due to missing comment");
            } else {
      				editCS(csType,action,(single != null),val);
            }
          }
				}
			} else if (action == "edit") {
				editCS(csType,action,(single != null),val);
			}
		}
	}
}


function editCS(csType,action,single,selLine,amendmentType,comment) {
	var maxHeight = window.screen.availHeight;
	var maxWidth = window.screen.availWidth;
//	var width = parseInt(window.screen.availWidth*0.98, 10); 
	var width = window.screen.availWidth;
	var height = 450;
//	var leftPoint = parseInt(maxWidth / 2) - parseInt(width / 2);
	var leftPoint = 1;
	var topPoint = parseInt(maxHeight / 2) - parseInt(height / 2);
	var qString = "type="+csType+
								"&seriesNo=<jsp:getProperty name="flightbotbean" property="seriesNo" />"+
								"&action="+action+"&editType="+action+"&single="+single+"&liveVersion="+liveVersion;
	if (single) {
		var dateSel = eval("frm."+csType+"DepartureDates");
//		var date = dateSel.options[dateSel.selectedIndex].value;
		var sectorsSel = eval("frm."+csType+"Sectors");
//		var sector = sectorsSel.options[sectorsSel.selectedIndex].value;
		var arr = selLine.split(',');
		qString +=	"&custSupp="+escape(arr[0])+
								"&seatClass="+arr[1];
    for (var x=0; x<dateSel.options.length; x++) {
      if (dateSel.options[x].selected) {
        qString += ("&startDate="+escape(dateSel.options[x].value)+"&endDate="+escape(dateSel.options[x].value));
      }
    }
    for (var x=0; x<sectorsSel.options.length; x++) {
      if (sectorsSel.options[x].selected) {
        qString += ("&sectors="+escape(sectorsSel.options[x].value));
      }
    }
	} else {
		var csSel = eval("frm."+csType+"Selector");
		var csCode = escape(csSel.length>0?csSel[csSel.selectedIndex].value:"");
		qString += "&custSupp="+csCode;
		if (selLine) {
			var arr = selLine.split(',');
			qString +=	"&startDate="+escape(arr[0])+
									"&endDate="+escape(arr[1])+
									"&sectors="+escape(arr[2])+
									"&seatClass="+arr[3];
		}
	}
	if (action == 'delete' && amendmentType != null && comment != null) {
		qString += "&amendmentType="+amendmentType+"&comments="+escape(comment);
		height = 10; width = 10;
	}
	var win = window.open("flightsellbuy.do?"+qString,'editchpu',
				'width='+width+',height='+height+
				',scrollbars=yes,menubar=no,location=no,status=no,toolbar=no,resizable=yes',false);
	win.focus();
}


function reloadSectorsByDateTable() {
	var tId = "sectorsByDateTable";
	deleteAllTableRows(tId);
	var dateNo = frm.opSelect.selectedIndex;
	dateNo = dateNo<0?0:dateNo;
	var sectorsArr = sectorsByDateArr[dateNo];
	for (var i=0; i < sectorsArr.length; i++) {
		addTableRow(tId,
								sectorsArr[i][0],
								sectorsArr[i][1],
								sectorsArr[i][2],
								sectorsArr[i][3],
								sectorsArr[i][4],
								sectorsArr[i][5],
								sectorsArr[i][6],
								sectorsArr[i][7]
/*								sectorsArr[i][8] */ /* added for meal type */
);
	}
	var trs = document.getElementById(tId).getElementsByTagName('tbody')[0].getElementsByTagName('tr');
	for (var j=0,len2=trs.length; j < len2; j++) {
		trs[j].className=(j==0?'':('line'+(j%2)));
	}
}


function reloadCustomersBySectorTable() {
	var tId = "allocationByCustomerTable";
	deleteAllTableRows(tId);
	var sectorNo = frm.customerSelector.selectedIndex;
	sectorNo = sectorNo<0?0:sectorNo;
	var sharersArr = customerAllocationArr[sectorNo];
	for (var i=0; i < sharersArr.length; i++) {
		var radioValue = sharersArr[i][1]+","+sharersArr[i][2]+","+sharersArr[i][3]+","+sharersArr[i][4];
		var x = document.createElement(
									"<input type='radio' class='rowradio' name='customerRowSelect' "+
									((dateBefore(sharersArr[i][1], cutoffDate) && !isManager)?"disabled='true' ":"")+
									"value='"+radioValue+"' />");
		var row = addTableRow(tId,
													x,
													sharersArr[i][1],
													sharersArr[i][2],
													sharersArr[i][3],
													sharersArr[i][4],
													sharersArr[i][5],
													sharersArr[i][6],
													sharersArr[i][7],
													sharersArr[i][8],
													sharersArr[i][9],
													sharersArr[i][10],
													sharersArr[i][11],
													sharersArr[i][12],
													sharersArr[i][13]  /* added for meal type */

                                                                                                        );
		row.style.backgroundColor = "#"+sharersArr[i][14];
	}
}


function reloadSuppliersBySectorTable() {
	var tId = "suppliersBySectorTable";
	deleteAllTableRows(tId);
	var sectorNo = frm.supplierSelector.selectedIndex;
	sectorNo = sectorNo<0?0:sectorNo;
	var suppliersArr = supplierAllocationArr[sectorNo];
	for (var i=0; i < suppliersArr.length; i++) {
		var radioValue = suppliersArr[i][1]+","+suppliersArr[i][2]+","+suppliersArr[i][3]+","+suppliersArr[i][4];
		var x = document.createElement(
									"<input type='radio' class='rowradio' name='supplierRowSelect' "+
									((dateBefore(suppliersArr[i][1], cutoffDate) && !isManager)?"disabled='true' ":"")+
									"value='"+radioValue+"' />");
		var row = addTableRow(tId,
													x,
													suppliersArr[i][1],
													suppliersArr[i][2],
													suppliersArr[i][3],
													suppliersArr[i][4],
													suppliersArr[i][5],
													suppliersArr[i][6],
													suppliersArr[i][7],
													suppliersArr[i][8],
													suppliersArr[i][9],
													suppliersArr[i][10],
													suppliersArr[i][11],
													suppliersArr[i][12],
													suppliersArr[i][13]  /* added for meal type */
												                                                        );
		row.style.backgroundColor = "#"+suppliersArr[i][14];
	}
}


function selectorContains(sel, val) {
	if (sel.options && sel.options.length)
		for (var i=0; i<sel.options.length; i++) {
			if (sel.options[i].value == val)
				return true;
		}
	return false;
}


function arrayContains(arr, val) {
  if (arr.length) {
    for (var i=0; i<arr.length; i++) {
      if (typeof arr[i] == "object" && arr[i].length && typeof val == "object" && val.length) { // we're comparing an array of arrays with an array 
        var identical = true;
        for (var x=0; x<arr[i].length; x++) {
          if (arr[i][x] != val[x])
            identical = false;
        }
        if (identical)
          return true;
      } else {
    		if (arr[i] == val)
    			return true;
      }
  	}
  }
	return false;
}


function deleteArrayElement(arr, idx) {
  var newArr = new Array();
  for (var i=0; i<arr.length; i++) {
    if (i != idx) {
      newArr[newArr.length] = arr[i];
    }
  }
  return newArr;
}


function loadSectors(csType) {
	var secSel = eval("frm."+csType+"Sectors");
        var prevSelected = secSel.selectedIndex < 0?"":secSel.options[secSel.selectedIndex].value;
	secSel.options.length = 0;
	var dateSel = eval("frm."+csType+"DepartureDates");
	if (dateSel.selectedIndex > -1) {
		var date = strToDate(dateSel.options[dateSel.selectedIndex].value);
		if (window.document.all.customerDateEditId) {
			if (dateBefore(date, cutoffDate) && ! isManager) {
				window.document.all.customerDateEditId.style.display = "none";
				window.document.all.supplierDateEditId.style.display = "none";
			} else {
				window.document.all.customerDateEditId.style.display = "block";
				window.document.all.supplierDateEditId.style.display = "block";
			}
		}
    var sIdx = 0;
    var secArr = new Array();
		var arr = eval(csType+"AllocationArr");
// Build array of all first dates gateways 
		for (var outer=0; outer < arr.length; outer++) {
			for (var inner=0; inner < arr[outer].length; inner++) {

// alert( arr[outer][inner][1] + "==" + arr[outer][inner][2] + "==" + arr[outer][inner][3] );

				if (dateBetween(date, strToDate(arr[outer][inner][1]), strToDate(arr[outer][inner][2])) &&
						!arrayContains(secArr, arr[outer][inner][3])) {
                  //  alert("inside if");
					secArr[secArr.length] = arr[outer][inner][3];
				}
			}
    }
// Remove all the ones not contained in subsequent dates 
    for (var dateIdx=0; dateIdx < dateSel.options.length; dateIdx++) {
      if (dateSel.options[dateIdx].selected) {
        date = strToDate(dateSel.options[dateIdx].value);
        var newArr = new Array();
    		for (var outer=0; outer < arr.length; outer++) {
    			for (var inner=0; inner < arr[outer].length; inner++) {
    				if (dateBetween(date, strToDate(arr[outer][inner][1]), strToDate(arr[outer][inner][2])) &&
    						!arrayContains(newArr, arr[outer][inner][3])) {
    					newArr[newArr.length] = arr[outer][inner][3];
        		}
          }
        }
        var tmpArr = new Array();
        for (var i=0; i < secArr.length; i++) {
          if (arrayContains(newArr, secArr[i])) {
            tmpArr[tmpArr.length] = secArr[i];
          }
        }
        secArr=tmpArr;
      }
    }
    for (var i=0; i < secArr.length; i++) {
      secSel.options[secSel.options.length] = new Option(secArr[i], secArr[i]);
      if (secArr[i] == prevSelected) {
        sIdx = secSel.length-1;
      }
    }
		secSel.selectedIndex = sIdx;;
	}
	loadBySectorDetails(csType);
}

/*
function oldLoadSectors(csType) {
	var secSel = eval("frm."+csType+"Sectors");
  var prevSelected = secSel.selectedIndex < 0?"":secSel.options[secSel.selectedIndex].value;
	secSel.options.length = 0;
	var dateSel = eval("frm."+csType+"DepartureDates");
	if (dateSel.selectedIndex > -1) {
		var date = strToDate(dateSel.options[dateSel.selectedIndex].value);
		if (window.document.all.customerDateEditId) {
			if (dateBefore(date, cutoffDate) && ! isManager) {
				window.document.all.customerDateEditId.style.display = "none";
				window.document.all.supplierDateEditId.style.display = "none";
			} else {
				window.document.all.customerDateEditId.style.display = "block";
				window.document.all.supplierDateEditId.style.display = "block";
			}
		}
    var sIdx = 0;
		var arr = eval(csType+"AllocationArr");
		for (var outer=0; outer < arr.length; outer++) {
			for (var inner=0; inner < arr[outer].length; inner++) {
				if (dateBetween(date, strToDate(arr[outer][inner][1]), strToDate(arr[outer][inner][2])) &&
						!selectorContains(secSel, arr[outer][inner][3])) {
					secSel.options[secSel.options.length] = new Option(arr[outer][inner][3], arr[outer][inner][3]);
          if (arr[outer][inner][3] == prevSelected) {
            sIdx = secSel.options.length-1;
          }
				}
			}
    }
		secSel.selectedIndex = sIdx;;
	}
	loadBySectorDetails(csType);
}
*/

  

function loadBySectorDetails(csType) {
	var tId = csType+"BySectorTable";
	deleteAllTableRows(tId);
	var secSel = eval("frm."+csType+"Sectors");
	var dateSel = eval("frm."+csType+"DepartureDates");
	if (secSel.selectedIndex > -1 && dateSel.selectedIndex > -1) {
    var first = true;
    var allocArray = new Array();
    var arr = eval(csType+"AllocationArr");
    for (var dateIdx=0; dateIdx<dateSel.options.length; dateIdx++) {
      if (dateSel.options[dateIdx].selected) {
        for (var secIdx=0; secIdx<secSel.options.length; secIdx++) {
          if (secSel.options[secIdx].selected) {
            var date = strToDate(dateSel.options[dateIdx].value);
            var sector = secSel.options[secIdx].value;
            if (first) { //if first then just populate with all entries for first date/sector 
              for (var outer=0; outer < arr.length; outer++) {
                for (var inner=0; inner < arr[outer].length; inner++) {
                  if (sector == arr[outer][inner][3] && 
                      dateBetween(date, strToDate(arr[outer][inner][1]), strToDate(arr[outer][inner][2]))) {
                    var xArr = new Array(arr[outer][inner][0],arr[outer][inner][4],arr[outer][inner][5],arr[outer][inner][6],arr[outer][inner][7],arr[outer][inner][8],arr[outer][inner][9],arr[outer][inner][10],arr[outer][inner][11],arr[outer][inner][12]);
                    if (!arrayContains(allocArray, xArr)) {
                      allocArray[allocArray.length] = xArr;
                    }
                  }
                }
              }
              first = false;
            } else { // if NOT first then remove all from allocArray that aren't in this date/sector 
// build tmpArray of all info for this date/sector 
              var tmpArray = new Array();
              for (var outer=0; outer < arr.length; outer++) {
                for (var inner=0; inner < arr[outer].length; inner++) {
                  if (sector == arr[outer][inner][3] && 
                      dateBetween(date, strToDate(arr[outer][inner][1]), strToDate(arr[outer][inner][2]))) {
                    var xArr = new Array(arr[outer][inner][0],arr[outer][inner][4],arr[outer][inner][5],arr[outer][inner][6],arr[outer][inner][7],arr[outer][inner][8],arr[outer][inner][9],arr[outer][inner][10],arr[outer][inner][11],arr[outer][inner][12]);
                    if (!arrayContains(tmpArray, xArr)) {
                      tmpArray[tmpArray.length] = xArr;
                    }
                  }
                }
              }
// now remove all entries in allocArray that isn't in tmpArray 
              var tmpAllocArray = new Array();
              for (var i=0; i < allocArray.length; i++) {
                if (arrayContains(tmpArray, allocArray[i])) {
                  tmpAllocArray[tmpAllocArray.length] = allocArray[i];
                }
              }
              allocArray = tmpAllocArray;
            }

          }
        }
      }
    }
    for (var idx=0; idx<allocArray.length; idx++) {
      var radioValue = allocArray[idx][0]+","+allocArray[idx][1];
      var x = document.createElement("<input type='radio' class='rowradio' name='"+csType+"BySectorRowSelect' value='"+radioValue+"'>");
      addTableRow(tId,
									x,
                  allocArray[idx][0],
                  allocArray[idx][1],
                  allocArray[idx][2],
                  allocArray[idx][3],
                  allocArray[idx][4],
                  allocArray[idx][5],
                  allocArray[idx][6],
                  allocArray[idx][7],
                  allocArray[idx][8],
                  allocArray[idx][9]);
    }
    var trs = document.getElementById(tId).getElementsByTagName('tbody')[0].getElementsByTagName('tr');
    for (var j=0,len2=trs.length; j < len2; j++) {
      trs[j].className=(j==0?'':('line'+(j%2)));
    }

  }
}


function doOnLoad() {
	frm=document.forms[0];
	for (var i=0; i < customerAllocationArr.length; i++) {
		frm.customerSelector.options[frm.customerSelector.options.length] = new Option(customerAllocationArr[i][0][0],customerAllocationArr[i][0][0]);
	}
	frm.customerSelector.selectedIndex = 0;
	for (var i=0; i < supplierAllocationArr.length; i++) {
		frm.supplierSelector.options[frm.supplierSelector.options.length] = new Option(supplierAllocationArr[i][0][0],supplierAllocationArr[i][0][0]);
	}
	frm.supplierSelector.selectedIndex = 0;
	if (customerAllocationArr.length > 0)
		reloadCustomersBySectorTable();
	if (supplierAllocationArr.length > 0)
		reloadSuppliersBySectorTable();
	for (var i=0; i<allDates.length; i++) {
		var o1 = new Option(allDates[i], allDates[i]);
		var o2 = new Option(allDates[i], allDates[i]);
		if (dateBefore(allDates[i], cutoffDate)) {
			o1.style.cssText = 'background-color: gray; color: white;';
			o2.style.cssText = 'background-color: gray; color: white;';
		}
		frm.customerDepartureDates.options[frm.customerDepartureDates.options.length] = o1;
		frm.supplierDepartureDates.options[frm.supplierDepartureDates.options.length] = o2;
	}
	frm.customerDepartureDates.selectedIndex = 0;
	frm.supplierDepartureDates.selectedIndex = 0;
	loadSectors('customer');
	loadSectors('supplier');
	changeSPTable(frm.sellPurchaseTable.value);
	changeSellTable(frm.sellCustDateTable.value);
	changePurchaseTable(frm.purcSectorDateTable.value);
}

</script>
</head>
<body onload="doOnLoad();">
<form method="post" action="flightbot.do">
<input type="hidden" name="seriesNo" value="<jsp:getProperty name="flightbotbean" property="seriesNo" />" />
<input type="hidden" name="sellPurchaseTable" value="<jsp:getProperty name="flightbotbean" property="sellPurchaseTable" />" />
<input type="hidden" name="sellCustDateTable" value="<jsp:getProperty name="flightbotbean" property="sellCustDateTable" />" />
<input type="hidden" name="purcSectorDateTable" value="<jsp:getProperty name="flightbotbean" property="purcSectorDateTable" />" />
<div id="selltable" style="display:block;">
<table cellspacing="0" cellpadding="2" class="outerTabTable" style="width:100%; height:100%;">
	<tr>
		<th class="activeTab" width="50%">Selling</th>
		<th class="inactiveTab" width="50%" onClick="changeSPTable('0');">Purchasing</th>
	</tr>
	<tr>
		<td colspan="2" class="activeData">
			<!--
				Inner table to select between Sell Allocation By Sector or Sell Allocation By Date
				The two DIV's ("sellSectorTable" and "sellDateTable") take turn in being active -
				when one is clicked on and made active (visible) the other is made inactive (hidden).
				This is with "sellSectorTable" as the active section
			-->
			<div id="sellCustomerTable" style="display:block;">
			<table cellspacing="0" cellpadding="2" class="innerTabTable" style="width: 100%; height:100%;">
				<tr>
					<th class="activeTab" width="50%">Allotment By Customer</th>
					<th class="inactiveTab" width="50%" onClick="changeSellTable('0');">Allotment By Date</th>
				</tr>
				<tr>
					<td colspan="2" class="activeData">
						<table cellspacing="0" cellpadding="2" style="width: 100%; height:100%;">
							<tr>
								<th width="12%">Customer</th>
								<td rowspan="2" align="left">
									<div style="overflow: auto; width: 100%; height: 100%;">
									<table id="allocationByCustomerTable" cellspacing="0" class="datatable" style="background-color: #ffffff; width: 100%;">
										<tr>
											<th>&nbsp;</th>
											<th>Date From</th>
											<th>Date To</th>
											<th>Sector</th>
											<th>Class</th>
											<th>Desc</th>
											<th>Qty</th>
											<th>Currency</th>
											<th>Price</th>
											<th>Selling Code</th>
											<th>Meal Type</th>
											<th>Contract</th>
											<th>Status</th>
											<th>Broker</th>
										</tr>
									</table>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<select	name="customerSelector"
													class="monospace"
													size="12"
													style="width: 80%;"
													onChange="reloadCustomersBySectorTable();" />
								</td>
							</tr>
<%
		if (request.isUserInRole("VIKING_MANAGER") ||
				request.isUserInRole("VIKING_EDITOR")) { 
%>
							<tr>
								<td class="activeData" style="border: 0px;">
									&nbsp;
								</td>
								<td colspan="2" class="activeData" style="text-align: center; border: 0px;">
									<a href="#" onClick="doCS('add','customer');">Add</a> |
									<a href="#" onClick="doCS('edit','customer');">Edit</a> |
									<a href="#" onClick="doCS('delete','customer');">Delete</a>
								</td>
							</tr>
<% } %>
						</table>
					</td>
				</tr>
			</table>
			</div>
			
			<!---
				Inner table to select between Sell Allocation By Sector or Sell Allocation By Date
				This is with Sell Allocation By Date as the active section
			--->
			<div id="sellDateTable" style="display:none;">
			<table cellspacing="0" cellpadding="2" class="innerTabTable" style="width:100%; height:100%;">
				<tr>
					<th class="inactiveTab" width="50%" onClick="changeSellTable('1');">Allotment By Customer</th>
					<th class="activeTab" width="50%">Allotment By Date</th>
				</tr>
				<tr>
					<td colspan="2" class="activeData">
						<table cellspacing="0" cellpadding="2" style="width:100%; height:100%;">
							<tr>
								<th width="12%">Date</th>
								<th width="12%">Sector</th>
								<td rowspan="2">
									<div style="overflow: auto; width: 100%; height: 100%">
										<table class="datatable" id="customerBySectorTable" cellspacing="0" cellpadding="2" style="width: 100%; background-color: #ffffff;">
											<tr>
												<th>&nbsp;</th>
												<th>Customer</th>
												<th>Class</th>
												<th>Desc</th>
												<th>Qty.</th>
												<th>Curr.</th>
												<th>Price</th>
                                                <th>Selling Code</th>
                                                <th>Meal Type</th>
												<th>Contract</th> 
												<th>Status</th>
												<th>Broker</th>
											</tr>
										</table>
									</div>
								</td>
							</tr>
							<tr>
								<td>
<!-- make selector multiple -->
									<select	name="customerDepartureDates"
													class="monospace"
													size="12"
                          multiple="true"
													style="width: 100%; height: 100%;"
													onChange="loadSectors('customer');" />
								</td>
								<td>
									<select	name="customerSectors"
													class="monospace"
													size="12"
                          multiple="true"
													style="width: 80%;"
													onChange="loadBySectorDetails('customer');" />
								</td>
							</tr>
<%
		if (request.isUserInRole("VIKING_MANAGER") ||
				request.isUserInRole("VIKING_EDITOR")) { 
%>
							<tr>
								<td class="activeData" style="border: 0px;">&nbsp;</td>
								<td class="activeData" style="border: 0px;"></td>
								<td colspan="2" class="activeData" style="text-align: center; border: 0px;">
									<div id="customerDateEditId">
										<a href="#" onClick="doCS('edit','customer','single');">Edit</a> |
										<a href="#" onClick="doCS('delete','customer','single');">Delete</a>
									</div>
								</td>
							</tr>
<% } %>
						</table>
					</td>
				</tr>
			</table>
			</div>
		</td>
	</tr>
</table>
</div>
<!---
	Outer table to select between Selling and Purchasing.
	This is with Purchasing as the active section
--->
<div id="purchasetable" style="display:none;">
<table cellspacing="0" cellpadding="2" class="outerTabTable" style="width:100%; height:100%;">
	<tr>
		<th class="inactiveTab" width="50%" onClick="changeSPTable('1');">Selling</th>
		<th class="activeTab" width="50%">Purchasing</th>
	</tr>
	<tr>
		<td colspan="2" class="activeData">
			<!--
				Inner table to select between Purchase Allocation By Sector or Purchase Allocation By Date
				The two DIV's ("sellSectorTable" and "sellDateTable") take turn in being active -
				when one is clicked on and made active (visible) the other is made inactive (hidden).
				This is with "sellSectorTable" as the active section
			-->
			<div id="purchaseCustomerTable" style="display:block;">
			<table cellspacing="0" cellpadding="2" class="innerTabTable" style="width:100%; height:100%;">
				<tr>
					<th class="activeTab" width="50%">Purchasing By Supplier</th>
					<th class="inactiveTab" width="50%" onClick="changePurchaseTable('0');">Purchasing By Date</th>
				</tr>
				<tr>
					<td colspan="2" class="activeData">
						<table cellspacing="0" cellpadding="2" style="width:100%; height:100%;">
							<tr>
								<th width="12%">Supplier</th>
								<td rowspan="2" align="left">
									<div style="overflow: auto; width: 100%; height: 100%">
									<table id="suppliersBySectorTable" class="datatable" cellspacing="0" style="background-color: #ffffff; width: 100%;">
										<tr>
											<th>&nbsp;</th>
											<th>Date From</th>
											<th>Date To</th>
											<th>Sector</th>
											<th>Class</th>
											<th>Desc</th>
											<th>Qty</th>
											<th>Currency</th>
											<th>Price</th>
                                            <th>Selling Code</th>
                                            <th>MealType</th>
											<th>Contract</th>
											<th>Status</th>
											<th>Broker</th>
										</tr>
									</table>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<select	name="supplierSelector"
													class="monospace"
													size="12"
													style="width: 80%;"
													onChange="reloadSuppliersBySectorTable();" />
								</td>
							</tr>
<%
		if (request.isUserInRole("VIKING_MANAGER") ||
				request.isUserInRole("VIKING_EDITOR")) { 
%>
							<tr>
								<td class="activeData" style="border: 0px;">
									&nbsp;
								</td>
								<td colspan="2" class="activeData" style="text-align: center; border: 0px;">
									<a href="#" onClick="doCS('add','supplier');">Add</a> |
									<a href="#" onClick="doCS('edit','supplier');">Edit</a> |
									<a href="#" onClick="doCS('delete','supplier');">Delete</a>
								</td>
							</tr>
<% } %>
						</table>
					</td>
				</tr>
			</table>
			</div>
			
			
			<!---
				Inner table to select between Purchase Allocation By Sector or Purchase Allocation By Date
				This is with Purchase Allocation By Date as the active section
			--->
			<div id="purchaseDateTable" style="display:none;">
			<table cellspacing="0" cellpadding="2" class="innerTabTable" style="width:100%; height:100%;">
				<tr>
					<th class="inactiveTab" width="50%" onClick="changePurchaseTable('1');">Purchasing By Sector</th>
					<th class="activeTab" width="50%">Purchasing By Date</th>
				</tr>
				<tr>
					<td colspan="3" class="activeData">
						<table cellspacing="0" cellpadding="2" style="width:100%; height:100%;">
							<tr>
								<th width="12%">Date</th>
								<th width="12%">Sector</th>
								<td rowspan="2">
									<div style="overflow: auto; width: 100%; height: 100%">
										<table class="datatable" id="supplierBySectorTable" cellspacing="0" cellpadding="2" style="background-color: #ffffff; width: 100%;">
											<tr>
												<th>&nbsp;</th>
												<th>Supplier</th>
												<th>Class</th>
												<th>Desc</th>
												<th>Qty.</th>
												<th>Curr.</th>
												<th>Price</th>
                                                <th>Selling Code</th>
                                                <th>MealType</th>
												<th>Contract</th>
												<th>Status</th>
												<th>Broker</th>
											</tr>
										</table>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<select	name="supplierDepartureDates"
													class="monospace"
													size="12"
						                          	multiple="true"
													style="width: 100%; height: 100%;"
													onChange="loadSectors('supplier');" />
								</td>
								<td>
									<select	name="supplierSectors"
													class="monospace"
													size="12"
	   					                            multiple="true"
													style="width: 80%;"
													onChange="loadBySectorDetails('supplier');" />
								</td>
							</tr>
<%
		if (request.isUserInRole("VIKING_MANAGER") ||
				request.isUserInRole("VIKING_EDITOR")) { 
%>
							<tr>
								<td class="activeData" style="border: 0px;">&nbsp;</td>
								<td class="activeData" style="border: 0px;"></td>
								<td colspan="2" class="activeData" style="text-align: center; border: 0px;">
									<div id="supplierDateEditId">
										<a href="#" onClick="doCS('edit','supplier','single');">Edit</a> |
										<a href="#" onClick="doCS('delete','supplier','single');">Delete</a>
									</div>
								</td>
							</tr>
<% } %>
						</table>
					</td>
				</tr>
			</table>
			</div>
		</td>
	</tr>
</table>
</div>
</form>
</body>
</html>