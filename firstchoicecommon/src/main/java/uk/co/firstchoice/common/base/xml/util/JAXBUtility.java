package uk.co.firstchoice.common.base.xml.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Collection of JAXB Utility functions.
 * 
 *  
 */
public class JAXBUtility {

	//  Prevent instantiation.
	protected JAXBUtility() {
	}

	/**
	 * Convenience one-liner to return a JAXB binded XML object.
	 * 
	 * @param jaxbPackageName --
	 *            package where generated JAXB classes reside
	 * @param xmlDocumentFile --
	 *            file containing XML document
	 * @return XML Object
	 * @throws FileNotFoundException
	 * @throws JAXBException
	 *  
	 */
	public static Object getJaxbXmlObject(String jaxbPackageName,
			File xmlDocumentFile) throws FileNotFoundException, JAXBException {
		if (xmlDocumentFile == null)
			throw new IllegalArgumentException(
					"Null xmlDocumentFile not allowed.");
		return JAXBUtility.getJaxbXmlObject(jaxbPackageName,
				new FileInputStream(xmlDocumentFile));
	}

	/**
	 * Convenience one-liner to return a JAXB binded XML Object
	 * 
	 * @param jaxbPackageName --
	 *            package where generated JAXB classes reside
	 * @param xmlContent --
	 *            XML document content as string
	 * @return XML Object
	 * @throws JAXBException
	 *  
	 */
	public static Object getJaxbXmlObject(String jaxbPackageName,
			String xmlContent) throws JAXBException {
		if (xmlContent == null)
			throw new IllegalArgumentException("Null xmlContent not allowed.");
		return JAXBUtility.getJaxbXmlObject(jaxbPackageName,
				new ByteArrayInputStream(xmlContent.getBytes()));
	}

	/**
	 * Convenience one-liner to returns a JAXB binded XML object.
	 * 
	 * @param jaxbPackageName --
	 *            package where generated JAXB classes reside
	 * @param xmlDocumentInputStream --
	 *            input stream for XML document
	 * @return XML Object
	 * @throws JAXBException
	 *  
	 */
	public static Object getJaxbXmlObject(String jaxbPackageName,
			InputStream xmlDocumentInputStream) throws JAXBException {
		if (jaxbPackageName == null)
			throw new IllegalArgumentException(
					"Null jaxbPackageName not allowed.");
		if (xmlDocumentInputStream == null)
			throw new IllegalArgumentException(
					"Null xmlDocumentInputStream not allowed.");

		JAXBContext jc = JAXBContext.newInstance(jaxbPackageName);
		Unmarshaller u = jc.createUnmarshaller();
		return u.unmarshal(xmlDocumentInputStream);
	}

	/**
	 * Convenience one-liner to flush an XML document from a JAXB binded object
	 * to a file.
	 * 
	 * @param jaxbPackageName
	 * @param xmlObject
	 * @param fileName
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 *  
	 */
	public static void flushXmlToFile(String jaxbPackageName, Object xmlObject,
			String fileName) throws JAXBException, FileNotFoundException {
		if (fileName == null)
			throw new IllegalArgumentException("Null fileName not allowed.");
		JAXBUtility.flushXmlToFile(jaxbPackageName, xmlObject, new File(
				fileName));
	}

	/**
	 * Convenience one-liner to flush an XML document from a JAXB binded object
	 * to a string.
	 * 
	 * @param jaxbPackageName
	 * @param xmlObject
	 * @param maxSize
	 * @return XML content
	 * @throws JAXBException
	 */
	public static String flushXmlToString(String jaxbPackageName,
			Object xmlObject) throws JAXBException {
		return JAXBUtility.flushXmlToString(jaxbPackageName, xmlObject,
				DEFAULT_MAX_XML_SIZE);
	}

	/**
	 * Convenience one-liner to flush an XML document from a JAXB binded object
	 * to a string.
	 * 
	 * @param jaxbPackageName
	 * @param xmlObject
	 * @param maxSize
	 * @return XML content
	 * @throws JAXBException
	 *  
	 */
	public static String flushXmlToString(String jaxbPackageName,
			Object xmlObject, int maxSize) throws JAXBException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(maxSize);
		JAXBUtility.flushXmlToStream(jaxbPackageName, xmlObject, outputStream);

		return outputStream.toString();
	}

	/**
	 * Convenience one-liner to flush an XML document from a JAXB binded object
	 * to a file.
	 * 
	 * @param jaxbPackageName
	 * @param xmlObject
	 * @param file
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 *  
	 */
	public static void flushXmlToFile(String jaxbPackageName, Object xmlObject,
			File file) throws JAXBException, FileNotFoundException {
		if (file == null)
			throw new IllegalArgumentException("Null file not allowed.");
		JAXBUtility.flushXmlToStream(jaxbPackageName, xmlObject,
				new FileOutputStream(file));
	}

	/**
	 * Convenience one-liner to flush an XML document from a JAXB binded object
	 * to an output stream of any type.
	 * 
	 * @param jaxbPackageName
	 * @param xmlObject
	 * @param output
	 * @throws JAXBException
	 *  
	 */
	public static void flushXmlToStream(String jaxbPackageName,
			Object xmlObject, OutputStream output) throws JAXBException {
		if (jaxbPackageName == null)
			throw new IllegalArgumentException(
					"Null jaxbPackageName not allowed.");
		if (xmlObject == null)
			throw new IllegalArgumentException("Null xmlObject not allowed.");
		if (output == null)
			throw new IllegalArgumentException(
					"Null output stream not allowed.");

		JAXBContext jc = JAXBContext.newInstance(jaxbPackageName);
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(xmlObject, output);
	}

	public static final int DEFAULT_MAX_XML_SIZE = 262144; // 256 KB
}