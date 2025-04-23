package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link primitives.Ray} class.
 */
class RayTests {

    /**
     * Test method for {@link primitives.Ray#getPoint(double)}.
     * This test checks the getPoint method of the Ray class.
     * It verifies that the method correctly calculates a point on the ray
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
}