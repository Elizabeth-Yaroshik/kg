import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class RasterAlgorithmsLab {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Демонстрация растровых алгоритмов с координатами");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);

            RasterPanel panel = new RasterPanel();

            JTextArea coordinatesArea = new JTextArea(15, 30);
            coordinatesArea.setEditable(false);
            JScrollPane coordinatesScroll = new JScrollPane(coordinatesArea);
            coordinatesScroll.setPreferredSize(new Dimension(350, 0));

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel, coordinatesScroll);
            splitPane.setDividerLocation(800);

            JPanel topControlPanel = new JPanel();
            setupTopControls(topControlPanel, panel, coordinatesArea);

            frame.setLayout(new BorderLayout());
            frame.add(topControlPanel, BorderLayout.NORTH);
            frame.add(splitPane, BorderLayout.CENTER);

            frame.setVisible(true);
        });
    }

    private static void setupTopControls(JPanel controlPanel, RasterPanel panel, JTextArea coordinatesArea) {
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        String[] algs = {"ЦДА", "Брезенхем (линия)", "Брезенхем (окружность)", "Пошаговый алгоритм",
                "Кастла-Питвея", "Сглаживание Ву"};
        JComboBox<String> algoBox = new JComboBox<>(algs);
        controlPanel.add(new JLabel("Алгоритм:"));
        controlPanel.add(algoBox);

        controlPanel.add(new JLabel("Масштаб:"));
        JSlider scaleSlider = new JSlider(5, 60, 20);
        scaleSlider.setMajorTickSpacing(5);
        scaleSlider.setPaintTicks(true);
        scaleSlider.setPreferredSize(new Dimension(150, 40));
        controlPanel.add(scaleSlider);

        JButton drawBtn = new JButton("Нарисовать");
        JButton clearBtn = new JButton("Очистить");
        controlPanel.add(drawBtn);
        controlPanel.add(clearBtn);

        JTextField x1f = new JTextField("2", 3);
        JTextField y1f = new JTextField("3", 3);
        JTextField x2f = new JTextField("10", 3);
        JTextField y2f = new JTextField("7", 3);
        JTextField radf = new JTextField("7", 3);
        controlPanel.add(new JLabel("x1:")); controlPanel.add(x1f);
        controlPanel.add(new JLabel("y1:")); controlPanel.add(y1f);
        controlPanel.add(new JLabel("x2:")); controlPanel.add(x2f);
        controlPanel.add(new JLabel("y2:")); controlPanel.add(y2f);
        controlPanel.add(new JLabel("радиус:")); controlPanel.add(radf);

        JLabel timeLabel = new JLabel("Время: -");
        controlPanel.add(timeLabel);

        scaleSlider.addChangeListener(e -> {
            panel.setScale(scaleSlider.getValue());
            panel.repaint();
        });

        drawBtn.addActionListener(e -> {
            String alg = (String) algoBox.getSelectedItem();
            int x1 = Integer.parseInt(x1f.getText());
            int y1 = Integer.parseInt(y1f.getText());
            int x2 = Integer.parseInt(x2f.getText());
            int y2 = Integer.parseInt(y2f.getText());
            int r  = Integer.parseInt(radf.getText());

            long start = System.nanoTime();
            List<PixelPoint> pixels = new ArrayList<>();
            switch (alg) {
                case "ЦДА":
                    pixels = Algorithms.ddaLine(x1,y1,x2,y2);
                    break;
                case "Брезенхем (линия)":
                    pixels = Algorithms.bresenhamLine(x1,y1,x2,y2);
                    break;
                case "Брезенхем (окружность)":
                    pixels = Algorithms.bresenhamCircle(x1,y1,r);
                    break;
                case "Пошаговый алгоритм":
                    pixels = Algorithms.stepByStepLine(x1,y1,x2,y2);
                    break;
                case "Кастла-Питвея":
                    pixels = Algorithms.castlePitwayLine(x1,y1,x2,y2);
                    break;
                case "Сглаживание Ву":
                    pixels = Algorithms.wuLine(x1,y1,x2,y2);
                    break;
            }
            long end = System.nanoTime();
            panel.setPixels(pixels);

            updateCoordinatesArea(coordinatesArea, alg, pixels, x1, y1, x2, y2, r, start, end);

            timeLabel.setText(String.format("Время: %d нс (%.3f мс)", (end-start), (end-start)/1e6));
            panel.repaint();
        });

        clearBtn.addActionListener(e -> {
            panel.clear();
            coordinatesArea.setText("");
            timeLabel.setText("Время: -");
        });
    }

    private static void updateCoordinatesArea(JTextArea area, String algorithm,
                                              List<PixelPoint> pixels, int x1, int y1,
                                              int x2, int y2, int radius,
                                              long startTime, long endTime) {
        StringBuilder sb = new StringBuilder();
        long duration = endTime - startTime;

        sb.append("Алгоритм: ").append(algorithm).append("\n");
        sb.append("Время выполнения: ").append(duration).append(" наносекунд\n");
        sb.append("Количество точек: ").append(pixels.size()).append("\n\n");

        if (!algorithm.equals("Брезенхем (окружность)")) {
            sb.append("Координаты отрезка:\n");
            sb.append("(").append(x1).append(", ").append(y1).append(") -> (")
                    .append(x2).append(", ").append(y2).append(")\n\n");
        } else {
            sb.append("Окружность с центром (").append(x1).append(", ").append(y1)
                    .append(") и радиусом ").append(radius).append("\n\n");
        }

        sb.append("Построенные точки:\n");
        int count = Math.min(50, pixels.size());
        for (int i = 0; i < count; i++) {
            PixelPoint p = pixels.get(i);
            if (algorithm.equals("Сглаживание Ву")) {
                sb.append(i + 1).append(": (").append(p.x).append(", ").append(p.y)
                        .append(") интенсивность: ").append(String.format("%.2f", p.intensity)).append("\n");
            } else {
                sb.append(i + 1).append(": (").append(p.x).append(", ").append(p.y).append(")\n");
            }
        }
        if (pixels.size() > 50) {
            sb.append("... и еще ").append(pixels.size() - 50).append(" точек");
        }

        area.setText(sb.toString());
    }
}

