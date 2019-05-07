package hr.fer.zemris.java.raytracer.RayCaster;

import hr.fer.zemris.java.raytracer.model.*;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;

import static hr.fer.zemris.java.raytracer.RayCaster.RayCaster.tracer;

public class RayCasterParallel2 {
    public static void main(String[] args) {
        RayTracerViewer.show(
                getIRayTracerProducer(), getIRayTracerAnimator(), 30, 30
        );
    }

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

                pool.invoke(new RenderingJob(eye, view, viewUp, horizontal, vertical, width, height, cancel,
                        0, height - 1, red, green, blue, scene, zAxis, yAxis, xAxis, screenCorner));
                pool.shutdown();

                observer.acceptResult(red, green, blue, requestNo);
            }
        };
    }

    private static class RenderingJob extends RecursiveAction {
        private static final int DIRECTLY_COMPUTABLE_AMOUNT = 16 * 16;

        private final Point3D eye;
        private final Point3D view;
        private final Point3D viewUp;
        private final double horizontal;
        private final double vertical;
        private final int width;
        private final int height;
        private final AtomicBoolean cancel;
        private final int yMax;
        private final int yMin;
        private final short[] red;
        private final short[] green;
        private final short[] blue;
        private final Scene scene;
        private final Point3D zAxis;
        private final Point3D yAxis;
        private final Point3D xAxis;
        private final Point3D screenCorner;

        private RenderingJob(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical, int width,
                             int height, AtomicBoolean cancel, int yMin, int yMax, short[] red, short[] green, short[] blue,
                             Scene scene, Point3D zAxis, Point3D yAxis, Point3D xAxis, Point3D screenCorner) {
            this.eye = eye;
            this.view = view;
            this.viewUp = viewUp;
            this.horizontal = horizontal;
            this.vertical = vertical;
            this.width = width;
            this.height = height;
            this.cancel = cancel;
            this.yMax = yMax;
            this.yMin = yMin;
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

            if ((yMax - yMin) < DIRECTLY_COMPUTABLE_AMOUNT) {
                computeDirectly();
                return;
            }

            invokeAll(
                new RenderingJob(eye, view, viewUp, horizontal, vertical, width, height, cancel, yMin,
                        yMin + (yMin + yMax) / 2, red, green, blue, scene, zAxis, yAxis, xAxis, screenCorner),
                new RenderingJob(eye, view, viewUp, horizontal, vertical, width, height, cancel,
                        yMin + (yMin + yMax) / 2 + 1, yMax, red, green, blue, scene, zAxis, yAxis, xAxis, screenCorner)
            );
        }

        private void computeDirectly() {
            short[] rgb = new short[3];
            int offset = yMin * width;

            for (int y = yMin; y <= yMax; y++) {
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