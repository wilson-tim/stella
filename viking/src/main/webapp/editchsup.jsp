<%@ page errorPage="/ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/vikingtags.tld" prefix="viking" %>
<jsp:useBean	id="sellbuyform"
							class="uk.co.firstchoice.viking.gui.SellBuyFormBean"
							scope="request"/>
<html:html>
<% int numRows = (sellbuyform.getCurrency()==null?0:sellbuyform.getCurrency().length); %>
<head>
<title><jsp:getProperty name="sellbuyform" property="editType" /> <bean:write name="sellbuyform" property="type" /></title>
<link href="<html:rewrite page="/css/viking.css"/>" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="<html:rewrite page="/table.js"/>"></script>

<script type="text/javascript" src="<html:rewrite page="/validation.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/utils.js"/>"></script>
<style>
td {vertical-align: top; text-align: center;}
table {border: 1px solid darkblue;}
input {border: 1px solid black;}
select {border: 1px solid black;}
</style>
<script>
var frm;

var liveVersion = <%= sellbuyform.isLiveVersion() %>;

function appendRow() { // add a row to the table of sectors 
	var rowNum = frm.price.length?frm.price.length:2;
	if (rowNum < 5) {
		if (validate(false)) {
			var e0 = document.createElement("<input type='hidden' name='oldcurrency' value='' />");
			var e1 = document.createElement("<select name='currency' onchange='newExchangeRate();' />");
			var e2 = document.createElement("<input	name='exchangeRate'	style='text-align: right;' onchange='if (validAmount(this, 0.0)) this.value=floatToString(parseFloat(this.value), 4);'	size='8' maxlength='8' />");
			var e3 = document.createElement("<input	name='price' style='text-align: right;' onchange='if (validAmount(this, 0.0)) calcTotal();' size='10' maxlength='8' />");
			var e4 = document.createElement("<input style='text-align: right; background-color: #eeeeee;' name='total' disabled size='13'>");
			var e5 = document.createElement("<input type='button' class='rowBtn' onclick='deleteRow(this.parentElement.parentElement.rowIndex-1);' value='Del' />");
<% if (sellbuyform.getType().equalsIgnoreCase("Customer")) { %>
			addTableRow("sbTable", "", "", "", "", "", "", "", "", e0, e1, e2, e3, e4, e5);
<% } else { %>
			addTableRow("sbTable", "", "", "", "", "", "", "", e0, e1, e2, e3, e4, e5);
<% } %>
			var newSel = frm.currency[frm.currency.length-1];
			for (var i=0; i < frm.currency[0].options.length; i++) {
				newSel.options[newSel.options.length] = new Option(frm.currency[0].options[i].text, frm.currency[0].options[i].value, frm.currency[0].options[i].text=='GBP', frm.currency[0].options[i].text=='GBP');
			}
		}
	} else {
		alert("Max 5 different currencies allowed");
	}
}


function deleteRow(lineNo) {
	var lastRow = frm.price.length?frm.price.length:1;
	if (frm.price.length) {
		for (var i=lineNo; i < frm.price.length-1; i++) {
			frm.currency[i].selectedIndex = frm.currency[i+1].selectedIndex;
			frm.exchangeRate[i].value = frm.exchangeRate[i+1].value;
			frm.price[i].value = frm.price[i+1].value;
			frm.total[i].value = frm.total[i+1].value;
		}
	}
	deleteTableRow("sbTable", lastRow);
}



function newExchangeRate() {
	if (frm.currency.length && frm.currency[0].length) {
		for (var i=0; i < frm.currency.length; i++) {
			var er = frm.currency[i].options[frm.currency[i].selectedIndex].value.split(';');
			frm.exchangeRate[i].value = er[1];
		}
	} else {
		var er = frm.currency.options[frm.currency.selectedIndex].value.split(';');
		frm.exchangeRate.value = er[1];
	}
}


function dateChange(from) {
	if (from) {
		if (frm.startDate.selectedIndex > frm.endDate.selectedIndex) {
			frm.endDate.selectedIndex = 0;
		}
	} else {
		if (frm.startDate.selectedIndex > frm.endDate.selectedIndex) {
			frm.startDate.selectedIndex = 0;
		}
	}
}


