public class BSTRotation<T extends Comparable<T>> extends BinarySearchTree<T> {
    /**
     * Performs the rotation operation on the provided nodes within this tree.
     * When the provided child is a left child of the provided parent, this
     * method will perform a right rotation. When the provided child is a right
     * child of the provided parent, this method will perform a left rotation.
     * When the provided nodes are not related in one of these ways, this
     * method will either throw a NullPointerException: when either reference is
     * null, or otherwise will throw an IllegalArgumentException.
     *
     * @param child is the node being rotated from child to parent position
     * @param parent is the node being rotated from parent to child position
     * @throws NullPointerException when either passed argument is null
     * @throws IllegalArgumentException when the provided child and parent
     *     nodes are not initially (pre-rotation) related that way
     */
    protected void rotate(BSTNode<T> child, BSTNode<T> parent)
            throws NullPointerException, IllegalArgumentException {

        if(child == null || parent == null) {
            throw new NullPointerException("Child or Parent is null");
        }

        //LEFT ROTATION
        if(parent.getRight() == child) {

            //the child's left child becomes the parent's right child
            parent.setRight(child.getLeft());

            if(child.getLeft() != null) {
                child.getLeft().setUp(parent);
            }

            //The child becomes the new parent as the parent in the subtree
            child.setLeft(parent);

            BSTNode<T> grandParentNode = parent.getUp();

            //Update the grandparent node if there is one
            if(grandParentNode != null) {
                if(grandParentNode.getLeft() == parent) {
                    grandParentNode.setLeft(child);
                } else {
                    grandParentNode.setRight(child);
                }
            }

            //Updating the root
            if(parent == root) {
                root = child;
            }

            //adjust the child and parent references to their parents
            child.setUp(grandParentNode);
            parent.setUp(child);


        }
        //RIGHT ROTATION
        else if(parent.getLeft() == child) {

            //the child's right child becomes the parent's left child
            parent.setLeft(child.getRight());

            if(child.getRight() != null) {
                child.getRight().setUp(parent);
            }

            //The child becomes the new parent as the parent in the subtree
            child.setRight(parent);

            BSTNode<T> grandParentNode = parent.getUp();

            ////Update the grandparent node if there is one
            if(grandParentNode != null) {
                if(grandParentNode.getRight() == parent) {
                    grandParentNode.setRight(child);
                } else {
                    grandParentNode.setLeft(child);
                }
            }

            //update the root
            if (parent == root) {
                root = child;
            }

            //adjust the child and parent reference to their parents
            child.setUp(grandParentNode);
            parent.setUp(child);

        } else {
            throw new IllegalArgumentException("Nodes are not child or parent and " +
                    "can't be rotated");
        }
    }
}
