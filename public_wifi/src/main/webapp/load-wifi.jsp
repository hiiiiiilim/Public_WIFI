<%@page import="db.DbTest"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<style>
body{
	 text-align:center;
}
</style>
<body>
<h3>

<% 
	DbTest db = new DbTest();
	db.dbInsert();

%>

<h1><%= db.dbCount() %>개의 WIFI 정보를 정상적으로 저장하였습니다.</h1>
<a href="index.jsp">홈 으로 가기</a>
</body>
</html>