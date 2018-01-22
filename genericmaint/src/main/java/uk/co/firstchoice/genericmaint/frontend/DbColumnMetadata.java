/*
 * dbColumnMetadata.java
 *
 * Created on 19 May 2003, 16:05
 */

package uk.co.firstchoice.genericmaint.frontend;

/**
 *
 * @author  Lsvensson
 */
public class DbColumnMetadata {

	private int columnWidth;
	private int columnDisplayWidth;
	private String columnDisplayLabel = "";
	private String columnName = "";
	private int dataType;
	private int decimalPlaces;
	private boolean blankAllowed;
	private boolean primaryKey = false;		// is this column part of primary key (r/o when editing)?

	/** Creates a new instance of dbColumnMetadata */
	public DbColumnMetadata() {
	}

	/** Getter for property columnDisplayLabel.
	 * @return Value of property columnDisplayLabel.
	 *
	 */
	public String getColumnDisplayLabel() {
		return columnDisplayLabel;
	}

	/** Setter for property columnDisplayLabel.
	 * @param columnDisplayLabel New value of property columnDisplayLabel.
	 *
	 */
	public void setColumnDisplayLabel(java.lang.String columnDisplayLabel) {
		this.columnDisplayLabel = columnDisplayLabel;
	}

	/** Getter for property blankAllowed.
	 * @return Value of property blankAllowed.
	 *
	 */
	public boolean isBlankAllowed() {
		return blankAllowed;
	}

	/** Setter for property blankAllowed.
	 * @param blankAllowed New value of property blankAllowed.
	 *
	 */
	public void setBlankAllowed(boolean blankAllowed) {
		this.blankAllowed = blankAllowed;
	}

	/** Getter for property columnWidth.
	 * @return Value of property columnWidth.
	 *
	 */
	public int getColumnWidth() {
		return columnWidth;
	}

	/** Setter for property columnWidth.
	 * @param columnWidth New value of property columnWidth.
	 *
	 */
	public void setColumnWidth(int columnWidth) {
		this.columnWidth = columnWidth;
		columnDisplayWidth = new Long(Math.round(Math.ceil(1.4*columnWidth))).intValue();
		columnDisplayWidth = columnDisplayWidth>40?40:columnDisplayWidth;
	}

	/** Getter for property columnName.
	 * @return Value of property columnName.
	 *
	 */
	public String getColumnName() {
		return columnName;
	}

	/** Setter for property columnName.
	 * @param columnName New value of property columnName.
	 *
	 */
	public void setColumnName(java.lang.String columnName) {
		this.columnName = columnName;
	}

	/** Getter for property primaryKey.
	 * @return Value of property primaryKey.
	 *
	 */
	public boolean isPrimaryKey() {
		return primaryKey;
	}

	/** Setter for property primaryKey.
	 * @param primaryKey New value of property primaryKey.
	 *
	 */
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	/** Setter for property dataType.
	 * @param dataType New value of property dataType.
	 *
	 */
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	/** Getter for property dataType.
	 * @return Value of property dataType.
	 *
	 */
	public int getDataType() {
		return dataType;
	}

	public boolean isDate() {
		return	dataType==java.sql.Types.DATE ||
				dataType==java.sql.Types.TIME ||
				dataType==java.sql.Types.TIMESTAMP;
	}

	public boolean isFloat() {
		return decimalPlaces > 0;
	}

	public boolean isInteger() {
		return	(dataType == java.sql.Types.BIGINT ||
				dataType == java.sql.Types.INTEGER ||
				dataType == java.sql.Types.NUMERIC)
				&& decimalPlaces == 0;
	}

	public boolean isString() {
		return	dataType == java.sql.Types.CHAR ||
				dataType == java.sql.Types.VARCHAR;
	}

	/** Getter for property columnDisplayWidth.
	 * @return Value of property columnDisplayWidth.
	 *
	 */
	public int getColumnDisplayWidth() {
		return columnDisplayWidth;
	}

	/** Getter for property decimalPlaces.
	 * @return Value of property decimalPlaces.
	 *
	 */
	public int getDecimalPlaces() {
		return decimalPlaces;
	}	

	/** Setter for property decimalPlaces.
	 * @param decimalPlaces New value of property decimalPlaces.
	 *
	 */
	public void setDecimalPlaces(int decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}
	
}
