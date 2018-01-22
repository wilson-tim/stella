/*
 * Created on 20-Jan-2005
 * 
 * class to define a typesafe enumerated type that represents a key into the
 * system configuration file
 */
package uk.co.firstchoice.common.base.config.properties;

public final class Parameter {
    // Final member representing the name of the parameter. Made final to stop
    // clients modifying the value
    public final String name;

    // The default value for this parameter
    // if not set the parameter must be present in the configuration
    public final String defaultValue;

    public final Parameter defaultParameter;

    /**
     * Protected constructor to stop clients from creating instanced of the
     * class
     * 
     * @param name
     *            The name of the parameter key representing the system property
     * @param defaultValue
     *            String
     */
    protected Parameter(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.defaultParameter = null;
    }

    /**
     * Protected constructor to stop clients from creating instanced of the
     * class
     * 
     * @param name
     *            The name of the parameter key representing the system property
     * @param defaultParameter
     *            The Parameter instance to default to if no value can be found
     *            for this name.
     */
    protected Parameter(String name, Parameter defaultParameter) {
        this.name = name;
        this.defaultValue = null;
        this.defaultParameter = defaultParameter;
    }

    /**
     * Protected constructor to stop clients from creating instanced of the
     * class
     * 
     * @param name
     *            The name of the parameter key representing the system property
     */
    protected Parameter(String name) {
        this.name = name;
        this.defaultValue = null;
        this.defaultParameter = null;
    }

    /**
     * Overridden toString method to print out the state of the object
     * 
     * @return The object's internal state
     */
    public String toString() {
        return (new StringBuffer("{name=").append(name)
                .append(",defaultValue=").append(defaultValue).append(
                        ",defaultParameter=").append(defaultParameter)
                .append("}")).toString();
    }
}