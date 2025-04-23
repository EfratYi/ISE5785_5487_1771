package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class RayTests {

    @Test
    void testGetPoint() {
        Ray ray = new Ray(new Point(1, 2, 3), new Vector(1, 0, 0)); // קרן בכיוון ציר X

        // TC01: t = 0 => מחזיר את ראש הקרן
        assertEquals(
                new Point(1, 2, 3),
                ray.getPoint(0),
                "getPoint(0) אמור להחזיר את ראש הקרן"
        );

        // TC02: t = 1 => זז צעד אחד בכיוון הקרן
        assertEquals(
                new Point(2, 2, 3),
                ray.getPoint(1),
                "getPoint(1) אמור להחזיר את הנקודה (2,2,3)"
        );

        // TC03: t = -2 => זז שני צעדים אחורה בכיוון ההפוך
        assertEquals(
                new Point(-1, 2, 3),
                ray.getPoint(-2),
                "getPoint(-2) אמור להחזיר את הנקודה (-1,2,3)"
        );

        // TC04: t = 0.5 => זז חצי צעד קדימה
        assertEquals(
                new Point(1.5, 2, 3),
                ray.getPoint(0.5),
                "getPoint(0.5) אמור להחזיר את הנקודה (1.5,2,3)"
        );
    }
}