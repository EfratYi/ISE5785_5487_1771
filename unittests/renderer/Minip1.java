package renderer;
import geometries.*;
import lighting.AmbientLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Point;
import primitives.Vector;
import scene.Scene;
import java.util.Random;

class Minip1 {
    private final Scene scene = new Scene("test_class");
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setRayTracer(scene, RayTracerType.SIMPLE);

    /**
     * Creates an enhanced pyramid geometry composed of base and side triangles.
     * The base triangles have a stronger green color, while the side faces
     * feature varied green tones to provide depth.
     * Each triangle is assigned specific material properties for diffuse (KD),
     * specular (KS) reflection, and shininess.
     *
     * @return a {@link Geometries} object containing six triangles forming the pyramid.
     */
    private Geometries createEnhancedPyramid() {
        Point apex = new Point(-70, 15, 20);
        Point p1 = new Point(-90, -50, 0);
        Point p2 = new Point(-50, -50, 0);
        Point p3 = new Point(-50, -50, 40);
        Point p4 = new Point(-90, -50, 40);

        return new Geometries(
                // Base triangles with stronger green
                new Triangle(p1, p2, p3)
                        .setEmission(new Color(40, 120, 60))
                        .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(30)),
                new Triangle(p1, p3, p4)
                        .setEmission(new Color(40, 120, 60))
                        .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(30)),
                // Side faces with varied green tones for depth
                new Triangle(p1, p2, apex)
                        .setEmission(new Color(50, 140, 70))
                        .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(30)),
                new Triangle(p2, p3, apex)
                        .setEmission(new Color(35, 110, 55))
                        .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(30)),
                new Triangle(p3, p4, apex)
                        .setEmission(new Color(30, 100, 50))
                        .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(30)),
                new Triangle(p4, p1, apex)
                        .setEmission(new Color(45, 130, 65))
                        .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(30))
        );
    }

    /**
     * Creates a realistic floor composed of two large triangles forming a rectangle.
     * The floor is positioned at the specified Y-coordinate and extends with the given size.
     * The floor triangles have a brownish color and a material with partial transparency.
     *
     * @param y    the Y-coordinate (height) at which the floor lies.
     * @param size the half-extent size of the floor in the X and Z directions.
     * @return a {@link Geometries} object containing two triangles forming the floor.
     */
    private Geometries createRealisticFloor(double y, double size) {
        return new Geometries(
                new Triangle(new Point(-size, y, -size * 2),
                        new Point(size, y, -size * 2),
                        new Point(size, y, size))
                        .setEmission(new Color(80, 60, 40))
                        .setMaterial(new Material()
                                .setKD(0.8)
                                .setKS(0.2)
                                .setShininess(10)
                                .setkT(0.1)),
                new Triangle(new Point(-size, y, -size * 2),
                        new Point(size, y, size),
                        new Point(-size, y, size))
                        .setEmission(new Color(80, 60, 40))
                        .setMaterial(new Material()
                                .setKD(0.8)
                                .setKS(0.2)
                                .setShininess(10)
                                .setkT(0.1))
        );
    }

    /**
     * Creates a large mirror composed of two triangles forming a rectangular surface.
     * The mirror is centered at the specified coordinates and has a reflective material
     * with high shininess and reflection coefficient.
     *
     * @return a {@link Geometries} object containing two triangles forming the mirror.
     */
    private Geometries createLargeMirror() {
        double width = 120;
        double height = 100;
        double centerX = -20;
        double centerY = 20;
        double centerZ = -60;

        Point p1 = new Point(centerX - width / 2, centerY - height / 2, centerZ);
        Point p2 = new Point(centerX + width / 2, centerY - height / 2, centerZ);
        Point p3 = new Point(centerX + width / 2, centerY + height / 2, centerZ);
        Point p4 = new Point(centerX - width / 2, centerY + height / 2, centerZ);

        return new Geometries(
                new Triangle(p1, p2, p3)
                        .setEmission(new Color(20, 20, 30))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(200).setkR(0.7)),
                new Triangle(p1, p3, p4)
                        .setEmission(new Color(20, 20, 30))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(200).setkR(0.7))
        );
    }

    /**
     * Creates a pyramid geometry at a specified base center point, with given size, height, and color.
     * The pyramid consists of a square base and four triangular side faces.
     * All faces share the same material properties for diffuse, specular reflection, and shininess.
     *
     * @param baseCenter the center point of the pyramid base.
     * @param size       the length of the base's side.
     * @param height     the height of the pyramid (distance from base to apex).
     * @param color      the emission color applied to all faces.
     * @return a {@link Geometries} object containing six triangles forming the pyramid.
     */
    private Geometries createRandomPyramid(Point baseCenter, double size, double height, Color color) {
        double half = size / 2;
        Point apex = baseCenter.add(new Vector(0, height, 0));
        Point p1 = baseCenter.add(new Vector(-half, 0, -half));
        Point p2 = baseCenter.add(new Vector(half, 0, -half));
        Point p3 = baseCenter.add(new Vector(half, 0, half));
        Point p4 = baseCenter.add(new Vector(-half, 0, half));

        Material mat = new Material().setKD(0.6).setKS(0.4).setShininess(40);

        return new Geometries(
                new Triangle(p1, p2, p3).setEmission(color).setMaterial(mat),
                new Triangle(p1, p3, p4).setEmission(color).setMaterial(mat),
                new Triangle(p1, p2, apex).setEmission(color).setMaterial(mat),
                new Triangle(p2, p3, apex).setEmission(color).setMaterial(mat),
                new Triangle(p3, p4, apex).setEmission(color).setMaterial(mat),
                new Triangle(p4, p1, apex).setEmission(color).setMaterial(mat)
        );
    }

    /**
     * Creates a sphere geometry with the specified center, radius, and color.
     * The sphere has fixed material properties for diffuse and specular reflection and shininess.
     *
     * @param center the center point of the sphere.
     * @param radius the radius of the sphere.
     * @param color  the emission color of the sphere.
     * @return a {@link Sphere} object with the specified attributes.
     */
    private Sphere createSphere(Point center, double radius, Color color) {
        return (Sphere) new Sphere(center, radius)
                .setEmission(color)
                .setMaterial(new Material().setKD(0.6).setKS(0.3).setShininess(60));
    }
    /**
     * Creates a translucent cube made of triangles.
     * The cube is centered at the given point, with a given size and color.
     * The cube has partial transparency and a slight reflective property.
     *
     * @param center the center point of the cube
     * @param size   the length of each edge of the cube
     * @param color  the emission color of the cube
     * @return a Geometries object containing the 12 triangles forming the cube
     */
    private Geometries createTranslucentCube(Point center, double size, Color color) {
        double half = size / 2;
        Material material = new Material()
                .setKD(0.3)      // diffuse reflection
                .setKS(0.4)      // specular reflection
                .setShininess(100)
                .setkT(0.5)      // moderate transparency
                .setkR(0.05);    // light reflection

        Point p1 = center.add(new Vector(-half, -half, -half));
        Point p2 = center.add(new Vector(half, -half, -half));
        Point p3 = center.add(new Vector(half, half, -half));
        Point p4 = center.add(new Vector(-half, half, -half));
        Point p5 = center.add(new Vector(-half, -half, half));
        Point p6 = center.add(new Vector(half, -half, half));
        Point p7 = center.add(new Vector(half, half, half));
        Point p8 = center.add(new Vector(-half, half, half));

        return new Geometries(
                new Triangle(p1, p2, p3).setEmission(color).setMaterial(material),
                new Triangle(p1, p3, p4).setEmission(color).setMaterial(material),
                new Triangle(p2, p6, p7).setEmission(color).setMaterial(material),
                new Triangle(p2, p7, p3).setEmission(color).setMaterial(material),
                new Triangle(p4, p3, p7).setEmission(color).setMaterial(material),
                new Triangle(p4, p7, p8).setEmission(color).setMaterial(material),
                new Triangle(p1, p4, p8).setEmission(color).setMaterial(material),
                new Triangle(p1, p8, p5).setEmission(color).setMaterial(material),
                new Triangle(p1, p5, p6).setEmission(color).setMaterial(material),
                new Triangle(p1, p6, p2).setEmission(color).setMaterial(material),
                new Triangle(p5, p8, p7).setEmission(color).setMaterial(material),
                new Triangle(p5, p7, p6).setEmission(color).setMaterial(material)
        );
    }

    /**
     * Creates a set of two walls: one with a window and one solid wall,
     * and a back wall to close the scene.
     *
     * @return a Geometries object containing the walls
     */
    Geometries createTwoWallsWithWindows() {
        Geometries walls = new Geometries();
        walls.add(createWallWithWindow(180));
        walls.add(createSolidWall(-300));
        walls.add(createBackWall(-730));
        return walls;
    }

    /**
     * Creates a solid wall (without a window) aligned along the X-axis.
     *
     * @param wallX the X coordinate of the wall
     * @return a Geometries object representing the wall
     */
    private Geometries createSolidWall(double wallX) {
        double wallYTop = 800;
        double wallYBottom = -80;
        double wallZLeft = -760;
        double wallZRight = 1500;

        Material wallMat = new Material().setKD(0.7).setKS(0.2).setShininess(30);
        Color wallColor = new Color(50, 50, 50);

        Geometries wall = new Geometries();

        wall.add(new Triangle(
                new Point(wallX, wallYTop, wallZLeft),
                new Point(wallX, wallYBottom, wallZLeft),
                new Point(wallX, wallYTop, wallZRight))
                .setEmission(wallColor).setMaterial(wallMat));

        wall.add(new Triangle(
                new Point(wallX, wallYBottom, wallZLeft),
                new Point(wallX, wallYBottom, wallZRight),
                new Point(wallX, wallYTop, wallZRight))
                .setEmission(wallColor).setMaterial(wallMat));

        return wall;
    }

    /**
     * Creates a back wall (along the Z-axis) of the scene.
     *
     * @param wallZ the Z coordinate of the wall
     * @return a Geometries object representing the wall
     */
    private Geometries createBackWall(double wallZ) {
        double wallYTop = 800;
        double wallYBottom = -80;
        double wallXLeft = -300;
        double wallXRight = 180;

        Material wallMat = new Material().setKD(0.7).setKS(0.2).setShininess(30);
        Color wallColor = new Color(50, 50, 50);

        Geometries wall = new Geometries();

        wall.add(new Triangle(
                new Point(wallXLeft, wallYTop, wallZ),
                new Point(wallXLeft, wallYBottom, wallZ),
                new Point(wallXRight, wallYTop, wallZ))
                .setEmission(wallColor).setMaterial(wallMat));

        wall.add(new Triangle(
                new Point(wallXLeft, wallYBottom, wallZ),
                new Point(wallXRight, wallYBottom, wallZ),
                new Point(wallXRight, wallYTop, wallZ))
                .setEmission(wallColor).setMaterial(wallMat));

        return wall;
    }

    /**
     * Creates a wall with a rectangular window opening.
     * The wall is composed of triangles around the window opening.
     *
     * @param wallX the X coordinate of the wall
     * @return a Geometries object representing the wall with the window
     */
    private Geometries createWallWithWindow(double wallX) {
        double wallYTop = 800;
        double wallYBottom = -80;
        double wallZLeft = -760;
        double wallZRight = 1500;

        double windowYTop = 40;
        double windowYBottom = 0;
        double windowZLeft = -80;
        double windowZRight = -10;

        Material wallMat = new Material().setKD(0.7).setKS(0.2).setShininess(30);
        Color wallColor = new Color(50, 50, 50);

        Geometries wall = new Geometries();

        // Top of window
        wall.add(new Triangle(
                new Point(wallX, wallYTop, wallZLeft),
                new Point(wallX, windowYTop, wallZLeft),
                new Point(wallX, wallYTop, wallZRight))
                .setEmission(wallColor).setMaterial(wallMat));
        wall.add(new Triangle(
                new Point(wallX, wallYTop, wallZRight),
                new Point(wallX, windowYTop, wallZLeft),
                new Point(wallX, windowYTop, wallZRight))
                .setEmission(wallColor).setMaterial(wallMat));

        // Bottom of window
        wall.add(new Triangle(
                new Point(wallX, windowYBottom, wallZLeft),
                new Point(wallX, wallYBottom, wallZLeft),
                new Point(wallX, windowYBottom, wallZRight))
                .setEmission(wallColor).setMaterial(wallMat));
        wall.add(new Triangle(
                new Point(wallX, windowYBottom, wallZRight),
                new Point(wallX, wallYBottom, wallZLeft),
                new Point(wallX, wallYBottom, wallZRight))
                .setEmission(wallColor).setMaterial(wallMat));

        // Right of window
        wall.add(new Triangle(
                new Point(wallX, windowYTop, windowZRight),
                new Point(wallX, windowYBottom, windowZRight),
                new Point(wallX, wallYTop, wallZRight))
                .setEmission(wallColor).setMaterial(wallMat));
        wall.add(new Triangle(
                new Point(wallX, wallYTop, wallZRight),
                new Point(wallX, windowYBottom, windowZRight),
                new Point(wallX, wallYBottom, wallZRight))
                .setEmission(wallColor).setMaterial(wallMat));

        // Left of window
        wall.add(new Triangle(
                new Point(wallX, windowYTop, windowZLeft),
                new Point(wallX, wallYTop, wallZLeft),
                new Point(wallX, windowYBottom, windowZLeft))
                .setEmission(wallColor).setMaterial(wallMat));
        wall.add(new Triangle(
                new Point(wallX, windowYBottom, windowZLeft),
                new Point(wallX, wallYTop, wallZLeft),
                new Point(wallX, wallYBottom, wallZLeft))
                .setEmission(wallColor).setMaterial(wallMat));

        return wall;
    }

    /**
     * Creates a transparent glass panel to be inserted in a window.
     *
     * @return a Geometries object representing the glass window pane
     */
    private Geometries createGlassInWindow() {
        double wallX = 180;
        double windowYTop = 40;
        double windowYBottom = 0;
        double windowZLeft = -80;
        double windowZRight = -10;

        Material glass = new Material()
                .setKD(0.3)      // diffuse reflection
                .setKS(0.4)      // specular reflection
                .setShininess(100)
                .setkT(0.5)      // moderate transparency
                .setkR(0.05);    // slight reflectivity

        Color glassColor = new Color(20, 20, 30); // dark glass

        Point p1 = new Point(wallX, windowYTop, windowZLeft);
        Point p2 = new Point(wallX, windowYTop, windowZRight);
        Point p3 = new Point(wallX, windowYBottom, windowZRight);
        Point p4 = new Point(wallX, windowYBottom, windowZLeft);

        return new Geometries(
                new Triangle(p1, p2, p3).setEmission(glassColor).setMaterial(glass),
                new Triangle(p1, p3, p4).setEmission(glassColor).setMaterial(glass)
        );
    }


    /**
     * Test method that creates a complex scene with multiple geometries and soft shadows.
     * The scene includes:
     * - A brown warm sunset-like background
     * - A large floor with matte material
     * - A large mirror reflecting the scene for visual depth
     * - A large green pyramid on the left
     * - Glass panes in a window
     * - Multiple light sources with spotlights and point lights, some with soft shadows
     * - Two walls with windows
     * - An ambient light with low intensity
     * - An array of 10 pyramids arranged in a grid, each with gradually changing colors
     * - 10 randomly positioned spheres with random colors and sizes
     * - 5 translucent cubes arranged in a row with a color gradient
     * Finally, the camera is set up with specified location, direction, view plane distance,
     * size, and resolution. The scene is rendered and written to an image file.
     */
    @Test
    void createSceneWithMultipleGeometriesSoftShadows() {
        // Set background color to a warm brown tone resembling sunset
        scene.setBackground(new Color(120, 80, 50));

        // Add a large floor surface with matte material
        scene.geometries.add(createRealisticFloor(-60, 400));

        // Add a large mirror in the background
        scene.geometries.add(createLargeMirror());

        // Add a large green pyramid (left side)
        scene.geometries.add(createEnhancedPyramid());

        // Add a glass object inside a window
        scene.geometries.add(createGlassInWindow());

        // Main dramatic spotlight - focused lighting with atmosphere
        scene.lights.add(new SpotLight(
                new Color(1400, 1100, 1000),
                new Point(200, 20, -45),
                new Vector(0.9995, 0, 0))
                .setRadius(12)
                .setKc(1).setKl(0.0001).setKq(0.000001)
        );

        // Secondary spotlight - illuminates from another angle
        scene.lights.add(new SpotLight(
                new Color(140, 110, 90),
                new Point(80, 100, 120),
                new Vector(-1, -1.2, -1))
                .setRadius(12)
                .setKc(1).setKl(0.0001).setKq(0.000001)
        );

        // Fill light - reduces hard shadows
        scene.lights.add(new PointLight(
                new Color(80, 70, 65),
                new Point(-80, 60, 100))
                .setRadius(8)
                .setKc(1).setKl(0.0002).setKq(0.000002)
        );

        // Accent light - emphasizes a specific area
        scene.lights.add(new SpotLight(
                new Color(100, 80, 50),
                new Point(150, 30, 50),
                new Vector(-1, -0.5, -1))
                .setRadius(6)
                .setKc(1).setKl(0.0003).setKq(0.000003)
        );

        // Add the wall with window on the right
        scene.geometries.add(createTwoWallsWithWindows());

        // Duplicate of the main dramatic spotlight (intentional or redundant)
        scene.lights.add(new SpotLight(
                new Color(1400, 1100, 1000),
                new Point(200, 20, -45),
                new Vector(0.9995, 0, 0))
                .setRadius(12)
                .setKc(1).setKl(0.0001).setKq(0.000001)
        );

        // Ambient light - soft global illumination
        scene.setAmbientLight(new AmbientLight(new Color(17, 17, 17)));

        // === Create 10 small pyramids in a grid layout ===
        for (int i = 0; i < 10; i++) {
            double x = -140 + (i % 5) * 70;  // 5 pyramids in a row, 70 units apart
            double z = -100 + (i / 5) * 80;  // 2 rows, 80 units apart
            Point center = new Point(x, -60, z);  // aligned to floor height
            scene.geometries.add(createRandomPyramid(center, 30, 40,
                    new Color(30 + i * 10, 100 + (i * 5) % 100, 60 + (i * 7) % 150)));
        }

        // === Create 10 spheres in random positions ===
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            double x = -120 + rand.nextDouble() * 240;  // between -120 and 120
            double z = 120 + rand.nextDouble() * 100;   // farther from pyramids (120-220)
            double y = -30;  // slightly above the floor
            Point center = new Point(x, y, z);
            double radius = 8 + rand.nextDouble() * 4;  // radius between 8 and 12
            int r = 80 + rand.nextInt(100);  // red 80–180
            int g = 50 + rand.nextInt(100);  // green 50–150
            int b = 80 + rand.nextInt(100);  // blue 80–180
            Color color = new Color(r, g, b);
            scene.geometries.add(createSphere(center, radius, color));
        }

        // === Create 5 transparent cubes in a row ===
        for (int i = 0; i < 5; i++) {
            double x = -80 + i * 40;  // 40 units apart
            double y = -45;           // slightly above floor
            double z = 150;           // same depth as spheres
            Point center = new Point(x, y, z);
            Color color = new Color(100 + i * 20, 80 + i * 10, 150 - i * 10);
            scene.geometries.add(createTranslucentCube(center, 25, color));
        }
