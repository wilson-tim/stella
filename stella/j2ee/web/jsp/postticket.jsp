<%@ page errorPage="ErrorPage.jsp" %>
<jsp:useBean id="ticket" class="uk.co.firstchoice.stella.frontend.TicketBean" scope="request">
<jsp:setProperty name="ticket" property="*"/>
</jsp:useBean>
<jsp:forward page="/stellabsp?cmd=saveticket" />