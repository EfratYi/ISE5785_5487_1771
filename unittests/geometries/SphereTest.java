package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link geometries.Sphere} class.
 * This class includes tests for the methods:
 * <ul>
 *     <li>{@link geometries.Sphere#getNormal(primitives.Point)}</li>
 *     <li>{@link geometries.Sphere#findIntersections(primitives.Ray)}</li>
 * </ul>
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
     * It verifies the behavior of the method for various rays:
     * <ul>
     *     <li>Rays that intersect the sphere.</li>
     *     <li>Rays that do not intersect the sphere.</li>
     *     <li>Rays that are tangent to the sphere.</li>
     *     <li>Rays that start inside or on the sphere.</li>
     * </ul>
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(new Point(0, 0, 0), 1);

        // TC01: Ray intersects the sphere at one point
        Ray ray1 = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        var result1 = sphere.findIntersections(ray1);
        assertNotNull(result1, "Ray intersects the sphere but got null");
        assertEquals(1, result1.size(), "Wrong number of intersection points");
        assertEquals(new Point(0, 0, 1), result1.get(0), "Wrong intersection point");

        // TC02: Ray does not intersect the sphere
        Ray ray2 = new Ray(new Point(0, 1, 0), new Vector(0, 2, 0));
        var result2 = sphere.findIntersections(ray2);
        assertNull(result2, "Ray does not intersect the sphere but got a point");

        // TC03: Ray intersects the sphere at one point
        Ray ray3 = new Ray(new Point(0, 0, -1), new Vector(0, 0, 1));
        var result3 = sphere.findIntersections(ray3);
        assertNotNull(result3, "Ray intersects the sphere but got null");
        assertEquals(1, result3.size(), "Wrong number of intersection points");
        assertEquals(new Point(0, 0, 1), result3.get(0), "Wrong intersection point");

        // TC04: Ray does not intersect the sphere
        Ray ray4 = new Ray(new Point(0, 0, 2), new Vector(0, 0, 1));
        var result4 = sphere.findIntersections(ray4);
        assertNull(result4, "Ray does not intersect the sphere but got a point");

        // TC05: Ray intersects the sphere at two points
        Ray ray5 = new Ray(new Point(0, 0, -2), new Vector(0, 0, 1));
        var result5 = sphere.findIntersections(ray5);
        assertNotNull(result5, "Ray intersects the sphere but got null");
        assertEquals(2, result5.size(), "Wrong number of intersection points");
        assertEquals(new Point(0, 0, -1), result5.get(0), "Wrong intersection point");
        assertEquals(new Point(0, 0, 1), result5.get(1), "Wrong intersection point");

        // TC06: Ray starts inside the sphere
        Ray ray6 = new Ray(new Point(0, 0.5, 0), new Vector(0, 1, 0));
        var result6 = sphere.findIntersections(ray6);
        assertNotNull(result6, "Ray intersects the sphere but got null");
        assertEquals(1, result6.size(), "Wrong number of intersection points");
        assertEquals(new Point(0, 1, 0), result6.get(0), "Wrong intersection point");

        // TC07: Ray starts inside the sphere and intersects at one point
        Ray ray7 = new Ray(new Point(0, 0.5, 0), new Vector(0, 0, 1));
        var result7 = sphere.findIntersections(ray7);
        assertNotNull(result7, "Ray intersects the sphere but got null");
        assertEquals(1, result7.size(), "Wrong number of intersection points");
        assertEquals(new Point(0, 0.5, Math.sqrt(0.75)), result7.get(0), "Wrong intersection point");

        // TC08: Ray starts outside the sphere and does not intersect
        Ray ray8 = new Ray(new Point(0, 2, 0), new Vector(0, 0, 1));
        var result8 = sphere.findIntersections(ray8);
        assertNull(result8, "Ray does not intersect the sphere but got a point");

        // TC09: Ray intersects the sphere at one point
        Ray ray9 = new Ray(new Point(-0.5, Math.sqrt(0.75), 0), new Vector(1, 0, 0));
        var result9 = sphere.findIntersections(ray9);
        assertNotNull(result9, "Ray intersects the sphere but got null");
        assertEquals(1, result9.size(), "Wrong number of intersection points");
        assertEquals(new Point(0.5, Math.sqrt(0.75), 0), result9.get(0), "Wrong intersection point");

        // TC10: Ray does not intersect the sphere
        Ray ray10 = new Ray(new Point(0, 1, 0), new Vector(0, 1, 0.5));
        var result10 = sphere.findIntersections(ray10);
        assertNull(result10, "Ray does not intersect the sphere but got a point");

        // TC11: Ray starts on the sphere and does not intersect
        Ray ray11 = new Ray(new Point(0, 0, 1), new Vector(0, 0, 1));
        var result11 = sphere.findIntersections(ray11);
        assertNull(result11, "Ray does not intersect the sphere but got a point");

        // TC12: Ray starts outside the sphere and does not intersect
        Ray ray12 = new Ray(new Point(0, 1, 2), new Vector(0, 0, 1));
        var result12 = sphere.findIntersections(ray12);
        assertNull(result12, "Ray does not intersect the sphere but got a point");

        // TC13: Ray starts outside the sphere and does not intersect
        Ray ray13 = new Ray(new Point(0, 1.5, -0.5), new Vector(0, 0, 1));
        var result13 = sphere.findIntersections(ray13);
        assertNull(result13, "Ray intersects the sphere but got null");
    }
}