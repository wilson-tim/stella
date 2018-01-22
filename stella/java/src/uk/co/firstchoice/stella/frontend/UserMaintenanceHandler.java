/*
 * UserMaintenanceHandler.java
 *
 * Created on 19 March 2003, 12:54
 */

package uk.co.firstchoice.stella.frontend;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

import uk.co.firstchoice.fcutil.FCException;
import uk.co.firstchoice.fcutil.OracleComUtils;

/**
 * 
 * @author Lsvensson
 */
public class UserMaintenanceHandler {

	/** Creates a new instance of UserMaintenanceHandler */
	public UserMaintenanceHandler() {
	}

	public String updateUserPassword(String userName, String oldPW, String newPW)
			throws StellaException {
		//		Connection conn = null;
		OracleComUtils ocu = null;
		CallableStatement cstmt = null;
		try {
			//			Context ctx = new InitialContext();
			//			DataSource ds = (DataSource)ctx.lookup("SecurityLookup");
			//			conn = ds.getConnection();
			//			cstmt = conn.prepareCall("{ ? = call security.set_password(?,?,?)
			// }");
			ocu = new OracleComUtils("java:/comp/env/SecurityLookup");
			cstmt = ocu
					.getCallableStatement("{ ? = call security.set_password(?,?,?) }");
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setString(2, userName);
			cstmt.setString(3, newPW);
			cstmt.setString(4, oldPW);
			cstmt.execute();
			return cstmt.getString(1) == null ? "" : cstmt.getString(1);
		} catch (SQLException e) {
			//			Logger logger = Logger.getLogger("global");
			//			logger.log(Level.SEVERE, "SQL Error in UserMaintenanceHandler
			// updateUserPassword", e);
			//			for (int i=0; i<logger.getHandlers().length; i++)
			// logger.getHandlers()[i].flush();
			throw new StellaException(
					"Error updating password to the data warehouse. Try restarting the application.",
					e);
		} catch (FCException e) {
			//			Logger logger = Logger.getLogger("global");
			//			logger.log(Level.SEVERE, "Naming Error in UserMaintenanceHandler
			// updateUserPassword", e);
			//			for (int i=0; i<logger.getHandlers().length; i++)
			// logger.getHandlers()[i].flush();
			throw new StellaException(
					"Error updating password to the data warehouse. Try restarting the application.",
					e.getCause());
		} finally {
			 try {
                     if (null != cstmt) cstmt.close();
		     if (null != ocu) ocu.close();
                        } catch (SQLException e) { 	}
		}
	}

	public Vector getAllStellaUsers() throws StellaException {
		//		Connection conn = null;
		//OracleComUtils ocu = null;
		//CallableStatement cstmt = null;
		//ResultSet rset = null;
		//try {
			//			Context ctx = new InitialContext();
			//			DataSource ds = (DataSource)ctx.lookup("SecurityLookup");
			//			conn = ds.getConnection();
			//			cstmt = conn.prepareCall("{ ? = call
			// security.get_all_user_details(?) }");
			//			cstmt.registerOutParameter(1, OracleTypes.CURSOR);
			/*ocu = new OracleComUtils("SecurityLookup");
			cstmt = ocu
					.getCallableStatement("{ ? = call security.get_all_user_details(?) }");
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.driver.OracleTypes.CURSOR);
			//cstmt.setString(2, "jdbc.stella");
			cstmt.setString(2, "STELLA");
			cstmt.execute();
			rset = (ResultSet) cstmt.getObject(1);
			*/
			//Vector allStellaUsers = new Vector();
			//while (rset.next()) {
				//allStellaUsers.add(rset.getString("USER_NAME"));
			
			    LdapData lusers = new LdapData();
				
			    return lusers.getAppUsers("STELLA");
			    //allStellaUsers.add(lusers.getAppUsers());
			    
				//allStellaUsers.add("Jyoti Renganathan");
				
				
			//}
			
			
			//return allStellaUsers;
	/*	} catch (SQLException e) {
			//			Logger logger = Logger.getLogger("global");
			//			logger.log(Level.SEVERE, "SQL Error in UserMaintenanceHandler
			// getAllStellaUsers", e);
			//			for (int i=0; i<logger.getHandlers().length; i++)
			// logger.getHandlers()[i].flush();
			throw new StellaException(
					"Error searching for data in the data warehouse. Try restarting the application.",
					e);
		} catch (FCException e) {
			//			Logger logger = Logger.getLogger("global");
			//			logger.log(Level.SEVERE, "Naming Error in UserMaintenanceHandler
			// getAllStellaUsers", e);
			//			for (int i=0; i<logger.getHandlers().length; i++)
			// logger.getHandlers()[i].flush();
			throw new StellaException(
					"Error searching for data in the data warehouse. Try restarting the application.",
					e.getCause());
		} finally {
			try {
				if (null != rset)
					rset.close();
				//				if (null != cstmt) cstmt.close();
				//				if (null != conn) conn.close();
				if (null != ocu)
					ocu.close();
			} catch (SQLException e) {
			}
		}*/
		
		
	}

}