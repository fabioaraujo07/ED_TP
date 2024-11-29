package Collections.Lists;


import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DoubleLinkedList<T> implements ListADT<T> {

    protected DoubleNode<T> head;
    protected DoubleNode<T> tail;
    protected int size;
    protected int modCount;

    public DoubleLinkedList() {
        head = null;
        tail = null;
        size = 0;
        modCount = 0;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty list");
        }
        DoubleNode<T> temp = head;
        head = head.getNext();

        if (head == null) {
            tail = null;
        }else {
            head.setPrev(null);
        }
        size--;
        modCount++;

        return temp.getData();
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty list");
        }
        DoubleNode<T> temp = tail;
        tail = tail.getPrev();

        if (tail == null) {
            head = null;
        }else {
            tail.setNext(null);
        }
        size--;
        modCount++;
        return temp.getData();
    }

    @Override
    public T remove(T element) {
        if(isEmpty()){
            throw new NoSuchElementException("Empty list");
        }
        DoubleNode<T> toRemove = find(element);
        if (size == 1){
            head = tail = null;
        } else if (toRemove.equals(head)) {
            removeFirst();
        } else if (toRemove.equals(tail)) {
            removeLast();
        }else {
            toRemove.getPrev().setNext(toRemove.getNext());
            toRemove.getNext().setPrev(toRemove.getPrev());
        }
        size--;
        modCount++;
        return toRemove.getData();
    }

    protected DoubleNode<T> find(T element){
        DoubleNode<T> current = head;
        while (current!=null && !current.getData().equals(element)) {
            current = current.getNext();
        }
        return current;
    }

    @Override
    public T first() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty list");
        }
        return head.getData();
    }

    @Override
    public T last() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty list");
        }
        return tail.getData();
    }

    @Override
    public boolean contains(T target) {
        try {
            find(target);
            return true;
        }catch (NoSuchElementException e){
            return false;
        }
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new DoubleLinkedListIterator();
    }

    private class DoubleLinkedListIterator implements Iterator<T> {
        private DoubleNode<T> current;
        private int expectedModCount;
        private boolean okToRemove;

        public DoubleLinkedListIterator() {
            current = head;
            expectedModCount = modCount;
            okToRemove = false;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException("The list was modified during the iteration");
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            okToRemove = true;

            T element = current.getData();
            current = current.getNext();
            return element;
        }

        @Override
        public void remove() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException("The list was modified during the iteration");
            }
            if (!okToRemove) {
                throw new NoSuchElementException();
            }
            okToRemove = false;

            try {
                DoubleLinkedList.this.remove(this.current.getPrev().getData());
                expectedModCount++;
            }catch (NoSuchElementException e){
                throw new ConcurrentModificationException("The list was modified during the iteration");
            }
        }
    }
}
