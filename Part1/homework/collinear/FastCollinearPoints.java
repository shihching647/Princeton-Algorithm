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
    private int numberOfSegments;


    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException("points array is null.");

        int n = points.length;
        segments = new LineSegment[n];

        // sort points
        Arrays.sort(points);
        System.out.println(Arrays.toString(points));

        // check points
        for (int i = 0; i < n - 1; i++) {
            Point p1 = points[i];
            Point p2 = points[i + 1];
            if (p1 == null || p2 == null)
                throw new IllegalArgumentException("points contain null.");
            if (!less(p1, p2))
                throw new IllegalArgumentException("points contain duplicate points.");
        }

        Point[] aux = new Point[points.length - 1];
        for (int i = 0; i < n; i++) {
            Point p = points[i];
            for (int j = i + 1, k = 0; j < n; j++) {
                aux[k++] = points[j];
            }
            System.out.println(Arrays.toString(aux));
            Arrays.sort(aux, p.slopeOrder());

            // Point minPoint = aux[0];
            double maxSlope = p.slopeTo(aux[0]);
            int count = 1;
            for (int j = 1; i + j < aux.length - 1; j++) {
                System.out.println(count);
                Point currentPoint = aux[j];
                double slope = p.slopeTo(currentPoint);
                if (slope == maxSlope) count++;
                else {
                    if (count >= NUMBER_OF_POINT - 1) {
                        System.out.println(p);
                        segments[numberOfSegments++] = new LineSegment(p, aux[j - 1]);
                    }
                    // minPoint = currentPoint;
                    maxSlope = slope;
                    count = 0;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
