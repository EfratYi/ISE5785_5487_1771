package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * The Geometries class represents a collection of geometric shapes.
 * It acts as a composite in the Composite design pattern, allowing multiple geometries
 * to be treated as a single entity. This enables operations like finding intersections
 * to be performed on the entire collection as if it were a single geometry.
 */
public class Geometries extends Intersectable {
    @Override
    public void setBoundingBox() {
        if (shapes.isEmpty()) return;

        double minX = Double.POSITIVE_INFINITY, maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;

        for (Intersectable shape : shapes) {
            shape.setBoundingBox();
            AABB box = shape.getBoundingBox();
            if (box == null || Double.isInfinite(box.minX) || Double.isInfinite(box.maxX)) continue;
            minX = Math.min(minX, box.minX);
            maxX = Math.max(maxX, box.maxX);
            minY = Math.min(minY, box.minY);
            maxY = Math.max(maxY, box.maxY);
            minZ = Math.min(minZ, box.minZ);
            maxZ = Math.max(maxZ, box.maxZ);

        }

        this.boundingBox = new AABB(minX, maxX, minY, maxY, minZ, maxZ);
//        if (shapes.isEmpty()) return;
//
//        List<AABB> boxes = new LinkedList<>();
//        for (Intersectable shape : shapes) {
//            shape.setBoundingBox();
//            AABB box = shape.getBoundingBox();
//            if (box != null) {
//                boxes.add(box);
//            }
//        }
//
//        this.boundingBox = AABB.union(boxes);
    }

    /**
     * Build BVH tree with sorting by longest axis of bounding box.
     * Splits shapes into two halves recursively.
     */
    public void buildBVH() {
        setBoundingBox();
        if (shapes.size() <= 2) return;

        double dx = boundingBox.maxX - boundingBox.minX;
        double dy = boundingBox.maxY - boundingBox.minY;
        double dz = boundingBox.maxZ - boundingBox.minZ;


        int axis = 0; // 0=X, 1=Y, 2=Z
        if (dy > dx && dy > dz) axis = 1;
        else if (dz > dx && dz > dy) axis = 2;

        final int sortAxis = axis;
        shapes.sort((a, b) -> {
            double aCoord = 0, bCoord = 0;
            switch (sortAxis) {
                case 0 -> {
                    aCoord = a.getBoundingBox().minX;
                    bCoord = b.getBoundingBox().minX;
                }
                case 1 -> {
                    aCoord = a.getBoundingBox().minY;
                    bCoord = b.getBoundingBox().minY;
                }
                case 2 -> {
                    aCoord = a.getBoundingBox().minZ;
                    bCoord = b.getBoundingBox().minZ;
                }
            }
            return Double.compare(aCoord, bCoord);
        });

        int mid = shapes.size() / 2;
        List<Intersectable> leftList = shapes.subList(0, mid);
        List<Intersectable> rightList = shapes.subList(mid, shapes.size());

        Geometries left = new Geometries();
        Geometries right = new Geometries();
        left.add(leftList.toArray(new Intersectable[0]));
        right.add(rightList.toArray(new Intersectable[0]));

        left.buildBVH();
        right.buildBVH();

        shapes.clear();
        shapes.add(left);
        shapes.add(right);
//        setBoundingBox();

    }
    /**
     * A list that holds all the geometric shapes in the collection.
     */
    private final List<Intersectable> shapes = new LinkedList<>();

    /**
     * Constructs a Geometries object and initializes it with the given geometries.
     * This allows creating a composite of multiple geometric shapes.
     *
     * @param geometries the geometries to add to the collection
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds one or more geometries to the collection.
     * This method allows dynamically extending the composite with additional shapes.
     *
     * @param geometries the geometries to add
     */
    public void add(Intersectable... geometries) {
        Collections.addAll(shapes, geometries);
    }

    /**
     * Finds the intersection points of a given ray with all geometries in the collection.
     * This method iterates through all the geometries in the composite and collects
     * their intersection points. If no intersections are found, it returns null.
     *
     * @param ray the ray to intersect with the geometries
     * @return a list of intersection points, or null if there are no intersections
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        if (this.boundingBox != null && !this.boundingBox.intersects(ray)) {
            return null;
        }
        if (shapes.isEmpty()) {
            return null;
        }
        List<Intersection> intersections = null;  // Initialize as null
        for (Intersectable shape : shapes) {
            if (shape.getBoundingBox() != null && !shape.getBoundingBox().intersects(ray)) {
                continue;  // Skip shapes that do not intersect with the ray
            }
            List<Intersection> shapeIntersections = shape.calculateIntersections(ray);
            if (shapeIntersections != null) {
                if (intersections == null) {
                    intersections = new LinkedList<>();  // Initialize only if there are intersections
                }

                intersections.addAll(shapeIntersections);  // Add all intersections from the shape
            }
        }
        return intersections;  // Return null if no intersections
    }
}
