/*
 * AccountsContainer.java
 *
 * Created on 17 September 2003, 10:51
 */

package uk.co.firstchoice.viking.gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import uk.co.firstchoice.fcutil.FCException;
import uk.co.firstchoice.viking.persist.VikingJDBC;

/**
 * 
 * @author Lars Svensson
 */
public class AccountsContainer {

	private SortedMap accountNames;

	private SortedSet accountCodes;

	//	private Map flightCodes; // a map of vectors containing the flight codes
	// for this supplier
	//	private String currentAccountCode;

	/** Creates a new instance of AccountsContainer */
	public AccountsContainer() {
		accountNames = new TreeMap();
		accountCodes = new TreeSet();
	}

	/**
	 * Creates and populates a new instance of AccountsContainer
	 * 
	 * @param vJdbc
	 * @throws FCException
	 */
	public AccountsContainer(VikingJDBC vJdbc) throws FCException {
		this();
		try {
			ResultSet rs = vJdbc.getAllAccountNames();
			//			boolean cac = false;
			while (rs.next()) {
				this.add(rs.getString("customer_code").trim(), rs
						.getString("customer_name"));

			}
			vJdbc.closeResultSet(rs);
		} catch (SQLException e) {
			throw new FCException("Error building list of Account names/codes",
					e);
		}
	}

	/**
	 * @param code
	 * @param name
	 */
	public void add(String code, String name /*
											  * , String fc1, String fc2, String
											  * fc3, String fc4
											  */) {
		accountCodes.add(code);
		accountNames.put(name, code);

	}

	/**
	 * @return
	 */
	public Set getAccountNames() {
		return accountNames.keySet();
	}

	/**
	 * @return
	 */
	public Collection getAccountCodes() {
		return accountNames.values();
	}

	/**
	 * @return
	 */
	public SortedSet getSortedAccountCodes() {
		return accountCodes;
	}

	/**
	 * @return
	 */
	public int size() {
		return accountCodes.size();
	}

}