package uk.co.firstchoice.stella;

/**
 * object definition to store a record from bsp input file
 * 
 * @author leigh ashton Created on 19 December 2002, 13:18
 * @version 1.0
 */
public class BSPRecord {

	/** record identifier (first x bytes of record in file) */
	public String recordID = "";

	/** whole line of text from file */
	public String recordText = "";

	/** transaction sequence within file */
	public int transSeq = 0;

	/** BKS record type -- only used for BKS type records */
	public String bksType = "";

	/** only populated on BKS records */
	public long ticketNo = 0;

	/** Creates a new instance of Class */
	public BSPRecord() {
	}

}