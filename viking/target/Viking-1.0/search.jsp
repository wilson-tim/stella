<%@ page errorPage="/ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/vikingtags.tld" prefix="viking" %>
<jsp:useBean	id="searchform"
							class="uk.co.firstchoice.viking.gui.SearchFormBean"
							scope="session"/>
<html:html>
<head>
<meta http-equiv="Expires" content="Mon, 24 Jun 1996, 00:00:00 GMT">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>Search</title>
<link href="<html:rewrite page="/css/viking.css"/>" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="<html:rewrite page="/table.js"/>"></script>

<script type="text/javascript" src="<html:rewrite page="/validation.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/utils.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/datepicker.js"/>"></script>

<script>
var frm;
var screenHeight = window.screen.availHeight;
var screenWidth = window.screen.availWidth;

var blanks = ' ';
var airportCodes = '<viking:vikingSelector selectionType="listGatewayCodes" />';
var airportNames = [<viking:vikingSelector selectionType="listGatewayNames" />];
var airlineCodes = '<viking:vikingSelector selectionType="listFlightCodes" />';
var airlineNames = [<viking:vikingSelector selectionType="listFlightNames" />];

var versions = <viking:vikingSelector	selectionType="versionsArray" />;

function doSeasonChange() {
	var a = frm.season.value.split(";");
	frm.version.options.length = 0;
	var cnt = 0;
	for (var idx = 0; idx < versions.length; idx++) {
		if (versions[idx][0] == a[0]) {
			for (var x = 1; x < versions[idx].length; x++) {
				var arr = versions[idx][x].split(';');
				frm.version.options[frm.version.options.length] = new Option(arr[1], arr[0], (arr[2]=='1'), (arr[2]=='1'));
                                <% if (searchform.getVersion().equals("")) { %>
                                    if (arr[2] == 'Y') {
                                        frm.version.selectedIndex = cnt;
                                    }    				
                               <% } %>     
                                cnt++;
			}
			break;
		}
	}
	<% if (!(searchform.getVersion().equals(""))) { %>
            for (var idx = 0; idx < frm.version.options.length; idx++) {
                    if (frm.version.options[idx].value == '<%= searchform.getVersion() %>') {
                            frm.version.selectedIndex = idx;
                    }
            }
	<% } %>
}



function showAirportName(pre, apt) {
	var field = eval("frm.dsp"+pre+"Airport");
	if (apt.length != 3)
		apt = "___";
	var idx = airportCodes.indexOf(apt)/4;
	field.value = idx<0?"":airportNames[idx];
}


function showAirlineName(aCode) {
	var field = frm.dspAirline;
	var span = document.getElementById('airlineSpan');
	aCode=(aCode+'    ').substring(0,3);
	var idx = airlineCodes.indexOf(aCode)/4;
	field.value = idx<0?"":airlineNames[idx];
}


function validate(isSeries) {
	var error = "";
	if (isSeries) {
	} else {
		if (frm.customer.selectedIndex == 0 && frm.supplier.selectedIndex == 0)
			error += "Either Customer or Supplier must be selected for Allocation search.\n";
	}
	if (error == "") {
		return true;
	} else {
		alert("Please correct the following errors:\n\n"+error);
		return false;
	}
}


function doReset() {
	for (var i = 0; i < frm.elements.length; i++) {
		var e = frm.elements[i];
		if (e.type == "text") {
			e.value = "";
		} else if (e.type == "select-one" && e.name != "season") {
      e.selectedIndex = 0;
    }
	}
	doSeasonChange();
  frm.includeAllocation.checked = true;
}


function doSearch(isSeries) {
	if (validate(isSeries)) {
		window.document.all["seriesBtn"].disabled = "true";
		window.document.all["customerBtn"].disabled = "true";
		window.document.all["resetBtn"].disabled = "true";
		window.document.all["cancelBtn"].disabled = "true";
		window.document.all["seriesBtn"].style.cursor = "wait";
		window.document.all["customerBtn"].style.cursor = "wait";
		window.document.all["resetBtn"].style.cursor = "wait";
		window.document.all["cancelBtn"].style.cursor = "wait";
		window.document.all["seriesBtn"].style.color = "gray";
		window.document.all["customerBtn"].style.color = "gray";
		window.document.all["resetBtn"].style.color = "gray";
		window.document.all["cancelBtn"].style.color = "gray";
		window.document.all["theBody"].style.cursor = "wait";
		var searchingStyle = window.document.all["searchingId"].style;
		searchingStyle.top = document.body.offsetHeight / 2;
		searchingStyle.width = 100;
		searchingStyle.left = (document.body.offsetWidth - 100) / 2;
		searchingStyle.display = "block";
		frm.searchType.value = isSeries?"S":"C";
                frm.action.value = "search";
		frm.submit();
	}
}


