package Collections.Lists;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class ArrayList<T> implements ListADT<T> {

    protected T[] lista;
    protected int rear;
    protected int modCount;


    public ArrayList() {
        this.lista = (T[]) new Object[20];
        this.rear = 0;
    }

    public ArrayList(int size) {
        this.lista = (T[]) new Object[size];
        this.rear = 0;
    }

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

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null; //criar uma exceção
        }
        --rear;
        T result = lista[rear];
        lista[rear] = null;
        modCount++;
        return result;
    }

    protected int findIndex(T index) {
        for (int i = 0; i < rear; i++) {
            if (lista[i].equals(index)) {
                return i;
            }
        }
        return -1;
    }

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

    @Override
    public T first() {
        return this.lista[0];
    }

    @Override
    public T last() {
        return this.lista[this.lista.length - 1];
    }

    @Override
    public boolean contains(T target) {
        return findIndex(target) != -1;
    }

    @Override
    public boolean isEmpty() {
        return rear == 0;
    }

    @Override
    public int size() {
        return rear;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator(lista, rear);
    }

    protected void expandCapacity() {
        T[] temp = (T[]) new Object[lista.length * 2];
        for (int i = 0; i < rear; i++) {
            temp[i] = lista[i];
        }
        lista = temp;
    }

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
