/**
 * SimpleRayTracer.java
 *
 * This class provides a basic implementation of a ray tracer. It handles diffuse, specular, reflection,
 * transparency effects, and supports soft shadows. It extends the RayTracerBase and calculates the color
 * at each pixel by simulating the interaction of light with objects in a 3D scene.
 */

package renderer;

import geometries.Geometries;
import geometries.Intersectable.Intersection;
import geometries.Material;
import lighting.PointLight;
import primitives.*;
import lighting.LightSource;
import scene.Scene;

import java.util.List;
import java.util.Objects;

import static primitives.Util.alignZero;

/**
 * Basic ray tracer that implements local and global illumination including shadows, reflections, and transparency.
 */
public class SimpleRayTracer extends RayTracerBase {
    private static final double DELTA = 0.1;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INITIAL_K = Double3.ONE;

    /**
     * Constructor to initialize the simple ray tracer with a scene.
     *
     * @param scene the 3D scene to be rendered
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
     * Calculates the diffuse reflection component.
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
     * Traces the ray and determines the resulting color.
     *
     * @param ray the ray to trace
     * @return the calculated color
     */
    @Override
    public Color traceRay(Ray ray) {
        if (Objects.equals(scene.geometries, new Geometries())) {
            return scene.background;
        }

        Intersection closestIntersection = findClosestIntersection(ray);
        return closestIntersection == null ? scene.background : calcColor(closestIntersection, ray);
    }

    /**
     * Finds the closest intersection of the ray with any object in the scene.
     *
     * @param ray the ray
     * @return the closest intersection
     */
    private Intersection findClosestIntersection(Ray ray) {
        List<Intersection> intersections = scene.geometries.calculateIntersections(ray);
        return intersections == null ? null : ray.findClosestIntersection(intersections);
    }

    /**
     * Sets up light source and calculates dot products.
     *
     * @param intersection the intersection to prepare
     * @param light the light source
     * @return true if surface is illuminated, false if light and view are on opposite sides
     */
    private boolean setLightSource(Intersection intersection, LightSource light) {
        intersection.lightSource = light;
        intersection.lightDirection = light.getL(intersection.point);
        intersection.dotProductLightNormal = intersection.normalAtPoint.dotProduct(intersection.lightDirection);
        return intersection.dotProductRayNormal * intersection.dotProductLightNormal > 0;
    }

    /**
     * Checks whether a point is not fully shadowed.
     *
     * @param intersection the intersection point
     * @return true if point is not in shadow
     */
    private boolean unshaded(Intersection intersection) {
        Vector l = intersection.lightDirection.scale(-1);
        Point shadowOrigin = intersection.point.add(intersection.normalAtPoint.scale(intersection.dotProductLightNormal < 0 ? DELTA : -DELTA));
        Ray shadowRay = new Ray(shadowOrigin, l, intersection.normalAtPoint);

        List<Intersection> intersections = scene.geometries.calculateIntersections(shadowRay);
        if (intersections == null) return true;

        double lightDistance = intersection.lightSource.getDistance(intersection.point);
        for (Intersection i : intersections) {
            if (shadowOrigin.distance(i.point) < lightDistance) {
                if (i.material.kT.lowerThan(MIN_CALC_COLOR_K)) return false;
            }
        }
        return true;
    }


    /**
     * Calculates the transparency factor for the intersection point.
     *
     * @param intersection the intersection point
     * @return the transparency factor
     */
    private Double3 transparency(Intersection intersection) {
        Vector l = intersection.lightDirection.scale(-1);
        Point shadowOrigin = intersection.point.add(intersection.normalAtPoint.scale(intersection.dotProductLightNormal < 0 ? DELTA : -DELTA));
        Ray shadowRay = new Ray(shadowOrigin, l, intersection.normalAtPoint);

        List<Intersection> intersections = scene.geometries.calculateIntersections(shadowRay);
        if (intersections == null) return Double3.ONE;

        double lightDistance = intersection.lightSource.getDistance(intersection.point);
        Double3 ktr = Double3.ONE;

        for (Intersection i : intersections) {
            if (shadowOrigin.distance(i.point) < lightDistance) {
                ktr = ktr.product(i.geometry.getMaterial().kT);
                if (ktr.lowerThan(MIN_CALC_COLOR_K)) return Double3.ZERO;
            }
        }
        return ktr;
    }

