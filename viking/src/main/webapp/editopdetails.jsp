<%@ page errorPage="/ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/vikingtags.tld" prefix="viking" %>
<jsp:useBean	id="flightopform"
							class="uk.co.firstchoice.viking.gui.FlightOpFormBean"
							scope="request"/>
<html:html>
<head>
<title>Add Operational Detail</title>
<style>
td { text-align: center; vertical-align: top;}
input { border: 1px solid black; text-align: right;}
table { border: 1px solid darkblue;}
.rowBtn { font-size: 6pt; margin: 1px; border: 1px; padding: -5px; text-align: center;}

</style>
<link href="<html:rewrite page="/css/viking.css"/>" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="<html:rewrite page="/table.js"/>"></script>

<script type="text/javascript" src="<html:rewrite page="/validation.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/utils.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/datepicker.js"/>"></script>

<%
	int numRows = (flightopform.getDepAirport()==null?0:flightopform.getDepAirport().length);
  java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

  String fromDate = request.isUserInRole("VIKING_EDITOR") &&
                    sdf.parse(flightopform.getFromDate()).getTime() < new java.util.Date().getTime()?
                    sdf.format(new java.util.Date()):
                    flightopform.getFromDate();
%>
<script>
var frm;
var selFromDate = "<%= fromDate %>";
var selToDate = "<%= flightopform.getToDate() %>";
var frequency = 7;
var liveVersion = <%= flightopform.isLiveVersion() %>;


function insertRowAbove(aboveLine) { // add a row to the table of sectors 
var s="";
for(i = 0; i < document.all.length; i++){
   s+=document.all(i).tagName;
}
	if (validate(false)) {
		var rowNo = frm.sectorNo?frm.sectorNo.length?(frm.sectorNo.length+1):2:1;
		var e1 = document.createElement("<input	name='sectorNo' readonly='true'	size='3' value='"+rowNo+"' />");
		var e2 = document.createElement("<input	type='hidden' name='sequenceNo' />");
		var e3 = document.createElement("<input	name='depAirport' onChange='this.value=trim(this.value).toUpperCase();' size='5' maxlength='3' />");
		var e4 = document.createElement("<input	name='arrAirport' onChange='this.value=trim(this.value).toUpperCase();' size='5' maxlength='3' />");
		var e5 = document.createElement("<input	name='depTime' size='7' maxlength='6' />");
		var e6 = document.createElement("<input	name='arrTime' size='7' maxlength='6' />");
		var e7 = document.createElement("<input	name='dayOffset' size='3' maxlength='2' />");
/*		var e8 = document.createElement("<input	name='flightPre' onChange='this.value=trim(this.value).toUpperCase();' size='4' maxlength='3' />"); 
		var e9 = document.createElement("<input	name='flightNo' onChange='this.value=trim(this.value).toUpperCase();' size='5' maxlength='4' />");
*/
		var e8 = document.createElement("<input	name='flightPre' onChange='this.value=trim(this.value);' size='4' maxlength='3' />");
		var e9 = document.createElement("<input	name='flightNo' onChange='this.value=trim(this.value);' size='5' maxlength='4' />");
     	var e10 = document.createElement("<input name='aircraft' onChange='this.value=trim(this.value).toUpperCase();' size='6' maxlength='4' />");
        var ele = document.createElement("select");
        ele.name = 'flightType';
		ele.id = 'flightType';

        <viking:vikingSelector name="fliightType"	selectionType="listFlightTypeSelector"  /> 
/*		var e11 = document.createElement("<input	name='numAlloc'	value='0'	readonly='true'	style='background-color: #f0f0f0;' size='3' />"); */

		var e12 = document.createElement("<input type='button' class='rowBtn' onclick='insertRowAbove(this.parentElement.parentElement.rowIndex - 1);' value='Insert sector above' />");
		var e13 = document.createElement("<input type='button' class='rowBtn' onclick='deleteRow(this.parentElement.parentElement.rowIndex - 1);' value='Delete sector' />");
  	    var e14 = document.createElement("<input type='hidden' name='rowChanged' />");
		var e15 = document.createElement("<input type='hidden' name='gwChanged' />"); 

		


		addTableRow("sectortable", e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, ele, e12, e13, e14, e15);  /* TUI Enhancements added ele*/
		if (aboveLine>-1) {

			for (var i=frm.sectorNo.length-1; i>aboveLine; i--) {
				frm.sequenceNo[i].value = frm.sequenceNo[i-1].value;
				frm.depAirport[i].value = frm.depAirport[i-1].value;
				frm.depAirport[i].defaultValue = frm.depAirport[i-1].defaultValue;
				frm.arrAirport[i].value = frm.arrAirport[i-1].value;
				frm.arrAirport[i].defaultValue = frm.arrAirport[i-1].defaultValue;
				frm.depTime[i].value = frm.depTime[i-1].value;
				frm.arrTime[i].value = frm.arrTime[i-1].value;
				frm.dayOffset[i].value = frm.dayOffset[i-1].value;
				frm.flightPre[i].value = frm.flightPre[i-1].value;
				frm.flightNo[i].value = frm.flightNo[i-1].value;
				frm.aircraft[i].value = frm.aircraft[i-1].value;
				frm.flightType[i].value = frm.flightType[i-1].value;

// alert("pause  i is "+ i + " abovelive is " + aboveLine);
            
//				frm.numAlloc[i].value = frm.numAlloc[i-1].value;
//				frm.numAlloc[i].defaultValue = frm.numAlloc[i-1].defaultValue;
			}

			frm.sequenceNo[aboveLine].value = "";
			frm.depAirport[aboveLine].value = "";
			frm.depAirport[aboveLine].defaultValue = "";
			frm.arrAirport[aboveLine].value = "";
			frm.arrAirport[aboveLine].defaultValue = "";
			frm.depTime[aboveLine].value = "";
			frm.arrTime[aboveLine].value = "";
			frm.dayOffset[aboveLine].value = "";
			frm.flightPre[aboveLine].value = "";
			frm.flightNo[aboveLine].value = "";
			frm.aircraft[aboveLine].value = "";
			frm.flightType[aboveLine].value = "";  // Make above line's Flight type as blank 

//			frm.numAlloc[aboveLine].value = "0";
//			frm.numAlloc[aboveLine].defaultValue = "0";
			frm.depAirport[aboveLine].focus();
		}
		if (frm.depAirport.length) {
			frm.depAirport[frm.depAirport.length-1].focus();
		} else {
			frm.depAirport.focus();
		}
	}
}


