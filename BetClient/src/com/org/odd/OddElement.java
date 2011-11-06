package com.org.odd;

import com.gargoylesoftware.htmlunit.html.HtmlElement;

public class OddElement {
	private Odd odd;
	private HtmlElement home;
	private HtmlElement away;
	public OddElement(Odd odd, HtmlElement home, HtmlElement away) {
		super();
		this.odd = odd;
		this.home = home;
		this.away = away;
	}
	public Odd getOdd() {
		return odd;
	}
	public void setOdd(Odd odd) {
		this.odd = odd;
	}
	public HtmlElement getHome() {
		return home;
	}
	public void setHome(HtmlElement home) {
		this.home = home;
	}
	public HtmlElement getAway() {
		return away;
	}
	public void setAway(HtmlElement away) {
		this.away = away;
	}
	

}
