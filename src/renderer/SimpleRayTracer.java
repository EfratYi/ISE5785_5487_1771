package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

/**
 * Simple ray tracer class.
 * Inherits from {@link RayTracerBase} and provides basic ray tracing functionality.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructor to initialize the simple ray tracer with a given scene.
     *
     * @param scene the scene to render
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Traces the given ray and calculates the color seen along the ray.
     *
     * @param ray the ray to trace
     * @return the color seen along the ray
     */
    @Override
    public Color traceRay(Ray ray) {
        // Find intersections between the ray and the geometries in the scene
        List<Point> intersections = scene.geometries.findIntersections(ray);

        // If no intersections are found, return the background color of the scene
        if (intersections == null) {
            return scene.background;
        }

        // Find the closest intersection point to the ray's origin
        Point closestPoint = ray.findClosestPoint(intersections);

        // Return the color of the closest point
        return calcColor( intersections.stream()
                .filter(intersection -> intersection.point.equals(closestPoint))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No intersection found for the closest point")));
    }


    private Color calcColor(Intersection intersection) {
        // Calculate the color at the intersection point
        // This is a placeholder implementation and should be replaced with actual shading logic
        return scene.background.add(intersection.geometry.getEmission());
    }
}