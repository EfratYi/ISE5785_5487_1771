package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * Abstract class representing an intersectable geometry in 3D space.
 * This class provides a method to find intersection points of a ray with the geometry.
 */
public abstract class Intersectable {

    public static class Intersection {
        public final Geometry geometry;
        public final Point point;

        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true; // same object
            if (obj == null || getClass() != obj.getClass()) return false; // null or different class

            Intersection other = (Intersection) obj;
            return geometry == other.geometry && point.equals(other.point);
        }
        @Override
        public String toString() {
            return "Intersection{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }
    }

    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        return null;
    }

    public final List<Intersection> calculateIntersections(Ray ray){
        return calculateIntersectionsHelper(ray);
    }
    /**
     * Finds the intersection points of a given ray with the geometry.
     * This method must be implemented by subclasses to define specific intersection logic.
     *
     * @param ray the ray to intersect with the geometry
     * @return a list of intersection points, or null if there are no intersections
     */
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
       }
    }
