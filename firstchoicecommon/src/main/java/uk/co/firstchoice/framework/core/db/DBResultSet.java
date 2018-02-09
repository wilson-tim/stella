/*
 * ResultSet.java
 *
 * Created on 21 October 2003, 10:07
 */

package uk.co.firstchoice.framework.core.db;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.math.BigDecimal;

/**
 *
 * @author  mkonda
 */
public class DBResultSet {
    
   // ArrayList containing the results
    private ArrayList _resultsList = null;
    
    // ArrayList containing the column names 
    private ArrayList _columnNames = null;
    
    // number of columns 
    private int _columnCount = 0;
    
    /** formatted width of each field included in toString() */
    private int toStringFormatWidth = 12;  
 
    public DBResultSet (int columnCount) {
        _columnCount = columnCount ;
        _resultsList = new ArrayList ();
        _columnNames = new ArrayList ();
    }
    
    public void add(Object obj) {
        _resultsList.add ( obj );
    }
    
    public void addColumnName(String columnName) {
        _columnNames.add(columnName);        
    }

    private int getColumnIndex(String columnName)  {
        int colIndex = -1;
        for (int i=0; i < _columnNames.size(); i++) {
            String thisColumnName = (String)_columnNames.get(i); 
            if (thisColumnName.equalsIgnoreCase (columnName)) {
                colIndex = i;
                break;
            }
        }

//        if (colIndex < 0)
//            throw new Exception("Invalid column name: " + columnName);
        return(colIndex);
    }
        public String getString(int row, String columnName)
    {
        return(getString(row, getColumnIndex(columnName)));
    }
    
    /**
     * Gets the value of the field corresponding to (row, col) as a boolean
     * @param row int 
     * @param col int
     * @return boolean value of field
     */
    public boolean getBoolean(int row, int col)
    {
        Object o = getObject(row, col);
        Boolean b = (Boolean)o; 
        return(b.booleanValue());
    }
    
    /**
     * Gets the value of the field corresponding to row, columnName as a boolean
     * @param row int
     * @param columnName String
     * @return boolean value of field
     */
    public boolean getBoolean(int row, String columnName)
    {
        return(getBoolean(row, getColumnIndex(columnName)));
    }

    /**
     * Gets the value of the field corresponding to (row, col) as a java.sql.Date
     * @param row int
     * @param col int
     * @return Date value of field
     */
    public Date getDate(int row, int col)
    {
        Object o = getObject(row, col);
        if (o instanceof Timestamp)
        {
            Timestamp t = (Timestamp)o;
            Date d = new Date(t.getTime());
            return(d);
        }
        else
        {
            Date d = (Date)o; 
            return(d);
        }
    }
    
    /**
     * Gets the value of the field corresponding to row, columnName as a java.sql.Date
     * @param row int
     * @param columnName String
     * @return Date value of field
     */
    public Date getDate(int row, String columnName)
    {
        return(getDate(row, getColumnIndex(columnName)));
    }
    
    /**
     * Gets the value of the field corresponding to (row, col) as a java.sql.Time
     * @param row int
     * @param col int
     * @return Time value of field
     */
    public Time getTime(int row, int col)
    {
        Object o = getObject(row, col);
        Time t = (Time)o; 
        return(t);
    }

    public Time getTime(int row, String columnName) {
        return(getTime(row, getColumnIndex(columnName)));
    }
    
    public Timestamp getTimestamp(int row, int col) {
        Object obj = getObject(row, col);
        if (obj instanceof Date) {
            Date dt = (Date) obj;
            Timestamp ts = new Timestamp(dt.getTime()); 
            return(ts);
        } else {    
            Timestamp ts = (Timestamp) obj; 
            return(ts);
        }
    }

    public Timestamp getTimestamp(int row, String columnName) {
        return(getTimestamp(row, getColumnIndex(columnName)));
    }
    
    public double getDouble(int row, int col) {
        Object obj = getObject(row, col);
        if (obj instanceof BigDecimal) {
            BigDecimal big = (BigDecimal) obj;
            return(big.doubleValue()); 
        } else {
            Double dbl = (Double) obj ; 
            return(dbl.doubleValue());
        }
    }

    public double getDouble(int row, String columnName) {
        return(getDouble(row, getColumnIndex(columnName)));
    }
    
    public double getFloat(int row, int col) {
        Object obj = getObject(row, col);
        if (obj instanceof BigDecimal) {
            BigDecimal big = (BigDecimal) obj;
            return(big.floatValue()); 
        } else {
            Float flt = (Float) obj; 
            return(flt.floatValue());
        }
    }

    public double getFloat(int row, String columnName) {
        return(getFloat(row, getColumnIndex(columnName)));
    }

    public long getLong(int row, String columnName) {
        return(getInt(row, columnName));
    }

    public int getInt(int row, String columnName) {
        return(getInt(row, getColumnIndex (columnName) ));
    }

    public int getInt(int row, int col) {
        Object obj = getObject(row, col);
        if (obj instanceof Integer)
            return(((Integer)obj).intValue());
        else    
            return(((BigDecimal)obj ).intValue());
    }

    public String getString(int row, int col) {
        
        Object obj = getObject(row, col);
        if (obj instanceof BigDecimal) {
            BigDecimal big = (BigDecimal) obj;
            return("" + getDouble(row, col)); 
        }
        else if (obj instanceof Integer)
            return("" + getInt(row, col));
        else if ((obj instanceof Date) || (obj instanceof Timestamp))
            return("" + getDate(row, col));
        else 
        {
            String s = (String) obj; 
            return(s);
        }
    }

    public Object getObject(int row, int col) {
        int index = (row * _columnCount) + col;
        return( _resultsList.get ( index ) );
    }

}

