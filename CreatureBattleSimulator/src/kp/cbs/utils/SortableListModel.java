/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractListModel;

/**
 *
 * @author Asus
 * @param <E>
 */
@SuppressWarnings("serial") // Same-version serialization only
public class SortableListModel<E extends Comparable<E>> extends AbstractListModel<E>
{
    private final LinkedList<E> delegate = new LinkedList<>();

    /**
     * Returns the number of components in this list.
     * <p>
     * This method is identical to {@code size}, which implements the
     * {@code List} interface defined in the 1.2 Collections framework.
     * This method exists in conjunction with {@code setSize} so that
     * {@code size} is identifiable as a JavaBean property.
     *
     * @return  the number of components in this list
     * @see #size()
     */
    @Override
    public int getSize() {
        return delegate.size();
    }

    /**
     * Returns the component at the specified index.
     * <blockquote>
     * <b>Note:</b> Although this method is not deprecated, the preferred
     *    method to use is {@code get(int)}, which implements the {@code List}
     *    interface defined in the 1.2 Collections framework.
     * </blockquote>
     * @param      index   an index into this list
     * @return     the component at the specified index
     * @throws     ArrayIndexOutOfBoundsException  if the {@code index}
     *             is negative or greater than the current size of this
     *             list
     * @see #get(int)
     */
    @Override
    public E getElementAt(int index) {
        return delegate.get(index);
    }

    /**
     * Returns the number of components in this list.
     *
     * @return  the number of components in this list
     * @see Vector#size()
     */
    public int size() {
        return delegate.size();
    }

    /**
     * Tests whether this list has any components.
     *
     * @return  {@code true} if and only if this list has
     *          no components, that is, its size is zero;
     *          {@code false} otherwise
     * @see Vector#isEmpty()
     */
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    /**
     * Returns an enumeration of the components of this list.
     *
     * @return  an enumeration of the components of this list
     * @see Vector#elements()
     */
    public List<E> elements() {
        return Collections.unmodifiableList(delegate);
    }

    /**
     * Tests whether the specified object is a component in this list.
     *
     * @param   elem   an object
     * @return  {@code true} if the specified object
     *          is the same as a component in this list
     * @see Vector#contains(Object)
     */
    public boolean contains(Object elem) {
        return delegate.contains(elem);
    }

    /**
     * Searches for the first occurrence of {@code elem}.
     *
     * @param   elem   an object
     * @return  the index of the first occurrence of the argument in this
     *          list; returns {@code -1} if the object is not found
     * @see Vector#indexOf(Object)
     */
    public int indexOf(Object elem) {
        return delegate.indexOf(elem);
    }

    /**
     * Returns the index of the last occurrence of {@code elem}.
     *
     * @param   elem   the desired component
     * @return  the index of the last occurrence of {@code elem}
     *          in the list; returns {@code elem} if the object is not found
     * @see Vector#lastIndexOf(Object)
     */
    public int lastIndexOf(Object elem) {
        return delegate.lastIndexOf(elem);
    }

    /**
     * Returns the component at the specified index.
     * <blockquote>
     * <b>Note:</b> Although this method is not deprecated, the preferred
     *    method to use is {@code get(int)}, which implements the
     *    {@code List} interface defined in the 1.2 Collections framework.
     * </blockquote>
     *
     * @param      index   an index into this list
     * @return     the component at the specified index
     * @throws     ArrayIndexOutOfBoundsException if the index
     * is negative or not less than the size of the list
     * @see #get(int)
     * @see Vector#elementAt(int)
     */
    public E elementAt(int index) {
        return delegate.get(index);
    }

    /**
     * Returns the first component of this list.
     * @return     the first component of this list
     * @see Vector#firstElement()
     * @throws NoSuchElementException if this
     * vector has no components
     */
    public E firstElement() {
        return delegate.getFirst();
    }

