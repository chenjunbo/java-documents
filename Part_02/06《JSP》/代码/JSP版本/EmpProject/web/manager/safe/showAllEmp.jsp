<%@ page import="com.qf.emp.entity.Emp" %>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <%
        List<Emp> emps = (List<Emp>) request.getAttribute("emps");
        for (Emp emp : emps) {


    %>
        <tr>
            <td><%=emp.getId()%></td>
            <td><%=emp.getName()%></td>
            <td><%=emp.getSalary()%></td>
            <td><%=emp.getAge()%></td>
            <td><a href="<%=request.getContextPath()+"/manager/safe/removeEmpController?id="+emp.getId()%>">删除</a></td>
            <td><a href="<%=request.getContextPath()+"/manager/safe/showEmpController?id="+emp.getId()%>">修改</a></td>
        </tr>
    <%
        }
    %>
</table>
</body>
</html>
