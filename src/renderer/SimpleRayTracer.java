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

    private Color calcColor(Intersectable.Intersection intersection, Ray ray, int level, Double3 k) {
        Vector n = intersection.geometry.getNormal(intersection.point);
        Vector v = ray.getDirection ();
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0) return Color.BLACK;
        Color color = scene.ambientLight.getIntensity()
                .add(calcColorLocalEffects(intersection));
        return 1 == level ? color : color.add(calcGlobalEffects(intersection, level, k));
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

        double ln = alignZero(l.dotProduct(n));
        if (ln == 0) return Double3.ZERO;

        Vector r = l.subtract(n.scale(2 * ln)).normalize();
        double minusVR = -alignZero(v.dotProduct(r));
        if (minusVR <= 0) return Double3.ZERO;

        return intersection.material.kS.scale(Math.pow(minusVR, intersection.material.sh));
    }

    private Ray constructReflectedRay(Intersectable.Intersection intersection) {
        Vector reflectedDir = incoming.subtract(normal.scale(2 * incoming.dotProduct(normal))).normalize();
        Vector delta = normal.scale(incoming.dotProduct(normal) > 0 ? DELTA : -DELTA);
        return new Ray(point.add(delta), reflectedDir);
    }

    private Ray constructRefractedRay(Intersectable.Intersection intersection) {
        Vector delta = normal.scale(incoming.dotProduct(normal) > 0 ? DELTA : -DELTA);
        return new Ray(point.add(delta), incoming);  // אותו כיוון, מניחים שקיפות ישרה
    }

    /**
     * Calculates the diffuse reflection component based on the Lambertian model.
     *
     * @param intersection the intersection data
     * @return the diffuse reflection coefficient
     */
    private Double3 calcDiffusive(Intersectable.Intersection intersection){
        double nl = intersection.dotProductLightNormal;
        double nlAbs = Math.abs(alignZero(nl));
        return intersection.material.kD.scale(nlAbs);
    }
    private Color calcLocalEffects(Intersectable.Intersection intersection) {
        Color color = intersection.geometry.getEmission();

        for (LightSource lightSource : scene.lights) {
            if (!setLightSource(intersection, lightSource)) {
                continue;
            }

            // תנאי הצללה: אם הנקודה בצל (עצם מסתיר), לא מוסיפים אור זה
            if (!unshaded(intersection)) {
                continue;
            }

            // חישוב עוצמת אור בנקודה
            Color iL = lightSource.getIntensity(intersection.point);

            // חישוב תרומת אור ישיר (דיפוזי + ספקולרי)
            Double3 diff = calcDiffusive(intersection);
            Double3 spec = calcSpecular(intersection);

            // הוספה לצבע הכולל
            color = color.add(iL.scale(diff.add(spec)));
        }

        return color;
    }


    /**
     * Traces the given ray and calculates the color seen along the ray.
     *
     * @param ray the ray to trace
     * @return the color seen along the ray
     */
    @Override
    public Color traceRay(Ray ray) {

        // Check if the scene has geometries
        if (Objects.equals(scene.geometries, new Geometries())) {
            return scene.background; // Return background color if no geometries
        }

        // Check if the ray intersects with any geometries in the scene
        if (scene.geometries.calculateIntersections(ray) == null) {
            return scene.background; // Return background color if no intersection
        }

        Intersectable.Intersection closestIntersection = findClosestIntersection(ray);

        if (closestIntersection == null) {
            return scene.background; // Return background color if no intersection
        }

        // Calculate the color at the intersection point
        return calcColor(closestIntersection, ray);
    }

    private Intersectable.Intersection findClosestIntersection(Ray ray) {
        List<Intersectable.Intersection> intersections = scene.geometries.calculateIntersections(ray);
        if (intersections == null || intersections.isEmpty()) {
            return null;
        }

        double minDistance = Double.POSITIVE_INFINITY;
        Intersectable.Intersection closestIntersection = null;

        for (Intersectable.Intersection intersection : intersections) {
            double distance = ray.getHead().distance(intersection.point);
            if (distance < minDistance) {
                minDistance = distance;
                closestIntersection = intersection;
            }
        }

        return closestIntersection;
    }


    /**
     * Calculates the final color at the intersection point, including ambient and local lighting.
     *
     * @param intersection the intersection data
     * @param ray the ray that hit the geometry
     * @return the resulting color at the intersection point
     */
    private Color calcColor(Intersectable.Intersection intersection, Ray ray) {
        return calcColor(intersection, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K)
                .add(scene.ambientLight.getIntensity());
    }
    private Color calcGlobalEffect(Ray ray, Double3 kx, int level, Double3 k) {
        // מכפלה של מקדמי ההנחתה: מה שהיה עד עכשיו כפול השקיפות/השתקפות של הקרן הנוכחית
        Double3 kkx = kx.product(k);

        // אם ההנחתה הכוללת נמוכה מהרף – אין טעם להמשיך לחשב, נחזיר שחור
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) {
            return Color.BLACK;
        }

        // מציאת נקודת החיתוך הקרובה ביותר
        Intersectable.Intersection intersection = findClosestIntersection(ray);

        // אם אין חיתוך – נחזיר צבע רקע
        if (intersection == null) {
            return scene.background;
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
                Double3 ktr = shadowIntersection.material.kT;
                if (ktr.lowerThan(MIN_CALC_COLOR_K)) {
                    return false; // אור נחסם על ידי גוף כמעט אטום
                }
            }
        }

        return true; // לא נמצא גוף אטום דיו כדי לחסום את האור
    }


}
