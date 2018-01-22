package uk.co.firstchoice.common.base.jdbc;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import uk.co.firstchoice.common.base.collections.DoubleArrayList;
import uk.co.firstchoice.common.base.collections.FloatArrayList;
import uk.co.firstchoice.common.base.collections.IntegerArrayList;
import uk.co.firstchoice.common.base.collections.ShortArrayList;

public class DataArrayList implements Serializable {

	private DataArrayList() {
	}

	public DataArrayList(SerializableResultSetMetaData metaData)
			throws SQLException {
		_metaData = metaData;

		this.buildArrayMetaData();

		_stringData = new ArrayList[_nbrStringColumns];
		for (int i = 0; i < _nbrStringColumns; i++) {
			_stringData[i] = new ArrayList();
		}

		_dateData = new ArrayList[_nbrDateColumns];
		for (int i = 0; i < _nbrDateColumns; i++) {
			_dateData[i] = new ArrayList();
		}

		_intData = new IntegerArrayList[_nbrIntColumns];
		for (int i = 0; i < _nbrIntColumns; i++) {
			_intData[i] = new IntegerArrayList();
		}

		_shortData = new ShortArrayList[_nbrShortColumns];
		for (int i = 0; i < _nbrShortColumns; i++) {
			_shortData[i] = new ShortArrayList();
		}

		_floatData = new FloatArrayList[_nbrFloatColumns];
		for (int i = 0; i < _nbrFloatColumns; i++) {
			_floatData[i] = new FloatArrayList();
		}

		_doubleData = new DoubleArrayList[_nbrDoubleColumns];
		for (int i = 0; i < _nbrDoubleColumns; i++) {
			_doubleData[i] = new DoubleArrayList();
		}

	}

	public int getNbrRows() {
		return _nbrRows;
	}

	public String getString(int columnIndex, int rowNbr) throws SQLException {
		int pos = this.getArrayOccurance(columnIndex);
		String output = null;
		switch (_javaType[pos]) {
		case DatabaseConstants.JAVA_STRING: {
			output = (String) _stringData[_arrayOccurance[pos]].get(rowNbr);
			break;
		}
		case DatabaseConstants.JAVA_SHORT: {
			output = Short.toString(this.getShort(columnIndex, rowNbr));
			break;
		}
		case DatabaseConstants.JAVA_INT: {
			output = Integer.toString(this.getInt(columnIndex, rowNbr));
			break;
		}
		case DatabaseConstants.JAVA_FLOAT: {
			output = Float.toString(this.getFloat(columnIndex, rowNbr));
			break;
		}
		case DatabaseConstants.JAVA_DOUBLE: {
			output = DECIMAL_FORMAT.format(this.getDouble(columnIndex, rowNbr));
			break;
		}
		case DatabaseConstants.JAVA_DATE: {
			output = DATE_FORMAT.format(this.getDate(columnIndex, rowNbr));
			break;
		}
		default: {
			throw new SQLException("Invalid for columns of type: "
					+ _metaData.getColumnTypeName(columnIndex));
		}
		}

		return output;
	}

	public short getShort(int columnIndex, int rowNbr) throws SQLException {
		int pos = this.getArrayOccurance(columnIndex);
		if (_javaType[pos] != DatabaseConstants.JAVA_SHORT) {
			throw new SQLException("Invalid for columns of type: "
					+ _metaData.getColumnTypeName(columnIndex));
		}

		return _shortData[_arrayOccurance[pos]].get(rowNbr);
	}

	public int getInt(int columnIndex, int rowNbr) throws SQLException {
		int pos = this.getArrayOccurance(columnIndex);
		if (_javaType[pos] != DatabaseConstants.JAVA_INT) {
			throw new SQLException("Invalid for columns of type: "
					+ _metaData.getColumnTypeName(columnIndex));
		}

		return _intData[_arrayOccurance[pos]].get(rowNbr);
	}

	public float getFloat(int columnIndex, int rowNbr) throws SQLException {
		int pos = this.getArrayOccurance(columnIndex);
		if (_javaType[pos] != DatabaseConstants.JAVA_FLOAT) {
			throw new SQLException("Invalid for columns of type: "
					+ _metaData.getColumnTypeName(columnIndex));
		}

		return _floatData[_arrayOccurance[pos]].get(rowNbr);
	}

