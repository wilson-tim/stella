/*
 * Created on 23-Feb-2005
 * File name: LocalSystemConfig.java
 * 
 */
package uk.co.firstchoice.viking.pax.config;

import uk.co.firstchoice.common.base.config.properties.Parameter;
import uk.co.firstchoice.common.base.config.properties.SystemConfig;
import uk.co.firstchoice.common.base.config.properties.SystemProperties;

/**
 * @author Dwood Local instance of SystemConfig, defines local parameters for
 *         this application
 */
public class LocalSystemConfig extends SystemConfig implements SystemProperties {

	/**
	 * servwer name
	 */
	public static final Parameter SERVER_ID = createParameter("server.name");

	/**
	 * database sid
	 */
	public static final Parameter DATABASE_SID = createParameter("database.sid");

	/**
	 * application name
	 */
	public static final Parameter APP_NAME = createParameter("application.name");
}