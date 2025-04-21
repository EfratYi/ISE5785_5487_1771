package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.alignZero;

/**
 * Class representing a tube in 3D space.
 * A tube is defined by a central axis (ray) and a radius.
 */
public class Tube extends RadialGeometry {
    /**
     * The central axis of the tube.
     */
    protected final Ray ray;

    /**
     * Constructor to initialize a tube with a given radius and central axis.
     *
     * @param radius the radius of the tube
     * @param ray    the central axis of the tube
     */
    public Tube(double radius, Ray ray) {
        super(radius);
        this.ray = ray;
    }

    /**
     * Calculates the normal vector to the tube at a given point.
     *
     * @param point the point on the surface of the tube
     * @return the normalized vector perpendicular to the tube's surface at the given point
     * @throws IllegalArgumentException if the point lies on the tube's axis
     */
    @Override
    public Vector getNormal(Point point) {
        Vector dir = ray.getDirection();
        Point p0 = ray.getHead();

        // Vector from the ray's head to the given point
        Vector pMinusP0 = point.subtract(p0);
        double t = alignZero(dir.dotProduct(pMinusP0));

        // Calculate the projection point on the tube's axis
        Point o = (t == 0) ? p0 : p0.add(dir.scale(t));

        // Calculate the normal vector
        Vector normal = point.subtract(o);

        // Ensure the normal vector is valid
        if (normal.lengthSquared() == 0) {
            throw new IllegalArgumentException("Cannot compute normal: point lies on the axis");
        }

        return normal.normalize();
    }
}