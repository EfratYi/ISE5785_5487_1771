package renderer;
import geometries.Geometries;
import geometries.Intersectable.Intersection;
import geometries.Material;
import lighting.PointLight;
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
    private Double3 calcSpecular(Intersection intersection){
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
    private Double3 calcDiffusive(Intersection intersection){
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

        Intersection closestIntersection = findClosestIntersection(ray);

        if (closestIntersection == null) {
            return scene.background; // Return background color if no intersection
        }

        // Calculate the color at the intersection point
        return calcColor(closestIntersection, ray);
    }

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
        // Store light source
        intersection.lightSource = light;

        // Compute light direction from the point to the light
        intersection.lightDirection = light.getL(intersection.point);

        // Compute dot product between normal and light direction
        intersection.dotProductLightNormal = intersection.normalAtPoint.dotProduct(intersection.lightDirection);

        // Check: both dot products should have the same sign (i.e., their product should be positive)
        return intersection.dotProductRayNormal * intersection.dotProductLightNormal > 0;
    }


    private boolean unshaded(Intersection intersection) {

        Vector l = intersection.lightDirection;
        Vector pointToLight = l.scale(-1);
        Vector delta = intersection.normalAtPoint.scale(intersection.dotProductLightNormal < 0 ? DELTA : -DELTA);
        Point shadowOrigin = intersection.point.add(delta);
        Ray shadowRay = new Ray(shadowOrigin, pointToLight,intersection.normalAtPoint);

        List<Intersection> shadowIntersections = scene.geometries.calculateIntersections(shadowRay);
        if (shadowIntersections == null) return true;

        double lightDistance = intersection.lightSource.getDistance(intersection.point);

        for (Intersection i : shadowIntersections) {
            double distToIntersection = shadowOrigin.distance(i.point);

            if (distToIntersection < lightDistance) {
                Double3 kT = i.material.kT;
                if (kT.lowerThan(MIN_CALC_COLOR_K)) {
                    return false; // גוף כמעט אטום – הצללה מלאה
                }
                // אם הגוף שקוף – ממשיכים לבדוק אולי אחריו יש גוף אטום
            }
        }

        return true; // לא נמצא גוף מספיק אטום שחוסם את האור
    }



    private Double3 transparency(Intersection intersection) {
        Vector l = intersection.lightDirection;
        Vector pointToLight = l.scale(-1);
        Vector delta = intersection.normalAtPoint.scale(intersection.dotProductLightNormal < 0 ? DELTA : -DELTA);
        Point shadowOrigin = intersection.point.add(delta);
        Ray shadowRay = new Ray(shadowOrigin, pointToLight,intersection.normalAtPoint);

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
                    return Double3.ZERO; // אור כמעט נחסם לגמרי
                }
            }
        }

        return ktr; // אור עבר באופן חלקי לפי שקיפות כוללת
    }

    /**
     * Calculates the local lighting effects (diffuse and specular) at the intersection point.
     *
     * @param intersection the intersection point
     * @return the color resulting from the local lighting effects
     */
    private Color calcColorLocalEffects(Intersection intersection,Double3 k) {
        Color color = intersection.geometry.getEmission();
        for (LightSource lightSource : scene.lights) {
            if (!setLightSource(intersection, lightSource)) {
                continue;
            }
            Double3 ktr = Double3.ONE; // התחל עם אור מלא
            if (lightSource instanceof PointLight) {
                PointLight pointLight = (PointLight) lightSource;
                if (pointLight.getRadius() > 0) {
                    // עבור אור נקודתי עם גודל - השתמש ב-soft shadows
                    ktr = calcLocalEffectsSoftShadows(pointLight, intersection);
                } else {
                    // עבור אור נקודתי ללא גודל - השתמש ב-transparency הרגיל
                    ktr = transparency(intersection);
                }
            } else {
                // עבור אור כיווני - השתמש ב-transparency הרגיל
                ktr = transparency(intersection);
            }
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

    private Color calcColor(Intersection intersection, int level, Double3 k) {
        Color color = calcColorLocalEffects(intersection,k);
        return level == 1 ? color :
                color.add(calcGlobalEffects(intersection, level, k));
    }


    private Ray constructReflectedRay(Intersection intersection) {
        Vector incoming = intersection.rayDirection; // כיוון הקרן הפוגעת
        Vector normal = intersection.normalAtPoint;  // הנורמל בנקודת הפגיעה
        Point point = intersection.point;                        // נקודת הפגיעה

        // מחשבים את כיוון ההשתקפות לפי חוק ההשתקפות
        Vector reflectedDir = incoming.subtract(normal.scale(2 * incoming.dotProduct(normal))).normalize();

        // מוסיפים DELTA כדי להזיז את נקודת ההתחלה מעט החוצה מהגוף
        Vector delta = normal.scale(incoming.dotProduct(normal) > 0 ? DELTA : -DELTA);
        return new Ray( point,reflectedDir,normal);
    }


    private Ray constructRefractedRay(Intersection intersection) {
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
    private Color calcColor(Intersection intersection, Ray ray) {
        return preprocessIntersection(intersection, ray.getDirection())
                ?scene.ambientLight.getIntensity()
                .scale(intersection.geometry.getMaterial().kA) // Scale by ambient reflection coefficient
                .add(calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K)): Color.BLACK;
    }
    private Color calcGlobalEffect(Ray ray, Double3 kx, int level, Double3 k){
        Double3 kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;

        Intersection intersection = findClosestIntersection(ray);
        if (intersection == null) return scene.background.scale(kx);

        return preprocessIntersection(intersection, ray.getDirection())
                ? calcColor(intersection, level - 1, kkx).scale(kx)
                : Color.BLACK;
    }


    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
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
    private boolean preprocessIntersection(Intersection intersection, Vector rayDirection) {
        intersection.rayDirection = rayDirection.normalize();
        intersection.normalAtPoint = intersection.geometry.getNormal(intersection.point);
        intersection.dotProductRayNormal = alignZero(intersection.rayDirection.dotProduct(intersection.normalAtPoint));
//        return !Util.isZero(intersection.dotProductRayNormal);
        return intersection.dotProductRayNormal != 0;
    }


    private Double3 calcLocalEffectsSoftShadows(PointLight lightSource, Intersection intersection) {
        // חישוב כיוון האור מהנקודה למקור האור
        Vector l = lightSource.getL(intersection.point);

        // יצירת וקטור ניצב לכיוון האור
        Vector vUp = null;
        try {
            // ננסה ליצור וקטור ניצב עם וקטור (0,1,0) או (1,0,0)
            if (Math.abs(l.dotProduct(new Vector(0, 1, 0))) < 0.9) {
                vUp = l.crossProduct(new Vector(0, 1, 0)).normalize();
            } else {
                vUp = l.crossProduct(new Vector(1, 0, 0)).normalize();
            }
        } catch (Exception e) {
            vUp = new Vector(0, 1, 0);
        }

        // מספר דגימות - מתחילים עם מספר קטן לבדיקה
        int numSamples = 5; // 5x5 = 25 דגימות

        // יצירת TargetArea
        TargetArea blackBoard = new TargetArea(
                numSamples,
                lightSource.getRadius() * 2, // קוטר האור
                l.scale(-1), // כיוון מהנקודה למקור האור
                vUp,
                lightSource.getPosition()
        );

        double totalShadow = 0.0;
        int validRays = 0;

        // דגימה של קרניים במישור מקור האור
        for (int i = 0; i < numSamples; i++) {
            for (int j = 0; j < numSamples; j++) {
                try {
                    // יצירת קרן מהנקודה למקור האור המדגם
                    Ray shadowRay = blackBoard.constructRay(j, i, intersection.point);

                    if (shadowRay != null) {
                        validRays++;

                        // בדיקה אם הקרן חסומה
                        if (isBlocked(shadowRay, lightSource.getDistance(intersection.point))) {
                            totalShadow += 1.0; // חסומה - מוסיפים הצללה מלאה
                    }
                        // אם לא חסומה - לא מוסיפים הצללה (0)
                    }
                } catch (Exception e) {
                    // המשך גם אם יש בעיה עם קרן אחת
                    continue;
                }
            }
        }

        // חישוב אחוז האור שעובר
        if (validRays > 0) {
            double shadowPercentage = totalShadow / validRays;
            double lightPercentage = 1.0 - shadowPercentage;
            return new Double3(lightPercentage);
        } else {
            // אם לא היו קרניים תקינות, השתמש בשקיפות הרגילה
            return transparency(intersection);
        }
    }

    /**
     * בודק אם קרן צללים חסומה על ידי אובייקט
     */
    private boolean isBlocked(Ray shadowRay, double lightDistance) {
        List<Intersection> shadowIntersections = scene.geometries.calculateIntersections(shadowRay);
        if (shadowIntersections == null) return false;

        for (Intersection shadowIntersection : shadowIntersections) {
            double distToIntersection = shadowRay.getHead().distance(shadowIntersection.point);

            // אם יש חיתוך לפני מקור האור
            if (distToIntersection < lightDistance - DELTA) {
                // אם הגוף אטום - יש הצללה
                if (shadowIntersection.geometry.getMaterial().kT.lowerThan(MIN_CALC_COLOR_K)) {
                    return true;
                }
            }
        }
        return false;
    }
}
