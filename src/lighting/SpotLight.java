package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a spotlight light source.
 * A spotlight is a point light source with a direction,
 * so the light intensity is influenced by the angle between the light's direction and the direction to the point.
 */
public class SpotLight extends PointLight {

    /**
     * The direction of the spotlight.
     */
    private final Vector direction;

    /**
     * Constructs a spotlight with the specified intensity, position, and direction.
     *
     * @param intensity the color intensity of the light
     * @param position  the position of the light source
     * @param direction the direction of the spotlight
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    /**
     * Calculates the intensity of the spotlight at a given point.
     * The intensity is reduced based on the angle between the spotlight's direction and the direction to the point.
     *
     * @param p the point at which to calculate the intensity
     * @return the color intensity at the given point
     */
    @Override
    public Color getIntensity(Point p) {
        Vector l = getL(p);
        double dirDotL = direction.dotProduct(l);
        if (dirDotL <= 0) {
            return Color.BLACK;
        }
        return super.getIntensity(p).scale(dirDotL);
    }

    /**
     * Sets the constant attenuation factor.
     *
     * @param kC the constant attenuation factor
     * @return the spotlight itself for method chaining
     */
    @Override
    public SpotLight setKc(double kC) {
        super.setKc(kC);
        return this;
    }

    /**
     * Sets the linear attenuation factor.
     *
     * @param kL the linear attenuation factor
     * @return the spotlight itself for method chaining
     */
    @Override
    public SpotLight setKl(double kL) {
        super.setKl(kL);
        return this;
    }

    /**
     * Sets the quadratic attenuation factor.
     *
     * @param kQ the quadratic attenuation factor
     * @return the spotlight itself for method chaining
     */
    @Override
    public SpotLight setKq(double kQ) {
        super.setKq(kQ);
        return this;
    }
    @Override
    public SpotLight setRadius(double radius) {
        super.setRadius(radius);
        return this;
    }

}
