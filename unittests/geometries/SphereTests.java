package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class SphereTests {

    @Test
    void testgetNormal() {
        Sphere sphere= new Sphere(new Point(0,0,0),1);
        Point pointOnSurface =new Point(2,0,0);
        Vector expectedNormal = new Vector(1,0,0);
        Vector actualNormal = sphere.getNormal(pointOnSurface);
        assertEquals(expectedNormal,actualNormal,"Bad normal to sphere");
    }
}