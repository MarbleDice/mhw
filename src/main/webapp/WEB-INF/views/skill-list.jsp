<%!String subtitle = "Skill List"; %>
<%@include file="includes/header.jsp" %>
<table>
	<c:forEach items="${skills}" var="skill">
		<tr>
			<td rowspan="${skill.descriptions.size()}">${skill.name}</td>
			<td>${skill.descriptions.get(0)}</td>
		</tr>
		<c:forEach items="${skill.descriptions}" begin="1" var="description">
			<tr>
				<td>${description}</td>
			</tr>
		</c:forEach>
	</c:forEach>
</table>
<%@include file="includes/footer.jsp" %>
