<!-- %@ page errorPage="/ErrorPage.jsp" %-->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-nested.tld" prefix="nested" %>
<%@ page import="uk.co.firstchoice.viking.pax.gui.search.PaxEditForm"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
<html:base/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>PAX Management</title>
	<link href="<html:rewrite page="/css/firstchoice.css"/>" type="text/css" rel="stylesheet"/>
	<link href="<html:rewrite page="/css/grid.css"/>" type="text/css" rel="stylesheet"/>
	<script type="text/javascript" src="<html:rewrite page="/CalendarPopup.js"/>"></script>
	<script type="text/javascript" src="<html:rewrite page="/autotab.js"/>"></script>
	
	<script type="text/javascript" src="<html:rewrite page="/grid.js"/>"></script>
	<script type="text/javascript" src="<html:rewrite page="/paxValidation.js"/>"></script>
</head>
<body class="single" >
<html:form action="/pax/edit.do" target="_edit">
<logic:present name="paxEditForm" property="flightNumber" >
	<html:hidden property="postState" value="<%=PaxEditForm.POST_VALUE%>"/>
		<table align="left">
			<tbody>
			<tr>
				<td class="main"><div class="location">:: Edit PAX&gt;&gt; &nbsp;<html:submit title="Submit" value="submit"/></div><p/></td>
			</tr>
			<tr>
			<td class="main">
				<table cellpadding="0" cellspacing="0" border="0">
				  <thead>
				  <tr>
					<th class="mainHeader" colspan="3"><html:text name="paxEditForm" property="flightNumber" /></th>
					<th></th>
					<th class="mainHeader">Initial</th>
					<th class="mainHeader">Update</th>
				  </tr>
				<tr>
				  <th>T/O Code</th>
				  <th>Class</th>
				  <th>Allocation</th>
				  <th></th>
				  <th>
					<table align="left" border="0" width="180">
						<thead>
						<tr>
						  <th width="40">From&nbsp;&nbsp;&nbsp;</th>
						  <th width="40">To&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
						  <th width="40">Adults&nbsp;&nbsp;&nbsp;&nbsp;</th>
						  <th width="40">Child&nbsp;&nbsp;</th>
						  <th width="40">Infant&nbsp;&nbsp;</th>
						</tr>
						</thead>
					</table>
				  </th>
				  <th>
					<table align="left"  border="0" width="180">
						<thead>
						<tr>
						  <th width="40">From&nbsp;&nbsp;&nbsp;</th>
						  <th width="40">To&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
						  <th width="40">Adults&nbsp;&nbsp;&nbsp;&nbsp;</th>
						  <th width="40">Child&nbsp;&nbsp;</th>
						  <th width="40">Infant&nbsp;&nbsp;</th>
						</tr>
						</thead>
					</table>
				   </th>
				  </tr>
				  </thead>
				  <tbody id="defTblBdy">
					<logic:present name="org.apache.struts.action.ERROR">
						<!-- display error message -->
						<html:errors property="SAVE_ERROR"/>
					</logic:present>
					<logic:equal name="paxEditForm"	property="saveStatus" value="true">
						<center><bean:message key="save.success"/></center><!-- display save data message -->
					</logic:equal>
					   	<nested:iterate property="paxItemArray">
						  <tr onmouseover="this.className='hilite';" onmouseout="this.className='plain';">
							<td><nested:text property="customerCode" readonly="true" size="6" /></td>
							<td><nested:text property="seatClass"   readonly="true" size="3" /></td>
							<td><nested:text property="allocation"  readonly="true" size="6" /></td>
							<td>
								<nested:hidden property="allocationId"  />
								<nested:hidden property="allocationType"  />
								<nested:hidden property="seriesDetailId"  />
								<nested:hidden property="series"  />
								<nested:hidden property="statusCode"  />
							</td>
						    <nested:iterate property="allocationItems" >
								<td width="180">
									<table align="left" width="180" border="0">
										<tr>
										  <td width="40"><nested:text property='departureGateFrom' 	size="4"  	readonly="true" /></td>
										  <td width="40"><nested:text property='departureGateTo' 	size="4"  	readonly="true" /></td>
										  <td width="40"><nested:text property='inAdults' 			size="4"  	onchange="this.className='changedField'; checkValue(this);"/></td>
										  <td width="40"><nested:text property='inChildren' 		size="4"  	onchange="this.className='changedField'; checkValue(this);"/></td>
										  <td width="40"><nested:text property='inInfants' 			size="4"  	onchange="this.className='changedField'; checkValue(this);"/></td>
										</tr>
									</table>

									<nested:hidden property="inAdultsPrev" 		 />
									<nested:hidden property="inChildrenPrev" 	 />
									<nested:hidden property="inInfantsPrev" 	 />
									
									<nested:hidden property="allocationType" 	 />
									<nested:hidden property="paxStatus" 		 />
									<nested:hidden property="paxId" 		 	 />

								</td>
						    </nested:iterate>
						  </tr>
						</nested:iterate>
				  </tbody>
				</table>
				</td>
			</tr>
		</tbody>
	</table>
</logic:present>
</html:form>
<br />
</body>
</html>