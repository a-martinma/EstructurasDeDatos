
public interface FloatList {

    /**
     * Returns the number of elements in this list
     *
     * @return
     */
    public int size();

    /**
     * Returns true if this list contains no elements
     *
     * @return
     */
    public boolean isempty();

    /**
     * Appends the specified element to the begining of this list
     *
     * @param value
     */
    public void add(Float value);

    /**
     * Inserts the specified element at the specified position in this list
     * (optional operation)
     *
     * @param index
     * @param value
     */
    public void add(int index, Float value);

    /**
     * Returns and returns the element at the beginning of this list
     *
     * @return
     */
    public Float remove();

    /**
     * Removes and returns the element at the specified position in this list
     *
     * @param index
     * @return
     */
    public Float remove(int index);

    /**
     * Returns the element at the beginning of this list
     *
     * @return
     */
    public Float get();

    /**
     * Returns the element at the specified position in this list
     *
     * @param index
     * @return
     */
    public Float get(int index);

    /**
     * Returns the element's position if this list contains the specified
     * element otherwise returns 0
     *
     * @param value
     * @return
     */
    public int search(Float value);

    /**
     * Returns true if this list contains the specified element.
     *
     * @param value
     * @return
     */
    public boolean contains(Float value);
}
