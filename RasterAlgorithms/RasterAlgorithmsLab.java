package lab3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class RasterAlgorithmsLab extends JFrame {
    private AlgorithmPanel algorithmPanel;
    private JComboBox<String> algorithmComboBox;
    private JTextField x1Field, y1Field, x2Field, y2Field;
    private JTextField radiusField;
    private JButton drawButton;
    private JButton clearButton;
    private JTextArea calculationArea;

    public RasterAlgorithmsLab() {
        setTitle("Лабораторная работа 3 - Растровые алгоритмы");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        algorithmPanel = new AlgorithmPanel();

        String[] algorithms = {
                "Пошаговый алгоритм",
                "Алгоритм ЦДА",
                "Алгоритм Брезенхема (линия)",
                "Алгоритм Брезенхема (окружность)"
        };
        algorithmComboBox = new JComboBox<>(algorithms);

        x1Field = new JTextField("10", 5);
        y1Field = new JTextField("10", 5);
        x2Field = new JTextField("20", 5);
        y2Field = new JTextField("15", 5);
        radiusField = new JTextField("5", 5);

        drawButton = new JButton("Нарисовать");
        clearButton = new JButton("Очистить");

        calculationArea = new JTextArea(10, 30);
        calculationArea.setEditable(false);

        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawShape();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                algorithmPanel.clear();
                calculationArea.setText("");
            }
        });
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 1));

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Алгоритм:"));
        inputPanel.add(algorithmComboBox);
        inputPanel.add(new JLabel("x1:"));
        inputPanel.add(x1Field);
        inputPanel.add(new JLabel("y1:"));
        inputPanel.add(y1Field);
        inputPanel.add(new JLabel("x2:"));
        inputPanel.add(x2Field);
        inputPanel.add(new JLabel("y2:"));
        inputPanel.add(y2Field);
        inputPanel.add(new JLabel("Радиус:"));
        inputPanel.add(radiusField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(drawButton);
        buttonPanel.add(clearButton);

        controlPanel.add(inputPanel);
        controlPanel.add(buttonPanel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(algorithmPanel, BorderLayout.CENTER);

        JScrollPane calculationScroll = new JScrollPane(calculationArea);
        calculationScroll.setPreferredSize(new Dimension(400, 0));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainPanel, calculationScroll);
        splitPane.setDividerLocation(800);

        add(splitPane, BorderLayout.CENTER);
    }

    private void drawShape() {
        try {
            int x1 = Integer.parseInt(x1Field.getText());
            int y1 = Integer.parseInt(y1Field.getText());
            int x2 = Integer.parseInt(x2Field.getText());
            int y2 = Integer.parseInt(y2Field.getText());
            int radius = Integer.parseInt(radiusField.getText());

            String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
            algorithmPanel.drawShape(selectedAlgorithm, x1, y1, x2, y2, radius, calculationArea);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Пожалуйста, введите корректные числовые значения", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RasterAlgorithmsLab().setVisible(true);
            }
        });
    }
}

class AlgorithmPanel extends JPanel {
    private BufferedImage buffer;
    private Graphics2D g2d;
    private List<Point> drawnPoints;
    private static final int GRID_SIZE = 10;
    private static final int ORIGIN_X = 400;
    private static final int ORIGIN_Y = 300;

