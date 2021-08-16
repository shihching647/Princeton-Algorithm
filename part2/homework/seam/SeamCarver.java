/* *****************************************************************************
 *  Name: 647
 *  Date: 2021-08-17
 *  Description:
 *  findVerticalSeam() algorithm:
 *  使用Dynamic Programming(其實就是照著Topological order去relax edges, 一樣的概念)
 *
 *  1.
 *  create一個大小一樣的 DP table, 裡面存的是DPNode
 *  (DPNode包含了到該點的最小energy, 該點的位置, 到該點的前一個位置)
 *  由 左 -> 右，由上 -> 下填表
 *  每一個位置(x, y)都是由上一排的相鄰的三個位置(x-1,y-1),(x,y-1),(x + 1,y-1)取最小值得到
 *  由minEnergyX()取得, 需注意的是這裡是比較DPNode的energy的值(累加的結果)
 *  每填入一個DPNode須記錄是如何到達該點的(pre欄位)
 *
 *  2.
 *  填好表後, iterate最後一列, 找到能量最小的值, 然後由DPNode的pre一路追到源頭
 *  並construct seam
 *
 *  findHorizontalSeam()也是一樣的邏輯
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    // Helper class
    private static class DPNode {
        private double enengy;
        private int pre;
        private int pos;

        public DPNode(double energy, int pre, int pos) {
            this.enengy = energy;
            this.pre = pre;
            this.pos = pos;
        }
    }

    private static final double ENERGY_MAX = 1000.0;
    private int[][] pixels;
    private int width;
    private int height;


    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException("argument for SeamCarver() is null");
        width = picture.width();
        height = picture.height();
        pixels = new int[height][width];
        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                pixels[y][x] = picture.getRGB(x, y);
            }
        }
    }

    // current picture
    public Picture picture() {
        Picture pic = new Picture(width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pic.setRGB(x, y, pixels[y][x]);
            }
        }
        return pic;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width)
            throw new IllegalArgumentException("x for energy() is out of range");
        if (y < 0 || y >= height)
            throw new IllegalArgumentException("y for energy() is out of range");
        if (x == 0 || x == width - 1 || y == 0 || y == height - 1)
            return ENERGY_MAX;
        int[] up = getRGBArray(x, y - 1);
        int[] down = getRGBArray(x, y + 1);
        int[] left = getRGBArray(x - 1, y);
        int[] right = getRGBArray(x + 1, y);
        double difX = 0.0, difY = 0.0;
        for (int i = 0; i < up.length; i++) {
            difX += Math.pow(left[i] - right[i], 2);
            difY += Math.pow(up[i] - down[i], 2);
        }
        return Math.sqrt(difX + difY);
    }

    private int[] getRGBArray(int x, int y) {
        return new int[] {
                (pixels[y][x] >> 16) & 0xFF,
                (pixels[y][x] >> 8) & 0xFF,
                pixels[y][x] & 0xFF
        };
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {

        // fill DP table
        DPNode[][] table = new DPNode[height][width];
        // 第一列
        for (int j = 0; j < width; j++) {
            table[0][j] = new DPNode(ENERGY_MAX, j, j);
        }
        // 其他
        for (int y = 1; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int minX = minEnergyX(x, y - 1, table);
                table[y][x] = new DPNode(energy(x, y) + table[y - 1][minX].enengy, minX, x);
            }
        }

        // 在最後一列find minimum energy
        DPNode start = null;
        double minEnergy = Double.POSITIVE_INFINITY;
        for (int i = 0; i < width; i++) {
            double energy = table[height - 1][i].enengy;
            if (minEnergy > energy) {
                minEnergy = energy;
                start = table[height - 1][i];
            }
        }

        // construct seam
        int[] seam = new int[height];
        int i;
        for (i = height - 1; i > 0; i--) {
            seam[i] = start.pos;
            start = table[i - 1][start.pre];
        }
        seam[i] = start.pre;
        return seam;
    }

    // find min energy in DP table {{x - 1, y}, {x, y}, {x + 1, y}}, return x position
    private int minEnergyX(int x, int y, DPNode[][] table) {
        double min = table[y][x].enengy;
        int minX = x;
        if (x - 1 >= 0 && table[y][x - 1].enengy < min) {
            min = table[y][x - 1].enengy;
            minX = x - 1;
        }
        if (x + 1 < width && table[y][x + 1].enengy < min) {
            minX = x + 1;
        }
        return minX;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {

        // fill DP table
        DPNode[][] table = new DPNode[height][width];
        // 第一行
        for (int i = 0; i < height; i++) {
            table[i][0] = new DPNode(ENERGY_MAX, i, i);
        }
        // 其他(上->下, 左->右填表)
        for (int x = 1; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int minY = minEnergyY(x - 1, y, table);
                table[y][x] = new DPNode(energy(x, y) + table[minY][x - 1].enengy, minY, y);
            }
        }

        // 在最後一行find minimum energy
        DPNode start = null;
        double minEnergy = Double.POSITIVE_INFINITY;
        for (int i = 0; i < height; i++) {
            double energy = table[i][width - 1].enengy;
            if (minEnergy > energy) {
                minEnergy = energy;
                start = table[i][width - 1];
            }
        }

        // construct seam
        int[] seam = new int[width];
        int i;
        for (i = width - 1; i > 0; i--) {
            seam[i] = start.pos;
            start = table[start.pre][i - 1];
        }
        seam[i] = start.pre;
        return seam;
    }

    // find min energy in DP table with {{x, y - 1}, {x, y}, {x, y + 1}}, return y position
    private int minEnergyY(int x, int y, DPNode[][] table) {
        double min = table[y][x].enengy;
        int minY = y;
        if (y - 1 >= 0 && table[y - 1][x].enengy < min) {
            min = table[y - 1][x].enengy;
            minY = y - 1;
        }
        if (y + 1 < height && table[y + 1][x].enengy < min) {
            minY = y + 1;
        }
        return minY;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException("argument for removeHorizontalSeam() is null");
        if (height <= 1)
            throw new IllegalArgumentException("height is smaller than 1px");
        if (seam.length != width)
            throw new IllegalArgumentException(
                    "length of seam for removeHorizontalSeam() is invalid");
        checkSeamValid(seam, height - 1);
        for (int x = 0; x < seam.length; x++) {
            for (int y = seam[x]; y < height - 1; y++) {
                pixels[y][x] = pixels[y + 1][x];
            }
        }
        height--;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException("argument for removeVerticalSeam() is null");
        if (width <= 1)
            throw new IllegalArgumentException("width is smaller than 1px");
        if (seam.length != height)
            throw new IllegalArgumentException(
                    "length of seam for removeVerticalSeam() is invalid");
        checkSeamValid(seam, width - 1);
        for (int y = 0; y < seam.length; y++) {
            for (int x = seam[y]; x < width - 1; x++) {
                pixels[y][x] = pixels[y][x + 1];
            }
        }
        width--;
    }

    private void checkSeamValid(int[] seam, int max) {
        boolean isValid = true;
        if (seam[0] < 0 || seam[0] > max) {
            isValid = false;
        }
        for (int i = 0; isValid && i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1 || seam[i + 1] < 0 || seam[i + 1] > max)
                isValid = false;
        }
        if (!isValid)
            throw new IllegalArgumentException("seam is invalid");
    }


    //  unit testing (optional)
    public static void main(String[] args) {

    }
}
