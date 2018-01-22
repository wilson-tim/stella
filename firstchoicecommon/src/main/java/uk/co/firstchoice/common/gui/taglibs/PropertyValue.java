/**
 * <p>
 * This class helps displays a given parameter from the system.properties to the
 * gui
 * 
 * @author dwood
 */

package uk.co.firstchoice.common.gui.taglibs;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import uk.co.firstchoice.common.base.config.properties.SystemConfig;
import uk.co.firstchoice.common.base.logging.LogManager;
import uk.co.firstchoice.common.base.logging.Logger;

public class PropertyValue extends TagSupport {

	private String jndi = null;
	
	/**
	 * The logger used to log tracing messages to the log file.
	 */
	private static Logger logger = LogManager.getLogger();

	/**
	 * This is the default length of the string buffer that is used for
	 * constructing the tag. The value chosen should comfortably accomodate most
	 * of not all the fisrtchoice tags that are likely to be generated.
	 */
	public static final int TAG_BUFFER_LENGTH = 256;


	/**
	 * @return Returns the jndi.
	 */
	public String getJndi() {
		return jndi;
	}
	/**
	 * @param jndi The jndi to set.
	 */
	public void setJndi(String jndi) {
		this.jndi = jndi;
	}
	
	
	/**
	 * This method renders the start of the start of the tag with the inputted
	 * key property and property value.
	 * 
	 * @return Tag.SKIP_BODY - this value means the tag body will be skipped
	 * @exception JspException
	 *                Wraps any execption thrown during the output calls
	 */
	public int doStartTag() throws JspException {

		SystemConfig systemConfig = null;
		
		// get the system.properties details
		if ( jndi != null ) {
		systemConfig = SystemConfig.getInstance(jndi);
		} else {
			systemConfig = SystemConfig.getInstance();
		}

		StringBuffer tagText = new StringBuffer(TAG_BUFFER_LENGTH);
		//  get the parameter
		if (this.id != null && this.id.length() > 0 ) {
		    final String keyValue = systemConfig.getParameter(this.id);

		    // build the tag text
			if (keyValue != null) {
				tagText.append(keyValue).append("");
			}
		}


		// send the tag text to the output
		try {
			JspWriter out = pageContext.getOut();
			out.print(tagText);
		} catch (IOException ioe) {
			logger.log(Logger.ERROR, new StringBuffer( "doStartTag() throws IOException"), ioe);
			throw new JspTagException(ioe.toString());
		}

		return Tag.SKIP_BODY;
	}

	/**
	 * Finalises the rendering of the tag
	 * 
	 * @return Tag.EVAL_PAGE - this value allows the evaluation of rest of page
	 * @exception JspException
	 *                Wraps any execption thrown during the output calls
	 */
	public int doEndTag() throws JspTagException {
		return Tag.EVAL_PAGE;
	}

}