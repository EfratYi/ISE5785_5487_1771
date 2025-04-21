package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Abstract class representing a geometric object in 3D space.
 * Provides a method to calculate the normal vector at a given point on the geometry.
 */
public abstract class Geometry extends Intersectable{

    /**
     * Calculates the normal vector to the geometry at the specified point.
     *
     * @param point the point on the geometry where the normal is to be calculated
     * @return the normal vector to the geometry at the given point
     */
    public abstract Vector getNormal(Point point);
}