	public double getDouble(int columnIndex, int rowNbr) throws SQLException {
		int pos = this.getArrayOccurance(columnIndex);
		if (_javaType[pos] != DatabaseConstants.JAVA_DOUBLE) {
			throw new SQLException("Invalid for columns of type: "
					+ _metaData.getColumnTypeName(columnIndex));
		}

		return _doubleData[_arrayOccurance[pos]].get(rowNbr);
	}

	public Date getDate(int columnIndex, int rowNbr) throws SQLException {
		int pos = this.getArrayOccurance(columnIndex);
		if (_javaType[pos] != DatabaseConstants.JAVA_DATE) {
			throw new SQLException("Invalid for columns of type: "
					+ _metaData.getColumnTypeName(columnIndex));
		}

		return (Date) (_dateData[_arrayOccurance[pos]].get(rowNbr));
	}

	public void gatherData(String dataBuffer, String columnDelimiter,
			String rowDelimiter) throws SQLException {
	}

	public void gatherData(ResultSet rs) throws SQLException {
		String stringValue = null;
		java.util.Date dateValue = null;
		int intValue = 0;
		short shortValue = 0;
		float floatValue = 0;
		double doubleValue = 0;
		ResultSetMetaData rsMetaData = rs.getMetaData();

		java.sql.Date sqlDateValue = null;
		java.sql.Time sqlTimeValue = null;
		java.sql.Timestamp sqlTimestampValue = null;

		while (rs.next()) {
			_nbrRows++;

			for (int i = 0; i < _metaData.getColumnCount(); i++) {
				switch (_javaType[i]) {
				case DatabaseConstants.JAVA_STRING: {
					switch (rsMetaData.getColumnType(i + 1)) {
					case Types.CHAR:
					case Types.VARCHAR: {
						stringValue = rs.getString(i + 1);
						break;
					}
					case Types.DECIMAL:
					case Types.DOUBLE:
					case Types.FLOAT:
					case Types.NUMERIC:
					case Types.REAL: {
						doubleValue = rs.getDouble(i + 1);
						stringValue = DECIMAL_FORMAT.format(doubleValue);
						break;
					}
					case Types.INTEGER:
					case Types.SMALLINT:
					case Types.TINYINT: {
						intValue = rs.getInt(i + 1);
						stringValue = Integer.toString(intValue);
						break;
					}
					case Types.TIMESTAMP:
					case Types.TIME:
					case -100: // Work-around for defect in Oracle 9iR1 JDBC
					// driver.
					// This can be removed when Oracle 9iR2 driver installed.
					case Types.DATE: {
						sqlTimestampValue = rs.getTimestamp(i);
						if (sqlDateValue != null)
							stringValue = DATE_FORMAT
									.format(new java.util.Date(
											sqlTimestampValue.getTime()));
						break;
					}
					default: {
						throw new SQLException("Conversion of type "
								+ rsMetaData.getColumnTypeName(i + 1)
								+ " to Java String not supported for column "
								+ rsMetaData.getColumnName(i + 1));
					}
					}

					if (rs.wasNull())
						_stringData[_arrayOccurance[i]]
								.add(DatabaseConstants.NULL_STR);
					else {
						_stringData[_arrayOccurance[i]].add(stringValue);
					}
					break;
				}
				case DatabaseConstants.JAVA_DATE: {
					if (_metaData.getColumnType(i + 1) == Types.DATE) {
						switch (rsMetaData.getColumnType(i + 1)) {
						case Types.CHAR:
						case Types.VARCHAR: {
							stringValue = rs.getString(i + 1);
							if (stringValue != null) {
								try {
									dateValue = DATE_FORMAT.parse(stringValue);
								} catch (Throwable t) {
									throw new SQLException("String "
											+ stringValue
											+ "can't be converted to date.");
								}
							}
							break;
						}
						case Types.DATE: {
							sqlDateValue = rs.getDate(i + 1);
							dateValue = new java.util.Date(sqlDateValue
									.getTime());
							break;
						}
						default: {
							throw new SQLException("Conversion of type "
									+ rsMetaData.getColumnTypeName(i + 1)
									+ " to Java Date not supported for column "
									+ rsMetaData.getColumnName(i + 1));
						}
						}

						if (rs.wasNull())
							dateValue = DatabaseConstants.NULL_DATE;
					} else if (_metaData.getColumnType(i + 1) == Types.TIME) {
						switch (rsMetaData.getColumnType(i + 1)) {
						case Types.CHAR:
						case Types.VARCHAR: {
							stringValue = rs.getString(i + 1);
							if (stringValue != null) {
								try {
									dateValue = DATE_FORMAT.parse(stringValue);
								} catch (Throwable t) {
									throw new SQLException("String "
											+ stringValue
											+ "can't be converted to date.");
								}
							}
							break;
						}
						case Types.TIME: {
							sqlTimeValue = rs.getTime(i + 1);
							dateValue = new java.util.Date(sqlTimeValue
									.getTime());
							break;
						}
						default: {
							throw new SQLException("Conversion of type "
									+ rsMetaData.getColumnTypeName(i + 1)
									+ " to Java Date not supported for column "
									+ rsMetaData.getColumnName(i + 1));
						}
						}

						if (rs.wasNull())
							dateValue = DatabaseConstants.NULL_DATE;
						else
							dateValue = new java.util.Date(sqlTimeValue
									.getTime());
					} else if (_metaData.getColumnType(i + 1) == Types.TIMESTAMP) {
						sqlTimestampValue = rs.getTimestamp(i + 1);
						if (rs.wasNull())
							dateValue = DatabaseConstants.NULL_DATE;
						else
							dateValue = new java.util.Date(sqlTimestampValue
									.getTime());
					}

					_dateData[_arrayOccurance[i]].add(dateValue);
					break;
				}
				case DatabaseConstants.JAVA_INT: {
					switch (rsMetaData.getColumnType(i + 1)) {
					case Types.CHAR:
					case Types.VARCHAR: {
						stringValue = rs.getString(i + 1);
						if (stringValue != null) {
							try {
								intValue = Integer.parseInt(stringValue);
							} catch (Throwable t) {
								throw new SQLException("String " + stringValue
										+ "can't be converted to integer.");
							}
						}
						break;
					}
					case Types.INTEGER:
					case Types.SMALLINT:
					case Types.TINYINT: {
						intValue = rs.getInt(i + 1);
						break;
					}
					default: {
						throw new SQLException("Conversion of type "
								+ rsMetaData.getColumnTypeName(i + 1)
								+ " to Java int not supported for column "
								+ rsMetaData.getColumnName(i + 1));
					}
					}

					if (rs.wasNull())
						_intData[_arrayOccurance[i]]
								.add(DatabaseConstants.NULL_INT);
					else
						_intData[_arrayOccurance[i]].add(intValue);
					break;
				}
				case DatabaseConstants.JAVA_SHORT: {
					switch (rsMetaData.getColumnType(i + 1)) {
					case Types.CHAR:
					case Types.VARCHAR: {
						stringValue = rs.getString(i + 1);
						if (stringValue != null) {
							try {
								shortValue = Short.parseShort(stringValue);
							} catch (Throwable t) {
								throw new SQLException("String " + stringValue
										+ "can't be converted to short.");
							}
						}
						break;
					}
					case Types.SMALLINT:
					case Types.TINYINT: {
						shortValue = rs.getShort(i + 1);
						break;
					}
					default: {
						throw new SQLException("Conversion of type "
								+ rsMetaData.getColumnTypeName(i + 1)
								+ " to Java short not supported for column "
								+ rsMetaData.getColumnName(i + 1));
					}
					}

					if (rs.wasNull())
						_shortData[_arrayOccurance[i]]
								.add(DatabaseConstants.NULL_SHORT);
					else
						_shortData[_arrayOccurance[i]].add(shortValue);
					break;
				}
				case DatabaseConstants.JAVA_FLOAT: {
					switch (rsMetaData.getColumnType(i + 1)) {
					case Types.CHAR:
					case Types.VARCHAR: {
						stringValue = rs.getString(i + 1);
						if (stringValue != null) {
							try {
								floatValue = Float.parseFloat(stringValue);
							} catch (Throwable t) {
								throw new SQLException("String " + stringValue
										+ "can't be converted to float.");
							}
						}
						break;
					}
					case Types.FLOAT:
					case Types.NUMERIC:
					case Types.REAL:
					case Types.DECIMAL: {
						floatValue = rs.getFloat(i + 1);
						break;
					}
					default: {
						throw new SQLException("Conversion of type "
								+ rsMetaData.getColumnTypeName(i + 1)
								+ " to Java float not supported for column "
								+ rsMetaData.getColumnName(i + 1));
					}
					}

					if (rs.wasNull())
						_floatData[_arrayOccurance[i]]
								.add(DatabaseConstants.NULL_FLOAT);
					else
						_floatData[_arrayOccurance[i]].add(floatValue);
					break;
				}
				case DatabaseConstants.JAVA_DOUBLE: {
					switch (rsMetaData.getColumnType(i + 1)) {
					case Types.CHAR:
					case Types.VARCHAR: {
						stringValue = rs.getString(i + 1);
						if (stringValue != null) {
							try {
								doubleValue = DECIMAL_FORMAT.parse(stringValue)
										.doubleValue();
							} catch (Throwable t) {
								throw new SQLException("String " + stringValue
										+ "can't be converted to double.");
							}
						}
						break;
					}
					case Types.FLOAT:
					case Types.NUMERIC:
					case Types.REAL:
					case Types.DECIMAL: {
						doubleValue = rs.getDouble(i + 1);
						break;
					}
					default: {
						throw new SQLException("Conversion of type "
								+ rsMetaData.getColumnTypeName(i + 1)
								+ " to Java double not supported for column "
								+ rsMetaData.getColumnName(i + 1));
					}
					}

					if (rs.wasNull())
						_doubleData[_arrayOccurance[i]]
								.add(DatabaseConstants.NULL_DOUBLE);
					else
						_doubleData[_arrayOccurance[i]].add(doubleValue);
					break;
				}
				}
			}
		}
	}

