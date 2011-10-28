package org.apache.jsp.BetAgentManager;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.io.PrintWriter;

public final class sessionAction_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");

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

    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
