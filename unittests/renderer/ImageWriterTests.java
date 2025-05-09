package renderer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import primitives.Color;

import java.io.File;

public class ImageWriterTests {
    @Test
    public void testImageWithGrid() {
        // יצירת ImageWriter עם רזולוציה של 800x500
        ImageWriter imageWriter = new ImageWriter(800, 500);

        // הגדרת צבע הרקע לתמונה (לבן)
        Color backgroundColor = new Color(255, 255, 255);

        // מילוי התמונה בצבע הרקע
        fillBackground(imageWriter, backgroundColor);

        // הגדרת צבע הרשת (שחור)
        Color gridColor = new Color(0, 0, 0);

        // הגדרת פרמטרים לרשת (16 שורות ו-10 עמודות)
        int rows = 16;
        int cols = 10;
        int gridWidth = 800 / cols;
        int gridHeight = 500 / rows;

        // יצירת הרשת
        drawGrid(imageWriter, rows, cols, gridWidth, gridHeight, gridColor);

        // שמירת התמונה לקובץ (יש לבצע בדיקה בתיקיית images אם הקובץ נוצר)
        String imageName = "test_image_with_grid";
        imageWriter.writeToImage(imageName);

        // כעת נוסיף בדיקה אם הקובץ נוצר בתיקיית images
        File file = new File("images/" + imageName + ".png");
        assertTrue(file.exists(), "The image file should be created");
    }

    /**
     * ממלא את התמונה בצבע הרקע שנבחר
     * @param imageWriter - אובייקט ImageWriter
     * @param backgroundColor - צבע הרקע
     */
    private static void fillBackground(ImageWriter imageWriter, Color backgroundColor) {
        for (int i = 0; i < imageWriter.nY(); i++) {
            for (int j = 0; j < imageWriter.nX(); j++) {
                imageWriter.writePixel(j, i, backgroundColor);
            }
        }
    }

    /**
     * מצייר רשת של קווים בתמונה
     * @param imageWriter - אובייקט ImageWriter
     * @param rows - מספר השורות ברשת
     * @param cols - מספר העמודות ברשת
     * @param gridWidth - רוחב של ריבוע ברשת
     * @param gridHeight - גובה של ריבוע ברשת
     * @param gridColor - צבע הקווים
     */
    private static void drawGrid(ImageWriter imageWriter, int rows, int cols, int gridWidth, int gridHeight, Color gridColor) {
        for (int i = 0; i <= rows; i++) {
            for (int j = 0; j <= cols; j++) {
                // קווים אופקיים
                for (int x = j * gridWidth; x < (j + 1) * gridWidth; x++) {
                    imageWriter.writePixel(x, i * gridHeight, gridColor);
                }
                // קווים אנכיים
                for (int y = i * gridHeight; y < (i + 1) * gridHeight; y++) {
                    imageWriter.writePixel(j * gridWidth, y, gridColor);
                }
            }
        }
    }
}