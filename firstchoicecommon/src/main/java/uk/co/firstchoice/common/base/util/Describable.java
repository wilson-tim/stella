package uk.co.firstchoice.common.base.util;

/**
 * Specification for making objects capable of providing description and state
 * information.
 *  
 */
public interface Describable {

	/**
	 * Provides a textual version of description and state.
	 *  
	 */
	public String describe();

}