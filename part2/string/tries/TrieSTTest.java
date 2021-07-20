/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

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


    public static void main(String[] args) {

    }
}
