package lighting;

import primitives.Color;

/**
 * Abstract base class representing a light source with a certain color intensity.
 * This class is intended to be extended by specific types of lights (e.g., point light, spot light).
 */
public abstract class Light {

    /**
     * The color intensity of the light.
     */
    protected final Color intensity;

    /**
     * Constructs a light with the given color intensity.
     *
     * @param intensity the color intensity of the light
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Returns the color intensity of the light.
     *
     * @return the color intensity
     */
    public Color getIntensity() {
        return intensity;
    }
}
