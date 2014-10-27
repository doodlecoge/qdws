<%--
  Created by IntelliJ IDEA.
  User: huaiwang
  Date: 13-11-7
  Time: 下午1:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Qddts Health Cheack</title>

    <style type="text/css">
        .tbl {
            table-layout: fixed;
            border-collapse: collapse;
        }

        .tbl td {
            border: 1px solid #aaa;
            vertical-align: top;
            padding: 5px 10px;
        }
    </style>
</head>
<body>
<table class="tbl">
    <tr>
        <td style="width: 120px;">Current Query:</td>
        <td>
            ${currentQuery}
        </td>
    </tr>

    <tr>
        <td>Waiting Queries:</td>
        <td>
            <c:choose>
                <c:when test="${fn:length(waitingQueries) > 0}">
                    <ol>
                        <c:forEach var="query" items="${waitingQueries}">
                            <li>${query}</li>
                        </c:forEach>
                    </ol>
                </c:when>
                <c:otherwise>
                    no waiting queries
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
</table>
</body>
</html>