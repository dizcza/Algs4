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
        int r = picture.get(x + 1, y).getRGB();
        int l = picture.get(x - 1, y).getRGB();
        int t = picture.get(x, y - 1).getRGB();
        int b = picture.get(x, y + 1).getRGB();

        float dxSq = 0.0f;
        float dySq = 0.0f;

        for (int bits = 0; bits <= 16; bits+=8) {
            float dx = (r >> bits - l >> bits) & 0xFF;
            float dy = (t >> bits - b >> bits) & 0xFF;
            dxSq += dx * dx;
            dySq += dy * dy;
        }

        float[] right = new float[3];
        float[] left = new float[3];
        float dxComp = 0.0f;
        picture.get(x + 1, y).getColorComponents(right);
        picture.get(x - 1, y).getColorComponents(left);
        for (int i = 0; i < 3; ++i) {
            dxComp += (right[i] - left[i]) * (right[i] - left[i]);
        }

        System.out.println(dxComp - dxSq);

        return Math.sqrt(dxSq + dySq);
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