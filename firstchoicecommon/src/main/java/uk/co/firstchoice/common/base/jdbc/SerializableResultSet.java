package uk.co.firstchoice.common.base.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * Implements a serializable version of a java.sql.ResultSet object. This is
 * useful for returning data to callers on remote machines (e.g. EJB clients,
 * Web services clients, etc.)
 * 
 * <BR>
 * <BR>
 */
public abstract class SerializableResultSet implements ResultSet, Serializable {

	private SerializableResultSet() {
	}

	public SerializableResultSet(ResultSet rs) throws SQLException {
		_metaData = new SerializableResultSetMetaData(rs.getMetaData()) {
                    public <T> T unwrap(Class<T> iface) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public boolean isWrapperFor(Class<?> iface) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                };
		_data = new DataArrayList(_metaData);
		_data.gatherData(rs);
	}

	public SerializableResultSet(ResultSet rs,
			ColumnDefinition[] dataDefinitionOverride) throws SQLException {
		_metaData = new SerializableResultSetMetaData(dataDefinitionOverride) {
                    public <T> T unwrap(Class<T> iface) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public boolean isWrapperFor(Class<?> iface) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                };
		_data = new DataArrayList(_metaData);
		_data.gatherData(rs);
	}

	public SerializableResultSet(ColumnDefinition[] dataDefinitionOverride,
			String dataBuffer, String columnDelimiter, String rowDelimiter)
			throws SQLException {
		_metaData = new SerializableResultSetMetaData(dataDefinitionOverride) {
                    public <T> T unwrap(Class<T> iface) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public boolean isWrapperFor(Class<?> iface) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                };
		_data = new DataArrayList(_metaData);
		_data.gatherData(dataBuffer, columnDelimiter, rowDelimiter);
	}

	public boolean next() throws SQLException {
		boolean answer = false;
		if (_scrollForwards) {
			_currentRow++;
			if (_currentRow < _data.getNbrRows()) {
				answer = true;
			}
		} else {
			_currentRow--;
			if (_currentRow > -1) {
				answer = true;
			}
		}
		return answer;
	}

	public void close() throws SQLException {
	}

	public boolean wasNull() throws SQLException {
		return _wasNull;
	}

	public String getString(int columnIndex) throws SQLException {
		String value = _data.getString(columnIndex, _currentRow);

		if (value == DatabaseConstants.NULL_STR)
			_wasNull = true;
		else
			_wasNull = false;

		return value;
	}