function calcTotal() {
	var qty = frm.seatQty.value;
	if (qty != "") {
		var qtyInt = parseInt(qty, 10);
		if (!isNaN(qtyInt)) {
			if (frm.price.length) {
				for (var i=0; i < frm.price.length; i++) {
					if (frm.price[i].value != "") {
						var price = parseFloat(frm.price[i].value);
						if (!isNaN(price)) {
							frm.price[i].value = floatToString(price);
							frm.total[i].value = floatToString(price*qtyInt);
						}
					}
				}
			} else {
				if (frm.price.value != "") {
					var price = parseFloat(frm.price.value);
					if (!isNaN(price)) {
						frm.price.value = floatToString(price);
						frm.total.value = floatToString(price*qtyInt);
					}
				}
			}
		}
	}
}


function loadDates() {
	var stDate = '<%= sellbuyform.getStartDate()==null||sellbuyform.getStartDate().length==0?"":sellbuyform.getStartDate()[0] %>';
	var enDate = '<%= sellbuyform.getEndDate()==null||sellbuyform.getEndDate().length==0?"":sellbuyform.getEndDate()[sellbuyform.getEndDate().length-1] %>';
	var dateArr = opener.allDates;
	for (var i=0; i<dateArr.length; i++) {
		if ('<bean:write name="sellbuyform" property="editType" />' != 'edit' ||
				dateBetween(dateArr[i], stDate, enDate)) {
			frm.startDate.options[frm.startDate.options.length] = new Option(dateArr[i], dateArr[i]);
			frm.endDate.options[frm.endDate.options.length] = new Option(dateArr[i], dateArr[i]);
		}
		if (dateArr[i] == stDate) frm.startDate.selectedIndex = frm.startDate.options.length-1;
		if (dateArr[i] == enDate) frm.endDate.selectedIndex = frm.endDate.options.length-1;
	}
	if (enDate == '') frm.endDate.selectedIndex = frm.endDate.options.length-1;
}


function doChangeCS() {
	frm.action.value="newsupplyer";
	frm.submit();
}


function validate(toSave) { //to save or to add new line
	var errors = "";
	if (toSave) {
		if (frm.sectors.selectedIndex < 0) errors += "No sectors selected\n";
<% if ("true".equalsIgnoreCase(sellbuyform.getSingle())) { %>
		if (frm.startDate.selectedIndex < 0) errors += "No dates selected\n";
<% } else { %>
		if (frm.startDate.selectedIndex > frm.endDate.selectedIndex) errors += "Start date cannot be after end date\n";
<% } %>
		if (frm.seatClass.options && frm.seatClass.options[frm.seatClass.selectedIndex].value == "") errors += "No seat class selected\n";
    if (liveVersion) {
  		if (frm.amendmentType.selectedIndex < 1) errors += "Amendment type not selected\n";
    	if (trim(frm.comments.value) == "") errors += "Comment must be entered\n";
    }
	}
	if (frm.currency.length && frm.currency[0].length) {
		for (var i=0; i < frm.currency.length; i++) {
			for (var j=i+1; j < frm.currency.length; j++) {
				if (frm.currency[i].options[frm.currency[i].selectedIndex].value ==
						frm.currency[j].options[frm.currency[j].selectedIndex].value) {
					errors += "There can only be one of each currency (Line "+(i+1)+" and "+(j+1)+" are the same)\n";
				}
			}
		}
	}
	if (frm.exchangeRate.length) {
		for (var i=0; i < frm.exchangeRate.length; i++) {
			if (frm.exchangeRate[i].value == "") errors += "Line "+i+": Exchange Rate missing\n";
		}
	} else {
		if (frm.exchangeRate.value == "") errors += "Exchange Rate missing\n";
	}


  	if (errors != "") {
		alert("Please correct the following error before saving:\n\n"+errors);
		return false;
	} else {
		return true;
	}
}


function doSubmit() {
   	if (validate(true)) {
    frm.saveBtn.disabled = 'true';
		frm.action.value = "save";
		frm.submit();
	}
}


function doOnLoad() {
	frm=window.document.forms[0];
	window.moveTo(	parseInt((window.screen.availWidth-document.body.offsetWidth) / 2),
									parseInt((window.screen.availHeight-document.body.offsetHeight) / 2));
	calcTotal();
<% if (!"true".equalsIgnoreCase(sellbuyform.getSingle())) { %>
	loadDates();
<% } %>
}


</script>
</head>

