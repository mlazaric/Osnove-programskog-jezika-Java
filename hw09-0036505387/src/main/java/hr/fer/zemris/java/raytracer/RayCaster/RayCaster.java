package hr.fer.zemris.java.raytracer.RayCaster;

import hr.fer.zemris.java.raytracer.model.*;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Math.pow;

public class RayCaster {

    public static final double DOUBLE_EPSILON = 1E-3;
    public static final int AMBIENT_LIGHT = 15;

    public static void main(String[] args) {
        RayTracerViewer.show(getIRayTracerProducer(),
                new Point3D(10,0,0),
                new Point3D(0,0,0),
                new Point3D(0,0,10),
                20, 20);
    }

    private static IRayTracerProducer getIRayTracerProducer() {
        return new IRayTracerProducer() {
            @Override
            public void produce(Point3D eye, Point3D view, Point3D viewUp,
                                double horizontal, double vertical, int width, int height,
                                long requestNo, IRayTracerResultObserver observer,
                                AtomicBoolean cancel) {
                System.out.println("Započinjem izračune...");

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

                Scene scene = RayTracerViewer.createPredefinedScene();

                short[] rgb = new short[3];
                int offset = 0;

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
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

                System.out.println("Izračuni gotovi...");
                observer.acceptResult(red, green, blue, requestNo);
                System.out.println("Dojava gotova...");
            }
        };
    }

    protected static void tracer(Scene scene, Ray ray, short[] rgb) {
        RayIntersection intersection = findClosestIntersection(scene, ray);

        if (intersection == null) {
            rgb[0] = rgb[1] = rgb[2] = 0;
            return;
        }

        determineColorFor(scene, ray, intersection, rgb);
    }

    protected static void determineColorFor(Scene scene, Ray eyeRay, RayIntersection intersection, short[] rgb) {
        rgb[0] = rgb[1] = rgb[2] = AMBIENT_LIGHT; // Ambient component

        for (LightSource source : scene.getLights()) {
            Ray ray = Ray.fromPoints(source.getPoint(), intersection.getPoint());

            RayIntersection closestIntersection = findClosestIntersection(scene, ray);

            if (closestIntersection == null ||  source.getPoint().sub(intersection.getPoint()).norm() >
                                                source.getPoint().sub(closestIntersection.getPoint()).norm() + DOUBLE_EPSILON) {
                continue;
            }

            // Add diffusion component
            Point3D n = intersection.getNormal();
            Point3D l = source.getPoint().sub(intersection.getPoint()).normalize();

            double diffusionCoefficient = l.scalarProduct(n);

            if (diffusionCoefficient > 0) {
                rgb[0] += diffusionCoefficient * intersection.getKdr() * source.getR();
                rgb[1] += diffusionCoefficient * intersection.getKdg() * source.getG();
                rgb[2] += diffusionCoefficient * intersection.getKdb() * source.getB();
            }

            // Add reflective component
            // https://math.stackexchange.com/a/13263
            Point3D r = l.negate().sub(n.scalarMultiply(2 * l.negate().scalarProduct(n))).normalize();
            Point3D v = eyeRay.start.sub(closestIntersection.getPoint()).normalize();

            double reflectionCoeffiecient = r.scalarProduct(v);

            if (reflectionCoeffiecient > 0) {
                reflectionCoeffiecient = pow(reflectionCoeffiecient, intersection.getKrn());

                rgb[0] += reflectionCoeffiecient * intersection.getKrr() * source.getR();
                rgb[1] += reflectionCoeffiecient * intersection.getKrg() * source.getG();
                rgb[2] += reflectionCoeffiecient * intersection.getKrb() * source.getB();
            }
        }
    }

    protected static RayIntersection findClosestIntersection(Scene scene, Ray ray) {
        RayIntersection closestIntersection = null;

        for (GraphicalObject object : scene.getObjects()) {
            RayIntersection intersection = object.findClosestRayIntersection(ray);

            if (intersection == null) {
                continue;
            }

            if (closestIntersection == null || intersection.getDistance() < closestIntersection.getDistance()) {
                closestIntersection = intersection;
            }
        }

        return closestIntersection;
    }
}
