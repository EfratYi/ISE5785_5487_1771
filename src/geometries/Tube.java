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
        Vector dir = _ray.getDirection();     // כיוון הציר
        Point p0 = _ray.getHead();            // ראש הקרן

        Vector pMinusP0 = point.subtract(p0); // וקטור בין נקודת הקרן לנקודה
        double t = dir.dotProduct(pMinusP0);

        Point o;  // נקודה על הציר הקרובה ל־point
        if (t == 0) {
            o = p0;
        } else {
            o = p0.add(dir.scale(t));
        }

        Vector normal = point.subtract(o);

        if (normal.lengthSquared() == 0) {
            throw new IllegalArgumentException("Cannot compute normal: point lies on the axis");
        }

        return normal.normalize();
    }



}
