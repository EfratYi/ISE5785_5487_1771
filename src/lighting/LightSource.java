package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Interface representing a light source in a 3D scene.
 * Implementing classes should provide the color intensity of the light at a given point
 * and the direction vector from the light source to that point.
 */
public interface LightSource {

    /**
     * Returns the intensity (color) of the light at a specified point.
     *
     * @param p the point at which the intensity is calculated
     * @return the color intensity at the given point
     */
    Color getIntensity(Point p);

    /**
     * Returns the normalized vector pointing from the light source toward the specified point.
     *
     * @param p the point to which the direction is calculated
     * @return the normalized direction vector
     */
    Vector getL(Point p);
    double getDistance(Point point);

}
