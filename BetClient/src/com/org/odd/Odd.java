package com.org.odd;

public class Odd {
	private String home;
	private String away;
	private int handicap;
	private float odd_home;
	private float odd_away;

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public String getAway() {
		return away;
	}

	public void setAway(String away) {
		this.away = away;
	}

	public int getHandicap() {
		return handicap;
	}

	public void setHandicap(int handicap) {
		this.handicap = handicap;
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

	public Odd(String home, String away, int handicap, float odd_home,
			float odd_away) {
		super();
		this.home = home;
		this.away = away;
		this.handicap = handicap;
		this.odd_home = odd_home;
		this.odd_away = odd_away;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((away == null) ? 0 : away.hashCode());
		result = prime * result + handicap;
		result = prime * result + ((home == null) ? 0 : home.hashCode());
		return result;
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
		if (away == null) {
			if (other.away != null)
				return false;
		} else if (!away.equals(other.away))
			return false;
		if (handicap != other.handicap)
			return false;
		if (home == null) {
			if (other.home != null)
				return false;
		} else if (!home.equals(other.home))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return home + ", " + away + ":" + handicap + ", " + odd_home + ", "
				+ odd_away;
	}

}