class PixelPoint {
    public int x, y;
    public float intensity;

    public PixelPoint(int x, int y) {
        this.x = x;
        this.y = y;
        this.intensity = 1.0f;
    }

    public PixelPoint(int x, int y, float intensity) {
        this.x = x;
        this.y = y;
        this.intensity = intensity;
    }
}

class RasterPanel extends JPanel {
    private int scale = 20;
    private List<PixelPoint> pixels = new ArrayList<>();
    private boolean showGrid = true;

    public RasterPanel() {
        setBackground(Color.white);
        setPreferredSize(new Dimension(800, 600));
    }

    public void setScale(int s) { this.scale = Math.max(1, s); }
    public void setPixels(List<PixelPoint> p) { this.pixels = p; }
    public void clear() { this.pixels = new ArrayList<>(); repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGridAndAxes(g);
        drawPixels(g);
    }

    private void drawGridAndAxes(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        int cx = w/2;
        int cy = h/2;

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1f));

        g2.setColor(new Color(220,220,220));
        for (int x = cx % scale; x < w; x += scale) g2.drawLine(x, 0, x, h);
        for (int y = cy % scale; y < h; y += scale) g2.drawLine(0, y, w, y);

        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(0, cy, w, cy);
        g2.drawLine(cx, 0, cx, h);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
        for (int i = -w/(2*scale)-1; i <= w/(2*scale)+1; i++) {
            int sx = cx + i*scale;
            g2.drawLine(sx, cy-4, sx, cy+4);
            if (i != 0) g2.drawString(Integer.toString(i), sx-6, cy+15);
        }
        for (int j = -h/(2*scale)-1; j <= h/(2*scale)+1; j++) {
            int sy = cy - j*scale;
            g2.drawLine(cx-4, sy, cx+4, sy);
            if (j != 0) g2.drawString(Integer.toString(j), cx+6, sy+4);
        }

        g2.drawString("(0,0)", cx+6, cy+12);
        g2.drawString("Масштаб: " + scale + " px/ед.", 10, 15);
    }

    private void drawPixels(Graphics g) {
        if (pixels == null) return;
        int w = getWidth();
        int h = getHeight();
        int cx = w/2, cy = h/2;

        int pointSize = Math.max(2, scale / 3);

        for (PixelPoint p : pixels) {
            int sx = cx + p.x * scale;
            int sy = cy - p.y * scale;

            if (p.intensity < 1.0f) {
                int red = 255;
                int greenBlue = (int)(255 * (1 - p.intensity));
                g.setColor(new Color(red, greenBlue, greenBlue));
            } else {
                g.setColor(Color.red);
            }

            g.fillRect(sx - pointSize/2, sy - pointSize/2, pointSize, pointSize);

            g.setColor(Color.black);
            g.drawRect(sx - pointSize/2, sy - pointSize/2, pointSize, pointSize);
        }
    }
}

class Algorithms {
    public static List<PixelPoint> ddaLine(int x1, int y1, int x2, int y2) {
        List<PixelPoint> pts = new ArrayList<>();
        int dx = x2 - x1;
        int dy = y2 - y1;
        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        if (steps == 0) {
            pts.add(new PixelPoint(x1, y1));
            return pts;
        }

        double x = x1, y = y1;
        double xInc = dx / (double) steps;
        double yInc = dy / (double) steps;

        for (int i = 0; i <= steps; i++) {
            pts.add(new PixelPoint((int)Math.round(x), (int)Math.round(y)));
            x += xInc;
            y += yInc;
        }
        return pts;
    }

