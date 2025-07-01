package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
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
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        Point p0 = ray.getHead();
        Vector v = ray.getDirection();

        if (p0.equals(center)) {
            // Ray starts at the center of the sphere
            return List.of(new Intersection(this, ray.getPoint(radius)));
        }

        Vector u = center.subtract(p0);
        double tm = alignZero(u.dotProduct(v));
        double dSquared = alignZero(u.lengthSquared() - tm * tm);

        if (alignZero(dSquared - radiusSquared) >= 0) {
            return null; // No intersections
        }

        double th = Math.sqrt(radiusSquared - dSquared);
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);

        // הגנה מפני ערכים קטנים מדי או שליליים
        boolean t1Valid = t1 > 0 && !Util.isZero(t1);
        boolean t2Valid = t2 > 0 && !Util.isZero(t2);

        if (t1Valid && t2Valid) {
            return List.of(
                    new Intersection(this, ray.getPoint(t1)),
                    new Intersection(this, ray.getPoint(t2))
            );
        }
        if (t1Valid) {
            return List.of(new Intersection(this, ray.getPoint(t1)));
        }
        if (t2Valid) {
            return List.of(new Intersection(this, ray.getPoint(t2)));
        }

        return null;
    }
    @Override
    public void setBoundingBox() {
        // מחשב תיבת גבול לפי נקודות קצה
        // החלפה לפי סוג הגיאומטריה
        double minX = center.getX() - radius;
        double maxX = center.getX() + radius;
        double minY = center.getY() - radius;
        double maxY = center.getY() + radius;
        double minZ = center.getZ() - radius;
        double maxZ = center.getZ() + radius;
        this.boundingBox = new BoundingBox(minX, maxX, minY, maxY, minZ, maxZ);

    }
}