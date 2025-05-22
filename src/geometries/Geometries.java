package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * The Geometries class represents a collection of geometric shapes.
 * It acts as a composite in the Composite design pattern, allowing multiple geometries
 * to be treated as a single entity. This enables operations like finding intersections
 * to be performed on the entire collection as if it were a single geometry.
 */
public class Geometries extends Intersectable {

    /**
     * A list that holds all the geometric shapes in the collection.
     */
    private final List<Intersectable> shapes = new LinkedList<>();

    /**
     * Constructs a Geometries object and initializes it with the given geometries.
     * This allows creating a composite of multiple geometric shapes.
     *
     * @param geometries the geometries to add to the collection
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds one or more geometries to the collection.
     * This method allows dynamically extending the composite with additional shapes.
     *
     * @param geometries the geometries to add
     */
    public void add(Intersectable... geometries) {
        Collections.addAll(shapes, geometries);
    }

    /**
     * Finds the intersection points of a given ray with all geometries in the collection.
     * This method iterates through all the geometries in the composite and collects
     * their intersection points. If no intersections are found, it returns null.
     *
     * @param ray the ray to intersect with the geometries
     * @return a list of intersection points, or null if there are no intersections
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        if (shapes.isEmpty()) {
            return null;
        }
        List<Intersection> intersections = null;  // Initialize as null
        for (Intersectable shape : shapes) {
            List<Intersection> shapeIntersections = shape.calculateIntersections(ray);
            if (shapeIntersections != null) {
                if (intersections == null) {
                    intersections = new LinkedList<>();  // Initialize only if there are intersections
                }

                intersections.addAll(shapeIntersections);  // Add all intersections from the shape
            }
        }
        return intersections;  // Return null if no intersections
    }
}
