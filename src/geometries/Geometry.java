package geometries;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Abstract class representing a geometric object in 3D space.
 * Provides methods for material and emission color management,
 * and an abstract method to calculate the normal vector at a given point on the geometry.
 */
public abstract class Geometry extends Intersectable {
    /**
     * The emission color of the geometry (default is black).
     */
    protected Color emission = Color.BLACK;

    /**
     * The material properties of the geometry.
     */
    private Material material = new Material();

    /**
     * Returns the emission color of the geometry.
     *
     * @return the emission color
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Returns the material of the geometry.
     *
     * @return the material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the material of the geometry.
     *
     * @param material the material to set
     * @return this geometry instance for chaining
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }

    /**
     * Sets the emission color of the geometry.
     *
     * @param emission the emission color to set
     * @return this geometry instance for chaining
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * Calculates the normal vector to the geometry at the specified point.
     *
     * @param point the point on the geometry where the normal is to be calculated
     * @return the normal vector to the geometry at the given point
     */
    public abstract Vector getNormal(Point point);
}
