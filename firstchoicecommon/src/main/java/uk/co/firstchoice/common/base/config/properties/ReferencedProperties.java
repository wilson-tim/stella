/**
 * File Name : ReferencedProperties.java
 * 
 * Created on 14-Jan-2005
 * 
 * @author Dwood
 * 
 * A subclass of the standard java.util.Properties class that allows references
 * within the InputStream that is accepted within the
 * {@link #load(java.io.InputStream)}method.
 *  
 */
package uk.co.firstchoice.common.base.config.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Stack;

class ReferencedProperties extends Properties {

    /** Signal value to indicate the start of a reference */
    private static final char REF_SIGNAL = '$';

    /** Escape character for escaping REF_SIGNAL. */
    private static final char ESCAPE = '\\';

    /** Start of a reference to another property. */
    private static final String START_REFERENCE = REF_SIGNAL + "{";

    /** End of a reference to another property. */
    private static final String END_REFERENCE = "}";

    /** String representing an escaped signal value. */
    private static final String ESCAPED_SIGNAL = "" + ESCAPE + REF_SIGNAL;

    /**
     * Stack used to hold the current list of references, used to detect
     * circular references.
     */
    private Stack stack = new Stack();

    /**
     * List that contains all resolved values so we don't have to expand more
     * than once.
     */
    private List resolved = new ArrayList();

    /**
     * Overriden constructor.
     */
    public ReferencedProperties() {
        super();
    }

    /**
     * Overriden constructor.
     * 
     * @param defaults
     *            The set of defaults if a property is not available
     */
    public ReferencedProperties(Properties defaults) {
        super(defaults);
    }

    /**
     * Load the properties from the specified stream. Values for properties
     * defined in this stream may contain references to other properties of the
     * form ${some.other.property}. For example:
     * <P>
     * 
     * property.one=Some value <BR>
     * property.two=${property.one} <BR>
     * property.three=A value with ${property.one} and ${property.two}
     * <P>
     * 
     * <B>NOTE: </B> Circular references within the values of the properties
     * will be detected and an IllegalStateException will be thrown. For
     * example, the following set of properties would result in an
     * IllegalStateException:
     * <P>
     * 
     * <code>
     * property.one=A value<BR>
     * property.two=A property with a reference to ${property.three}<BR>
     * property.three=Uses ${property.two}  <B>ERROR!</B><P>
     * </code>
     * 
     * Additionally, if any property value contains an unclosed reference an
     * IllegalStateException is thrown.
     * <P>
     * 
     * <B>NOTE ON ESCAPING: </B> <BR>
     * To escape a '$' sign in a property value, preface it with a backslash
     * character, e.g.
     * <P>
     * 
     * <code>property.one=This is a value with a \\$ sign in it.</code>
     * <P>
     * 
     * Note the use of a double backslash which is required to escape the
     * backslash itself.
     * <P>
     * 
     * <B>NOTE ON KEYS: </B> <BR>
     * This subclass imposes constraints on keys read from the input stream. No
     * key in the property list may contain the Strings "${" or "}". So, for
     * example, the following line in a file read by this method would result in
     * an IllegalStateException:
     * <P>
     * 
     * <code>my.invalid.${property=an error!</code>
     * 
     * @param inStream
     *            The input stream from which to load the properties
     * @throws IOException
     *             If an exception occurs reading the stream
     * @throws IllegalStateException
     *             If there is a circular reference or unterminated reference in
     *             a value of a property, or if a key contains an illegal
     *             character
     */
    public synchronized void load(InputStream inStream) throws IOException {
        // load using superclass
        super.load(inStream);

        expandAllValues();

    }

    public synchronized void loadUnexpanded(InputStream inStream)
            throws IOException {
        // load using superclass
        super.load(inStream);

    }

    public void expandAllValues() throws IllegalStateException {
        for (Iterator i = this.keySet().iterator(); i.hasNext();) {
            String nextKey = (String) i.next();
            if (nextKey.indexOf(START_REFERENCE) > -1
                    || nextKey.indexOf(END_REFERENCE) > -1) {
                throw new IllegalStateException("Invalid key value: '"
                        + nextKey + "'");
            }
            expandValues(nextKey);

            if (this.stack.size() > 0) {
                throw new IllegalStateException(
                        "Inconsistent reference stack state");
            }
        }

        // clear the stack and resolved list
        this.stack.clear();
        this.resolved.clear();
    }

    /**
     * Performs the work of resolving all references within a given property
     * value based on the key.
     * 
     * @param propertyKey
     *            The key of the property in this object to resolve
     * @throws IllegalStateException
     *             if the value is malformed
     * @see #load(java.io.InputStream)
     */
    private void expandValues(String propertyKey) {

        // push this key onto the stack to remember we are processing it
        stack.push(propertyKey);

        String initialValue = this.getProperty(propertyKey);
        String value = initialValue;

        if (value == null) {
            String errMsg = new StringBuffer(
                    "Value not found while parsing properties: ${"
                            + propertyKey + "}").toString();
            System.out.println(errMsg);
            throw new IllegalStateException(errMsg);
        }

        int startAt = 0, startIndex = 0, endIndex = 0;
        while ((startIndex = value.indexOf(START_REFERENCE, startAt)) != -1) {

            // if the $ sign is escaped ignore it
            if (startIndex > 0 && value.charAt(startIndex - 1) == ESCAPE)
                continue;

            // get the first index of }, if not found throw exception
            endIndex = value.indexOf(END_REFERENCE, startIndex);
            if (endIndex == -1) {
                String errMsg = new StringBuffer(
                        "Found unterminated reference in property value: "
                                + propertyKey + "=" + initialValue).toString();
                System.out.println(errMsg);
                throw new IllegalStateException(errMsg);
            }

            // resolve the key to fetch
            String nextKey = value.substring(startIndex + 2, endIndex);

            // if the stack already contains the key, we have a circular
            // reference
            if (stack.contains(nextKey)) {
                String errMsg = new StringBuffer(
                        "circular reference detected for key=" + nextKey
                                + ", current stack=" + stack).toString();
                System.out.println(errMsg);

                throw new IllegalStateException(errMsg);
            }

            // we don't resolve a key if it's already done...
            if (!resolved.contains(nextKey)) {
                // recursive call to get the key
                expandValues(nextKey);
            }

            // create a new value with the expanded value
            String replacedVal = this.getProperty(nextKey);

            StringBuffer result = new StringBuffer();
            result.append(value.substring(0, startIndex));
            result.append(replacedVal);
            result.append(value.substring(endIndex + 1));
            value = result.toString();

            // increment startAt so next iteration starts after the replaced val
            startAt = startIndex + replacedVal.length();

        }

        // loop over, unescaping any escaped $ signs
        StringBuffer unescaper = new StringBuffer();
        startAt = 0;
        startIndex = 0;
        endIndex = 0;
        while ((startIndex = value.indexOf(ESCAPED_SIGNAL, startAt)) != -1) {
            unescaper.append(value.substring(startAt, startIndex));
            unescaper.append(REF_SIGNAL);
            startAt = startIndex + ESCAPED_SIGNAL.length();
        }
        unescaper.append(value.substring(startAt));
        value = unescaper.toString();

        this.setProperty(propertyKey, value);

        // remove current key from stack
        stack.pop();

        // add to resolved keys
        this.resolved.add(propertyKey);

        return;
    }
}