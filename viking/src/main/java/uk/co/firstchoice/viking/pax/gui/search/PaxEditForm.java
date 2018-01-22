/*
 * Created on 08-Mar-2005
 * Filename: PaxEditForm.java
 */
package uk.co.firstchoice.viking.pax.gui.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import uk.co.firstchoice.common.gui.struts.util.CloneableForm;
import uk.co.firstchoice.viking.pax.gui.utils.StringConstants;

/**
 * @author Dwood
 * Form object for Viking pax editor
 */
public class PaxEditForm  extends CloneableForm implements Serializable {

	private String flightNumber = null;
		
	private List paxItemArray = new ArrayList();

	private boolean saveStatus = false;
	
	
	/**
	 * Default constructor, sets up Pax Item list
	 */
	public PaxEditForm() {
		final List paxList = new ArrayList();
		for (int count = 0 ; count < 10; count++){
			final PaxItemBean paxBean = new PaxItemBean();
			paxList.add(paxBean);
		}
		this.setPaxItemArray(paxList);
	}
	
	/**
	 * @return Returns the paxItemArray.
	 */
	public List getPaxItemArray() {
		return paxItemArray;
	}	
	
	/**
	 * @param paxItemArray The paxItemArray to set.
	 */
	public void setPaxItemArray(final List paxItemArray) {
		this.paxItemArray = paxItemArray;
	}
	
	
	/**
	 * @param index int
	 * @return Returns the paxItemArray.
	 */
	public PaxItemBean getPaxItem(final int index) {
		while (this.paxItemArray.size() <= index ) {
			this.paxItemArray.add(new PaxItemBean());
		}		
		return (PaxItemBean) this.paxItemArray.get(index);
	}

	/**
	 * @return Returns the flightNumber.
	 */
	public String getFlightNumber() {
		return this.flightNumber;
	}
	/**
	 * @param flightNumber The flightNumber to set.
	 * Note: This method sets the flight number of each PaxItemBean in the form
	 * as this value is stored on a per item basis and is only referenced 
	 * in the form object for ease of access.
	 */
	public void setFlightNumber(final String flightNumber) {
		if ( flightNumber != null && paxItemArray != null ) {
			this.flightNumber = flightNumber;
			for (int paxCounter = 0; paxCounter < this.paxItemArray.size(); paxCounter++ ){
				PaxItemBean paxItem = (PaxItemBean) this.paxItemArray.get(paxCounter);
				if ( paxItem == null ) {
					paxItem = new PaxItemBean();
				}
				paxItem.setFlightNumber(flightNumber);
				this.paxItemArray.set(paxCounter,paxItem);
			}
		}
	}

	
	/**
	 * form method to save any error messages, so that 
	 * they can be displayed to the end user.
	 * 
	 * @param mapping ActionMapping
	 * @param request HttpServletRequest
	 * @return HttpServletRequest
	 */
	public ActionMessages saveError(final ActionMapping mapping,
			final HttpServletRequest request) {
		ActionMessages errors = new ActionMessages();

		errors.add("SAVE_ERROR", new ActionMessage(StringConstants.SAVE_ERROR));
		return errors;
	}
	/**
	 * @return Returns the saveStatus.
	 */
	public boolean isSaveStatus() {
		return this.saveStatus;
	}
	/**
	 * @param saveStatus The saveStatus to set.
	 */
	public void setSaveStatus(final boolean saveStatus) {
		this.saveStatus = saveStatus;
	}

	/**
	 * @param mapping ActionMapping
	 * @param ActionMapping ServletRequest
	 */
	public void reset(final ActionMapping mapping, final ServletRequest request) {
		this.flightNumber = null;;
		this.paxItemArray = new ArrayList();
		this.paxItemArray.add(new PaxItemBean());
		
		this.saveStatus = false;
	}
}