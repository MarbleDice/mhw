<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>MHW</title>
		<link rel="shortcut icon" href="favicon.ico" type="image/vnd.microsoft.icon">
		<link rel="stylesheet" href="styles/default.css" type="text/css">
		<script src="//ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js" type="text/javascript"></script>
		<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.8.21/jquery-ui.min.js" type="text/javascript"></script>
	</head>
	<body>
		<p>Oh hello there</p>
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
	</body>
</html>