function doOnLoad() {
	frm=document.forms[0];
  frm.includeAllocation.checked = true;
	doSeasonChange();
	showAirportName('Dep',frm.depAirport.value);
	showAirportName('Arr',frm.arrAirport.value);
	showAirlineName(frm.flightPre.value);
	window.document.all["seriesBtn"].disabled = false;
	window.document.all["customerBtn"].disabled = false;
	window.document.all["resetBtn"].disabled = false;
	window.document.all["cancelBtn"].disabled = false;
	window.document.all["seriesBtn"].style.color = "#008000";
	window.document.all["customerBtn"].style.color = "#008000";
	window.document.all["resetBtn"].style.color = "black";
	window.document.all["cancelBtn"].style.color = "black";
}
</script>

<style>
body { text-align: center; }
button { width: 1.5cm; padding: 0px; margin: 2px; }
table {border: 1px solid darkblue; }
td { text-align: center; vertical-align: top; }
h3 { margin: 3px; }
.nm { margin: 0px; }
.datatable th { text-align: center; vertical-align: middle; color: darkBlue; padding: 1px;}
.datatable td { text-align: center; }
.monospace { font-family: courier; font-size: 7pt; font-weight: normal; }
.nbl { border-left: 0px; }
.nbr { border-right: 0px; }
.nblr { border-left: 0px; border-right: 0px; }
.dspAirport {background-color: transparent; border: 0px; margin: 0px; padding: 0px; color: white; text-align: center; font-size: smaller;}
@media screen {
  .noScreen { display: none; }
  .A { background-color: lightgreen; color: black; }  <!-- Agreed -->
  .F { color: black; }                                <!-- Fixed -->
  .H { background-color: mistyrose; color: black; }   <!-- Historic -->
  .N { background-color: lightblue; color: black; }   <!-- New -->
  .Z { color: black; }                                <!-- null -->
}
</style>
</head>
<body onLoad="doOnLoad();" id="theBody">
<html:form action="search.do">
<input type="hidden" name="action" value="reload" />
<html:hidden property="searchType" name="searchform" />
<h6 id="searchingId" style="display:none; position:absolute;">Searching</h6>
<h3>Search</h3>
<table cellspacing="0" cellpadding="0" class="innerTabTable" border="1" style="width: 100%; border: 0px; margin: 0px;">
	<tr>
		<th>Series #</th>
		<th>Season</th>
		<th>Version</th>
		<th>Start Date</th>
		<th>End Date</th>
		<th colspan="2">Departure Time</th>
		<th colspan="2">Arrival Time</th>
		<th>Weekday</th>
		<th>Airport 1</th>
		<th>Airport 2</th>
		<th> Selling Code</th>
		<th>Flight Type</th>
		<th>Meal Type</th>

	</tr>
	<tr>
		<td>
			<html:text	property="seriesNo"
									name="searchform"
									size="6"
									maxlength="5" />
		</td>
		
