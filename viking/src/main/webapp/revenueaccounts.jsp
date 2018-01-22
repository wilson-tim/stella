<%@ page errorPage="/ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/vikingtags.tld" prefix="viking" %>
<jsp:useBean	id="revenueaccountform"
							class="uk.co.firstchoice.viking.gui.RevenueAccountFormBean"
							scope="request"/>
<% String action = revenueaccountform.getAction(); %>					<!-- view, edit or add -->
<% String inputFlag = action.equals("view")?" disabled ":""; %>	<!-- "" or "disabled" (if viewing) -->
<!--<% inputFlag = ""; %> <!-- for testing -->
<html:html>
<head>
<title>Customer/Supplier Accounts</title>
<link href="<html:rewrite page="/css/viking.css"/>" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="<html:rewrite page="/table.js"/>"></script>

<script type="text/javascript" src="<html:rewrite page="/validation.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/utils.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/datepicker.js"/>"></script>


<style>
.datatable th { text-align: center; vertical-align: center; color: darkBlue; }
.datatable td { text-align: center; }
.detailstable th { text-align: right; vertical-align: center; color: darkBlue; background-color: transparent; }
.detailstable td { }
</style>

<script>
var frm;

var fullDescriptions = 
[
<%
  java.util.List ac = revenueaccountform.getAllContracts();
  for (java.util.Iterator i = ac.iterator(); i.hasNext(); ) {
    uk.co.firstchoice.viking.gui.ContractDetailsFormBean cdfb = 
        (uk.co.firstchoice.viking.gui.ContractDetailsFormBean)i.next();
    out.print("\""+cdfb.getPaymentTerms().replaceAll("\r\n","~")+"\",");
  }
%>

];

function validate() {
	var errors = "";
<% if (action.equals("add")) { %>
	if (frm.code.value == "") errors += "Code is missing\n";
<% } %>
	if (frm.name.value == "") errors += "Name is missing\n";
	if (frm.atol.value == "") errors += "ATOL is missing\n";
	var fc1 = frm.flightCode1.value;
	var fc2 = frm.flightCode2.value;
	var fc3 = frm.flightCode3.value;
	var fc4 = frm.flightCode4.value;
	if ((fc1 != "" && (fc1 == fc2 || fc1 == fc3 || fc1 == fc4)) ||
			(fc2 != "" && (fc2 == fc3 || fc2 == fc4)) ||
			(fc3 != "" && (fc3 == fc4))) {
		errors += "No duplicate flight codes\n";
	}
	if (errors == "") {
		return true;
	} else {
		alert("Please correct the following errors prior to submitting:\n\n"+errors);
		return false;
	}
}


function doOnLoad() {
	frm=document.forms[0];
}


function doSubmit(action) {
	if (action!="save" || validate()) {
		frm.code.disabled = false;
		frm.action.value = action;
// add or edit - what are we saving, new or old?
		frm.submit();
	}
}


function showTerms(lineno) {
	if (fullDescriptions && lineno > -1 && lineno < fullDescriptions.length) {
		var txt = fullDescriptions[lineno];
		if (txt == "") txt = "No terms available"
		window.document.all.fullTerms.innerText=txt.replace(/~/g, "\n");
		window.document.all.fullTerms.style.display='block';
	}
}


function hideTerms() {
	window.document.all.fullTerms.innerText="";
	window.document.all.fullTerms.style.display='none';
}


function addEditContract(action) {
	var ok = true;
	var contractID = "";
	var contractURL = "";
	if (action=="edit") {
		contractID = radioSelected(frm.contractSelector);
		if (contractID == "") {
			alert("Please select contract id to edit");
			ok = false;
		} else {
			contractURL = '&contractID='+escape(contractID);
		}
	}
	if (ok) {
		var width = 400;
		var height = 450;
		var maxHeight = window.screen.availHeight;
		var maxWidth = window.screen.availWidth;
		var leftPoint = parseInt(maxWidth / 2) - parseInt(width / 2);
		var topPoint = parseInt(maxHeight / 2) - parseInt(height / 2);
		var code = frm.code.value;
		var win = window.open("contractdetails.do?action="+action+"&code="+escape(code)+contractURL,
													'contractdetails','width='+width+',height='+height+
													',scrollbars=yes,menubar=no,location=no,status=no,toolbar=no,resizable=yes',false);
		win.focus();
	}
}


