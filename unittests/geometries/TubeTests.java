package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the Tube class.
 */
public class TubeTests {

    /**
     * Test method for {@link geometries.Tube#getNormal(Point)}.
     * This test checks the getNormal method of the Tube class.
     */
    @Test
    void testgetNormal() {
        Tube tube = new Tube(1, new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)));

        // 1️⃣ Equivalence partition: point on the regular surface
        Point p1 = new Point(1, 0, 5); // Point on the surface
        Vector normal1 = tube.getNormal(p1);
        assertEquals(new Vector(1, 0, 0), normal1, "Normal on the side is incorrect");

        // 2️⃣ Boundary case: point creating a right angle with the ray head
        Point p2 = new Point(1, 0, 0); // Point opposite the ray head
        Vector normal2 = tube.getNormal(p2);
        assertEquals(new Vector(1, 0, 0), normal2, "Normal at edge case is incorrect");

        // 3️⃣ Check that the normal is a unit vector (length 1)
        assertEquals(1, normal1.length(), 1e-10, "Normal is not a unit vector");
        assertEquals(1, normal2.length(), 1e-10, "Normal is not a unit vector");
    }
}