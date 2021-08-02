/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.MinPQ;

public class HuffmanTest {

    private static final int R = 256;

    private HuffmanTest() {

    }

    private static class Node implements Comparable<Node> {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public int compareTo(Node that) {
            return Integer.compare(this.freq, that.freq);
        }
    }

    public static void compress() {
        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();

        // 1. build codeworld (Huffman algorithm 講義p31)
        // 1.1 count frequency
        int[] freq = new int[R];
        for (int i = 0; i < input.length; i++)
            freq[input[i]]++;

        // 1.2 build Huffman trie
        Node root = buildTrie(freq);

        // 1.3 build lookup table
        String[] st = new String[R];
        buildTable(st, root, "");

        // 2. write Huffman encoding
        // 2.1 write trie for decoder (講義p28)
        writeTrie(root);

        // 2.2 write number of bytes in original uncompressed message
        BinaryStdOut.write(input.length);

        // 2.3 encode input
        for (int i = 0; i < input.length; i++) {
            String code = st[input[i]];
            for (int c = 0; c < code.length(); c++) {
                if (code.charAt(c) == '0')
                    BinaryStdOut.write(false);
                else if (code.charAt(c) == '1')
                    BinaryStdOut.write(true);
                else throw new IllegalStateException("Illegal state");
            }
        }

        BinaryStdOut.close();
    }

    private static Node buildTrie(int[] freq) {
        MinPQ<Node> pq = new MinPQ<>();
        for (char c = 0; c < R; c++) {
            if (freq[c] > 0)
                pq.insert(new Node(c, freq[c], null, null));
        }

        while (pq.size() > 1) {
            Node right = pq.delMin();
            Node left = pq.delMin();
            Node parent = new Node('\0', right.freq + left.freq, right, left);
            pq.insert(parent);
        }

        return pq.delMin();
    }

    private static void buildTable(String[] st, Node x, String s) {
        // 此處不須要檢查 x == null, 因為是bottom up建立的關係, 所以所有Node的subTree數量不是0就是2
        if (!x.isLeaf()) {
            buildTable(st, x.left, s + "0");
            buildTable(st, x.right, s + "1");
        }
        else {
            st[x.ch] = s;
        }
    }

    private static void writeTrie(Node x) {
        // preorder traversal
        if (x.isLeaf()) {
            BinaryStdOut.write(true); // 遇到leaf node寫ch前補一個1
            BinaryStdOut.write(x.ch, 8);
            return;
        }
        BinaryStdOut.write(false); // internal node寫0
        writeTrie(x.left);
        writeTrie(x.right);
    }

    public static void expand() {

        // 1. read in Huffman trie from input stream
        Node root = readTrie();

        // 2. number of bytes to write
        int n = BinaryStdIn.readInt();

        // 3. decode input
        for (int i = 0; i < n; i++) {
            Node x = root;
            while (!x.isLeaf()) {
                boolean bit = BinaryStdIn.readBoolean();
                if (bit) x = x.right;
                else x = x.left;
            }
            BinaryStdOut.write(x.ch, 8);
        }
        BinaryStdOut.close();
    }

    private static Node readTrie() {
        boolean isLeaf = BinaryStdIn.readBoolean();
        if (isLeaf) {
            char c = BinaryStdIn.readChar();
            return new Node(c, -1, null, null);
        }
        Node left = readTrie();
        Node right = readTrie();
        return new Node('\0', -1, left, right);
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
