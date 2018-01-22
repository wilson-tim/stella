<%@ page errorPage="ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="stellatags" prefix="stella" %>
<jsp:useBean id="airlinedetails" class="uk.co.firstchoice.stella.frontend.AirlineBean" scope="request"/>
<html>
<head>
<title>Airline Details</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="/stella/stella.css" rel="stylesheet" type="text/css">
<script src="validation.js"></script>
<script>
var frm;

function doOnLoad() {
	frm=window.document.forms[0];
	window.moveTo(	parseInt((window.screen.availWidth-document.body.offsetWidth) / 2),
					parseInt((window.screen.availHeight-document.body.offsetHeight) / 2));
}

function doSubmit() {
	frm.Save.disabled = true;
	frm.Cancel.disabled = true;
	window.document.all["theBody"].style.cursor = "wait";
	frm.airlineNumber.disabled = false;
	frm.submit();
}


function numberInUse(num) {
	var sel = frm.airlineSelect;
	var no = parseInt(num, 10);
	for (var i=0; i<sel.options.length; i++)
		if (parseInt(sel.options[i].value, 10) == no)
			return true;
	return false;
}


function validateForm() {
	var errors = "";
	var orgNo = "<%= airlinedetails.getAirlineNumber() %>"; <%-- if blank it's a new ticket --%>
	if (!validNumber(frm.airlineNumber.value, 1, 999)) {
		errors += "Airline number must be between 1 and 999\n";
	} else if (orgNo == "") {		<%-- new ticket - check that number not in use --%>
		if (numberInUse(frm.airlineNumber.value))
			errors += "Airline number "+frm.airlineNumber.value+" already in use\n";
	}
	if (frm.airlineCode.value.length != 2)
		errors += "Airline Code must be two characters in length\n";
	if (frm.airlineName.value == "")
		errors += "Airline Name cannot be blank\n";
	if (frm.airlineAddress1.value == "")
		errors += "Address Line 1 cannot be blank\n";
	if (frm.airlineCity.value == "")
		errors += "City / Town cannot be blank\n";
	if (frm.airlinePostcode.value == "")
		errors += "Postcode cannot be blank\n";
	if (frm.airlineAddressee.value == "")
		errors += "Airline Addressee cannot be blank\n";
	var amount = parseFloat(frm.airlineTaxCostTL.value);
	if (!isNaN(amount)) {
		frm.airlineTaxCostTL.value = floatToString(amount);
	} else {
		errors += "Tax Cost Tolerance Level must be a valid amount\n"
	}
	amount = parseFloat(frm.airlineSeatCostTL.value);
	if (!isNaN(amount)) {
		frm.airlineSeatCostTL.value = floatToString(amount);
	} else {
		errors += "Seat Cost Tolerance Level must be a valid amount\n"
	}
	amount = parseFloat(frm.airlineBookingPayment.value);
	if (!isNaN(amount)) {
		frm.airlineBookingPayment.value = floatToString(amount);
	} else {
		errors += "Booking Payment Commission must be a valid amount\n"
	}
	if (errors == "") {
		return true;
	} else {
		alert("Airline details could not be saved due to the following errors:\n\n"+errors);
		return false;
	}
}

</script>
</head>

