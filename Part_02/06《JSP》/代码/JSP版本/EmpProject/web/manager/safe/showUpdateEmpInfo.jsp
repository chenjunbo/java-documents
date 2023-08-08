<%@ page import="com.qf.emp.entity.Emp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>修改员工页面</title>
</head>
<body>
    <%
        Emp emp = (Emp)request.getAttribute("emp");
    %>
    <form action="/empproject/manager/safe/updateEmpController" method="post">
        编号：<input type="text" name="id" value="<%=emp.getId()%>" readonly><br/>
        姓名：<input type="text" name="name" value="<%=emp.getName()%>"><br/>
        工资：<input type="text" name="salary" value="<%=emp.getSalary()%>"><br/>
        年龄：<input type="text" name="age" value="<%=emp.getAge()%>"><br/>
        <input type="submit" value="修改">
    </form>
</body>
</html>
