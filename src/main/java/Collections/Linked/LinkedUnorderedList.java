package Collections.Linked;

import Collections.Interfaces.UnorderedListADT;

import java.util.NoSuchElementException;

public class LinkedUnorderedList<T> extends LinkedList<T> implements UnorderedListADT<T> {

    /**
     * Constructs an empty unordered list.
     */
    public LinkedUnorderedList() {
        super();
    }

    /**
     * Adds the specified element to the front of the list.
     *
     * @param element the element to be added to the front of the list
     */
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

    /**
     * Adds the specified element to the front of the list.
     *
     * @param element the element to be added to the front of the list
     */
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

    /**
     * Adds the specified element after the target element in the list.
     *
     * @param element the element to be added after the target element
     * @param target the target element after which the new element will be added
     * @throws NoSuchElementException if the target element is not found in the list
     */
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
