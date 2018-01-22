package uk.co.firstchoice.common.base.util;

/**
 * A library of commonly used special characters.
 * 
 * <p>.
 */
public class CommonCharacter {

	private CommonCharacter() {
	}

	/**
	 * Convenience reference for the "line.separator" property from
	 * System.getProperties().
	 *  
	 */
	public static final String LINE_FEED = System.getProperty("line.separator");

	/**
	 * Convenience reference for the "path.separator" property from
	 * System.getProperties().
	 *  
	 */
	public static final String PATH_SEPARATOR = System
			.getProperty("path.separator");

	/**
	 * Convenience reference for the "file.separator" property from
	 * System.getProperties().
	 *  
	 */
	public static final String FILE_SEPARATOR = System
			.getProperty("file.separator");

	/**
	 * Convenience reference for the "tab" (\t) property.
	 *  
	 */
	public static final String TAB = "\t";

}