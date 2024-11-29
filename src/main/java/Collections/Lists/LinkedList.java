package Collections.Lists;

import Exceptions.EmptyCollectionException;

import java.util.Iterator;
import java.util.NoSuchElementException;


public abstract class LinkedList<T> implements ListADT<T> {

    protected int modCount;
    protected int count;
    protected LinearNode<T> front, rear;

    public LinkedList() {
        this.modCount = 0;
        this.count = 0;
        this.front = null;
        this.rear = null;
    }

    @Override
    public T removeFirst() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Empty list");
        }
        LinearNode<T> removedNode = this.front;

        this.front = front.getNext();

        //Check if the list is now empty.
        if (this.front == null) {
            this.rear = null;
        }
        this.count--;
        this.modCount++;

        return removedNode.getElement();
    }

    @Override
    public T removeLast() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Empty list");
        }

        LinearNode<T> removedNode = rear;

        if (front == rear) {
            front = rear = null;
        } else {
            // Percorre a lista para encontrar o penúltimo nó
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


    @Override
    public T first() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Empty list");
        }
        return front.getElement();
    }

    @Override
    public T last() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Empty list");
        }
        return rear.getElement();
    }

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

    @Override
    public boolean contains(T target) {
        try {
            find(target);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public boolean isEmpty() {
        return (count == 0);
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public Iterator<T> iterator() {
        return new MyIterator();
    }

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

    private class MyIterator implements Iterator<T> {

        private boolean okToRemove;
        private int expectedModCount;
        private LinearNode<T> current;
        private LinearNode<T> lastReturned;

        public MyIterator() {
            expectedModCount = modCount;
            current = front;
            okToRemove = false;
        }

        @Override
        public boolean hasNext() {
            return (current != null);
        }

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
            //incrementa e retorna
            current = current.getNext();
            return toReturn;
        }

        @Override
        public void remove() {
            if (expectedModCount != modCount) {
                throw new java.util.ConcurrentModificationException();
            }

            if (!okToRemove) {
                throw new IllegalStateException("Next() not called or element already removed.");
            }

            // Atualiza para que não seja possível remover o mesmo elemento novamente
            okToRemove = false;

            try {
                // Reinicia do início para encontrar o nó anterior ao lastReturned
                LinearNode<T> previous = null;
                LinearNode<T> current = front;

                // Percorre até encontrar o lastReturned
                while (current != null && current != lastReturned) {
                    previous = current;
                    current = current.getNext();
                }

                if (current == null) {
                    throw new IllegalStateException("Iterator corrupted.");
                }

                // Remove o lastReturned da lista
                if (previous == null) { // Caso especial: lastReturned é o primeiro elemento
                    front = front.getNext();
                } else {
                    previous.setNext(current.getNext());
                }

                // Atualiza rear caso o último elemento tenha sido removido
                if (current == rear) {
                    rear = previous;
                }

                count--;
                modCount++;
                expectedModCount = modCount;

            } catch (EmptyCollectionException e) {
                // Tratar exceção caso necessário (neste caso, geralmente é ignorado)
            }
        }

    }

    //recursividade exercicio 1
    public String imprimir(LinearNode<T> node) {
        if (node == null) {
            return "";

        }
        return node.getElement() + " " +  imprimir(node.getNext());
    }

}
