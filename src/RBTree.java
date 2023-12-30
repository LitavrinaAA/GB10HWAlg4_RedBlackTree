import java.util.HashMap;

public class RBTree<V extends Comparable<V>> {
    private Node root;
    private class Node {
        private V value;
        private RBTree<V>.Node parent;
        private RBTree<V>.Node leftChild;
        private RBTree<V>.Node rightChild;
        private RBTree<V>.Node prev;    // needed to unlink next upon deletion
        private Color color; //

        public Node(V value) {
            this.value = value;

        }
        public boolean isLeftChild() {
            return this == parent.leftChild;
        }
        public void flipColor() {
           color = ( color == Color.Red?Color.Black:Color.Red);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + value +
                    ", leftChild=" + leftChild +
                    ", rightChild=" + rightChild +
                    ", prev=" + prev +
                    ", color=" + color +
                    '}';
        }
    }

    public RBTree<V> insert(V data) {
        Node node = new Node(data);
        root = insert(root, node);
        recolorAndRotate(node);
        return this;
    }
    private Node insert(Node node, Node nodeToInsert) {
        if (node == null) {
            return nodeToInsert;
        }
        if (nodeToInsert.value.compareTo(node.value) < 0) {
            node.leftChild = insert(node.leftChild, nodeToInsert);
            node.leftChild.parent = node;
        } else if (nodeToInsert.value.compareTo(node.value) > 0) {
            node.rightChild = insert(node.rightChild, nodeToInsert);
            node.rightChild.parent = node;
        }
        return node;
    }
    private void recolorAndRotate(Node node) {
        Node parent = node.parent;
        if (node != root && parent.color == Color.Red) {
            Node grandParent = node.parent.parent;
            Node uncle = parent.isLeftChild() ?
                    grandParent.rightChild : grandParent.leftChild;
            if (uncle != null && uncle.color == Color.Red) { // перекрасить
                handleRecoloring(parent, uncle, grandParent);
            } else if (parent.isLeftChild()) { // Left-Left  Left-Right
                handleLeftSituations(node, parent, grandParent);
            } else if (!parent.isLeftChild()) { // Right-Right  Right-Left
                handleRightSituations(node, parent, grandParent);
            }
        }
        root.color = Color.Black; // Color the root node black
    }

    private void handleRightSituations(Node node, Node parent, Node grandParent) {
        if (node.isLeftChild()) {
            rotateRight(parent);
        }
        parent.flipColor();
        grandParent.flipColor();
        rotateLeft(grandParent);
        recolorAndRotate(node.isLeftChild() ? grandParent : parent);
    }

    private void handleLeftSituations(Node node, Node parent, Node grandParent) {
        if (!node.isLeftChild()) {
            rotateLeft(parent);
        }
        parent.flipColor();
        grandParent.flipColor();
        rotateRight(grandParent);
        recolorAndRotate(node.isLeftChild() ? parent : grandParent);
    }

    private void handleRecoloring(Node parent, Node uncle, Node grandParent) {
        uncle.flipColor();
        parent.flipColor();
        grandParent.flipColor();
        recolorAndRotate(grandParent);
    }

    private void rotateRight(Node node) {
        Node leftNode = node.leftChild;
        node.leftChild = leftNode.rightChild;
        if (node.leftChild != null) {
            node.leftChild.parent = node;
        }
        leftNode.rightChild = node;
        leftNode.parent = node.parent;
        updateChildrenOfParentNode(node, leftNode);
        node.parent = leftNode;
    }

    private void rotateLeft(Node node) {
        Node rightNode = node.rightChild;
        node.rightChild = rightNode.leftChild;
        if (node.rightChild != null) {
            node.rightChild.parent = node;
        }
        rightNode.leftChild = node;
        rightNode.parent = node.parent;
        updateChildrenOfParentNode(node, rightNode);
        node.parent = rightNode;
    }

    private void updateChildrenOfParentNode(Node node, Node  tempNode) {
        if (node.parent == null) {
            root = tempNode;
        } else if (node.isLeftChild()) {
            node.parent.leftChild = tempNode;
        } else {
            node.parent.rightChild = tempNode;
        }
    }


    public void traverse() {
        traverseInOrder(root);
    }

    private void traverseInOrder(Node node) {
        if (node != null) {
            traverseInOrder(node.leftChild);
            System.out.println(node);
            traverseInOrder(node.rightChild);
        }
    }


}
