package renderer;

import geometries.Intersectable;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangular;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class CameraIntersectionsIntegrationTestss {


    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setDirection(new Point(0, 0, 1), Vector.AXIS_Y)
            .setVpSize(3, 3)
            .setVpDistance(1);

    private final Camera camera1 = cameraBuilder.setLocation(Point.ZERO).build();
    private final Camera camera2 = cameraBuilder.setLocation(new Point(0, 0, 0.5)).build();


    private void assertCountIntersections(Camera camera, Intersectable geometry, int expectedAmount) {
        int totalIntersections = 0;

        int nX = 3;
        int nY = 3;

        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                Ray ray = camera.constructRay(nX, nY, j, i);
                var intersections = geometry.findIntersections(ray);
                if (intersections != null) {
                    totalIntersections += intersections.size();
                }
            }
        }
        assertEquals(expectedAmount, totalIntersections, "Wrong number of intersections");

    }
    @Test
    public void testSphereIntersections() {
        assertCountIntersections(camera1, new Sphere(new Point(0, 0, -3), 1d), 2);
        assertCountIntersections(camera2, new Sphere(new Point(0, 0, -2.5), 2.5), 18);
        assertCountIntersections(camera2, new Sphere(new Point(0, 0, -2), 2d), 10);
        assertCountIntersections(camera2, new Sphere(new Point(0, 0, -1), 4d), 9);
        assertCountIntersections(camera1, new Sphere(new Point(0, 0, 1), 0.5), 0);

    }

    @Test
    public void testPlaneIntersections() {
        assertCountIntersections(camera1, new Plane(new Point(0, 0, -5), new Vector(0, 0, 1)), 9);
        assertCountIntersections(camera1, new Plane(new Point(0, 0, -5), new Vector(0, 1, 2)), 9);
        assertCountIntersections(camera1, new Plane(new Point(0, 0, -5), new Vector(0, 1, 1)), 6);
        assertCountIntersections(camera1, new Plane(new Point(0, 0, -5), new Vector(0, 1, 1)), 6);

    }

    @Test
    public void testTriangleIntersections() {
        assertCountIntersections(camera1, new Triangular(new Point(1, 1, -2), new Point(-1, 1, -2), new Point(0, -1, -2)), 1);
        assertCountIntersections(camera1, new Triangular(new Point(1, 1, -2), new Point(1, 1, -2), new Point(0,-20,-2)), 2);


    }
}