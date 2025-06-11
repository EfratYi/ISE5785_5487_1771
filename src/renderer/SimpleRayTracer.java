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

//    private boolean unshaded(Intersectable.Intersection intersection) {
//        Vector l = intersection.lightDirection;
//        Vector n = intersection.normalAtPoint;
//        Point p = intersection.point;
//        LightSource light = intersection.lightSource;
//
//        Vector lightDir = l.scale(-1); // כיוון הקרן - מהנקודה אל מקור האור
//        Point shadowOrigin = p.add(n.scale(n.dotProduct(lightDir) > 0 ? DELTA : -DELTA)); // היסט קטן כדי להימנע מחיתוך עצמי
//        Ray shadowRay = new Ray(shadowOrigin, lightDir);
//
//        List<Intersectable.Intersection> intersections = scene.geometries.calculateIntersections(shadowRay);
//        if (intersections == null) return true; // אין כלום בדרך – לא חסום
//
//        double lightDistance = light.getDistance(p);
//        Double3 transparency = Double3.ONE; // מתחילים מ־1 (כל האור עובר)
//
//        for (Intersectable.Intersection shadowHit : intersections) {
//            if (shadowHit.point.distance(p) < lightDistance) {
//                Double3 kT = shadowHit.geometry.getMaterial().kT;
//
//                // אם האובייקט אטום – חסום לחלוטין
//                if (kT.lowerThan(MIN_CALC_COLOR_K)) {
//                    return false;
//                }
//
//                // נחליש את האור בהתאם לשקיפות
//                transparency = transparency.product(kT);
//
//                // אם האור נחלש מתחת לסף – נחשב כחסום
//                if (transparency.lowerThan(MIN_CALC_COLOR_K)) {
//                    return false;
//                }
//            }
//        }
//
//        return true; // אף עצם לא חסם את האור באופן משמעותי
//    }


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




    private Double3 transparency(Intersectable.Intersection intersection) {
        Vector l = intersection.lightDirection;
        Vector n = intersection.normalAtPoint;
        Point p = intersection.point;
        LightSource light = intersection.lightSource;
        Double3 ktr = Double3.ONE;

        // כיוון הקרן - מהנקודה אל מקור האור
        Vector lightDir = l.scale(-1);
        Point shadowOrigin = p.add(n.scale(n.dotProduct(lightDir) > 0 ? DELTA : -DELTA));
        Ray shadowRay = new Ray(shadowOrigin, lightDir);

        List<Intersectable.Intersection> shadowIntersections = scene.geometries.calculateIntersections(shadowRay);
        if (shadowIntersections == null) return ktr;

        double lightDistance = light.getDistance(p);

        for (Intersectable.Intersection shadowHit : shadowIntersections) {
            if (shadowHit.point.distance(p) < lightDistance) {
                Double3 kT = shadowHit.geometry.getMaterial().kT;
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
     * @return the color resulting from the local lighting effects
     */
    private Color calcColorLocalEffects(Intersectable.Intersection intersection,Double3 k) {
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
//        Color color = intersection.geometry.getEmission();
//
//        for (LightSource lightSource : scene.lights) {
//            if (setLightSource(intersection, lightSource) && unshaded(intersection)) {
//                Color iL = lightSource.getIntensity(intersection.point);
//                Double3 diff = calcDiffusive(intersection);
//                Double3 spec = calcSpecular(intersection);
//                color = color.add(iL.scale(diff.add(spec)));
//            }
//        }
//        return color;
    }

    private Color calcColor(Intersectable.Intersection intersection, int level, Double3 k) {
        Color color = calcColorLocalEffects(intersection,k);
        return level == 1 ? color :
                color.add(calcGlobalEffects(intersection, level, k));
    }




    private Ray constructReflectedRay(Intersectable.Intersection intersection) {
        Vector incoming = intersection.rayDirection; // כיוון הקרן הפוגעת
        Vector normal = intersection.normalAtPoint;  // הנורמל בנקודת הפגיעה
        Point point = intersection.point;                        // נקודת הפגיעה

        // מחשבים את כיוון ההשתקפות לפי חוק ההשתקפות
        Vector reflectedDir = incoming.subtract(normal.scale(2 * incoming.dotProduct(normal))).normalize();

        // מוסיפים DELTA כדי להזיז את נקודת ההתחלה מעט החוצה מהגוף
        Vector delta = normal.scale(incoming.dotProduct(normal) > 0 ? DELTA : -DELTA);
        return new Ray( point,reflectedDir,normal);
    }


    private Ray constructRefractedRay(Intersectable.Intersection intersection) {
        Vector reflectedDir = intersection.rayDirection; // כיוון הקרן הפוגעת
        Vector normal = intersection.normalAtPoint;  // הנורמל בנקודת הפגיעה
        Point point = intersection.point;                        // נקודת הפגיעה
        return new Ray( point,reflectedDir,normal);
    }


    /**
     * Calculates the final color at the intersection point, including ambient and local lighting.
     *
     * @param intersection the intersection data
     * @param ray the ray that hit the geometry
     * @return the resulting color at the intersection point
     */
    private Color calcColor(Intersectable.Intersection intersection, Ray ray) {
        return preprocessIntersection(intersection, ray.getDirection())
                ?scene.ambientLight.getIntensity()
                .scale(intersection.geometry.getMaterial().kA) // Scale by ambient reflection coefficient
                .add(calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K)): Color.BLACK;
    }
    private Color calcGlobalEffect(Ray ray, Double3 kx, int level, Double3 k){
        Double3 kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;

        Intersectable.Intersection intersection = findClosestIntersection(ray);
        if (intersection == null) return scene.background.scale(kx);

        return preprocessIntersection(intersection, ray.getDirection())
                ? calcColor(intersection, level - 1, kkx).scale(kx)
                : Color.BLACK;
    }


    private Color calcGlobalEffects(Intersectable.Intersection intersection, int level, Double3 k) {
        Color color = Color.BLACK;
        Material material = intersection.material;

        // קרן משתקפת
        Double3 kR = material.kR;
        Ray reflectedRay = constructReflectedRay(intersection);
        color = color.add(calcGlobalEffect(reflectedRay, kR, level, k));

        // קרן שקופה
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
    private boolean preprocessIntersection(Intersectable.Intersection intersection, Vector rayDirection) {
        intersection.rayDirection = rayDirection.normalize();
        intersection.normalAtPoint = intersection.geometry.getNormal(intersection.point);
        intersection.dotProductRayNormal = intersection.rayDirection.dotProduct(intersection.normalAtPoint);
        return !Util.isZero(intersection.dotProductRayNormal);
    }



}
