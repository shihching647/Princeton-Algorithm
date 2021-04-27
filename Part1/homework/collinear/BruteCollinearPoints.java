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
    private int numberOfSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException("points array is null.");

        int n = points.length;
        segments = new LineSegment[n];

        // sort points
        Arrays.sort(points);

        // check points
        for (int i = 0; i < n - 1; i++) {
            Point p1 = points[i];
            Point p2 = points[i + 1];
            if (p1 == null || p2 == null)
                throw new IllegalArgumentException("points contain null.");
            if (!less(p1, p2))
                throw new IllegalArgumentException("points contain duplicate points.");
        }

        for (int p = 0; p < n - 3; p++) {
            for (int q = p + 1; q < n - 2; q++) {
                for (int r = q + 1; r < n - 1; r++) {
                    if (points[p].slopeTo(points[q]) == points[q].slopeTo(points[r])) {
                        for (int s = r + 1; s < n; s++) {
                            if (points[r].slopeTo(points[s]) == points[p].slopeTo(points[q])) {
                                segments[numberOfSegments++] = new LineSegment(points[p],
                                                                               points[s]);
                                System.out.println(new LineSegment(points[p], points[s]));
                            }
                        }
                    }
                }
            }
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
        LineSegment[] result = new LineSegment[numberOfSegments];
        for (int i = 0; i < result.length; i++) {
            result[i] = segments[i];
        }
        return result;
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
