package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Class representing a directional light source.
 * A directional light simulates a light source that is infinitely far away,
 * such as sunlight, which has a constant direction and intensity.
 */
public class DirectionalLight extends Light implements LightSource {

    /**
     * The constant direction vector of the light.
     */
    private final Vector direction;

    /**
     * Constructs a directional light with specified intensity and direction.
     *
     * @param intensity the color intensity of the light
     * @param direction the direction vector of the light, will be normalized
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    /**
     * Returns the color intensity of the light at a given point.
     * For directional light, intensity is constant and does not depend on the point.
     *
     * @param p the point where intensity is calculated
     * @return the constant color intensity
     */
    @Override
    public Color getIntensity(Point p) {
        return intensity; // Directional light has constant intensity
    }

    /**
     * Returns the direction vector from the light to the given point.
     * For directional light, this vector is constant.
     *
     * @param p the point to which the direction is calculated
     * @return the constant direction vector
     */
    @Override
    public Vector getL(Point p) {
        return direction; // Directional light direction is constant
    }

    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY;
    }
}
