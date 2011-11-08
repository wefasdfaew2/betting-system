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

	public HashMap<String, OddElement> getOddsFromSobet(HtmlTable odd_table) {
		HashMap<String, OddElement> result = new HashMap<String, OddElement>();
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

					OddElement new_element = new OddElement(odd,
							row.getCell(5), row.getCell(6));
					result.put(odd.getId(), new_element);
				}
			} catch (Exception e) {
				// TODO: handle exception
				// e.printStackTrace();
			}
			try {
				handicap = convertHandicap(row.getCell(7).asText().trim());
				if (!handicap.isEmpty()) {
					handicap = convertHandicap(handicap);
					float odd1 = Float.parseFloat(row.getCell(8).asText());
					float odd2 = Float.parseFloat(row.getCell(9).asText());
					Odd odd = new Odd(team1, team2, handicap, odd1, odd2,
							OddType.OU_FULLTIME);

					OddElement new_element = new OddElement(odd,
							row.getCell(8), row.getCell(9));
					result.put(odd.getId(), new_element);
				}
			} catch (Exception e) {
				// TODO: handle exception
				// e.printStackTrace();
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

					OddElement new_element = new OddElement(odd,
							row.getCell(11), row.getCell(12));
					result.put(odd.getId(), new_element);
				}
			} catch (Exception e) {
				// TODO: handle exception
				// e.printStackTrace();
			}
			try {
				handicap = row.getCell(13).asText().trim();
				if (!handicap.isEmpty()) {
					handicap = convertHandicap(handicap);
					float odd1 = Float.parseFloat(row.getCell(14).asText());
					float odd2 = Float.parseFloat(row.getCell(15).asText());
					Odd odd = new Odd(team1, team2, handicap, odd1, odd2,
							OddType.OU_HALFTIME);

					OddElement new_element = new OddElement(odd,
							row.getCell(14), row.getCell(15));
					result.put(odd.getId(), new_element);
				}
			} catch (Exception e) {
				// TODO: handle exception
				// e.printStackTrace();
			}
		}

		return result;
	}

	private String get_match_Id(HtmlElement e) {
		String[] ids = e.getAttribute("id").split("_");
		return ids[ids.length - 1];
	}

	public HashMap<String, OddElement> getOddsFromThreeInOne(HtmlTable odd_table) {
		HashMap<String, OddElement> result = new HashMap<String, OddElement>();
		HtmlTableBody body;
		try {
			body = odd_table.getBodies().get(0);
		} catch (Exception e) {
			// TODO: handle exception
			return result;
		}
		for (HtmlTableRow row : body.getRows()) {
			// only process row with first cell contain two span (contain odd)
			HtmlTableCell cell = null;
			boolean negative_handicap = false;
			try {
				cell = row.getCell(0);
			} catch (Exception e) {
				// TODO: handle exception
				// e.printStackTrace();
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
								&& e.hasAttribute("id")
								&& (!e.hasAttribute("style"))) {
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
					// TODO: handle exception
					// e.printStackTrace();
					continue;
				}
				team1 = team1.trim().toUpperCase();
				team2 = team2.trim().toUpperCase();
				if (team1.equals("") || team2.equals(""))
					continue;
				try {
					HtmlElement cell2 = row.getCell(2);
					HtmlElement cell3 = row.getCell(3);
					HtmlElement cell4 = row.getCell(4);
					handicap = convertHandicap(cell2.asText());
					// get handicap id (to avoid mismatch handicap matching)
					String handicap_id = get_match_Id(cell2);

					if (!handicap.equals("")) {
						if (negative_handicap)
							handicap = "-" + handicap;
						String odd_string1 = cell3.asText();
						String odd_string2 = cell4.asText();
						String odd_id1 = get_match_Id(cell3);
						String odd_id2 = get_match_Id(cell4);
						// only add if match
						if (handicap_id.equals(odd_id1)
								&& handicap_id.equals(odd_id2))
							if ((!odd_string1.equals(""))
									&& (!odd_string2.equals(""))) {

								float odd1 = Float.parseFloat(odd_string1);
								float odd2 = Float.parseFloat(odd_string2);
								Odd odd = new Odd(team1, team2, handicap, odd1,
										odd2, OddType.HDP_FULLTIME);
								odd.setOdd_home_xpath(((HtmlElement) cell3
										.getFirstChild()).getAttribute("href"));
								odd.setOdd_away_xpath(((HtmlElement) cell4
										.getFirstChild()).getAttribute("href"));
								OddElement new_element = new OddElement(odd,
										cell3, cell4);
								result.put(odd.getId(), new_element);
							}
					}
				} catch (Exception e) {
					// TODO: handle exception
					// e.printStackTrace();
				}
				try {
					HtmlElement cell5 = row.getCell(5);
					HtmlElement cell6 = row.getCell(6);
					HtmlElement cell7 = row.getCell(7);
					handicap = convertHandicap(cell5.asText());
					// get handicap id (to avoid mismatch handicap matching)
					String handicap_id = get_match_Id(cell5);

					if (!handicap.equals("")) {
						String odd_string1 = cell6.asText();
						String odd_string2 = cell7.asText();
						String odd_id1 = get_match_Id(cell6);
						String odd_id2 = get_match_Id(cell7);
						// only add if match
						if (handicap_id.equals(odd_id1)
								&& handicap_id.equals(odd_id2))
							if ((!odd_string1.equals(""))
									&& (!odd_string2.equals(""))) {
								float odd1 = Float.parseFloat(odd_string1);
								float odd2 = Float.parseFloat(odd_string2);
								Odd odd = new Odd(team1, team2, handicap, odd1,
										odd2, OddType.OU_FULLTIME);

								odd.setOdd_home_xpath(((HtmlElement) cell6
										.getFirstChild()).getAttribute("href"));
								odd.setOdd_away_xpath(((HtmlElement) cell7
										.getFirstChild()).getAttribute("href"));

								OddElement new_element = new OddElement(odd,
										cell6, cell7);
								result.put(odd.getId(), new_element);
							}
					}
				} catch (Exception e) {
					// TODO: handle exception
					// e.printStackTrace();
				}
				try {
					HtmlElement cell8 = row.getCell(8);
					HtmlElement cell9 = row.getCell(9);
					HtmlElement cell10 = row.getCell(10);
					handicap = convertHandicap(cell8.asText());
					// get handicap id (to avoid mismatch handicap matching)
					String handicap_id = get_match_Id(cell8);
					if (!handicap.equals("")) {
						if (negative_handicap)
							handicap = "-" + handicap;
						String odd_string1 = cell9.asText();
						String odd_string2 = cell10.asText();
						String odd_id1 = get_match_Id(cell9);
						String odd_id2 = get_match_Id(cell10);
						// only add if match
						if (handicap_id.equals(odd_id1)
								&& handicap_id.equals(odd_id2))
							if ((!odd_string1.equals(""))
									&& (!odd_string2.equals(""))) {
								float odd1 = Float.parseFloat(odd_string1);
								float odd2 = Float.parseFloat(odd_string2);
								Odd odd = new Odd(team1, team2, handicap, odd1,
										odd2, OddType.HDP_HALFTIME);

								odd.setOdd_home_xpath(((HtmlElement) cell9
										.getFirstChild()).getAttribute("href"));
								odd.setOdd_away_xpath(((HtmlElement) cell10
										.getFirstChild()).getAttribute("href"));

								OddElement new_element = new OddElement(odd,
										cell9, cell10);
								result.put(odd.getId(), new_element);
							}
					}
				} catch (Exception e) {
					// TODO: handle exception
					// e.printStackTrace();
				}
				try {
					HtmlElement cell11 = row.getCell(11);
					HtmlElement cell12 = row.getCell(12);
					HtmlElement cell13 = row.getCell(13);
					handicap = convertHandicap(cell11.asText());
					// get handicap id (to avoid mismatch handicap matching)
					String handicap_id = get_match_Id(cell11);
					if (!handicap.equals("")) {
						String odd_string1 = cell12.asText();
						String odd_string2 = row.getCell(13).asText();
						String odd_id1 = get_match_Id(cell12);
						String odd_id2 = get_match_Id(cell13);
						// only add if match
						if (handicap_id.equals(odd_id1)
								&& handicap_id.equals(odd_id2))
							if ((!odd_string1.equals(""))
									&& (!odd_string2.equals(""))) {
								float odd1 = Float.parseFloat(odd_string1);
								float odd2 = Float.parseFloat(odd_string2);
								Odd odd = new Odd(team1, team2, handicap, odd1,
										odd2, OddType.OU_HALFTIME);

								odd.setOdd_home_xpath(((HtmlElement) cell12
										.getFirstChild()).getAttribute("href"));
								odd.setOdd_away_xpath(((HtmlElement) cell13
										.getFirstChild()).getAttribute("href"));

								OddElement new_element = new OddElement(odd,
										cell12, cell13);
								result.put(odd.getId(), new_element);
							}
					}
				} catch (Exception e) {
					// TODO: handle exception
					// e.printStackTrace();
				}

			}

			// System.out.println(result);

		}

		return result;
	}
}