	public boolean getBoolean(int columnIndex) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getBoolean() not yet implemented.");
	}

	public byte getByte(int columnIndex) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getByte() not yet implemented.");
	}

	public short getShort(int columnIndex) throws SQLException {
		short value = _data.getShort(columnIndex, _currentRow);

		if (value == DatabaseConstants.NULL_SHORT)
			_wasNull = true;
		else
			_wasNull = false;

		return value;
	}

	public int getInt(int columnIndex) throws SQLException {
		int value = _data.getInt(columnIndex, _currentRow);

		if (value == DatabaseConstants.NULL_INT)
			_wasNull = true;
		else
			_wasNull = false;

		return value;
	}

	public long getLong(int columnIndex) throws SQLException {
		int value = _data.getInt(columnIndex, _currentRow);

		if (value == DatabaseConstants.NULL_INT)
			_wasNull = true;
		else
			_wasNull = false;

		return (long) value;
	}

	public float getFloat(int columnIndex) throws SQLException {
		float value = _data.getFloat(columnIndex, _currentRow);

		if (value == DatabaseConstants.NULL_FLOAT)
			_wasNull = true;
		else
			_wasNull = false;

		return value;
	}

	public double getDouble(int columnIndex) throws SQLException {
		double value = _data.getDouble(columnIndex, _currentRow);

		if (value == DatabaseConstants.NULL_DOUBLE)
			_wasNull = true;
		else
			_wasNull = false;

		return value;
	}

	public BigDecimal getBigDecimal(int columnIndex, int scale)
			throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getBigDecimal() not yet implemented.");
	}

	public byte[] getBytes(int columnIndex) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getBytes() not yet implemented.");
	}

	public Date getDate(int columnIndex) throws SQLException {
		java.util.Date value = _data.getDate(columnIndex, _currentRow);

		if (value == DatabaseConstants.NULL_DATE)
			_wasNull = true;
		else
			_wasNull = false;

		return new java.sql.Date(value.getTime());
	}

	public Time getTime(int columnIndex) throws SQLException {
		java.util.Date value = _data.getDate(columnIndex, _currentRow);

		if (value == DatabaseConstants.NULL_DATE)
			_wasNull = true;
		else
			_wasNull = false;

		return new java.sql.Time(value.getTime());
	}

	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		java.util.Date value = _data.getDate(columnIndex, _currentRow);

		if (value == DatabaseConstants.NULL_DATE)
			_wasNull = true;
		else
			_wasNull = false;

		return new java.sql.Timestamp(value.getTime());
	}

	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getAsciiStream() not yet implemented.");
	}

	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getUnicodeStream() not yet implemented.");
	}

	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getBinaryStream() not yet implemented.");
	}

	public String getString(String columnName) throws SQLException {
		return this.getString(_metaData.getColumnIndexByName(columnName));
	}

	public boolean getBoolean(String columnName) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getBoolean() not yet implemented.");
	}

	public byte getByte(String columnName) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getByte() not yet implemented.");
	}

	public short getShort(String columnName) throws SQLException {
		return this.getShort(_metaData.getColumnIndexByName(columnName));
	}

	public int getInt(String columnName) throws SQLException {
		return this.getInt(_metaData.getColumnIndexByName(columnName));
	}

	public long getLong(String columnName) throws SQLException {
		return this.getLong(_metaData.getColumnIndexByName(columnName));
	}

	public float getFloat(String columnName) throws SQLException {
		return this.getFloat(_metaData.getColumnIndexByName(columnName));
	}

	public double getDouble(String columnName) throws SQLException {
		return this.getDouble(_metaData.getColumnIndexByName(columnName));
	}

	public BigDecimal getBigDecimal(String columnName, int scale)
			throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getBigDecimal() not yet implemented.");
	}

	public byte[] getBytes(String columnName) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getBytes() not yet implemented.");
	}

	public Date getDate(String columnName) throws SQLException {
		return this.getDate(_metaData.getColumnIndexByName(columnName));
	}

	public Time getTime(String columnName) throws SQLException {
		return this.getTime(_metaData.getColumnIndexByName(columnName));
	}

	public Timestamp getTimestamp(String columnName) throws SQLException {
		return this.getTimestamp(_metaData.getColumnIndexByName(columnName));
	}

	public InputStream getAsciiStream(String columnName) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getAsciiStream() not yet implemented.");
	}

	public InputStream getUnicodeStream(String columnName) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getUnicodeStream() not yet implemented.");
	}

	public InputStream getBinaryStream(String columnName) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getBinaryStream() not yet implemented.");
	}

	public SQLWarning getWarnings() throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getWarnings() not yet implemented.");
	}

	public void clearWarnings() throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method clearWarnings() not yet implemented.");
	}

	public String getCursorName() throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getCursorName() not yet implemented.");
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		return _metaData;
	}

	public Object getObject(int columnIndex) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getObject() not yet implemented.");
	}

	public Object getObject(String columnName) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getObject() not yet implemented.");
	}

	public int findColumn(String columnName) throws SQLException {
		return _metaData.getColumnIndexByName(columnName);
	}

	public Reader getCharacterStream(int columnIndex) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getCharacterStream() not yet implemented.");
	}

	public Reader getCharacterStream(String columnName) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getCharacterStream() not yet implemented.");
	}

	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getBigDecimal() not yet implemented.");
	}

	public BigDecimal getBigDecimal(String columnName) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getBigDecimal() not yet implemented.");
	}

	public boolean isBeforeFirst() throws SQLException {
		boolean answer = false;
		if (_currentRow < 0)
			answer = true;
		return answer;
	}

	public boolean isAfterLast() throws SQLException {
		boolean answer = false;
		if (_currentRow >= _data.getNbrRows())
			answer = true;
		return answer;
	}

	public boolean isFirst() throws SQLException {
		boolean answer = false;
		if (_currentRow == 0)
			answer = true;
		return answer;
	}

	public boolean isLast() throws SQLException {
		boolean answer = false;
		if (_currentRow == _data.getNbrRows() - 1)
			answer = true;
		return answer;
	}

	public void beforeFirst() throws SQLException {
		_currentRow = -1;
	}

	public void afterLast() throws SQLException {
		_currentRow = _data.getNbrRows();
	}

	public boolean first() throws SQLException {
		return (_currentRow == 0);
	}

	public boolean last() throws SQLException {
		return (_currentRow == _data.getNbrRows() - 1);
	}

	public int getRow() throws SQLException {
		return _currentRow + 1;
	}

	public boolean absolute(int row) throws SQLException {
		boolean answer = true;
		if (row > 0) {
			if (row > _data.getNbrRows())
				answer = false;
			_currentRow = row - 1;
		} else {
			int newRow = _data.getNbrRows() + row;
			if (newRow < 0)
				answer = false;
			_currentRow = newRow - 1;
		}

		return answer;
	}

	public boolean relative(int rows) throws SQLException {
		return this.absolute(_currentRow + 1 + rows);
	}

	public boolean previous() throws SQLException {
		boolean answer = true;
		_currentRow--;
		if (_currentRow < 0)
			answer = false;
		return answer;
	}

	public void setFetchDirection(int direction) throws SQLException {
		if (direction == ResultSet.FETCH_FORWARD)
			_scrollForwards = true;
		else
			_scrollForwards = false;
	}

	public int getFetchDirection() throws SQLException {
		int direction = ResultSet.FETCH_REVERSE;
		if (_scrollForwards)
			direction = ResultSet.FETCH_FORWARD;
		return direction;
	}

	public void setFetchSize(int rows) throws SQLException {
		return;
	}

	public int getFetchSize() throws SQLException {
		return 1;
	}

	public int getType() throws SQLException {
		return ResultSet.TYPE_SCROLL_INSENSITIVE;
	}

	public int getConcurrency() throws SQLException {
		return ResultSet.CONCUR_READ_ONLY;
	}

	public boolean rowUpdated() throws SQLException {
		return false;
	}

	public boolean rowInserted() throws SQLException {
		return false;
	}

	public boolean rowDeleted() throws SQLException {
		return false;
	}

	public void updateNull(int columnIndex) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateNull() not yet implemented.");
	}

	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateBoolean() not yet implemented.");
	}

	public void updateByte(int columnIndex, byte x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateByte() not yet implemented.");
	}

	public void updateShort(int columnIndex, short x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateShort() not yet implemented.");
	}

	public void updateInt(int columnIndex, int x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateInt() not yet implemented.");
	}

	public void updateLong(int columnIndex, long x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateLong() not yet implemented.");
	}

	public void updateFloat(int columnIndex, float x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateFloat() not yet implemented.");
	}

	public void updateDouble(int columnIndex, double x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateDouble() not yet implemented.");
	}

	public void updateBigDecimal(int columnIndex, BigDecimal x)
			throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateBigDecimal() not yet implemented.");
	}

	public void updateString(int columnIndex, String x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateString() not yet implemented.");
	}

	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateBytes() not yet implemented.");
	}

	public void updateDate(int columnIndex, Date x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateDate() not yet implemented.");
	}

	public void updateTime(int columnIndex, Time x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateTime() not yet implemented.");
	}

	public void updateTimestamp(int columnIndex, Timestamp x)
			throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateTimestamp() not yet implemented.");
	}

	public void updateAsciiStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateAsciiStream() not yet implemented.");
	}

	public void updateBinaryStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateBinaryStream() not yet implemented.");
	}

	public void updateCharacterStream(int columnIndex, Reader x, int length)
			throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateCharacterStream() not yet implemented.");
	}

	public void updateObject(int columnIndex, Object x, int scale)
			throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateObject() not yet implemented.");
	}

	public void updateObject(int columnIndex, Object x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateObject() not yet implemented.");
	}

	public void updateNull(String columnName) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateNull() not yet implemented.");
	}

	public void updateBoolean(String columnName, boolean x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateBoolean() not yet implemented.");
	}

	public void updateByte(String columnName, byte x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateByte() not yet implemented.");
	}

	public void updateShort(String columnName, short x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateShort() not yet implemented.");
	}

	public void updateInt(String columnName, int x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateInt() not yet implemented.");
	}

	public void updateLong(String columnName, long x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateLong() not yet implemented.");
	}

	public void updateFloat(String columnName, float x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateFloat() not yet implemented.");
	}

	public void updateDouble(String columnName, double x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateDouble() not yet implemented.");
	}

	public void updateBigDecimal(String columnName, BigDecimal x)
			throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateBigDecimal() not yet implemented.");
	}

	public void updateString(String columnName, String x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateString() not yet implemented.");
	}

	public void updateBytes(String columnName, byte[] x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateBytes() not yet implemented.");
	}

	public void updateDate(String columnName, Date x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateDate() not yet implemented.");
	}

	public void updateTime(String columnName, Time x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateTime() not yet implemented.");
	}

	public void updateTimestamp(String columnName, Timestamp x)
			throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateTimestamp() not yet implemented.");
	}

	public void updateAsciiStream(String columnName, InputStream x, int length)
			throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateAsciiStream() not yet implemented.");
	}

	public void updateBinaryStream(String columnName, InputStream x, int length)
			throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateBinaryStream() not yet implemented.");
	}

	public void updateCharacterStream(String columnName, Reader reader,
			int length) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateCharacterStream() not yet implemented.");
	}

	public void updateObject(String columnName, Object x, int scale)
			throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateObject() not yet implemented.");
	}

	public void updateObject(String columnName, Object x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateObject() not yet implemented.");
	}

	public void insertRow() throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method insertRow() not yet implemented.");
	}

	public void updateRow() throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateRow() not yet implemented.");
	}

	public void deleteRow() throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method deleteRow() not yet implemented.");
	}

	public void refreshRow() throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method refreshRow() not yet implemented.");
	}

	public void cancelRowUpdates() throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method cancelRowUpdates() not yet implemented.");
	}

	public void moveToInsertRow() throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method moveToInsertRow() not yet implemented.");
	}

	public void moveToCurrentRow() throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method moveToCurrentRow() not yet implemented.");
	}

	public Statement getStatement() throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getStatement() not yet implemented.");
	}

	public Object getObject(int i, Map map) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getObject() not yet implemented.");
	}

	public Ref getRef(int i) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getRef() not yet implemented.");
	}

	public Blob getBlob(int i) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getBlob() not yet implemented.");
	}

	public Clob getClob(int i) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getClob() not yet implemented.");
	}

	public Array getArray(int i) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getArray() not yet implemented.");
	}

	public Object getObject(String colName, Map map) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getObject() not yet implemented.");
	}

	public Ref getRef(String colName) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getRef() not yet implemented.");
	}

	public Blob getBlob(String colName) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getBlob() not yet implemented.");
	}

	public Clob getClob(String colName) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getClob() not yet implemented.");
	}

	public Array getArray(String colName) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getArray() not yet implemented.");
	}

	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getDate() not yet implemented.");
	}

	public Date getDate(String columnName, Calendar cal) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getDate() not yet implemented.");
	}

	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getTime() not yet implemented.");
	}

	public Time getTime(String columnName, Calendar cal) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getTime() not yet implemented.");
	}

	public Timestamp getTimestamp(int columnIndex, Calendar cal)
			throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getTimestamp() not yet implemented.");
	}

	public Timestamp getTimestamp(String columnName, Calendar cal)
			throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getTimestamp() not yet implemented.");
	}

	public URL getURL(int columnIndex) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getURL() not yet implemented.");
	}

	public URL getURL(String columnName) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method getURL() not yet implemented.");
	}

	public void updateRef(int columnIndex, Ref x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateRef() not yet implemented.");
	}

	public void updateRef(String columnName, Ref x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateRef() not yet implemented.");
	}

	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateBlob() not yet implemented.");
	}

	public void updateBlob(String columnName, Blob x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateBlob() not yet implemented.");
	}

	public void updateClob(int columnIndex, Clob x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateClob() not yet implemented.");
	}

	public void updateClob(String columnName, Clob x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateClob() not yet implemented.");
	}

	public void updateArray(int columnIndex, Array x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateArray() not yet implemented.");
	}

	public void updateArray(String columnName, Array x) throws SQLException {
		/** @todo: Implement this java.sql.ResultSet method */
		throw new java.lang.UnsupportedOperationException(
				"Method updateArray() not yet implemented.");
	}

	public int getNbrRows() {
		return _data.getNbrRows();
	}

	private SerializableResultSetMetaData _metaData = null;

	private int _currentRow = -1;

	private boolean _wasNull = false;

	private boolean _scrollForwards = true;

	private DataArrayList _data = null;

}