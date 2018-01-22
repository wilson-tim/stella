package uk.co.firstchoice.stella.frontend;

/*******************************************************************************
 * Create and find tickets
 ******************************************************************************/

import java.sql.CallableStatement;
import java.sql.SQLException;

import uk.co.firstchoice.fcutil.FCException;
import uk.co.firstchoice.fcutil.OracleComUtils;

//import javax.sql.*;
//import javax.naming.*;
//import java.util.logging.*;
//import oracle.jdbc.driver.*;

public class TicketHandler {

	public TicketHandler() {
	}

	public TicketBean createNewTicket(TicketBean tb, String saveType,
			String userID) throws StellaException {
		// Validate and save ticket
		/***********************************************************************
		 * FUNCTION insert_ticket ( p_source_ind CHAR, 1 p_pnr_no CHAR, 2
		 * p_departure_date DATE, 3 p_ticket_no CHAR, 4 p_airline_num CHAR, 5
		 * p_branch_code CHAR, 6 p_booking_reference_no CHAR, 7 p_season CHAR, 8
		 * p_e_ticket_ind CHAR, 9 p_ticketing_agent_initials CHAR, 10
		 * p_commission_amt CHAR, 11 p_commission_pct CHAR, 12
		 * p_selling_fare_amt CHAR, 13 p_published_fare_amt CHAR, 14 p_iata_no
		 * CHAR, 15 p_entry_user_id CHAR, 16 p_ticket_issue_date DATE, 17
		 * p_num_pax CHAR, 18 p_passenger_name CHAR, 19 p_gb_tax_amt CHAR, 20
		 * p_remaining_tax_amt CHAR, 21 p_ub_tax_amt CHAR, 22 p_linked_ticket_no
		 * CHAR, 23 p_ccy_code CHAR, 24 p_pseudo_city_code CHAR, 25
		 * p_passenger_type CHAR 26 document_type CHAR 27 ) RETURN CHAR;
		 **********************************************************************/
		OracleComUtils ocu = null;
		try {
			ocu = new OracleComUtils("java:/comp/env/jdbc.stella");
			CallableStatement cstmt = ocu
					.getCallableStatement("{?=call p_stella_get_data.insert_ticket(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setString(2, "OL");
			cstmt.setString(3, tb.getPnr());
			if (!saveType.equalsIgnoreCase("void")) {
				cstmt.setDate(4, StellaUtils.parseDate(tb.getDepartureDate()));
			} else {
				cstmt.setNull(4, java.sql.Types.DATE);
			}
			cstmt.setString(5, tb.getTicketNumber());
			cstmt.setString(6, tb.getAirlineNumber());
			cstmt.setString(7, tb.getBranchCode());
			cstmt.setString(8, tb.getAtopReference());
			cstmt.setString(9, "" /* tb.getAtopSeason() */);
			cstmt.setString(10, tb.getEticket().equals("Y") ? "Y" : "N");
			cstmt.setString(11, tb.getAgentInit());
			cstmt.setString(12, Float.toString(tb.getCommissionAmtFloat()));
			cstmt.setString(13, Float.toString(tb.getCommissionPctFloat()));
			cstmt.setString(14, Float.toString(tb.getSellingFareFloat()));
			cstmt.setString(15, Float.toString(tb.getPublishedFareFloat()));
			cstmt.setString(16, tb.getIataNumber());
			cstmt.setString(17, userID);
			cstmt.setDate(18, StellaUtils.parseDate(tb.getIssueDate()));
			cstmt.setString(19, tb.getNumPassengers());
			cstmt.setString(20, tb.getPassengerName());
			cstmt.setString(21, Float.toString(tb.getGbTaxFloat()));
			cstmt.setString(22, Float.toString(tb.getRemainingTaxFloat()));
			cstmt.setString(23, Float.toString(tb.getUbTaxFloat()));
			cstmt.setString(24, tb.getConjunctionMaster());
			cstmt.setString(25, ""); //ccy code
			cstmt.setString(26, ""); //city code
			cstmt.setString(27, tb.getPassengerType().substring(0, 1));
			cstmt.setString(28, tb.getTicketType());
			cstmt.setString(29, tb.getPnrUniqueKey() == 0 ? "I" : "U"); //insert
			// or
			// update
			// I/U
			cstmt.setString(30, tb.getExchangeTicketNo());
			if (tb.getPnrUniqueKey() != 0) {
				cstmt.setLong(31, tb.getPnrUniqueKey());
			} else {
				cstmt.setNull(31, java.sql.Types.BIGINT);
			}
			cstmt.setString(32, tb.getTourCode());
			cstmt.setString(33, tb.getFareBasisCode());
			cstmt.setString(34, tb.getConjunctionMaster().equals("") ? "N"
					: "Y"); //conjunction_ind
			cstmt.setDate(35, StellaUtils.parseDate(tb.getPnrDate()));
			cstmt.execute();
			String res = cstmt.getString(1);
			res = res == null ? "" : res;
			tb.setSaveError(res);

			if (!tb.getSaveError().startsWith("Error"))
				tb.setSaveError(""); //we only care about errors - delete other
			// msgs.

			if (tb.getSaveError().equals("")) {
				if (saveType.equalsIgnoreCase("conjunction")) {
					if (tb.getConjunctionMaster().equals(""))
						tb.setConjunctionMaster(tb.getTicketNumber());
					tb.setLastSaveType("c");
					tb
							.setTicketNumber(""
									+ (StellaUtils.stringToLong(tb
											.getTicketNumber()) + 1));
					tb.setPublishedFare("0.00");
					tb.setSellingFare("0.00");
					tb.setCommissionPct("0.00");
					tb.setCommissionAmt("0.00");
					tb.setGbTax("0.00");
					tb.setUbTax("0.00");
					tb.setRemainingTax("0.00");
				} else if (saveType.equalsIgnoreCase("repeat")) {
					tb.setLastSaveType("r");
					tb
							.setTicketNumber(""
									+ (StellaUtils.stringToLong(tb
											.getTicketNumber()) + 1));
					tb.setPassengerName("");
				}
			}
			return tb;
		} catch (SQLException e) {
			//			Logger logger = Logger.getLogger("global");
			//			logger.log(Level.SEVERE, "SQL Error in TicketHandler,
			// createNewTicket", e);
			//			for (int i=0; i<logger.getHandlers().length; i++)
			// logger.getHandlers()[i].flush();
			throw new StellaException(
					"Error saving new ticket to the data warehouse. Try restarting the application.",
					e);
		} catch (FCException e) {
			throw new StellaException(e.getMessage(), e.getCause());
		} finally {
			if (ocu != null)
				ocu.close();
		}
	}

	public String deleteTicket(String ticketNo) throws StellaException {
		OracleComUtils ocu = null;
		try {
			ocu = new OracleComUtils("java:/comp/env/jdbc.stella");
			CallableStatement cstmt = ocu
					.getCallableStatement("{?=call p_stella_get_data.delete_ticket(?)}");
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setString(2, ticketNo);
			cstmt.execute();
			return cstmt.getString(1);
		} catch (SQLException e) {
			throw new StellaException(
					"Error deleteing ticket. Try restarting the application.",
					e);
		} catch (FCException e) {
			throw new StellaException(e.getMessage(), e.getCause());
		} finally {
			if (ocu != null)
				ocu.close();
		}
	}

}