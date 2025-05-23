package renderer;

import geometries.Geometries;
import geometries.Intersectable;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;
import java.util.Objects;



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

        // Check if the scene has geometries
        if (Objects.equals(scene.geometries, new Geometries())) {
            return scene.background; // Return background color if no geometries
        }
        // Check if the ray intersects with any geometries in the scene
        if (scene.geometries.calculateIntersections(ray) == null) {
            return scene.background; // Return background color if no intersection
        }
        Intersectable.Intersection closestIntersection = ray.findClosestIntersection(scene.geometries.calculateIntersections(ray));

        if (closestIntersection == null) {
            return scene.background; // Return background color if no intersection
        }
        // Calculate the color at the intersection point
        return calcColor(closestIntersection);
    }


    private Color calcColor(Intersectable.Intersection intersection) {
        return scene
                .ambientLight.getIntensity().scale(intersection.geometry.getMaterial().kA).add(intersection.geometry.getEmission());    }

}
