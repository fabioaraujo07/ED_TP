package Collections.Lists;

public class LinearNode<T>{

    private LinearNode<T> next;

    private T element;


    public LinearNode() {
        this.next = null;
        this.element = null;
    }

    public LinearNode(T element){
        this.next = null;
        this.element = element;
    }

    public LinearNode<T> getNext() {
        return next;
    }

    public T getElement() {
        return element;
    }

    public void setNext(LinearNode<T> next) {
        this.next = next;
    }

    public void setElement(T element) {
        this.element = element;
    }
}
