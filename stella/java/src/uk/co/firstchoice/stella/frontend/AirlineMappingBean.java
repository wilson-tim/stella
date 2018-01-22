/*
 * AirlineMappingBean.java
 *
 * Created on 24 April 2003, 13:15
 */

package uk.co.firstchoice.stella.frontend;

import java.util.TreeMap;
import java.util.TreeSet;

/**
 * 
 * @author Lsvensson
 */
public class AirlineMappingBean {

	private TreeMap usersByAirline = null; //Map key = Airline, Map data =

	// SortedSet of users

	private TreeMap airlinesByUser = null; //Map key = User, Map data =

	// SortedSet of airlines

	/** Creates a new instance of AirlineMappingBean */
	public AirlineMappingBean() {
		usersByAirline = new TreeMap();
		airlinesByUser = new TreeMap();
	}

	public void addEntry(String user, String airline) {
		TreeSet data;

		if (airlinesByUser.containsKey(user))
			data = (TreeSet) airlinesByUser.get(user);
		else {
			data = new TreeSet();
			airlinesByUser.put(user, data);
		}
		if (!data.contains(airline))
			data.add(airline);

		if (usersByAirline.containsKey(airline))
			data = (TreeSet) usersByAirline.get(airline);
		else {
			data = new TreeSet();
			usersByAirline.put(airline, data);
		}
		if (!data.contains(user))
			data.add(user);

	}

	/**
	 * Getter for property airlinesByUser.
	 * 
	 * @return Value of property airlinesByUser.
	 *  
	 */
	public java.util.TreeMap getAirlinesByUser() {
		return airlinesByUser;
	}

	/**
	 * Getter for property usersByAirline.
	 * 
	 * @return Value of property usersByAirline.
	 *  
	 */
	public java.util.TreeMap getUsersByAirline() {
		return usersByAirline;
	}

}