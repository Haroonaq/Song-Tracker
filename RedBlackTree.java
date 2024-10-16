import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class RedBlackTree <T extends Comparable<T>> extends BSTRotation<T> {

    /**
    Inserting a node into a Red Black Tree
     */
    @Override
    public void insert(T value) {
        if (value == null) {
            throw new NullPointerException("No data to insert");
        }

        RBTNode<T> newNode = new RBTNode<>(value);
        newNode.isRed = true;


        //if the tree is empty set the newNode as the root
        if (root == null) {
            root = newNode;
        }
        else {
            //use the insertHelper method to insert the node
            insertHelper(newNode, root);
        }

        //if the new node is not the root, make sure the red property is satisfied
        if (newNode.getUp() != null) {
            ensureRedProperty(newNode);
        }

        //the root is black after all insertions
        if (this.root != null) {
            ((RBTNode<T>) this.root).isRed = false;

        }
    }

    /**
     * Checks if a new red node in the RedBlackTree causes a red property violation
     * by having a red parent. If this is not the case, the method terminates without
     * making any changes to the tree. If a red property violation is detected, then
     * the method repairs this violation and any additional red property violations
     * that are generated as a result of the applied repair operation.
     * @param newRedNode a newly inserted red node, or a node turned red by previous repair
     */
    protected void ensureRedProperty(RBTNode<T> newRedNode) {

        // Case 1 - newRedNode is inserted at the root, must change to black and exit
        RBTNode<T> root = (RBTNode<T>)(this.root);
        if(newRedNode.equals(root)) {
            newRedNode.isRed = false;
            return;
        }

        // Case 2 - if the parent is black exit
        RBTNode<T> parent = newRedNode.getUp();
        if(!parent.isRed){
            return;
        }

        //Case 3 - Red Aunt violations, need to swap colors parent, aunt, and grandparent
        RBTNode<T> grandparent = parent.getUp();

        // Aunt depends on what side the parent is on
        RBTNode<T> aunt;

        if (parent.equals(grandparent.getLeft())) {
            aunt = grandparent.getRight();
        } else {
            aunt = grandparent.getLeft();
        }

        if(aunt != null && aunt.isRed){
            aunt.isRed = false;
            parent.isRed = false;
            grandparent.isRed = true;

            ensureRedProperty(grandparent);

            return;
        }

        //Case 4 - Black Aunt or null violations

        // 0 for left, 1 for right
        int childSide = (newRedNode.equals(parent.getLeft())) ? 0 : 1;
        // 0 for left, 1 for right
        int parentSide = (parent.equals(grandparent.getLeft())) ? 0 : 1;

        if(childSide == parentSide) {
            // Black Line Case - rotate grandparent and parent, swap colors
            rotate(parent, grandparent);

            grandparent.isRed = true;
            parent.isRed = false;
        }
        else {
            // Black Zig Case - double rotation involving parent, child, and grandparent

            // rotate parent and child
            rotate(newRedNode, parent);
            // rotate grandparent and child
            rotate(newRedNode, grandparent);

            // fix the colors after rotation
            newRedNode.isRed = false;
            grandparent.isRed = true;
        }

    }

    /**
     * Checks basic functionality of methods to see if the root
     * would be black and the children should be red
     */
    @Test
    public void RedBlackTreeTest1(){
        RedBlackTree<Integer> RBTTester  = new RedBlackTree<>();
        //insert root value in the tree which has to be colored black
        RBTTester.insert(10);
        RBTNode<Integer> rootRBT = (RBTNode<Integer>) (RBTTester.root);

        Assertions.assertEquals("10(b)", rootRBT.toString());

        //insert child values in the tree which have to be colored red
        RBTTester.insert(5);
        RBTTester.insert(15);

        //Tree should be
        //                   10(b)
        //               5(r)    15(r)

        RBTNode<Integer> rightChild = rootRBT.getRight();
        RBTNode<Integer> leftChild = rootRBT.getLeft();

        Assertions.assertEquals("5(r)", leftChild.toString());
        Assertions.assertEquals("15(r)", rightChild.toString());

    }


    /**
     * Test containing a Q03.RBTInsert quiz example, question 2
     */
    @Test
    public void RedBlackTreeTest2(){
        RedBlackTree<String> RBTTester  = new RedBlackTree<>();

        //insert values in the tree
        RBTTester.insert("L");
        RBTTester.insert("F");
        RBTTester.insert("T");
        RBTTester.insert("B");
        RBTTester.insert("J");
        RBTTester.insert("N");
        RBTTester.insert("S");

        //Assign names to each node

        RBTNode<String> rootRBT = (RBTNode<String>) (RBTTester.root);
        RBTNode<String> rightParent = rootRBT.getRight();
        RBTNode<String> leftParent = rootRBT.getLeft();
        RBTNode<String> leftParentLeftChild = leftParent.getLeft();
        RBTNode<String> leftParentRightChild = leftParent.getRight();
        RBTNode<String> rightParentLeftChild = rightParent.getLeft();
        RBTNode<String> rightParentRightChild = rightParent.getRight();

        //The order should be
        //                    L(b)
        //               F(b)         S(b)
        //            B(r)  J(r)   N(r)  T(r)

        Assertions.assertEquals("L(b)", rootRBT.toString());
        Assertions.assertEquals("F(b)", leftParent.toString());
        Assertions.assertEquals("S(b)", rightParent.toString());
        Assertions.assertEquals("B(r)", leftParentLeftChild.toString());
        Assertions.assertEquals("J(r)", leftParentRightChild.toString());
        Assertions.assertEquals("N(r)", rightParentLeftChild.toString());
        Assertions.assertEquals("T(r)", rightParentRightChild.toString());


    }


    /**
     * Test to see what happens when there is a Red Aunt Violation
     */
    @Test
    public void RedBlackTreeTest3(){

        RedBlackTree<Integer> RBTTester  = new RedBlackTree<>();

        //insert values in the tree
        RBTTester.insert(10);
        RBTTester.insert(8);
        RBTTester.insert(12);
        RBTTester.insert(1);

        //Assign names to each node

        RBTNode<Integer> grandparent = (RBTNode<Integer>) (RBTTester.root);
        RBTNode<Integer> aunt = grandparent.getRight();
        RBTNode<Integer> parent = grandparent.getLeft();
        RBTNode<Integer> child = parent.getLeft();

        //Tree should be
        //                 10(b)
        //             8(b)    12(b)
        //          1(r)


        Assertions.assertEquals("10(b)", grandparent.toString());
        Assertions.assertEquals("8(b)", grandparent.getLeft().toString());
        Assertions.assertEquals("12(b)", grandparent.getRight().toString());
        Assertions.assertEquals("1(r)", parent.getLeft().toString());

    }

}


