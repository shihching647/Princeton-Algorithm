/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class RunLengthTest {

    private static final int R = 256;
    private static final int LG_R = 8;

    private RunLengthTest() {

    }

    public static void compress() {
        char run = 0;
        boolean old = false; // 從false開始

        while (!BinaryStdIn.isEmpty()) {
            boolean b = BinaryStdIn.readBoolean();
            if (b != old) { // 當讀到的b不等於前一個old
                BinaryStdOut.write(run, LG_R); // 寫入
                run = 1;    // run為1
                old = !old; // 切換
            }
            else {
                // 當run超過最大count時
                if (run == R - 1) {
                    BinaryStdOut.write(run, LG_R); // 寫入前一個run
                    run = 0; // 相反bit的下一個run的length為0，之後相同的bit繼續run
                    BinaryStdOut.write(run, LG_R); // 寫入相反的bit
                }
                run++;
            }
        }
        BinaryStdOut.write(run, LG_R); // 寫入最後一個run的結果
        BinaryStdOut.close();
    }

    public static void expand() {
        boolean b = false; // 從false開始
        while (!BinaryStdIn.isEmpty()) {
            int run = BinaryStdIn.readInt(LG_R);
            for (int i = 0; i < run; i++) {
                BinaryStdOut.write(b);
            }
            b = !b;
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
