package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Vector class.
 */
class VectorTests {
    private static final double DELTA = 0.000001;
    private final Vector v123 = new Vector(1, 2, 3);
    private final Vector v03M2 = new Vector(0, 3, -2);
    private final Vector vM2M4M6 = new Vector(-1, -2, -3);

    /**
     * Test method for {@link primitives.Vector#Vector(double, double, double)}.
     * This test checks the constructor of the Vector class.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Normal vector
        assertDoesNotThrow(() -> new Vector(1, 2, 3), "Failed constructing a normal vector");
        // =============== Boundary Values Tests ==================
        // TC10: Zero vector
        assertThrows(IllegalArgumentException.class, () -> new Vector(0, 0, 0), "Constructed a zero vector");
    }

    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}.
     * This test checks the add method of the Vector class.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Normal vector
        Vector v = v123.add(v03M2);
        assertEquals(new Vector(1, 5, 1), v, "add() wrong result");

        // =============== Boundary Values Tests ==================
        // TC10: Zero vector (should throw an exception)
         assertThrows(IllegalArgumentException.class, () -> v123.add(vM2M4M6), "add() for zero vector should throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#scale(double)}.
     * This test checks the scale method of the Vector class.
     */
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Normal vector
        Vector v = v123.scale(2);
        assertEquals(new Vector(2, 4, 6), v, "scale() wrong result");
        // TC02: Normal vector
        v = v123.scale(-1);
        assertEquals(new Vector(-1, -2, -3), v, "scale() wrong result");
        // =============== Boundary Values Tests ==================
        // TC10: Zero vector (should throw an exception)
        assertThrows(IllegalArgumentException.class, () -> v123.scale(0), //
                "scale() for zero vector should throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)}.
     * This test checks the dotProduct method of the Vector class.
     */
    @Test
    void testDotProduct() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Normal vector
        double dp = v123.dotProduct(v03M2);
        assertEquals(0, dp, DELTA, "dotProduct() wrong result");
        // TC02: Normal vector
        dp = v123.dotProduct(vM2M4M6);
        assertEquals(-14, dp, DELTA, "dotProduct() wrong result");
        // TC03: Dot product of a vector with itself
        dp = v123.dotProduct(v123);
        assertEquals(14, dp, DELTA, "dotProduct() wrong result");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
     * This test checks the crossProduct method of the Vector class.
     */
    @Test
    void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============
        Vector vr = v123.crossProduct(v03M2);
        // TC01: Test that length of cross-product is proper (orthogonal vectors taken
        // for simplicity)
        assertEquals(v123.length() * v03M2.length(), vr.length(), DELTA, "crossProduct() wrong result length");
        assertEquals(0, vr.dotProduct(v123), "crossProduct() result is not orthogonal to 1st operand");
        assertEquals(0, vr.dotProduct(v03M2), "crossProduct() result is not orthogonal to 2nd operand");
        // =============== Boundary Values Tests ==================
        // TC11: test zero vector from cross-product of parallel vectors
        assertThrows(IllegalArgumentException.class, () -> v123.crossProduct(vM2M4M6), //
                "crossProduct() for parallel vectors does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#lengthSquared()}.
     * This test checks the lengthSquared method of the Vector class.
     */
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Normal vector
        double ls = v123.lengthSquared();
        assertEquals(14, ls, DELTA, "lengthSquared() wrong result");

    }

    /**
     * Test method for {@link primitives.Vector#length()}.
     * This test checks the length method of the Vector class.
     */
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Normal vector
        double l = v123.length();
        assertEquals(Math.sqrt(14), l, DELTA, "length() wrong result");
    }

    /**
     * Test method for {@link primitives.Vector#normalize()}.
     * This test checks the normalize method of the Vector class.
     */
    @Test
    void testNormalize() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Normal vector
        Vector v = v123.normalize();
        assertEquals(1, v.length(), DELTA, "normalize() does not return a unit vector");
    }
}