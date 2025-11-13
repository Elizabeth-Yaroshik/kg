package ImageProcessing;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;

public class ImageProcessingApp extends JFrame {
    private BufferedImage originalImage;
    private BufferedImage processedImage;
    private JLabel originalLabel;
    private JLabel processedLabel;

    public ImageProcessingApp() {
        setTitle("Обработка изображений - Лабораторная работа 2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton loadButton = new JButton("Загрузить изображение");
        JButton pointsButton = new JButton("Обнаружение точек");
        JButton linesButton = new JButton("Обнаружение линий");
        JButton gradientsButton = new JButton("Обнаружение перепадов яркости");
        JButton localThreshold1Button = new JButton("Локальный порог 1 (Среднее)");
        JButton localThreshold2Button = new JButton("Локальный порог 2 (Медиана)");

        buttonPanel.add(loadButton);
        buttonPanel.add(pointsButton);
        buttonPanel.add(linesButton);
        buttonPanel.add(gradientsButton);
        buttonPanel.add(localThreshold1Button);
        buttonPanel.add(localThreshold2Button);

        JPanel imagePanel = new JPanel(new GridLayout(1, 2));

        originalLabel = new JLabel("Оригинальное изображение", SwingConstants.CENTER);
        processedLabel = new JLabel("Обработанное изображение", SwingConstants.CENTER);

        originalLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        originalLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        processedLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        processedLabel.setHorizontalTextPosition(SwingConstants.CENTER);

        imagePanel.add(originalLabel);
        imagePanel.add(processedLabel);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(imagePanel, BorderLayout.CENTER);

        add(mainPanel);

        loadButton.addActionListener(new LoadImageListener());
        pointsButton.addActionListener(new PointsDetectionListener());
        linesButton.addActionListener(new LinesDetectionListener());
        gradientsButton.addActionListener(new GradientsDetectionListener());
        localThreshold1Button.addActionListener(new LocalThreshold1Listener());
        localThreshold2Button.addActionListener(new LocalThreshold2Listener());
    }

    public void loadImage(File imageFile) {
        try {
            originalImage = javax.imageio.ImageIO.read(imageFile);
            processedImage = null;
            updateImageDisplay();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Ошибка загрузки изображения: " + ex.getMessage());
        }
    }

    private class LoadImageListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(ImageProcessingApp.this);

            if (result == JFileChooser.APPROVE_OPTION) {
                loadImage(fileChooser.getSelectedFile());
            }
        }
    }

    private class PointsDetectionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (originalImage != null) {

                processedImage = AdvancedImageProcessor.detectPoints(originalImage, 50);
                updateImageDisplay();
            } else {
                JOptionPane.showMessageDialog(ImageProcessingApp.this,
                        "Сначала загрузите изображение");
            }
        }
    }

    private class LinesDetectionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (originalImage != null) {
                processedImage = detectLines(originalImage);
                updateImageDisplay();
            } else {
                JOptionPane.showMessageDialog(ImageProcessingApp.this,
                        "Сначала загрузите изображение");
            }
        }
    }

    private class GradientsDetectionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (originalImage != null) {
                processedImage = detectGradients(originalImage);
                updateImageDisplay();
            } else {
                JOptionPane.showMessageDialog(ImageProcessingApp.this,
                        "Сначала загрузите изображение");
            }
        }
    }

    private class LocalThreshold1Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (originalImage != null) {
                processedImage = localMeanThreshold(originalImage, 15, 10);
                updateImageDisplay();
            } else {
                JOptionPane.showMessageDialog(ImageProcessingApp.this,
                        "Сначала загрузите изображение");
            }
        }
    }

    private class LocalThreshold2Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (originalImage != null) {
                processedImage = localMedianThreshold(originalImage, 15, 10);
                updateImageDisplay();
            } else {
                JOptionPane.showMessageDialog(ImageProcessingApp.this,
                        "Сначала загрузите изображение");
            }
        }
    }

    private void updateImageDisplay() {
        if (originalImage != null) {

            Dimension originalSize = calculateScaledSize(originalImage, 500, 500);
            ImageIcon originalIcon = new ImageIcon(scaleImage(originalImage, originalSize.width, originalSize.height));
            originalLabel.setIcon(originalIcon);
            originalLabel.setText("Оригинальное изображение (" + originalImage.getWidth() + "x" + originalImage.getHeight() + ")");
        } else {
            originalLabel.setIcon(null);
            originalLabel.setText("Оригинальное изображение");
        }

        if (processedImage != null) {

            Dimension processedSize = calculateScaledSize(processedImage, 500, 500);
            ImageIcon processedIcon = new ImageIcon(scaleImage(processedImage, processedSize.width, processedSize.height));
            processedLabel.setIcon(processedIcon);
            processedLabel.setText("Обработанное изображение (" + processedImage.getWidth() + "x" + processedImage.getHeight() + ")");
        } else {
            processedLabel.setIcon(null);
            processedLabel.setText("Обработанное изображение");
        }
    }

    private Dimension calculateScaledSize(BufferedImage image, int maxWidth, int maxHeight) {
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        double widthRatio = (double) maxWidth / originalWidth;
        double heightRatio = (double) maxHeight / originalHeight;

        double ratio = Math.min(widthRatio, heightRatio);

        int scaledWidth = (int) (originalWidth * ratio);
        int scaledHeight = (int) (originalHeight * ratio);

        return new Dimension(scaledWidth, scaledHeight);
    }

    private Image scaleImage(BufferedImage image, int width, int height) {
        return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    private BufferedImage detectLines(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int[][] horizontalKernel = {{-1, -1, -1}, {2, 2, 2}, {-1, -1, -1}};
        int[][] verticalKernel = {{-1, 2, -1}, {-1, 2, -1}, {-1, 2, -1}};
        int[][] diagonal1Kernel = {{-1, -1, 2}, {-1, 2, -1}, {2, -1, -1}};
        int[][] diagonal2Kernel = {{2, -1, -1}, {-1, 2, -1}, {-1, -1, 2}};

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int maxResponse = 0;

                int responseH = applyKernel(image, x, y, horizontalKernel);
                int responseV = applyKernel(image, x, y, verticalKernel);
                int responseD1 = applyKernel(image, x, y, diagonal1Kernel);
                int responseD2 = applyKernel(image, x, y, diagonal2Kernel);

                maxResponse = Math.max(Math.max(responseH, responseV), Math.max(responseD1, responseD2));

                if (maxResponse > 100) {
                    result.setRGB(x, y, 0x0000FF);
                } else {
                    int pixel = image.getRGB(x, y);
                    int gray = (int)(0.299 * ((pixel >> 16) & 0xFF) +
                            0.587 * ((pixel >> 8) & 0xFF) +
                            0.114 * (pixel & 0xFF));
                    int grayPixel = (gray << 16) | (gray << 8) | gray;
                    result.setRGB(x, y, grayPixel);
                }
            }
        }

        return result;
    }

    private BufferedImage detectGradients(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int[][] sobelX = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] sobelY = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};

        int maxGradient = 0;
        int[][] gradients = new int[width][height];

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int gx = 0;
                int gy = 0;

                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int pixel = image.getRGB(x + kx, y + ky);
                        int gray = (int)(0.299 * ((pixel >> 16) & 0xFF) +
                                0.587 * ((pixel >> 8) & 0xFF) +
                                0.114 * (pixel & 0xFF));

                        gx += gray * sobelX[ky + 1][kx + 1];
                        gy += gray * sobelY[ky + 1][kx + 1];
                    }
                }

                int magnitude = (int)Math.sqrt(gx * gx + gy * gy);
                gradients[x][y] = magnitude;
                maxGradient = Math.max(maxGradient, magnitude);
            }
        }

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int magnitude = gradients[x][y];
                if (maxGradient > 0) {
                    int intensity = (int)(255.0 * magnitude / maxGradient);
                    int gradientPixel = (intensity << 16) | (intensity << 8) | intensity;
                    result.setRGB(x, y, gradientPixel);
                } else {
                    result.setRGB(x, y, 0);
                }
            }
        }

        return result;
    }

    private int applyKernel(BufferedImage image, int x, int y, int[][] kernel) {
        int response = 0;
        for (int ky = -1; ky <= 1; ky++) {
            for (int kx = -1; kx <= 1; kx++) {
                int pixel = image.getRGB(x + kx, y + ky);
                int gray = (int)(0.299 * ((pixel >> 16) & 0xFF) +
                        0.587 * ((pixel >> 8) & 0xFF) +
                        0.114 * (pixel & 0xFF));
                response += gray * kernel[ky + 1][kx + 1];
            }
        }
        return Math.abs(response);
    }

    private BufferedImage localMeanThreshold(BufferedImage image, int windowSize, int constant) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);

        int halfWindow = windowSize / 2;

        for (int y = halfWindow; y < height - halfWindow; y++) {
            for (int x = halfWindow; x < width - halfWindow; x++) {
                int sum = 0;
                int count = 0;

                for (int ky = -halfWindow; ky <= halfWindow; ky++) {
                    for (int kx = -halfWindow; kx <= halfWindow; kx++) {
                        int pixel = image.getRGB(x + kx, y + ky);
                        int gray = (int)(0.299 * ((pixel >> 16) & 0xFF) +
                                0.587 * ((pixel >> 8) & 0xFF) +
                                0.114 * (pixel & 0xFF));
                        sum += gray;
                        count++;
                    }
                }

                int mean = sum / count;
                int threshold = mean - constant;

                int currentPixel = image.getRGB(x, y);
                int currentGray = (int)(0.299 * ((currentPixel >> 16) & 0xFF) +
                        0.587 * ((currentPixel >> 8) & 0xFF) +
                        0.114 * (currentPixel & 0xFF));

                int newPixel = (currentGray > threshold) ? 0xFFFFFF : 0x000000;
                result.setRGB(x, y, newPixel);
            }
        }

        return result;
    }

    private BufferedImage localMedianThreshold(BufferedImage image, int windowSize, int constant) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);

        int halfWindow = windowSize / 2;

        for (int y = halfWindow; y < height - halfWindow; y++) {
            for (int x = halfWindow; x < width - halfWindow; x++) {

                int[] values = new int[windowSize * windowSize];
                int index = 0;

                for (int ky = -halfWindow; ky <= halfWindow; ky++) {
                    for (int kx = -halfWindow; kx <= halfWindow; kx++) {
                        int pixel = image.getRGB(x + kx, y + ky);
                        int gray = (int)(0.299 * ((pixel >> 16) & 0xFF) +
                                0.587 * ((pixel >> 8) & 0xFF) +
                                0.114 * (pixel & 0xFF));
                        values[index++] = gray;
                    }
                }

                Arrays.sort(values);
                int median = values[values.length / 2];
                int threshold = median - constant;

                int currentPixel = image.getRGB(x, y);
                int currentGray = (int)(0.299 * ((currentPixel >> 16) & 0xFF) +
                        0.587 * ((currentPixel >> 8) & 0xFF) +
                        0.114 * (currentPixel & 0xFF));

                int newPixel = (currentGray > threshold) ? 0xFFFFFF : 0x000000;
                result.setRGB(x, y, newPixel);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ImageProcessingApp().setVisible(true);
        });
    }
}