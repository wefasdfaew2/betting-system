package com.org.webbrowser;

import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableHeader;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.org.captcha.CaptchaUtilities;
import com.org.captcha.Site;

public class SbobetAgent implements Runnable {
	private final Logger logger;
	private MySqlUtil sql_util;
	private String username;
	private String password;
	private String from_date;
	private String to_date;

	private void logException(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		this.logger.error(sw.toString());
	}

	public SbobetAgent(String username, String password, String from_date,
			String to_date) {
		super();
		PropertyConfigurator.configure("log4j.properties");
		logger = Logger.getLogger(SbobetAgent.class);
		this.username = username;
		this.password = password;
		this.from_date = from_date;
		this.to_date = to_date;
	}

	public void travere(HtmlPage win_lose) throws Exception {
		HtmlTable table = (HtmlTable) win_lose.getElementById("divDataList")
				.getFirstChild();
		// get the header
		HtmlTableHeader header = table.getHeader();
		if (header.getRows().get(0).getCell(0).asText().equals("Username")) {
			// travere all row of the body
			HtmlTableBody body = table.getBodies().get(0);
			List<HtmlTableRow> all_rows = body.getRows();
			int row_count = all_rows.size();

			for (int i = 0; i < row_count; i++) {
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

						table = (HtmlTable) win_lose.getElementById(
								"divDataList").getFirstChild();
						body = table.getBodies().get(0);
						all_rows = body.getRows();
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
			List<HtmlElement> a_tags = win_lose.getElementById("navPath")
					.getElementsByTagName("a");
			// get the next to the last anchor
			HtmlAnchor a_tag = (HtmlAnchor) a_tags.get(a_tags.size() - 1);
			String username = a_tag.asText();
			logger.info("Found User:");
			logger.info(username);
			// get the list of ip, we put it in a dictionary with key is
			// <ip,user>
			HashMap<String, String> dict = new HashMap<String, String>();
			HtmlTableBody body = table.getBodies().get(0);
			List<HtmlTableRow> all_rows = body.getRows();
			logger.info("found ip list :");
			for (HtmlTableRow row : all_rows) {
				// get the last cell is the ip cell
				List<HtmlTableCell> all_cells = row.getCells();
				String ip = all_cells.get(all_cells.size() - 1).asText();

				Pattern p = Pattern.compile("\\d\\d/\\d\\d/\\d\\d\\d\\d");
				Matcher m = p.matcher(all_cells.get(2).asText());
				// Split input with the pattern
				while (m.find()) {
					String date = m.group();
					if (!dict.containsKey(ip + date)) {
						dict.put(ip + date, username);
						// now add to database
						logger.info(ip + ": " + date);
						String[] sub_ip = ip.split("\\.");
						sql_util.insert(username, sub_ip[0], sub_ip[1],
								sub_ip[2], sub_ip[3], date);
					}
				}

			}
			// System.out.println(table.asText());
			Thread.sleep(2000);
		}
		// go back
		List<HtmlElement> a_tags = win_lose.getElementById("navPath")
				.getElementsByTagName("a");
		// get the next to the last anchor
		HtmlAnchor a_tag = (HtmlAnchor) a_tags.get(a_tags.size() - 2);
		a_tag.click();
	}

	public void sboAgent() throws Exception {
		final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_3_6);
		webClient.setJavaScriptEnabled(true);
		webClient.setTimeout(10000);
		final HtmlPage page = webClient.getPage("http://agent.sbobetasia.com/");
		HtmlForm login_form = page.getFormByName("loginForm");
		// get image, first get link after that get inputstream
		HtmlElement element = login_form.getElementById("imgImgText");
		URL image_url = page.getFullyQualifiedUrl(element.getAttribute("src"));
		logger.info(image_url);
		UnexpectedPage image_page = webClient.getPage(image_url);
		// get captcha code
		CaptchaUtilities captcha_util = new CaptchaUtilities();
		BufferedImage imageBuffer = ImageIO.read(image_page.getInputStream());
		String code = captcha_util.GetNumber(imageBuffer, Site.ASBOBET);
		logger.info(code);

		login_form.getInputByName("username").setValueAttribute(username);
		login_form.getInputByName("password").setValueAttribute(password);
		login_form.getInputByName("vcode").setValueAttribute(code);

		// virtual submit button to navigate to agent page
		HtmlButton submitButton = (HtmlButton) page.createElement("button");
		submitButton.setAttribute("type", "submit");
		login_form.appendChild(submitButton);
		submitButton.click();

		for (final WebWindow window : webClient.getWebWindows()) {
			HtmlPage p = (HtmlPage) window.getEnclosedPage();
			if (p.getTitleText().equals("Menu")) {
				// here is menu to Win lose link click
				HtmlPage win_lose = p.getElementById("Report2_WinLoss").click();
				// inside win lose data table
				sql_util = new MySqlUtil("root", "123456",
						"jdbc:mysql://localhost/sbobet");
				win_lose.getElementById("dpFrom").setAttribute("value",
						from_date);
				win_lose.getElementById("dpTo").setAttribute("value", to_date);
				win_lose.getElementById("btnSettingSubmit").click();
				travere(win_lose);
				break;
			}
		}

		webClient.closeAllWindows();
	}

	@Test
	public void unittest() throws Exception {
		SbobetAgent agent = new SbobetAgent("10@hzw", "mm222222", "10/01/2011",
				"10/15/2011");
		agent.sboAgent();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.sboAgent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logException(e);
		}
	}
}
