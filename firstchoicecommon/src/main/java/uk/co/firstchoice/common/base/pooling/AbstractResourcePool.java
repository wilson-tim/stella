package uk.co.firstchoice.common.base.pooling;

import java.util.Properties;
import java.util.Stack;

import uk.co.firstchoice.common.base.logging.LogManager;
import uk.co.firstchoice.common.base.logging.Logger;
import uk.co.firstchoice.common.base.pooling.support.ResourcePoolRegistry;
import uk.co.firstchoice.common.base.util.CommonCharacter;

/**
 * Abstract class providing basic resource pool functionality. Provides a
 * default implementation for all ResourcePool interface methods except for
 * createResource(). The easiest way to create a ResourcePool implementation is
 * to extend this class.
 *
 *
 */
public abstract class AbstractResourcePool extends Thread implements
		ResourcePool {

	/** Default sleep time in milliseconds */
	public static final long DEFAULT_SLEEP_TIME = 5000 * 12 * 3; //3 minutes.

	/** Default number of resources allocated to pool */
	public static final int DEFAULT_POOL_SIZE = 3;

	/** Enforce single instantiation rule */
	private static ResourcePoolRegistry _singleton;

	/** Enable locking of registry */
	private static Object _lockResourcePoolRegistry = new Object();

	/** Enforce proper locking of pool for pool operations */
	private Object _lockResourcePool = new Object();

	/** Stack of Resources. */
	private Stack _pool = new Stack();

	/** Number of Resources Issued: */
	private int _nbrResourcesIssued = 0;

	/** Number of Resources Recycled: */
	private int _nbrResourcesRecycled = 0;

	/** Number of Resources Terminated: */
	private int _nbrResourcesTerminated = 0;

	/** Number of Resources Created: */
	private int _nbrResourcesCreated = 0;

	/** Size of Stack of Resources. */
	private int _poolSize = DEFAULT_POOL_SIZE;

	/** Resource properties */
	private Properties _resourceCreationProperties = new Properties();

	/** Sleep Time in MilliSeconds */
	private long _sleepTimeInMillis = DEFAULT_SLEEP_TIME;

	/** Pool name */
	private String _poolName = null;

	/**
	 * Constructor
	 */
	private AbstractResourcePool() throws ResourceException {
		synchronized (_lockResourcePoolRegistry) {
			if (_singleton == null) {
				_singleton = ResourcePoolRegistry.getResourcePoolRegistry();
			}
			try {
				_singleton.registerPool(this);
			} catch (ResourceException e) {
				_singleton.unRegisterPool(this);
				throw e;
			}
		}
	}

	public AbstractResourcePool(String poolName, Properties creationProperties)
			throws ResourceException {
		this();
		_poolName = poolName;
		this.setCreationProperties(creationProperties);
		this.initPool();
		this.start();
	}

	public AbstractResourcePool(String poolName, Properties creationProperties,
			int poolSize) throws ResourceException {
		this();
		_poolName = poolName;
		this.setCreationProperties(creationProperties);
		this.setPoolSize(poolSize);
		this.initPool();
		this.start();
	}

	public AbstractResourcePool(String poolName, Properties creationProperties,
			int poolSize, long sleepIntervalInMillis) throws ResourceException {
		this();
		_poolName = poolName;
		this.setCreationProperties(creationProperties);
		this.setPoolSize(poolSize);
		this.setSleepTimeInMillis(sleepIntervalInMillis);
		this.initPool();
		this.start();
	}

	/**
	 * Allocates initial resources for pool.
	 *
	 */
	public void initPool() throws ResourceException {
		for (int i = 0; i < this.getPoolSize(); i++) {
			this.push(createResource(_resourceCreationProperties));
			_nbrResourcesCreated++;
		}
	}

	public abstract Resource createResource(Properties props)
			throws ResourceException;

	/**
	 * Provides pool status information.
	 *
	 */
	public void logStatus() {
		this.logStatus(LogManager.getLogger());
	}

	/**
	 * Provides pool status information.
	 *
	 */
	public void logStatus(Logger out) {
		StringBuffer info = new StringBuffer(1024);

		info.append("Status Info for Pool ");
		info.append(this.getPoolName());
		info.append(CommonCharacter.LINE_FEED);

		info.append("Nbr Resources Issued: ");
		info.append(Integer.toString(_nbrResourcesIssued));
		info.append(CommonCharacter.LINE_FEED);

		info.append("Nbr Resources Recycled: ");
		info.append(Integer.toString(_nbrResourcesRecycled));
		info.append(CommonCharacter.LINE_FEED);

		info.append("Nbr Resources Terminated: ");
		info.append(Integer.toString(_nbrResourcesTerminated));
		info.append(CommonCharacter.LINE_FEED);

		info.append("Nbr Resources Created: ");
		info.append(Integer.toString(_nbrResourcesCreated));
		info.append(CommonCharacter.LINE_FEED);

		info.append("------------------------------------------------");
		out.logInfo(info.toString());
	}

	/**
	 * Sets properties used to create resources for the pool.
	 *
	 * @param Properties
	 *            needed by resource at startup.
	 *
	 */
	public void setCreationProperties(Properties props) {
		_resourceCreationProperties = props;
	}

	/**
	 * Obtains resource from the pool. Creates a new one if necessary.
	 *
	 * @return Resource Valid and usable resource.
	 *
	 */
	public Resource getResource() throws ResourceException {
		Resource resource = null;
		_nbrResourcesIssued++;
		if (this.isEmpty()) {
			_nbrResourcesCreated++;
			return createResource(_resourceCreationProperties); //  Should log
			// increase in
			// pool size!
		} else {
			resource = this.pop();
			if (resource != null && resource.isValid())
				return resource;
			else {
				try {
					_nbrResourcesTerminated++;
					resource.terminate();
				} catch (Exception e) {
				}
				_nbrResourcesCreated++;
				return createResource(_resourceCreationProperties);
			}
		}
	}

	/**
	 * Checks if resource pool is empty.
	 *
	 * @return indicator true if empty, false if not.
	 *
	 */
	private boolean isEmpty() {
		boolean answer = true;
		synchronized (_lockResourcePool) {
			answer = _pool.empty();
		}
		return answer;
	}

	/**
	 * Pops a resource off the stack.
	 *
	 * @return resource
	 *
	 */
	private Resource pop() {
		Resource obj = null;
		synchronized (_lockResourcePool) {
			obj = (Resource) _pool.pop();
		}
		return obj;
	}

	/**
	 * Peeks at a specific resource off the stack.
	 *
	 * @return resource
	 *
	 */
	private Resource peek(int index) {
		Resource obj = null;
		synchronized (_lockResourcePool) {
			obj = (Resource) _pool.elementAt(index);
		}
		return obj;
	}

	/**
	 * Pops a specific resource off the stack.
	 *
	 * @return resource
	 *
	 */
	private void removeResourceAt(int index) {
		synchronized (_lockResourcePool) {
			_pool.removeElementAt(index);
		}
	}

	/**
	 * Pushes a resource to the stack.
	 *
	 * @param resource
	 *
	 */
	private void push(Resource r) {
		synchronized (_lockResourcePool) {
			_pool.push(r);
		}
	}

	/**
	 * Thread safe true pool size.
	 *
	 * @param pool
	 *            size
	 *
	 */
	private int getSize() {
		int answer = 0;
		synchronized (_lockResourcePool) {
			answer = _pool.size();
		}
		return answer;
	}

	/**
	 * Sets the sleep time. This thread will validate all resources in the pool
	 * at the interval specified here. If invalid resources are found, they will
	 * be terminated and replaced by valid resources.
	 *
	 * @param Interval
	 *            time in milliseconds.
	 *
	 */
	public void setSleepTimeInMillis(long millis) {
		_sleepTimeInMillis = millis;
	}

	/**
	 * Gets the sleep interval time in milliseconds.
	 *
	 * @return sleep interval in milliseconds.
	 *
	 */
	public long getSleepTimeInMillis() {
		return _sleepTimeInMillis;
	}

	/**
	 * Gets the number of resources allocated to pool.
	 *
	 * @return number of resources allocated to pool.
	 *
	 */
	public int getPoolSize() {
		return _poolSize;
	}

	/**
	 * Sets the number of resources that should be allocated to the pool.
	 *
	 * @param sleep
	 *            Number of resources allocated to pool.
	 *
	 */
	public void setPoolSize(int size) {
		_poolSize = size;
	}

	/**
	 * Gets the used to start resources in the pool.
	 *
	 * @return Properties used to start resources.
	 */
	public Properties getCreationProperties() {
		return _resourceCreationProperties;
	}

	/**
	 * Gets the singleton instance of the resource pool.
	 *
	 * @return resource pool.
	 *
	 */
	public static ResourcePool getPool(String poolName)
			throws ResourceException {
		ResourcePool pool = null;
		if (_singleton != null)
			_singleton.getResourcePool(poolName);
		return pool;
	}

	/**
	 * Closes all resources in the pool. Note that if you have specific
	 * resources to close in addition to the pool, you should override this
	 * method.
	 *
	 */
	public void closeAll() {
		Resource resource = null;

		synchronized (_lockResourcePool) {
			while (_pool.size() > 0) {

				try {
					resource = (Resource) _pool.pop();
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					_nbrResourcesTerminated++;
					resource.terminate();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			try {
				_singleton.unRegisterPool(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Validates and puts a resource back in the resource pool.
	 *
	 * @param resource.
	 *
	 */
	public void recycleResource(Resource resource) throws ResourceException {
		synchronized (_lockResourcePool) {
			if (resource != null && resource.isValid()) {
				_nbrResourcesRecycled++;
				_pool.push(resource);
			}
		}
	}

	/**
	 * Validates idle resources in the pool, replaces them if necessary.
	 *
	 */
	public void run() {
		Resource resource = null;
		try {
			while (!this.isInterrupted()) {
				for (int i = 0; i < this.getSize(); i++) {
					resource = this.peek(i);
					if (!(resource != null && resource.isValid())) {
						this.removeResourceAt(i);
						_nbrResourcesTerminated++;
						resource.terminate();
						try {
							_nbrResourcesCreated++;
							this
									.push(createResource(_resourceCreationProperties));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				try {
					Thread.sleep(this.getSleepTimeInMillis());
				} catch (InterruptedException e) {
					this.closeAll();
					return;
				}
			}
			if (this.isInterrupted())
				this.closeAll();
		} catch (Throwable e) {
			LogManager.getLogger().logError(
					"Pool Error name=" + this.getPoolName(), e);
			this.closeAll();
		}
	}

	/**
	 * Provides name of the resoruce pool.
	 *
	 */
	public String getPoolName() throws ResourceException {
		return _poolName;
	}
}

