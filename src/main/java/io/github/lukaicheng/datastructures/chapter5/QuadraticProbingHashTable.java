package io.github.lukaicheng.datastructures.chapter5;

/**
 * 
 * Chapter 5.4 探测散列表 - 平方探测法示例
 * 
 * 开放定址法是除了分离链接法之外另外一种解决散列冲突的思路，常见的有线性探测法、平方探测法、双散列等。
 * 
 * @see https://users.cs.fiu.edu/~weiss/dsaajava2/code/QuadraticProbingHashTable.java
 */
public class QuadraticProbingHashTable<T> {

    private static final int DEFAULT_TABLE_SIZE = 11;

    private HashEntry<T>[] array;
    private int currentSize;

    public QuadraticProbingHashTable() {
        this(DEFAULT_TABLE_SIZE);
    }

    public QuadraticProbingHashTable(int size) {
        allocateArray(size);
        makeEmpty();
    }

    public void makeEmpty() {
        currentSize = 0;
        for (int i = 0; i < array.length; i++) {
            array[i] = null;
        }
    }

    public boolean contains(T t) {
        int pos = findPos(t);
        return isActive(pos);
    }

    public void insert(T t) {
        int currentPos = findPos(t);
        if (isActive(currentPos)) {
            //如果元素已经存在，则直接返回
            return;
        }
        array[currentPos] = new HashEntry<T>(t);
        if (++currentSize > array.length / 2) {
            //当前散列表装载超过一半则需要重新散列
            rehash();
        }
    }

    public void remove(T t) {
        int currentPos = findPos(t);
        if (isActive(currentPos)) {
            array[currentPos].isActive = false;//逻辑删除，当重新散列的时候会真正移除元素
        }
    }

    private static class HashEntry<T> {

        public T element;
        public boolean isActive;

        public HashEntry(T t) {
            this(t, true);
        }

        public HashEntry(T t, boolean active) {
            this.element = t;
            this.isActive = active;
        }
    }

    @SuppressWarnings("unchecked")
    private void allocateArray(int arraySize) {
        array = new HashEntry[arraySize];
    }

    private boolean isActive(int currentPos) {
        return array[currentPos] != null && array[currentPos].isActive;
    }

    /**
     * 由平方探测法的基本定义f(i) = i^2 可进行如下推导:
     * f(i) = (i-1 + 1)^2 = (i-2)^2 + 2*(i-1)*1 + 1^2 = f(i-1) + 2i-1 
     * 可见下一个探测单元距离当前探测单元为2i-1，这个步长随着探测次数增加增幅为2
     * 
     * 本方法实现了此平方探测法
     */
    private int findPos(T t) {
        int offset = 1;
        int currentPos = myhash(t);
        while (array[currentPos] != null && !array[currentPos].element.equals(t)) {
            currentPos += offset;
            offset += 2;
            if (currentPos >= array.length) {
                currentPos -= array.length;
            }
        }
        return currentPos;
    }

    private void rehash() {
        HashEntry<T>[] oldArray = array;
        allocateArray(nextPrime(2 * oldArray.length));
        currentSize = 0;
        for (int i = 0; i < oldArray.length; i++) {
            if (oldArray[i] != null && oldArray[i].isActive) { //在rehash阶段会把原先逻辑删除的元素真正移除
                insert(oldArray[i].element);
            }
        }
    }

    private int myhash(T t) {
        int hashVal = t.hashCode();
        hashVal %= array.length;
        if (hashVal < 0) {
            hashVal += array.length;
        }
        return hashVal;
    }

    /**
     * Internal method to find a prime number at least as large as n.
     * @param n the starting number (must be positive).
     * @return a prime number larger than or equal to n.
     */
    private int nextPrime(int n) {
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
    private boolean isPrime(int n) {
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