    /**
     * Computes local lighting effects.
     *
     * @param intersection the intersection
     * @param k accumulated attenuation factor
     * @return color due to local lighting
     */
    private Color calcColorLocalEffects(Intersection intersection, Double3 k) {
        Color color = intersection.geometry.getEmission();
        for (LightSource light : scene.lights) {
            if (!setLightSource(intersection, light)) continue;

            Double3 ktr = light instanceof PointLight && ((PointLight) light).getRadius() > 0
                    ? calcLocalEffectsSoftShadows((PointLight) light, intersection)
                    : transparency(intersection);

            if (ktr.product(k).greaterThan(MIN_CALC_COLOR_K)) {
                Color iL = light.getIntensity(intersection.point).scale(ktr);
                color = color.add(iL.scale(calcDiffusive(intersection).add(calcSpecular(intersection))));
            }
        }
        return color;
    }

    private Color calcColor(Intersection intersection, int level, Double3 k) {
        Color color = calcColorLocalEffects(intersection, k);
        return level == 1 ? color : color.add(calcGlobalEffects(intersection, level, k));
    }

    private Color calcColor(Intersection intersection, Ray ray) {
        return preprocessIntersection(intersection, ray.getDirection())
                ? scene.ambientLight.getIntensity().scale(intersection.geometry.getMaterial().kA)
                .add(calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K))
                : Color.BLACK;
    }

    private Ray constructReflectedRay(Intersection intersection) {
        Vector dir = intersection.rayDirection;
        Vector normal = intersection.normalAtPoint;
        Point point = intersection.point;
        Vector reflectedDir = dir.subtract(normal.scale(2 * dir.dotProduct(normal))).normalize();
        return new Ray(point, reflectedDir, normal);
    }

    private Ray constructRefractedRay(Intersection intersection) {
        return new Ray(intersection.point, intersection.rayDirection, intersection.normalAtPoint);
    }

    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
        Color color = Color.BLACK;
        Material material = intersection.material;

        color = color.add(calcGlobalEffect(constructReflectedRay(intersection), material.kR, level, k));
        color = color.add(calcGlobalEffect(constructRefractedRay(intersection), material.kT, level, k));

        return color;
    }

    private Color calcGlobalEffect(Ray ray, Double3 kx, int level, Double3 k) {
        Double3 kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;

        Intersection i = findClosestIntersection(ray);
        return (i == null ? scene.background.scale(kx) :
                preprocessIntersection(i, ray.getDirection()) ? calcColor(i, level - 1, kkx).scale(kx) : Color.BLACK);
    }

    private boolean preprocessIntersection(Intersection intersection, Vector rayDirection) {
        intersection.rayDirection = rayDirection.normalize();
        intersection.normalAtPoint = intersection.geometry.getNormal(intersection.point);
        intersection.dotProductRayNormal = alignZero(intersection.rayDirection.dotProduct(intersection.normalAtPoint));
        return intersection.dotProductRayNormal != 0;
    }

    /**
     * Calculates soft shadows using a sampling strategy.
     *
     * @param lightSource area light source
     * @param intersection the intersection
     * @return shadow intensity modifier
     */
    private Double3 calcLocalEffectsSoftShadows(PointLight lightSource, Intersection intersection) {
        Vector l = lightSource.getL(intersection.point);
        Vector vUp;

        try {
            vUp = (Math.abs(l.dotProduct(new Vector(0, 1, 0))) < 0.9)
                    ? l.crossProduct(new Vector(0, 1, 0)).normalize()
                    : l.crossProduct(new Vector(1, 0, 0)).normalize();
        } catch (Exception e) {
            vUp = new Vector(0, 1, 0);
        }

        int numSamples = 5;
        TargetArea area = new TargetArea(numSamples, lightSource.getRadius() * 2, l.scale(-1), vUp, lightSource.getPosition());
        double totalShadow = 0;
        int validRays = 0;

        for (int i = 0; i < numSamples; i++) {
            for (int j = 0; j < numSamples; j++) {
                try {
                    Ray shadowRay = area.constructRay(j, i, intersection.point);
                    if (shadowRay != null) {
                        validRays++;
                        if (isBlocked(shadowRay, lightSource.getDistance(intersection.point))) {
                            totalShadow++;
                        }
                    }
                } catch (Exception ignored) {}
            }
        }

        return validRays > 0 ? new Double3(1 - (totalShadow / validRays)) : transparency(intersection);
    }

    /**
     * Determines whether a ray is blocked before reaching the light source.
     *
     * @param shadowRay the ray toward the light
     * @param lightDistance distance to light source
     * @return true if the ray is blocked
     */
    private boolean isBlocked(Ray shadowRay, double lightDistance) {
        List<Intersection> intersections = scene.geometries.calculateIntersections(shadowRay);
        if (intersections == null) return false;

        for (Intersection i : intersections) {
            if (shadowRay.getHead().distance(i.point) < lightDistance - DELTA &&
                    i.geometry.getMaterial().kT.lowerThan(MIN_CALC_COLOR_K)) {
                return true;
            }
        }
        return false;
    }
}
