package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * The Sphere class represents a sphere in 3D space.
 * It is defined by a center point and a radius.
 */
public class Sphere extends RadialGeometry {

    /**
     * The center point of the sphere.
     */
    private final Point center;

    /**
     * Constructs a Sphere object with a given center and radius.
     *
     * @param center The center point of the sphere.
     * @param radius The radius of the sphere.
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }

    /**
     * Returns the normal vector to the sphere at a given point.
     *
     * @param point The point on the surface of the sphere.
     * @return The normal vector to the sphere at the given point.
     */
    @Override
    public Vector getNormal(Point point) {
        if (point.equals(center)) {
            throw new IllegalArgumentException("Point cannot be the center of the sphere");
        }
        return point.subtract(center).normalize();
    }

    /**
     * Finds the intersection points of a given ray with the sphere.
     *
     * @param ray The ray to intersect with the sphere.
     * @return A list of intersection points if the ray intersects the sphere,
     * or null if there are no intersections.
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        Point p0 = ray.getHead();
        Vector v = ray.getDirection();

        if (p0.equals(center)) {
            return List.of(ray.getPoint(radius)); // Ray starts at the center of the sphere
        }

        Vector u = center.subtract(p0);
        double tm = alignZero(u.dotProduct(v));
        double dSquared = alignZero(u.lengthSquared() - tm * tm);
        double radiusSquared = radius * radius;

        if (alignZero(dSquared - radiusSquared) >= 0) {
            return null; // No intersections
        }


        double th = Math.sqrt(radiusSquared - dSquared);
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);

        // If both t1 and t2 are negative, the intersection points are behind the ray's origin
        if (t1 <= 0 && t2 <= 0) {
            return null;
        }

        // If both t1 and t2 are positive, there are two intersection points
        if (t1 > 0 && t2 > 0) {
            Point p1 = p0.add(v.scale(t1));
            Point p2 = p0.add(v.scale(t2));
            return List.of(p1, p2);
        }

        // If one of t1 or t2 is negative, return the positive intersection point
        double t = t1 > 0 ? t1 : t2;
        return List.of(p0.add(v.scale(t)));
    }
}