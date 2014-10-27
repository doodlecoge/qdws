<%--
  Created by IntelliJ IDEA.
  User: huaiwang
  Date: 8/23/13
  Time: 1:19 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Cdets Query To Mysql Query</title>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.10.2.js"></script>
</head>
<body>
${curjob.getProject()},${curjob.getProduct()},${curjob.isForce()}<hr/>
<hr/>
<hr/>
<c:forEach items="${jobs}" var="job">
    ${job.getProject()},${job.getProduct()},${job.isForce()}<hr/>
</c:forEach>

</body>
</html>