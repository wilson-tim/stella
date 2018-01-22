package uk.co.firstchoice.util.businessrules;

/** general number crunching / validating / parsing stuff */
public class NumberProcessor {

	/**
	 * validate that string is numeric in content
	 * 
	 * @param inString
	 *            string of number to be parsed
	 * @return true if valid, false if not
	 */

	public static boolean validateStringAsNumber(String inString) {
		try {
			Double x = new Double(inString);
		} catch (NumberFormatException nfe) {
			return false;
		}

		return true;
	}

} // end class
