<%@ page import="com.zerobase.publicwifi.WifiService" %>
<%@ page import="java.util.List" %>
<%@ page import="com.zerobase.publicwifi.History" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: thdefn
  Date: 2023/02/09
  Time: 6:03 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>와이파이 정보 구하기</title>
    <script type="text/javascript">
        function deleteRow(id){
            let row = document.getElementById(id);
            row.parentNode.removeChild(row);
        }
    </script>
    <style>
        .menu:after{clear: both; display: block; content: ''}
        .item{
            float: left; text-align: center; border-right: 1px solid black; padding-left: 5px; padding-right: 5px;
        }
        .item.last{
            float: left; border-right: none; padding-left: 5px;
        }
        #table {
            width: 100%; border-collapse: collapse; font-family: Arial, Helvetica, sans-serif;
        }
        #table td, #table th{
            border: 1px solid #ddd; padding: 8px; text-align: left;
        }
        #table tr:nth-child(even){
            background-color: #f2f2f2;
        }
        #table th{
            padding-top: 12px; padding-bottom: 12px; text-align: center; background-color: #04AA6D; color: white; padding-left: 5px; padding-right: 5px;
        }
    </style>
</head>
<body>
<%
    String lat = request.getParameter("lat");
    String lnt = request.getParameter("lnt");
    List<History> historyList = new ArrayList<>();

    if(lat != null && lnt != null) {

        Float latitude = Float.valueOf(lat);
        Float longitude = Float.valueOf(lnt);

        WifiService wifiService = new WifiService();
        historyList = wifiService.getHistory(latitude, longitude);
    }
%>

<h1>위치 히스토리 목록</h1><br/>
<div class="menu">
    <div class="item"> <a href="index.jsp"> 홈 </a> </div>
    <div class="item"> <a href="history.jsp"> 위치 히스토리 목록 </a> </div>
    <div class="item last"> <a href="load-wifi.jsp"> Open API 와이파이 정보 가져오기 </a> </div>
</div><br/>

<table id="table">
    <thead>
    <tr>
        <th>ID</th>
        <th>X좌표</th>
        <th>Y좌표</th>
        <th>조회일자</th>
        <th>비고</th>
    </tr>
    </thead>
    <tbody>
    <%
        for (History history : historyList) {
    %>
    <tr id='<%=history.getId()%>'>
        <td><%=history.getId()%></td>
        <td><%=history.getxCoord()%></td>
        <td><%=history.getyCoord()%></td>
        <td><%=history.getCreatedAt()%></td>
        <td>
            <form action="hs.do" method="get" target="mock">
                <input type="hidden" name="hist_id" value='<%=history.getId()%>'>
                <input type='submit' value='삭제'>
            </form>
        </td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>

<iframe name="mock" style="display: none;"></iframe>

</body>
</html>
