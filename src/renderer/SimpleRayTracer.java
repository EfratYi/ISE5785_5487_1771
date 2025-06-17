package renderer;

import geometries.Geometries;
import geometries.Intersectable.Intersection;
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

import static primitives.Util.alignZero;
/**
 * Simple ray tracer class.
 * Inherits from {@link RayTracerBase} and provides basic ray tracing functionality.
 */
public class SimpleRayTracer extends RayTracerBase {

    private static final double DELTA = 0.1;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INITIAL_K = Double3.ONE;

    /**
     * Constructor to initialize the simple ray tracer with a given scene.
     *
     * @param scene the scene to render
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Calculates the specular reflection component based on the Phong reflection model.
     *
     * @param intersection the intersection data
     * @return the specular reflection coefficient
     */
    private Double3 calcSpecular(Intersection intersection) {
        Vector l = intersection.lightDirection;
        Vector n = intersection.normalAtPoint;
        Vector v = intersection.rayDirection;

        double ln = alignZero(l.dotProduct(n));
        if (ln == 0) return Double3.ZERO;

        Vector r = l.subtract(n.scale(2 * ln)).normalize();
        double minusVR = -alignZero(v.dotProduct(r));
        if (minusVR <= 0) return Double3.ZERO;

        return intersection.material.kS.scale(Math.pow(minusVR, intersection.material.sh));
    }

    /**
     * Calculates the diffuse reflection component based on the Lambertian model.
     *
     * @param intersection the intersection data
     * @return the diffuse reflection coefficient
     */
    private Double3 calcDiffusive(Intersection intersection) {
        double nl = intersection.dotProductLightNormal;
        double nlAbs = Math.abs(alignZero(nl));
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
        if (Objects.equals(scene.geometries, new Geometries())) {
            return scene.background;
        }

        if (scene.geometries.calculateIntersections(ray) == null) {
            return scene.background;
        }

        Intersection closestIntersection = findClosestIntersection(ray);

        if (closestIntersection == null) {
            return scene.background;
        }

        return calcColor(closestIntersection, ray);
    }

    /**
     * Finds the closest intersection point for a given ray.
     *
     * @param ray the ray to check
     * @return the closest intersection, or null if none exist
     */
    private Intersection findClosestIntersection(Ray ray) {
        List<Intersection> intersections = scene.geometries.calculateIntersections(ray);
        return intersections == null ? null : ray.findClosestIntersection(intersections);
    }

    /**
     * Initializes the light-related fields of the intersection.
     *
     * @param intersection the intersection to update
     * @param light the light source
     * @return false if both dot products (with ray direction and with light direction) are zero, true otherwise
     */
    private boolean setLightSource(Intersection intersection, LightSource light) {
        intersection.lightSource = light;
        intersection.lightDirection = light.getL(intersection.point);
        intersection.dotProductLightNormal = intersection.normalAtPoint.dotProduct(intersection.lightDirection);
        return intersection.dotProductRayNormal * intersection.dotProductLightNormal > 0;
    }

