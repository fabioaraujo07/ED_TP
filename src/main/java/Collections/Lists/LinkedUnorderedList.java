package Collections.Lists;

import java.util.NoSuchElementException;

public class LinkedUnorderedList<T> extends LinkedList<T> implements UnorderedListADT<T> {

    @Override
    public void addToFront(T element) {
        LinearNode<T> newNode = new LinearNode<>(element);
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        }else {
            newNode.setNext(front);
            front = newNode;
        }
        count++;
        modCount++;
    }

    @Override
    public void addToRear(T element) {
        LinearNode<T> newNode = new LinearNode<>(element);
        if (isEmpty()) {
            front = newNode;
        }else {
            rear.setNext(newNode);
        }
        rear = newNode;
        count++;
        modCount++;
    }

    @Override
    public void addAfter(T element, T target) {
        if (isEmpty()) {
            throw new NoSuchElementException("The list is empty.");
        }
        LinearNode<T> current = front;

        while (current != null && !current.getElement().equals(target)) {
            current = current.getNext();
        }

        if (current == null) {
            throw new NoSuchElementException("Target element not found.");
        }

        LinearNode<T> newNode = new LinearNode<>(element);
        newNode.setNext(current.getNext());
        current.setNext(newNode);

        if (current == rear) {
            rear = newNode;
        }

        count++;
        modCount++;
    }
}
