package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link geometries.Sphere} class.
 */
public class SphereTest {

    /**
     * Vector v1 with components (1, 0, 0).
     */
    Vector v1 = new Vector(1, 0, 0);

    /**
     * Point p1 with coordinates (0, 0, 0).
     */
    Point p1 = new Point(0, 0, 0);

    /**
     * Point p2 with coordinates (1, 0, 0).
     */
    Point p2 = new Point(1, 0, 0);

    /**
     * A point used in some tests.
     */
    private final Point p001 = new Point(0, 0, 1);

    /**
     * A point used in some tests.
     */
    private final Point p100 = new Point(1, 0, 0);

    /**
     * A vector used in some tests.
     */
    private final Vector v001 = new Vector(0, 0, 1);

    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     * This test checks the getNormal method of the Sphere class.
     * It verifies that the normal vector is calculated correctly and is normalized.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests =================
        // TC01: Simple test
        assertEquals(v1.normalize(), new Sphere(p1, 1).getNormal(p2), "getNormal() wrong result");
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
     * This test checks the findIntersections method of the Sphere class.
     * It verifies the behavior of the method for rays that:
     * 1. Do not intersect the sphere.
     * 2. Intersect the sphere at two points.
     * 3. Start inside the sphere.
     * 4. Start after the sphere.
     * 5. Handle boundary cases such as tangency and rays passing through the center.
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(p100, 1);

        final Point gp1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        final Point gp2 = new Point(1.53484692283495, 0.844948974278318, 0);
        final var exp = List.of(gp1, gp2);

        final Vector v310 = new Vector(3, 1, 0);
        final Vector v110 = new Vector(1, 1, 0);

        final Point p01 = new Point(-1, 0, 0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(p01, v110)), "Ray's line out of sphere");

        // TC02: Ray starts before and crosses the sphere (2 points)
        final var result1 = sphere.findIntersections(new Ray(p01, v310));
        assertNotNull(result1, "Can't be empty list");
        assertEquals(2, result1.size(), "Wrong number of points");
        assertEquals(exp, result1, "Ray crosses sphere");

        // TC03: Ray starts inside the sphere (1 point)
        // (Test case not implemented)

        // TC04: Ray starts after the sphere (0 points)
        // (Test case not implemented)

        // =============== Boundary Values Tests ==================

        // **** Group 1: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 point)
        // (Test case not implemented)

        // TC12: Ray starts at sphere and goes outside (0 points)
        // (Test case not implemented)

        // **** Group 2: Ray's line goes through the center
        // TC21: Ray starts before the sphere (2 points)
        // (Test case not implemented)

        // TC22: Ray starts at sphere and goes inside (1 point)
        // (Test case not implemented)

        // TC23: Ray starts inside (1 point)
        // (Test case not implemented)

        // TC24: Ray starts at the center (1 point)
        // (Test case not implemented)

        // TC25: Ray starts at sphere and goes outside (0 points)
        // (Test case not implemented)

        // TC26: Ray starts after sphere (0 points)
        // (Test case not implemented)

        // **** Group 3: Ray's line is tangent to the sphere (all tests 0 points)
        // TC31: Ray starts before the tangent point
        // (Test case not implemented)

        // TC32: Ray starts at the tangent point
        // (Test case not implemented)

        // TC33: Ray starts after the tangent point
        // (Test case not implemented)

        // **** Group 4: Special cases
        // TC41: Ray's line is outside sphere, ray is orthogonal to ray start to sphere's center line
        // (Test case not implemented)

        // TC42: Ray's starts inside, ray is orthogonal to ray start to sphere's center line
        // (Test case not implemented)
    }
}