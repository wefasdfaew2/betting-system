package com.org.player;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
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
import com.org.odd.OddElement;
import com.org.odd.OddSide;
import com.org.odd.OddUtilities;
import com.org.webbrowser.ThreeInOneMemberClient;

public class ThreeInOnePlayer extends Thread implements MessageListener {
	private final Logger logger;
	private String username;
	private String pass;
	private int sleep_time = 3000;
	private TopicPublisher p;
	private OddUtilities util;
	private OddSide side;
	private HtmlPage odd_page;
	private WebClient webClient;
	private HtmlPage page;
	private HtmlPage ticket_page;
	private HashMap<String, OddElement> current_map_odds;
	HtmlTable table = null;
	HtmlTable table_nonlive = null;
	HtmlElement refresh_live;
	HtmlElement refresh_nonlive;
	HtmlElement refresh_early;

	public static void main(String[] argv) throws JMSException,
			FailingHttpStatusCodeException, MalformedURLException, IOException,
			InterruptedException {
		OddSide side = OddSide.LIVE;
		ThreeInOnePlayer client = new ThreeInOnePlayer(argv[0], argv[1], side);
		client.homePage();
	}

	public void startConnection() throws JMSException {
		String url = "tcp://localhost:61616?jms.useAsyncSend=true";
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
		Connection connection = factory.createConnection();
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		Queue topic = session.createQueue(this.username);
		MessageConsumer consumer = session.createConsumer(topic);
		consumer.setMessageListener(this);
		connection.start();
		logger.info("Waiting for bet command...");
	}

	public Logger getLogger() {
		return logger;
	}

	public ThreeInOnePlayer(String username, String pass, OddSide side)
			throws JMSException {
		super();
		this.p = new TopicPublisher();
		this.username = username;
		this.pass = pass;
		PropertyConfigurator.configure("log4j.properties");
		logger = Logger.getLogger(ThreeInOnePlayer.class);
		this.util = new OddUtilities();
		this.side = side;
		this.current_map_odds = new HashMap<String, OddElement>();
	}

	public ThreeInOnePlayer(String acc, OddSide side) throws JMSException {
		super();
		this.p = new TopicPublisher();
		String[] a = acc.split(",");
		this.username = a[0];
		this.pass = a[1];
		PropertyConfigurator.configure("log4j.properties");
		logger = Logger.getLogger(ThreeInOnePlayer.class);
		this.util = new OddUtilities();
		this.side = side;
		this.current_map_odds = new HashMap<String, OddElement>();
	}

	private void sendData(HashMap<String, OddElement> map_odds)
			throws JMSException {
		HashMap<String, Odd> send_odds = new HashMap<String, Odd>();

		// now send only the odd which is updated
		// iter through old if not in new then set null
		for (Entry<String, OddElement> e : this.current_map_odds.entrySet()) {
			// if not in new
			if (!map_odds.containsKey(e.getKey())) {
				send_odds.put(e.getKey(), null);
			}
		}
		// iter through new and see if update then send
		for (Entry<String, OddElement> e : map_odds.entrySet()) {
			// compre if update
			if (this.current_map_odds.containsKey(e.getKey())) {
				Odd new_odd = e.getValue().getOdd();
				Odd old_odd = this.current_map_odds.get(e.getKey()).getOdd();
				if (!Odd.compareValue(new_odd, old_odd)) {
					send_odds.put(new_odd.getId(), new_odd);
				}

			}
		}
		// update current map to update map
		this.current_map_odds.clear();
		this.current_map_odds.putAll(map_odds);
		p.sendMapMessage(send_odds, "3in");
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

		webClient = new WebClient(BrowserVersion.FIREFOX_3_6);
		webClient.setJavaScriptEnabled(true);
		webClient.setTimeout(5000);
		// webClient.setThrowExceptionOnScriptError(false);
		// webClient.setThrowExceptionOnFailingStatusCode(false);

		page = webClient.getPage("http://www.3in1bet.com");

		// Get frames
		// this page contain login form
		HtmlPage login_page = (HtmlPage) page.getFrameByName("f2")
				.getEnclosedPage();

		// get captcha image url
		webClient.waitForBackgroundJavaScript(3000);

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
		after_login = submitButton.click();

		webClient.waitForBackgroundJavaScript(3000);

		logger.info("Logged in as " + this.username);

		// after log in the request the main page
		webClient.waitForBackgroundJavaScript(5000);

		URL main_url = after_login.getFullyQualifiedUrl("Main.aspx");
		page = webClient.getPage(main_url);

		webClient.waitForBackgroundJavaScript(3000);

		FrameWindow frm_left = page.getFrameByName("fraPanel");
		HtmlPage left_page = (HtmlPage) frm_left.getEnclosedPage();

		webClient.waitForBackgroundJavaScript(3000);

		HtmlElement refresh_full = (HtmlElement) left_page
				.getFirstByXPath("/html/body/div/form/div[7]/ul/li[2]");
		// System.out.println(refresh_full.asXml());
		if (this.side == OddSide.EARLY) {
			HtmlElement early = (HtmlElement) left_page
					.getFirstByXPath("/html/body/div/form/div[7]/ul/li");
			early.click();
			webClient.waitForBackgroundJavaScript(3000);
		} else {
			if (refresh_full != null)
				refresh_full.click();
		}

		// tblData5
		// Process get table and display
		ticket_page = (HtmlPage) this.webClient.getWebWindowByName("fraPanel")
				.getEnclosedPage();

		FrameWindow frm_main = page.getFrameByName("fraMain");
		odd_page = (HtmlPage) frm_main.getEnclosedPage();

		webClient.waitForBackgroundJavaScript(3000);

		table = (HtmlTable) odd_page.getElementById("tblData5");
		table_nonlive = (HtmlTable) odd_page.getElementById("tblData6");

		i = 1;
		// sendData(table, table_nonlive);
		// establish connection
		try {
			this.startConnection();
		} catch (JMSException e) {
			logger.info("error establish JMS connection...exiting..");
			return;
		}

		// virtual button to click refresh, call javascript skip check time
		refresh_live = odd_page.createElement("button");
		refresh_live
				.setAttribute(
						"onclick",
						"var data = GetOddsParams(5, LastRunningVersion);var url = GetOddsUrl();callWebService(url, data, onLoadedIncRunningData, onLoadingDataException);");
		odd_page.appendChild(refresh_live);

		refresh_nonlive = odd_page.createElement("button");
		refresh_nonlive
				.setAttribute(
						"onclick",
						"var data = GetOddsParams(3, LastTodayVersion);var url = GetOddsUrl();callWebService(url, data, onLoadedIncTodayData, onLoadingDataException);");
		odd_page.appendChild(refresh_nonlive);

		refresh_early = odd_page.createElement("button");
		refresh_early
				.setAttribute(
						"onclick",
						"var data = GetOddsParams(7, LastTodayVersion);var url = GetOddsUrl();callWebService(url, data, onLoadedIncTodayData, onLoadingDataException);");
		odd_page.appendChild(refresh_early);

		logger.info("complete login...");
		// webClient.closeAllWindows();
	}

