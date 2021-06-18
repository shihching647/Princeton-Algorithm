/* *****************************************************************************
 *  Name: 647
 *  Date:
 *  Description: 這是兩個RectHV的版本
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.List;

public class KdTree2 {

    private static class TreeNode {
        private static final boolean VERTICAL = true;
        private static final boolean HORIZONTAL = false;
        private Point2D point;
        private boolean level;
        private RectHV lefRect, rightRect;
        private TreeNode left, right;

        public TreeNode(Point2D point, boolean level, RectHV rectHV) {
            this.point = point;
            this.level = level;
            if (level == VERTICAL) {
                lefRect = new RectHV(rectHV.xmin(), rectHV.ymin(), point.x(), rectHV.ymax());
                rightRect = new RectHV(point.x(), rectHV.ymin(), rectHV.xmax(), rectHV.ymax());
            }
            else {
                lefRect = new RectHV(rectHV.xmin(), rectHV.ymin(), rectHV.xmax(), point.y());
                rightRect = new RectHV(rectHV.xmin(), point.y(), rectHV.xmax(), rectHV.ymax());
            }
        }

        public void draw() {
            // draw point
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            point.draw();

            // draw line
            StdDraw.setPenRadius();
            Point2D start, end;
            if (level == TreeNode.VERTICAL) {
                StdDraw.setPenColor(StdDraw.RED);
                start = new Point2D(point.x(), lefRect.ymin());
                end = new Point2D(point.x(), lefRect.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                start = new Point2D(lefRect.xmin(), point.y());
                end = new Point2D(lefRect.xmax(), point.y());
            }
            start.drawTo(end);
        }
    }

    private TreeNode root;
    private int size;

    // construct an empty set of points
    public KdTree2() {

    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("args to insert() is null");
        root = insert(p, root, TreeNode.VERTICAL, new RectHV(0, 0, 1, 1));
    }

    private TreeNode insert(Point2D p, TreeNode node, boolean level, RectHV rectHV) {
        if (node == null) {
            size++;
            return new TreeNode(p, level, rectHV);
        }
        int comp = compare(p, node.point, level);
        if (p.equals(node.point)) return node;
        else if (comp < 0) node.left = insert(p, node.left, !level, node.lefRect);
        else node.right = insert(p, node.right, !level, node.rightRect);
        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("args to contains() is null");
        if (isEmpty())
            return false;

        TreeNode node = root;
        boolean level = root.level;
        while (node != null) {
            int comp = compare(p, node.point, level);
            if (p.equals(node.point)) return true;
            else if (comp < 0) node = node.left;
            else node = node.right;
            level = !level;
        }
        return false;
    }

    // level為HORIZONTAL比較y座標, VERTICAL比較x座標
    private int compare(Point2D p1, Point2D p2, boolean level) {
        return level == TreeNode.HORIZONTAL ? Double.compare(p1.y(), p2.y()) :
               Double.compare(p1.x(), p2.x());
    }

    // draw all points to standard draw
    public void draw() {
        if (isEmpty()) return;
        // level order
        Queue<TreeNode> q = new Queue<>();
        q.enqueue(root);
        while (!q.isEmpty()) {
            TreeNode node = q.dequeue();
            node.draw();
            if (node.left != null) q.enqueue(node.left);
            if (node.right != null) q.enqueue(node.right);
        }
    }

    // level order (for debug)
    private void levelOrder() {
        if (isEmpty()) return;
        // level order
        Queue<TreeNode> q = new Queue<>();
        q.enqueue(root);
        while (!q.isEmpty()) {
            TreeNode node = q.dequeue();
            System.out.println(node.point);
            if (node.left != null) q.enqueue(node.left);
            if (node.right != null) q.enqueue(node.right);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("args to range() is null");

        List<Point2D> list = new LinkedList<>();
        range(root, rect, list);
        return list;
    }

    private void range(TreeNode x, RectHV rect, List<Point2D> set) {
        if (x == null) return;
        // 檢查x.point是否在rect裡面
        if (rect.contains(x.point)) set.add(x.point);
        // 左邊subTree對應的rect與搜尋的rect有相交才需往下搜尋
        if (x.lefRect.intersects(rect)) range(x.left, rect, set);
        // 右邊subTree對應的rect與搜尋的rect有相交才需往下搜尋
        if (x.rightRect.intersects(rect)) range(x.right, rect, set);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("args to nearest() is null");
        if (isEmpty())
            return null;

        Point2D minPoint = root.point;
        minPoint = nearest(root, p, minPoint);
        return minPoint;
    }

    private Point2D nearest(TreeNode x, Point2D p, Point2D minPoint) {
        if (x == null) return minPoint;

        // 檢查x.point與p的距離是否小於minPoint與p的距離
        if (x.point.distanceSquaredTo(p) < minPoint.distanceSquaredTo(p))
            minPoint = x.point;
        // 先走靠近p點的那一個subTree (包含p或是距離較小)
        if (x.lefRect.contains(p) || x.lefRect.distanceSquaredTo(p) < x.rightRect
                .distanceSquaredTo(p)) {
            minPoint = nearest(x.left, p, minPoint);
            // 回來時檢查 "minPoint與p的距離" 是否大於等於 "p與另外一邊的矩型的距離" , 滿足才需搜尋另外一個subTree
            if (x.rightRect.distanceSquaredTo(p) <= minPoint.distanceSquaredTo(p))
                minPoint = nearest(x.right, p, minPoint);
        }
        else {
            minPoint = nearest(x.right, p, minPoint);
            if (x.lefRect.distanceSquaredTo(p) <= minPoint.distanceSquaredTo(p))
                minPoint = nearest(x.left, p, minPoint);
        }
        return minPoint;
    }

    public static void main(String[] args) {
        KdTree2 kdTree = new KdTree2();
        kdTree.insert(new Point2D(0.7, 0.2));
        kdTree.insert(new Point2D(0.5, 0.4));
        kdTree.insert(new Point2D(0.2, 0.3));
        kdTree.insert(new Point2D(0.4, 0.7));
        kdTree.insert(new Point2D(0.9, 0.6));
        System.out.println(kdTree.nearest(new Point2D(0.85, 0.28)));
    }
}
