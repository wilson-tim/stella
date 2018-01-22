<%@ page errorPage="jsp/ErrorPage.jsp" %>
<jsp:useBean id="bspdetails" class="uk.co.firstchoice.stella.frontend.BspDetailBean" scope="request"/>
<html>
<head>
<title>BSP Exception Details</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="stella.css" rel="stylesheet" type="text/css">
<script src="validation.js"></script>
</head>

<body>
<h3>BSP Exception Details</h3>
<p/><hr/>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td style="text-align: center;">
      <button accesskey="c" onClick="window.close();"><u>C</u>lose</button>
    </td>
  </tr>
</table>
<hr/><p/>
<h6><jsp:getProperty name="bspdetails" property="error" /></h6>

<table cellspacing="0" class="dataTable">
	<tr>
		<th>Airline Number</th>
		<th>Document Number</th>
		<th>Transaction Code</th>
		<th>Reason Code</th>
		<th>BSP Period End Date</th>
		<th>Discrepancy Type</th>
		<th>Last Reconciled Date</th>
	</tr>
	<tr>
		<td><jsp:getProperty name="bspdetails" property="airlineNum" /></td>
		<td><jsp:getProperty name="bspdetails" property="documentNumber" /></td>
		<td><jsp:getProperty name="bspdetails" property="transactionCode" /></td>
		<td><jsp:getProperty name="bspdetails" property="reasonCode" /></td>
		<td><jsp:getProperty name="bspdetails" property="bspPeriodEndDate" /></td>
		<td><jsp:getProperty name="bspdetails" property="discrepancyType" /></td>
		<td><jsp:getProperty name="bspdetails" property="lastReconciledDate" /></td>
	</tr>
</table>
<p/>
<table cellspacing="0" class="dataTable">
	<tr>
		<th>BSP Seat Cost</th>
		<th>BSP Tax Amount</th>
		<th>Total BSP Cost</th>
		<th>Stella Seat Amount</th>
		<th>Stella Tax Amount</th>
		<th>Total Stella Cost</th>
		<th>Unmatched Amount</th>
	</tr>
	<tr>
		<td><jsp:getProperty name="bspdetails" property="bspSeatCost" /></td>
		<td><jsp:getProperty name="bspdetails" property="taxAmt" /></td>
		<td><jsp:getProperty name="bspdetails" property="totalBspCost" /></td>
		<td><jsp:getProperty name="bspdetails" property="stellaSeatAmt" /></td>
		<td><jsp:getProperty name="bspdetails" property="stellaTaxAmt" /></td>
		<td><jsp:getProperty name="bspdetails" property="totalStellaCost" /></td>
		<td><jsp:getProperty name="bspdetails" property="unmatchedAmt" /></td>
	</tr>
</table>
<p/>
<table cellspacing="0" class="dataTable">
	<tr>
		<th>Short Description</th>
		<th>BSP Supp Comm Amount</th>
		<th>BSP Commissionable Amt.</th>
		<th>BSP Commission Amt.</th>
		<th>BSP Net Fare Amount</th>
		<th>Conjunction Ind.</th>
	</tr>
	<tr>
		<td><jsp:getProperty name="bspdetails" property="shortDesc" /></td>
		<td><jsp:getProperty name="bspdetails" property="bspSuppCommAmt" /></td>
		<td><jsp:getProperty name="bspdetails" property="bspCommissionableAmt" /></td>
		<td><jsp:getProperty name="bspdetails" property="bspCommissionAmt" /></td>
		<td><jsp:getProperty name="bspdetails" property="bspNetFareAmt" /></td>
		<td><jsp:getProperty name="bspdetails" property="conjunctionInd" /></td>
	</tr>
</table>
</form>
</body>
</html>
