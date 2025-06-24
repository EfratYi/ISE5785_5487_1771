package renderer;
import org.junit.jupiter.api.Test;
import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

import java.util.Random;


/**
 * Tests for reflection and transparency functionality, test for partial
 * shadows (with transparency) and soft shadows
 * @author Dan Zilberstein
 */
class updated_test_class {
    private final Scene scene = new Scene("updated_test_class");
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setRayTracer(scene, RayTracerType.SIMPLE);

    /**
     * Create enhanced green pyramid (left side)
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
     * Create small teal pyramid (front center)
     */
    private Geometries createSmallTealPyramid() {
        Point apex = new Point(10, -10, 50);
        Point p1 = new Point(-5, -50, 35);
        Point p2 = new Point(25, -50, 35);
        Point p3 = new Point(25, -50, 65);
        Point p4 = new Point(-5, -50, 65);

        return new Geometries(
                new Triangle(p1, p2, p3)
                        .setEmission(new Color(0, 140, 160))
                        .setMaterial(new Material()
                                .setKD(0.1)
                                .setKS(0.3)
                                .setShininess(100)
                                .setkT(0.85)
                                .setkR(0.1)),
                new Triangle(p1, p3, p4)
                        .setEmission(new Color(0, 140, 160))
                        .setMaterial(new Material()
                                .setKD(0.1)
                                .setKS(0.3)
                                .setShininess(100)
                                .setkT(0.85)
                                .setkR(0.1)),
                new Triangle(p1, p2, apex)
                        .setEmission(new Color(0, 160, 180))
                        .setMaterial(new Material()
                                .setKD(0.1)
                                .setKS(0.3)
                                .setShininess(100)
                                .setkT(0.85)
                                .setkR(0.1)),
                new Triangle(p2, p3, apex)
                        .setEmission(new Color(0, 120, 140))
                        .setMaterial(new Material()
                                .setKD(0.1)
                                .setKS(0.3)
                                .setShininess(100)
                                .setkT(0.85)
                                .setkR(0.1)),
                new Triangle(p3, p4, apex)
                        .setEmission(new Color(0, 130, 150))
                        .setMaterial(new Material()
                                .setKD(0.1)
                                .setKS(0.3)
                                .setShininess(100)
                                .setkT(0.85)
                                .setkR(0.1)),
                new Triangle(p4, p1, apex)
                        .setEmission(new Color(0, 150, 170))
                        .setMaterial(new Material()
                                .setKD(0.1)
                                .setKS(0.3)
                                .setShininess(100)
                                .setkT(0.85)
                                .setkR(0.1))
        );
    }

    /**
     * Create prominent golden cube (right side)
     */
    private Geometries createProminentGoldenCube() {
        double size = 50;
        double centerX = 110;
        double centerY = -15;
        double centerZ = -80;

        // Calculate all 8 vertices
        Point p1 = new Point(centerX - size/2, centerY - size/2, centerZ - size/2);
        Point p2 = new Point(centerX + size/2, centerY - size/2, centerZ - size/2);
        Point p3 = new Point(centerX + size/2, centerY + size/2, centerZ - size/2);
        Point p4 = new Point(centerX - size/2, centerY + size/2, centerZ - size/2);
        Point p5 = new Point(centerX - size/2, centerY - size/2, centerZ + size/2);
        Point p6 = new Point(centerX + size/2, centerY - size/2, centerZ + size/2);
        Point p7 = new Point(centerX + size/2, centerY + size/2, centerZ + size/2);
        Point p8 = new Point(centerX - size/2, centerY + size/2, centerZ + size/2);

        Color cubeColor = new Color(200, 160, 0);
        Material cubeMaterial = new Material().setKD(0.6).setKS(0.4).setShininess(40);

        return new Geometries(
                new Triangle(p1, p2, p3).setEmission(cubeColor).setMaterial(cubeMaterial),
                new Triangle(p1, p3, p4).setEmission(cubeColor).setMaterial(cubeMaterial),
                new Triangle(p2, p6, p7).setEmission(cubeColor).setMaterial(cubeMaterial),
                new Triangle(p2, p7, p3).setEmission(cubeColor).setMaterial(cubeMaterial),
                new Triangle(p4, p3, p7).setEmission(cubeColor).setMaterial(cubeMaterial),
                new Triangle(p4, p7, p8).setEmission(cubeColor).setMaterial(cubeMaterial),
                new Triangle(p1, p4, p8).setEmission(cubeColor).setMaterial(cubeMaterial),
                new Triangle(p1, p8, p5).setEmission(cubeColor).setMaterial(cubeMaterial),
                new Triangle(p1, p5, p6).setEmission(cubeColor).setMaterial(cubeMaterial),
                new Triangle(p1, p6, p2).setEmission(cubeColor).setMaterial(cubeMaterial),
                new Triangle(p5, p8, p7).setEmission(cubeColor).setMaterial(cubeMaterial),
                new Triangle(p5, p7, p6).setEmission(cubeColor).setMaterial(cubeMaterial)
        );
    }