function deleteRow(lineNo) {
	var lastRow = frm.sectorNo.length?frm.sectorNo.length:1;
	var delSeqNo;
//	var numAlloc;
	if (frm.sequenceNo.length) {
		delSeqNo = frm.sequenceNo[lineNo].value;
//		numAlloc = frm.numAlloc[lineNo].value;
	} else {
		delSeqNo = frm.sequenceNo.value;
//		numAlloc = frm.numAlloc.value;
	}
//	if (numAlloc != "" && numAlloc != "0") {
//		alert("You cannot delete a sector with allocations on it. Please delete allocations first.");
//	} else {
		if (delSeqNo != "") {	// was it an existing sector that got deleted? 
			frm.deletedSequences.value += ((frm.deletedSequences.value==""?"":",")+delSeqNo);
		}
		if (frm.sectorNo.length) {

			for (var i=lineNo; i < frm.sectorNo.length-1; i++) {

				frm.sequenceNo[i].value = frm.sequenceNo[i+1].value;
				frm.depAirport[i].value = frm.depAirport[i+1].value;
				frm.depAirport[i].defaultValue = frm.depAirport[i+1].defaultValue;
				frm.arrAirport[i].value = frm.arrAirport[i+1].value;
				frm.arrAirport[i].defaultValue = frm.arrAirport[i+1].defaultValue;
				frm.depTime[i].value = frm.depTime[i+1].value;
				frm.arrTime[i].value = frm.arrTime[i+1].value;
				frm.dayOffset[i].value = frm.dayOffset[i+1].value;
				frm.flightPre[i].value = frm.flightPre[i+1].value;
				frm.flightNo[i].value = frm.flightNo[i+1].value;
				frm.aircraft[i].value = frm.aircraft[i+1].value;
				frm.flightType[i].value = frm.flightType[i+1].value;

//				frm.numAlloc[i].value = frm.numAlloc[i+1].value;
//				frm.numAlloc[i].defaultValue = frm.numAlloc[i+1].defaultValue;

			}
//		}
		deleteTableRow("sectortable",lastRow);
	}
}


