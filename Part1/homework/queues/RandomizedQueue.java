/* *****************************************************************************
 *  Name: 647
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private static final int DEFAULT_CAPACITY = 8;
    private Item[] q;
    private int n;

    // construct an empty randomized queue
    public RandomizedQueue() {
        q = (Item[]) new Object[DEFAULT_CAPACITY];
        n = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        validate(item);
        if (n == q.length) resize(2 * q.length);
        q[n++] = item;
    }

    private void validate(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Can't insert null Object!");
    }

    // remove and return a random item
    public Item dequeue() {
        check();
        int index = StdRandom.uniform(Integer.MAX_VALUE) % n;
        // swap q[index], q[n - 1] and return q[n - 1] and set q[n-1] = null
        Item temp = q[index];
        q[index] = q[n - 1];
        q[n - 1] = null;
        n--;
        if (n > 0 && n <= q.length / 4) resize(q.length / 2);
        return temp;
    }

    private void resize(int capacity) {
        // System.out.println("capacity = " + capacity);
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            copy[i] = q[i];
        }
        q = copy;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        check();
        int index = StdRandom.uniform(Integer.MAX_VALUE) % n;
        return q[index];
    }

    private void check() {
        if (isEmpty())
            throw new NoSuchElementException("randomized queue is empty!");
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private Item[] seq;
        private int i;

        public RandomizedQueueIterator() {
            seq = (Item[]) new Object[n];
            for (int j = 0; j < n; j++) {
                seq[j] = q[j];
            }
            StdRandom.shuffle(seq);
        }

        public boolean hasNext() {
            return i < n;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = seq[i];
            i++;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        System.out.println("----------Test enqueue()--------------");
        for (int i = 0; i < 12; i++) {
            queue.enqueue(i);
        }

        System.out.println("----------Test smaple()--------------");
        for (int i = 0; i < 20; i++) {
            System.out.print(queue.sample() + ",");
        }
        System.out.println();

        System.out.println("----------Test iterator()--------------");
        for (int i = 0; i < 5; i++) {
            for (int item : queue) {
                System.out.print(item + ",");
            }
            System.out.println();
        }

        System.out.println("----------Test dequeue()--------------");
        while (!queue.isEmpty()) {
            System.out.println(queue.dequeue());
        }

        System.out.println("==================================================================");
        System.out.println("Test Exception");
        // queue.enqueue(null);
        // queue.sample();
        // queue.dequeue();
    }
}
