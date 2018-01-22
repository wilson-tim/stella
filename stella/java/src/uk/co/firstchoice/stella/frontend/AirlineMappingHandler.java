/*
 * AirlineMappingHandler.java
 *
 * Created on 24 April 2003, 13:08
 */

package uk.co.firstchoice.stella.frontend;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeMap;

import uk.co.firstchoice.fcutil.FCException;
import uk.co.firstchoice.fcutil.OracleComUtils;

//import oracle.jdbc.driver.*;

/**
 * 
 * @author Lsvensson
 */
public class AirlineMappingHandler {

	/** Creates a new instance of AirlineMappingHandler */
	public AirlineMappingHandler() {
	}

	public AirlineMappingBean getMappings() throws StellaException {

		OracleComUtils ocu = null;
		//		Connection conn = null;
		//		CallableStatement cstmt = null;
                CallableStatement cstmt = null;
		ResultSet rset = null;

		try {
			//			Context ctx = new InitialContext();
			//			DataSource ds = (DataSource)ctx.lookup("java:/comp/env/jdbc.stella");
			//			conn = ds.getConnection();
			//			cstmt = conn.prepareCall("{ ? = call
			// p_stella_get_data.get_airline_user_allocations(?,?) }");
			//			cstmt.registerOutParameter(1, OracleTypes.CURSOR);
			ocu = new OracleComUtils("java:/comp/env/jdbc.stella");
			 cstmt = ocu
					.getCallableStatement("{ ? = call p_stella_get_data.get_airline_user_allocations(?,?) }");
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setNull(2, java.sql.Types.CHAR);
			cstmt.setNull(3, java.sql.Types.INTEGER);
			cstmt.execute();
			rset = (ResultSet) cstmt.getObject(1);
			AirlineMappingBean amb = new AirlineMappingBean();
			String s;
			while (rset.next()) {
				s = "00" + rset.getString("AIRLINE_NUM");
				s = s.substring(s.length() - 3);
				amb.addEntry(rset.getString("USER_NAME"), rset
						.getString("AIRLINE_NAME")
						+ "-" + s);
			}

			//			cstmt = conn.prepareCall("{ ? = call
			// p_stella_get_data.get_all_airlines() }");
			//			cstmt.registerOutParameter(1, OracleTypes.CURSOR);
			cstmt = ocu
					.getCallableStatement("{ ? = call p_stella_get_data.get_all_airlines() }");
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.execute();
			rset = (ResultSet) cstmt.getObject(1);
			String noStr = "";
			TreeMap uba = amb.getUsersByAirline();
			while (rset.next()) {
				noStr = "00" + rset.getInt("AIRLINE_NUM");
				noStr = rset.getString("AIRLINE_NAME") + "-"
						+ noStr.substring(noStr.length() - 3);
				if (!uba.containsKey(noStr)) {
					amb.addEntry("", noStr);
				}
			}

			return amb;
		} catch (SQLException e) {
			//			Logger logger = Logger.getLogger("global");
			//			logger.log(Level.SEVERE, "SQL Error in AirlineMappingHandler
			// callSearch", e);
			//			for (int i=0; i<logger.getHandlers().length; i++)
			// logger.getHandlers()[i].flush();
			throw new StellaException(
					"Error searching for airline mappings in the data warehouse. Try restarting the application.",
					e);
		} catch (FCException e) {
			//			Logger logger = Logger.getLogger("global");
			//			logger.log(Level.SEVERE, "Naming Error in AirlineMappingHandler
			// callSearch", e);
			//			for (int i=0; i<logger.getHandlers().length; i++)
			// logger.getHandlers()[i].flush();
			throw new StellaException(e.getMessage(), e.getCause());
		} finally {
			 try {
                     if (null != rset) rset.close();
                     if (null != cstmt) cstmt.close();
		     if (null != ocu) ocu.close();
                        } catch (SQLException e) { 	}
		}
	}

	public String saveMappings(javax.servlet.http.HttpServletRequest request)
			throws StellaException {
		String type = request.getParameter("type"); // "user" or "airline" - the
		// key. or adduser
		String key = request.getParameter((type.equals("user") ? "userName"
				: type.equals("adduser") ? "amAddUserSelect" : "airline"));
		String[] values = request.getParameterValues(type.equals("user")
				|| type.equals("adduser") ? "amAirlineSelect" : "amUserSelect");
		String error = "";
		CallableStatement cstmt = null;
		OracleComUtils ocu = null;
		try {
			ocu = new OracleComUtils("java:/comp/env/jdbc.stella");
			int iKey = 0;
			if (!type.equals("adduser")) {
				cstmt = ocu
						.getCallableStatement("{ ? = call p_stella_get_data.delete_airline_user_alloc(?,?) }");
				cstmt.registerOutParameter(1, java.sql.Types.CHAR);
				if (type.equals("user")) { // delete all airlines allocated to
					// this user
					cstmt.setNull(2, java.sql.Types.INTEGER); // airline num
					cstmt.setString(3, key); // user name
				} else { // delete all users allocated to this airline
					iKey = Integer.parseInt(key.substring(key.length() - 3));
					cstmt.setInt(2, iKey); // airline num
					cstmt.setNull(3, java.sql.Types.CHAR); // user name
				}
				cstmt.execute();
				String err = StellaUtils.notNull(cstmt.getString(1));
				error = err.startsWith("Error,") ? err : "";
			}
			if (error.equals("") && values != null) {
				String userName = request.getRemoteUser();
				//				cstmt = conn.prepareCall("{ ? = call
				// p_stella_get_data.insert_airline_user_alloc(?,?,?) }");
				cstmt = ocu
						.getCallableStatement("{ ? = call p_stella_get_data.insert_airline_user_alloc(?,?,?) }");
				cstmt.registerOutParameter(1, java.sql.Types.CHAR);
				for (int i = 0; i < values.length; i++) {
					if (type.equals("airline")) {
						cstmt.setInt(2, iKey);
						cstmt.setString(3, values[i]);
					} else {
						cstmt.setInt(2, Integer.parseInt(values[i]));
						cstmt.setString(3, key);
					}
					cstmt.setString(4, userName);
					cstmt.execute();
					if (cstmt.getString(1) != null)
						error += cstmt.getString(1);
				}
			}
			return error;
		} catch (SQLException e) {
			//			Logger logger = Logger.getLogger("global");
			//			logger.log(Level.SEVERE, "SQL Error in AirlineMappingHandler
			// callSearch", e);
			//			for (int i=0; i<logger.getHandlers().length; i++)
			// logger.getHandlers()[i].flush();
			//			throw new StellaException("Error searching for data in the data
			// warehouse. Try restarting the application.");
			throw new StellaException(
					"Error saving airline mappings to the data warehouse. Try restarting the application.",
					e);
		} catch (FCException e) {
			//			Logger logger = Logger.getLogger("global");
			//			logger.log(Level.SEVERE, "Naming Error in AirlineMappingHandler
			// callSearch", e);
			//			for (int i=0; i<logger.getHandlers().length; i++)
			// logger.getHandlers()[i].flush();
			//			throw new StellaException("Error searching for data in the data
			// warehouse. Try restarting the application.");
			throw new StellaException(e.getMessage(), e.getCause());
		} finally {
			 try {
                     if (null != cstmt) cstmt.close();
		     if (null != ocu) ocu.close();
                        } catch (SQLException e) { 	}
		}
	}

}