    public AlgorithmPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 600));
        drawnPoints = new ArrayList<>();

        buffer = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        g2d = buffer.createGraphics();
        clear();
    }

    public void clear() {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        drawCoordinateSystem();
        drawnPoints.clear();
        repaint();
    }

    private void drawCoordinateSystem() {
        g2d.setColor(Color.LIGHT_GRAY);

        for (int x = ORIGIN_X % GRID_SIZE; x < getWidth(); x += GRID_SIZE) {
            g2d.drawLine(x, 0, x, getHeight());
        }
        for (int y = ORIGIN_Y % GRID_SIZE; y < getHeight(); y += GRID_SIZE) {
            g2d.drawLine(0, y, getWidth(), y);
        }

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(0, ORIGIN_Y, getWidth(), ORIGIN_Y);
        g2d.drawLine(ORIGIN_X, 0, ORIGIN_X, getHeight());

        g2d.drawString("X", getWidth() - 20, ORIGIN_Y - 10);
        g2d.drawString("Y", ORIGIN_X + 10, 20);

        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        for (int x = ORIGIN_X + GRID_SIZE; x < getWidth(); x += GRID_SIZE) {
            if ((x - ORIGIN_X) % (GRID_SIZE * 5) == 0) {
                g2d.drawString(String.valueOf((x - ORIGIN_X) / GRID_SIZE), x - 5, ORIGIN_Y + 15);
            }
        }
        for (int y = ORIGIN_Y + GRID_SIZE; y < getHeight(); y += GRID_SIZE) {
            if ((y - ORIGIN_Y) % (GRID_SIZE * 5) == 0) {
                g2d.drawString(String.valueOf(-(y - ORIGIN_Y) / GRID_SIZE), ORIGIN_X + 5, y + 5);
            }
        }
    }

    public void drawShape(String algorithm, int x1, int y1, int x2, int y2, int radius, JTextArea calculationArea) {
        clear();

        long startTime = System.nanoTime();

        switch (algorithm) {
            case "Пошаговый алгоритм":
                stepByStepLine(x1, y1, x2, y2);
                break;
            case "Алгоритм ЦДА":
                ddaLine(x1, y1, x2, y2);
                break;
            case "Алгоритм Брезенхема (линия)":
                bresenhamLine(x1, y1, x2, y2);
                break;
            case "Алгоритм Брезенхема (окружность)":
                bresenhamCircle(x1, y1, radius);
                break;
        }

        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        StringBuilder calcText = new StringBuilder();
        calcText.append("Алгоритм: ").append(algorithm).append("\n");
        calcText.append("Время выполнения: ").append(duration).append(" наносекунд\n");
        calcText.append("Количество точек: ").append(drawnPoints.size()).append("\n\n");

        if (!algorithm.equals("Алгоритм Брезенхема (окружность)")) {
            calcText.append("Координаты отрезка:\n");
            calcText.append("(").append(x1).append(", ").append(y1).append(") -> (")
                    .append(x2).append(", ").append(y2).append(")\n\n");
        } else {
            calcText.append("Окружность с центром (").append(x1).append(", ").append(y1)
                    .append(") и радиусом ").append(radius).append("\n\n");
        }

        calcText.append("Построенные точки (первые 20):\n");
        int count = Math.min(20, drawnPoints.size());
        for (int i = 0; i < count; i++) {
            Point p = drawnPoints.get(i);
            calcText.append("(").append(p.x).append(", ").append(p.y).append(")\n");
        }
        if (drawnPoints.size() > 20) {
            calcText.append("... и еще ").append(drawnPoints.size() - 20).append(" точек");
        }

        calculationArea.setText(calcText.toString());
        repaint();
    }

    private void stepByStepLine(int x1, int y1, int x2, int y2) {
        int screenX1 = ORIGIN_X + x1 * GRID_SIZE;
        int screenY1 = ORIGIN_Y - y1 * GRID_SIZE;
        int screenX2 = ORIGIN_X + x2 * GRID_SIZE;
        int screenY2 = ORIGIN_Y - y2 * GRID_SIZE;

        if (screenX1 == screenX2) {
            int startY = Math.min(screenY1, screenY2);
            int endY = Math.max(screenY1, screenY2);
            for (int y = startY; y <= endY; y++) {
                drawPoint(screenX1, y);
            }
            return;
        }

        double k = (double)(screenY2 - screenY1) / (screenX2 - screenX1);
        double b = screenY1 - k * screenX1;

        if (Math.abs(k) <= 1) {
            int startX = Math.min(screenX1, screenX2);
            int endX = Math.max(screenX1, screenX2);
            for (int x = startX; x <= endX; x++) {
                int y = (int) Math.round(k * x + b);
                drawPoint(x, y);
            }
        } else {
            int startY = Math.min(screenY1, screenY2);
            int endY = Math.max(screenY1, screenY2);
            for (int y = startY; y <= endY; y++) {
                int x = (int) Math.round((y - b) / k);
                drawPoint(x, y);
            }
        }
    }

    private void ddaLine(int x1, int y1, int x2, int y2) {
        int screenX1 = ORIGIN_X + x1 * GRID_SIZE;
        int screenY1 = ORIGIN_Y - y1 * GRID_SIZE;
        int screenX2 = ORIGIN_X + x2 * GRID_SIZE;
        int screenY2 = ORIGIN_Y - y2 * GRID_SIZE;

        int dx = screenX2 - screenX1;
        int dy = screenY2 - screenY1;
        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        if (steps == 0) {
            drawPoint(screenX1, screenY1);
            return;
        }

        float xIncrement = (float) dx / steps;
        float yIncrement = (float) dy / steps;

        float x = screenX1;
        float y = screenY1;

        for (int i = 0; i <= steps; i++) {
            drawPoint(Math.round(x), Math.round(y));
            x += xIncrement;
            y += yIncrement;
        }
    }

    private void bresenhamLine(int x1, int y1, int x2, int y2) {
        int screenX1 = ORIGIN_X + x1 * GRID_SIZE;
        int screenY1 = ORIGIN_Y - y1 * GRID_SIZE;
        int screenX2 = ORIGIN_X + x2 * GRID_SIZE;
        int screenY2 = ORIGIN_Y - y2 * GRID_SIZE;

        int dx = Math.abs(screenX2 - screenX1);
        int dy = Math.abs(screenY2 - screenY1);

        int sx = screenX1 < screenX2 ? 1 : -1;
        int sy = screenY1 < screenY2 ? 1 : -1;

        int err = dx - dy;
        int currentX = screenX1;
        int currentY = screenY1;

        while (true) {
            drawPoint(currentX, currentY);

            if (currentX == screenX2 && currentY == screenY2) break;

            int err2 = 2 * err;

            if (err2 > -dy) {
                err -= dy;
                currentX += sx;
            }

            if (err2 < dx) {
                err += dx;
                currentY += sy;
            }
        }
    }

    private void bresenhamCircle(int xc, int yc, int r) {
        int screenXc = ORIGIN_X + xc * GRID_SIZE;
        int screenYc = ORIGIN_Y - yc * GRID_SIZE;
        int screenR = r * GRID_SIZE;

        int x = 0;
        int y = screenR;
        int d = 3 - 2 * screenR;

        drawCirclePoints(screenXc, screenYc, x, y);

        while (y >= x) {
            x++;

            if (d > 0) {
                y--;
                d = d + 4 * (x - y) + 10;
            } else {
                d = d + 4 * x + 6;
            }

            drawCirclePoints(screenXc, screenYc, x, y);
        }
    }

    private void drawCirclePoints(int xc, int yc, int x, int y) {
        drawPoint(xc + x, yc + y);
        drawPoint(xc - x, yc + y);
        drawPoint(xc + x, yc - y);
        drawPoint(xc - x, yc - y);
        drawPoint(xc + y, yc + x);
        drawPoint(xc - y, yc + x);
        drawPoint(xc + y, yc - x);
        drawPoint(xc - y, yc - x);
    }

    private void drawPoint(int x, int y) {
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
            g2d.setColor(Color.RED);
            g2d.fillRect(x - 1, y - 1, 3, 3);
            drawnPoints.add(new Point(x, y));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(buffer, 0, 0, this);
    }
}