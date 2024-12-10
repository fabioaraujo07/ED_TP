//package Collections.Array;
//
//import Interfaces.BinarySearchTreeADT;
//
//import java.util.Iterator;
//
//public class TestArrayBinaryTree {
//    public static void main(String[] args) {
//        BinarySearchTreeADT<Integer> tree = new ArrayBinarySearchTree<>();
//
//        tree.addElement(10);
//        tree.addElement(5);
//        tree.addElement(15);
//        tree.addElement(3);
//        tree.addElement(7);
//        tree.addElement(13);
//        tree.addElement(16);



//        System.out.println("InOrder:");
//        Iterator<String> inOrderIterator = tree.iteratorInOrder();
//        while (inOrderIterator.hasNext()) {
//            System.out.print(inOrderIterator.next() + " ");
//        }
//        System.out.println();
//
//        System.out.println("PreOrder:");
//        Iterator<String> preOrderIterator = tree.iteratorPreOrder();
//        while (preOrderIterator.hasNext()) {
//            System.out.print(preOrderIterator.next() + " ");
//        }
//        System.out.println();
//
//        System.out.println("PostOrder:");
//        Iterator<String> postOrderIterator = tree.iteratorPostOrder();
//        while (postOrderIterator.hasNext()) {
//            System.out.print(postOrderIterator.next() + " ");
//        }
//        System.out.println();

//        System.out.println("LevelOrder:");
//        Iterator<Integer> levelOrderIterator = tree.iteratorLevelOrder();
//        while (levelOrderIterator.hasNext()) {
//            System.out.print(levelOrderIterator.next() + " ");
//        }
//        System.out.println();
//
//        tree.removeElement(10);
//
//        System.out.println("LevelOrder:");
//        Iterator<Integer> levelOrderIterator2 = tree.iteratorLevelOrder();
//        while (levelOrderIterator2.hasNext()) {
//            System.out.print(levelOrderIterator2.next() + " ");
//        }
//        System.out.println();
//
//    }
//}
