package Collections.Lists;

public class ArrayOrderedList<T> extends ArrayList<T> implements OrderedListADT<T> {
    public ArrayOrderedList() {
        super();
    }

    public ArrayOrderedList(int tamanho) {
        super(tamanho);
    }

    @Override
    public void add(T element) {
        if (this.rear == this.lista.length){
            expandCapacity();
        }

        if (!(element instanceof Comparable)) {
            throw new ClassCastException();
        }

        int index = findPosition((Comparable<T> )element);
        for (int i = this.rear;i > index;i--){
            this.lista[i] = this.lista[i-1];

        }
        this.lista[index]=element;
        this.rear++;
        this.modCount++;

    }

    private int findPosition(Comparable<T> element) {
        int pos = 0;
        while (pos < this.lista.length && this.lista[pos] != null && element.compareTo(this.lista[pos]) <= 0) {
            pos++;
        }
        return pos;
    }
}
