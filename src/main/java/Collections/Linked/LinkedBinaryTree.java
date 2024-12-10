package Collections.Linked;





import Collections.Array.UnorderedArrayList;
import Collections.Interfaces.BinaryTreeADT;
import Exceptions.ElementNotFound;

import java.util.Iterator;

public class LinkedBinaryTree<T> implements BinaryTreeADT<T> {

    protected int count;
    protected BinaryTreeNode<T> root;

    public LinkedBinaryTree() {
        root = null;
        count = 0;
    }

    public LinkedBinaryTree(T element) {
        root = new BinaryTreeNode<>(element);
        count = 1;
    }

    @Override
    public T getRoot() {
        return root.element;
    }

    public void setRoot(BinaryTreeNode<T> root) {
        this.root = root;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
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
        BinaryTreeNode<T> current = findAgain(targetElement, root);

        if (current == null) {
            throw new ElementNotFound("Element not found");
        }
        return (current.element);
    }

    private BinaryTreeNode<T> findAgain(T targetElement, BinaryTreeNode<T> next) {
        if (next == null) {
            return null;
        }
        if (next.element.equals(targetElement)) {
            return next;
        }

        BinaryTreeNode<T> temp = findAgain(targetElement, next.left);

        if(temp == null){
            temp = findAgain(targetElement, next.right);
        }
        return temp;
    }

    @Override
    public Iterator<T> iteratorInOrder() {
        UnorderedArrayList<T> temp = new UnorderedArrayList<>();
        inorder(root, temp);
        return temp.iterator();
    }

    protected void inorder (BinaryTreeNode<T> node, UnorderedArrayList<T> temp) {
        if (node != null) {
            inorder(node.left, temp);
            temp.addToRear(node.element);
            inorder(node.right, temp);
        }
    }

    @Override
    public Iterator<T> iteratorPreOrder() {
        UnorderedArrayList<T> temp = new UnorderedArrayList<>();
        preorder(root, temp);
        return temp.iterator();
    }

    protected void preorder (BinaryTreeNode<T> node, UnorderedArrayList<T> temp) {
        if (node != null) {
            temp.addToRear(node.element);
            preorder(node.left, temp);
            preorder(node.right, temp);
        }
    }

    @Override
    public Iterator<T> iteratorPostOrder() {
        UnorderedArrayList<T> temp = new UnorderedArrayList<>();
        postorder(root, temp);
        return temp.iterator();
    }

    protected void postorder (BinaryTreeNode<T> node, UnorderedArrayList<T> temp) {
        if (node != null) {
            postorder(node.left, temp);
            postorder(node.right, temp);
            temp.addToRear(node.element);
        }
    }

    @Override
    public Iterator<T> iteratorLevelOrder() {
        UnorderedArrayList<T> temp = new UnorderedArrayList<>();
        levelorder(root, temp);
        return temp.iterator();
    }

    protected void levelorder(BinaryTreeNode<T> node, UnorderedArrayList<T> temp){
        LinkedQueue<BinaryTreeNode<T>> nodes = new LinkedQueue<>();

        if (node != null) {
            nodes.enqueue(node);
            while (!nodes.isEmpty()){
                BinaryTreeNode<T> element = nodes.dequeue();
                temp.addToRear(element.element);
                if (element.left != null) {
                    nodes.enqueue(element.left);
                }
                if (element.right != null) {
                    nodes.enqueue(element.right);
                }
            }
        }
    }

}
