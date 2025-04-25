package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Geometries class.
 */
class GeometriesTests {

    /**
     * Test method for {@link geometries.Geometries#add(Intersectable...)}.
     * This test verifies the behavior of the add method in the Geometries class.
     * It checks the addition of geometries to the collection and ensures the collection behaves as expected.
     */
    @Test
    void testAdd() {
        Geometries geometries = new Geometries();
        assertTrue(geometries.findIntersections(new Ray(new Point(0, 0, 0), new Vector(1, 0, 0))) == null,
                "Expected null for an empty Geometries collection");

        geometries.add(new Sphere(new Point(1, 0, 0), 1));
        assertFalse(geometries.findIntersections(new Ray(new Point(0, 0, 0), new Vector(1, 0, 0))) == null,
                "Expected non-null after adding a geometry");
    }

    /**
     * Test method for {@link geometries.Geometries#findIntersections(Ray)}.
     * This test verifies the behavior of the findIntersections method in the Geometries class.
     * It checks various scenarios, including:
     * - An empty collection
     * - No intersections
     * - One intersection
     * - Some intersections
     * - All geometries intersecting
     */
    @Test
    void testFindIntersections() {
        Ray testRay = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));

        // Empty collection (BVA)
        Geometries geometries = new Geometries();
        assertNull(geometries.findIntersections(testRay), "Expected null when the Geometries collection is empty");

        // No shape intersects (BVA)
        geometries = new Geometries(
                new Sphere(new Point(5, 5, 5), 1),
                new Sphere(new Point(-5, -5, -5), 1)
        );
        assertNull(geometries.findIntersections(testRay), "Expected null when no shapes intersect");

        // One shape intersects (BVA)
        geometries = new Geometries(
                new Sphere(new Point(1, 0, 0), 1),
                new Sphere(new Point(5, 5, 5), 1)
        );
        List<Point> result = geometries.findIntersections(testRay);
        assertNotNull(result, "Expected at least one intersection");
        assertEquals(2, result.size(), "Expected one intersection point");

        // Some shapes intersect (EP)
        geometries = new Geometries(
                new Sphere(new Point(1, 0, 0), 1),
                new Sphere(new Point(2, 0, 0), 1),
                new Sphere(new Point(5, 5, 5), 1)
        );
        result = geometries.findIntersections(testRay);
        assertNotNull(result, "Expected intersections when some shapes intersect");
        assertEquals(4, result.size(), "Expected four intersection points");

        // All shapes intersect (BVA)
        geometries = new Geometries(
                new Sphere(new Point(1, 0, 0), 1),
                new Sphere(new Point(2, 0, 0), 1),
                new Sphere(new Point(3, 0, 0), 1)
        );
        result = geometries.findIntersections(testRay);
        assertNotNull(result, "Expected intersections when all shapes intersect");
        assertEquals(6, result.size(), "Expected six intersection points");

    }
}