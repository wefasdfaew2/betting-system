package com.org.player;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
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
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.org.captcha.CaptchaUtilities;
import com.org.captcha.Site;
import com.org.messagequeue.JMSConfiguration;
import com.org.messagequeue.TopicPublisher;
import com.org.odd.Odd;
import com.org.odd.OddElement;
import com.org.odd.OddSide;
import com.org.odd.OddUtilities;

public class SbobetPlayer extends Thread implements MessageListener, IPlayer {
	String url = JMSConfiguration.getHostURL();
	private TopicPublisher p;
	private final Logger logger;
	private String username;
	private String pass;
	// private int sleep_time = 500;
	private OddUtilities util;
	private OddSide side;
	private HtmlPage page;
	private HtmlPage odd_page;
	private HtmlPage ticket_page;
	private HtmlPage left_page;
	private HtmlPage ticket_div;
	private WebClient webClient;
	private boolean isCrawler;
	private HashMap<String, OddElement> current_map_odds;
	HtmlElement refresh_live;
	HtmlElement refresh_nonlive;
	private boolean isPolling = false;
	private HtmlElement stop_button;
	private boolean isLoggin = false;
	Session session;
	Connection connection;

	public HashMap<String, OddElement> getCurrent_map_odds() {
		return current_map_odds;
	}

	public void setCurrent_map_odds(HashMap<String, OddElement> current_map_odds) {
		this.current_map_odds = current_map_odds;
	}

