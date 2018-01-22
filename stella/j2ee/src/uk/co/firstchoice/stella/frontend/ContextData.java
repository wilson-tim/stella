package uk.co.firstchoice.stella.frontend;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import uk.co.firstchoice.fcutil.FCException;
import uk.co.firstchoice.fcutil.OracleComUtils;

public class ContextData {

	// Bean Properties
	private String version = "1.08";

	private String serverName = "";

	private String dwName = "";

	private Vector airlines = null;

	private Vector ticketDocTypes = null;

	private Vector refundDocTypes = null;

	private Vector iataNumbers = null;
	
	private Vector specialistBranch = null;

	public ContextData() throws StellaException {

		OracleComUtils ocu = null;
		CallableStatement cstmt = null;
		ResultSet rset = null;
		Statement stmt = null;
		try {
			ocu = new OracleComUtils("java:/comp/env/jdbc.stella");
			cstmt = ocu
					.getCallableStatement("{ ? = call p_stella_get_data.get_all_airlines() }");
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.driver.OracleTypes.CURSOR);
			cstmt.execute();
			rset = (ResultSet) cstmt.getObject(1);
			airlines = new Vector();
			String noStr = "";
			while (rset.next()) {
				noStr = "000" + rset.getInt("AIRLINE_NUM");
				airlines.add(rset.getString("AIRLINE_NAME") + "-"
						+ noStr.substring(noStr.length() - 3) + ";"
						+ rset.getString("SECTOR_PAYMENT_IND"));
			}
			
			// Added BY  Jyoti, 20/04/06 , dropdown for exception screen 
			cstmt = ocu
			.getCallableStatement("{ ? = call p_stella_get_data.get_specialist_branchlist() }");
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.driver.OracleTypes.CURSOR);
			cstmt.execute();
			rset = (ResultSet) cstmt.getObject(1);
			specialistBranch = new Vector();
			while (rset.next()) {
				specialistBranch.add(rset.getString("BRANCH_CODE") + "-"
						+ rset.getString("DESCRIPTION"));
			}
			

			cstmt = ocu
					.getCallableStatement("{ ? = call p_stella_get_data.get_all_doc_types(?) }");
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.driver.OracleTypes.CURSOR);
			cstmt.setString(2, "");
			cstmt.execute();
			rset = (ResultSet) cstmt.getObject(1);
			ticketDocTypes = new Vector();
			refundDocTypes = new Vector();
			String type = "";
			while (rset.next()) {
				noStr = rset.getString("DOC_TYPE_CODE") + "-"
						+ rset.getString("DESCRIPTION");
				type = rset.getString("DOC_CATEGORY_CODE");
				if (type.equals("T"))
					ticketDocTypes.add(noStr);
				else if (type.equals("R"))
					refundDocTypes.add(noStr);
			}

			cstmt = ocu
					.getCallableStatement("{ ? = call p_stella_get_data.get_all_iata_details() }");
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.driver.OracleTypes.CURSOR);
			cstmt.execute();
			rset = (ResultSet) cstmt.getObject(1);
			iataNumbers = new Vector();
			while (rset.next()) {
				iataNumbers.add(rset.getString("IATA_NO") + "-"
						+ rset.getString("DESCRIPTION"));
			}

			stmt = ocu.getStatement();
			rset = stmt.executeQuery("select * from global_name");
			if (rset.next())
				dwName = rset.getString(1);
			dwName = (null == dwName ? "N/A" : (dwName.length() > 3 ? dwName
					.substring(0, 3) : dwName));
		} catch (SQLException e) {
			throw new StellaException("SQL Error retriving ContextData.", e);
		} catch (FCException e) {
			throw new StellaException("Naming Error retriving ContextData. ("
					+ e.getMessage() + ")", e.getCause());
		} finally {
			
				 try {
                     if (null != rset) rset.close();
                     if (null != cstmt) cstmt.close();
		     if (null != ocu) ocu.close();
                        } catch (SQLException e) { 	}
		}

	}

	public String getDwName() {
		return dwName;
	}

	public String getServerName() {
		return serverName;
	}

	public String getVersion() {
		return version;
	}

	public Vector getAirlines() {
		return airlines;
	}

	public Vector getIataNumbers() {
		return iataNumbers;
	}

	public Vector getTicketDocTypes() {
		return ticketDocTypes;
	}

	public Vector getRefundDocTypes() {
		return refundDocTypes;
	}

	public Vector getSpecialistBranchList() {
		return specialistBranch;
	}
	
}