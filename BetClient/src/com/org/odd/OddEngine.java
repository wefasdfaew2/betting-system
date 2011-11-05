package com.org.odd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.jms.JMSException;

import org.apache.log4j.Logger;

import com.org.messagequeue.TopicPublisher;

public class OddEngine {
	private Logger logger;
	private HashMap<String, HashMap<String, Odd>> all_odd;
	private HashMap<String, Long> time_stamp;
	TopicPublisher sbo;
	TopicPublisher three_in_one;
	private int played = 1;

	public int isPlayed() {
		return played;
	}

	public void setPlayed(int played) {
		this.played = played;
	}

	public OddEngine(Logger logger) {
		this.logger = logger;
		this.all_odd = new HashMap<String, HashMap<String, Odd>>();
		this.time_stamp = new HashMap<String, Long>();
		try {
			this.sbo = new TopicPublisher("Maj3259005");
			this.three_in_one = new TopicPublisher("lvmml7006002");
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public float diff(float a, float b) {
		float a1 = 0;
		float b1 = 0;
		if (a < 0)
			a1 = 2 + a;
		else
			a1 = a;
		if (b < 0)
			b1 = 2 + b;
		else
			b1 = b;
		return Math.abs(a1 - b1);
	}

	public void addOdd(HashMap<String, Odd> odds, String client_name)
			throws JMSException {
		// put new odd into
		// remove wrong odd from 3in bet
		List<String> wrong_odd = new ArrayList<String>();
		if (this.all_odd.containsKey(client_name)) {
			HashMap<String, Odd> old_odds = this.all_odd.get(client_name);
			for (Entry<String, Odd> e : odds.entrySet()) {
				// check if old odd are so differ
				if (old_odds.containsKey(e.getKey())) {
					Odd old_o = old_odds.get(e.getKey());
					Odd new_o = e.getValue();
					if (diff(old_o.getOdd_home(), new_o.getOdd_home())
							+ diff(old_o.getOdd_away(), new_o.getOdd_away()) > 0.5) {
						wrong_odd.add(e.getKey());
						// logger.error("wrong odd update at :" + client_name);
						// logger.error("old" + old_o);
						// logger.error("new" + new_o);
					}
				}
			}
			for (String key : wrong_odd) {
				odds.remove(key);
			}
		}
		this.all_odd.put(client_name, odds);
		long time_get = System.currentTimeMillis();
		this.time_stamp.put(client_name, time_get);
		// Start compare
		for (Odd o : odds.values()) {
			// compare to all other table
			for (Entry<String, HashMap<String, Odd>> e : this.all_odd
					.entrySet()) {
				if (e.getKey().equals(client_name))
					continue; // not compare to itself
				else {
					HashMap<String, Odd> table = e.getValue();
					// compare to same odd at another table
					if (table.containsKey(o.getId())) {
						if (played > 0
								&& (time_get - time_stamp.get(e.getKey())) < 1000)
							this.getGoodOdd(o, table.get(o.getId()),
									client_name, e.getKey());
					}
				}

			}
			// this for monitor and debug only, remove if complete
			// if ((o.getHome().equals("FSV Frankfurt Am".toUpperCase()))
			// && o.getType() == OddType.HDP_FULLTIME) {
			// logger.info(o + ": " + client_name);
			// }
		}
	}

	public void placeBet(Odd odd, String client, TeamType t)
			throws JMSException {
		if (t == TeamType.HOME) {
			if (client.equals("3in")) {
				this.three_in_one.sendOddMessage(odd, TeamType.HOME);
			} else if (client.equals("sbobet")) {
				this.sbo.sendOddMessage(odd, TeamType.HOME);
			}
		}
		if (t == TeamType.AWAY) {
			if (client.equals("3in")) {
				this.three_in_one.sendOddMessage(odd, TeamType.AWAY);
			} else if (client.equals("sbobet")) {
				this.sbo.sendOddMessage(odd, TeamType.AWAY);
			}
		}
	}

	public void getGoodOdd(Odd odd1, Odd odd2, String client1, String client2)
			throws JMSException {
		if (odd1.getOdd_home() * odd2.getOdd_away() < 0)
			if (odd1.getOdd_home() + odd2.getOdd_away() > 0.05) {
				logger.fatal("money team1 at \n");
				logger.fatal(odd1 + " and \n");
				logger.fatal(odd2);
				logger.fatal(client1 + ":" + client2);

				placeBet(odd1, client1, TeamType.HOME);
				placeBet(odd2, client2, TeamType.AWAY);
				played--;

			}
		if (odd2.getOdd_home() * odd1.getOdd_away() < 0)
			if (odd2.getOdd_home() + odd1.getOdd_away() > 0.05) {
				logger.fatal("money team2 at \n");
				logger.fatal(odd1 + " and \n");
				logger.fatal(odd2);
				logger.fatal(client1 + ":" + client2);

				placeBet(odd2, client2, TeamType.HOME);
				placeBet(odd1, client1, TeamType.AWAY);
				played--;
			}

		if (odd1.getOdd_home() < 0 && odd2.getOdd_away() < 0) {
			logger.fatal("Stupid money team1 at \n");
			logger.fatal(odd1 + " and \n");
			logger.fatal(odd2);
			logger.fatal(client1 + ":" + client2);

			placeBet(odd1, client1, TeamType.HOME);
			placeBet(odd2, client2, TeamType.AWAY);
			played--;
		}
		if (odd2.getOdd_home() < 0 && odd1.getOdd_away() < 0) {
			logger.fatal("Stupid money team2 at \n");
			logger.fatal(odd1 + " and \n");
			logger.fatal(odd2);
			logger.fatal(client1 + ":" + client2);

			placeBet(odd2, client2, TeamType.HOME);
			placeBet(odd1, client1, TeamType.AWAY);
			played--;
		}
	}

}
