package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Represents a camera in a 3D scene, responsible for constructing rays through pixels.
 */
public class Camera implements Cloneable {

    /**
     * Private constructor to enforce the use of the Builder pattern.
     */
    private Camera() {
    }

    private Vector vTo = new Vector(0, 0, -1);
    private Vector vUp = new Vector(0, -1, 0);
    private Vector vRight = new Vector(-1, 0, 0);
    private Point p0 = Point.ZERO;
    private double distance = 0.0;
    private double width = 0.0;
    private double height = 0.0;
    private renderer.ImageWriter imageWriter;
    private renderer.RayTracerBase rayTracer;
    private int nX = 1;
    private int nY = 1;

    /**
     * Returns a new Builder instance for constructing a Camera object.
     *
     * @return a new Builder instance
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Constructs a list of points representing rays through a specific pixel.
     *
     * @param nX the number of horizontal pixels
     * @param nY the number of vertical pixels
     * @param j  the column index of the pixel
     * @param i  the row index of the pixel
     * @return a list of points representing the rays
     */
    public List<Point> constructRayThroughPixel(int nX, int nY, int j, int i) {
        return null;
    }

    /**
     * @return the direction vector pointing to the view plane
     */
    public Vector getVTo() {
        return vTo;
    }

    /**
     * @return the up direction vector of the camera
     */
    public Vector getVUp() {
        return vUp;
    }

    /**
     * @return the right direction vector of the camera
     */
    public Vector getVRight() {
        return vRight;
    }

    /**
     * @return the location of the camera
     */
    public Point getP0() {
        return p0;
    }

    /**
     * @return the distance from the camera to the view plane
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @return the width of the view plane
     */
    public double getWidth() {
        return width;
    }

    /**
     * @return the height of the view plane
     */
    public double getHeight() {
        return height;
    }

    /**
     * Constructs a ray through a specific pixel in the view plane.
     *
     * @param nX the number of horizontal pixels
     * @param nY the number of vertical pixels
     * @param j  the column index of the pixel
     * @param i  the row index of the pixel
     * @return the constructed ray
     * @throws IllegalArgumentException if the pixel coordinates are invalid
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        if (nX <= 0 || nY <= 0)
            throw new IllegalArgumentException("Invalid pixel coordinates");

        Point pIJ = p0;

        double yI = -(i - (nY - 1) / 2d) * height / nY;
        double xJ = (j - (nX - 1) / 2d) * width / nX;

        if (!isZero(xJ)) pIJ = pIJ.add(vRight.scale(xJ));
        if (!isZero(yI)) pIJ = pIJ.add(vUp.scale(yI));

        pIJ = pIJ.add(vTo.scale(distance));

//        return new Ray(p0, pIJ.subtract(p0).normalize());
        return new Ray(p0, pIJ.subtract(p0));//pC-p0


    }

    /**
     * Renders the image by casting rays through all pixels.
     *
     * @return the Camera instance
     */
    public Camera renderImage() {
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                castRay(j, i);
            }
        }
        return this;
    }

    /**
     * Prints a grid on the image with a specified interval and color.
     *
     * @param interval the interval between grid lines
     * @param color    the color of the grid lines
     * @return the Camera instance
     */
    public Camera printGrid(int interval, primitives.Color color) {
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                if (i % interval == 0 || j % interval == 0) {
                    imageWriter.writePixel(j, i, color);
                }
            }
        }
        return this;
    }

    /**
     * Writes the rendered image to a file.
     *
     * @param filename the name of the file
     * @return the Camera instance
     */
    public Camera writeToImage(String filename) {
        imageWriter.writeToImage(filename);
        return this;
    }

    /**
     * Casts a ray through a specific pixel and writes the resulting color to the image.
     *
     * @param j the column index of the pixel
     * @param i the row index of the pixel
     */
    private void castRay(int j, int i) {
        Ray ray = constructRay(nX, nY, j, i);
        primitives.Color color = rayTracer.traceRay(ray);
        imageWriter.writePixel(j, i, color);
    }

    /**
     * Builder class for constructing a Camera object.
     */
    public static class Builder {
        private final Camera camera = new Camera();

        /**
         * Sets the direction vectors of the camera.
         *
         * @param vTo the direction vector pointing to the view plane
         * @param vUp the up direction vector
         * @throws IllegalArgumentException if vTo and vUp are not orthogonal
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if (!isZero(vUp.dotProduct(vTo))) {
                throw new IllegalArgumentException("vUp and vTo must be orthogonal");
            }
            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            camera.vRight = vTo.crossProduct(vUp).normalize();
            return this;
        }

        /**
         * Sets the direction of the camera to a target point.
         *
         * @param target the target point
         * @return this Builder instance
         */
        public Builder setDirection(Point target) {
            return setDirection(target, Vector.AXIS_Y);
        }

        /**
         * Sets the direction of the camera to a target point with a specified up vector.
         *
         * @param target the target point
         * @param vUp    the up direction vector
         * @return this Builder instance
         * @throws IllegalArgumentException if vTo and vUp are not orthogonal
         */
        public Builder setDirection(Point target, Vector vUp) {
            camera.vTo = target.subtract(camera.p0).normalize();
            camera.vRight = camera.vTo.crossProduct(vUp).normalize();
            if (!isZero(camera.vTo.dotProduct(camera.vRight)))
                throw new IllegalArgumentException("vUp and vTo must be orthogonal");
            camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();
            return this;
        }

        /**
         * Sets the location of the camera.
         *
         * @param p0 the location point
         * @return this Builder instance
         */
        public Builder setLocation(Point p0) {
            camera.p0 = p0;
            return this;
        }

        /**
         * Sets the distance from the camera to the view plane.
         *
         * @param distance the distance value
         * @return this Builder instance
         * @throws IllegalArgumentException if the distance is not positive
         */
        public Builder setVpDistance(double distance) {
            if (alignZero(distance) <= 0) {
                throw new IllegalArgumentException("Distance must be positive");
            }
            camera.distance = distance;
            return this;
        }

        /**
         * Sets the size of the view plane.
         *
         * @param width  the width of the view plane
         * @param height the height of the view plane
         * @return this Builder instance
         * @throws IllegalArgumentException if width or height is not positive
         */
        public Builder setVpSize(double width, double height) {
            if (alignZero(width) <= 0 || alignZero(height) <= 0) {
                throw new IllegalArgumentException("Width and height must be positive");
            }
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Sets the resolution of the view plane.
         *
         * @param nX the number of horizontal pixels
         * @param nY the number of vertical pixels
         * @return this Builder instance
         */
        public Builder setResolution(int nX, int nY) {
            camera.nX = nX;
            camera.nY = nY;
            return this;
        }

        /**
         * Builds and returns the Camera object.
         *
         * @return the constructed Camera object
         */
        public Camera build() {
            if (camera.nX <= 0 || camera.nY <= 0) {
                throw new IllegalArgumentException("Resolution must be positive");
            }
            camera.imageWriter = new renderer.ImageWriter(camera.nX, camera.nY);
            if (camera.rayTracer == null) {
                camera.rayTracer = new renderer.SimpleRayTracer(null);
            }
            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }

        /**
         * Sets the ray tracer for the camera.
         *
         * @param scene the scene to be rendered
         * @param type  the type of ray tracer
         * @return this Builder instance
         */
        public Builder setRayTracer(Scene scene, RayTracerType type) {
            switch (type) {
                case SIMPLE:
                    camera.rayTracer = new renderer.SimpleRayTracer(scene);
                    break;
                default:
                    camera.rayTracer = null;
            }
            return this;
        }
    }
}