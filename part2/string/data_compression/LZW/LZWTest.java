/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.TST;

public class LZWTest {

    private static final int R = 256;  // number of input chars
    private static final int L = 4096; // number of codewords = 2^W
    private static final int W = 12;   // codeword width

    private LZWTest() {

    }

    public static void compress() {
        String input = BinaryStdIn.readString();

        // code form 0 ~ R - 1 is single character
        TST<Integer> st = new TST<>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);

        // R is reserved codeword for EOF
        int code = R + 1; // current code(從R + 1開始)
        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);
            BinaryStdOut.write(st.get(s), W);
            int t = s.length();
            if (t < input.length() && code < L) {
                st.put(s + input.charAt(t), code++);
            }
            input = input.substring(t);
        }
        BinaryStdOut.write(R, W); // write EOF codeword
        BinaryStdOut.close();
    }

    public static void expand() {
        String[] st = new String[L]; // 不用使用TST, 用陣列就可以
        int i; // next available codeword value

        // code form 0 ~ R - 1 is single character
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;

        st[i++] = "";   // R is reserved codeword for EOF

        // start read bit stream
        int codeword = BinaryStdIn.readInt(W);
        if (codeword == R) return;

        // val跟s差一個character, 因為建立table需要下一個character
        String val = st[codeword];
        while (true) {
            BinaryStdOut.write(val); // 寫val
            codeword = BinaryStdIn.readInt(W); // 讀下一個
            if (codeword == R) break;
            String s = st[codeword]; // 下一個為s
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L) st[i++] = val + s.charAt(0); // 建table
            val = s; // s指定給val, 下一輪在寫入
        }

        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
