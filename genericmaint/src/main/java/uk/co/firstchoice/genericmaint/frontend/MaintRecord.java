/*
 * MaintRecord.java
 *
 * Created on 16 May 2003, 17:24
 */

package uk.co.firstchoice.genericmaint.frontend;

import java.util.*;
import java.sql.*;
import uk.co.firstchoice.fcutil.*;

/**
 *
 * @author  Lsvensson
 */

public class MaintRecord {

	private boolean valid = false;
	private String datasource = null;			//
	private String appTitle = null;				// xxxxx Maintenance
	private String userRoleForAccess = null;	// XXXXX_MAINTENANCE_USER
	private String applName = null;				// Stella
	private String tableName = null;			// Table
	private Vector metaData = null;
	private String error = "";
	private String configDwName = null;
	private String applDwName = null;
	private String schemaOwner = null;
	/** Creates a new instance of MaintRecord */
	public MaintRecord() {
	}
	
	/** Creates a new instance of MaintRecord */
	public MaintRecord(String appl, String table) throws MaintException {

		OracleComUtils ocu = null;
		CallableStatement cstmt = null;
		ResultSet rset = null;
		metaData = new Vector();
		try {


			ocu = new OracleComUtils("java:/comp/env/SecurityLookup");

			cstmt = ocu.getCallableStatement("{ ? = call p_util_maint.get_maint_gui_parameters(?,?) }");
		
			cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setString(2, appl);
			cstmt.setString(3, table);
			cstmt.execute();
			rset = (ResultSet)cstmt.getObject(1);
			if (rset.next()) {
				valid = true;
				applName = appl;
				tableName = table;
		
				datasource = rset.getString("gui_datasource_name");
				appTitle = rset.getString("page_title");
				schemaOwner = rset.getString("schema_owner");
				userRoleForAccess = rset.getString("user_role");
				Statement stmt = ocu.getStatement();
				rset = stmt.executeQuery("select * from global_name");
					
				if (rset.next())
					configDwName = rset.getString(1);
				configDwName = (null==configDwName?
									"N/A":
									(configDwName.length()>3?
										configDwName.substring(0,3):
										configDwName));
				ocu.close();
				ocu = new OracleComUtils(datasource);
				stmt = ocu.getStatement();
				rset = stmt.executeQuery("select * from global_name");
				if (rset.next())
					applDwName = rset.getString(1);
				applDwName = (null==applDwName?
									"N/A":
									(applDwName.length()>3?
										applDwName.substring(0,3):
										applDwName));
				
				System.out.println("*****applDwName***** " + applDwName);
				
			}
		}
		catch (SQLException e) {
			throw new MaintException("Error getting airline details from the data warehouse. Try restarting the application.", e);
		}
		catch (FCException e) {
			throw new MaintException(e.getMessage(), e.getCause());
		}
		finally {
			try {
				if (rset != null) rset.close();
				if (ocu != null) ocu.close();
			} catch (SQLException e) {}
		}
	}

	
	/** Getter for property datasource.
	 * @return Value of property datasource.
	 *
	 */
	public String getDatasource() {
		return datasource;
	}
	
	/** Getter for property appTitle.
	 * @return Value of property appTitle.
	 *
	 */
	public String getAppTitle() {
		return appTitle;
	}
	
	/** Getter for property tablename.
	 * @return Value of property tablename.
	 *
	 */
	public String getApplicationName() {
		return applName;
	}
	
	/** Getter for property tablename.
	 * @return Value of property tablename.
	 *
	 */
	public String getTableName() {
		return tableName;
	}
	
	/** Getter for property userRoleForAccess.
	 * @return Value of property userRoleForAccess.
	 *
	 */
	public String getUserRoleForAccess() {
		return userRoleForAccess;
	}
	
	/** Getter for property valid.
	 * @return Value of property valid.
	 *
	 */
	public boolean isValid() {
		return valid;
	}
	
	/** Getter for property metaData.
	 * @return Value of property metaData.
	 *
	 */
	public Vector getMetaData() {
		return metaData;
	}
	
	/** Getter for property error.
	 * @return Value of property error.
	 *
	 */
	public String getError() {
		return error;
	}
	
	/** Setter for property error.
	 * @param error New value of property error.
	 *
	 */
	public void setError(String error) {
		this.error = error;
	}
	
	/** Getter for property applDwName.
	 * @return Value of property applDwName.
	 *
	 */
	public String getApplDwName() {
		return applDwName;
	}	
	
	/** Getter for property configDwName.
	 * @return Value of property configDwName.
	 *
	 */
	public String getConfigDwName() {
		return configDwName;
	}
	
	/** Getter for property schemaOwner.
	 * @return Value of property schemaOwner.
	 *
	 */
	public java.lang.String getSchemaOwner() {
		return schemaOwner;
	}
	
	/** Setter for property schemaOwner.
	 * @param schemaName New value of property schemaOwner.
	 *
	 */
	public void setSchemaOwner(java.lang.String schemaOwner) {
		this.schemaOwner = schemaOwner;
	}
	
}