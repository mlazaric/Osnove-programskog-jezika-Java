package hr.fer.zemris.java.fractals.Newton;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Newton {

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

        System.out.println("Image of fractal will appear shortly. Thank you.");

        ComplexRootedPolynomial polynomial = new ComplexRootedPolynomial(Complex.ONE, roots.toArray(Complex[]::new));
        FractalViewer.show(new NewtonFractal(polynomial));
    }

    private static class NewtonFractal implements IFractalProducer {
        private final ComplexRootedPolynomial polynomial;
        private final ExecutorService pool;
        private final int numberOfJobs;

        private static final int MAX_ITERATIONS = 16 * 16 * 16;

        private NewtonFractal(ComplexRootedPolynomial polynomial) {
            this.polynomial = polynomial;
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
                try {
                    result.get();
                } catch (InterruptedException | ExecutionException e) {}
            }

            System.out.println("Calculation finished, drawing fractal...");
            observer.acceptResult(data, (short) (polynomial.toComplexPolynom().order() + 1), requestNo);
        }
    }

    private static class CalculateRowsJob implements Callable<Void> {
        private static final double CONVERGENCE_TRESHOLD = 1E-3;

        private final int yStart;
        private final int yEnd;
        private final double reMin;
        private final double reMax;
        private final double imMin;
        private final double imMax;
        private final int width;
        private final int height;
        private final int maxIterations;
        private final short[] data;
        private final AtomicBoolean cancel;
        private final ComplexRootedPolynomial polynomial;
        private final ComplexPolynomial derived;

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
                        zn = zn.subtract(polynomial.apply(zn).divide(derived.apply(zn)));

                        numberOfIterations++;

                        if (cancel.get()) {
                            return null;
                        }
                    } while (prevZn.subtract(zn).module() > CONVERGENCE_TRESHOLD && numberOfIterations < maxIterations);

                    data[offset] = (short) (polynomial.indexOfClosestRootFor(zn, CONVERGENCE_TRESHOLD) + 1);
                    offset++;

                    if (cancel.get()) {
                        return null;
                    }
                }
            }

            return null;
        }

        private Complex mapToComplex(int x, int y) {
            double real = x * (reMax - reMin) / (width - 1.0) + reMin;
            double imag = (height - 1.0 - y) * (imMax - imMin) / (height - 1.0) + imMin;

            return new Complex(real, imag);
        }
    }

    private static class DaemonicThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);

            thread.setDaemon(true);

            return thread;
        }
    }
}
