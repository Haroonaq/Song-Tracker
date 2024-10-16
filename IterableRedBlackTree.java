import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * This class extends RedBlackTree into a tree that supports iterating over the values it
 * stores in sorted, ascending order.
 */
public class IterableRedBlackTree<T extends Comparable<T>>
        extends RedBlackTree<T> implements IterableSortedCollection<T> {

    // Stores the min value for the iterator
    private Comparable<T> iteratorMin = null;

    // Store the max value for the iterator
    private Comparable<T> iteratorMax = null;

    /**
     * Allows setting the start (minimum) value of the iterator. When this method is called,
     * every iterator created after it will use the minimum set by this method until this method
     * is called again to set a new minimum value.
     * @param min the minimum for iterators created for this tree, or null for no minimum
     */
    public void setIteratorMin(Comparable<T> min) {
        this.iteratorMin = min;
    }

    /**
     * Allows setting the stop (maximum) value of the iterator. When this method is called,
     * every iterator created after it will use the maximum set by this method until this method
     * is called again to set a new maximum value.
     * @param min the maximum for iterators created for this tree, or null for no maximum
     */
    public void setIteratorMax(Comparable<T> max) {
        this.iteratorMax = max;
    }

    /**
     * Returns an iterator over the values stored in this tree. The iterator uses the
     * start (minimum) value set by a previous call to setIteratorMin, and the stop (maximum)
     * value set by a previous call to setIteratorMax. If setIteratorMin has not been called
     * before, or if it was called with a null argument, the iterator uses no minimum value
     * and starts with the lowest value that exists in the tree. If setIteratorMax has not been
     * called before, or if it was called with a null argument, the iterator uses no maximum
     * value and finishes with the highest value that exists in the tree.
     */
    public Iterator<T> iterator() {
        return new RBTIterator<T>(root,iteratorMin,iteratorMax);
    }

    /**
     * Nested class for Iterator objects created for this tree and returned by the iterator method.
     * This iterator follows an in-order traversal of the tree and returns the values in sorted,
     * ascending order.
     */
    protected static class RBTIterator<R> implements Iterator<R> {

        // stores the start point (minimum) for the iterator
        Comparable<R> min = null;
        // stores the stop point (maximum) for the iterator
        Comparable<R> max = null;
        // stores the stack that keeps track of the inorder traversal
        Stack<BSTNode<R>> stack = null;

        /**
         * Constructor for a new iterator if the tree with root as its root node, and
         * min as the start (minimum) value (or null if no start value) and max as the
         * stop (maximum) value (or null if no stop value) of the new iterator.
         * @param root root node of the tree to traverse
         * @param min the minimum value that the iterator will return
         * @param max the maximum value that the iterator will return
         */
        public RBTIterator(BSTNode<R> root, Comparable<R> min, Comparable<R> max) {
            this.min = min;
            this.max = max;
            this.stack = new Stack<>();
            buildStackHelper(root);
        }

        /**
         * Helper method for initializing and updating the stack. This method both
         * - finds the next data value stored in the tree (or subtree) that is bigger
         *   than or equal to the specified start point (maximum), and
         * - builds up the stack of ancestor nodes that contain values larger than or
         *   equal to the start point so that those nodes can be visited in the future.
         * @param node the root node of the subtree to process
         */
        private void buildStackHelper(BSTNode<R> node) {
            //Base case - if the node is null return out of the method
            if(node == null){
                return;
            }

            //if the node has a smaller value than the min value
            // then recursively call right subtree
            if(min != null && min.compareTo(node.data) > 0) {
                buildStackHelper(node.getRight());
            }

            //otherwise push node onto the stack and recursively call the left subtree
            else {
                stack.push(node);
                buildStackHelper(node.getLeft());
            }
        }

        /**
         * Returns true if the iterator has another value to return, and false otherwise.
         */
        public boolean hasNext() {
            //if stack is empty there isnt a next value to return
           if(stack.isEmpty()){
               return false;
           }

            R nextValue = stack.peek().getData();

           //if the max value is smaller than the next value in the list return false
           if(max != null && max.compareTo(nextValue) < 0){
               return false;
           }

            return true;

        }

        /**
         * Returns the next value of the iterator.
         * @throws NoSuchElementException if the iterator has no more values to return
         */
        public R next() {

            //if there is no elements in the list then there in a NoSuchElementException error
            if(!hasNext()){
                throw new NoSuchElementException("There is no element left in the list");
            }

            BSTNode<R> currentNode = stack.pop();
            R currentValue = currentNode.getData();

            //if currentNode has a right child then push leftmost nodes onto the stack
            if (currentNode.getRight() != null) {
                buildStackHelper(currentNode.getRight());
            }


            return currentValue;
        }

    }

    //Test cases

    /**
     * Test to see if the integers are sorted in the right order with no duplicates
     * and a starting min value
     */
    @Test
    public void iteratorTest1(){
        IterableRedBlackTree<Integer> testRBT = new IterableRedBlackTree<>();
        //insert values in the tree
        testRBT.insert(30);
        testRBT.insert(10);
        testRBT.insert(25);
        testRBT.insert(15);
        testRBT.insert(20);
        testRBT.insert(5);

        //5 should not be in the list since it is less than 9
        testRBT.setIteratorMin(9);
        //sorts the list
        Iterator<Integer> test = testRBT.iterator();

        int[] expected = {10, 15, 20, 25, 30};
        int i = 0;

        //checks if the value in the expected array matches up correctly 
        while(test.hasNext()) {
            Assertions.assertEquals(expected[i], test.next());
            i++;
        }
    }

    /**
     * Test to see if strings with duplicates are sorted in the right order and if the
     * list stops at the max value
     */
    @Test
    public void iteratorTest2(){
        IterableRedBlackTree<String> testRBT = new IterableRedBlackTree<>();
        //insert values in the tree
        testRBT.insert("c");
        testRBT.insert("c");
        testRBT.insert("z");
        testRBT.insert("a");
        testRBT.insert("b");
        testRBT.insert("a");
        testRBT.insert("d");
        testRBT.insert("h");

        //"z" and "h" should not be in the list because they come after "g"
        //which is the max
        testRBT.setIteratorMax("g");
        //sorts the list
        Iterator<String> test2 = testRBT.iterator();

        String[] expected = {"a", "a", "b", "c", "c", "d"};
        int i = 0;

        //checks if the value in the expected array matches up correctly
        while(test2.hasNext()){
            Assertions.assertEquals(expected[i], test2.next());
            i++;
        }
    }

    /**
     * Test with duplicates and a min and max value in the list that prevents certain
     * elements from entering the list. Checks if it sorts eligible integers in the
     * right order
     */
    @Test
    public void iteratorTest3(){
        IterableRedBlackTree<Integer> testRBT = new IterableRedBlackTree<>();
        //insert values in the tree
        testRBT.insert(30);
        testRBT.insert(10);
        testRBT.insert(25);
        testRBT.insert(15);
        testRBT.insert(10);
        testRBT.insert(5);
        testRBT.insert(30);

        //5 and the duplicates of 30 should not be in the list
        testRBT.setIteratorMin(9);
        testRBT.setIteratorMax(26);
        //sorts the list
        Iterator<Integer> test3 = testRBT.iterator();

        int[] expected = {10, 10, 15, 25};
        int i = 0;

        //checks if the value in the expected array matches up correctly
        while(test3.hasNext()) {
            Assertions.assertEquals(expected[i], test3.next());
            i++;
        }
    }
}
