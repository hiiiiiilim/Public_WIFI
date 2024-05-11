<%@page import="java.util.List"%>
<%@page import="db.DbTest"%>
<%@page import="db.HistoryData"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>와이파이 정보 구하기</title>
<style>
 table, td, th {
	  border : 1px solid gray;
	  border-collapse : collapse;
	};
</style>
</head>
<body>
	<h1>위치 히스토리 목록</h1>
	<p><a href="index.jsp">홈</a> | <a href="">위치 히스토리 목록</a> | <a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a></p>
	<%
		DbTest dbTest = new DbTest();
		List<HistoryData> historyList = dbTest.historySelect();
	%>
	
	
	<table>
		<tr style="background-color:#00AB6F; color:white">
			<th>ID</th>
			<th>X좌표</th>
			<th>Y좌표</th>
			<th>조회일자</th>
			<th>비고</th>
		</tr>
		<%
		 for (HistoryData historyData : historyList) {
		
		%>
		<tr>
			<td><%= historyData.getHistory_id() %></td>
			<td><%= historyData.getX() %></td>
			<td><%= historyData.getY() %></td>
			<td><%= historyData.getSearch_DT() %></td>
			<td><input type="button" value="삭제" ></td>
		</tr>
		<%} %>
		
	</table>
</body>
</html>