function editContract() {
	var contract = radioSelected(frm.contractSelector);
//code & contractID
	if (contract == "") {
		alert("Please select contract id to edit");
	} else {
		alert("editing "+contract);
	}
}


function deleteContract() {
	var contract = radioSelected(frm.contractSelector);
	if (contract == "") {
		alert("Please select contract id to delete");
	} else {
		if (confirm("Are you sure you wish to delete contract id "+contract)) {
			doSubmit("deletecontract");
		}
	}
}


function changeCode(code) {				// User has changed either code or name to view
	if (frm.code[0].options[frm.code[0].selectedIndex].value!=code) {	<!-- Was it not the code (but the name) that was changed? -->
		for (var i = 0; i < frm.code[0].options.length; i++) {
			if (frm.code[0].options[i].value == code) {
				frm.code[0].selectedIndex = i;		// select the equivilant code 
				break;
			}
		}
	}
	doSubmit('view');
}

</script>
</head>
<body onLoad="doOnLoad();">
<html:form action="revenueaccount.do">
<!--
 Action is "save" if the user is saving, or "reload" if document need to be reloaded
 due to changed contracts (set from edit contract pop-up window)
-->
<input type="hidden" name="action"/>
<html:hidden	name="revenueaccountform"
							property="saveType" />
<!--<input	type="hidden"
				name="fromAction"
				value="<jsp:getProperty name="revenueaccountform" property="action" />"
				/>-->	<!-- if action==save then fromAction=add or edit -->
<h3>Customer/Supplier Accounts</h3>
<h6><jsp:getProperty name="revenueaccountform" property="error" /></h6>
<table cellspacing="0" cellpadding="1" class="detailstable" width="100%">
	<tr>
		<th>Code</th>
		<td>
<% if (!action.equals("view")) { %>
			<input	name="code"
							type="text"
							size="8"
							maxlength="6"
							onChange="this.value=this.value.toUpperCase();"
							<%= action.equals("edit")?"disabled":"" %>
							value="<jsp:getProperty name="revenueaccountform" property="code" />"
							/>
<% } else { %>
			<html:select	name="revenueaccountform"
										property="code"
										onchange="changeCode(this.options[this.selectedIndex].value);">
				<html:options	labelName="allAccounts"
											labelProperty="sortedAccountCodes"
											name="allAccounts"
											property="sortedAccountCodes" />
			</html:select>
<% } %>
		</td>
		<th>Name</th>
		<td>
