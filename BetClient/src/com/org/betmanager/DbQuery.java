package com.org.betmanager;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DbQuery
 */
@WebServlet(description = "Query Database", urlPatterns = { "/DbQuery" })
public class DbQuery extends HttpServlet {
	private Connection conn = null;

	public void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
				System.out.println("Database connection terminated");
			} catch (Exception e) { /* ignore close errors */
			}
		}

	}

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DbQuery() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String ip1 = request.getParameter("ip1");
		String ip2 = request.getParameter("ip2");
		String ip3 = request.getParameter("ip3");
		String ip4 = request.getParameter("ip4");
		String site = request.getParameter("site");
		String date = request.getParameter("date");
		String datefrom = request.getParameter("datefrom");
		String dateto = request.getParameter("dateto");

		String select_query = "SELECT * FROM USER ";
		String condition = "";
		if (ip1 != null)
			if (!ip1.equals("")) {
				condition += " WHERE ";
				condition += "ip1=\"" + ip1 + "\"";
			}
		if (ip2 != null)
			if (!ip2.equals("")) {
				if (!condition.equals(""))
					condition += " AND ";
				else
					condition += " WHERE ";
				condition += "ip2=\"" + ip2 + "\"";
			}
		if (ip3 != null)
			if (!ip3.equals("")) {
				if (!condition.equals(""))
					condition += " AND ";
				else
					condition += " WHERE ";
				condition += "ip3=\"" + ip3 + "\"";
			}
		if (ip4 != null)
			if (!ip4.equals("")) {
				if (!condition.equals(""))
					condition += " AND ";
				else
					condition += " WHERE ";
				condition += "ip4=\"" + ip4 + "\"";
			}
		if (date != null)
			if (!date.equals("")) {
				if (!condition.equals(""))
					condition += " AND ";
				else
					condition += " WHERE ";
				condition += "date=STR_TO_DATE('" + date + "','%m/%d/%Y')";
			}
		if (datefrom != null)
			if (!datefrom.equals("")) {
				if (!condition.equals(""))
					condition += " AND ";
				else
					condition += " WHERE ";
				condition += "date>STR_TO_DATE('" + datefrom + "','%m/%d/%Y')";
			}
		if (dateto != null)
			if (!dateto.equals("")) {
				if (!condition.equals(""))
					condition += " AND ";
				else
					condition += " WHERE ";
				condition += "date<STR_TO_DATE('" + dateto + "','%m/%d/%Y')";
			}
		select_query += condition;
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter writer = response.getWriter();

		// writer.write(select_query);

		Statement stm;
		try {			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/" + site, "root", "123456");

			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(select_query);
			ResultSetMetaData md = rs.getMetaData();
			int count = md.getColumnCount();
			writer.write("<html>\n");
			writer.write("<body>\n");
			writer.write("<table border=1>\n");
			writer.write("<tr>");
			for (int i = 1; i <= count; i++) {
				writer.write("<th>");
				writer.write(md.getColumnLabel(i));
			}
			writer.write("</tr>");
			while (rs.next()) {
				writer.write("<tr>");
				for (int i = 1; i <= count; i++) {
					writer.write("<td>");
					writer.write(rs.getString(i));
				}
				writer.write("</tr>\n");
			}
			writer.write("</table>\n");
			writer.write("</body>\n");
			writer.write("</html>\n");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
