package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * Abstract class representing an intersectable geometry in 3D space.
 * This class provides a method to find intersection points of a ray with the geometry.
 */
public abstract class Intersectable {

    /**
     * Finds the intersection points of a given ray with the geometry.
     * This method must be implemented by subclasses to define specific intersection logic.
     *
     * @param ray the ray to intersect with the geometry
     * @return a list of intersection points, or null if there are no intersections
     */
    abstract List<Point> findIntersections(Ray ray);
}