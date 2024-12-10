package Collections.Linked;


import Collections.Interfaces.QueueADT;

public class LinkedQueue<T> implements QueueADT<T> {

    private LinearNode<T> front, rear;
    private int count;

    public LinkedQueue() {
        front = null;
        rear = null;
        count = 0;
    }

    public LinearNode<T> getFront() {
        return front;
    }

    public LinearNode<T> getRear() {
        return rear;
    }

    @Override
    public void enqueue(T element) {
        LinearNode<T> newNode = new LinearNode<>(element);
        if (rear == null) {
            front = rear = newNode;
        }
        else {
            rear.setNext(newNode);
            rear = newNode;
        }
        count++;
    }

    @Override
    public T dequeue() {
        if (isEmpty()) {
            return null;
        }
        if (front == rear) {
            rear = null;
        }
        LinearNode<T> temp = front;
        front = front.getNext();
        count--;
        return temp.getElement();
    }

    @Override
    public T first() {
        return ((T) this.front);
    }

    @Override
    public boolean isEmpty() {
        if (this.front == null){
            return true;
        };
        return false;
    }

    @Override
    public int size() {
        return this.count;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        LinearNode<T> current = front;
        while (current != null) {
            sb.append(current.getElement());

            if (current.getNext() != null) {
                sb.append(", ");
            }

            current = current.getNext();
        }

        sb.append("]");
        return sb.toString();
    }
}
