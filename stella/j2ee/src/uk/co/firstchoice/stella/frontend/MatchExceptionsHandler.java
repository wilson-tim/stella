/*
 * MatchExceptionsHandler.java
 *
 * Created on 12 March 2003, 12:54
 */

package uk.co.firstchoice.stella.frontend;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import uk.co.firstchoice.fcutil.FCException;
import uk.co.firstchoice.fcutil.OracleComUtils;

/**
 * 
 * @author Lsvensson
 */
public class MatchExceptionsHandler {

	/** Creates a new instance of MatchExceptionsHandler */
	public MatchExceptionsHandler() {
	}

	public BeanCollectionBean findMatchExceptions(HttpServletRequest request /*
																			  * String
																			  * userName
																			  */) throws StellaException {
	
		OracleComUtils ocu = null;
		CallableStatement cstmt = null;
		ResultSet rset = null;
		BeanCollectionBean bcb = new BeanCollectionBean();
		
		String userGroups ; 

		
		try {
			String exceptionType = request.getParameter("exceptionType");  // BSP(B)/ Selling Syste (S)
			String specialistBranch = request.getParameter("specialistBranch");
		
			//String level = request.getParameter("specialistBranch"); 
			
			//			if (exceptionType == null) exceptionType = "D";
			if (exceptionType != null) {
				ocu = new OracleComUtils("java:/comp/env/jdbc.stella");
				/////////////////
	
				LdapData lt =  new LdapData();
				System.out.println("user " + request.getRemoteUser());
				//System.out.println(lt.getUserGroups("Jyoti Renganathan","STELLA").toString());
				userGroups = lt.getUserGroups(request.getRemoteUser(),"STELLA");
			 	System.out.println("user Groups " + userGroups);
	
				//////////////
				cstmt = ocu
						.getCallableStatement("{ ? = call p_stella_get_data.get_user_exceptions(?,?,?,?,?,?,?) }"); 
				//1) retrun value 2) p_user_name 3)show_bsp_exceptions 4)p_show_dwhse_exceptions 5) p_show_itour_exceptions 6)p_show_tlink_exceptions 7)p_specialist_branch
				cstmt.registerOutParameter(1,
						oracle.jdbc.driver.OracleTypes.CURSOR);
				cstmt.setString(2, request.getRemoteUser() /* userName */);
				cstmt.setString(8, userGroups);  
			
				//cstmt.setString(8, request.() /* user role */);
				
				switch (exceptionType.charAt(0)) {
		
				case 'B': // BSP
					cstmt.setString(3, "Y");
					cstmt.setString(4, "N");
					cstmt.setString(5, "N");
					cstmt.setString(6, "Y");
					cstmt.setString(7, specialistBranch);
					break;
				case 'S' : // Selling Systems   DWHS/AAIR/HAJS/CITA/MEON/SOVE
					cstmt.setString(3, "N");
					if (specialistBranch.equals("AAIR")){
						cstmt.setString(4, "N");
						cstmt.setString(5, "Y"); // itour
						cstmt.setString(6, "N");
						cstmt.setString(7,specialistBranch ); // will be AAIR
						
					}else if (specialistBranch.equals("DWHS")){
						cstmt.setString(4, "Y"); //dwhs
						cstmt.setString(5, "N");
						cstmt.setString(6, "N");
						cstmt.setString(7,"");
					}else{
						cstmt.setString(4, "N");
						cstmt.setString(5, "N");
						cstmt.setString(6, "Y");  // travelink
						cstmt.setString(7,specialistBranch );
				
					}
					break;
							
		  		}
				
				cstmt.execute();
				rset = (ResultSet) cstmt.getObject(1);
				MatchExceptionsBean meb = null;
				bcb.setIndicator(exceptionType);
				bcb.setSpecialistBranch(specialistBranch);
				
				while (rset.next()) {
					meb = new MatchExceptionsBean();
					meb.setAtopReference(StellaUtils.notNull(rset
							.getString("BOOKING_REFERENCE_NO")));
					meb.setAtopSeason(StellaUtils.notNull(rset
							.getString("SEASON")));
					meb.setDepartureDate(StellaUtils.dateToShortString(rset
							.getDate("FIRST_DEPARTURE_DATE")));
					meb.setSource(rset.getString("CRS_CODE"));
					meb
							.setDwSeatCost(rset
									.getFloat("DATA_WAREHOUSE_SEAT_COST"));
					meb.setDwTaxCost(rset.getFloat("DATA_WAREHOUSE_TAX_COST"));
					meb.setDwTotalCost(rset
							.getFloat("DATA_WAREHOUSE_TOTAL_COST"));
					meb.setFailType(rset.getString("DISCREPANCY_TYPE"));
					meb.setMatchProcessDate(StellaUtils.dateToShortString(rset
							.getDate("LAST_RECONCILE_ATTEMPT_DATE")));
					meb.setOldReasonCode(rset.getString("BOOKING_REASON_CODE"));
					meb.setPnr(rset.getString("PNR_NO"));
					meb.setStellaSeatCost(rset.getFloat("STELLA_SEAT_COST"));
					meb.setStellaTaxCost(rset.getFloat("STELLA_TAX_COST"));
					meb.setStellaTotalCost(rset.getFloat("STELLA_TOTAL_COST"));
					meb.setUnmatchedAmount(rset.getFloat("UNMATCHED_AMT"));
					meb.setPnr_id(rset.getLong("PNR_ID"));
					meb.setExceptionType(rset.getString("RECONCILE_TYPE"));
					bcb.getBeanCollection().add(meb);
				}
			}
			return bcb;
		} catch (	Exception e) {
			throw new StellaException(
					"Error searching for exception data in the data warehouse. Try restarting the application.",e);
		} /*catch ( FCException e) {
			throw new StellaException(
					"Error searching for exception data in the data warehouse. Try restarting the application.",
					e.getCause());
		} */
		finally {
			 try {
                     if (null != rset) rset.close();
                     if (null != cstmt) cstmt.close();
		     if (null != ocu) ocu.close();
                        } catch (SQLException e) { 	}
		}
	}

