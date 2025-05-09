package renderer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import primitives.Color;

import java.io.File;

/**
 * Unit tests for the {@link ImageWriter} class.
 * Verifies the functionality of creating and manipulating images.
 */
public class ImageWriterTests {

    /**
     * Test method for creating an image with a grid.
     * Verifies that the image is created with the correct grid and saved to a file.
     */
    @Test
    public void testImageWithGrid() {
        // Create an ImageWriter with a resolution of 800x500
        ImageWriter imageWriter = new ImageWriter(800, 500);

        // Set the background color to white
        Color backgroundColor = new Color(255, 255, 255);

        // Fill the image with the background color
        fillBackground(imageWriter, backgroundColor);

        // Set the grid color to black
        Color gridColor = new Color(0, 0, 0);

        // Define grid parameters (16 rows and 10 columns)
        int rows = 16;
        int cols = 10;
        int gridWidth = 800 / cols;
        int gridHeight = 500 / rows;

        // Draw the grid
        drawGrid(imageWriter, rows, cols, gridWidth, gridHeight, gridColor);

        // Save the image to a file
        String imageName = "test_image_with_grid";
        imageWriter.writeToImage(imageName);

        // Verify that the image file is created in the "images" directory
        File file = new File("images/" + imageName + ".png");
        assertTrue(file.exists(), "The image file should be created");
    }

    /**
     * Fills the image with the specified background color.
     *
     * @param imageWriter    the {@link ImageWriter} object
     * @param backgroundColor the background color
     */
    private static void fillBackground(ImageWriter imageWriter, Color backgroundColor) {
        for (int i = 0; i < imageWriter.nY(); i++) {
            for (int j = 0; j < imageWriter.nX(); j++) {
                imageWriter.writePixel(j, i, backgroundColor);
            }
        }
    }

    /**
     * Draws a grid of lines on the image.
     *
     * @param imageWriter the {@link ImageWriter} object
     * @param rows        the number of rows in the grid
     * @param cols        the number of columns in the grid
     * @param gridWidth   the width of each grid cell
     * @param gridHeight  the height of each grid cell
     * @param gridColor   the color of the grid lines
     */
    private static void drawGrid(ImageWriter imageWriter, int rows, int cols, int gridWidth, int gridHeight, Color gridColor) {
        int width = imageWriter.nX();
        int height = imageWriter.nY();

        // Draw horizontal lines
        for (int i = 0; i <= rows; i++) {
            int y = i * gridHeight;
            if (y >= height) y = height - 1; // Ensure it does not exceed bounds
            for (int x = 0; x < width; x++) {
                imageWriter.writePixel(x, y, gridColor);
            }
        }

        // Draw vertical lines
        for (int j = 0; j <= cols; j++) {
            int x = j * gridWidth;
            if (x >= width) x = width - 1; // Ensure it does not exceed bounds
            for (int y = 0; y < height; y++) {
                imageWriter.writePixel(x, y, gridColor);
            }
        }
    }
}