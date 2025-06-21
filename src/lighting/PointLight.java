package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;
import renderer.TargetArea;

/**
 * Represents a point light source in 3D space.
 * A point light emits light in all directions from a specific position.
 * The intensity of the light decreases with distance according to attenuation factors.
 */
public class PointLight extends Light implements LightSource {

    /**
     * The position of the point light in 3D space.
     */
    private final Point position;

    /**
     * Constant attenuation factor.
     */
    private double kC = 1;  // constant attenuation

    /**
     * Linear attenuation factor.
     */
    private double kL = 0;  // linear attenuation

    /**
     * Quadratic attenuation factor.
     */
    private double kQ = 0;  // quadratic attenuation
    /**
     * Radius for soft shadows (0 means hard shadows)
     */

    /**
     * Number of sample points for soft shadows
     */
    private double radius = 0;

    public PointLight setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    /**
     * Sets the number of sample points for soft shadows
     *
     */
    public double getRadius() {
        return radius;
    }



    /**
     * Creates a target area for soft shadows
     *
     * @param hitPoint the point being lit
     * @return TargetArea object, or null if radius is 0
     */

    /**
     * Constructs a point light with the given intensity and position.
     *
     * @param intensity the color intensity of the light
     * @param position  the position of the light source
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    /**
     * Sets the constant attenuation factor.
     *
     * @param kC the constant attenuation factor
     * @return the current PointLight object (for method chaining)
     */
    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Sets the linear attenuation factor.
     *
     * @param kL the linear attenuation factor
     * @return the current PointLight object (for method chaining)
     */
    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * Sets the quadratic attenuation factor.
     *
     * @param kQ the quadratic attenuation factor
     * @return the current PointLight object (for method chaining)
     */
    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }

    /**
     * Calculates the light intensity at a given point in space.
     * The intensity decreases based on the distance from the light source and the attenuation factors.
     *
     * @param p the point where the intensity is calculated
     * @return the color intensity at the specified point
     */
    @Override
    public Color getIntensity(Point p) {
        double d = position.distance(p);
        return intensity.scale(1 / (kC + kL * d + kQ * d * d));
    }

    /**
     * Returns the normalized direction vector from the light source to a given point.
     *
     * @param p the point to which the direction is calculated
     * @return the normalized vector from the light source to the point
     */
    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }

    @Override
    public double getDistance(Point point) {
        return this.position.distance(point);
    }
    public Point getPosition() {

        return position;
    }

}
