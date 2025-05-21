package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ImageWriter} class.
 */
class ImageWriterTests {

    /**
     * Test method for {@link ImageWriter#writeToImage(String)}.
     * <p>
     * This test creates an image with a yellow background and red grid lines,
     * saves it to a file, and verifies that the file was created successfully.
     * </p>
     */
    @Test
    void ImageWriterTest() {
        int width = 800; // Width of the image
        int height = 500; // Height of the image
        int gridSize = 50; // Size of the grid cells

        // Create an ImageWriter instance
        ImageWriter imageWriter = new ImageWriter(width, height);

        // Define colors for the background and grid
        Color yellow = new Color(255, 255, 0); // Yellow background
        Color red = new Color(255, 0, 225); // Red grid lines

        // Draw the grid and background
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x % gridSize == 0 || y % gridSize == 0) {
                    imageWriter.writePixel(x, y, red); // Red grid lines
                } else {
                    imageWriter.writePixel(x, y, yellow); // Yellow background
                }
            }
        }

        // Save the image to a file
        String fileName = "test_image_with_grid";
        imageWriter.writeToImage(fileName);

        // Verify that the image file was created successfully
        File imageFile = new File("images/" + fileName + ".png");
        assertTrue(imageFile.exists(), "Expected image file was not found.");
    }
}