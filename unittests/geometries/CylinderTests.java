package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;
class CylinderTests {

    @Test
    void testgetNormal() {
        // יוצרים גליל עם בסיס תחתון במרכז (0,0,0), רדיוס 1, כיוון ציר (0,0,1), וגובה 5
        Cylinder cylinder = new Cylinder(1, new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 5);

        // 1️⃣ מחלקת שקילות 1: נקודה על המעטפת
        Point p1 = new Point(1, 0, 2); // נקודה במעטפת החיצונית
        Vector normal1 = cylinder.getNormal(p1);
        assertEquals(new Vector(1, 0, 0), normal1, "Normal on the side is incorrect");

        // 2️⃣ מחלקת שקילות 2: נקודה על הבסיס התחתון
        Point p2 = new Point(0.5, 0.5, 0); // נקודה בתוך הבסיס התחתון
        Vector normal2 = cylinder.getNormal(p2);
        assertEquals(new Vector(0, 0, -1), normal2, "Normal on the bottom base is incorrect");

        // 3️⃣ מחלקת שקילות 3: נקודה על הבסיס העליון
        Point p3 = new Point(0.5, -0.5, 5); // נקודה בתוך הבסיס העליון
        Vector normal3 = cylinder.getNormal(p3);
        assertEquals(new Vector(0, 0, 1), normal3, "Normal on the top base is incorrect");

        // 4️⃣ מקרה גבול 1: מרכז הבסיס התחתון
        Point p4 = new Point(0, 0, 0);
        Vector normal4 = cylinder.getNormal(p4);
        assertEquals(new Vector(0, 0, -1), normal4, "Normal at bottom center is incorrect");

        // 5️⃣ מקרה גבול 2: מרכז הבסיס העליון
        Point p5 = new Point(0, 0, 5);
        Vector normal5 = cylinder.getNormal(p5);
        assertEquals(new Vector(0, 0, 1), normal5, "Normal at top center is incorrect");

        // 6️⃣ מקרה גבול 3: חיבור המעטפת עם הבסיס התחתון
        Point p6 = new Point(1, 0, 0);
        Vector normal6 = cylinder.getNormal(p6);
        assertEquals(new Vector(1, 0, 0), normal6, "Normal at bottom edge is incorrect");

        // 7️⃣ מקרה גבול 4: חיבור המעטפת עם הבסיס העליון
        Point p7 = new Point(1, 0, 5);
        Vector normal7 = cylinder.getNormal(p7);
        assertEquals(new Vector(1, 0, 0), normal7, "Normal at top edge is incorrect");
    }
}