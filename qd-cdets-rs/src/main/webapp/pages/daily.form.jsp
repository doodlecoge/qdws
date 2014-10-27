<%--
  Created by IntelliJ IDEA.
  User: huaiwang
  Date: 8/13/13
  Time: 3:17 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Get Daily Defect Count</title>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.10.2.js"></script>

    <style type="text/css">
        #ifrm {
            width: 100%;
            border: 0;
        }
    </style>
</head>
<body>



<form action="../service/daily_defects" method="post" target="ifrm">
    <table style="width: 100%">
        <tr>
            <td style="text-align: right; width: 100px;">
                Time Field:
            </td>
            <td>
                <input type="text" name="time" style="width: 60%" value="Submitted-on"/>
            </td>
        </tr>

        <tr>
            <td style="text-align: right; width: 100px;">
                Query:
            </td>
            <td>
                <textarea name="query" style="width: 90%; height: 100px;">[Product] = 'identity' AND [Project] = 'CSC.csg'</textarea>
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
            console.log(h + ",,,")
            $(this).height(h);
            var h = doc.body.scrollHeight;
            console.log(h + "---");
            $(this).height(h);
        });
    });
</script>
</body>
</html>