<% if (action.equals("add") || action.equals("edit")) { %>
			<input	name="name"
							type="text"
							size="30"
							maxlengt="30"
							value="<jsp:getProperty name="revenueaccountform" property="name" />"
							/>
<% } else { %>
			<html:select	name="revenueaccountform"
										property="code"
										onchange="changeCode(this.options[this.selectedIndex].value);">
				<html:options	labelName="allAccounts"
											labelProperty="accountNames"
											name="allAccounts"
											property="accountCodes" />
			</html:select>
<% } %>
		</td>
		<th>ATOL</th>
		<td>
			<input	type="text"
							name="atol"
							size="8"
							<%= inputFlag %>
							value="<jsp:getProperty name="revenueaccountform" property="atol" />"
							/>
		</td>
 		<th>Flight Codes</th>
		<td>
			<input	type="text"
							name="flightCode1"
							size="5"
							maxlength="3"
  
							<%= inputFlag %>
							value="<jsp:getProperty name="revenueaccountform" property="flightCode1" />"
							/>
		</td>
	</tr>
	<tr>
		<th>Contact</th>
		<td>
			<input	type="text"
							name="contactName"
							size="30"
							<%= inputFlag %>
							value="<jsp:getProperty name="revenueaccountform" property="contactName" />"
							/>
		</td>
 		<th>Office Phone</th>
		<td>
			<input	type="text"
							name="officePhone"
							size="20"
							<%= inputFlag %>
							value="<jsp:getProperty name="revenueaccountform" property="officePhone" />"
							/>
		</td>
		<th>Office Fax</th>
		<td>
			<input	type="text"
							name="officeFax"
							size="20"
							<%= inputFlag %>
							value="<jsp:getProperty name="revenueaccountform" property="officeFax" />"
							/>
		</td>
		<th></th>
		<td>
			<input	type="text"
							name="flightCode2"
							size="5"
							maxlength="3"
							<%= inputFlag %>
							value="<jsp:getProperty name="revenueaccountform" property="flightCode2" />"
							/>
		</td>
	</tr>
	<tr>
		<th>Address</th>
		<td>
			<input	type="text"
							name="address1"
							size="30"
							<%= inputFlag %>
							value="<jsp:getProperty name="revenueaccountform" property="address1" />"
							/>
		</td>
		<th>Out Of Office Contact</th>
		<td>
			<input	type="text"
							name="oooContact1"
							size="30"
							<%= inputFlag %>
							value="<jsp:getProperty name="revenueaccountform" property="oooContact1" />"
							/>
		</td>
		<th>Out Of Office Phone</th>
		<td>
			<input	type="text"
							name="oooPhone1"
							size="20"
							<%= inputFlag %>
							value="<jsp:getProperty name="revenueaccountform" property="oooPhone1" />"
							/>
		</td>
		<th></th>
		<td>
			<input	type="text"
							name="flightCode3"
							size="5"
							maxlength="3"
							<%= inputFlag %>
							value="<jsp:getProperty name="revenueaccountform" property="flightCode3" />"
							/>
		</td>
	</tr>
	<tr>
		<th></th>
		<td>
			<input	type="text"
							name="address2"
							size="30"
							<%= inputFlag %>
							value="<jsp:getProperty name="revenueaccountform" property="address2" />"
							/>
		</td>
		<th>Out Of Office Contact</th>
		<td>
			<input	type="text"
							name="oooContact2"
							size="30"
							<%= inputFlag %>
							value="<jsp:getProperty name="revenueaccountform" property="oooContact2" />"
							/>
		</td>
		<th>Out Of Office Phone</th>
		<td>
			<input	type="text"
							name="oooPhone2"
							size="20"
							<%= inputFlag %>
							value="<jsp:getProperty name="revenueaccountform" property="oooPhone2" />"
							/>
		</td>
		<th></th>
		<td>
			<input	type="text"
							name="flightCode4"
							size="5"
							maxlength="3"
							<%= inputFlag %>
							value="<jsp:getProperty name="revenueaccountform" property="flightCode4" />"
							/>
		</td>
	</tr>
	<tr>
		<th></th>
		<td>
			<input	type="text"
							name="address3"
							size="30"
							<%= inputFlag %>
							value="<jsp:getProperty name="revenueaccountform" property="address3" />"
							/>
		</td>
		<th>Ops E-mail</th>
		<td>
			<input	type="text"
							name="opsEmail"
							size="30"
							<%= inputFlag %>
							value="<jsp:getProperty name="revenueaccountform" property="opsEmail" />"
							/>
		</td>
		<th>Inform Of Delay</th>
		<td>
			<viking:vikingSelector selectionType="ynSelector" name="informDelay" value="<%= revenueaccountform.getInformDelay() %>"/>
		</td>
	</tr>
	<tr>
		<th></th>
		<td>
			<input	type="text"
							name="address4"
							size="30"
							<%= inputFlag %>
							value="<jsp:getProperty name="revenueaccountform" property="address4" />"
							/>
		</td>
		<th>General E-mail</th>
		<td>
			<input	type="text"
							name="generalEmail"
							size="30"
							<%= inputFlag %>
							value="<jsp:getProperty name="revenueaccountform" property="generalEmail" />"
							/>
		</td>
		<th>Between 00:00-06:00</th>
		<td>
			<viking:vikingSelector selectionType="ynSelector" name="informLate" value="<%= revenueaccountform.getInformLate() %>"/>
		</td>
	</tr>
	<tr>
		<th></th>
		<td>
			<input	type="text"
							name="address5"
							size="30"
							<%= inputFlag %>
							value="<jsp:getProperty name="revenueaccountform" property="address5" />"
							/>
		</td>
	</tr>
	<tr>
		<th>Post code</th>
		<td>
			<input	type="text"
							name="postCode"
							size="10"
							onChange="this.value=this.value.toUpperCase();"
							<%= inputFlag %>
							value="<jsp:getProperty name="revenueaccountform" property="postCode" />"
							/>
		</td>
		<th>Account Holder</th>
		<td>
			<input	type="text"
							name="accountHolder"
							size="30"
							<%= inputFlag %>
							value="<jsp:getProperty name="revenueaccountform" property="accountHolder" />"
							/>
		</td>
		<th>TUI Group Customers</th>
		<td>
