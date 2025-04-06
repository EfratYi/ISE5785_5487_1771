package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TubeTests {
    @Test
    void testgetNormal() {
        Tube tube = new Tube(1, new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)));

        // 1️⃣ מחלקת שקילות: נקודה על המעטפת הרגילה
        Point p1 = new Point(1, 0, 5); // נקודה על המעטפת
        Vector normal1 = tube.getNormal(p1);
        assertEquals(new Vector(1, 0, 0), normal1, "Normal on the side is incorrect");

        // 2️⃣ מקרה קצה: נקודה היוצרת זווית ישרה עם ראש הקרן
        Point p2 = new Point(1, 0, 0); // נקודה מול ראש הקרן
        Vector normal2 = tube.getNormal(p2);
        assertEquals(new Vector(1, 0, 0), normal2, "Normal at edge case is incorrect");

        // 3️⃣ בדיקה שהנורמל הוא וקטור יחידה (אורך 1)
        assertEquals(1, normal1.length(), 1e-10, "Normal is not a unit vector");
        assertEquals(1, normal2.length(), 1e-10, "Normal is not a unit vector");
    }
}
