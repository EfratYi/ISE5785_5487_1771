package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ImageWriterTests {

    @Test
    void ImageWriterTest() {
        int width = 800;
        int height = 500;
        int gridSize = 50;

        ImageWriter imageWriter = new ImageWriter(width, height);

        // צבע רקע צהוב
        Color yellow = new Color(255, 255, 0);
        Color red = new Color(255, 0, 0);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x % gridSize == 0 || y % gridSize == 0) {
                    imageWriter.writePixel(x, y, red); // קווים אדומים
                } else {
                    imageWriter.writePixel(x, y, yellow); // רקע צהוב
                }
            }
        }

        // שמירת התמונה
        String fileName = "test_image_with_grid";
        imageWriter.writeToImage(fileName);

        // בדיקה שהתמונה נשמרה (באופן ידני)
        File imageFile = new File("images/" + fileName + ".png");
        assertTrue(imageFile.exists(), "Expected image file was not found.");
    }
}
