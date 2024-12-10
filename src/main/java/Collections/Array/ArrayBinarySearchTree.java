package Collections.Array;



import Collections.Interfaces.BinarySearchTreeADT;
import Exceptions.ElementNotFound;

import java.util.Iterator;

public class ArrayBinarySearchTree<T> extends ArrayBinaryTree<T> implements BinarySearchTreeADT<T> {

    protected int height;
    protected int maxIndex;

    public ArrayBinarySearchTree() {
        super();
        height = 0;
        maxIndex = -1;
    }

    public ArrayBinarySearchTree(T element) {
        super(element);
        height = 1;
        maxIndex = 0;
    }

    @Override
    public void addElement (T element) {
        if (tree.length < maxIndex*2+3){
            expandCapacity();
        }

        Comparable<T> tempelement = (Comparable<T>)element;

        if (isEmpty())
        {
            tree[0] = element;
            maxIndex = 0;
        }

        else{
            boolean added = false;
            int currentIndex = 0;
            while (!added)
            {
                if (tempelement.compareTo((tree[currentIndex]) ) < 0)
                {
                    /** go left */
                    if (tree[currentIndex*2+1] == null)
                    {
                        tree[currentIndex*2+1] = element;
                        added = true;
                        if (currentIndex*2+1 > maxIndex)
                            maxIndex = currentIndex*2+1;
                    }
                    else
                        currentIndex = currentIndex*2+1;
                }
                else
                {
                    /** go right */
                    if (tree[currentIndex*2+2] == null)
                    {
                        tree[currentIndex*2+2] = element;
                        added = true;
                        if (currentIndex*2+2 > maxIndex)
                            maxIndex = currentIndex*2+2;
                    }
                    else
                        currentIndex = currentIndex*2+2;
                }
            }
        }
        height = (int)(Math.log(maxIndex + 1) / Math.log(2)) + 1;
        count++;
    }


    protected int findIndex(Comparable<T> targetElement, int currentIndex) throws ElementNotFound {
        if (currentIndex >= tree.length || tree[currentIndex] == null) {
            throw new ElementNotFound("Element not found in binary search tree.");
        }

        Comparable<T> currentElement = (Comparable<T>) tree[currentIndex];

        if (targetElement.compareTo((T) currentElement) == 0) {
            return currentIndex;
        }

        if (targetElement.compareTo((T) currentElement) < 0) {
            return findIndex(targetElement, currentIndex * 2 + 1);
        } else {
            return findIndex(targetElement, currentIndex * 2 + 2);
        }
    }


    @Override
    public T removeElement(T targetElement) throws ElementNotFound {
        if (isEmpty()) {
            throw new ElementNotFound("binary search tree");
        }

        Comparable<T> tempElement = (Comparable<T>) targetElement;

        int targetIndex = findIndex(tempElement, 0);

        T result = tree[targetIndex];
        replace(targetIndex);
        count--;

        int temp = maxIndex;
        maxIndex = -1;
        for (int i = 0; i <= temp; i++) {
            if (tree[i] != null) {
                maxIndex = i;
            }
        }

        height = (int) (Math.log(maxIndex + 1) / Math.log(2)) + 1;

        return result;
    }

