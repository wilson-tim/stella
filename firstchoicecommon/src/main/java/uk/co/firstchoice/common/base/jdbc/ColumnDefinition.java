package uk.co.firstchoice.common.base.jdbc;

import java.io.Serializable;
import java.sql.Types;

/**
 * Represents a column definition for the SerializableResultSet
 */
public class ColumnDefinition implements Serializable {

	public ColumnDefinition() {
	}

	public ColumnDefinition(int type, String name, String label,
			boolean nullable) {
		this.setDataType(type);
		this.setColumnName(name);
		this.setNullableInd(nullable);
		this.setColumnLabel(label);
	}

	public ColumnDefinition(int type, String name, String label,
			boolean nullable, int size) {
		this(type, name, label, nullable);
		this.setSize(size);
	}

	public ColumnDefinition(int type, String name, String label,
			boolean nullable, int precision, int scale) {
		this(type, name, label, nullable);
		this.setPrecision(precision);
		this.setScale(scale);
	}

	public void setDataType(int type) {
		_dataType = type;
	}

	public int getDataType() {
		return _dataType;
	}

	public void setColumnName(String name) {
		_columnName = name;
	}

	public String getColumnName() {
		return _columnName;
	}

	public void setColumnLabel(String name) {
		_columnLabel = name;
	}

	public String getColumnLabel() {
		return _columnLabel;
	}

	public void setNullableInd(boolean ind) {
		_isNullable = ind;
	}

	public boolean getNullableInd() {
		return _isNullable;
	}

	public void setSize(int size) {
		_size = size;
	}

	public int getSize() {
		return _size;
	}

	public void setPrecision(int size) {
		_precision = size;
		this.setSize(size);
	}

	public int getPrecision() {
		return _precision;
	}

	public void setScale(int size) {
		_scale = size;
	}

	public int getScale() {
		return _scale;
	}

	public String getDataTypeName() {
		String name = "UNKNOWN";
		switch (_dataType) {
		case Types.CHAR:
		case Types.VARCHAR: {
			name = "String";
			break;
		}
		}

		return name;
	}

	private static final int NA = -1;

	private int _dataType = Types.OTHER;

	private String _columnName = null;

	private String _columnLabel = null;

	private boolean _isNullable = false;

	private int _size = NA;

	private int _precision = NA;

	private int _scale = NA;

}