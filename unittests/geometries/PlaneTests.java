package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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

    @Test
    void testFindIntersections() {
        Plane plane = new Plane(
                new Point(0, 0, 1),
                new Point(1, 0, 1),
                new Point(0, 1, 1)); //  z = 1

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray intersects the plane
        Ray ray1 = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        var result1 = plane.findIntersections(ray1);
        assertNotNull(result1, "Ray intersects the plane but got null");
        assertEquals(1, result1.size(), "Wrong number of intersection points");
        assertEquals(new Point(0, 0, 1), result1.get(0), "Wrong intersection point");

        // TC02: Ray does not intersect the plane (ray goes away from plane)
        Ray ray2 = new Ray(new Point(0, 0, 2), new Vector(0, 0, 1));
        assertNull(plane.findIntersections(ray2), "Ray does not intersect the plane but got a point");

        // =============== Boundary Values Tests ==================

        // TC03: Ray is parallel and outside the plane
        Ray ray3 = new Ray(new Point(0, 0, 2), new Vector(1, 0, 0));
        assertNull(plane.findIntersections(ray3), "Parallel ray out of the plane should not intersect");

        // TC04: Ray is parallel and lies in the plane
        Ray ray4 = new Ray(new Point(0, 0, 1), new Vector(1, 0, 0));
        assertNull(plane.findIntersections(ray4), "Ray in the plane should return null by implementation");

        // TC05: Ray is orthogonal and starts before the plane
        Ray ray5 = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        assertEquals(List.of(new Point(0, 0, 1)), plane.findIntersections(ray5), "Orthogonal ray should intersect");

        // TC06: Ray is orthogonal and starts in the plane
        Ray ray6 = new Ray(new Point(0, 0, 1), new Vector(0, 0, 1));
        assertNull(plane.findIntersections(ray6), "Orthogonal ray from the plane should return null");

        // TC07: Ray is orthogonal and starts after the plane
        Ray ray7 = new Ray(new Point(0, 0, 2), new Vector(0, 0, 1));
        assertNull(plane.findIntersections(ray7), "Orthogonal ray after the plane should not intersect");

        // TC08: Ray starts at the reference point of the plane
        Ray ray8 = new Ray(new Point(0, 0, 1), new Vector(1, 1, 1));
        assertNull(plane.findIntersections(ray8), "Ray starts on the plane, should return null");
    }
}