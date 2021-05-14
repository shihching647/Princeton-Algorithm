/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Solver {
    private int moves;
    private List<Board> solutions;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("initial board is null.");

        Comparator<Board> comparator = new Comparator<Board>() {
            public int compare(Board o1, Board o2) {
                return Integer.compare(o1.manhattan(), o2.manhattan());
            }
        };
        MinPQ<Board> pq = new MinPQ<>(comparator);
        moves = -1;
        solutions = new LinkedList<>();
        pq.insert(initial);
        showInfo(initial);
        System.out.println("====================");
        Board pre = null;

        while (solutions.isEmpty()) {
            Board board = pq.delMin();
            if (board.isGoal())
                solutions.add(board);
            else {
                pre = board;
                moves++;
                for (Board b : board.neighbors()) {
                    if (!b.equals(pre)) {
                        pq.insert(b);
                        showInfo(b);
                    }
                }
                System.out.println("====================");
            }
        }
    }

    private void showInfo(Board b) {
        System.out.println("priority  = " + (b.manhattan() + moves));
        System.out.println("moves     = " + moves);
        System.out.println("manhattan = " + b.manhattan());
        System.out.println(b);
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return true;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return solutions.size() == 0 ? moves : -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solutions;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
