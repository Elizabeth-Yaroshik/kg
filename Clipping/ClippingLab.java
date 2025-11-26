package lab4;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class ClippingLab extends JFrame {
    private DrawingPanel drawingPanel;
    private JComboBox<String> algorithmComboBox;
    private JButton loadButton, processButton;
    private JFileChooser fileChooser;

    private List<LineSegment> segments;
    private Rectangle clippingWindow;
    private List<LineSegment> clippedSegments;
    private List<Point2D> clippingPolygon;
    private boolean usePolygonClipping = false;

    public ClippingLab() {
        initializeUI();
        setupEventHandlers();
    }

    private void initializeUI() {
        setTitle("Лабораторная работа 4 - Вариант 14 (Лианга-Барски + выпуклый многоугольник)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        algorithmComboBox = new JComboBox<>(new String[]{
                "Лианга-Барски (прямоугольное окно)", "Отсечение выпуклым многоугольником"
        });

        loadButton = new JButton("Загрузить данные");
        processButton = new JButton("Выполнить отсечение");

        controlPanel.add(new JLabel("Режим отсечения:"));
        controlPanel.add(algorithmComboBox);
        controlPanel.add(loadButton);
        controlPanel.add(processButton);

        drawingPanel = new DrawingPanel();

        add(controlPanel, BorderLayout.NORTH);
        add(new JScrollPane(drawingPanel), BorderLayout.CENTER);

        setSize(1000, 700);
        setLocationRelativeTo(null);

        fileChooser = new JFileChooser();

        segments = new ArrayList<>();
        clippedSegments = new ArrayList<>();
        clippingPolygon = new ArrayList<>();
    }

    private void setupEventHandlers() {
        loadButton.addActionListener(e -> loadDataFromFile());
        processButton.addActionListener(e -> performClipping());

        algorithmComboBox.addActionListener(e -> {
            usePolygonClipping = (algorithmComboBox.getSelectedIndex() == 1);
            drawingPanel.repaint();
        });
    }

    private void loadDataFromFile() {
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                parseInputFile(reader);
                drawingPanel.repaint();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Ошибка чтения файла", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void parseInputFile(BufferedReader reader) throws IOException {
        segments.clear();
        clippingPolygon.clear();

        String line;
        int lineNumber = 0;
        int segmentCount = 0;
        boolean readingPolygon = false;
        int polygonPointCount = 0;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] tokens = line.split("\\s+");

            if (lineNumber == 0) {

                segmentCount = Integer.parseInt(tokens[0]);
            } else if (lineNumber <= segmentCount) {

                double x1 = Double.parseDouble(tokens[0]);
                double y1 = Double.parseDouble(tokens[1]);
                double x2 = Double.parseDouble(tokens[2]);
                double y2 = Double.parseDouble(tokens[3]);
                segments.add(new LineSegment(x1, y1, x2, y2));
            } else if (lineNumber == segmentCount + 1) {

                double xmin = Double.parseDouble(tokens[0]);
                double ymin = Double.parseDouble(tokens[1]);
                double xmax = Double.parseDouble(tokens[2]);
                double ymax = Double.parseDouble(tokens[3]);
                clippingWindow = new Rectangle((int)xmin, (int)ymin, (int)(xmax-xmin), (int)(ymax-ymin));
            } else if (lineNumber == segmentCount + 2) {

                polygonPointCount = Integer.parseInt(tokens[0]);
                readingPolygon = true;
            } else if (readingPolygon && clippingPolygon.size() < polygonPointCount) {

                double x = Double.parseDouble(tokens[0]);
                double y = Double.parseDouble(tokens[1]);
                clippingPolygon.add(new Point2D.Double(x, y));
            }
            lineNumber++;
        }
    }

    private void performClipping() {
        if (segments.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Загрузите данные сначала", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        clippedSegments.clear();

        if (usePolygonClipping) {
            if (clippingPolygon.size() < 3) {
                JOptionPane.showMessageDialog(this, "Недостаточно точек для выпуклого многоугольника", "Ошибка", JOptionPane.WARNING_MESSAGE);
                return;
            }
            for (LineSegment segment : segments) {
                LineSegment clipped = clipSegmentByConvexPolygon(segment, clippingPolygon);
                if (clipped != null) {
                    clippedSegments.add(clipped);
                }
            }
        } else {
            if (clippingWindow == null) {
                JOptionPane.showMessageDialog(this, "Отсутствует отсекающее окно", "Ошибка", JOptionPane.WARNING_MESSAGE);
                return;
            }
            for (LineSegment segment : segments) {
                LineSegment clipped = liangBarskyClip(segment);
                if (clipped != null) {
                    clippedSegments.add(clipped);
                }
            }
        }

        drawingPanel.repaint();
    }

    private LineSegment liangBarskyClip(LineSegment segment) {
        double x1 = segment.x1, y1 = segment.y1;
        double x2 = segment.x2, y2 = segment.y2;

        double dx = x2 - x1;
        double dy = y2 - y1;

        double p1 = -dx;
        double p2 = dx;
        double p3 = -dy;
        double p4 = dy;

        double q1 = x1 - clippingWindow.x;
        double q2 = clippingWindow.x + clippingWindow.width - x1;
        double q3 = y1 - clippingWindow.y;
        double q4 = clippingWindow.y + clippingWindow.height - y1;

        double u1 = 0.0;
        double u2 = 1.0;

        if (p1 == 0) {
            if (q1 < 0) return null;
        } else {
            double r = q1 / p1;
            if (p1 < 0) {
                u1 = Math.max(u1, r);
            } else {
                u2 = Math.min(u2, r);
            }
        }

        if (p2 == 0) {
            if (q2 < 0) return null;
        } else {
            double r = q2 / p2;
            if (p2 < 0) {
                u1 = Math.max(u1, r);
            } else {
                u2 = Math.min(u2, r);
            }
        }

        if (p3 == 0) {
            if (q3 < 0) return null;
        } else {
            double r = q3 / p3;
            if (p3 < 0) {
                u1 = Math.max(u1, r);
            } else {
                u2 = Math.min(u2, r);
            }
        }

        if (p4 == 0) {
            if (q4 < 0) return null;
        } else {
            double r = q4 / p4;
            if (p4 < 0) {
                u1 = Math.max(u1, r);
            } else {
                u2 = Math.min(u2, r);
            }
        }

        if (u1 > u2) return null;

        double nx1 = x1 + u1 * dx;
        double ny1 = y1 + u1 * dy;
        double nx2 = x1 + u2 * dx;
        double ny2 = y1 + u2 * dy;

        return new LineSegment(nx1, ny1, nx2, ny2);
    }

    private LineSegment clipSegmentByConvexPolygon(LineSegment segment, List<Point2D> polygon) {
        double x1 = segment.x1, y1 = segment.y1;
        double x2 = segment.x2, y2 = segment.y2;

        double tEnter = 0.0;
        double tLeave = 1.0;

        double dx = x2 - x1;
        double dy = y2 - y1;

        for (int i = 0; i < polygon.size(); i++) {
            Point2D p1 = polygon.get(i);
            Point2D p2 = polygon.get((i + 1) % polygon.size());

            double nx = -(p2.getY() - p1.getY());
            double ny = p2.getX() - p1.getX();

            Point2D center = getPolygonCenter(polygon);
            double testX = center.getX() - p1.getX();
            double testY = center.getY() - p1.getY();

            if (testX * nx + testY * ny < 0) {
                nx = -nx;
                ny = -ny;
            }

            double f1 = (x1 - p1.getX()) * nx + (y1 - p1.getY()) * ny;
            double f2 = dx * nx + dy * ny;

            if (Math.abs(f2) < 1e-10) {

                if (f1 < 0) return null;
                continue;
            }

            double t = -f1 / f2;

            if (f2 > 0) {
                tEnter = Math.max(tEnter, t);
            } else {

                tLeave = Math.min(tLeave, t);
            }

            if (tEnter > tLeave) return null;
        }

        if (tEnter <= tLeave) {
            double nx1 = x1 + tEnter * dx;
            double ny1 = y1 + tEnter * dy;
            double nx2 = x1 + tLeave * dx;
            double ny2 = y1 + tLeave * dy;
            return new LineSegment(nx1, ny1, nx2, ny2);
        }

        return null;
    }

    private Point2D getPolygonCenter(List<Point2D> polygon) {
        double sumX = 0, sumY = 0;
        for (Point2D p : polygon) {
            sumX += p.getX();
            sumY += p.getY();
        }
        return new Point2D.Double(sumX / polygon.size(), sumY / polygon.size());
    }
    private boolean isConvexPolygon(List<Point2D> polygon) {
        if (polygon.size() < 3) return false;

        int sign = 0;
        for (int i = 0; i < polygon.size(); i++) {
            Point2D a = polygon.get(i);
            Point2D b = polygon.get((i + 1) % polygon.size());
            Point2D c = polygon.get((i + 2) % polygon.size());

            double cross = (b.getX() - a.getX()) * (c.getY() - b.getY()) -
                    (b.getY() - a.getY()) * (c.getX() - b.getX());

            if (Math.abs(cross) > 1e-10) {
                int currentSign = (cross > 0) ? 1 : -1;
                if (sign == 0) {
                    sign = currentSign;
                } else if (sign != currentSign) {
                    return false;
                }
            }
        }
        return true;
    }

    private static class LineSegment {
        double x1, y1, x2, y2;

        LineSegment(double x1, double y1, double x2, double y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }

    private class DrawingPanel extends JPanel {
        private static final int MARGIN = 50;
        private double scale = 2.0;

        public DrawingPanel() {
            setPreferredSize(new Dimension(1000, 700));
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            drawCoordinateSystem(g2d);

            if (usePolygonClipping) {
                drawClippingPolygon(g2d);
            } else {
                drawClippingWindow(g2d);
            }

            drawOriginalSegments(g2d);
            drawClippedSegments(g2d);
            drawLegend(g2d);
        }

        private void drawCoordinateSystem(Graphics2D g2d) {
            int width = getWidth();
            int height = getHeight();

            g2d.setColor(Color.LIGHT_GRAY);

            g2d.drawLine(MARGIN, height - MARGIN, width - MARGIN, height - MARGIN);
            g2d.drawLine(MARGIN, MARGIN, MARGIN, height - MARGIN);

            g2d.setColor(new Color(240, 240, 240));
            for (int x = MARGIN; x <= width - MARGIN; x += 20) {
                g2d.drawLine(x, MARGIN, x, height - MARGIN);
            }
            for (int y = MARGIN; y <= height - MARGIN; y += 20) {
                g2d.drawLine(MARGIN, y, width - MARGIN, y);
            }

            g2d.setColor(Color.BLACK);
            g2d.drawString("X", width - MARGIN + 5, height - MARGIN);
            g2d.drawString("Y", MARGIN, MARGIN - 5);
        }

        private void drawClippingWindow(Graphics2D g2d) {
            if (clippingWindow != null) {
                g2d.setColor(new Color(0, 150, 0, 50));
                g2d.fillRect(
                        MARGIN + (int)(clippingWindow.x * scale),
                        MARGIN + (int)(clippingWindow.y * scale),
                        (int)(clippingWindow.width * scale),
                        (int)(clippingWindow.height * scale)
                );
                g2d.setColor(Color.GREEN);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRect(
                        MARGIN + (int)(clippingWindow.x * scale),
                        MARGIN + (int)(clippingWindow.y * scale),
                        (int)(clippingWindow.width * scale),
                        (int)(clippingWindow.height * scale)
                );
                g2d.setStroke(new BasicStroke(1));
            }
        }

        private void drawClippingPolygon(Graphics2D g2d) {
            if (clippingPolygon.size() >= 3) {
                int[] xPoints = new int[clippingPolygon.size()];
                int[] yPoints = new int[clippingPolygon.size()];

                for (int i = 0; i < clippingPolygon.size(); i++) {
                    xPoints[i] = MARGIN + (int)(clippingPolygon.get(i).getX() * scale);
                    yPoints[i] = MARGIN + (int)(clippingPolygon.get(i).getY() * scale);
                }

                g2d.setColor(new Color(0, 150, 0, 80));
                g2d.fillPolygon(xPoints, yPoints, clippingPolygon.size());
                g2d.setColor(Color.GREEN);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawPolygon(xPoints, yPoints, clippingPolygon.size());

                g2d.setColor(Color.RED);
                for (int i = 0; i < clippingPolygon.size(); i++) {
                    int x = MARGIN + (int)(clippingPolygon.get(i).getX() * scale);
                    int y = MARGIN + (int)(clippingPolygon.get(i).getY() * scale);
                    g2d.fillOval(x - 3, y - 3, 6, 6);
                }
                g2d.setStroke(new BasicStroke(1));
            }
        }

        private void drawOriginalSegments(Graphics2D g2d) {
            g2d.setColor(Color.BLUE);
            for (LineSegment segment : segments) {
                drawSegment(g2d, segment, false);
            }
        }

        private void drawClippedSegments(Graphics2D g2d) {
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(3));

            for (LineSegment segment : clippedSegments) {
                drawSegment(g2d, segment, true);
            }

            g2d.setStroke(new BasicStroke(1));
        }

        private void drawSegment(Graphics2D g2d, LineSegment segment, boolean isClipped) {
            int x1 = MARGIN + (int)(segment.x1 * scale);
            int y1 = MARGIN + (int)(segment.y1 * scale);
            int x2 = MARGIN + (int)(segment.x2 * scale);
            int y2 = MARGIN + (int)(segment.y2 * scale);

            if (isClipped) {
                g2d.setStroke(new BasicStroke(3));
            } else {
                g2d.setStroke(new BasicStroke(1));
            }
            g2d.drawLine(x1, y1, x2, y2);
            g2d.setStroke(new BasicStroke(1));
        }

        private void drawLegend(Graphics2D g2d) {
            int legendX = getWidth() - 200;
            int legendY = 20;

            g2d.setColor(Color.BLUE);
            g2d.drawString("Исходные отрезки", legendX, legendY + 20);

            if (usePolygonClipping) {
                g2d.setColor(Color.GREEN);
                g2d.drawString("Выпуклый многоугольник", legendX, legendY + 40);
            } else {
                g2d.setColor(Color.GREEN);
                g2d.drawString("Отсекающее окно", legendX, legendY + 40);
            }

            g2d.setColor(Color.RED);
            g2d.drawString("Отсеченные части", legendX, legendY + 60);

            String algorithm = usePolygonClipping ?
                    "Алгоритм: Цируса-Бека" : "Алгоритм: Лианга-Барски";
            g2d.setColor(Color.BLACK);
            g2d.drawString(algorithm, legendX, legendY + 90);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ClippingLab().setVisible(true);
        });
    }
}