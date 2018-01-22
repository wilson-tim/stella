package uk.co.firstchoice.viking.gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.co.firstchoice.fcutil.FCException;
import uk.co.firstchoice.viking.gui.util.ForwardConstants;
import uk.co.firstchoice.viking.persist.VikingJDBC;

/**
 * The action executed when the revenue account screen is opened or submitted.
 * The field Action in the RevenueAccountFormBean object passed along in <CODE>
 * form</CODE> contain the action to perform
 */
public class RevenueAccountAction extends Action {

	/**
	 * Process the specified HTTP request, and create the corresponding HTTP
	 * response (or forward to another web component that will create it).
	 * Return an <code>ActionForward</code> instance describing where and how
	 * control should be forwarded, or <code>null</code> if the response has
	 * already been completed.
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param form
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * @exception Exception
	 *                if the application business logic throws an exception
	 * @return ActionForward where to go next
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		VikingJDBC vJdbc = new VikingJDBC();
		HttpSession sess = request.getSession();
		AccountsContainer accounts = (AccountsContainer) sess
				.getAttribute(ForwardConstants.ALL_ACCOUNTS);
		if (accounts == null) {
			accounts = new AccountsContainer(vJdbc);
			sess.setAttribute(ForwardConstants.ALL_ACCOUNTS, accounts);
		}
		String error;
		String code;
		RevenueAccountFormBean rafb = (RevenueAccountFormBean) form;
		if (rafb.getAction().equals(ForwardConstants.EMPTY)) {
			rafb.setAction(ForwardConstants.VIEW_ACTION);
		}
		if (rafb.getAction().equals(ForwardConstants.VIEW_ACTION)) {
			code = rafb.getCode();
			if (code.equals(ForwardConstants.EMPTY)) {
				if (accounts.size() > 0) {
					code = (String) accounts.getSortedAccountCodes().first();
				}
			}
			lookupAccount(vJdbc, code, rafb);
		} else if (rafb.getAction().equals(ForwardConstants.EDIT_ACTION)) {
			lookupAccount(vJdbc, rafb.getCode(), rafb);
			rafb.setSaveType(ForwardConstants.EDIT_ACTION);
		} else if (rafb.getAction().equals(ForwardConstants.SAVE_ACTION)) {
			saveAccount(vJdbc, rafb, request);
			if (rafb.getError().equals(ForwardConstants.EMPTY)) {
				accounts = new AccountsContainer(vJdbc);
				sess.setAttribute(ForwardConstants.ALL_ACCOUNTS, accounts);
				lookupAccount(vJdbc, rafb.getCode(), rafb);
				rafb.setAction(ForwardConstants.VIEW_ACTION);
			} else {
				rafb.setAction(rafb.getSaveType());
			}
		} else if (rafb.getAction().equals(ForwardConstants.DELETE_ACTION)) {
			error = vJdbc.deleteAccountDetails(rafb.getCode());
			if (error.equals(ForwardConstants.EMPTY)) {
				rafb.reset(mapping, request);
				accounts = new AccountsContainer(vJdbc);
				sess.setAttribute(ForwardConstants.ALL_ACCOUNTS, accounts);
				if (accounts.size() > 0) {
					lookupAccount(vJdbc, (String) accounts
							.getSortedAccountCodes().first(), rafb);
				}
			} else {
				rafb.setError(error);
			}
			rafb.setAction(ForwardConstants.VIEW_ACTION);
		} else if (rafb.getAction().equals(ForwardConstants.ADD_ACTION)) {
			rafb.reset(mapping, request);
			rafb.setAction(ForwardConstants.ADD_ACTION);
			rafb.setSaveType(ForwardConstants.ADD_ACTION);
			rafb.setCountryCode("GB");
		} else if (rafb.getAction().equals("deletecontract")) { // delete a
			// single
			// contract from
			// the list
			rafb.setError(vJdbc.deleteAccountContractDetails(rafb.getCode(),
					rafb.getContractSelector()));
			lookupAccount(vJdbc, rafb.getCode(), rafb);
			rafb.setAction(ForwardConstants.EDIT_ACTION);
		} else if (rafb.getAction().equals(ForwardConstants.RELOAD_ACTION)) {
			lookupAccount(vJdbc, rafb.getCode(), rafb);
			rafb
					.setAction(ForwardConstants.EDIT_ACTION /* rafb.getFromAction() */);
		} else {
			System.out.println("Unknown action ('" + rafb.getAction()
					+ "') in RevenueAccountAction");
		}
		vJdbc.close();
		return (mapping.findForward(ForwardConstants.SUCCESS));
	}

	/**
	 * @param vJdbc
	 * @throws FCException
	 * @return
	 */
	//	private AccountsContainer populateAccounts(VikingJDBC vJdbc) throws
	// FCException {
	//		AccountsContainer ac = new AccountsContainer(vJdbc);
	//		try {
	//			ResultSet rs = vJdbc.getAllAccountNames();
	//			while (rs.next()) {
	//				ac.add(rs.getString("customer_code").trim(),
	// rs.getString("customer_name"));
	//			}
	//			return ac;
	//		}
	//		catch (SQLException e) {
	//			throw new FCException("Error building list of Account names/codes", e);
	//		}
	//	}
	/**
	 * @param vJdbc
	 * @param code
	 * @param rafb
	 * @throws FCException
	 */
	private void lookupAccount(VikingJDBC vJdbc, String code,
			RevenueAccountFormBean rafb) throws FCException {
		try {
			if (!code.equals(ForwardConstants.EMPTY)) {
				ResultSet rs = vJdbc.getAccountDetails(code);
				if (rs.next()) {
					rafb.setAccountDeputy(rs.getString("account_deputy"));
					rafb.setAccountHolder(rs.getString("account_holder"));
					String s = rs.getString("fcgroup_customer");
					s = (s == null ? ForwardConstants.EMPTY : s);
					rafb.setFcGroup(s.trim().toUpperCase().startsWith("Y"));
					rafb.setAddress1(rs.getString("address1"));
					rafb.setAddress2(rs.getString("address2"));
					rafb.setAddress3(rs.getString("address3"));
					rafb.setAddress4(rs.getString("address4"));
					rafb.setAddress5(rs.getString("address5"));
					rafb.setAtol(rs.getString("atol"));
					rafb.setCode(code);
					rafb.setComments(rs.getString("comments"));
					rafb.setContactName(rs.getString("contact"));
					rafb.setCountryCode(rs.getString("country_code"));
					rafb.setFlightCode1(rs.getString("flight_code1"));
					rafb.setFlightCode2(rs.getString("flight_code2"));
					rafb.setFlightCode3(rs.getString("flight_code3"));
					rafb.setFlightCode4(rs.getString("flight_code4"));
					rafb.setGeneralEmail(rs.getString("general_email"));
					rafb.setInformDelay(rs.getString("inform_of_delay_ind"));
					rafb.setInformLate(rs.getString("during_midnight_ind"));
					rafb.setName(rs.getString("customer_name"));
					rafb.setOfficeFax(rs.getString("office_fax"));
					rafb.setOfficePhone(rs.getString("office_phone"));
					rafb.setOooContact1(rs.getString("out_of_office_contact1"));
					rafb.setOooContact2(rs.getString("out_of_office_contact2"));
					rafb.setOooPhone1(rs.getString("out_of_office_phone1"));
					rafb.setOooPhone2(rs.getString("out_of_office_phone2"));
					rafb.setOpsEmail(rs.getString("ops_email"));
					rafb.setPostCode(rs.getString("postcode"));
				}
				Vector v = new Vector();
				rs = vJdbc.getAccountContracts(code, ForwardConstants.EMPTY);
				while (rs.next()) {
					ContractDetailsFormBean cdfb = new ContractDetailsFormBean();
					cdfb.setCode(code);
					cdfb.setContractID(rs.getString("contract_id").trim());
					cdfb.setApdI(rs.getString("apd_i_ind"));
					cdfb.setApdPct(rs.getString("apd_per"));
					cdfb.setApdR(rs.getString("apd_r_ind"));
					cdfb.setCurrencyFixed(rs.getString("currency_ind"));
					cdfb.setEuroFixed(rs.getString("euro_ind"));
					cdfb.setFuelFixed(rs.getString("fuel_ind"));
					cdfb.setOsTaxesInI(rs.getString("os_taxes_in_i_ind"));
					cdfb.setOsTaxesInR(rs.getString("os_taxes_in_r_ind"));
					cdfb.setOsTaxesInPct(rs.getString("os_taxes_in_per"));
					cdfb.setOsTaxesOutI(rs.getString("os_taxes_out_i_ind"));
					cdfb.setOsTaxesOutR(rs.getString("os_taxes_out_r_ind"));
					cdfb.setOsTaxesOutPct(rs.getString("os_taxes_out_per"));
					cdfb.setPaymentTerms(rs.getString("payment_terms_text"));
					cdfb.setPilI(rs.getString("pil_i_ind"));
					cdfb.setPilPct(rs.getString("pil_per"));
					cdfb.setPilR(rs.getString("pil_r_ind"));
					cdfb.setSeason(rs.getString("season"));
					cdfb.setUkTaxesInI(rs.getString("uk_taxes_in_i_ind"));
					cdfb.setUkTaxesInR(rs.getString("uk_taxes_in_r_ind"));
					cdfb.setUkTaxesInPct(rs.getString("uk_taxes_in_per"));
					cdfb.setUkTaxesOutI(rs.getString("uk_taxes_out_i_ind"));
					cdfb.setUkTaxesOutR(rs.getString("uk_taxes_out_r_ind"));
					cdfb.setUkTaxesOutPct(rs.getString("uk_taxes_out_per"));
					cdfb.setContractType(rs.getString("contract_type_code"));
					v.add(cdfb);
				}
				rafb.setAllContracts(v);
				vJdbc.closeResultSet(rs);
			}
		} catch (SQLException e) {
			throw new FCException("Error retrieving RevenueAccount# " + code, e);
		}
	}

	/**
	 * @param vJdbc
	 * @param rafb
	 * @param request
	 * @throws FCException
	 */
	private void saveAccount(VikingJDBC vJdbc, RevenueAccountFormBean rafb,
			HttpServletRequest request) throws FCException {

		String error = vJdbc.addUpdateAccountDetails(rafb.getSaveType().equals(
				ForwardConstants.ADD_ACTION), rafb.getCode(), rafb.getName(),
				rafb.getContactName(), rafb.getAddress1(), rafb.getAddress2(),
				rafb.getAddress3(), rafb.getAddress4(), rafb.getAddress5(),
				rafb.getPostCode(), rafb.getCountryCode(), rafb
						.getOfficePhone(), rafb.getOooContact1(), rafb
						.getOooContact2(), rafb.getOooPhone1(), rafb
						.getOooPhone2(), rafb.getOpsEmail(), rafb
						.getGeneralEmail(), rafb.getAtol(),
				rafb.getOfficeFax(), rafb.getInformDelay(), rafb
						.getInformLate(), rafb.getFlightCode1(), rafb
						.getFlightCode2(), rafb.getFlightCode3(), rafb
						.getFlightCode4(), rafb.getAccountHolder(), rafb
						.getAccountDeputy(), rafb.isFcGroup(), rafb
						.getComments(), request.getRemoteUser());
		rafb.setError(error);
	}

}