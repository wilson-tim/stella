package uk.co.firstchoice.common.base.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

/**
 * Resource bundles database specific objects. If your application supports
 * multiple databases and has different versions of a SQL statement to
 * accomodate database-specific SQL syntax, for example.
 * 
 * <p>
 * In this way, you can write code that is platform independent and extensible.
 * 
 * <p>
 * Each resource in the bundle contains the same items, but they differ per
 * database product dialiect. For example, with a resource bundle "MyResources",
 * you could have a "MyResources_Oracle" that contains Oracle-specific SQL, a
 * "MyResources_DB2" that contains DB2-specific SQL, and a "MyResources_MySQL"
 * that contains MySQL-specific syntax SQL.
 * <p>
 * Entries in the resource confirm to standard Properties syntax (in fact, they
 * are Properties). For example, <blockquote>
 * 
 * <pre>
 * 
 *  
 *   
 *    
 *     		####  SQL to return table information
 *    		table.sql=select t.owner,t.table_name,c.column_name,\
 *    			c.column_id,c.data_type,c.data_length,\
 *    			c.data_precision,c.data_scale,c.nullable \
 *    			from all_tables t, all_tab_columns c \
 *    			where t.owner = c.owner and \
 *    				t.table_name = c.table_name and \
 *    				t.owner like ? and \
 *    				t.table_name like ? \
 *    			order by 1, 2, 4
 *     
 *    
 *   
 *  
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * The database-specific extension name comes directly from the
 * getDatabaseProductName() from the JDBC driver DatabaseMetaData.
 * <p>
 * The properties files associated with the bundle are loaded as resources from
 * the classpath.
 */
public class SQLResourceBundle {
	private static HashMap _bundleCache = new HashMap();

	private Properties _defaultProperties = null;

	private Properties _dbSpecificProperties = null;

	public SQLResourceBundle() {
	}

	/**
	 * Provides a resource bundle given a database connection and a bundle name.
	 * Typically, the bundle name is the fully-qualified name of the class using
	 * it. For example,
	 * 
	 * <blockquote>
	 * 
	 * <pre>
	 * _sqlResourceBundle = SQLResourceBundle.getBundle(DatabaseMetaDataDAO.class
	 * 		.getName(), conn);
	 * String tableSQL = _sqlResourceBundle.getString(&quot;table.sql&quot;);
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * <p>
	 * By default, the System classloader is used to load the resource from the
	 * classpath.
	 * 
	 * @param baseName
	 * @param conn
	 * @return SQLResourceBundle
	 * @throws SQLException
	 * @throws IOException
	 */
	public static SQLResourceBundle getBundle(String baseName, Connection conn)
			throws SQLException, IOException {
		return SQLResourceBundle.getBundle(baseName, conn, ClassLoader
				.getSystemClassLoader());
	}

	/**
	 * Provides a resource bundle given a database connection and a bundle name.
	 * Typically, the bundle name is the fully-qualified name of the class using
	 * it. For example,
	 * 
	 * <blockquote>
	 * 
	 * <pre>
	 * _sqlResourceBundle = SQLResourceBundle.getBundle(DatabaseMetaDataDAO.class
	 * 		.getName(), conn);
	 * String tableSQL = _sqlResourceBundle.getString(&quot;table.sql&quot;);
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * <p>
	 * The loader provided is used to load the resource from the classpath.
	 * 
	 * @param baseName
	 * @param conn
	 * @param loader
	 * @return SQLResourceBundle
	 * @throws SQLException
	 * @throws IOException
	 */
	public static SQLResourceBundle getBundle(String baseName, Connection conn,
			ClassLoader loader) throws SQLException, IOException {
		if (conn == null)
			throw new IllegalArgumentException("Null connection not allowed.");
		if (baseName == null)
			throw new IllegalArgumentException("Null baseName not allowed.");
		if (loader == null)
			throw new IllegalArgumentException("Null loader not allowed.");

		SQLResourceBundle bundle = null;
		if (_bundleCache.containsKey(baseName)) {
			synchronized (_bundleCache) {
				bundle = (SQLResourceBundle) _bundleCache.get(baseName);
			}
		} else {
			bundle = new SQLResourceBundle();
			String defaultName = baseName.replace('.', '/');
			String dbSpecificName = defaultName + "_"
					+ conn.getMetaData().getDatabaseProductName();

			InputStream stream = loader.getResourceAsStream(defaultName
					+ ".properties");
			if (stream != null) {
				bundle._defaultProperties = new Properties();
				bundle._defaultProperties.load(stream);
			}

			stream = loader.getResourceAsStream(dbSpecificName + ".properties");
			if (stream != null) {
				bundle._dbSpecificProperties = new Properties();
				bundle._dbSpecificProperties.load(stream);
			}

			synchronized (_bundleCache) {
				_bundleCache.put(baseName, bundle);
			}
		}

		if (bundle._dbSpecificProperties == null
				&& bundle._defaultProperties == null) {
			throw new IllegalArgumentException("SQLResourceBundle not found: "
					+ baseName);
		}

		return bundle;
	}

	/**
	 * Returns string for a given key.
	 * 
	 * <p>
	 * Usage example: <blockquote>
	 * 
	 * <pre>
	 * _sqlResourceBundle = SQLResourceBundle.getBundle(DatabaseMetaDataDAO.class
	 * 		.getName(), conn);
	 * String tableSQL = _sqlResourceBundle.getString(&quot;table.sql&quot;);
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param keyName
	 * @return value
	 */
	public String getString(String keyName) {
		String value = null;

		if (_dbSpecificProperties != null
				&& _dbSpecificProperties.containsKey(keyName)) {
			value = _dbSpecificProperties.getProperty(keyName);
		} else if (_defaultProperties != null) {
			value = _defaultProperties.getProperty(keyName);
		}

		return value;
	}
}