package com.org.betmanager;

import java.io.IOException;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.org.webbrowser.IbetAgent;

/**
 * Servlet implementation class IbetCaptcha
 */
@WebServlet("/IbetCaptcha")
public class IbetCaptcha extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public IbetCaptcha() {
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
		IbetAgent client = new IbetAgent();
		request.getSession().setAttribute("client", client);
		ImageIO.write(client.getImageBuffer(), "jpg",
				response.getOutputStream());
		// Object session_client = request.getSession().getAttribute("client");
		// if (session_client == null) {
		// IbetAgentClient client = new IbetAgentClient();
		// request.getSession().setAttribute("client", client);
		// ImageIO.write(client.getImageBuffer(), "jpg",
		// response.getOutputStream());
		// } else {
		// ImageIO.write(((IbetAgentClient) session_client).getImageBuffer(),
		// "jpg", response.getOutputStream());
		// }
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
