import edu.princeton.cs.algs4.Picture;

import java.util.Objects;

public class SeamCarver {

    private final static double BORDER_ENERGY = 1000d;

    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    private boolean isBorder(int x, int y) {
        return x == 0 || x == width() - 1 || y == 0 || y == height() - 1;
    }

    private float[] rgb(int x, int y) {
        float[] flatten = new float[3];
        picture.get(x, y).getColorComponents(flatten);
        return flatten;
    }

    private double distSq(int x0, int y0, int x1, int y1) {
        float[] first = rgb(x0, y0);
        float[] second = rgb(x1, y1);
        float distSq = 0.0f;
        for (int i = 0; i < 3; ++i) {
            distSq += (second[i] - first[i]) * (second[i] - first[i]);
        }
        return distSq;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        verifyIndices(x, y);
        if (isBorder(x, y)) {
            return BORDER_ENERGY;
        }
        double dxSq = distSq(x-1, y, x+1, y);
        double dySq = distSq(x, y-1, x, y+1);

        return 255 * Math.sqrt(dxSq + dySq);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return null;
    }

    private void verifyIndices(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y>= height()) {
            throw new IndexOutOfBoundsException();
        }
    }

    private double[][] prepareDistTo() {
        double[][] distTo = new double[height() - 1][width()];
        for (int x = 0; x < width(); ++x) {
            distTo[0][x] = BORDER_ENERGY;
        }
        for (int y = 1; y < height() - 1; ++y) {
            for (int x = 0; x < width(); ++x) {
                distTo[y][x] = Double.POSITIVE_INFINITY;
            }
        }
        return distTo;
    }

    private int[][] preparePathTo() {
        int[][] pathTo = new int[height() - 1][width()];
        for (int x = 0; x < width(); ++x) {
            pathTo[0][x] = x;
        }
        return pathTo;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[][] pathTo = preparePathTo();
        double[][] distTo = prepareDistTo();
        for (int y = 0; y < height() - 2; ++y) {
            for (int x = 1; x < width() - 1; ++x) {
                for (int dx = -1; dx <= 1; ++dx) {
                    double e = energy(x + dx, y + 1);
                    if (distTo[y][x] + e < distTo[y+1][x+dx]) {
                        distTo[y+1][x+dx] = distTo[y][x] + e;
                        pathTo[y+1][x+dx] = x;
                    }
                }
            }
        }
        double minDist = Double.POSITIVE_INFINITY;
        int xEnd = 0;
        for (int x = 0; x < width(); ++x) {
            if (distTo[height() - 2][x] < minDist) {
                minDist = distTo[height() - 2][x];
                xEnd = x;
            }
        }
        int[] seam = new int[height()];
        seam[height() - 1] = xEnd;
        seam[height() - 2] = xEnd;
        for (int y = height() - 3; y > 0; --y) {
            seam[y] = pathTo[y+1][seam[y+1]];
        }
        seam[0] = seam[1];
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        Objects.requireNonNull(seam);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        Objects.requireNonNull(seam);
    }
}