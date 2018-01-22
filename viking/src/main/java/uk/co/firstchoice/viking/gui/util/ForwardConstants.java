/*
 * Constants.java
 *
 * Created on 29 September 2003, 10:09
 */

package uk.co.firstchoice.viking.gui.util;

/**
 * 
 * @author Lars Svensson
 */
public class ForwardConstants {

	public static final String EMPTY = "";

	public static final String VIEW_ACTION = "view";

	public static final String EDIT_ACTION = "edit";

	public static final String SAVE_ACTION = "save";

	public static final String ADD_ACTION = "add";

	public static final String RELOAD_ACTION = "reload";

	public static final String RELOAD_PARENT_ACTION = "reloadparent";

	public static final String DELETE_ACTION = "delete";

	public static final String DELETE_SERIES_ACTION = "deleteSeries";

	public static final String ALL_ACCOUNTS = "allAccounts";

	public static final String ALL_FLIGHTCODES = "allFlightCodes";

	public static final String ALL_GATEWAYCODES = "allGatewayCodes";
	
	public static final String DISPLAY_WARNING = "display_warning"; 

	public static final String SUCCESS = "success";

	public static final String EDIT_ACCOUNT = "editaccount";

	public static final String RECORD_ADD = "I";

	public static final String RECORD_EDIT = "E";

	public static final String RECORD_CANCEL = "C";

	public static final String RECORD_DELETE = "D";

	public static final String SEARCH = "!!!";

	public static final String MULTILINE_SEPERATOR = ";";

	public static final String BLANK_CC_CODE = ";0.00";

	public static final String SESSION_DATA = "sessiondata";

	public static final String VIKING_MANAGER = "VIKING_MANAGER";

	public static final String VIKING_EDITOR = "VIKING_EDITOR";
	
	
	/**
	 * Pax related forward keys
	 *  
	 */
	public static final String PAX_RESULTS = "results";

	/** Private so as to prevent creation of instance of Constants */
	private ForwardConstants() {
	}

}