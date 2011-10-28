package com.org.webbrowser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;
import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.org.captcha.CaptchaUtilities;
import com.org.captcha.Site;
import com.org.messagequeue.TopicPublisher;
import com.org.odd.Odd;

public class ThreeInOneMemberClient extends Thread {
	private final Logger logger;
	private String username;
	private String pass;
	private int sleep_time = 100;
	TopicPublisher p;

	public Logger getLogger() {
		return logger;
	}

	public ThreeInOneMemberClient(String username, String pass)
			throws JMSException {
		super();
		this.p = new TopicPublisher();
		this.username = username;
		this.pass = pass;
		PropertyConfigurator.configure("log4j.properties");
		logger = Logger.getLogger(ThreeInOneMemberClient.class);
	}

	public ThreeInOneMemberClient(String acc) throws JMSException {
		super();
		this.p = new TopicPublisher();
		String[] a = acc.split(",");
		this.username = a[0];
		this.pass = a[1];
		PropertyConfigurator.configure("log4j.properties");
		logger = Logger.getLogger(ThreeInOneMemberClient.class);

	}

	public void witeStringtoFile(String content, String file)
			throws IOException {
		FileOutputStream op = new FileOutputStream(new File(file));
		op.write(content.getBytes());
		op.close();
	}

	public void homePage() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException, InterruptedException,
			JMSException {
		int i = 1;
		int j = 1;

		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_3_6);
		webClient.setJavaScriptEnabled(true);
		webClient.setTimeout(5000);
		webClient.setThrowExceptionOnScriptError(false);
		webClient.setThrowExceptionOnFailingStatusCode(false);

		HtmlPage page;

		page = webClient.getPage("http://www.3in1bet.com");

		// Get frames
		// this page contain login form
		HtmlPage login_page = (HtmlPage) page.getFrameByName("f2")
				.getEnclosedPage();

		// get captcha image url

		Thread.sleep(3000);

		HtmlElement image_element = (HtmlElement) login_page
				.getFirstByXPath("/html/body/div/form/div[2]/ul/li[3]/img");

		URL image_url = login_page.getFullyQualifiedUrl(image_element
				.getAttribute("src"));

		// System.out.println(image_url);

		UnexpectedPage image_page = webClient.getPage(image_url);
		CaptchaUtilities captcha_util = new CaptchaUtilities();
		BufferedImage imageBuffer = ImageIO.read(image_page.getInputStream());
		String code = captcha_util.GetNumber(imageBuffer, Site.THREEINONE);

		// get captcha code

		// System.out.println(code);

		// get login form
		List<HtmlForm> forms = login_page.getForms();
		HtmlForm login_form = forms.get(0);

		// System.out.println(login_form.asXml());
		// System.out.println(login_form.getInputByName("UserName"));
		login_form.getInputByName("UserName").setValueAttribute(username);
		login_form.getInputByName("Password").setValueAttribute(pass);

		login_form.getInputByName("txtInvalidation").setValueAttribute(code);
		// virtual submit button to navigate to member page
		HtmlButton submitButton = (HtmlButton) login_page
				.createElement("button");
		submitButton.setAttribute("type", "submit");
		login_form.appendChild(submitButton);

		// try to log in
		HtmlPage after_login = null;

		Thread.sleep(3000);

		after_login = submitButton.click();
		logger.info("Logged in as" + this.username);

		// after log in the request the main page
		Thread.sleep(3000);

		URL main_url = after_login.getFullyQualifiedUrl("Main.aspx");
		page = webClient.getPage(main_url);

		FrameWindow frm_left = page.getFrameByName("fraPanel");
		HtmlPage left_page = (HtmlPage) frm_left.getEnclosedPage();

		Thread.sleep(3000);

		HtmlElement refresh_full = (HtmlElement) left_page
				.getFirstByXPath("/html/body/div/form/div[7]/ul/li[2]");
		// System.out.println(refresh_full.asXml());

		refresh_full.click();

		// tblData5
		// Process get table and display
		FrameWindow frm_main = page.getFrameByName("fraMain");
		HtmlPage odd_page = (HtmlPage) frm_main.getEnclosedPage();

		Thread.sleep(5000);

		HtmlTable table = (HtmlTable) odd_page.getElementById("tblData5");
		HtmlTable table_nonlive = (HtmlTable) odd_page
				.getElementById("tblData6");

		i = 1;
		// sendData(table, table_nonlive);
		long delay = 0;

		while (true) {
			long startTime = System.currentTimeMillis();
			// Click update live and non-live
			// non -live
			// HtmlElement refresh = odd_page.getElementById("imgbtnRefresh");
			//
			// refresh.click();
			// System.out.println("non-live update");
			// live
			HtmlAnchor anchor = odd_page
					.getAnchorByHref("javascript: RefreshRunning();");
			anchor.click();
			
			Thread.sleep(sleep_time);
			
			long endTime = System.currentTimeMillis();
			delay = endTime - startTime;
			String d = "" + delay;
			p.sendMessage(d);
			// sendData(table, table_nonlive);
			Odd.getOddsFromThreeInOne(table);
			i++;
			// refresh all after 30s
			if (i % 100 == 0) {
				// Click to update all
				// System.out.println(refresh_full.asXml());

				// refresh_full.click();
				// sendData(table, table_nonlive);
				i = 1;
				j++;
				if (j % 100 == 0) {
					// now exit
					this.p.disconnect();
					return;
				}
			}

		}
		// webClient.closeAllWindows();
	}

	private void sendData(HtmlTable table, HtmlTable table_nonlive)
			throws JMSException {
		if (table_nonlive != null) {
			// logger.info(table_nonlive.asText());
			p.sendMessage(table_nonlive.asText());
		}
		if (table != null) {
			// logger.info(table.asText());
			p.sendMessage(table.asText());
		}
	}

	/**
	 * Get stack trace as string
	 * 
	 * @param aThrowable
	 * @return
	 */
	public static String getStackTrace(Throwable aThrowable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		aThrowable.printStackTrace(printWriter);
		return result.toString();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true)
			try {
				homePage();
				Thread.sleep(sleep_time);
			} catch (Exception e) {
				logger.error(getStackTrace(e));

				try {
					Thread.sleep(sleep_time);
				} catch (InterruptedException e1) {
					logger.error(getStackTrace(e1));
				}
			}
	}
}
