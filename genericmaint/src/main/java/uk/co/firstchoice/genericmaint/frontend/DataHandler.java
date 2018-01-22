/*
 * dataHandler.java
 *
 * Created on 19 May 2003, 16:45
 */

package uk.co.firstchoice.genericmaint.frontend;

import java.util.*;
import java.sql.*;
import uk.co.firstchoice.fcutil.*;
import javax.servlet.http.*;

/**
 *
 * @author  Lsvensson
 */
public class DataHandler {
	
	/** Creates a new instance of dataHandler */
	public DataHandler() {
	}
	
	public Vector getData(MaintRecord mr) throws MaintException {
		OracleComUtils ocu = null;
		CallableStatement cstmt = null;
		ResultSet rset = null;
		ResultSet primaryKeys = null;
		try {
			ocu = new OracleComUtils(mr.getDatasource());
			cstmt = ocu.getCallableStatement("{ ? = call "+mr.getSchemaOwner()+".p_"+mr.getApplicationName()+"_maint."+mr.getTableName()+"_GetAll() }");
			cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
			cstmt.execute();
			rset = (ResultSet)cstmt.getObject(1);
			ResultSetMetaData rsmd = rset.getMetaData();
			DatabaseMetaData dbmd = ocu.getConnection().getMetaData();
			Vector dataVector = new Vector();
			if (mr.getMetaData().size() == 0) {		// only populate once

				primaryKeys = dbmd.getPrimaryKeys(null, mr.getSchemaOwner()/*mr.getApplicationName()*/, mr.getTableName());
				LinkedList pkList = new LinkedList();
				while (primaryKeys.next()) {
					pkList.add(primaryKeys.getString("COLUMN_NAME").toUpperCase());
				}
				DbColumnMetadata dbcmd = null;
				for (int i=1; i<=rsmd.getColumnCount(); i++) {
					dbcmd = new DbColumnMetadata();
					dbcmd.setBlankAllowed(rsmd.isNullable(i)==rsmd.columnNullable);
					dbcmd.setColumnDisplayLabel(rsmd.getColumnLabel(i).replaceAll("_", "<br>"));
					dbcmd.setColumnName(rsmd.getColumnName(i).toUpperCase());
					dbcmd.setDataType(rsmd.getColumnType(i));
					dbcmd.setDecimalPlaces(rsmd.getScale(i));
					dbcmd.setColumnWidth(dbcmd.isDate()?10:rsmd.getColumnDisplaySize(i));
					dbcmd.setPrimaryKey(pkList.contains(dbcmd.getColumnName()));
					mr.getMetaData().add(dbcmd);
				}
			}
			Vector row = null;
			while (rset.next()) {
				row = new Vector();
				for (int i=1; i<=rsmd.getColumnCount(); i++) {
					if (((DbColumnMetadata)mr.getMetaData().get(i-1)).isDate()) {
						row.add(FCUtils.dateToString(rset.getDate(i)));
					} else {
						row.add(FCUtils.notNull(rset.getString(i)));
					}
				}
				dataVector.add(row);
			}
			return dataVector;
		}
		catch (SQLException e) {
			throw new MaintException("Error retrieving data from the data warehouse. Try restarting the application.", e);
		}
		catch (FCException e) {
			throw new MaintException(e.getMessage(), e.getCause());
		}
		finally {
			try {
				if (rset != null) rset.close();
				if (primaryKeys != null) primaryKeys.close();
				if (ocu != null) ocu.close();
			} catch (SQLException e) {}
		}
	}


	public void saveRecord(MaintRecord mr, HttpServletRequest request, String action) throws MaintException {
		OracleComUtils ocu = null;
		CallableStatement cstmt = null;
		try {
			String inParamInd = "";
			for (int i=0; i<mr.getMetaData().size(); i++)
				inParamInd += "?,";
			inParamInd += "?,?";			// An extras for action (A/U/D) and userName
			ocu = new OracleComUtils(mr.getDatasource());
			cstmt = ocu.getCallableStatement(
				"{ ? = call "+mr.getSchemaOwner()+".p_"+mr.getApplicationName()+
				"_maint."+mr.getTableName()+"_Save("+inParamInd+") }");
			cstmt.registerOutParameter(1, java.sql.Types.CHAR);
			for (int i=0; i<mr.getMetaData().size(); i++) {
				String param = request.getParameter("column"+i);
				if (param == null) {
					cstmt.setNull(i+2, ((DbColumnMetadata)mr.getMetaData().get(i)).getDataType());
				} else if (((DbColumnMetadata)mr.getMetaData().get(i)).isDate()) {
					cstmt.setDate(i+2, FCUtils.parseDate(param));
				} else {
					cstmt.setString(i+2, param);
				}
			}
			cstmt.setString(mr.getMetaData().size()+2, action);	// Add / Update / Delete
			cstmt.setString(mr.getMetaData().size()+3, request.getRemoteUser());
			cstmt.execute();
			String err = cstmt.getString(1);
			mr.setError((err==null?"":err));
		}
		catch (SQLException e) {
			throw new MaintException("Error saving data to the data warehouse. Try restarting the application.", e);
		}
		catch (FCException e) {
			throw new MaintException(e.getMessage(), e.getCause());
		}
		finally {
			if (ocu != null) ocu.close();
		}
	}


}