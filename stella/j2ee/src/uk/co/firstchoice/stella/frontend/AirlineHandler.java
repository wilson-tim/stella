package uk.co.firstchoice.stella.frontend;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uk.co.firstchoice.fcutil.FCException;
import uk.co.firstchoice.fcutil.OracleComUtils;

//import java.util.logging.*;

public class AirlineHandler {

	public AirlineHandler() {
	}

	public AirlineBean getAirlineDetails(int airlineNum) throws StellaException {
		OracleComUtils ocu = null;
		AirlineBean ab = null;
                CallableStatement cstmt = null;
                ResultSet rset = null;
		try {
			ocu = new OracleComUtils("java:/comp/env/jdbc.stella");
			 cstmt = ocu
					.getCallableStatement("{?=call stella.p_maint.get_airline_details(?)}");
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.driver.OracleTypes.CURSOR);
			cstmt.setInt(2, airlineNum);
			cstmt.execute();
			 rset = (ResultSet) cstmt.getObject(1);
			ab = new AirlineBean();
			if (rset.next()) {
				long lng = airlineNum;
				ab.setAirlineNumber(StellaUtils.leftPad(lng, 3));
				ab.setAirlineCode(StellaUtils.notNull(rset
						.getString("airline_code")));
				ab.setAirlineName(StellaUtils.notNull(rset
						.getString("airline_name")));
				ab.setAirlineContact(StellaUtils.notNull(rset
						.getString("contact_name")));
				ab.setAirlineAddress1(StellaUtils.notNull(rset
						.getString("address_line_1")));
				ab.setAirlineAddress2(StellaUtils.notNull(rset
						.getString("address_line_2")));
				ab.setAirlineCity(StellaUtils.notNull(rset
						.getString("city_town")));
				ab.setAirlineCounty(StellaUtils.notNull(rset
						.getString("county")));
				ab.setAirlineCountry(StellaUtils.notNull(rset
						.getString("country")));
				ab.setAirlinePostcode(StellaUtils.notNull(rset
						.getString("postcode")));
				ab.setAirlinePhone(StellaUtils.notNull(rset
						.getString("telephone_no")));
				ab.setAirlineAddressee(StellaUtils.notNull(rset
						.getString("addressee")));
				ab.setAirlineTaxCostTL(StellaUtils.notNull(rset
						.getString("tax_amt_tolerance_level")));
				ab.setAirlineSeatCostTL(StellaUtils.notNull(rset
						.getString("seat_amt_tolerance_level")));
				ab.setAirlineSectorPayment(StellaUtils.notNull(rset
						.getString("sector_payment_ind")));
				ab.setAirlineBookingPayment(StellaUtils.notNull(rset
						.getString("bkg_payment_commission_amt")));
			} else
				System.out.println("not found");
			return ab;
		} catch (SQLException e) {
			//			Logger logger = Logger.getLogger("global");
			//			logger.log(Level.SEVERE, "SQL Error in AirlineHandler,
			// getAirlineDetails", e);
			//			for (int i=0; i<logger.getHandlers().length; i++)
			// logger.getHandlers()[i].flush();
			throw new StellaException(
					"Error getting airline details from the data warehouse. Try restarting the application.",
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
	}

	public void saveAirlineDetails(AirlineBean ab, String username)
			throws StellaException {
		OracleComUtils ocu = null;
                CallableStatement cstmt = null;
		try {
			ocu = new OracleComUtils("java:/comp/env/jdbc.stella");
			if (ab.getDoDelete().equals("YES")) {
				cstmt = ocu
						.getCallableStatement("{?=call stella.p_maint.delete_airline_details(?)}");
				cstmt.registerOutParameter(1, java.sql.Types.CHAR);
				cstmt.setInt(2, Integer.parseInt(ab.getAirlineNumber()));
			} else {
				cstmt = ocu
						.getCallableStatement("{?=call stella.p_maint.save_airline_details(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
				cstmt.registerOutParameter(1, java.sql.Types.CHAR);
				cstmt.setInt(2, Integer.parseInt(ab.getAirlineNumber()));
				cstmt.setString(3, ab.getAirlineCode());
				cstmt.setString(4, ab.getAirlineName());
				cstmt.setString(5, ab.getAirlineContact());
				cstmt.setString(6, ab.getAirlineAddress1());
				cstmt.setString(7, ab.getAirlineAddress2());
				cstmt.setString(8, ab.getAirlineCity());
				cstmt.setString(9, ab.getAirlineCounty());
				cstmt.setString(10, ab.getAirlineCountry());
				cstmt.setString(11, ab.getAirlinePostcode());
				cstmt.setString(12, ab.getAirlinePhone());
				cstmt.setString(13, ab.getAirlineAddressee());
				cstmt.setFloat(14, ab.getAirlineTaxCostTLFloat());
				cstmt.setFloat(15, ab.getAirlineSeatCostTLFloat());
				cstmt.setString(16, ab.getAirlineSectorPayment());
				cstmt.setFloat(17, ab.getAirlineBookingPaymentFloat());
				cstmt.setString(18, username);
			}
			cstmt.execute();
			String err = cstmt.getString(1);
			if (err == null || !err.startsWith("Error,")) {
				ab.setSaveError("");
			} else {
				ab.setSaveError(err);
			}
		} catch (SQLException e) {
			throw new StellaException(
					"Error saving airline details to the data warehouse. Try restarting the application.",
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