package renderer;

import org.junit.jupiter.api.Test;
import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

/**
 * Enhanced test for creating a geometric composition scene with realistic lighting
 * Fixed version - removed unwanted shadows and added transparency + POLYGON!
 */
class EnhancedGeometricCompositionWithPolygonTest {

    private final Scene scene = new Scene("Enhanced Geometric Composition Scene with Polygon");
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
                        .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(60).setkT(0.3)),
                new Triangle(p1, p3, p4)
                        .setEmission(new Color(0, 140, 160))
                        .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(60).setkT(0.3)),
                new Triangle(p1, p2, apex)
                        .setEmission(new Color(0, 160, 180))
                        .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(60).setkT(0.3)),
                new Triangle(p2, p3, apex)
                        .setEmission(new Color(0, 120, 140))
                        .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(60).setkT(0.3)),
                new Triangle(p3, p4, apex)
                        .setEmission(new Color(0, 130, 150))
                        .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(60).setkT(0.3)),
                new Triangle(p4, p1, apex)
                        .setEmission(new Color(0, 150, 170))
                        .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(60).setkT(0.3))
        );
    }

    /**
     * Create prominent golden cube (right side)
     */
    private Geometries createProminentGoldenCube() {
        double size = 50;
        double centerX = 110;
        double centerY = -15;
        double centerZ = -100;

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
     * Create realistic floor - shortened behind mirror
     */
    private Geometries createRealisticFloor(double y, double size) {
        return new Geometries(
                // Main floor area (in front of mirror) - וודא שהרצפה נעצרת לפני המראה
                new Triangle(new Point(-size, y, -130),  // עצירה לפני המראה (במקום -120)
                        new Point(size, y, -130),
                        new Point(size, y, size))
                        .setEmission(new Color(80, 60, 40))
                        .setMaterial(new Material()
                                .setKD(0.8)
                                .setKS(0.2)
                                .setShininess(10)),
                new Triangle(new Point(-size, y, -130),
                        new Point(size, y, size),
                        new Point(-size, y, size))
                        .setEmission(new Color(80, 60, 40))
                        .setMaterial(new Material()
                                .setKD(0.8)
                                .setKS(0.2)
                                .setShininess(10))
        );
    }

    /**
     * Create rectangular polygon mirror on back wall - בדיוק כמו המראה המקורית אבל מפוליגון!
     */
    private Geometries createRectangularPolygonMirror() {
        double width = 100;
        double height =10;
        double centerX = -80;
        double centerY = 90;
        double centerZ = -135;  // Much further back - closer to wall

        // 4 נקודות של המלבן - בדיוק כמו המראה המקורית
        // חשוב: נקודות בסדר נכון עבור פוליגון (נגד כיוון השעון)
        Point p1 = new Point(centerX - width/2, centerY - height/2, centerZ);  // שמאל למטה
        Point p2 = new Point(centerX + width/2, centerY - height/2, centerZ);  // ימין למטה
        Point p3 = new Point(centerX + width/2, centerY + height/2, centerZ);  // ימין למעלה
        Point p4 = new Point(centerX - width/2, centerY + height/2, centerZ);  // שמאל למעלה

        // אותו חומר רפלקטיבי כמו המראה המקורית
        return new Geometries(
                new Polygon(p1, p2, p3, p4)  // פוליגון ריבועי במקום 2 משולשים!
                        .setEmission(new Color(20, 20, 30))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(200).setkR(0.7))
        );
    }

    /**
     * Create decorative octagonal polygon on the floor - פוליגון נוסף לקישוט
     * מתומן מקושט על הרצפה
     */
    private Geometries createFloorOctagonDecoration() {
        double radius = 35;
        double centerX = 30;
        double centerY = -59.5;  // ממש על הרצפה
        double centerZ = 20;

        // יצירת 8 נקודות של המתומן
        Point[] octPoints = new Point[8];
        for (int i = 0; i < 8; i++) {
            double angle = Math.PI * i / 4.0;  // 45 מעלות בין כל נקודה
            double x = centerX + radius * Math.cos(angle);
            double z = centerZ + radius * Math.sin(angle);
            octPoints[i] = new Point(x, centerY, z);
        }

        // חומר מבריק עם צבע סגול יפה
        Material octMaterial = new Material()
                .setKD(0.6)
                .setKS(0.4)
                .setShininess(80)
                .setkT(0.2);  // שקיפות קלה

        Color octColor = new Color(120, 60, 140);  // סגול יפה

        return new Geometries(
                new Polygon(octPoints)
                        .setEmission(octColor)
                        .setMaterial(octMaterial)
        );
    }

    /**
     * Main enhanced scene with realistic lighting and POLYGON! - FIXED VERSION
     */
    @Test
    void createEnhancedGeometricSceneWithPolygon() {
        // Warm background
        scene.setBackground(new Color(120, 80, 50));

        // Clean floor without transparency issues
        scene.geometries.add(createRealisticFloor(-60, 400));

        // Add RECTANGULAR POLYGON MIRROR - same as original but using Polygon instead of triangles! 🔥
        scene.geometries.add(createRectangularPolygonMirror());

        // Add decorative octagonal polygon on floor for extra polygon fun! 🎨
        // scene.geometries.add(createFloorOctagonDecoration());  // זמנית מושבת לבדוק אם זה מפריע

        // Add main green pyramid (left side)
        scene.geometries.add(createEnhancedPyramid());

        // Add small teal pyramid (front center)
        scene.geometries.add(createSmallTealPyramid());

        // Add prominent golden cube (right side)
        scene.geometries.add(createProminentGoldenCube());

        // TRANSPARENT red sphere that was hiding pyramid - now see-through!
        scene.geometries.add(
                new Sphere(new Point(-30, -20, 80), 22)
                        .setEmission(new Color(120, 25, 25))  // Much dimmer emission
                        .setMaterial(new Material()
                                .setKD(0.2)          // Even less diffuse reflection
                                .setKS(0.3)          // Reduced specular to be less bright
                                .setShininess(60)    // Lower shininess for softer look
                                .setkT(0.85))        // Higher transparency to cast less shadow
        );

        // Second sphere - solid orange (no transparency issues)
        scene.geometries.add(
                new Sphere(new Point(50, 0, 100), 25)
                        .setEmission(new Color(200, 60, 30))
                        .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(60))
        );

        // IMPROVED LIGHTING - much softer to minimize sphere shadows

        // Very gentle ambient light to fill shadows
        scene.setAmbientLight(new AmbientLight(new Color(40, 40, 40)));

        // Much softer main directional light
        scene.lights.add(new DirectionalLight(
                new Color(100, 80, 60),   // Even softer
                new Vector(1, -1, -1)
        ));

        // Very gentle spot light
        scene.lights.add(new SpotLight(
                new Color(60, 45, 30),    // Much softer spot light
                new Point(60, 80, 80),
                new Vector(-1, -1.5, -1))
                .setKc(1).setKl(0.0002).setKq(0.000002)  // Stronger attenuation
        );

        // Multiple soft fill lights to eliminate sphere shadows
        scene.lights.add(new PointLight(
                new Color(50, 40, 30),    // Soft fill light from above
                new Point(0, 120, 50))
                .setKc(1).setKl(0.0008).setKq(0.00001)
        );

        scene.lights.add(new PointLight(
                new Color(30, 25, 20),    // Additional soft fill from side
                new Point(-100, 50, 120))
                .setKc(1).setKl(0.001).setKq(0.00002)
        );

        // Additional light to highlight the polygon mirror
        scene.lights.add(new PointLight(
                new Color(40, 40, 40),    // Neutral light for the rectangular mirror
                new Point(-10, 100, -100))
                .setKc(1).setKl(0.0005).setKq(0.00001)
        );

        // Camera positioned for best view of all polygons
        cameraBuilder
                .setLocation(new Point(-50, 10, 180))
                .setDirection(new Point(-1, 0, 1), new Vector(0, 1, 0))
                .setVpDistance(80)
                .setVpSize(160, 160)
                .setResolution(1200, 1200)
                .build()
                .renderImage()
                .writeToImage("mirror");
    }

    /**
     * Ultra high quality version with polygons
     */
