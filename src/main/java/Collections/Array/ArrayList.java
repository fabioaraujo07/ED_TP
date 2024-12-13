package Collections.Array;



import Collections.Interfaces.ListADT;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class ArrayList<T> implements ListADT<T> {

    protected T[] lista;
    protected int rear;
    protected int modCount;

    /**
     * Constructs an ArrayList with the default initial capacity of 20.
     */
    public ArrayList() {
        this.lista = (T[]) new Object[20];
        this.rear = 0;
    }

    /**
     * Constructs an ArrayList with the specified initial capacity.
     *
     * @param size the initial capacity of the list
     */
    public ArrayList(int size) {
        this.lista = (T[]) new Object[size];
        this.rear = 0;
    }

    /**
     * Removes and returns the first element in the list.
     * Shifts all subsequent elements one position to the left.
     *
     * @return the removed element, or {@code null} if the list is empty
     */
    @Override
    public T removeFirst() {
        T result = this.lista[0];
        for (int i = 0; i < rear; i++) {
            lista[i] = lista[i + 1];
        }
        lista[rear] = null;
        rear--;
        modCount++;
        return result;
    }

    /**
     * Removes and returns the last element in the list.
     *
     * @return the removed element, or {@code null} if the list is empty
     */
    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        --rear;
        T result = lista[rear];
        lista[rear] = null;
        modCount++;
        return result;
    }

    /**
     * Removes and returns the specified element from the list.
     * Shifts subsequent elements to fill the gap.
     *
     * @param element the element to be removed
     * @return the removed element, or {@code null} if the element is not found
     */
    @Override
    public T remove(T element) {
        int index = findIndex(element);
        if (index == -1) {
            return null;
        }
        T result = lista[index];
        for (int i = index; i < rear; i++) {
            lista[i] = lista[i + 1];
        }
        rear--;
        modCount++;
        return result;
    }

    /**
     * Returns the first element in the list without removing it.
     *
     * @return the first element, or {@code null} if the list is empty
     */
    @Override
    public T first() {
        return this.lista[0];
    }

    /**
     * Returns the last element in the list without removing it.
     *
     * @return the last element, or {@code null} if the list is empty
     */
    @Override
    public T last() {
        return this.lista[this.lista.length - 1];
    }

    /**
     * Checks if the list contains the specified element.
     *
     * @param target the element to search for
     * @return {@code true} if the element is found, {@code false} otherwise
     */
    @Override
    public boolean contains(T target) {
        return findIndex(target) != -1;
    }

    /**
     * Checks if the list is empty.
     *
     * @return {@code true} if the list is empty, {@code false} otherwise
     */
    @Override
    public boolean isEmpty() {
        return rear == 0;
    }

    /**
     * Returns the number of elements in the list.
     *
     * @return the size of the list
     */
    @Override
    public int size() {
        return rear;
    }

    /**
     * Returns an iterator for traversing the elements of the list.
     *
     * @return an iterator over the elements in the list
     */
    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator(lista, rear);
    }

    /**
     * Dynamically doubles the capacity of the underlying array when it is full.
     */
    protected void expandCapacity() {
        T[] temp = (T[]) new Object[lista.length * 2];
        for (int i = 0; i < rear; i++) {
            temp[i] = lista[i];
        }
        lista = temp;
    }

    /**
     * Returns the index of the specified element in the list.
     *
     * @param index the element to search for
     * @return the index of the element, or -1 if it is not found
     */
    protected int findIndex(T index) {
        for (int i = 0; i < rear; i++) {
            if (lista[i].equals(index)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Private inner class to provide an iterator for the ArrayList.
     */
    private class ArrayIterator implements Iterator<T> {

        private int current;
        private int expectedModCount;
        private boolean isOkToRemove;

        public ArrayIterator(T[] array, int count) {
            this.expectedModCount = modCount;
            this.current = 0;
            isOkToRemove = false;
        }

        @Override
        public boolean hasNext() {
            return current < expectedModCount;
        }

        @Override
        public T next() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            isOkToRemove = true;
            ++current;
            return (T) lista[current - 1];
        }

        @Override
        public void remove() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (!isOkToRemove) {
                throw new ConcurrentModificationException();
            }

            T element = next();
            ArrayList.this.remove(element);
            isOkToRemove = true;
            expectedModCount++;
            current--;
        }
    }
}
