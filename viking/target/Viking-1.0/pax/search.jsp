<%@ page errorPage="/ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/firstchoice-tags.tld" prefix="fctags" %>
<%@ page import="uk.co.firstchoice.viking.pax.gui.search.PaxSearchForm"%>
<html:xhtml/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<html:base/>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>PAX Management</title>
	<link href="<html:rewrite page="/css/firstchoice.css"/>" type="text/css" rel="stylesheet"/>
	<script type="text/javascript" src="<html:rewrite page="/CalendarPopup.js"/>"></script>
	<script type="text/javascript" src="<html:rewrite page="/autotab.js"/>"></script>
	<script language="JavaScript" src="<html:rewrite page="/PopupWindow.js"/>"></script>
	<script type="text/javascript">
	<!--
		function checkSearch() {
			// checks to see if results where serached for and reloads search frame if required
			if ( document.forms[0].searchType.value == "results" ) {
				parent.frames["_results"].location.reload();
			}

			// checks to see if report was serached for and loads report page
			if ( document.forms[0].searchType.value == "report" ) {
				window.open('<html:rewrite page='/pax/report.do' />','report','height=768,width=1024,left=1,top=1,status=no,toolbar=no,menubar=no,location=no,scrollbar=true');
			}
		}
		
		function displayMatched() {
			document.forms[0].searchType.value = "results"; // set search type value
			validateForm();
		}

		function displayReport() {
			document.forms[0].searchType.value = "report"; // set search type value
			validateForm();
		}
		
		function resetForm() {
			document.forms[0].reset();
		}

		function validateForm() {
			message = "";

			if (message != "") { 
				alert(message);
			} else {
				document.paxSearchForm.submit(); // submit form
			}
		}
	-->
	</script>
</head>
<body class="single" onload="checkSearch()" id="reportPopup">
	<div class="body">
	<html:form action="/pax/search">
	<html:hidden property="searchType" />
	<html:hidden property="postState" value="<%=PaxSearchForm.POST_VALUE%>"/>
		<table class="page" align="left" border="0">
			<tbody>
			<tr>
				<td class="main">
					<div class="location">:: PAX Management :: Search Criteria&gt;&gt;</div><p/>
					<table id="mainContent" border="0">
					  <tr>
					   <td>
						   <script type="text/javascript">
								var calendar = new CalendarPopup();
								calendar.showNavigationDropdowns();
							</script>
						  </td>
						   <td align="left">From<br/>&nbsp;
							<html:text property="fromDate" size="8" readonly="true" />&nbsp;<a href="#" onclick="calendar.select(document.forms[0].fromDate,'anchor1','dd/MM/yyyy'); return false;" title="calendar.select(document.forms[0].fromDate,'anchor1','MM/dd/yyyy'); return false;" name="anchor1" id="anchor1"><img src="<html:rewrite page="/images/cal.gif"/>" title="from date" alt="from date"  /></a>
							</td>
						   <td align="left">To<br/>&nbsp;
							<html:text property="toDate" size="8" readonly="true" />&nbsp;<a href="#" onclick="calendar.select(document.forms[0].toDate,'anchor2','dd/MM/yyyy'); return false;" title="calendar.select(document.forms[0].toDate,'anchor2','MM/dd/yyyy'); return false;" name="anchor2" id="anchor2"><img src="<html:rewrite page="/images/cal.gif"/>" title="to date" alt="to date"  /></a>
						   </td>
						   <td align="left" colspan="2">Flight Type<br/>&nbsp;
							<html:select property="flightType">
								<html:options collection="flightTypeList" property="customerCode" labelProperty="customerName"/>
							</html:select>
						   </td>
						 </tr>
						  <tr>
						   <td align="left" colspan="2">Customer<br/>&nbsp;
								<html:select property="carrier">
									<option value=""></option>
									<html:options collection="carrierList" property="customerCode" labelProperty="customerName"/>
								</html:select>
						   </td>
						   <td align="left"><html:submit title="Generate Report" property="reportButton" value="Generate Report" onclick="displayReport()"/></td>
						   <td align="left"><html:submit title="Display Matched" value="Display Matched" property="resultsButton" onclick="displayMatched()"/></td>
						   <td align="left"><html:button title="Reset" value="Reset" property="Reset form" onclick="resetForm()"/></td>
						 </tr>
					</table>
				</td>
				</tr>
			</tbody>
		</table>
    </html:form>
	</div>
	<br/><br/>
</body>
</html>