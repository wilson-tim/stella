package uk.co.firstchoice.common.base.pooling;

import java.util.Properties;

/**
 * Interface for Resource Pools. A default ResourcePool implementation can be
 * found in AbstractResourcePool.
 *  
 */
public interface ResourcePool {

	/**
	 * Defines how a resource is created.
	 */
	public Resource createResource(Properties props) throws ResourceException;

	/**
	 * Defines how a resource is recycled for use by others.
	 */
	public void recycleResource(Resource resource) throws ResourceException;

	/**
	 * Returns Resource Pool name. This a unique identifier that is used to
	 * distinguish one type of object pool from another.
	 * <P>
	 * 
	 * @return Name of resource.
	 */
	public String getPoolName() throws ResourceException;

	/**
	 * Gets a resource from the pool.
	 * 
	 * @return Resource
	 * @throws ResourceException
	 */
	public Resource getResource() throws ResourceException;

}

