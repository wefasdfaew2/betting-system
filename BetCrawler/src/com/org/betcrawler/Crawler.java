package com.org.betcrawler;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import org.apache.http.client.cache.HeaderConstants;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;

import com.org.captcha.CaptchaUtilities;
import com.org.captcha.Site;

public class Crawler {

	private String domain;
	private String initURL;
	private String captchaRegularExpression;
	private String captchaURL;
	private String loginURL;
	private Site site;
	private String htmldata;
	private HttpClient httpclient;

	public Crawler(String domain, String initURL,
			String captchaRegularExpression, String loginURL, Site site) {
		super();
		this.domain = domain;
		this.initURL = initURL;
		this.captchaRegularExpression = captchaRegularExpression;
		this.site = site;
		this.loginURL = loginURL;
		this.httpclient = new DefaultHttpClient();
	}

	public String getLoginURL() {
		return loginURL;
	}

	public void setLoginURL(String loginURL) {
		this.loginURL = loginURL;
	}

	public String getHtmldata() {
		return htmldata;
	}

	public void setHtmldata(String htmldata) {
		this.htmldata = htmldata;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getInitURL() {
		return initURL;
	}

	public void setInitURL(String initURL) {
		this.initURL = initURL;
	}

	public String getCaptchaRegularExpression() {
		return captchaRegularExpression;
	}

	public void setCaptchaRegularExpression(String captchaRegularExpression) {
		this.captchaRegularExpression = captchaRegularExpression;
	}

	public String getCaptchaURL() {
		return captchaURL;
	}

	public void setCaptchaURL(String captchaURL) {
		this.captchaURL = captchaURL;
	}
	
	public InputStream BasicGetImageStreamFromURL(String url, String referer) {
		// TODO Auto-generated method stub

		HttpGet httpget = new HttpGet(url);
		httpget.getParams()
				.setParameter(CoreProtocolPNames.USER_AGENT,
						"Mozilla/5.0 (Windows NT 6.1; rv:7.0.1) Gecko/20100101 Firefox/7.0.1");
		httpget.getParams().setParameter(
				CoreProtocolPNames.HTTP_CONTENT_CHARSET, "utf-8");

		httpget.setHeader("Referer", referer);
		httpget.setHeader("Accept-Language", "en-us,en;q=0.5");
		httpget.setHeader("Accept",
				"image/png,image/*;q=0.8,*/*;q=0.5\r\n");
		httpget.setHeader("Accept-Encoding", "gzip, deflate");
		httpget.setHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");

		HttpResponse response;
		try {
			response = httpclient.execute(httpget);			
			Header h = response.getFirstHeader("Content-Encoding");
			HttpEntity entity = response.getEntity();
			InputStream inputStream = null;
			if (entity != null) {
				if (h != null)
					if (h.getValue().equals("gzip")
							|| h.getValue().equals("deflate")) {
						GzipDecompressingEntity gzip_entity = new GzipDecompressingEntity(
								entity);
						inputStream = gzip_entity.getContent();

					} else {
						inputStream = entity.getContent();
					}
				else {
					inputStream = entity.getContent();
				}
			}
			return inputStream;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}


	public InputStream BasicGetStreamFromURL(String url, String referer) {
		// TODO Auto-generated method stub

		HttpGet httpget = new HttpGet(url);
		httpget.getParams()
				.setParameter(CoreProtocolPNames.USER_AGENT,
						"Mozilla/5.0 (Windows NT 6.1; rv:7.0.1) Gecko/20100101 Firefox/7.0.1");
		httpget.getParams().setParameter(
				CoreProtocolPNames.HTTP_CONTENT_CHARSET, "utf-8");

		httpget.setHeader("Referer", referer);
		httpget.setHeader("Accept-Language", "en-us,en;q=0.5");
		httpget.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpget.setHeader("Accept-Encoding", "gzip, deflate");
		httpget.setHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");

		HttpResponse response;
		try {
			response = httpclient.execute(httpget);			
			Header h = response.getFirstHeader("Content-Encoding");
			HttpEntity entity = response.getEntity();
			InputStream inputStream = null;
			if (entity != null) {
				if (h != null)
					if (h.getValue().equals("gzip")
							|| h.getValue().equals("deflate")) {
						GzipDecompressingEntity gzip_entity = new GzipDecompressingEntity(
								entity);
						inputStream = gzip_entity.getContent();

					} else {
						inputStream = entity.getContent();
					}
				else {
					inputStream = entity.getContent();
				}
			}
			return inputStream;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * get Validator code from URL string and its referer
	 * 
	 * @param URL
	 * @return
	 */
	public String GetValidateCode(String URL, String referer) {
		InputStream inputStream = BasicGetStreamFromURL(URL, referer);
		try {
			BufferedImage img = ImageIO.read(inputStream);
			CaptchaUtilities util = new CaptchaUtilities();
			return util.GetNumber(img, this.site);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "invalid";
		}

	}

	public String GetHiddenParamTea(String html) {
		// pattern to find hidden Tea
		Pattern p = Pattern
				.compile("<input id=\"tea\" name=\"tea\" type=\"hidden\" value=\"(.*)\" />");
		String pattern = "";
		Matcher matcher = p.matcher(html);
		while (matcher.find()) {
			pattern = matcher.group(1);
			System.out.println(pattern);
			return pattern;
		}

		return null;

	}

	public String GetHiddenParamKey(String html) {
		// pattern to find hidden Tea
		Pattern p = Pattern
				.compile("<input id=\"key\" name=\"key\" type=\"hidden\" value=\"(.*)\" />");
		String pattern = "";
		Matcher matcher = p.matcher(html);
		while (matcher.find()) {
			pattern = matcher.group(1);
			System.out.println(pattern);
			return pattern;
		}

		return null;

	}

	public String GetHiddenParamtzDiff(String html) {
		// pattern to find hidden Tea
		Pattern p = Pattern
				.compile("<input name=\"tzDiff\" type=\"hidden\" value=\"(.*)\" />");
		String pattern = "";
		Matcher matcher = p.matcher(html);
		while (matcher.find()) {
			pattern = matcher.group(1);
			System.out.println(pattern);
			return pattern;
		}
		return null;
	}

	public String GetHiddenParamSV(String html) {
		// pattern to find hidden Tea
		Pattern p = Pattern
				.compile("<input name=\"sv\" type=\"hidden\" value=\"(.*)\" />");
		String pattern = "";
		Matcher matcher = p.matcher(html);
		while (matcher.find()) {
			pattern = matcher.group(1);
			System.out.println(pattern);
			return pattern;
		}
		return null;
	}

	public String GetCaptchaImageURL(String html) {
		// pattern to find captcha image url
		Pattern p = Pattern.compile(this.captchaRegularExpression);
		String image_url = "";
		Matcher matcher = p.matcher(html);
		while (matcher.find()) {
			image_url = matcher.group();
			// System.out.println(image_url);
			return image_url;
		}

		return null;
	}

	public List<BasicNameValuePair> getLoginPostData(String html_data) {
		// TODO Auto-generated method stub
		List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
		
		
		return null;
	}
	
	public InputStream basicPost(List<BasicNameValuePair> nameValuePairs, String post_url){
		InputStream inputStream = null;
		HttpPost httppost = new HttpPost(post_url);

		httppost.getParams()
				.setParameter(CoreProtocolPNames.USER_AGENT,
						"Mozilla/5.0 (Windows NT 6.1; rv:5.0) Gecko/20100101 Firefox/5.0");
		httppost.getParams().setParameter(
				CoreProtocolPNames.HTTP_CONTENT_CHARSET, "utf-8");

		httppost.setHeader("Referer", this.domain + this.initURL);
		httppost.setHeader("Accept-Language", "en-us,en;q=0.5");
		httppost.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httppost.setHeader("Accept-Encoding", "gzip, deflate");
		httppost.setHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block

			System.out.println("Cannot encode data to http post");
			e.printStackTrace();
			return null;
		}
		HttpResponse response;
		try {
			response = httpclient.execute(httppost);
			Header h = response.getFirstHeader("Content-Encoding");
			HttpEntity entity = response.getEntity();			
			if (entity != null) {
				if (h != null)
					if (h.getValue().equals("gzip")
							|| h.getValue().equals("deflate")) {
						GzipDecompressingEntity gzip_entity = new GzipDecompressingEntity(
								entity);
						inputStream = gzip_entity.getContent();

					} else {
						inputStream = entity.getContent();
					}
				else {
					inputStream = entity.getContent();
				}
			}			
			return inputStream;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return inputStream;
	}
	
	public InputStream basicPost(String post_content, String post_url){
		InputStream inputStream = null;
		HttpPost httppost = new HttpPost(post_url);

		httppost.getParams()
				.setParameter(CoreProtocolPNames.USER_AGENT,
						"Mozilla/5.0 (Windows NT 6.1; rv:5.0) Gecko/20100101 Firefox/5.0");
		httppost.getParams().setParameter(
				CoreProtocolPNames.HTTP_CONTENT_CHARSET, "utf-8");

		httppost.setHeader("Referer", this.domain + this.initURL);
		httppost.setHeader("Accept-Language", "en-us,en;q=0.5");
		httppost.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httppost.setHeader("Accept-Encoding", "gzip, deflate");
		httppost.setHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		
		try {
			StringEntity se = new StringEntity(post_content);
			httppost.setEntity(se);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block

			System.out.println("Cannot encode data to http post");
			e.printStackTrace();
			return null;
		}
		HttpResponse response;
		try {
			response = httpclient.execute(httppost);
			Header h = response.getFirstHeader("Content-Encoding");
			HttpEntity entity = response.getEntity();			
			if (entity != null) {
				if (h != null)
					if (h.getValue().equals("gzip")
							|| h.getValue().equals("deflate")) {
						GzipDecompressingEntity gzip_entity = new GzipDecompressingEntity(
								entity);
						inputStream = gzip_entity.getContent();

					} else {
						inputStream = entity.getContent();
					}
				else {
					inputStream = entity.getContent();
				}
			}			
			return inputStream;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return inputStream;
	}

	public InputStream ProcessLogin(String username, String password,
			String captcha) {

		HttpPost httppost = new HttpPost(this.domain + this.loginURL);

		httppost.getParams()
				.setParameter(CoreProtocolPNames.USER_AGENT,
						"Mozilla/5.0 (Windows NT 6.1; rv:5.0) Gecko/20100101 Firefox/5.0");
		httppost.getParams().setParameter(
				CoreProtocolPNames.HTTP_CONTENT_CHARSET, "utf-8");

		httppost.setHeader("Referer", this.domain + this.initURL);
		httppost.setHeader("Accept-Language", "en-us,en;q=0.5");
		httppost.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httppost.setHeader("Accept-Encoding", "gzip, deflate");
		httppost.setHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");

		// Add your data
		List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", username));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		nameValuePairs.add(new BasicNameValuePair("code", captcha));
		nameValuePairs.add(new BasicNameValuePair("page", "default"));
		nameValuePairs.add(new BasicNameValuePair("lang", "en"));
		nameValuePairs.add(new BasicNameValuePair("key", this
				.GetHiddenParamKey(this.htmldata)));
		nameValuePairs.add(new BasicNameValuePair("tea", this
				.GetHiddenParamTea(this.htmldata)));
		nameValuePairs.add(new BasicNameValuePair("tzDiff", this
				.GetHiddenParamtzDiff(this.htmldata)));
		nameValuePairs.add(new BasicNameValuePair("sv", this
				.GetHiddenParamSV(this.htmldata)));

		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block

			System.out.println("Cannot encode data to http post");
			e.printStackTrace();
			return null;
		}

		HttpResponse response;
		try {
			response = httpclient.execute(httppost);
			Header h = response.getFirstHeader("Content-Encoding");
			HttpEntity entity = response.getEntity();
			InputStream inputStream = null;
			if (entity != null) {
				if (h != null)
					if (h.getValue().equals("gzip")
							|| h.getValue().equals("deflate")) {
						GzipDecompressingEntity gzip_entity = new GzipDecompressingEntity(
								entity);
						inputStream = gzip_entity.getContent();

					} else {
						inputStream = entity.getContent();
					}
				else {
					inputStream = entity.getContent();
				}
			}
			System.out.println("Successfully login");
			return inputStream;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
