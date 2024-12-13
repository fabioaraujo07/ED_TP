package Collections.Linked;


import Collections.Interfaces.ListADT;
import Exceptions.EmptyCollectionException;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class LinkedList<T> implements ListADT<T> {

    protected int modCount;
    protected int count;
    protected LinearNode<T> front, rear;

    /**
     * Constructs an empty linked list.
     */
    public LinkedList() {
        this.modCount = 0;
        this.count = 0;
        this.front = null;
        this.rear = null;
    }

    /**
     * Removes and returns the first element from the list.
     *
     * @return the first element from the list
     * @throws EmptyCollectionException if the list is empty
     */
    @Override
    public T removeFirst() throws EmptyCollectionException {

        if (isEmpty()) {
            throw new EmptyCollectionException("Empty list");
        }

        LinearNode<T> removedNode = this.front;
        this.front = front.getNext();
        if (this.front == null) {
            this.rear = null;
        }
        this.count--;
        this.modCount++;

        return removedNode.getElement();
    }

    /**
     * Removes and returns the last element from the list.
     *
     * @return the last element from the list
     * @throws EmptyCollectionException if the list is empty
     */
    @Override
    public T removeLast() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Empty list");
        }

        LinearNode<T> removedNode = rear;

        if (front == rear) {
            front = rear = null;
        } else {
            LinearNode<T> current = front;
            while (current.getNext() != rear) {
                current = current.getNext();
            }
            rear = current;
            rear.setNext(null);
        }

        this.count--;
        this.modCount++;
        return removedNode.getElement();
    }

    /**
     * Removes and returns the specified element from the list.
     *
     * @param element the element to be removed
     * @return the removed element
     * @throws EmptyCollectionException if the list is empty
     * @throws NoSuchElementException if the element is not found in the list
     */
    @Override
    public T remove(T element) throws EmptyCollectionException, NoSuchElementException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Empty list");
        }

        LinearNode<T> previous = null;
        LinearNode<T> current = front;

        while (current != null && !current.getElement().equals(element)) {
            previous = current;
            current = current.getNext();
        }

        if (current == null) {
            throw new NoSuchElementException("Element not found in the list");
        }
        if (current == front) {
            front = front.getNext();
        } else {
            previous.setNext(current.getNext());
        }
        if (current == rear) {
            rear = previous;
        }

        count--;
        modCount++;
        return current.getElement();
    }

    /**
     * Returns the first element in the list.
     *
     * @return the first element in the list
     * @throws EmptyCollectionException if the list is empty
     */
    @Override
    public T first() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Empty list");
        }
        return front.getElement();
    }

    /**
     * Returns the last element in the list.
     *
     * @return the last element in the list
     * @throws EmptyCollectionException if the list is empty
     */
    @Override
    public T last() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Empty list");
        }
        return rear.getElement();
    }

    /**
     * Finds and returns the node containing the specified target element.
     *
     * @param target the element to be found
     * @return the node containing the target element
     * @throws NoSuchElementException if the element is not found
     */
    protected LinearNode<T> find(T target) throws NoSuchElementException {

        LinearNode<T> current = front;
        while (current != null) {
            if (target.equals(current.getElement())) {
                return current;
            }
            current = current.getNext();
        }
        throw new NoSuchElementException("Element not found");
    }

    /**
     * Returns true if the list contains the specified target element.
     *
     * @param target the element to be checked
     * @return true if the list contains the target element, false otherwise
     */
    @Override
    public boolean contains(T target) {
        try {
            find(target);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Returns true if the list is empty, false otherwise.
     *
     * @return true if the list is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return (count == 0);
    }

    /**
     * Returns the number of elements in the list.
     *
     * @return the number of elements in the list
     */
    @Override
    public int size() {
        return count;
    }

    /**
     * Returns an iterator over the elements in the list.
     *
     * @return an iterator over the elements in the list
     */
    @Override
    public Iterator<T> iterator() {
        return new MyIterator();
    }

    /**
     * Returns a string representation of the list.
     *
     * @return a string representation of the list
     */
    @Override
    public String toString() {

        String string = "List: \n";

        LinearNode<T> current = front;
        while (current != null) {
            string += current.getElement() + "\n";
            current = current.getNext();
        }

        return string;
    }

    /**
     * Iterator implementation for the linked list.
     */
    private class MyIterator implements Iterator<T> {

        private boolean okToRemove;
        private int expectedModCount;
        private LinearNode<T> current;
        private LinearNode<T> lastReturned;

        /**
         * Constructs an iterator for the linked list.
         */
        public MyIterator() {
            expectedModCount = modCount;
            current = front;
            okToRemove = false;
        }

        /**
         * Returns true if the iteration has more elements.
         *
         * @return true if the iteration has more elements, false otherwise
         */
        @Override
        public boolean hasNext() {
            return (current != null);
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public T next() {
            if (expectedModCount != modCount) {
                throw new java.util.ConcurrentModificationException();
            }

            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            this.okToRemove = true;

            T toReturn = current.getElement();
            current = current.getNext();
            return toReturn;
        }

        /**
         * Removes the last element returned by this iterator.
         *
         * @throws IllegalStateException if the next method has not been called, or remove has already been called after the last call to next
         * @throws java.util.ConcurrentModificationException if the list has been modified after the iterator was created
         */
        @Override
        public void remove() {
            if (expectedModCount != modCount) {
                throw new java.util.ConcurrentModificationException();
            }

            if (!okToRemove) {
                throw new IllegalStateException("Next() not called or element already removed.");
            }

            okToRemove = false;

            try {
                LinearNode<T> previous = null;
                LinearNode<T> current = front;

                while (current != null && current != lastReturned) {
                    previous = current;
                    current = current.getNext();
                }

                if (current == null) {
                    throw new IllegalStateException("Iterator corrupted.");
                }

                if (previous == null) {
                    front = front.getNext();
                } else {
                    previous.setNext(current.getNext());
                }

                if (current == rear) {
                    rear = previous;
                }

                count--;
                modCount++;
                expectedModCount = modCount;

            } catch (EmptyCollectionException e) {
                // Tratar exceção caso necessário (neste caso, geralmente é ignorado)
            }

            T element = next();
            okToRemove = false;
            expectedModCount++;
        }

    }
}
