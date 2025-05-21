package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTests {
    private final Point p1 = new Point(1, 2, 3);
    private final Point p2 = new Point(4, 5, 6);

    /**
     * Test method for {@link Point#subtract(Point)}.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Normal point
        Vector v = p1.subtract(p2);
        assertEquals(new Vector(-3, -3, -3), v, "subtract() wrong result");
        // =============== Boundary Values Tests ==================
        // TC10: Same point
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1), "subtract() for same point should throw an exception");
    }

    /**
     * Test method for {@link Point#add(Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Normal point
        Point p = p1.add(new Vector(1, 1, 1));
        assertEquals(new Point(2, 3, 4), p, "add() wrong result");
        // =============== Boundary Values Tests ==================
        // TC10: Zero vector
        assertThrows(IllegalArgumentException.class, () -> p1.add(new Vector(0, 0, 0)), "add() for zero vector should throw an exception");
    }

    /**
     * Test method for {@link Point#distanceSquared(Point)}.
     */
    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Normal points
        double ds = p1.distanceSquared(p2);
        assertEquals(27, ds, "distanceSquared() wrong result");
        // =============== Boundary Values Tests ==================
        // TC10: Same point
        assertEquals(0, p1.distanceSquared(p1), "distanceSquared() for same point should be 0");
    }

    /**
     * Test method for {@link Point#distance(Point)}.
     */
    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Normal points
        double d = p1.distance(p2);
        assertEquals(Math.sqrt(27), d, "distance() wrong result");
        // =============== Boundary Values Tests ==================
        // TC10: Same point
        assertEquals(0, p1.distance(p1), "distance() for same point should be 0");
    }
}