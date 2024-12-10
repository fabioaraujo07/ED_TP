package Collections.Array;




import Collections.Interfaces.BinaryTreeADT;
import Collections.Linked.LinkedQueue;
import Exceptions.ElementNotFound;

import java.util.Iterator;

public class ArrayBinaryTree<T> implements BinaryTreeADT<T> {

    protected int count;
    protected T[] tree;
    private final int CAPACITY = 50;

    public ArrayBinaryTree() {
        tree = (T[]) new Object[CAPACITY];
        count = 0;
    }

    public ArrayBinaryTree(T element) {
        tree = (T[]) new Object[CAPACITY];
        tree[0] = element;
        count = 1;
    }

    @Override
    public T getRoot() {
        return tree[0];
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public boolean contains(T targetElement) {
        return false;
    }

    @Override
    public T find(T targetElement) throws ElementNotFound {
        T temp = null;
        boolean found = false;

        for (int i = 0; i < count && !found; i++) {
            if (tree[i].equals(targetElement)) {
                found = true;
                temp = tree[i];
            }
        }
        if (!found) {
            throw new ElementNotFound("Element not found");
        }
        return temp;
    }

    @Override
    public Iterator<T> iteratorInOrder() {
        UnorderedArrayList<T> templist = new UnorderedArrayList<>();
        inorder (0, templist);
        return templist.iterator();
    }

    protected void inorder (int node, UnorderedArrayList<T> templist) {
        if (node < tree.length)
            if (tree[node] != null) {
                inorder(node * 2 + 1, templist);
                templist.addToRear(tree[node]);
                inorder((node + 1) * 2, templist);
            }
    }

    @Override
    public Iterator<T> iteratorPreOrder() {
        UnorderedArrayList<T> templist = new UnorderedArrayList<>();
        preorder (0, templist);
        return templist.iterator();
    }

    protected void preorder (int node, UnorderedArrayList<T> templist) {
        if (node < tree.length)
            if (tree[node] != null) {
                templist.addToRear(tree[node]);
                preorder (node * 2 + 1, templist);
                preorder((node + 1) * 2, templist);
            }
    }

    @Override
    public Iterator<T> iteratorPostOrder() {
        UnorderedArrayList<T> templist = new UnorderedArrayList<>();
        postorder (0, templist);
        return templist.iterator();
    }

    protected void postorder (int node, UnorderedArrayList<T> templist) {
        if (node < tree.length)
            if (tree[node] != null) {
                postorder (node * 2 + 1, templist);
                postorder((node + 1) * 2, templist);
                templist.addToRear(tree[node]);
            }
    }

    @Override
    public Iterator<T> iteratorLevelOrder() {
        UnorderedArrayList<T> templist = new UnorderedArrayList<>();
        levelOrder(templist);
        return templist.iterator();
    }


    protected void levelOrder(UnorderedArrayList<T> templist) {
        LinkedQueue<Integer> queue = new LinkedQueue<>();
        queue.enqueue(0); // Começa com a raiz (índice 0)

        while (!queue.isEmpty()) {
            int node = queue.dequeue(); // Remove o nó da frente da fila
            if (node < tree.length && tree[node] != null) {
                templist.addToRear(tree[node]); // Visita o nó atual

                // Adiciona o filho esquerdo e direito à fila, se existirem
                int leftChild = node * 2 + 1;
                int rightChild = (node + 1) * 2;

                if (leftChild < tree.length && tree[leftChild] != null) {
                    queue.enqueue(leftChild);
                }

                if (rightChild < tree.length && tree[rightChild] != null) {
                    queue.enqueue(rightChild);
                }
            }
        }
    }

    protected void expandCapacity() {
        int  newCapacity = tree.length * 2;
        T[] newTree = (T[]) new Object[newCapacity];

        for (int i = 0; i < count; i++) {
            newTree[i] = tree[i];
        }
        tree = newTree;
    }

}
