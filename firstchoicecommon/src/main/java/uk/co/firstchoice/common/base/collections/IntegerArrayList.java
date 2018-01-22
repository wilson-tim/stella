package uk.co.firstchoice.common.base.collections;

import java.io.Serializable;

/**
 * An arraylist for primitive "int" data types. This class does NOT implement
 * java.util.List.
 * 
 * <p>.
 */
public class IntegerArrayList implements Serializable {

    public IntegerArrayList() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_CAPACITY_INCREMENT);
    }

    public IntegerArrayList(int initialCapacity) {
        this(initialCapacity, DEFAULT_CAPACITY_INCREMENT);
    }

    public IntegerArrayList(int initialCapacity, int capacityIncrement) {
        _capacityIncrement = capacityIncrement;
        _data = new int[initialCapacity];
    }

    /**
     * Adds a value to the end of the list.
     * 
     * @param value
     */
    public void add(int value) {
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
    public int get(int index) {
        return _data[index];
    }

    /**
     * Converts list to an array.
     * 
     * @return listAsArray
     */
    public int[] toArray() {
        int[] newArray = new int[_currentCount];
        System.arraycopy(_data, 0, newArray, 0, _currentCount);
        return newArray;
    }

    /**
     * Returns list as byte array.
     * 
     * @return byteArray
     */
    public byte[] toByteArray() {
        byte[] byteArray = null;
        int[] intArray = this.toArray();

        if (intArray != null) {
            byteArray = new byte[intArray.length];
            for (int i = 0; i < _currentCount; i++) {
                byteArray[i] = (byte) _data[i];
            }
        }

        return byteArray;
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
        int[] newArray = new int[_data.length + _capacityIncrement];
        System.arraycopy(_data, 0, newArray, 0, _data.length);
        _data = newArray;
    }

    private static final int DEFAULT_INITIAL_CAPACITY = 100;

    private static final int DEFAULT_CAPACITY_INCREMENT = 100;

    private int _capacityIncrement = 0;

    private int _currentCount = 0;

    private int[] _data = null;

}