package com.org.player;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;

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
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowNotFoundException;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
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
import com.org.webbrowser.ThreeInOneMemberClient;

public class SbobetPlayer extends Thread implements MessageListener {
	private TopicPublisher p;
	private final Logger logger;
	private String username;
	private String pass;
	private int sleep_time = 500;
	private OddUtilities util;
	private OddSide side;
	private HtmlPage page;
	private HtmlPage odd_page;
	private HtmlPage ticket_page;
	private HtmlPage ticket_div;
	private WebClient webClient;
	private boolean isCrawler;
	private HashMap<Odd, HtmlElement[]> map_odds;
	HtmlTable table = null;
	HtmlTable table_nonlive = null;
	HtmlElement refresh_live;
	HtmlElement refresh_nonlive;

	public static void main(String[] argv) throws JMSException,
			FailingHttpStatusCodeException, MalformedURLException, IOException,
			InterruptedException {
		OddSide side = OddSide.LIVE;
		SbobetPlayer client = new SbobetPlayer(argv[0], argv[1], side, false);
		client.homePage();
	}

	public Logger getLogger() {
		return logger;
	}

	public void startConnection() throws JMSException {
		String url = "tcp://localhost:61616";
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

	public SbobetPlayer(String acc, OddSide side, boolean isCrawler)
			throws JMSException {
		super();
		this.p = new TopicPublisher();
		String[] a = acc.split(",");
		this.username = a[0];
		this.pass = a[1];
		PropertyConfigurator.configure("log4j.properties");
		this.logger = Logger.getLogger(SbobetPlayer.class);
		this.util = new OddUtilities();
		this.side = side;
		this.isCrawler = isCrawler;
		this.map_odds = new HashMap<Odd, HtmlElement[]>();
	}

	public SbobetPlayer(String username, String pass, OddSide side,
			boolean isCrawler) throws JMSException {
		super();
		this.p = new TopicPublisher();
		this.username = username;
		this.pass = pass;
		System.setProperty("filename", username + ".log");
		PropertyConfigurator.configure("log4j.properties");
		this.logger = Logger.getLogger(SbobetPlayer.class);
		this.util = new OddUtilities();
		this.side = side;
		this.isCrawler = isCrawler;
		this.map_odds = new HashMap<Odd, HtmlElement[]>();
	}

	private void sendData() throws JMSException {
		HashMap<String, Odd> odds = new HashMap<String, Odd>();
		for (Odd o : this.map_odds.keySet()) {
			odds.put(o.getId(), o);
		}
		p.sendMapMessage(odds, "sbobet");
	}

	public void homePage() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException, InterruptedException,
			JMSException {
		webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_8);
		webClient.setJavaScriptEnabled(true);
		webClient.setTimeout(5000);
		webClient.setThrowExceptionOnScriptError(false);

		page = webClient.getPage("http://www.sbobetasia.com/");

		webClient.waitForBackgroundJavaScript(3000);

		HtmlElement link = (HtmlElement) page.getByXPath(
				"/html/body/div/div[3]/div").get(0);
		page = link.click();

		webClient.waitForBackgroundJavaScript(3000);

		FrameWindow top_frame = page.getFrameByName("topFrame");
		page = (HtmlPage) top_frame.getEnclosedPage();

		// page contain no login form
		URL image_url = page.getFullyQualifiedUrl(page.getElementById("vc")
				.getAttribute("src"));
		// System.out.println(image_url);
		UnexpectedPage image_page = webClient.getPage(image_url);
		// get captcha code
		CaptchaUtilities captcha_util = new CaptchaUtilities();
		BufferedImage imageBuffer = ImageIO.read(image_page.getInputStream());
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
		page = link.click();

		logger.info("loggin as " + this.username);
		// after login find the new host

		webClient.waitForBackgroundJavaScript(30000);

		HtmlPage left_page = (HtmlPage) webClient.getWebWindowByName(
				"leftFrame").getEnclosedPage();
		if (this.side == OddSide.EARLY) {
			// navigate to early market page
			HtmlElement e = left_page.createElement("a");
			e.setAttribute("onclick", "javascript:onClickMarket('1',3);");
			left_page.appendChild(e);
			e.click();
		} else {
			// navigate to today page (contain both live and non live)
			HtmlElement e = left_page.createElement("a");
			e.setAttribute("onclick", "javascript:onClickMarket('1',2);");
			left_page.appendChild(e);
			e.click();
		}

		odd_page = (HtmlPage) webClient.getWebWindowByName("mainFrame")
				.getEnclosedPage();

		// get the odd table
		// live odd

		webClient.waitForBackgroundJavaScript(3000);

		int i = 1;
		refresh_live = odd_page.createElement("button");
		refresh_live.setAttribute("onclick", "od_OnRefreshManually(0)");

		refresh_nonlive = odd_page.createElement("button");
		refresh_nonlive.setAttribute("onclick", "od_OnRefreshManually(1);");

		odd_page.appendChild(refresh_live);
		odd_page.appendChild(refresh_nonlive);

		// if not iscrawler then start listen to bet
		if (!this.isCrawler)
			try {
				this.startConnection();
			} catch (JMSException e) {
				logger.info("error establish JMS connection");
			}
		else
			logger.info("start crawling...");

	}

