package com.org.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.RollingFileAppender;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.org.betcrawler.Crawler;
import com.org.captcha.CaptchaUtilities;
import com.org.captcha.Site;

public class ThreeInOneBetClient {
	private final Logger logger;
	private String username;
	private String pass;

	public Logger getLogger() {
		return logger;
	}

	public ThreeInOneBetClient(String username, String pass) {
		super();
		// TODO Auto-generated constructor stub
		this.username = username;
		this.pass = pass;
		System.setProperty("filename", "yourlog.log");
		PropertyConfigurator.configure("log4j.properties");
		logger = Logger.getLogger(ThreeInOneBetClient.class);
		// RollingFileAppender appndr = (RollingFileAppender) logger
		// .getAppender("R");
		// appndr.setFile("E:/Dev/mylog.log");
		// appndr.activateOptions();
		// logger.addAppender(appndr);
	}

	public void parseOddData(String element_name, String data) {
		JSONObject obj;
		try {
			obj = new JSONObject(data);
			JSONArray array = obj.getJSONArray(element_name);
			for (int i = 0; i < array.length(); i++) {
				JSONArray ar = array.getJSONArray(i);
				if (ar.length() > 0)
					// System.out.println(ar);
					logger.info(ar.toString());
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

	}

	public String getDate() {
		Calendar now = Calendar.getInstance();
		return (now.get(Calendar.MONTH) + 1) + "/" + now.get(Calendar.DATE)
				+ "/" + now.get(Calendar.YEAR) + " "
				+ (now.get(Calendar.HOUR_OF_DAY) + 1) + ":"
				+ now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND)
				+ "." + now.get(Calendar.MILLISECOND) + "0000";
	}

	private String getPostData1() {
		return "fc=5&m_accType=MY&SystemLanguage=en-US&TimeFilter=0&m_gameType=S_&m_SortByTime=0&m_LeagueList=&SingleDouble=single&clientTime="
				+ getDate() + "&c=A&fav=&exlist=0";
	}

	private String getPostData2() {
		return "fc=3&m_accType=MY&SystemLanguage=en-US&TimeFilter=0&m_gameType=S_&m_SortByTime=0&m_LeagueList=&SingleDouble=single&clientTime="
				+ getDate() + "&c=A&fav=&exlist=0";
	}

	public void testLogin3in1Bet() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException, InterruptedException {
		Crawler crawler = new Crawler("http://www.3in1bet.com/",
				"Checker.aspx", "", "", Site.THREEINONE);
		// get wich member host

		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_3_6);
		webClient.setJavaScriptEnabled(true);
		webClient.setTimeout(5000);
	

		HtmlPage page;

		page = webClient.getPage("http://www.3in1bet.com");

		// Get frames
		// this page contain login form
		HtmlPage login_page = (HtmlPage) page.getFrameByName("f2")
				.getEnclosedPage();
		String host = "http://" + login_page.getFullyQualifiedUrl("").getHost();
		webClient.closeAllWindows();
		webClient = null;

		InputStream ip = crawler.BasicGetStreamFromURL(
				"http://www.3in1bet.com/", "");

		// System.out.println("Main");
		String html = IOUtils.toString(ip, "utf-8");
		// System.out.println(html);

		ip.close();

		// System.out.println("Checker");
		ip = crawler.BasicGetStreamFromURL(host + "/Checker.aspx", host);

		html = IOUtils.toString(ip, "utf-8");
		// System.out.println(html);
		ip.close();

		// System.out.println("Default");
		ip = crawler.BasicGetStreamFromURL(host + "/Default.aspx", host);

		html = IOUtils.toString(ip, "utf-8");
		// System.out.println(html);
		ip.close();

		// System.out.println("Verify");

		String code = crawler.GetValidateCode(host + "/verify.aspx", host
				+ "/Default.aspx");
		// System.out.println(code);

		List<BasicNameValuePair> post_data = new ArrayList<BasicNameValuePair>();
		post_data.add(new BasicNameValuePair("ddl_language", "en-US"));
		post_data.add(new BasicNameValuePair("UserName", username));
		post_data.add(new BasicNameValuePair("Password", pass));
		post_data.add(new BasicNameValuePair("txtInvalidation", code));

		ip = crawler.basicPost(post_data, host + "/main/processlogin.aspx");
		html = IOUtils.toString(ip, "utf-8");
		// System.out.println(html);
		ip.close();

		ip = crawler.BasicGetStreamFromURL(host + "/Main.aspx", host);

		html = IOUtils.toString(ip, "utf-8");
		// System.out.println(html);
		ip.close();

		// Get odds data
		// System.out.println(getDate());

		String postdata = "fc=1&m_accType=MY&SystemLanguage=en-US&TimeFilter=0&m_gameType=S_&m_SortByTime=0&m_LeagueList=&SingleDouble=single&clientTime=&c=A&fav=&exlist=0";

		String post_url = host + "/Member/BetsView/BetLight/DataOdds.ashx";

		ip = crawler.basicPost(postdata, post_url);
		html = IOUtils.toString(ip, "utf-8");
		ip.close();
		// System.out.println(html);

		parseOddData("today", html);

		parseOddData("data", html);

		ip.close();
		int sleep_time = 100;
		int i = 1;
		while (true) {

			ip = crawler.basicPost(getPostData1(), post_url);
			html = IOUtils.toString(ip, "utf-8");
			// System.out.println(html);

			parseOddData("data", html);
			ip.close();

			ip = crawler.basicPost(getPostData2(), post_url);
			html = IOUtils.toString(ip, "utf-8");
			// System.out.println(html);
			Thread.sleep(sleep_time);

			parseOddData("data", html);
			ip.close();

			Thread.sleep(sleep_time);

			// break;

			// refresh full
			if (i % 100 == 0) {
				i = 1;
				ip = crawler.basicPost(postdata, post_url);
				html = IOUtils.toString(ip, "utf-8");
				ip.close();
				// System.out.println(html);

				parseOddData("today", html);

				parseOddData("data", html);

			}
			i++;

			Thread.sleep(sleep_time);

		}

	}

}
