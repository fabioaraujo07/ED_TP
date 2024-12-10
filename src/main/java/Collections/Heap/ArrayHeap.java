package Collections.Heap;


import Collections.Array.ArrayBinaryTree;
import Collections.Interfaces.HeapADT;
import Exceptions.EmptyCollectionException;

public class ArrayHeap<T> extends ArrayBinaryTree<T> implements HeapADT<T> {

    public ArrayHeap() {
        super();
    }

    @Override
    public void addElement(T obj) {
        if (count == tree.length){
            expandCapacity();
        }
        tree[count] = obj;
        count++;

        if (count>1){
            heapifyAdd();
        }
    }

    /**
     * Reorders this heap to maintain the ordering property after
     * adding a node.
     */
    private void heapifyAdd() {
        T temp;
        int next = count - 1;
        temp = tree[next];
        while ((next != 0) && (((Comparable)temp).compareTo(tree[(next-1)/2]) < 0)) {
            tree[next] = tree[(next-1)/2];
            next = (next-1)/2;
        }
        tree[next] = temp;
    }

    @Override
    /**
     * Remove the element with the lowest value in this heap and
     * returns a reference to it.
     * Throws an EmptyCollectionException if the heap is empty.
     *
     * @return a reference to the element with the
     * lowest value in this head
     * @throws EmptyCollectionException if an empty collection
     * exception occurs
     */
    public T removeMin() throws EmptyCollectionException
    {
        if (isEmpty())
            throw new EmptyCollectionException ("Empty Heap");
        T minElement = tree[0];
        tree[0] = tree[count-1];
        heapifyRemove();
        count--;
        return minElement;
    }

    /**
     * Reorders this heap to maintain the MinHeap ordering property after removal.
     * Ensures that the smallest element is at the root and every parent node
     * is smaller than its children.
     */
    private void heapifyRemove() {
        T temp; // Temporarily stores the element to reposition
        int node = 0; // Start from the root
        int left = 2 * node + 1; // Index of the left child
        int right = 2 * (node + 1); // Index of the right child
        int next; // Index of the next node to swap with

        // Determine the initial child to compare
        if ((tree[left] == null) && (tree[right] == null)) {
            next = count; // If no children, stop the process
        } else if (tree[left] == null) {
            next = right; // Only the right child exists
        } else if (tree[right] == null) {
            next = left; // Only the left child exists
        } else if (((Comparable) tree[left]).compareTo(tree[right]) < 0) {
            next = left; // Left child is smaller
        } else {
            next = right; // Right child is smaller
        }

        // Store the root element to reposition later
        temp = tree[node];

        // Loop to reposition the element while maintaining MinHeap property
        while ((next < count) && (((Comparable) tree[next]).compareTo(temp) < 0)) {
            // Swap the current node with the smaller child
            tree[node] = tree[next];
            node = next; // Move down to the child

            // Update indices of the left and right children
            left = 2 * node + 1;
            right = 2 * (node + 1);

            // Determine the next child to compare
            if ((left >= count || tree[left] == null) && (right >= count || tree[right] == null)) {
                next = count; // No more children, stop
            } else if (right >= count || tree[right] == null) {
                next = left; // Only left child exists
            } else if (left >= count || tree[left] == null) {
                next = right; // Only right child exists
            } else if (((Comparable) tree[left]).compareTo(tree[right]) < 0) {
                next = left; // Left child is smaller
            } else {
                next = right; // Right child is smaller
            }
        }

        // Place the original element in its correct position
        tree[node] = temp;
    }


    @Override
    public T findMin() {
        return tree[0];
    }
}
