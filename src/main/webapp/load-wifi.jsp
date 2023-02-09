<%@ page import="com.zerobase.publicwifi.WifiService" %><%--
  Created by IntelliJ IDEA.
  User: thdefn
  Date: 2023/02/08
  Time: 7:47 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>와이파이 정보 구하기</title>
    <style>
        body{
            margin-left: 0; margin-right: 0; height: 200vh;
        }
        .wrapper{
            width: 100%;
            text-align: center;
            margin: 5px;
        }
    </style>
</head>
<body>
<%
    WifiService wifiService = new WifiService();
    int result = wifiService.loadWifi();
%>
<div class="wrapper">
    <br/>
    <h1 id="title"> <%=result%>개의 WIFI 정보를 정상적으로 저장하였습니다.</h1>
    <br/>
    <a href="index.jsp">홈 으로 가기</a>
</div>
</body>
</html>
