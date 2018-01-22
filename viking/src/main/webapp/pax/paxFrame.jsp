<?xml version="1.0" encoding="UTF-8"?>
<%@ page errorPage="/ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<html:xhtml/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="eng">
<html:base/>
<head>
<title>PAX Management</title>
</head>
<noframes>This page requires frames to be viewed properly.</noframes>
<%if (request.isUserInRole("VIKING_PAX_MANAGER")) {%>
	<frameset rows="30%,*, 42%" name="_paxframe">
		<frame src="<html:rewrite page='/pax/search.do' />" target="_self" name="_search"/>
		<frame src="<html:rewrite page='/pax/results.do' />" target="_self" name="_results"/>
		<frame src="<html:rewrite page='/pax/edit.do' />" target="_self" name="_edit"/>
	</frameset>
<%}%>
</html>