//    @Test
//    void createUltraHighQualitySceneWithPolygons() {
//        // Use the fixed scene setup with polygons
//        createEnhancedGeometricSceneWithPolygon();
//
//        // Override with ultra-high quality settings
//        cameraBuilder
//                .setLocation(new Point(-90, 50, 180))
//                .setDirection(new Point(-1, 0, 1), new Vector(0, 1, 0))
//                .setVpDistance(200)
//                .setVpSize(160, 160)
//                .setResolution(2000, 2000)
//                .build()
//                .renderImage()
//                .writeToImage("enhanced_geometric_composition_polygon_mirror_ultra_HQ");
//    }
//
//    /**
//     * Alternative camera angle to better show the floor octagon
//     */
//    @Test
//    void createPolygonFocusedView() {
//        // Same scene setup
//        createEnhancedGeometricSceneWithPolygon();
//
//        // Camera positioned to show both polygons clearly
//        cameraBuilder
//                .setLocation(new Point(-60, 80, 150))
//                .setDirection(new Point(0, -10, 0), new Vector(0, 1, 0))
//                .setVpDistance(150)
//                .setVpSize(140, 140)
//                .setResolution(1200, 1200)
//                .build()
//                .renderImage()
//                .writeToImage("enhanced_geometric_composition_polygon_focused");
//    }
}