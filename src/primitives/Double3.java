/**
 *
 */
package primitives;

import static primitives.Util.isZero;

/**
 * This class will serve all primitive classes based on three numbers.
 * It is implemented as a Java record, providing a compact and immutable
 * representation of a triplet of double values.
 *
 * @param d1 first number
 * @param d2 second number
 * @param d3 third number
 *
 * @author Dan Zilberstein
 */
public record Double3(double d1, double d2, double d3) {

    /**
     * Zero triad (0, 0, 0)
     */
    public static final Double3 ZERO = new Double3(0, 0, 0);

    /**
     * One's triad (1, 1, 1)
     */
    public static final Double3 ONE = new Double3(1, 1, 1);

    /**
     * Constructor to initialize a Double3 object with the same number value
     * for all three components.
     *
     * @param value number value for all 3 components
     */
    public Double3(double value) {
        this(value, value, value);
    }

    /**
     * Compares this Double3 to another object for equality.
     * The comparison uses a zero-check utility to allow for numerical imprecision.
     *
     * @param obj the object to compare with
     * @return true if the values are equal within precision limits, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Double3 other)
                && isZero(d1 - other.d1)
                && isZero(d2 - other.d2)
                && isZero(d3 - other.d3);
    }

    /**
     * Computes a hash code based on the sum of the three components, rounded.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return (int) Math.round(d1 + d2 + d3);
    }

    /**
     * Returns a string representation of the Double3 object.
     *
     * @return a string in the format "(d1,d2,d3)"
     */
    @Override
    public String toString() {
        return "(" + d1 + "," + d2 + "," + d3 + ")";
    }

    /**
     * Adds this Double3 to another Double3, component-wise.
     *
     * @param rhs the other Double3 to add
     * @return a new Double3 containing the sum of the components
     */
    public Double3 add(Double3 rhs) {
        return new Double3(d1 + rhs.d1, d2 + rhs.d2, d3 + rhs.d3);
    }

    /**
     * Subtracts another Double3 from this one, component-wise.
     *
     * @param rhs the other Double3 to subtract
     * @return a new Double3 containing the difference of the components
     */
    public Double3 subtract(Double3 rhs) {
        return new Double3(d1 - rhs.d1, d2 - rhs.d2, d3 - rhs.d3);
    }

    /**
     * Scales this Double3 by a scalar, multiplying each component by the scalar.
     *
     * @param rhs the scalar multiplier
     * @return a new Double3 scaled by the given value
     */
    public Double3 scale(double rhs) {
        return new Double3(d1 * rhs, d2 * rhs, d3 * rhs);
    }

    /**
     * Reduces this Double3 by a scalar, dividing each component by the scalar.
     *
     * @param rhs the scalar divisor
     * @return a new Double3 reduced by the given value
     */
    public Double3 reduce(double rhs) {
        return new Double3(d1 / rhs, d2 / rhs, d3 / rhs);
    }

    /**
     * Performs component-wise multiplication of this Double3 with another.
     *
     * @param rhs the other Double3 to multiply with
     * @return a new Double3 containing the component-wise products
     */
    public Double3 product(Double3 rhs) {
        return new Double3(d1 * rhs.d1, d2 * rhs.d2, d3 * rhs.d3);
    }

    /**
     * Checks whether all components of this Double3 are less than a given value.
     *
     * @param k the value to compare with
     * @return true if all components are less than k, false otherwise
     */
    public boolean lowerThan(double k) {
        return d1 < k && d2 < k && d3 < k;
    }

    /**
     * Checks whether all components of this Double3 are less than the corresponding
     * components of another Double3.
     *
     * @param other the other Double3 to compare with
     * @return true if each component of this is less than the corresponding component
     *         in the other Double3, false otherwise
     */
    public boolean greaterThan(double other) {
        return d1 > other && d2 > other && d3 > other;
    }
}