	public void doPolling() throws IOException, InterruptedException,
			JMSException {
		long delay = 0;
		HashMap<String, OddElement> map_odds = new HashMap<String, OddElement>();
		// Click update live and non-live
		// live
		if (this.side == OddSide.LIVE || this.side == OddSide.TODAY) {
			long startTime = System.currentTimeMillis();
			odd_page = refresh_live.click();
			// Thread.sleep(sleep_time);
			table = (HtmlTable) odd_page.getElementById("tblData5");
			map_odds.putAll(this.util.getOddsFromThreeInOne(table));
			long endTime = System.currentTimeMillis();
			delay = endTime - startTime;
			String d = "" + delay;

		}
		if (this.side == OddSide.NON_LIVE || this.side == OddSide.EARLY
				|| this.side == OddSide.TODAY) {
			long startTime = System.currentTimeMillis();
			// non -live
			if (this.side == OddSide.NON_LIVE)
				odd_page = refresh_nonlive.click();
			else if (this.side == OddSide.EARLY)
				odd_page = refresh_early.click();
			// Thread.sleep(sleep_time);
			table_nonlive = (HtmlTable) odd_page.getElementById("tblData6");
			map_odds.putAll(this.util.getOddsFromThreeInOne(table_nonlive));
			long endTime = System.currentTimeMillis();
			delay = endTime - startTime;
			String d = "" + delay;
			// p.sendMessage(d);
		}
		this.sendData(map_odds);
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
		try {
			doPolling();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		try {
			// System.out.println(((TextMessage) message).getText());
			if (message instanceof ObjectMessage) {
				ObjectMessage mes = (ObjectMessage) message;
				Odd odd = (Odd) mes.getObject();
				boolean is_home = mes.getBooleanProperty("home");
				// do update again to up to newest odd, if crawl exactly do not
				// need to do this
				HashMap<String, OddElement> tmp_map = new HashMap<String, OddElement>();
				if (table_nonlive != null)
					tmp_map.putAll(this.util
							.getOddsFromThreeInOne(table_nonlive));
				if (table != null)
					tmp_map.putAll(this.util.getOddsFromThreeInOne(table));

				if (tmp_map.containsKey(odd.getId())) {
					logger.info(tmp_map.get(odd.getId()).getOdd());
					if (is_home)
						this.placeBet(tmp_map.get(odd.getId()).getHome());
					else
						this.placeBet(tmp_map.get(odd.getId()).getAway());
				} else {
					logger.info("odd disapear...");
				}
//				logger.info(odd);

			} else if (message instanceof TextMessage) {
				TextMessage mes = (TextMessage) message;
				if (mes.getText().equals("UPDATE")) {
					Thread t = new Thread(this);
					t.start();
				}

			}
			// logger.info(((ObjectMessage)message));
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void placeBet(HtmlElement element) {
		try {
			HtmlElement odd_element = (HtmlElement) element.getFirstChild();
			String submit_odd = odd_element.asText();
			logger.info(odd_element.asXml());

			ticket_page = (HtmlPage) this.webClient.getWebWindowByName(
					"fraPanel").getEnclosedPage();

			odd_element.click();
			Thread.sleep(300);

			String bet_odd = ticket_page.getElementById("lb_bet_odds").asText();
			// logger.info("match : "
			// + ticket_page.getElementById("pn_title").asText());
			// logger.info("team to bet :"
			// + ticket_page.getElementById("lb_bet_team").asText());
			// logger.info("hdp info : "
			// + ticket_page.getElementById("td_tbg").asText());
			// logger.info("submitted to bet :" + submit_odd);
			// logger.info("real odd to bet :" + bet_odd);
			logger.info(ticket_page.asText());

			// if (getEquals(submit_odd, bet_odd)) {
			// logger.fatal("Ha ha we can bet now !!!");
			// HtmlElement bet_button = ticket_page.createElement("button");
			// bet_button.setAttribute("onclick", "onBet();");
			// // click bet
			//
			// ticket_page = bet_button.click();
			// logger.info(ticket_page.asText());
			//
			// }
		} catch (Exception e) {
			logger.error(ThreeInOneMemberClient.getStackTrace(e));
		}
	}

	private boolean getEquals(String o1, String o2) {
		try {
			float a1 = Float.parseFloat(o1);
			float a2 = Float.parseFloat(o2);
			return a1 == a2;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
