/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

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

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double minEnergy = Double.POSITIVE_INFINITY;
        int[] seam = new int[width];
        for (int y = 1; y < height - 1; y++) {
            minEnergy = findHorizontalSP(y, minEnergy, seam);
        }
        return seam;
    }

    private double findHorizontalSP(int y, double minEnergy, int[] seam) {
        int[] curSeam = new int[width];
        int curX = 1, curY = y;
        double energy = 0;
        curSeam[0] = curY;
        for (int i = 1; i < width - 1; i++) {
            curSeam[i] = curY;
            energy += energy(curX, curY);
            curX++;
            curY = minEnergyY(curX, curY);
        }
        curSeam[width - 1] = curY;
        if (energy < minEnergy) {
            System.arraycopy(curSeam, 0, seam, 0, width);
            return energy;
        }
        return minEnergy;
    }

    // private void show(int[] a) {
    //     for (int i = 0; i < a.length; i++)
    //         System.out.print(a[i] + ",");
    //     System.out.println();
    // }

    // find min energy in {{x, y - 1}, {x, y}, {x, y + 1}}, return y position
    private int minEnergyY(int x, int y) {
        double min = energy(x, y);
        int minY = y;
        if (energy(x, y - 1) < min) {
            min = energy(x, y - 1);
            minY = y - 1;
        }
        if (energy(x, y + 1) < min) {
            minY = y + 1;
        }
        return minY;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double minEnergy = Double.POSITIVE_INFINITY;
        int[] seam = new int[height];
        for (int x = 1; x < width - 1; x++) {
            minEnergy = findVerticalSP(x, minEnergy, seam);
        }
        return seam;
    }

    private double findVerticalSP(int x, double minEnergy, int[] seam) {
        int[] curSeam = new int[height];
        int curX = x, curY = 1;
        double energy = 0;
        curSeam[0] = curX;
        for (int i = 1; i < height - 1; i++) {
            curSeam[i] = curX;
            energy += energy(curX, curY);
            curY++;
            curX = minEnergyX(curX, curY);
        }
        curSeam[height - 1] = curX;
        if (energy < minEnergy) {
            System.arraycopy(curSeam, 0, seam, 0, height);
            return energy;
        }
        return minEnergy;
    }

    // find min energy in {{x - 1, y}, {x, y}, {x + 1, y}}, return x position
    private int minEnergyX(int x, int y) {
        double min = energy(x, y);
        int minX = x;
        if (energy(x - 1, y) < min) {
            min = energy(x - 1, y);
            minX = x - 1;
        }
        if (energy(x + 1, y) < min) {
            minX = x + 1;
        }
        return minX;
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
        checkSeamValid(seam);
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
        checkSeamValid(seam);
        for (int y = 0; y < seam.length; y++) {
            for (int x = seam[y]; x < width - 1; x++) {
                pixels[y][x] = pixels[y][x + 1];
            }
        }
        width--;
    }

    private void checkSeamValid(int[] seam) {
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1)
                throw new IllegalArgumentException("seam is invalid");
        }
    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }
}