function gwChange(rowNo) {
	frm.depAirport[rowNo].value = trim(frm.depAirport[rowNo].value.toUpperCase());
	frm.arrAirport[rowNo].value = trim(frm.arrAirport[rowNo].value.toUpperCase());
	if ((frm.depAirport[rowNo].value != "" && frm.depAirport[rowNo].defaultValue != "" &&
			 frm.depAirport[rowNo].value != frm.depAirport[rowNo].defaultValue) ||
			(frm.arrAirport[rowNo].value != "" && frm.arrAirport[rowNo].defaultValue != "" &&
			 frm.arrAirport[rowNo].value != frm.arrAirport[rowNo].defaultValue)) {
//		frm.numAlloc[rowNo].value = "0";
//		frm.numAlloc[rowNo].style.color = "red";
	} else {
//		frm.numAlloc[rowNo].value = frm.numAlloc[rowNo].defaultValue;
//		frm.numAlloc[rowNo].style.color = "black";
	}
}


function doOnLoad() {
	frm=window.document.forms[0];
	window.moveTo(	parseInt((window.screen.availWidth-document.body.offsetWidth) / 2),
									parseInt((window.screen.availHeight-document.body.offsetHeight) / 2));
	doDowChange();
}


function doDowChange() {
	var dow = strToDate('<%= flightopform.getToDate() %>').getDay();
	buildDateSelector(frm.fromDate, '<%= fromDate %>', '<%= flightopform.getToDate() %>', dow, frequency, selFromDate);
	buildDateSelector(frm.toDate, '<%= fromDate %>', '<%= flightopform.getToDate() %>', dow, frequency, selToDate);
}


function dateChange(from) {
	if (from) {
		if (frm.fromDate.selectedIndex > frm.toDate.selectedIndex) {
			frm.toDate.selectedIndex = 0;
		}
	} else {
		if (frm.fromDate.selectedIndex > frm.toDate.selectedIndex) {
			frm.fromDate.selectedIndex = 0;
		}
	}
}


function getFieldValue(fieldName) {
	var field = eval("frm."+fieldName);
	if (field.length) {
		return field[0].value;
	} else {
		return field.value;
	}
}


function getFieldDefaultValue(fieldName) {
	var field = eval("frm."+fieldName);
	if (field.length) {
		return field[0].defaultValue;
	} else {
		return field.defaultValue;
	}
}


