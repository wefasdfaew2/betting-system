package com.org.webbrowser;

import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.org.captcha.CaptchaUtilities;
import com.org.captcha.Site;
import com.org.messagequeue.TopicListener;
import com.org.messagequeue.TopicPublisher;
import com.org.odd.Odd;
import com.org.odd.OddSide;
import com.org.odd.OddUtilities;

public class SbobetMemberClient extends Thread {
	private TopicPublisher p;
	private final Logger logger;
	private String username;
	private String pass;
	private int sleep_time = 100;
	private OddUtilities util;
	private OddSide side;

	public Logger getLogger() {
		return logger;
	}

	public SbobetMemberClient(String acc, OddSide side) throws JMSException {
		super();
		this.p = new TopicPublisher();
		String[] a = acc.split(",");
		this.username = a[0];
		this.pass = a[1];
		PropertyConfigurator.configure("log4j.properties");
		this.logger = Logger.getLogger(SbobetMemberClient.class);
		this.util = new OddUtilities();
		this.side = side;
	}

	public SbobetMemberClient(String username, String pass, OddSide side)
			throws JMSException {
		super();
		this.p = new TopicPublisher();
		this.username = username;
		this.pass = pass;
		System.setProperty("filename", username + ".log");
		PropertyConfigurator.configure("log4j.properties");
		this.logger = Logger.getLogger(SbobetMemberClient.class);
		this.util = new OddUtilities();
		this.side = side;
	}