    /**
     * Returns the last component of the list.
     *
     * @return  the last component of the list
     * @see Vector#lastElement()
     * @throws NoSuchElementException if this vector
     * has no components
     */
    public E lastElement() {
        return delegate.getLast();
    }

    /**
     * Sets the component at the specified {@code index} of this
     * list to be the specified element. The previous component at that
     * position is discarded.
     * <blockquote>
     * <b>Note:</b> Although this method is not deprecated, the preferred
     *    method to use is {@code set(int,Object)}, which implements the
     *    {@code List} interface defined in the 1.2 Collections framework.
     * </blockquote>
     *
     * @param      element what the component is to be set to
     * @param      index   the specified index
     * @throws     ArrayIndexOutOfBoundsException if the index is invalid
     * @see #set(int,Object)
     * @see Vector#setElementAt(Object,int)
     */
    public void setElementAt(E element, int index) {
        delegate.set(index, element);
        fireContentsChanged(this, index, index);
    }

    /**
     * Deletes the component at the specified index.
     * <blockquote>
     * <b>Note:</b> Although this method is not deprecated, the preferred
     *    method to use is {@code remove(int)}, which implements the
     *    {@code List} interface defined in the 1.2 Collections framework.
     * </blockquote>
     *
     * @param      index   the index of the object to remove
     * @see #remove(int)
     * @see Vector#removeElementAt(int)
     * @throws ArrayIndexOutOfBoundsException if the index is invalid
     */
    public void removeElementAt(int index) {
        delegate.remove(index);
        fireIntervalRemoved(this, index, index);
    }

    /**
     * Inserts the specified element as a component in this list at the
     * specified <code>index</code>.
     * <blockquote>
     * <b>Note:</b> Although this method is not deprecated, the preferred
     *    method to use is {@code add(int,Object)}, which implements the
     *    {@code List} interface defined in the 1.2 Collections framework.
     * </blockquote>
     *
     * @param      element the component to insert
     * @param      index   where to insert the new component
     * @exception  ArrayIndexOutOfBoundsException if the index was invalid
     * @see #add(int,Object)
     * @see Vector#insertElementAt(Object,int)
     */
    public void insertElementAt(E element, int index) {
        delegate.add(index, element);
        fireIntervalAdded(this, index, index);
    }

    /**
     * Adds the specified component to the end of this list.
     *
     * @param   element   the component to be added
     * @see Vector#addElement(Object)
     */
    public void addElement(E element) {
        int index = delegate.size();
        delegate.add(element);
        fireIntervalAdded(this, index, index);
    }

    /**
     * Removes the first (lowest-indexed) occurrence of the argument
     * from this list.
     *
     * @param   obj   the component to be removed
     * @return  {@code true} if the argument was a component of this
     *          list; {@code false} otherwise
     * @see Vector#removeElement(Object)
     */
    public boolean removeElement(Object obj) {
        int index = indexOf(obj);
        boolean rv = delegate.remove(obj);
        if (index >= 0) {
            fireIntervalRemoved(this, index, index);
        }
        return rv;
    }


    /**
     * Removes all components from this list and sets its size to zero.
     * <blockquote>
     * <b>Note:</b> Although this method is not deprecated, the preferred
     *    method to use is {@code clear}, which implements the
     *    {@code List} interface defined in the 1.2 Collections framework.
     * </blockquote>
     *
     * @see #clear()
     * @see Vector#removeAllElements()
     */
    public void removeAllElements() {
        int index1 = delegate.size()-1;
        delegate.clear();
        if (index1 >= 0) {
            fireIntervalRemoved(this, 0, index1);
        }
    }


    /**
     * Returns a string that displays and identifies this
     * object's properties.
     *
     * @return a String representation of this object
     */
    @Override
   public String toString() {
        return delegate.toString();
    }


    /* The remaining methods are included for compatibility with the
     * Java 2 platform Vector class.
     */