<% if ( request.isUserInRole("VIKING_RESTRICTED_EXTERNAL_READER")) {  %>
      <td>
			<viking:vikingSelector	selectionType="restrictedseasonSelector"
															name="season"
															value="<%= searchform.getSeason() %>"
															onChange="doSeasonChange();" />
		</td>
<% }else { %>
        <td>
			<viking:vikingSelector	selectionType="seasonSelector"
															name="season"
															value="<%= searchform.getSeason() %>"
															onChange="doSeasonChange();" />
		</td>
<% } %>
		<td>
			<html:select name="searchform" property="version">
				<html:option value=""></html:option>
			</html:select>
		</td>
		<td>
			<html:text	property="startDate"
									name="searchform"
									size="12"
									maxlength="10"
									ondblclick="show_calendar(frm.name+'.startDate');"/>
		</td>
		<td>
			<html:text	property="endDate"
									name="searchform"
									size="12"
									maxlength="10"
									ondblclick="show_calendar(frm.name+'.endDate');"/>
		</td>
		<td class="nbr">
			<html:text	property="depTime"
									name="searchform"
									size="7"
									maxlength="5"
                  onchange="this.value=validTime(this.value)" />
		</td>
		<td class="nbl">
			<html:select name="searchform" property="depTimeOper">
				<html:option value="lt"><=</html:option>
				<html:option value="eq">=</html:option>
				<html:option value="gt">>=</html:option>
			</html:select>
		</td>
		<td class="nbr">
			<html:text	property="arrTime"
									name="searchform"
									size="7"
									maxlength="5"
                  onchange="this.value=validTime(this.value)" />
		</td>
		<td class="nbl">
			<html:select name="searchform" property="arrTimeOper">
				<html:option value="lt"><=</html:option>
				<html:option value="eq">=</html:option>
				<html:option value="gt">>=</html:option>
			</html:select>
		</td>
		<td>
			<viking:vikingSelector	selectionType="dowSelector"
															value="<%= searchform.getWeekday() %>"
															name="weekday" />
		</td>
		<td>
			<html:text	property="depAirport"
									name="searchform"
									size="5"
									maxlength="3"
									onchange="this.value=trim(this.value).toUpperCase(); showAirportName('Dep',this.value);" /><br/>
			<input name="dspDepAirport" class="dspAirport" readonly="true" size="15" tabindex="-1" />
		</td>
		<td>
			<html:text	property="arrAirport"
									name="searchform"
									size="5"
									maxlength="3"
									onchange="this.value=trim(this.value).toUpperCase(); showAirportName('Arr',this.value);" /><br/>
			<input name="dspArrAirport" class="dspAirport" readonly="true" size="15" tabindex="-1" />
		</td>
		<td>
			<html:text	property="geminiCode"
									name="searchform"
									size="5"
									maxlength="15" 
									onblur="this.value=trim(this.value).toUpperCase();"/>
		</td>
		
		<td>
			<viking:vikingSelector	selectionType="flightTypeSelector"
															name="flightType"
															value="<%= searchform.getFlightType() %>" />


		</td>
		<td>
			<viking:vikingSelector	selectionType="mealTypeSelector"
															name="mealType"
															value="<%= searchform.getMealType() %>"	 />
		</td>

	</tr>
</table>
<table cellspacing="0" cellpadding="0" class="innerTabTable" border="1" style="width: 100%; border: 0px; margin: 0px;">
	<tr>
		<th>Flight Pre./No</th>
		<th>A/C Type</th>
		<th>Customer</th>
		<th>Supplier</th>
		<th>Broker</th>
		<th>Class</th>
		<th>Contract Id</th>
<!--	Not in use
        <th colspan="2">Total Price</th> -->
		<th colspan="3">Seat Qty</th>
