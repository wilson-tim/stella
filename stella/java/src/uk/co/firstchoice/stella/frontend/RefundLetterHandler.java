/*
 * RefundLetterHandler.java
 *
 * Created on 04 June 2003, 11:53
 */

package uk.co.firstchoice.stella.frontend;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uk.co.firstchoice.fcutil.FCException;
import uk.co.firstchoice.fcutil.OracleComUtils;

/**
 * 
 * @author Lsvensson
 */
public class RefundLetterHandler {

	/** Creates a new instance of RefundLetterHandler */
	public RefundLetterHandler() {
	}

	public RefundLetterBean createRefundLetter(MatchExceptionsBean meb)
			throws StellaException {
		RefundLetterBean rlb = new RefundLetterBean();
		OracleComUtils ocu = null;
                CallableStatement cstmt = null;
		ResultSet rset = null;
		try {
			ocu = new OracleComUtils("java:/comp/env/jdbc.stella");
			cstmt = ocu
					.getCallableStatement("{ ? = call p_stella_get_data.get_search_results(?,?,?,?,?,?,?,?,?,?,?) }");
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setNull(2, java.sql.Types.INTEGER);
			cstmt.setString(3, meb.getPnr());
			cstmt.setNull(4, java.sql.Types.INTEGER);
			cstmt.setNull(5, java.sql.Types.CHAR);
			;
			cstmt.setNull(6, java.sql.Types.INTEGER);
			cstmt.setNull(7, java.sql.Types.CHAR);
			;
			cstmt.setNull(8, java.sql.Types.DATE);
			cstmt.setNull(9, java.sql.Types.DATE);
			cstmt.setNull(10, java.sql.Types.INTEGER);
			cstmt.setNull(11, java.sql.Types.INTEGER);
			cstmt.setNull(12, java.sql.Types.INTEGER);
			cstmt.execute();
			rset = (ResultSet) cstmt.getObject(1);
			while (rset.next()) {
				if (rset.getString("RECTYPE").equalsIgnoreCase("TKT")) {
					rlb.getAllTickets().add(
							rset.getString("TICKET_NO") + ";"
									+ rset.getString("PASSENGER_NAME"));
					if (rlb.getAllTickets().size() == 1) {
						rlb.setAirlineNum(rset.getInt("AIRLINE_NUM"));
						rlb.setAirlineName(rset.getString("AIRLINE_NAME"));
					}
				}
			}
			rlb.setAirlineBean(new AirlineHandler().getAirlineDetails(rlb
					.getAirlineNum()));
			rlb.setMatchExceptionsBean(meb);
			rlb.setAtopReference(meb.getAtopReference());
			rlb.setAtopSeason(meb.getAtopSeason());
			rlb.setUnmatchedAmount(meb.getUnmatchedAmount());
			cstmt = ocu
					.getCallableStatement("{ ? = call p_stella_get_data.get_next_refund_letter_id() }");
			cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
			cstmt.execute();
			rlb.setLetterNo(cstmt.getInt(1));
			cstmt = ocu
					.getCallableStatement("{ ? = call p_stella_get_data.get_office_address() }");
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.execute();
			rlb.setOfficeAddress(cstmt.getString(1));
		} catch (SQLException e) {
			throw new StellaException(
					"Error searching for data in the data warehouse. Try restarting the application.",
					e);
		} catch (FCException e) {
			throw new StellaException(e.getMessage(), e.getCause());
		} finally {
			 try {
                     if (null != rset) rset.close();
                     if (null != cstmt) cstmt.close();
		     if (null != ocu) ocu.close();
                        } catch (SQLException e) { 	}
		}

		return rlb;
	}

