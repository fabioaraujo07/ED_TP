package Collections.Stack;

import Collections.Lists.LinearNode;
import Exceptions.EmptyCollectionException;

public class LinkedStack<T> implements StackADT<T> {

    private int count;

    private LinearNode<T> top;

    public LinkedStack() {
        count = 0;
        top = null;
    }


    @Override
    public void push(T item) {
        LinearNode<T> newNode = new LinearNode<>(item);

            newNode.setNext(top);
            top = newNode;
            count++;
        }

    @Override
    public T pop() {
        T element = top.getElement();
        top = top.getNext();
        count--;
        return element;
    }

    @Override
    public T peek() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Stack is empty");
        }
        return top.getElement();
    }

    @Override
    public boolean isEmpty() {
        if (count == 0) {
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public String toString() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Stack is empty");
        }

        String result = "";
        LinearNode<T> current = top;

        int count = 0;


        while (current != null) {
            result += current.getElement().toString();
            current = current.getNext();

            if (current != null) {
                result += " ";
            }
            count++;
        }

        return result;
    }

}
