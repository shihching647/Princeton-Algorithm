/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

/**
 * 使用方式
 * <p>
 * java-algs4 GenomeTest - < genomeTiny.txt > genomeTiny.2bit
 * (-代表壓縮) < (欲壓縮的檔案) > (壓縮完的檔案)
 * <p>
 * java-algs4 GenomeTest + > genomeTiny.2bit
 * (+代表解壓縮) < (壓縮完的檔案)
 * <p>
 * 其他用法請參考課本P.821
 */

import edu.princeton.cs.algs4.Alphabet;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdIn;

public class GenomeTest {

    private GenomeTest() {

    }

    public static void compress() {
        Alphabet DNA = Alphabet.DNA;
        String s = StdIn.readString();
        int n = s.length();
        BinaryStdOut.write(n);

        // Write two-bit code for char.
        for (int i = 0; i < n; i++) {
            int c = DNA.toIndex(s.charAt(i));
            BinaryStdOut.write(c, 2);
        }
        BinaryStdOut.close();
    }

    public static void expand() {
        Alphabet DNA = Alphabet.DNA;
        int n = BinaryStdIn.readInt();

        for (int i = 0; i < n; i++) {
            char c = BinaryStdIn.readChar(2);
            BinaryStdOut.write(DNA.toChar(c));
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
