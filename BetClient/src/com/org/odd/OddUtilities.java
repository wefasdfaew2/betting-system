package com.org.odd;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

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

	public List<Odd> getOddsFromSobet(HtmlTable odd_table) {
		List<Odd> result = new ArrayList<Odd>();
		for (HtmlTableBody body : odd_table.getBodies()) {
			HtmlTableRow row = null;
			HtmlTableCell first_cell = null;
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
			String team = "";
			String team1 = "";
			String team2 = "";
			try {
				team = row.getCell(1).asText();
				team1 = team.split("\n")[0].trim();
				team2 = team.split("\n")[1].trim();
			} catch (Exception e) {
				// TODO: handle exception
				continue;
			}
			String handicap = "";
			try {
				handicap = row.getCell(4).asText().trim();
				if (!handicap.isEmpty()) {
					handicap = convertHandicap(handicap);
					float odd1 = Float.parseFloat(row.getCell(5).asText());
					float odd2 = Float.parseFloat(row.getCell(6).asText());
					Odd odd = new Odd(team1, team2, handicap, odd1, odd2,
							OddType.HDP_FULLTIME);
					result.add(odd);
				}
			} catch (Exception e) {

			}
			try {
				handicap = convertHandicap(row.getCell(7).asText().trim());
				if (!handicap.isEmpty()) {
					handicap = convertHandicap(handicap);
					float odd1 = Float.parseFloat(row.getCell(8).asText());
					float odd2 = Float.parseFloat(row.getCell(9).asText());
					Odd odd = new Odd(team1, team2, handicap, odd1, odd2,
							OddType.OU_FULLTIME);
					result.add(odd);
				}
			} catch (Exception e) {

			}
			try {
				handicap = row.getCell(10).asText().trim();
				if (!handicap.isEmpty()) {
					handicap = convertHandicap(handicap);
					float odd1 = Float.parseFloat(row.getCell(11).asText());
					float odd2 = Float.parseFloat(row.getCell(12).asText());
					Odd odd = new Odd(team1, team2, handicap, odd1, odd2,
							OddType.HDP_HALFTIME);
					result.add(odd);
				}
			} catch (Exception e) {

			}
			try {
				handicap = row.getCell(13).asText().trim();
				if (!handicap.isEmpty()) {
					handicap = convertHandicap(handicap);
					float odd1 = Float.parseFloat(row.getCell(14).asText());
					float odd2 = Float.parseFloat(row.getCell(15).asText());
					Odd odd = new Odd(team1, team2, handicap, odd1, odd2,
							OddType.OU_HALFTIME);
					result.add(odd);
				}
			} catch (Exception e) {

			}
		}

		return result;
	}

	public List<Odd> getOddsFromThreeInOne(HtmlTable odd_table) {
		List<Odd> result = new ArrayList<Odd>();
		HtmlTableBody body = odd_table.getBodies().get(0);
		for (HtmlTableRow row : body.getRows()) {
			// only process row with first cell contain two span (contain odd)

			HtmlTableCell cell = row.getCell(0);

			if (cell.getClass()
					.getName()
					.equals("com.gargoylesoftware.htmlunit.html.HtmlTableDataCell")
					&& cell.getColumnSpan() == 1 && cell.getRowSpan() == 1) {
				String team = row.getCell(1).asText();
				String team1 = team.split("\n")[0].trim();
				String team2 = team.split("\n")[1].trim();
				String handicap = convertHandicap(row.getCell(2).asText());
				if (!handicap.equals("")) {
					String odd_string = row.getCell(3).asText();
					if (!odd_string.equals("")) {

						float odd1 = Float.parseFloat(odd_string);
						float odd2 = Float.parseFloat(row.getCell(4).asText());
						Odd odd = new Odd(team1, team2, handicap, odd1, odd2,
								OddType.HDP_FULLTIME);
						result.add(odd);
					}
				}
				handicap = convertHandicap(row.getCell(5).asText());
				if (!handicap.equals("")) {
					String odd_string = row.getCell(6).asText();
					if (!odd_string.equals("")) {
						float odd1 = Float.parseFloat(odd_string);
						float odd2 = Float.parseFloat(row.getCell(7).asText());
						Odd odd = new Odd(team1, team2, handicap, odd1, odd2,
								OddType.OU_FULLTIME);
						result.add(odd);
					}
				}
				handicap = convertHandicap(row.getCell(8).asText());
				if (!handicap.equals("")) {
					String odd_string = row.getCell(9).asText();
					if (!odd_string.equals("")) {
						float odd1 = Float.parseFloat(odd_string);
						float odd2 = Float.parseFloat(row.getCell(10).asText());
						Odd odd = new Odd(team1, team2, handicap, odd1, odd2,
								OddType.HDP_HALFTIME);
						result.add(odd);
					}
				}
				handicap = convertHandicap(row.getCell(11).asText());
				if (!handicap.equals("")) {
					String odd_string = row.getCell(12).asText();
					if (!odd_string.equals("")) {
						float odd1 = Float.parseFloat(odd_string);
						float odd2 = Float.parseFloat(row.getCell(13).asText());
						Odd odd = new Odd(team1, team2, handicap, odd1, odd2,
								OddType.OU_HALFTIME);
						result.add(odd);
					}
				}

			}

			// System.out.println(result);

		}

		return result;
	}
}
