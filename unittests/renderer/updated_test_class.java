package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

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

        //מקור
        // Lighting setup
//        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));
//
//        scene.lights.add(new DirectionalLight(
//                new Color(150, 120, 100),
//                new Vector(1, -1, -1)
//        ));
//
//        // Regular lights without soft shadows (size = 0)
//        scene.lights.add(new SpotLight(
//                new Color(100, 80, 60),
//                new Point(60, 80, 80),
//                new Vector(-1, -1.5, -1))
//                .setRadius(0)  // No soft shadows
//                .setKc(1).setKl(0.0001).setKq(0.000001)
//        );
//
//        scene.lights.add(new PointLight(
//                new Color(80, 60, 40),
//                new Point(-50, 50, 150))
//                .setRadius(0)  // No soft shadows
//                .setKc(1).setKl(0.0001).setKq(0.000001)
//        );

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
     * Test with soft shadows
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
        scene.setAmbientLight(new AmbientLight(new Color(25, 25, 25))); // Slightly brighter ambient

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
                new Color(60, 50, 40),    // Dimmer fill light
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


        //מקור
        // Lighting setup
//        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

//        scene.lights.add(new DirectionalLight(
//                new Color(150, 120, 100),
//                new Vector(1, -1, -1)
//        ));
//
//        // Regular lights without soft shadows (size = 0)
//        scene.lights.add(new SpotLight(
//                new Color(100, 80, 60),
//                new Point(60, 80, 80),
//                new Vector(-1, -1.5, -1))
//                .setRadius(10)
//                .setKc(1).setKl(0.0001).setKq(0.000001)
//        );
//
//        scene.lights.add(new PointLight(
//                new Color(80, 60, 40),
//                new Point(-50, 50, 150))
//                .setRadius(10)
//                .setKc(1).setKl(0.0001).setKq(0.000001)
//        );

        // Build camera
        cameraBuilder
                .setLocation(new Point(-90, 50, 180))
                .setDirection(new Point(-1, 0, 1), new Vector(0, 1, 0))
                .setVpDistance(180)
                .setVpSize(160, 160)
                .setResolution(800, 800)
                .build()
                .renderImage()
                .writeToImage("jhj____Shadows");
    }
        }
