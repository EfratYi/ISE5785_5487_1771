package primitives;

/**
 * Class representing a point in 3D space.
 */
public class Point {
    /**
     * The zero point (0,0,0)
     */
    public static Point ZERO = new Point(0, 0, 0);

    /**
     * The coordinates of the point
     */
    final Double3 xyz;

    /**
     * Constructor to initialize Point based on Double3 object.
     *
     * @param xyz the Double3 object containing the coordinates
     */
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * Constructor to initialize Point based on x, y, z coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public Point(double x, double y, double z) {
        this(new Double3(x, y, z));
    }

    /**
     * Compares this point to another object for equality.
     * Returns true if the other object is a Point with the same coordinates.
     *
     * @param obj the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Point other)) return false;
        return xyz.equals(other.xyz);
    }

    /**
     * Returns a hash code value for the point.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return xyz.hashCode();
    }

    /**
     * Returns a string representation of the point.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return "" + xyz;
    }

    /**
     * Adds a vector to this point.
     *
     * @param vector the vector to add
     * @return a new Point resulting from the addition
     */
    public Point add(Vector vector) {
        return new Point(xyz.add(vector.xyz));
    }

    /**
     * Calculates the squared distance between this point and another point.
     *
     * @param other the other point
     * @return the squared distance
     */
    public double distanceSquared(Point other) {
        double x2x1 = other.xyz.d1() - xyz.d1();
        double y2y1 = other.xyz.d2() - xyz.d2();
        double z2z1 = other.xyz.d3() - xyz.d3();
        return x2x1 * x2x1 + y2y1 * y2y1 + z2z1 * z2z1;
    }

    /**
     * Calculates the distance between this point and another point.
     *
     * @param other the other point
     * @return the distance
     */
    public double distance(Point other) {
        return Math.sqrt(distanceSquared(other));
    }

    /**
     * Subtracts another point from this point to create a vector.
     *
     * @param other the other point
     * @return a new Vector resulting from the subtraction
     */
    public Vector subtract(Point other) {
        return new Vector(xyz.subtract(other.xyz));
    }
}
