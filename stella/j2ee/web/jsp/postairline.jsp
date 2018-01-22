<%@ page errorPage="jsp/ErrorPage.jsp" %>
<jsp:useBean id="airlinedetails" class="uk.co.firstchoice.stella.frontend.AirlineBean" scope="request">
<jsp:setProperty name="airlinedetails" property="*"/>
</jsp:useBean>
<jsp:forward page="/stellabsp?cmd=saveairlinedetails" />