package lighting;

import primitives.Color;

public class AmbientLight {
    // שדה פרטי סופי - לא ניתן לשינוי אחרי אתחול
    private final Color intensity;

    // קבוע סטטי ציבורי בשם NONE, מאותחל לצבע שחור
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

    // בנאי שמקבל את עוצמת האור ושומר אותה בשדה
    public AmbientLight(Color IA) {
        this.intensity = IA;
    }

    // מתודה שמחזירה את עוצמת התאורה
    public Color getIntensity() {
        return intensity;
    }
}
