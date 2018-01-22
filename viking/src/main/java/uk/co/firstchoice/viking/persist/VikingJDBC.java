/*
 * VikingJDBC.java
 *
 * Created on 13 August 2003, 13:36
 */

package uk.co.firstchoice.viking.persist;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import uk.co.firstchoice.fcutil.FCException;
import uk.co.firstchoice.fcutil.FCUtils;
import uk.co.firstchoice.fcutil.OracleComUtils;
import uk.co.firstchoice.viking.gui.CustomerBean;
import uk.co.firstchoice.viking.gui.util.ForwardConstants;
import uk.co.firstchoice.viking.pax.gui.search.PaxAllocationItemBean;
import uk.co.firstchoice.viking.pax.gui.search.PaxItemBean;
import uk.co.firstchoice.viking.pax.gui.utils.PaxUtil;

/**
 * Class to do all JDBC communication between the Viking program and the
 * database back-end.
 * 
 * @author Lars Svensson
 */
public class VikingJDBC {

	private OracleComUtils comms = null;
	
	private Connection connection = null;

	private static final String getSeasonsCSTMT = "{ ? = call viking.p_viking_getset_data.season_get()}";
	
	// to get current season plus 1 
	private static final String getRestrictedSeasonsCSTMT = "{ ? = call viking.p_viking_getset_data.restricted_season_get()}";
	
	//	private static final String getSeatClassesCSTMT =
	//	"{ ? = call viking.p_viking_getset_data.seatclasses_get()}";
	private static final String getStatusCodesCSTMT = "{ ? = call viking.p_viking_getset_data.allocation_status_code_get()}";

	private static final String getVersionsCSTMT = "{ ? = call viking.p_version_maint.version_type_get()}";

	private static final String getExchangeRateBySeasonCSTMT = "{ ? = call viking.p_viking_get_data.vk_season_rates_get(?)}";

	private static final String getExchangeRateBySeriesCSTMT = "{ ? = call viking.p_viking_get_data.vk_series_season_rates_get(?)}";

	private static final String getSeriesCSTMT = "{ ? = call viking.p_viking_getset_data.series_get(?,?,?) }";

	private static final String insertSeriesCSTMT = "{ ? = call viking.p_viking_getset_data.series_insert (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";

	private static final String deleteSeriesCSTMT = "{ ? = call viking.p_viking_getset_data.series_delete(?,?) } ";

	private static final String getSeriesDetailRangeCSTMT = "{ ? = call viking.p_viking_getset_data.series_detail_get(?,?,?) } ";

	private static final String getSeriesDetailDateCSTMT = "{ ? = call viking.p_viking_getset_data.series_detail_get(?,?) } ";

	private static final String getSeriesDetailCSTMT = "{ ? = call viking.p_viking_getset_data.series_detail_range_get(?) } ";

	private static final String getSeriesDetailDepDateCSTMT = "{ ? = call viking.p_viking_getset_data.series_detail_depdate_get(?) } ";

	private static final String insertUpdateSeriesDetailRangeCSTMT = "{ ? = call viking.p_viking_getset_data.series_detail_range_update(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }"; /* Added flight type  column for TUI */

	private static final String deleteSeriesDetailRangeCSTMT = "{ ? = call viking.p_viking_getset_data.series_detail_range_delete(?,?,?,?,?) }";

	private static final String deleteCancelSeriesDetailRangeCSTMT = "{ ? = call viking.p_viking_getset_data.series_detail_range_del(?,?,?,?,?) }";

	private static final String getCanceledSeriesCSTMT = "{ ? = call viking.p_viking_get_data.cancelled_series_get(?,?,?) }";

	private static final String getRotationDepDatesCSTMT = "{ ? = call viking.p_viking_get_data.rotation_departures_get(?,?,?,?) }";

	private static final String getAllocationAirportwiseCSTMT = "{ ? = call viking.p_viking_getset_data.series_detail_get_gateway(?)}";

	private static final String getAllocationCSTMT = "{ ? = call viking.p_viking_getset_data.allocation_get(?)}";

	private static final String getEditAllocationCSTMT = "{ ? = call viking.p_viking_getset_data.allocation_get(?,?,?,?,?,?,?,?)}";

	private static final String getAllocationAirportDetailCSTMT = "{ ? = call viking.p_viking_getset_data.allocation_airport_detail_get(?,?}";

	private static final String insertAllocationCustomerCSTMT = "{ ? = call viking.p_viking_getset_data.allocation_range_insert(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }"; // Added meal_type_code  

	private static final String updateAllocationCustomerCSTMT = "{ ? = call viking.p_viking_getset_data.allocation_range_update(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }"; // Added Meal_type_code

	private static final String deleteAllocationCSTMT = "{ ? = call viking.p_viking_getset_data.allocation_range_delete(?,?,?,?,?,?,?,?,?,?,?)}";

	private static final String getAllocationDatewiseCSTMT = "{ ? = call viking.p_viking_getset_data.allocation_datewise_get(?,?)}";

	private static final String getAllocationDateDetailCSTMT = "{ ? = call viking.p_viking_getset_data.allocation_date_detail_get(?,?)}";

	private static final String getSharersByDepDateCSTMT = "{ ? = call viking.p_viking_get_data.all_dept_alloc_get(?,?,?,?)}";

	private static final String getSearchResultCSTMT = "{ ? = call viking.search_result(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }"; // Added Flighttype and meal type in search criteria

	private static final String getAllCountriesCSTMT = // Return list of all
	// countries and country
	// codes
	"{ ? = call viking.p_viking_getset_data.country_get() }";

	private static final String getAllContractCodesCSTMT = // Return list of all
	// contract code and
	// country codes
	"{ ? = call viking.p_viking_getset_data.contract_type_code_get() }";

	private static final String getAllAccountNamesCSTMT = // Return list of all
	// current account
	// names and codes
	"{ ? = call viking.p_viking_getset_data.customer_names_all_get() }";

	private static final String getAllAirlineCSTMT = "{ ? = call viking.p_viking_getset_data.airline_get() }";

	private static final String getAllGatewayCSTMT = "{ ? = call viking.p_viking_getset_data.gateway_code_get() }";

	private static final String getAccountDetailsCSTMT = // Return account
	// details (key = code)
	"{ ? = call viking.p_viking_getset_data.customer_code_get(?) }";

	private static final String addUpdateAccountDetailsCSTMT = // Adds or
	// updates an
	// Account record
	"{ ? = call viking.p_viking_getset_data.customer_add_update(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";

	private static final String deleteAccountDetailsCSTMT = // Deletes an
	// Account record
	"{ ? = call viking.p_viking_getset_data.customer_delete(?) }";

	private static final String getAccountContractsCSTMT = // Return account
	// contracts (key =
	// code,contractID)
	"{ ? = call viking.p_viking_getset_data.contracts_get(?,?) }";

	private static final String addUpdateAccountContractCSTMT = // Adds or
	// updates an
	// Account
	// Contract
	"{ ? = call viking.p_viking_getset_data.contract_add_update(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";

	private static final String deleteAccountContractCSTMT = // Deletes an
	// Account contract
	"{ ? = call viking.p_viking_getset_data.contract_delete(?,?) }";

	private static final String getCapacityDetailsCSTMT = // Get capacity
	// details for pop-up
	"{ ? = call viking.p_viking_get_data.vk_series_alloc_totals_get(?,?,?) }";

	private static final String getAmendmentDetailsCSTMT = // Get amendment
	// details for pop-up
	"{ ? = call viking.p_viking_get_data.vk_amendment_history_get(?,?,?) }";

	private static final String getAllSeatClassesCSTMT = "{ ? = call viking.p_viking_maint.vk_seat_class_getall() }";
	
	private static final String getAllMealTypesCSTMT = "{ ? = call viking.p_viking_maint.vk_meal_type_getall() }";
	
	private static final String getAllFlightTypesCSTMT = "{ ? = call viking.p_viking_maint.vk_flight_type_getall() }";

	private static final String getAllCommentTypesCSTMT = "{ ? = call viking.p_viking_maint.vk_comment_type_getall() }";

	private static final String insertCommentCSTMT = "{ ? = call viking.p_viking_getset_data.add_comments(?,?,?,?,?,?,?) }";

	private static final String versionSetLiveCSTMT = "{ ? = call viking.p_version_maint.vk_set_live_season_version(?,?) }"; //season,version_no

	private static final String versionSetDefaultCSTMT = "{ ? = call viking.p_version_maint.vk_set_default_season_version(?,?) }"; //season,version_no

	private static final String getOperationsDetailsCSTMT = "{ ? = call viking.p_viking_ops_data.vk_ops_flight_details_get(?,?,?,?,?,?,?) }";

	private static final String updateFlightActualsCSTMT = "{ ? = call viking.p_viking_ops_data.update_flight_actuals(?,?,?,?,?,?,?) }";

	private static final String updateFlightCommentsCSTMT = "{ ? = call viking.p_viking_ops_data.update_flight_comments(?,?,?) }";

	private static final String updateFlightEstOrActualCSTMT = "{ ? = call viking.p_viking_ops_data.update_est_or_actual(?,?,?) }";
	
	private static final String getCustomerListCSTMT = "{ ? = call viking.p_viking_pax_mgmt.vk_get_customers(?) }";
		
	private static final String versionSaveCSTMT = "{ ? = call viking.p_version_maint.vk_version_type_save(?,?,?,?,?) }";

	private static final String versionCopyCSTMT = "{ ? = call viking.p_version_maint.version_copy(?,?,?,?) }";

	private static final String versionDeleteCSTMT = "{ ? = call viking.p_version_maint.version_delete(?,?) }";

	private static final String getDWNameQuery = "select * from global_name";

	/**
	 * String that represents getting data from pl/sql stored procedure pl/sql
	 * accepts the following arguments:
	 * 
	 * from_date IN DATE , tv_date IN DATE , supplier IN NUMBER , carrier IN
	 * NUMBER , charterer IN VARCHAR2
	 *  
	 */
	private static final String SEARCH_PAX = "{ ? = call viking.P_VIKING_PAX_MGMT.get_search_details(?,?,?,?) }";
	
