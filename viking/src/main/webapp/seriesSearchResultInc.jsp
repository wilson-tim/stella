<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<jsp:useBean	id="searchform"
							class="uk.co.firstchoice.viking.gui.SearchFormBean"
							scope="session"/>
<html>
<html:base/>
<head>
<link href="<html:rewrite page="/css/viking.css"/>" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="<html:rewrite page="/table.js"/>"></script>
<%@ include file="/searchResultHeader.html" %>
</head>
<body>
<form>
<object id="dsoSearch" classid="clsid:333C7BC4-460F-11D0-BC04-0080C7055A83">
	<param name="DataURL" value="searchCSV.jsp" />
	<param name="UseHeader" value="true" />
	<param name="FieldDelim" value="^" />
	<param name="RowDelim" value="~" />
</object>
<% int numCustCol = searchform.getMaxNumCustomers(); %>
				<table class="datatable" id="searchresult" datasrc="#dsoSearch" cellspacing="0" cellpadding="4" style="width: 100%;">
					<thead>
						<tr class="noScreen">
							<th colspan="99" style="border: 0px;">
								<h3>Series Search</h3>
							</th>
						</tr>
						<tr class="noPrint">
							<th colspan="<%= numCustCol*2 %>" style="text-align: left; border-right: 0px;">
								Sort Order: <span id="sortId" style="color: black;">-NONE-</span> (<a href="javascript:resetSort();">reset</a>)
							</th>
							<th colspan="3" style="text-align: center; border-left: 0px;">
								Total: <%= searchform.getSearchResult().size() %>
							</th>
							<th colspan="3" style="text-align: center; border-left: 0px;">
								<a href="#" onClick="print();">Print</a>
							</th>
              <th colspan="6"></th>
						</tr>
						<tr>
							<th class="sortHeader" rowspan="2" onClick="sortTable('seriesNo',this);" currentsort=""><span class="sortStatus"></span>#</th>
							<th class="sortHeader" rowspan="2" onClick="sortTable('route',this);" currentsort=""><span class="sortStatus"></span>Route</th>
							<th class="sortHeader" rowspan="2" onClick="sortTable('dow',this);" currentsort=""><span class="sortStatus"></span>DOW</th>
							<th class="sortHeader" rowspan="2" onClick="sortTable('startDate',this);" currentsort=""><span class="sortStatus"></span>Start<br/>Date</th>
							<th class="sortHeader" rowspan="2" onClick="sortTable('endDate',this);" currentsort=""><span class="sortStatus"></span>End<br/>Date</th>
							<th class="sortHeader" rowspan="2" onClick="sortTable('depTime',this);" currentsort=""><span class="sortStatus"></span>Dep<br/>Time</th>
							<th class="sortHeader" rowspan="2" onClick="sortTable('flightNumber',this);" currentsort=""><span class="sortStatus"></span>Flight<br/>Number</th>
							<th class="sortHeader" rowspan="2" onClick="sortTable('supplier',this);" currentsort=""><span class="sortStatus"></span>Supp</th>
							<th class="sortHeader" rowspan="2" onClick="sortTable('carrier',this);" currentsort=""><span class="sortStatus"></span>Carr</th>
							<th colspan="2" style="border-bottom: 0px;">Purchased</th>
							<th colspan="2" style="border-bottom: 0px;">Available</th>
							<th colspan="<%= numCustCol*2 %>" style="border-bottom: 0px;">Customers</th>
						</tr>
						<tr>
							<th class="sortHeader" style="border-top: 0px;" onClick="sortTable('purchasedMin',this);" currentsort=""><span class="sortStatus"></span>Min</th>
							<th class="sortHeader" style="border-top: 0px;" onClick="sortTable('purchasedMax',this);" currentsort=""><span class="sortStatus"></span>Max</th>
							<th class="sortHeader" style="border-top: 0px;" onClick="sortTable('availableMin',this);" currentsort=""><span class="sortStatus"></span>Min</th>
							<th class="sortHeader" style="border-top: 0px;" onClick="sortTable('availableMax',this);" currentsort=""><span class="sortStatus"></span>Max</th>
							<% for (int idx=0; idx<numCustCol; idx++) { %>
								<th colspan="2" style="border-top: 0px;">&nbsp;</th>
							<% } %>
						</tr>
					</thead>
					<tbody>
						<tr class="line0">
							<td>

								<a datafld="seriesNo" onClick="doOpenSeries(this.href);">
									<span datafld="seriesNo"></span>
								</a>
							</td>
							<td nowrap="true" style="text-align: left;">
               					 <span dataformatas="html" datafld="route"></span>
        			        </td>
							<td><span datafld="dow"></span></td>
							<td><span datafld="startDate"></span></td>
							<td><span datafld="endDate"></span></td>
							<td><span datafld="depTime"></span></td>
							<td nowrap="true" style="text-align: center;"><span dataformatas="html"  datafld="flightNumber"></span></td>
							<td><span datafld="supplier"></span></td>
							<td><span datafld="carrier"></span></td>
							<td nowrap>
								<a datafld="seriesNo" onClick="showDetails(this.href+';Purchased');">
									<span dataformatas="html" datafld="dPurchasedMin"></span>
								</a>
							</td>
							<td nowrap>
								<a datafld="seriesNo" onClick="showDetails(this.href+';Purchased');">
									<span dataformatas="html" datafld="dPurchasedMax"></span>
								</a>
							</td>
							<td nowrap>
								<a datafld="seriesNo" onClick="showDetails(this.href+';Available');">
									<span dataformatas="html" datafld="dAvailableMin"></span>
								</a>
							</td>
							<td nowrap>
								<a datafld="seriesNo" onClick="showDetails(this.href+';Available');">
									<span dataformatas="html" datafld="dAvailableMax"></span>
								</a>
							</td>


<% for (int idx=0; idx<numCustCol; idx++) { %>

							<td nowrap>
								<a datafld="custKey<%= idx+1 %>" onClick="showDetails(this.href);">
									<span dataformatas="html" datafld="custName<%= idx+1 %>"></span>
								</a>&nbsp;
							</td>
							<td nowrap>
								<a datafld="custKey<%= idx+1 %>" onClick="showDetails(this.href);">
									<span dataformatas="html" datafld="custMinMax<%= idx+1 %>"></span>
								</a>&nbsp;
							</td>
<% } %>
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