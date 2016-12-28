package io.github.lukaicheng.datastructures.chapter4;

/**
 * Chapter 4.3 二叉查找树示例
 * 
 * 二叉树：首先它是一棵树，其次每个节点都不能有多于两个的儿子
 * 二叉查找树：在二叉树的基础上，它添加了一个限制条件，即对于树中每个节点X，其左子树中所有项的值小于X中的项，而有右子树中所有项的值大于X中的项
 * 
 */
public class BinarySearchTree<T extends Comparable<? super T>> {

    private static class BinaryNode<T> {

        BinaryNode(T t, BinaryNode<T> left, BinaryNode<T> right) {
            this.element = t;
            this.left = left;
            this.right = right;
        }

        private T element;
        private BinaryNode<T> left;
        private BinaryNode<T> right;

    }

    private BinaryNode<T> root;

    public BinarySearchTree() {
        root = null;
    }

    public void makeEmpty() {
        root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public boolean contains(T t) {
        return contains(t, root);
    }

    public T findMin() {
        if (isEmpty()) {
            return null;
        }
        return findMin(root).element;
    }

    public T findMax() {
        if (isEmpty()) {
            return null;
        }
        return findMax(root).element;
    }

    public void insert(T t) {
        root = insert(t, root);
    }

    public void remove(T t) {
        root = remove(t, root);
    }

    public void printTree() {
        if (isEmpty()) {
            System.out.println("Empty tree");
        } else {
            printTree(root);
        }
    }

    private boolean contains(T t, BinaryNode<T> node) {
        if (node == null) {
            return false;
        }
        int result = t.compareTo(node.element);
        if (result == 0) {
            return true;
        } else if (result < 0) {//小于当前根元素，则往左子树查找
            return contains(t, node.left);
        } else {//大于当前根元素，则往右子树查找
            return contains(t, node.right);
        }
    }

    private BinaryNode<T> findMin(BinaryNode<T> node) {
        if (node == null) {
            return null;
        }
        BinaryNode<T> left = node.left;
        if (left == null) {
            return node;
        } else {
            return findMin(left);
        }
    }

    private BinaryNode<T> findMax(BinaryNode<T> node) {
        if (node == null) {
            return null;
        }
        BinaryNode<T> right = node.right;
        if (right == null) {
            return node;
        } else {
            return findMax(right);
        }
    }

    /**
     * 内部元素插入方法，返回插入该元素之后子树的根节点 
     */
    private BinaryNode<T> insert(T t, BinaryNode<T> node) {
        if (node == null) {
            return new BinaryNode<T>(t, null, null);
        }
        int result = t.compareTo(node.element);
        if (result < 0) {
            node.left = insert(t, node.left);
        } else if (result > 0) {
            node.right = insert(t, node.right);
        }
        //元素匹配此子树根节点，无需重复插入，直接返回该根节点
        return node;
    }

    /**
     * 删除会遇到三种情况
     * 
     * 一、叶子节点：由于不存在左子树和右子树，因此可以直接被删除
     * 二、只存在左子树或者右子树：删除节点之后，需要调整节点子树的链
     * 三、同时存在左子树和右子树：用节点的右子树最小数据替换被删除节点并递归删除那个节点
     * 
     * @param t
     * @param node
     * @return
     */
    private BinaryNode<T> remove(T t, BinaryNode<T> node) {
        if (node == null) {
            return null;
        }
        int result = t.compareTo(t);
        if (result < 0) {
            node.left = remove(t, node.left);
        } else if (result > 0) {
            node.right = remove(t, node.right);
        }

        if (node.left == null && node.right == null) {
            //判定为叶子节点可以直接删除
            return null;
        } else if (node.left == null || node.right == null) {
            //只存在左子树或者右子树
            return (node.left != null) ? node.left : node.right;
        } else {
            //左子树和右子树都存在
            //1.首先取得被删除节点的右子树最小值节点，并以此替代被删除节点
            node.element = findMin(node.right).element;
            //2.然后删除右子树最小节点，因为右子树最小值节点不会拥有左儿子，所以这次递归remove会相对容易
            node.right = remove(node.element, node.right);
            return node;
        }
    }

    /**
     * 以中序遍历打印树
     */
    private void printTree(BinaryNode<T> node) {
        if (node != null) {
            printTree(node.left);
            System.out.println(node.element);
            printTree(node.right);
        }
    }
}
