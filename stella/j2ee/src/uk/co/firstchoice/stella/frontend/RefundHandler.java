package uk.co.firstchoice.stella.frontend;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;

import uk.co.firstchoice.fcutil.FCException;
import uk.co.firstchoice.fcutil.OracleComUtils;

public class RefundHandler {

	public RefundHandler() {
	}

	public Collection allReasonCodes() throws StellaException {
		OracleComUtils ocu = null;
		CallableStatement cstmt = null;
                ResultSet rset = null;
		try {
			ocu = new OracleComUtils("java:/comp/env/jdbc.stella");
			//			ocu.getConnection().setAutoCommit(false);
			cstmt = ocu
					.getCallableStatement("{?=call p_stella_get_data.get_refund_reasons()}");
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.driver.OracleTypes.CURSOR);
			cstmt.execute();
			rset = (ResultSet) cstmt.getObject(1);
			Collection c = new Vector();
			while (rset.next()) {
				if ("Y".equalsIgnoreCase(rset.getString("enabled_ind"))) {
					c.add(rset.getString("refund_reason_code") + "~"
							+ rset.getString("description") + "~"
							+ rset.getString("allow_additional_free_text_ind"));
				}
			}
			return c;
		} catch (SQLException e) {
			throw new StellaException(
					"Error finding list of ADM reason codes. Try restarting the application.",
					e);
		} catch (FCException e) {
			throw new StellaException(
					"Error finding list of ADM reason codes. Try restarting the application.",
					e);
                }finally {
                    try {
                     if (null != rset) rset.close();
                     if (null != cstmt) cstmt.close();
		     if (null != ocu) ocu.close();
                        } catch (SQLException e) { 	}
                       
		}

                }

		
