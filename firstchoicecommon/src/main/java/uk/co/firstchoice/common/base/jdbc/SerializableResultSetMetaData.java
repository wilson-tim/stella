package uk.co.firstchoice.common.base.jdbc;

import java.io.Serializable;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

/**
 * Serializable version of a java.sql.ResultSetMetaData object.
 *  
 */
public abstract class SerializableResultSetMetaData implements ResultSetMetaData,
		Serializable {

	private SerializableResultSetMetaData() {
	}

	public SerializableResultSetMetaData(ColumnDefinition[] column) {
		_columnDefinition = column;
		this.buildNameIndexMap();
	}

	public SerializableResultSetMetaData(ResultSetMetaData rsMetaData)
			throws SQLException {
		int columnCount = rsMetaData.getColumnCount();
		boolean isNullable = false;

		_columnDefinition = new ColumnDefinition[columnCount];

		for (int i = 0; i < columnCount; i++) {
			if (rsMetaData.isNullable(i + 1) == ResultSetMetaData.columnNullable)
				isNullable = true;
			else
				isNullable = false;

			switch (rsMetaData.getColumnType(i + 1)) {
			case Types.DECIMAL:
			case Types.DOUBLE:
			case Types.FLOAT:
			case Types.NUMERIC:
			case Types.REAL:
			case Types.INTEGER:
			case Types.SMALLINT:
			case Types.TINYINT: {
				_columnDefinition[i] = new ColumnDefinition(rsMetaData
						.getColumnType(i + 1), rsMetaData.getColumnName(i + 1),
						rsMetaData.getColumnLabel(i + 1), isNullable,
						rsMetaData.getPrecision(i + 1), rsMetaData
								.getScale(i + 1));
				break;
			}
			case Types.CHAR:
			case Types.LONGVARCHAR:
			case Types.VARCHAR: {
				_columnDefinition[i] = new ColumnDefinition(rsMetaData
						.getColumnType(i + 1), rsMetaData.getColumnName(i + 1),
						rsMetaData.getColumnLabel(i + 1), isNullable,
						rsMetaData.getColumnDisplaySize(i + 1));
				break;
			}
			default: {
				_columnDefinition[i] = new ColumnDefinition(rsMetaData
						.getColumnType(i + 1), rsMetaData.getColumnName(i + 1),
						rsMetaData.getColumnLabel(i + 1), isNullable);
				break;
			}
			}
		}

		this.buildNameIndexMap();
	}

	public int getColumnCount() throws SQLException {
		return _columnDefinition.length;
	}

	public boolean isAutoIncrement(int column) throws SQLException {
		return false;
	}

	public boolean isCaseSensitive(int column) throws SQLException {
		return false;
	}

	public boolean isSearchable(int column) throws SQLException {
		return false;
	}

	public boolean isCurrency(int column) throws SQLException {
		return false;
	}

	public int isNullable(int column) throws SQLException {
		if (_columnDefinition[this.getArrayPosition(column)].getNullableInd()) {
			return ResultSetMetaData.columnNullable;
		} else
			return ResultSetMetaData.columnNoNulls;
	}

	public boolean isSigned(int column) throws SQLException {
		boolean answer = false;
		int dataType = _columnDefinition[this.getArrayPosition(column)]
				.getDataType();

		switch (dataType) {
		case Types.DECIMAL:
		case Types.DOUBLE:
		case Types.FLOAT:
		case Types.INTEGER:
		case Types.NUMERIC:
		case Types.REAL:
		case Types.SMALLINT:
		case Types.TINYINT: {
			answer = true;
		}
		}

		return answer;
	}

	public int getColumnDisplaySize(int column) throws SQLException {
		return _columnDefinition[this.getArrayPosition(column)].getSize();
	}

	public String getColumnLabel(int column) throws SQLException {
		return _columnDefinition[this.getArrayPosition(column)]
				.getColumnLabel();
	}

	public String getColumnName(int column) throws SQLException {
		return _columnDefinition[this.getArrayPosition(column)].getColumnName();
	}

	public String getSchemaName(int column) throws SQLException {
		/** @todo: Implement this java.sql.ResultSetMetaData method */
		throw new java.lang.UnsupportedOperationException(
				"Method getSchemaName() not yet implemented.");
	}

	public int getPrecision(int column) throws SQLException {
		return _columnDefinition[this.getArrayPosition(column)].getPrecision();
	}

	public int getScale(int column) throws SQLException {
		return _columnDefinition[this.getArrayPosition(column)].getScale();
	}

	public String getTableName(int column) throws SQLException {
		/** @todo: Implement this java.sql.ResultSetMetaData method */
		throw new java.lang.UnsupportedOperationException(
				"Method getTableName() not yet implemented.");
	}

	public String getCatalogName(int column) throws SQLException {
		/** @todo: Implement this java.sql.ResultSetMetaData method */
		throw new java.lang.UnsupportedOperationException(
				"Method getCatalogName() not yet implemented.");
	}

	public int getColumnType(int column) throws SQLException {
		return _columnDefinition[this.getArrayPosition(column)].getDataType();
	}

	public String getColumnTypeName(int column) throws SQLException {
		return _columnDefinition[this.getArrayPosition(column)]
				.getDataTypeName();
	}

	public boolean isReadOnly(int column) throws SQLException {
		return true;
	}

	public boolean isWritable(int column) throws SQLException {
		return false;
	}

	public boolean isDefinitelyWritable(int column) throws SQLException {
		return false;
	}

	public String getColumnClassName(int column) throws SQLException {
		/** @todo: Implement this java.sql.ResultSetMetaData method */
		throw new java.lang.UnsupportedOperationException(
				"Method getColumnClassName() not yet implemented.");
	}

	public int getColumnIndexByName(String columnName) throws SQLException {
		Integer i = (Integer) _columnNameIndexRef.get(columnName.toLowerCase());
		if (i == null)
			throw new SQLException("Name " + columnName
					+ " not part of result set.");
		return i.intValue();
	}

	private int getArrayPosition(int column) throws SQLException {
		int position = column - 1;
		if (position < 0)
			throw new SQLException("Invalid Column: " + column);
		if (column > _columnDefinition.length)
			throw new SQLException("Invalid Column: " + column);
		return position;
	}

	private void buildNameIndexMap() {
		for (int i = 0; i < _columnDefinition.length; i++) {
			_columnNameIndexRef.put(_columnDefinition[i].getColumnName()
					.toLowerCase().trim(), new Integer(i + 1));
		}
	}

	private ColumnDefinition[] _columnDefinition = null;

	private HashMap _columnNameIndexRef = new HashMap();
}