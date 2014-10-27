<%--
  Created by IntelliJ IDEA.
  User: huaiwang
  Date: 8/14/13
  Time: 1:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Health Control</title>
</head>
<body>
<form method="post">
    Waiting Jobs: ${waitingJobs}
    <hr />

    Current Job: ${currentJob}
    <hr />

    <c:choose>
        <c:when test="${state}">
            Stopped
            <input type="hidden" name="state" value="false"/>
            <input type="submit" value="Run" />
        </c:when>
        <c:otherwise>
            Running
            <input type="hidden" name="state" value="true"/>
            <input type="submit" value="Stop" />
        </c:otherwise>
    </c:choose>
</form>
</body>
</html>