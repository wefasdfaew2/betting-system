package com.org.odd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

public class Odd implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String home;
	private String away;
	private String handicap;
	private float odd_home;
	private float odd_away;
	private OddType type;
	private String odd_home_xpath;
	private String odd_away_xpath;

	public String getAway() {
		return away;
	}

	public void setAway(String away) {
		this.away = away;
	}

	public static boolean compareValue(Odd o1, Odd o2) {
		return o1.odd_home == o2.odd_home && o1.odd_away == o2.odd_away;
	}

	public String getOdd_home_xpath() {
		return odd_home_xpath;
	}

	public void setOdd_home_xpath(String odd_home_xpath) {
		this.odd_home_xpath = odd_home_xpath;
	}

	public String getOdd_away_xpath() {
		return odd_away_xpath;
	}

	public void setOdd_away_xpath(String odd_away_xpath) {
		this.odd_away_xpath = odd_away_xpath;
	}

	public Odd(String home, String away, String handicap, float odd_home,
			float odd_away, OddType type) {
		super();
		this.home = home;
		this.away = away;
		this.handicap = handicap;
		this.odd_home = odd_home;
		this.odd_away = odd_away;
		this.type = type;
	}

	public float getOdd_home() {
		return odd_home;
	}

	public void setOdd_home(float odd_home) {
		this.odd_home = odd_home;
	}

	public float getOdd_away() {
		return odd_away;
	}

	public void setOdd_away(float odd_away) {
		this.odd_away = odd_away;
	}

	public String getId() {
		return home + " vs " + away + ":" + handicap + ":" + type;
	}

	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Odd other = (Odd) obj;
		return this.getId().equals(other.getId());
	}

	@Override
	public String toString() {
		return home + " vs " + away + ":" + handicap + " , " + odd_home + " , "
				+ odd_away + ", type=" + type + ", xpath = " + odd_home_xpath
				+ ":" + odd_away_xpath;
	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public String getHandicap() {
		return handicap;
	}

	public void setHandicap(String handicap) {
		this.handicap = handicap;
	}

	public OddType getType() {
		return type;
	}

	public void setType(OddType type) {
		this.type = type;
	}

}
