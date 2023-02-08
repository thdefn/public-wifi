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
            border: 1px solid #ddd;
            padding: 8px;
        }
        #table tr:nth-child(even){
            background-color: #f2f2f2;
        }
        #table th{
            padding-top: 12px; padding-bottom: 12px; text-align: center; background-color: #04AA6D; color: white; padding-left: 5px; padding-right: 5px;
        }
        #table td tr:empty::before{content : "위치 정보를 입력한 후에 조회해주세요"; text-align: center; column-span: all; }


    </style>
</head>

<body>
<h1>와이파이 정보 구하기</h1><br/>
<div class="menu">
    <div class="item"> 홈 </div>
    <div class="item"> 위치 히스토리 목록 </div>
    <div class="item last"> <a href="load-wifi.jsp"> Open API 와이파이 정보 가져오기 </a> </div>
</div><br/>

<form>
    <label for="lat"> LAT: </label>
    <input type="text" id="lat" value="0.0">
    <label for="lnt">, LNT: </label>
    <input type="text" id="lnt" value="0.0">
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
    <tr>
    </tr>
    </tbody>
</table>
</body>
</html>