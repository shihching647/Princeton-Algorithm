/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BoggleSolver {
    private static final int[] SCORE_BOARD = { 0, 0, 0, 1, 1, 2, 3, 5 };
    private static final int MIN_LENGTH_OF_MAX_POINT = 8;
    private static final int MIN_LENGTH_TO_GET_SCORE = 3;
    private static final int MAX_POINT = 11;
    private static final int R = 26;
    private static final char SPECIAL_CHAR = 'Q';

    private TST<Integer>[] dic; // dic分別為開頭為'A' ~ 'Z'的 TST(Ternary Search Trie)
    private boolean[][] internalBoard;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        dic = new TST[R];
        for (int i = 0; i < dic.length; i++)
            dic[i] = new TST<>();

        for (String word : dictionary) {
            TST<Integer> table = dic[(word.charAt(0) - 'A')];
            if (word.length() >= MIN_LENGTH_TO_GET_SCORE)
                table.put(word, calculateScore(word));
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> result = new HashSet<>();
        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                internalBoard = new boolean[board.rows()][board.cols()];
                char ch = board.getLetter(row, col);
                dfs(row, col, new StringBuilder(), board, dic[ch - 'A'],
                    result);
            }
        }
        return result;
    }

    private void dfs(int row, int col, StringBuilder prefix, BoggleBoard board,
                     TST<Integer> table, Set<String> result) {

        // Cut off. If there are no more words start with prefix, stop immediately.
        if (prefix.length() >= MIN_LENGTH_TO_GET_SCORE) {
            Iterator<String> it = table.keysWithPrefix(prefix.toString()).iterator();
            if (!it.hasNext())
                return;
        }

        internalBoard[row][col] = true;
        char ch = board.getLetter(row, col);
        String str = prefix.append(ch).toString();
        // special case : 'Q' -> "QA"
        if (ch == SPECIAL_CHAR)
            str = prefix.append('U').toString();

        if (str.length() >= MIN_LENGTH_TO_GET_SCORE && table.get(str) != null)
            result.add(str);

        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                if (r >= 0 && r < board.rows() && c >= 0 && c < board.cols()
                        && !internalBoard[r][c])
                    dfs(r, c, prefix, board, table, result);
            }
        }

        internalBoard[row][col] = false;
        prefix.deleteCharAt(prefix.length() - 1);
        // special case 要多移除一個字元
        if (ch == SPECIAL_CHAR)
            prefix.deleteCharAt(prefix.length() - 1);
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word.length() < 1)
            return 0;
        Integer score = dic[word.charAt(0) - 'A'].get(word);
        return score == null ? 0 : score;
    }

    private int calculateScore(String word) {
        if (word.length() >= MIN_LENGTH_OF_MAX_POINT) return MAX_POINT;
        else return SCORE_BOARD[word.length()];
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
