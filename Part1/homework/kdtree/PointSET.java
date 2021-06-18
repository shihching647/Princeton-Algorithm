/* *****************************************************************************
 *  Name: 647
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> set;

    // construct an empty set of points
    public PointSET() {
        set = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("args to insert() is null");
        set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("args to contains() is null");
        return set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : set) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("args to range() is null");
        TreeSet<Point2D> result = new TreeSet<>();
        for (Point2D p : set) {
            if (rect.contains(p))
                result.add(p);
        }
        return result;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("args to nearest() is null");

        double min = Double.POSITIVE_INFINITY;
        Point2D minPoint = null;
        for (Point2D point : set) {
            double distance = point.distanceSquaredTo(p);
            if (distance < min) {
                min = distance;
                minPoint = point;
            }
        }
        return minPoint;
    }

    public static void main(String[] args) {

    }
}
