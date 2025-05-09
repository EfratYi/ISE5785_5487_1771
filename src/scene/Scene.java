package scene;

import lighting.AmbientLight;
import geometries.Geometries;
import primitives.Color;

/**
 * Represents a 3D scene containing geometries, lighting, and background settings.
 */
public class Scene {
    /**
     * The name of the scene.
     */
    public String name;

    /**
     * The background color of the scene. Defaults to black.
     */
    public Color background = Color.BLACK;

    /**
     * The ambient light of the scene. Defaults to no ambient light.
     */
    public AmbientLight ambientLight = AmbientLight.NONE;

    /**
     * The collection of geometries in the scene.
     */
    public Geometries geometries = new Geometries();

    /**
     * Constructs a new Scene with the specified name.
     *
     * @param name the name of the scene
     */
    public Scene(String name) {
        this.name = name;
    }

    /**
     * Sets the background color of the scene.
     *
     * @param background the background color
     * @return the current Scene instance
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Sets the ambient light of the scene.
     *
     * @param ambientLight the ambient light
     * @return the current Scene instance
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Sets the geometries of the scene.
     *
     * @param geometries the collection of geometries
     * @return the current Scene instance
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }
}