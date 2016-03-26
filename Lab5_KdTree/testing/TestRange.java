import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by dizcza on 08.03.16.
 */
public class TestRange {

    public static void main(String[] args) {
        int N = 1000;
        KdTree kdTree = new KdTree();
        PointSET pointSET = new PointSET();
        for (int i = 0; i < N; i++) {
            double x = StdRandom.uniform(0.0, 1.0);
            double y = StdRandom.uniform(0.0, 1.0);
            kdTree.insert(new Point2D(x, y));
            kdTree.insert(new Point2D(x, y));
            pointSET.insert(new Point2D(x, y));
        }

        if (kdTree.size() != pointSET.size()) {
            throw new RuntimeException("Incorrect range.");
        }

        double x0 = StdRandom.uniform(0.0, 1.0);
        double y0 = StdRandom.uniform(0.0, 1.0);
        double x1 = StdRandom.uniform(x0, 1.0);
        double y1 = StdRandom.uniform(y0, 1.0);
//        double x0 = 0.1;
//        double y0 = 0.1;
//        double x1 = 0.8;
//        double y1 = 0.8;
        RectHV rect = new RectHV(x0, y0, x1, y1);

        kdTree.draw();
        StdDraw.setPenRadius();
        StdDraw.setPenColor(StdDraw.MAGENTA);
        rect.draw();
        StdDraw.show();

        assert pointSET.size() == kdTree.size();

        long start = System.nanoTime();
        Iterable<Point2D> pointsInsideSet = pointSET.range(rect);
        double bruteDuration = (System.nanoTime() - start) / (double) pointSET.size();
        start = System.nanoTime();
        Iterable<Point2D> pointsInsideTree = kdTree.range(rect);
        double treeDuration = System.nanoTime() - start;

        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(.02);
        int inTree = 0;
        for (Point2D p : pointsInsideTree) {
            p.draw();
            inTree++;
        }
        treeDuration /= (inTree + Math.log(kdTree.size()) / Math.log(2));
        StdDraw.setPenRadius();
        StdDraw.setPenColor(StdDraw.MAGENTA);
        rect.draw();

        StdDraw.setPenRadius(.01);
        StdDraw.setPenColor(StdDraw.BLUE);
        int inSet = 0;
        for (Point2D p : pointsInsideSet) {
            p.draw();
            inSet++;
        }

        if (inSet - inTree != 0) {
            throw new RuntimeException("Incorrect range.");
        }

        StdDraw.show();
        System.out.println(String.format("%f | %f", bruteDuration, treeDuration));
        System.out.println("DONE");
    }
}
