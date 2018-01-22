/*
 * Created on 25-Feb-2005
 * filename: SystemProperties.java
 */
package uk.co.firstchoice.common.base.config.properties;

/**
 * @author Dwood
 * 
 * Interface for SystemConfig and child LocalSystemConfig objects
 */
public interface SystemProperties {
    /**
     * Get the value of the system configuration property
     * 
     * @param param
     *            The type (i.e. name) of the system configuration property
     * @return String The String value of the property
     */
    public abstract String getParameter(Parameter param);

    /**
     * Get the value of the system configuration property
     * 
     * @param String
     *            The type (i.e. name) of the system configuration property
     * @return String The String value of the property
     */
    public abstract String getParameter(String paramName);
}