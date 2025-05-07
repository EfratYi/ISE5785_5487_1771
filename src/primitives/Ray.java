package primitives;

import java.util.Objects;

import static primitives.Util.isZero;

/**
 * Class representing a ray in 3D space, defined by a starting point (head) and a direction vector.
 */
public class Ray {
    /**
     * The starting point of the ray.
     */
    Point head;

    /**
     * The normalized direction vector of the ray.
     */
    Vector direction;

    /**
     * Constructor to initialize a ray with a starting point and a direction vector.
     * The direction vector is normalized upon initialization.
     *
     * @param p the starting point of the ray
     * @param v the direction vector of the ray
     */
    public Ray(Point p, Vector v) {
        head = p;
        direction = v.normalize();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return (o instanceof Ray other)
                && this.head.equals(other.head)
                && this.direction.equals(other.direction);
    }


    /**
     * Getter for the starting point of the ray.
     *
     * @return the starting point of the ray
     */
    public Point getHead() {
        return head;
    }

    /**
     * Getter for the direction vector of the ray.
     *
     * @return the normalized direction vector of the ray
     */
    public Vector getDirection() {
        return direction;
    }

    public Point getPoint(double t) {
        if (isZero(t)) {
            return head;
        }
        return head.add(direction.scale(t));
    }


    @Override
    public String toString() {
        return "Ray{" +
                "p=" + head +
                ", v=" + direction +
                '}';
    }

}