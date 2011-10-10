package com.org.client;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import com.org.betcrawler.Crawler;
import com.org.captcha.CaptchaUtilities;
import com.org.captcha.Site;

public class SbobetMemberClient {

	public void testLogInSbobet() {
		Crawler crawler = new Crawler("http://www.sbobetasia.com/",
				"hometop.aspx?ip=&p=",
				"controls/ImgText.ashx\\?c=(.*)&a=(.*)&f=default",
				"processlogin.aspx", Site.SBOBET);
		InputStream ip = crawler.BasicGetStreamFromURL(crawler.getDomain()
				+ crawler.getInitURL(), crawler.getDomain());
		try {
			String html = IOUtils.toString(ip, "utf-8");
			System.out.println(html);
			ip.close();
			String image_url = crawler.GetCaptchaImageURL(html);
			crawler.setHtmldata(html);
			crawler.GetHiddenParamTea(html);
			crawler.GetHiddenParamKey(html);
			crawler.GetHiddenParamSV(html);
			crawler.GetHiddenParamtzDiff(html);

			String code = crawler.GetValidateCode("http://www.sbobetasia.com/"
					+ image_url, "http://www.sbobetasia.com/");
			System.out.println(code);

			ip = crawler.ProcessLogin("maj1599999", "aaaa1111", code);
			String logindata = IOUtils.toString(ip, "utf-8");
			System.out.println(logindata);
			ip.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("encoding error");
			// e.printStackTrace();
		}
		// fail("Not yet implemented");
	}

	

}
