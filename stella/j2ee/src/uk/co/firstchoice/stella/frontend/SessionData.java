 package uk.co.firstchoice.stella.frontend;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
//import javax.servlet.http.HttpServletRequest;

import uk.co.firstchoice.fcutil.FCException;
import uk.co.firstchoice.fcutil.OracleComUtils;


public class SessionData {

	private Vector userReasons = null;

	private Vector bspUserReasons = null;

	public SessionData() {
	}

	public SessionData(String un) throws StellaException {

		OracleComUtils ocu = null;
		CallableStatement cstmt = null;
		ResultSet rset = null;
		try {

			ocu = new OracleComUtils("java:/comp/env/jdbc.stella");
		////////////////

			TraditionalPersonDaoImpl t = new TraditionalPersonDaoImpl();
			t.getAllPersonNames();
			
            System.out.println("IN SESSION DATA ::::::::::::: " + un);			
			///////////////////
			String userGroups ; 
			LdapData lt =  new LdapData();

			System.out.println("calling get user groups for user " + un);
			
			userGroups = lt.getUserGroups(un,"STELLA");
			
			System.out.println("user Groups in session data " + userGroups);
			
			/////////
			cstmt = ocu
					.getCallableStatement("{ ? = call p_stella_get_data.get_user_reasons(?,?) }");
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.driver.OracleTypes.CURSOR);
			cstmt.setString(2, un);
			
			//cstmt.setString(3, "STELLA_BSP_EXECUTIVE,STELLA_SUPERVISOR");
			cstmt.setString(3, userGroups);
			cstmt.execute();
					
			rset = (ResultSet) cstmt.getObject(1);
			userReasons = new Vector();
			bspUserReasons = new Vector();
			while (rset.next()) {
				if (rset.getString("APPLICATION_KEY").trim().equalsIgnoreCase(
						"STLBSPR")) {
					bspUserReasons.add(rset.getString("REASON_CODE") + "-"
							+ rset.getString("DESCRIPTION")
							+ rset.getString("prompt_refund_letter_ind"));
				} else {
					userReasons.add(rset.getString("REASON_CODE") + "-"
							+ rset.getString("DESCRIPTION")
							+ rset.getString("prompt_refund_letter_ind"));
				}
			}
		} catch (SQLException e) {
			throw new StellaException("Error constructing SessionData.", e);
		} catch (FCException e) {
			throw new StellaException("Error constructing SessionData.", e
					.getCause());
		} finally {
			 try {
                     if (null != rset) rset.close();
                     if (null != cstmt) cstmt.close();
		     if (null != ocu) ocu.close();
                        } catch (SQLException e) { 	}
		}

	}

	public Vector getUserReasons() {
		return userReasons;
	}

	public Vector getBspUserReasons() {
		return bspUserReasons;
	}

}