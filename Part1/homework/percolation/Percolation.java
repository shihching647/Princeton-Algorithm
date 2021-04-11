/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid;
    private final int size;
    private final WeightedQuickUnionUF isPercolationUF; //用來判斷是否Percolation(包含上下兩個virtual site)
    private final WeightedQuickUnionUF isFullUF; //用來判斷是否Full(只包上面一個virtual site)
    private int numOfOpenSite;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be a positive integer.");
        size = n;
        grid = new boolean[size + 1][size + 1];
        isPercolationUF = new WeightedQuickUnionUF(
                size * size + 2); // plus virtual site point and bottom site
        isFullUF = new WeightedQuickUnionUF(size * size + 1); // plus virtual top site
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);
        if (!isOpen(row, col)) {
            grid[row][col] = true;
            int[][] neighbors = {
                    { row - 1, col }, { row + 1, col },
                    { row, col - 1 }, { row, col + 1 }
            };
            for (int i = 0; i < neighbors.length; i++) {
                int eachRow = neighbors[i][0];
                int eachCol = neighbors[i][1];
                if (check(eachRow, eachCol) && isOpen(eachRow, eachCol)) {
                    isPercolationUF.union(getUFIndex(row, col), getUFIndex(eachRow, eachCol));
                    isFullUF.union(getUFIndex(row, col), getUFIndex(eachRow, eachCol));
                }
            }
            numOfOpenSite++;
            // both UF should connect top row with virtual top site
            if (row == 1) {
                isPercolationUF.union(getVirtualTopUFIndex(), getUFIndex(row, col));
                isFullUF.union(getVirtualTopUFIndex(), getUFIndex(row, col));
            }
            // only percolationUF should connect bottom row with virtual bottom site
            if (row == size) isPercolationUF.union(getVirtualBottomUFIndex(), getUFIndex(row, col));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        boolean isConnectedWithTopVirtualSite =
                isFullUF.find(getVirtualTopUFIndex()) == isFullUF
                        .find(getUFIndex(row, col));
        return isConnectedWithTopVirtualSite;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOfOpenSite;
    }

    // does the system percolate?
    public boolean percolates() {
        return isPercolationUF.find(getVirtualTopUFIndex()) == isPercolationUF
                .find(getVirtualBottomUFIndex());
    }

    private void validate(int row, int col) {
        if (row > size || row < 1 || col > size || col < 1)
            throw new IllegalArgumentException("row and column must in range 1 ~ " + size);
    }

    private boolean check(int row, int col) {
        if (row > size || row < 1 || col > size || col < 1)
            return false;
        return true;
    }

    private int getUFIndex(int row, int col) {
        return (row - 1) * size + (col - 1);
    }

    private int getVirtualTopUFIndex() {
        return size * size;
    }

    private int getVirtualBottomUFIndex() {
        return size * size + 1;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation per = new Percolation(10);
        System.out.println(per.check(10, 10));
    }
}
