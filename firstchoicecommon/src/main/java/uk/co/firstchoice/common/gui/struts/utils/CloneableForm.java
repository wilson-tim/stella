/**
 * File Name : CloneableForm.java
 * Created on 14-Jan-2005
 *
 * @author Dwood
 *
 */
package uk.co.firstchoice.common.gui.struts.utils;

import java.io.Serializable;

import org.apache.struts.action.ActionForm;

import uk.co.firstchoice.common.base.logging.LogManager;
import uk.co.firstchoice.common.base.logging.Logger;

/**
 * Object to allow forms to be cloned accross multiple server intances
 */
public class CloneableForm extends ActionForm implements ClonebleFormInterface,
		Cloneable, Serializable {

	/**
	 * The logger used to log tracing messages to the log file.
	 */
	private static Logger logger = LogManager.getLogger();

	/**
	 * serialised object idetifier, used for comparing objects from diffrent
	 * JVM's
	 */
	public static String serialVersionUID = null;

	/**
	 * static string to denote if the struts form has been posted to or not
	 */
	public static final String POST_VALUE = "posted";

	/**
	 * string to store state if form has been posted to or not
	 */
	protected String postState = "notPostedYet";

	private String formId = null;

	/**
	 * Method to clone a CloneableForm. Try to use the clone method, if it fails
	 * throw a RuntimeException.
	 *
	 * @return CloneableForm a copy of the form
	 * @throws RuntimeException
	 */
	public CloneableForm createClone() {
		try {
			return (CloneableForm) clone();
		} catch (CloneNotSupportedException cnse) {
			logger.log(Logger.ERROR, cnse);
			throw new RuntimeException(
					"Unable to continue because the CloneableForm does not support cloning!");
		}
	}

	/**
	 * @return Returns the formId.
	 */
	public String getFormId() {
		return formId;
	}

	/**
	 * @param formId
	 *            String
	 */
	public void setFormId(String formId) {
		this.formId = formId;
	}

	/**
	 * @return String
	 */
	public String getPostState() {
		return postState;
	}

	/**
	 * @param postState
	 *            String
	 */
	public void setPostState(final String postState) {
		this.postState = postState;
	}
}

