/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;

import java.util.HashMap;
import java.util.Map;

public class BurrowsWheeler {

    private static final int R = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray suffixArray = new CircularSuffixArray(s);

        // find first index(原始String在CircularSuffixArray排序的位置)
        for (int i = 0; i < suffixArray.length(); i++) {
            if (suffixArray.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }

        // suffixArray.index(i)回傳的index為原始String以第i字元當作起頭的circular suffix
        // 該suffixArray.index(i)往前推一格便可得到最後一個字元
        for (int i = 0; i < suffixArray.length(); i++) {
            int index = suffixArray.index(i) - 1; // 前一個字元
            if (index < 0) index += suffixArray.length(); // circular
            BinaryStdOut.write(s.charAt(index));
        }

        BinaryStdOut.close();
    }


    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        // 使用R個queue去記住各char出現的位置, 方便後面建立next[] -> 會有warning導致分數太低
        // 改用map紀錄
        Map<Character, Queue<Integer>> map = new HashMap<>();
        int count = 0;
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            Queue<Integer> q = map.getOrDefault(c, new Queue<>());
            q.enqueue(count++);
            map.put(c, q);
        }

        // construct last column t[] and next[]
        char[] t = new char[count];
        int[] next = new int[count];
        int i = 0;
        for (char c = 0; c < R; c++) {
            Queue<Integer> q = map.get(c);
            if (q != null) {
                while (!q.isEmpty()) {
                    t[i] = c;
                    next[i++] = q.dequeue();
                }
            }
        }

        // decode original string
        int j = 0;
        while (j < count) {
            BinaryStdOut.write(t[first]);
            first = next[first];
            j++;
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
