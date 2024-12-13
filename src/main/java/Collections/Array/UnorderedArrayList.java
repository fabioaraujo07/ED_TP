package Collections.Array;

import Collections.Interfaces.UnorderedListADT;

public class UnorderedArrayList<T> extends ArrayList<T> implements UnorderedListADT<T> {

    /**
     * Creates an empty unordered array list with default capacity.
     */
    public UnorderedArrayList() {
        super();
    }

    /**
     * Creates an empty unordered array list with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list
     */
    public UnorderedArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Adds the specified element to the front of the list.
     * All existing elements are shifted one position to the right.
     *
     * @param element the element to be added to the front of the list
     */
    @Override
    public void addToFront(T element) {
        if (this.rear == this.lista.length){
            expandCapacity();
        }

        for (int i = this.rear; i > 0; i--){
            this.lista[i] = this.lista[i-1];
        }
        this.lista[0] = element;
        this.rear++;
        this.modCount++;

    }

    /**
     * Adds the specified element to the rear of the list.
     *
     * @param element the element to be added to the rear of the list
     */
    @Override
    public void addToRear(T element) {
        if (this.rear == this.lista.length){
            expandCapacity();
        }

        this.lista[this.rear] = element;
        this.rear++;
        this.modCount++;

    }

    /**
     * Adds the specified element after the specified target element in the list.
     * All elements after the target are shifted one position to the right.
     *
     * @param element the element to be added
     * @param target  the target element after which the new element will be added
     * @throws IllegalArgumentException if the target element is not found in the list
     */
    @Override
    public void addAfter(T element, T target) {
        if (this.rear == this.lista.length){
            expandCapacity();
        }
        int index = findIndex(target);
        for (int i = this.rear; i > index; i--){//Depois ver o porquê
            this.lista[i] = this.lista[i-1];
        }
        this.lista[index] = element;
        this.rear++;
        this.modCount++;
    }
}
