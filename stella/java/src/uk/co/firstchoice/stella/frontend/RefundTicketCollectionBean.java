package uk.co.firstchoice.stella.frontend;

import java.util.Vector;

public class RefundTicketCollectionBean {

	private Vector refundTickets = null;

	private int editingLineNo = -1;

	private float documentTotal = 0;

	private String airlineNum = "";

	private String airlineName = "";

	private boolean viewed = false; //Is this refund ticket being viewed

	// instead of edited

	private boolean edited = false; //Is this refund being edited as opposed to

	// created?

	private boolean newRefund = true; // true = new refund, false = edited old

	// one

	public RefundTicketCollectionBean() {
		refundTickets = new Vector();
	}

	public Vector getRefundTickets() {
		return refundTickets;
	}

	public RefundTicket getEditedTicket() {
		if (editingLineNo == -1 || editingLineNo > refundTickets.size())
			return null;
		else
			return (RefundTicket) refundTickets.elementAt(editingLineNo);
	}

	public void setEditingLineNo(int value) {
		editingLineNo = value;
	}

	public int getEditingLineNo() {
		return editingLineNo;
	}

	public String getDocumentTotal() {
		return StellaUtils.floatToString(documentTotal);
	}

	public float getDocumentTotalFloat() {
		return documentTotal;
	}

	public void setDocumentTotal(String value) throws StellaException {
		documentTotal = StellaUtils.stringToFloat(value);
	}

	public void setDocumentTotal(float value) {
		documentTotal = value;
	}

	public String getAirlineNum() {
		return airlineNum;
	}

	public void setAirlineNum(String an) {
		airlineNum = an;
	}

	public String getAirlineName() {
		return airlineName;
	}

	public void setAirlineName(String an) {
		airlineName = an;
	}

	/**
	 * Getter for property edited.
	 * 
	 * @return Value of property edited.
	 *  
	 */
	public boolean isEdited() {
		return edited;
	}

	/**
	 * Setter for property edited.
	 * 
	 * @param edited
	 *            New value of property edited.
	 *  
	 */
	public void setEdited(boolean edited) {
		this.edited = edited;
	}

	/**
	 * Getter for property newRefund.
	 * 
	 * @return Value of property newRefund.
	 *  
	 */
	public boolean isNewRefund() {
		return newRefund;
	}

	/**
	 * Setter for property newRefund.
	 * 
	 * @param newRefund
	 *            New value of property newRefund.
	 *  
	 */
	public void setNewRefund(boolean newRefund) {
		this.newRefund = newRefund;
	}

	/**
	 * Getter for property viewed.
	 * 
	 * @return Value of property viewed.
	 *  
	 */
	public boolean isViewed() {
		return viewed;
	}

	/**
	 * Setter for property viewed.
	 * 
	 * @param viewed
	 *            New value of property viewed.
	 *  
	 */
	public void setViewed(boolean viewed) {
		this.viewed = viewed;
	}

}