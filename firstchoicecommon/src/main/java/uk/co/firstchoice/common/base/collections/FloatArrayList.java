package uk.co.firstchoice.common.base.collections;

import java.io.Serializable;

/**
 * Dynamic list for float primitives. This class does NOT implement
 * java.util.List.
 * 
 * <BR>
 * <BR>.
 */
public class FloatArrayList implements Serializable {

    public FloatArrayList() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_CAPACITY_INCREMENT);
    }

    public FloatArrayList(int initialCapacity) {
        this(initialCapacity, DEFAULT_CAPACITY_INCREMENT);
    }

    public FloatArrayList(int initialCapacity, int capacityIncrement) {
        _capacityIncrement = capacityIncrement;
        _data = new float[initialCapacity];
    }

    /**
     * Adds a value to the end of the list.
     * 
     * @param value
     */
    public void add(float value) {
        if (_currentCount == _data.length)
            this.bumpCapacity();
        _data[_currentCount] = value;
        _currentCount++;
    }

    /**
     * Returns value at specified index from the list.
     * 
     * @param index
     * @return value
     */
    public float get(int index) {
        return _data[index];
    }

    /**
     * Converts list to an array.
     * 
     * @return listAsArray
     */
    public float[] toArray() {
        float[] newArray = new float[_currentCount];
        System.arraycopy(_data, 0, newArray, 0, _data.length);
        return newArray;
    }

    /**
     * Returns length of list.
     * 
     * @return listLength
     */
    public int length() {
        return _currentCount;
    }

    private void bumpCapacity() {
        float[] newArray = new float[_data.length + _capacityIncrement];
        System.arraycopy(_data, 0, newArray, 0, _data.length);
        _data = newArray;
    }

    private static final int DEFAULT_INITIAL_CAPACITY = 100;

    private static final int DEFAULT_CAPACITY_INCREMENT = 100;

    private int _capacityIncrement = 0;

    private int _currentCount = 0;

    private float[] _data = null;
}