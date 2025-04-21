package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Class representing a plane in 3D space, defined by a point on the plane and a normal vector.
 */
public class Plane extends Geometry {
    /**
     * A point on the plane.
     */
    public Point q0;

    /**
     * The normalized normal vector of the plane.
     */
    public Vector normal;

    /**
     * Constructor to initialize a plane with a point and a normal vector.
     * The normal vector is normalized upon initialization.
     *
     * @param q0     a point on the plane
     * @param normal the normal vector to the plane
     */
    public Plane(Point q0, Vector normal) {
        this.q0 = q0;
        this.normal = normal.normalize();
    }

    /**
     * Constructor to initialize a plane using three points in 3D space.
     * The points must not be collinear.
     *
     * @param point1 the first point on the plane
     * @param point2 the second point on the plane
     * @param point3 the third point on the plane
     * @throws IllegalArgumentException if the points are collinear
     */
    public Plane(Point point1, Point point2, Point point3) {
        this.q0 = point1;
        Vector u = point2.subtract(point1);
        Vector v = point3.subtract(point1);
        if (u.crossProduct(v).length() == 0) {
            throw new IllegalArgumentException("The points are on the same line");
        }
        Vector n = u.crossProduct(v);
        this.normal = n.normalize();
    }

    /**
     * Calculates the normal vector to the plane at a given point.
     *
     * @param point the point on the plane (not used in this implementation)
     * @return the normalized normal vector of the plane
     */
    @Override
    public Vector getNormal(Point point) {
        return normal;
    }

    /**
     * Getter for the normalized normal vector of the plane.
     *
     * @return the normalized normal vector of the plane
     */
    public Vector getNormal() {
        return normal;
    }
}