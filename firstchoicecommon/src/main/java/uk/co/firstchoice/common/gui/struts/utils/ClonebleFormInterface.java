/*
 * Created on 27-Jan-2005
 *
 * Base First Choice interface for implemented struts ActionForm objects.
 */
package uk.co.firstchoice.common.gui.struts.utils;

/**
 * @author Dwood
 * 
 * Base First Choice interface for implemented struts ActionForm objects.
 */
public interface ClonebleFormInterface {
	/**
	 * Method to clone a CloneableForm. Try to use the clone method, if it fails
	 * throw a RuntimeException.
	 * 
	 * @return CloneableForm a copy of the form
	 * @throws RuntimeException
	 *             if the clone fails
	 */
	public abstract CloneableForm createClone();

	/**
	 * @return Returns the formId.
	 */
	public abstract String getFormId();

	/**
	 * @param formId
	 *            The formId to set.
	 * @return Returns the formId.
	 */
	public abstract void setFormId(String formId);

	/**
	 * @return Returns the postState.
	 */
	public abstract String getPostState();

	/**
	 * @param postState
	 *            The postState to set.
	 * @return Returns the postState.
	 */
	public abstract void setPostState(String postState);
}