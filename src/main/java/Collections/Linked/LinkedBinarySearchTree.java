package Collections.Linked;


import Collections.Interfaces.BinarySearchTreeADT;
import Exceptions.ElementNotFound;

public class LinkedBinarySearchTree<T> extends LinkedBinaryTree<T> implements BinarySearchTreeADT<T> {

    public LinkedBinarySearchTree() {
        super();
    }

    public LinkedBinarySearchTree(T element) {
        super(element);
    }

    @Override
    public void addElement(T element) {
        BinaryTreeNode<T> temp = new BinaryTreeNode<T> (element);
        Comparable<T> comparableElement = (Comparable<T>)element;
        if (isEmpty())
            root = temp;
        else {
            BinaryTreeNode<T> current = root;
            boolean added = false;
            while (!added) {
                if (comparableElement.compareTo(current.element) < 0) {
                    if (current.left == null)
                    {
                        current.left = temp;
                        added = true;
                    }
                    else
                        current = current.left;
                }
                else
                {
                    if (current.right == null)
                    {
                        current.right = temp;
                        added = true;
                    }
                    else
                        current = current.right;
                }
            }
        }
        count++;
    }

    @Override
    public T removeElement(T targetElement) throws ElementNotFound {
        T result = null;
        if (!isEmpty())
        {
            if (((Comparable)targetElement).equals(root.element))
            {
                result = root.element;
                root = replacement (root);
                count--;
            }
            else
            {
                BinaryTreeNode<T> current, parent = root;
                boolean found = false;
                if (((Comparable)targetElement).compareTo(root.element) < 0)
                    current = root.left;
                else
                    current = root.right;
                while (current != null && !found)
                {
                    if (targetElement.equals(current.element))
                    {
                        found = true;
                        count--;
                        result = current.element;
                        if (current == parent.left)
                        {
                            parent.left = replacement (current);
                        }
                        else
                        {
                            parent.right = replacement (current);
                        }
                    }
                    else
                    {
                        parent = current;
                        if (((Comparable)targetElement).compareTo(current.element) < 0)
                            current = current.left;
                        else
                            current = current.right;
                    }
                } //while
                if (!found)
                    throw new ElementNotFound("binary search tree");
            }
        } // end outer if
        return result;
    }

    protected BinaryTreeNode<T> replacement (BinaryTreeNode<T> node)
    {
        BinaryTreeNode<T> result = null;
        if ((node.left == null)&&(node.right==null))
            result = null;
        else if ((node.left != null)&&(node.right==null))
            result = node.left;
        else if ((node.left == null)&&(node.right != null))
            result = node.right;
        else
        {
            BinaryTreeNode<T> current = node.right;
            BinaryTreeNode<T> parent = node;
            while (current.left != null) {
                parent = current;
                current = current.left;
            }
            if (node.right == current)
                current.left = node.left;
            else
            {
                parent.left = current.right;
                current.right = node.right;
                current.left = node.left;
            }
            result = current;
        }
        return result;
    }

    @Override
    public void removeAllOccurrences(T targetElement) {
        if (isEmpty()) {
            throw new ElementNotFound("Empty tree");
        }
        try {
            while (true) {
                removeElement(targetElement);
            }
        }catch (ElementNotFound e) {}

    }


    @Override
    public T removeMin() {
        if (isEmpty()){
            throw new ElementNotFound("binary search tree");
        }
        BinaryTreeNode<T> current = root;
        BinaryTreeNode<T> parent = null;

        while (current.left != null) {
            parent = current;
            current = current.left;
        }

        T min  = current.element;

        if (parent == null){
            root = current.right;
        }
        else {
            parent.left = current.right;
        }
        count--;
        return min;
    }

    @Override
    public T removeMax() {
        if (isEmpty()){
            throw new ElementNotFound("binary search tree");
        }
        BinaryTreeNode<T> current = root;
        BinaryTreeNode<T> parent = null;

        while (current.right != null) {
            parent = current;
            current = current.right;
        }

        T max  = current.element;

        if (parent == null){
            root = current.left;
        }
        else {
            parent.right = current.left;
        }
        count--;
        return max;
    }

    @Override
    public T findMin() {
        if (isEmpty()){
            return null;
        }
        BinaryTreeNode<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.element;
    }

    @Override
    public T findMax() {
        if (isEmpty()){
            return null;
        }
        BinaryTreeNode<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.element;
    }
}
