/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import java.util.Set;

public class TrieSTTest<Value> {

    private static final int R = 26;
    private static final char OFF_SET = 65;

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
        x.next[c - OFF_SET] = put(x.next[c - OFF_SET], key, val, d + 1);
        return x;
    }

    public Object get(String key) {
        if (key == null) throw new IllegalArgumentException("key is null for get()");
        Node x = get(root, key, 0);
        if (x == null) return null;
        return x.val;
    }

    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("key is null for contains()");
        return get(key) != null;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        int c = key.charAt(d);
        return get(x.next[c - OFF_SET], key, d + 1);
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


    public boolean hasKeysWithPrefix(String prefix, Set<Character> charSet) {
        Node x = get(root, prefix, 0);
        return hasKeysWithPrefix(x, new StringBuilder(prefix), charSet);
    }

    // collect all keys root at x
    private boolean hasKeysWithPrefix(Node x, StringBuilder prefix, Set<Character> charSet) {
        if (x == null) return false;
        if (x.val != null) return true;
        for (char c : charSet) {
            prefix.append(c);
            if (hasKeysWithPrefix(x.next[c - OFF_SET], prefix, charSet))
                return true;
            prefix.deleteCharAt(prefix.length() - 1);
        }
        return false;
    }

    public static void main(String[] args) {
    }
}
