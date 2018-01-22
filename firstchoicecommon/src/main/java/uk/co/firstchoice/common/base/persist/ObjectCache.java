/**
 * 
 * Provides a cache of objects that will expire its contents as those contents
 * fail to be used. This cache uses <span
 * class="code">java.lang.ref.SoftReference </span> to guarantee that cached
 * items will be removed when they have not been referenced for a long time.
 * 
 * NOTE: This class is not synchronized and therefore should be synchronized by
 * the application for multi-threaded use.
 *  
 */
package uk.co.firstchoice.common.base.persist;

//Java imports
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class ObjectCache implements Collection, Serializable {
	/**
	 * A hash map indexing references by unique keys.
	 */
	private HashMap cache = new HashMap();

	/*
	 * Constructs a new empty cache.
	 */
	public ObjectCache() {
		super();
	}

	/**
	 * Unsupported.
	 * 
	 * @param ob
	 *            ignored
	 * @return never returns
	 * @throws java.lang.UnsupportedOperationException
	 *             always
	 */
	public boolean add(Object ob) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Unsupported.
	 * 
	 * @param coll
	 *            ignored
	 * @return never returns
	 * @throws java.lang.UnsupportedOperationException
	 *             always
	 */
	public boolean addAll(final Collection coll) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Caches the specified object identified by the specified key.
	 * 
	 * @param key
	 *            a unique key for this object
	 * @param val
	 *            the object to be cached
	 */
	public void cache(final Object key, final Object val) {
		cache.put(key, new SoftReference(val));
	}

	/**
	 * Clears the entire cache.
	 */
	public void clear() {
		cache.clear();
	}

	/**
	 * Checks the specified object against the cache and verifies that it is in
	 * the cache. This method will return <span class="keyword">false </span> if
	 * the object was once in the cache but has expired due to inactivity.
	 * 
	 * @param ob
	 *            the object to check for in the cache
	 * @return true if the object is in the cache
	 */
	public boolean contains(final Object ob) {
		Iterator it = cache.values().iterator();

		while (it.hasNext()) {
			SoftReference ref = (SoftReference) it.next();
			Object item = ref.get();

			if (item != null && ob.equals(item)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks the passed in collection and determines if all elements of that
	 * collection are contained within this cache. Care should be taken in
	 * reading too much into a failure. If one of the elements was once in this
	 * cache but has expired due to inactivity, this method will return false.
	 * 
	 * @param coll
	 *            the collection to test
	 * @return true if all elements of the tested collection are in the cache
	 */
	public boolean containsAll(final Collection coll) {
		Iterator it = coll.iterator();

		while (it.hasNext()) {
			if (!contains(it.next())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if an object with the specified key is in the cache.
	 * 
	 * @param key
	 *            the object's identifier
	 * @return true if the object is in the cache
	 */
	public boolean containsKey(final Object key) {
		if (!cache.containsKey(key)) {
			return false;
		}

		SoftReference ref = (SoftReference) cache.get(key);

		if (ref.get() == null) {
			release(key);
			return false;
		}
		return true;

	}

	/**
	 * Provides the cached object identified by the specified key. This method
	 * will return <span class="code">null </span> if the specified object is
	 * not in the cache.
	 * 
	 * @param key
	 *            the unique identifier of the desired object
	 * @return the cached object or null
	 */
	public Object get(final Object key) {
		SoftReference ref = (SoftReference) cache.get(key);
		Object ob;

		if (ref == null) {
			return null;
		}
		ob = ref.get();
		if (ob == null) {
			release(key);
		}
		return ob;
	}

	/**
	 * @return true if the cache is empty
	 */
	public boolean isEmpty() {
		return cache.isEmpty();
	}

	/**
	 * Provides all of the valid objects in the cache. This method will not be
	 * the snappiest method in the world.
	 * 
	 * @return all valid objects in the cache
	 */
	public Iterator iterator() {
		return toList().iterator();
	}

	/**
	 * Releases the specified object from the cache.
	 * 
	 * @param key
	 *            the unique identified for the item to release
	 */
	public void release(final Object key) {
		cache.remove(key);
	}

	/**
	 * Unsupported.
	 * 
	 * @param ob
	 *            ignored
	 * @return never returns
	 * @throws java.lang.UnsupportedOperationException
	 *             always
	 */
	public boolean remove(final Object ob) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Unsupported.
	 * 
	 * @param coll
	 *            ignored
	 * @return never returns
	 * @throws java.lang.UnsupportedOperationException
	 *             always
	 */
	public boolean removeAll(final Collection coll) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Unsupported.
	 * 
	 * @param coll
	 *            ignored
	 * @return never returns
	 * @throws java.lang.UnsupportedOperationException
	 *             always
	 */
	public boolean retainAll(final Collection coll) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return the number of elements in the cache
	 */
	public int size() {
		return toList().size();
	}

	/**
	 * @return the cache as an array
	 */
	public Object[] toArray() {
		return toList().toArray();
	}

	/**
	 * @return the cache as an array
	 */
	public Object[] toArray(final Object[] arr) {
		return toList().toArray(arr);
	}

	/**
	 * @return the cache as an array
	 */
	private ArrayList toList() {
		Iterator it = cache.values().iterator();
		ArrayList tmp = new ArrayList();

		while (it.hasNext()) {
			SoftReference ref = (SoftReference) it.next();
			Object ob = ref.get();

			if (ob != null) {
				tmp.add(ob);
			}
		}
		return tmp;
	}
}