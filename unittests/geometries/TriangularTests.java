package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link geometries.Triangular} class.
 */
class TriangularTests {

    /**
     * Test method for {@link geometries.Triangular#getNormal(primitives.Point)}.
     * This test checks the getNormal method of the Triangular class.
     * It verifies that the normal vector is calculated correctly for a given point on the triangle.
     */
    @Test
    void testGetNormal() {
        // Arrange
        Point a = new Point(0, 0, 0);
        Point b = new Point(1, 0, 0);
        Point c = new Point(0, 1, 0);
        Triangular triangle = new Triangular(a, b, c);

        // Act
        Vector normal = triangle.getNormal(a);

        // Assert
        assertEquals(new Vector(0, 0, 1), normal, "Wrong normal vector");
    }

    /**
     * Test method for {@link geometries.Triangular#findIntersections(primitives.Ray)}.
     * This test checks the findIntersections method of the Triangular class.
     * It verifies the behavior of the method for rays that:
     * 1. Intersect inside the triangle.
     * 2. Miss the triangle (outside against edge or vertex).
     * 3. Intersect exactly on the edge or vertex of the triangle.
     */
    @Test
    void testFindIntersections() {
        // Arrange
        Triangular triangle = new Triangular(
                new Point(0, 0, 0),
                new Point(1, 0, 0),
                new Point(0, 1, 0)
        );

        // TC01: Ray intersects inside the triangle
        Ray ray1 = new Ray(new Point(0.25, 0.25, -1), new Vector(0, 0, 1));
        assertEquals(
                List.of(new Point(0.25, 0.25, 0)),
                triangle.findIntersections(ray1),
                "Ray should intersect inside the triangle"
        );

        // TC02: Ray intersects outside the triangle - against edge
        Ray ray2 = new Ray(new Point(-1, 0.5, -1), new Vector(0, 0, 1));
        assertNull(
                triangle.findIntersections(ray2),
                "Ray should not intersect (outside against edge)"
        );

        // TC03: Ray intersects outside the triangle - against vertex
        Ray ray3 = new Ray(new Point(-0.5, -0.5, -1), new Vector(0, 0, 1));
        assertNull(
                triangle.findIntersections(ray3),
                "Ray should not intersect (outside against vertex)"
        );

        // TC04: Ray intersects exactly on edge
        Ray ray4 = new Ray(new Point(0.5, 0, -1), new Vector(0, 0, 1));
        assertNull(
                triangle.findIntersections(ray4),
                "Ray should not intersect (on edge)"
        );

        // TC05: Ray intersects exactly on vertex
        Ray ray5 = new Ray(new Point(0, 0, -1), new Vector(0, 0, 1));
        assertNull(
                triangle.findIntersections(ray5),
                "Ray should not intersect (on vertex)"
        );
    }

}