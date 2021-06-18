/* *****************************************************************************
 *  Name: 647
 *  Date:
 *  Description:
 *  這是一個RectHV的版本, 與兩個RectHV的版本只差在 getLeftRect(),
 *  getRightRect()兩個方法, 沒有left就new一個新的RectHV給他
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.List;

public class KdTree {

    private static class TreeNode {
        // 常數與memory usage無關
        private static final boolean VERTICAL = true;
        private static final boolean HORIZONTAL = false;
        private Point2D point;
        private boolean level;
        private RectHV rect;
        private TreeNode left, right;

        public TreeNode(Point2D point, boolean level, RectHV rect) {
            this.point = point;
            this.level = level;
            this.rect = rect;
        }

        public RectHV getLeftRect() {
            // 若有left node直接回傳
            if (left != null)
                return left.rect;

            if (level == TreeNode.VERTICAL)
                return new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax());
            else
                return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
        }

        public RectHV getRightRect() {
            // 若有right node直接回傳
            if (right != null)
                return right.rect;

            if (level == TreeNode.VERTICAL)
                return new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax());
            else
                return new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
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
                start = new Point2D(point.x(), rect.ymax());
                end = new Point2D(point.x(), rect.ymin());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                start = new Point2D(rect.xmin(), point.y());
                end = new Point2D(rect.xmax(), point.y());
            }
            start.drawTo(end);
        }
    }

    private TreeNode root;
    private int size;

    // construct an empty set of points
    public KdTree() {

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
        if (root == null) {
            root = new TreeNode(p, TreeNode.VERTICAL, new RectHV(0, 0, 1, 1));
            size = 1;
        }
        else
            root = insert(p, root, TreeNode.VERTICAL, root.rect);
    }

    private TreeNode insert(Point2D p, TreeNode node, boolean level, RectHV rectHV) {
        if (node == null) {
            size++;
            return new TreeNode(p, level, rectHV);
        }
        int comp = compare(p, node.point, level);
        if (p.equals(node.point)) return node;
        else if (comp < 0) node.left = insert(p, node.left, !level, node.getLeftRect());
        else node.right = insert(p, node.right, !level, node.getRightRect());
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
        if (x.left != null && x.left.rect.intersects(rect)) range(x.left, rect, set);
        // 右邊subTree對應的rect與搜尋的rect有相交才需往下搜尋
        if (x.right != null && x.right.rect.intersects(rect)) range(x.right, rect, set);
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
        if (x.getLeftRect().contains(p) ||
                x.getLeftRect().distanceSquaredTo(p) < x.getRightRect().distanceSquaredTo(p)) {
            minPoint = nearest(x.left, p, minPoint);
            // 回來時檢查 "minPoint與p的距離" 是否大於等於 "p與另外一邊的矩型的距離" , 滿足才需搜尋另外一個subTree
            if (x.getRightRect().distanceSquaredTo(p) <= minPoint.distanceSquaredTo(p))
                minPoint = nearest(x.right, p, minPoint);
        }
        else {
            minPoint = nearest(x.right, p, minPoint);
            if (x.getLeftRect().distanceSquaredTo(p) <= minPoint.distanceSquaredTo(p))
                minPoint = nearest(x.left, p, minPoint);
        }
        return minPoint;
    }

    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        // A  0.372 0.497
        // B  0.564 0.413
        // C  0.226 0.577
        // D  0.144 0.179
        // E  0.083 0.51
        // F  0.32 0.708
        // G  0.417 0.362
        // H  0.862 0.825
        // I  0.785 0.725
        // J  0.499 0.208
        kdTree.insert(new Point2D(0.372, 0.497));
        kdTree.insert(new Point2D(0.564, 0.413));
        kdTree.insert(new Point2D(0.226, 0.577));
        kdTree.insert(new Point2D(0.144, 0.179));
        kdTree.insert(new Point2D(0.083, 0.51));
        kdTree.insert(new Point2D(0.32, 0.708));
        kdTree.insert(new Point2D(0.417, 0.362));
        kdTree.insert(new Point2D(0.862, 0.825));
        kdTree.insert(new Point2D(0.785, 0.725));
        kdTree.insert(new Point2D(0.499, 0.208));
        System.out.println(kdTree.nearest(new Point2D(0.26, 0.1)));
    }
}
