<%--
  Created by IntelliJ IDEA.
  User: huaiwang
  Date: 8/13/13
  Time: 2:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Replicate Cdets Project</title>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.10.2.js"></script>
</head>
<body>

<div>${err.message}</div>

<form method="post" target="ifrm">
    <table style="width: 100%">
        <tr>
            <td style="text-align: right; width: 100px;">
                Project:
            </td>
            <td>
                <input type="text" name="project" style="width: 60%" value="CSC.csg"/>
            </td>
        </tr>

        <tr>
            <td style="text-align: right; width: 100px;">
                Product:
            </td>
            <td>
                <input type="text" name="product" style="width: 60%" value="identity"/>
            </td>
        </tr>

        <tr>
            <td></td>
            <td>
                <input type="submit"/>
            </td>
        </tr>
    </table>
</form>


<iframe id="ifrm" frameborder="0"></iframe>

<script type="text/javascript">
    $(function() {
        $("#ifrm").load(function() {
            var doc = $(this).contents()[0];
            var h = $(doc.body).height();
            $(this).height(h);
            var h = doc.body.scrollHeight;
            $(this).height(h + 10);
        });
    });
</script>

</body>
</html>