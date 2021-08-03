/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] moveToFrontList = new char[R];
        for (char c = 0; c < R; c++)
            moveToFrontList[c] = c;

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int i;
            // find i such that moveToFrontList[i] == c
            for (i = 0; i < R; i++) {
                if (moveToFrontList[i] == c) {
                    BinaryStdOut.write(i, 8);
                    break;
                }
            }
            moveToFront(moveToFrontList, i);
        }
        BinaryStdOut.close();
    }

    // move ith element to index 0
    private static void moveToFront(char[] a, int i) {
        // move i to the fist element
        while (i > 0) {
            swap(a, i, i - 1);
            i--;
        }
    }

    private static void swap(char[] a, int i, int j) {
        char temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] moveToFrontList = new char[R];
        for (char c = 0; c < R; c++)
            moveToFrontList[c] = c;

        while (!BinaryStdIn.isEmpty()) {
            int i = BinaryStdIn.readChar();
            BinaryStdOut.write(moveToFrontList[i]);
            moveToFront(moveToFrontList, i);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
