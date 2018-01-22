<%@ page errorPage="ErrorPage.jsp" %>
<jsp:useBean id="search" class="uk.co.firstchoice.stella.frontend.SearchBean" scope="request">
<jsp:setProperty name="search" property="*"/>
</jsp:useBean>
<link href="stella.css" rel="stylesheet" type="text/css">
<jsp:forward  page="/stellabsp?cmd=dosearch" />