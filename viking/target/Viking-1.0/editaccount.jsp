<%@ page errorPage="/ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/vikingtags.tld" prefix="viking" %>
<jsp:useBean	id="contractdetailsform"
							class="uk.co.firstchoice.viking.gui.ContractDetailsFormBean"
							scope="request"/>
<html:html>
<head>
<title>Edit Contract Details</title>
<link href="<html:rewrite page="/css/viking.css"/>" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="<html:rewrite page="/table.js"/>"></script>

<script type="text/javascript" src="<html:rewrite page="/validation.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/utils.js"/>"></script>

<style>
input {border: 1px solid black;}
table {border: 1px solid darkblue;}
th {color: darkBlue; background-color: transparent; border: 0px; text-align: right;}
td {border: 0px; text-align: left;}
</style>
<script>
var frm;

function formatFloat(field) {
	if (validAmount(field, 0.0, 100.0)=="") {
		field.value = floatToString(parseFloat(field.value));
	}
}

function validate() {
	var errors = "";
	var err = "";
	if (frm.contractID.value == "") errors += "Contract ID is missing\n";
	if (frm.fuelFixed.value == "") errors += "Fuel Fixed indicator is blank\n";
	if (frm.currencyFixed.value == "") errors += "Currency Fixed indicator is blank\n";
	if (frm.euroFixed.value == "") errors += "Euro Fixed indicator is blank\n";
	if (frm.apdR.value == "") errors += "APD-R indicator is blank\n";
	if (frm.apdI.value == "") errors += "APD-I indicator is blank\n";
	if (frm.apdPct.value == "") {
		errors += "APD-Pct is blank\n";
	} else {
		//err = validAmount(frm.apdPct.value, 0.0, 100.0);
		err = validAmount(frm.apdPct, 0.0, 100.0);
		errors += (err==""?"":"Error in APD-Pct. "+err+"\n");
	}

	if (frm.osTaxesInR.value == "") errors += "OS Taxes In-R indicator is blank\n";
	if (frm.osTaxesInI.value == "") errors += "OS Taxes In-I indicator is blank\n";
	if (frm.osTaxesInPct.value == "") {
		errors += "OS Taxes In-Pct is blank\n";
	} else {
		err = validAmount(frm.osTaxesInPct, 0.0, 100.0);
		errors += (err==""?"":"Error in OS Taxes In-Pct. "+err+"\n");
	}
	
	if (frm.osTaxesOutR.value == "") errors += "OS Taxes Out-R indicator is blank\n";
	if (frm.osTaxesOutI.value == "") errors += "OS Taxes Out-I indicator is blank\n";
	if (frm.osTaxesOutPct.value == "") {
		errors += "OS Taxes Out-Pct is blank\n";
	} else {
		err = validAmount(frm.osTaxesOutPct, 0.0, 100.0);
		errors += (err==""?"":"Error in OS Taxes Out-Pct. "+err+"\n");
	}
	
	if (frm.pilR.value == "") errors += "PIL-R indicator is blank\n";
	if (frm.pilI.value == "") errors += "PIL-I indicator is blank\n";
	if (frm.pilPct.value == "") {
		errors += "PIL-Pct is blank\n";
	} else {
		err = validAmount(frm.pilPct, 0.0, 100.0);
		errors += (err==""?"":"Error in PIL-Pct. "+err+"\n");
	}
	
	if (frm.ukTaxesInR.value == "") errors += "UK Taxes In-R indicator is blank\n";
	if (frm.ukTaxesInI.value == "") errors += "UK Taxes In-I indicator is blank\n";
	if (frm.ukTaxesInPct.value == "") {
		errors += "UK Taxes In-Pct is blank\n";
	} else {
		err = validAmount(frm.ukTaxesInPct, 0.0, 100.0);
		errors += (err==""?"":"Error in UK Taxes In-Pct. "+err+"\n");
	}
	
	if (frm.ukTaxesOutR.value == "") errors += "UK Taxes Out-R indicator is blank\n";
	if (frm.ukTaxesOutI.value == "") errors += "UK Taxes Out-I indicator is blank\n";
	if (frm.ukTaxesOutPct.value == "") {
		errors += "UK Taxes Out-Pct is blank\n";
	} else {
		err = validAmount(frm.ukTaxesOutPct, 0.0, 100.0);
		errors += (err==""?"":"Error in UK Taxes Out-Pct. "+err+"\n");
	}
	
	if (errors != "") {
		alert("Please correct the following errors before saving:\n\n"+errors);
		return false;
	} else {
		return true;
	}
}

