package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * The Triangular class represents a triangle in 3D space.
 * It extends the Polygon class and is defined by three vertices.
 */
public class Triangle extends Polygon {
    Vector edge1;
    Vector edge2;

    /**
     * Constructs a Triangular object with three vertices.
     *
     * @param a The first vertex of the triangle.
     * @param b The second vertex of the triangle.
     * @param c The third vertex of the triangle.
     */
    public Triangle(Point a, Point b, Point c) {
        super(a, b, c);

        Point p0 = vertices.get(0);
        Point p1 = vertices.get(1);
        Point p2 = vertices.get(2);

        edge1 = p1.subtract(p0);
        edge2 = p2.subtract(p0);

    }

    /**
     * Returns the normal vector to the triangle at a given point.
     * The normal vector is the same for all points on the triangle since it lies in a single plane.
     *
     * @param point The point on the triangle (not used in this implementation).
     * @return The normal vector to the triangle.
     */
    @Override
    public Vector getNormal(Point point) {
        return plane.getNormal(null);
    }

    /**
     * Finds the intersection points of a given ray with the triangle.
     * This method uses the Moller-Trumbore intersection algorithm.
     *
     * @param ray The ray to intersect with the triangle.
     * @return A list of intersection points if the ray intersects the triangle,
     * or null if there are no intersections.
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        Point p0 = ray.getHead();
        Vector dir = ray.getDirection();

        Vector h = dir.crossProduct(edge2);
        double a = alignZero(edge1.dotProduct(h));
        // The ray is parallel to the triangle's plane
        if (isZero(a)) {
            return null;
        }

        double f = 1.0 / a;
        Vector s = p0.subtract(vertices.getFirst());
        double u = alignZero(f * s.dotProduct(h));
        // The intersection is outside the triangle
        if (u <= 0 || u >= 1) {
            return null;
        }

        Vector q = s.crossProduct(edge1);
        double v = alignZero(f * dir.dotProduct(q));

        // The intersection is outside the triangle
        if (v <= 0 || u + v >= 1) {
            return null;
        }

        double t = alignZero(f * edge2.dotProduct(q));
        // The intersection is behind the ray's origin
        if (t <= 0) {
            return null;
        }

        return List.of(new Intersection(this, ray.getPoint(t)));
    }
    @Override
    public void setBoundingBox() {
        Point p0 = vertices.get(0);
        Point p1 = vertices.get(1);
        Point p2 = vertices.get(2);
        // מחשבים min ו-max לכל ציר
        double minX = Math.min(Math.min(p0.getX(), p1.getX()), p2.getX());
        double maxX = Math.max(Math.max(p0.getX(), p1.getX()), p2.getX());

        double minY = Math.min(Math.min(p0.getY(), p1.getY()), p2.getY());
        double maxY = Math.max(Math.max(p0.getY(), p1.getY()), p2.getY());

        double minZ = Math.min(Math.min(p0.getZ(), p1.getZ()), p2.getZ());
        double maxZ = Math.max(Math.max(p0.getZ(), p1.getZ()), p2.getZ());

        // יוצרים את תיבת ה-AABB ושומרים אותה
        this.boundingBox = new BoundingBox(minX, maxX, minY, maxY, minZ, maxZ);
    }

}