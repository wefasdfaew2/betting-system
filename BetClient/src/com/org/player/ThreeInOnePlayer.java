package com.org.player;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
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
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.xalan.xsltc.compiler.sym;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.org.captcha.CaptchaUtilities;
import com.org.captcha.Site;
import com.org.messagequeue.JMSConfiguration;
import com.org.messagequeue.MessagePublisher;
import com.org.messagequeue.TopicPublisher;
import com.org.odd.Odd;
import com.org.odd.OddElement;
import com.org.odd.OddSide;
import com.org.odd.OddUtilities;

public class ThreeInOnePlayer extends Thread implements MessageListener {
	String url = JMSConfiguration.getHostURL();
	private final Logger logger;
	private String username;
	private String pass;
	// private int sleep_time = 3000;
	private TopicPublisher p;
	private OddUtilities util;
	private OddSide side;
	private HtmlPage odd_page;
	private WebClient webClient;
	private HtmlPage page;
	private HtmlPage ticket_page;
	private HashMap<String, OddElement> current_map_odds;

	HtmlElement refresh_live;
	HtmlElement refresh_nonlive;
	HtmlElement refresh_early;
	private boolean isPolling = false;
	private boolean isLoggin = false;
	Session session;
	Connection connection;
	MessagePublisher sbo = new MessagePublisher("Maj3259005");