    public static List<PixelPoint> bresenhamLine(int x1, int y1, int x2, int y2) {
        List<PixelPoint> pts = new ArrayList<>();
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;

        int currentX = x1;
        int currentY = y1;

        while (true) {
            pts.add(new PixelPoint(currentX, currentY));
            if (currentX == x2 && currentY == y2) break;

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
        return pts;
    }

    public static List<PixelPoint> bresenhamCircle(int xc, int yc, int r) {
        List<PixelPoint> pts = new ArrayList<>();
        int x = 0;
        int y = r;
        int d = 3 - 2 * r;

        add8(pts, xc, yc, x, y);

        while (x <= y) {
            x++;
            if (d <= 0) {
                d = d + 4 * x + 6;
            } else {
                d = d + 4 * (x - y) + 10;
                y--;
            }
            add8(pts, xc, yc, x, y);
        }
        return pts;
    }

    public static List<PixelPoint> stepByStepLine(int x1, int y1, int x2, int y2) {
        List<PixelPoint> points = new ArrayList<>();

        int dx = x2 - x1;
        int dy = y2 - y1;

        if (Math.abs(dx) >= Math.abs(dy)) {
            if (x1 > x2) {
                int temp = x1; x1 = x2; x2 = temp;
                temp = y1; y1 = y2; y2 = temp;
                dx = -dx; dy = -dy;
            }

            float k = (float) dy / dx;
            float y = y1;

            for (int x = x1; x <= x2; x++) {
                points.add(new PixelPoint(x, Math.round(y)));
                y += k;
            }
        } else {
            if (y1 > y2) {
                int temp = x1; x1 = x2; x2 = temp;
                temp = y1; y1 = y2; y2 = temp;
                dx = -dx; dy = -dy;
            }

            float k = (float) dx / dy;
            float x = x1;

            for (int y = y1; y <= y2; y++) {
                points.add(new PixelPoint(Math.round(x), y));
                x += k;
            }
        }

        return points;
    }

    public static List<PixelPoint> castlePitwayLine(int x1, int y1, int x2, int y2) {
        List<PixelPoint> pts = new ArrayList<>();
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;

        int currentX = x1;
        int currentY = y1;

        if (dx >= dy) {
            int err = 2 * dy - dx;

            for (int i = 0; i <= dx; i++) {
                pts.add(new PixelPoint(currentX, currentY));
                if (err >= 0) {
                    currentY += sy;
                    err -= 2 * dx;
                }
                err += 2 * dy;
                currentX += sx;
            }
        } else {
            int err = 2 * dx - dy;

            for (int i = 0; i <= dy; i++) {
                pts.add(new PixelPoint(currentX, currentY));
                if (err >= 0) {
                    currentX += sx;
                    err -= 2 * dy;
                }
                err += 2 * dx;
                currentY += sy;
            }
        }

        return pts;
    }

    public static List<PixelPoint> wuLine(int x1, int y1, int x2, int y2) {
        List<PixelPoint> pts = new ArrayList<>();

        boolean steep = Math.abs(y2 - y1) > Math.abs(x2 - x1);

        if (steep) {
            int temp = x1; x1 = y1; y1 = temp;
            temp = x2; x2 = y2; y2 = temp;
        }

        if (x1 > x2) {
            int temp = x1; x1 = x2; x2 = temp;
            temp = y1; y1 = y2; y2 = temp;
        }

        float dx = x2 - x1;
        float dy = y2 - y1;
        float gradient = dx == 0 ? 1 : dy / dx;

        float y = y1;
        for (int x = x1; x <= x2; x++) {
            if (steep) {
                pts.add(new PixelPoint((int)Math.floor(y), x, 1 - fractionalPart(y)));
                pts.add(new PixelPoint((int)Math.floor(y) + 1, x, fractionalPart(y)));
            } else {
                pts.add(new PixelPoint(x, (int)Math.floor(y), 1 - fractionalPart(y)));
                pts.add(new PixelPoint(x, (int)Math.floor(y) + 1, fractionalPart(y)));
            }
            y += gradient;
        }

        return pts;
    }

    private static float fractionalPart(float x) {
        return x - (float)Math.floor(x);
    }

    private static void add8(List<PixelPoint> pts, int xc, int yc, int x, int y) {
        pts.add(new PixelPoint(xc + x, yc + y));
        pts.add(new PixelPoint(xc - x, yc + y));
        pts.add(new PixelPoint(xc + x, yc - y));
        pts.add(new PixelPoint(xc - x, yc - y));
        pts.add(new PixelPoint(xc + y, yc + x));
        pts.add(new PixelPoint(xc - y, yc + x));
        pts.add(new PixelPoint(xc + y, yc - x));
        pts.add(new PixelPoint(xc - y, yc - x));
    }
}
