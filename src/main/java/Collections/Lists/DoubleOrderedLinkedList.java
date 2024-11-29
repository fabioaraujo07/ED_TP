package Collections.Lists;


public class DoubleOrderedLinkedList<T> extends DoubleLinkedList<T> implements OrderedListADT<T> {

    public DoubleOrderedLinkedList() {
        super();
    }

    @Override
    public void add(T element) {
        if (!(element instanceof Comparable)) {
            throw new IllegalArgumentException("Not Comparable");
        }
        Comparable<T> comparable = (Comparable<T>) element;
        DoubleNode<T> newNode = new DoubleNode<>(element);

        if (isEmpty()){
            head = newNode;
            tail = newNode;
        } else {
            DoubleNode<T> current = head;
            while (current != null && comparable.compareTo(current.getData()) > 0) {
                current = current.getNext();
            }

            if (current == null) {
                newNode.setPrev(tail);
                tail.setNext(newNode);
                tail = newNode;
            } else if (current.getPrev() == null) {
                newNode.setNext(head);
                head.setPrev(newNode);
                head = newNode;
            }else {
                newNode.setNext(current);
                newNode.setPrev(current.getPrev());
                current.getPrev().setNext(newNode);
                current.setPrev(newNode);
            }
        }
        size++;
        modCount++;
    }
}
