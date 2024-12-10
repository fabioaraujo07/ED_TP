package Collections.Interfaces;

/**
 * Defines the interface to an ordered list collection. Only
 * Comparable elements may be added to this list.
 *
 * @param <T> the class of the objects in the list
 */

public interface OrderedListADT<T> extends ListADT<T> {
    /**
     * Adds the specified element to this list at
     * the proper location
     *
     * @param element the element to be added to this list
     */
    public void add (T element);
}
