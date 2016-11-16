package io.github.lukaicheng.datastructures.chapter3;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Chapter 3.5 LinkedList类的实现
 * 
 * 与书本中的原版样例代码稍微有些出入，添加了部分注释，调整了部分代码
 * 
 */
public class MyLinkedList<T> implements Iterable<T> {
    /**
     * head和tail分别指向双向链表的头和尾，但实质上并不被包含在链表内，只是起到很好的指向作用，借助于它们可以更好的处理一些特殊情况，简化编码
     */
    //指向表第一个元素
    private Node<T> head;
    //指向表最后一个元素
    private Node<T> tail;
    //记录实际元素个数
    private int size;
    //当检测到表结构发生变化则进行递增
    private int modCount = 0;

    private static class Node<T> {

        public T data;
        public Node<T> prev;
        public Node<T> next;

        public Node(T data, Node<T> prev, Node<T> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    public MyLinkedList() {
        clear();
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void clear() {
        head = new Node<T>(null, null, null);
        tail = new Node<T>(null, head, null);
        head.next = tail;
        size = 0;
        modCount++;
    }

    public boolean add(T t) {
        Node<T> newNode = new Node<T>(t, tail.prev, tail);
        newNode.prev.next = newNode;
        tail.prev = newNode;
        size++;
        modCount++;
        //原书简洁方式add(size(),t);
        return true;
    }

    public void add(int index, T t) {
        Node<T> currentNode = getNode(index);
        addBefore(currentNode, t);
    }

    private void addBefore(Node<T> node, T t) {
        Node<T> newNode = new Node<T>(t, node.prev, node);
        //将原先该位置的前驱节点的后续节点指向新节点
        newNode.prev.next = newNode;
        //将原先该位置的前驱节点指向新节点
        node.prev = newNode;
        size++;
        modCount++;
    }

    public T get(int index) {
        Node<T> node = getNode(index);
        return node.data;
    }

    public T set(int index, T t) {
        Node<T> node = getNode(index);
        T oldValue = node.data;
        node.data = t;
        return oldValue;
    }

    public T remove(int index) {
        Node<T> node = getNode(index);
        return remove(node);
    }

    private T remove(Node<T> node) {
        node.next.prev = node.prev;
        node.prev.next = node.next;
        size--;
        modCount++;
        return node.data;
    }

    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    private Node<T> getNode(int index) {
        if (index < 0 || index > size()) {
            //由于getNode方法被get、set、remove、add等方法使用，因此此处对于index=size()情况在后续代码做了处理
            throw new IndexOutOfBoundsException();
        }
        Node<T> result;
        //借助两个端点标记进行查找
        if (index < size() / 2) {
            //索引位置更靠近起始端点
            result = head.next;
            for (int i = 0; i < index; i++) {
                result = result.next;
            }
        } else {
            //索引位置更靠近结束端点
            //因为在判定index合法时认定index=size()这个情况合法，此处赋值tail而不是tail.prev就是为了兼容这个情况
            result = tail;
            for (int i = size(); i > index; i--) {
                result = result.prev;
            }
        }
        return result;
    }

    class LinkedListIterator implements Iterator<T> {

        private Node<T> current = head.next;//初始化指向表头
        private int expectedModCount = modCount;//增加modCount机制判定是否在迭代器迭代期间，原始链表发生结构性改变
        private boolean okToRemove = false;//标识是否可以进行移除操作

        public boolean hasNext() {
            return current != tail;
        }

        public T next() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (current == tail) {
                throw new NoSuchElementException();
            }
            T result = current.data;
            current = current.next;
            okToRemove = true;
            return result;
        }

        public void remove() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (!okToRemove) {
                throw new IllegalStateException();
            }
            //和MyArrayList中不同的是，前驱节点的删除，不影响current节点(current不需要移动)
            MyLinkedList.this.remove(current.prev);
            okToRemove = false;
            expectedModCount++;
        }

    }

}
