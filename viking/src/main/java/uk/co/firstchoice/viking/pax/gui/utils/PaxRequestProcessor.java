/*
 * Created on 13-Apr-2005
 * filename: PaxRequestProcessor.java
 */
package uk.co.firstchoice.viking.pax.gui.utils;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.MappedPropertyDescriptor;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServletWrapper;
import org.apache.struts.action.RequestProcessor;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.upload.MultipartRequestHandler;
import org.apache.struts.upload.MultipartRequestWrapper;
import org.apache.struts.util.RequestUtils;

import uk.co.firstchoice.viking.gui.tmp.PropertyUtils;
import uk.co.firstchoice.viking.pax.gui.search.PaxEditForm;

/**
 * @author Dwood
 *
 * Custom request processor for Pax edit form:
 *  so that data is saved back to form
 * TODO - refactor this class into seperate objects
 */
public class PaxRequestProcessor extends RequestProcessor {

    /**
     * <p>Process an <code>HttpServletRequest</code> and create the
     * corresponding <code>HttpServletResponse</code>.</p>
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a processing exception occurs
     */
    public void process(HttpServletRequest request,
                        HttpServletResponse response)
        throws IOException, ServletException {

    	// Identify the path component we will use to select a mapping
    	String path = processPath(request, response);
        if (path == null) {
            return;
        }
        
        // Identify the mapping for this request
        ActionMapping mapping = processMapping(request, response, path);
        if (mapping == null) {
            return;
        }
        
        // Process any ActionForm bean related to this request
        ActionForm form = processActionForm(request, response, mapping);
        
       if ( form instanceof PaxEditForm ) {
        	this.processPopulate(request, response, form, mapping);
        } else {
        	super.process(request, response);
        }

    	
        // Wrap multipart requests with a special wrapper
        request = processMultipart(request);
        
        // Select a Locale for the current user if requested
        processLocale(request, response);

        // Set the content type and no-caching headers if requested
        processContent(request, response);
        processNoCache(request, response);

        // General purpose preprocessing hook
        if (!processPreprocess(request, response)) {
            return;
        }

        // Check for any role required to perform this action
        if (!processRoles(request, response, mapping)) {
            return;
        }
        
        if (!processValidate(request, response, form, mapping)) {
            return;
        }

        // Process a forward or include specified by this mapping
        if (!processForward(request, response, mapping)) {
            return;
        }
        if (!processInclude(request, response, mapping)) {
            return;
        }

        // Create or acquire the Action instance to process this request
        Action action = processActionCreate(request, response, mapping);
        if (action == null) {
            return;
        }

        // Call the Action instance itself
        ActionForward forward =
            processActionPerform(request, response,
                                 action, form, mapping);

        // Process the returned ActionForward instance
        processForwardConfig(request, response, forward);

    }

	
    /**
     * Populate the properties of the specified ActionForm instance from
     * the request parameters included with this request.  In addition,
     * request attribute <code>Globals.CANCEL_KEY</code> will be set if
     * the request was submitted with a button created by
     * <code>CancelTag</code>.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param form The ActionForm instance we are populating
     * @param mapping The ActionMapping we are using
     *
     * @exception ServletException if thrown by RequestUtils.populate()
     */
    protected void processPopulate(HttpServletRequest request,
                                   HttpServletResponse response,
                                   ActionForm form,
                                   ActionMapping mapping)
        throws ServletException {

        if (form == null) {
            return;
        }

        form.setServlet(this.servlet);
        form.reset(mapping, request);
        if (mapping.getMultipartClass() != null) {
            request.setAttribute(Globals.MULTIPART_KEY,
                                 mapping.getMultipartClass());
        }
        populate(form, mapping.getPrefix(), mapping.getSuffix(), request);

        // Set the cancellation request attribute if appropriate
        if ((request.getParameter(Constants.CANCEL_PROPERTY) != null) ||
            (request.getParameter(Constants.CANCEL_PROPERTY_X) != null)) {
            request.setAttribute(Globals.CANCEL_KEY, Boolean.TRUE);
        }
    }


