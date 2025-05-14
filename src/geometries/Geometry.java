package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Abstract class for geometric objects in 3D space.
 * Extends {@link Intersectable} and provides a method to calculate the normal vector.
 */
public abstract class Geometry extends Intersectable {

    /**
     * Calculates the normal vector to the geometry at a given point.
     *
     * @param point the point on the geometry
     * @return the normal vector at the given point
     */
    public abstract Vector getNormal(Point point);
}