<!--		<th>Bus. Area</th> -->
		<th>Status</th>
    <th>Incl. Alloc.</th>
		<td rowspan="1" style="border: 0px; vertical-align: middle;">
			<button	id="seriesBtn"
							accesskey="S"
							disabled="true"
							style="color: #008000;"
							onclick="doSearch(true);"><u>S</u>eries</button>
			<button	id="customerBtn"
							accesskey="A"
							disabled="true"
							style="color: #008000;"
							onclick="doSearch(false);"><u>A</u>lloc.</button>
		</td>
	</tr>
	<tr>
		<th nowrap="true" style="vertical-align: top; font-size: 7pt; width: 80pt;">
			<html:text	property="flightPre"
									name="searchform"
									size="5"
									maxlength="3" 
									onblur="this.value=trim(this.value); showAirlineName(this.value);"/> 
									<!--   onblur="this.value=trim(this.value).toUpperCase(); showAirlineName(this.value);"/>   -->
			<html:text	property="flightNo"
									name="searchform"
									size="6"
									maxlength="6" 
									onblur="this.value=trim(this.value).toUpperCase();"/><br/>
									<!--  onblur="this.value=trim(this.value).toUpperCase();"/><br/>  -->
			<input name="dspAirline" class="dspAirport" readonly="true" size="15" tabindex="-1" />
		</th>
		<td>
			<html:text	property="aircraftType"
									name="searchform"
									size="7" />
		</td>

		<td>
			<html:select name="searchform" property="customer">
				<html:option value=""></html:option>
				<html:option value="NONE">-NONE-</html:option>
				<html:options	labelName="allAccounts"
											labelProperty="sortedAccountCodes"
											name="allAccounts"
											property="sortedAccountCodes" />
			</html:select>
		</td>
		<td>
			<html:select name="searchform" property="supplier">
				<html:option value=""></html:option>
				<html:option value="DUMMY">-BLANK-</html:option>
				<html:option value="NONE">-NONE-</html:option>
				<html:options	labelName="allAccounts"
											labelProperty="sortedAccountCodes"
											name="allAccounts"
											property="sortedAccountCodes" />
			</html:select>
		</td>
		<td>
			<html:select name="searchform" property="broker">
				<html:option value=""></html:option>
				<html:options	labelName="allAccounts"
											labelProperty="sortedAccountCodes"
											name="allAccounts"
											property="sortedAccountCodes" />
			</html:select>
		</td>
		<td>
			<viking:vikingSelector	selectionType="seatClassSelector"
															value="<%= searchform.getSeatClass() %>"
															name="seatClass" />
		</td>
		<td>
			<html:text	property="contractId"
									name="searchform"
									size="10"
									onchange="this.value=trim(this.value).toUpperCase();" />
		</td>
		<!--<td class="nbr">
			<html:text	property="totalPrice"
									name="searchform"
									size="7"
									maxlength="5" />
		<td class="nbl">
			<html:select name="searchform" property="totalPriceOper">
				<html:option value="lt"><=</html:option>
				<html:option value="eq">=</html:option>
				<html:option value="gt">>=</html:option>
			</html:select>
		</td>-->
		<td class="nbr">
			<html:select name="searchform" property="seatQtyType">
				<html:option value="avail">Avail</html:option>
				<html:option value="sold">Sold</html:option>
				<html:option value="purchased">Purch.</html:option>
			</html:select>
		</td>
		<td class="nblr">
			<html:select name="searchform" property="seatQtyOper">
				<html:option value="lt"><=</html:option>
				<html:option value="eq">=</html:option>
				<html:option value="gt">>=</html:option>
			</html:select>
		</td>
		<td class="nbl">
			<html:text	property="seatQty"
									name="searchform"
									size="5"
									maxlength="3" />
		</td>
<!--		<td> -->
			<html:hidden	property="businessArea"
									name="searchform" />
<!--
									size="7"
									maxlength="5" 
									onblur="this.value=trim(this.value).toUpperCase();"
  	</td>
-->
		<td>
			<html:select name="searchform" property="status">
				<html:option value="" />
				<html:options	labelName="sessiondata"
											labelProperty="statusCodes"
											name="sessiondata"
											property="statusCodes" />
			</html:select>
		</td>
    <td>
      <html:checkbox name="searchform" property="includeAllocation" />
    </td>
		<td rowspan="1" style="border: 0px; vertical-align: middle;">
			<button	id="resetBtn"
							disabled="true"
							accesskey="R"
							onclick="doReset();"><u>R</u>eset</button>
			<button	id="cancelBtn"
							disabled="true"
							accesskey="C"
							onclick="location.href='menu.do';"><u>C</u>lose</button>
		</td>
	</tr>
</table>
<% if (searchform.getSearchResult() != null) { %>
<span class="N">New</span> -
<span class="H">Historic</span> -
<span class="A">Agreed</span> -
<span class="F">Fixed</span>
<iframe class="nm"
				name="searchResult"
			<% if (searchform.getSearchType().equals("S")) { %>
				src="<html:rewrite page="/seriesSearchResultInc.jsp"/>";
			<% } else { %>
				src="<html:rewrite page="/allocationSearchResultInc.jsp"/>";
			<% } %>
				width="100%"
				height="80%"
				frameborder="0" marginheight="0" marginwidth="0" />
<% } %>
</html:form>
</body>
</html:html>