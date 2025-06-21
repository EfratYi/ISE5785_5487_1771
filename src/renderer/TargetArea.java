package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.isZero;

/**
 * Represents a target area for soft shadows.
 * The target area is a plane around a light source from which multiple shadow rays are cast.
 */
public class TargetArea {

    // Resolution (square root of the number of beams)
    int res;

    // Size of each side (size of the light source)
    double size;

    // Direction to move up on the board
    Vector vUp;

    //Direction to move right on the board
    Vector vRight;

    //The direction we move the board from p0
    Vector vTo;

    // Center of the board
    Point p0;

    /**
     * Constructs a Blackboard object with the given parameters.
     *
     * @param res  the resolution (square root of the number of beams)
     * @param size the size of each side (size of the light source)
     * @param vTo  the direction to move the board from p0
     * @param vUp  the direction to move up on the board
     * @param p0   the center of the board
     */
    public TargetArea(int res, double size, Vector vTo, Vector vUp, Point p0) {
        this.res = res;
        this.size = size;
        this.vTo = vTo.normalize();
        this.vUp = vUp.normalize();
        this.vRight = this.vTo.crossProduct(this.vUp).normalize();
        this.p0 = p0;
    }

    /**
     * Constructs a ray between the given point and a pixel on the board.
     * If the pixel is outside the circle, returns null.
     *
     * @param j the column index of the pixel
     * @param i the row index of the pixel
     * @param p the point the ray starts at (a point on the geometry)
     * @return a new Ray object representing the constructed ray, or null if the pixel is outside the circle
     */
    public Ray constructRay(int j, int i, Point p) {
        // מרכז הלוח הוא מיקום מקור האור
        Point pc = p0;

        // חישוב גודל פיקסל בודד
        double pixelSize = size / res;

        // חישוב מיקום הפיקסל על הלוח
        double yi = (i - (res - 1) / 2.0) * pixelSize;
        double xj = (j - (res - 1) / 2.0) * pixelSize;

        Point pij = pc;
        if (!isZero(xj))
            pij = pij.add(vRight.scale(xj));
        if (!isZero(yi))
            pij = pij.add(vUp.scale(yi));

        // בדיקה אם הפיקסל מחוץ למעגל (רדיוס = size/2)
        if (pc.distance(pij) > size / 2.0)
            return null;

        // חישוב כיוון הקרן מנקודת הפגיעה למקור האור המדגם
        Vector dir = pij.subtract(p).normalize();

        // יצירת והחזרת הקרן עם DELTA משופר
        Vector normal = dir.scale(-1); // הנורמל הוא בכיוון הפוך לכיוון הקרן
        return new Ray(p, dir, normal);
    }
}