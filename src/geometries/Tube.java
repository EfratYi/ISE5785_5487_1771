package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

public class Tube extends RadialGeometry{
    Ray _ray;
    public Tube(double radius, Ray ray) {
        super(radius);
        this._ray=ray;
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
