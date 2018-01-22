package uk.co.firstchoice.stella.frontend;

import java.util.Vector;

public class SearchResultCollectionBean {

	private Vector foundDocuments = null;

	private long grandTotal = 0;

	public SearchResultCollectionBean() {
		foundDocuments = new Vector();
	}

	public Vector getFoundDocuments() {
		return foundDocuments;
	}

	public String getGrandTotal() {
		return StellaUtils.floatToString(grandTotal / 100f);
	}

	public float getGrandTotalFloat() {
		return (grandTotal / 100f);
	}

	public void addToGrandTotal(float amt) {
		grandTotal += Math.round(amt * 100f);
	}
}