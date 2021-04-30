/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {

    private LineSegment[] segments;
    private int numberOfSegments = 0;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        check(points);

        int n = points.length;
        segments = new LineSegment[n * n];

        // if points[] only contains one element
        if (points.length <= 1) {
            segments = new LineSegment[0];  // 回傳長度為0的陣列, 而不是null
            return;
        }

        // sort points
        Point[] copyPoints = points.clone();
        Arrays.sort(copyPoints);
        checkDuplicate(copyPoints);

        for (int p = 0; p < n - 3; p++) {
            for (int q = p + 1; q < n - 2; q++) {
                for (int r = q + 1; r < n - 1; r++) {
                    if (copyPoints[p].slopeTo(copyPoints[q]) == copyPoints[q]
                            .slopeTo(copyPoints[r])) {
                        for (int s = r + 1; s < n; s++) {
                            if (copyPoints[r].slopeTo(copyPoints[s]) == copyPoints[p]
                                    .slopeTo(copyPoints[q])) {
                                segments[numberOfSegments++] = new LineSegment(copyPoints[p],
                                                                               copyPoints[s]);
                                // System.out.println(new LineSegment(copyPoints[p], copyPoints[s]));
                            }
                        }
                    }
                }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
