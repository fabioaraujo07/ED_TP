package Collections.Linked;


import Collections.Interfaces.QueueADT;

public class LinkedQueue<T> implements QueueADT<T> {

    private LinearNode<T> front, rear;
    private int count;

    /**
     * Constructs an empty queue.
     */
    public LinkedQueue() {
        front = null;
        rear = null;
        count = 0;
    }

    /**
     * Returns the front node of the queue.
     *
     * @return the front node of the queue
     */
    public LinearNode<T> getFront() {
        return front;
    }
    /**
     * Returns the rear node of the queue.
     *
     * @return the rear node of the queue
     */
    public LinearNode<T> getRear() {
        return rear;
    }

    /**
     * Adds the specified element to the rear of the queue.
     *
     * @param element the element to be added to the queue
     */
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

    /**
     * Removes and returns the front element from the queue.
     *
     * @return the front element from the queue
     */
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

    /**
     * Returns the front element of the queue without removing it.
     *
     * @return the front element of the queue
     */
    @Override
    public T first() {
        return ((T) this.front);
    }

    /**
     * Returns true if the queue is empty, false otherwise.
     *
     * @return true if the queue is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        if (this.front == null){
            return true;
        };
        return false;
    }

    /**
     * Returns the number of elements in the queue.
     *
     * @return the number of elements in the queue
     */
    @Override
    public int size() {
        return this.count;
    }

    /**
     * Returns a string representation of the queue.
     *
     * @return a string representation of the queue
     */
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
