package uk.co.firstchoice.viking.gui;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import uk.co.firstchoice.viking.gui.util.ForwardConstants;

/**
 * Form Bean to hold the values for the VersionMaint jsp form. Have getter and
 * setter methods for all values. Is automatically populated by Struts (see
 * Struts configuration)
 */
public final class VersionFormBean extends ActionForm {

	private String action = ForwardConstants.EMPTY; //create, copy, delete

	private String error = ForwardConstants.EMPTY;

	private String liveSeason = ForwardConstants.EMPTY;

	private String liveVersion = ForwardConstants.EMPTY;

	private String defaultSeason = ForwardConstants.EMPTY;

	private String defaultVersion = ForwardConstants.EMPTY;

	private String newSeason = ForwardConstants.EMPTY;

	private String newVersion = ForwardConstants.EMPTY;

	private String copyFromSeason = ForwardConstants.EMPTY;

	private String copyFromVersion = ForwardConstants.EMPTY;

	private String copyToSeason = ForwardConstants.EMPTY;

	private String copyToVersion = ForwardConstants.EMPTY;

	private String renameFromSeason = ForwardConstants.EMPTY;

	private String renameFromVersion = ForwardConstants.EMPTY;

	private String renameToSeason = ForwardConstants.EMPTY;

	private String renameToVersion = ForwardConstants.EMPTY;

	private String deleteSeason = ForwardConstants.EMPTY;

	private String deleteVersion = ForwardConstants.EMPTY;

	/**
	 * Reset all properties to their default values.
	 * 
	 * @@param mapping
	 *            The mapping used to select this instance
	 * @@param request
	 *            The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		action = ForwardConstants.EMPTY; //create, copy, delete
		error = ForwardConstants.EMPTY;
		defaultSeason = ForwardConstants.EMPTY;
		defaultVersion = ForwardConstants.EMPTY;
		newSeason = ForwardConstants.EMPTY;
		newVersion = ForwardConstants.EMPTY;
		copyFromSeason = ForwardConstants.EMPTY;
		copyFromVersion = ForwardConstants.EMPTY;
		copyToSeason = ForwardConstants.EMPTY;
		copyToVersion = ForwardConstants.EMPTY;
		renameFromSeason = ForwardConstants.EMPTY;
		renameFromVersion = ForwardConstants.EMPTY;
		renameToSeason = ForwardConstants.EMPTY;
		renameToVersion = ForwardConstants.EMPTY;
		deleteSeason = ForwardConstants.EMPTY;
		deleteVersion = ForwardConstants.EMPTY;
	}

	/**
	 * Getter for property action.
	 * 
	 * @@return Value of property action.
	 *  
	 */
	public java.lang.String getAction() {
		return action;
	}

	/**
	 * Setter for property action.
	 * 
	 * @@param action
	 *            New value of property action.
	 *  
	 */
	public void setAction(java.lang.String action) {
		this.action = action;
	}

	/**
	 * Getter for property deleteSeason.
	 * 
	 * @@return Value of property deleteSeason.
	 *  
	 */
	public java.lang.String getDeleteSeason() {
		return deleteSeason;
	}

	/**
	 * Setter for property deleteSeason.
	 * 
	 * @@param deleteSeason
	 *            New value of property deleteSeason.
	 *  
	 */
	public void setDeleteSeason(java.lang.String deleteSeason) {
		this.deleteSeason = deleteSeason;
	}

	/**
	 * Getter for property deleteVersion.
	 * 
	 * @@return Value of property deleteVersion.
	 *  
	 */
	public java.lang.String getDeleteVersion() {
		return deleteVersion;
	}

	/**
	 * Setter for property deleteVersion.
	 * 
	 * @@param deleteVersion
	 *            New value of property deleteVersion.
	 *  
	 */
	public void setDeleteVersion(java.lang.String deleteVersion) {
		this.deleteVersion = deleteVersion;
	}

	/**
	 * Getter for property fromSeason.
	 * 
	 * @@return Value of property fromSeason.
	 *  
	 */
	public java.lang.String getCopyFromSeason() {
		return copyFromSeason;
	}

	/**
	 * Setter for property fromSeason.
	 * 
	 * @@param fromSeason
	 *            New value of property fromSeason.
	 *  
	 */
	public void setCopyFromSeason(java.lang.String copyFromSeason) {
		this.copyFromSeason = copyFromSeason;
	}

	/**
	 * Getter for property fromVersion.
	 * 
	 * @@return Value of property fromVersion.
	 *  
	 */
	public java.lang.String getCopyFromVersion() {
		return copyFromVersion;
	}

	/**
	 * Setter for property fromVersion.
	 * 
	 * @@param fromVersion
	 *            New value of property fromVersion.
	 *  
	 */
	public void setCopyFromVersion(java.lang.String copyFromVersion) {
		this.copyFromVersion = copyFromVersion;
	}

	/**
	 * Getter for property newSeason.
	 * 
	 * @@return Value of property newSeason.
	 *  
	 */
	public java.lang.String getNewSeason() {
		return newSeason;
	}

	/**
	 * Setter for property newSeason.
	 * 
	 * @@param newSeason
	 *            New value of property newSeason.
	 *  
	 */
	public void setNewSeason(java.lang.String newSeason) {
		this.newSeason = newSeason;
	}

	/**
	 * Getter for property newVersion.
	 * 
	 * @@return Value of property newVersion.
	 *  
	 */
	public java.lang.String getNewVersion() {
		return newVersion;
	}

