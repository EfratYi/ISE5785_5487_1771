package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

/**
 * Simple ray tracer class.
 * Inherits from RayTracerBase and provides basic ray tracing functionality.
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
        // מחפש נקודות חיתוך בין הקרן לבין הגיאומטריות בסצנה
        List<Point> intersections = scene.geometries.findIntersections(ray);

        // אם אין חיתוכים - מחזיר את צבע הרקע של הסצנה
        if (intersections == null || intersections.isEmpty()) {
            return scene.background;
        }

        // מחפש את הנקודה הקרובה ביותר לתחילת הקרן
        Point closestPoint = ray.findClosestPoint(intersections);

        // מחזיר את הצבע של הנקודה הזו
        return calcColor(closestPoint);
    }

    /**
     * מחשב את צבע הנקודה (בשלב הזה מחזיר רק את האור הסביבתי)
     *
     * @param point הנקודה לחשב לה צבע
     * @return הצבע המחושב
     */
    private Color calcColor(Point point) {
        // בשלב הזה מחזיר רק את עוצמת התאורה הסביבתית (Ambient Light)
        return scene.ambientLight.getIntensity();
    }
}
