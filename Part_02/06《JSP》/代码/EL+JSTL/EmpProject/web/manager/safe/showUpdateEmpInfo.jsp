<%@ page import="com.qf.emp.entity.Emp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>修改员工页面</title>
</head>
<body>

    <form action="<c:url context='${pageContext.request.contextPath}' value='/manager/safe/updateEmpController'></c:url>" method="post">
        编号：<input type="text" name="id" value="${emp.id}" readonly><br/>
        姓名：<input type="text" name="name" value="${emp.name}"><br/>
        工资：<input type="text" name="salary" value="${emp.salary}"><br/>
        年龄：<input type="text" name="age" value="${emp.age}"><br/>
        <input type="submit" value="修改">
    </form>
</body>
</html>
