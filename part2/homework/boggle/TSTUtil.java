/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;

public class TSTUtil<Value> {

    private static class Node<Value> {
        private char c;
        private Value val;
        private Node<Value> left, right, mid;
    }

    private int n;
    private Node<Value> root;

    public TSTUtil() {

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

    public Iterable<String> keys() {
        Queue<String> result = new Queue<>();
        collect(root, new StringBuilder(), result);
        return result;
    }

    public boolean hasKeysWithPrefix(String prefix) {
        if (prefix == null)
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        Node<Value> x = get(root, prefix, 0);
        Queue<String> result = new Queue<>();
        if (x == null) return false;
        if (x.val != null) return true; // 檢查本身是否含有val
        return hasKeysWithPrefix(x.mid, new StringBuilder(prefix));
    }

    private boolean hasKeysWithPrefix(Node<Value> x, StringBuilder prefix) {
        if (x == null) return false;
        // inorder traversal
        if (hasKeysWithPrefix(x.left, prefix))
            return true;
        if (x.val != null)
            return true;
        prefix.append(x.c);
        if (hasKeysWithPrefix(x.mid, prefix)) return true;
        prefix.deleteCharAt(prefix.length() - 1);
        if (hasKeysWithPrefix(x.right, prefix)) return true;
        return false;
    }

    public Iterable<String> keysWithPrefix(String prefix) {
        if (prefix == null)
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        Node<Value> x = get(root, prefix, 0);
        Queue<String> result = new Queue<>();
        if (x == null) return result;
        if (x.val != null) result.enqueue(prefix); // 檢查本身是否含有val
        collect(x.mid, new StringBuilder(prefix), result);
        return result;
    }


    // collect all keys root at x
    private void collect(Node<Value> x, StringBuilder prefix, Queue<String> result) {
        if (x == null) return;
        // inorder traversal
        collect(x.left, prefix, result);
        if (x.val != null) result.enqueue(prefix.toString() + x.c);
        prefix.append(x.c);
        collect(x.mid, prefix, result);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, result);
    }

    public String longestPrefixOf(String query) {
        if (query == null)
            throw new IllegalArgumentException("calls longestPrefixOf() with null argument");
        if (query.length() == 0) return null;
        int length = 0;
        Node<Value> x = root;
        int d = 0;
        while (x != null && d < query.length()) {
            char c = query.charAt(d);
            if (c < x.c) x = x.left;
            else if (c > x.c) x = x.right;
            else {
                d++;
                if (x.val != null) length = d;
                x = x.mid;
            }
        }
        return query.substring(0, length);
    }

    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> result = new Queue<>();
        collect(root, new StringBuilder(), 0, pattern, result);
        return result;
    }

    private void collect(Node<Value> x, StringBuilder prefix, int d, String pattern,
                         Queue<String> result) {
        if (x == null) return;
        char c = pattern.charAt(d);
        if (c == '.' || c < x.c) collect(x.left, prefix, d, pattern, result);
        if (c == '.' || c == x.c) {
            if (x.val != null && d == pattern.length() - 1)
                result.enqueue(prefix.toString() + x.c);
            if (d < pattern.length() - 1) {
                collect(x.mid, prefix.append(x.c), d + 1, pattern, result);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
        if (c == '.' || c > x.c) collect(x.right, prefix, d, pattern, result);
    }

}