//	}

	public void addTicket(RefundBean rb, TicketBean tb,RefundTicketCollectionBean rtcb) throws StellaException {
		try {
			RefundTicket rt = new RefundTicket();
			if (!rb.getDocumentType().equals("")
					&& !rb.getPostTicket().equals("")) {
				String[] ticketParams = rb.getPostTicket().split(",");
				rt.setTicketNumber(tb.getTicketNumber());
				rt.setTotalSeatCost(tb.getSeatCost());
				rt.setTotalTax(tb.getTotalTax());

				rt.setAirlinePenalty(ticketParams[0]);
				rt.setFareUsed(ticketParams[1]);
				rt.setSeatCost(ticketParams[2]);
				rt.setTaxAdjust(ticketParams[3]);
				rt.setTaxCost(ticketParams[4]);
				rt.setCommissionPct(ticketParams[5]);
				rt.setCommissionAmt(ticketParams[6]);
				rtcb.setDocumentTotal(rtcb.getDocumentTotalFloat()
						+ rt.getDocumentTotalFloat());
				if (rtcb.getEditingLineNo() == -1) {
					rt.setAdded(true);
					rtcb.getRefundTickets().add(rt);
				} else {
					rtcb.setEdited(false); //we want it removed, not marked as
					// deleted
					removeTicket(rb, rtcb, rtcb.getEditingLineNo());
					rtcb.setEdited(true); //but we are still editing
					rt.setUpdated(true); //this is now a changed ticket
					rtcb.getRefundTickets().add(rtcb.getEditingLineNo(), rt);
				}
				rtcb.setAirlineNum(tb.getAirlineNumber());
				rtcb.setAirlineName(tb.getAirlineName());
			}
		} // try
		catch (IndexOutOfBoundsException e) {
			throw new StellaException(
					"Data error adding a ticket to the refund. Please go back and verify the data.",
					e);
		}
	}

	public void removeTicket(RefundBean rb, RefundTicketCollectionBean rtcb,
			int ticketNo) {
		if (ticketNo >= 0 && ticketNo < rtcb.getRefundTickets().size()) {
			RefundTicket rt = (RefundTicket) rtcb.getRefundTickets().elementAt(
					ticketNo);
			rtcb.setDocumentTotal(rtcb.getDocumentTotalFloat()
					- rt.getDocumentTotalFloat());
			if (rtcb.isEdited()) { //Is the refund being edited as opposed to
				// created ie. has it
				//been saved before, then mark as deleted so we can update DB
				((RefundTicket) rtcb.getRefundTickets().elementAt(ticketNo))
						.setDeleted(true);
			} else {
				//otherwise just remove
				rtcb.getRefundTickets().removeElementAt(ticketNo);
			}
		}
		if (rtcb.getRefundTickets().size() == 0) {
			rtcb.setAirlineNum("");
			rtcb.setAirlineName("");
		}
	}

	public boolean saveRefund(RefundBean rb, RefundTicketCollectionBean rtcb,
			String userID) throws StellaException {
		/***********************************************************************
		 * p_stella_get_data.insert_refund ( p_refund_document_no CHAR, 1
		 * p_doc_type_code CHAR, 2 p_issue_date DATE, 3 p_dispute_adm_ind CHAR,
		 * 4 p_dispute_date DATE, 5 p_entry_user_id CHAR, 6 p_amended_user_id
		 * CHAR, 7 p_pseudo_city_code CHAR, 8 p_ccy_code CHAR, 9 p_source_ind
		 * CHAR, 10 p_ticket_no CHAR, 11 p_airline_num CHAR, 12 p_seat_amt CHAR,
		 * 13 p_tax_amt CHAR, 14 p_fare_used_amt CHAR, 15 p_airline_penalty_amt
		 * CHAR, 16 p_tax_adj_amt CHAR, 17 p_update_or_insert CHAR) 18
		 **********************************************************************/
		if (rtcb.getRefundTickets().size() == 0) {
			rb.setSaveError("Error: No tickets specified in refund");
			return false;
		}
		//DebugLevel debug = DebugLevel.ON;
		OracleComUtils ocu = null;
		CallableStatement cstmt = null;
                ResultSet rset = null;
		try {
			ocu = new OracleComUtils("java:/comp/env/jdbc.stella");

			if (rtcb.isNewRefund()) {
				cstmt = ocu
						.getCallableStatement("{ ? = call p_stella_get_data.get_refund_tickets(?) }");
				cstmt.registerOutParameter(1,
						oracle.jdbc.driver.OracleTypes.CURSOR);
				cstmt.setLong(2, StellaUtils.stringToLong(rb
						.getDocumentNumber()));
				cstmt.execute();
				 rset = (ResultSet) cstmt.getObject(1);
				if (rset.next()) {
					rb.setSaveError("Error: Refund number already exists");
					return false;
				}
			}

			ocu.getConnection().setAutoCommit(false);
			cstmt = ocu
					.getCallableStatement("{?=call p_stella_get_data.insert_refund(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			cstmt.setString(2, rb.getDocumentNumber());
			cstmt.setString(3, rb.getDocumentType());
			cstmt.setDate(4, StellaUtils.parseDate(rb.getDocumentDate()));
			if (rb.getDisputeAdm().equalsIgnoreCase("Y")) {
				cstmt.setString(5, "Y");
				cstmt.setDate(6, StellaUtils.parseDate(rb.getDisputeDate()));
			} else {
				cstmt.setString(5, "N");
				cstmt.setNull(6, java.sql.Types.DATE);
			}
			cstmt.setString(7, userID);
			cstmt.setString(8, "");
			cstmt.setString(9, "");
			cstmt.setString(10, "OL");
			cstmt.setString(21, rb.getIataNumber());
			cstmt.setString(22, rb.getAdmReasonCode());
			cstmt.setString(23, rb.getAdmReasonText());
			rb.setSaveError("");
			RefundTicket rt;
			String result = "";
			String action = "";
			for (Enumeration e = rtcb.getRefundTickets().elements(); e
					.hasMoreElements()
					&& rb.getSaveError().equals("");) {
				rt = (RefundTicket) e.nextElement();
				cstmt.setString(11, rt.getTicketNumber());
				cstmt.setString(12, rtcb.getAirlineNum()); //airline num
				cstmt.setString(13, Float.toString(rt.getSeatCostFloat()));
				cstmt.setString(14, Float.toString(rt.getTaxCostFloat()));
				cstmt.setString(15, Float.toString(rt.getFareUsedFloat()));
				cstmt
						.setString(16, Float.toString(rt
								.getAirlinePenaltyFloat()));
				cstmt.setString(17, Float.toString(rt.getTaxAdjustFloat()));
				action = //!rtcb.isEdited()?"I": //Refund is not edited, but
				// new
				rtcb.isNewRefund() ? "I" : //Refund is new
						//!rtcb.isEdited()?"I": //Refund is not edited, but new
						rt.isAdded() ? "I" : rt.isUpdated() ? "U" : rt
								.isDeleted() ? "D" : "U"; // ""; 060803 - need
				// to always update to
				// get header details
				// updated
				//System.out.println(rtcb.isNewRefund()+","+rtcb.isEdited()+","+rt.isAdded());
				cstmt.setString(18, action);
				cstmt.setString(19, Float.toString(rt.getCommissionAmtFloat()));
				cstmt.setString(20, Float.toString(rt.getCommissionPctFloat()));
				if (!action.equals("")) { //otherwise an unchanged ticket on a
					// refund - do nothing
					cstmt.execute();
					result = cstmt.getString(1);
					result = result == null ? "" : result;
					rb.setSaveError(result);
				}
			}
			if (rb.getSaveError().equals(""))
				ocu.getConnection().setAutoCommit(true);
			else
				ocu.getConnection().rollback();
			if (rb.getSaveError().equals(""))
				return true;
			else
				return false;
		} catch (SQLException e) {
			throw new StellaException(
					"Error saving refund to the data warehouse. Try restarting the application.",
					e);
		} catch (FCException e) {
			throw new StellaException(
					"Error saving refund to the data warehouse. Try restarting the application.",
					e.getCause());
		} finally {
                    try {
                     if (null != rset) rset.close();
                     if (null != cstmt) cstmt.close();
		     if (null != ocu) ocu.close();
                        } catch (SQLException e) { 	}
                }
        }

}