	/**
	 * String that represents getting data from pl/sql stored procedure pl/sql
	 * accepts the following arguments:
	 * 
	 * from_date IN DATE , tv_date IN DATE , supplier IN NUMBER , carrier IN
	 * NUMBER , charterer IN VARCHAR2
	 *  
	 */
	private static final String REPORT_PAX = "{ ? = call viking.P_VIKING_PAX_MGMT.get_report_details(?,?,?,?) }";

	
	/**
	 * String that represents getting data from pl/sql stored procedure pl/sql
	 * accepts the following arguments:
	 * 
	 * supplier_id IN NUMBER , supplier_detail_id IN NUMBER , allocation_id IN
	 * NUMBER , allocation_type IN CHAR
	 *  
	 */
	private static final String GET_PAX_PER_FLIGHT = "{ ? = call viking.P_VIKING_PAX_MGMT.get_pax_details_per_flight(?,?) }";

	/**
	 * String that represents getting data from pl/sql function. The function
	 * managed creating and updating new records from within the database pl/sql
	 * accepts the following arguments:
	 * 
	 * in_supplier_id IN VK_ALLOCATION_PAX.ALLOCATION_SERIES_ID%TYPE ,
	 * in_supplier_detail_id IN
	 * VK_ALLOCATION_PAX.ALLOCATION_SERIES_DETAIL_ID%TYPE , in_allocation_id IN
	 * VK_ALLOCATION_PAX.ALLOCATION_ALLOCATION_ID%TYPE , in_allocation_type IN
	 * VK_ALLOCATION.ALLOCATION_TYPE%TYPE , in_pax_id IN
	 * VK_ALLOCATION_PAX.ALLOCATION_PAX_ID%TYPE , in_adults IN
	 * VK_ALLOCATION_PAX.NO_OF_ADULT%TYPE , in_children IN
	 * VK_ALLOCATION_PAX.NO_OF_CHILDREN%TYPE , in_infants IN
	 * VK_ALLOCATION_PAX.NO_OF_INFANTS%TYPE , in_status_type IN
	 * VK_ALLOCATION_PAX.PAX_STATUS_ID%TYPE
	 *  
	 */
	private static final String UPDATE_PAX_DETAILS = "{ ? = call viking.P_VIKING_PAX_MGMT.save_pax_details(?,?,?,?,?,?,?,?,?,?) }";

	private String caller;

	private static final boolean DEBUG = true;

	/**
	 * Constructor. Create a new ComUtil object. The "Viking" parameter is the
	 * database resource to use on the server.
	 * 
	 * @throws FCException
	 *             Thrown from ComUtils if JDBC communication couldn't be
	 *             established, and thrown on to caller.
	 */
	public VikingJDBC() throws FCException {
		if (DEBUG) {
			StackTraceElement[] ste = new Throwable().getStackTrace();
			caller = ste[1].getClassName();
			if (caller.indexOf('.') > -1) {
				caller = caller.substring(caller.lastIndexOf('.') + 1);
			}
			System.out.println("vJDBC created. Caller: " + caller + " at: "
					+ new java.util.Date());
		}
//		comms = new OracleComUtils("jdbc.viking");
		comms = new OracleComUtils("jdbc.stella");
	}

	/** Closes the ComUtil connection. */
	public void close() {
		if (comms != null) {
			comms.close();
			comms = null;
			if (DEBUG) {
				System.out.println("vJDBC ended.   Caller: " + caller + " at: "
						+ new java.util.Date());
			}
		}
	}

	/**
	 * finalize method - called when object is destroyed by the garbage
	 * collection. Calls the Close method.
	 * 
	 * @throws Throwable
	 *             Pass any exception to caller
	 */
	protected void finalize() throws Throwable {
		close();
	}

	/**
	 * Time will be stored in oracle as a float. It is thought better that the
	 * middleware convert from the proper time format HH:MM to a string that can
	 * be converted to a float HH.MM
	 * 
	 * @param properTime
	 *            Time in HH:MM format (or null)
	 * @return Time in HH.MM format (or null)
	 */
	private String timeToDecimal(String properTime) {
		return properTime == null ? null : properTime.replace(':', '.');
	}

	/**
	 * Start a transaction - group the next actions together and either commit
	 * or abort all as a group
	 * 
	 * @throws FCException
	 *             FCException passed along from CommUtils
	 */
	public void startTransaction() throws FCException {
		try {
			java.sql.Connection conn = comms.getConnection();
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			throw new FCException("SQL Error starting transaction", e);
		}
	}

	/**
	 * Commit the current transaction
	 * 
	 * @throws FCException
	 *             From CommUtils
	 */
	public void commitTransaction() throws FCException {
		try {
			java.sql.Connection conn = comms.getConnection();
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			throw new FCException("SQL Error committing transaction", e);
		}
	}

	/**
	 * Abort current transaction
	 * 
	 * @throws FCException
	 *             From CommUtils
	 */
	public void abortTransaction() throws FCException {
		try {
			java.sql.Connection conn = comms.getConnection();
			if (!conn.getAutoCommit()) {	// if autocommit is turned off we're in a transaction
				conn.rollback();
				conn.setAutoCommit(true);
			}
		} catch (SQLException e) {
			throw new FCException("SQL Error aborting transaction", e);
		}
	}

