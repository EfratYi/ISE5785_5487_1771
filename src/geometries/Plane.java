package geometries;

import primitives.Point;
import primitives.Vector;

public class Plane extends Geometry{
    public Point q0;
    public Vector normal;

    public Plane(Point q0, Vector normal) {
        this.q0 = q0;
        this.normal = normal.normalize();
    }
    public Plane(Point point1, Point point2, Point point3) {
        this.q0=point1;
        Vector u = point2.subtract(point1);
        Vector v = point3.subtract(point1);
        if(u.crossProduct(v).length() == 0) {
            throw new IllegalArgumentException("The points are on the same line");
        }
        Vector n = u.crossProduct(v);
        this.normal = n.normalize();
    }

    @Override
    public Vector getNormal(Point point) {
        return normal;
    }
    public Vector getNormal() {
        return normal;
    }
}
