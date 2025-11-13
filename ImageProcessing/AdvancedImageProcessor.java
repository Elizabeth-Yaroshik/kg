package ImageProcessing;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AdvancedImageProcessor {

    public static BufferedImage detectPoints(BufferedImage image, int threshold) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                result.setRGB(x, y, image.getRGB(x, y));
            }
        }

        int[][] laplacian = {{0, 1, 0}, {1, -4, 1}, {0, 1, 0}};

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int response = 0;

                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int pixel = image.getRGB(x + kx, y + ky);
                        int gray = (int)(0.299 * ((pixel >> 16) & 0xFF) +
                                0.587 * ((pixel >> 8) & 0xFF) +
                                0.114 * (pixel & 0xFF));

                        response += gray * laplacian[ky + 1][kx + 1];
                    }
                }

                if (Math.abs(response) > threshold) {
                    result.setRGB(x, y, 0xFF0000);
                }
            }
        }

        return result;
    }

}