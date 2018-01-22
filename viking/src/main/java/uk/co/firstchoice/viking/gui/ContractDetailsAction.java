package uk.co.firstchoice.viking.gui;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.co.firstchoice.fcutil.FCException;
import uk.co.firstchoice.viking.gui.util.ForwardConstants;
import uk.co.firstchoice.viking.persist.VikingJDBC;

public class ContractDetailsAction extends Action {

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

		ContractDetailsFormBean cdfb = (ContractDetailsFormBean) form;
		String action = cdfb.getAction();
		ActionForward nextAction = mapping
				.findForward(ForwardConstants.EDIT_ACCOUNT);
		//System.out.println("ContractDetailsActionForm.execute:
		// Action="+action);
		if (action.equals(ForwardConstants.EDIT_ACTION)) {
			getContract(cdfb);
		} else if (action.equals(ForwardConstants.SAVE_ACTION)) {
			saveContract(cdfb, request.getRemoteUser());
			if (cdfb.getError().equals(ForwardConstants.EMPTY)) {
				nextAction = mapping
						.findForward(ForwardConstants.RELOAD_PARENT_ACTION);
			}
		}
		return nextAction;
	}

	private void getContract(ContractDetailsFormBean cdfb) throws FCException {
		VikingJDBC vJdbc = new VikingJDBC();
		try {
			//System.out.println("Searching for: Code: >"+cdfb.getCode()+"< ID:
			// >"+cdfb.getContractID()+"<");
			ResultSet rs = vJdbc.getAccountContracts(cdfb.getCode(), cdfb
					.getContractID());
			if (rs.next()) {
				cdfb.setApdI(rs.getString("apd_i_ind"));
				cdfb.setApdPct(rs.getString("apd_per"));
				cdfb.setApdR(rs.getString("apd_r_ind"));
				cdfb.setCurrencyFixed(rs.getString("currency_ind"));
				cdfb.setContractType(rs.getString("contract_type_code"));
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
			} else {
				cdfb.setError("Contract# " + cdfb.getContractID()
						+ " not found");
			}
			vJdbc.closeResultSet(rs);
		} catch (SQLException e) {
			throw new FCException("SQL Error retriving AccountContract "
					+ cdfb.getContractID(), e);
		}
		vJdbc.close();
	}

	private void saveContract(ContractDetailsFormBean cdfb, String userID)
			throws FCException {

		VikingJDBC vJdbc = new VikingJDBC();
		String error = vJdbc.addUpdateAccountContract(cdfb.getFromAction()
				.equals(ForwardConstants.ADD_ACTION), cdfb.getContractID(),
				cdfb.getCode(), cdfb.getContractType(), cdfb.getFuelFixed(),
				cdfb.getCurrencyFixed(), cdfb.getEuroFixed(), cdfb.getApdR(),
				cdfb.getApdI(), cdfb.getApdPct(), cdfb.getOsTaxesInR(), cdfb
						.getOsTaxesInI(), cdfb.getOsTaxesInPct(), cdfb
						.getOsTaxesOutR(), cdfb.getOsTaxesOutI(), cdfb
						.getOsTaxesOutPct(), cdfb.getPilR(), cdfb.getPilI(),
				cdfb.getPilPct(), cdfb.getSeason(), cdfb.getUkTaxesInR(), cdfb
						.getUkTaxesInI(), cdfb.getUkTaxesInPct(), cdfb
						.getUkTaxesOutR(), cdfb.getUkTaxesOutI(), cdfb
						.getUkTaxesOutPct(), cdfb.getPaymentTerms(), userID);
		cdfb.setError(error);
		vJdbc.close();
	}

}