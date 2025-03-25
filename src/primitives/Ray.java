package primitives;

public class Ray {
    Point head;
    Vector direction;
    public Ray(Point p, Vector v) {
        head = p;
        direction = v.normalize();
    }
}
