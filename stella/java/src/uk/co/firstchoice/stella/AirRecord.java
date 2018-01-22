/*
 * AirRecord.java
 *
 * Created on 12 August 2004, 10:56
 */

package uk.co.firstchoice.stella;

/**
 * object definition to store a record from air input file
 * 
 * @author Jyoti Renganathan
 */
public class AirRecord {

	/** record identifier (first 3 letters of record in file) */
	public String recordID = "";

	/** whole line of text from file */
	public String recordText = "";

	/** Creates a new instance of Class */
	public AirRecord() {
	}

}