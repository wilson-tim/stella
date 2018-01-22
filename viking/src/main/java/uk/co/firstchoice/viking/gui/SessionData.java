package uk.co.firstchoice.viking.gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import uk.co.firstchoice.fcutil.FCException;
import uk.co.firstchoice.fcutil.FCUtils;
import uk.co.firstchoice.viking.persist.VikingJDBC;

public class SessionData {

	// Bean Properties

	private static final String VERSION = "0.13"; // added flight type and meal type

	private static final String BUILDDATE = "@@builddate@@"; // @@ builddate @@

	// is replaced by
	// ANT with current
	// date/time prior
	// to compileing

	private String serverName = "";

	private String dwName = "";

	private Map countries;

	private Map contractTypeCodes;

	private Collection seasons; // Data: "S03;01/03/2003;28/10/2003"
	
	private Collection restrcitedSeasons; // Data: "S03;01/03/2003;28/10/2003"
	

	private Map statusCodes;

	private Map versions;

	private Map seatClasses;
	
	private Map mealTypes;

	private Collection amendmentTypes;
	
	private Map flightTypes;

	/**
	 * @throws FCException
	 */
	public SessionData() throws FCException {
		try {
			/* Data Warehouse name */
			VikingJDBC vJdbc = new VikingJDBC();
			this.dwName = vJdbc.getDataWarehouseName();

			/* Build "countries" Map of country code/names */
			ResultSet rs = vJdbc.getAllCountries();
			countries = new TreeMap();
			while (rs.next()) {
				String country = rs.getString("country_desc");
				if (country.length() > 30) {
					country = country.substring(0, 30);
				}
				countries.put(country, rs.getString("country_code"));
			}

			/* Build map of contract type/desc */
			rs = vJdbc.getAllContractCodes();
			this.contractTypeCodes = new HashMap();
			while (rs.next()) {
				contractTypeCodes.put(rs.getString("contract_type_code"), rs
						.getString("contract_type_desc"));
			}

			/* Build collection of defined seasons and boundary dates */
			this.seasons = new Vector();
			rs = vJdbc.getSeasons();
			while (rs.next()) {
				String s = rs.getString("season") + ";"
						+ FCUtils.dateToString(rs.getDate("season_start_date"))
						+ ";"
						+ FCUtils.dateToString(rs.getDate("season_end_date"))
						+ ";" + rs.getString("is_default");
				this.seasons.add(s);
			}

			/* Build collection of defined Restrcited seasons and boundary dates */
			rs = vJdbc.getRestrcitedSeasons();
			this.restrcitedSeasons = new Vector();
			while (rs.next()) {
				String s = rs.getString("season") + ";"
						+ FCUtils.dateToString(rs.getDate("season_start_date"))
						+ ";"
						+ FCUtils.dateToString(rs.getDate("season_end_date"))
						+ ";" + rs.getString("is_default");
				this.restrcitedSeasons.add(s);
			}
			
			/* Build collection of defined seat classes */
			this.seatClasses = new TreeMap(); //Vector();
			this.seatClasses.put("", "");
			rs = vJdbc.getAllSeatClasses();
			while (rs.next()) {
				this.seatClasses.put(rs.getString("seat_class"), rs
						.getString("description"));
			}

			/* Build map of meal Type codes/descriptions */
			this.mealTypes = new TreeMap(); //Vector();
			this.mealTypes.put("", "");
			rs = vJdbc.getAllMealTypes();
			while (rs.next()) {
				this.mealTypes.put(rs.getString("meal_type_code"), rs
						.getString("meal_type_desc"));
			}
			
			/* Build collection of defined amendment types */
			this.amendmentTypes = new Vector();
			this.amendmentTypes.add(" ; ");
			rs = vJdbc.getAllCommentTypes();
			while (rs.next()) {
				this.amendmentTypes.add(rs.getString("comment_type_code") + ";"
						+ rs.getString("description"));
			}

			
			/* Build map of flight Type codes/descriptions */
			this.flightTypes = new TreeMap(); //Vector();
			this.flightTypes.put("", "");
			rs = vJdbc.getAllFlightTypes();
			while (rs.next()) {
				this.flightTypes.put(rs.getString("flight_type_code"), rs
						.getString("flight_type_desc"));
			}
			
			
			/* Build map of status codes/descriptions */
			rs = vJdbc.getStatusCodes();
			this.statusCodes = new HashMap();
			while (rs.next()) {
				this.statusCodes.put(rs.getString("allocation_status_code"), rs
						.getString("allocation_status_desc"));
			}

			/* Build map of seasons with a collection of versions */
			versions = new Hashtable();
			rs = vJdbc.getVersions();
			LinkedHashSet values;
			while (rs.next()) {
				String season = rs.getString("season");
				String vers = rs.getString("version_no") + ";"
						+ rs.getString("version_desc") + ";"
						+ rs.getString("default_ind") + ";"
						+ rs.getString("live_version_ind");
				if (versions.containsKey(season)) {
					((LinkedHashSet) versions.get(season)).add(vers);
				} else {
					values = new LinkedHashSet();
					values.add(vers);
					versions.put(season, values);
				}
			}

			vJdbc.close();

		} catch (SQLException e) {
			throw new FCException("SQL Error retrieving country names", e);
		}
	}

	/**
	 * @return
	 */
	public String getDwName() {
		return this.dwName;
	}

	/**
	 * @return
	 */
	public String getServerName() {
		return this.serverName;
	}

	/**
	 * @return
	 */
	public String getVersion() {
		return VERSION;
	}

	/**
	 * @return
	 */
	public String getBuildDT() {
		return BUILDDATE;
	}

	/**
	 * @return
	 */
	public Map getCountries() {
		return this.countries;
	}

	/**
	 * @return
	 */
	public Set getCountryNames() {
		return this.countries.keySet();
	}

	/**
	 * @return
	 */
	public Collection getCountryCodes() {
		return this.countries.values();
	}

	/**
	 * @return
	 */
	public Set getContractTypeCodes() {
		return this.contractTypeCodes.keySet();
	}

	/**
	 * @return
	 */
	public Collection getContractTypeNames() {
		return this.contractTypeCodes.values();
	}

	/**
	 * @return
	 */
	public Collection getSeasons() {
		return this.seasons;
	}

	/**
	 * @return
	 */
	public Collection getRestrictedSeasons() {
		return this.restrcitedSeasons;
	}
	/**
	 * @return
	 */
	public Set getSeatClasses() {
		return this.seatClasses.keySet();
	}

	public Map getSeatClassDescriptions() {
		return this.seatClasses;
	}

	/**
	 * @return
	 */
	public Set getMealTypes() {
		return this.mealTypes.keySet();
	}

	
	public Map getMealTypesDesc() {
		return this.mealTypes;
	}
	
	/**
	 * @return
	 */
	public Set getFlightTypes() {
		return this.flightTypes.keySet();
	}

	
	public Map getFlightTypesDesc() {
		return this.flightTypes;
	}
	

	/**
	 * @return
	 */
	public Collection getAmendmentTypes() {
		return this.amendmentTypes;
	}

	/**
	 * @return
	 */
	public Set getStatusCodes() {
		return this.statusCodes.keySet();
	}

	/**
	 * @return
	 */
	public Map getVersions() {
		return this.versions;
	}

}