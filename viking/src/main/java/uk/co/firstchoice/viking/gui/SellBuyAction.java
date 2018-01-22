package uk.co.firstchoice.viking.gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.co.firstchoice.fcutil.FCException;
import uk.co.firstchoice.fcutil.FCUtils;
import uk.co.firstchoice.viking.gui.util.ForwardConstants;
import uk.co.firstchoice.viking.persist.VikingJDBC;

public class SellBuyAction extends Action {

	/**
	 * Process the specified HTTP request, and create the corresponding HTTP
	 * response (or forward to another web component that will create it).
	 * Return an <code>ActionForward</code> instance describing where and how
	 * control should be forwarded, or <code>null</code> if the response has
	 * already been completed.
	 * 
	 * @param form
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * @exception Exception
	 *                if the application business logic throws an exception
	 * @return
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SellBuyFormBean sbfb = (SellBuyFormBean) form;
		ActionForward next = mapping.findForward(ForwardConstants.SUCCESS);
		String action = sbfb.getAction();
		VikingJDBC vJdbc = new VikingJDBC();
		String gatewayfrom = ForwardConstants.EMPTY;
		String gatewayto = ForwardConstants.EMPTY;
		if (action.equals(ForwardConstants.SAVE_ACTION)) {
			String error = ForwardConstants.EMPTY;
			try {
				vJdbc.startTransaction();

System.out.println(" SellBuyAction  1 .........."+ sbfb.getMealType() );

				if ("true".equalsIgnoreCase(sbfb.getSingle())) {
					// Single (not range - but now possibly multiple single
					// dates) can edit multiple dates and sectors
					for (int dateIdx = 0; dateIdx < sbfb.getStartDate().length; dateIdx++) {
						for (int sectorIdx = 0; sectorIdx < sbfb.getSectors().length; sectorIdx++) {

							gatewayfrom = sbfb.getSectors(sectorIdx).substring(
									0, 3);
							gatewayto = sbfb.getSectors(sectorIdx).substring(4,
									7);
	System.out.println(" SellBuyAction  1 in IF  .........." + sbfb.getMealType());
							error = vJdbc
									.allocationRangeInsertUpdate(
											sbfb.getSeriesNoLong(),
											//sbfb.getCustSupp(),
											sbfb.getType().equalsIgnoreCase(
													"Customer") ? "S" : "P",
											gatewayfrom,
											gatewayto,
											sbfb.getStartDate()[dateIdx],
											sbfb.getStartDate()[dateIdx], //sbfb.getEndDate()[0],
											sbfb.getSeatClass(),
											sbfb.getStatus(),
											sbfb.getSeatQty().equals("") ? 0
													: Integer.parseInt(sbfb
															.getSeatQty()),
											sbfb.getCustSupp(),
											sbfb.getContractId(),
											sbfb.getBroker(),
											sbfb.getGeminiCode(),
                                            sbfb.getMealType(),
											sbfb.getOldcurrency().length > 0 ? sbfb
													.getOldcurrency(0)
													: ForwardConstants.EMPTY,
											sbfb.getCurrency().length > 0 ? (sbfb
													.getCurrency(0)).substring(
													0, 3)
													: ForwardConstants.EMPTY,
											sbfb.getExchangeRate().length > 0 ? Float
													.parseFloat(sbfb
															.getExchangeRate(0))
													: 0,
											(sbfb.getPrice().length > 0 && !sbfb
													.getPrice(0).equals("")) ? Float
													.parseFloat(sbfb
															.getPrice(0))/* /sbfb.getSectors().length */
													: 0f,

											sbfb.getOldcurrency().length > 1 ? sbfb
													.getOldcurrency(1)
													: ForwardConstants.EMPTY,
											sbfb.getCurrency().length > 1 ? (sbfb
													.getCurrency(1)).substring(
													0, 3)
													: ForwardConstants.EMPTY,
											sbfb.getExchangeRate().length > 1 ? Float
													.parseFloat(sbfb
															.getExchangeRate(1))
													: 0,
											sbfb.getPrice().length > 1 ? Float
													.parseFloat(sbfb
															.getPrice(1))/* /sbfb.getSectors().length */
											: 0f,

