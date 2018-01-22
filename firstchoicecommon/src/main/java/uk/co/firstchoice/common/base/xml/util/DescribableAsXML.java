package uk.co.firstchoice.common.base.xml.util;

/**
 * Specification for making objects capable of providing a description of
 * themselves in valid XML.
 * 
 *  
 */
public interface DescribableAsXML {
	/**
	 * Provides XML Element Text for a given instance.
	 * 
	 * @return XML Text
	 *  
	 */
	public String describeAsXMLElement();

	/**
	 * Provides XML Element Text for a given instance.
	 * 
	 * @return XML Text
	 *  
	 */
	public String describeAsXMLElement(String fieldName);

	/**
	 * Provides XML Document Text for a given instance.
	 * 
	 * @return XML Text
	 *  
	 */
	public String describeAsXMLDocument();
}