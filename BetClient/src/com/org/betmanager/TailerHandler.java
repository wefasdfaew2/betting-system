package com.org.betmanager;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TailerHandler extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Object t = req.getSession().getAttribute("tailer");
		if (t == null) {
			TailerReader reader = new TailerReader();
			req.getSession().setAttribute("tailer", reader);
			reader.getdata();
		} else {
			TailerReader reader = (TailerReader) t;
			PrintWriter writer = resp.getWriter();
			writer.write(reader.getListener().getString_buffer().toString());
			reader.getListener().setString_buffer(new StringBuffer());
			writer.close();
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(req, resp);
	}

}
