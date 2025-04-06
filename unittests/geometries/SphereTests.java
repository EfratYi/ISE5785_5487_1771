package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Sphere class.
 */
class SphereTests {

    /**
     * Test method for {@link geometries.Sphere#getNormal(Point)}.
     * This test checks the getNormal method of the Sphere class.
     */
    @Test
    void testgetNormal() {
        Sphere sphere = new Sphere(new Point(0, 0, 0), 1);
        Point pointOnSurface = new Point(2, 0, 0);
        Vector expectedNormal = new Vector(1, 0, 0);
        Vector actualNormal = sphere.getNormal(pointOnSurface);
        assertEquals(expectedNormal, actualNormal, "Bad normal to sphere");
    }
}