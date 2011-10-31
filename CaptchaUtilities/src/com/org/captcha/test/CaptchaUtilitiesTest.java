package com.org.captcha.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.org.captcha.CaptchaUtilities;
import com.org.captcha.Site;

public class CaptchaUtilitiesTest {

	@Test
	public void testGetNumberStringSite() {
		CaptchaUtilities util = new CaptchaUtilities();
		System.out.println((util.GetNumber(
				"G:\\Users\\Thai Son\\Desktop\\X6.jpg", Site.THREEINONE)));
	}

	@Test
	public void testGetNumberBufferedImageSite() {
		// fail("Not yet implemented");
	}

}