    /**
     * Checks if the intersection point is unshaded by other geometries.
     *
     * @param intersection the intersection to check
     * @return true if unshaded, false otherwise
     */
    private boolean unshaded(Intersection intersection) {
        Vector l = intersection.lightDirection;
        Vector pointToLight = l.scale(-1);
        Vector delta = intersection.normalAtPoint.scale(intersection.dotProductLightNormal < 0 ? DELTA : -DELTA);
        Point shadowOrigin = intersection.point.add(delta);
        Ray shadowRay = new Ray(shadowOrigin, pointToLight, intersection.normalAtPoint);

        List<Intersection> shadowIntersections = scene.geometries.calculateIntersections(shadowRay);
        if (shadowIntersections == null) return true;

        double lightDistance = intersection.lightSource.getDistance(intersection.point);

        for (Intersection i : shadowIntersections) {
            double distToIntersection = shadowOrigin.distance(i.point);
            if (distToIntersection < lightDistance) {
                Double3 kT = i.material.kT;
                if (kT.lowerThan(MIN_CALC_COLOR_K)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Calculates the transparency factor at the intersection point.
     *
     * @param intersection the intersection to check
     * @return the transparency factor
     */
    private Double3 transparency(Intersection intersection) {
        Vector l = intersection.lightDirection;
        Vector pointToLight = l.scale(-1);
        Vector delta = intersection.normalAtPoint.scale(intersection.dotProductLightNormal < 0 ? DELTA : -DELTA);
        Point shadowOrigin = intersection.point.add(delta);
        Ray shadowRay = new Ray(shadowOrigin, pointToLight, intersection.normalAtPoint);

        List<Intersection> shadowIntersections = scene.geometries.calculateIntersections(shadowRay);
        if (shadowIntersections == null) return Double3.ONE;

        double lightDistance = intersection.lightSource.getDistance(intersection.point);
        Double3 ktr = Double3.ONE;

        for (Intersection i : shadowIntersections) {
            double distToIntersection = shadowOrigin.distance(i.point);
            if (distToIntersection < lightDistance) {
                Double3 kT = i.geometry.getMaterial().kT;
                ktr = ktr.product(kT);
                if (ktr.lowerThan(MIN_CALC_COLOR_K)) {
                    return Double3.ZERO;
                }
            }
        }

        return ktr;
    }

    /**
     * Calculates the local lighting effects (diffuse and specular) at the intersection point.
     *
     * @param intersection the intersection point
     * @param k the attenuation factor
     * @return the color resulting from the local lighting effects
     */
    private Color calcColorLocalEffects(Intersection intersection, Double3 k) {
        Color color = intersection.geometry.getEmission();
        for (LightSource lightSource : scene.lights) {
            if (!setLightSource(intersection, lightSource)) {
                continue;
            }
            Double3 ktr = transparency(intersection);
            if (ktr.product(k).greaterThan(MIN_CALC_COLOR_K)) {
                Color iL = lightSource.getIntensity(intersection.point).scale(ktr);
                color = color.add(
                        iL.scale(calcDiffusive(intersection)
                                .add(calcSpecular(intersection))));
            }
        }
        return color;
    }

    /**
     * Calculates the color at the intersection point, including global effects.
     *
     * @param intersection the intersection point
     * @param level the recursion level
     * @param k the attenuation factor
     * @return the resulting color
     */
    private Color calcColor(Intersection intersection, int level, Double3 k) {
        Color color = calcColorLocalEffects(intersection, k);
        return level == 1 ? color :
                color.add(calcGlobalEffects(intersection, level, k));
    }

    /**
     * Constructs a reflected ray at the intersection point.
     *
     * @param intersection the intersection data
     * @return the reflected ray
     */
    private Ray constructReflectedRay(Intersection intersection) {
        Vector incoming = intersection.rayDirection;
        Vector normal = intersection.normalAtPoint;
        Point point = intersection.point;

        Vector reflectedDir = incoming.subtract(normal.scale(2 * incoming.dotProduct(normal))).normalize();
        Vector delta = normal.scale(incoming.dotProduct(normal) > 0 ? DELTA : -DELTA);
        return new Ray(point, reflectedDir, normal);
    }

    /**
     * Constructs a refracted ray at the intersection point.
     *
     * @param intersection the intersection data
     * @return the refracted ray
     */
    private Ray constructRefractedRay(Intersection intersection) {
        Vector reflectedDir = intersection.rayDirection;
        Vector normal = intersection.normalAtPoint;
        Point point = intersection.point;
        return new Ray(point, reflectedDir, normal);
    }

    /**
     * Calculates the final color at the intersection point, including ambient and local lighting.
     *
     * @param intersection the intersection data
     * @param ray the ray that hit the geometry
     * @return the resulting color at the intersection point
     */
    private Color calcColor(Intersection intersection, Ray ray) {
        return preprocessIntersection(intersection, ray.getDirection())
                ? scene.ambientLight.getIntensity()
                .scale(intersection.geometry.getMaterial().kA)
                .add(calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K)) : Color.BLACK;
    }

    /**
     * Calculates the global effect for a given ray.
     *
     * @param ray the ray to calculate
     * @param kx the reflection or refraction coefficient
     * @param level the recursion level
     * @param k the attenuation factor
     * @return the resulting color
     */
    private Color calcGlobalEffect(Ray ray, Double3 kx, int level, Double3 k) {
        Double3 kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;

        Intersection intersection = findClosestIntersection(ray);
        if (intersection == null) return scene.background.scale(kx);

        return preprocessIntersection(intersection, ray.getDirection())
                ? calcColor(intersection, level - 1, kkx).scale(kx)
                : Color.BLACK;
    }

    /**
     * Calculates the global effects (reflection and refraction) at the intersection point.
     *
     * @param intersection the intersection data
     * @param level the recursion level
     * @param k the attenuation factor
     * @return the resulting color
     */
    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
        Color color = Color.BLACK;
        Material material = intersection.material;

        Double3 kR = material.kR;
        Ray reflectedRay = constructReflectedRay(intersection);
        color = color.add(calcGlobalEffect(reflectedRay, kR, level, k));

        Double3 kT = material.kT;
        Ray refractedRay = constructRefractedRay(intersection);
        color = color.add(calcGlobalEffect(refractedRay, kT, level, k));

        return color;
    }

    /**
     * Performs preprocessing on the intersection:
     * Initializes the ray direction, normal at the hit point, and their dot product.
     *
     * @param intersection the intersection to preprocess
     * @param rayDirection the direction of the incoming ray
     * @return true if the dot product of the normal and ray direction is non-zero, false otherwise
     */
    private boolean preprocessIntersection(Intersection intersection, Vector rayDirection) {
        intersection.rayDirection = rayDirection.normalize();
        intersection.normalAtPoint = intersection.geometry.getNormal(intersection.point);
        intersection.dotProductRayNormal = alignZero(intersection.rayDirection.dotProduct(intersection.normalAtPoint));
        return intersection.dotProductRayNormal != 0;
    }
}