/******************************************************************************
 *  Compilation:  javac ResizeDemo.java
 *  Execution:    java ResizeDemo input.png columnsToRemove rowsToRemove
 *  Dependencies: SeamCarver.java SCUtility.java
 *                
 *
 *  Read image from file specified as command line argument. Use SeamCarver
 *  to remove number of rows and columns specified as command line arguments.
 *  Show the images and print time elapsed to screen.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class ResizeDemo {

    private static final boolean HORIZONTAL   = true;
    private static final boolean VERTICAL     = false;

    private static void printSeam(SeamCarverLinked carver, int[] seam, boolean direction) {
        double totalSeamEnergy = 0.0;

        for (int row = 0; row < carver.height(); row++) {
            for (int col = 0; col < carver.width(); col++) {
                double energy = carver.energy(col, row);
                String marker = " ";
                if ((direction == HORIZONTAL && row == seam[col]) ||
                        (direction == VERTICAL   && col == seam[row])) {
                    marker = "*";
                    totalSeamEnergy += energy;
                }
                StdOut.printf("%7.2f%s ", energy, marker);
            }
            StdOut.println();
        }
        // StdOut.println();
        StdOut.printf("Total energy = %f\n", totalSeamEnergy);
        StdOut.println();
        StdOut.println();
    }

    private static void printSeam(SeamCarver carver, int[] seam, boolean direction) {
        double totalSeamEnergy = 0.0;

        for (int row = 0; row < carver.height(); row++) {
            for (int col = 0; col < carver.width(); col++) {
                double energy = carver.energy(col, row);
                String marker = " ";
                if ((direction == HORIZONTAL && row == seam[col]) ||
                        (direction == VERTICAL   && col == seam[row])) {
                    marker = "*";
                    totalSeamEnergy += energy;
                }
                StdOut.printf("%7.2f%s ", energy, marker);
            }
            StdOut.println();
        }
        // StdOut.println();
        StdOut.printf("Total energy = %f\n", totalSeamEnergy);
        StdOut.println();
        StdOut.println();
    }

    private static void printSeam(SeamCarverLinked sc) {
        StdOut.printf("Vertical seam: { ");
        int[] verticalSeam = sc.findVerticalSeam();
        for (int x : verticalSeam)
            StdOut.print(x + " ");
        StdOut.println("}");
        printSeam(sc, verticalSeam, VERTICAL);
    }

    private static void printSeam(SeamCarver sc) {
        StdOut.printf("Vertical seam: { ");
        int[] verticalSeam = sc.findVerticalSeam();
        for (int x : verticalSeam)
            StdOut.print(x + " ");
        StdOut.println("}");
        printSeam(sc, verticalSeam, VERTICAL);
    }

    private static void printEnergy(SeamCarver sc) {
        StdOut.printf("Printing energy calculated for each pixel.\n");
        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++) {
                StdOut.printf("%9.2f ", sc.energy(col, row));
            }
            StdOut.println();
        }
    }

    private static void printEnergy(SeamCarverLinked sc) {
        StdOut.printf("Printing energy calculated for each pixel.\n");
        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++) {
                StdOut.printf("%9.2f ", sc.energy(col, row));
            }
            StdOut.println();
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            StdOut.println("Usage:\njava ResizeDemo [image filename] [num cols to remove] [num rows to remove]");
            args = new String[] {"Lab7_SeamCarving/testing/6x5.png", "1", "5"};
        }

        Picture inputImg = new Picture(args[0]);
        int removeColumns = Integer.parseInt(args[1]);
        int removeRows = Integer.parseInt(args[2]); 

        StdOut.printf("image is %d columns by %d rows\n", inputImg.width(), inputImg.height());
        SeamCarver sc = new SeamCarver(inputImg);
        SeamCarverLinked scl = new SeamCarverLinked(inputImg);

        Stopwatch sw = new Stopwatch();

//        for (int i = 0; i < removeRows; i++) {
//            int[] horizontalSeam = sc.findHorizontalSeam();
//            sc.removeHorizontalSeam(horizontalSeam);
//        }

        printSeam(sc);
        for (int i = 0; i < removeColumns; i++) {
            int[] verticalSeam = sc.findVerticalSeam();
            sc.removeVerticalSeam(verticalSeam);
            scl.removeVerticalSeam(verticalSeam);
            printSeam(sc);
            printEnergy(scl);
        }
        Picture scOut = sc.picture();
        Picture sclOut = scl.picture();
        String modifiedShouldPath = args[0].substring(0, args[0].length() - 4) + "_modified_should.png";
        String modifiedGotPath = args[0].substring(0, args[0].length() - 4) + "_modified_got.png";
        scOut.save(modifiedShouldPath);
        sclOut.save(modifiedGotPath);

        StdOut.printf("new image size is %d columns by %d rows\n", sc.width(), sc.height());

        StdOut.println("Resizing time: " + sw.elapsedTime() + " seconds.");
//        inputImg.show();
//        outputImg.show();
    }
    
}
