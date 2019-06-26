<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Servlet TodoJspServlet</title>
    </head>
    <body>
        <c:url var="todoJspServletUrl" value="/TodoJspServlet" />
        <h1>Servlet TodoJspServlet at ${fn:escapeXml(todoJspServletUrl)}</h1>
        <form method='POST' action='${fn:escapeXml(todoJspServletUrl)}'>
            <p>
                <input type='text' name='label' id='label' autofocus='autofocus' />
                <button type='submit' name='button' id='button_add' value='add'>Add</button>
            </p>
            <c:choose>
                <%--
                empty todoList would only be true if todoList == null
                todoList.empty is not allowed because empty is a keyword
                todoList['empty'] would work
                todoList.isEmpty() works in recent el versions
                --%>
                <c:when test="${empty todoList or todoList.isEmpty()}">
                    <p id='items_empty'>The list is empty.</p>
                </c:when>
                <c:otherwise>
                    <table id='items' border='1'>
                        <%--
                        c:forEach doesn't work with an iterable
                        but it does work with collection or with an iterator
                        --%>
                        <c:forEach var="item" items="${todoList.iterator()}">
                            <c:set var="checkboxName" value="done[${item.id}]"/>
                            <c:set var="checkboxChecked" value="${item.done ? ' checked=\\\'checked\\\'' : ''}"/>
                            <c:set var="label" value="${item.label}"/>
                            <c:set var="buttonName" value="remove[${item.id}]"/>
                            <tr>
                                <td>
                                    <input type='hidden' name='__checkbox_${fn:escapeXml(checkboxName)}' value='true' />
                                    <input type='checkbox' name='${fn:escapeXml(checkboxName)}' id='${fn:escapeXml(checkboxName)}' value='true'${checkboxChecked} />
                                </td>
                                <td><label for='${fn:escapeXml(checkboxName)}'>${fn:escapeXml(label)}</label></td>
                                <td><button type='submit' name='button' value='${fn:escapeXml(buttonName)}'>Remove</button></td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:otherwise>
            </c:choose>
            <p><button type='submit' name='button' id='button_save' value='save'>Save</button></p>
            <c:url var="backUrl" value="/" />
            <p><a href='${fn:escapeXml(backUrl)}'>Go back</a>.</p>
        </form>
    </body>
</html>