    /**
     * Populate the properties of the specified JavaBean from the specified
     * HTTP request, based on matching each parameter name (plus an optional
     * prefix and/or suffix) against the corresponding JavaBeans "property
     * setter" methods in the bean's class.  Suitable conversion is done for
     * argument types as described under <code>setProperties()</code>.
     * <p>
     * If you specify a non-null <code>prefix</code> and a non-null
     * <code>suffix</code>, the parameter name must match <strong>both</strong>
     * conditions for its value(s) to be used in populating bean properties.
     * If the request's content type is "multipart/form-data" and the
     * method is "POST", the HttpServletRequest object will be wrapped in
     * a MultipartRequestWrapper object.
     *
     * @param bean The JavaBean whose properties are to be set
     * @param prefix The prefix (if any) to be prepend to bean property
     *               names when looking for matching parameters
     * @param suffix The suffix (if any) to be appended to bean property
     *               names when looking for matching parameters
     * @param request The HTTP request whose parameters are to be used
     *                to populate bean properties
     *
     * @exception ServletException if an exception is thrown while setting
     *            property values
     */
    public static void populate (
        Object bean,
        String prefix,
        String suffix,
        HttpServletRequest request)
        throws ServletException {

        // Build a list of relevant request parameters from this request
        HashMap properties = new HashMap();
        // Iterator of parameter names
        Enumeration names = null;
        // Map for multipart parameters
        Map multipartParameters = null;

        String contentType = request.getContentType();
        String method = request.getMethod();
        boolean isMultipart = false;

        if ((contentType != null)
            && (contentType.startsWith("multipart/form-data"))
            && (method.equalsIgnoreCase("POST"))) {

            // Get the ActionServletWrapper from the form bean
            ActionServletWrapper servlet;
            if (bean instanceof ActionForm) {
                servlet = ((ActionForm) bean).getServletWrapper();
            } else {
                throw new ServletException(
                    "bean that's supposed to be "
                        + "populated from a multipart request is not of type "
                        + "\"org.apache.struts.action.ActionForm\", but type "
                        + "\""
                        + bean.getClass().getName()
                        + "\"");
            }

            // Obtain a MultipartRequestHandler
            MultipartRequestHandler multipartHandler = getMultipartHandler(request);

            // Set the multipart request handler for our ActionForm.
            // If the bean isn't an ActionForm, an exception would have been
            // thrown earlier, so it's safe to assume that our bean is
            // in fact an ActionForm.
             ((ActionForm) bean).setMultipartRequestHandler(multipartHandler);

            if (multipartHandler != null) {
                isMultipart = true;
                // Set servlet and mapping info
                servlet.setServletFor(multipartHandler);
                multipartHandler.setMapping(
                    (ActionMapping) request.getAttribute(Globals.MAPPING_KEY));
                // Initialize multipart request class handler
                multipartHandler.handleRequest(request);
                //stop here if the maximum length has been exceeded
                Boolean maxLengthExceeded =
                    (Boolean) request.getAttribute(
                        MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);
                if ((maxLengthExceeded != null) && (maxLengthExceeded.booleanValue())) {
                    return;
                }
                //retrive form values and put into properties
                multipartParameters = getAllParametersForMultipartRequest(
                        request, multipartHandler);
                names = Collections.enumeration(multipartParameters.keySet());
            }
        }

        if (!isMultipart) {
            names = request.getParameterNames();
        }

        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String stripped = name;
            if (prefix != null) {
                if (!stripped.startsWith(prefix)) {
                    continue;
                }
                stripped = stripped.substring(prefix.length());
            }
            if (suffix != null) {
                if (!stripped.endsWith(suffix)) {
                    continue;
                }
                stripped = stripped.substring(0, stripped.length() - suffix.length());
            }
            if (isMultipart) {
                properties.put(stripped, multipartParameters.get(name));
            } else {
                properties.put(stripped, request.getParameterValues(name));
            }
        }

