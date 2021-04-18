/* *****************************************************************************
 *  Name: 647
 *  Date:
 *  Description: Deque implemented with doubly-linked list.
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    // helper doubly-linked list class
    private static class DNode<Item> {
        private DNode<Item> prev;
        private DNode<Item> next;
        private Item item;

        public DNode(Item item) {
            this.item = item;
            prev = null;
            next = null;
        }
    }

    private DNode<Item> first;
    private DNode<Item> last;
    private int n;

    // construct an empty deque
    public Deque() {
        first = new DNode<>(null);
        last = new DNode<>(null);
        first.next = last;
        last.prev = first;
        n = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        validate(item);
        DNode<Item> newNode = new DNode<>(item);
        DNode<Item> oldFirst = first.next;
        newNode.prev = first;
        first.next = newNode;
        newNode.next = oldFirst;
        oldFirst.prev = newNode;
        n++;
    }

    // add the item to the back
    public void addLast(Item item) {
        validate(item);
        DNode<Item> newNode = new DNode<>(item);
        DNode<Item> oldLast = last.prev;
        newNode.next = last;
        last.prev = newNode;
        newNode.prev = oldLast;
        oldLast.next = newNode;
        n++;
    }

    private void validate(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Can't insert null Object!");
    }

    // remove and return the item from the front
    public Item removeFirst() {
        check();
        DNode<Item> firstNode = first.next;
        Item item = firstNode.item;
        DNode<Item> secondFirst = firstNode.next;
        first.next = secondFirst;
        secondFirst.prev = first;
        firstNode.next = null;
        firstNode.prev = null;
        n--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        check();
        DNode<Item> lastNode = last.prev;
        Item item = lastNode.item;
        DNode<Item> secondLast = lastNode.prev;
        last.prev = secondLast;
        secondLast.next = last;
        lastNode.prev = null;
        lastNode.next = null;
        n--;
        return item;
    }

    private void check() {
        if (isEmpty())
            throw new NoSuchElementException("deque is empty!");
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private DNode<Item> current = first.next;

        public boolean hasNext() {
            return current != last;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();

        System.out.println("--Test addFirst()--");
        for (int i = 0; i < 10; i++) {
            deque.addFirst(i);
        }
        System.out.println("size = " + deque.size());
        System.out.println("isEmpty = " + deque.isEmpty());
        for (int item : deque) System.out.print(item + ", ");
        System.out.println("\n");

        System.out.println("--Test removeLast()--");
        for (int i = 0; i < 10; i++) {
            System.out.println(deque.removeLast());
        }
        System.out.println("size = " + deque.size());
        System.out.println("isEmpty = " + deque.isEmpty());

        System.out.println("==================================================================");

        System.out.println("--Test addLast()--");
        for (int i = 0; i < 10; i++) {
            deque.addLast(i);
        }
        System.out.println("size = " + deque.size());
        System.out.println("isEmpty = " + deque.isEmpty());
        for (int item : deque) System.out.print(item + ", ");
        System.out.println("\n");

        System.out.println("--Test removeFirst()--");
        for (int i = 0; i < 10; i++) {
            System.out.println(deque.removeFirst());
        }
        System.out.println("size = " + deque.size());
        System.out.println("isEmpty = " + deque.isEmpty());

        System.out.println("==================================================================");

        System.out.println("--Test addLast()--");
        for (int i = 0; i < 10; i++) {
            deque.addLast(i);
        }
        System.out.println("size = " + deque.size());
        System.out.println("isEmpty = " + deque.isEmpty());
        for (int item : deque) System.out.print(item + ", ");
        System.out.println("\n");

        System.out.println("--Test removeLast()--");
        for (int i = 0; i < 10; i++) {
            System.out.println(deque.removeLast());
        }
        System.out.println("size = " + deque.size());
        System.out.println("isEmpty = " + deque.isEmpty());

        System.out.println("==================================================================");

        System.out.println("--Test addFirst()--");
        for (int i = 0; i < 10; i++) {
            deque.addFirst(i);
        }
        System.out.println("size = " + deque.size());
        System.out.println("isEmpty = " + deque.isEmpty());
        for (int item : deque) System.out.print(item + ", ");
        System.out.println("\n");

        System.out.println("--Test removeFirst()--");
        for (int i = 0; i < 10; i++) {
            System.out.println(deque.removeFirst());
        }
        System.out.println("size = " + deque.size());
        System.out.println("isEmpty = " + deque.isEmpty());

        System.out.println("==================================================================");
        System.out.println("Test Exception");
        // deque.removeLast();
        // deque.removeFirst();
        // deque.addFirst(null);
        // deque.addLast(null);
    }

}
