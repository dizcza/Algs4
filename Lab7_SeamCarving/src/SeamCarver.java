import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

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

    private float delta(int x, int y, int dx, int dy) {
        float[] first = new float[3];
        float[] second = new float[3];
        picture.get(x + dx, y + dy).getColorComponents(first);
        picture.get(x - dx, y - dy).getColorComponents(second);
        float distSq = 0.0f;
        for (int i = 0; i < 3; ++i) {
            float diff = second[i] - first[i];
            distSq += diff * diff;
        }
        return distSq;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (isBorder(x, y)) {
            return BORDER_ENERGY;
        }
        float dxSq = delta(x, y, 1, 0);
        float dySq = delta(x, y, 0, 1);

        return 255 * Math.sqrt(dxSq + dySq);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return null;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return null;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {

    }
}