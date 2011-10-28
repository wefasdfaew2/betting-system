package com.org.webbrowser;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

public class IbetAgent implements Runnable {
	private final Logger logger;
	private MySqlUtil sql_util;
	private WebClient webClient;
	private HtmlPage page;
	private BufferedImage imageBuffer;
	private HtmlForm login_form;
	private String username;
	private String password;
	private String from;
	private String to;
	private String captcha;

	private void logException(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		this.logger.error(sw.toString());
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public WebClient getWebClient() {
		return webClient;
	}

	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}

	public HtmlPage getPage() {
		return page;
	}

	public void setPage(HtmlPage page) {
		this.page = page;
	}

	public BufferedImage getImageBuffer() {
		return imageBuffer;
	}

	public void setImageBuffer(BufferedImage imageBuffer) {
		this.imageBuffer = imageBuffer;
	}

	public IbetAgent() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		PropertyConfigurator.configure("log4j.properties");
		logger = Logger.getLogger(IbetAgent.class);
		this.webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_8);
		this.webClient.setJavaScriptEnabled(true);
		this.webClient.setTimeout(10000);
		this.webClient.setThrowExceptionOnScriptError(true);

		this.page = webClient.getPage("http://ag.ibc88.com/");
		this.login_form = page.getFormByName("fLogin");
		// get image, first get link after that get inputstream
		HtmlElement image_element = login_form.getElementById("imgCode");
		URL image_url = page.getFullyQualifiedUrl(image_element
				.getAttribute("src"));

		UnexpectedPage image_page = webClient.getPage(image_url);
		// get captcha code
		// CaptchaUtilities captcha_util = new CaptchaUtilities();
		this.imageBuffer = ImageIO.read(image_page.getInputStream());
	}

	public void ibetAgent(String fromdate, String todate) throws Exception {

		// FileOutputStream output = new FileOutputStream("C:\\captcha.jpg");
		// ImageIO.write(imageBuffer, "jpg", output);
		// String code = captcha_util.GetNumber(imageBuffer, Site.ASBOBET);
		// System.out.println(code);

		login_form.getInputByName("txtUserName").setValueAttribute(
				this.username);
		login_form.getInputByName("txtPassWord").setValueAttribute(
				this.password);
		login_form.getInputByName("txtCaptcha").setValueAttribute(this.captcha);

		HtmlButton submitButton = (HtmlButton) this.page
				.createElement("button");
		submitButton.setAttribute("type", "submit");
		this.login_form.appendChild(submitButton);

		this.page = submitButton.click();
		Thread.sleep(2000);
		HtmlPage menu_page = (HtmlPage) this.webClient.getWebWindowByName(
				"menu").getEnclosedPage();

		// navigate to win lost
		Thread.sleep(2000);
		HtmlAnchor win_lost = menu_page
				.getFirstByXPath("/html/body/div/div[2]/div[2]/div/div/div[2]/a[2]");
		win_lost.click();
		// now change the date time of the input date time field then click
		// submit

		HtmlPage win_lose = (HtmlPage) this.webClient
				.getWebWindowByName("main").getEnclosedPage();
		HtmlForm main_form = (HtmlForm) win_lose.getElementById("frmmain");
		main_form.getInputByName("fdate").setAttribute("value", fromdate);
		main_form.getInputByName("tdate").setAttribute("value", todate);
		HtmlButtonInput submit_button = main_form.getElementById("dSubmit");
		submit_button.click();

		// System.out.println(menu_page.asXml());

		win_lose = (HtmlPage) this.webClient.getWebWindowByName("main")
				.getEnclosedPage();
		// System.out.println(win_lose.asText());
		sql_util = new MySqlUtil("root", "123456",
				"jdbc:mysql://localhost/ibet");

		travere(win_lose);
		logger.info("done");
		this.webClient.closeAllWindows();
		// login_form.getInputByName("txtCaptcha").setValueAttribute(code);
	}

	public void travere(HtmlPage win_lose) throws Exception {		

		HtmlTable table = (HtmlTable) win_lose.getElementById("tbl-rpt");

		// get the header, if header is null then it is not the member page

		if (table != null && table.getHeader() == null) {
			// travere all row of the body

			HtmlTableBody body = table.getBodies().get(0);
			List<HtmlTableRow> all_rows = body.getRows();
			int row_count = all_rows.size();
			// System.out.println(row_count);
			for (int i = 3; i < row_count; i++) {
				HtmlTableRow row = all_rows.get(i);				
				// each row click to the first cell to member
				// if not the total row
				if (row.getCells().size() == 0) {
					logger.info(row.asText() + "stuck stupid zero row !!!"
							+ table.asText());
					continue;
				}

				if (!row.getCell(0).asText().equals("Total")
						&& !row.getCell(0).asText().equals("No records found.")) {
					// System.out.println(row.asText());

					HtmlSpan span = (HtmlSpan) row.getCell(0)
							.getHtmlElementsByTagName("span").get(0);
					try {
						HtmlPage sub_page = span.click();
						travere(sub_page);
						
						// after click to child user the page is modified
						// so we click to back to the page
						// List<HtmlElement> a_tags = win_lose.getElementById(
						// "navPath").getElementsByTagName("a");
						// // get the next to the last anchor
						// HtmlAnchor a_tag = (HtmlAnchor) a_tags.get(a_tags
						// .size() - 2);
						// a_tag.click();

						// table = (HtmlTable) win_lose.getElementById(
						// "divDataList").getFirstChild();
						// body = table.getBodies().get(0);
						// all_rows = body.getRows();
					} catch (Exception e) {
						logException(e);
					}
					// and we start traverse

				}
			}
		} else {
			// now find the end user not agent
			// we process it to take user data
			// get the user id
			String title = ((HtmlElement) win_lose
					.getFirstByXPath("/html/body/div")).asText();
			table = (HtmlTable) win_lose.getElementById("hor-minimalist-a");
			// System.out.println(table.getBodies().get(0).asText());
			// List<HtmlElement> a_tags = win_lose.getElementById("navPath")
			// .getElementsByTagName("a");
			// // get the next to the last anchor
			// HtmlAnchor a_tag = (HtmlAnchor) a_tags.get(a_tags.size() - 1);
			// String username = a_tag.asText();
			logger.info("Found User:");
			String[] element = title.split(" ");
			String user = element[element.length - 1];
			logger.info(user);
			// // get the list of ip, we put it in a dictionary with key is
			// // <ip,user>
			HashMap<String, String> dict = new HashMap<String, String>();
			HtmlTableBody body = table.getBodies().get(0);
			List<HtmlTableRow> all_rows = body.getRows();
			logger.info("found ip list :");
			for (HtmlTableRow row : all_rows) {
				String ip = "";
				// get the last cell is the ip cell
				List<HtmlTableCell> all_cells = row.getCells();
				if (all_cells.size() >= 6) {
					String[] ip_list = all_cells.get(6).asText().split("\n");
					ip = ip_list[ip_list.length - 1];
					// System.out.println(ip);
				}
				Pattern p = Pattern.compile("\\d\\d/\\d\\d/\\d\\d\\d\\d ");
				Matcher m = p.matcher(all_cells.get(1).asText());
				// Split input with the pattern
				while (m.find()) {
					String date = m.group();

					if (!dict.containsKey(ip + date)) {
						dict.put(ip + date, user);
						// now add to database
						logger.info(ip + ": " + date);
						String[] sub_ip = ip.split("\\.");
						if (sub_ip.length >= 4)
							sql_util.insert(user, sub_ip[0], sub_ip[1],
									sub_ip[2], sub_ip[3], date);
					}
				}

			}
			// // System.out.println(table.asText());

			Thread.sleep(2000);

		}
		// go back
		HtmlButton back_button = (HtmlButton) win_lose.createElement("button");
		back_button.setAttribute("onclick", "history.go(-1)");
		win_lose.appendChild(back_button);
		win_lose = back_button.click();

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.ibetAgent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logException(e);
		}
	}

	private void ibetAgent() throws Exception {
		// TODO Auto-generated method stub
		// GregorianCalendar gcal = new GregorianCalendar();
		// SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		// Date start = sdf.parse(this.from);
		// Date end = sdf.parse(this.to);
		// gcal.setTime(start);
		// while (gcal.getTime().before(end)) {
		// String from = sdf.format(gcal.getTime());
		// String to;
		// gcal.add(Calendar.DAY_OF_YEAR, 5);
		// if (gcal.getTime().before(end)) {
		// to = sdf.format(gcal.getTime());
		// } else {
		// to = sdf.format(end);
		// }
		// System.out.println(from + "-->" + to);
		// IbetAgent sub_agent = new IbetAgent();
		// }
		this.ibetAgent(this.from, this.to);

	}

	public static void main(String[] args) throws ParseException {
		GregorianCalendar gcal = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date start = sdf.parse("10/10/2010");
		Date end = sdf.parse("10/30/2010");
		gcal.setTime(start);
		while (gcal.getTime().before(end)) {
			String from = sdf.format(gcal.getTime());
			String to;
			gcal.add(Calendar.DAY_OF_YEAR, 5);
			if (gcal.getTime().before(end)) {
				to = sdf.format(gcal.getTime());
			} else {
				to = sdf.format(end);
			}
			System.out.println(from + "-->" + to);
		}
	}

}
