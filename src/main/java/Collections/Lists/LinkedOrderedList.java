package Collections.Lists;


public class LinkedOrderedList<T> extends LinkedList<T> implements Collections.Lists.OrderedListADT<T> {

    @Override
    public void add(T element) {
        if (!(element instanceof Comparable)) {
            throw new ClassCastException();
        }
        Comparable<T> comparableElement = (Comparable<T>) element;
        LinearNode<T> newNode = new LinearNode<>(element);
        if (isEmpty()){
            front = newNode;
            rear = newNode;
        }else {
            LinearNode<T> current = front;
            LinearNode<T> previous = null;
            while (current != null && comparableElement.compareTo(current.getElement()) < 0) {
                previous = current;
                current = current.getNext();
            }
            if (previous == null) {
                newNode.setNext(front);
                front = newNode;
            }else {
                newNode.setNext(current);
                previous.setNext(newNode);
            }
            if (newNode.getNext() == null) {
                rear = newNode;
            }
        }
        count++;
        modCount++;
    }
}
