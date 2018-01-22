package uk.co.firstchoice.common.base.collections;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A classic implementation of a queue. The JDK provides an implementation of a
 * Stack, but not a queue. This queue also allows the prioritization of queue
 * contents.
 * 
 * <p>.
 */

public class Queue {

    //  Storage mechanism for Queue.
    private ArrayList _list = null;

    public Queue() {
        _list = new ArrayList();
    }

    public Queue(Collection c) {
        _list = new ArrayList(c);
    }

    public Queue(int initialCapacity) {
        _list = new ArrayList(initialCapacity);
    }

    /**
     * Tests if Queue is empty.
     */
    public synchronized boolean empty() {
        boolean answer = false;

        if (_list.size() > 0)
            answer = false;
        return answer;
    }

    /**
     * Provides size of Queue.
     */
    public synchronized int size() {
        return _list.size();
    }

    /**
     * Provides the next object on the Queue without removing it.
     */
    public synchronized Object peek() {
        PrioritizedObject obj = null;

        if (_list.size() > 0)
            obj = (PrioritizedObject) _list.get(_list.size() - 1);
        return obj._obj;
    }

    /**
     * Adds an object to the Queue
     */
    public synchronized Object push(Object obj) {
        this.push(obj, Queue.NORM_PRIORITY);

        return this;
    }

    /**
     * Adds an object to the Queue
     */
    public synchronized Object push(Object obj, int priority) {
        PrioritizedObject foo = null;
        boolean objectAdded = false;

        if (priority < Queue.MIN_PRIORITY || priority > Queue.MAX_PRIORITY) {
            throw new java.lang.IllegalArgumentException("Priority " + priority
                    + "is not allowed.");
        }

        if (_list.size() == 0)
            _list.add(new PrioritizedObject(obj, priority));
        else {
            for (int i = 0; i < _list.size(); i++) {
                foo = (PrioritizedObject) _list.get(i);
                if (foo._priority <= priority) {
                    _list.add(i, new PrioritizedObject(obj, priority));
                    objectAdded = true;
                    i = _list.size() + 2;
                }
            }

            if (!objectAdded) {
                _list.add(new PrioritizedObject(obj, priority));
            }
        }

        return this;
    }

    /**
     * Removes the next object on the Queue and provides it to the caller.
     */
    public synchronized Object pop() {
        PrioritizedObject obj = null;
        int size = _list.size();

        if (size > 0) {
            obj = (PrioritizedObject) _list.get(size - 1);
            _list.remove(size - 1);
        }

        if (obj != null)
            return obj._obj;

        return null;
    }

    private class PrioritizedObject {
        PrioritizedObject(Object obj, int priority) {
            _obj = obj;
            _priority = priority;
        }

        Object _obj = null;

        int _priority = Queue.NORM_PRIORITY;
    }

    public static final int NORM_PRIORITY = Thread.NORM_PRIORITY;

    public static final int MIN_PRIORITY = Thread.MIN_PRIORITY;

    public static final int MAX_PRIORITY = Thread.MAX_PRIORITY;
}