function validate(toSave) {
		var errors = "";
		var gwChanged = false;
		if (frm.toDate && frm.toDate.type=='text') {
      if (validDate(frm.toDate.value) == "") {
        errors += "Please enter a valid date\n";
      } else {
        frm.toDate.value = validDate(frm.toDate.value);
        if (!dateBetween(frm.toDate.value, "<%= flightopform.getSeasonStart() %>", "<%= flightopform.getSeasonEnd() %>")) {
          errors += "Date not within season\n";
        }
      }
    }
    if (liveVersion) {
      if (toSave && frm.amendmentType.selectedIndex < 1) errors += "Amendment type not selected\n";
      if (toSave && trim(frm.comments.value) == "") errors += "Comment must be entered\n";
    }
		var numLines = frm.sectorNo?frm.sectorNo.length?frm.sectorNo.length:1:0;
		if (numLines == 1) { // not an array
			if ((getFieldDefaultValue("depAirport") != "" && getFieldValue("depAirport") != getFieldDefaultValue("depAirport")) ||
					(getFieldDefaultValue("arrAirport") != "" && getFieldValue("arrAirport") != getFieldDefaultValue("arrAirport"))) {
				gwChanged = true;
			}
			if (getFieldValue("depAirport") == "") errors += "Departure airport is missing.\n";
			if (getFieldValue("arrAirport") == "") errors += "Arrival airport is missing.\n";
			if (trim(getFieldValue("depTime")) != "") {
				if (validTime(getFieldValue("depTime")) == "") {
          var slotNo = parseInt(getFieldValue("depTime"));
          if (isNaN(slotNo) || trim(getFieldValue("depTime")) != slotNo || slotNo < 1 || slotNo > 3) {
            errors += "Departure time is invalid.\n";
          }
				} else {
					frm.depTime.value = validTime(getFieldValue("depTime"));
				}
			}
			if (trim(getFieldValue("arrTime")) != "") {
				if (validTime(getFieldValue("arrTime")) == "") {
					errors += "Arrival time is invalid.\n";
				} else {
					frm.arrTime.value = validTime(getFieldValue("arrTime"));
				}
			}
		} else {	// more than one line - it's an array
			for (var i=0; i < numLines-1; i++) {
        if (frm.arrAirport[i].value != frm.depAirport[i+1].value) {
          errors += "Sector "+(i+1)+"'s arrival airport is different from sector "+(i+2)+"'s departure airport.\n";
        }
      }
			for (var i=0; i < numLines; i++) {
				if ((frm.depAirport[i].defaultValue != "" && frm.depAirport[i].value != frm.depAirport[i].defaultValue) ||
						(frm.arrAirport[i].defaultValue != "" && frm.arrAirport[i].value != frm.arrAirport[i].defaultValue)) {
					gwChanged = true;
				}
				if (frm.depAirport[i].value == "") errors += "Line "+(i+1)+": Departure airport is missing.\n";
				if (frm.arrAirport[i].value == "") errors += "Line "+(i+1)+": Arrival airport is missing.\n";
				if (frm.depTime[i].value != "") {
					if (validTime(frm.depTime[i].value) == "") {
            var slotNo = parseInt(frm.depTime[i].value);
            if (i > 0 || isNaN(slotNo) || trim(frm.depTime[i].value) != slotNo || slotNo < 1 || slotNo > 3) {
  						errors += "Line "+(i+1)+": Departure time is invalid.\n";
            }
					} else {
						frm.depTime[i].value = validTime(frm.depTime[i].value);
					}
				}
				if (frm.arrTime[i].value != "") {
					if (validTime(frm.arrTime[i].value) == "") {
						errors += "Line "+(i+1)+": Arrival time is invalid.\n";
					} else {
						frm.arrTime[i].value = validTime(frm.arrTime[i].value);
					}
				}
        if (i > 0 && validTime(frm.arrTime[i-1].value) != "" && validTime(frm.depTime[i].value) != "") {
          var dOffset = parseInt(frm.dayOffset[i].value);
          var isNextDay = !(isNaN(dOffset) || dOffset <= 0);
          if (timeToSeconds(validTime(frm.arrTime[i-1].value)) >= timeToSeconds(validTime(frm.depTime[i].value)) &&
              !isNextDay) {
            if (!confirm("There seem to be inconsistencies in the arrival/departure timings of slot "+i+" and "+(i+1)+". Continue anyway?")) {
              errors += ("Timing errors between slot "+i+" and "+(i+1)+"\n");
            }
          }
        }
			}
		}
		if (errors!="") {
			alert("Please correct the following errors"+(toSave?" before saving":"")+":\n\n"+errors);
			return false;
		}
		return true;
}


function markChanged() {
	var dateChanged =
		(
			(	frm.fromDate &&
			(	(frm.fromDate.type=='text' && frm.fromDate.value != frm.fromDate.defaultValue) ||
				(frm.fromDate.type=='select-one' && !frm.fromDate.options[frm.fromDate.selectedIndex].defaultSelected)
			)) ||
			(	frm.toDate &&
			(	(frm.toDate.type=='text' && frm.toDate.value != frm.toDate.defaultValue) ||
				(frm.toDate.type=='select-one' && !frm.toDate.options[frm.toDate.selectedIndex].defaultSelected)
			))
		);
	var cancelledFlightChanged = frm.cancelledFlight.checked != frm.cancelledFlight.defaultChecked;
	var numRows = frm.sectorNo?frm.sectorNo.length?frm.sectorNo.length:1:0;

	if (numRows == 1) {
		if (frm.sectorNo.value != frm.sectorNo.defaultValue ||
				frm.depAirport.value != frm.depAirport.defaultValue ||
				frm.arrAirport.value != frm.arrAirport.defaultValue ||
				frm.depTime.value != frm.depTime.defaultValue ||
				frm.arrTime.value != frm.arrTime.defaultValue ||
				frm.dayOffset.value != frm.dayOffset.defaultValue ||
				frm.flightPre.value != frm.flightPre.defaultValue ||
				frm.flightNo.value != frm.flightNo.defaultValue ||
				frm.aircraft.value != frm.aircraft.defaultValue ||
				frm.flightType.value != frm.flightType.defaultValue ||
				dateChanged || cancelledFlightChanged) {
			frm.rowChanged.value = '1';
			if ((frm.depAirport.defaultValue != "" && frm.depAirport.value != frm.depAirport.defaultValue) ||
					(frm.arrAirport.defaultValue != "" && frm.arrAirport.value != frm.arrAirport.defaultValue)) {
				frm.gwChanged.value = '1';
			} else {
				frm.gwChanged.value = '0';
			}
		} else {
			frm.rowChanged.value = '0';
			frm.gwChanged.value = '0';
		}
	} else {
		for (var i=0; i < numRows; i++) {
			if (frm.sectorNo[i].value != frm.sectorNo[i].defaultValue ||
					frm.depAirport[i].value != frm.depAirport[i].defaultValue ||
					frm.arrAirport[i].value != frm.arrAirport[i].defaultValue ||
					frm.depTime[i].value != frm.depTime[i].defaultValue ||
					frm.arrTime[i].value != frm.arrTime[i].defaultValue ||
					frm.dayOffset[i].value != frm.dayOffset[i].defaultValue ||
					frm.flightPre[i].value != frm.flightPre[i].defaultValue ||
					frm.flightNo[i].value != frm.flightNo[i].defaultValue ||
					frm.aircraft[i].value != frm.aircraft[i].defaultValue ||
					frm.flightType[i].value != frm.flightType[i].defaultValue ||
				    dateChanged || cancelledFlightChanged) {
				frm.rowChanged[i].value = '1';




				if ((frm.depAirport[i].defaultValue != "" && frm.depAirport[i].value != frm.depAirport[i].defaultValue) ||
						(frm.arrAirport[i].defaultValue != "" && frm.arrAirport[i].value != frm.arrAirport[i].defaultValue)) {
					frm.gwChanged[i].value = '1';
				} else {
					frm.gwChanged[i].value = '0';
				}
			} else {
				frm.rowChanged[i].value = '0';
				frm.gwChanged[i].value = '0';
			}
		}
	}
}


