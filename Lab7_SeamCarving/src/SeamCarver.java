import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.Objects;

public class SeamCarver {

    private final static double BORDER_ENERGY = 1000d;

    private Picture picture;
    private boolean horizontal = false;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
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

    private double[] rgb(int x, int y) {
        double[] flatten = new double[3];
        Color color = picture.get(x, y);
        flatten[0] = color.getRed();
        flatten[1] = color.getGreen();
        flatten[2] = color.getBlue();
        return flatten;
    }

    private double distSq(int x0, int y0, int x1, int y1) {
        double[] first = rgb(x0, y0);
        double[] second = rgb(x1, y1);
        double distSq = 0.0d;
        for (int i = 0; i < 3; ++i) {
            distSq += (second[i] - first[i]) * (second[i] - first[i]);
        }
        return distSq;
    }

    private void transpose() {
        Picture transposed = new Picture(height(), width());
        for (int x = 0; x < width(); ++x) {
            for (int y = 0; y < height(); ++y) {
                transposed.set(y, x, picture.get(x, y));
            }
        }
        picture = transposed;
        horizontal = !horizontal;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        verifyIndices(x, y);
        if (isBorder(x, y)) {
            return BORDER_ENERGY;
        }
        double dxSq = distSq(x-1, y, x+1, y);
        double dySq = distSq(x, y-1, x, y+1);

        return Math.sqrt(dxSq + dySq);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (!horizontal) {
            transpose();
        }
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }

    private void verifyIndices(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y>= height()) {
            throw new IndexOutOfBoundsException();
        }
    }

    private double[][] prepareDistTo() {
        double[][] distTo = new double[height()][width()];
        for (int x = 0; x < width(); ++x) {
            distTo[0][x] = BORDER_ENERGY;
        }
        for (int y = 1; y < height(); ++y) {
            for (int x = 0; x < width(); ++x) {
                distTo[y][x] = Double.POSITIVE_INFINITY;
            }
        }
        return distTo;
    }

    private int[][] preparePathTo() {
        int[][] pathTo = new int[height()][width()];
        for (int x = 0; x < width(); ++x) {
            pathTo[0][x] = x;
            pathTo[height() - 1][x] = x;
        }
        return pathTo;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[][] pathTo = preparePathTo();
        double[][] distTo = prepareDistTo();
        for (int y = 0; y < height() - 1; ++y) {
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
            if (distTo[height() - 1][x] < minDist) {
                minDist = distTo[height() - 1][x];
                xEnd = x;
            }
        }
        int[] seam = new int[height()];
        seam[height() - 1] = xEnd;
        for (int y = height() - 2; y >= 0; --y) {
            seam[y] = pathTo[y+1][seam[y+1]];
        }
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        Objects.requireNonNull(seam);
        if (!horizontal) {
            transpose();
        }
        removeVerticalSeam(seam);
        transpose();
    }

    private void verifySeamLength(int[] seam, int len) {
        if (seam.length != len) {
            throw new IllegalArgumentException();
        }
    }

    private void verifySeamRange(int[] seam, int maxElement) {
        for (int coord : seam) {
            if (coord < 0 || coord > maxElement) {
                throw new IllegalArgumentException();
            }
        }
        for (int i = 1; i < seam.length; ++i) {
            int shift = seam[i] - seam[i-1];
            if (shift < -1 || shift > 1) {
                throw new IllegalArgumentException();
            }
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        Objects.requireNonNull(seam);
        verifySeamLength(seam, height());
        verifySeamRange(seam, width() - 1);
        Picture shrunk = new Picture(width() - 1, height());
        for (int y = 0; y < height(); ++y) {
            for (int x = 0; x < seam[y]; ++x) {
                shrunk.set(x, y, picture.get(x, y));
            }
            for (int x = seam[y]; x < width() - 1; ++x) {
                shrunk.set(x, y, picture.get(x + 1, y));
            }
        }
        picture = shrunk;
    }
}