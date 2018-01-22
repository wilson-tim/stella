/**
 * File Name : SystemConfigRuntimeException.java Created on 14-Jan-2005
 * 
 * @author Dwood
 * 
 * Represents a runtime exception of the SystemConfig class (e.g. initialisation
 * exception)
 */
package uk.co.firstchoice.common.base.config.properties;

public class SystemConfigException extends RuntimeException {

    /**
     * Default constructor. Initalises an empty LoggerFactoryRuntimeException
     * object
     */
    public SystemConfigException() {
        super();
    }

    /**
     * Construct a SystemConfigRuntimeException with a specific error message
     * 
     * @param msg
     */
    public SystemConfigException(String msg) {
        super(msg);
    }

}