	public void updateMatchExceptions(BeanCollectionBean bcb,
			HttpServletRequest request) throws StellaException {
		String anyChanged = request.getParameter("anyChanged");
		
		//System.out.println("anyChanged" + anyChanged);
		
		if (bcb.getBeanCollection() != null && "1".equals(anyChanged)) {
			OracleComUtils ocu = null;
			CallableStatement cstmt = null;
			try {
				ocu = new OracleComUtils("java:/comp/env/jdbc.stella");
				//			cstmt = ocu.getCallableStatement(
				//					"{ ? = call p_stella_get_data.update_pnr_reason_code(?,?,?,?)
				// }");
				cstmt = ocu
						.getCallableStatement("{ ? = call p_stella_get_data.update_exception_reason_code(?,?,?,?,?) }");
				cstmt.registerOutParameter(1, java.sql.Types.CHAR);
				//                int index = 0;
				MatchExceptionsBean meb = null;
				String[] reasonCodes = request
						.getParameterValues("newReasonCode");
				String[] pnrs = request.getParameterValues("pnr");
				String[] pnrIds = request.getParameterValues("pnrId");
				String[] oldReasonCodes = request
						.getParameterValues("oldReasonCode");
				String errors = "";
				String err = "";
				for (int idx = 0; idx < reasonCodes.length; idx++) {
					if (!"--".equals(reasonCodes[idx])) {
						cstmt.setString(2, pnrs[idx].length() <= 6 ? "D" : "B");
						cstmt.setLong(3, Long.parseLong(pnrIds[idx]));
						cstmt.setString(4, reasonCodes[idx].substring(0, 2)); // QA;1
						// ->
						// QA
						cstmt.setString(5, request.getRemoteUser());
						cstmt.setString(6, oldReasonCodes[idx]);
						cstmt.execute();
						err = cstmt.getString(1);
						if (!(err == null || err.equals("")))
							errors += "Line " + (idx) + ": " + err + "<br>";
					}
				}
				/*
				 * for (Enumeration e = bcb.getBeanCollection().elements();
				 * e.hasMoreElements(); ) { meb =
				 * (MatchExceptionsBean)e.nextElement(); // reasonCode =
				 * request.getParameter("newReasonCode"+index++); String
				 * reasonCode = reasonCodes[index++]; if
				 * (!reasonCode.equals("--")) { cstmt.setString(2,
				 * meb.getPnr().length() <=6?"D":"B"); cstmt.setLong(3,
				 * meb.getPnr_id()); cstmt.setString(4,
				 * reasonCode.substring(0,2)); // QA;1 -> QA cstmt.setString(5,
				 * request.getRemoteUser()); cstmt.setString(6,
				 * meb.getOldReasonCode()); cstmt.execute(); err =
				 * cstmt.getString(1); if (!(err == null || err.equals("")))
				 * errors += "Line "+(index)+": "+err+" <br> "; } }
				 */
				bcb.setError(errors);
			} catch (SQLException e) {
				throw new StellaException(
						"Error updating exception data in the data warehouse. Try restarting the application.",
						e);
			} catch (FCException e) {
				throw new StellaException(
						"Error updating exception data in the data warehouse. Try restarting the application.",
						e.getCause());
			} finally {
				 try {
                     if (null != cstmt) cstmt.close();
		     if (null != ocu) ocu.close();
                        } catch (SQLException e) { 	}
			}
		}
	}