											sbfb.getOldcurrency().length > 2 ? sbfb
													.getOldcurrency(2)
													: ForwardConstants.EMPTY,
											sbfb.getCurrency().length > 2 ? (sbfb
													.getCurrency(2)).substring(
													0, 3)
													: ForwardConstants.EMPTY,
											sbfb.getExchangeRate().length > 2 ? Float
													.parseFloat(sbfb
															.getExchangeRate(2))
													: 0,
											sbfb.getPrice().length > 2 ? Float
													.parseFloat(sbfb
															.getPrice(2))/* /sbfb.getSectors().length */
											: 0f,

											sbfb.getOldcurrency().length > 3 ? sbfb
													.getOldcurrency(3)
													: ForwardConstants.EMPTY,
											sbfb.getCurrency().length > 3 ? (sbfb
													.getCurrency(3)).substring(
													0, 3)
													: ForwardConstants.EMPTY,
											sbfb.getExchangeRate().length > 3 ? Float
													.parseFloat(sbfb
															.getExchangeRate(3))
													: 0,
											sbfb.getPrice().length > 3 ? Float
													.parseFloat(sbfb
															.getPrice(3))/* /sbfb.getSectors().length */
											: 0f,

											sbfb.getOldcurrency().length > 4 ? sbfb
													.getOldcurrency(4)
													: ForwardConstants.EMPTY,
											sbfb.getCurrency().length > 4 ? (sbfb
													.getCurrency(4)).substring(
													0, 3)
													: ForwardConstants.EMPTY,
											sbfb.getExchangeRate().length > 4 ? Float
													.parseFloat(sbfb
															.getExchangeRate(4))
													: 0,
											sbfb.getPrice().length > 4 ? Float
													.parseFloat(sbfb
															.getPrice(4))/* /sbfb.getSectors().length */
											: 0f,