    /**
     * Removes the node specified for removal and shifts the tree array accordingly.
     *
     * @param targetIndex the node to be removed
     */
    protected void replace(int targetIndex) {
        int currentIndex, parentIndex, temp, oldIndex, newIndex;
        UnorderedArrayList<Integer> oldlist = new UnorderedArrayList<>();
        UnorderedArrayList<Integer> newlist = new UnorderedArrayList<>();
        UnorderedArrayList<Integer> templist = new UnorderedArrayList<>();
        Iterator<Integer> oldIt, newIt;

        /**
         * if target node has no children
         */
        if ((targetIndex * 2 + 1 >= tree.length) || (targetIndex * 2 + 2 >= tree.length)) {
            tree[targetIndex] = null;
        } /**
         * if target node has no children
         */
        else if ((tree[targetIndex * 2 + 1] == null) && (tree[targetIndex * 2 + 2] == null)) {
            tree[targetIndex] = null;
        } /**
         * if target node only has a left child
         */
        else if ((tree[targetIndex * 2 + 1] != null) && (tree[targetIndex * 2 + 2] == null)) {
            /**
             * fill newlist with indices of nodes that will replace the corresponding indices in oldlist
             */
            currentIndex = targetIndex * 2 + 1;
            templist.addToRear(currentIndex);
            while (!templist.isEmpty()) {
                currentIndex = (templist.removeFirst());
                newlist.addToRear(currentIndex);
                if ((currentIndex * 2 + 2) <= (Math.pow(2, height) - 2)) {
                    templist.addToRear(currentIndex * 2 + 1);
                    templist.addToRear(currentIndex * 2 + 2);
                }
            }

            /**
             * fill oldlist
             */
            currentIndex = targetIndex;
            templist.addToRear(currentIndex);
            while (!templist.isEmpty()) {
                currentIndex = (templist.removeFirst());
                oldlist.addToRear(currentIndex);
                if ((currentIndex * 2 + 2) <= (Math.pow(2, height) - 2)) {
                    templist.addToRear(currentIndex * 2 + 1);
                    templist.addToRear(currentIndex * 2 + 2);
                }
            }

            /**
             * do replacement
             */
            oldIt = oldlist.iterator();
            newIt = newlist.iterator();
            while (newIt.hasNext()) {
                oldIndex = oldIt.next();
                newIndex = newIt.next();
                tree[oldIndex] = tree[newIndex];
                tree[newIndex] = null;
            }
        } /**
         * if target node only has a right child
         */
        else if ((tree[targetIndex * 2 + 1] == null) && (tree[targetIndex * 2 + 2] != null)) {
            /**
             * fill newlist with indices of nodes that will replace the corresponding indices in oldlist
             */
            currentIndex = targetIndex * 2 + 2;
            templist.addToRear(currentIndex);
            while (!templist.isEmpty()) {
                currentIndex = (templist.removeFirst());
                newlist.addToRear(currentIndex);
                if ((currentIndex * 2 + 2) <= (Math.pow(2, height) - 2)) {
                    templist.addToRear(currentIndex * 2 + 1);
                    templist.addToRear(currentIndex * 2 + 2);
                }
            }

            /**
             * fill oldlist
             */
            currentIndex = targetIndex;
            templist.addToRear(currentIndex);
            while (!templist.isEmpty()) {
                currentIndex = (templist.removeFirst());
                oldlist.addToRear(currentIndex);
                if ((currentIndex * 2 + 2) <= (Math.pow(2, height) - 2)) {
                    templist.addToRear(currentIndex * 2 + 1);
                    templist.addToRear(currentIndex * 2 + 2);
                }
            }

            /**
             * do replacement
             */
            oldIt = oldlist.iterator();
            newIt = newlist.iterator();
            while (newIt.hasNext()) {
                oldIndex = oldIt.next();

                newIndex = newIt.next();
                tree[oldIndex] = tree[newIndex];
                tree[newIndex] = null;
            }
        } /**
         * if target node has two children
         */
        else {
            currentIndex = targetIndex * 2 + 2;

            while (tree[currentIndex * 2 + 1] != null) {
                currentIndex = currentIndex * 2 + 1;
            }

            tree[targetIndex] = tree[currentIndex];

            /**
             * the index of the root of the subtree to be replaced
             */
            int currentRoot = currentIndex;

            /**
             * if currentIndex has a right child
             */
            if (tree[currentRoot * 2 + 2] != null) {
                /**
                 * fill newlist with indices of nodes that will replace the corresponding indices in oldlist
                 */
                currentIndex = currentRoot * 2 + 2;
                templist.addToRear(currentIndex);
                while (!templist.isEmpty()) {
                    currentIndex = (templist.removeFirst());
                    newlist.addToRear(currentIndex);
                    if ((currentIndex * 2 + 2) <= (Math.pow(2, height) - 2)) {
                        templist.addToRear(currentIndex * 2 + 1);
                        templist.addToRear(currentIndex * 2 + 2);
                    }
                }

                /**
                 * fill oldlist
                 */
                currentIndex = currentRoot;
                templist.addToRear(currentIndex);
                while (!templist.isEmpty()) {
                    currentIndex = (templist.removeFirst());
                    oldlist.addToRear(currentIndex);
                    if ((currentIndex * 2 + 2) <= (Math.pow(2, height) - 2)) {
                        templist.addToRear(currentIndex * 2 + 1);
                        templist.addToRear(currentIndex * 2 + 2);
                    }
                }

                /**
                 * do replacement
                 */
                oldIt = oldlist.iterator();
                newIt = newlist.iterator();
                while (newIt.hasNext()) {
                    oldIndex = oldIt.next();
                    newIndex = newIt.next();

                    tree[oldIndex] = tree[newIndex];
                    tree[newIndex] = null;
                }
            } else {
                tree[currentRoot] = null;
            }
        }
    }

    @Override
    public void removeAllOccurrences(T targetElement) {
        try {
            while (true) {
                removeElement(targetElement);
            }
        } catch (ElementNotFound e) {
            throw new ElementNotFound("No more element");
        }
    }


    @Override
    public T removeMin() {
        if (isEmpty()) {
            throw new ElementNotFound("binary search tree");
        }

        int currentIndex = 0;

        // Percorre até o nó mais à esquerda
        while (currentIndex * 2 + 1 < tree.length && tree[currentIndex * 2 + 1] != null) {
            currentIndex = currentIndex * 2 + 1;
        }

        T minElement = tree[currentIndex];
        removeElement(minElement); // Remove o menor elemento encontrado
        return minElement;
    }


    @Override
    public T removeMax() {
        if (isEmpty()) {
            throw new ElementNotFound("binary search tree");
        }

        int currentIndex = 0;

        // Percorre até o nó mais à direita
        while (currentIndex * 2 + 2 < tree.length && tree[currentIndex * 2 + 2] != null) {
            currentIndex = currentIndex * 2 + 2;
        }

        T maxElement = tree[currentIndex];
        removeElement(maxElement); // Remove o maior elemento encontrado
        return maxElement;
    }


    @Override
    public T findMin() {
        if (isEmpty()) {
            throw new ElementNotFound("binary search tree");
        }

        int currentIndex = 0;

        // Percorre até o nó mais à esquerda
        while (currentIndex * 2 + 1 < tree.length && tree[currentIndex * 2 + 1] != null) {
            currentIndex = currentIndex * 2 + 1;
        }

        return tree[currentIndex];
    }


    @Override
    public T findMax() {
        if (isEmpty()) {
            throw new ElementNotFound("binary search tree");
        }

        int currentIndex = 0;

        // Percorre até o nó mais à direita
        while (currentIndex * 2 + 2 < tree.length && tree[currentIndex * 2 + 2] != null) {
            currentIndex = currentIndex * 2 + 2;
        }

        return tree[currentIndex];
    }


}
