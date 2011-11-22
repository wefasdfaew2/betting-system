package com.org.player;

public class UserInfo {
	private boolean is_locked;
	private boolean is_suspended;
	private int max_bet;
	private int min_bet;
	private long credit;

	public UserInfo(boolean is_locked, boolean is_suspended, int max_bet,
			int min_bet, long credit) {
		super();
		this.is_locked = is_locked;
		this.is_suspended = is_suspended;
		this.max_bet = max_bet;
		this.min_bet = min_bet;
		this.credit = credit;
	}

	public boolean isIs_locked() {
		return is_locked;
	}

	public void setIs_locked(boolean is_locked) {
		this.is_locked = is_locked;
	}

	public boolean isIs_suspended() {
		return is_suspended;
	}

	public void setIs_suspended(boolean is_suspended) {
		this.is_suspended = is_suspended;
	}

	public int getMax_bet() {
		return max_bet;
	}

	public void setMax_bet(int max_bet) {
		this.max_bet = max_bet;
	}

	public int getMin_bet() {
		return min_bet;
	}

	public void setMin_bet(int min_bet) {
		this.min_bet = min_bet;
	}

	public long getCredit() {
		return credit;
	}

	public void setCredit(long credit) {
		this.credit = credit;
	}
}
