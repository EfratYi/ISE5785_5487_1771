package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Class representing a collection of geometric shapes.
 * This class allows grouping multiple geometries and finding their intersections with a given ray.
 */
public class Geometries extends Intersectable {

    /**
     * List of geometric shapes in the collection.
     */
    private final List<Intersectable> shapes = new LinkedList<>();

    /**
     * Constructor to initialize the collection with a variable number of geometries.
     *
     * @param geometries the geometries to add to the collection
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds one or more geometries to the collection.
     *
     * @param geometries the geometries to add
     */
    public void add(Intersectable... geometries) {
        Collections.addAll(shapes, geometries);
    }

    /**
     * Finds the intersection points of a given ray with all geometries in the collection.
     * If no intersections are found, returns null.
     *
     * @param ray the ray to intersect with the geometries
     * @return a list of intersection points, or null if there are no intersections
     */
    @Override
    List<Point> findIntersections(Ray ray) {
        List<Point> intersections = new LinkedList<>();
        for (Intersectable shape : shapes) {
            List<Point> shapeIntersections = shape.findIntersections(ray);
            if (shapeIntersections != null) {
                intersections.addAll(shapeIntersections);
            }
        }
        if (intersections.isEmpty()) {
            return null;
        }
        return intersections;
    }
}