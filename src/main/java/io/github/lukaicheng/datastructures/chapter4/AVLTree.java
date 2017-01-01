package io.github.lukaicheng.datastructures.chapter4;

/**
 * Chapter 4.4 AVL树示例
 * 
 * AVL树：每个节点的左子树和右子树的高度最多差1的二叉查找树(空树高度定义为-1)
 * 在高度为h的AVL树中，最少节点数S(h) = S(h-1) + S(h-2) + 1
 * 
 */
public class AVLTree<T extends Comparable<? super T>> {

    private AVLNode<T> root;

    public void insert(T t) {
        insert(t, root);
    }

    private static class AVLNode<T> {

        T element;
        AVLNode<T> left;
        AVLNode<T> right;
        int height;

        AVLNode(T t) {
            this(t, null, null);
        }

        AVLNode(T t, AVLNode<T> left, AVLNode<T> right) {
            this.element = t;
            this.left = left;
            this.right = right;
            this.height = 0;
        }
    }

    private AVLNode<T> insert(T x, AVLNode<T> tree) {
        if (tree == null) {
            return new AVLNode<T>(x);
        }
        int compareResult = x.compareTo(tree.element);
        if (compareResult < 0) {
            tree.left = insert(x, tree.left);
            if (height(tree.left) - height(tree.right) == 2) {
                if (x.compareTo(tree.left.element) < 0) {
                    //左左情况 - 单旋转
                    tree = rotateWithLeftChild(tree);
                } else {
                    //左右情况 - 双旋转
                    tree = doubleWithLeftChild(tree);
                }
            }
        } else if (compareResult > 0) {
            tree.right = insert(x, tree.right);
            if (height(tree.right) - height(tree.left) == 2) {
                if (x.compareTo(tree.right.element) > 0) {
                    //右右情况 - 单旋转
                    tree = rotateWithRightChild(tree);
                } else {
                    //右左情况 - 双旋转
                    tree = doubleWithRightChild(tree);
                }
            }
        } else {
            //已存在，无需在重复插入
        }
        tree.height = Math.max(height(tree.left), height(tree.right)) + 1;
        return tree;
    }

    /**
     * 左左情况-单旋转(右旋)
     */
    private AVLNode<T> rotateWithLeftChild(AVLNode<T> tree) {
        AVLNode<T> pivot = tree.left;
        tree.left = pivot.right;
        pivot.right = tree;
        tree.height = Math.max(height(tree.left), height(tree.right)) + 1;
        pivot.height = Math.max(height(pivot.left), tree.height) + 1;
        return pivot;
    }

    /**
     * 右右情况-单旋转(左旋)
     */
    private AVLNode<T> rotateWithRightChild(AVLNode<T> tree) {
        AVLNode<T> pivot = tree.right;
        tree.right = pivot.left;
        pivot.left = tree;
        tree.height = Math.max(height(tree.left), height(tree.right)) + 1;
        pivot.height = Math.max(tree.height, height(pivot.right)) + 1;
        return pivot;
    }

    /**
     * 左右情况-双旋转
     */
    private AVLNode<T> doubleWithLeftChild(AVLNode<T> tree) {
        //以左子树为根先进行左转
        tree.left = rotateWithRightChild(tree.left);
        //再进行右旋
        return rotateWithLeftChild(tree);
    }

    /**
     * 右左情况-双旋转
     */
    private AVLNode<T> doubleWithRightChild(AVLNode<T> tree) {
        //以右子树为根先进行右旋
        tree.right = rotateWithLeftChild(tree.right);
        //再进行左旋
        return rotateWithRightChild(tree);
    }

    /**
     * 返回树的高，如果是空树，则返回-1 
     */
    private int height(AVLNode<T> tree) {
        return tree == null ? -1 : tree.height;
    }
}
