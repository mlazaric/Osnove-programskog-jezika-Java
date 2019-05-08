package hr.fer.zemris.java.fractals.Newton;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Draws fractals derived from Newton-Raphson iterative method.
 *
 * @author Marko Lazarić
 */
public class Newton {

    /**
     * Prompts the user for 2 or more complex roots and then draws the resulting fractal produced by the Newton-Raphson
     * iterative method.
     *
     * @param args the arguments are ignored
     */
    public static void main(String[] args) {
        System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.\n" +
                "Please enter at least two roots, one root per line. Enter 'done' when done.");

        List<Complex> roots = new ArrayList<>();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.format("Root %d> ", roots.size() + 1);
            String input = sc.nextLine().strip();

            if (input.equals("done")) {
                if (roots.size() < 2) {
                    System.out.println("At least two roots are required.");
                    continue;
                }
                else {
                    break;
                }
            }

            try {
                String parseableString = input.replaceAll("\\s+", "") // Remove all spaces from the string.
                                              .replaceAll("i([0-9.]+)", "$1i"); // Move i after the number, i54 -> 54i.

                Complex root = Complex.parse(parseableString);

                roots.add(root);
            }
            catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        sc.close();

        System.out.println("Image of the fractal will appear shortly. Thank you.");

        ComplexRootedPolynomial polynomial = new ComplexRootedPolynomial(Complex.ONE, roots.toArray(Complex[]::new));
        FractalViewer.show(new NewtonFractal(polynomial));
    }

    /**
     * Produces a fractal created using the Newton-Raphson iterative method on the polynomial.
     * Uses {@link ExecutorService} for parallelisation.
     *
     * @author Marko Lazarić
     */
    private static class NewtonFractal implements IFractalProducer {

        /**
         * The polynomial used in Newton-Raphson's method.
         */
        private final ComplexRootedPolynomial polynomial;

        /**
         * The thread pool used for parallelisation.
         */
        private final ExecutorService pool;

        /**
         * The number of jobs to create in the thread pool.
         */
        private final int numberOfJobs;

        /**
         * Maximum number of iterations before assuming divergence.
         */
        private static final int MAX_ITERATIONS = 16 * 16 * 16;

        /**
         * Creates a new {@link NewtonFractal} with the given argument.
         *
         * @param polynomial the polynomial used in Newton-Raphson's method
         *
         * @throws NullPointerException if {@code polynomial} is {@code null}
         */
        private NewtonFractal(ComplexRootedPolynomial polynomial) {
            this.polynomial = Objects.requireNonNull(polynomial, "Cannot produce fractal of null.");
            this.numberOfJobs = 8 * Runtime.getRuntime().availableProcessors();
            this.pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new DaemonicThreadFactory());
        }

        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height, long requestNo,
                            IFractalResultObserver observer, AtomicBoolean cancel) {
            System.out.println("Starting calculation...");

            short[] data = new short[width * height];
            int rowsPerJob = height / numberOfJobs;

            List<Future<Void>> futureResults = new ArrayList<>();

            for (int jobIndex = 0; jobIndex < numberOfJobs; ++jobIndex) {
                int yStart = jobIndex * rowsPerJob;
                int yEnd = (jobIndex + 1) * rowsPerJob - 1;

                // Make sure we do not miss any rows because of integer division.
                if (jobIndex == (numberOfJobs - 1)) {
                    yEnd = height - 1;
                }

                CalculateRowsJob job = new CalculateRowsJob(yStart, yEnd,
                        reMin, reMax, imMin, imMax, width, height,
                        MAX_ITERATIONS, data, cancel, polynomial);

                futureResults.add(pool.submit(job));
            }

            for (Future<Void> result : futureResults) {
                if (cancel.get()) {
                    return;
                }

                try {
                    result.get();
                } catch (InterruptedException | ExecutionException e) {}
            }

            System.out.println("Calculation finished, drawing fractal...");
            observer.acceptResult(data, (short) (polynomial.toComplexPolynom().order() + 1), requestNo);
        }
    }

    /**
     * Models a single job in the {@link NewtonFractal} drawing process.
     */
    private static class CalculateRowsJob implements Callable<Void> {
        /**
         * The epsilon used for determining convergence.
         */
        private static final double CONVERGENCE_THRESHOLD = 1E-3;

        /**
         * The first y coordinate to calculate.
         */
        private final int yStart;

        /**
         * The last y coordinate to calculate.
         */
        private final int yEnd;

        /**
         * The minimum value of the real part.
         */
        private final double reMin;

        /**
         * The maximum value of the real part.
         */
        private final double reMax;

        /**
         * The minimum value of the imaginary part.
         */
        private final double imMin;

        /**
         * The maximum value of the imaginary part.
         */
        private final double imMax;

        /**
         * The number of pixels in a row.
         */
        private final int width;

        /**
         * The number of pixels in a column.
         */
        private final int height;

        /**
         * The number of iterations before assuming divergence.
         */
        private final int maxIterations;

        /**
         * The indices of the closest roots.
         */
        private final short[] data;

        /**
         * Whether this job should be cancelled.
         */
        private final AtomicBoolean cancel;

        /**
         * The polynomial used in Newton-Raphson's method.
         */
        private final ComplexRootedPolynomial polynomial;

        /**
         * The derivation of {@link #polynomial}.
         */
        private final ComplexPolynomial derived;

        /**
         * Creates a new {@link CalculateRowsJob} with the given arguments.
         *
         * @param yStart the first y coordinate to calculate
         * @param yEnd the last y coordinate to calculate
         * @param reMin the minimum value of the real part
         * @param reMax the maximum value of the real part
         * @param imMin the minimum value of the imaginary part
         * @param imMax the maximum value of the imaginary part
         * @param width the number of pixels in a row
         * @param height the number of pixels in a column
         * @param maxIterations the number of iterations before assuming divergence
         * @param data the indices of the closest roots
         * @param cancel whether this job should be cancelled
         * @param polynomial the polynomial used in Newton-Raphson's method
         */
        private CalculateRowsJob(int yStart, int yEnd,
                                 double reMin, double reMax, double imMin, double imMax, int width, int height,
                                 int maxIterations, short[] data, AtomicBoolean cancel, ComplexRootedPolynomial polynomial) {
            this.yStart = yStart;
            this.yEnd = yEnd;
            this.reMin = reMin;
            this.reMax = reMax;
            this.imMin = imMin;
            this.imMax = imMax;
            this.width = width;
            this.height = height;
            this.maxIterations = maxIterations;
            this.data = data;
            this.cancel = cancel;
            this.polynomial = polynomial;
            this.derived = polynomial.toComplexPolynom().derive();
        }

        /**
         * Calculates indices of the closest root for pixels in rows from yStart, to yEnd, inclusive.
         *
         * {@inheritDoc}
         */
        @Override
        public Void call() {
            int offset = width * yStart;

            for (int y = yStart; y <= yEnd; ++y) {
                for (int x = 0; x < width; ++x) {
                    Complex zn = mapToComplex(x, y);
                    Complex prevZn = null;

                    int numberOfIterations = 0;

                    do {
                        prevZn = zn;
                        zn = zn.sub(polynomial.apply(zn).divide(derived.apply(zn)));

                        numberOfIterations++;

                        if (cancel.get()) {
                            return null;
                        }
                    } while (prevZn.sub(zn).module() > CONVERGENCE_THRESHOLD && numberOfIterations < maxIterations);

                    data[offset] = (short) (polynomial.indexOfClosestRootFor(zn, CONVERGENCE_THRESHOLD) + 1);
                    offset++;

                    if (cancel.get()) {
                        return null;
                    }
                }
            }

            return null;
        }

        /**
         * Maps x and y coordinate of a pixel to a {@link Complex} number.
         *
         * @param x the x coordinate of the pixel
         * @param y the y coordinate of the pixel
         * @return the {@link Complex} number the pixel maps to
         */
        private Complex mapToComplex(int x, int y) {
            double real = x * (reMax - reMin) / (width - 1.0) + reMin;
            double imag = (height - 1.0 - y) * (imMax - imMin) / (height - 1.0) + imMin;

            return new Complex(real, imag);
        }
    }

    /**
     * Creates {@link Thread}s with the daemon flag set to true.
     *
     * @author Marko Lazarić
     */
    private static class DaemonicThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);

            thread.setDaemon(true);

            return thread;
        }
    }
}
