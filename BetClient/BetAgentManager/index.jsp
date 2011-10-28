
<%
	session.setAttribute("isLogin", null);
%>
<html>

<head>

<title>User Login</title>
</head>
<br>
<body Bgcolor="#0099cc">
<hr>
<hr>

<form method="POST" action="sessionAction.jsp">
<p><b>UserName:</b> <input type="text" name="UserName" size="10"></p>
<p><b>Password:</b> &nbsp;&nbsp;<input type="Password"
	name="Password" size="10"></p>
<p><input type="submit" value="Submit" name="submit"><input
	type="reset" value="Reset" name="reset"></p>
<hr>
<hr>
</form>

</body>

</html>