	/**
	 * Setter for property newVersion.
	 * 
	 * @@param newVersion
	 *            New value of property newVersion.
	 *  
	 */
	public void setNewVersion(java.lang.String newVersion) {
		this.newVersion = newVersion;
	}

	/**
	 * Getter for property toSeason.
	 * 
	 * @@return Value of property toSeason.
	 *  
	 */
	public java.lang.String getCopyToSeason() {
		return copyToSeason;
	}

	/**
	 * Setter for property copyToSeason.
	 * 
	 * @@param copyToSeason
	 *            New value of property copyToSeason.
	 *  
	 */
	public void setCopyToSeason(java.lang.String copyToSeason) {
		this.copyToSeason = copyToSeason;
	}

	/**
	 * Getter for property toVersion.
	 * 
	 * @@return Value of property toVersion.
	 *  
	 */
	public java.lang.String getCopyToVersion() {
		return copyToVersion;
	}

	/**
	 * Setter for property toVersion.
	 * 
	 * @@param toVersion
	 *            New value of property toVersion.
	 *  
	 */
	public void setCopyToVersion(java.lang.String copyToVersion) {
		this.copyToVersion = copyToVersion;
	}

	/**
	 * Getter for property error.
	 * 
	 * @@return Value of property error.
	 *  
	 */
	public java.lang.String getError() {
		return error;
	}

	/**
	 * Setter for property error.
	 * 
	 * @@param error
	 *            New value of property error.
	 *  
	 */
	public void setError(java.lang.String error) {
		this.error = error;
	}

	/**
	 * Getter for property renameFromSeason.
	 * 
	 * @@return Value of property renameFromSeason.
	 *  
	 */
	public java.lang.String getRenameFromSeason() {
		return renameFromSeason;
	}

	/**
	 * Setter for property renameFromSeason.
	 * 
	 * @@param renameFromSeason
	 *            New value of property renameFromSeason.
	 *  
	 */
	public void setRenameFromSeason(java.lang.String renameFromSeason) {
		this.renameFromSeason = renameFromSeason;
	}

	/**
	 * Getter for property renameFromVersion.
	 * 
	 * @@return Value of property renameFromVersion.
	 *  
	 */
	public java.lang.String getRenameFromVersion() {
		return renameFromVersion;
	}

	/**
	 * Setter for property renameFromVersion.
	 * 
	 * @@param renameFromVersion
	 *            New value of property renameFromVersion.
	 *  
	 */
	public void setRenameFromVersion(java.lang.String renameFromVersion) {
		this.renameFromVersion = renameFromVersion;
	}

	/**
	 * Getter for property renameToSeason.
	 * 
	 * @@return Value of property renameToSeason.
	 *  
	 */
	public java.lang.String getRenameToSeason() {
		return renameToSeason;
	}

	/**
	 * Setter for property renameToSeason.
	 * 
	 * @@param renameToSeason
	 *            New value of property renameToSeason.
	 *  
	 */
	public void setRenameToSeason(java.lang.String renameToSeason) {
		this.renameToSeason = renameToSeason;
	}

	/**
	 * Getter for property renameToVersion.
	 * 
	 * @@return Value of property renameToVersion.
	 *  
	 */
	public java.lang.String getRenameToVersion() {
		return renameToVersion;
	}

	/**
	 * Setter for property renameToVersion.
	 * 
	 * @@param renameToVersion
	 *            New value of property renameToVersion.
	 *  
	 */
	public void setRenameToVersion(java.lang.String renameToVersion) {
		this.renameToVersion = renameToVersion;
	}

	/**
	 * Getter for property defaultSeason.
	 * 
	 * @@return Value of property defaultSeason.
	 *  
	 */
	public java.lang.String getDefaultSeason() {
		return defaultSeason;
	}

	/**
	 * Setter for property defaultSeason.
	 * 
	 * @@param defaultSeason
	 *            New value of property defaultSeason.
	 *  
	 */
	public void setDefaultSeason(java.lang.String defaultSeason) {
		this.defaultSeason = defaultSeason;
	}

	/**
	 * Getter for property defaultVersion.
	 * 
	 * @@return Value of property defaultVersion.
	 *  
	 */
	public java.lang.String getDefaultVersion() {
		return defaultVersion;
	}

	/**
	 * Setter for property defaultVersion.
	 * 
	 * @@param defaultVersion
	 *            New value of property defaultVersion.
	 *  
	 */
	public void setDefaultVersion(java.lang.String defaultVersion) {
		this.defaultVersion = defaultVersion;
	}

	/**
	 * Getter for property liveSeason.
	 * 
	 * @@return Value of property liveSeason.
	 */
	public java.lang.String getLiveSeason() {
		return liveSeason;
	}

	/**
	 * Setter for property liveSeason.
	 * 
	 * @@param liveSeason
	 *            New value of property liveSeason.
	 */
	public void setLiveSeason(java.lang.String liveSeason) {
		this.liveSeason = liveSeason;
	}

	/**
	 * Getter for property liveVersion.
	 * 
	 * @@return Value of property liveVersion.
	 */
	public java.lang.String getLiveVersion() {
		return liveVersion;
	}

	/**
	 * Setter for property liveVersion.
	 * 
	 * @@param liveVersion
	 *            New value of property liveVersion.
	 */
	public void setLiveVersion(java.lang.String liveVersion) {
		this.liveVersion = liveVersion;
	}

}