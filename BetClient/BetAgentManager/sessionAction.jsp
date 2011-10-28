<%@page import="java.util.*"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.ResultSetMetaData"%>
<%@page import="java.io.PrintWriter"%>
<%
	String username = request.getParameter("UserName");
	String password = request.getParameter("Password");
	session.setAttribute("UserName", username);
	session.setAttribute("Password", password);
	PrintWriter writer = response.getWriter();
	writer.write("<html>\n");
	writer.write("<body>\n");

	Connection conn = null;
	try {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		conn = DriverManager.getConnection(
				"jdbc:mysql://localhost/sbobet", "root", "123456");
		Statement stm = conn.createStatement();
		String select_query = "SELECT * FROM ADMIN WHERE USERNAME=\""
				+ username + "\"";
		ResultSet rs = stm.executeQuery(select_query);
		ResultSetMetaData md = rs.getMetaData();
		int count = md.getColumnCount();
		while (rs.next()) {
			if (password.equals(rs.getString(2))) {				
				session.setAttribute("isLogin", "logged");
				response.sendRedirect("/BetAgentManager/mainform.jsp");
				out.println("logged");
			}
		}
		writer.write("<h1>Incorect Login</h1>");
		writer.write("</body></html>");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
%>