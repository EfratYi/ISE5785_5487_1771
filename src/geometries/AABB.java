package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Axis-Aligned Bounding Box (AABB) implementation.
 */
public class AABB {
    public final double minX, maxX;
    public final double minY, maxY;
    public final double minZ, maxZ;


    /**
     * Constructs an AABB with explicit min/max for each axis.
     */
    public AABB(double minX, double maxX, double minY, double maxY, double minZ, double maxZ) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

//    /**
//     * Constructs an AABB from two corner points.
//     */
//    public AABB(Point min, Point max) {
//        this(min.getX(), max.getX(), min.getY(), max.getY(), min.getZ(), max.getZ());
//    }
//
//

    /**
     * Unites multiple bounding boxes into one that contains all of them.
     */
    public static AABB union(List<AABB> boxes) {
        if (boxes.isEmpty()) return null;

        double minX = Double.POSITIVE_INFINITY, maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;

        for (AABB box : boxes) {
            if (box == null) continue; // מתעלמים מתיבות ריקות

            minX = Math.min(minX, box.minX);
            maxX = Math.max(maxX, box.maxX);
            minY = Math.min(minY, box.minY);
            maxY = Math.max(maxY, box.maxY);
            minZ = Math.min(minZ, box.minZ);
            maxZ = Math.max(maxZ, box.maxZ);
        }

        return new AABB(minX, maxX, minY, maxY, minZ, maxZ);
    }

    /**
     * Tests whether the given ray intersects this bounding box.
     * Implements the slab method.
     */
    public boolean intersects(Ray ray) {
        Point origin = ray.getHead();
        Vector dir = ray.getDirection();

        double ox = origin.getX();
        double oy = origin.getY();
        double oz = origin.getZ();

        double dx = dir.getX();
        double dy = dir.getY();
        double dz = dir.getZ();

        double invDx = 1.0 / dx;
        double invDy = 1.0 / dy;
        double invDz = 1.0 / dz;

        double tx1 = (minX - ox) * invDx;
        double tx2 = (maxX - ox) * invDx;
        double ty1 = (minY - oy) * invDy;
        double ty2 = (maxY - oy) * invDy;
        double tz1 = (minZ - oz) * invDz;
        double tz2 = (maxZ - oz) * invDz;

        double tmin = Math.max(Math.max(Math.min(tx1, tx2), Math.min(ty1, ty2)), Math.min(tz1, tz2));
        double tmax = Math.min(Math.min(Math.max(tx1, tx2), Math.max(ty1, ty2)), Math.max(tz1, tz2));

        return tmax >= tmin && tmax >= 0;
    }
}