<body style="text-align: center;" onLoad="doOnLoad();">
<html:form action="flightsellbuy.do">
<html:hidden name="sellbuyform" property="seriesNo" />
<html:hidden name="sellbuyform" property="action" />
<html:hidden name="sellbuyform" property="type" />
<html:hidden name="sellbuyform" property="single" />

<input type="hidden" name="delstartDate" value="<%= sellbuyform.getStartDate()==null || sellbuyform.getStartDate().length == 0?"":sellbuyform.getStartDate()[0] %>" >
<input type="hidden" name="delendDate" value="<%= sellbuyform.getEndDate()==null || sellbuyform.getEndDate().length == 0?"":sellbuyform.getEndDate()[0] %>" >


<!--<html:hidden name="sellbuyform" property="csCode" />-->
<html:hidden name="sellbuyform" property="editType" /> <!-- edit or add -->
<h3><jsp:getProperty name="sellbuyform" property="editType" /> <bean:write name="sellbuyform" property="type" /></h3>
<h6><jsp:getProperty name="sellbuyform" property="error" /></h6>
<table cellpadding="0" cellspacing="0">
	<tr>
		<td colspan="2">
			<table id="sbTable" callspacing="0" cellpadding="3" style="border: 0px; margin: 0px;">
				<tr>
					<th>Sectors</th>
			<% if ("true".equalsIgnoreCase(sellbuyform.getSingle())) { %>
          <th>Dates</th>
      <% } else { %>
					<th>Start Date</th>
					<th>End Date</th>
      <% } %>
					<th>Class</th>
					<th><bean:write name="sellbuyform" property="type" /></th>
			<% if (sellbuyform.getType().equalsIgnoreCase("customer")) { %>
					<th>Selling<br/> Code</th>
			<% } %>
                    <th>Meal Type</th>
					<th>Contract ID</th>
					<th>Status</th>
					<th colspan="2">Seat<br/>Qty.</th>
					<th><a href="javascript:appendRow();">(Add)</a><br/>Currency</th>
					<th>Ex. Rate</th>
					<th>Price (ea.)</th>
					<th>Total</th>
					<th>Broker</th>
				</tr>
			<%
				for (int i=0; i<numRows; i++) {
			%>
				<tr>
<% if (i==0) { %>
					<td rowspan="10">
						<html:select name="sellbuyform" property="sectors" multiple="true" size="5">
							<html:options	labelName="sellbuyform"
														labelProperty="availSectors"
														name="sellbuyform"
														property="availSectors" />
						</html:select>
					</td>
<% } %>
<% if ("true".equalsIgnoreCase(sellbuyform.getSingle())) { %>
  <% if (i==0) { %>
          <td rowspan="10">
						<html:select	name="sellbuyform"
                          multiple="true" size="5"
													property="startDate">
							<html:options	labelName="sellbuyform"
														labelProperty="startDate"
														name="sellbuyform"
														property="startDate" />
						</html:select>
          </td>
  <% } %>
<% } else { %>
					<td>
  <% if (i==0) { %>
						<html:select	name="sellbuyform"
													property="startDate"
													onchange="dateChange(true);">
						</html:select>
  <% } %>
					</td>
					<td>
  <% if (i==0) { %>
						<html:select	name="sellbuyform"
													property="endDate"
													onchange="dateChange(true);">
						</html:select>
  <% } %>
					</td>
<% } %>
					<td>
<% if (i==0) { %>
	<% if (sellbuyform.getEditType().equalsIgnoreCase("edit")) { %>
						<html:text	name="sellbuyform"
												property="seatClass"
												size="3"
												readonly="true" />
	<% } else { %>
						<viking:vikingSelector	selectionType="seatClassSelector"
																		value="<%= sellbuyform.getSeatClass() %>"
																		name="seatClass" />
<!--
			<html:text	name="sellbuyform"
									property="seatClass"
									size="3"
									maxlength="2"
									onchange="this.value=trim(this.value).toUpperCase();" />
-->
	<% } %>
<% } %>
					</td>
					<td>
<% if (i==0) { %>
	<% if (sellbuyform.getEditType().equalsIgnoreCase("edit")) { %>
						<html:text	name="sellbuyform"
												property="custSupp"
												size="8"
												maxlength="6"
												readonly="true" />
	<% } else { %>
						<html:select name="sellbuyform" property="custSupp" onchange="doChangeCS();">
							<html:options	labelName="allAccounts"
														labelProperty="sortedAccountCodes"
														name="allAccounts"
														property="sortedAccountCodes" />
						</html:select>
	<% } %>
<% } %>
					</td>
<% if (sellbuyform.getType().equalsIgnoreCase("customer")) { %>
					<td>
	<% if (i==0) { %>
						<html:text	name="sellbuyform"
												property="geminiCode"
												size="8"
												maxlength="6"
												onchange="this.value=trim(this.value).toUpperCase();" />
			
	<% } %>
					</td>
<% } %>


					<td>
<% if (i==0) { %>
						<html:select name="sellbuyform" property="mealType">
							<html:options	labelName="sessiondata"
														labelProperty="mealTypes"
														name="sessiondata"
														property="mealTypes" />
						</html:select>
<% } %>
					</td>

					<td>
<% if (i==0) { %>
						<html:select name="sellbuyform" property="contractId">
							<html:option value="" />
							<html:options	labelName="sellbuyform"
														labelProperty="contractCodes"
														name="sellbuyform"
														property="contractCodes" />
						</html:select>
<% } %>
					</td>

					<td>
<% if (i==0) { %>
						<html:select name="sellbuyform" property="status">
							<html:options	labelName="sessiondata"
														labelProperty="statusCodes"
														name="sessiondata"
														property="statusCodes"
														 />
						</html:select>
<% } %>
					</td>
					<td>
<% if (i==0) { %>
						<html:text	name="sellbuyform"
												property="seatQty"
												size="4"
												maxlength="3"
												style="text-align: right;"
												onchange="if (validNumberField(this, true, 0, 999)) calcTotal();" />
<% } %>
					</td>
					<td>
						<input type="hidden" name="oldcurrency" value="<%= sellbuyform.getCurrency(i) %>">
					</td>
					<td>
						<viking:vikingSelector	selectionType="currencySelector"
																		name="currency"
																		onChange="newExchangeRate();"
																		value="<%= sellbuyform.getCurrency(i) %>" />
					</td>
					<td>
						<input	name="exchangeRate"
										value="<%= sellbuyform.getExchangeRate(i) %>"
										style="text-align: right;"
										onchange="if (validDisplyErrorAmount(this)) { return displayMessage(this) } else return false;"
										size="8"
										maxlength="8" />
					</td>
					<td>
						<input	name="price"
										value="<%= sellbuyform.getPrice(i) %>"
										style="text-align: right;"
										onchange="if (validAmount(this, 0.0)) calcTotal();"
										size="10"
										maxlength="8" />
					</td>
					<td>
						<input style="text-align: right; background-color: #eeeeee;" name="total" disabled size="13">
					</td>
					<td>
<% if (i==0) { %>
						<html:select name="sellbuyform" property="broker">
							<html:option value="" />
							<html:options	labelName="allAccounts"
														labelProperty="sortedAccountCodes"
														name="allAccounts"
														property="sortedAccountCodes" />
						</html:select>
<% } else { %>
						<input type='button' class='rowBtn' onclick='deleteRow(this.parentElement.parentElement.rowIndex-1);' value='Del' />
<% } %>
					</td>
				</tr>
<% } %>
			</table>
		</td>
	</tr>
	<tr>
		<th style="text-align: left; padding-top: 4px; border-top: 1px solid black;">Comments</th>
		<th style="text-align: right; border-top: 1px solid black;">
			Amendment Type <viking:vikingSelector	selectionType="amendmentTypeSelector"
																						name="amendmentType" />
		</th>
	</tr>
	<tr>
		<td colspan="2" style="padding: 2px;">
			<html:textarea	name="sellbuyform"
											property="comments"
											rows="5"
											style="width: 100%" />
		</td>
	</tr>
</table><p/>
<button	accesskey="S"
        name="saveBtn"
				style="color: #008000;"
				onClick="doSubmit();"><u>S</u>ave</button>
<button	accesskey="C"
				style="color: #c00000;"
				onClick="window.close();"><u>C</u>ancel</button>
</html:form>
<% if ("true".equalsIgnoreCase(sellbuyform.getSingle())) { %>
Please note that any changes made, will be applied to each of the selected sectors on each of the selected
dates.<br/>The price entered will NOT be split between the selected sectors, but will be applied to each.
<% } %>
</body>
</html:html>
