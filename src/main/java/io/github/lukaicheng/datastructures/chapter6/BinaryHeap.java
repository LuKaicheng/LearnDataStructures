package io.github.lukaicheng.datastructures.chapter6;

/**
 * Chapter 6.3 二叉堆的示例
 * 
 * 二叉堆是一颗被完全填满的二叉树
 * 堆序性质：在一个堆中，对于每一个节点X，X的父亲中的关键字小于(或等于)X中的关键字，根节点除外(它没有父亲)
 * 
 * @author Lucifer
 *
 */
public class BinaryHeap<T extends Comparable<? super T>> {

    private static final int DEFAULT_CAPACITY = 10;

    private T[] array; //The heap of array
    private int currentSize; //Number of elements in heap

    public BinaryHeap() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public BinaryHeap(int capacity) {
        currentSize = 0;
        array = (T[]) new Comparable[capacity + 1];
    }

    @SuppressWarnings("unchecked")
    public BinaryHeap(T[] items) {
        currentSize = items.length;
        array = (T[]) new Comparable[(currentSize + 2) * 11 / 10];

        int i = 1;
        for (T item : items) {
            array[i++] = item;
        }
        buildHeap();
    }

    /**
     * Insert into the priority queue,maintaining the heap order.
     * Duplicates are allowed.
     * 
     * @param x the item to insert
     */
    public void insert(T x) {
        if (currentSize == array.length - 1) {
            enlargeArray(currentSize * 2 + 1);
        }
        int hole = currentSize++;
        //Percolate up
        while (hole > 1 && x.compareTo(array[hole / 2]) < 0) {
            array[hole] = array[hole / 2];
            hole /= 2;
        }
        array[hole] = x;
    }

    public T deleteMin() {
        if (isEmpty()) {
            return null;
        }
        T minItem = findMin();
        array[1] = array[currentSize--];
        percolateDown(1);
        return minItem;
    }

    public T findMin() {
        if (isEmpty()) {
            throw null;
        } else {
            return array[1];
        }
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    @SuppressWarnings("unchecked")
    public void makeEmpty() {
        currentSize = 0;
        array = (T[]) new Comparable[DEFAULT_CAPACITY + 1];
    }

    /**
     * 将位置hole中的元素下滤
     * 
     * @param hole
     */
    private void percolateDown(int hole) {
        int child;
        T tmp = array[hole];
        while (hole * 2 <= currentSize) {
            child = hole * 2;
            if (child != currentSize && array[child + 1].compareTo(array[child]) < 0) {
                child++;
            }
            if (array[child].compareTo(tmp) < 0) {
                array[hole] = array[child];
                hole = child;
            } else {
                break;
            }
        }
        array[hole] = tmp;
    }

    /**
     * 将任意顺序的数据构建成二叉堆
     */
    private void buildHeap() {
        for (int i = currentSize / 2; i > 0; i--) {
            //对至少所有非叶节点进行一次下滤，可以将原先任意顺序的数组变成堆
            percolateDown(i);
        }

    }

    @SuppressWarnings("unchecked")
    private void enlargeArray(int newSize) {
        if (newSize <= array.length) {
            return;
        }
        T[] old = array;
        array = (T[]) new Comparable[newSize];
        for (int i = 0; i < old.length; i++) {
            array[i] = old[i];
        }
    }
}
