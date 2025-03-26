package primitives;

/**
 * Class representing a vector in 3D space.
 */
public class Vector extends Point {
    /**
     * Constructor to initialize Vector based on Double3 object.
     * @param xyz the Double3 object containing the coordinates
     */
    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals( Double3.ZERO)) {
            throw new IllegalArgumentException("Zero vector is illegal");
        }
    }

    /**
     * Constructor to initialize Vector based on x, y, z coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public Vector(double x, double y, double z) {
        this(new Double3(x, y, z));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vector other)) return false;
        return xyz.equals(other.xyz);
    }

    /**
     * Calculates the squared length of the vector.
     * @return the squared length
     */
    public double lengthSquared() {
        return xyz.d1() * xyz.d1() + xyz.d2() * xyz.d2() + xyz.d3() * xyz.d3();
    }

    /**
     * Adds another vector to this vector.
     * @param vector the vector to add
     * @return a new Vector resulting from the addition
     */
    public Vector add(Vector vector) {
        return new Vector(xyz.add(vector.xyz));
    }

    /**
     * Calculates the length of the vector.
     * @return the length
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Normalizes the vector to a unit vector.
     * @return a new Vector that is the normalized version of this vector
     */
    public Vector normalize() {
        double length = length();
        return new Vector(xyz.scale(1.0 / length));
    }

    /**
     * Calculates the dot product of this vector and another vector.
     * @param other the other vector
     * @return the dot product
     */
    public double dotProduct(Vector other) {
        return xyz.d1() * other.xyz.d1()
                + xyz.d2() * other.xyz.d2()
                + xyz.d3() * other.xyz.d3();
    }

    /**
     * Calculates the cross product of this vector and another vector.
     * @param other the other vector
     * @return a new Vector that is the cross product of this vector and the other vector
     */
    public Vector crossProduct(Vector other) {
        return new Vector(
                xyz.d2() * other.xyz.d3() - xyz.d3() * other.xyz.d2(),
                xyz.d3() * other.xyz.d1() - xyz.d1() * other.xyz.d3(),
                xyz.d1() * other.xyz.d2() - xyz.d2() * other.xyz.d1());
    }
}