	public RefundLetterBean findRefundLetter(int letterNo)
			throws StellaException {
		RefundLetterBean rlb = null;
		OracleComUtils ocu = null;
                CallableStatement cstmt = null;
		ResultSet rset = null;
		try {
			ocu = new OracleComUtils("java:/comp/env/jdbc.stella");
			cstmt = ocu
					.getCallableStatement("{ ? = call p_stella_get_data.get_single_refund_letter(?) }");
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setInt(2, letterNo);
			cstmt.execute();
			rset = (ResultSet) cstmt.getObject(1);
			if (rset.next()) {
				rlb = new RefundLetterBean();
				rlb.setNewRefundLetter(false);
				rlb.setLetterNo(letterNo);
				rlb.setAirlineNum(rset.getInt("AIRLINE_NUM"));
				rlb.setAirlineBean(new AirlineHandler().getAirlineDetails(rlb
						.getAirlineNum()));
				rlb.setAirlineName(rlb.getAirlineBean().getAirlineName());
				rlb.setSavedLetterText(rset.getString("FREE_TEXT"));
				//				rlb.setOfficeAddress(rset.getString("OFFICE_ADDRESS"));
				rlb.setLetterAuthor(rset.getString("ENTRY_USER_ID"));
				rlb.setCreationDate(rset.getDate("ENTRY_DATE"));
				rlb.setAtopReference(rset.getString("BOOKING_REFERENCE_NO"));
				rlb.setAtopSeason(rset.getString("SEASON_TYPE")
						+ rset.getString("SEASON_YEAR"));
				cstmt = ocu
						.getCallableStatement("{ ? = call p_stella_get_data.get_office_address() }");
				cstmt.registerOutParameter(1, java.sql.Types.CHAR);
				cstmt.execute();
				rlb.setOfficeAddress(cstmt.getString(1));
			}
			return rlb;
		} catch (SQLException e) {
			throw new StellaException(
					"Error searching for data in the data warehouse. Try restarting the application.",
					e);
		} catch (FCException e) {
			throw new StellaException(e.getMessage(), e.getCause());
		} finally {
			 try {
                     if (null != rset) rset.close();
                     if (null != cstmt) cstmt.close();
		     if (null != ocu) ocu.close();
                        } catch (SQLException e) { 	}
		}

		//		return rlb;
	}

	public void lookupText(RefundLetterBean rlb, boolean singular)
			throws StellaException {
		OracleComUtils ocu = null;
                CallableStatement cstmt = null;
		try {
			ocu = new OracleComUtils("java:/comp/env/jdbc.stella");
			 cstmt = ocu.getCallableStatement("{ ? = call p_stella_get_data.get_template_text(?) }");
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setInt(2, singular ? 1 : 2);
			cstmt.execute();
			rlb.setLetterText(cstmt.getString(1).replace('^', '\n'));
		} catch (SQLException e) {
			throw new StellaException(
					"Error searching for data in the data warehouse. Try restarting the application.",
					e);
		} catch (FCException e) {
			throw new StellaException(e.getMessage(), e.getCause());
		} finally {
			 try {
                     if (null != cstmt) cstmt.close();
		     if (null != ocu) ocu.close();
                        } catch (SQLException e) { 	}
		}
	}

	public String saveRefundLetter(RefundLetterBean rlb/* , String userName */)
			throws StellaException {
		OracleComUtils ocu = null;
                CallableStatement cstmt = null;

		try {
			ocu = new OracleComUtils("java:/comp/env/jdbc.stella");
			 cstmt = ocu
					.getCallableStatement("{ ? = call p_stella_get_data.insert_refund_letter(?,?,?,?,?,?) }");
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setInt(2, rlb.getLetterNo());
			cstmt.setInt(3, rlb.getAirlineNum());
			cstmt.setString(4, rlb.getLetterAuthor()/* userName */);
			cstmt.setFloat(5, StellaUtils.stringToFloat(rlb
					.getUnmatchedAmount()));
			String letterNumbers = "";
			for (int i = 0; i < rlb.getSelectedTickets().length - 1; i++)
				letterNumbers += (rlb.getSelectedTickets()[i] + ",");
			letterNumbers += rlb.getSelectedTickets()[rlb.getSelectedTickets().length - 1];

			String letterText = rlb.getLetterText1() + letterNumbers
					+ rlb.getLetterText2() + rlb.getUnmatchedAmount()
					+ rlb.getLetterText3() + rlb.getDisputeReason()
					+ rlb.getLetterText4();

			cstmt.setString(6, letterText);
			cstmt.setString(7, letterNumbers);
			cstmt.execute();
			return StellaUtils.notNull(cstmt.getString(1));
		} catch (SQLException e) {
			throw new StellaException(
					"Error searching for data in the data warehouse. Try restarting the application.",
					e);
		} catch (FCException e) {
			throw new StellaException(e.getMessage(), e.getCause());
		} finally {
			try {
                     if (null != cstmt) cstmt.close();
		     if (null != ocu) ocu.close();
                        } catch (SQLException e) { 	}
		}
	}

}