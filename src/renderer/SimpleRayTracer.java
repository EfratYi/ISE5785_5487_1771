package renderer;

import geometries.Geometries;
import geometries.Intersectable;
import geometries.Material;
import primitives.Color;
import primitives.Double3;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import lighting.LightSource;
import primitives.Util;

import scene.Scene;

import java.util.List;
import java.util.Objects;

/**
 * Simple ray tracer class.
 * Inherits from {@link RayTracerBase} and provides basic ray tracing functionality.
 */
public class SimpleRayTracer extends RayTracerBase {


    private static final double DELTA = 0.1;

    /**
     * Constructor to initialize the simple ray tracer with a given scene.
     *
     * @param scene the scene to render
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Calculates the local lighting effects (diffuse and specular) at the intersection point.
     *
     * @param intersection the intersection point
     * @return the color resulting from the local lighting effects
     */
    private Color calcColorLocalEffects(Intersectable.Intersection intersection) {
        Color color = intersection.geometry.getEmission();
        for (LightSource lightSource : scene.lights) {
            if (!setLightSource(intersection, lightSource)) {
                continue;
            }
            if (!unshaded(intersection)) {
                continue;
            }

            Color iL = lightSource.getIntensity(intersection.point);
            Double3 diff = calcDiffusive(intersection);
            Double3 spec = calcSpecular(intersection);
            color = color.add(iL.scale(diff.add(spec)));
        }
        return color;
    }

    /**
     * Calculates the specular reflection component based on the Phong reflection model.
     *
     * @param intersection the intersection data
     * @return the specular reflection coefficient
     */
    private Double3 calcSpecular(Intersectable.Intersection intersection){
        Vector l = intersection.lightDirection;
        Vector n = intersection.normalAtPoint;
        Vector v = intersection.rayDirection;

        double ln = Util.alignZero(l.dotProduct(n));
        if (ln == 0) return Double3.ZERO;

        Vector r = l.subtract(n.scale(2 * ln)).normalize();
        double minusVR = -Util.alignZero(v.dotProduct(r));
        if (minusVR <= 0) return Double3.ZERO;

        return intersection.material.kS.scale(Math.pow(minusVR, intersection.material.sh));
    }

    /**
     * Calculates the diffuse reflection component based on the Lambertian model.
     *
     * @param intersection the intersection data
     * @return the diffuse reflection coefficient
     */
    private Double3 calcDiffusive(Intersectable.Intersection intersection){
        double nl = intersection.dotProductLightNormal;
        double nlAbs = Math.abs(Util.alignZero(nl));
        return intersection.material.kD.scale(nlAbs);
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
        return calcColor(closestIntersection, ray);
    }

    /**
     * Calculates the final color at the intersection point, including ambient and local lighting.
     *
     * @param intersection the intersection data
     * @param ray the ray that hit the geometry
     * @return the resulting color at the intersection point
     */
    private Color calcColor(Intersectable.Intersection intersection, Ray ray) {
        // Initialize ray direction, normal, and dot product
        if (!preprocessIntersection(intersection, ray.getDirection())) {
            return Color.BLACK;
        }

        return scene
                .ambientLight.getIntensity().scale(intersection.geometry.getMaterial().kA)
                .add(calcColorLocalEffects(intersection));
    }

    /**
     * Performs preprocessing on the intersection:
     * Initializes the ray direction, normal at the hit point, and their dot product.
     *
     * @param intersection the intersection to preprocess
     * @param rayDirection the direction of the incoming ray
     * @return true if the dot product of the normal and ray direction is non-zero, false otherwise
     */
    private boolean preprocessIntersection(Intersectable.Intersection intersection, Vector rayDirection) {
        // Store ray direction
        intersection.rayDirection = rayDirection;

        // Compute the normal at the hit point from the geometry
        Vector normal = intersection.geometry.getNormal(intersection.point);
        intersection.normalAtPoint = normal;

        // Compute dot product
        intersection.dotProductRayNormal = normal.dotProduct(rayDirection);

        // Valid intersection only if the dot product is non-zero
        return intersection.dotProductRayNormal != 0;
    }

    /**
     * Initializes the light-related fields of the intersection.
     *
     * @param intersection the intersection to update
     * @param light the light source
     * @return false if both dot products (with ray direction and with light direction) are zero, true otherwise
     */
    private boolean setLightSource(Intersectable.Intersection intersection, LightSource light) {
        // Store light source
        intersection.lightSource = light;

        // Compute light direction from the point to the light
        intersection.lightDirection = light.getL(intersection.point);

        // Compute dot product between normal and light direction
        intersection.dotProductLightNormal = intersection.normalAtPoint.dotProduct(intersection.lightDirection);

        // Check: both dot products should have the same sign (i.e., their product should be positive)
        return intersection.dotProductRayNormal * intersection.dotProductLightNormal > 0;
    }


    private boolean unshaded(Intersectable.Intersection intersection) {
        Vector l = intersection.lightDirection;
        Vector n = intersection.normalAtPoint;
        double nl = intersection.dotProductLightNormal;
        LightSource lightSource = intersection.lightSource;

        Vector epsVector = n.scale(nl < 0 ? DELTA : -DELTA);
        Point movedPoint = intersection.point.add(epsVector);
        Ray shadowRay = new Ray(movedPoint, l.scale(-1));

        List<Intersectable.Intersection> shadowIntersections = scene.geometries.calculateIntersections(shadowRay);
        if (shadowIntersections == null) return true;

        double lightDistance = lightSource.getDistance(intersection.point);

        for (Intersectable.Intersection shadowIntersection : shadowIntersections) {
            if (shadowIntersection.point.distance(movedPoint) < lightDistance) {
                return false;
            }
        }

        return true;
    }

}
