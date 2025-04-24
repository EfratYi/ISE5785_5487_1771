package renderer;

import primitives.Point;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Camera implements Cloneable {
    private Camera() {
    }

    private Vector vTo;
    private Vector vUp;
    private Vector vRight;
    private Point p0;
    private Point pcenter;
    private double distance = 0.0;
    private double width = 0.0;
    private double height = 0.0;


    public Builder getBuilder() {
        return new Builder();
    }

    public List<Point> constructRayThroughPixel(int nX, int nY, int j, int i) {
        return null;
    }

    public Vector getVTo() {
        return vTo;
    }

    public Vector getVUp() {
        return vUp;
    }

    public Vector getVRight() {
        return vRight;
    }

    public Point getP0() {
        return p0;
    }

    public double getDistance() {
        return distance;
    }

    public Point getPcenter() {
        return pcenter;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }


    public static class Builder {
        private final Camera camera = new Camera();


        public void setDirection(Vector vTo, Vector vUp) {
            if (!isZero(vTo.dotProduct(vUp))) {
                throw new IllegalArgumentException("vTo and vUp must be orthogonal");
            }
            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            camera.vRight = vTo.crossProduct(vUp).normalize();
        }

        public Builder setLocation(Point p0) {
            camera.p0 = p0;
            return this;
        }

        public Builder setVpDistance(double distance) {
            if (alignZero(distance) <= 0) {
                throw new IllegalArgumentException("Distance must be positive");
            }
            camera.distance = distance;

            return this;
        }

        public Builder setVpSize(double width, double height) {
            if (alignZero(width) <= 0 || alignZero(height) <= 0) {
                throw new IllegalArgumentException("Width and height must be positive");
            }
            camera.width = width;
            camera.height = height;
            return this;
        }

        public Camera build() {

            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }

    }

}