function doSubmit() {
	if (validate(true)) {
    frm.saveBtn.disabled = 'true';
		markChanged();
		frm.action.value='save';
		frm.submit();
	}
}


</script>
</head>

<body style="text-align: center;" onLoad="doOnLoad();">
<html:form action="flightopdetails.do">
<html:hidden name="flightopform" property="action" />
<html:hidden name="flightopform" property="startAction" />
<html:hidden name="flightopform" property="seriesNo" />
<html:hidden name="flightopform" property="group" /> 
<html:hidden name="flightopform" property="originalNumSectors" />
<input type="hidden" name="delFromDate" value="<%= fromDate %>" /> 
<input type="hidden" name="delToDate" value="<%= flightopform.getToDate() %>" />	 
<input type="hidden" name="deletedSequences" />
<h3>Add Operational Detail</h3>
<h6><jsp:getProperty name="flightopform" property="error" /></h6>
<table cellspacing="0">
	<tr>
		<td>
			<table cellspacing="0">
<% if (flightopform.isGroup()) { %>
				<tr>
					<th>Start Date</th>
					<th>
						<select name="fromDate" onChange="dateChange(true);">
							<option></option>
						</select>
					</th>
				</tr>
				<tr>
					<th>End Date</th>
					<th>
						<select name="toDate" onChange="dateChange(false);">
							<option></option>
						</select>
					</th>
				</tr>
<% } else {%>
	<% if (flightopform.getAction().equalsIgnoreCase("edit")) { %>
				<tr>
					<th>Prev. Date</th>
					<th>
						<input	name="fromDate"
										value="<%= flightopform.getFromDate() %>"
										readonly="true"
										size="13"
										maxlength="10" />
					</th>
				</tr>
	<% } %>
				<tr>
					<th>New Date</th>
					<th>
						<input	name="toDate"
										value="<%= flightopform.getAction().equalsIgnoreCase("edit")?flightopform.getFromDate():"" %>"
										ondblclick="show_calendar(frm.name+'.toDate', new String(strToDate('<%= flightopform.getFromDate() %>').getMonth()), new String(strToDate('<%= flightopform.getFromDate() %>').getFullYear()));"
										size="13"
										maxlength="10" />
					</th>
				</tr>
<% } %>
<!--
				<tr>
					<th>Weekday</th>
					<th>
						<viking:vikingSelector	selectionType="dowSelector"
																		value="<%= flightopform.getWeekday() %>"
																		onChange="doDowChange();"
																		name="weekday" />
					</th>
				</tr>
-->
			</table>
		</td>
		<td>
			<table cellspacing="0" id="sectortable">
				<tr>
					<th colspan="2">Sector</th>
					<th>From</th>
					<th>To</th>
					<th>Dep.</th>
					<th>Arr.</th>
					<th>+Day</th>
					<th colspan="2">Flight<br>Pre. Number</th>
					<th>Aircraft</th>
					<th>Flight<br/>Type</th>
					<th>Num<br/>Alloc</th>
					<th colspan="2"></th>
				</tr>
