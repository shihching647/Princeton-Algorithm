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

public final class Solver {

    // Helper inner class, support game tree representation
    private static class Node {
        private int priority;
        private int moves;
        private int manhattan;
        private Board board;
        private Node parent;

        public Node(Board board, Node parent) {
            this.board = board;
            this.parent = parent;
            this.moves = parent == null ? 0 : parent.moves + 1;
            this.manhattan = board.manhattan();
            this.priority = this.moves + this.manhattan;
        }

        public static Comparator<Node> getManhattanComparator() {
            return new Comparator<Node>() {
                public int compare(Node o1, Node o2) {
                    if (o1.priority == o2.priority)
                        return Integer.compare(o1.manhattan, o2.manhattan);
                    else
                        return Integer.compare(o1.priority, o2.priority);
                }
            };
        }

        public static Comparator<Node> getHammingComparator() {
            return new Comparator<Node>() {
                public int compare(Node o1, Node o2) {
                    return Integer
                            .compare(o1.board.hamming() + o1.moves, o2.board.hamming() + o2.moves);
                }
            };
        }
    }

    private int moves;
    private List<Board> solutions;
    private Node solutionNode; // 用來記錄solution的node
    private Boolean isSolvable = null;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("initial board is null.");

        moves = -1;
        solve(initial);
    }

    private void solve(Board initial) {
        MinPQ<Node> pq = new MinPQ<>(Node.getManhattanComparator());
        MinPQ<Node> twinPq = new MinPQ<>(Node.getManhattanComparator());
        pq.insert(new Node(initial, null));
        twinPq.insert(new Node(initial.twin(), null));

        // do A* algorithm search on initial board and twin of initial board
        while (isSolvable == null) {
            aStar(pq, pq.delMin(), false);
            aStar(twinPq, twinPq.delMin(), true);
        }
        // 有解的話, 在依照solutionNode回查路徑
        if (isSolvable) {
            solutions = new LinkedList<>();
            while (solutionNode != null) {
                solutions.add(0, solutionNode.board);
                solutionNode = solutionNode.parent;
            }
        }
    }

    /**
     * A* algorithm 主要邏輯
     *
     * @param pq          A*用的 priority queue
     * @param currentNode 目前所要檢查的Game Tree Node
     * @param isTwin      是否為twin的搜尋
     */
    private void aStar(MinPQ<Node> pq, Node currentNode, boolean isTwin) {
        Board board = currentNode.board;
        if (board.isGoal()) {
            if (isTwin) {  // 如果是twin解出來, 代表原board不可解
                isSolvable = false;
            }
            else { // 有解
                isSolvable = true;
                moves = currentNode.moves;
                solutionNode = currentNode;
            }
        }
        else {
            for (Board b : board.neighbors()) {
                Board preBoard = (currentNode.parent != null) ? currentNode.parent.board : null;
                if (!b.equals(preBoard)) {
                    Node n = new Node(b, currentNode);
                    pq.insert(n);
                    // showInfo(n);
                }
            }
            // System.out.println("====================");
        }
    }

    private void showInfo(Node node) {
        System.out.println("priority  = " + node.priority);
        System.out.println("moves     = " + node.moves);
        System.out.println("manhattan = " + node.manhattan);
        System.out.println(node.board);
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
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
