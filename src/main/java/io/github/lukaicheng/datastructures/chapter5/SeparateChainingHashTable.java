package io.github.lukaicheng.datastructures.chapter5;

import java.util.LinkedList;
import java.util.List;

/**
 * Chapter 5.3 分离链接法示例
 *
 * 分离链接法是解决冲突的一种方法，通常做法是将散列到同一个值的所有元素保留到一个表中
 * JDK中的HashMap正式基于此原理设计实现而成
 * 
 * load factor : 散列表元素个数与该表大小的比。本实现中其值相当于1
 */
public class SeparateChainingHashTable<T> {

    private static final int DEFAULT_TABLE_SIZE = 101;

    private List<T>[] theLists;
    private int currentSize;

    public SeparateChainingHashTable() {
        this(DEFAULT_TABLE_SIZE);
    }

    @SuppressWarnings("unchecked")
    public SeparateChainingHashTable(int size) {
        theLists = new LinkedList[nextPrime(size)];//使表的大小会素数能够保证一个好的分布
        for (int i = 0; i < theLists.length; i++) {
            theLists[i] = new LinkedList<T>();
        }
    }

    public void insert(T t) {
        List<T> whichList = theLists[myhash(t)];
        if (!whichList.contains(t)) { //只有当元素不存在时，才会进行添加
            whichList.add(t);
            if (++currentSize > theLists.length) {
                //当load factor > 1时需要扩大散列表
                rehash();
            }
        }
    }

    public void remove(T t) {
        List<T> whichList = theLists[myhash(t)];
        if (whichList.contains(t)) {
            whichList.remove(t);
            currentSize--;
        }
    }

    public boolean contains(T t) {
        List<T> whichList = theLists[myhash(t)];
        return whichList.contains(t);
    }

    public void makeEmpty() {
        for (int i = 0; i < theLists.length; i++) {
            theLists[i].clear();
        }
        currentSize = 0;
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        List<T>[] oldLists = theLists;
        theLists = new List[nextPrime(2 * theLists.length)];
        for (int i = 0; i < theLists.length; i++) {
            theLists[i] = new LinkedList<T>();
        }
        //将容量重置，并把原有元素逐个插入到新散列中
        currentSize = 0;
        for (int i = 0; i < oldLists.length; i++) {
            for (T item : oldLists[i]) {
                insert(item);
            }
        }
    }

    private int myhash(T t) {
        int hashVal = t.hashCode();
        hashVal %= theLists.length;
        if (hashVal < 0) {
            hashVal += theLists.length;
        }
        return hashVal;
    }

    /**
     * Internal method to find a prime number at least as large as n.
     * @param n the starting number (must be positive).
     * @return a prime number larger than or equal to n.
     */
    private static int nextPrime(int n) {
        if (n % 2 == 0) {
            n++;
        }
        while (!isPrime(n)) {
            n += 2;
        }
        return n;
    }

    /**
     * Internal method to test if a number is prime.
     * Not an efficient algorithm.
     * @param n the number to test.
     * @return the result of the test.
     */
    private static boolean isPrime(int n) {
        if (n == 2 || n == 3) {
            return true;
        }
        if (n == 1 || n % 2 == 0) {
            return false;
        }
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

}
