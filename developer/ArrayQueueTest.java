/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayQueueTest<Item> implements Iterable<Item> {

    private static final int DEFAULT_CAPACITY = 8;
    private Item[] a;
    private int n;
    private int first;
    private int last;

    public ArrayQueueTest() {
        a = (Item[]) new Object[DEFAULT_CAPACITY];
        n = 0;
        first = 0;
        last = 0;
    }

    public int size() {
        return n;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public void enqueue(Item item) {
        if (n == a.length) resize(2 * a.length);
        a[last++] = item;
        if (last == a.length) last = 0; // wrap-around
        n++;
    }

    public Item dequeue() {
        Item temp = a[first];
        a[first] = null;
        first++;
        if (first == a.length) first = 0; // wrap-around
        n--;
        if (n > 0 && n < a.length / 4) resize(a.length / 2);
        return temp;
    }

    //新的array直接從 copy[0]開始塞
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            copy[i] = a[(first + i) % a.length];
        }
        first = 0;
        last = n;
        System.out.println("Resize " + a.length + " -> " + copy.length);
        a = copy;
    }

    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {

        private int i;

        public ArrayIterator() {
            i = 0;
        }

        public boolean hasNext() {
            return i < n;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return a[(first + i++) % a.length];
        }
    }

    public static void main(String[] args) {
        ArrayQueueTest<Integer> queue = new ArrayQueueTest<>();
        System.out.println("--Test enqueue--");
        for (int i = 0; i < 16; i++) {
            queue.enqueue(i);
        }

        System.out.println("--Test iterator--");
        for (int item : queue) {
            System.out.println(item);
        }

        System.out.println("--Test dequeue--");
        while (!queue.isEmpty()) {
            System.out.println(queue.dequeue());
        }
        System.out.println("isEmpty = " + queue.isEmpty());
    }
}
