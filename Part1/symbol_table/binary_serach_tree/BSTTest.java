/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

public class BSTTest<Key extends Comparable<Key>, Value> {

    // Helper class
    private class Node {
        private Key key;
        private Value val;
        private Node left, right;
        private int size; // number of nodes in subtree

        public Node(Key key, Value val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }

    private Node root; // the root of BST

    public BSTTest() {

    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    }

    public Value get(Key key) {
        return get(root, key);
    }

    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(root, key) != null;
    }

    private Value get(Node x, Key key) {
        if (key == null) throw new IllegalArgumentException("calls get() with a null key");
        if (x == null) return null; // terminal condition
        int comp = key.compareTo(x.key);
        if (comp < 0) return get(x.left, key);
        else if (comp > 0) return get(x.right, key);
        else return x.val;
    }

    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("calls put() with a null key");
        if (val == null) {
            delete(key);
            return;
        }
        root = put(root, key, val);
        assert check();
    }

    private Node put(Node x, Key key, Value val) {
        if (x == null) return new Node(key, val, 1);
        int comp = key.compareTo(x.key);
        if (comp < 0) x.left = put(x.left, key, val);
        else if (comp > 0) x.right = put(x.right, key, val);
        else x.val = val;
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    /**
     * Removes the specified key and its associated value from this symbol table
     * (if the key is in this symbol table).
     *
     * @param key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    private void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("calls delete() with a null key");
        root = delete(root, key);
        assert check();
    }

    private Node delete(Node x, Key key) {
        if (x == null) return null;  // didn't find the key

        int comp = key.compareTo(x.key);
        if (comp < 0) x.left = delete(x.left, key); // try to find key
        else if (comp > 0) x.right = delete(x.right, key); // try to find key
        else {
            // find the key
            // case 1, 2 -> the node has no child or one child
            if (x.left == null) return x.right;
            if (x.right == null) return x.left;

            // case 3 -> the node has two children, find the successor x.
            // Delete it and replace with current node t
            Node t = x;
            x = min(x.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    // ==============================================================================================
    // 3.2 Ordered operations
    // 1. min/max
    // 2. floor/ceiling
    // 3. rank
    // 4. select
    // 5. keys
    // 6. deleteMin/deleteMax
    //

    /**
     * Returns the smallest key in the symbol table.
     *
     * @return the smallest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
        return min(root).key;
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        return min(x.left);

        // iterate
        // while (x.left != null)
        //     x = x.left;
        // return x;
    }

    /**
     * Returns the smallest key in the symbol table.
     *
     * @return the smallest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    public Key max() {
        if (isEmpty()) throw new NoSuchElementException("calls max() with empty symbol table");
        return max(root).key;
    }

    private Node max(Node x) {
        if (x.right == null) return x;
        return max(x.right);

        // iterate
        // while (x.right != null)
        //     x = x.right;
        // return x;
    }

    /**
     * Returns the largest key in the symbol table less than or equal to {@code key}.
     *
     * @param key the key
     * @return the largest key in the symbol table less than or equal to {@code key}
     * @throws NoSuchElementException   if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Key floor(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to floor() is nul");
        if (isEmpty()) throw new NoSuchElementException("calls floor() with empty symbol table");
        Node x = floor(root, key);
        if (x == null) throw new IllegalArgumentException("argument to floor() is too small");
        return x.key;

        // another implementation
        // Key k = floor2(root, key, null);
        // if (k == null) throw new IllegalArgumentException("argument to floor() is too small");
        // return k;
    }

    private Node floor(Node x, Key key) {
        if (x == null) return null;
        int comp = key.compareTo(x.key);
        if (comp < 0) return floor(x.left, key);
        else if (comp == 0) return x;
        else {
            Node node = floor(x.right, key);
            if (node != null) return node;
            else return x;
        }
    }

    private Key floor2(Node x, Key key, Key best) {
        if (x == null) return best;
        int comp = key.compareTo(x.key);
        if (comp < 0) return floor2(x.left, key, best);
        else if (comp > 0) return floor2(x.right, key, x.key);
        else return x.key;
    }

    /**
     * Returns the smallest key in the symbol table greater than or equal to {@code key}.
     *
     * @param key the key
     * @return the smallest key in the symbol table greater than or equal to {@code key}
     * @throws NoSuchElementException   if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Key ceiling(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to ceiling() is nul");
        if (isEmpty()) throw new NoSuchElementException("calls ceiling() with empty symbol table");
        Node x = ceiling(root, key);
        if (x == null) throw new IllegalArgumentException("argument to ceiling() is too large");
        return x.key;
    }

    private Node ceiling(Node x, Key key) {
        if (x == null) return null;
        int comp = key.compareTo(x.key);
        if (comp > 0) return ceiling(x.right, key);
        else if (comp == 0) return x;
        Node node = ceiling(x.left, key);
        if (node == null) return x;
        else return node;
    }

    /**
     * Return the number of keys in the symbol table strictly less than {@code key}.
     *
     * @param key the key
     * @return the number of keys in the symbol table strictly less than {@code key}
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public int rank(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to rank() is null");
        return rank(key, root);
    }

    private int rank(Key key, Node x) {
        if (x == null) return 0;
        int comp = key.compareTo(x.key);
        if (comp < 0) return rank(key, x.left);
        else if (comp > 0) return 1 + size(x.left) + rank(key, x.right);
        else return size(x.left); // 等於的話要回傳x.left, 因為rank的定義是：小於key的個數(不包含等於)
    }

    /**
     * Return the key in the symbol table of a given {@code rank}.
     * This key has the property that there are {@code rank} keys in
     * the symbol table that are smaller. In other words, this key is the
     * ({@code rank}+1)st smallest key in the symbol table.
     *
     * @param rank the order statistic
     * @return the key in the symbol table of given {@code rank}
     * @throws IllegalArgumentException unless {@code rank} is between 0 and
     *                                  <em>n</em>–1
     */
    public Key select(int rank) {
        if (rank < 0 || rank >= size())
            throw new IllegalArgumentException("argument to select() is invalid: " + rank);
        return select(rank, root);
    }

    private Key select(int rank, Node x) {
        if (x == null) return null;
        int leftSize = size(x.left);
        if (leftSize == rank) return x.key; // 左邊size為rank, 就表示本身為第rank + 1小的key(select定義)
        else if (leftSize > rank) return select(rank, x.left); // leftSize超過rank，往左找
        else return select(rank - leftSize - 1, x.right); // -1是扣掉自己
    }

    /**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     *
     * @return all keys in the symbol table
     */
    public Iterable<Key> keys() {
        if (isEmpty()) return new Queue<Key>();
        // implementation 1
        return keys(min(), max());

        // implementation2
        // Queue<Key> q = new Queue<>();
        // inorder(root, q);
        // return q;
    }

    private Iterable<Key> keys(Key lo, Key hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to keys() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to keys() is null");

        Queue<Key> q = new Queue<>();
        keys(root, q, lo, hi);
        return q;
    }

    private void keys(Node x, Queue<Key> q, Key lo, Key hi) {
        if (x == null) return;
        int compLow = lo.compareTo(x.key);
        int compHigh = hi.compareTo(x.key);
        if (compLow < 0) keys(x.left, q, lo, hi); // 比low小, 還要再往下找x.left
        if (compLow <= 0 && compHigh >= 0) q.enqueue(x.key); // 介於lo ~ hi加到queue裡面
        if (compHigh > 0) keys(x.right, q, lo, hi); // 比high大，還要再往下找x.right
    }

    private void inorder(Node x, Queue<Key> q) {
        if (x == null) return;
        inorder(x.left, q);
        q.enqueue(x.key);
        inorder(x.right, q);
    }

    /**
     * Returns the number of keys in the symbol table in the given range.
     *
     * @param lo minimum endpoint
     * @param hi maximum endpoint
     * @return the number of keys in the symbol table between {@code lo}
     * (inclusive) and {@code hi} (inclusive)
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *                                  is {@code null}
     */
    public int size(Key lo, Key hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to size() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to size() is null");

        if (lo.compareTo(hi) > 0) return 0;
        else if (contains(hi)) return rank(hi) - rank(lo) + 1; // 少於hi的個數 - 少於lo的個數 + 自己
        else return rank(hi) - rank(lo); // 少於hi的個數 - 少於lo的個數
    }

    /**
     * Returns the height of the BST (for debugging).
     *
     * @return the height of the BST (a 1-node tree has height 0)
     */
    public int height() {
        return height(root);
    }

    private int height(Node x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.left), height(x.right));
    }

    public Iterable<Key> levelOrder() {
        Queue<Key> keys = new Queue<>();
        Queue<Node> queue = new Queue<>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            if (x == null) continue;
            keys.enqueue(x.key);
            queue.enqueue(x.left);
            queue.enqueue(x.right);
        }
        return keys;
    }

    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow");
        root = deleteMin(root);
        assert check();
    }

    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow");
        root = deleteMax(root);
        assert check();
    }

    private Node deleteMax(Node x) {
        if (x.right == null) return x.left;
        x.right = deleteMin(x.right);
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }


    /*************************************************************************
     *  Check integrity of BST data structure.
     ***************************************************************************/

    private boolean check() {
        if (!isBST()) StdOut.println("Not in symmetric order");
        if (!isSizeConsistent()) StdOut.println("Subtree counts not consistent");
        if (!isRankConsistent()) StdOut.println("Ranks not consistent");
        return true;
    }

    // does this binary tree satisfy symmetric order?
    private boolean isBST() {
        return isBST(root, null, null);
    }

    private boolean isBST(Node x, Key min, Key max) {
        if (x == null) return true;
        if (min != null && x.key.compareTo(min) <= 0) return false;
        if (max != null && x.key.compareTo(max) >= 0) return false;
        return isBST(x.left, min, x.key) && isBST(x.right, x.key, max);
    }

    private boolean isSizeConsistent() {
        return isSizeConsistent(root);
    }

    private boolean isSizeConsistent(Node x) {
        if (x == null) return true;
        if (size(x) != 1 + size(x.left) + size(x.right)) return false;
        return isSizeConsistent(x.left) && isSizeConsistent(x.right);
    }

    private boolean isRankConsistent() {
        for (int i = 0; i < size(); i++)
            if (i != rank(select(i))) return false; // select(i)找到的key, 在用rank(key)去檢查是否 == i
        for (Key key : keys())
            if (key.compareTo(select(rank(key))) != 0)
                return false; // rank(key)有多少key小於他, 在用這個值去select檢查找出來的key是否相等
        return true;
    }

    public static void main(String[] args) {
        // Basic operation, get(), put(), delete(), keys(), levelOrder(), height()
        BSTTest<Integer, Character> tree = new BSTTest<>();
        tree.put(1, 'A');
        tree.put(26, 'Z');
        tree.put(7, 'G');
        tree.put(2, 'B');
        tree.put(24, 'X');
        tree.put(11, 'K'); // removed
        // levelOrder() = 1,26,7,2,24,11
        for (int key : tree.levelOrder()) {
            System.out.print(key + ", ");
        }
        System.out.println();
        System.out.println("tree.size() = " + tree.size()); // 6
        System.out.println("tree.get(2) = " + tree.get(2)); // 'B'
        System.out.println("tree.get(4) = " + tree.get(4)); // null
        System.out.println("tree.height() = " + tree.height()); // 4
        // keys()
        for (int key : tree.keys()) {
            System.out.print(key + ", ");
        }
        System.out.println();
        // delete()
        tree.delete(3);
        tree.delete(11);
        for (int key : tree.keys()) {
            System.out.print(key + ", ");
        }
        System.out.println();


        // ordered operations: floor(), ceiling(), min(), max(), deleteMin(), deleteMax()
        // select(), rank()
        System.out.println("tree.floor(24) = " + tree.floor(24)); // 24
        System.out.println("tree.floor(10) = " + tree.floor(10)); // 7
        System.out.println("tree.ceiling(24) = " + tree.ceiling(24)); // 24
        System.out.println("tree.ceiling(6) = " + tree.ceiling(6)); // 7
        System.out.println("tree.min() = " + tree.min()); // 1
        System.out.println("tree.max() = " + tree.max()); // 26
        System.out.println("tree.select(0) = " + tree.select(0)); // = min() = 1
        System.out.println("tree.select(1) = " + tree.select(1)); // = 2
        System.out.println("tree.rank(1) = " + tree.rank(1)); // 沒有小於1的 -> 0
        System.out.println("tree.rank(3) = " + tree.rank(3)); // 有1,2  -> 2
        System.out.println("tree.size(1, 24) = " + tree.size(1, 24)); // 4

        tree.deleteMin();
        tree.deleteMax();
        for (int key : tree.keys()) {
            System.out.print(key + ", ");
        }
        System.out.println();
        System.out.println(tree.floor(1)); // exception

    }
}
