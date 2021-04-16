/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayStackTest<Item> implements Iterable<Item> {

    private static final int DEFAULT_CAPACITY = 4;
    private Item[] a;
    private int n;

    public ArrayStackTest() {
        a = (Item[]) new Object[DEFAULT_CAPACITY];
        n = 0;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void push(Item item) {
        if (n == a.length) resize(2 * a.length);
        a[n++] = item;
    }

    public Item pop() {
        if (n == 0) throw new NoSuchElementException("Stack is empty.");
        Item result = a[n - 1];
        a[n - 1] = null;
        n--;
        System.out.println("a.length = " + a.length + ", n = " + n);
        if (n > 0 && n == a.length / 4) resize(a.length / 2);
        return result;
    }

    private void resize(int capacity) {
        System.out.println("capacity = " + capacity);
        a = Arrays.copyOf(a, capacity);
    }

    public Iterator<Item> iterator() {
        return new StackIterator();
    }

    private class StackIterator implements Iterator<Item> {

        private int current;

        public StackIterator() {
            current = n;
        }

        public boolean hasNext() {
            return current > 0;
        }

        public Item next() {
            if (current == 0) throw new NoSuchElementException();
            return a[--current];
        }
    }

    public static void main(String[] args) {
        ArrayStackTest<String> stack = new ArrayStackTest<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) stack.push(item);
            else if (!stack.isEmpty()) StdOut.print(stack.pop() + " ");
        }
        StdOut.println("(" + stack.size() + " left on stack)");

        // print what's left on the stack
        StdOut.print("Left on stack: ");
        for (String s : stack) {
            StdOut.print(s + " ");
        }
        StdOut.println();
        for (int i = 0; i < 5; i++) {
            System.out.println(stack.pop());
        }
    }
}
