<%!String subtitle = "Equipment List";%>
<%@include file="includes/header.jsp"%>
<table>
	<c:forEach items="${matrix.entrySet()}" var="entry">
	<tr>
		<td>${entry.key}</td>
		<c:forEach items="${entry.value}" var="equipment">
		<td>
			<ul>
				<c:forEach items="${equipment.skills}" var="skill">
				<li>${skill}</li>
				</c:forEach>
			</ul>
		</td>
		</c:forEach>
	</tr>
	</c:forEach>
</table>
<%@include file="includes/footer.jsp"%>
