/*
 * CommentHolderBean.java
 *
 * Created on 04 December 2003, 15:47
 */

package uk.co.firstchoice.viking.gui;

/**
 * 
 * @author Lars Svensson
 */
public class CommentHolderBean {

	private String date;

	private String user;

	private String amendmentType;

	private String comments;

	/** Creates a new instance of CommentHolderBean */
	public CommentHolderBean() {
	}

	/**
	 * @param date
	 * @param user
	 * @param amendmentType
	 * @param comments
	 */
	public CommentHolderBean(String date, String user, String amendmentType,
			String comments) {
		this.date = date;
		this.user = user;
		this.amendmentType = amendmentType;
		this.comments = comments;
	}

	/**
	 * Getter for property amendmentType.
	 * 
	 * @return Value of property amendmentType.
	 *  
	 */
	public java.lang.String getAmendmentType() {
		return amendmentType;
	}

	/**
	 * Setter for property amendmentType.
	 * 
	 * @param amendmentType
	 *            New value of property amendmentType.
	 *  
	 */
	public void setAmendmentType(java.lang.String amendmentType) {
		this.amendmentType = amendmentType;
	}

	/**
	 * Getter for property comments.
	 * 
	 * @return Value of property comments.
	 *  
	 */
	public java.lang.String getComments() {
		return comments;
	}

	/**
	 * Setter for property comments.
	 * 
	 * @param comments
	 *            New value of property comments.
	 *  
	 */
	public void setComments(java.lang.String comments) {
		this.comments = comments;
	}

	/**
	 * Getter for property date.
	 * 
	 * @return Value of property date.
	 *  
	 */
	public java.lang.String getDate() {
		return date;
	}

	/**
	 * Setter for property date.
	 * 
	 * @param date
	 *            New value of property date.
	 *  
	 */
	public void setDate(java.lang.String date) {
		this.date = date;
	}

	/**
	 * Getter for property user.
	 * 
	 * @return Value of property user.
	 *  
	 */
	public java.lang.String getUser() {
		return user;
	}

	/**
	 * Setter for property user.
	 * 
	 * @param user
	 *            New value of property user.
	 *  
	 */
	public void setUser(java.lang.String user) {
		this.user = user;
	}

}