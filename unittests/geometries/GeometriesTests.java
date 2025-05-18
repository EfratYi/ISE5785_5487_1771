package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Geometries} class.
 */
class GeometriesTests {

    /**
     * Test method for {@link Geometries#findIntersections(Ray)}.
     * This test checks the behavior of the findIntersections method for various cases:
     * 1. Empty collection of geometries.
     * 2. No geometries are intersected.
     * 3. Only one geometry is intersected.
     * 4. Some geometries (but not all) are intersected.
     * 5. All geometries are intersected.
     */
    @Test
    void testFindIntersections() {
        // =============== Boundary Value Analysis (BVA) ===============

        // TC01: Empty collection of geometries
        Geometries emptyGeometries = new Geometries();
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
        assertNull(emptyGeometries.findIntersections(ray), "Expected null for empty geometries");

        // TC02: No geometries are intersected
        Geometries geometries1 = new Geometries(
                new Sphere(new Point(0, 0, 5), 1),
                new Plane(new Point(0, 0, 5), new Vector(0, 0, 1))
        );
        ray = new Ray(new Point(0, 0, 0), new Vector(0, 1, 0)); // Parallel and does not intersect
        assertNull(geometries1.findIntersections(ray), "Expected null when no shapes are intersected");

        // TC03: Only one geometry is intersected
        Geometries geometries2 = new Geometries(
                new Sphere(new Point(0, 0, 5), 1), // Intersected
                new Plane(new Point(0, 0, -5), new Vector(0, 0, 1)) // Not intersected
        );
        ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)); // Intersects only the sphere
        List<Point> result = geometries2.findIntersections(ray);
        assertNotNull(result, "Expected non-null when one shape is intersected");
        assertEquals(2, result.size(), "Expected 2 intersection points with the sphere");

        // =============== Equivalence Partitioning (EP) ===============

        // TC04: Some geometries (but not all) are intersected
        Geometries geometries3 = new Geometries(
                new Sphere(new Point(0, 0, 5), 1), // Intersected
                new Plane(new Point(0, 0, 2), new Vector(0, 0, 1)), // Intersected
                new Plane(new Point(0, 0, 10), new Vector(0, -1, 0)) // Not intersected
        );
        ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        result = geometries3.findIntersections(ray);
        assertNotNull(result, "Expected non-null when some shapes are intersected");
        assertEquals(3, result.size(), "Expected 3 intersection points");

        // TC05: All geometries are intersected
        Geometries geometries4 = new Geometries(
                new Sphere(new Point(0, 0, 5), 1), // 2 intersection points
                new Plane(new Point(0, 0, 4), new Vector(0, 0, 1)), // 1 intersection point
                new Triangle(                      // 1 intersection point
                        new Point(-1, 1, 6),
                        new Point(1, 1, 6),
                        new Point(0, -1, 6)
                )
        );
        ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)); // Intersects all
        result = geometries4.findIntersections(ray);
        assertNotNull(result, "Expected non-null when all shapes are intersected");
        assertEquals(4, result.size(), "Expected 4 intersection points");
    }
}