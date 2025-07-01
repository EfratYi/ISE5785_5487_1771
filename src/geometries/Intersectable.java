package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * Abstract class representing an intersectable geometry in 3D space.
 * This class provides methods to find intersection points of a ray with the geometry.
 */
public abstract class Intersectable {
    protected BoundingBox boundingBox = null;

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public abstract void setBoundingBox();
    /**
     * Finds the intersection points of a given ray with the geometry.
     * Uses the abstract method {@link #calculateIntersections(Ray)} and extracts the points.
     *
     * @param ray the ray to intersect with the geometry
     * @return a list of intersection points, or null if there are no intersections
     */
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
    }

    /**
     * Helper method to calculate intersections.
     * Subclasses must implement this method to provide specific intersection logic.
     *
     * @param ray the ray to intersect with the geometry
     * @return a list of intersections (with geometry and points), or null if none
     */
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);

    /**
     * Calculates the intersections of the ray with the geometry.
     *
     * @param ray the ray to intersect with the geometry
     * @return a list of intersections, or null if there are no intersections
     */
    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersectionsHelper(ray);
    }

    /**
     * Class representing an intersection of a ray with a geometry.
     * Contains information about the geometry, the intersection point, and material,
     * as well as additional fields useful for lighting calculations.
     */
    public static class Intersection {

        /**
         * The geometry that was intersected.
         */
        public final Geometry geometry;

        /**
         * The point of intersection.
         */
        public final Point point;

        /**
         * The material of the intersected geometry.
         */
        public final Material material;

        /**
         * Direction vector of the ray.
         */
        public primitives.Vector rayDirection;

        /**
         * Normal vector at the intersection point.
         */
        public primitives.Vector normalAtPoint;

        /**
         * Dot product between the ray direction and the normal vector.
         */
        public double dotProductRayNormal;

        /**
         * The light source relevant to this intersection.
         */
        public lighting.LightSource lightSource;

        /**
         * Vector direction from the light source to the intersection point.
         */
        public primitives.Vector lightDirection;

        /**
         * Dot product between the light direction and the normal vector.
         */
        public double dotProductLightNormal;

        /**
         * Constructor initializing the intersection with a geometry and point.
         * Sets the material based on the geometry if available.
         *
         * @param geometry the geometry intersected
         * @param point the intersection point
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
            if (geometry != null) {
                this.material = geometry.getMaterial();
            } else {
                this.material = null;
            }
        }

        @Override
        public String toString() {
            return "Intersection{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Intersection that = (Intersection) obj;
            return geometry.equals(that.geometry) && point.equals(that.point);
        }
    }
}

