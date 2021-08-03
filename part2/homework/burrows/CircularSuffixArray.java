/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;

import java.util.Arrays;

public class CircularSuffixArray {

    private static class CircularSuffix implements Comparable<CircularSuffix> {
        private final String text;
        private final int start;

        private CircularSuffix(String text, int start) {
            this.text = text;
            this.start = start;
        }

        private int length() {
            return text.length();
        }

        private char charAt(int i) {
            return text.charAt((start + i) % text.length());
        }

        public int compareTo(CircularSuffix that) {
            if (this == that) return 0;  // optimization
            for (int i = 0; i < length(); i++) {
                if (this.charAt(i) < that.charAt(i)) return -1;
                if (this.charAt(i) > that.charAt(i)) return +1;
            }
            return this.length() - that.length();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length(); i++)
                sb.append(charAt(i));
            return sb.toString();
        }
    }

    private final String s;
    private final int[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException(
                    "argument for constructor CircularSuffixArray() is null");
        this.s = s;
        index = new int[length()];
        CircularSuffix[] circularSuffixArray = new CircularSuffix[length()];
        for (int i = 0; i < length(); i++) {
            circularSuffixArray[i] = new CircularSuffix(s, i);
        }
        
        // 排序
        Arrays.sort(circularSuffixArray);

        // 回填index[]
        for (int i = 0; i < length(); i++)
            index[i] = circularSuffixArray[i].start;
    }

    // length of s
    public int length() {
        return s.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > length())
            throw new IllegalArgumentException("argument for index() out of bound");
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s = BinaryStdIn.readString();
        CircularSuffixArray test = new CircularSuffixArray(s);
        System.out.println(Arrays.toString(test.index));
    }
}
