<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Peter Zaoral
  Date: 21.4.2015
  Time: 22:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>List users</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css"/>
</head>
<body>
<div id="content">
  <table>
    <tr>
      <th> Name </th>
      <th> Description </th>
      <th> Start of event </th>
      <th> End of event </th>
      <th> Address </th>

      <th> Delete </th>
      <th> Update </th>
    </tr>
    <c:forEach items="${Events}"  var="event">
      <tr>
        <td><c:out value="${event.name}"/></td>
        <td><c:out value="${event.description}"/></td>
        <td><c:out value="${event.dateBeginString}"/> </td>
        <td><c:out value="${event.dateEndString}"/></td>
        <td><c:out value="${event.address}"/> </td>
        <td>
          <form method="post" action="${pageContext.request.contextPath}/events/delete?eid=${event.id}">
            <input class="delete" type="submit" value="Delete">
          </form>
        </td>
        <td>
          <form method="post" action="${pageContext.request.contextPath}/events/update?eid=${event.id}">
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

  <form method="post" action="${pageContext.request.contextPath}/events/add?update_flag=${update_flag}&eid=${update_event_id}">
    <table id="event">
      <tr>
        <td>Name</td>
        <td><input type="text" name="name" pattern="^[a-zA-Z\s-_]*$" value="${update_sel_event.name}" /></td>
      </tr>
      <tr>
        <td>Description</td>
        <td><input type="text" name="description" value="${update_sel_event.description}" /></td>
      </tr>
      <tr>
        <td>Start of Event</td>
        <td><input type="datetime" name="dateBegin" value="${update_sel_event.dateBeginString}" /></td>
      </tr>
      <tr>
        <td>End of Event</td>
        <td><input type="datetime" name="dateEnd" value="${update_sel_event.dateEndString}"/></td>
      </tr>
      <tr>
        <td>Address</td>
        <td><textarea name="address"  >
            <c:out value="${update_sel_event.address}"/>
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
