package com.org.odd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

public class OddUtilities {
	private String convertHandicap(String handicap) {
		String result = "";
		try {
			// System.out.println(handicap);
			if (handicap.contains("-")) {
				String[] h = handicap.split("-");
				float h1 = Float.parseFloat(h[0]);
				float h2 = Float.parseFloat(h[1]);
				return "" + (h1 + h2) / 2;
			} else if (handicap.contains("/")) {
				String[] h = handicap.split("/");
				float h1 = Float.parseFloat(h[0]);
				float h2 = Float.parseFloat(h[1]);
				return "" + (h1 + h2) / 2.0f;
			} else {
				float h = Float.parseFloat(handicap);
				return "" + h;
			}
			// System.out.println(result);

		} catch (Exception e) {
			// TODO: handle exception
			// System.out.println("handicap error :" + handicap);
		}
		return result;
	}

	public HashMap<String, Odd> getOddsFromSobet(HtmlTable odd_table) {
		HashMap<String, Odd> result = new HashMap<String, Odd>();
		for (HtmlTableBody body : odd_table.getBodies()) {
			HtmlTableRow row = null;
			HtmlTableCell first_cell = null;
			boolean negative_handicap = false;
			try {
				row = body.getRows().get(0);
				first_cell = row.getCell(0);
			} catch (Exception e) {
				continue;
			}
			if (first_cell.getColumnSpan() > 10
					|| first_cell.getTextContent().equals("TIME")) {
				continue;
			}
			String team1 = "";
			String team2 = "";
			try {
				boolean home = true;
				for (HtmlElement e : row.getCell(1).getChildElements()) {
					if (e instanceof HtmlSpan) {
						if (home) {
							team1 = e.asText();
							home = false;
						} else {
							team2 = e.asText();
							// if team 2 is handicap
							if (e.getAttribute("class").equals("Red")) {
								negative_handicap = true;
							}
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				continue;
			}
			if (team1.equals("") || team2.equals(""))
				continue;
			team1 = team1.trim().toUpperCase();
			team2 = team2.trim().toUpperCase();
			String handicap = "";
			try {
				handicap = row.getCell(4).asText().trim();
				if (!handicap.isEmpty()) {
					handicap = convertHandicap(handicap);
					if (negative_handicap)
						handicap = "-" + handicap;
					float odd1 = Float.parseFloat(row.getCell(5).asText());
					float odd2 = Float.parseFloat(row.getCell(6).asText());
					Odd odd = new Odd(team1, team2, handicap, odd1, odd2,
							OddType.HDP_FULLTIME);
					odd.setOdd_home_xpath(row.getCell(5).getCanonicalXPath());
					odd.setOdd_away_xpath(row.getCell(6).getCanonicalXPath());
					result.put(odd.getId(), odd);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				handicap = convertHandicap(row.getCell(7).asText().trim());
				if (!handicap.isEmpty()) {
					handicap = convertHandicap(handicap);
					float odd1 = Float.parseFloat(row.getCell(8).asText());
					float odd2 = Float.parseFloat(row.getCell(9).asText());
					Odd odd = new Odd(team1, team2, handicap, odd1, odd2,
							OddType.OU_FULLTIME);
					odd.setOdd_home_xpath(row.getCell(8).getCanonicalXPath());
					odd.setOdd_away_xpath(row.getCell(9).getCanonicalXPath());
					result.put(odd.getId(), odd);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				handicap = row.getCell(10).asText().trim();
				if (!handicap.isEmpty()) {
					handicap = convertHandicap(handicap);
					if (negative_handicap)
						handicap = "-" + handicap;
					float odd1 = Float.parseFloat(row.getCell(11).asText());
					float odd2 = Float.parseFloat(row.getCell(12).asText());
					Odd odd = new Odd(team1, team2, handicap, odd1, odd2,
							OddType.HDP_HALFTIME);
					odd.setOdd_home_xpath(row.getCell(11).getCanonicalXPath());
					odd.setOdd_away_xpath(row.getCell(12).getCanonicalXPath());
					result.put(odd.getId(), odd);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				handicap = row.getCell(13).asText().trim();
				if (!handicap.isEmpty()) {
					handicap = convertHandicap(handicap);
					float odd1 = Float.parseFloat(row.getCell(14).asText());
					float odd2 = Float.parseFloat(row.getCell(15).asText());
					Odd odd = new Odd(team1, team2, handicap, odd1, odd2,
							OddType.OU_HALFTIME);
					odd.setOdd_home_xpath(row.getCell(14).getCanonicalXPath());
					odd.setOdd_away_xpath(row.getCell(15).getCanonicalXPath());
					result.put(odd.getId(), odd);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public HashMap<String, Odd> getOddsFromThreeInOne(HtmlTable odd_table) {
		HashMap<String, Odd> result = new HashMap<String, Odd>();
		HtmlTableBody body;
		try {
			body = odd_table.getBodies().get(0);
		} catch (Exception e) {
			return result;
		}
		for (HtmlTableRow row : body.getRows()) {
			// only process row with first cell contain two span (contain odd)
			HtmlTableCell cell = null;
			boolean negative_handicap = false;
			try {
				cell = row.getCell(0);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			if (cell.getClass()
					.getName()
					.equals("com.gargoylesoftware.htmlunit.html.HtmlTableDataCell")
					&& cell.getColumnSpan() == 1 && cell.getRowSpan() == 1) {

				String team1 = "";
				String team2 = "";
				String handicap;
				try {
					boolean home = true;
					for (HtmlElement e : row.getCell(1).getChildElements()) {
						if ((e instanceof HtmlSpan) && !e.asText().equals("")
								&& e.hasAttribute("id") && (!e.hasAttribute("style"))) {
							if (home) {
								team1 = e.asText();
								home = false;
							} else {
								team2 = e.asText();
								// if team 2 is handicap
								if (e.getAttribute("class").equals("red")) {
									negative_handicap = true;
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				team1 = team1.trim().toUpperCase();
				team2 = team2.trim().toUpperCase();
				if (team1.equals("") || team2.equals(""))
					continue;
				try {
					handicap = convertHandicap(row.getCell(2).asText());
					if (!handicap.equals("")) {
						if (negative_handicap)
							handicap = "-" + handicap;
						String odd_string1 = row.getCell(3).asText();
						String odd_string2 = row.getCell(4).asText();
						if ((!odd_string1.equals(""))
								&& (!odd_string2.equals(""))) {

							float odd1 = Float.parseFloat(odd_string1);
							float odd2 = Float.parseFloat(odd_string2);
							Odd odd = new Odd(team1, team2, handicap, odd1,
									odd2, OddType.HDP_FULLTIME);
							odd.setOdd_home_xpath(row.getCell(3)
									.getCanonicalXPath());
							odd.setOdd_away_xpath(row.getCell(4)
									.getCanonicalXPath());
							result.put(odd.getId(), odd);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					handicap = convertHandicap(row.getCell(5).asText());
					if (!handicap.equals("")) {
						String odd_string1 = row.getCell(6).asText();
						String odd_string2 = row.getCell(7).asText();
						if ((!odd_string1.equals(""))
								&& (!odd_string2.equals(""))) {
							float odd1 = Float.parseFloat(odd_string1);
							float odd2 = Float.parseFloat(odd_string2);
							Odd odd = new Odd(team1, team2, handicap, odd1,
									odd2, OddType.OU_FULLTIME);
							odd.setOdd_home_xpath(row.getCell(6)
									.getCanonicalXPath());
							odd.setOdd_away_xpath(row.getCell(7)
									.getCanonicalXPath());
							result.put(odd.getId(), odd);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					handicap = convertHandicap(row.getCell(8).asText());
					if (!handicap.equals("")) {
						if (negative_handicap)
							handicap = "-" + handicap;
						String odd_string1 = row.getCell(9).asText();
						String odd_string2 = row.getCell(10).asText();
						if ((!odd_string1.equals(""))
								&& (!odd_string2.equals(""))) {
							float odd1 = Float.parseFloat(odd_string1);
							float odd2 = Float.parseFloat(odd_string2);
							Odd odd = new Odd(team1, team2, handicap, odd1,
									odd2, OddType.HDP_HALFTIME);
							odd.setOdd_home_xpath(row.getCell(9)
									.getCanonicalXPath());
							odd.setOdd_away_xpath(row.getCell(10)
									.getCanonicalXPath());
							result.put(odd.getId(), odd);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					handicap = convertHandicap(row.getCell(11).asText());
					if (!handicap.equals("")) {
						String odd_string1 = row.getCell(12).asText();
						String odd_string2 = row.getCell(13).asText();
						if ((!odd_string1.equals(""))
								&& (!odd_string2.equals(""))) {
							float odd1 = Float.parseFloat(odd_string1);
							float odd2 = Float.parseFloat(odd_string2);
							Odd odd = new Odd(team1, team2, handicap, odd1,
									odd2, OddType.OU_HALFTIME);
							odd.setOdd_home_xpath(row.getCell(12)
									.getCanonicalXPath());
							odd.setOdd_away_xpath(row.getCell(13)
									.getCanonicalXPath());
							result.put(odd.getId(), odd);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			// System.out.println(result);

		}

		return result;
	}
}
