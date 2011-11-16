package com.org.odd;

public class TeamHeader {
	private String team1;
	private String team2;
	private boolean nagative_handicap;

	public TeamHeader(String team1, String team2, boolean nagative_handicap) {
		super();
		this.team1 = team1;
		this.team2 = team2;
		this.nagative_handicap = nagative_handicap;
	}

	public String getTeam1() {
		return team1;
	}

	public void setTeam1(String team1) {
		this.team1 = team1;
	}

	public String getTeam2() {
		return team2;
	}

	public void setTeam2(String team2) {
		this.team2 = team2;
	}

	public boolean isNagative_handicap() {
		return nagative_handicap;
	}

	public void setNagative_handicap(boolean nagative_handicap) {
		this.nagative_handicap = nagative_handicap;
	}

}
