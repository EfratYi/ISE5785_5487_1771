package primitives;

import geometries.Intersectable;

import java.util.List;

import static primitives.Util.isZero;
import geometries.Intersectable.Intersection;

/**
 * Represents a ray in 3D space, defined by a starting point and a direction vector.
 */
public class Ray {
    /**
     * The starting point of the ray.
     */
    private final Point head;

    /**
     * The normalized direction vector of the ray.
     */
    private final Vector direction;

    /**
     * Constructs a ray with a starting point and a direction vector.
     * The direction vector is normalized upon initialization.
     *
     * @param p the starting point of the ray
     * @param v the direction vector of the ray
     */
    public Ray(Point p, Vector v) {
        head = p;
        direction = v.normalize();
    }

    /**
     * Returns the starting point of the ray.
     *
     * @return the starting point
     */
    public Point getHead() {
        return head;
    }

    /**
     * Returns the normalized direction vector of the ray.
     *
     * @return the direction vector
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * Calculates a point on the ray at a given distance from the starting point.
     *
     * @param t the distance from the starting point
     * @return the calculated point
     */
    public Point getPoint(double t) {
        if (isZero(t)) {
            return head;
        }
        return head.add(direction.scale(t));
    }

    /**
     * Finds the closest point to the ray's starting point from a list of points.
     * The closest point is determined by the smallest distance to the ray's head.
     *
     * @param points the list of points
     * @return the closest point, or null if the list is empty or null
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null ? null
                : findClosestIntersection(points.stream().map(p -> new Intersection(null, p)).toList()).point;
    }
      public Intersection findClosestIntersection( List<Intersection> intersections) {
        if (intersections == null || intersections.isEmpty()) {
            return null;
        }
        Point closestPoint = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Intersection intersection : intersections) {
            Point p = intersection.point;
            double distance = p.distance(head);
            if (distance < minDistance) {
                minDistance = distance;
                closestPoint = p;
            }
        }
        return new Intersection(intersections.get(0).geometry, closestPoint);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return (o instanceof Ray other)
                && this.head.equals(other.head)
                && this.direction.equals(other.direction);
    }

    @Override
    public String toString() {
        return "Ray{" +
                "head=" + head +
                ", direction=" + direction +
                '}';
    }
}