        // Set the corresponding properties of our bean
        try {
            populateBean(bean, properties);
        } catch (Exception e) {
            throw new ServletException("BeanUtils.populate", e);
        }

    }
    

    /**
     * <p>Populate the JavaBeans properties of the specified bean, based on
     * the specified name/value pairs.  This method uses Java reflection APIs
     * to identify corresponding "property setter" method names, and deals
     * with setter arguments of type <code>String</code>, <code>boolean</code>,
     * <code>int</code>, <code>long</code>, <code>float</code>, and
     * <code>double</code>.  In addition, array setters for these types (or the
     * corresponding primitive types) can also be identified.</p>
     * 
     * <p>The particular setter method to be called for each property is
     * determined using the usual JavaBeans introspection mechanisms.  Thus,
     * you may identify custom setter methods using a BeanInfo class that is
     * associated with the class of the bean itself.  If no such BeanInfo
     * class is available, the standard method name conversion ("set" plus
     * the capitalized name of the property in question) is used.</p>
     * 
     * <p><strong>NOTE</strong>:  It is contrary to the JavaBeans Specification
     * to have more than one setter method (with different argument
     * signatures) for the same property.</p>
     *
     * <p><strong>WARNING</strong> - The logic of this method is customized
     * for extracting String-based request parameters from an HTTP request.
     * It is probably not what you want for general property copying with
     * type conversion.  For that purpose, check out the
     * <code>copyProperties()</code> method instead.</p>
     *
     * @param bean JavaBean whose properties are being populated
     * @param properties Map keyed by property name, with the
     *  corresponding (String or String[]) value(s) to be set
     *
     * @exception IllegalAccessException if the caller does not have
     *  access to the property accessor method
     * @exception InvocationTargetException if the property accessor method
     *  throws an exception
     */
    public static void populateBean(Object bean, Map properties)
        throws IllegalAccessException, InvocationTargetException {

        // Do nothing unless both arguments have been specified
        if ((bean == null) || (properties == null)) {
            return;
        }


        // Loop through the property name/value pairs to be set
        Iterator names = properties.keySet().iterator();
        while (names.hasNext()) {

            // Identify the property name and value(s) to be assigned
            String name = (String) names.next();
            if (name == null) {
                continue;
            }
            Object value = properties.get(name);

            // Perform the assignment for this property
            setProperty(bean, name, value);

        }

    }
    
    

    /**
     * <p>Set the specified property value, performing type conversions as
     * required to conform to the type of the destination property.</p>
     *
     * <p>If the property is read only then the method returns 
     * without throwing an exception.</p>
     *
     * <p>If <code>null</code> is passed into a property expecting a primitive value,
     * then this will be converted as if it were a <code>null</code> string.</p>
     *
     * <p><strong>WARNING</strong> - The logic of this method is customized
     * to meet the needs of <code>populate()</code>, and is probably not what
     * you want for general property copying with type conversion.  For that
     * purpose, check out the <code>copyProperty()</code> method instead.</p>
     *
     * @param bean Bean on which setting is to be performed
     * @param name Property name (can be nested/indexed/mapped/combo)
     * @param value Value to be set
     *
     * @exception IllegalAccessException if the caller does not have
     *  access to the property accessor method
     * @exception InvocationTargetException if the property accessor method
     *  throws an exception
     */
    public static void setProperty(Object bean, String name, Object value)
        throws IllegalAccessException, InvocationTargetException {

        // Resolve any nested expression to get the actual target bean
        Object target = bean;
        int delim = name.lastIndexOf(PropertyUtils.NESTED_DELIM);
        if (delim >= 0) {
            try {
                target =
                    PropertyUtils.getProperty(bean, name.substring(0, delim));
            } catch (NoSuchMethodException e) {
                return; // Skip this property setter
            }
            name = name.substring(delim + 1);

        }

        // Declare local variables we will require
        String propName = null;          // Simple name of target property
        Class type = null;               // Java type of target property
        int index = -1;                  // Indexed subscript value (if any)
        String key = null;               // Mapped key value (if any)

        // Calculate the property name, index, and key values
        propName = name;
        int i = propName.indexOf(PropertyUtils.INDEXED_DELIM);
        if (i >= 0) {
            int k = propName.indexOf(PropertyUtils.INDEXED_DELIM2);
            try {
                index =
                    Integer.parseInt(propName.substring(i + 1, k));
            } catch (NumberFormatException e) {
                ;
            }
            propName = propName.substring(0, i);
        }
        int j = propName.indexOf(PropertyUtils.MAPPED_DELIM);
        if (j >= 0) {
            int k = propName.indexOf(PropertyUtils.MAPPED_DELIM2);
            try {
                key = propName.substring(j + 1, k);
            } catch (IndexOutOfBoundsException e) {
                ;
            }
            propName = propName.substring(0, j);
        }


        PropertyDescriptor descriptor = null;
        try {
            descriptor =
                PropertyUtils.getPropertyDescriptor(target, name);
            if (descriptor == null) {
                return; // Skip this property setter
            }
        } catch (NoSuchMethodException e) {
            return; // Skip this property setter
        }
        if (descriptor instanceof MappedPropertyDescriptor) {
            if (((MappedPropertyDescriptor) descriptor).getMappedWriteMethod() == null) {

                return; // Read-only, skip this property setter
            }
            type = ((MappedPropertyDescriptor) descriptor).
                getMappedPropertyType();
        } else if (descriptor instanceof IndexedPropertyDescriptor) {
            if (((IndexedPropertyDescriptor) descriptor).getIndexedWriteMethod() == null) {

                return; // Read-only, skip this property setter
            }
            type = ((IndexedPropertyDescriptor) descriptor).
                getIndexedPropertyType();
        } else {
            if (descriptor.getWriteMethod() == null) {
                if (log.isDebugEnabled()) {
                    log.debug("Skipping read-only property");
                }
                return; // Read-only, skip this property setter
            }
            type = descriptor.getPropertyType();
        }
        

        // Convert the specified value to the required type
        Object newValue = null;
        if (type.isArray() && (index < 0)) { // Scalar value into array
            if (value == null) {
                String values[] = new String[1];
                values[0] = (String) value;
                newValue = ConvertUtils.convert((String[]) values, type);
            } else if (value instanceof String) {
                String values[] = new String[1];
                values[0] = (String) value;
                newValue = ConvertUtils.convert((String[]) values, type);
            } else if (value instanceof String[]) {
                newValue = ConvertUtils.convert((String[]) value, type);
            } else {
                newValue = value;
            }
        } else if (type.isArray()) {         // Indexed value into array
            if (value instanceof String) {
                newValue = ConvertUtils.convert((String) value,
                                                type.getComponentType());
            } else if (value instanceof String[]) {
                newValue = ConvertUtils.convert(((String[]) value)[0],
                                                type.getComponentType());
            } else {
                newValue = value;
            }
        } else {                             // Value into scalar
            if ((value instanceof String) || (value == null)) {
                newValue = ConvertUtils.convert((String) value, type);
            } else if (value instanceof String[]) {
                newValue = ConvertUtils.convert(((String[]) value)[0],
                                                type);
            } else if (ConvertUtils.lookup(value.getClass()) != null) {
                newValue = ConvertUtils.convert(value.toString(), type);
            } else {
                newValue = value;
            }
        }

        // Invoke the setter method
        try {
            if (index >= 0) {
                PropertyUtils.setIndexedProperty(target, propName,
                                                 index, newValue);
            } else if (key != null) {
                PropertyUtils.setMappedProperty(target, propName,
                                                key, newValue);
            } else {
                PropertyUtils.setProperty(target, propName, newValue);
            }
        } catch (NoSuchMethodException e) {
            throw new InvocationTargetException
                (e, "Cannot set " + propName);
        }

    }


    /**
     * Create a map containing all of the parameters supplied for a multipart
     * request, keyed by parameter name. In addition to text and file elements
     * from the multipart body, query string parameters are included as well.
     *
     * @param request The (wrapped) HTTP request whose parameters are to be
     *                added to the map.
     * @param multipartHandler The multipart handler used to parse the request.
     *
     * @return the map containing all parameters for this multipart request.
     */
    private static Map getAllParametersForMultipartRequest(
            HttpServletRequest request,
            MultipartRequestHandler multipartHandler) {
        Map parameters = new HashMap();
        Enumeration enumerator;

        Hashtable elements = multipartHandler.getAllElements();
        enumerator = elements.keys();
        while (enumerator.hasMoreElements()) {
            String key = (String) enumerator.nextElement();
            parameters.put(key, elements.get(key));
        }

        if (request instanceof MultipartRequestWrapper) {
            request = ((MultipartRequestWrapper)request).getRequest();
            enumerator = request.getParameterNames();
            while (enumerator.hasMoreElements()) {
                String key = (String) enumerator.nextElement();
                parameters.put(key, request.getParameterValues(key));
            }
        }

        return parameters;
    }
    

    /**
     * Try to locate a multipart request handler for this request. First, look
     * for a mapping-specific handler stored for us under an attribute. If one
     * is not present, use the global multipart handler, if there is one.
     *
     * @param request The HTTP request for which the multipart handler should
     *                be found.
     * @return the multipart handler to use, or <code>null</code> if none is
     *         found.
     *
     * @exception ServletException if any exception is thrown while attempting
     *                             to locate the multipart handler.
     */
    private static MultipartRequestHandler getMultipartHandler(HttpServletRequest request)
        throws ServletException {

        MultipartRequestHandler multipartHandler = null;
        String multipartClass = (String) request.getAttribute(Globals.MULTIPART_KEY);
        request.removeAttribute(Globals.MULTIPART_KEY);

        // Try to initialize the mapping specific request handler
        if (multipartClass != null) {
            try {
                multipartHandler = (MultipartRequestHandler) RequestUtils.applicationInstance(multipartClass);
            } catch (ClassNotFoundException cnfe) {
                log.error(
                    "MultipartRequestHandler class \""
                        + multipartClass
                        + "\" in mapping class not found, "
                        + "defaulting to global multipart class");
            } catch (InstantiationException ie) {
                log.error(
                    "InstantiaionException when instantiating "
                        + "MultipartRequestHandler \""
                        + multipartClass
                        + "\", "
                        + "defaulting to global multipart class, exception: "
                        + ie.getMessage());
            } catch (IllegalAccessException iae) {
                log.error(
                    "IllegalAccessException when instantiating "
                        + "MultipartRequestHandler \""
                        + multipartClass
                        + "\", "
                        + "defaulting to global multipart class, exception: "
                        + iae.getMessage());
            }

            if (multipartHandler != null) {
                return multipartHandler;
            }
        }

        ModuleConfig moduleConfig = (ModuleConfig) request.getAttribute(Globals.MODULE_KEY);
        multipartClass = moduleConfig.getControllerConfig().getMultipartClass();

        // Try to initialize the global request handler
        if (multipartClass != null) {
            try {
                multipartHandler = (MultipartRequestHandler) RequestUtils.applicationInstance(multipartClass);
            } catch (ClassNotFoundException cnfe) {
                throw new ServletException(
                    "Cannot find multipart class \""
                        + multipartClass
                        + "\""
                        + ", exception: "
                        + cnfe.getMessage());
            } catch (InstantiationException ie) {
                throw new ServletException(
                    "InstantiaionException when instantiating "
                        + "multipart class \""
                        + multipartClass
                        + "\", exception: "
                        + ie.getMessage());
            } catch (IllegalAccessException iae) {
                throw new ServletException(
                    "IllegalAccessException when instantiating "
                        + "multipart class \""
                        + multipartClass
                        + "\", exception: "
                        + iae.getMessage());
            }

            if (multipartHandler != null) {
                return multipartHandler;
            }
        }

        return multipartHandler;
    }

}