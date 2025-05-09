package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link primitives.Ray} class.
 */
class RayTests {

    /**
     * Test method for {@link primitives.Ray#getPoint(double)}.
     * Verifies that the method correctly calculates a point on the ray
     * for various values of the parameter t.
     */
    @Test
    void testGetPoint() {
        Ray ray = new Ray(new Point(1, 2, 3), new Vector(1, 0, 0)); // Ray in the X-axis direction

        // ============ Equivalence Partitions Tests ==============

        // TC01: t = 0 => Returns the ray's origin
        assertEquals(
                new Point(1, 2, 3),
                ray.getPoint(0),
                "getPoint(0) should return the ray's origin"
        );

        // TC02: t = 1 => Moves one step in the ray's direction
        assertEquals(
                new Point(2, 2, 3),
                ray.getPoint(1),
                "getPoint(1) should return the point (2,2,3)"
        );

        // TC03: t = -2 => Moves two steps backward in the opposite direction
        assertEquals(
                new Point(-1, 2, 3),
                ray.getPoint(-2),
                "getPoint(-2) should return the point (-1,2,3)"
        );

        // TC04: t = 0.5 => Moves half a step forward
        assertEquals(
                new Point(1.5, 2, 3),
                ray.getPoint(0.5),
                "getPoint(0.5) should return the point (1.5,2,3)"
        );
    }

    /**
     * Test method for {@link primitives.Ray#findClosestPoint(List)}.
     * Verifies that the method correctly identifies the closest point
     * to the ray's origin from a list of points.
     */
    @Test
    void testFindClosestPoint() {
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));

        // ============ Equivalence Partition Test (EP) ==============

        // TC01: The closest point is in the middle of the list
        List<Point> points1 = List.of(
                new Point(5, 0, 0),
                new Point(2, 0, 0),  // Closest point
                new Point(10, 0, 0)
        );
        assertEquals(new Point(2, 0, 0), ray.findClosestPoint(points1), "TC01: The closest point is in the middle of the list");

        // =============== Boundary Values Tests (BVA) ==================

        // TC02: Empty list
        assertNull(ray.findClosestPoint(List.of()), "TC02: Empty list");

        // TC03: The first point is the closest
        List<Point> points3 = List.of(
                new Point(1, 0, 0),  // Closest point
                new Point(3, 0, 0),
                new Point(5, 0, 0)
        );
        assertEquals(new Point(1, 0, 0), ray.findClosestPoint(points3), "TC03: The first point is the closest");

        // TC04: The last point is the closest
        List<Point> points4 = List.of(
                new Point(5, 0, 0),
                new Point(3, 0, 0),
                new Point(1, 0, 0)   // Closest point
        );
        assertEquals(new Point(1, 0, 0), ray.findClosestPoint(points4), "TC04: The last point is the closest");
    }
}