<%@ page import="com.zerobase.publicwifi.Wifi" %>
<%@ page import="com.zerobase.publicwifi.WifiService" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>와이파이 정보 구하기</title>
    <script type="text/javascript">

        function success(position){
            var crd = position.coords;
            document.getElementById("lat").value = crd.latitude;
            document.getElementById("lnt").value = crd.longitude;
            console.log(`latitude : ${crd.latitude}`);
            console.log(`longitude : ${crd.longitude}`);
        }

        function error(error){
            console.warn(`error(${err.code}): ${err.message}`);
        }

        function getLocation(){
            navigator.geolocation.getCurrentPosition(success, error);
        }

        function sendDataforHistory(){
            //history.jsp에 위도 경도 전송
            let latTag = document.form.lat;
            let lntTag = document.form.lnt;

            //적합한 값인지 체크
            if(latTag == null || latTag.value == "0.0"){
                alert("위도 값을 입력하세요")
                latTag.focus();
                return false;
            }

            if(lntTag == null || lntTag.value == "0.0"){
                alert("경도 값을 입력하세요")
                lntTag.focus();
                return false;
            }

            //적합한 값이면 새로운 form을 만들어 전송
            let f = document.createElement('form');

            let lat;
            lat = document.createElement('input');
            lat.setAttribute('type','hidden');
            lat.setAttribute('name','lat');
            lat.setAttribute('value',latTag.value);

            let lnt;
            lnt = document.createElement('input');
            lnt.setAttribute('type','hidden');
            lnt.setAttribute('name','lnt');
            lnt.setAttribute('value',lntTag.value);

            f.appendChild(lat);
            f.appendChild(lnt);
            f.setAttribute('method','post');
            f.setAttribute('action',"/history.jsp");

            document.body.appendChild(f);
            f.submit();
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
        #table td:empty::before{content : "위치 정보를 입력한 후에 조회해주세요"; text-align: center; column-span: all; }
        #table th td:empty::before{ align-items: center; }


    </style>
</head>

<body>
<%
    boolean isSubmit = false;
    Float latitude = null;
    Float longitude = null;
    String lat = request.getParameter("lat");
    String lnt = request.getParameter("lnt");
    if(lat!=null && lnt!=null){
        latitude = Float.valueOf(lat);
        longitude = Float.valueOf(lnt);
        isSubmit = true;
    }else {
        lat = "0.0";
        lnt = "0.0";
    }
%>

<h1>와이파이 정보 구하기</h1><br/>
<div class="menu">
    <div class="item"> <a href="index.jsp"> 홈 </a> </div>
    <div class="item"> <a href="javascript:void(0)" onclick="sendDataforHistory()"> 위치 히스토리 목록 </a> </div>
    <div class="item last"> <a href="load-wifi.jsp"> Open API 와이파이 정보 가져오기 </a> </div>
</div><br/>

<form method="get" name="form">
    <label for="lat"> LAT: </label>
    <input type="text" id="lat" value='<%=lat%>' name="lat">
    <label for="lnt">, LNT: </label>
    <input type="text" id="lnt" value='<%=lnt%>' name="lnt">
    <input type="button" id="loc" value="내 위치 가져오기" onclick="getLocation()">
    <input type="submit" id="info" value="근처 WIFI 정보보기">
</form><br/>

<table id="table">
    <thead>
    <tr>
        <th>거리(km)</th>
        <th>관리번호</th>
        <th>자치구</th>
        <th>와이파이명</th>
        <th>도로명주소</th>
        <th>상세주소</th>
        <th>설치위치(층)</th>
        <th>설치유형</th>
        <th>설치기관</th>
        <th>서비스구분</th>
        <th>망종류</th>
        <th>설치년도</th>
        <th>실내외구분</th>
        <th>WIFI접속환경</th>
        <th>X좌표</th>
        <th>Y좌표</th>
        <th>작업일자</th>
    </tr>
    </thead>
    <tbody>
    <%
        if(isSubmit){
            WifiService wifiService = new WifiService();
            List<Wifi> wifiList = wifiService.getCloseList(latitude,longitude);
            wifiService.addHistory(latitude, longitude);

            for (Wifi wifi:wifiList) {

    %>
    <tr>
        <td> <%=wifi.getDistance()%></td>
        <td> <%=wifi.getManageId()%></td>
        <td> <%=wifi.getDistrict()%></td>
        <td> <%=wifi.getName()%></td>
        <td> <%=wifi.getRoadAddress()%></td>
        <td> <%=wifi.getDetailAddress()%></td>
        <td> <%=wifi.getFloor()%></td>
        <td> <%=wifi.getInstallType()%></td>
        <td> <%=wifi.getInstallAgency()%></td>
        <td> <%=wifi.getService()%></td>
        <td> <%=wifi.getNetwork()%></td>
        <td> <%=wifi.getYear()%></td>
        <td> <%= wifi.isIndoor()?"실내":"실외"%></td>
        <td> <%=wifi.getEnvironment()%></td>
        <td> <%=wifi.getxCoord()%></td>
        <td> <%=wifi.getyCoord()%></td>
        <td> <%=wifi.getWorkAt()%></td>
    </tr>
    <%
            }
        }
        else{
    %>

    <tr>
        <td colspan="17"></td>
    </tr>

    <%
        }
    %>

    </tbody>
</table>
</body>
</html>