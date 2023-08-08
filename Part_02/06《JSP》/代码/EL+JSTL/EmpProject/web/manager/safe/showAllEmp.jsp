<%@ page import="com.qf.emp.entity.Emp" %>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>查询所有员工页面</title>
</head>
<body>
<table border="1">
    <tr>
        <td>编号</td>
        <td>姓名</td>
        <td>工资</td>
        <td>年龄</td>
        <td colspan="2">操作</td>
    </tr>

    <c:forEach var="emp" items="${emps}">
        <tr>
            <td>${emp.id}</td>
            <td>${emp.name}</td>
            <td>${emp.salary}</td>
            <td>${emp.age}</td>

            <td><a href="<c:url context='${pageContext.request.contextPath}' value='/manager/safe/removeEmpController?id=${emp.id}'></c:url>">删除</a></td>
            <td><a href="<c:url context='${pageContext.request.contextPath}' value='/manager/safe/showEmpController?id=${emp.id}'></c:url>">修改</a></td>

        </tr>
    </c:forEach>
</table>
</body>
</html>