<% if (action.equals("view")) { %>
			<html:checkbox name="revenueaccountform" property="fcGroup" disabled="true" />
<% } else { %>
			<html:checkbox name="revenueaccountform" property="fcGroup" />
<% } %>
		</td>
	</tr>
	<tr>
		<th>Country</th>
		<td>
<% if (action.equals("view")) { %>
			<html:select name="revenueaccountform" property="countryCode" disabled="true">
				<html:options	labelName="sessiondata"
											labelProperty="countryNames"
											name="sessiondata"
											property="countryCodes" />
			</html:select>
<% } else { %>
			<html:select name="revenueaccountform" property="countryCode">
				<html:options	labelName="sessiondata"
											labelProperty="countryNames"
											name="sessiondata"
											property="countryCodes" />
			</html:select>
<% } %>
		</td>
		<th>Deputy</th>
		<td>
			<input	type="text"
							name="accountDeputy"
							size="30"
							<%= inputFlag %>
							value="<jsp:getProperty name="revenueaccountform" property="accountDeputy" />"
							/>
		</td>
	</tr>
</table>
<p/>
<table class="datatable" cellspacing="0" cellpadding="1" width="100%">
	<tr>
		<th colspan="99" style="font-size: larger; font-weight: bold;">Contract Information</th>
	</tr>
	<tr>
		<th colspan="8" style="border-bottom: 0px;">&nbsp;</th>
		<th colspan="18" style="border-bottom: 0px;">Passenger Taxes</th>
	</tr>
	<tr>
		<th colspan="4" style="border-bottom: 0px;">&nbsp;</th>
		<th colspan="4" style="border-bottom: 0px;">Surcharges</th>
		<th colspan="3" style="border-bottom: 0px;">APD</th>
		<th colspan="3" style="border-bottom: 0px;">OS Tax In</th>
		<th colspan="3" style="border-bottom: 0px;">OS Tax Out</th>
		<th colspan="3" style="border-bottom: 0px;">PIL</th>
		<th colspan="3" style="border-bottom: 0px;">UK Tax In</th>
		<th colspan="3" style="border-bottom: 0px;">UK Tax Out</th>
	</tr>
	<tr>
		<th>&nbsp;</th>
		<th>Seas.</th>
		<th colspan="2">Contract ID</th>
		<th>Fuel</th>
		<th>Curr.</th>
		<th>Euro</th>
		<th>Terms</th>
		<th>R</th>
		<th>I</th>
		<th>%</th>
		<th>R</th>
		<th>I</th>
		<th>%</th>
		<th>R</th>
		<th>I</th>
		<th>%</th>
		<th>R</th>
		<th>I</th>
		<th>%</th>
		<th>R</th>
		<th>I</th>
		<th>%</th>
		<th>R</th>
		<th>I</th>
		<th>%</th>
	</tr>
