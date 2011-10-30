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
	public String toString() {
		return home + " vs " + away + ":" + handicap + " , " + odd_home + " , "
				+ odd_away + ", type=" + type;
	}

	
}
