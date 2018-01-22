<%@ page contentType="text/plain;charset=UTF-8" import="java.util.*" session="true" %>
<jsp:useBean id="matchexceptions" class="uk.co.firstchoice.stella.frontend.BeanCollectionBean" scope="session"/>
<%
 
if (matchexceptions != null && matchexceptions.getBeanCollection() != null) {
  StringBuffer sb = new StringBuffer();
  sb.append("xx^atopSeasonDisp^atopSeason^atopReference:Int^matchProcessDate:Date DMY");
  sb.append("^pnr^pnr_id^departureDate:Date DMY^failType^source^dwSeatCostDisp^dwSeatCost:float");
  sb.append("^dwTaxCostDisp^dwTaxCost:float^dwTotalCostDisp^dwTotalCost:float^stellaSeatCostDisp^stellaSeatCost:float");
  sb.append("^stellaTaxCostDisp^stellaTaxCost:float^stellaTotalCostDisp^stellaTotalCost:float^unmatchedAmountDisp^unmatchedAmount:float");
  sb.append("^oldReasonCode^newReasonCode^pnrException^bspLink");

	for (Iterator i = matchexceptions.getBeanCollection().iterator(); i.hasNext(); ) {
    uk.co.firstchoice.stella.frontend.MatchExceptionsBean meb = (uk.co.firstchoice.stella.frontend.MatchExceptionsBean)i.next();
    sb.append("~^");
    sb.append(meb.getAtopSeason()+"^");
    sb.append((meb.getAtopSeason().length()==3?(meb.getAtopSeason().substring(1)+meb.getAtopSeason().charAt(0)):meb.getAtopSeason())+"^");
    sb.append(meb.getAtopReference()+"^");
    sb.append(meb.getMatchProcessDate()+"^");
    sb.append(meb.getPnr()+"^");
    sb.append(meb.getPnr_id()+"^");
    sb.append(meb.getDepartureDate()+"^");
    sb.append(meb.getFailType()+"^");
    sb.append(meb.getSource()+"^");
    sb.append(meb.getDwSeatCost()+"^");
    sb.append(meb.getDwSeatCost()+"^");
    sb.append(meb.getDwTaxCost()+"^");
    sb.append(meb.getDwTaxCost()+"^");
    sb.append(meb.getDwTotalCost()+"^");
    sb.append(meb.getDwTotalCost()+"^");
    sb.append(meb.getStellaSeatCost()+"^");
    sb.append(meb.getStellaSeatCost()+"^");
    sb.append(meb.getStellaTaxCost()+"^");
    sb.append(meb.getStellaTaxCost()+"^");
    sb.append(meb.getStellaTotalCost()+"^");
    sb.append(meb.getStellaTotalCost()+"^");
    sb.append(meb.getUnmatchedAmount()+"^");
    sb.append(meb.getUnmatchedAmount()+"^");
    sb.append(meb.getOldReasonCode()+"^");
    sb.append(meb.getNewReasonCode()+"^");
    sb.append(meb.getPnr()+";"+meb.getExceptionType()+"^");
    if (!"DWH".equals(meb.getExceptionType())) {
      sb.append('*');
    }
	}

  out.println(sb.toString());
}
%>