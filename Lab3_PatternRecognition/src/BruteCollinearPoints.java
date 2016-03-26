import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private final LineSegment[] lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(final Point[] pointsGiven) {
        if (pointsGiven == null) throw new NullPointerException();
        Point[] points = Arrays.copyOf(pointsGiven, pointsGiven.length);
        Arrays.sort(points);
        checkIfNotRepeated(points);
        List<LineSegment> segments = new ArrayList<>();
        for (int p0 = 0; p0 < points.length - 3; ++p0) {
            for (int p1 = p0 + 1; p1 < points.length - 2; ++p1) {
                for (int p2 = p1 + 1; p2 < points.length - 1; ++p2) {
                    for (int p3 = p2 + 1; p3 < points.length; ++p3) {
                        if (areCollinear(points, p0, p1, p2, p3)) {
                            segments.add(new LineSegment(points[p0], points[p3]));
                        }
                    }
                }
            }
        }
        lineSegments = new LineSegment[segments.size()];
        segments.toArray(lineSegments);
    }

    private void checkIfNotRepeated(Point[] points) {
        for (int currId = 0; currId < points.length - 1; ++currId) {
            if (points[currId].compareTo(points[currId+1]) == 0) {
                throw new IllegalArgumentException("Repeated points!");
            }
        }
    }

    private boolean areCollinear(final Point[] points, final int... interestedIds) {
        Point p0 = points[interestedIds[0]];
        Point p1 = points[interestedIds[1]];
        double referenceSlope = p0.slopeTo(p1);
        for (int pid = 2; pid < interestedIds.length; ++pid) {
            Point other = points[interestedIds[pid]];
            double thisSlope = p0.slopeTo(other);
            if (thisSlope != referenceSlope) {
                return false;
            }
        }
        return true;
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

        BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(points);
        LineSegment[] segments = bruteCollinearPoints.segments();

        for (LineSegment lineSegment : segments) {
            lineSegment.draw();
        }

        StdDraw.show(0);
        System.out.println(Arrays.toString(segments));
        System.out.println("# " + segments.length);
    }
}
