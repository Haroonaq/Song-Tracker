/**
 * This class contains various of methods that are needed to construct
 * and change a binary search tree
 * @param <T> - any data type can be used
 */
public class BinarySearchTree <T extends Comparable<T>> implements SortedCollection <T> {

    /**
     * The top value of the BST (root)
     */
    protected BSTNode<T> root;

    /**
     * Performs the naive binary search tree insert algorithm to recursively
     * insert the provided newNode (which has already been initialized with a
     * data value) into the provided tree/subtree.  When the provided subtree
     * is null, this method does nothing.
     */
    protected void insertHelper(BSTNode<T> newNode, BSTNode<T> subtree) {

        //if the subtree is null then the method does nothing

        if (subtree == null) {
            return;
        }


        //if the new node is bigger or equal than subtree insert to the left
        if (newNode.getData().compareTo(subtree.getData()) >= 0) {

            if (subtree.getRight() == null) {
                subtree.setRight(newNode);
                newNode.setUp(subtree);
            } else {
                insertHelper(newNode, subtree.getRight());
            }

        }

        // if the new node is smaller than the subtree insert to the right

        if (newNode.getData().compareTo(subtree.getData()) < 0) {

            if (subtree.getLeft() == null) {
                subtree.setLeft(newNode);
                newNode.setUp(subtree);
            } else {
                insertHelper(newNode, subtree.getLeft());
            }

        }
    }

    /**
     * Inserts a new data value into the sorted collection.
     *
     * @param data the new value being insterted
     * @throws NullPointerException if data argument is null, we do not allow
     *                              null values to be stored within a SortedCollection
     */

    @Override
    public void insert(T data) throws NullPointerException {

        if (data == null) {
            throw new NullPointerException("There is no data to insert");
        }

        BSTNode<T> newNode = new BSTNode<T>(data);

        // insert data at root if root is null otherwise call the helper method to find
        // a null value where data can be inserted

        if (root == null) {
            root = newNode;
        } else {
            insertHelper(newNode, root);
        }

    }

    /**
     * If the data value is in the BST then this method will be true, otherwise
     * it will be false
     *
     * @param data        - the data you want to see is in the BST
     * @param currentNode - the node used to traverse through the BST
     * @return - true if the method contains the data value you want to check
     * and false if it doesn't
     */

    public boolean containsHelper(Comparable<T> data, BSTNode<T> currentNode) {

        //base case
        if (currentNode == null) {
            return false;
        }

        int comparison = data.compareTo(currentNode.getData());

        if (comparison == 0) {
            return true;
        } else if (comparison > 0) {
            return containsHelper(data, currentNode.getRight());
        } else {
            return containsHelper(data, currentNode.getLeft());
        }
    }


    /**
     * Check whether data is stored in the tree.
     *
     * @param data the value to check for in the collection
     * @return true if the collection contains data one or more times,
     * and false otherwise
     */
    @Override
    public boolean contains(Comparable<T> data) {

        if (data == null) {
            return false;
        }

        return containsHelper(data, root);

    }

    /**
     * Checks how big the BST is
     *
     * @param currentNode - used to traverse through the BST
     * @return the size of the BST
     */
    private int sizeHelper(BSTNode<T> currentNode) {
        if (currentNode == null) {
            return 0;
        }
        //add one for every node in the BST to the size
        return 1 + sizeHelper(currentNode.getLeft()) + sizeHelper(currentNode.getRight());

    }

    /**
     * Counts the number of values in the collection, with each duplicate value
     * being counted separately within the value returned.
     *
     * @return the number of values in the collection, including duplicates
     */
    @Override
    public int size() {

        return sizeHelper(root);

    }

    /**
     * Checks if the collection is empty.
     *
     * @return true if the collection contains 0 values, false otherwise
     */
    @Override
    public boolean isEmpty() {
        if (root == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes all values and duplicates from the collection.
     */
    @Override
    public void clear() {
        root = null;
    }
}
