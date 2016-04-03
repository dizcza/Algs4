import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.Iterator;
import java.util.Objects;

public class SeamCarverLinked {

    private final static double BORDER_ENERGY = 1000d;

    private class Node {
        private Node top;
        private Node right;
        private Node bottom;
        private Node left;
        private Color color;
        private double energy;

        private Node(Color color) {
            this.color = color;
        }

        private void bind(int x, int y) {
            top = getNode(x, y - 1);
            right = getNode(x + 1, y);
            bottom = getNode(x, y + 1);
            left = getNode(x - 1, y);
        }

        private boolean isBorder() {
            return top == null || right == null || bottom == null || left == null;
        }
    }

    private Node getNode(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }
        else {
            return nodes[y][x];
        }
    }

    private Picture picture;
    private boolean horizontal = false;
    private Node[][] nodes;

    private int width = 0;
    private int height = 0;

    // create a seam carver object based on the given picture
    public SeamCarverLinked(Picture picture) {
        this.picture = new Picture(picture);
        width = picture.width();
        height = picture.height();
        nodes = new Node[height][width];
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                nodes[y][x] = new Node(picture.get(x, y));
            }
        }
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                nodes[y][x].bind(x, y);
                calcEnergy(nodes[y][x]);
            }
        }
    }

    // current picture
    public Picture picture() {
        Picture pic = new Picture(width, height);
        for (int y = 0; y < height; ++y) {
            int x = 0;
            Node slice = nodes[y][x];
            while (slice != null) {
                pic.set(x++, y, slice.color);
                slice = slice.right;
            }
        }
        return pic;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
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
        return nodes[y][x].energy;
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
        if (x < 0 || x >= width || y < 0 || y>= height) {
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
        if (seam.length != len || len < 2) {
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

    private double[] rgb(Node node) {
        double[] flatten = new double[3];
        Color color = node.color;
        flatten[0] = color.getRed();
        flatten[1] = color.getGreen();
        flatten[2] = color.getBlue();
        return flatten;
    }

    private double distSq(Node from, Node to) {
        double[] first = rgb(from);
        double[] second = rgb(to);
        double distSq = 0.0d;
        for (int i = 0; i < 3; ++i) {
            distSq += (second[i] - first[i]) * (second[i] - first[i]);
        }
        return distSq;
    }

    private void calcEnergy(Node node) {
        if (node == null) return;
        if (node.isBorder()) {
            node.energy = BORDER_ENERGY;
        }
        else {
            double dxSq = distSq(node.right, node.left);
            double dySq = distSq(node.top, node.bottom);
            node.energy = Math.sqrt(dxSq + dySq);
        }
    }

    private Node removeVerticalNode(int x, int y) {
        Node deleted = getNode(x, y);
        assert deleted != null;
        Node left = getNode(x - 1, y);
        if (left != null) {
            left.right = deleted.right;
            deleted.top = left.top;
            deleted.left = left.left;
            deleted.bottom = left.bottom;
        }
        if (deleted.top != null) {
            deleted.top.bottom = deleted.right;
        }
        if (deleted.bottom != null) {
            deleted.bottom.top = deleted.right;
        }
        if (deleted.right != null) {
            deleted.right = left;
        }
        return deleted;
    }

    private void updEnergy(Node deleted) {
        Node left = deleted.left;
        if (left != null) {
            deleted.color = left.color;
            calcEnergy(left);
            deleted.energy = left.energy;
        }
        calcEnergy(deleted.top);
        calcEnergy(deleted.right);
        calcEnergy(deleted.bottom);
        calcEnergy(deleted.left);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        Objects.requireNonNull(seam);
        verifySeamLength(seam, height);
        verifySeamRange(seam, width - 1);
        Node[] deleted = new Node[height];
        for (int y = 0; y < height; ++y) {
            deleted[y] = removeVerticalNode(seam[y], y);
        }
        for (int y = 0; y < height; ++y) {
            updEnergy(deleted[y]);
        }
        width--;
    }
}