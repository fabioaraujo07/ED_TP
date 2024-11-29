package Collections.Lists;

public class UnorderedArrayList<T> extends ArrayList<T> implements UnorderedListADT<T> {
    public UnorderedArrayList() {
        super();
    }

    public UnorderedArrayList(int initialCapacity) {
        super(initialCapacity);
    }

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

    @Override
    public void addToRear(T element) {
        if (this.rear == this.lista.length){
            expandCapacity();
        }

        this.lista[this.rear] = element;
        this.rear++;
        this.modCount++;

    }

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
