/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class TrieSTTest<Value> {

    private static final int R = 256;        // extended ASCII

    private static class Node {
        private Object val;
        private Node[] next = new Node[R];
    }

    private int n;
    private Node root;

    public TrieSTTest() {

    }

    public int size() {
        return n;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void put(String key, Value val) {
        if (key == null) throw new IllegalArgumentException("key is null for put()");
        if (val == null) delete(key);
        else root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, Value val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (x.val == null) n++;
            x.val = val;
            return x;
        }
        char c = key.charAt(d);
        x.next[c] = put(x.next[c], key, val, d + 1);
        return x;
    }

    public Value get(String key) {
        if (key == null) throw new IllegalArgumentException("key is null for get()");
        Node x = get(root, key, 0);
        if (x == null) return null;
        return (Value) x.val;
    }

    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("key is null for contains()");
        return get(key) != null;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        int c = key.charAt(d);
        return get(x.next[c], key, d + 1);
    }

    public void delete(String key) {
        if (key == null) throw new IllegalArgumentException("key is null for delete()");
        root = delete(root, key, 0);
    }

    private Node delete(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) {
            if (x.val != null) n--;
            x.val = null;
        }
        else {
            int c = key.charAt(d);
            x.next[c] = delete(x.next[c], key, d + 1);
        }

        // If node has null value and all null links, remove that node (and recur).
        if (x.val != null) return x;
        for (int c = 0; c < R; c++) {
            if (x.next[c] != null)
                return x;
        }
        return null;
    }

    public Iterable<String> keys() {
        return keysWithPrefix("");
    }

    public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> result = new Queue<>();
        Node x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), result);
        return result;
    }

    // collect all keys root at x
    private void collect(Node x, StringBuilder prefix, Queue<String> result) {
        if (x == null) return;
        if (x.val != null) result.enqueue(prefix.toString());
        for (char c = 0; c < R; c++) {
            prefix.append(c);
            collect(x.next[c], prefix, result);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    public String longestPrefixOf(String query) {
        if (query == null)
            throw new IllegalArgumentException("argument to longestPrefixOf() is null");
        // length 為prefix match最長的長度
        int length = longestPrefixOf(root, query, 0, -1);
        if (length == -1) return null;
        else return query.substring(0, length);
    }

    private int longestPrefixOf(Node x, String query, int d, int length) {
        if (x == null) return length;
        if (x.val != null) length = d; // Keep track of longest key encountered.
        if (d == query.length()) return length; // terminate
        char c = query.charAt(d);
        return longestPrefixOf(x.next[c], query, d + 1, length);
    }

    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> result = new Queue<String>();
        collect(root, new StringBuilder(), pattern, result);
        return result;
    }

    private void collect(Node x, StringBuilder prefix, String pattern, Queue<String> result) {
        if (x == null) return;
        int d = prefix.length();
        if (d == pattern.length() && x.val != null)
            result.enqueue(prefix.toString());
        if (d == pattern.length()) return; // terminate
        char c = pattern.charAt(d);
        if (c == '.') { // wildcard character
            for (char i = 0; i < R; i++) {
                prefix.append(i);
                collect(x.next[i], prefix, pattern, result);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
        else {
            prefix.append(c);
            collect(x.next[c], prefix, pattern, result);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    public static void main(String[] args) {
        // build symbol table from standard input
        TrieSTTest<Integer> st = new TrieSTTest<Integer>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }

        // print results
        if (st.size() < 100) {
            StdOut.println("keys(\"\"):");
            for (String key : st.keys()) {
                StdOut.println(key + " " + st.get(key));
            }
            StdOut.println();
        }

        StdOut.println("longestPrefixOf(\"shellsort\"):");
        StdOut.println(st.longestPrefixOf("shellsort"));
        StdOut.println();

        StdOut.println("longestPrefixOf(\"quicksort\"):");
        StdOut.println(st.longestPrefixOf("quicksort"));
        StdOut.println();

        StdOut.println("keysWithPrefix(\"shor\"):");
        for (String s : st.keysWithPrefix("shor"))
            StdOut.println(s);
        StdOut.println();

        StdOut.println("keysThatMatch(\".he.l.\"):");
        for (String s : st.keysThatMatch(".he.l."))
            StdOut.println(s);
    }
}
