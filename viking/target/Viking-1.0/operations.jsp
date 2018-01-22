<%@ page errorPage="/ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/vikingtags.tld" prefix="viking" %>
<jsp:useBean	id="operationsform"
							class="uk.co.firstchoice.viking.gui.OperationsFormBean"
							scope="request"/>
<html:html>
<head>
<meta http-equiv="Expires" content="Mon, 24 Jun 1996, 00:00:00 GMT">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>Operations</title>
<link href="<html:rewrite page="/css/viking.css"/>" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="<html:rewrite page="/table.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/validation.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/utils.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/datepicker.js"/>"></script>
<script>
var frm;

function doOnLoad() {
	frm = document.forms[0];
	return ;
}


function checkFocus() {
//alert(document.form[seriesIdx<%=operationsform.getSeriesId()%>_<%= operationsform.getSeriesDetailId()%>]);	
	//alert( document.getElementId(seriesIdx<%=operationsform.getSeriesId()%>_<%= operationsform.getSeriesDetailId()%>) );

	//if ( document.getElementId(seriesIdx<%=operationsform.getSeriesId()%>_<%= operationsform.getSeriesDetailId()%>)) {
	//	document.getElementId(seriesIdx<%=operationsform.getSeriesId()%>_<%= operationsform.getSeriesDetailId()%>).focus(); 
	//}
 if ( document.seriesIdx<%=operationsform.getSeriesId()%>_<%=operationsform.getSeriesDetailId()%> ) {
	  document.seriesIdx<%=operationsform.getSeriesId()%>_<%= operationsform.getSeriesDetailId()%>.actualArr.focus(); 
  }
}

function doOpenSeries(no) {
	event.returnValue = false;
	var maxWidth = parseInt(window.screen.width*1);
	var win = window.open("editseries.do?seriesNo="+no,'',
				'top=0,left=0,width='+window.screen.width+',height='+window.screen.height+
				',scrollbars=yes,menubar=yes,location=yes,status=yes,toolbar=no,resizable=yes',false);
	win.moveTo(-5, -5);
	win.resizeTo(win.screen.availWidth+10, win.screen.availHeight+9);
	win.focus();
}   

function checkTime(time) {
  if (time.indexOf("-") == -1 && time.indexOf("+") == -1) {
    return validTime(time);
  } else {
    var prefix = time.indexOf("-") > -1?"-":"+";
    var arr = time.split(prefix);
    var t = validTime(arr[0]);
    var d = parseInt(arr[1]);
    if (t != "" && arr[1] == d) {
      return t+prefix+d;
    } else {
      return "";
    }
  }
}

function validateSearch() {
  return true;
}

function doChange(field) {
  var updatingStyle = window.document.all["updatingId"].style;
	updatingStyle.top = document.body.offsetHeight / 2;
	updatingStyle.width = 100;
	updatingStyle.left = (document.body.offsetWidth - 100) / 2;
	updatingStyle.display = "block";
//    frm.seriesId.value = field.form.seriesId.value;
	frm.seriesId.value = field.form.seriesId.value;
    frm.seriesDetailId.value = field.form.seriesDetailId.value;
    if (field.name == 'comments') {
       frm.comments.value = field.form.comments.value;
       frm.departureDate.value = field.form.departureDate.value;
       frm.action.value = "savecomments";
	}
   else {
	   var formId = field.form.seriesId.value;
	   form1 = eval("frm" + formId);
	   estimateOrActual =  form1.elements["estimateOrActual" + formId];
	   if (estimateOrActual.checked) {
		   frm.estimateOrActual.value = "A";
	   }
	   else {
		   frm.estimateOrActual.value = "E";
	   }
	   if (field.name.substring(0,16) == 'estimateOrActual') {
		  frm.allSeriesDetailId.value = form1.elements["seriesdetailallid"].value;
          frm.action.value = "saveestimateoractual";
       }
	   else {
	       var time = field.form.actualDep.value;
	       if (time.indexOf("-") == -1 && time.indexOf("+") == -1) {
	          frm.actualDep.value = time;
	          frm.depOvernight.value = "";
	       } else {
	          var prefix = time.indexOf("-") > -1?"-":"+";
	          var arr = time.split(prefix);
	          var t = validTime(arr[0]);
	          var d = parseInt(arr[1]);
	          if (t != "" && arr[1] == d) {
	             frm.actualDep.value = t;
	             frm.depOvernight.value = (prefix=="-"?"-":"")+d;
	          } else {
	            frm.actualDep.value = "";
	            frm.depOvernight.value = "";
	         }
      	}
	    time = field.form.actualArr.value;
	    if (time.indexOf("-") == -1 && time.indexOf("+") == -1) {
	      frm.actualArr.value = time;
	      frm.arrOvernight.value = "";
	    } else {
	      var prefix = time.indexOf("-") > -1?"-":"+";
	      var arr = time.split(prefix);
	      var t = validTime(arr[0]);
	      var d = parseInt(arr[1]);
	      if (t != "" && arr[1] == d) {
	        frm.actualArr.value = t;
	        frm.arrOvernight.value = (prefix=="-"?"-":"")+d;
	      } else {
	        frm.actualArr.value = "";
	        frm.arrOvernight.value = "";
	      }
	    }
    
//    frm.actualDep.value = field.form.actualDep.value;
//    frm.depOvernight.value = field.form.depOvernight.value;
//    frm.actualArr.value = field.form.actualArr.value;
//    frm.arrOvernight.value = field.form.arrOvernight.value;
    frm.action.value = "savetimings";
	}
  }
  frm.submit();
}
</script>

