/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

public class TrieSTTest<Value> {

    private static final int R = 26;
    private static final char OFF_SET = 65;

    private static class Node {
        private Object val;
        private Node[] next = new Node[R];
        private int numOfKeys = 0; // 記錄在這個Node底下的key有幾個
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
        else root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, Value val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (x.val == null) {
                n++;
                x.numOfKeys++;
            }
            x.val = val;
            return x;
        }
        char c = key.charAt(d);
        // 先扣掉可能會改變的key的數量
        if (x.next[c - OFF_SET] != null) {
            x.numOfKeys -= x.next[c - OFF_SET].numOfKeys;
        }
        x.next[c - OFF_SET] = put(x.next[c - OFF_SET], key, val, d + 1);
        // 在加回改變後的key的數量
        if (x.next[c - OFF_SET] != null) {
            x.numOfKeys += x.next[c - OFF_SET].numOfKeys;
        }
        return x;
    }

    public Object get(String key) {
        if (key == null) throw new IllegalArgumentException("key is null for get()");
        Node x = get(root, key, 0);
        if (x == null) return null;
        return x.val;
    }

    private Node get(Node x, String key, int d) {
        Node p = x;
        while (p != null && d < key.length()) {
            int c = key.charAt(d);
            p = p.next[c - OFF_SET];
            d++;
        }
        return p;
    }

    public boolean hasKeysWithPrefix(String prefix) {
        Node x = get(root, prefix, 0);
        if (x == null)
            return false;
        return x.numOfKeys != 0;
    }

    public static void main(String[] args) {

    }
}