	public BeanCollectionBean findMatchHistory(String pnrId)
			throws StellaException {
		OracleComUtils ocu = null;
		CallableStatement cstmt = null;
		ResultSet rset = null;
		try {
			ocu = new OracleComUtils("java:/comp/env/jdbc.stella");
			cstmt = ocu
					.getCallableStatement("{ ? = call p_stella_get_data.get_reconciliation_history(?) }");
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.driver.OracleTypes.CURSOR);
			cstmt.setLong(2, Long.parseLong(pnrId));
			cstmt.execute();
			rset = (ResultSet) cstmt.getObject(1);
			MatchHistoryBean mhb = null;
			BeanCollectionBean bcb = new BeanCollectionBean();
			while (rset.next()) {
				mhb = new MatchHistoryBean();
				mhb.setAmendedUserId(rset.getString("amended_user_id"));
				mhb.setDwSeatCost(rset.getFloat("data_warehouse_seat_cost"));
				mhb.setDwTaxCost(rset.getFloat("data_warehouse_tax_cost"));
				mhb.setDwTotalCost(rset.getFloat("data_warehouse_total_cost"));
				mhb.setProcessCode(rset.getString("process_code"));
				mhb.setProcessDate(StellaUtils.dateToString(rset
						.getDate("process_date")));
				mhb.setReasonCode(rset.getString("reason_code"));
				mhb.setReasonShortDesc(rset.getString("reason_short_desc"));
				mhb.setStellaSeatCost(rset.getFloat("stella_seat_cost"));
				mhb.setStellaTaxCost(rset.getFloat("stella_tax_cost"));
				mhb.setStellaTotalCost(rset.getFloat("stella_total_cost"));
				mhb.setUnmatchedAmount(rset.getFloat("unmatched_amt"));
				bcb.getBeanCollection().add(mhb);
			}
			return bcb;
		} catch (SQLException e) {
			throw new StellaException(
					"Error searching for match history in the data warehouse. Try restarting the application.",
					e);
		} catch (FCException e) {
			throw new StellaException(
					"Error searching for match history in the data warehouse. Try restarting the application.",
					e.getCause());
		} finally {
			 try {
                     if (null != rset) rset.close();
                     if (null != cstmt) cstmt.close();
		     if (null != ocu) ocu.close();
                        } catch (SQLException e) { 	}
		}
	}

	public BeanCollectionBean findMatchDescriptions() throws StellaException {
		OracleComUtils ocu = null;
		CallableStatement cstmt = null;
		ResultSet rset = null;
		try {
			ocu = new OracleComUtils("java:/comp/env/jdbc.stella");
			cstmt = ocu
					.getCallableStatement("{ ? = call p_stella_get_data.get_all_reasons() }");
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.driver.OracleTypes.CURSOR);
			cstmt.execute();
			rset = (ResultSet) cstmt.getObject(1);
			MatchDescriptionBean mdb = null;
			BeanCollectionBean bcb = new BeanCollectionBean();
			while (rset.next()) {
				mdb = new MatchDescriptionBean();
				mdb.setReasonCode(rset.getString("REASON_CODE"));
				mdb.setReasonDescription(rset.getString("DESCRIPTION"));
				bcb.getBeanCollection().add(mdb);
			}
			return bcb;
		} catch (SQLException e) {
			throw new StellaException(
					"Error finding match description in the data warehouse. Try restarting the application.",
					e);
		} catch (FCException e) {
			throw new StellaException(
					"Error finding match description in the data warehouse. Try restarting the application.",
					e.getCause());
		} finally {
			 try {
                     if (null != rset) rset.close();
                     if (null != cstmt) cstmt.close();
		     if (null != ocu) ocu.close();
                        } catch (SQLException e) { 	}
		}
	}

	public BspDetailBean findBspDetails(String bspid) throws StellaException {
		OracleComUtils ocu = null;
		CallableStatement cstmt = null;
		ResultSet rset = null;
		try {
			ocu = new OracleComUtils("java:/comp/env/jdbc.stella");
			cstmt = ocu
					.getCallableStatement("{ ? = call p_stella_bsp_data.get_bsp_tran_details(?,?,?) }");
			cstmt
					.registerOutParameter(1,
							oracle.jdbc.driver.OracleTypes.CURSOR);
			cstmt.setNull(2, java.sql.Types.INTEGER);
			cstmt.setNull(3, java.sql.Types.INTEGER);
			cstmt.setInt(4, StellaUtils.stringToInt(bspid));
			cstmt.execute();
			rset = (ResultSet) cstmt.getObject(1);
			BspDetailBean bdb = new BspDetailBean();
			if (rset.next()) {
				bdb.setAirlineNum(rset.getString("AIRLINE_NUM"));
				bdb.setDocumentNumber(rset.getString("TKT_OR_REFUND_NUM"));
				bdb.setBspCrsCode(rset.getString("BSP_CRS_CODE"));
				bdb.setTransactionCode(rset.getString("TRANSACTION_CODE"));
				bdb.setReasonCode(rset.getString("REASON_CODE"));
				bdb.setBspPeriodEndDate(StellaUtils.dateToString(rset
						.getDate("BSP_PERIOD_ENDING_DATE")));
				bdb.setDiscrepancyType(rset.getString("DISCREPANCY_TYPE"));
				bdb.setLastReconciledDate(StellaUtils.dateToString(rset
						.getDate("LAST_RECONCILED_DATE")));
				bdb.setBspSeatCost(StellaUtils.floatToString(rset
						.getFloat("BSP_SEAT_COST")));
				bdb.setTaxAmt(StellaUtils.floatToString(rset
						.getFloat("TAX_AMT")));
				bdb.setTotalBspCost(StellaUtils.floatToString(rset
						.getFloat("TOTAL_BSP_COST")));
				bdb.setStellaSeatAmt(StellaUtils.floatToString(rset
						.getFloat("STELLA_SEAT_AMT")));
				bdb.setStellaTaxAmt(StellaUtils.floatToString(rset
						.getFloat("STELLA_TAX_AMT")));
				bdb.setTotalStellaCost(StellaUtils.floatToString(rset
						.getFloat("TOTAL_STELLA_COST")));
				bdb.setUnmatchedAmt(StellaUtils.floatToString(rset
						.getFloat("UNMATCHED_AMT")));
				bdb.setShortDesc(rset.getString("REASON_SHORT_DESC"));
				bdb.setBspSuppCommAmt(StellaUtils.floatToString(rset
						.getFloat("BSP_SUPP_COMM_AMT")));
				bdb.setBspCommissionableAmt(StellaUtils.floatToString(rset
						.getFloat("BSP_COMMISSIONABLE_AMT")));
				bdb.setBspCommissionAmt(StellaUtils.floatToString(rset
						.getFloat("BSP_COMMISSION_AMT")));
				bdb.setBspNetFareAmt(StellaUtils.floatToString(rset
						.getFloat("BSP_NET_FARE_AMT")));
				bdb.setConjunctionInd(rset.getString("CONJUNCTION_IND"));
			} else {
				bdb.setError("BSP details not found (" + bspid + ")");
			}
			return bdb;
		} catch (SQLException e) {
			throw new StellaException(
					"Error finding BSP details in the data warehouse. Try restarting the application.",
					e);
		} catch (FCException e) {
			throw new StellaException(
					"Error finding BSP details in the data warehouse. Try restarting the application.",
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