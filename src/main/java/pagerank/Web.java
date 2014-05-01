package pagerank;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple Google's PageRank algorithm implementation.
 *
 * @see http://patft.uspto.gov/netacgi/nph-Parser?patentnumber=6,285,999
 * @see http://www.ams.org/samplings/feature-column/fcarc-pagerank
 *
 * Wiki:
 * > PageRank is an algorithm used by Google Search to rank websites in their search engine results.
 * > PageRank was named after Larry Page, one of the founders of Google.
 * > PageRank is a way of measuring the importance of website pages.
 */

/**
 * TODO:
 * - dumping factor
 * - generify
 * - clean code
 */

/**
 * This class represents Web.
 * In order to calculate PageRank we only
 * need a list of pages and connections between them.
 */
public class Web {

    /** maximum number of iterations (to prevent infinite loop).
     *  In practice ~100 iterations is enough */
    private static final int MAX_ITERS = 1000;

    private static final double EPSILON = 0.0000001;

    /**
     * List of all pages in this Web.
     */
    private final List<Page> pages = new ArrayList<Page>();

    /**
     * Add new page (if not already added).
     * @param page
     */
    public void add(Page page) {
        if (page == null) {
            return;
        }
        if (!this.pages.contains(page)) {
            this.pages.add(page);
        }
    }

    /**
     * Add many pages.
     * @param pages
     */
    public void add(Page... pages) {
        if (pages == null) {
            return;
        }
        for (Page p : pages) {
            this.add(p);
        }
    }

    /**
     * Calculate PageRank.
     */
    public double[][] pageRank() {
        final int size = this.pages.size();

        /* default rank for first iteration */
        double rank = 1.0 / size;

        /* ranks vector */
        double[][] ranks = new double[size][1];
        for (int i = 0; i < size; i++) {
            ranks[i][0] = rank;
        }

        /**
         * Construct transition matrix.
         *
         * i.e. if we have 4 pages, then
         * we should make 4x4 matrix where
         * rows and columns show if there is
         * a link from `i` page to `j` page.
         *
         * [i][j]-value shows 'weight' of the link.
         * 'weight' == 1.0 / number_of_outbound_links
         *
         * Example:
         *
         * we have 3 pages: A, B, C.
         * Links:
         *
         * A -> B, C
         * B -> A
         * C -> A, B
         *
         * A's link weight = 1.0 / 2 = 0.5
         * B's link weight = 1.0 / 1 = 1.0
         * C's link weight = 1.0 / 2 = 0.5
         *
         * Transition matrix:
         *    A    B     C
         * A  0    1    0.5
         *
         * B  0.5  0    0.5
         *
         * C  0.5  0     0
         **/
        double[][] matrix = new double[size][size];
        for (int i = 0; i < size; i ++) {
            Page page = this.pages.get(i);

            double weight = 0d;
            if (!page.getLinks().isEmpty()) {
                weight = 1.0 / page.getLinks().size();
            }

            for (int j = 0; j < size; j++) {
                if (page.isLinkedTo(j + 1)) {
                    matrix[j][i] = weight;
                }
            }
        }

        /* calculate PageRank */
        int c = 0;
        double[][] old = null;
        for (int i = 0; i < MAX_ITERS; i++) {
            c += 1;
            /* check if we have reached equilibrium value */
            if (old != null && isEquilibrium(old, ranks)) {
                /* set ranks */
                for (int p = 0; p < size; p++) {
                    pages.get(p).setRank(ranks[p][0]);
                }
                break;
            }
            /* remember previous ranks vector */
            old = ranks;

            /* compute new ranks vector */
            ranks = multiplyMatrices(matrix, ranks);
        }
        return ranks;
    }

    /**
     * Check if equilibrium value has been reached
     * (two matrices are equal to each other).
     *
     * @param A
     * @param B
     * @return
     */
    public static boolean isEquilibrium(double[][] A, double[][] B) {
        if (A == null || B == null) {
            throw new IllegalArgumentException("Matrices cannot be null!");
        }
        if (A.length != B.length) {
            throw new IllegalArgumentException("Matrices must be of the same size!");
        }
        for (int i = 0; i < A.length; i++) {
            if (A[i].length != B[i].length) {
                throw new IllegalArgumentException("Matrices should have the same row length!");
            }
            for (int j = 0; j < A[i].length; j++) {
                if (!doubleEquals(A[i][j], B[i][j])) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Check if two doubles are equal (with #EPSILON accuracy)
     * We don't need to know if
     * one double is greater/less than another.
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean doubleEquals(double a, double b) {
        return Math.abs(a - b) <= EPSILON;
    }

    /**
     * Multiply two matrices.
     *
     * @see stackoverflow.com/questions/17623876/matrix-multiplication-using-arrays
     * @param A
     * @param B
     * @return
     */
    public static double[][] multiplyMatrices(double[][] A, double[][] B) {
        int aRows = A.length;
        int aColumns = A[0].length;

        int bRows = B.length;
        int bColumns = B[0].length;

        if (aColumns != bRows) {
            throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
        }

        double[][] C = new double[aRows][bColumns];

        for (int i = 0; i < aRows; i++) { // aRow
            for (int j = 0; j < bColumns; j++) { // bColumn
                for (int k = 0; k < aColumns; k++) { // aColumn
                    C[i][j] = C[i][j] + A[i][k] * B[k][j];
                }
            }
        }
        return C;
    }

    @Override
    public String toString() {
        return "Web{" + "pages=" + pages + '}';
    }
}
