<%@ page errorPage="/ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/firstchoice-tags.tld" prefix="fctags" %>
<%@ page import="uk.co.firstchoice.viking.pax.gui.utils.StringConstants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
<html:base/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>PAX Management</title>
	<link href="<html:rewrite page="/css/firstchoice.css"/>" type="text/css" rel="stylesheet"/>
	<link href="<html:rewrite page="/css/grid.css"/>" type="text/css" rel="stylesheet"/>
	<script type="text/javascript" src="<html:rewrite page="/grid.js"/>"></script>
	<script type="text/javascript" src="<html:rewrite page="/CalendarPopup.js"/>"></script>
	<script type="text/javascript" src="<html:rewrite page="/autotab.js"/>"></script>
	
	<script type="text/javascript" src="<html:rewrite page="/paxValidation.js"/>"></script>
</head>
<body class="single">
<div class="body">
<logic:present name="<%=StringConstants.PAX_RESULTS%>">
<form name="results">
	<table class="page" align="left" border="0">
		<tbody>
		<tr>
			<td class="main"><div class="location">:: Flight Results&gt;&gt; </div><p/></td>
		</tr>
		<tr>
			<td class="main">
				<table cellpadding="0" cellspacing="0">
				  <thead>
				  <tr>
				      <th>Departure Date</th>
				      <th>Departure Time</th>
				      <th>Flight #</th>
				      <th>Route</th>
				      <th>Series #</th>
				      <th></th>
				    </tr>
				  </thead>
				  <tbody>
				   <logic:iterate id="paxItem" name="<%=StringConstants.PAX_RESULTS%>" type="uk.co.firstchoice.viking.pax.gui.search.PaxItemBean" >
				   <tr onmouseover="this.className='hilite';" onmouseout="this.className='plain';" onclick="processItem(this,'<bean:write filter="false" name="paxItem" property="flightNumber"/>','<html:rewrite page='/pax/edit.do' property="paxPropertiesMap" name="paxItem"/>')";></td>
				      <td><bean:write filter="false" name="paxItem" property="departureDate"/></td>
				      <td><bean:write filter="false" name="paxItem" property="departureTime"/></td>				     
					  <td class="numeric"><bean:write filter="false" name="paxItem" property="flightNumber"/></td>
				      <td><bean:write filter="false" name="paxItem" property="route"/></td>
				      <td class="numeric"><bean:write filter="false" name="paxItem" property="series"/></td>
					  <td class="numeric"><input type="checkbox" value="" name="<bean:write filter="false" name="paxItem" property="flightNumber"/>" onclick="processItem(this,'<bean:write filter="false" name="paxItem" property="flightNumber"/>','<html:rewrite page='/pax/edit.do' property="paxPropertiesMap" name="paxItem"/>')";></input></td>
				    </tr>
					</logic:iterate> 
				  </tbody>
				</table>
				</td>
			</tr>
		</tbody>
	</table>
</form>
</logic:present>
</div>
<br /><br />
</body>
</html>