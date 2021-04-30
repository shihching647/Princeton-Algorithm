/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints_backup {

    private static final int NUMBER_OF_POINT = 4;
    private LineSegment[] segments;
    private int numberOfSegments;


    // finds all line segments containing 4 or more points
    public FastCollinearPoints_backup(Point[] points) {
        check(points);

        int n = points.length;
        segments = new LineSegment[9000];

        // sort points
        Point[] copyPoints = points.clone();
        Arrays.sort(copyPoints);
        checkDuplicate(copyPoints);

        Point[] aux = new Point[n - 1];
        Point preDestination = copyPoints[0];
        double preSlope = preDestination.slopeTo(preDestination);

        for (int i = 0; i < n; i++) {
            Point p = copyPoints[i];
            for (int j = i + 1, k = 0; j < n; j++) {
                aux[k++] = copyPoints[j];
            }
            // System.out.println("aux[] = " + Arrays.toString(aux));
            Arrays.sort(aux, 0, n - 1 - i, p.slopeOrder());
            // System.out.println("aux[] = " + Arrays.toString(aux));
            // Point minPoint = aux[0];
            double maxSlope = p.slopeTo(aux[0]);
            // System.out.println("point = " + aux[0] + "maxSlope = " + maxSlope);
            int count = 1;
            int j;
            double slope = 0;
            for (j = 1; i + j < n - 1; j++) {
                Point currentPoint = aux[j];
                slope = p.slopeTo(currentPoint);
                // System.out.println("slope = " + slope);
                // if (slope == preSlope) continue;
                if (slope == maxSlope) count++;
                else {
                    if (count >= NUMBER_OF_POINT - 1
                            && (preDestination.compareTo(aux[j - 1]) != 0 || slope != preSlope)) {
                        // System.out.println("找到: " + p + ", " + aux[j - 1]);
                        segments[numberOfSegments++] = new LineSegment(p, aux[j - 1]);
                    }
                    // minPoint = currentPoint;
                    maxSlope = slope;
                    count = 1;
                }
                // System.out.println(count);
            }
            if (count >= NUMBER_OF_POINT - 1 && (preDestination.compareTo(aux[j - 1]) != 0
                    || slope != preSlope)) {
                // System.out.println("找到: " + p + ", " + aux[j - 1]);
                segments[numberOfSegments++] = new LineSegment(p, aux[j - 1]);
                preDestination = aux[j - 1];
                preSlope = maxSlope;
                // i += count - 1;
            }
        }
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
        FastCollinearPoints_backup collinear = new FastCollinearPoints_backup(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        // System.out.println("count = " + collinear.numberOfSegments);
    }
}
