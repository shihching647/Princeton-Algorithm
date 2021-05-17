/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public final class Board {
    private final int[][] tiles;
    private int n; // dimension
    private int hamming;
    private int manhattan;
    private int blankI, blankJ;

    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        this.tiles = new int[n][n];
        int[][] goalBoard = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.tiles[i][j] = tiles[i][j];
                int correctVale = getCorrectValue(i, j);
                goalBoard[i][j] = (correctVale == n * n) ? 0 : correctVale;
                if (tiles[i][j] == 0) {
                    blankI = i;
                    blankJ = j;
                }
                else { // if tile[i][j] is not blank, compute Hamming and Manhattan distance
                    // compute hamming
                    if (tiles[i][j] != goalBoard[i][j]) hamming++;
                    // compute manhattan
                    manhattan += computeManhattan(tiles[i][j], i, j);
                }
            }
        }
    }

    private int getCorrectValue(int i, int j) {
        if (i == n - 1 && j == n - 1) return 0;
        else return i * n + j + 1;
    }

    private int computeManhattan(int value, int i, int j) {
        if (value == 0) {
            return 0;
        }
        value--;
        int correctI = value / n;
        int correctJ = value % n;
        return Math.abs(correctI - i) + Math.abs(correctJ - j);
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(" " + tiles[i][j]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan == 0 && hamming == 0;
    }

    // does this board equal y?
    @Override
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y.getClass() != Board.class) return false; // check data type
        Board that = (Board) y;
        if (this == that) return true;      // check reference
        if (this.n != that.n) return false; // check dimension
        // return checkTiles(this.tiles, that.tiles);
        return Arrays.deepEquals(this.tiles, that.tiles);
    }

    // check every title
    private boolean checkTiles(int[][] tiles1, int[][] tiles2) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // System.out.println("tiles1[i][j] = " + tiles1[i][j]);
                // System.out.println("tiles2[i][j] = " + tiles2[i][j]);
                if (tiles1[i][j] != tiles2[i][j]) return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> list = new LinkedList<>();
        int[] deltaI = { -1, 0, 1, 0 };
        int[] deltaJ = { 0, 1, 0, -1 };
        for (int i = 0; i < deltaI.length; i++) {
            int newI = blankI + deltaI[i];
            int newJ = blankJ + deltaJ[i];
            if (newI >= 0 && newI < n && newJ >= 0 && newJ < n) {
                list.add(createNewBoard(newI, newJ, blankI, blankJ));
            }
        }
        return list;
    }

    private Board createNewBoard(int i, int j, int newI, int newJ) {
        // 2-D array clone
        int[][] newTiles = new int[n][n];
        for (int k = 0; k < n; k++) {
            newTiles[k] = tiles[k].clone();
        }

        int temp = newTiles[i][j];
        newTiles[i][j] = newTiles[newI][newJ];
        newTiles[newI][newJ] = temp;
        return new Board(newTiles);
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[] deltaI = { -1, 0, 1, 0 };
        int[] deltaJ = { 0, 1, 0, -1 };
        int[] temp = new int[4];
        int k = 0;
        for (int i = 0; i < deltaI.length; i++) {
            int newI = blankI + deltaI[i];
            int newJ = blankJ + deltaJ[i];
            if (newI >= 0 && newI < n && newJ >= 0 && newJ < n) {
                temp[k++] = newI;
                temp[k++] = newJ;
                if (k == 4) break;
            }
        }
        return createNewBoard(temp[0], temp[1], temp[2], temp[3]);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // for each command-line argument
        for (String filename : args) {

            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

            Board board = new Board(tiles);
            showBoardInfo(board);
            System.out.println("board.equals(board) = " + board.equals(board));
            System.out.println("twin() = ");
            System.out.println(board.twin());
            System.out.println("======================");

            for (Board b : board.neighbors()) {
                showBoardInfo(b);
                System.out.println("board.equals(b) = " + board.equals(b));
                System.out.println("twin() = ");
                System.out.println(b.twin());
                System.out.println("======================");
            }


        }

    }

    private static void showBoardInfo(Board board) {
        System.out.println(board);
        System.out.println("dimension() = " + board.dimension());
        System.out.println("isGoal() = " + board.isGoal());
        System.out.println("manhattan() = " + board.manhattan());
        System.out.println("hamming() = " + board.hamming());
    }
}