	public void doPolling() throws IOException, JMSException {
		this.map_odds = new HashMap<Odd, HtmlElement[]>();
		if (this.side == OddSide.LIVE || this.side == OddSide.TODAY) {
			refresh_live.click();

			if (odd_page.getElementById("levents") != null
					&& odd_page.getElementById("levents").getFirstChild() != null
					&& !odd_page.getElementById("levents").getFirstChild()
							.asXml().equals("")) {
				table = (HtmlTable) odd_page.getElementById("levents")
						.getFirstChild();
				table = (HtmlTable) table.getBodies().get(0).getRows().get(0)
						.getCell(0).getFirstChild();

				this.map_odds.putAll(this.util.getOddsFromSobet(table));

			}
		}
		// non live, early and today both table in div "events"
		if (this.side == OddSide.NON_LIVE || this.side == OddSide.EARLY
				|| this.side == OddSide.TODAY) {
			if (this.side == OddSide.NON_LIVE)
				refresh_nonlive.click();
			else if (this.side == OddSide.EARLY)
				refresh_live.click();

			if (odd_page.getElementById("events") != null
					&& odd_page.getElementById("events").getFirstChild() != null
					&& !odd_page.getElementById("events").getFirstChild()
							.asXml().equals("")) {
				table_nonlive = (HtmlTable) odd_page.getElementById("events")
						.getFirstChild();
				table_nonlive = (HtmlTable) table_nonlive.getBodies().get(0)
						.getRows().get(0).getCell(0).getFirstChild();
				this.map_odds.putAll(this.util.getOddsFromSobet(table_nonlive));
			}
		}
		this.sendData();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			doPolling();
		} catch (Exception e) {
			logger.error(getStackTrace(e));
		}
	}

	public static String getStackTrace(Throwable aThrowable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		aThrowable.printStackTrace(printWriter);
		return result.toString();
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
				HashMap<Odd, HtmlElement[]> tmp_map = new HashMap<Odd, HtmlElement[]>();
				if (table_nonlive != null)
					tmp_map.putAll(this.util.getOddsFromSobet(table_nonlive));
				if (table != null)
					tmp_map.putAll(this.util.getOddsFromSobet(table));
				if (tmp_map.containsKey(odd)) {
					if (is_home)
						this.placeBet(tmp_map.get(odd)[0]);
					else
						this.placeBet(tmp_map.get(odd)[1]);
				} else {
					logger.info("odd disapear...");
				}
				logger.info(odd);

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
			String xpath = odd_element.getCanonicalXPath();
			// from the xpath find the div which contain the action that the
			// span element missing
			String[] xpath_element = xpath.split("/");
			String div_xpath = "/" + xpath_element[0] + "/" + xpath_element[1]
					+ "/" + xpath_element[2];
			// logger.info(div_xpath);
			HtmlElement div_element = this.odd_page.getFirstByXPath(div_xpath);
			String div_javascript = div_element.getAttribute("onclick");
			odd_element.setAttribute("onclick", "od_OnClick(0, event)");

			// if (this.side == OddSide.LIVE || this.side == OddSide.EARLY)
			// odd_element.setAttribute("onclick", "od_OnClick(0, event)");
			// else if (this.side == OddSide.NON_LIVE)
			// odd_element.setAttribute("onclick", "od_OnClick(1, event)");

			String submit_odd = odd_element.asText();
			logger.info(odd_element.asXml());

			odd_element.click();
			Thread.sleep(100);
			try {
				ticket_page = (HtmlPage) this.webClient.getWebWindowByName(
						"ticketFrame").getEnclosedPage();

				HtmlElement e = (HtmlElement) ticket_page.getElementsByTagName(
						"script").get(0);
				String javascript = e.asXml().split("\n")[2].replaceAll(
						"parent.leftWindow.", "");
				// logger.info(javascript);
				// java script to open ticket form by add virtual a element
				ticket_div = (HtmlPage) this.webClient.getWebWindowByName(
						"leftFrame").getEnclosedPage();
				e = ticket_div.createElement("a");
				e.setAttribute("onclick", "javascript:" + javascript);
				// logger.info(e.asXml());
				ticket_div.appendChild(e);
				e.click();
				Thread.sleep(100);
			} catch (Exception e) {
				logger.info("ticket openned");
			}
			// after open ticket form then find the real odd
			String bet_odd = ticket_div.getElementById("ticoddsVal").asText();

			logger.info("submitted to bet :" + submit_odd);
			logger.info("real odd to bet :" + bet_odd);
			// fill the stake
			HtmlElement stake_input = ticket_div.getElementById("stake");
			stake_input.setAttribute("value", "10");
			logger.info(ticket_div.asText());
			// virtual element to place bet
			// if (getEquals(submit_odd, bet_odd)) {
			// logger.info("match odd accept now bet ha ha!");
			// e = ticket_div.createElement("a");
			// e.setAttribute("onclick", "return placebet();");
			// ticket_div.appendChild(e);
			// e.click();
			// }

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info(ticket_page.asXml());
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
