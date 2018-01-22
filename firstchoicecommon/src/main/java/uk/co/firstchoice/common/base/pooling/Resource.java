package uk.co.firstchoice.common.base.pooling;

import java.util.Properties;

/**
 * Interface for generic resource implementations.
 *  
 */
public interface Resource {

	/**
	 * Initialize Resource. This method is called after a resource is
	 * instantiated.
	 * 
	 * <p>
	 * The properties are intended to be anything the resource might need to
	 * function properly. For example, pooled JDBC connections need to know the
	 * driver name, connection URL, userid and password.
	 * <P>
	 * 
	 * @param Resource
	 *            Creation Properties
	 */
	public void initialize(Properties props) throws ResourceException;

	/**
	 * Terminate Resource. Any termination logic should be implemented here.
	 * 
	 * <p>
	 * For example, pooled JDBC connections need to issue a close().
	 * <P>
	 */
	public void terminate() throws ResourceException;

	/**
	 * Tests validity of Resource. This is necessary so that invalid resources
	 * can be disposed of and aren't doled out to your application.
	 * <P>
	 * 
	 * @return True if resource is valid and usable, false if not.
	 */
	public boolean isValid() throws ResourceException;

}