	public void sbobetMemberHomepage() {
		final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_3_6);
		webClient.setJavaScriptEnabled(true);
		webClient.setTimeout(5000);
		webClient.setThrowExceptionOnScriptError(false);
		HtmlPage page;
		try {
			page = webClient.getPage("http://www.sbobetasia.com/");
			HtmlElement link = (HtmlElement) page.getByXPath(
					"/html/body/div/div[3]/div").get(0);
			page = link.click();
			FrameWindow top_frame = page.getFrameByName("topFrame");
			page = (HtmlPage) top_frame.getEnclosedPage();

			// page contain no login form
			URL image_url = page.getFullyQualifiedUrl(page.getElementById("vc")
					.getAttribute("src"));
			// System.out.println(image_url);
			UnexpectedPage image_page = webClient.getPage(image_url);
			// get captcha code
			CaptchaUtilities captcha_util = new CaptchaUtilities();
			BufferedImage imageBuffer = ImageIO.read(image_page
					.getInputStream());
			// File outputfile = new File("G:\\captcha.jpg");
			// ImageIO.write(imageBuffer, "jpg", outputfile);

			// String code = captcha_util.GetNumber(imageBuffer, Site.SBOBET);
			String code = captcha_util.GetNumber(imageBuffer, Site.SBOBET);
			// System.out.println(code);
			//
			HtmlInput id = (HtmlInput) page.getElementById("id");
			id.setValueAttribute(this.username);
			HtmlInput pass = (HtmlInput) page.getElementById("password");
			pass.setValueAttribute(this.pass);
			HtmlInput cd = (HtmlInput) page.getElementById("code");
			cd.setValueAttribute(code);
			// press redirect button to login page
			link = (HtmlElement) page
					.getByXPath(
							"/html/body/div/div/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr/td[10]/table/tbody/tr/td[2]")
					.get(0);
			// System.out.println(link.asXml());
			page = link.click();

			logger.info("loggin as " + this.username);
			// after login find the new host
			page = (HtmlPage) webClient.getWebWindows().get(0)
					.getEnclosedPage();

			// process if it is change pass page
			// String bet_url_page = page.getFullyQualifiedUrl(
			// "/webroot/restricted/go_product.aspx?at=true&p=hr")
			// .toString();
			// System.out.println(bet_url_page);
			//
			// page = (HtmlPage) webClient.getPage(bet_url_page);
			Thread.sleep(3000);

			// page = (HtmlPage) webClient.getWebWindowByName("frmTop")
			// .getEnclosedPage();
			// // if the page contain the submit button then click it
			// HtmlElement aggree = page.getElementByName("action");
			// page = aggree.click();
			//
			// // switch to the sport betting page
			// HtmlAnchor anchor = (HtmlAnchor) page.getByXPath(
			// "/html/body/div/div[2]/a").get(0);
			// // System.out.println(anchor.asXml());
			// page = anchor.click();

			// get the odd page in the mainFrame frame
			HtmlPage odd_page = (HtmlPage) webClient.getWebWindowByName(
					"mainFrame").getEnclosedPage();
			// logger.info(odd_page.asXml());
			// sleep to get full table
			// Thread.sleep(3000);
			// get the odd table
			// live odd
			HtmlTable table = null;
			HtmlTable table_nonlive = null;

			Thread.sleep(5000);
			int i = 1;
			// while (table_nonlive == null || table == null) {
			//
			// Thread.sleep(10000);
			// if (!odd_page.getElementById("levents").getFirstChild().asXml()
			// .equals("")) {
			// table = (HtmlTable) odd_page.getElementById("levents")
			// .getFirstChild();
			// }
			// if (!odd_page.getElementById("events").getFirstChild().asXml()
			// .equals("")) {
			// table_nonlive = (HtmlTable) odd_page.getElementById(
			// "events").getFirstChild();
			// }
			//
			// logger.info("sleep");
			// // logger.info(odd_page.asXml());
			// if (i % 10 == 0) {
			// return;
			// }
			// i++;
			// }
			//
			// if (table_nonlive != null) {
			// logger.info("non-live");
			// logger.info(table_nonlive.asText());
			// }
			// if (table != null) {
			// logger.info("live");
			// logger.info(table.asText());
			// }
			i = 1;
			HtmlElement refresh_live = odd_page
					.getFirstByXPath("/html/body/div/table/tbody/tr/td/table/thead/tr/th/table/tbody/tr/td[2]");
			HtmlElement refresh_nonlive = odd_page
					.getFirstByXPath("/html/body/div[3]/table/tbody/tr/td/table/thead/tr/th/table/tbody/tr/td[2]");
			while (true) {
				long startTime = System.currentTimeMillis();
				Thread.sleep(sleep_time);
				if (this.side == OddSide.LIVE) {
					refresh_live.click();
					if (!odd_page.getElementById("levents").getFirstChild()
							.asXml().equals("")) {
						table = (HtmlTable) odd_page.getElementById("levents")
								.getFirstChild();
						table = (HtmlTable) table.getBodies().get(0).getRows()
								.get(0).getCell(0).getFirstChild();
						// when table is malform just continue not throw
						// exception
						if (table != null)
							try {
								// String data = table.asText();
								long endTime = System.currentTimeMillis();
								long delay = endTime - startTime;
								String d = "" + delay;

								if (table != null) {
									// p.sendMessage(data);
									p.sendMapMessage(
											this.util.getOddsFromSobet(table),
											this.username);
								}
							} catch (Exception e) {
								// TODO: handle exception
								logger.error(getStackTrace(e));
								continue;
							}

					}
				}
				startTime = System.currentTimeMillis();
				if (this.side == OddSide.NON_LIVE) {
					refresh_nonlive.click();
					if (!odd_page.getElementById("events").getFirstChild()
							.asXml().equals("")) {
						table_nonlive = (HtmlTable) odd_page.getElementById("events")
								.getFirstChild();
						table_nonlive = (HtmlTable) table_nonlive.getBodies().get(0).getRows()
								.get(0).getCell(0).getFirstChild();
						// when table is malform just continue not throw exception
						if (table_nonlive != null)
							try {
								// String data = table.asText();
								long endTime = System.currentTimeMillis();
								long delay = endTime - startTime;
								String d = "" + delay;

								if (table_nonlive != null) {
									// p.sendMessage(data);
									p.sendMapMessage(
											this.util.getOddsFromSobet(table_nonlive),
											this.username);
								}
							} catch (Exception e) {
								// TODO: handle exception
								logger.error(getStackTrace(e));
								continue;
							}

					}
				}
				i++;
				if (i % 3000 == 0) {
					break;
				}
			}

		} catch (Exception e) {
			// e.printStackTrace();
			logger.info(getStackTrace(e));
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true)
			try {
				this.sbobetMemberHomepage();
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

	public static String getStackTrace(Throwable aThrowable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		aThrowable.printStackTrace(printWriter);
		return result.toString();
	}
}