<style>
td { text-align: center; }
th { vertical-align: middle; }
</style>
</head>
<body onLoad="doOnLoad();" id="theBody">
<html:form action="operations.do">
<input type="hidden" name="action"/>
<input type="hidden" name="seriesId"/>
<input type="hidden" name="seriesDetailId"/>
<input type="hidden" name="actualDep"/>
<input type="hidden" name="depOvernight"/>
<input type="hidden" name="actualArr"/>
<input type="hidden" name="arrOvernight"/>
<input type="hidden" name="comments"/>
<input type="hidden" name="departureDate"/>
<input type="hidden" name="estimateOrActual"/> 
<input type="hidden" name="allSeriesDetailId"/>
<h3>Operations - Actual Flight Timings</h3>
<h6><bean:write property="error" name="operationsform"/></h6>
<h6 id="updatingId" style="display:none; position:absolute;">Updating</h6>
<table cellspacing="0" cellpadding="0" class="innerTabTable" border="1" style="width: 100%; border: 0px; margin: 0px;">
	<tr>
		<th>Date From:</th>
		<td>
			<html:text	property="startDate"
									name="operationsform"
									size="12"
									maxlength="10"
									ondblclick="show_calendar(frm.name+'.startDate');"/>
		</td>
		<th>To:</th>
		<td>
			<html:text	property="endDate"
									name="operationsform"
									size="12"
									maxlength="10"
									ondblclick="show_calendar(frm.name+'.endDate');"/>
		</td>
		<th>From Time:</th>
		<td>
			<html:text	property="fromTime"
									name="operationsform"
									size="9"
									maxlength="7"
                  onchange="this.value=validTime(this.value)" />
		</td>
		<th>Only incomplete:</th>
    		<td>
		      <html:checkbox name="operationsform" property="onlyIncomplete" />
		    </td>
		<th>Exclude Carrier</th>
			<td>
             <html:checkbox name="operationsform" property="incExcCarrierInd" />
			</td>
		<th>Carrier Code:</th>
		<td>
			<html:text	property="excludeCarrier"
									name="operationsform"
									size="17"
									maxlength="15"    />

                  <!--  onchange="this.value=this.value.toUpperCase();"  -->

		</td>
		<th>Gatewaycode:</th>
		<td> <html:text	property="gatewayCode"
									name="operationsform"
									size="3"
									maxlength="3"
                  onchange="this.value=this.value.toUpperCase();" /></td>
                  
    <th><button onclick="if (validateSearch()) this.form.submit();">Search</button></th>
    <th><button style="color: red;" onclick="window.close();">Close</button></th>
	</tr>