											sbfb.getEditType().equals("add") ? ForwardConstants.RECORD_ADD
													: ForwardConstants.RECORD_EDIT,
											request.getRemoteUser());
							if ((error != null)
									&& !error.equals(ForwardConstants.EMPTY)) {
								System.out.println(" Error " + error);
								vJdbc.abortTransaction();
								sbfb.setError(error);
								break;
							}
						}
					}

				} else {
					// Not single i.e. range
					for (int i = 0; i < sbfb.getSectors().length; i++) {

						gatewayfrom = sbfb.getSectors(i).substring(0, 3);
						gatewayto = sbfb.getSectors(i).substring(4, 7);

System.out.print("  sellbuy action  in else 1 ......");

						// Check whether the date is changed if so then do
						// delete the extra records
						if (sbfb.getEditType().equals("edit")) {
							if (!((sbfb.getDelstartDate()).equals(sbfb
									.getStartDate()))) {
								error = vJdbc.allocationRangeDelete(sbfb
										.getSeriesNoLong(), sbfb.getType()
										.equalsIgnoreCase("Customer") ? "S"
										: "P", sbfb.getCustSupp(), gatewayfrom,
										gatewayto, sbfb.getDelstartDate(), sbfb
												.getStartDate()[0], sbfb
												.getSeatClass(), "S", sbfb
												.getDeleteAfterWarning(),
										request.getRemoteUser());
								if ((error != null)
										&& !error
												.equals(ForwardConstants.EMPTY)) {
									System.out
											.println(" Error while deleting start of date"
													+ error);
									vJdbc.abortTransaction();
									sbfb.setError(error);
									break;
								}
							}

							if (!((sbfb.getDelendDate()).equals(sbfb
									.getEndDate()))) {
								error = vJdbc.allocationRangeDelete(sbfb
										.getSeriesNoLong(), sbfb.getType()
										.equalsIgnoreCase("Customer") ? "S"
										: "P", sbfb.getCustSupp(), gatewayfrom,
										gatewayto, sbfb.getEndDate()[0], sbfb
												.getDelendDate(), sbfb
												.getSeatClass(), "E", sbfb
												.getDeleteAfterWarning(),
										request.getRemoteUser());
								if ((error != null)
										&& !error
												.equals(ForwardConstants.EMPTY)) {
									System.out
											.println(" Error while deleting end of date"
													+ error);
									vJdbc.abortTransaction();
									sbfb.setError(error);
									break;
								}
							}

						} // if editType = edit

						
						System.out.print("  sellbuy action  in else 2 ......"  + sbfb.getMealType() );						
						
						error = vJdbc
								.allocationRangeInsertUpdate(
										sbfb.getSeriesNoLong(),
										//sbfb.getCustSupp(),
										sbfb.getType().equalsIgnoreCase(
												"Customer") ? "S" : "P",
										gatewayfrom,
										gatewayto,
										sbfb.getStartDate()[0],
										sbfb.getEndDate()[0],
										sbfb.getSeatClass(),
										sbfb.getStatus(),
										sbfb.getSeatQty().equals("") ? 0
												: Integer.parseInt(sbfb
														.getSeatQty()),
										sbfb.getCustSupp(),
										sbfb.getContractId(),
										sbfb.getBroker(),
										sbfb.getGeminiCode(),
										sbfb.getMealType(),
										sbfb.getOldcurrency().length > 0 ? sbfb
												.getOldcurrency(0)
												: ForwardConstants.EMPTY,
										sbfb.getCurrency().length > 0 ? (sbfb
												.getCurrency(0))
												.substring(0, 3)
												: ForwardConstants.EMPTY,
										sbfb.getExchangeRate().length > 0 ? Float
												.parseFloat(sbfb
														.getExchangeRate(0))
												: 0,
										(sbfb.getPrice().length > 0 && !sbfb
												.getPrice(0).equals("")) ? Float
												.parseFloat(sbfb.getPrice(0))
												/ sbfb.getSectors().length
												: 0f,

										sbfb.getOldcurrency().length > 1 ? sbfb
												.getOldcurrency(1)
												: ForwardConstants.EMPTY,
										sbfb.getCurrency().length > 1 ? (sbfb
												.getCurrency(1))
												.substring(0, 3)
												: ForwardConstants.EMPTY,
										sbfb.getExchangeRate().length > 1 ? Float
												.parseFloat(sbfb
														.getExchangeRate(1))
												: 0,
										sbfb.getPrice().length > 1 ? Float
												.parseFloat(sbfb.getPrice(1))
												/ sbfb.getSectors().length : 0f,

										sbfb.getOldcurrency().length > 2 ? sbfb
												.getOldcurrency(2)
												: ForwardConstants.EMPTY,
										sbfb.getCurrency().length > 2 ? (sbfb
												.getCurrency(2))
												.substring(0, 3)
												: ForwardConstants.EMPTY,
										sbfb.getExchangeRate().length > 2 ? Float
												.parseFloat(sbfb
														.getExchangeRate(2))
												: 0,
										sbfb.getPrice().length > 2 ? Float
												.parseFloat(sbfb.getPrice(2))
												/ sbfb.getSectors().length : 0f,

										sbfb.getOldcurrency().length > 3 ? sbfb
												.getOldcurrency(3)
												: ForwardConstants.EMPTY,
										sbfb.getCurrency().length > 3 ? (sbfb
												.getCurrency(3))
												.substring(0, 3)
												: ForwardConstants.EMPTY,
										sbfb.getExchangeRate().length > 3 ? Float
												.parseFloat(sbfb
														.getExchangeRate(3))
												: 0,
										sbfb.getPrice().length > 3 ? Float
												.parseFloat(sbfb.getPrice(3))
												/ sbfb.getSectors().length : 0f,

										sbfb.getOldcurrency().length > 4 ? sbfb
												.getOldcurrency(4)
												: ForwardConstants.EMPTY,
										sbfb.getCurrency().length > 4 ? (sbfb
												.getCurrency(4))
												.substring(0, 3)
												: ForwardConstants.EMPTY,
										sbfb.getExchangeRate().length > 4 ? Float
												.parseFloat(sbfb
														.getExchangeRate(4))
												: 0,
										sbfb.getPrice().length > 4 ? Float
												.parseFloat(sbfb.getPrice(4))
												/ sbfb.getSectors().length : 0f,

										sbfb.getEditType().equals("add") ? ForwardConstants.RECORD_ADD
												: ForwardConstants.RECORD_EDIT,
										request.getRemoteUser());
						if ((error != null)
								&& !error.equals(ForwardConstants.EMPTY)) {
							System.out.println(" Error " + error);
							vJdbc.abortTransaction();
							sbfb.setError(error);
							break;
						}
					}
				}
				error = vJdbc.insertComment(sbfb.getSeriesNo(), request
						.getRemoteUser(), "A", sbfb.getComments(), sbfb
						.getAmendmentType());
			} catch (FCException e) {
				e.printStackTrace();
				vJdbc.abortTransaction();
				throw e;
			}
			if (sbfb.getError().equals(ForwardConstants.EMPTY)) {
				vJdbc.commitTransaction();
				next = (mapping
						.findForward(ForwardConstants.RELOAD_PARENT_ACTION));
			} else {
				populateSeletorLists(sbfb, vJdbc, request);
			}
		} else if (sbfb.getAction().equalsIgnoreCase("newsupplyer")) {
			populateSeletorLists(sbfb, vJdbc, request);
		} else if (sbfb.getAction().equalsIgnoreCase(
				ForwardConstants.EDIT_ACTION)) {
			String[] gws = sbfb.getSectors(0).split("-");
			ResultSet rset = vJdbc.getAllocation(sbfb.getSeriesNoLong(), sbfb
					.getType().equalsIgnoreCase("customer") ? "S" : "P", sbfb
					.getCustSupp(), gws[0], gws[1], sbfb.getStartDate()[0],
					sbfb.getEndDate()[0], sbfb.getSeatClass());
			populateSeletorLists(sbfb, vJdbc, request);
			sbfb.setAvailSectors(sbfb.getSectors());
			int rowNo = 0;
			Vector currency = new Vector();
			Vector exchangerate = new Vector();
			Vector price = new Vector();
			while (rset.next()) {
				if (++rowNo == 1) {
					sbfb.setContractId(FCUtils.notNull(
							rset.getString("contract_id")).trim());
					sbfb.setBroker(FCUtils.notNull(
							rset.getString("broker_code")).trim());
					sbfb.setGeminiCode(FCUtils.notNull(
							rset.getString("gemini_code")).trim());
					sbfb.setMealType(FCUtils.notNull(
							rset.getString("meal_type_code")).trim());
					sbfb.setStatus(rset.getString("allocation_status_code"));
					sbfb.setSeatQty(rset.getString("no_of_seat"));
				}
				currency.add(rset.getString("currency_code"));
				exchangerate.add(FCUtils.floatToString(rset
						.getFloat("uk_exchange_rate"), 4));
				price.add(FCUtils.floatToString(rset.getFloat("seat_price")));
			}
			String[] tmp = new String[0];
			sbfb.setCurrency((String[]) currency.toArray(tmp));
			sbfb.setExchangeRate((String[]) exchangerate.toArray(tmp));
			sbfb.setPrice((String[]) price.toArray(tmp));
			vJdbc.closeResultSet(rset);
		} else if (sbfb.getAction().equalsIgnoreCase("add")) {
			populateSeletorLists(sbfb, vJdbc, request);
			sbfb.setSectors(sbfb.getAvailSectors());
		} else if (sbfb.getAction().equalsIgnoreCase("delete")) {

			try {
				String error = "";
				vJdbc.startTransaction();
				if ("true".equalsIgnoreCase(sbfb.getSingle())) {
					// Single can delete multiple dates and sectors
					for (int secIdx = 0; secIdx < sbfb.getSectors().length; secIdx++) {
						for (int dateIdx = 0; dateIdx < sbfb.getStartDate().length; dateIdx++) {

							gatewayfrom = sbfb.getSectors(secIdx).substring(0, 3);
							gatewayto = sbfb.getSectors(secIdx).substring(4, 7);
							error += vJdbc.allocationRangeDelete(sbfb
									.getSeriesNoLong(), sbfb.getType()
									.equalsIgnoreCase("Customer") ? "S" : "P", sbfb
									.getCustSupp(), gatewayfrom, gatewayto, sbfb
									.getStartDate()[dateIdx],
									sbfb.getStartDate()[dateIdx], sbfb
											.getSeatClass(), "X", // dummy parameter
									sbfb.getDeleteAfterWarning(), request
											.getRemoteUser());
							if (!(((error != null) && (!(error.equals("null")))))) {
								error = "";
							}
						}
					}
				} else {
					// Range can only delete single date and sector
					gatewayfrom = sbfb.getSectors(0).substring(0, 3);
					gatewayto = sbfb.getSectors(0).substring(4, 7);
					error = vJdbc
							.allocationRangeDelete(sbfb.getSeriesNoLong(), sbfb
									.getType().equalsIgnoreCase("Customer") ? "S"
									: "P", sbfb.getCustSupp(), gatewayfrom,
									gatewayto, sbfb.getStartDate()[0], sbfb
											.getEndDate()[0], sbfb.getSeatClass(),
									"X"//, dummy parameter
									, sbfb.getDeleteAfterWarning(), request
											.getRemoteUser());
					if (!(((error != null) && (!(error.equals("null")))))) {
						error = "";
					}
				}
				if ((error != null) && !error.equals(ForwardConstants.EMPTY)) {
					vJdbc.abortTransaction();
					sbfb.setError(error);
					request.setAttribute("sellbuybean", sbfb);
					next = mapping.findForward(ForwardConstants.DISPLAY_WARNING);
				} else {
					error = vJdbc.insertComment(sbfb.getSeriesNo(), request
							.getRemoteUser(), "A", sbfb.getComments(), sbfb
							.getAmendmentType());
					//next = mapping.findForward(ForwardConstants.SUCCESS);
					vJdbc.commitTransaction();
					next = (mapping
							.findForward(ForwardConstants.RELOAD_PARENT_ACTION));
				}
			} catch (FCException e) {
				System.out.println(" FCException" + e.toString());
				vJdbc.abortTransaction();
				request.setAttribute("sellbuybean", sbfb);
				next = mapping.findForward(ForwardConstants.DISPLAY_WARNING);
			}
		}
		vJdbc.close();
		return next;

	}

	/*
	 * Populate the values for the Sector, Cust/Supp and Contract ID selectors
	 *  
	 */
	private void populateSeletorLists(SellBuyFormBean sbfb, VikingJDBC vJdbc,
			HttpServletRequest request) throws FCException {
		Collection currencies = new TreeSet();
		try {
			ResultSet rset = vJdbc.getExchangeRatesBySeries(sbfb
					.getSeriesNoLong());
			while (rset.next()) {
				currencies.add(rset.getString("currency_code")
						+ ";"
						+ FCUtils.floatToString(rset
								.getFloat("uk_exchange_rate"), 4));
			}
			vJdbc.closeResultSet(rset);
		} catch (SQLException e) {
			throw new FCException("SQL Error looking up exchange rates", e);
		}
		request.setAttribute("currencies", currencies);
		HttpSession sess = request.getSession();
		sbfb.setAvailSectors(vJdbc.getAllocationAirportwise(
				sbfb.getSeriesNoLong()).split(
				ForwardConstants.MULTILINE_SEPERATOR /* u000d */));
		AccountsContainer accounts = (AccountsContainer) sess
				.getAttribute(ForwardConstants.ALL_ACCOUNTS);
		if (accounts == null) {
			accounts = new AccountsContainer(vJdbc);
			sess.setAttribute(ForwardConstants.ALL_ACCOUNTS, accounts);
		}
		String cs = sbfb.getCustSupp();
		if (cs.equals(ForwardConstants.EMPTY)) {
			cs = (String) accounts.getSortedAccountCodes().first();
		}
		sbfb.setContractCodes(lookupContractCodes(vJdbc, cs));
	}

	private List lookupContractCodes(VikingJDBC vJdbc, String custSuppId)
			throws FCException {
		try {
			Vector v = new Vector();
			ResultSet rs = vJdbc.getAccountContracts(custSuppId,
					ForwardConstants.EMPTY);
			while (rs.next()) {
				v.add(rs.getString("contract_id").trim());
			}
			vJdbc.closeResultSet(rs);
			return v;
		} catch (SQLException e) {
			throw new FCException(
					"Error retrieving contract codes for cust/supp# "
							+ custSuppId, e);
		}
	}

}