	/**
	 * Calles stored procedure to retrieve all seasons in database
	 * 
	 * @throws FCException
	 *             Passed along from ComUtil
	 * @return Returns a ResultSet of season codes and dates
	 */
	public ResultSet getSeasons() throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getSeasonsCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException("SQL Error looking up seasons", e);
		}
	}
	
	/**
	 * Calles stored procedure to retrieve current season plus one in database
	 * 
	 * @throws FCException
	 *             Passed along from ComUtil
	 * @return Returns a ResultSet of season codes and dates
	 */
	public ResultSet getRestrcitedSeasons() throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getRestrictedSeasonsCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException("SQL Error looking up Restricted seasons", e);
		}
	}	
	

	/**
	 * Calles stored procedure to retrieve all gateway code
	 * 
	 * @throws FCException
	 *             Passed along from ComUtil
	 * @return Returns a ResultSet of status codes and colour codes to display
	 */
	public ResultSet getAllGatewayCodes() throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getAllGatewayCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException("SQL Error looking up gateway code", e);
		}
	}
	/**
	 * Calls stored procedure to retrieve all gateway code
	 * 
	 * @throws FCException
	 *             Passed along from ComUtil
	 * @return Returns a List of carrier codes and identifiers
	 */
	public List getCarrierList() throws FCException {
		final String customerType = "S"; 

		return getCustomerList(customerType);
	}

	
	/**
	 * Calls stored procedure to retrieve all gateway code
	 * 
	 * @throws FCException
	 *             Passed along from ComUtil
	 * @return Returns a List of carrier codes and identifiers
	 */
	public List getFlightTypeList() throws FCException {
		List flightTypeList = new ArrayList();
	
		final CustomerBean customerBean1 = new CustomerBean();
		customerBean1.setCustomerCode("");
		customerBean1.setCustomerName("");
		
		flightTypeList.add(customerBean1);

		final CustomerBean customerBean2 = new CustomerBean();
		customerBean2.setCustomerCode("FCA");
		customerBean2.setCustomerName("FCA");
		
		flightTypeList.add(customerBean2);

		final CustomerBean customerBean3 = new CustomerBean();
		customerBean3.setCustomerCode("3rd Party");
		customerBean3.setCustomerName("3rd Party");
		
		flightTypeList.add(customerBean3);
		return flightTypeList;
	}
		
	
	
	/**
	 * 
	 * @param customerType
	 * @return
	 * @throws FCException
	 */
	protected List getCustomerList(String customerType) throws FCException {
		try {
			
			CallableStatement cstmt = comms
			.getCallableStatement(getCustomerListCSTMT);

			cstmt.registerOutParameter(1,
					oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setString(2, customerType);
			
			cstmt.execute();
			ResultSet resultSet = (ResultSet) cstmt.getObject(1);
			List customerList = new ArrayList();
			while (resultSet.next()) {
				final CustomerBean customerBean = new CustomerBean();
				customerBean.setCustomerCode(resultSet.getString("customer_code"));
				customerBean.setCustomerName(resultSet.getString("customer_name"));
				customerList.add(customerBean);
			}			
			return customerList;
		} catch (SQLException e) {
			throw new FCException("SQL Error looking up customer", e);
		}
		
	}
	
	/**
	 * Calles stored procedure to retrieve all airline(customer flightcodes) in
	 * database
	 * 
	 * @throws FCException
	 *             Passed along from ComUtil
	 * @return Returns a ResultSet of status codes and colour codes to display
	 */
	public ResultSet getAllAirlineCodes() throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getAllAirlineCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException("SQL Error looking up status codes", e);
		}
	}

	/**
	 * Calles stored procedure to retrieve all inventory status code from
	 * database
	 * 
	 * @throws FCException
	 *             Passed along from ComUtil
	 * @return Returns a ResultSet of status and desc
	 */
	public ResultSet getStatusCodes() throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getStatusCodesCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException("SQL Error looking up status code", e);
		}
	}

	/**
	 * Calles stored procedure to retrieve all versions in database
	 * 
	 * @throws FCException
	 *             Passed along from ComUtil
	 * @return Returns a ResultSet of season codes and dates
	 */
	public ResultSet getVersions() throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getVersionsCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException("SQL Error looking up versions", e);
		}
	}

	/**
	 * Lookup exchange rates for a given season
	 * 
	 * @param season
	 *            The key
	 * @throws FCException
	 *             FCException passed along
	 * @return Resultset of all exchange rates for this season
	 */
	public ResultSet getExchangeRatesBySeason(String season) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getExchangeRateBySeasonCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setString(2, season);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException(
					"SQL Error looking up exchange rates for season " + season,
					e);
		}
	}

	/**
	 * Lookup exchange rates for a given series (will use the seriesID to find
	 * the season and then lookup exchange rates for that season)
	 * 
	 * @param seriesNo
	 *            Key
	 * @throws FCException
	 *             FCException
	 * @return Resultset of exchange rates
	 */
	public ResultSet getExchangeRatesBySeries(long seriesNo) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getExchangeRateBySeriesCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setLong(2, seriesNo);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException(
					"SQL Error looking up exchange rates for series "
							+ seriesNo, e);
		}
	}

	/**
	 * Calles stored procedure to retrieve the main series header with
	 * information about season,
	 * 
	 * @return Resultset
	 * @param seriesNo
	 *            Key
	 * @throws FCException
	 *             FCException
	 */
	public ResultSet getSeries(long seriesNo) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getSeriesCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setLong(2, seriesNo);
			cstmt.setNull(3, java.sql.Types.DATE);
			cstmt.setNull(4, java.sql.Types.DATE);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			System.out.println("Error" + e);
			throw new FCException(
					"SQL Error looking up Series id: " + seriesNo, e);

		}
	}

	/**
	 * @return
	 * @param sector1
	 * @param sector2
	 * @param sector3
	 * @param sector4
	 * @param sector5
	 * @param sector6
	 * @param slot
	 * @param initialCapacity
	 * @param amendmentType
	 * @param version_no
	 * @param startDate
	 * @param endDate
	 * @param dayOfWeek
	 * @param frequencyCode
	 * @param comments
	 * @param userID
	 * @throws FCException
	 */

	public String insertSeries(

	int version_no,
	//String season,
			String startDate, String endDate, String dayOfWeek,
			String frequencyCode, String sector1, String sector2,
			String sector3, String sector4, String sector5, String sector6,
			String slot, String initialCapacity, String amendmentType,
			String comments, String userID) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(insertSeriesCSTMT);
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setInt(2, version_no);
			cstmt.setString(3, startDate);
			cstmt.setString(4, endDate);
			cstmt.setString(5, dayOfWeek);
			cstmt.setString(6, frequencyCode);
			cstmt.setString(7, sector1);
			cstmt.setString(8, sector2);
			cstmt.setString(9, sector3);
			cstmt.setString(10, sector4);
			cstmt.setString(11, sector5);
			cstmt.setString(12, sector6);
			cstmt.setString(13, amendmentType);
			cstmt.setString(14, comments);
			cstmt.setString(15, userID);
			cstmt.setString(16, slot);
			try {
				cstmt.setInt(17, Integer.parseInt(initialCapacity));
			} catch (NumberFormatException e) {
				cstmt.setNull(17, java.sql.Types.INTEGER);
			}
			cstmt.execute();
			return cstmt.getString(1);
		} catch (SQLException e) {
			try {
				abortTransaction();
			} catch (FCException ex) {
			}
			throw new FCException("SQL Error inserting Series Detail", e);
		}
	}

	/**
	 * @return
	 * @param seriesId
	 * @param userID
	 * @throws FCException
	 */
	public String deleteSeries(long seriesId, String userID) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(deleteSeriesCSTMT);
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setLong(2, seriesId);
			cstmt.setString(3, userID);
			cstmt.execute();
			return cstmt.getString(1);
		} catch (SQLException e) {
			try {
				abortTransaction();
			} catch (FCException ex) {
			}
			throw new FCException("SQL Error deleting Series :" + seriesId, e);
		}
	}

	/**
	 * @param id
	 * @param fromdate
	 * @param todate
	 * @throws FCException
	 * @return
	 */
	public ResultSet getSeriesDetail(long id, String fromdate, String todate)
			throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getSeriesDetailRangeCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setLong(2, id);
			cstmt.setString(3, fromdate);
			cstmt.setString(4, todate);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FCException("SQL Error looking up Series Detail Range", e);
		}
	}

	/**
	 * @param seriesId
	 * @param departuredate
	 * @throws FCException
	 * @return
	 */
	public ResultSet getSeriesDetail(long seriesId, String departuredate)
			throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getSeriesDetailDateCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setLong(2, seriesId);
			cstmt.setString(3, departuredate);
			cstmt.execute();

			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FCException("SQL Error looking up Series Detail Range", e);
		}
	}

	/**
	 * @param id
	 * @throws FCException
	 * @return
	 */
	public ResultSet getSeriesDetail(long id) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getSeriesDetailCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setLong(2, id);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			System.out.println(e);
			throw new FCException("SQL Error looking up Series Detail Range", e);
		}
	}

	/**
	 * @param id
	 * @throws FCException
	 * @return
	 */
	public ResultSet getSeriesDetailDepDate(long id) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getSeriesDetailDepDateCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setLong(2, id);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException(
					"SQL Error looking up Series Detail Dep Date", e);
		}
	}

	/**
	 * @return
	 * @param depdate
	 * @param oldsequenceno
	 * @param gwchanged
	 * @param sequenceno
	 * @param flightcode
	 * @param aircrafttype
	 * @param flighttype
	 * @param recordtype
	 * @param id
	 * @param fromdate
	 * @param todate
	 * @param gatewaycodefrom
	 * @param gatewaycodeto
	 * @param arrivaltime
	 * @param departuretime
	 * @param flightno
	 * @param overnightperiod
	 * @param userid
	 * @throws FCException
	 */

	public String insertUpdateSeriesDetail(long id, String fromdate,
			String todate, String depdate, int oldsequenceno, String gwchanged,
			int sequenceno, String gatewaycodefrom, String gatewaycodeto,
			String departureslot, String departuretime, String arrivaltime,
			String flightcode, String flightno, String aircrafttype,String flighttype,
			int overnightperiod,
			//	String amendmentType,
			//	String comments,
			String recordtype, String userid) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(insertUpdateSeriesDetailRangeCSTMT);
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setLong(2, id);
			cstmt.setString(3, fromdate);
			cstmt.setString(4, todate);
			cstmt.setString(5, depdate);
			cstmt.setInt(6, oldsequenceno);
			cstmt.setString(7, gwchanged);
			cstmt.setInt(8, sequenceno);
			cstmt.setString(9, gatewaycodefrom);
			cstmt.setString(10, gatewaycodeto);
			cstmt.setString(11, departureslot);
			cstmt.setString(12, timeToDecimal(departuretime));
			cstmt.setString(13, timeToDecimal(arrivaltime));
			cstmt.setString(14, flightcode);
			cstmt.setString(15, flightno);
			cstmt.setString(16, aircrafttype);
			cstmt.setString(17, flighttype);
			cstmt.setInt(18, overnightperiod);
			cstmt.setString(19, recordtype);
			cstmt.setString(20, userid);
			cstmt.execute();
			return cstmt.getString(1);
		} catch (SQLException e) {
			System.out.println(e);
			//throw new FCException("SQL Error inserting/updating Series
			// Detail", e);
			try {
				abortTransaction();
			} catch (FCException ex) {
			}
			throw new FCException("SQL Error inserting Operation Detail", e);
		}
	}

	/**
	 * @return
	 * @param fromdate
	 * @param todate
	 * @param seqno
	 * @param id
	 * @param userid
	 * @throws FCException
	 */

	public String deleteSeriesDetail(long id, String fromdate, String todate,
			int seqno, String userid
	//	String amendmentType,
	//	String comments
	) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(deleteSeriesDetailRangeCSTMT);
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setLong(2, id);
			cstmt.setString(3, fromdate);
			cstmt.setString(4, todate);
			cstmt.setInt(5, seqno);
			cstmt.setString(6, userid);
			//			cstmt.setString(7, amendmentType);
			//			cstmt.setString(8, comments);
			cstmt.execute();
			return cstmt.getString(1);
		} catch (SQLException e) {
			try {
				abortTransaction();
			} catch (FCException ex) {
			}
			throw new FCException("SQL Error delete Series Detail range ", e);
		}
	}

	/**
	 * @parm todate
	 * @parm type - Cancel/Delete
	 * @return
	 * @param todate
	 * @param rectype
	 * @param id
	 * @param fromdate
	 * @param userid
	 * @throws FCException
	 */

	public String deleteCancelSeriesDetail(long id, String fromdate,
			String todate, String rectype, String userid
	//	String amendmentType,
	//	String comments
	) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(deleteCancelSeriesDetailRangeCSTMT);
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setLong(2, id);
			cstmt.setString(3, fromdate);
			cstmt.setString(4, todate);
			cstmt.setString(5, rectype);
			cstmt.setString(6, userid);
			//			cstmt.setString(7, amendmentType);
			//			cstmt.setString(8, comments);
			cstmt.execute();
			return cstmt.getString(1);
		} catch (SQLException e) {
			try {
				abortTransaction();
			} catch (FCException ex) {
			}
			throw new FCException("SQL Error delete/cancel series Detail ", e);
		}
	}

	public ResultSet getCancelledSeries(long seriesID) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getCanceledSeriesCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setLong(2, seriesID);
			cstmt.setNull(3, java.sql.Types.DATE);
			cstmt.setNull(4, java.sql.Types.DATE);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			System.out.println(e);
			throw new FCException("SQL Error looking up cancelled series", e);
		}
	}

	public ResultSet getRotationDepDates(long seriesId, String key,
			java.sql.Date minDate, java.sql.Date maxDate) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getRotationDepDatesCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setLong(2, seriesId);
			cstmt.setString(3, key);
			cstmt.setDate(4, minDate);
			cstmt.setDate(5, maxDate);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			System.out.println(e);
			throw new FCException(
					"SQL Error looking up rotation departure dates", e);
		}
	}

	/**
	 * @param seriesID
	 * @throws FCException
	 * @return
	 */
	public ResultSet getAllocation(long seriesID) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getAllocationCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setLong(2, seriesID);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			System.out.println(e);
			throw new FCException(
					"SQL Error looking up get allocation range sales/purchase ",
					e);
		}
	}

	/**
	 * @return
	 * @param allocationtype
	 * @param customercode
	 * @param gatewaycodefrom
	 * @param gatewaycodeto 
	 * @param fromdate
	 * @param todate
	 * @param seatclass
	 * @param seriesID
	 * @throws FCException
	 */
	public ResultSet getAllocation(long seriesID, String allocationtype,
			String customercode, String gatewaycodefrom, String gatewaycodeto,
			String fromdate, String todate, String seatclass)
			throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getEditAllocationCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setLong(2, seriesID);
			cstmt.setString(3, allocationtype);
			cstmt.setString(4, customercode);
			cstmt.setString(5, gatewaycodefrom);
			cstmt.setString(6, gatewaycodeto);
			cstmt.setString(7, fromdate);
			cstmt.setString(8, todate);
			cstmt.setString(9, seatclass);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			System.out.println(e);
			throw new FCException(
					"SQL Error looking up get allocation edit range sales/purchase ",
					e);
		}
	}

	/**
	 * @return
	 * @param id
	 * @throws FCException
	 */
	public String getAllocationAirportwise(long id) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getAllocationAirportwiseCSTMT);
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setLong(2, id);
			cstmt.execute();
			return cstmt.getString(1);
		} catch (SQLException e) {
			System.out.println(e);
			throw new FCException(
					"SQL Error looking up get allocation range sales/purchase ",
					e);
		}
	}

	/**
	 * @param id
	 * @param version
	 * @throws FCException
	 * @return
	 */
	public ResultSet getAllocationAirportDetail(long id, long version)
			throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getAllocationAirportDetailCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setLong(2, id);
			cstmt.setLong(3, version);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException("SQL Error looking up Allocation datewise ",
					e);
		}
	}

	/**
	 * @return
	 * @param allocationstatuscode
	 * @param oldcurrencycode1
	 * @param oldcurrencycode2
	 * @param oldcurrencycode3
	 * @param oldcurrencycode4
	 * @param oldcurrencycode5
	 * @param record_type
	 * @param amendmentType
	 * @param id
	 * @param allocationtype
	 * @param gatewaycodefrom
	 * @param gatewaycodeto
	 * @param fromdate
	 * @param todate
	 * @param seatclass
	 * @param noofseat
	 * @param customercode
	 * @param contractid
	 * @param brokercode
	 * @param geminicode
	 * @param currencycode1
	 * @param exchangerate1
	 * @param seatprice1
	 * @param currencycode2
	 * @param exchangerate2
	 * @param seatprice2
	 * @param currencycode3
	 * @param exchangerate3
	 * @param seatprice3
	 * @param currencycode4
	 * @param exchangerate4
	 * @param seatprice4
	 * @param currencycode5
	 * @param exchangerate5
	 * @param seatprice5
	 * @throws FCException
	 */

	public String allocationRangeInsertUpdate(long id, String allocationtype,
			String gatewaycodefrom, String gatewaycodeto, String fromdate,
			String todate, String seatclass, String allocationstatuscode,
			int noofseat, String customercode, String contractid,
			String brokercode, String geminicode,String mealtype,

			String oldcurrencycode1, String currencycode1, float exchangerate1,
			float seatprice1,

			String oldcurrencycode2, String currencycode2, float exchangerate2,
			float seatprice2,

			String oldcurrencycode3, String currencycode3, float exchangerate3,
			float seatprice3,

			String oldcurrencycode4, String currencycode4, float exchangerate4,
			float seatprice4,

			String oldcurrencycode5, String currencycode5, float exchangerate5,
			float seatprice5,

			String record_type, String amendmentType
	//	String comments,
	//	String userid

	) throws FCException {
		String statementType = " delete ";
		try {
			CallableStatement cstmt = null;
			if (record_type.equals(ForwardConstants.RECORD_ADD)) {
				cstmt = comms
						.getCallableStatement(insertAllocationCustomerCSTMT);
				statementType = " insert ";
			} else if (record_type.equals(ForwardConstants.RECORD_EDIT)) {
				cstmt = comms
						.getCallableStatement(updateAllocationCustomerCSTMT);
				statementType = " update ";
			}
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setLong(2, id);
			cstmt.setString(3, allocationtype);
			cstmt.setString(4, gatewaycodefrom);
			cstmt.setString(5, gatewaycodeto);
			cstmt.setString(6, fromdate);
			cstmt.setString(7, todate);
			cstmt.setString(8, seatclass);
			cstmt.setString(9, allocationstatuscode);
			cstmt.setInt(10, noofseat);
			cstmt.setString(11, customercode);
			cstmt.setString(12, contractid);
			cstmt.setString(13, brokercode);
			cstmt.setString(14, geminicode);
			cstmt.setString(15, mealtype);
			int i = 15;
			int cnt = 0;
			if (record_type.equals(ForwardConstants.RECORD_EDIT)) {
				cnt++;
				cstmt.setString(i + cnt, oldcurrencycode1);
			}
			cstmt.setString(i + cnt + 1, currencycode1);
			cstmt.setFloat(i + cnt + 2, exchangerate1);
			cstmt.setFloat(i + cnt + 3, seatprice1);
			if (record_type.equals(ForwardConstants.RECORD_EDIT)) {
				cnt++;
				cstmt.setString(i + cnt + 3, oldcurrencycode2);
			}
			cstmt.setString(i + cnt + 4, currencycode2);
			cstmt.setFloat(i + cnt + 5, exchangerate2);
			cstmt.setFloat(i + cnt + 6, seatprice2);
			if (record_type.equals(ForwardConstants.RECORD_EDIT)) {
				cnt++;
				cstmt.setString(i + cnt + 6, oldcurrencycode3);
			}
			cstmt.setString(i + cnt + 7, currencycode3);
			cstmt.setFloat(i + cnt + 8, exchangerate3);
			cstmt.setFloat(i + cnt + 9, seatprice3);
			if (record_type.equals(ForwardConstants.RECORD_EDIT)) {
				cnt++;
				cstmt.setString(i + cnt + 9, oldcurrencycode4);
			}
			cstmt.setString(i + cnt + 10, currencycode4);
			cstmt.setFloat(i + cnt + 11, exchangerate4);
			cstmt.setFloat(i + cnt + 12, seatprice4);
			if (record_type.equals(ForwardConstants.RECORD_EDIT)) {
				cnt++;
				cstmt.setString(i + cnt + 12, oldcurrencycode5);
			}
			cstmt.setString(i + cnt + 13, currencycode5);
			cstmt.setFloat(i + cnt + 14, exchangerate5);
			cstmt.setFloat(i + cnt + 15, seatprice5);


			cstmt.setString(i + cnt + 16, amendmentType);

			cstmt.execute();
			
			return cstmt.getString(1);
		} catch (SQLException e) {
			System.out.println("SQL Error " + statementType
					+ " Allocation Detail" + e);
			try {
				abortTransaction();
			} catch (FCException ex) {
			}
			throw new FCException("SQL Error " + statementType
					+ " Allocation Detail", e);
		}
	}

	/**
	 * @param id
	 * @param version
	 * @throws FCException
	 * @return
	 */
	public ResultSet getAllocationDatewise(long id, long version)
			throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getAllocationDatewiseCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setLong(2, id);
			cstmt.setLong(3, version);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException(
					"SQL Error looking up Allocation airportwise ", e);
		}
	}

	public ResultSet getAllotmentsByDepDate(long seriesId, boolean sharers)
			throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getSharersByDepDateCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setLong(2, seriesId);
			cstmt.setString(3, sharers ? "S" : "P");
			cstmt.setNull(4, java.sql.Types.DATE);
			cstmt.setNull(5, java.sql.Types.DATE);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException(
					"SQL Error looking up Sharers By Departure Date ", e);
		}
	}

	/**
	 * @return
	 * @param gatewaycodefrom
	 * @param gatewaycodeto
	 * @param id
	 * @param allocationtype
	 * @param customercode
	 * @param fromdate
	 * @param todate
	 * @param seatclass
	 * @param delstartend
	 * @param userid
	 * @throws FCException
	 */

	public String allocationRangeDelete(long id, String allocationtype,
			String customercode, String gatewaycodefrom, String gatewaycodeto,
			String fromdate, String todate, String seatclass,
			String delstartend, String deleteRec,String userid) throws FCException {
		try {
			/*
			 * System.out.println("Deleing id: "+id);
			 * System.out.println("Deleing allocationtype: "+ allocationtype);
			 * System.out.println("Deleing customercode: "+ customercode);
			 * System.out.println("Deleing gatewaycodefrom: "+ gatewaycodefrom);
			 * System.out.println("Deleing gatewaycodeto: "+ gatewaycodeto);
			 * System.out.println("Deleing fromdate: "+ fromdate);
			 * System.out.println("Deleing todate: "+ todate);
			 * System.out.println("Deleing seatclass: "+ seatclass);
			 * System.out.println("Deleing delstartend: "+ delstartend);
			 * System.out.println("Deleing userid: "+userid);
			 */
			CallableStatement cstmt = comms
					.getCallableStatement(deleteAllocationCSTMT);
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setLong(2, id);
			cstmt.setString(3, allocationtype);
			cstmt.setString(4, customercode);
			cstmt.setString(5, gatewaycodefrom);
			cstmt.setString(6, gatewaycodeto);
			cstmt.setString(7, fromdate);
			cstmt.setString(8, todate);
			cstmt.setString(9, seatclass);
			cstmt.setString(10, delstartend);
			cstmt.setString(11, deleteRec);
			cstmt.setString(12, userid);
			//			cstmt.setString(12, amendmentType);
			//			cstmt.setString(13, comments);
			cstmt.execute();
			return cstmt.getString(1);
		} catch (SQLException e) {
			try {
				abortTransaction();
			} catch (FCException ex) {
			}
			throw new FCException("SQL Error delete allocation customerwise", e);
		}
	}

	/**
	 * @param id
	 * @param version
	 * @throws FCException
	 * @return
	 */
	public ResultSet getAllocationDateDetail(long id, long version)
			throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getAllocationDateDetailCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setLong(2, id);
			cstmt.setLong(3, version);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException(
					"SQL Error looking up Allocation airportwise ", e);
		}
	}

	/*
	 * public ResultSet getAllocationByPeriod(long id) throws FCException { try {
	 * CallableStatement cstmt =
	 * comms.getCallableStatement(getAllocationByPeriodCSTMT);
	 * cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
	 * cstmt.setLong(2, id); cstmt.setString(3, "S"); // Sales cstmt.execute();
	 * return (ResultSet)cstmt.getObject(1); } catch (SQLException e) { throw
	 * new FCException("SQL Error looking up AllocationByPeriod", e); } }
	 */

	/**
	 * @throws FCException
	 * @return
	 */
	public ResultSet getAllCountries() throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getAllCountriesCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException("SQL Error looking up AllCountries", e);
		}
	}

	/**
	 * @throws FCException
	 * @return
	 */
	public ResultSet getAllAccountNames() throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getAllAccountNamesCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException("SQL Error looking up AllAccountNames", e);
		}
	}

	/**
	 * @param code
	 * @throws FCException
	 * @return
	 */
	public ResultSet getAccountDetails(String code) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getAccountDetailsCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setString(2, code);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException("SQL Error looking up AccountDetails", e);
		}
	}

	/**
	 * @param code
	 * @throws FCException
	 * @return
	 */
	public String deleteAccountDetails(String code) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(deleteAccountDetailsCSTMT);
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setString(2, code);
			cstmt.execute();
			String error = cstmt.getString(1);
			return error == null ? "" : error;
		} catch (SQLException e) {
			throw new FCException("SQL Error deleting up AccountDetails", e);
		}
	}

	/**
	 * @return
	 * @param notes
	 * @param add
	 * @param code
	 * @param name
	 * @param contact
	 * @param address1
	 * @param address2
	 * @param address3
	 * @param address4
	 * @param address5
	 * @param postcode
	 * @param countryCode
	 * @param officePhone
	 * @param oooContact1
	 * @param oooContact2
	 * @param oooPhone1
	 * @param oooPhone2
	 * @param opsEmail
	 * @param generalEmail
	 * @param atol
	 * @param officeFax
	 * @param informDelay
	 * @param informLate
	 * @param flightCode1
	 * @param flightCode2
	 * @param flightCode3
	 * @param flightCode4
	 * @param accountHolder
	 * @param accountDeputy
	 * @param fcgroupCustomer
	 * @param userId
	 * @throws FCException
	 */
	public String addUpdateAccountDetails(boolean add, String code,
			String name, String contact, String address1, String address2,
			String address3, String address4, String address5, String postcode,
			String countryCode, String officePhone, String oooContact1,
			String oooContact2, String oooPhone1, String oooPhone2,
			String opsEmail, String generalEmail, String atol,
			String officeFax, String informDelay, String informLate,
			String flightCode1, String flightCode2, String flightCode3,
			String flightCode4, String accountHolder, String accountDeputy,
			boolean fcgroupCustomer, String notes, String userId)
			throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(addUpdateAccountDetailsCSTMT);
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setString(2, add ? "I" : "U");
			cstmt.setString(3, code);
			cstmt.setString(4, name);
			cstmt.setString(5, contact);
			cstmt.setString(6, address1);
			cstmt.setString(7, address2);
			cstmt.setString(8, address3);
			cstmt.setString(9, address4);
			cstmt.setString(10, address5);
			cstmt.setString(11, postcode);
			cstmt.setString(12, countryCode);
			cstmt.setString(13, officePhone);
			cstmt.setString(14, oooContact1);
			cstmt.setString(15, oooContact2);
			cstmt.setString(16, oooPhone1);
			cstmt.setString(17, oooPhone2);
			cstmt.setString(18, opsEmail);
			cstmt.setString(19, generalEmail);
			cstmt.setString(20, atol);
			cstmt.setString(21, officeFax);
			cstmt.setString(22, informDelay);
			cstmt.setString(23, informLate);
			cstmt.setString(24, flightCode1);
			cstmt.setString(25, flightCode2);
			cstmt.setString(26, flightCode3);
			cstmt.setString(27, flightCode4);
			cstmt.setString(28, accountHolder);
			cstmt.setString(29, accountDeputy);
			cstmt.setString(30, fcgroupCustomer ? "Y" : "N");
			cstmt.setString(31, notes);
			cstmt.setString(32, userId);
			cstmt.execute();
			return cstmt.getString(1);
		} catch (SQLException e) {
			try {
				abortTransaction();
			} catch (FCException ex) {
			}
			throw new FCException(
					"SQL Error adding or updating AccountDetails", e);
		}
	}

	/**
	 * @return
	 * @param season
	 * @param add
	 * @param contractID
	 * @param code
	 * @param contractTypeCode
	 * @param fuelFixed
	 * @param currencyFixed
	 * @param euroFixed
	 * @param apdR
	 * @param apdI
	 * @param apdPct
	 * @param osTaxesInR
	 * @param osTaxesInI
	 * @param osTaxesInPct
	 * @param osTaxesOutR
	 * @param osTaxesOutI
	 * @param osTaxesOutPct
	 * @param pilR
	 * @param pilI
	 * @param pilPct
	 * @param ukTaxesInR
	 * @param ukTaxesInI
	 * @param ukTaxesInPct
	 * @param ukTaxesOutR
	 * @param ukTaxesOutI
	 * @param ukTaxesOutPct
	 * @param paymentTerms
	 * @param userID
	 * @throws FCException
	 */
	public String addUpdateAccountContract(boolean add, String contractID,
			String code, String contractTypeCode, String fuelFixed,
			String currencyFixed, String euroFixed, String apdR, String apdI,
			String apdPct, String osTaxesInR, String osTaxesInI,
			String osTaxesInPct, String osTaxesOutR, String osTaxesOutI,
			String osTaxesOutPct, String pilR, String pilI, String pilPct,
			String season, String ukTaxesInR, String ukTaxesInI,
			String ukTaxesInPct, String ukTaxesOutR, String ukTaxesOutI,
			String ukTaxesOutPct, String paymentTerms, String userID)
			throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(addUpdateAccountContractCSTMT);
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setString(2, add ? "I" : "U");
			cstmt.setString(3, contractID);
			cstmt.setString(4, code);
			cstmt.setString(5, contractTypeCode);
			cstmt.setString(6, fuelFixed);
			cstmt.setString(7, currencyFixed);
			cstmt.setString(8, euroFixed);
			cstmt.setString(9, apdR);
			cstmt.setString(10, apdI);
			cstmt.setString(11, apdPct);
			cstmt.setString(12, osTaxesInR);
			cstmt.setString(13, osTaxesInI);
			cstmt.setString(14, osTaxesInPct);
			cstmt.setString(15, osTaxesOutR);
			cstmt.setString(16, osTaxesOutI);
			cstmt.setString(17, osTaxesOutPct);
			cstmt.setString(18, pilR);
			cstmt.setString(19, pilI);
			cstmt.setString(20, pilPct);
			cstmt.setString(21, ukTaxesInR);
			cstmt.setString(22, ukTaxesInI);
			cstmt.setString(23, ukTaxesInPct);
			cstmt.setString(24, ukTaxesOutR);
			cstmt.setString(25, ukTaxesOutI);
			cstmt.setString(26, ukTaxesOutPct);
			cstmt.setString(27, paymentTerms);
			cstmt.setString(28, season);
			cstmt.setString(29, userID);
			cstmt.execute();
			return cstmt.getString(1);
		} catch (SQLException e) {
			try {
				abortTransaction();
			} catch (FCException ex) {
			}
			throw new FCException(
					"SQL Error adding or updating AccountDetails", e);
		}
	}

	/**
	 * @param accountCode
	 * @param contractID
	 * @throws FCException
	 * @return
	 */
	public ResultSet getAccountContracts(String accountCode, String contractID)
			throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getAccountContractsCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setString(2, accountCode);
			if (contractID == null || contractID.equals("")) {
				cstmt.setNull(3, java.sql.Types.CHAR); // Get all contracts
			} else {
				cstmt.setString(3, contractID); // Get specific contract
			}
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException("SQL Error looking up AccountContracts", e);
		}
	}

	/**
	 * @param code
	 * @param contractID
	 * @throws FCException
	 * @return
	 */
	public String deleteAccountContractDetails(String code, String contractID)
			throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(deleteAccountContractCSTMT);
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setString(2, code);
			cstmt.setString(3, contractID);
			cstmt.execute();
			String error = cstmt.getString(1);
			return error == null ? "" : error;
		} catch (SQLException e) {
			try {
				abortTransaction();
			} catch (FCException ex) {
			}
			throw new FCException("SQL Error deleting up AccountContract", e);
		}
	}

	/**
	 * @throws FCException
	 * @return
	 */
	public String getDataWarehouseName() throws FCException {
		try {
			Statement stmt = comms.getStatement();
			ResultSet rset = stmt.executeQuery(getDWNameQuery);
			String dwName = "";
			if (rset.next()) {
				dwName = rset.getString(1);
			}
			dwName = (null == dwName ? "N/A" : (dwName.length() > 3 ? dwName
					.substring(0, 3) : dwName));
			return dwName;
		} catch (SQLException e) {
			throw new FCException("Error getting DW name", e);
		}
	}

	/**
	 * @throws FCException
	 * @return
	 */
	public ResultSet getAllContractCodes() throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getAllContractCodesCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException("SQL Error looking up AllContractCodes", e);
		}
	}

	/**
	 * @throws FCException
	 * @return
	 */
	public ResultSet getAllSeatClasses() throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getAllSeatClassesCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException("SQL Error looking up all seat classes", e);
		}
	}

	/**
	 * @throws FCException
	 * @return
	 */
	public ResultSet getAllMealTypes() throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getAllMealTypesCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException("SQL Error looking up all meal types", e);
		}
	}
	
	/**
	 * @throws FCException
	 * @return
	 */
	public ResultSet getAllFlightTypes() throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getAllFlightTypesCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException("SQL Error looking up all Flight types", e);
		}
	}
	
	/**
	 * @throws FCException
	 * @return
	 */
	public ResultSet getAllCommentTypes() throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getAllCommentTypesCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException("SQL Error looking up all comment types", e);
		}
	}

	/**
	 * @return
	 * @param searchType
	 * @param seriesNo
	 * @param versionNo
	 * @param flightcode
	 * @param flightno
	 * @param startdate
	 * @param enddate
	 * @param deptime
	 * @param deptimeopr
	 * @param arrtime
	 * @param departure_gateway
	 * @param arrival_gateway
	 * @param arrtimeopr
	 * @param aircrafttype
	 * @param flighttype
	 * @param weekday
	 * @param cancel
	 * @param customer
	 * @param supplier
	 * @param broker
	 * @param seatclass
	 * @param contractid
	 * @param totalprice
	 * @param totalpriceopr
	 * @parm seattype
	 * @param seatqty
	 * @param seatqtyopr
	 * @param status
	 * @throws FCException
	 */
	public ResultSet getSearchResult(String searchType, long seriesNo,
			long versionNo, String flightcode, String flightno,
			String startdate, String enddate, String depTime,
			String deptimeopr, String arrTime, String departure_gateway,
			String arrival_gateway, String arrtimeopr, String aircrafttype,
			String weekday,
			String customer, String supplier, String broker, String seatclass,
			String contractid, long totalprice, String totalpriceopr,
			String seattype, long seatqty, String seatqtyopr, String status,
			String geminiCode, boolean showQuantities,
			String flightType,String mealType ) throws FCException {
		try {

			CallableStatement cstmt = comms
					.getCallableStatement(getSearchResultCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setString(2, searchType);
			cstmt.setLong(3, seriesNo);
			cstmt.setLong(4, versionNo);
			cstmt.setString(5, flightcode);
			cstmt.setString(6, flightno);
			cstmt.setString(7, startdate);
			cstmt.setString(8, enddate);
			if (depTime == null || "0".equals(depTime.trim())) {
				cstmt.setNull(9, java.sql.Types.FLOAT);
			} else {
				cstmt.setFloat(9, Float.parseFloat(timeToDecimal(depTime)));
			}
			cstmt.setString(10, deptimeopr);
			if (arrTime == null || "0".equals(arrTime.trim())) {
				cstmt.setNull(11, java.sql.Types.FLOAT);
			} else {
				cstmt.setFloat(11, Float.parseFloat(timeToDecimal(arrTime)));
			}
			cstmt.setString(12, departure_gateway);
			cstmt.setString(13, arrival_gateway);
			cstmt.setString(14, arrtimeopr);
			cstmt.setString(15, aircrafttype);
			cstmt.setString(16, weekday);
			//			cstmt.setString(17,cancel);
			cstmt.setString(17, customer);
			cstmt.setString(18, supplier);
			cstmt.setString(19, broker);
			cstmt.setString(20, seatclass);
			cstmt.setString(21, contractid);
			cstmt.setLong(22, totalprice);
			cstmt.setString(23, totalpriceopr);
			cstmt.setString(24, seattype);
			cstmt.setLong(25, seatqty);
			cstmt.setString(26, seatqtyopr);
			cstmt.setString(27, status);
			cstmt.setString(28, geminiCode);
			cstmt.setString(29, showQuantities ? "Y" : "N");
			cstmt.setString(30, flightType);
			cstmt.setString(31, mealType);
			//            cstmt.setFetchSize(150);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException("SQL Error Searching Result", e);
		}
	}

	/**
	 * Get all the customer / purchased or available
	 * 
	 * @return Resultset
	 * @param allocationType
	 *            'S'-sales 'P'-purchases 'A'-customer
	 * @param customerName
	 *            The customer to display sales for
	 * @param seriesId
	 *            The series id
	 * @throws FCException
	 *             Thrown if any Exceptions are caught
	 */
	public ResultSet getCapacityDetails(String seriesId, char allocationType,
			String customerName) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getCapacityDetailsCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setLong(2, Long.parseLong(seriesId));
			cstmt.setString(3, "" + allocationType);
			if (allocationType == 'S'
					&& !customerName.equals(ForwardConstants.EMPTY)) {
				cstmt.setString(4, customerName);
			} else {
				cstmt.setNull(4, java.sql.Types.CHAR);
			}
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException("SQL Error looking up CapacityDetails", e);
		} catch (NumberFormatException e) {
			throw new FCException(
					"Error looking up CapacityDetails. Invalig series no. ("
							+ seriesId + ")", e);
		}
	}

	/**
	 * @param seriesId
	 * @throws FCException
	 * @return
	 */
	public ResultSet getAmendmentDetails(String seriesId) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getAmendmentDetailsCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setLong(2, Long.parseLong(seriesId));
			cstmt.setNull(3, java.sql.Types.DATE);
			cstmt.setNull(4, java.sql.Types.DATE);
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException("SQL Error looking up Amendment Details", e);
		} catch (NumberFormatException e) {
			throw new FCException(
					"Error looking up Amendment Details. Invalig series no. ("
							+ seriesId + ")", e);
		}
	}

	/**
	 * @param seriesId
	 * @param userId
	 * @param commentSource
	 * @param comment
	 * @param amendmentType
	 * @throws FCException
	 * @return
	 */
	public String insertComment(String seriesId, String userId,
			String commentSource, //
			String comment, String amendmentType) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(insertCommentCSTMT);
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setInt(2, Integer.parseInt(seriesId));
			cstmt.setString(3, userId);
			cstmt.setNull(4, java.sql.Types.CHAR);
			cstmt.setString(5, commentSource);
			cstmt.setString(6, "I");
			cstmt.setString(7, comment);
			cstmt.setString(8, amendmentType);
			cstmt.execute();
			return FCUtils.notNull(cstmt.getString(1));
		} catch (SQLException e) {
			try {
				abortTransaction();
			} catch (FCException ex) {
			}
			throw new FCException("SQL Error adding new comment", e);
		} catch (NumberFormatException e) {
			try {
				abortTransaction();
			} catch (FCException ex) {
			}
			throw new FCException("Error adding new comment", e);
		}
	}

	public String versionSetLive(String versionNumber, String season,
			String userId) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(versionSetLiveCSTMT);
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setString(2, season);
			cstmt.setInt(3, Integer.parseInt(versionNumber));
			cstmt.execute();
			return FCUtils.notNull(cstmt.getString(1));
		} catch (SQLException e) {
			throw new FCException("SQL Error setting new live version", e);
		} catch (NumberFormatException e) {
			throw new FCException("Error setting new live version", e);
		}
	}

	public String versionSetDefault(String versionNumber, String season,
			String userId) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(versionSetDefaultCSTMT);
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setString(2, season);
			cstmt.setInt(3, Integer.parseInt(versionNumber));
			cstmt.execute();
			return FCUtils.notNull(cstmt.getString(1));
		} catch (SQLException e) {
			throw new FCException("SQL Error setting new default version", e);
		} catch (NumberFormatException e) {
			throw new FCException("Error setting new default version", e);
		}
	}

	public String versionCreate(String versionName, String season, String userId)
			throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(versionSaveCSTMT);
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setNull(2, java.sql.Types.INTEGER); // No version no when
			// creating
			cstmt.setString(3, season);
			cstmt.setString(4, versionName);
			cstmt.setString(5, "A"); // Add version
			cstmt.setString(6, userId);
			cstmt.execute();
			return FCUtils.notNull(cstmt.getString(1));
		} catch (SQLException e) {
			throw new FCException("SQL Error creating new version", e);
		} catch (NumberFormatException e) {
			throw new FCException("Error creating new version", e);
		}
	}

	public String versionRename(String versionNumber, String season,
			String versionName, String userId) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(versionSaveCSTMT);
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setInt(2, Integer.parseInt(versionNumber));
			cstmt.setString(3, season);
			cstmt.setString(4, versionName);
			cstmt.setString(5, "U"); // Add version
			cstmt.setString(6, userId);
			cstmt.execute();
			return FCUtils.notNull(cstmt.getString(1));
		} catch (SQLException e) {
			throw new FCException("SQL Error renaming version", e);
		} catch (NumberFormatException e) {
			throw new FCException("Error renaming version", e);
		}
	}

	public String versionCopy(String oldVersionNumber, String newSeason,
			String newVersionName, String userId) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(versionCopyCSTMT);
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setInt(2, Integer.parseInt(oldVersionNumber));
			cstmt.setString(3, newSeason);
			cstmt.setString(4, newVersionName);
			cstmt.setString(5, userId);
			cstmt.execute();
			return FCUtils.notNull(cstmt.getString(1));
		} catch (SQLException e) {
			throw new FCException("SQL Error copying version", e);
		} catch (NumberFormatException e) {
			throw new FCException("Error copying version", e);
		}
	}

	public String versionDelete(String versionNumber, String userId)
			throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(versionDeleteCSTMT);
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setInt(2, Integer.parseInt(versionNumber));
			cstmt.setString(3, userId);
			cstmt.execute();
			return FCUtils.notNull(cstmt.getString(1));
		} catch (SQLException e) {
			throw new FCException("SQL Error deleting version", e);
		} catch (NumberFormatException e) {
			throw new FCException("Error deleting version", e);
		}
	}

	public ResultSet getOperationsDetails(String departureDateFrom,
			String departureDateTo, String departureTimeFrom,
			String excludeCarrier, boolean excludeCarrierInd,
			String gatewayCode, boolean onlyIncomplete) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(getOperationsDetailsCSTMT);
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			if (FCUtils.emptyString(departureDateFrom)) {
				cstmt.setNull(2, java.sql.Types.DATE);
			} else {
				cstmt.setDate(2, FCUtils.parseDate(departureDateFrom));
			}
			if (FCUtils.emptyString(departureDateTo)) {
				cstmt.setNull(3, java.sql.Types.DATE);
			} else {
				cstmt.setDate(3, FCUtils.parseDate(departureDateTo));
			}
			if (FCUtils.emptyString(departureTimeFrom)) {
				cstmt.setNull(4, java.sql.Types.FLOAT);
			} else {
				cstmt.setFloat(4, Float
						.parseFloat(timeToDecimal(departureTimeFrom)));
			}
			if (!(excludeCarrierInd)) {
				if (FCUtils.emptyString(excludeCarrier)) {
					cstmt.setNull(5, java.sql.Types.CHAR);
				} else {
					cstmt.setString(5, excludeCarrier);
				}
				cstmt.setNull(6, java.sql.Types.CHAR);
			} else {
				cstmt.setNull(5, java.sql.Types.CHAR);
				if (FCUtils.emptyString(excludeCarrier)) {
					cstmt.setNull(6, java.sql.Types.CHAR);
				} else {
					cstmt.setString(6, excludeCarrier);
				}
			}
			cstmt.setString(7, gatewayCode);
			cstmt.setString(8, onlyIncomplete ? "Y" : "N");
			cstmt.execute();
			return (ResultSet) cstmt.getObject(1);
		} catch (SQLException e) {
			throw new FCException("SQL Error looking up Operational Details", e);
		}
	}

	/*
	 * private static final String updateFlightActualsCSTMT = "{ ? = call
	 * viking.p_viking_ops_data.update_flight_actuals(?,?,?,?,?,?) }";
	 */
	public String operationsUpdate(String seriesId, String seriesDetailId,
			String actualDepTime, String actualArrTime, String depOvernight,
			String arrOvernight,
			String estimateOrActual) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(updateFlightActualsCSTMT);
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setInt(2, Integer.parseInt(seriesId));
			cstmt.setInt(3, Integer.parseInt(seriesDetailId));
			if (FCUtils.emptyString(actualDepTime)) {
				cstmt.setNull(4, java.sql.Types.FLOAT);
			} else {
				cstmt.setFloat(4, Float
						.parseFloat(timeToDecimal(actualDepTime)));
			}
			if (FCUtils.emptyString(actualArrTime)) {
				cstmt.setNull(5, java.sql.Types.FLOAT);
			} else {
				cstmt.setFloat(5, Float
						.parseFloat(timeToDecimal(actualArrTime)));
			}
			if (FCUtils.emptyString(depOvernight)) {
				cstmt.setNull(6, java.sql.Types.INTEGER);
			} else {
				cstmt.setInt(6, Integer.parseInt(depOvernight));
			}
			if (FCUtils.emptyString(arrOvernight)) {
				cstmt.setNull(7, java.sql.Types.INTEGER);
			} else {
				cstmt.setInt(7, Integer.parseInt(arrOvernight));
			}
			if (FCUtils.emptyString(estimateOrActual)) {
				cstmt.setString(8,"E");
			}	
			else{
				cstmt.setString(8,estimateOrActual);
			}
			cstmt.execute();
			return FCUtils.notNull(cstmt.getString(1));
		} catch (SQLException e) {
			throw new FCException("SQL Error updating flight actuals", e);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new FCException(
					"Input error. One of the entered numbers are in the wrong format",
					e);
		}
	}

	/*
	 * private static final String updateFlightCommentsCSTMT = "{ ? = call
	 * viking.p_viking_ops_data.update_flight_comments(?,?,?) }";
	 */
	public String operationsCommentsUpdate(String seriesId,
			String departureDate, String comment) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(updateFlightCommentsCSTMT);
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setInt(2, Integer.parseInt(seriesId));
			cstmt.setDate(3, FCUtils.parseDate(departureDate));
			if (FCUtils.emptyString(comment)) {
				cstmt.setNull(4, java.sql.Types.CHAR);
			} else {
				cstmt.setString(4, comment);
			}
			cstmt.execute();
			return FCUtils.notNull(cstmt.getString(1));
		} catch (SQLException e) {
			throw new FCException("SQL Error updating flight comment", e);
		}
	}

	/*
	 * private static final String updateFlightEstOrAcutalCSTMT = "{ ? = call
	 * viking.p_viking_ops_data.update_flight_est_or_actual(?,?,?) }";
	 */
	public String operationsEstimateOrActualUpdate(String seriesId
			, String seriesdetailallid, String estOrAct) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(updateFlightEstOrActualCSTMT);
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setInt(2, Integer.parseInt(seriesId));
			if (FCUtils.emptyString(seriesdetailallid)) {
				cstmt.setNull(3, java.sql.Types.CHAR);
			} else {
				cstmt.setString(3, seriesdetailallid);
			}
			if (FCUtils.emptyString(estOrAct)) {
				cstmt.setString(4, "E");
			} else {
				cstmt.setString(4, estOrAct);
			}
			cstmt.execute();
			return FCUtils.notNull(cstmt.getString(1));
		} catch (SQLException e) {
			throw new FCException("SQL Error updating flight comment", e);
		}
	}

	
	/**
	 * Method to call pl/sql stored procedure:
	 * proc_get_search_details(?,?,?,?,?)
	 * 
	 * 
	 * from_date IN DATE , tv_date IN DATE , supplier IN NUMBER , carrier IN
	 * NUMBER , charterer IN VARCHAR2
	 * 
	 * @param searchItem
	 *            PaxSearchItem
	 * @return List
	 * @throws FCException
	 *             Exception
	 */
	public List searchPax(final PaxItemBean searchItem, String flightType) throws FCException {
		List resultsList = new ArrayList();
		try {
			CallableStatement cstmt = comms.getCallableStatement(SEARCH_PAX);
			cstmt.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			PaxUtil paxUtil = new PaxUtil();
			cstmt.setString(2, paxUtil.dateToStringConversion(searchItem.getFromDate()));
			cstmt.setString(3, paxUtil.dateToStringConversion(searchItem.getToDate()));
			cstmt.setString(4, searchItem.getCarrier());

			cstmt.setString(5, flightType);
				
			cstmt.execute();

			ResultSet results = (ResultSet) cstmt.getObject(1);
			
			while (results.next()) {
				
				final PaxItemBean returnItem = new PaxItemBean();

			    returnItem.setSeriesDetailId(results.getString("SERIES_DETAIL_ID"));
				returnItem.setSeries(results.getString("SERIES_ID"));
		        returnItem.setDepartureDate(results.getDate("DEPARTURE_DATE"));
				returnItem.setFlightCode(results.getString("FLIGHT_CODE"));
				returnItem.setAllocationType(results.getString("ALLOCATION_TYPE"));
		        returnItem.setFlightNumber(results.getString("FLIGHT_NUMBER"));
		        returnItem.setDepartureTime(results.getString("DEPARTURE_TIME"));
				returnItem.setRoute(results.getString("GATEWAY_CODE_FROM") + "-" + results.getString("GATEWAY_CODE_TO"));
				
				resultsList.add(returnItem);
			}
			results.close();
			
			return resultsList; 
		} catch (SQLException e) {
			throw new FCException("SQL Error searching: ", e);
		}
	}



	/**
	 * Method to call pl/sql stored procedure:
	 * get_report_details(?,?,?,?,?)
	 * 
	 * 
	 * from_date IN DATE , tv_date IN DATE , supplier IN NUMBER , carrier IN
	 * NUMBER , charterer IN VARCHAR2
	 * 
	 * @param searchItem
	 *            PaxSearchItem
	 * @return List
	 * @throws FCException
	 *             Exception
	 */
	public List reportPax(final PaxItemBean searchItem, String flightType) throws FCException {

		try {
			
			CallableStatement cstmt = comms.getCallableStatement(REPORT_PAX);
			cstmt.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			PaxUtil paxUtil = new PaxUtil();
			cstmt.setString(2, paxUtil.dateToStringConversion(searchItem.getFromDate()));
			cstmt.setString(3, paxUtil.dateToStringConversion(searchItem.getToDate()));
			cstmt.setString(4, searchItem.getCarrier());

			cstmt.setString(5, flightType);
				
			cstmt.execute();
			List paxDetailsList = new ArrayList();
			ResultSet paxResults = (ResultSet) cstmt.getObject(1);
			
			// set up default id variables for looping
			int prevAllocId = 0;
	        int prevPaxId = 0;
	        int counter = 0 ;
			int allocCount = 0;
			boolean updateStatus = false;
	        PaxItemBean paxItem = new PaxItemBean();

			List allocItemList = new ArrayList();
			allocItemList.add(new PaxAllocationItemBean()); 
			allocItemList.add(new PaxAllocationItemBean()); 
			
	        int prevPaxStatusId = 0 ;
	        
			while (paxResults.next()) {

				if (paxResults.getString("CONTRACT_ID") != null && paxResults.getInt("NO_OF_SEAT") > 0 ) {
					prevAllocId = paxResults.getInt("ALLOCATION_PAX_ID");
			        prevPaxId = paxResults.getInt("ALLOCATION_ID");
			        counter++;
			        if ( counter > 0 ) {
			        	break;
			        }
				}
			}
			paxResults.close();
			
			
			// as this is a callable statement we cannot
			// call ResultSet.beforeFirst();
			// so we must execute the sql again :�(
			
			cstmt.execute();
			paxResults = (ResultSet) cstmt.getObject(1);
			String prevDepartureGateFrom = "";
			String prevFlightNo = "";
			while (paxResults.next()) {

				if (paxResults.getString("CONTRACT_ID") != null && paxResults.getInt("NO_OF_SEAT") > 0 ) {
					//if ( paxResults.getInt("ALLOCATION_PAX_ID") != prevAllocId ) {
					if ( (paxResults.getInt("ALLOCATION_PAX_ID") != prevAllocId ) || counter == 1) {	
						// get allocation data
						final PaxAllocationItemBean allocItem = new PaxAllocationItemBean();
						
						allocItem.setAllocationId(paxResults.getInt("ALLOCATION_ID"));
						allocItem.setPaxId(paxResults.getInt("ALLOCATION_PAX_ID"));
						allocItem.setAllocationType(paxResults.getString("ALLOCATION_TYPE"));
						allocItem.setInAdults(paxResults.getInt("NO_OF_ADULTS"));
						allocItem.setInChildren(paxResults.getInt("NO_OF_CHILDREN"));
						allocItem.setInInfants(paxResults.getInt("NO_OF_INFANTS"));
						allocItem.setPaxStatus(paxResults.getInt("PAX_STATUS_ID"));
						allocItem.setDepartureGateFrom(paxResults.getString("GATEWAY_CODE_FROM"));
						allocItem.setDepartureGateTo(paxResults.getString("GATEWAY_CODE_TO"));
						prevPaxStatusId = paxResults.getInt("PAX_STATUS_ID");
						
						
						// add to correct part of list
						
						if ( paxResults.getInt("PAX_STATUS_ID") == 2 || allocCount == 1 ) {
					
							allocItemList.set(1, allocItem);
							allocCount = 0 ;
							updateStatus = true;
							
						} else if ( paxResults.getInt("PAX_STATUS_ID") == 1 && ((PaxAllocationItemBean) allocItemList.get(1)).getDepartureGateFrom() == null) {
							
							allocItemList.set(0, allocItem);
							allocCount++;
							updateStatus = true;
							
						} else {
							
							allocItemList.set(0, allocItem);
							allocCount++;
							updateStatus = false;
							
						}
						
						// paxResults.getInt("ALLOCATION_PAX_ID") returns 0 for
						// non existent pax allocations
						if ( paxResults.getInt("ALLOCATION_PAX_ID") != 0 ) {
							prevAllocId = paxResults.getInt("ALLOCATION_PAX_ID");
						}
					}	else {
						
						// reset the allocation bean list so that 
						// we do not have duplicate items
						allocItemList = new ArrayList();
					}
					
					if ( paxResults.getInt("ALLOCATION_ID") != prevPaxId || counter == 1 ) {
					//if ( paxResults.getInt("ALLOCATION_ID") != prevPaxId ) {
						
						paxItem.setCustomerCode(paxResults.getString("CUSTOMER_CODE"));
						paxItem.setSupplierId(paxResults.getString("CUSTOMER_CODE"));
						paxItem.setAllocationId(paxResults.getInt("ALLOCATION_ID"));
						paxItem.setStatusCode(paxResults.getString("PAX_STATUS_ID"));
						paxItem.setDepartureDate(paxResults.getDate("DEPARTURE_DATE"));
						paxItem.setAllocation(paxResults.getInt("NO_OF_SEAT"));
						paxItem.setSeatClass(paxResults.getString("SEAT_CLASS"));
						paxItem.setSeries(paxResults.getString("SERIES_ID"));
						if ((paxResults.getString("FLIGHT_NUMBER") != null)) {
							//if (( paxResults.getString("FLIGHT_NUMBER").compareTo(prevFlightNo) == 0 ) && ( paxResults.getString("GATEWAY_CODE_FROM").compareTo(prevDepartureGateFrom) != 0 ) ) {
							//	paxItem.setFlightNumber(paxResults.getString("FLIGHT_NUMBER") + " ");
							//}
							//else {
						    paxItem.setFlightNumber(paxResults.getString("FLIGHT_NUMBER"));
							//}
							prevFlightNo = paxResults.getString("FLIGHT_NUMBER");
						}
						else {
						   paxItem.setFlightNumber("");
						   prevFlightNo = "";
						}
						paxItem.setAllocationType(paxResults.getString("ALLOCATION_TYPE"));
						paxItem.setFlightCode(paxResults.getString("FLIGHT_CODE"));
						
						paxItem.setRoute(paxResults.getString("GATEWAY_CODE_FROM") + "-" + paxResults.getString("GATEWAY_CODE_TO"));
						
						paxItem.setAllocationItems( allocItemList ); 
												
						prevPaxId = paxResults.getInt("ALLOCATION_ID");
						
						
						prevDepartureGateFrom = paxResults.getString("GATEWAY_CODE_FROM");
						
						if ( updateStatus == true && ((PaxAllocationItemBean) allocItemList.get(1)).getDepartureGateFrom() != null && ((PaxAllocationItemBean) allocItemList.get(1)).getDepartureGateFrom() != null ) {
							
							paxDetailsList.add(paxItem);
							paxItem = new PaxItemBean();
							allocItemList = new ArrayList(); 
							allocItemList.add(new PaxAllocationItemBean()); 
							allocItemList.add(new PaxAllocationItemBean()); 
						} else if ( updateStatus == false ) {
							
							paxDetailsList.add(paxItem);
							paxItem = new PaxItemBean();
							allocItemList = new ArrayList(); 
							allocItemList.add(new PaxAllocationItemBean()); 
							allocItemList.add(new PaxAllocationItemBean());							
						}
						allocCount= 0;
					}
				}
			}

			paxResults.close();
			return paxDetailsList;
			
		} catch (SQLException e) {
			throw new FCException("SQL Error on pax reporting: ", e);
		}
	}
	
	/**
	 * Method to call pl/sql stored procedure: func_get_pax_per_flight(?,?,?,?)
	 * 
	 * 
	 * from_date IN DATE , tv_date IN DATE , supplier IN NUMBER , carrier IN
	 * NUMBER , charterer IN VARCHAR2
	 * 
	 * @param supplierId
	 *            int
	 * @param supplierDetailId
	 *            int
	 * @param allocationId
	 *            int
	 * @param allocationType
	 *            String
	 * @return ResultSet
	 * @throws FCException
	 *             Exception
	 */
	public List getPaxDetails(final PaxItemBean inPaxItem) throws FCException {
		try {
			CallableStatement cstmt = comms
					.getCallableStatement(GET_PAX_PER_FLIGHT);

			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			
			cstmt.setString(2,inPaxItem.getSeries());
			cstmt.setString(3,inPaxItem.getSeriesDetailId());
			
			cstmt.execute();
			List paxDetailsList = new ArrayList();
			ResultSet paxResults = (ResultSet) cstmt.getObject(1);
			PaxUtil paxUtil = new PaxUtil();
			
			// set up default id variables for looping
			int prevAllocId = 0;
	        int prevPaxId = 0;
	        int counter = 0 ;
			int allocCount = 0;
			boolean updateStatus = false;
	        PaxItemBean paxItem = new PaxItemBean();

			List allocItemList = new ArrayList();
			allocItemList.add(new PaxAllocationItemBean()); 
			allocItemList.add(new PaxAllocationItemBean()); 
			
	        int prevPaxStatusId = 0 ;
	        
			while (paxResults.next()) {
				if (paxResults.getString("CONTRACT_ID") != null && paxResults.getInt("NO_OF_SEAT") > 0 ) {
					prevAllocId = paxResults.getInt("ALLOCATION_PAX_ID");
			        prevPaxId = paxResults.getInt("ALLOCATION_ID");
			        counter++;
			        if ( counter > 0 ) {
			        	break;
			        }
				}
			}
			paxResults.close();
			// as this is a callable statement we cannot
			// call ResultSet.beforeFirst();
			// so we must execute the sql again :�(
			
			cstmt.execute();
			paxResults = (ResultSet) cstmt.getObject(1);

			while (paxResults.next()) {
				if (paxResults.getString("CONTRACT_ID") != null && paxResults.getInt("NO_OF_SEAT") > 0 ) {
					if (paxResults.getInt("ALLOCATION_PAX_ID") != prevAllocId || counter == 1 ) {	
						// get allocation data
						final PaxAllocationItemBean allocItem = new PaxAllocationItemBean();
						
						allocItem.setAllocationId(paxResults.getInt("ALLOCATION_ID"));
						allocItem.setPaxId(paxResults.getInt("ALLOCATION_PAX_ID"));
						allocItem.setAllocationType(paxResults.getString("ALLOCATION_TYPE"));
						allocItem.setInAdults(paxResults.getInt("NO_OF_ADULTS"));
						allocItem.setInChildren(paxResults.getInt("NO_OF_CHILDREN"));
						allocItem.setInInfants(paxResults.getInt("NO_OF_INFANTS"));
						allocItem.setPaxStatus(paxResults.getInt("PAX_STATUS_ID"));
						allocItem.setDepartureGateFrom(paxResults.getString("GATEWAY_CODE_FROM"));
						allocItem.setDepartureGateTo(paxResults.getString("GATEWAY_CODE_TO"));
						
						
						// add to correct part of list
						if ( paxResults.getInt("PAX_STATUS_ID") == 2 || allocCount == 1 ) {
							allocItemList.set(1, allocItem);
							allocCount = 0 ;
							updateStatus = true;
						} else if ( paxResults.getInt("PAX_STATUS_ID") == 1 && ((PaxAllocationItemBean) allocItemList.get(1)).getDepartureGateFrom() == null) {
							allocItemList.set(0, allocItem);
							allocCount++;
							updateStatus = true;
						} else {
							allocItemList.set(0, allocItem);
							allocCount++;
							updateStatus = false;
						}
						
						// paxResults.getInt("ALLOCATION_PAX_ID") returns 0 for
						// non existent pax allocations
						if ( paxResults.getInt("ALLOCATION_PAX_ID") != 0 ) {
							prevAllocId = paxResults.getInt("ALLOCATION_PAX_ID");
						}
						
					}	else {
						// reset the allocation bean list so that 
						// we do not have duplicate items
						allocItemList = new ArrayList();
					}
	
					if ( paxResults.getInt("ALLOCATION_ID") != prevPaxId || counter == 1 ) {
						
						paxItem.setCustomerCode(paxResults.getString("CUSTOMER_CODE"));
						// get pax & flight data 
						paxItem.setSeriesDetailId(inPaxItem.getSeriesDetailId());
						paxItem.setSeries(inPaxItem.getSeries());
						paxItem.setSupplierId(paxResults.getString("CUSTOMER_CODE"));
						paxItem.setAllocationId(paxResults.getInt("ALLOCATION_ID"));
						paxItem.setStatusCode(paxResults.getString("PAX_STATUS_ID"));
						paxItem.setDepartureDate(paxResults.getDate("DEPARTURE_DATE"));
						paxItem.setAllocation(paxResults.getInt("NO_OF_SEAT"));
						paxItem.setSeatClass(paxResults.getString("SEAT_CLASS"));
						paxItem.setFlightNumber(paxResults.getString("FLIGHT_NUMBER"));
						paxItem.setAllocationType(paxResults.getString("ALLOCATION_TYPE"));
						
						paxItem.setFlightCode(paxResults.getString("FLIGHT_CODE"));
						paxItem.setRoute(paxResults.getString("GATEWAY_CODE_FROM") + "-" + paxResults.getString("GATEWAY_CODE_TO"));
					
						paxItem.setAllocationItems( allocItemList ); 
												
						prevPaxId = paxResults.getInt("ALLOCATION_ID");
						
						if ( updateStatus == true && ((PaxAllocationItemBean) allocItemList.get(1)).getDepartureGateFrom() != null && ((PaxAllocationItemBean) allocItemList.get(1)).getDepartureGateFrom() != null ) {
							paxDetailsList.add(paxItem);
							paxItem = new PaxItemBean();
							allocItemList = new ArrayList(); 
							allocItemList.add(new PaxAllocationItemBean()); 
							allocItemList.add(new PaxAllocationItemBean()); 
						} else if ( updateStatus == false ) {
							paxDetailsList.add(paxItem);
							paxItem = new PaxItemBean();
							allocItemList = new ArrayList(); 
							allocItemList.add(new PaxAllocationItemBean()); 
							allocItemList.add(new PaxAllocationItemBean());							
						}
					}
				}
			}
			
			return paxDetailsList;
		} catch (SQLException e) {
			throw new FCException("SQL Error retreving pax details for flight", e);
		}
	}

	/**
	 * Method to call pl/sql stored procedure: func_get_pax_per_flight(?,?,?,?)
	 * Method loops though itemBean.getAllocationItem() list and 
	 * insert/updates data accordingly
	 * 
	 * from_date IN DATE , tv_date IN DATE , supplier IN NUMBER , carrier IN
	 * NUMBER , charterer IN VARCHAR2
	 * 
	 * @param itemBean PaxItemBean
	 * @param userId String
	 * @return PaxItemBean
	 * @throws FCException Exception
	 */
	 public PaxItemBean savePaxDetails(final PaxItemBean itemBean, final String userId) throws FCException {
		try {
			CallableStatement cstmt = comms.getCallableStatement(UPDATE_PAX_DETAILS);
			boolean saveState = false;
			for ( int itemCounter = 0; itemCounter < itemBean.getAllocationItems().size(); itemCounter++) {
				final PaxAllocationItemBean allocItem = (PaxAllocationItemBean) itemBean.getAllocationItems().get(itemCounter);
				if ( allocItem != null ) { 
					cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.NUMBER);
					cstmt.setString(2, itemBean.getSeries());
					cstmt.setString(3, itemBean.getSeriesDetailId());
					cstmt.setInt(4, itemBean.getAllocationId());
					cstmt.setString(5, allocItem.getAllocationType());
					cstmt.setInt(6, allocItem.getPaxId() );
					cstmt.setInt(7, allocItem.getInAdults());
					cstmt.setInt(8, allocItem.getInChildren());
					cstmt.setInt(9, allocItem.getInInfants());
					cstmt.setInt(10, allocItem.getPaxStatus());
					cstmt.setString(11, userId);	
		
					cstmt.execute();
					
					allocItem.setPaxId(cstmt.getInt(1));

					if (allocItem.getPaxId() == 0 ) {
						// set error flag, but continue to 
						// update other records so that data is not lost
						saveState = true;
					}
				}
				itemBean.getAllocationItems().set(itemCounter, allocItem);
			}
			
			if (saveState == true) {
				// there was an error saving the data
				throw new FCException("SQL Error updating pax details: There was an error updating one or more records ");
			}
			return itemBean;

		} catch (SQLException e) {
			throw new FCException("SQL Error updating pax details ", e);
		}
	}

		/**
		 * Calles stored procedure to retrieve all customer code
		 * 
		 * @throws FCException
		 *             Passed along from ComUtil
		 * @return Returns a List of customer codes and names
		 */
		public List getSupplierList() throws FCException {
			final String customerType = "S"; 
			
			return getCustomerList(customerType);
		}
		
	 
	public void closeResultSet(ResultSet rs) throws FCException {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			throw new FCException("SQL Error while closing the resultset ", e);
		}
	}
}