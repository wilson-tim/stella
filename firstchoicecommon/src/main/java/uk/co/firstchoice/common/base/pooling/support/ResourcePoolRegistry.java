package uk.co.firstchoice.common.base.pooling.support;

import java.util.HashMap;

import uk.co.firstchoice.common.base.pooling.ResourceException;
import uk.co.firstchoice.common.base.pooling.ResourcePool;

public class ResourcePoolRegistry {

	/** Enforce single instantiation rule */
	private static ResourcePoolRegistry _registry = null;

	private static Object _lockRegistry = new Object();

	/** Registry Entries */
	private HashMap _registryEntry = new HashMap();

	public ResourcePoolRegistry() throws ResourceException {
		synchronized (_lockRegistry) {
			if (_registry == null) {
				_registry = this;
			} else
				throw new ResourceException(
						"Only one resource pool registry allowed.");
		}
	}

	/**
	 * Provides the singleton occurance of the Resource Pool Registry.
	 * 
	 * @return Pool Registry
	 */
	public static ResourcePoolRegistry getResourcePoolRegistry()
			throws ResourceException {
		if (_registry == null) {
			synchronized (_lockRegistry) {
				_registry = new ResourcePoolRegistry();
			}
		}

		return _registry;
	}

	/**
	 * Registers a new resource pool and checks that it's the only pool of it's
	 * type in the registry.
	 * 
	 * @param Resource
	 *            Pool
	 */
	public void registerPool(ResourcePool pool) throws ResourceException {
		String poolName = pool.getPoolName();

		synchronized (_lockRegistry) {
			if (_registryEntry.containsKey(poolName))
				throw new ResourceException("Only one occurance of pool "
						+ poolName + " allowed.");

			_registryEntry.put(poolName, pool);
		}

	}

	/**
	 * UnRegisters a new resource pool.
	 * 
	 * @param Resource
	 *            Pool
	 */
	public void unRegisterPool(ResourcePool pool) throws ResourceException {
		String poolName = pool.getClass().getName();
		this.getResourcePool(poolName);
	}

	/**
	 * UnRegisters a new resource pool.
	 * 
	 * @param Resource
	 *            Pool Class
	 */
	public void unRegisterPool(Class classObject) throws ResourceException {
		String poolName = classObject.getName();
		this.getResourcePool(poolName);
	}

	/**
	 * UnRegisters a new resource pool.
	 * 
	 * @param Resource
	 *            Pool Name
	 */
	public void unRegisterPool(String poolName) throws ResourceException {
		synchronized (_lockRegistry) {
			if (_registryEntry.containsKey(poolName))
				_registryEntry.remove(poolName);
			else
				throw new ResourceException("No occurances of pool " + poolName
						+ " allowed.");
		}
	}

	/**
	 * Provides a given resource pool. Produces an exception if the pool doesn't
	 * exist.
	 * 
	 * @param pool
	 *            name
	 */
	public ResourcePool getResourcePool(String poolName)
			throws ResourceException {
		ResourcePool pool = null;

		synchronized (_lockRegistry) {
			if (_registryEntry.containsKey(poolName)) {
				Object obj = _registryEntry.get(poolName);
				pool = (ResourcePool) obj;
			} else
				pool = null;
		}

		return pool;
	}

}