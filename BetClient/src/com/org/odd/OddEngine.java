package com.org.odd;

import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

public class OddEngine {
	private HashMap<String, Odd> best_odd_team1;
	private HashMap<String, Odd> best_odd_team2;
	private HashMap<String, String> best_client_team1;
	private HashMap<String, String> best_client_team2;
	private Logger logger;
	private HashMap<String, HashMap<String, Odd>> all_odd;

	public OddEngine(Logger logger) {
		this.best_odd_team1 = new HashMap<String, Odd>();
		this.best_odd_team2 = new HashMap<String, Odd>();
		this.best_client_team1 = new HashMap<String, String>();
		this.best_client_team2 = new HashMap<String, String>();
		this.logger = logger;
		this.all_odd = new HashMap<String, HashMap<String, Odd>>();
	}

	public void addOdd(HashMap<String, Odd> odds, String client_name) {
		// put new odd into
		this.all_odd.put(client_name, odds);
		// Start compare
		for (Odd o : odds.values()) {
			if ((o.getHome().equals("Bhutan U19 (N)".toUpperCase()) || o
					.getHome().equals("Djurgardens U21".toUpperCase()))
					&& o.getType() == OddType.HDP_FULLTIME) {
				logger.info(o);
			}
		}
	}

	public void getGoodOdd(Odd odd1, Odd odd2, String client1, String client2) {
		if (odd1.getOdd_home() * odd2.getOdd_away() < 0)
			if (odd1.getOdd_home() + odd2.getOdd_away() > 0.08) {
				logger.fatal("money team1 at \n");
				logger.fatal(odd1 + " and \n");
				logger.fatal(odd2);
				logger.fatal(client1 + ":" + client2);
			}
		if (odd2.getOdd_home() * odd1.getOdd_away() < 0)
			if (odd2.getOdd_home() + odd1.getOdd_away() > 0.08) {
				logger.fatal("money team2 at \n");
				logger.fatal(odd1 + " and \n");
				logger.fatal(odd2);
				logger.fatal(client1 + ":" + client2);
			}

		if (odd1.getOdd_home() < 0 && odd2.getOdd_away() < 0) {
			logger.fatal("Stupid money team1 at \n");
			logger.fatal(odd1 + " and \n");
			logger.fatal(odd2);
			logger.fatal(client1 + ":" + client2);
		}
		if (odd2.getOdd_home() < 0 && odd1.getOdd_away() < 0) {
			logger.fatal("Stupid money team2 at \n");
			logger.fatal(odd1 + " and \n");
			logger.fatal(odd2);
			logger.fatal(client1 + ":" + client2);
		}
	}

	public void addOdd1(Odd odd, String client_name) {
		// compare with current best odd
		String odd_id = odd.getId();
		if (best_odd_team1.containsKey(odd_id)) {
			// compare
			float current_best = best_odd_team1.get(odd_id).getOdd_home();
			if ((current_best < 0 && odd.getOdd_home() < 0)
					|| (current_best > 0 && odd.getOdd_home() > 0)) {
				if (odd.getOdd_home() > current_best) {
					// update
					// logger.info("team1 update at : " + client_name + " : "
					// + odd);
					// logger.info("team1 old odd at : "
					// + best_client_team1.get(odd_id) + " : "
					// + best_odd_team1.get(odd_id));
					best_odd_team1.put(odd_id, odd);
					best_client_team1.put(odd_id, client_name);
					getGoodOdd(odd, best_odd_team2.get(odd_id), client_name,
							best_client_team2.get(odd_id));
				}
			} else if (current_best > 0 && odd.getOdd_home() < 0) {
				// update
				// logger.info("team1 update at : " + client_name + " : " +
				// odd);
				// logger.info("team1 old odd at : "
				// + best_client_team1.get(odd_id) + " : "
				// + best_odd_team1.get(odd_id));
				best_odd_team1.put(odd_id, odd);
				best_client_team1.put(odd_id, client_name);
				getGoodOdd(odd, best_odd_team2.get(odd_id), client_name,
						best_client_team2.get(odd_id));
			}
		} else {
			best_odd_team1.put(odd_id, odd);
			best_client_team1.put(odd_id, client_name);
		}

		// comapre with best odd of team 2
		if (best_odd_team2.containsKey(odd_id)) {
			// compare
			float current_best = best_odd_team2.get(odd_id).getOdd_away();
			if ((current_best < 0 && odd.getOdd_away() < 0)
					|| (current_best > 0 && odd.getOdd_away() > 0)) {
				if (odd.getOdd_away() > current_best) {
					// update
					// logger.info("team2 update at : " + client_name + " : "
					// + odd);
					// logger.info("team2 old odd at : "
					// + best_client_team1.get(odd_id) + " : "
					// + best_odd_team1.get(odd_id));
					best_odd_team2.put(odd_id, odd);
					best_client_team2.put(odd_id, client_name);
					getGoodOdd(odd, best_odd_team1.get(odd_id), client_name,
							best_client_team1.get(odd_id));
				}
			} else if (current_best > 0 && odd.getOdd_away() < 0) {
				// update
				// logger.info("team2 update at : " + client_name + " : " +
				// odd);
				// logger.info("team2 old odd at : "
				// + best_client_team1.get(odd_id) + " : "
				// + best_odd_team1.get(odd_id));
				best_odd_team2.put(odd_id, odd);
				best_client_team2.put(odd_id, client_name);
				getGoodOdd(odd, best_odd_team1.get(odd_id), client_name,
						best_client_team1.get(odd_id));
			}
		} else {
			best_odd_team2.put(odd_id, odd);
			best_client_team2.put(odd_id, client_name);
		}
	}
}
