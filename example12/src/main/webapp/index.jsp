<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Start Page</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <h1>Hello World!</h1>
        <c:url var="helloServletUrl" value="/HelloServlet" />
        <p><a href="${fn:escapeXml(helloServletUrl)}">HelloServlet</a></p>
        <c:url var="todoJspServletUrl" value="/TodoJspServlet" />
        <p><a href="${fn:escapeXml(todoJspServletUrl)}">TodoJspServlet</a></p>
    </body>
</html>
