package geometries;

/**
 * Abstract class representing radial geometries in 3D space, such as spheres or cylinders.
 * Radial geometries are defined by a radius.
 */
public abstract class RadialGeometry extends Geometry {
    /**
     * The radius of the radial geometry.
     */
    final double _radius;

    /**
     * Constructor to initialize a radial geometry with a given radius.
     *
     * @param radius the radius of the radial geometry
     */
    public RadialGeometry(double radius) {
        _radius = radius;
    }
}