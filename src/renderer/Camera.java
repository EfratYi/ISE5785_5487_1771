package renderer;

import primitives. Point;
import primitives.Vector;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Camera implements Cloneable {
    private Camera() {



    }



        private Vector vTo;
        private Vector vUp;
        private Vector vRight;
        private Point p0;
        private double distance = 0.0;
        private double width = 0.0;
        private double height = 0.0;


        public Builder getBuilder() {
            return new Builder();
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
        public double getWidth() {
            return width;
        }
        public double getHeight() {
            return height;
        }
        public void setVpSize(double width, double height) {
            if(alignZero(width) == 0 || alignZero(height) == 0)
                throw new IllegalArgumentException("width and height must be greater than 0");
            this.width = width;
            this.height = height;
        }


        public void setDistance(double distance) {
            this.distance = distance;
        }
        public void setVTo(Vector vTo) {
            this.vTo = vTo;
        }
        public void setVUp(Vector vUp) {
            this.vUp = vUp;
        }
        public void setVRight(Vector vRight) {
            this.vRight = vRight;
        }
        public void setP0(Point p0) {
            this.p0 = p0;
        }




public static class Builder{
    private final Camera camera = new Camera();

    public void setDirection(Vector vTo, Vector vUp) {
        if(!isZero(vTo.dotProduct(vUp))){
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

    public Builder setVTo(Vector vTo) {
        camera.vTo = vTo;
        return this;
    }

    public Builder setVUp(Vector vUp) {
        camera.vUp = vUp;
        return this;
    }

    public Builder setVRight(Vector vRight) {
        camera.vRight = vRight;
        return this;
    }

    public Builder setP0(Point p0) {
        camera.p0 = p0;
        return this;
    }

    public Builder setDistance(double distance) {
        camera.distance = distance;
        return this;
    }

    public Builder setWidth(double width) {
        camera.width = width;
        return this;
    }

    public Builder setHeight(double height) {
        camera.height = height;
        return this;
    }

    public Camera build() {

        try {
            return (Camera) camera.clone();
        } catch (CloneNotSupportedException e) {
            return null;        }
    }

}

}