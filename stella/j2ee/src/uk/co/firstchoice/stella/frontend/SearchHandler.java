package uk.co.firstchoice.stella.frontend;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uk.co.firstchoice.fcutil.FCException;
import uk.co.firstchoice.fcutil.OracleComUtils;

public class SearchHandler {

	public SearchHandler() {
	}

	public TicketBean getSingleTicket(String tn) throws StellaException {
		OracleComUtils ocu = null;
                CallableStatement cstmt = null;
		ResultSet rset = null;
		try {
			ocu = new OracleComUtils("java:/comp/env/jdbc.stella");
			
			cstmt = ocu
					.getCallableStatement("{ ? = call p_stella_get_data.get_single_ticket_details(?) }");
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.driver.OracleTypes.CURSOR);
			cstmt.setLong(2, StellaUtils.stringToLong(tn));
			cstmt.execute();
			rset = (ResultSet) cstmt.getObject(1);
			TicketBean tb = null;
			if (rset.next()) {
				tb = new TicketBean();
				tb.setPnr(StellaUtils.notNull(rset.getString("PNR_NO")));
				tb.setTicketNumber(StellaUtils.notNull(rset
						.getString("TICKET_NO")));
				tb.setAirlineNumber(StellaUtils.notNull(rset
						.getString("AIRLINE_NUM")));
				tb.setAirlineName(StellaUtils.notNull(rset
						.getString("AIRLINE_NAME")));
				tb
						.setIataNumber(StellaUtils.notNull(rset
								.getString("IATA_NO")));
				tb.setCommissionPct(rset.getFloat("COMMISSION_PCT"));
				tb.setPublishedFare(rset.getFloat("PUBLISHED_FARE_AMT"));
				tb.setSellingFare(rset.getFloat("SELLING_FARE_AMT"));
				tb.setCommissionAmt(rset.getFloat("COMMISSION_AMT"));
				tb.setAgentInit(StellaUtils.notNull(
						rset.getString("TICKETING_AGENT_INITIALS")).trim());
				tb.setEticket(StellaUtils.notNull(rset
						.getString("E_TICKET_IND")));
				tb.setIssueDate(StellaUtils.notNull(rset
						.getString("TICKET_ISSUE_DATE")));
				tb.setPassengerName(StellaUtils.notNull(rset
						.getString("PASSENGER_NAME")));
				tb.setNumPassengers(StellaUtils.notNull(rset
						.getString("NUM_PAX")));
				tb.setGbTax(rset.getFloat("GB_TAX_AMT"));
				tb.setUbTax(rset.getFloat("UB_TAX_AMT"));
				tb.setRemainingTax(rset.getFloat("REMAINING_TAX_AMT"));
				tb.setPassengerType(StellaUtils.notNull(rset
						.getString("PASSENGER_TYPE")));
				String ar = StellaUtils.notNull(rset
						.getString("BOOKING_REFERENCE_NO"));
				tb.setAtopReference(ar.equals("999999999") ? "FLEX" : ar);
				tb.setBranchCode(StellaUtils.notNull(rset
						.getString("BRANCH_CODE")));
				tb.setAtopSeason(StellaUtils.notNull(rset
						.getString("DERIVED_SEASON")));
				tb.setDepartureDate(StellaUtils.notNull(rset
						.getString("DEPARTURE_DATE")));
				tb.setTicketType(StellaUtils.notNull(rset
						.getString("DOC_TYPE_CODE")));
				tb.setSourceIndicator(StellaUtils.notNull(rset
						.getString("SOURCE_IND")));
				tb.setExchangeTicketNo(StellaUtils.notNull(rset
						.getString("EXCHANGE_TICKET_NO")));
				tb.setPnrUniqueKey(rset.getLong("PNR_ID"));
				tb.setSectorPayment(StellaUtils.notNull(
						rset.getString("SECTOR_PAYMENT_IND")).equals("Y"));
				tb
						.setTourCode(StellaUtils.notNull(rset
								.getString("TOUR_CODE")));
				tb.setFareBasisCode(StellaUtils.notNull(rset
						.getString("FARE_BASIS_CODE")));
				tb.setConjunctionMaster(StellaUtils.notNull(rset
						.getString("LINKED_TICKET_NO")));
				tb.setCreatedBy(rset.getString("ENTRY_USER_ID"));
				tb.setPnrDate(StellaUtils.notNull(rset
						.getString("PNR_CREATION_DATE")));
				tb.setCreatedOn(StellaUtils.dateToShortString(rset
						.getDate("ENTRY_DATE")));
				if (rset.getDate("AMENDED_DATE") != null) {
					tb.setLastEditedBy(rset.getString("AMENDED_USER_ID"));
					tb.setLastEditedOn(StellaUtils.dateToShortString(rset
							.getDate("AMENDED_DATE")));
				}
			}
			return tb;
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


                    /*try {
				if (null != rset)
					rset.close();
				if (null != ocu)
					ocu.close();
			} catch (SQLException e) {

			}*/
		
	}