    /**
     * Create realistic floor
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
     * Create mirror (back wall)
     */
    private Geometries createLargeMirror() {
        double width = 120;
        double height = 100;
        double centerX = -10;
        double centerY = 20;
        double centerZ = -60;

        Point p1 = new Point(centerX - width/2, centerY - height/2, centerZ);
        Point p2 = new Point(centerX + width/2, centerY - height/2, centerZ);
        Point p3 = new Point(centerX + width/2, centerY + height/2, centerZ);
        Point p4 = new Point(centerX - width/2, centerY + height/2, centerZ);

        return new Geometries(
                new Triangle(p1, p2, p3)
                        .setEmission(new Color(20, 20, 30))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(200).setkR(0.7)),
                new Triangle(p1, p3, p4)
                        .setEmission(new Color(20, 20, 30))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(200).setkR(0.7))
        );
    }

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

    private Sphere createSphere(Point center, double radius, Color color) {
        return (Sphere) new Sphere(center, radius)
                .setEmission(color)
                .setMaterial(new Material().setKD(0.6).setKS(0.3).setShininess(60));
    }

    private Geometries createTranslucentCube(Point center, double size, Color color) {
        double half = size / 2;
        Material material = new Material()
                .setKD(0.3)      // החזרת אור דיפיוזי
                .setKS(0.4)      // החזרת אור מבריק
                .setShininess(100)
                .setkT(0.5)      // שקיפות מתונה - נראית אך עדיין שקופה
                .setkR(0.05);    // החזרת אור קלה (רפלקציה)

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
     * Test without soft shadows
     */
    @Test
    void createEnhancedGeometricSceneRegular() {
        // Set up scene
        scene.setBackground(new Color(120, 80, 50));
        scene.geometries.add(createRealisticFloor(-60, 400));
        scene.geometries.add(createLargeMirror());
        scene.geometries.add(createEnhancedPyramid());
        scene.geometries.add(createSmallTealPyramid());
        scene.geometries.add(createProminentGoldenCube());

        // Add spheres
        scene.geometries.add(
                new Sphere(new Point(-30, -20, 80), 22)
                        .setEmission(new Color(180, 40, 40))
                        .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(60))
        );

        scene.geometries.add(
                new Sphere(new Point(50, 0, 100), 25)
                        .setEmission(new Color(200, 60, 30))
                        .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(60))
        );

        // Improved lighting setup
        scene.setAmbientLight(new AmbientLight(new Color(25, 25, 25))); // Slightly brighter ambient

        // Replace DirectionalLight with softer, positioned light source
        scene.lights.add(new PointLight(
                new Color(120, 100, 80),  // Softer, warmer general illumination
                new Point(0, 100, 200))   // High and distant for general lighting
                .setRadius(0)            // Soft shadows
                .setKc(1).setKl(0.00005).setKq(0.0000005)
        );

        // Main spotlight for dramatic effect
        scene.lights.add(new SpotLight(
                new Color(140, 110, 90),
                new Point(80, 100, 120),
                new Vector(-1, -1.2, -1))
                .setRadius(0)  // Soft shadows
                .setKc(1).setKl(0.0001).setKq(0.000001)
        );

        // Fill light to reduce harsh shadows
        scene.lights.add(new PointLight(
                new Color(60, 50, 40),    // Dimmer fill light
                new Point(-80, 60, 100))
                .setRadius(0)             // Soft shadows
                .setKc(1).setKl(0.0002).setKq(0.000002)
        );

        // Accent light for the golden cube
        scene.lights.add(new SpotLight(
                new Color(100, 80, 50),
                new Point(150, 30, 50),
                new Vector(-1, -0.5, -1))
                .setRadius(0)   // Smaller radius for more defined but still soft shadows
                .setKc(1).setKl(0.0003).setKq(0.000003)
        );


        // Build camera
        cameraBuilder
                .setLocation(new Point(-90, 50, 180))
                .setDirection(new Point(-1, 0, 1), new Vector(0, 1, 0))
                .setVpDistance(180)
                .setVpSize(160, 160)
                .setResolution(800, 800)
                .build()
                .renderImage()
                .writeToImage("jhj");
    }

    /**
     * Test scene rendering with enhanced geometries and soft shadows.
     * <p>
     * This test creates a visually rich scene that includes:
     * <ul>
     *   <li>A realistic floor and multiple complex geometric objects like a mirror, pyramid, and a golden cube.</li>
     *   <li>Two prominent spheres with differing positions, sizes, and emission colors.</li>
     *   <li>A detailed lighting setup with ambient light, point lights, and spotlights, all configured to produce soft shadows using radius-based diffusion.</li>
     * </ul>
     * The camera is positioned at a calculated angle and distance to capture a balanced perspective of the scene,
     * and the rendered image is saved under the filename {@code Shadows}.
     */
    @Test
    void createEnhancedGeometricSceneSoftShadows() {
        // Set up scene
        scene.setBackground(new Color(120, 80, 50));
        scene.geometries.add(createRealisticFloor(-60, 400));
        scene.geometries.add(createLargeMirror());
        scene.geometries.add(createEnhancedPyramid());
        scene.geometries.add(createSmallTealPyramid());
        scene.geometries.add(createProminentGoldenCube());

        // Add spheres
        scene.geometries.add(
                new Sphere(new Point(-30, -20, 80), 22)
                        .setEmission(new Color(180, 40, 40))
                        .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(60))
        );

        scene.geometries.add(
                new Sphere(new Point(50, 0, 100), 25)
                        .setEmission(new Color(200, 60, 30))
                        .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(60))
        );



        // Improved lighting setup
        scene.setAmbientLight(new AmbientLight(new Color(17, 17, 17))); // Slightly brighter ambient

        // Replace DirectionalLight with softer, positioned light source
        scene.lights.add(new PointLight(
                new Color(120, 100, 80),  // Softer, warmer general illumination
                new Point(0, 100, 200))   // High and distant for general lighting
                .setRadius(15)            // Soft shadows
                .setKc(1).setKl(0.00005).setKq(0.0000005)
        );

        // Main spotlight for dramatic effect
        scene.lights.add(new SpotLight(
                new Color(140, 110, 90),
                new Point(80, 100, 120),
                new Vector(-1, -1.2, -1))
                .setRadius(12)  // Soft shadows
                .setKc(1).setKl(0.0001).setKq(0.000001)
        );

        // Fill light to reduce harsh shadows
        scene.lights.add(new PointLight(
                new Color(80, 70, 65),    // Dimmer fill light
                new Point(-80, 60, 100))
                .setRadius(8)             // Soft shadows
                .setKc(1).setKl(0.0002).setKq(0.000002)
        );

        // Accent light for the golden cube
        scene.lights.add(new SpotLight(
                new Color(100, 80, 50),
                new Point(150, 30, 50),
                new Vector(-1, -0.5, -1))
                .setRadius(6)   // Smaller radius for more defined but still soft shadows
                .setKc(1).setKl(0.0003).setKq(0.000003)
        );

        // Build camera
        cameraBuilder
                .setLocation(new Point(-190, 50, 180))
                .setDirection(new Point(-1, 0, 1), new Vector(0, 1, 0))
                .setVpDistance(180)
                .setVpSize(160, 160)
                .setResolution(800, 800)
                .build()
                .renderImage()
                .writeToImage("Shadows");
    }
    /**
     * Test scene rendering with multiple geometries and soft shadows.
     * <p>
     * This test generates a complex 3D scene including:
     * <ul>
     *   <li>A realistic base floor and mirror, reused for visual consistency.</li>
     *   <li>Ten pyramids placed in a grid-like pattern with varied colors.</li>
     *   <li>Ten randomly positioned spheres with randomized colors and radii.</li>
     *   <li>Five translucent cubes placed in a row to test transparency and soft shadow rendering.</li>
     * </ul>
     * The lighting setup uses multiple soft sources (point and spot lights with radius)
     * to simulate realistic lighting and minimize harsh shadows. The camera is placed further away
     * for a wide-angle view capturing the entire setup. The rendered result is saved as
     * {@code multiply_amount_of_geometries_with_soft_shadows}.
     */
    @Test
    void createSceneWithMultipleGeometriesSoftShadows() {
        // Set up scene
        scene.setBackground(new Color(120, 80, 50));
        scene .geometries.add(createRealisticFloor(-60, 400));
        scene.geometries.add(createLargeMirror());
        scene.geometries.add(createEnhancedPyramid());
        scene.geometries.add(createSmallTealPyramid());

        scene.geometries.add(createRealisticFloor(-60, 400));
        scene.geometries.add(createLargeMirror());

        for (int i = 0; i < 10; i++) {
            double x = -140 + (i % 5) * 70;
            double z = -100 + (i / 5) * 80;
            Point center = new Point(x, -60, z);
            scene.geometries.add(createRandomPyramid(center, 30, 40, new Color(30 + i * 10, 100 + (i * 5) % 100, 60 + (i * 7) % 150)));
        }

        Random rand = new Random();

        for (int i = 0; i < 10; i++) {
            double x = -120 + rand.nextDouble() * 240;  // בין -120 ל-120
            double z = 120 + rand.nextDouble() * 100;   // רחוק מהפירמידות
            double y = -30;

            Point center = new Point(x, y, z);
            double radius = 8 + rand.nextDouble() * 4;

            int r = 80 + rand.nextInt(100);
            int g = 50 + rand.nextInt(100);
            int b = 80 + rand.nextInt(100);
            Color color = new Color(r, g, b);

            scene.geometries.add(createSphere(center, radius, color));
        }
        for (int i = 0; i < 5; i++) {
            double x = -80 + i * 40;  // מרווחים אופקיים בין קוביות
            double y = -45;           // גובה הקוביות
            double z = 150;           // אותו עומק של הכדורים

            Point center = new Point(x, y, z);
            Color color = new Color(100 + i * 20, 80 + i * 10, 150 - i * 10);  // צבע משתנה לכל קוביה

            scene.geometries.add(createTranslucentCube(center, 25, color));  // גודל 25, קוביה שקופה
        }

        // Improved lighting setup
        scene.setAmbientLight(new AmbientLight(new Color(17, 17, 17))); // Slightly brighter ambient

        // Replace DirectionalLight with softer, positioned light source
        scene.lights.add(new PointLight(
                new Color(120, 100, 80),  // Softer, warmer general illumination
                new Point(0, 100, 200))   // High and distant for general lighting
                .setRadius(15)            // Soft shadows
                .setKc(1).setKl(0.00005).setKq(0.0000005)
        );

        // Main spotlight for dramatic effect
        scene.lights.add(new SpotLight(
                new Color(140, 110, 90),
                new Point(80, 100, 120),
                new Vector(-1, -1.2, -1))
                .setRadius(12)  // Soft shadows
                .setKc(1).setKl(0.0001).setKq(0.000001)
        );

        // Fill light to reduce harsh shadows
        scene.lights.add(new PointLight(
                new Color(80, 70, 65),    // Dimmer fill light
                new Point(-80, 60, 100))
                .setRadius(8)             // Soft shadows
                .setKc(1).setKl(0.0002).setKq(0.000002)
        );

        // Accent light for the golden cube
        scene.lights.add(new SpotLight(
                new Color(100, 80, 50),
                new Point(150, 30, 50),
                new Vector(-1, -0.5, -1))
                .setRadius(6)   // Smaller radius for more defined but still soft shadows
                .setKc(1).setKl(0.0003).setKq(0.000003)
        );

        // Build camera
        cameraBuilder
                .setLocation(new Point(50, 20, 440)) // יותר רחוק, שומר על אותו קו מבט
                .setDirection(new Point(-10, 20, -60), new Vector(0, 1, 0)) // עדיין מביט על המראה
                .setVpDistance(180)
                .setVpSize(160, 160)
                .setResolution(800, 800)
                .build()
                .renderImage()
                .writeToImage("multiply_amount_of_geometries_with_soft_shadows");
    }
}