//        scene.geometries.buildBVH();

        // === Configure camera and render image ===
        cameraBuilder
                .setLocation(new Point(10, 20, 440)) // further away, same line of sight
                .setDirection(new Point(-10, 20, -60), new Vector(0, 1, 0)) // looking at mirror
                .setVpDistance(180)
                .setVpSize(160, 160)
                .setResolution(800, 800)
                .build()
                .renderImage()
                .writeToImage("multiply_amount_of_geometries_with_soft_shadows_with_window");
    }

    @Test
    void createSceneWithMultipleGeometriesSoftShadows_MultiThreaded() {
        Scene scene = new Scene("Scene with many geometries and soft shadows");

        scene.setBackground(new Color(120, 80, 50));
        scene.geometries.add(createRealisticFloor(-60, 400));
        scene.geometries.add(createLargeMirror());
        scene.geometries.add(createEnhancedPyramid());
        scene.geometries.add(createGlassInWindow());
        scene.geometries.add(createTwoWallsWithWindows());

        // Light sources
        scene.lights.add(new SpotLight(
                new Color(1400, 1100, 1000),
                new Point(200, 20, -45),
                new Vector(0.9995, 0, 0))
                .setRadius(12)
                .setKc(1).setKl(0.0001).setKq(0.000001));

        scene.lights.add(new SpotLight(
                new Color(140, 110, 90),
                new Point(80, 100, 120),
                new Vector(-1, -1.2, -1))
                .setRadius(12)
                .setKc(1).setKl(0.0001).setKq(0.000001));

        scene.lights.add(new PointLight(
                new Color(80, 70, 65),
                new Point(-80, 60, 100))
                .setRadius(8)
                .setKc(1).setKl(0.0002).setKq(0.000002));

        scene.lights.add(new SpotLight(
                new Color(100, 80, 50),
                new Point(150, 30, 50),
                new Vector(-1, -0.5, -1))
                .setRadius(6)
                .setKc(1).setKl(0.0003).setKq(0.000003));

        // Duplicate main spotlight (intentional)
        scene.lights.add(new SpotLight(
                new Color(1400, 1100, 1000),
                new Point(200, 20, -45),
                new Vector(0.9995, 0, 0))
                .setRadius(12)
                .setKc(1).setKl(0.0001).setKq(0.000001));

        scene.setAmbientLight(new AmbientLight(new Color(17, 17, 17)));

        // Pyramids
        for (int i = 0; i < 10; i++) {
            double x = -140 + (i % 5) * 70;
            double z = -100 + (i / 5) * 80;
            Point center = new Point(x, -60, z);
            scene.geometries.add(createRandomPyramid(center, 30, 40,
                    new Color(30 + i * 10, 100 + (i * 5) % 100, 60 + (i * 7) % 150)));
        }

        // Spheres
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            double x = -120 + rand.nextDouble() * 240;
            double z = 120 + rand.nextDouble() * 100;
            double y = -30;
            Point center = new Point(x, y, z);
            double radius = 8 + rand.nextDouble() * 4;
            Color color = new Color(
                    80 + rand.nextInt(100),
                    50 + rand.nextInt(100),
                    80 + rand.nextInt(100));
            scene.geometries.add(createSphere(center, radius, color));
        }

        // Transparent cubes
        for (int i = 0; i < 5; i++) {
            double x = -80 + i * 40;
            double y = -45;
            double z = 150;
            Point center = new Point(x, y, z);
            Color color = new Color(100 + i * 20, 80 + i * 10, 150 - i * 10);
            scene.geometries.add(createTranslucentCube(center, 25, color));
        }

        // Optional: Build BVH for performance
        // scene.geometries.buildBVH();

        cameraBuilder
                .setResolution(800, 800)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(10, 20, 440))
                .setDirection(new Point(-10, 20, -60), new Vector(0, 1, 0))
                .setVpDistance(180)
                .setVpSize(160, 160)
                .setMultithreading(-2) // לדוגמה: -2 = מספר תהליכונים אוטומטי
                .setDebugPrint(1.0)  // הדפסת התקדמות כל 1%
                .build()
                .renderImage()
                .writeToImage("multiply_amount_of_geometries_with_soft_shadows_with_window_multythreaded");
    }

    @Test
    void createSceneWithMultipleGeometries() {
        // Set background color to a warm brown tone resembling sunset
        scene.setBackground(new Color(120, 80, 50));

        // Add a large floor surface with matte material
        scene.geometries.add(createRealisticFloor(-60, 400));

        // Add a large mirror in the background
        scene.geometries.add(createLargeMirror());

        // Add a large green pyramid (left side)
        scene.geometries.add(createEnhancedPyramid());

        // Add a glass object inside a window
        scene.geometries.add(createGlassInWindow());

        // Main dramatic spotlight - focused lighting with atmosphere
        scene.lights.add(new SpotLight(
                new Color(1400, 1100, 1000),
                new Point(200, 20, -45),
                new Vector(0.9995, 0, 0))
                .setRadius(0)
                .setKc(1).setKl(0.0001).setKq(0.000001)
        );

        // Secondary spotlight - illuminates from another angle
        scene.lights.add(new SpotLight(
                new Color(140, 110, 90),
                new Point(80, 100, 120),
                new Vector(-1, -1.2, -1))
                .setRadius(0)
                .setKc(1).setKl(0.0001).setKq(0.000001)
        );

        // Fill light - reduces hard shadows
        scene.lights.add(new PointLight(
                new Color(80, 70, 65),
                new Point(-80, 60, 100))
                .setRadius(0)
                .setKc(1).setKl(0.0002).setKq(0.000002)
        );

        // Accent light - emphasizes a specific area
        scene.lights.add(new SpotLight(
                new Color(100, 80, 50),
                new Point(150, 30, 50),
                new Vector(-1, -0.5, -1))
                .setRadius(0)
                .setKc(1).setKl(0.0003).setKq(0.000003)
        );

        // Add the wall with window on the right
        scene.geometries.add(createTwoWallsWithWindows());

        // Duplicate of the main dramatic spotlight (intentional or redundant)
        scene.lights.add(new SpotLight(
                new Color(1400, 1100, 1000),
                new Point(200, 20, -45),
                new Vector(0.9995, 0, 0))
                .setRadius(0)
                .setKc(1).setKl(0.0001).setKq(0.000001)
        );

        // Ambient light - soft global illumination
        scene.setAmbientLight(new AmbientLight(new Color(17, 17, 17)));

        // === Create 10 small pyramids in a grid layout ===
        for (int i = 0; i < 10; i++) {
            double x = -140 + (i % 5) * 70;  // 5 pyramids in a row, 70 units apart
            double z = -100 + (i / 5) * 80;  // 2 rows, 80 units apart
            Point center = new Point(x, -60, z);  // aligned to floor height
            scene.geometries.add(createRandomPyramid(center, 30, 40,
                    new Color(30 + i * 10, 100 + (i * 5) % 100, 60 + (i * 7) % 150)));
        }

        // === Create 10 spheres in random positions ===
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            double x = -120 + rand.nextDouble() * 240;  // between -120 and 120
            double z = 120 + rand.nextDouble() * 100;   // farther from pyramids (120-220)
            double y = -30;  // slightly above the floor
            Point center = new Point(x, y, z);
            double radius = 8 + rand.nextDouble() * 4;  // radius between 8 and 12
            int r = 80 + rand.nextInt(100);  // red 80–180
            int g = 50 + rand.nextInt(100);  // green 50–150
            int b = 80 + rand.nextInt(100);  // blue 80–180
            Color color = new Color(r, g, b);
            scene.geometries.add(createSphere(center, radius, color));
        }

        // === Create 5 transparent cubes in a row ===
        for (int i = 0; i < 5; i++) {
            double x = -80 + i * 40;  // 40 units apart
            double y = -45;           // slightly above floor
            double z = 150;           // same depth as spheres
            Point center = new Point(x, y, z);
            Color color = new Color(100 + i * 20, 80 + i * 10, 150 - i * 10);
            scene.geometries.add(createTranslucentCube(center, 25, color));
        }

        scene.geometries.buildBVH();

        // === Configure camera and render image ===
        cameraBuilder
                .setLocation(new Point(10, 20, 440)) // further away, same line of sight
                .setDirection(new Point(-10, 20, -60), new Vector(0, 1, 0)) // looking at mirror
                .setVpDistance(180)
                .setVpSize(160, 160)
                .setResolution(800, 800)
                .build()
                .renderImage()
                .writeToImage("multiply_amount_of_geometries_with_with_window");
    }
}








