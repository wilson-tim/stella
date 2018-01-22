<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-nested.tld" prefix="nested" %>
<jsp:useBean	id="searchform"
							class="uk.co.firstchoice.viking.gui.SearchFormBean"
							scope="session"/>
<html>
<head>
<link href="<html:rewrite page="/css/viking.css"/>" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="<html:rewrite page="/table.js"/>"></script>
<%@ include file="/searchResultHeader.html"%>
</head>
<body>
<form>
<object id="dsoSearch" classid="clsid:333C7BC4-460F-11D0-BC04-0080C7055A83">
	<param name="DataURL" value="searchCSV.jsp" />
	<param name="UseHeader" value="true" />
	<param name="FieldDelim" value="^" />
	<param name="RowDelim" value="~" />
</object>
<table class="datatable" id="searchresult" datasrc="#dsoSearch" cellspacing="0" cellpadding="4" style="width: 100%;">
					<thead>
						<tr class="noScreen">
							<th colspan="99" style="border: 0px;">
								<h3>Allocations for 
<%= searchform.getCustomer().equals("")?"":("customer "+searchform.getCustomer()+(searchform.getSupplier().equals("")?"":" and ")) %>
<%= searchform.getSupplier().equals("")?"":("supplier "+searchform.getSupplier()) %></h3>
							</th>
						</tr>
						<tr class="noPrint">
							<th colspan="14" style="text-align: left; border-right: 0px;">
								Sort Order: <span id="sortId" style="color: black;">-NONE-</span> (<a href="javascript:resetSort();">reset</a>)
							</th>
							<th colspan="2" style="text-align: center; border-left: 0px;">
								<a href="#" onClick="print();">Print</a>
							</th>
						</tr>
						<tr>
							<th class="sortHeader" onClick="sortTable('seriesNo',this);" currentsort=""><span class="sortStatus"></span>#</th>
							<th class="sortHeader" onClick="sortTable('gwFrom',this);" currentsort=""><span class="sortStatus"></span>From</th>
							<th class="sortHeader" onClick="sortTable('gwTo',this);" currentsort=""><span class="sortStatus"></span>To</th>
							<th class="sortHeader" onClick="sortTable('startDate',this);" currentsort=""><span class="sortStatus"></span>Start<br/>Date</th>
							<th class="sortHeader" onClick="sortTable('endDate',this);" currentsort=""><span class="sortStatus"></span>End<br/>Date</th>
							<th class="sortHeader" onClick="sortTable('dayOfWeek',this);" currentsort=""><span class="sortStatus"></span>DOW</th>
							<th class="sortHeader" onClick="sortTable('frequency',this);" currentsort=""><span class="sortStatus"></span>Freq<br/>uency</th>
							<th class="sortHeader" onClick="sortTable('depTime',this);" currentsort=""><span class="sortStatus"></span>Dep<br/>Time</th>
							<th class="sortHeader" onClick="sortTable('arrTime',this);" currentsort=""><span class="sortStatus"></span>Arr<br/>Time</th>
							<th class="sortHeader" onClick="sortTable('flight',this);" currentsort=""><span class="sortStatus"></span>Flight</th>
							<th class="sortHeader" onClick="sortTable('brokerCode',this);" currentsort=""><span class="sortStatus"></span>Broker</th>
							<th class="sortHeader" onClick="sortTable('geminiCode',this);" currentsort=""><span class="sortStatus"></span>Selling<br/> Code</th>
							<th class="sortHeader" onClick="sortTable('allocation',this);" currentsort=""><span class="sortStatus"></span>Alloc.</th>
							<th class="sortHeader" onClick="sortTable('seatClass',this);" currentsort=""><span class="sortStatus"></span>Class</th>
							<th>Currency</th>
							<th>Fare</th>
						</tr>
<!--            
						<tr>
							<th colspan="99" style="border-bottom: 0px;">
								<span class="N">New</span> -
								<span class="H">Historic</span> -
								<span class="A">Agreed</span> -
								<span class="F">Fixed</span>
							</th>
						</tr>
-->
					</thead>
					<tbody>
						<tr class="line0">
							<td>
								<a datafld="seriesNo" onClick="doOpenSeries(this.href);">
									<span datafld="seriesNo"></span>
								</a>
							</td>
							<td><span datafld="gwFrom"></span></td>
							<td><span datafld="gwTo"></span></td>
							<td><span datafld="startDate"></span></td>
							<td><span datafld="endDate"></span></td>
							<td><span datafld="dayOfWeek"></span></td>
							<td><span datafld="frequency"></span></td>
							<td><span datafld="depTime"></span></td>
							<td><span datafld="arrTime"></span></td>
							<td><span datafld="flight"></span></td>
							<td><span datafld="brokerCode"></span></td>
							<td><span datafld="geminiCode"></span></td>
							<td><span dataformatas="html" datafld="dAllocation"></span></td>
							<td><span datafld="seatClass"></span></td>
							<td><span datafld="currency"></span></td>
							<td style="text-align: right;"><span datafld="fare"></span></td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<th colspan="99">
								<span class="N">New</span> -
								<span class="H">Historic</span> -
								<span class="A">Agreed</span> -
								<span class="F">Fixed</span>
							</th>
						</tr>
					</tfoot>
				</table>
</form>
</body>
</html>