package hr.fer.zemris.java.raytracer.RayCaster;

import hr.fer.zemris.java.raytracer.model.*;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;

import static hr.fer.zemris.java.raytracer.RayCaster.RayCaster.tracer;

/**
 * Models a simple ray caster using Phong's model for lighting and {@link ForkJoinPool} for parallelisation.
 *
 * @author Marko Lazarić
 */
public class RayCasterParallel2 {

    /**
     * Creates a predefined scene and renders it.
     *
     * @param args the arguments are ignored
     */
    public static void main(String[] args) {
        RayTracerViewer.show(
                getIRayTracerProducer(), getIRayTracerAnimator(), 30, 30
        );
    }

    /**
     * Creates a new ray tracer animator.
     *
     * @return the new ray tracer animator.
     */
    private static IRayTracerAnimator getIRayTracerAnimator() {
        return new IRayTracerAnimator() {
            long time;

            @Override
            public void update(long deltaTime) {
                time += deltaTime;
            }

            @Override
            public Point3D getViewUp() { // fixed in time
                return new Point3D(0, 0, 10);
            }

            @Override
            public Point3D getView() { // fixed in time
                return new Point3D(-2, 0, -0.5);
            }

            @Override
            public long getTargetTimeFrameDuration() {
                return 150; // redraw scene each 150 milliseconds
            }

            @Override
            public Point3D getEye() { // changes in time
                double t = (double) time / 10000 * 2 * Math.PI;
                double t2 = (double) time / 5000 * 2 * Math.PI;
                double x = 50 * Math.cos(t);
                double y = 50 * Math.sin(t);
                double z = 30 * Math.sin(t2);
                return new Point3D(x, y, z);
            }
        };
    }

