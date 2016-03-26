import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by dizcza on 08.03.16.
 */
public class TestNearest {

    public static void main(String[] args) {
        int N = 1000;
        KdTree kdTree = new KdTree();
        PointSET pointSET = new PointSET();
        for (int i = 0; i < N; i++) {
            double x = StdRandom.uniform(0.0, 1.0);
            double y = StdRandom.uniform(0.0, 1.0);
            kdTree.insert(new Point2D(x, y));
            pointSET.insert(new Point2D(x, y));
        }
        double x0 = StdRandom.uniform(0.0, 1.0);
        double y0 = StdRandom.uniform(0.0, 1.0);
        Point2D target = new Point2D(x0, y0);

        kdTree.draw();
        StdDraw.setPenRadius(.025);
        StdDraw.setPenColor(StdDraw.CYAN);
        target.draw();
        StdDraw.show();

        Point2D nearestFromSetMutable = pointSET.nearest(target);
        Point2D nearestFromSet = new Point2D(nearestFromSetMutable.x(), nearestFromSetMutable.y());
        Point2D nearestFromTreeMutable = kdTree.nearest(target);
        Point2D nearestFromTree = new Point2D(nearestFromTreeMutable.x(), nearestFromTreeMutable.y());

        kdTree.draw();
        StdDraw.setPenRadius(.02);
        StdDraw.setPenColor(StdDraw.BLUE);
        nearestFromSet.draw();
        StdDraw.setPenRadius(.015);
        StdDraw.setPenColor(StdDraw.RED);
        nearestFromTree.draw();
        StdDraw.show();

        if (!nearestFromSet.equals(nearestFromTree)) {
            throw new RuntimeException("Incorrect nearest.");
        }

        System.out.println("DONE");
    }
}
