/*
 * DataCollectionBean.java
 *
 * Created on 16 May 2003, 17:25
 */

package uk.co.firstchoice.genericmaint.frontend;

import java.util.Vector;

/**
 *
 * @author  Lsvensson
 */
public class DataCollectionBean {
	
	private Vector metaData = null;
	private Vector rows = null;
	
	/** Creates a new instance of DataCollectionBean */
	public DataCollectionBean() {
		metaData = new Vector();
		rows = new Vector();
	}
	
	/** Getter for property metaData.
	 * @return Value of property metaData.
	 *
	 */
	public Vector getMetaData() {
		return metaData;
	}
	
	/** Getter for property rows.
	 * @return Value of property rows.
	 *
	 */
	public Vector getRows() {
		return rows;
	}
	
}
