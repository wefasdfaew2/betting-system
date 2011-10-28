package com.org.betmanager;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.org.webbrowser.IbetAgent;

/**
 * Servlet implementation class LoginIbet
 */
@WebServlet("/LoginIbet")
public class LoginIbet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginIbet() {
		super();
		// TODO Auto-generated constructor stub
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
		String captcha = request.getParameter("captcha");

		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter writer = response.getWriter();
		writer.write(username);
		writer.write(password);
		writer.write(from);
		writer.write(to);
		writer.write(captcha);

		// SbobetAgent agent = new SbobetAgent();
		try {
			IbetAgent client = (IbetAgent) request.getSession()
					.getAttribute("client");
			client.setPassword(password);
			client.setUsername(username);
			client.setCaptcha(captcha);
			client.setFrom(from);
			client.setTo(to);
			
			new Thread(client).start();
			// agent.sboAgent(username, password, from, to);
		} catch (Exception e) {
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
