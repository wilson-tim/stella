<%@ page errorPage="ErrorPage.jsp" %>
<jsp:useBean id="refund" class="uk.co.firstchoice.stella.frontend.RefundBean" scope="request">
<jsp:setProperty name="refund" property="*"/>
</jsp:useBean>
<jsp:forward page="/stellabsp"/>