	public void getSingleRefund(String refundDocNo, RefundBean rb,
			RefundTicketCollectionBean rtcb) throws StellaException {
		OracleComUtils ocu = null;
                CallableStatement cstmt = null;
		ResultSet rset = null;
		try {
			ocu = new OracleComUtils("java:/comp/env/jdbc.stella");
			
			System.out.println("------- get single refund  -------");
			
			 cstmt = ocu
					.getCallableStatement("{ ? = call p_stella_get_data.get_refund_tickets(?) }");
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.driver.OracleTypes.CURSOR);
			cstmt.setLong(2, StellaUtils.stringToLong(refundDocNo));
			cstmt.execute();
			rset = (ResultSet) cstmt.getObject(1);
			RefundTicket rt = null;
			boolean firstDoc = true;
			rtcb.setNewRefund(false); // this is not a newly created refund, but
			// an edited one
			//			java.sql.Date mostRecent = null;
			//			java.sql.Date lastEdit = null;
			while (rset.next()) {
				if (firstDoc) {
					firstDoc = false;
					rb.setDisputeAdm(rset.getString("DISPUTE_ADM_IND"));
					java.sql.Date dt = rset.getDate("DISPUTE_DATE");
					if (null != dt)
						rb.setDisputeDate(StellaUtils.dateToString(dt));
					dt = rset.getDate("ISSUE_DATE");
					if (null != dt)
						rb.setDocumentDate(StellaUtils.dateToString(dt));
					rb.setDocumentNumber(rset.getString("REFUND_DOCUMENT_NO"));
					rb.setDocumentType(rset.getString("DOC_TYPE_CODE"));
					rb.setIataNumber(rset.getString("IATA_NO"));
					rtcb.setEdited(true);
					rtcb.setAirlineName(rset.getString("AIRLINE_NAME"));
					rtcb.setAirlineNum(rset.getString("AIRLINE_NUM"));
					rb.setCreatedBy(rset.getString("REFUND_BATCH_ENTRY_USER"));
					rb.setCreatedOn(StellaUtils.dateToShortString(rset
							.getDate("REFUND_BATCH_ENTRY_DATE")));
					rb.setLastEditedBy(StellaUtils.notNull(rset
							.getString("REFUND_BATCH_AMENDED_USER")));
					rb.setLastEditedOn(StellaUtils.dateToShortString(rset
							.getDate("REFUND_BATCH_AMENDED_DATE")));
					rb.setAdmReasonCode(rset.getString("refund_reason_code"));
					rb.setAdmReasonText(rset.getString("reason_free_text"));
				}
				rt = new RefundTicket();
				rt.setAirlinePenalty(rset.getString("AIRLINE_PENALTY_AMT"));
				rt.setCommissionAmt(rset.getString("COMMISSION_AMT"));
				rt.setCommissionPct(rset.getString("COMMISSION_PCT"));
				rt.setFareUsed(rset.getString("FARE_USED_AMT"));
				rt.setSeatCost(rset.getString("SEAT_AMT"));
				rt.setTaxAdjust(rset.getString("TAX_USED_AMT"));
				rt.setTaxCost(rset.getString("TAX_AMT"));
				rt.setTicketNumber(rset.getString("TICKET_NO"));
				rt.setDocumentTotal(rset.getFloat("TOTAL_DOC_COST"));
				rt.setCreatedBy(rset.getString("ENTRY_USER_ID"));
				rt.setCreatedOn(StellaUtils.dateToShortString(rset
						.getDate("ENTRY_DATE")));
				rt.setLastEditedBy(StellaUtils.notNull(rset
						.getString("AMENDED_USER_ID")));
				rt.setLastEditedOn(StellaUtils.dateToShortString(rset
						.getDate("AMENDED_DATE")));
				rtcb.setDocumentTotal(rtcb.getDocumentTotalFloat()
						+ rt.getDocumentTotalFloat());
				rtcb.getRefundTickets().add(rt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new StellaException(
					"Error searching for data in the data warehouse. Try restarting the application.",
					e);
		} catch (FCException e) {
			e.printStackTrace();
			throw new StellaException(e.getMessage(), e.getCause());
		}                finally {
                      try {
                     if (null != rset) rset.close();
                     if (null != cstmt) cstmt.close();
		     if (null != ocu) ocu.close();
                        } catch (SQLException e) { 	}


		}
                /*finally {
			try {
				if (null != rset)
					rset.close();
				if (null != ocu)
					ocu.close();
			} catch (SQLException e) {
			}*/
		}

	public SearchResultCollectionBean doSearch(SearchBean sb)
			throws StellaException {
		sb.setSearchPerformed(true);

		/***********************************************************************
		 * FUNCTION get_search_results (p_in_ticket_no NUMBER, p_in_pnr CHAR,
		 * p_in_booking_ref NUMBER, p_in_season CHAR, -- in s03 format, not
		 * s2003 p_in_airline_num NUMBER, p_in_pax_name CHAR,
		 * p_in_departure_date DATE, p_in_ticket_issue_date DATE,
		 * p_in_refund_doc NUMBER, p_in_refund_letter_id NUMBER, p_in_iata_num
		 * NUMBER ) RETURN p_stella_get_data.return_refcursor
		 **********************************************************************/
		OracleComUtils ocu = null;
		ResultSet rset = null;
		try {
			System.out.println("------- in do search in search handler at 221-------");
			ocu = new OracleComUtils("java:/comp/env/jdbc.stella");
			CallableStatement cstmt = ocu
					.getCallableStatement("{ ? = call stella.f_get_search_results(?,?,?,?,?,?,?,?,?,?,?) }");
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.driver.OracleTypes.CURSOR);

			
			  System.out.println(sb.getTicketNumber() +"." + sb.getPnr() + "." +
			  sb.getAtopReference() + "." + sb.getAtopSeason() + "." +
			  sb.getAirlineNumber() + "." + sb.getPassengerName() + "." +
			  sb.getDepartureDate() + "." + sb.getIssueDate() + "." +
			  sb.getRefundNumber() + "." + sb.getRefundLetterNumber() + "." +
			  sb.getIataNumber() + "." );
			 
			if (sb.getTicketNumber().equals(""))
				cstmt.setNull(2, java.sql.Types.INTEGER);
			else
				cstmt
						.setLong(2, StellaUtils.stringToLong(sb
								.getTicketNumber()));
			cstmt.setString(3, sb.getPnr());
			if (sb.getAtopReference().equals(""))
				cstmt.setNull(4, java.sql.Types.INTEGER);
			else
				cstmt.setLong(4, StellaUtils
						.stringToLong(sb.getAtopReference()));
			cstmt.setString(5, sb.getAtopSeason());
			if (sb.getAirlineNumber().equals(""))
				cstmt.setNull(6, java.sql.Types.INTEGER);
			else
				cstmt.setLong(6, StellaUtils
						.stringToLong(sb.getAirlineNumber()));
			cstmt.setString(7, sb.getPassengerName());
			if (sb.getDepartureDate().equals(""))
				cstmt.setNull(8, java.sql.Types.DATE);
			else
				cstmt.setDate(8, StellaUtils.parseDate(sb.getDepartureDate()));
			if (sb.getIssueDate().equals(""))
				cstmt.setNull(9, java.sql.Types.DATE);
			else
				cstmt.setDate(9, StellaUtils.parseDate(sb.getIssueDate()));
			if (sb.getRefundNumber().equals(""))
				cstmt.setNull(10, java.sql.Types.INTEGER);
			else
				cstmt.setLong(10, StellaUtils
						.stringToLong(sb.getRefundNumber()));
			if (sb.getRefundLetterNumber().equals(""))
				cstmt.setNull(11, java.sql.Types.INTEGER);
			else
				cstmt.setLong(11, StellaUtils.stringToLong(sb
						.getRefundLetterNumber()));
			if (sb.getIataNumber().equals(""))
				cstmt.setNull(12, java.sql.Types.INTEGER);
			else
				cstmt.setLong(12, StellaUtils.stringToLong(sb.getIataNumber()));
			cstmt.execute();
			rset = (ResultSet) cstmt.getObject(1);
			float docAmt;
			SearchResultBean srb = null;
			SearchResultCollectionBean srcb = new SearchResultCollectionBean();
			while (rset.next()) {
				srb = new SearchResultBean();
				srb.setPnr(rset.getString("PNR_NO"));
				srb.setAirlineName(rset.getString("AIRLINE_NAME"));
				if (rset.getString("RECTYPE").equalsIgnoreCase("TKT")) {
					srb.setDocumentType("Ticket");
					srb.setDocumentNo(StellaUtils.notNull(rset
							.getString("TICKET_NO")));
					srb.setPassengerName(StellaUtils.notNull(rset
							.getString("PASSENGER_NAME")));
					String ar = StellaUtils.notNull(rset
							.getString("BOOKING_REFERENCE_NO"));
					srb.setAtopReference(ar.equals("999999999") ? "FLEX" : ar);
					srb.setAtopSeason(StellaUtils.notNull(rset
							.getString("DERIVED_SEASON")));
					srb.setDepartureDate(StellaUtils.notNull(rset
							.getString("DEPARTURE_DATE")));
				} else if (rset.getString("RECTYPE").equalsIgnoreCase("RFL")) { // refund
					// letter
					srb.setDocumentType("Refund Letter");
					srb.setDocumentNo(StellaUtils.notNull(rset
							.getString("REFUND_DOCUMENT_NO")));

				} else {
					srb.setDocumentType("Refund-"
							+ StellaUtils.notNull(rset
									.getString("REFUND_DOC_TYPE_CODE")));
					srb.setDocumentNo(StellaUtils.notNull(rset
							.getString("REFUND_DOCUMENT_NO")));
				}
				docAmt = rset.getFloat("TOTAL_DOC_COST");
				srb.setDocumentTotal(docAmt);
				srcb.addToGrandTotal(docAmt);
				srcb.getFoundDocuments().add(srb);
			}
			System.out.println("------- in do search in search handler at 318-------");
			return srcb;
		} catch (SQLException e) {
			System.out.println("***" + e);
			throw new StellaException(
					"Error searching for data in the data warehouse. Try restarting the application.",
					e);
		} catch (FCException e) {
			throw new StellaException(e.getMessage(), e.getCause());
		} finally {
			try {
				if (null != rset)
					rset.close();
				if (null != ocu)
					ocu.close();
			} catch (SQLException e) {
			}
		}
	}

}