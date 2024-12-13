package Collections.Linked;

public class LinearNode<T>{

    private LinearNode<T> next;
    private T element;

    /**
     * Constructs an empty node.
     */
    public LinearNode() {
        this.next = null;
        this.element = null;
    }

    /**
     * Constructs a node with the specified element.
     *
     * @param element the element to be stored in the node
     */
    public LinearNode(T element){
        this.next = null;
        this.element = element;
    }

    /**
     * Returns the next node.
     *
     * @return the next node
     */
    public LinearNode<T> getNext() {
        return next;
    }

    /**
     * Returns the element stored in the node.
     *
     * @return the element stored in the node
     */
    public T getElement() {
        return element;
    }

    /**
     * Sets the next node.
     *
     * @param next the next node
     */
    public void setNext(LinearNode<T> next) {
        this.next = next;
    }

    /**
     * Sets the element stored in the node.
     *
     * @param element the element to be stored in the node
     */
    public void setElement(T element) {
        this.element = element;
    }
}
