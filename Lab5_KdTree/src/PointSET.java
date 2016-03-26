import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public class PointSET {

    private final Set<Point2D> points = new TreeSet<>();

    // construct an empty set of points
    public PointSET() {

    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        Objects.requireNonNull(p);
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        Objects.requireNonNull(p);
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        for (Point2D p : points) {
            p.draw();
        }
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        Objects.requireNonNull(rect);
        Stack<Point2D> container = new Stack<>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                container.add(p);
            }
        }
        return container;
    }

    // a nearest neighbor in the set to point p;
    // null if the set is empty
    public Point2D nearest(Point2D target) {
        Objects.requireNonNull(target);
        Point2D nearestPoint = null;
        double minDistSq = Double.POSITIVE_INFINITY;
        for (Point2D p : points) {
            double distSq = p.distanceSquaredTo(target);
            if (distSq < minDistSq) {
                minDistSq = distSq;
                nearestPoint = p;
            }
        }
        return nearestPoint;
    }

    public static void main(String[] args) {

    }
}