function doOnLoad() {
	frm=window.document.forms[0];
	window.moveTo(	parseInt((window.screen.availWidth-document.body.offsetWidth) / 2),
									parseInt((window.screen.availHeight-document.body.offsetHeight) / 2));
}

function doSubmit() {
	if (validate()) {
		frm.contractID.disabled = false;
		frm.action.value="save";
		frm.submit();
	}
}

</script>
<html:base/>
</head>

<body style="text-align: center;" onLoad="doOnLoad();">
<html:form action="contractdetails.do">
<input type="hidden" name="action"/>
<input	type="hidden"
				name="fromAction"
				value="<jsp:getProperty name="contractdetailsform" property="action" />"/>
<input	type="hidden"
				name="code"
				value="<jsp:getProperty name="contractdetailsform" property="code" />"/>
<h3>Edit Contract Details</h3>
<h6><jsp:getProperty name="contractdetailsform" property="error" /></h6>
<table cellspacing="0" cellpadding="2">
	<tr>
		<th>Contract ID</th>
		<td colspan="3">
			<input	type="text"
							name="contractID"
							size="18"
							maxlength="15"
							<%= contractdetailsform.getAction().equals("edit")?"disabled":"" %>
							value="<jsp:getProperty name="contractdetailsform" property="contractID" />">
		</td>
		<th>Season</th>
		<td>
			<viking:vikingSelector	selectionType="seasonSelector"
															name="season"
															value="<%= contractdetailsform.getSeason() %>"
															/>
		</td>
	</tr>
		<th>Contract Type</th>
		<td colspan="3">
			<html:select name="contractdetailsform" property="contractType">
				<html:options	labelName="sessiondata"
											labelProperty="contractTypeNames"
											name="sessiondata"
											property="contractTypeCodes" />
			</html:select>
		</td>
	<tr>
	</tr>
	<tr>
		<th colspan="2" style="text-align: center;">Surcharges</th>
		<th colspan="4" style="text-align: center;">Passenger Taxes</th>
	</tr>
	<tr></tr>
	<tr>
		<th></th>
		<th style="text-align: center;">Fixed</th>
		<th></th>
		<th style="text-align: center;">R</th>
		<th style="text-align: center;">I</th>
		<th style="text-align: center;">%</th>
	</tr>
	<tr>
		<th>Fuel</th>
		<td style="text-align: center;">
			<viking:vikingSelector	selectionType="ynSelector"
															name="fuelFixed"
															value="<%= contractdetailsform.getFuelFixed() %>"
			/>
		</td>
		<th>APD</th>
		<td style="text-align: center;">
			<viking:vikingSelector selectionType="ynSelector" name="apdR" value="<%= contractdetailsform.getApdR() %>"/>
		</td>
		<td style="text-align: center;">
			<viking:vikingSelector selectionType="ynSelector" name="apdI" value="<%= contractdetailsform.getApdI() %>"/>
		</td>
		<td style="text-align: center;">
			<input	type="text"
							name="apdPct"
							size="6"
							maxlength="5"
							style="text-align: right;"
							onChange="formatFloat(this);"
							value="<jsp:getProperty name="contractdetailsform" property="apdPct" />">
		</td>
	</tr>
	<tr>
		<th>Currency</th>
		<td style="text-align: center;">
			<viking:vikingSelector selectionType="ynSelector" name="currencyFixed" value="<%= contractdetailsform.getCurrencyFixed() %>"/>
		</td>
		<th>OS Taxes In</th>
		<td style="text-align: center;">
			<viking:vikingSelector selectionType="ynSelector" name="osTaxesInR" value="<%= contractdetailsform.getOsTaxesInR() %>"/>
		</td>
		<td style="text-align: center;">
			<viking:vikingSelector selectionType="ynSelector" name="osTaxesInI" value="<%= contractdetailsform.getOsTaxesInI() %>"/>
		</td>
		<td style="text-align: center;">
			<input	type="text"
							name="osTaxesInPct"
							size="6"
							maxlength="5"
							style="text-align: right;"
							onChange="formatFloat(this);"
							value="<jsp:getProperty name="contractdetailsform" property="osTaxesInPct" />">
		</td>
	</tr>
	<tr>
		<th>Euro</th>
		<td style="text-align: center;">
			<viking:vikingSelector selectionType="ynSelector" name="euroFixed" value="<%= contractdetailsform.getEuroFixed() %>"/>
		</td>
		<th>OS Taxes Out</th>
		<td style="text-align: center;">
			<viking:vikingSelector selectionType="ynSelector" name="osTaxesOutR" value="<%= contractdetailsform.getOsTaxesOutR() %>"/>
		</td>
		<td style="text-align: center;">
			<viking:vikingSelector selectionType="ynSelector" name="osTaxesOutI" value="<%= contractdetailsform.getOsTaxesOutI() %>"/>
		</td>
		<td style="text-align: center;">
			<input	type="text"
							name="osTaxesOutPct"
							size="6"
							maxlength="5"
							style="text-align: right;"
							onChange="formatFloat(this);"
							value="<jsp:getProperty name="contractdetailsform" property="osTaxesOutPct" />">
		</td>
	</tr>
	<tr>
		<th colspan="2">&nbsp;</th>
		<th>PLI</th>
		<td style="text-align: center;">
			<viking:vikingSelector selectionType="ynSelector" name="pilR" value="<%= contractdetailsform.getPilR() %>"/>
		</td>
		<td style="text-align: center;">
			<viking:vikingSelector selectionType="ynSelector" name="pilI" value="<%= contractdetailsform.getPilI() %>"/>
		</td>
		<td style="text-align: center;">
			<input	type="text"
							name="pilPct"
							size="6"
							maxlength="5"
							style="text-align: right;"
							onChange="formatFloat(this);"
							value="<jsp:getProperty name="contractdetailsform" property="pilPct" />">
		</td>
	</tr>
	<tr>
		<th colspan="2">&nbsp;</th>
		<th>UK Taxes In</th>
		<td style="text-align: center;">
			<viking:vikingSelector selectionType="ynSelector" name="ukTaxesInR" value="<%= contractdetailsform.getUkTaxesInR() %>"/>
		</td>
		<td style="text-align: center;">
			<viking:vikingSelector selectionType="ynSelector" name="ukTaxesInI" value="<%= contractdetailsform.getUkTaxesInI() %>"/>
		</td>
		<td style="text-align: center;">
			<input	type="text"
							name="ukTaxesInPct"
							size="6"
							maxlength="5"
							style="text-align: right;"
							onChange="formatFloat(this);"
							value="<jsp:getProperty name="contractdetailsform" property="ukTaxesInPct" />">
		</td>
	</tr>
	<tr>
		<th colspan="2">&nbsp;</th>
		<th>UK Taxes Out</th>
		<td style="text-align: center;">
			<viking:vikingSelector selectionType="ynSelector" name="ukTaxesOutR" value="<%= contractdetailsform.getUkTaxesOutR() %>"/>
		</td>
		<td style="text-align: center;">
			<viking:vikingSelector selectionType="ynSelector" name="ukTaxesOutI" value="<%= contractdetailsform.getUkTaxesOutI() %>"/>
		</td>
		<td style="text-align: center;">
			<input	type="text"
							name="ukTaxesOutPct"
							size="6"
							maxlength="5"
							style="text-align: right;"
							onChange="formatFloat(this);"
							value="<jsp:getProperty name="contractdetailsform" property="ukTaxesOutPct" />">
		</td>
	</tr>
	<tr></tr>
	<tr>
		<th>Payment Terms</th>
	</tr>
	<tr>
		<td colspan="6">
			<textarea	name="paymentTerms"
								rows="4"
								style="width: 100%"><jsp:getProperty	name="contractdetailsform"
																											property="paymentTerms" /></textarea>
		</td>
	</tr>
</table>
<p/>
<button	accesskey="S"
				style="color: #008000;"
				onClick="doSubmit();"><u>S</u>ave</button>
<button	accesskey="C"
				style="color: #c00000;"
				onClick="window.close();"><u>C</u>ancel</button>
</html:form>
</body>
</html:html>
