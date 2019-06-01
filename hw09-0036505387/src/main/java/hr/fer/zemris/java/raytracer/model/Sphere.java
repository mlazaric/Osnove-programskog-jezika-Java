package hr.fer.zemris.java.raytracer.model;


import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Models a sphere represented by its center point, radius and the various light related coefficients.
 *
 * @author Marko Lazarić
 */
public class Sphere extends GraphicalObject {

    /**
     * The center point of the sphere.
     */
    private final Point3D center;

    /**
     * The radius of the sphere.
     */
    private final double radius;

    /**
     * The red, green and blue diffusion light coefficients.
     */
    private final double kdr, kdg, kdb;

    /**
     * The red, green and blue reflection light coefficients.
     */
    private final double krr, krg, krb;

    /**
     * The shininess factor.
     */
    private final double krn;

    /**
     * Creates a new {@link Sphere} with the given arguments.
     *
     * @param center the center point of the sphere
     * @param radius the radius of the sphere
     * @param kdr the red diffusion light coefficient
     * @param kdg the green diffusion light coefficient
     * @param kdb the blue diffusion light coefficient
     * @param krr the red reflection light coefficient
     * @param krg the green reflection light coefficient
     * @param krb the blue reflection light coefficient
     * @param krn the shininess factor
     */
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

    /**
     * Finds the closest intersection to ray start between the ray and the sphere, or null if they do not intersect.
     *
     * @param ray the ray to intersect with the sphere
     * @return the closest intersection or null
     */
    public RayIntersection findClosestRayIntersection(Ray ray) {
        // https://en.wikipedia.org/wiki/Line–sphere_intersection
        double discriminant = pow(ray.direction.scalarProduct(ray.start.sub(center)), 2)
                            - pow(ray.start.sub(center).norm(), 2)
                            + pow(radius, 2);

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