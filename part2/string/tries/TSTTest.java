/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

public class TSTTest<Value> {

    private static class Node<Value> {
        private char c;
        private Value val;
        private Node<Value> left, right, mid;
    }

    private int n;
    private Node<Value> root;

    public TSTTest() {

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
        root = put(root, key, val, 0);
    }

    private Node<Value> put(Node<Value> x, String key, Value val, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node<>();
            x.c = c;
        }
        if (c < x.c) x.left = put(x.left, key, val, d);
        else if (c > x.c) x.right = put(x.right, key, val, d);
        else if (d < key.length() - 1) x.mid = put(x.mid, key, val, d + 1);
        else {
            if (x.val == null) n++;
            x.val = val;
        }
        return x;
    }

    public Value get(String key) {
        if (key == null) throw new IllegalArgumentException("key is null for get()");
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node<Value> x = get(root, key, 0);
        if (x == null) return null;
        return x.val;
    }

    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("key is null for contains()");
        return get(key) != null;
    }

    private Node<Value> get(Node<Value> x, String key, int d) {
        if (x == null) return null;
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        int c = key.charAt(d);
        if (c < x.c) return get(x.left, key, d);
        else if (c > x.c) return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid, key, d + 1);
        else return x;
    }

    public void delete(String key) {
        if (key == null) throw new IllegalArgumentException("key is null for delete()");
        root = delete(root, key, 0);
    }

    private Node<Value> delete(Node<Value> x, String key, int d) {
        if (x == null) return null;
        char c = key.charAt(d);
        if (c < x.c) x.left = delete(x.left, key, d);
        else if (c > x.c) x.right = delete(x.right, key, d);
        else if (d < key.length() - 1) x.mid = delete(x.mid, key, d + 1);
        else {
            if (x.val != null) n--;
            x.val = null;
        }

        // If node has null value and mid, left, right are all null links, remove that node (and recur).
        if (x.val == null && x.mid == null && x.left == null && x.right == null)
            return null;
        else return x;
    }


    public static void main(String[] args) {

    }
}
