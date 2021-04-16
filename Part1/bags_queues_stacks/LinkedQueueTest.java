/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedQueueTest<Item> implements Iterable<Item> {

    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
    }

    private Node<Item> first;
    private Node<Item> last;
    private int n;

    public LinkedQueueTest() {
        first = null;
        last = null;
        n = 0;
    }

    public int size() {
        return n;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public void enqueue(Item item) {
        Node<Item> oldLast = last;
        last = new Node<>();
        last.item = item;
        last.next = null;
        if (isEmpty()) first = last;
        else oldLast.next = last;
        n++;
    }

    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        Item item = first.item;
        first = first.next;
        n--;
        if (isEmpty()) last = first;
        return item;
    }

    public Iterator<Item> iterator() {
        return new LinkedQueueIterator();
    }

    private class LinkedQueueIterator implements Iterator<Item> {
        private Node<Item> current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        LinkedQueueTest<Integer> queue = new LinkedQueueTest<>();
        System.out.println("--Test enqueue--");
        for (int i = 0; i < 16; i++) {
            System.out.println(i);
            queue.enqueue(i);
        }

        System.out.println("--Test iterator--");
        for (int item : queue) {
            System.out.println(item);
        }

        System.out.println("--Test dequeue--");
        while (!queue.isEmpty()) {
            System.out.println(queue.dequeue());
            System.out.println("size = " + queue.size());
        }
        System.out.println("isEmpty = " + queue.isEmpty());
    }
}
