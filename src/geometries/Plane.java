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
        this.normal=null;
        this.q0=point1;
    }

    @Override
    public Vector getNormal(Point point) {
        return normal;
    }
    public Vector getNormal() {
        return normal;
    }
}
