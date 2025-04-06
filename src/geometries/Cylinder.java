package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

public class Cylinder extends Tube{
    double _height;
    public Cylinder(double radius, Ray ray, double height) {
        super(radius, ray);
        this._height=height;
    }
  @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
