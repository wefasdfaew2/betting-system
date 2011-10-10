package com.org.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.http.message.BasicNameValuePair;

import com.org.betcrawler.Crawler;
import com.org.captcha.Site;

public class SbobetAgentClient {

	public void testLoginSbobetAgent() {
		Crawler crawler = new Crawler("http://agent.sbobetasia.com/", "",
				"imgtext.ashx\\?s=(.*)&c=(.*)&f=default", "processlogin.aspx",
				Site.ASBOBET);
		InputStream ip = crawler.BasicGetStreamFromURL(crawler.getDomain()
				+ crawler.getInitURL(), crawler.getDomain());
		try {
			String html = IOUtils.toString(ip, "utf-8");
			System.out.println(html);

			ip.close();

			// get captcha and decode
			List<BasicNameValuePair> post_data = new ArrayList<BasicNameValuePair>();
			ip = crawler.basicPost(post_data,
					"http://agent.sbobetasia.com/ImgTextRefresh.aspx");
			html = IOUtils.toString(ip, "utf-8");
			System.out.println(html);
			ip.close();
			String image_url = crawler.GetCaptchaImageURL(html);
			System.out.println(image_url);
			String code = crawler.GetValidateCode(crawler.getDomain()
					+ image_url, crawler.getDomain());
			System.out.println(code);

			// do login by post

			post_data.add(new BasicNameValuePair("username", "10@hzw"));
			post_data.add(new BasicNameValuePair("password", "mm222222"));
			post_data.add(new BasicNameValuePair("vcode", code));

			ip = crawler.basicPost(post_data,
					crawler.getDomain() + crawler.getLoginURL());
			html = IOUtils.toString(ip, "utf-8");
			System.out.println(html);
			ip.close();

			// get id post data
			Pattern p = Pattern
					.compile("<input type='hidden' name='id' value='(.*)' />");
			String id = "";
			Matcher matcher = p.matcher(html);
			while (matcher.find()) {
				id = matcher.group(1);
				System.out.println(id);
			}
			// get key post data
			p = Pattern
					.compile("<input type='hidden' name='key' value='(.*)' />");
			String key = "";
			matcher = p.matcher(html);
			while (matcher.find()) {
				key = matcher.group(1);
				System.out.println(key);
			}

			// get key post data
			p = Pattern
					.compile("<input type='hidden' name='lang' value='(.*)' />");
			String lang = "";
			matcher = p.matcher(html);
			while (matcher.find()) {
				lang = matcher.group(1);
				System.out.println(lang);
			}
			// get url get
			p = Pattern.compile("action='(.*)' method=");
			String go_url = "";
			matcher = p.matcher(html);
			while (matcher.find()) {
				go_url = matcher.group(1);

			}
			go_url = go_url + "?id=" + id + "&key=" + key + "&lang=" + lang;
			System.out.println(go_url);
			ip = crawler.BasicGetStreamFromURL(go_url, crawler.getDomain()
					+ crawler.getLoginURL());
			html = IOUtils.toString(ip, "utf-8");
			System.out.println(html);
			// crawler.setHtmldata(html);
			// crawler.GetHiddenParamTea(html);
			// crawler.GetHiddenParamKey(html);
			// crawler.GetHiddenParamSV(html);
			// crawler.GetHiddenParamtzDiff(html);
			//
			// String code =
			// crawler.GetValidateCode("http://www.sbobetasia.com/"
			// + image_url, "http://www.sbobetasia.com/");
			// System.out.println(code);
			//
			// ip = crawler.ProcessLogin("maj1599999", "aaaa1111", code);
			// String logindata = IOUtils.toString(ip, "utf-8");
			// System.out.println(logindata);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("encoding error");
			// e.printStackTrace();
		}
		// fail("Not yet implemented");

	}
}
