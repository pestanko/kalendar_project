<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: wermington
  Date: 17.4.2015
  Time: 18:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>List users</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css"/>
</head>
<body>
  <div id="content">
    <table>
      <tr>
        <th> First name </th>
        <th> Last name </th>
        <th> User name </th>
        <th> Email </th>
        <th> Mobile number</th>
        <th> Address </th>

        <th> Delete </th>
        <th> Update </th>
      </tr>
      <c:forEach items="${Users}"  var="user">
        <tr>
          <td><c:out value="${user.firstName}"/></td>
          <td><c:out value="${user.lastName}"/></td>
          <td><c:out value="${user.userName}"/> </td>
          <td><c:out value="${user.email}"/></td>
          <td><c:out value="${user.mobileNumber}"/></td>
          <td><c:out value="${user.address}"/> </td>
          <td>
            <form method="post" action="${pageContext.request.contextPath}/users/delete?uid=${user.id}">
              <input class="delete" type="submit" value="Delete">
            </form>
          </td>
          <td>
            <form method="post" action="${pageContext.request.contextPath}/users/update?uid=${user.id}">
              <input class="update" type="submit" value="Update">
            </form>
          </td>
        </tr>
      </c:forEach>
    </table>

    <c:if test="${not empty Error}">

      <div class="error">
        <h4>Errors: </h4>
       <em> <c:out value="${Error}" escapeXml="false" /></em>
      </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/users/add?update_flag=${update_flag}&uid=${update_user_id}">
      <table id="add_user">
        <tr>
          <td>First Name</td>
          <td>
            <input type="text" maxlength="40" name="firstName" pattern="^[a-zA-Z]*$" value="${update_sel_user.firstName}" />
          </td>
        </tr>
        <tr>
          <td>Last Name</td>
          <td>
            <input type="text" maxlength="40" name="lastName" pattern="^[a-zA-Z]*$" value="${update_sel_user.lastName}" />
          </td>
        </tr>
        <tr>
          <td>User Name</td>
          <td>
            <input type="text" maxlength="40" name="userName" pattern="^[a-zA-Z0-9._]*$" value="${update_sel_user.userName}" />
          </td>
        </tr>
        <tr>
          <td>Email</td>
          <td><input type="email" maxlength="70" name="email" value="${update_sel_user.email}" />
          </td>
        </tr>
        <tr>
          <td>Mobile number</td>
          <td><input type="text" maxlength="25" name="mobileNumber" value="${update_sel_user.mobileNumber}" />
          </td>
        </tr>
        <tr>
          <td>Address</td>
          <td><textarea maxlength="100" name="address">
            <c:out value="${update_sel_user.address}"/>
          </textarea></td>
        </tr>
        <tr>
          <td class="button" ><input type="reset" value="Clear" style="text-align: center"/></td>
          <td class="button"><input type="submit" value="Send" style="text-align: center"/> </td>
        </tr>
        <tr>
          <td></td>
          <td></td>
        </tr>
      </table>
    </form>

  </div>
</body>
</html>
