package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Triangular extends Polygon {
    private Point a;
    private Point b;
    private Point c;

    public Triangular(Point a, Point b, Point c) {
        super(a, b, c);
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public Vector getNormal(Point point) {
        return plane.getNormal(point);
    }
    @Override
    public List<Point> findIntersections(Ray ray) {
        Point p0 = ray.getHead();
        Vector dir = ray.getDirection();
        Point v0 = vertices.get(0);
        Point v1 = vertices.get(1);
        Point v2 = vertices.get(2);

        Vector edge1 = v1.subtract(v0);
        Vector edge2 = v2.subtract(v0);

        Vector h = dir.crossProduct(edge2);
        double a = alignZero(edge1.dotProduct(h));
        if (isZero(a)) {
            return null; // הקרן מקבילה למישור המשולש
        }

        double f = 1.0 / a;
        Vector s = p0.subtract(v0);
        double u = alignZero(f * s.dotProduct(h));
        if (u <= 0 || u >= 1) {
            return null; // מחוץ למשולש (כולל קצה)
        }

        Vector q = s.crossProduct(edge1);
        double v = alignZero(f * dir.dotProduct(q));
        if (v <= 0 || u + v >= 1) {
            return null; // מחוץ למשולש או על קצה
        }

        double t = alignZero(f * edge2.dotProduct(q));
        if (t <= 0) {
            return null; // החיתוך מאחורי הקרן
        }

        return List.of(ray.getPoint(t));
    }

}
