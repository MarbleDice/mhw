<table>
	<tr>
		<th>Armor Set</th>
		<th>Head</th>
		<th>Chest</th>
		<th>Hands</th>
		<th>Waist</th>
		<th>Legs</th>
	</tr>
	<c:forEach var="entry" items="${matrix.entrySet()}">
	<tr>
		<th>${entry.key}</th>
		<c:forEach var="equipment" items="${entry.value}">
		<td>
			<c:if test="${not empty equipment}">
			<ul>
				<c:forEach var="skillValue" items="${equipment.skills}">
				<li><a href="/skill/${skillValue.skill.urlName}">${skillValue.skill.name}</a> ${skillValue.pointValue.label}</li>
				</c:forEach>
				<c:if test="${not equipment.slots.isEmpty()}">
				<li><c:forEach var="slot" items="${equipment.slotLabels}">${slot}</c:forEach></li>
				</c:if>
			</ul>
			</c:if>
		</td>
		</c:forEach>
	</tr>
	</c:forEach>
</table>