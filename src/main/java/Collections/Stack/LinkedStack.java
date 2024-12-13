package Collections.Stack;


import Collections.Interfaces.StackADT;
import Collections.Linked.LinearNode;
import Exceptions.EmptyCollectionException;

public class LinkedStack<T> implements StackADT<T> {

    private int count;
    private LinearNode<T> top;

    /**
     * Constructs an empty stack.
     */
    public LinkedStack() {
        count = 0;
        top = null;
    }

    /**
     * Adds the specified element to the top of the stack.
     *
     * @param item the element to be added to the stack
     */
    @Override
    public void push(T item) {
        LinearNode<T> newNode = new LinearNode<>(item);
        newNode.setNext(top);
        top = newNode;
        count++;
    }

    /**
     * Removes and returns the top element from the stack.
     *
     * @return the element removed from the stack
     * @throws EmptyCollectionException if the stack is empty
     */
    @Override
    public T pop() {

        if (isEmpty()) {
            throw new EmptyCollectionException("Stack is empty");
        }

        T element = top.getElement();
        top = top.getNext();
        count--;
        return element;
    }

    /**
     * Returns the top element of the stack without removing it.
     *
     * @return the top element of the stack
     * @throws EmptyCollectionException if the stack is empty
     */
    @Override
    public T peek() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Stack is empty");
        }
        return top.getElement();
    }

    /**
     * Returns true if the stack is empty, false otherwise.
     *
     * @return true if the stack is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Returns the number of elements in the stack.
     *
     * @return the number of elements in the stack
     */
    @Override
    public int size() {
        return count;
    }

    /**
     * Returns a string representation of the stack.
     *
     * @return a string representation of the stack
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder result = new StringBuilder("[");
        LinearNode<T> current = top;
        while (current != null) {
            result.append(current.getElement());
            current = current.getNext();
            if (current != null) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }
}