	public static void main(String[] argv) {
		while (true) {
			try {
				OddSide side = OddSide.LIVE;
				ThreeInOnePlayer client = new ThreeInOnePlayer(argv[0],
						argv[1], side);
				try {
					client.startConnection();
				} catch (JMSException e) {
					client.logger.info("error establish JMS connection");
					return;
				}

				client.homePage();
				Thread.sleep(1000 * 60 * 60 * 1);// play for 2 hours
				client.loggout();
				client.isLoggin = false;

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

	private synchronized void loggout() throws JMSException {
		// stop any polling thread
		if (this.isAlive())
			this.interrupt();
		this.webClient.closeAllWindows();
		this.webClient = null;
		this.session.close();
		this.connection.stop();
		this.isLoggin = false;
	}

	private synchronized void startConnection() throws JMSException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
		connection = factory.createConnection();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue topic = session.createQueue(this.username);
		MessageConsumer consumer = session.createConsumer(topic);
		consumer.setMessageListener(this);
		connection.start();
		logger.info("Connected to Messge Server...");
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

	private synchronized void sendData(HashMap<String, OddElement> map_odds)
			throws JMSException {
		HashMap<String, Odd> send_odds = new HashMap<String, Odd>();

		// now send only the odd which is updated
		// iterate through old if not in new then set null
		for (Entry<String, OddElement> e : this.current_map_odds.entrySet()) {
			// if not in new
			if (!map_odds.containsKey(e.getKey())) {
				send_odds.put(e.getKey(), null);
			}
		}
		// iterate through new and see if update then send
		for (Entry<String, OddElement> e : map_odds.entrySet()) {
			// compare if update
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
			p.sendMapMessage(send_odds, "3in");
	}

	public void witeStringtoFile(String content, String file)
			throws IOException {
		FileOutputStream op = new FileOutputStream(new File(file));
		op.write(content.getBytes());
		op.close();
	}

	private synchronized void homePage() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException, InterruptedException,
			JMSException {
		webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_6);
		webClient.setJavaScriptEnabled(true);
		webClient.setTimeout(5000);
		webClient.setThrowExceptionOnScriptError(false);
		webClient.setThrowExceptionOnFailingStatusCode(false);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());

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

		this.isLoggin = true;
		logger.info("Logged in as " + this.username);

		HtmlForm form = (HtmlForm) odd_page.getElementById("frmGVHDP");
		// virtual button to click refresh, call javascript skip check time
		refresh_live = odd_page.createElement("a");
		refresh_live
				.setAttribute(
						"onclick",
						"RefreshRunning();secondsLiveLeft = 10000000000;secondsTodayLeft = 10000000000;");
		form.appendChild(refresh_live);

		while (true) {
			long a = System.currentTimeMillis();
			refresh_live.click();
			this.doPolling();
			long b = System.currentTimeMillis();
			logger.info(b - a);
			Thread.sleep(2000);
		}
		// webClient.closeAllWindows();
	}

	private synchronized void doPolling() throws IOException,
			InterruptedException, JMSException {

		this.isPolling = true;
		// long delay = 0;
		HashMap<String, OddElement> map_odds = new HashMap<String, OddElement>();
		// Click update live and non-live
		// live
		FrameWindow frm_main = page.getFrameByName("fraMain");
		odd_page = (HtmlPage) frm_main.getEnclosedPage();

		// refresh_nonlive = odd_page.createElement("a");
		// refresh_nonlive
		// .setAttribute(
		// "onclick",
		// "RefreshIncrement();secondsLiveLeft = 10000000000;secondsTodayLeft = 10000000000;");
		// form.appendChild(refresh_nonlive);
		if (this.side == OddSide.LIVE || this.side == OddSide.TODAY) {
			// long startTime = System.currentTimeMillis();
			// refresh_live.click();

			// Thread.sleep(sleep_time);
			HtmlTable table = (HtmlTable) odd_page.getElementById("tblData5");

			map_odds = this.util.getOddsFromThreeInOne(table);
			// long endTime = System.currentTimeMillis();
			// long delay = endTime - startTime;
			// logger.info(delay);
			// logger.info(map_odds);
			// String d = "" + delay;

		}
		if (this.side == OddSide.NON_LIVE || this.side == OddSide.EARLY
				|| this.side == OddSide.TODAY) {
			// long startTime = System.currentTimeMillis();
			// non -live
			// Thread.sleep(sleep_time);

			HtmlTable table_nonlive = (HtmlTable) odd_page
					.getElementById("tblData6");
			map_odds = this.util.getOddsFromThreeInOne(table_nonlive);
			// long endTime = System.currentTimeMillis();
			// long delay = endTime - startTime;
			// logger.info(delay);
			// String d = "" + delay;
			// p.sendMessage(d);
		}
		this.sendData(map_odds);
		// stop_button.click();
		this.isPolling = false;
	}

	// private void sendData(HtmlTable table, HtmlTable table_nonlive)
	// throws JMSException {
	// if (table_nonlive != null) {
	// // logger.info(table_nonlive.asText());
	// p.sendMessage(table_nonlive.asText());
	// }
	// if (table != null) {
	// // logger.info(table.asText());
	// p.sendMessage(table.asText());
	// }
	// }

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
			if (this.isPolling || !this.isLoggin)
				return;
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
				// try {
				// this.doPolling();
				// } catch (Exception e) {
				// // TODO: handle exception
				// }
				// if (this.current_map_odds.containsKey(odd.getId())) {
				logger.info(odd);
				if (is_home)
					this.placeBet(odd.getOdd_home_xpath(), odd.getOdd_home());
				else
					this.placeBet(odd.getOdd_away_xpath(), odd.getOdd_away());
				// } else {
				// logger.info("odd disapear...");
				// }
				// logger.info(odd);

			} else if (message instanceof TextMessage) {
				TextMessage mes = (TextMessage) message;
				if (mes.getText().equals("UPDATE")) {
					Thread t = new Thread(this);
					t.start();
				} else {
					// this for debug only
					this.placeBet(mes.getText(), 0.1f);
				}

			}
			// logger.info(((ObjectMessage)message));
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private synchronized void placeBet(String script, float odd_value) {
		try {
			// logger.info("3in received");
			// String submit_odd = odd_element.asText();
			String key = script.split("'")[1];

			// form hdpdouble from bet page
			HtmlPage main_page = (HtmlPage) this.webClient.getWebWindowByName(
					"fraMain").getEnclosedPage();
			String java_script = "var info = CacheData('key');$(this).text(info);"
					.replaceAll("key", key);
			HtmlForm form = (HtmlForm) main_page.getElementById("frmGVHDP");
			HtmlElement odd_element = main_page.createElement("a");
			odd_element.setAttribute("onclick", java_script);
			form.appendChild(odd_element);
			odd_element.click();

			String info = odd_element.asText();
			logger.info(info);

			// admin left page
			ticket_page = (HtmlPage) this.webClient.getWebWindowByName(
					"fraPanel").getEnclosedPage();
			odd_element = ticket_page.createElement("a");
			odd_element.setAttribute("onclick",
					"onOddsClick('info');".replaceAll("info", info));
			// logger.info(odd_element.asXml());
			odd_element.click();
			this.webClient.waitForBackgroundJavaScript(100);
			String odd_string = "";
			float real_odd = 0;
			long start = System.currentTimeMillis();
			for (int i = 0; i < 15; i++) {
				odd_string = ticket_page.getElementById("lb_bet_odds").asText();
				if (!odd_string.isEmpty()) {
					real_odd = Float.parseFloat(odd_string);
					break;
				} else {
					synchronized (ticket_page) {
						ticket_page.wait(50);
					}
				}
			}
			if (real_odd == 0)
				return;
			long end = System.currentTimeMillis();
			logger.info("delay is :" + (end - start));
			logger.info("real odd is : " + real_odd);
			logger.info("real match is "
					+ ticket_page.getElementById("lb_home_team").asText() + ":"
					+ ticket_page.getElementById("lb_away_team").asText());
			if (real_odd * odd_value > 0 && real_odd < odd_value)
				return;
			if (real_odd > 0 && odd_value < 0)
				return;
			// good to bet then bet now
			this.sbo.setText("OK");
			(new Thread(this.sbo)).start();

			// String bet_odd =
			// ticket_page.getElementById("lb_bet_odds").asText();
			// logger.info("match : "
			// + ticket_page.getElementById("pn_title").asText());
			// logger.info("team to bet :"
			// + ticket_page.getElementById("lb_bet_team").asText());
			// logger.info("hdp info : "
			// + ticket_page.getElementById("td_tbg").asText());
			// logger.info("submitted to bet :" + submit_odd);
			// logger.info("real odd to bet :" + bet_odd);
			// logger.info(ticket_page.asText());

			// if (getEquals(submit_odd, bet_odd)) {
			// logger.fatal("Ha ha we can bet now !!!");
			HtmlElement bet_button = ticket_page.createElement("button");
			bet_button.setAttribute("onclick", "onBet();");
			ticket_page.appendChild(bet_button);
			// click bet

			ticket_page = bet_button.click();
			logger.info(ticket_page.asText());
			//
			// }
		} catch (Exception e) {
			logger.error(StackTraceUtil.getStackTrace(e));
		}
	}

	public void placeBet(String script, HtmlElement element) {
		try {
			HtmlElement odd_element = (HtmlElement) element.getFirstChild();
			// String submit_odd = odd_element.asText();
			odd_element.setAttribute("onclick", script);
			odd_element.removeAttribute("href");
			logger.info(odd_element.asXml());

			odd_element.click();
			Thread.sleep(30);

			ticket_page = (HtmlPage) this.webClient.getWebWindowByName(
					"fraPanel").getEnclosedPage();

			// String bet_odd =
			// ticket_page.getElementById("lb_bet_odds").asText();
			// logger.info("match : "
			// + ticket_page.getElementById("pn_title").asText());
			// logger.info("team to bet :"
			// + ticket_page.getElementById("lb_bet_team").asText());
			// logger.info("hdp info : "
			// + ticket_page.getElementById("td_tbg").asText());
			// logger.info("submitted to bet :" + submit_odd);
			// logger.info("real odd to bet :" + bet_odd);
			// logger.info(ticket_page.asText());

			// if (getEquals(submit_odd, bet_odd)) {
			// logger.fatal("Ha ha we can bet now !!!");
			HtmlElement bet_button = ticket_page.createElement("button");
			bet_button.setAttribute("onclick", "onBet();");
			// click bet

			ticket_page = bet_button.click();
			Thread.sleep(30);
			logger.info(ticket_page.asText());
			//
			// }
		} catch (Exception e) {
			logger.error(StackTraceUtil.getStackTrace(e));
		}
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