	public static void main(String[] argv) {
		while (true) {
			try {
				OddSide side = OddSide.LIVE;
				SbobetPlayer client = new SbobetPlayer(argv[0], argv[1], side,
						false);
				// if not is crawler then start listen to bet
				if (!client.isCrawler)
					try {
						client.startConnection();
					} catch (JMSException e) {
						client.logger.info("error establish JMS connection");
						return;
					}
				else
					client.logger.info("start crawling...");

				client.homePage();
				Thread.sleep(1000 * 60 * 60 * 1);// play for 1 hours
				client.loggout();

			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FailingHttpStatusCodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public Logger getLogger() {
		return logger;
	}

	public void startConnection() throws JMSException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
		this.connection = factory.createConnection();
		this.session = connection
				.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue topic = session.createQueue(this.username);
		MessageConsumer consumer = session.createConsumer(topic);
		consumer.setMessageListener(this);
		connection.start();
		logger.info("Connected to Messge Server...");
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
		this.current_map_odds = new HashMap<String, OddElement>();
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
		this.current_map_odds = new HashMap<String, OddElement>();
	}

	public void sendData(HashMap<String, OddElement> map_odds)
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
			} else {
				send_odds.put(e.getKey(), e.getValue().getOdd());
			}
		}
		// update current map to update map
		this.current_map_odds = map_odds;
		if (send_odds.size() > 0)
			p.sendMapMessage(send_odds, "sbobet");
	}

	public void loggout() throws JMSException {
		// stop any polling thread
		if (this.isAlive())
			this.interrupt();
		this.webClient.closeAllWindows();
		this.webClient = null;
		this.session.close();
		this.connection.stop();
		this.isLoggin = false;
	}

	public void homePage() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException, InterruptedException,
			JMSException {
		webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_6);
		webClient.setJavaScriptEnabled(true);
		webClient.setTimeout(5000);
		webClient.setThrowExceptionOnScriptError(false);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());

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

		// after login find the new host

		webClient.waitForBackgroundJavaScript(30000);

		left_page = (HtmlPage) webClient.getWebWindowByName("leftFrame")
				.getEnclosedPage();
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
		//
		// int i = 1;
		

		this.isLoggin = true;
		logger.info("loggin as " + this.username);

		// getUserInfo();

		// while (true) {
		// this.doPolling();
		// Thread.sleep(1000);
		// }

	}

	private UserInfo getUserInfo() {
		UserInfo info = null;
		HtmlPage credit_page = (HtmlPage) this.webClient.getWebWindowByName(
				"BCIFrame").getEnclosedPage();
		float credit = Float.parseFloat(credit_page.getElementById("bc")
				.asText());
		System.out.println(credit);
		return info;
	}

	public void doPolling() {
		// avoid overhead if is polling then return
		// if not login then quit
		// set is polling to true so while polling do not polling any more
		this.isPolling = true;
		HashMap<String, OddElement> map_odds = new HashMap<String, OddElement>();
		if (this.side == OddSide.LIVE || this.side == OddSide.TODAY) {
			// refresh_live.click();
			odd_page.executeJavaScript("od_OnRefreshManually(0)");

			if (odd_page.getElementById("levents") != null
					&& odd_page.getElementById("levents").getFirstChild() != null
					&& !odd_page.getElementById("levents").getFirstChild()
							.asXml().equals("")) {
				HtmlTable table = (HtmlTable) odd_page
						.getElementById("levents").getFirstChild();
				table = (HtmlTable) table.getBodies().get(0).getRows().get(0)
						.getCell(0).getFirstChild();

				map_odds = this.util.getOddsFromSobet(table);

			}
		}
		// non live, early and today both table in div "events"
		if (this.side == OddSide.NON_LIVE || this.side == OddSide.EARLY
				|| this.side == OddSide.TODAY) {
			if (this.side == OddSide.NON_LIVE)
				odd_page.executeJavaScript("od_OnRefreshManually(1)");
			else if (this.side == OddSide.EARLY)
				odd_page.executeJavaScript("od_OnRefreshManually(0)");

			if (odd_page.getElementById("events") != null
					&& odd_page.getElementById("events").getFirstChild() != null
					&& !odd_page.getElementById("events").getFirstChild()
							.asXml().equals("")) {
				HtmlTable table_nonlive = (HtmlTable) odd_page.getElementById(
						"events").getFirstChild();
				table_nonlive = (HtmlTable) table_nonlive.getBodies().get(0)
						.getRows().get(0).getCell(0).getFirstChild();
				map_odds = this.util.getOddsFromSobet(table_nonlive);
			}
		}

		// trying to stop all java script :(

		try {
			this.sendData(map_odds);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// stop_button.click();

		this.isPolling = false;
	}

	public void poll() {
		// TODO Auto-generated method stub
		try {
			if (isPolling || !isLoggin)
				return;
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
				// logger.info("sbo received");
				ObjectMessage mes = (ObjectMessage) message;
				Odd odd = (Odd) mes.getObject();
				boolean is_home = mes.getBooleanProperty("home");
				// do update again to up to newest odd, if crawl exactly do not
				// need to do this

				if (this.current_map_odds.containsKey(odd.getId())) {
					logger.info(odd);
					if (is_home)
						this.placeBet(this.current_map_odds.get(odd.getId())
								.getHome());
					else
						this.placeBet(this.current_map_odds.get(odd.getId())
								.getAway());
				} else {
					logger.info("odd disapear...");
				}

			} else if (message instanceof TextMessage) {
				TextMessage mes = (TextMessage) message;
				if (mes.getText().equals("UPDATE")) {
					this.poll();
				} else if (mes.getText().equals("OK")) {
					try {
						this.doBet();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						this.logger.info(StackTraceUtil.getStackTrace(e));
					}
				} else {
					this.poll();
					for (OddElement e : this.current_map_odds.values()) {
						this.placeBet(e.getHome());
						return;
					}
				}
			}
			// logger.info(((ObjectMessage)message));
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.startConnection();
			this.homePage();
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void placeBet(HtmlElement element) {
		try {
			HtmlElement odd_element = (HtmlElement) element.getFirstChild();
			odd_element.setAttribute("onclick", "od_OnClick(0, event)");

			// if (this.side == OddSide.LIVE || this.side == OddSide.EARLY)
			// odd_element.setAttribute("onclick", "od_OnClick(0, event)");
			// else if (this.side == OddSide.NON_LIVE)
			// odd_element.setAttribute("onclick", "od_OnClick(1, event)");

			// String submit_odd = odd_element.asText();
			// logger.info(odd_element.asXml());

			odd_element.click();
			Thread.sleep(50);
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
				Thread.sleep(50);
			} catch (Exception e) {
				logger.info("ticket openned");
			}
			// // after open ticket form then find the real odd
			// String bet_odd =
			// ticket_div.getElementById("ticoddsVal").asText();
			//
			// logger.info("submitted to bet :" + submit_odd);
			// logger.info("real odd to bet :" + bet_odd);
			// fill the stake
			HtmlElement stake_input = ticket_div.getElementById("stake");
			stake_input.setAttribute("value", "10");

			// }

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info(ticket_page.asXml());
			logger.error(StackTraceUtil.getStackTrace(e));
		}

	}

	private void doBet() throws IOException, InterruptedException {
		// virtual element to place bet
		// if (getEquals(submit_odd, bet_odd)) {
		// logger.info("match odd accept now bet ha ha!");
		ticket_div = (HtmlPage) this.webClient.getWebWindowByName("leftFrame")
				.getEnclosedPage();
		HtmlElement e = ticket_div.createElement("a");
		e.setAttribute("onclick", "return placebet();");
		ticket_div.appendChild(e);
		e.click();
		Thread.sleep(50);
		logger.info(ticket_div.asText());
	}

	// private boolean getEquals(String o1, String o2) {
	// try {
	// float a1 = Float.parseFloat(o1);
	// float a2 = Float.parseFloat(o2);
	// return a1 == a2;
	// } catch (NumberFormatException e) {
	// return false;
	// }
	// }
}
