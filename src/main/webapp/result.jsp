<%--
  Created by IntelliJ IDEA.
  User: thdefn
  Date: 2023/02/10
  Time: 1:34 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>result</title>
    <script>
        var id = ${id}; //row 삭제 처리를 위한 hist_id 받아오기
        parent.deleteRow(id);
    </script>
</head>
<body>
</body>
</html>
