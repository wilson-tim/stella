<%@ page errorPage="/ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/vikingtags.tld" prefix="viking" %>
<jsp:useBean	id="flightdetailform"
							class="uk.co.firstchoice.viking.gui.FlightDetailsFormBean"
							scope="request"/>
<html:html>
<head>
<title>Edit Series Detail</title>
<link href="<html:rewrite page="/css/viking.css"/>" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="<html:rewrite page="/table.js"/>"></script>
<style>
td {vertical-align: top; text-align: center;}
input {border: 1px solid black;}
table {border: 1px solid darkblue;}
.dspAirport {background-color: transparent; border: 0px; margin: 0px; padding: 0px; color: black; text-align: center;}
</style>

<script type="text/javascript" src="<html:rewrite page="/table.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/validation.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/utils.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/datepicker.js"/>"></script>
<script type="text/javascript">
var frm;
var seasonStartDate;
var seasonEndDate;
var fromDate = new Date(1997, 0, 1);

var airportCodes = '<viking:vikingSelector selectionType="listGatewayCodes" />';
var airportNames = [<viking:vikingSelector selectionType="listGatewayNames" />];

var versions = <viking:vikingSelector	selectionType="versionsArray" />



function doOnLoad() {
    
	frm=window.document.forms[0];
	window.moveTo(	parseInt((window.screen.availWidth-document.body.offsetWidth) / 2),
									parseInt((window.screen.availHeight-document.body.offsetHeight) / 2));
	if (frm.startDate.value=="" && frm.endDate.value=="") {
		doSeasonChange();
	}
	frm.season.focus();
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


function populateDateSelectors() {
	var dow = frm.weekday.options[frm.weekday.selectedIndex].value%7;
	buildDateSelector(frm.startDate, seasonStartDate, seasonEndDate, dow, 7);
	buildDateSelector(frm.endDate, seasonStartDate, seasonEndDate, dow, 7);
	frm.endDate.selectedIndex = frm.endDate.options.length-1;
}


function doSectorChange(nr) {
	var sectorField = eval("frm.sector"+nr);
	var aptField = eval("frm.apName"+nr);
	var apt = sectorField.value = trim(sectorField.value).toUpperCase();
	if (apt.length != 3)
		apt = "___";
	var idx = airportCodes.indexOf(apt)/4;
	aptField.value = idx<0?"":airportNames[idx];
}


function doSeasonChange() {
	var a = frm.season.value.split(";");
	seasonStartDate = a[1];
	seasonEndDate = a[2];
//	frm.startDate.value = a[1];
//	frm.endDate.value = a[2];
	frm.version.options.length = 0;
	for (var idx = 0; idx < versions.length; idx++) {
		if (versions[idx][0] == a[0]) {
			for (var x = 1; x < versions[idx].length; x++) {
				var arr = versions[idx][x].split(';');
				frm.version.options[frm.version.options.length] = new Option(arr[1], arr[0], (arr[2]=='1'), (arr[2]=='1'));
			}
			break;
		}
	}
	populateDateSelectors();
}


function validate() {
	var errors = "";
  var liveVersion = true;
	if (frm.version.selectedIndex < 0 || frm.version.options[frm.version.selectedIndex].value == "") {
		errors += "Version not selected\n";
  } else {
    var seas = frm.season.options[frm.season.selectedIndex].value.split(";")[0];
    var vers = frm.version.options[frm.version.selectedIndex].value;
  	for (var idx = 0; idx < versions.length; idx++) {
    	if (versions[idx][0] == seas) {
      	for (var x = 1; x < versions[idx].length; x++) {
        	var arr = versions[idx][x].split(';');
          if (arr[0] == vers) {
            liveVersion = arr[3] == "Y";
            break;
          }
        }
        break;
      }
    }
  }
	if (frm.weekday.selectedIndex < 1)
		errors += "Weekday not selected\n";
	var lastAirportFilled = 0;
  var apt;
	for (var i=1; i<7; i++) {
    apt = trim(eval("frm.sector"+i).value);
		if (apt == "") {
      for (var x=(i+1); x<7; x++) {
        eval("frm.sector"+x).value = "";
        eval("frm.apName"+x).value = "";
      }
			break;
		} else {
      if (apt.length != 3 || airportCodes.indexOf(apt) < 0) {
        errors += ("Airport #"+i+" is unknown\n");
      }
    }
		lastAirportFilled = i;
	}
	if (lastAirportFilled < 2)
		errors += "At least two airports must be entered\n";
  if (liveVersion) {
  	if (frm.amendmentType.selectedIndex < 1) errors += "Amendment type not selected\n";
  	if (trim(frm.comments.value) == "")
  		errors += "Comment must be entered\n";
  }
	if (errors == "") {
		return true;
	} else {
		alert("Please correct the following errors before saving:\n\n"+errors);
		return false;
	}
}

function doSubmit() {
	if (validate()) {
    frm.saveBtn.disabled = 'true';
		frm.submit();
  }
}

</script>
<html:base/>
</head>


<body style="text-align: center;" onload="doOnLoad();">
<html:form action="flightdetails.do">
<input type="hidden" name="action" value="save" />
<h3>Edit Series Details</h3>
<h6><jsp:getProperty name="flightdetailform" property="error" /></h6>
<table cellspacing="1" cellpadding="0">
	<tr>
		<td valign="left">
			<table cellspacing="0" style="border: 0px; width: 100%;">
				<tr>
					<th>Series No.</th>
					<th>Season</th>
					<th>Version</th>
					<th>Weekday</th>
					<th>Start Date</th>
					<th>End Date</th>
					<th>Frequency</th>
				</tr>
				<tr>
					<td>
						<html:text	name="flightdetailform"
												property="series"
												readonly="true"
												size="10" />
					</td>
					<td>
						<viking:vikingSelector	selectionType="seasonSelector"
																		value="<%= flightdetailform.getSeason() %>"
																		name="season"
																		onChange="doSeasonChange();" />
					</td>
					<td>
						<html:select name="flightdetailform" property="version">
							<html:option value=""></html:option>
						</html:select>
					</td>
					<td>
						<viking:vikingSelector	selectionType="dowSelector"
																		value="<%= flightdetailform.getWeekday() %>"
																		onChange="populateDateSelectors();"
																		name="weekday" />
					</td>
					<td>
						<html:select	name="flightdetailform"
													property="startDate" >
							<html:option value="" ></html:option>
						</html:select>
<!--
						<html:text	name="flightdetailform"
												property="startDate"
												size="12"
												maxlength="10"
												ondblclick="show_calendar(frm.name+'.startDate');"
												onblur="validDateField(this, true, fromDate); return false;" />
-->
					</td>
					<td>
						<html:select	name="flightdetailform"
													property="endDate" >
							<html:option value="" />
						</html:select>
<!--
						<html:text	name="flightdetailform"
												property="endDate"
												size="12"
												maxlength="10"
												ondblclick="show_calendar(frm.name+'.endDate');"
												onblur="validDateField(this, true, fromDate); return false;" />
-->
					</td>
					<td>
						<html:select name="flightdetailform" property="frequency">
							<html:option value="W">Weekly</html:option>
							<html:option value="F">Fortnightly</html:option>
							<html:option value="O">Other</html:option>
						</html:select>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td style="text-align: left;">
			<table cellspacing="2" style="border: 0px;">
				<tr>
					<th>Airport 1 -></th>
					<th>Airport 2 -></th>
					<th>Airport 3 -></th>
					<th>Airport 4 -></th>
					<th>Airport 5 -></th>
					<th>Airport 6</th>
					<th>Slot</th>
					<th>Est. Capa.</th>
				</tr>
				<tr>
					<td>
						<html:text	name="flightdetailform"
												property="sector1"
												onchange="doSectorChange(1);"
												size="5"
												maxlength="3" /><br/>
						<input name="apName1" class="dspAirport" readonly="true" size="15" tabindex="-1" />
					</td>
					<td>
						<html:text	name="flightdetailform"
												property="sector2"
												onchange="doSectorChange(2);"
												size="5"
												maxlength="3" /><br/>
						<input name="apName2" class="dspAirport" readonly="true" size="15" tabindex="-1" />
					</td>
					<td>
						<html:text	name="flightdetailform"
												property="sector3"
												onchange="doSectorChange(3);"
												size="5"
												maxlength="3" /><br/>
						<input name="apName3" class="dspAirport" readonly="true" size="15" tabindex="-1" />
					</td>
					<td>
						<html:text	name="flightdetailform"
												property="sector4"
												onchange="doSectorChange(4);"
												size="5"
												maxlength="3" /><br/>
						<input name="apName4" class="dspAirport" readonly="true" size="15" tabindex="-1" />
					</td>
					<td>
						<html:text	name="flightdetailform"
												property="sector5"
												onchange="doSectorChange(5);"
												size="5"
												maxlength="3" /><br/>
						<input name="apName5" class="dspAirport" readonly="true" size="15" tabindex="-1" />
					</td>
					<td>
						<html:text	name="flightdetailform"
												property="sector6"
												onchange="doSectorChange(6);"
												size="5"
												maxlength="3" /><br/>
						<input name="apName6" class="dspAirport" readonly="true" size="15" tabindex="-1" />
					</td>
					<td>
						<html:text	name="flightdetailform"
												property="slot"
												onchange="this.value=trim(this.value).toUpperCase();"
												size="5"
												maxlength="3" />
					</td>
					<td>
						<html:text	name="flightdetailform"
												property="initialCapacity"
												onchange="this.value=trim(this.value).toUpperCase();"
												size="5"
												maxlength="3" />
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td valign="left">
			<table cellspacing="0" style="border: 0px; width: 100%;">
				<tr>
					<th style="text-align: left;">
						Comments
					</th>
					<th style="text-align: right;">
						Amendment Type <viking:vikingSelector	selectionType="amendmentTypeSelector"
																									name="amendmentType" />
					</th>
				</tr>
				<tr>
					<td colspan="2">
						<html:textarea	name="flightdetailform"
														property="comments"
														rows="5"
														style="width: 100%;" />
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<p/>
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
