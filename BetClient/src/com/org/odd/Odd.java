package com.org.odd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

public class Odd implements Serializable {
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

	public String getId() {
		return home + " vs " + away + ":" + handicap + ":" + type;
	}

	@Override
	public String toString() {
		return home + " vs " + away + ":" + handicap + " , " + odd_home + " , "
				+ odd_away + ", type=" + type;
	}

	public static List<Odd> getOddsFromSobet(HtmlTable odd_table) {
		List<Odd> result = new ArrayList<Odd>();
		for (HtmlTableBody body : odd_table.getBodies()) {
			HtmlTableRow row = body.getRows().get(0);
			HtmlTableCell first_cell = row.getCell(0);
			if (first_cell.getColumnSpan() > 10
					|| first_cell.getTextContent().equals("TIME")) {
				continue;
			}
			String team = row.getCell(1).asText();
			String team1 = team.split("\n")[0].trim();
			String team2 = team.split("\n")[1].trim();
			String handicap = row.getCell(4).asText().trim();
			if (!handicap.equals("")) {
				float odd1 = Float.parseFloat(row.getCell(5).asText());
				float odd2 = Float.parseFloat(row.getCell(6).asText());
				Odd odd = new Odd(team1, team2, handicap, odd1, odd2,
						OddType.HDP_FULLTIME);
				result.add(odd);
			}
			handicap = row.getCell(7).asText().trim();
			if (!handicap.equals("")) {
				float odd1 = Float.parseFloat(row.getCell(8).asText());
				float odd2 = Float.parseFloat(row.getCell(9).asText());
				Odd odd = new Odd(team1, team2, handicap, odd1, odd2,
						OddType.OU_FULLTIME);
				result.add(odd);
			}
			handicap = row.getCell(10).asText().trim();
			if (!handicap.equals("")) {
				float odd1 = Float.parseFloat(row.getCell(11).asText());
				float odd2 = Float.parseFloat(row.getCell(12).asText());
				Odd odd = new Odd(team1, team2, handicap, odd1, odd2,
						OddType.HDP_HALFTIME);
				result.add(odd);
			}
			handicap = row.getCell(13).asText().trim();
			if (!handicap.equals("")) {
				float odd1 = Float.parseFloat(row.getCell(14).asText());
				float odd2 = Float.parseFloat(row.getCell(15).asText());
				Odd odd = new Odd(team1, team2, handicap, odd1, odd2,
						OddType.OU_HALFTIME);
				result.add(odd);
			}
			
		}

		return result;
	}

	public static List<Odd> getOddsFromThreeInOne(HtmlTable odd_table) {
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
				String handicap = row.getCell(2).asText();
				if (!handicap.equals("")) {
					float odd1 = Float.parseFloat(row.getCell(3).asText());
					float odd2 = Float.parseFloat(row.getCell(4).asText());
					Odd odd = new Odd(team1, team2, handicap, odd1, odd2,
							OddType.HDP_FULLTIME);
					result.add(odd);
				}
				handicap = row.getCell(5).asText();
				if (!handicap.equals("")) {
					float odd1 = Float.parseFloat(row.getCell(6).asText());
					float odd2 = Float.parseFloat(row.getCell(7).asText());
					Odd odd = new Odd(team1, team2, handicap, odd1, odd2,
							OddType.OU_FULLTIME);
					result.add(odd);
				}
				handicap = row.getCell(8).asText();
				if (!handicap.equals("")) {
					float odd1 = Float.parseFloat(row.getCell(9).asText());
					float odd2 = Float.parseFloat(row.getCell(10).asText());
					Odd odd = new Odd(team1, team2, handicap, odd1, odd2,
							OddType.HDP_HALFTIME);
					result.add(odd);
				}
				handicap = row.getCell(11).asText();
				if (!handicap.equals("")) {
					float odd1 = Float.parseFloat(row.getCell(12).asText());
					float odd2 = Float.parseFloat(row.getCell(13).asText());
					Odd odd = new Odd(team1, team2, handicap, odd1, odd2,
							OddType.OU_HALFTIME);
					result.add(odd);
				}

			}

			// System.out.println(result);

		}

		return result;
	}
}