	private void buildArrayMetaData() throws SQLException { // assumes _metaData
		// is not null
		_javaType = new int[_metaData.getColumnCount()];
		_arrayOccurance = new int[_metaData.getColumnCount()];

		for (int i = 0; i < _metaData.getColumnCount(); i++) {
			switch (_metaData.getColumnType(i + 1)) {
			case Types.DECIMAL:
			case Types.DOUBLE:
			case Types.NUMERIC:
			case Types.REAL: {
				_javaType[i] = DatabaseConstants.JAVA_DOUBLE;
				_arrayOccurance[i] = _nbrDoubleColumns;
				_nbrDoubleColumns++;
				break;
			}

			case Types.FLOAT: {
				_javaType[i] = DatabaseConstants.JAVA_FLOAT;
				_arrayOccurance[i] = _nbrFloatColumns;
				_nbrFloatColumns++;
				break;
			}

			case Types.INTEGER: {
				_javaType[i] = DatabaseConstants.JAVA_INT;
				_arrayOccurance[i] = _nbrIntColumns;
				_nbrIntColumns++;
				break;
			}

			case Types.SMALLINT:
			case Types.TINYINT: {
				_javaType[i] = DatabaseConstants.JAVA_SHORT;
				_arrayOccurance[i] = _nbrShortColumns;
				_nbrShortColumns++;
				break;
			}

			case Types.CHAR:
			case Types.LONGVARCHAR:
			case Types.VARCHAR: {
				_javaType[i] = DatabaseConstants.JAVA_STRING;
				_arrayOccurance[i] = _nbrStringColumns;
				_nbrStringColumns++;
				break;
			}

			case Types.DATE:
			case Types.TIME:
			case Types.TIMESTAMP:
			case -100: // Work-around for defect in Oracle 9iR1 JDBC driver.
			// This can be removed when Oracle 9iR2 driver installed.
			{
				_javaType[i] = DatabaseConstants.JAVA_DATE;
				_arrayOccurance[i] = _nbrDateColumns;
				_nbrDateColumns++;
				break;
			}

			default: {
				throw new SQLException("Column type "
						+ _metaData.getColumnTypeName(i + 1) + " for column "
						+ "not Supported");
			}
			}
		}
	}

	private int getArrayOccurance(int column) throws SQLException {
		int answer = column - 1;
		if (answer < 0 || column > _metaData.getColumnCount())
			throw new SQLException("Index " + column + " out of range");
		return answer;
	}

	private SerializableResultSetMetaData _metaData = null;

	private int _nbrRows = 0;

	private int[] _javaType = null;

	private int[] _arrayOccurance = null;

	private int _nbrStringColumns = 0;

	private int _nbrDateColumns = 0;

	private int _nbrIntColumns = 0;

	private int _nbrShortColumns = 0;

	private int _nbrFloatColumns = 0;

	private int _nbrDoubleColumns = 0;

	private ArrayList[] _stringData = null;

	private ArrayList[] _dateData = null;

	private IntegerArrayList[] _intData = null;

	private ShortArrayList[] _shortData = null;

	private FloatArrayList[] _floatData = null;

	private DoubleArrayList[] _doubleData = null;

	private final DecimalFormat DECIMAL_FORMAT = new DecimalFormat(
			"#.#################");

	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");

}