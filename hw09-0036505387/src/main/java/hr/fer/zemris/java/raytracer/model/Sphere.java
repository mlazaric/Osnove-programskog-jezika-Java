package hr.fer.zemris.java.raytracer.model;


import static java.lang.Math.*;

public class Sphere extends GraphicalObject {

    private final Point3D center;
    private final double radius;
    private final double kdr, kdg, kdb;
    private final double krr, krg, krb, krn;

    public Sphere(Point3D center, double radius, double kdr, double kdg,
                  double kdb, double krr, double krg, double krb, double krn) {
        this.center = center;
        this.radius = radius;

        this.kdr = kdr;
        this.kdg = kdg;
        this.kdb = kdb;

        this.krr = krr;
        this.krg = krg;
        this.krb = krb;
        this.krn = krn;
    }

    public RayIntersection findClosestRayIntersection(Ray ray) {
        // https://en.wikipedia.org/wiki/Line–sphere_intersection
        double discriminant = pow(ray.direction.scalarProduct(ray.start.sub(center)), 2)
                - pow(ray.start.sub(center).norm(), 2) + pow(radius, 2);

        // No solutions exist.
        if (discriminant < 0) {
            return null;
        }

        // If two solutions exist, the closer one will be the one where we sub the sqrt of discriminant.
        // If one solution exists, it doesn't matter if we sub or add the sqrt of discriminant.
        double distance = -(ray.direction.scalarProduct(ray.start.sub(center))) - sqrt(discriminant);

        // Ray is a parametric definition of a line, point = direction * t + start, where t is the distance from the start.
        Point3D point = ray.start.add(ray.direction.scalarMultiply(distance));

        boolean outer = true;

        return new RayIntersection(point, distance, outer) {
            @Override
            public Point3D getNormal() {
                return point.sub(center).normalize();
            }

            @Override
            public double getKdr() {
                return kdr;
            }

            @Override
            public double getKdg() {
                return kdg;
            }

            @Override
            public double getKdb() {
                return kdb;
            }

            @Override
            public double getKrr() {
                return krr;
            }

            @Override
            public double getKrg() {
                return krg;
            }

            @Override
            public double getKrb() {
                return krb;
            }

            @Override
            public double getKrn() {
                return krn;
            }
        };
    }

}