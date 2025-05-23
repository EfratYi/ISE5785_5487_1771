package lighting;

import primitives.Color;

/**
 * Represents ambient light in a scene, providing a base level of illumination.
 */
public class AmbientLight extends Light {

    /**
     * A constant representing no ambient light (black color).
     */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * Constructs an AmbientLight with the specified intensity.
     *
     * @param IA the intensity of the ambient light
     */
    public AmbientLight(Color IA) {
        super(IA);
    }

    /**
     * Returns the intensity of the ambient light.
     *
     * @return the intensity as a {@link primitives.Color} object
     */
    public Color getIntensity() {
        return intensity;
    }
}