<%
	for (int i=0; i<numRows; i++) {
%>
				<tr>
					<td>
						<input	name="sectorNo"
										value="<%= i+1 %>"
										readonly="true"
										style="background-color: #f0f0f0;"
										size="3" />
					</td>
					<td>
						 <input	 type="hidden"
										name="sequenceNo"
										value="<%= flightopform.getSequenceNo(i) %>" />
						
					</td>
					<td>
						<input	name="depAirport"
										value="<%= flightopform.getDepAirport(i) %>"
										onChange="gwChange(this.parentElement.parentElement.rowIndex-1);"
										size="5"
										maxlength="3" />
					</td>
					<td>
						<input	name="arrAirport"
										value="<%= flightopform.getArrAirport(i) %>"
										onChange="gwChange(this.parentElement.parentElement.rowIndex-1);"
										size="5"
										maxlength="3" />
					</td>
					<td>
						<input	name="depTime"
										value="<%= flightopform.getDepTime(i)+flightopform.getDepSlot(i) %>" // only time OR slot should return something 
										size="7"
										maxlength="6" />
					</td>
					<td>
						<input	name="arrTime"
										value="<%= flightopform.getArrTime(i) %>"
										size="7"
										maxlength="6" />
					</td>
					<td>
						<input	name="dayOffset"
										value="<%= flightopform.getDayOffset(i) %>"
										size="3"
										maxlength="2" />
					</td>
					<td>
						<input	name="flightPre"
										value="<%= flightopform.getFlightPre(i) %>"
										size="4"
										maxlength="3"
										onChange="this.value=trim(this.value);"/>
										<!--  onChange="this.value=trim(this.svalue).toUpperCase();"/>  -->
					</td>
					<td>
						<input	name="flightNo" 
										value="<%= flightopform.getFlightNo(i) %>"
										size="6"
										maxlength="6"
								        onChange="this.value=trim(this.value);" />
										<!--  onChange="this.value=trim(this.value).toUpperCase();" />  -->
					</td>
					<td>
						<input	name="aircraft"
										value="<%= flightopform.getAircraft(i) %>"
										onChange="this.value=trim(this.value).toUpperCase();"  
										size="6"
										maxlength="4" />
					</td>
					<td>
                        <viking:vikingSelector	selectionType="flightTypeSelector"
																		value="<%= flightopform.getFlightType(i) %>"
																		name="flightType" />  
 
					<!-- 	<input	name="flightType"
										value="<%= flightopform.getFlightType(i) %>"
										onChange="this.value=trim(this.value).toUpperCase();"
										size="15"
										maxlength="10" />  -->
					</td>


	
					

					<td>
						<input type='button' class='rowBtn' onclick='insertRowAbove(this.parentElement.parentElement.rowIndex-1);' value='Insert sector above' />
					</td>
					<td>
						<input type='button' class='rowBtn' onclick='deleteRow(this.parentElement.parentElement.rowIndex-1);' value='Delete sector' />
					</td>
					<td>
						 <input type='hidden' name='rowChanged' value='0' /> 


					</td>
					<td>
						<input  type='hidden' name='gwChanged' value='0' />
					</td>
				</tr>
<%
	}
%>
			</table>
		</td>
	</tr>
	<tr>
		<td></td>
		<td style="text-align: right;">
			<input type='button' class='rowBtn' onclick='insertRowAbove(-1);' value='Append sector' />
		</td>
	<tr>
		<th style="text-align: left; padding-top: 4px; border-top: 1px solid black;">
			Comments
		</th>
		<th style="text-align: right; border-top: 1px solid black;">
			Amendment Type <viking:vikingSelector	selectionType="amendmentTypeSelector"
																						name="amendmentType" />
		</th>
	</tr>
	<tr>
		<td colspan="2">
			<html:textarea	name="flightopform"
											property="comments"
											rows="5"
											style="width: 100%" />
		</td>
	</tr>
</table>
<p/>
<html:checkbox	name="flightopform"
								property="cancelledFlight"
								style="border: 0px;"/> <b>Cancelled Flight</b><p/>
<button	accesskey="S"
        name="saveBtn"
				style="color: #008000;"
				onClick="doSubmit();"><u>S</u>ave</button>
<button	accesskey="C"
				style="color: #c00000;"
				onClick="window.close();"><u>C</u>ancel</button>
</html:form>
</body>
</html:html>
