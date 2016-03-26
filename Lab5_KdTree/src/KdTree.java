import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Objects;
import java.util.Stack;


public class KdTree {
    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        private Node(Point2D p, Node parent, boolean parentOrient) {
            this.p = p;
            if (parent == null) {
                rect = new RectHV(0d, 0d, 1d, 1d);
            }
            else {
                if (parentOrient == VERTICAL) {
                    if (p.x() < parent.p.x()) {
                        rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.p.x(), parent.rect.ymax());
                    }
                    else {
                        rect = new RectHV(parent.p.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
                    }
                }
                else {
                    if (p.y() < parent.p.y()) {
                        rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.p.y());
                    }
                    else {
                        rect = new RectHV(parent.rect.xmin(), parent.p.y(), parent.rect.xmax(), parent.rect.ymax());
                    }
                }
            }
        }

        private void draw(boolean orientation) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            p.draw();
            StdDraw.setPenRadius();
            if (orientation == VERTICAL) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(p.x(), rect.ymin(), p.x(), rect.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(rect.xmin(), p.y(), rect.xmax(), p.y());
            }
        }
    }

    private Node root;
    private double minDistSq = Double.POSITIVE_INFINITY;
    private int totalPoints = 0;

    // construct an empty KdTree
    public KdTree() {

    }

    // is the KdTree empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the KdTree
    public int size() {
        return totalPoints;
    }

    // add the point to the KdTree (if it is not already in the set)
    public void insert(Point2D newPoint) {
        Objects.requireNonNull(newPoint);
        if (root == null) {
            root = new Node(newPoint, null, VERTICAL);
        }
        else {
            Node insertionPlace = root;
            Node parent = null;
            boolean parentOrient = HORIZONTAL;
            while (insertionPlace != null) {
                parent = insertionPlace;
                parentOrient = !parentOrient;
                if (newPoint.equals(insertionPlace.p)) {
                    return;
                }
                insertionPlace = next(newPoint, insertionPlace, parentOrient);
            }

            Node tail = new Node(newPoint, parent, parentOrient);
            if (isSmaller(newPoint, parent, parentOrient)) {
                parent.lb = tail;
            }
            else {
                parent.rt = tail;
            }
        }
        totalPoints++;
    }

    private static boolean isSmaller(Point2D p, Node node, boolean parentOrient) {
        if (parentOrient == VERTICAL) {
            return p.x() < node.p.x();
        }
        else {
            return p.y() < node.p.y();
        }
    }

    // does the KdTree contain point p?
    public boolean contains(Point2D targetPoint) {
        Objects.requireNonNull(targetPoint);
        Node possibleNode = root;
        boolean parentOrient = HORIZONTAL;
        while (possibleNode != null) {
            if (targetPoint.equals(possibleNode.p)) return true;
            parentOrient = !parentOrient;
            possibleNode = next(targetPoint, possibleNode, parentOrient);
        }
        return false;
    }

    private Node next(Point2D targetPoint, Node node, boolean parentOrient) {
        if (isSmaller(targetPoint, node, parentOrient)) {
            return node.lb;
        }
        else {
            return node.rt;
        }
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, VERTICAL);
    }

    private void draw(Node node, boolean orientation) {
        if (node == null) return;
        node.draw(orientation);
        draw(node.lb, !orientation);
        draw(node.rt, !orientation);
    }


    private void fill(Stack<Point2D> collection, final RectHV rect, Node node) {
        if (rect.contains(node.p)) {
            collection.add(node.p);
        }
        if (node.lb != null && rect.intersects(node.lb.rect)) {
            fill(collection, rect, node.lb);
        }
        if (node.rt != null && rect.intersects(node.rt.rect)) {
            fill(collection, rect, node.rt);
        }
    }
    
    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        Objects.requireNonNull(rect);
        Stack<Point2D> collection = new Stack<>();
        if (root != null) {
            fill(collection, rect, root);
        }
        return collection;
    }


    private Point2D nearest(final Point2D target, Node node, Point2D closest) {
        if (node == null || node.rect.distanceSquaredTo(target) > minDistSq) return closest;

        // update min dist
        double distSq = target.distanceSquaredTo(node.p);
        if (distSq < minDistSq) {
            minDistSq = distSq;
            closest = node.p;
        }

        final double lbDistSq, rtDistSq;
        if (node.lb == null) {
            lbDistSq = Double.POSITIVE_INFINITY;
        }
        else {
            lbDistSq = node.lb.rect.distanceSquaredTo(target);
        }
        if (node.rt == null) {
            rtDistSq = Double.POSITIVE_INFINITY;
        }
        else {
            rtDistSq = node.rt.rect.distanceSquaredTo(target);
        }

        final Node first, second;
        if (lbDistSq < rtDistSq) {
            first = node.lb;
            second = node.rt;
        }
        else {
            first = node.rt;
            second = node.lb;
        }

        closest = nearest(target, first, closest);
        closest = nearest(target, second, closest);

        return closest;
    }

    // a nearest neighbor in the set to point p;
    // null if the set is empty
    public Point2D nearest(Point2D target) {
        Objects.requireNonNull(target);
        minDistSq = Double.POSITIVE_INFINITY;
        return nearest(target, root, null);
    }


    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(0.8, 0.6));
        kdTree.insert(new Point2D(0.4, 0.7));
        kdTree.insert(new Point2D(0.3, 0.2));
        kdTree.insert(new Point2D(0.5, 0.8));
        kdTree.insert(new Point2D(0.4, 0.7));

        kdTree.draw();
        StdDraw.show();

        System.out.println(kdTree.size());
    }

}
