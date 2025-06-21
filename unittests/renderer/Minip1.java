package renderer;
import geometries.*;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Point;
import primitives.Vector;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.SimpleRayTracer;
import scene.Scene;

import static java.awt.Color.*;

class Minip1 {


    private final Scene scene = new Scene("mini Scene");
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
                                .setKD(0.1)          // Very low diffuse
                                .setKS(0.3)          // Some specular
                                .setShininess(100)   // High shininess for glass effect
                                .setkT(0.85)         // 85% transparent
                                .setkR(0.1))  ,
                new Triangle(p1, p3, p4)
                        .setEmission(new Color(0, 140, 160))
                        .setMaterial(new Material()
                                .setKD(0.1)          // Very low diffuse
                                .setKS(0.3)          // Some specular
                                .setShininess(100)   // High shininess for glass effect
                                .setkT(0.85)         // 85% transparent
                                .setkR(0.1))  ,
                new Triangle(p1, p2, apex)
                        .setEmission(new Color(0, 160, 180))
                        .setMaterial(new Material()
                                .setKD(0.1)          // Very low diffuse
                                .setKS(0.3)          // Some specular
                                .setShininess(100)   // High shininess for glass effect
                                .setkT(0.85)         // 85% transparent
                                .setkR(0.1))  ,
                new Triangle(p2, p3, apex)
                        .setEmission(new Color(0, 120, 140))
                        .setMaterial(new Material()
                                .setKD(0.1)          // Very low diffuse
                                .setKS(0.3)          // Some specular
                                .setShininess(100)   // High shininess for glass effect
                                .setkT(0.85)         // 85% transparent
                                .setkR(0.1))  ,
                new Triangle(p3, p4, apex)
                        .setEmission(new Color(0, 130, 150))
                        .setMaterial(new Material()
                                .setKD(0.1)          // Very low diffuse
                                .setKS(0.3)          // Some specular
                                .setShininess(100)   // High shininess for glass effect
                                .setkT(0.85)         // 85% transparent
                                .setkR(0.1))  ,
                new Triangle(p4, p1, apex)
                        .setEmission(new Color(0, 150, 170))
                        .setMaterial(new Material()
                                .setKD(0.1)          // Very low diffuse
                                .setKS(0.3)          // Some specular
                                .setShininess(100)   // High shininess for glass effect
                                .setkT(0.85)         // 85% transparent
                                .setkR(0.1))
        );
    }

    /**
     * Create prominent golden cube (right side) - simplified like yellow cube
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

        // Uniform golden color for all faces (like the simple yellow cube)
        Color cubeColor = new Color(200, 160, 0);
        Material cubeMaterial = new Material().setKD(0.6).setKS(0.4).setShininess(40);

        return new Geometries(
                // All faces with same color and material
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
     * Create realistic floor - like the simple scene but with slight transparency
     */
    private Geometries createRealisticFloor(double y, double size) {
        return new Geometries(
                new Triangle(new Point(-size, y, -size * 2),
                        new Point(size, y, -size * 2),
                        new Point(size, y, size))
                        .setEmission(new Color(80, 60, 40))  // Same as simple scene
                        .setMaterial(new Material()
                                .setKD(0.8)          // Same as simple scene
                                .setKS(0.2)          // Same as simple scene
                                .setShininess(10)    // Same as simple scene
                                .setkT(0.1)),        // Just a tiny bit of transparency
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
     * Create mirror (back wall) - like simple scene
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
                        .setEmission(new Color(20, 20, 30))  // Same as simple scene
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(200).setkR(0.7)),
                new Triangle(p1, p3, p4)
                        .setEmission(new Color(20, 20, 30))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(200).setkR(0.7))
        );
    }

    /**
     * Main enhanced scene with realistic lighting (based on simple scene)
     */
    @Test
    void createEnhancedGeometricScene() {
        // Warm background like simple scene
        scene.setBackground(new Color(120, 80, 50));

        // Realistic floor
        scene.geometries.add(createRealisticFloor(-60, 400));

        // Add mirror for reflections
        scene.geometries.add(createLargeMirror());

        // Add main green pyramid (left side)
        scene.geometries.add(createEnhancedPyramid());

        // Add small teal pyramid (front center)
        scene.geometries.add(createSmallTealPyramid());


        // Add prominent golden cube (right side)
        scene.geometries.add(createProminentGoldenCube());

        // Add red-orange spheres
        scene.geometries.add(
                new Sphere(new Point(-30, -20, 80), 22)
                        .setEmission(new Color(180, 40, 40))  // Similar to simple scene red
                        .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(60))
        );

        scene.geometries.add(
                new Sphere(new Point(50, 0, 100), 25)
                        .setEmission(new Color(200, 60, 30))
                        .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(60))
        );

        // REALISTIC LIGHTING - exactly like simple scene approach

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

        // Colored accent lights for atmosphere
        // Cool blue light from behind/side - creates nice contrast
        scene.lights.add(new SpotLight(
                new Color(40, 80, 120),
                new Point(-150, 80, 0),
                new Vector(1, -0.8, 0.2))
                .setRadius(10)
                .setKc(1).setKl(0.0002).setKq(0.000002)
        );

        // Warm orange/red light from the opposite side
        scene.lights.add(new PointLight(
                new Color(120, 60, 20),
                new Point(100, -20, 200))
                .setRadius(8)
                .setKc(1).setKl(0.0004).setKq(0.000004)
        );

        // Purple rim light for the spheres - creates beautiful edge lighting
        scene.lights.add(new SpotLight(
                new Color(80, 40, 100),
                new Point(0, 120, 200),
                new Vector(0, -1, -0.5))
                .setRadius(7)
                .setKc(1).setKl(0.0005).setKq(0.000005)
        );

        // Green light to complement the pyramid
        scene.lights.add(new PointLight(
                new Color(60, 100, 40),
                new Point(-120, 30, 100))
                .setRadius(9)
                .setKc(1).setKl(0.0003).setKq(0.000003)
        );

        // Soft white light from below for subtle floor illumination
        scene.lights.add(new PointLight(
                new Color(80, 80, 90),
                new Point(0, -40, 50))
                .setRadius(20)  // Very soft shadows
                .setKc(1).setKl(0.0001).setKq(0.000001)
        );

        // Dramatic spotlight from far right - creates strong directional lighting
        scene.lights.add(new SpotLight(
                new Color(100, 90, 70),
                new Point(200, 60, 100),
                new Vector(-1.5, -0.3, -0.8))
                .setRadius(5)   // Sharper but still soft
                .setKc(1).setKl(0.0006).setKq(0.000006)
        );




        //מקור
//        // Soft ambient light (like simple scene)
//        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));
//
//        // Main directional light - soft and natural (like simple scene)
//        scene.lights.add(new DirectionalLight(
//                new Color(150, 120, 100),  // Same intensity as simple scene
//                new Vector(1, -1, -1)      // Same direction as simple scene
//        ));
////
////        // Gentle spot light (like simple scene)
//        scene.lights.add(new SpotLight(
//                new Color(100, 80, 60),    // Same as simple scene
//                new Point(60, 80, 80),
//                new Vector(-1, -1.5, -1)).setRadius(30)
//                .setKc(1).setKl(0.0001).setKq(0.000001)  // Same attenuation
//        );


    }

    /**
     * Ultra high quality version with realistic lighting
     */
    @Test
    void createUltraHighQualityScene() {
        // Use the enhanced scene setup
        createEnhancedGeometricScene();

        // Override with ultra-high quality settings
        cameraBuilder
                .setLocation(new Point(-90, 50, 180))
                .setDirection(new Point(-1, 0, 1), new Vector(0, 1, 0))
                .setVpDistance(180)
                .setVpSize(160, 160)
                .setResolution(2000, 2000)
                .build()
                .renderImage()
                .writeToImage("mini");
    }
}


