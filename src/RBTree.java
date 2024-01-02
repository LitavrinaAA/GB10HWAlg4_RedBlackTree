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
            leftChild = null;
            rightChild = null;
            color = Color.Red;
        }

        // так и не поняла как этот кусок встроить и куда :(((
        // в инсерте реализовала сравнение, сразу при вставке
//        public boolean contains(V value) {
//            Tree.Node node = root;
//            while (node != null) {
//                if (node.value.equals(value))
//                    return true;
//                if (node.value.compareTo(value) > 0)
//                    node = node.left;
//                else
//                    node = node.right;
//            }
//            return false;
//        }
        public boolean isLeftChild() {
            return this == parent.leftChild;
        }

        public void flipColor() {
            color = (color == Color.Red ? Color.Black : Color.Red);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + value +
                    ", color=" + color +
                    ", leftChild=" + leftChild +
                    ", rightChild=" + rightChild +
                    '}';
        }
    }

    public RBTree<V> insert(V data) {
        Node node = new Node(data); //создам ноду со значением
        root = insert(root, node);//начиная с корня найдем куда вставить
        recolorAndRotate(node); // все, ноду вставили и теперь ужас перекраска
        return this;
    }

    private Node insert(Node node, Node nodeToInsert) {
        if (node == null) { // если узел пустой, то заполнм его, если нет, то ...
            return nodeToInsert;
        }
        if (nodeToInsert.value.compareTo(node.value) < 0) { // то возьмем у этой ноды левого или правого ребенка
            node.leftChild = insert(node.leftChild, nodeToInsert); // и его так же отправим в insert, где так же проверим (рекурсия)
            node.leftChild.parent = node;//запишем левому ребенку эту ноду как родителя
        } else if (nodeToInsert.value.compareTo(node.value) > 0) {
            node.rightChild = insert(node.rightChild, nodeToInsert);
            node.rightChild.parent = node;
        }
        return node;
    }

    //"Дядя" узла играет важную роль при выполнении поворотов и перекраски узлов
    // для восстановления свойств красно-черного дерева после вставки узла.
    // Например, при выполнении операции вставки или удаления узла,
    // анализ "дяди" помогает определить, какие действия должны быть предприняты,
    // чтобы сохранить баланс и соблюсти свойства красно-черного дерева.
    private void recolorAndRotate(Node node) {
        Node parent = node.parent; //берем родителя этой ноды
        if (node != root && parent.color == Color.Red) { //если не корень и красный родитель
            Node grandParent = node.parent.parent; //дедушка
            Node uncle = parent.isLeftChild() ? //дядя ( Красные ноды могут быть только левым дочерним элементом)
                    grandParent.rightChild : grandParent.leftChild;//у дедушки узнаем кто дядя
            //1-ый случай: папа и дядя красные -> перекрашиваем
            if (uncle != null && uncle.color == Color.Red) { // и дядя красный? - перекрашиваем!
                handleRecoloring(parent, uncle, grandParent);//и мой родитель и дядя красный?! значит и дедушку перекрасим. Всех перекрасить.
            //2-ой папа красный, дядя нет -> поворот и перекрашиваем папу и деда
            } else if (parent.isLeftChild()) { // красный левый родитель  дядя не красный
                handleLeftSituations(node, parent, grandParent);//история с левыми
            } else if (!parent.isLeftChild()) { // красный правый родитель  дядя не красный
                handleRightSituations(node, parent, grandParent);//история с НЕ левыми
            }
        }
        root.color = Color.Black; // root - black
    }

    private void handleRightSituations(Node node, Node parent, Node grandParent) {
        if (node.isLeftChild()) {
            rotateRight(parent);//если нода левая, то повернем родителя вправо
        }
        parent.flipColor(); //просто смена цвета
        grandParent.flipColor(); // и у дедушки
        rotateLeft(grandParent);//деда на лево
        recolorAndRotate(node.isLeftChild() ? grandParent : parent);
    }

    private void handleLeftSituations(Node node, Node parent, Node grandParent) {
        if (!node.isLeftChild()) {
            rotateLeft(parent); //родителя на лево
        }
        parent.flipColor();
        grandParent.flipColor();//деда на право
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
        node.parent = leftNode; //левую ноду в родители
    }

    private void rotateLeft(Node node) {
        Node rightNode = node.rightChild;
        node.rightChild = rightNode.leftChild;
        if (node.rightChild != null) {
            node.rightChild.parent = node;
        }
        rightNode.leftChild = node;
        rightNode.parent = node.parent;
        updateChildrenOfParentNode(node, rightNode);//перепись родителя в дети ноды, которая теперь станет родителем
        node.parent = rightNode;//правую ноду в родители
    }

    private void updateChildrenOfParentNode(Node node, Node tempNode) {
        if (node.parent == null) {
            root = tempNode;
        } else if (node.isLeftChild()) {
            node.parent.leftChild = tempNode;
        } else {
            node.parent.rightChild = tempNode;
        }
    }
    public void print() {
        System.out.println(root);
    }


}
