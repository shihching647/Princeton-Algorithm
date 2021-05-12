/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.NoSuchElementException;

public class MaxPQTest<K> {
    private K[] pq;
    private int n;

    public MaxPQTest(int capacity) {
        pq = (K[]) new Object[capacity + 1];
        n = 0;
    }

    public MaxPQTest() {
        this(1);
    }

    public MaxPQTest(K[] keys) {
        n = keys.length;
        pq = (K[]) new Object[n + 1];
        for (int i = 0; i < n; i++) {
            pq[i + 1] = keys[i];
        }
        for (int k = n / 2; k >= 1; k--) {
            sink(k);
        }
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void insert(K key) {
        if (size() == pq.length - 1)
            resize(2 * pq.length);
        pq[++n] = key;
        swin(n);
        assert (isMaxHeap());
    }

    private void swin(int k) {
        while (k > 1 && less(k / 2, k)) {
            exch(k / 2, k);
            k = k / 2;
        }
    }

    public K max() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return pq[1];
    }

    public K delMax() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        K temp = pq[1];
        exch(1, n);
        pq[n--] = null;
        sink(1);
        if ((n > 0) && (n == (pq.length - 1) / 4)) resize(pq.length / 2);
        assert (isMaxHeap());
        return temp;
    }

    private void sink(int k) {
        while (2 * k <= n) {
            int j = 2 * k;
            if (j < n && less(j, j + 1)) j = j + 1; // choose the bigger one child
            if (!less(k, j)) break; // if the parent is not less than bigger child , break
            exch(j, k);
            k = j;
        }
    }

    private boolean less(int i, int j) {
        return ((Comparable<K>) pq[i]).compareTo(pq[j]) < 0;
    }

    private void exch(int i, int j) {
        K temp = pq[i];
        pq[i] = pq[j];
        pq[j] = temp;
    }

    private void resize(int capacity) {
        K[] temp = (K[]) new Object[capacity];
        for (int i = 1; i <= n; i++) {
            temp[i] = pq[i];
        }
        pq = temp;
    }

    private boolean isMaxHeap() {
        // check pq[0] is null
        if (pq[0] != null) return false;

        // check pq[1] ~ pq[n] is not null
        for (int i = 1; i <= n; i++) {
            if (pq[i] == null) return false;
        }

        // check pq[n+1] ~ pq[pq.length - 1] is null
        for (int i = n + 1; i < pq.length; i++) {
            if (pq[i] != null) return false;
        }
        return isMaxHeapOrdered(1);
    }

    private boolean isMaxHeapOrdered(int k) {
        if (k > n) return true; // terminate condition
        int left = 2 * k;
        int right = left + 1;
        if (left <= n && less(k, left)) return false; // if left > k => no left child
        if (right <= n && less(k, right)) return false; // if right > k => no right child
        return isMaxHeapOrdered(left) && isMaxHeapOrdered(right);
    }

    public static void main(String[] args) {
        Integer[] arr = new Integer[10];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = StdRandom.uniform(100);
        }
        MaxPQTest<Integer> pq = new MaxPQTest<Integer>(arr);
        System.out.println("Max = " + pq.max());
        while (!pq.isEmpty()) {
            System.out.print(pq.delMax() + " ");
        }
    }
}
