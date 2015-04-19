<%--
  Created by IntelliJ IDEA.
  User: wermington
  Date: 17.4.2015
  Time: 17:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>Index page for calendar</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
</head>
<body>
  <div id="content">
    <h1>Welcome on my superpage</h1>
    <ul>
      <li>
        <a href="${pageContext.request.contextPath}/users/">
          Users
        </a>
      </li>
      <li>
        <a href="${pageContext.request.contextPath}/events/">
          Events
        </a>
      </li>
    </ul>




  </div>
</body>
</html>
