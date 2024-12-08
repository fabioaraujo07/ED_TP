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

        if (isEmpty()) {
            throw new EmptyCollectionException("Stack is empty");
        }

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
        return count == 0;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]"; // Representação de uma pilha vazia
        }

        StringBuilder result = new StringBuilder("[");
        LinearNode<T> current = top; // Supondo que `top` é o nó superior da pilha
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