    /**
     * Creates a new ray tracer producer.
     *
     * @return the new ray tracer producer
     */
    private static IRayTracerProducer getIRayTracerProducer() {
        return new IRayTracerProducer() {
            @Override
            public void produce(Point3D eye, Point3D view, Point3D viewUp,
                                double horizontal, double vertical, int width,
                                int height, long requestNo,
                                IRayTracerResultObserver observer,
                                AtomicBoolean cancel) {
                short[] red = new short[width*height];
                short[] green = new short[width*height];
                short[] blue = new short[width*height];

                // From PDF
                Point3D OG = view.sub(eye).normalize();
                Point3D VUV = viewUp.normalize();

                Point3D zAxis = OG;
                Point3D yAxis = VUV.sub(OG.scalarMultiply(OG.scalarProduct(VUV))).normalize();
                Point3D xAxis = zAxis.vectorProduct(yAxis).normalize();

                Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2))
                                           .add(yAxis.scalarMultiply(vertical / 2));

                Scene scene = RayTracerViewer.createPredefinedScene2();

                ForkJoinPool pool = new ForkJoinPool();

                pool.invoke(new TracingJob(eye, view, viewUp, horizontal, vertical, width, height, cancel,
                        0, height - 1, red, green, blue, scene, zAxis, yAxis, xAxis, screenCorner));
                pool.shutdown();

                observer.acceptResult(red, green, blue, requestNo);
            }
        };
    }

    /**
     * Models a rendering job using the {@link ForkJoinPool} and {@link RecursiveAction}.
     *
     * @author Marko Lazarić
     */
    private static class TracingJob extends RecursiveAction {

        /**
         * The number of rows of pixels it should compute directly.
         */
        private static final int DIRECTLY_COMPUTABLE_AMOUNT = 16 * 16 * 16;

        /**
         * The location of the eyes.
         */
        private final Point3D eye;

        /**
         * The location of the view.
         */
        private final Point3D view;

        /**
         * The up direction.
         */
        private final Point3D viewUp;

        /**
         * The width of the scene to trace.
         */
        private final double horizontal;

        /**
         * The height of the scene to trace.
         */
        private final double vertical;

        /**
         * The number of pixels in a row.
         */
        private final int width;

        /**
         * The number of pixels in a column.
         */
        private final int height;

        /**
         * Whether the job should be cancelled.
         */
        private final AtomicBoolean cancel;

        /**
         * The last y coordinate to trace.
         */
        private final int yEnd;

        /**
         * The first y coordinate to trace.
         */
        private final int yStart;

        /**
         * The red component of lighting.
         */
        private final short[] red;

        /**
         * The green component of lighting.
         */
        private final short[] green;

        /**
         * The blue component of lighting.
         */
        private final short[] blue;

        /**
         * The scene in which to trace.
         */
        private final Scene scene;

        /**
         * The z axis vector.
         */
        private final Point3D zAxis;

        /**
         * The y axis vector.
         */
        private final Point3D yAxis;

        /**
         * The x axis vector.
         */
        private final Point3D xAxis;

        /**
         * The screen corner.
         */
        private final Point3D screenCorner;

        /**
         * Creates a new {@link TracingJob} with the given arguments.
         *
         * @param eye the location of the eyes
         * @param view the location of the view
         * @param viewUp the up direction
         * @param horizontal the width of the scene to trace
         * @param vertical the height of the scene to trace
         * @param width the number of pixels in a row
         * @param height the number of pixels in a column
         * @param cancel whether the job should be cancelled
         * @param yStart the first y coordinate to trace
         * @param yEnd the last y coordinate to trace
         * @param red the red component of lighting
         * @param green the green component of lighting
         * @param blue the blue component of lighting
         * @param scene the scene in which to trace
         * @param zAxis the z axis vector
         * @param yAxis the y axis vector
         * @param xAxis the x axis vector
         * @param screenCorner the screen corner
         */
        private TracingJob(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical, int width,
                           int height, AtomicBoolean cancel, int yStart, int yEnd, short[] red, short[] green, short[] blue,
                           Scene scene, Point3D zAxis, Point3D yAxis, Point3D xAxis, Point3D screenCorner) {
            this.eye = eye;
            this.view = view;
            this.viewUp = viewUp;
            this.horizontal = horizontal;
            this.vertical = vertical;
            this.width = width;
            this.height = height;
            this.cancel = cancel;
            this.yEnd = yEnd;
            this.yStart = yStart;
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.scene = scene;
            this.zAxis = zAxis;
            this.yAxis = yAxis;
            this.xAxis = xAxis;
            this.screenCorner = screenCorner;
        }

        @Override
        protected void compute() {
            if (cancel.get()) {
                return;
            }

            if ((yEnd - yStart) < DIRECTLY_COMPUTABLE_AMOUNT) {
                computeDirectly();
                return;
            }

            invokeAll(
                new TracingJob(eye, view, viewUp, horizontal, vertical, width, height, cancel, yStart,
                        yStart + (yStart + yEnd) / 2, red, green, blue, scene, zAxis, yAxis, xAxis, screenCorner),
                new TracingJob(eye, view, viewUp, horizontal, vertical, width, height, cancel,
                        yStart + (yStart + yEnd) / 2 + 1, yEnd, red, green, blue, scene, zAxis, yAxis, xAxis, screenCorner)
            );
        }

        /**
         * Directly traces all the pixels from yStart to yEnd (inclusive).
         */
        private void computeDirectly() {
            short[] rgb = new short[3];
            int offset = yStart * width;

            for (int y = yStart; y <= yEnd; y++) {
                for (int x = 0; x < width; x++) {
                    if (cancel.get()) {
                        return;
                    }

                    Point3D screenPoint = screenCorner.add(xAxis.scalarMultiply(x * horizontal / (width - 1)))
                                                      .sub(yAxis.scalarMultiply(y * vertical / (height - 1)));

                    Ray ray = Ray.fromPoints(eye, screenPoint);

                    tracer(scene, ray, rgb);

                    red[offset] = rgb[0] > 255 ? 255 : rgb[0];
                    green[offset] = rgb[1] > 255 ? 255 : rgb[1];
                    blue[offset] = rgb[2] > 255 ? 255 : rgb[2];

                    offset++;
                }
            }
        }
    }
}