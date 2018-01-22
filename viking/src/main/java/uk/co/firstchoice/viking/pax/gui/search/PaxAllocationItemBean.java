/*
 * Created on 06-Mar-2005
 * Filename: PaxAllocationItemBean.java
 */
package uk.co.firstchoice.viking.pax.gui.search;


/**
 * @author Dwood
 *
 * Bean to represent pax data for a given allocation id
 */
public class PaxAllocationItemBean {

	private int allocationId = 0;

	private int paxId = 0;

	private int inAdults = 0;

	private int inChildren = 0;

	private int inInfants = 0;

	private String allocationType = null;

	private int paxStatus = 0;

	private String departureGateFrom = null;

	private String departureGateTo = null;


	// previous fields - used in form validation only
	private int inAdultsPrev = 0;

	private int inChildrenPrev = 0;

	private int inInfantsPrev = 0;


	/**
	 * @return Returns the allocationId.
	 */
	public int getAllocationId() {
		return this.allocationId;
	}
	/**
	 * @param allocationId The allocationId to set.
	 */
	public void setAllocationId(final int allocationId) {
		this.allocationId = allocationId;
	}

	/**
	 * @return Returns the paxStatus.
	 */
	public int getPaxStatus() {
		return paxStatus;
	}
	/**
	 * @param paxStatus The paxStatus to set.
	 */
	public void setPaxStatus(final int paxStatus) {
		this.paxStatus = paxStatus;
	}
	/**
	 * @return Returns the allocationType.
	 */
	public String getAllocationType() {
		return this.allocationType;
	}
	/**
	 * @param allocationType The allocationType to set.
	 */
	public void setAllocationType(String allocationType) {
		this.allocationType = allocationType;
	}

	/**
	 * @return Returns the inAdults.
	 */
	public int getInAdults() {
		return this.inAdults;
	}
	/**
	 * @param inAdults The inAdults to set.
	 */
	public void setInAdults(final int inAdults) {
		this.inAdultsPrev = inAdults;
		this.inAdults = inAdults;
	}
	/**
	 * @return Returns the inChildren.
	 */
	public int getInChildren() {
		return this.inChildren;
	}
	/**
	 * @param inChildren The inChildren to set.
	 */
	public void setInChildren(final int inChildren) {
		this.inChildrenPrev = inChildren;
		this.inChildren = inChildren;
	}
	/**
	 * @return Returns the inInfants.
	 */
	public int getInInfants() {
		return this.inInfants;
	}
	/**
	 * @param inInfants The inInfants to set.
	 */
	public void setInInfants(final int inInfants) {
		this.inInfantsPrev = inInfants;
		this.inInfants = inInfants;
	}
	/**
	 * @return Returns the paxId.
	 */
	public int getPaxId() {
		return this.paxId;
	}
	/**
	 * @param paxId The paxId to set.
	 */
	public void setPaxId(final int paxId) {
		this.paxId = paxId;
	}

	/**
	 * @return Returns the departureGateFrom.
	 */
	public String getDepartureGateFrom() {
		return this.departureGateFrom;
	}
	/**
	 * @param departureGateFrom The departureGateFrom to set.
	 */
	public void setDepartureGateFrom(final String departureGateFrom) {
		this.departureGateFrom = departureGateFrom;
	}
	/**
	 * @return Returns the departureGateTo.
	 */
	public String getDepartureGateTo() {
		return this.departureGateTo;
	}
	/**
	 * @param departureGateTo The departureGateTo to set.
	 */
	public void setDepartureGateTo(final String departureGateTo) {
		this.departureGateTo = departureGateTo;
	}


	/**
	 * @return Returns the inAdults.
	 */
	public int getInAdultsPrev() {
		return this.inAdultsPrev;
	}
	/**
	 * @return Returns the inChildren.
	 */
	public int getInChildrenPrev() {
		return this.inChildrenPrev;
	}
	/**
	 * @return Returns the inInfants.
	 */
	public int getInInfantsPrev() {
		return this.inInfantsPrev;
	}
	public int getTotalAllocation() {
		return (this.inAdults + this.inChildren + this.inInfants);
	}
}
