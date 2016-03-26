import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;


public class FastCollinearPoints {

    private static final int MIN_SEGMENT_SIZE = 4;
    private LineSegment[] lineSegments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(final Point[] pointsGiven) {
        if (pointsGiven == null) throw new NullPointerException();
        Point[] initialPoints = Arrays.copyOf(pointsGiven, pointsGiven.length);
        Arrays.sort(initialPoints);
        boolean alreadyCollinear = checkNeighborSlopes(initialPoints);
        if (alreadyCollinear && initialPoints.length >= MIN_SEGMENT_SIZE) {
            lineSegments = new LineSegment[] {
                    new LineSegment(initialPoints[0], initialPoints[initialPoints.length-1])
            };
        }
        else {
            fillSegments(initialPoints);
        }
    }

    private void fillSegments(final Point[] initialPoints) {
        Point[] points = Arrays.copyOf(initialPoints, initialPoints.length);
        List<LineSegment> segmentsList = new ArrayList<>();
        for (Point origin : initialPoints) {
            Comparator<Point> comparator = origin.slopeOrder();
            Arrays.sort(points, comparator);
            int pid = 1;
            while (pid < points.length - 1) {
                TreeSet<Point> collinearPoints = new TreeSet<>();
                collinearPoints.add(origin);
                pid = findWhereSegmentBegins(points, comparator, pid);
                while (pid < points.length - 1
                        && comparator.compare(points[pid], points[pid+1]) == 0) {
                    collinearPoints.add(points[pid]);
                    collinearPoints.add(points[++pid]);
                }
                if (collinearPoints.size() >= MIN_SEGMENT_SIZE) {
                    Point first = collinearPoints.first();
                    Point last = collinearPoints.last();
                    if (origin == first) {
                        segmentsList.add(new LineSegment(first, last));
                    }
                }
            }
        }
        lineSegments = new LineSegment[segmentsList.size()];
        segmentsList.toArray(lineSegments);
    }

    private int findWhereSegmentBegins(Point[] points,
                                       Comparator<Point> comparator,
                                       int startLooking) {
        int pid = startLooking;
        while (pid < points.length - 1) {
            int relativeOrder = comparator.compare(points[pid], points[++pid]);
            if (relativeOrder == 0) {
                return pid - 1;
            }
        }
        return points.length - 1;
    }

    private boolean checkNeighborSlopes(Point[] points) {
        if (points.length < 2) return false;
        final double referenceSlope = points[0].slopeTo(points[1]);
        if (referenceSlope == Double.NEGATIVE_INFINITY) {
            throw new IllegalArgumentException("Repeated points!");
        }
        boolean allPointsCollinear = true;
        for (int currId = 1; currId < points.length - 1; ++currId) {
            double slope = points[currId].slopeTo(points[currId+1]);
            if (slope == Double.NEGATIVE_INFINITY) {
                throw new IllegalArgumentException("Repeated points!");
            }
            else if (slope != referenceSlope) {
                allPointsCollinear = false;
            }
        }
        return allPointsCollinear;
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(lineSegments, lineSegments.length);
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        StdDraw.setXscale(-20, 32768);
        StdDraw.setYscale(-20, 32768);
        StdDraw.show(0);

        String path = "/home/dizcza/IdeaProjects/Lab3_PatternRecognition/testing/input/input3.txt";
        In input = new In(path);
        int totalPoints = input.readInt();
        Point[] points = new Point[totalPoints];
        for (int pid = 0; pid < totalPoints; ++pid) {
            int x = input.readInt();
            int y = input.readInt();
            points[pid] = new Point(x, y);
        }

//        points = new Point[] {
//                new Point(4, 4),
//                new Point(5, 5),
//                new Point(1, 1),
//        };

        FastCollinearPoints fastCollinearPoints = new FastCollinearPoints(points);
        LineSegment[] segments = fastCollinearPoints.segments();

        for (LineSegment lineSegment : segments) {
            lineSegment.draw();
        }

        StdDraw.show(0);
        System.out.println(Arrays.toString(segments));
        System.out.println("# " + segments.length);
    }
}
