package geometries;

import primitives.Ray;

public class Cylinder extends Tube{
    double _height;
    public Cylinder(double radius, Ray ray, double height) {
        super(radius, ray);
        this._height=height;
    }
}
