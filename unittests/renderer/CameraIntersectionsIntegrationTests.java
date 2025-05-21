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

/**
 * Integration tests for the {@link Camera} class and its interaction with geometries.
 * Verifies the number of intersections between rays constructed by the camera and various geometries.
 */
class CameraIntersectionsIntegrationTests {

    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setVpDistance(1)
            .setDirection(new Vector(0, 0, -1), new Vector(0, -1, 0))
            .setVpSize(3, 3);

    private final Camera camera1 = cameraBuilder.setLocation(Point.ZERO).build();
    private final Camera camera2 = cameraBuilder.setLocation(new Point(0, 0, 0.5)).build();

    /**
     * Helper method to assert the number of intersections between rays from the camera
     * and a given geometry.
     *
     * @param camera         the camera used to construct rays
     * @param geometry       the geometry to test intersections with
     * @param expectedAmount the expected number of intersections
     */
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

    /**
     * Test method for intersections between rays from the camera and a {@link Sphere}.
     * Verifies the number of intersections for various sphere configurations.
     */
    @Test
    public void testSphereIntersections() {
        assertCountIntersections(camera1, new Sphere(new Point(0, 0, -3), 1d), 2);
        assertCountIntersections(camera2, new Sphere(new Point(0, 0, -2.5), 2.5), 18);
        assertCountIntersections(camera2, new Sphere(new Point(0, 0, -2), 2d), 10);
        assertCountIntersections(camera2, new Sphere(new Point(0, 0, -1), 4d), 9);
        assertCountIntersections(camera2, new Sphere(new Point(0, 0, 1), 0.5), 0);
    }

    /**
     * Test method for intersections between rays from the camera and a {@link Plane}.
     * Verifies the number of intersections for various plane configurations.
     */
    @Test
    public void testPlaneIntersections() {
        assertCountIntersections(camera2, new Plane(new Point(0, 0, -5), new Vector(0, 0, 1)), 9);
        assertCountIntersections(camera2, new Plane(new Point(0, 0, -5), new Vector(0, 1, 2)), 9);
        assertCountIntersections(camera2, new Plane(new Point(0, 0, -5), new Vector(0, 1, 1)), 6);
        assertCountIntersections(camera2, new Plane(new Point(0, 0, -5), new Vector(0, 1, 1)), 6);
    }

    /**
     * Test method for intersections between rays from the camera and a {@link Triangular}.
     * Verifies the number of intersections for various triangular configurations.
     */
    @Test
    public void testTriangleIntersections() {
        assertCountIntersections(camera2, new Triangular(new Point(0, 1, -2), new Point(1, -1, -2), new Point(-1, -1, -2)), 1);
        assertCountIntersections(camera2, new Triangular(new Point(0, 20, -2), new Point(1, -1, -2), new Point(-1, -1, -2)), 2);
    }
}