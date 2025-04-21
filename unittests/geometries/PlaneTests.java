package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Plane class.
 */
class PlaneTests {

    /**
     * Test method for {@link geometries.Plane#Plane(Point, Point, Point)}.
     * This test checks the constructor of the Plane class.
     */
    @Test
    void testConstructor() {
        assertDoesNotThrow(() -> new Plane(
                        new Point(0, 0, 0),
                        new Point(1, 0, 0),
                        new Point(1, 2, 0)),
                "Failed constructing a correct plane");

        assertThrows(IllegalArgumentException.class,
                () -> new Plane(new Point(0, 0, 1),
                        new Point(0, 0, 2),
                        new Point(0, 0, 3)),
                "Constructed a plane with wrong order of vertices");

        assertThrows(IllegalArgumentException.class,
                () -> new Plane(new Point(1, 1, 1),
                        new Point(1, 1, 1),
                        new Point(2, 2, 2)),
                "Constructed a plane with duplicate points P1 and P2");

        assertThrows(IllegalArgumentException.class,
                () -> new Plane(new Point(1, 1, 1),
                        new Point(2, 2, 2),
                        new Point(1, 1, 1)),
                "Constructed a plane with duplicate points P1 and P3");

        assertThrows(IllegalArgumentException.class,
                () -> new Plane(new Point(1, 1, 1),
                        new Point(2, 2, 2),
                        new Point(2, 2, 2)),
                "Constructed a plane with duplicate points P2 and P3");

        assertThrows(IllegalArgumentException.class,
                () -> new Plane(new Point(1, 1, 1),
                        new Point(1, 1, 1),
                        new Point(1, 1, 1)),
                "Constructed a plane with all points being the same");
    }

    /**
     * Test method for {@link geometries.Plane#getNormal(Point)}.
     * This test checks the getNormal method of the Plane class.
     */
    @Test
    void testgetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Plane pl = new Plane(new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1));
        assertEquals(new Vector(1, 1, 1).normalize(), pl.getNormal(new Point(0, 0, 0)), "Bad normal to triangle");
    }

    /**
     * Test method for {@link geometries.Plane# getNormal()}.
     * This test checks the getNormal method of the Plane class.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Plane pl = new Plane(new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1));
        assertEquals(new Vector(1, 1, 1).normalize(), pl.getNormal(null), "Bad normal to triangle");
    }
}