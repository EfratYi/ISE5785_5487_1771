package scene;

import lighting.AmbientLight;
import geometries.Geometries;
import primitives.Color;

public class Scene {
    // שדות ציבוריים (PDS)
    public String name;
    public Color background = Color.BLACK;
    public AmbientLight ambientLight = AmbientLight.NONE;
    public Geometries geometries = new Geometries();

    // בנאי שמקבל רק את שם הסצנה
    public Scene(String name) {
        this.name = name;
    }

    // מתודה לעדכון צבע הרקע
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    // מתודה לעדכון תאורת הרקע
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    // מתודה לעדכון המודלים הגיאומטריים
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }
}
