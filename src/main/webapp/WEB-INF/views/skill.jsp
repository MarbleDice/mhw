<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="subtitle" value="${skill.name}" />
<%@include file="includes/header.jsp"%>
<table>
	<c:forEach items="${skill.descriptions}" var="description" varStatus="status">
	<tr>
		<td>Lv ${status.count}</td>
		<td>${description}</td>
	</tr>
	</c:forEach>
</table>

<h3>Equipment with ${skill.name}</h3>
<%@include file="includes/equipment-table.jsp"%>

<%@include file="includes/footer.jsp"%>
