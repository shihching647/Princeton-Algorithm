/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {

    private static final int NUMBER_OF_POINT = 4;
    private LineSegment[] segments;
    private int numberOfSegments = 0;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        check(points); // check for null element
        int n = points.length;
        // According to FAQ Enrichment part - Q3: you will need quadratic space in the worst case.
        segments = new LineSegment[n * n];

        // if points[] only contains one element
        if (points.length <= 1) {
            segments = new LineSegment[0]; // 回傳長度為0的陣列, 而不是null
            return;
        }

        // sort points (You can't mutate the input array)
        Point[] copyPoints = points.clone();
        Arrays.sort(copyPoints);
        checkDuplicate(copyPoints); // check for duplicate elements

        Point[] aux = new Point[n];   // points sort by slope related to p
        Point[] temp = new Point[n];  // points with same slope related to p, temp[0] = p

        for (int i = 0; i < n; i++) {
            Point p = copyPoints[i];
            System.arraycopy(copyPoints, 0, aux, 0, n);

            // sorted by slope to p
            Arrays.sort(aux, p.slopeOrder());

            // sort後, 此時aux[0]必定為p, 因為 p.slopeTo(p) = -INFINITE

            // init temp[0] to p, temp[1] to aux[1]
            temp[0] = p;
            temp[1] = aux[1];
            int count = 2; // 目前temp[]裡的有效元素個數
            double slope = p.slopeTo(aux[1]);
            boolean needCheck = false;

            for (int j = 2; j < aux.length; j++) {
                Point currentPoint = aux[j];
                double currentSlope = p.slopeTo(currentPoint);
                if (currentSlope == slope) { // 因為x, y介於-32,767~32,767, 所以可以直接比較兩個double
                    temp[count++] = currentPoint;
                    if (j == aux.length - 1) {
                        needCheck = true; // 最後一個element要檢查一次
                        j++; // 因為下面檢查是檢查 aux[j - 1], 故j要在此+1
                    }
                }
                else {
                    // 斜率不同時就要檢查
                    needCheck = true;
                }

                // 檢查p, aux[j - 1] 是否該被該被加入到segments[]內
                // 假設線段 A -> B -> C -> D (由小到大排列)共線
                // 因為copyPoints[]已經sort過, 且aux[]也sort過, 且Arrays.sort()為stable, 故
                // 當p == A時, temp[] = {A, B, C, D}
                // 當p == B時, temp[] = {B, A, C, D}
                // 故只有當temp[]是排序好的時候, 才需加入segments[]內
                if (needCheck) {
                    if (count >= NUMBER_OF_POINT) {
                        // 只要temp[0] < temp[1]時才代表, 此時temp[0](也就是p)是最小的端點
                        if (less(temp[0], temp[1])) {
                            // System.out.println("找到: " + p + ", " + aux[j - 1]);
                            segments[numberOfSegments++] = new LineSegment(p, aux[j - 1]);
                        }
                    }
                    // reset temp[0] to p, temp[1] to current point, slope to current slope
                    temp[0] = p;
                    temp[1] = currentPoint;
                    count = 2;
                    slope = currentSlope;
                    needCheck = false;
                }
                // System.out.println(count);
            }
        }
        // 把segments[]轉換為長度為numberOfSegments的陣列
        normalizeResult();
    }

    private void normalizeResult() {
        LineSegment[] result = new LineSegment[numberOfSegments];
        for (int i = 0; i < result.length; i++) {
            result[i] = segments[i];
        }
        segments = result;
    }


    // check arguments is legal or not
    private void check(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException("points array is null.");
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException("points contain null.");
        }
    }

    // check points contain duplicate points
    private void checkDuplicate(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            Point p1 = points[i];
            Point p2 = points[i + 1];
            if (!less(p1, p2))
                throw new IllegalArgumentException("points contain duplicate points.");
        }
    }

    private boolean less(Point p1, Point p2) {
        return p1.compareTo(p2) < 0;
    }

    // the number of line segments
    public int numberOfSegments() {
        return numberOfSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.clone(); // 不能直接回傳segments, 因為這樣會被修改
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        // System.out.println("count = " + collinear.numberOfSegments);
        System.out.println(Arrays.toString(collinear.segments()));
    }
}
