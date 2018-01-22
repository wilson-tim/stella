/*
 * MirRecord.java
 *
 * Created on 19 December 2002, 13:18
 */

package uk.co.firstchoice.stella;

/**
 * object definition to store a record from mir input file
 * 
 * @author leigh ashton
 */
public class MirRecord {

	/** record identifier (first x bytes of record in file) */
	public String recordID = "";

	/** whole line of text from file */
	public String recordText = "";

	/** Creates a new instance of Class */
	public MirRecord() {
	}

}