    /**
     * Returns an array containing all of the elements in this list in the
     * correct order.
     *
     * @return an array containing the elements of the list
     * @see Vector#toArray()
     */
    public Object[] toArray() {
        return delegate.toArray();
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of element to return
     * @return the element at the specified position in this list
     * @throws ArrayIndexOutOfBoundsException if the index is out of range
     * ({@code index &lt; 0 || index &gt;= size()})
     */
    public E get(int index) {
        return delegate.get(index);
    }

    /**
     * Replaces the element at the specified position in this list with the
     * specified element.
     *
     * @param index index of element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws ArrayIndexOutOfBoundsException if the index is out of range
     * ({@code index &lt; 0 || index &gt;= size()})
     */
    public E set(int index, E element) {
        delegate.set(index, element);
        fireContentsChanged(this, index, index);
        return element;
    }

    /**
     * Inserts the specified element at the specified position in this list.
     *
     * @param index index at which the specified element is to be inserted
     * @param element element to be inserted
     * @throws ArrayIndexOutOfBoundsException if the index is out of range
     * ({@code index &lt; 0 || index &gt; size()})
     */
    public void add(int index, E element) {
        delegate.add(index, element);
        fireIntervalAdded(this, index, index);
    }

    /**
     * Removes the element at the specified position in this list.
     * Returns the element that was removed from the list
     *
     * @param index the index of the element to removed
     * @return the element previously at the specified position
     * @throws ArrayIndexOutOfBoundsException if the index is out of range
     * ({@code index &lt; 0 || index &gt;= size()})
     */
    public E remove(int index) {
        E rv = delegate.remove(index);
        fireIntervalRemoved(this, index, index);
        return rv;
    }

    /**
     * Removes all of the elements from this list.  The list will
     * be empty after this call returns (unless it throws an exception).
     */
    public void clear() {
        int index1 = delegate.size()-1;
        delegate.clear();
        if (index1 >= 0) {
            fireIntervalRemoved(this, 0, index1);
        }
    }

    /**
     * Deletes the components at the specified range of indexes.
     * The removal is inclusive, so specifying a range of (1,5)
     * removes the component at index 1 and the component at index 5,
     * as well as all components in between.
     *
     * @param      fromIndex the index of the lower end of the range
     * @param      toIndex   the index of the upper end of the range
     * @throws ArrayIndexOutOfBoundsException if the index was invalid
     * @throws IllegalArgumentException if {@code fromIndex &gt; toIndex}
     * @see        #remove(int)
     */
    public void removeRange(int fromIndex, int toIndex) {
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException("fromIndex must be <= toIndex");
        }
        for(int i = toIndex; i >= fromIndex; i--) {
            delegate.remove(i);
        }
        fireIntervalRemoved(this, fromIndex, toIndex);
    }

    /**
     * Adds all of the elements present in the collection to the list.
     *
     * @param c the collection which contains the elements to add
     * @throws NullPointerException if {@code c} is null
     */
    public void addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return;
        }

        int startIndex = getSize();

        delegate.addAll(c);
        fireIntervalAdded(this, startIndex, getSize() - 1);
    }

    /**
     * Adds all of the elements present in the collection, starting
     * from the specified index.
     *
     * @param index index at which to insert the first element from the
     * specified collection
     * @param c the collection which contains the elements to add
     * @throws ArrayIndexOutOfBoundsException if {@code index} does not
     * fall within the range of number of elements currently held
     * @throws NullPointerException if {@code c} is null
     */
    public void addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > getSize()) {
            throw new ArrayIndexOutOfBoundsException("index out of range: " +
                                                                       index);
        }

        if (c.isEmpty()) {
            return;
        }

        delegate.addAll(index, c);
        fireIntervalAdded(this, index, index + c.size() - 1);
    }
    
    public final void sort()
    {
        Collections.sort(delegate);
    }
    
    public final void sort(Comparator<E> cmp)
    {
        Collections.sort(delegate, cmp);
    }
}
