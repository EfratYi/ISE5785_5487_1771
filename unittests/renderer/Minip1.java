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

        private Geometries createLargeMirror() {
            double width = 120;
            double height = 100;
            double centerX = -20;
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
        Geometries createTwoWallsWithWindows() {
        Geometries walls = new Geometries();
        walls.add(createWallWithWindow(180));
        walls.add(createSolidWall(-300));
        walls.add(createBackWall(-730));

        return walls;
        }
    private Geometries createSolidWall(double wallX) {
        double wallYTop = 800;
        double wallYBottom = -80;
        double wallZLeft = -760;
        double wallZRight = 1500;

        Material wallMat = new Material().setKD(0.7).setKS(0.2).setShininess(30);
        Color wallColor = new Color(50, 50, 50);

        Geometries wall = new Geometries();

        // מלבן שלם משני משולשים
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

    private Geometries createWallWithWindow(double wallX) {
            double wallYTop = 800;
            double wallYBottom = -80;
            double wallZLeft = -760;
            double wallZRight = 1500;

            // גודל החלון
            double windowYTop = 40;
            double windowYBottom = 0;
            double windowZLeft = -80;
            double windowZRight = -10;

            Material wallMat = new Material().setKD(0.7).setKS(0.2).setShininess(30);

            Color wallColor = new Color(50, 50, 50);

            Geometries wall = new Geometries();

            // 4 חלקים סביב הפתח
            // למעלה מהחלון
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

            // למטה מהחלון
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

            // מימין לחלון
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

            // משמאל לחלון
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
        private Geometries createGlassInWindow() {
            double wallX = 180;
            double windowYTop = 40;
            double windowYBottom = 0;
            double windowZLeft = -80;
            double windowZRight = -10;

            Material glass = new Material()
                    .setKD(0.3)      // החזרת אור דיפיוזי
                    .setKS(0.4)      // החזרת אור מבריק
                    .setShininess(100)
                    .setkT(0.5)      // שקיפות מתונה - נראית אך עדיין שקופה
                    .setkR(0.05); // השתקפות קלה

            Color glassColor = new Color(20, 20, 30); // זכוכית כהה

            Point p1 = new Point(wallX, windowYTop, windowZLeft);
            Point p2 = new Point(wallX, windowYTop, windowZRight);
            Point p3 = new Point(wallX, windowYBottom, windowZRight);
            Point p4 = new Point(wallX, windowYBottom, windowZLeft);

            return new Geometries(
                    new Triangle(p1, p2, p3).setEmission(glassColor).setMaterial(glass),
                    new Triangle(p1, p3, p4).setEmission(glassColor).setMaterial(glass)
            );
        }
        private SpotLight createSunlightThroughWindow() {
            double wallX = 180;
            double windowYTop = 40;
            double windowYBottom = 0;
            double windowZLeft = -80;
            double windowZRight = -10;

            // מיקום האור ממש קרוב לחלון
            Point lightPosition = new Point(wallX - 1, (windowYTop + windowYBottom) / 2, (windowZLeft + windowZRight) / 2);

            // וקטור אור פנימה לכיוון החדר (X שלילי)
            Vector lightDirection = new Vector(-1, 0.0001, 0).normalize();

            return new SpotLight(
                    new Color(1200, 1000, 800),
                    lightPosition,
                    lightDirection
            ).setRadius(10)
                    .setKc(1).setKl(0.0005).setKq(0.00001);
        }
        private Geometries createPyramidBehindWindow() {
            Point baseCenter = new Point(200, 20, -45); // מאחורי הקיר, באמצע החלון
            double size = 30;
            double height = 40;
            Color color = new Color(50, 200, 50); // ירוק בולט

            return createRandomPyramid(baseCenter, size, height, color);
        }
        @Test
        void createSceneWithMultipleGeometriesSoftShadows() {
            // הגדרת הרקע - צבע חום חם שמזכיר שקיעה
            scene.setBackground(new Color(120, 80, 50));
            // יצירת הרצפה - משטח גדול עם חומר מט שמחקה אדמה/עץ
            scene.geometries.add(createRealisticFloor(-60, 400));
            // יצירת מראה גדולה ברקע - תשקף את כל הסצנה ותוסיף עומק ויזואלי
            scene.geometries.add(createLargeMirror());
            // יצירת הפירמידה הירוקה הגדולה (צד שמאל)
            scene.geometries.add(createEnhancedPyramid());
            scene.geometries.add(createGlassInWindow());
            ////    // זרקור דרמטי ראשי - יוצר תאורה ממוקדת ואווירה
                scene.lights.add(new SpotLight(
                        new Color(1400, 1100, 1000), // חזק יחסית לאפקט דרמטי
                        new Point(200, 20, -45),     // ממוקם מהצד
                        new Vector(0.9995, 0, 0))    // מכוון כמעט ישר פנימה
                        .setRadius(12)               // צללים רכים
                        .setKc(1).setKl(0.0001).setKq(0.000001)
                );
                // זרקור משני - מאיר מזווית אחרת
                scene.lights.add(new SpotLight(
                        new Color(140, 110, 90),     // רך יותר
                        new Point(80, 100, 120),     // מלמעלה ומהצד
                        new Vector(-1, -1.2, -1))    // כיוון אלכסוני למטה
                        .setRadius(12)
                        .setKc(1).setKl(0.0001).setKq(0.000001)
                );
                // אור מילוי - מפחית צללים קשים
                scene.lights.add(new PointLight(
                        new Color(80, 70, 65),       // עדין מאוד
                        new Point(-80, 60, 100))     // מהצד השני
                        .setRadius(8)
                        .setKc(1).setKl(0.0002).setKq(0.000002)
                );
                // אור הדגשה - מדגיש אזור מסוים
                scene.lights.add(new SpotLight(
                        new Color(100, 80, 50),      // חם ועדין
                        new Point(150, 30, 50),      // מהצד הימני
                        new Vector(-1, -0.5, -1))    // מכוון פנימה ומטה
                        .setRadius(6)
                        .setKc(1).setKl(0.0003).setKq(0.000003)
                );
            // יצירת הקיר עם החלון (צד ימין)
    scene.geometries.add(createTwoWallsWithWindows());
            scene.lights.add(new SpotLight(
                    new Color(1400, 1100, 1000),
                    new Point(200, 20, -45),
                    new Vector(0.9995, 0, 0))
                    .setRadius(12)
                    .setKc(1).setKl(0.0001).setKq(0.000001)
            );
            // === מערך האורות המורכב ===
//            scene.lights.add(new SpotLight(
//                    new Color(140, 110, 90),     // רך יותר
//                    new Point(80, 100, 120),     // מלמעלה ומהצד
//                    new Vector(-1, -1.2, -1))    // כיוון אלכסוני למטה
//                    .setRadius(12)
//                    .setKc(1).setKl(0.0001).setKq(0.000001)
//            );
//            scene.lights.add(new SpotLight(
//                    new Color(140, 110, 90),     // רך יותר
//                    new Point(80, 100, 120),     // מלמעלה ומהצד
//                    new Vector(-1, -1.2, -1))    // כיוון אלכסוני למטה
//                    .setRadius(12)
//                    .setKc(1).setKl(0.0001).setKq(0.000001)
//            );
//            scene.lights.add(new SpotLight(
//                    new Color(100, 80, 50),      // חם ועדין
//                    new Point(150, 30, 50),      // מהצד הימני
//                    new Vector(-1, -0.5, -1))    // מכוון פנימה ומטה
//                    .setRadius(6)
//                    .setKc(1).setKl(0.0003).setKq(0.000003)
//            );
            // אור סביבתי רך - מאיר הכל באופן אחיד ברמה נמוכה
            scene.setAmbientLight(new AmbientLight(new Color(17, 17, 17)));

//



//    scene.lights.add(
//            new SpotLight(
//                    new Color(800, 700, 500),
//                    new Point(670, 20, -35),  // לפני הקיר
//                    new Vector(0, -5, -70))      // יורה פנימה בציר X
//                    .setKl(0.0005)
//                    .setKq(0.00005)
//                    .setRadius(10)
//    );



//
//    // זרקור דרמטי ראשי - יוצר תאורה ממוקדת ואווירה
//    scene.lights.add(new SpotLight(
//            new Color(1400, 1100, 1000), // חזק יחסית לאפקט דרמטי
//            new Point(200, 20, -45),     // ממוקם מהצד
//            new Vector(0.9995, 0, 0))    // מכוון כמעט ישר פנימה
//            .setRadius(12)               // צללים רכים
//            .setKc(1).setKl(0.0001).setKq(0.000001)
//    );
//
//    // זרקור משני - מאיר מזווית אחרת
//    scene.lights.add(new SpotLight(
//            new Color(140, 110, 90),     // רך יותר
//            new Point(80, 100, 120),     // מלמעלה ומהצד
//            new Vector(-1, -1.2, -1))    // כיוון אלכסוני למטה
//            .setRadius(12)
//            .setKc(1).setKl(0.0001).setKq(0.000001)
//    );
//
//    // אור מילוי - מפחית צללים קשים
//    scene.lights.add(new PointLight(
//            new Color(80, 70, 65),       // עדין מאוד
//            new Point(-80, 60, 100))     // מהצד השני
//            .setRadius(8)
//            .setKc(1).setKl(0.0002).setKq(0.000002)
//    );
//
//    // אור הדגשה - מדגיש אזור מסוים
//    scene.lights.add(new SpotLight(
//            new Color(100, 80, 50),      // חם ועדין
//            new Point(150, 30, 50),      // מהצד הימני
//            new Vector(-1, -0.5, -1))    // מכוון פנימה ומטה
//            .setRadius(6)
//            .setKc(1).setKl(0.0003).setKq(0.000003)
//    );

            // === יצירת 10 פירמידות קטנות במערך מסודר ===
            for (int i = 0; i < 10; i++) {
                double x = -140 + (i % 5) * 70;  // 5 פירמידות בשורה, מרווח של 70 יחידות
                double z = -100 + (i / 5) * 80;  // 2 שורות, מרווח של 80 יחידות
                Point center = new Point(x, -60, z);  // גובה הרצפה
                // צבע משתנה לכל פירמידה בהדרגה
                scene.geometries.add(createRandomPyramid(center, 30, 40,
                        new Color(30 + i * 10, 100 + (i * 5) % 100, 60 + (i * 7) % 150)));
            }
            // === יצירת 10 כדורים במיקומים אקראיים ===
            Random rand = new Random();
            for (int i = 0; i < 10; i++) {
                double x = -120 + rand.nextDouble() * 240;  // בין -120 ל-120
                double z = 120 + rand.nextDouble() * 100;   // רחוק מהפירמידות (120-220)
                double y = -30;  // קצת מעל הרצפה
                Point center = new Point(x, y, z);
                double radius = 8 + rand.nextDouble() * 4;  // רדיוס בין 8-12
                // צבעים אקראיים בטווח נחמד
                int r = 80 + rand.nextInt(100);  // 80-180
                int g = 50 + rand.nextInt(100);  // 50-150
                int b = 80 + rand.nextInt(100);  // 80-180
                Color color = new Color(r, g, b);
                scene.geometries.add(createSphere(center, radius, color));
            }
            // === יצירת 5 קוביות שקופות בשורה ===
            for (int i = 0; i < 5; i++) {
                double x = -80 + i * 40;  // מרווח של 40 יחידות בין קוביות
                double y = -45;           // קצת מעל הרצפה
                double z = 150;           // באותו עומק של הכדורים
                Point center = new Point(x, y, z);
                // גרדיאנט צבעים לאורך השורה
                Color color = new Color(100 + i * 20, 80 + i * 10, 150 - i * 10);
                scene.geometries.add(createTranslucentCube(center, 25, color));
            }
            // === הגדרת המצלמה ועיבוד התמונה ===
            cameraBuilder
                    .setLocation(new Point(10, 20, 440)) // יותר רחוק, שומר על אותו קו מבט
                    .setDirection(new Point(-10, 20, -60), new Vector(0, 1, 0)) // עדיין מביט על המראה
                    .setVpDistance(180)
                    .setVpSize(160, 160)
                    .setResolution(800, 800)
                    .build()
                    .renderImage()
                    .writeToImage("geometries_with_soft_shadows_with_light");
        }
        }








