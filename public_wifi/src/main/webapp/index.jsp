<%@page import="db.Wifi2"%>
<%@page import="java.util.List"%>
<%@page import="db.Wifi"%>
<%@page import="db.DbTest"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>HOME</title>
<style type="text/css">
  table, td, th {
	  border : 1px solid gray;
	  border-collapse : collapse;
	};
</style>
</head>
<body>

	
	<h1>와이파이 정보 구하기</h1>
	<p><a href="index.jsp">홈</a> | <a href="history.jsp">위치 히스토리 목록</a> | <a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a></p>
	
	<form method="get" action="index.jsp" id="formList">
	LAT: <input type=text name="lat" value="0.0">, LNT: <input type="text" name="lnt" value="0.0">
	<input type="button" value="내 위치 가져오기" onclick="findLocation()">
	<input type="submit" value="근처 WIPI 정보 보기">
	</form>
    
    
  
    <script>
    
    </script>
    <p>
	<table>
		<tr style="background-color:#00AB6F; color:white">
			<th>거리[Km]</th>
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
		
		<script>
		function getNearbyWifiInfo(lat, lng) {
		    var xhr = new XMLHttpRequest();
		    xhr.open("GET", "wifiInfo?lat=" + lat + "&lng=" + lng, true);
		    xhr.onreadystatechange = function() {
		        if (xhr.readyState == XMLHttpRequest.DONE && xhr.status == 200) {
		            // 서블릿에서 처리한 결과를 여기서 받아서 처리
		            // 예: 받은 데이터를 테이블에 표시
		            var response = JSON.parse(xhr.responseText);
		            displayWifiInfo(response); // 받은 데이터를 화면에 표시하는 함수 호출
		        }
		    };
		    xhr.send();
		}
		</script>
		
		<%
		  String lat = request.getParameter("lat");
		  String lnt = request.getParameter("lnt");
		
		  if (lat == null || lnt == null || lat.equals("0.0") || lnt.equals("0.0")) {
		%>
		  <td colspan="17" style="text-align:center;">위치 정보를 입력한 후에 조회해 주세요.</td>
		<%
		  } else {
		      DbTest dbTest = new DbTest();
		      List<Wifi2> wifi2List = dbTest.getWifiList(lat, lnt);
		      if (wifi2List != null) {
		          for (Wifi2 wifi2 : wifi2List) {
		%>
		  <tr>
		      <td><%= wifi2.getDis() %></td>
		      <td><%= wifi2.getX_SWIFI_MGR_NO() %></td>
		      <td><%= wifi2.getX_SWIFI_WRDOFC() %></td>
		      <td><%= wifi2.getX_SWIFI_MAIN_NM() %></td>
		      <td><%= wifi2.getX_SWIFI_ADRES1() %></td>
		      <td><%= wifi2.getX_SWIFI_ADRES2() %></td>
		      <td><%= wifi2.getX_SWIFI_INSTL_FLOOR() %></td>
		      <td><%= wifi2.getX_SWIFI_INSTL_TY() %></td>
		      <td><%= wifi2.getX_SWIFI_INSTL_MBY() %></td>
		      <td><%= wifi2.getX_SWIFI_SVC_SE() %></td>
		      <td><%= wifi2.getX_SWIFI_CMCWR() %></td>
		      <td><%= wifi2.getX_SWIFI_CNSTC_YEAR() %></td>
		      <td><%= wifi2.getX_SWIFI_INOUT_DOOR() %></td>
		      <td><%= wifi2.getX_SWIFI_REMARS3() %></td>
		      <td><%= wifi2.getLAT() %></td>
		      <td><%= wifi2.getLNT() %></td>
		      <td><%= wifi2.getWORK_DTTM() %></td>
		  </tr>
		<%
		          }
		      }
		  }
		%>
   
	</table>
	
	<script>
        var latInput = document.getElementsByName("lat")[0];
        var lntInput = document.getElementsByName("lnt")[0];

        function findLocation() {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(showYourLocation);
                
            } else {
                alert("위치를 검색할 수 없습니다.");
            }
        }

        function showYourLocation(position) {
            var lat = position.coords.latitude;
            var lnt = position.coords.longitude;
            latInput.value = lat;
            lntInput.value = lnt;
            
            var xhr = new XMLHttpRequest();
        xhr.open("POST", "historyServlet?lat=" + lat + "&lnt=" + lnt, true);
        xhr.send();
			

        }

    </script>
</body>
</html>