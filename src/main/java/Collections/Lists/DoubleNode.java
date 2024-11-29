package Collections.Lists;

public class DoubleNode<T> {
    private T data;
    private DoubleNode<T> next;
    private DoubleNode<T> prev;

    public DoubleNode(T data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }

    public DoubleNode(){
        this.data = null;
        this.next = null;
        this.prev = null;
    }

    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    public DoubleNode<T> getNext() {
        return next;
    }
    public void setNext(DoubleNode<T> next) {
        this.next = next;
    }
    public DoubleNode<T> getPrev() {
        return prev;
    }
    public void setPrev(DoubleNode<T> prev) {
        this.prev = prev;
    }
    @Override
    public String toString() {
        return data != null ? data.toString() : "null";
    }

}
