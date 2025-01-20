package list;

import java.util.Iterator;
import material.Position;

public interface MyListBetter<E> extends Iterable<Position<E>>{
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
     * @return  
     */
    public Position<E> add(E value);
    
    /**
     * Inserts the specified element after the specified position in this list (optional operation)
     * 
     * @param pos
     * @param value 
     * @return  
     */
    public Position<E> addAfter(Position<E> pos, E value);
    
    /**
     * Inserts the specified element before the specified position in this list (optional operation)
     * 
     * @param pos
     * @param value 
     * @return  
     */
    public Position<E> addBefore(Position<E> pos, E value);
    
    /**
     * Remove and returns the element at the beginning of this list
     * 
     * @param pos
     * @return 
     */
    public E remove(Position<E> pos);
    
        
    /**
     * Returns the element at the beginning of this list
     * 
     * @return 
     */
    public Position<E> get();
    
    /**
     * Modifies the element at the specified position in this list
     * 
     * @param pos
     * @param value
     * @return 
     */
    public Position<E> set(Position<E> pos, E value);
    
    /**
     * Returns the element's Position if this list contains the specified element
     * otherwise returns null
     * 
     * @param value
     * @return 
     */
    public Position<E> search(E value);
    
    /**
     * Returns true if this list contains the specified element.
     * 
     * @param value
     * @return 
     */
    public boolean contains (E value);
    
    /**
     * Returns an iterator of the elements
     * 
     * @return 
     */
    @Override
    public Iterator<Position<E>> iterator();
}
