package io.github.lukaicheng.datastructures.chapter3;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Chapter 3.4 ArrayList类的实现
 * 
 * 与书本中的原版样例代码稍微有些出入，添加了部分注释，调整了部分代码
 * 
 */
public class MyArrayList<T> implements Iterable<T> {
    //默认数组容量
    private static final int DEFAULT_CAPICITY = 10;
    //实际元素存放数组
    private T[] elements;
    //内部实际的元素个数
    private int size = 0;

    public MyArrayList() {
        clear();
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void clear() {
        size = 0;
        ensureCapacity(DEFAULT_CAPICITY);
    }

    public void trimToSize() {
        ensureCapacity(size());
    }

    public boolean add(T t) {
        //原书中采取直接调用add(index,t)的重载方法，此处我修改成直接在数组末位添加元素的方式
        ensureCapacity(size() + 1);
        elements[size++] = t;
        return true;
    }

    public void add(int index, T t) {
        rangeCheckForAdd(index);
        if (elements.length == size()) {
            //容量饱和，扩容到2倍+1，+1是防止原先容量为0的情况
            ensureCapacity(size() * 2 + 1);
        }
        for (int i = size(); i > index; i--) {
            //将原先数组中从index位置开始的元素往后移动一个单位
            elements[i] = elements[i - 1];
        }
        elements[index] = t;
    }

    public T get(int index) {
        rangeCheck(index);
        return elements[index];
    }

    public T set(int index, T t) {
        rangeCheck(index);
        T oldElement = elements[index];
        elements[index] = t;
        return oldElement;
    }

    public T remove(int index) {
        rangeCheck(index);
        T removeElement = elements[index];
        for (int i = index; i < size() - 1; i++) {
            //将被删除元素之后的元素向前移动一个单位
            elements[i] = elements[i + 1];
        }
        size--;
        return removeElement;
    }

    public Iterator<T> iterator() {
        return new ArrayListIterator();
    }

    class ArrayListIterator implements Iterator<T> {

        private int current = 0;

        public boolean hasNext() {
            return current < size();
        }

        public T next() {
            if (current >= size()) {
                throw new NoSuchElementException();
            }
            return elements[current++];
        }

        public void remove() {
            //使用--current原因不仅仅是因为需要移出当前位置之前的那个元素，还考虑到由于元素被移除其后续元素都前进了一位
            MyArrayList.this.remove(--current);
        }

    }

    private void rangeCheck(int index) {
        if (index < 0 && index >= size()) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void rangeCheckForAdd(int index) {
        if (index < 0 && index > size()) {
            //Add相比Get、Set，允许在末尾插入
            throw new IndexOutOfBoundsException();
        }
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity(int newCapacity) {
        if (newCapacity < size()) {
            //如果新容量要求小于实际元素个数，则目前的容量满足需求
            return;
        }
        if (newCapacity == size() && elements.length == size()) {
            //如果新容量要求等于实际元素个数，并且实际元素已经占据数组的全部空间，则无需再进行收缩操作
            return;
        }
        T[] oldArray = elements;
        elements = (T[]) new Object[newCapacity];
        for (int i = 0; i < size(); i++) {
            elements[i] = oldArray[i];
        }
    }

}