</table>  
</html:form>
<% if (operationsform.getSeriesIds().length > 0) { %>
<table cellspacing="0" cellpadding="2" border="1" >
  <tr>
    <th></th>
    <th>Series No</th>
    <th>Dep. Date</th>
    <th>Route</th>
<% for (int i = 0; i < operationsform.getMaxSectors(); i++) { %>
    <th>Dep</th>
    <th>Arr</th>
<% } %>
    <th>Supp</th>
    <th>Sharers</th>
  </tr>
<% 
  String roStr = request.isUserInRole("VIKING_OPERATIONS")?"":" readonly='true' ";
  String roDisableStr = request.isUserInRole("VIKING_OPERATIONS")?"":" disabled ";
  for (int seriesIdx = 0; seriesIdx < operationsform.getSeriesIds().length; seriesIdx++) { 
    uk.co.firstchoice.viking.gui.OperationsSeriesBean osb = 
      (uk.co.firstchoice.viking.gui.OperationsSeriesBean)operationsform.getSeries().get(operationsform.getSeriesIds()[seriesIdx]);
%>
  <tr>
    <th>Flight Details</th>
    <td><a onClick="doOpenSeries(<%= osb.getSeriesNo() %>);"><%= osb.getSeriesNo() %></a>
<!--    <input type="text" name="seriesIdx<%=osb.getSeriesNo()%>"/>-->
	</td>
    <td><%= osb.getDepartureDate() %></td>
    <td><%= osb.getRoute() %></td>
<%
    String prevFlightNo = "";
    for (java.util.Iterator i = osb.getSectors().iterator(); i.hasNext(); ) {
      uk.co.firstchoice.viking.gui.OperationsSectorBean osecb = 
        (uk.co.firstchoice.viking.gui.OperationsSectorBean)i.next();
      out.print("<td>");
      if (!prevFlightNo.equals(osecb.getFlightNo())) {
        prevFlightNo = osecb.getFlightNo();
        out.print(prevFlightNo);
      }
      out.print("</td><td></td>");
    }
    if (osb.getSectors().size() < operationsform.getMaxSectors()) {
      out.print("<td colspan='"+(operationsform.getMaxSectors()-osb.getSectors().size())*2+"'></td>");
    }
%>
    <td><%= osb.getSupplier() %></td>
    <td><%= osb.getSharers() %></td>
  </tr>
  <tr>
    <th>Sch Flt Time</th>
    <td></td>
    <td></td>
    <td></td>
<%
    for (java.util.Iterator i = osb.getSectors().iterator(); i.hasNext(); ) {
      uk.co.firstchoice.viking.gui.OperationsSectorBean osecb = 
        (uk.co.firstchoice.viking.gui.OperationsSectorBean)i.next();
      out.print("<td>"+osecb.getScheduledDep()+"</td>");
      out.print("<td>"+osecb.getScheduledArr()+"</td>");
    }
%>
  </tr>
  <tr>
    <th>Est / Act Flt Time</th>
    <td></td>
    <td></td>
<%
	StringBuffer detailid = null;
    int sectorIdx = 0;
	String firstdetailId = null;
    for (java.util.Iterator i = osb.getSectors().iterator(); i.hasNext(); ) {
      uk.co.firstchoice.viking.gui.OperationsSectorBean osecb = 
        (uk.co.firstchoice.viking.gui.OperationsSectorBean)i.next();
%>
 	  <% 
	if ( sectorIdx == 0) { %>
	  <form name="frm<%=osb.getSeriesNo()%>">
	  <td>Actual :  <input type="checkbox" name="estimateOrActual<%=osb.getSeriesNo()%>"   <%=roDisableStr%>
					<% if (osecb.getEstimateOrActual().equals("A")) { %> checked <% } %>
					onclick='doChange(this);'
		/>
      <input type="hidden" name="seriesId" value="<%= osb.getSeriesNo() %>"/>
	  <input type="hidden" name="seriesDetailId" value="<%= osecb.getSeriesDetailId() %>"/>
	  <input type="hidden" name="seriesdetailallid" value="">
	  <% firstdetailId =  osecb.getSeriesDetailId(); %>
	  </td>
      </form> 
	<% } %> 
      <form name="seriesIdx<%=osb.getSeriesNo()%>_<%= osecb.getSeriesDetailId()%>" >
      <td>
        <input type="hidden" name="seriesId" value="<%= osb.getSeriesNo() %>"/>
        <input type="hidden" name="seriesDetailId" value="<%= osecb.getSeriesDetailId() %>"/>
        <input type='text'
               name='actualDep'
               <%= roStr %>
               size='7' 
               maxlength='7' 
               value='<%= osecb.getActualDep()+osecb.getDepOvernight() %>' 
               onchange='this.value=checkTime(this.value); doChange(this);'>
      </td>
<!--
      <td><input type='text'
                 name='depOvernight' 
                 size='2' 
                 maxlength='2' 
                 value='<%= osecb.getDepOvernight() %>' 
                 onchange='doChange(this);'></td>
-->
      <td><input type='text'
                 name='actualArr' 
                 size='7' 
                 <%= roStr %>
                 maxlength='7' 
                 value='<%= osecb.getActualArr()+osecb.getArrOvernight() %>' 
                 onchange='this.value=checkTime(this.value); doChange(this);'></td>
<!--
      <td><input type='text'
                 name='arrOvernight' 
                 size='2' 
                 maxlength='2' 
                 value='<%= osecb.getArrOvernight() %>' 
                 onchange='doChange(this);'></td>
-->
      </form>
<%   
	 if (sectorIdx !=0 ) {
	   detailid.append(",");
	 }
	 else {
	   detailid = new StringBuffer(); 
	 }
	 detailid.append(osecb.getSeriesDetailId());
     sectorIdx++;
    } %>
	<% if (detailid != null) { %>
		<script>
	   	form1 = eval("frm" + <%=osb.getSeriesNo()%>);
	   	seriesdetailallid = form1.elements["seriesdetailallid"];
		eval(seriesdetailallid.value="<%=detailid%>");
		</script>
	<% } %>
	<!--  </form>-->
  </tr>
  <tr>
    <th>Comments</th>
    <form onblur="alert(1);">
    <td colspan="<%= 5+operationsform.getMaxSectors()*2 %>">
      <input type="hidden" name="seriesId" value="<%= osb.getSeriesNo() %>" />
      <input type="hidden" name="departureDate" value="<%= osb.getDepartureDate() %>" />
      <input type="text"
             <%= roStr %>
             name="comments"
             style="width: 100%; color: red; "
             value="<%= osb.getComments() %>"
             onchange='doChange(this);' />
	 <input type="hidden" name="seriesDetailId" value="<%= firstdetailId %>"/>
    </td>
    </form>
  </tr>
  <tr>
    <th><%= seriesIdx + 1 %>&nbsp;</th>
    <td colspan="<%= 5+operationsform.getMaxSectors()*2 %>">&nbsp;</td>
  </tr>
<% } %>
</table>
<script>checkFocus();</script>
<% } else { %>
<h6>No departures found in the specified period</h6>
<% } %>
</body>
</html:html>