<% if (revenueaccountform.getAllContracts().size()==0 && !action.equals("add")) { %>
	<tr><td colspan="99" style="text-align: center; font-weight: bold;">No Contracts Available</td></tr>
<% } %>
<% int lineNo = 0; %>
	<logic:iterate	id="contract"
									name="revenueaccountform"
									property="allContracts"
									type="uk.co.firstchoice.viking.gui.ContractDetailsFormBean">
		<tr class="line<%= (lineNo%2) %>">
			<td>
				<% if (!action.equals("view")) { %>
					<html:radio	idName="contract"
											value="contractID"
											property="contractSelector"
											styleClass="rowradio" />
				<% } %>
			</td>
			<td><bean:write name="contract" property="season" /></td>
			<td><bean:write name="contract" property="contractType" /></td>
			<td style="text-align: left;"><bean:write name="contract" property="contractID" /></td>
			<td><bean:write name="contract" property="fuelFixed" /></td>
			<td><bean:write name="contract" property="currencyFixed" /></td>
			<td><bean:write name="contract" property="euroFixed" /></td>
			<td	style="text-align: left; cursor: hand;"
					onmousedown="showTerms(<%= lineNo++ %>);"
					onmouseout="hideTerms();"
					onmouseup="hideTerms();">
				<bean:write name="contract" property="shortPaymentTerms" />
			</td>
			<td><bean:write name="contract" property="apdR" /></td>
			<td><bean:write name="contract" property="apdI" /></td>
			<td><bean:write name="contract" property="apdPct" /></td>
			<td><bean:write name="contract" property="osTaxesInR" /></td>
			<td><bean:write name="contract" property="osTaxesInI" /></td>
			<td><bean:write name="contract" property="osTaxesInPct" /></td>
			<td><bean:write name="contract" property="osTaxesOutR" /></td>
			<td><bean:write name="contract" property="osTaxesOutI" /></td>
			<td><bean:write name="contract" property="osTaxesOutPct" /></td>
			<td><bean:write name="contract" property="pilR" /></td>
			<td><bean:write name="contract" property="pilI" /></td>
			<td><bean:write name="contract" property="pilPct" /></td>
			<td><bean:write name="contract" property="ukTaxesInR" /></td>
			<td><bean:write name="contract" property="ukTaxesInI" /></td>
			<td><bean:write name="contract" property="ukTaxesInPct" /></td>
			<td><bean:write name="contract" property="ukTaxesOutR" /></td>
			<td><bean:write name="contract" property="ukTaxesOutI" /></td>
			<td><bean:write name="contract" property="ukTaxesOutPct" /></td>
		</tr>
	</logic:iterate>
<% if (action.equals("edit")) { %>
	<tr>
		<td colspan="99" style="padding: 4px;">
			<a href="#" onClick="addEditContract('add');">Add</a>
	<% if (revenueaccountform.getAllContracts().size()>0) { %>
			| <a href="#" onClick="addEditContract('edit');">Edit</a> |
			<a href="#" onClick="deleteContract();">Delete</a>
	<% } %>
		</td>
	</tr>
<% } %>
</table>
<p/>
<table cellspacing="0" cellpadding="1" class="datatable" width="100%">
	<tr>
		<th style="font-size: larger; font-weight: bold;">Notes</th>
	</tr>
	<tr>
		<td>
			<textarea	name="comments"
								rows="5"
								style="width: 100%;"
								<%= inputFlag %>><jsp:getProperty name="revenueaccountform" property="comments" /></textarea>
		</td>
	</tr>
</table>

<hr/><center>
<% if (!action.equals("view")) { %>
<button	accesskey="S"
				style="color: #008000;"
				onClick="doSubmit('save');"><u>S</u>ave</button>
<button	accesskey="R"
				style="color: #c00000;"
				onClick="if (confirm('Are you sure you wish to reset all fields?')) frm.reset();"><u>R</u>eset</button>
<button	accesskey="C"
				style="color: #c00000;"
				onClick="if (confirm('Are you sure you wish to cancel and loose any changes?')) {doSubmit('view');}"><u>C</u>ancel</button>
<% } else {%>
<%
		if (request.isUserInRole("VIKING_MANAGER") ||
				request.isUserInRole("VIKING_EDITOR")) { 
%>
			<button	accesskey="N"
							onClick="doSubmit('add');"><u>N</u>ew Account</button>
			<% if (!revenueaccountform.getCode().equals("")) { %>
				<button	accesskey="E"
								onClick="doSubmit('edit');"><u>E</u>dit</button>
				<button	style="color: #B00000;"
								onClick="if (confirm('Are you sure you wish to delete the account for '+frm.code[0].value+'?')) {doSubmit('delete');}">Delete</button>
			<% } %>
	<% } %>
	<button	accesskey="C"
					onClick="location.href='menu.do';"><u>C</u>lose</button>
<% } %>
</html:form>
<div id="fullTerms" style="padding:20px; position:absolute;top:150;left:450;width:300;display:none;background-color:#ffffc0;color:black;border: 3px solid darkBlue;text-align:left;"></div>
</body>
</html:html>