<body onLoad="doOnLoad();" id="theBody">
<form method="post" action="/stella/jsp/postairline.jsp" >
<input name="oracleUniqueKey" type="hidden" value="<%= airlinedetails.getOracleUniqueKey() %>"/>
<input name="doDelete" type="hidden" value="">
<div style="display: none;"><stella:stellaSelector selectionType="airlineSelect" /></div>
<h3>Airline Details</h3>
  <table width="100%" border="0" cellspacing="0">
    <tr>
      <th>Airline Number:</th>
      <td><input name="airlineNumber" type="text" tabindex="2" size="4" maxlength="3" value="<%= airlinedetails.getAirlineNumber() %>" <%= airlinedetails.getAirlineNumber().equals("")?"":"disabled" %>></td>
      <th>Airline Code:</th>
      <td><input name="airlineCode" type="text" tabindex="3" size="4" maxlength="2" value="<%= airlinedetails.getAirlineCode() %>" onBlur="this.value=this.value.toUpperCase();"></td>
    </tr>
    <tr>
      <th>Airline Name:</th>
      <td colspan="3"><input name="airlineName" type="text" tabindex="4" size="55" maxlength="50" value="<%= airlinedetails.getAirlineName() %>" onBlur="this.value=this.value.toUpperCase();"></td>
    </tr>
    <tr>
      <th>Airline Contact:</th>
      <td colspan="3"><input name="airlineContact" type="text" tabindex="5" size="65" maxlength="60" value="<%= airlinedetails.getAirlineContact() %>"></td>
    </tr>
    <tr>
      <th>Address Line 1:</th>
      <td colspan="3"><input name="airlineAddress1" type="text" tabindex="6" size="65" maxlength="60" value="<%= airlinedetails.getAirlineAddress1() %>"></td>
    </tr>
    <tr>
      <th>Address Line 2:</th>
      <td colspan="3"><input name="airlineAddress2" type="text" tabindex="7" size="65" maxlength="60" value="<%= airlinedetails.getAirlineAddress2() %>"></td>
    </tr>
    <tr>
      <th>City / Town:</th>
      <td colspan="3"><input name="airlineCity" type="text" tabindex="8" size="65" maxlength="60" value="<%= airlinedetails.getAirlineCity() %>"></td>
    </tr>
    <tr>
      <th>County:</th>
      <td colspan="3"><input name="airlineCounty" type="text" tabindex="9" size="65" maxlength="60" value="<%= airlinedetails.getAirlineCounty() %>"></td>
    </tr>
    <tr>
      <th>Country:</th>
      <td colspan="3"><input name="airlineCountry" type="text" tabindex="10" size="65" maxlength="60" value="<%= airlinedetails.getAirlineCountry() %>"></td>
    </tr>
    <tr>
      <th>Postcode:</th>
      <td><input name="airlinePostcode" type="text" tabindex="11" size="12" maxlength="10" value="<%= airlinedetails.getAirlinePostcode() %>" onBlur="this.value=this.value.toUpperCase();"></td>
      <th>Telephone Number:</th>
      <td><input name="airlinePhone" type="text" tabindex="12" size="24" maxlength="20" value="<%= airlinedetails.getAirlinePhone() %>"></td>
    </tr>
    <tr>
      <th>Addressee:</th>
      <td colspan="3"><input name="airlineAddressee" type="text" tabindex="13" size="65" maxlength="60" value="<%= airlinedetails.getAirlineAddressee() %>"></td>
    </tr>
    <tr>
      <th>Tax Cost Tolerance Level:</th>
      <td><input name="airlineTaxCostTL" type="text" class="amount" tabindex="14" onBlur="validAmount(this, 0.0)" size="9" maxlength="8" value="<%= airlinedetails.getAirlineTaxCostTL() %>"></td>
      <th>Seat Cost Tolerance Level:</th>
      <td><input name="airlineSeatCostTL" type="text" class="amount" tabindex="15" onBlur="validAmount(this, 0.0)" size="9" maxlength="8" value="<%= airlinedetails.getAirlineSeatCostTL() %>"></td>
    </tr>
    <tr>
      <th>Sector Airline Payment:</th>
      <td>
		<input name="airlineSectorPayment" type="radio" tabindex="16" value="Y" <%= airlinedetails.getAirlineSectorPayment().equals("Y")?"checked":"" %>>Yes
		<input name="airlineSectorPayment" type="radio" tabindex="16" value="N" <%= !airlinedetails.getAirlineSectorPayment().equals("Y")?"checked":"" %>>No
	  </td>
      <th>Booking Payment Comm.:</th>
      <td><input name="airlineBookingPayment" type="text" class="amount" tabindex="17" onBlur="validAmount(this, 0.0)" size="9" maxlength="8" value="<%= airlinedetails.getAirlineBookingPayment() %>"></td>
    </tr>
  </table>
  <p/><hr/>
  <table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr><td style="text-align: center;">
      <button name="Save" accesskey="s" onClick="if (validateForm()) {doSubmit();};"><u>S</u>ave</button>
      <button name="Cancel" accesskey="c" onClick="if (confirm('Cancel airline updates and loose input')) window.close();"><u>C</u>ancel</button>
      <button name="Delete" style="color : red;" onClick="if (confirm('Are you sure you wish to completely remove this airline and all its mappings?')) {frm.doDelete.value='YES'; doSubmit()};" <%= airlinedetails.getAirlineNumber().equals("")?"Disabled":"" %>>Delete</button>
    </td></tr>
  </table>
  <hr/><p/>
  <h6><%= airlinedetails.getSaveError() %></h6>
</form>
</body>
</html>
