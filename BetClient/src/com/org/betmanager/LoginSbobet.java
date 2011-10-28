package com.org.betmanager;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.org.webbrowser.SbobetAgent;

/**
 * Servlet implementation class DbQuery
 */
@WebServlet(description = "Login to Agent", urlPatterns = { "/Login" })
public class LoginSbobet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginSbobet() {
		super();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String from = request.getParameter("from");
		String to = request.getParameter("to");

		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter writer = response.getWriter();
		writer.write(username);
		writer.write(password);
		writer.write(from);
		writer.write(to);

		SbobetAgent agent = new SbobetAgent(username, password, from, to);
		new Thread(agent).start();

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
