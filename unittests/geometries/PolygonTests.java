package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Polygon class.
 */
class PolygonTests {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals.
     */
    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}.
     * This test checks the constructor of the Polygon class.
     * It verifies that the constructor works for valid polygons and throws exceptions
     * for invalid polygons (e.g., wrong vertex order, non-planar vertices, concave polygons).
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct convex quadrangular with vertices in correct order
        assertDoesNotThrow(() -> new Polygon(new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(-1, 1, 1)),
                "Failed constructing a correct polygon");

        // TC02: Wrong vertices order
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0), new Point(-1, 1, 1)), //
                "Constructed a polygon with wrong order of vertices");

        // TC03: Not in the same plane
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 2, 2)), //
                "Constructed a polygon with vertices that are not in the same plane");

        // TC04: Concave quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                        new Point(0.5, 0.25, 0.5)), //
                "Constructed a concave polygon");

        // =============== Boundary Values Tests ==================

        // TC10: Vertex on a side of a quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                        new Point(0, 0.5, 0.5)),
                "Constructed a polygon with vertex on a side");

        // TC11: Last point = first point
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)),
                "Constructed a polygon with duplicate vertices");

        // TC12: Co-located points
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 1, 0)),
                "Constructed a polygon with co-located vertices");
    }

    /**
     * Test method for {@link geometries.Polygon#getNormal(primitives.Point)}.
     * This test checks the getNormal method of the Polygon class.
     * It ensures that the normal vector is a unit vector and is orthogonal to all edges of the polygon.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        Point[] pts =
                {new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1)};
        Polygon pol = new Polygon(pts);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");
        // generate the test result
        Vector result = pol.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), DELTA, "Polygon's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        for (int i = 0; i < 3; ++i)
            assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])), DELTA,
                    "Polygon's normal is not orthogonal to one of the edges");
    }

    /**
     * Test method for {@link geometries.Polygon#findIntersections(primitives.Ray)}.
     * This test checks the findIntersections method of the Polygon class.
     * It verifies the behavior of the method for rays that:
     * 1. Intersect inside the polygon.
     * 2. Miss the polygon.
     * 3. Are parallel to the polygon plane.
     * 4. Lie on the polygon plane but outside the polygon.
     * 5. Hit the edge or vertex of the polygon.
     */
    @Test
    void testFindIntersections() {

        // מקרה 1: קרן פוגעת בתוך הפוליגון (כיוון ישיר)
        Polygon polygon1 = new Polygon(
                new Point(0, 0, 0),
                new Point(5, 0, 0),
                new Point(5, 5, 0),
                new Point(0, 5, 0)
        );


    }

}